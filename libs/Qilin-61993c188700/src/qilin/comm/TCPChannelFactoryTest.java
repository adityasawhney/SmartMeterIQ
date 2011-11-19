package qilin.comm;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import qilin.comm.TCPChannelFactory.TCPChannel;



public class TCPChannelFactoryTest {
	
	byte[] testbuf;
	
	public TCPChannelFactoryTest() {
		// Create a random source buffer.
		testbuf = new byte[Message.BUF_LEN * 2 + 7];
		Random rand = new Random();
		rand.nextBytes(testbuf);
	}
	
	/**
	 * Test two peers communicating between themselves.
	 */
	@Test
	public void testPoint2Point() throws IOException, InterruptedException {
		final TCPChannelFactory p1 = new TCPChannelFactory();
		final TCPChannelFactory p2 = new TCPChannelFactory();
		
		final String p1name = p1.localname;
		final String p2name = p2.localname;

		Thread p1thread = new Thread(p1, "p1 Thread");
		Thread p2thread = new Thread(p2, "p2 Thread");
		
		p1thread.start();
		p2thread.start();
		
		// Client for p2 (client for p1 runs in main thread)
		Runnable p2client = new Runnable() {
			@Override
			public void run() {
				try {
				TCPChannel chan2 = p2.getChannel(p1name);
				
				assertNotNull(chan2);
				
				Message msg2a = new Message();
				msg2a.write(testbuf, 0, testbuf.length);
				
				chan2.send(msg2a);
				
				Message msg2b = chan2.receive();
				
				assertNotNull(msg2b);
				assertEquals(testbuf.length, msg2b.length);
				
				byte[] buf2 = new byte[testbuf.length];
				msg2b.read(buf2, 0, buf2.length);
				
				assertArrayEquals(testbuf, buf2);
				} catch (IOException ioe) {
					throw new Error("Unexpected IO Exception: " + ioe.getMessage());
				}
			}
		};
		
		Thread p2clientThread = new Thread(p2client, "p2 client Thread");
		p2clientThread.start();
		// Connect p1 to p2
		
		TCPChannel chan1 = p1.getChannel();
		
		assertNotNull(chan1);
		assertEquals(p2name, chan1.getName());
		
		Message msg1a = new Message();
		msg1a.write(testbuf, 0, testbuf.length);
		
		chan1.send(msg1a);
		
		Message msg1b = chan1.receive();
		assertNotNull(msg1b);
		
		byte[] buf2 = new byte[testbuf.length];
		msg1b.read(buf2, 0, buf2.length);
		
		assertArrayEquals(testbuf, buf2);
		
		p2clientThread.join();
		p1.stop();
		p2.stop();
		p1thread.join();
		p2thread.join();
	}
}
