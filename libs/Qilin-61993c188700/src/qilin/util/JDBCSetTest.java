package qilin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.junit.Test;




public class JDBCSetTest {
	final String dbName = getClass().getName()+"-test.db";
	Logger logger = Logger.getLogger(getClass().getName());
	JDBCSet<String> stringSet;
	Connection dbCon;
	
	class StringEncoder implements ByteEncoder<String> {
		@Override
		public String decode(byte[] input) {
			return new String(input);
		}

		@Override
		public String denseDecode(byte[] input) {
			return decode(input);
		}

		@Override
		public byte[] encode(String input) {
			return input.getBytes();
		}

		@Override
		public int getMinLength() {
			return 0;
		}
		
	}
	
	public JDBCSetTest() throws SQLException, ClassNotFoundException {

		Class.forName("org.sqlite.JDBC");
		logger.info("Opening db connection to " + dbName);
		dbCon = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		
		stringSet = new JDBCSet<String>(dbCon, "testTable", "val", new StringEncoder(), true);
		stringSet.add("first");
		stringSet.add("second");
	}
	
	@Test
	public void testContains() {
		assertTrue(stringSet.contains("first"));
		assertTrue(stringSet.contains("second"));
		assertFalse(stringSet.contains("third"));
		assertFalse(stringSet.contains("fourth"));
	}
	
	@Test
	public void testIterate() {
		int size = stringSet.size();
		assertEquals(2, size);
		
		int count = 0;
		for (String str : stringSet) {
			logger.info("Found string: " + str);
			++count;
		}
		
		assertEquals(size, count);
	}
	
	@Test
	public void testRemove() {
		assertFalse(stringSet.remove("third"));
		assertTrue(stringSet.remove("first"));
		
		assertTrue(stringSet.contains("second"));
		assertFalse(stringSet.contains("first"));

		assertEquals(1, stringSet.size());
	}
	
	@Test
	public void testAdd() {
		stringSet.add("third");
		assertTrue(stringSet.contains("third"));
		assertFalse(stringSet.contains("fourth"));
		stringSet.add("fourth");
		assertTrue(stringSet.contains("fourth"));
	}

	@Test
	public void testClear() {
		stringSet.clear();
		assertEquals(0, stringSet.size());
		assertFalse(stringSet.contains("first"));
		stringSet.add("fourth");
		assertTrue(stringSet.contains("fourth"));
	}
}
