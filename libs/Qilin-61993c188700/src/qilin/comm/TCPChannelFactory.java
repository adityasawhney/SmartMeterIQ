package qilin.comm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import qilin.util.EncodingUtils;
import qilin.util.NonInterruptable;


/**
 * Class for handling TCP connections. 
 * The TCPChannelFactory
 * is responsible for creating TCP connections to a peer (given a host/port pair),
 * and for reading and writing messages from/to the peer. 
 * TCPChannel implements a multiplexing server -- after creating the factory
 * with a local port value, the run() method should be called (the recommended
 * way is by creating a new thread). The TCPChannel then listens on the port
 * for incoming connections and handles message transmission and reception. 
 * @author talm
 *
 */
public class TCPChannelFactory implements Runnable {
	boolean shouldRun;
	
	/**
	 * The local port number on which the server listens
	 */
	int localport;
	
	/**
	 * The name of the local host
	 */
	String localname;
	
	/**
	 * The local server channel.
	 */
	ServerSocketChannel serverChannel;
	
	/**
	 * The selector for the local server as well as existing connections.
	 */
	Selector selector;
	
	/**
	 * Currently connected or pending peers indexed by channel.
	 */
	Map<SocketChannel, TCPChannel> peersByChannel;
	
	/**
	 * Currently connected or pending peers indexed by name.
	 */
	Map<String, TCPChannel> peersByName;
	
	
	static class ConnectRequest {
		/**
		 * Name of peer to connect to ("ip:port")
		 */
		String peerName;
		
		/**
		 * Queue on which the newly opened channel is returned
		 */
		BlockingQueue<TCPChannel> responseQueue;
	}
	
	/**
	 * Currently pending connection requests. The elements in the queue contain
	 * the requested destination and a queue onto which the connected channel
	 * will be put when the connection is complete.
	 */
	BlockingQueue<ConnectRequest> connectRequests;
	
	/**
	 * Currently pending accept requests. Each element in the queue contains
	 * a queue. When a connection is accepted, the new channel will
	 * be placed on the next available request queue.
	 */
	BlockingQueue<TCPChannel> acceptedChannels;
	
	/**
	 * A mapping of sockets waiting for connection to complete to queues
	 * into which the connected TCPChannels should be placed. 
	 */
	Map<TCPChannel, ConnectRequest> connectedPending;
	

	/**
	 * Queue of peers who are requesting write access.
	 */
	BlockingQueue<TCPChannel> writePendingPeers;
	
	
	/**
	 * Get a new buffer.
	 * @return a newly allocated buffer.
	 */
	protected ByteBuffer getNewBuffer() {
		return ByteBuffer.allocate(Message.BUF_LEN); 
	}
	
	/**
	 * Resolve a peer name into a socket address. Accepts names of the 
	 * form host:port.
	 * @param name
	 * @return inetsocetaddress corresponding to the name, or null if name is not of the form host:port.
	 */
	InetSocketAddress resolve(String name) {
		String[] hostport = name.split(":", 2);
		if (hostport.length != 2)
			return null;
		return new InetSocketAddress(hostport[0], Integer.valueOf(hostport[1]));
	}
	
	/**
	 * Get a communication channel to a specified peer. 
	 * @param peername a label for the peer. 
	 * @return A channel through which messages can be sent to and 
	 * 		received from the peer.
	 */
	public TCPChannel getChannel(String peername) throws IOException {
		// Create a "connection request" consisting of the following pair:
		// a: an address to connect to
		// b: a blocking queue through which the new channel will be returned.
		// We do this so that the connection can be performed in a different thread.
		BlockingQueue<TCPChannel> resQueue = new ArrayBlockingQueue<TCPChannel>(1); 
		ConnectRequest req = new ConnectRequest();
		req.peerName = peername;
		req.responseQueue = resQueue;
		
		assert connectRequests.remainingCapacity() > 0;
		NonInterruptable.put(connectRequests, req);
		// Wake up the selector so it will poll connectRequests.
		selector.wakeup();
		
		TCPChannel peer = NonInterruptable.take(resQueue);
		
		if (peer.state == ChannelConnectionState.CLOSED)
			// This could happen if there was a connection error.
			return null;
		
		return peer;
	}
	
