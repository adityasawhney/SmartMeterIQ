package qilin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.junit.Test;




public class JDBCMapTest {
	final String dbName = getClass().getName()+"-test.db";
	Logger logger = Logger.getLogger(getClass().getName());
	JDBCMap<String, String> stringMap;
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
	
	
	public JDBCMapTest() throws SQLException, ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		logger.info("Opening db connection to " + dbName);
		dbCon = DriverManager.getConnection("jdbc:sqlite:" + dbName);
		
		StringEncoder encoder = new StringEncoder();
		stringMap = new JDBCMap<String,String>(dbCon, "testTable", "key", "val", encoder, encoder, true);
		stringMap.put("first", "one");
		stringMap.put("second", "two");
	}
	

	@Test
	public void testContainsKey() {
		assertTrue(stringMap.containsKey("first"));
		assertTrue(stringMap.containsKey("second"));
		assertFalse(stringMap.containsKey("third"));
		assertFalse(stringMap.containsKey("fourth"));
	}
	@Test
	public void testGet() {
		assertEquals("one", stringMap.get("first"));
		assertEquals("two", stringMap.get("second"));
		assertNull(stringMap.get("third"));
		assertNull(stringMap.get("fourth"));
	}
	
	@Test
	public void testIterate() {
		int size = stringMap.size();
		assertEquals(2, size);
		
		int count = 0;
		for (Entry<String, String> ent : stringMap.entrySet()) {
			logger.info("Found "+ent.getKey()+"="+ent.getValue());
			++count;
		}
		
		assertEquals(size, count);
	}
	
	@Test
	public void testRemove() {
		assertNull(stringMap.remove("third"));
		assertEquals("one", stringMap.remove("first"));
		
		assertTrue(stringMap.containsKey("second"));
		assertFalse(stringMap.containsKey("first"));

		assertEquals(1, stringMap.size());
	}
	
	@Test
	public void testAdd() {
		stringMap.put("third", "three");
		assertEquals("three", stringMap.get("third"));
		assertFalse(stringMap.containsKey("fourth"));
		stringMap.put("fourth", "four");
		assertEquals("four", stringMap.get("fourth"));
	}

	@Test
	public void testClear() {
		stringMap.clear();
		assertEquals(0, stringMap.size());
		assertFalse(stringMap.containsKey("first"));
		stringMap.put("fourth", "four");
		assertTrue(stringMap.containsKey("fourth"));
	}

	
	
	
}
