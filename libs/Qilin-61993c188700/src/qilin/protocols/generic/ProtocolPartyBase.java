package qilin.protocols.generic;

import java.io.IOException;
import java.util.Random;

import qilin.comm.Channel;
import qilin.protocols.ProtocolParty;

/**
 * A default implementation of {@link ProtocolParty}.
 * @author talm
 *
 */
public class ProtocolPartyBase implements ProtocolParty {
	/**
	 * A communication channel to the peer.
	 */
	protected Channel toPeer;
	
	/**
	 * Randomness generator.
	 */
	protected Random rand;

	/**
	 * Set the relevant class members.
	 */
	@Override
	public void setParameters(Channel toPeer, Random rand) {
		this.toPeer = toPeer;
		this.rand = rand;
	}

	/**
	 * Default initialization does nothing.
	 */
	@Override
	public void init() throws IOException, InterruptedException {
		// Default init does nothing.
	}
}
