package qilin.util;

import java.io.IOException;

import qilin.comm.Channel;
import qilin.comm.Message;


/**
 * Channel that counts data that is sent through it.
 * @author talm
 *
 */
public class CountingChannel implements Channel {
	Channel chan;
	long sentBytes;
	long receivedBytes;
	
	public CountingChannel(Channel chan) {
		this.chan = chan;
		sentBytes = 0;
		receivedBytes = 0;
	}
	
	@Override
	public Message newMessage() {
		return chan.newMessage();
	}

	@Override
	public Message receive() throws IOException {
		Message msg = chan.receive();
		receivedBytes += msg.length();
		return msg;
	}

	@Override
	public void send(Message msg) throws IOException {
		sentBytes += msg.length();
		chan.send(msg);
	}
	
	public long getSentBytes() {
		return sentBytes;
	}
	
	public long getReceivedBytes() {
		return receivedBytes;
	}
	
	public long getTotalBytes() {
		return sentBytes + receivedBytes;
	}
	
	public void resetCounts() {
		sentBytes = receivedBytes = 0;
	}
}
