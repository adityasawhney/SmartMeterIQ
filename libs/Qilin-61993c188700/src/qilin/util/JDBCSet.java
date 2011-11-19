package qilin.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * A database-backed {@link Set}
 * @author talm
 *
 * @param <E> element type
 */
public class JDBCSet<E> extends AbstractSet<E> {
	final public static String DEFAULT_COL = "val";
	Connection dbCon;
	JDBCInterface<E> dbInterface;

	interface JDBCInterface<E> {
		public ResultSet query(Object o) throws SQLException;
		public E decode(ResultSet rs) throws SQLException;
		public int getSize() throws SQLException;
		public ResultSet getAll() throws SQLException;
		public boolean put(E val) throws SQLException;
		public boolean remove(E val) throws SQLException;
		public void clear() throws SQLException;
	}

	JDBCInterface<E> newInterface(final Connection dbCon, final String table, final String col, final ByteEncoder<E> encoder) throws SQLException {
		return new JDBCInterface<E>() {
			PreparedStatement query = dbCon.prepareStatement("SELECT "+col+" FROM "+table+" WHERE "+
					col+" = ?");
			PreparedStatement getSize = dbCon.prepareStatement("SELECT COUNT(*) FROM " + table);
			PreparedStatement getAll = dbCon.prepareStatement("SELECT "+col+" FROM " + table);		
			PreparedStatement deleteVal = dbCon.prepareStatement("DELETE FROM "+table
					+ " WHERE "+col+" = ?");
			PreparedStatement insertVal = dbCon.prepareStatement("INSERT INTO "+table+" ("+col+") VALUES (?)");
			PreparedStatement clearAll = dbCon.prepareStatement("DELETE FROM "+table);
			
			@Override
			public E decode(ResultSet rs) throws SQLException {
				return encoder.decode(rs.getBytes(col));
			}

	
			@SuppressWarnings(value="unchecked")
			@Override
			public ResultSet query(Object o) throws SQLException {
				query.setBytes(1, encoder.encode((E) o));
				return query.executeQuery();
			}


			@Override
			public ResultSet getAll() throws SQLException {
				return getAll.executeQuery();
			}

			@Override
			public int getSize() throws SQLException  {
				ResultSet rs = getSize.executeQuery();
				int size;
				if (!rs.next()) {
					size = 0;
				} else {
					size = rs.getInt(1);
				}
				rs.close();
				
				return size;
			}


			@Override
			public boolean put(E val) throws SQLException {
				ResultSet rs = query(val);
				boolean hasVal = rs.next();
				rs.close();

				if (!hasVal) {
					insertVal.setBytes(1, encoder.encode(val));
					insertVal.executeUpdate();
				}
				return hasVal;
			}


			@Override
			public boolean remove(E val) throws SQLException {
				deleteVal.setBytes(1, encoder.encode(val));
				int rows = deleteVal.executeUpdate();
				return rows > 0;
			}


			@Override
			public void clear() throws SQLException {
				clearAll.executeUpdate();
			}
			
		};
	}
	
	public JDBCSet(final Connection dbCon, final String table, final String col, final ByteEncoder<E> encoder, boolean dropTable) throws SQLException {
		this.dbCon = dbCon;

		Statement st = dbCon.createStatement();
		
		if (dropTable)
			st.execute("DROP TABLE IF EXISTS " + table);
		
		st.execute("CREATE TABLE IF NOT EXISTS "+table+" ("+col+" BLOB PRIMARY KEY)");
		st.close();
		this.dbInterface = newInterface(dbCon, table, col, encoder);
	}
	

	public JDBCSet(Connection dbCon, JDBCInterface<E> dbInterface) throws SQLException {
		this.dbCon = dbCon;
		this.dbInterface = dbInterface;
	}
	
	@Override
	public boolean contains(Object o) {
		try {
			ResultSet rs = dbInterface.query(o);
			boolean has = rs.next();
			rs.close();
			return has;
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in contains() query", e);
		}
	}

	@Override
	public boolean add(E val) {
		try {
			return dbInterface.put(val);
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in add() update", e);
		}
	}
	
	@SuppressWarnings(value="unchecked")
	@Override
	public boolean remove(Object val) {
		try {
			return dbInterface.remove((E) val);
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in remove()", e);
		}
	};
	
	@Override
	public void clear() {
		try {
			dbInterface.clear();
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in clear()", e);
		}
	}
	
	@Override
	public Iterator<E> iterator() {
		try {
			final ResultSet rs = dbInterface.getAll();
			final Boolean rsNotEmpty = rs.next();
			if (!rsNotEmpty) {
				rs.close();
			}
			
			return new Iterator<E>() {
				boolean hasNext = rsNotEmpty;
				E nextVal = hasNext ? dbInterface.decode(rs) : null;
				
				@Override
				public boolean hasNext() {
					return hasNext;
				}

				@Override
				public E next() {
					try {
						if (!hasNext) {
							throw new NoSuchElementException();
						}
						E retVal = nextVal;
						hasNext = rs.next();
						if (hasNext)
							nextVal = dbInterface.decode(rs);
						else
							rs.close();
						return retVal;
					} catch (SQLException e) {
						throw new UnexpectedSQLException("Error getting next", e);
					}
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException("Remove not supported");
				}
			};
		} catch (SQLException e) {
			throw new  UnexpectedSQLException("Error getting iterator", e);
		}
	}

	@Override
	public int size() {
		try { 
			return dbInterface.getSize();
		} catch (SQLException e) {
			throw new  UnexpectedSQLException("Error getting size", e);
		}
	}
}