	/**
	 * Get a communication channel by waiting for the next peer to connect 
	 * @return A channel through which messages can be sent to and 
	 * 		received from the peer.
	 */
	public TCPChannel getChannel() throws IOException {
		TCPChannel peer = NonInterruptable.take(acceptedChannels);
		
		if (peer.state == ChannelConnectionState.CLOSED)
			// This could happen if there was a connection error.
			return null;
		
		return peer;
	}
	
	
	
	enum ChannelConnectionState {
		CLOSED,					// not connected
		CONNECTING,				// Waiting for TCP Connection to a peer
		ACCEPT_HANDSHAKE_RECV,	// Receive handshake bytes (acceptor)
		CONNECTED				// Connected to peer
	};

	final static int HANDSHAKE_ACK = 0;
	final static int HANDSHAKE_NACK = 1;
	
	public class TCPChannel implements Channel {
		/**
		 * Name of peer.
		 */
		String name;
		
		/**
		 * Address of peer
		 */
		InetSocketAddress addr;
		
		/**
		 * Channel connected to peer.
		 */
		SocketChannel channel;
		
		/**
		 * The channel's selection key.
		 */
		SelectionKey key;
		
		/**
		 * Queue of messages waiting to be sent
		 */
		BlockingQueue<Message> writePendingMessages;
		
		/**
		 * Queue of received messages waiting to be read.
		 */
		BlockingQueue<Message> readPendingMessages;
		
		/** 
		 * The buffer currently being written. Buffers are
		 * removed from {@link #writeMessage} once they are 
		 * fully written. 
		 */
		ByteBuffer writeBuffer;
		
		/**
		 * The message currently being written. When the message is
		 * completely written, the next message is removed from 
		 * {@link #writePendingMessages}.
		 */
		Message writeMessage;

		/**
		 * The buffer currently being read. Buffers are added to
		 * {@link #readMessage} as they are filled.
		 */
		ByteBuffer readBuffer;

		/**
		 * The message currently being read. When a message is completely
		 * read, it is added to {@link #readPendingMessages} 
		 */
		Message readMessage;

		/**
		 * Number of bytes remaining to be read into the 
		 * {@link #readMessage} message. A negative number
		 * signifies that this many bytes of the message length
		 * are still being read (e.g., -3 means one byte of the
		 * message length has been read, and the buffer in {@link #readBuffer}
		 * contains that byte).
		 * 
		 */
		int readRemaining;
		
		/**
		 * Is this channel connected to a peer.
		 */
		
		
		ChannelConnectionState state;

	
		/**
		 * Construct an unconnected TCPChannel to a known peer.
		 * If peername does not resolve correctly, addr will be null. 
		 * @param peername
		 */
		TCPChannel(String peername) {
			name = peername;
			addr = resolve(peername);
			writePendingMessages = new LinkedBlockingQueue<Message>();
			readPendingMessages = new LinkedBlockingQueue<Message>();
			readRemaining = -4;
			state = ChannelConnectionState.CLOSED;
		}
		

		/**
		 * Construct a TCPChannel from a connected channel;
		 * we won't know the peer's name until the handshake is finished.
		 */
		TCPChannel(SocketChannel channel) {
			this.channel = channel;
			name = null;
			addr = (InetSocketAddress) channel.socket().getRemoteSocketAddress();
			state = ChannelConnectionState.ACCEPT_HANDSHAKE_RECV;
			writePendingMessages = new LinkedBlockingQueue<Message>();
			readPendingMessages = new LinkedBlockingQueue<Message>();
			readRemaining = -4;
		}
		
		@Override
		public Message newMessage() {
			return new Message();
		}

