package qilin.comm;

import java.io.IOException;
import java.util.Queue;

/**
 * Create a local channel that uses pre-existing {@link Message} queues
 * for incoming and outgoing messages.
 * @author talm
 *
 */
public class QueueChannel implements Channel {
	Queue<Message> incoming;
	Queue<Message> outgoing;
	

	public QueueChannel(Queue<Message> incoming, Queue<Message> outgoing) {
		this.incoming = incoming;
		this.outgoing = outgoing;
	}
	
	@Override
	public Message newMessage() {
		return new Message();
	}

	@Override
	public Message receive() throws IOException {
		return incoming.remove();
	}

	@Override
	public void send(Message msg) throws IOException {
		outgoing.add(msg);
	}

}
