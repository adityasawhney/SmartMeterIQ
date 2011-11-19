package qilin.protocols;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Random;

import org.junit.Test;

import qilin.comm.Channel;
import qilin.comm.Message;
import qilin.util.GlobalTestParams;


abstract public class OT1of2Test implements GlobalTestParams {
	/**
	 * 
	 * @return an initialized chooser
	 */
	abstract protected OT1of2.Chooser getOTChooser();
	abstract protected Channel getChoosertoSenderChannel();
	
	/**
	 * 
	 * @return an initialized sender
	 */
	abstract protected OT1of2.Sender getOTSender();
	abstract protected Channel getSendertoChooserChannel();
	
	@Test
	public void testOT() throws IOException, InterruptedException {
		final OT1of2.Chooser chooser = getOTChooser();
		final OT1of2.Sender sender = getOTSender();
		final byte[][] s = {new byte[Message.BUF_LEN * 3 + 29], new byte[Message.BUF_LEN * 3 + 29]};
		
		final Random rand = new Random();
		for (int i = 0; i < s.length; ++i)
			rand.nextBytes(s[i]);
				
		Runnable senderRunner = new Runnable() {
			@Override
			public void run() {
				sender.setParameters(getSendertoChooserChannel(), rand);
				try {
					sender.init();
					for (int i = 0; i < CONFIDENCE; ++i) {
						sender.send(s[0], s[1]);
					}
				} catch (IOException ioe) {
					throw new AssertionError("IOException during send: " + ioe.getMessage());
				} catch (InterruptedException ie) {
					throw new AssertionError("InterruptedException during init: " + ie.getMessage());
				}
			}
		};

		Thread senderThread = new Thread(senderRunner);
		senderThread.start();
		
		byte[] resp;
		
		chooser.setParameters(getChoosertoSenderChannel(), rand);
		
		for (int i = 0; i < CONFIDENCE; ++i) {
			int c = rand.nextInt(2);
			resp = chooser.receive(c);
			assertArrayEquals(s[c], resp);
		}
		
		senderThread.join();
	}
}