		@Override
		public void send(Message msg) throws IOException {
			NonInterruptable.put(writePendingMessages, msg);
			// wake up selector so that it will check if
			// keys need to become interested in write.
			NonInterruptable.put(writePendingPeers, this);
			selector.wakeup(); 
		}

		@Override
		public Message receive() throws IOException {
			return NonInterruptable.take(readPendingMessages);
		}


		public String getName() {
			return name;
		}
	}

	public TCPChannelFactory(int localport) throws IOException {
		shouldRun = true;
		
		this.localport = localport;
		InetAddress addr = InetAddress.getLocalHost();
		
		selector = Selector.open();
		
		serverChannel = ServerSocketChannel.open();
		ServerSocket socket = serverChannel.socket();
		if (localport == 0) {
			socket.bind(null);
			this.localport = socket.getLocalPort();
		} else { 
			socket.bind(new InetSocketAddress(localport));
		}

		this.localname = addr.getHostName() + ":" + this.localport;
		serverChannel.configureBlocking(false);
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		peersByChannel = new HashMap<SocketChannel, TCPChannel>();
		peersByName = new HashMap<String, TCPChannel>();
		connectedPending = new HashMap<TCPChannel, ConnectRequest>();
		connectRequests = new LinkedBlockingQueue<ConnectRequest>();
		acceptedChannels = new LinkedBlockingQueue<TCPChannel>();
		writePendingPeers = new LinkedBlockingQueue<TCPChannel>();
	}
	
	public TCPChannelFactory() throws IOException {
		this(0);
	}
	
	
	public void stop() {
		shouldRun = false;
		selector.wakeup();
	}
	/**
	 * Start a loop that is responsible for sending and receiving messages
	 * and delivering them to local destinations.
	 */
	@Override
	public void run() {
		while(shouldRun) {
			try {
				selector.select();
				
				for (SelectionKey key : selector.selectedKeys()) {
					if (key.isAcceptable()) {
						// Received a new connection
						acceptNewConnection(key);
					} else {
						SocketChannel chan = (SocketChannel) key.channel();
						TCPChannel peerChannel = peersByChannel.get(chan);
						
						if (key.isConnectable()) {
							connectNewConnection(peerChannel);
						}
						if (key.isValid() && key.isReadable()) {
							readFromConnection(peerChannel);
						}
						if (key.isValid() && key.isWritable()) {
							writeToConnection(peerChannel);
						}
					}
				}
				
				// Check for sockets that need their write bit set.
				for (TCPChannel writingPeer = writePendingPeers.poll(); 
					writingPeer != null; writingPeer = writePendingPeers.poll()) {
					if (writingPeer.key.isValid())
						writingPeer.key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
				}
				
				// Check for pending connection requests
				ConnectRequest req;
				
				req = connectRequests.poll();
				if (req != null) 
					handleConnectRequest(req);
				
			} catch (IOException ioe) {
				// TODO: Deal with exceptions more robustly
				shouldRun = false; // Just exit
			}
		}
	}
	
	
	void acceptNewConnection(SelectionKey key) {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		
		try {
			SocketChannel channel = server.accept();
			if (channel != null) {
				channel.configureBlocking(false);
				TCPChannel peer = new TCPChannel(channel);

				// we won't send anything until receiving the handshake,
				// so no need for OP_WRITE yet
				peer.key = channel.register(selector, SelectionKey.OP_READ);

				peersByChannel.put(peer.channel, peer);
				// We don't know the peer's name yet, so we don't put it in
				// peersByName.
			}
		} catch (IOException ioe) {
			// Ignore, the connection just died.
		} 
	}

