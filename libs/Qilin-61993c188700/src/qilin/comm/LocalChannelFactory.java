package qilin.comm;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import qilin.util.NonInterruptable;
import qilin.util.Pair;


/**
 * Class for handling local (in-process) connections. 
 * The class creates channel pairs that are connected to each other;
 * messages sent through one channel are received on the other.
 * @author talm
 *
 */
public class LocalChannelFactory {
	public class LocalChannel implements Channel {
		LocalChannel peerChannel;
		BlockingQueue<Message> incoming;

		protected LocalChannel() {
			incoming = new LinkedBlockingQueue<Message>();
		}
		
		@Override
		public Message newMessage() {
			return new Message();
		}

		@Override
		public Message receive() throws IOException {
			return NonInterruptable.take(incoming);
		}

		@Override
		public void send(Message msg) throws IOException {
			NonInterruptable.put(peerChannel.incoming, msg);
		}
	}
	
	public Pair<Channel, Channel> getChannelPair() {
		LocalChannel a = new LocalChannel();
		LocalChannel b = new LocalChannel();
		
		a.peerChannel = b;
		b.peerChannel = a;
		
		return new Pair<Channel,Channel> (a,b);
	}
}
