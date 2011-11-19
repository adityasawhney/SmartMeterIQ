package qilin.comm;

import java.io.IOException;


/**
 * The Channel represents a point-to-point communication channel for sending and 
 * receiving messages. Messages transmission is guaranteed to be reliable and
 * in order. 
 *  
 * @author talm
 * @see LocalChannelFactory
 * @see TCPChannelFactory
 *
 */
public interface Channel {
	/**
	 * Return a new message object suitable for sending over this channel
	 * @return a new message.
	 */
	public Message newMessage();

	/**
	 * Send a message. 
	 * @param msg
	 */
	public void send(Message msg) throws IOException;
	
	/**
	 * Wait until a message is received
	 * @return the message received.
	 */
	public Message receive() throws IOException;
}