	/**
	 * Generate a handshake message for the peer.
	 * Handshake consists of localname.
	 * @param peer
	 * @return the handshake message
	 */
	Message getHandshakeMessage(TCPChannel peer) {
		Message msg = new Message();
	
		byte[] localname = this.localname.getBytes();
		msg.write(localname, 0, localname.length);
		return msg;
	}

	
	/**
	 * Handle the accept handshake from the peer.
	 * @param msg
	 * @param peer
	 */
	void handleAcceptHandshake(Message msg, TCPChannel peer) {
		// The handshake should contain the peer name.
		byte[] encodedPeerName = new byte[msg.length];
		for (int pos = 0; pos < encodedPeerName.length; ) {
			int n = msg.read(encodedPeerName, pos, encodedPeerName.length - pos);
			assert n > 0;
			pos += n;
		}
		
		String peername = new String(encodedPeerName);
		peer.name = peername;
		peersByName.put(peer.name, peer);
		peer.state = ChannelConnectionState.CONNECTED;
		NonInterruptable.put(acceptedChannels, peer);

		peer.key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}
	
	/**
	 * Handle a connect request. 
	 * @param req the request connection.
	 */
	void handleConnectRequest(ConnectRequest req) throws IOException {
		// Shouldn't have multiple pending requests for same peer.
		assert connectedPending.get(req.peerName) == null;

		TCPChannel newPeer = new TCPChannel(req.peerName);
		if (newPeer.addr == null)
			// Name resolved badly.
			NonInterruptable.put(req.responseQueue, newPeer);
		else {
			try {
				newPeer.channel = SocketChannel.open();
				newPeer.channel.configureBlocking(false);
				newPeer.key = newPeer.channel.register(selector, SelectionKey.OP_CONNECT);
				newPeer.state = ChannelConnectionState.CONNECTING;
				newPeer.channel.connect(newPeer.addr);
				connectedPending.put(newPeer, req);
				peersByChannel.put(newPeer.channel, newPeer);
				peersByName.put(newPeer.name, newPeer);
			} catch (IOException ioe) {
				// Connection error -- we return a "bad" channel.
				newPeer.state = ChannelConnectionState.CLOSED;

				NonInterruptable.put(req.responseQueue, newPeer);
			}
		}
	}

	/**
	 * handle the final stage of a TCP connection (when the channel becomes
	 * connectable).
	 * @param peer
	 */
	void connectNewConnection(TCPChannel peer) {
		ConnectRequest req = connectedPending.get(peer);
		
		// A channel in the connecting state should have a corresponding
		// queue entry.
		assert req != null;

		boolean connected = false;
		try {
			connected = peer.channel.finishConnect();
			peer.key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
		} catch (IOException ioe) {
			connected = false;
		}
		
		if (!connected) {
			// Failed to connect. Add the closed peer to the queue.
			closePeer(peer);
			connectedPending.remove(peer.name);
			NonInterruptable.put(req.responseQueue, peer);
			return;
		}
		
		// We're connected. We now need to send the connection handshake message.
		peer.state = ChannelConnectionState.CONNECTED;

		// handshake message will actually be written in the writeToConnection
		// method when the selector returns the channel as writable.
		peer.writeMessage = getHandshakeMessage(peer);
		setupMessageWrite(peer);
		
		NonInterruptable.put(req.responseQueue, peer);
	}

