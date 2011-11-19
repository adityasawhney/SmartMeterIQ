package qilin.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * A database-backed {@link Map}
 * @author talm
 *
 * @param <K> key type.
 * @param <V> value type.
 */

public class JDBCMap<K,V> extends AbstractMap<K, V> {
	Connection dbCon;
	String table;
	String keyCol, valCol;
	
	ByteEncoder<K> keyEncoder;
	ByteEncoder<V> valEncoder;

	PreparedStatement getSize;
	PreparedStatement getAll;
	PreparedStatement getVal;
	PreparedStatement updateVal;
	PreparedStatement insertVal;
	PreparedStatement removeVal;
	PreparedStatement clearAll;
	
	JDBCSet<Map.Entry<K, V>> entrySet;
	
	public JDBCMap(Connection dbCon, final String table, final String keyCol, final String valCol,
					final ByteEncoder<K> keyEncoder, final ByteEncoder<V> valEncoder, boolean dropTable) throws SQLException {
		this.dbCon = dbCon;
		this.keyCol = keyCol;
		this.valCol = valCol;
		this.keyEncoder = keyEncoder;
		this.valEncoder = valEncoder;
		
		Statement st = dbCon.createStatement();
		if (dropTable)
			st.execute("DROP TABLE IF EXISTS " + table);
		st.execute("CREATE TABLE IF NOT EXISTS "+table+" ("+keyCol+" BLOB PRIMARY KEY, "+valCol+" BLOB)");
		st.close();
		
		getSize = dbCon.prepareStatement("SELECT COUNT(*) FROM " + table);
		getAll = dbCon.prepareStatement("SELECT * FROM " + table);
		getVal = dbCon.prepareStatement("SELECT * FROM " + table
					+ " WHERE "+keyCol+" = ?");
		updateVal = dbCon.prepareStatement("UPDATE "+table+" SET "+valCol+" = ?"
				+ " WHERE "+keyCol+" = ?");
		insertVal = dbCon.prepareStatement("INSERT INTO "+table+" ("+valCol+","+keyCol+") VALUES (?,?)");
		removeVal = dbCon.prepareStatement("DELETE FROM "+table+" WHERE "+keyCol+" = ?");
		clearAll = dbCon.prepareStatement("DELETE FROM "+table);

		JDBCSet.JDBCInterface<Entry<K,V>> inter = new JDBCSet.JDBCInterface<Entry<K,V>>() {
			@Override
			public java.util.Map.Entry<K, V> decode(ResultSet rs)
					throws SQLException {
				
				K key = keyEncoder.decode(rs.getBytes(keyCol));
				V val = valEncoder.decode(rs.getBytes(valCol));
				return new SimpleEntry<K, V>(key, val);
			}

			@Override
			public ResultSet getAll() throws SQLException {
				return getAll.executeQuery();
			}

			@Override
			public int getSize() throws SQLException {
				return JDBCMap.this.getSize();
			}

			@SuppressWarnings(value="unchecked")
			@Override
			public ResultSet query(Object o) throws SQLException {
				K key = ((Entry<K, V>) o).getKey();
				return JDBCMap.this.query(key);
			}

			@Override
			public void clear() throws SQLException {
				throw new UnsupportedOperationException("Read-only set");
			}

			@Override
			public boolean put(java.util.Map.Entry<K, V> val)
					throws SQLException {
				throw new UnsupportedOperationException("Read-only set");
			}

			@Override
			public boolean remove(java.util.Map.Entry<K, V> val)
					throws SQLException {
				throw new UnsupportedOperationException("Read-only set");
			}
			
		};
		entrySet = new JDBCSet<Entry<K,V>>(dbCon, inter); 
 
		
	}


	int getSize() throws SQLException {
		int size;
		ResultSet rs = getSize.executeQuery();
		if (!rs.next())
			size = 0;
		else 
			size = rs.getInt(1);
		rs.close();
		return size;
	}
	
	ResultSet query(K key) throws SQLException {
		getVal.setBytes(1, keyEncoder.encode(key));
		return getVal.executeQuery();
	}
	

	@SuppressWarnings(value="unchecked")
	@Override
	public boolean containsKey(Object o) {
		try {
			ResultSet rs = query((K) o);
			boolean has = rs.next();
			rs.close();
			return has;
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in containsKey() query", e);
		}
	}
	
	@SuppressWarnings(value="unchecked")
	@Override
	public V get(Object o) {
		try {
			ResultSet rs = query((K) o);
			V val = null;
			if (rs.next()) 
				val = valEncoder.decode(rs.getBytes(valCol));
			rs.close();
			return val;
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in get()", e);
		}
	}
	
	@Override
	public V put(K key, V val) {
		try {
			ResultSet rs = query(key);
			V oldval = null;
			if (rs.next()) 
				oldval = valEncoder.decode(rs.getBytes(valCol));
			rs.close();
			
			if (oldval != null) {
				updateVal.setBytes(1, valEncoder.encode(val));
				updateVal.setBytes(2, keyEncoder.encode(key));
				updateVal.executeUpdate();
			} else {
				insertVal.setBytes(1, valEncoder.encode(val));
				insertVal.setBytes(2, keyEncoder.encode(key));
				insertVal.executeUpdate();
			}
			return oldval;
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in put()", e);
		}
	}

	@SuppressWarnings(value="unchecked")
	@Override
	public V remove(Object key) {
		try {
			ResultSet rs = query((K) key);
			V oldval = null;
			if (rs.next()) 
				oldval = valEncoder.decode(rs.getBytes(valCol));
			rs.close();
			
			if (oldval != null) {
				removeVal.setBytes(1, keyEncoder.encode((K) key));
				removeVal.executeUpdate();
			}
			return oldval;
		} catch (SQLException e) {
			throw new UnexpectedSQLException("Error in get() query", e);
		}
	}
	
	@Override
	public void clear() {
		try {
			clearAll.executeUpdate();
		} catch (SQLException e) {
			throw new  UnexpectedSQLException("Error in clear()", e);
		}
		
	}
	
	@Override
	public Set<Entry<K, V>> entrySet() {
		return entrySet;
	}

	@Override
	public int size() {
		try {
			return getSize();
		} catch (SQLException e) {
			throw new  UnexpectedSQLException("Error getting size", e);
		}
	}
}