	/**
	 * Read from the connection into a message.
	 * @param peer the TCPChannel to read from.
	 */
	void readFromConnection(TCPChannel peer) {
		Message msg;
		ByteBuffer buf;
		if (peer.readMessage != null) {
			if (peer.readBuffer == null) {
				peer.readBuffer = getNewBuffer();
				peer.readBuffer.clear();
			}
				
			buf = peer.readBuffer;
			msg = peer.readMessage;
		} else {
			// Starting a new message
			peer.readMessage = msg = new Message();
			peer.readBuffer = buf = getNewBuffer();
			buf.clear();
			buf.limit(4); // we only want to read the length at first

			peer.readRemaining = -4; // Need to read 4 bytes of length first.
		}
		
		int count;
		try {
			// Loop while there's still something to read for the current message
			do {
				assert peer.readRemaining != 0;

				if (peer.readRemaining < 0) {
					// Need to read some bytes of message length
					count = peer.channel.read(buf);
					if (count == -peer.readRemaining) {
						// We can decode the length now.
						buf.flip();
						int len = EncodingUtils.decodeInt(buf);
						
						assert len > 0;
						
						if (len > 0) {
							peer.readRemaining = len;
							buf.clear();
						} else {
							// This should only happen with a bad peer. 
							peer.readRemaining = -4;
							buf.rewind();
						}
					} else {
						peer.readRemaining += count;
					}
				} else {
					// We're reading the message itself
					if (peer.readRemaining < buf.remaining()) {
						buf.limit(buf.position() + peer.readRemaining); 
					}
					count = peer.channel.read(buf);
					if (count > 0) 
						peer.readRemaining -= count;

					if (buf.remaining() == 0) {
						// Finished reading to the buffer
						// There should be at least one byte in it.
						assert buf.position() != 0;
						
						msg.writeBuf(buf);
						if (peer.readRemaining == 0) {
							// finished reading the message
							switch(peer.state) {
							case ACCEPT_HANDSHAKE_RECV:
								// The message is the handshake.
								
								// No messages should have been sent yet.
								assert peer.writeMessage == null;
								
								handleAcceptHandshake(msg, peer);								
								break;
							default:
								// Put the message on the readPending queue.
								assert msg.length > 0;
								
								NonInterruptable.put(peer.readPendingMessages, msg);
								break;

							}

							peer.readBuffer = buf = null;
							peer.readMessage = msg = null;
							break; // exit the message reading loop
						} else {
 
							peer.readBuffer = buf = getNewBuffer();
							buf.clear();
						}
					}
				}
			} while (count > 0);

		} catch (IOException ioe) {
			// Treat any exception as a close
			count = -1;
		}
		if (count < 0) {
			closePeer(peer);
		}

	}

	/**
	 * Set up the writeBuffer for writing new message.
	 * This involves creating a new buffer with the message
	 * length. The new message is assumed to be in peer.writeMessage.
	 * @param peer
	 */
	void setupMessageWrite(TCPChannel peer) {
		// New buffer for sending length.
		peer.writeBuffer = getNewBuffer();
		peer.writeBuffer.clear();
		EncodingUtils.encode(peer.writeMessage.length, peer.writeBuffer);
		peer.writeBuffer.flip();
	}


	void writeToConnection(TCPChannel peer) {
		if (peer.state == ChannelConnectionState.ACCEPT_HANDSHAKE_RECV) {
			// We don't want anything to be written before we're in
			// state CONNECTED
			peer.key.interestOps(peer.key.interestOps() & ~SelectionKey.OP_WRITE);
			return;
		}
		
		if (peer.writeMessage == null) {
			if (peer.writePendingMessages.isEmpty()) {
				peer.key.interestOps(peer.key.interestOps() & ~SelectionKey.OP_WRITE);
				return;
			}

			peer.writeMessage = NonInterruptable.take(peer.writePendingMessages);
			setupMessageWrite(peer);
		}
		
		int count;
		try {
			do {
				count = peer.channel.write(peer.writeBuffer);
				if (peer.writeBuffer.remaining() == 0) {
					// Finished writing a buffer, get the next one.
					peer.writeBuffer = peer.writeMessage.readBuf();
				}
				
			} while (count > 0 && peer.writeBuffer != null);
			if (peer.writeBuffer == null) {
				// Wrote out entire message.
				assert peer.writeMessage.length == 0;
				peer.writeMessage = null;
			}
		} catch (IOException ioe) {
			// Treat any exception as a close
			count = -1;
		}
		if (count < 0) {
			closePeer(peer);
		}
	}

	/**
	 * Close the TCPChannel and remove it from the peers map.
	 * @param peer TCPChannel to close and remove.
	 */
	void closePeer(TCPChannel peer) {
		peer.state = ChannelConnectionState.CLOSED;
		if (peer.name != null)
			peersByName.remove(peer.name);
		if (peer.channel != null) {
			peersByChannel.remove(peer.channel);
			try {
				peer.channel.close();
			} catch (IOException ioe) {
				// Ignore -- we're closing the channel anyway.
			}
		}
	}

	public String getLocalname() {
		return localname;
	}
}

