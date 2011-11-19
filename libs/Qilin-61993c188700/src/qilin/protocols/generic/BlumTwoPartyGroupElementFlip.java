package qilin.protocols.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import qilin.comm.Message;
import qilin.primitives.Group;
import qilin.primitives.NonInteractiveCommitment;
import qilin.protocols.CheatingPeerException;
import qilin.protocols.TwoPartyGroupElementFlip;
import qilin.util.StreamEncoder;

/**
 * Use the Blum "coin-flip over the telephone" to randomly select a group
 * element. Neither side (by itself) will know any additional side information
 * about the selected element (e.g., the discrete log of the selected element
 * will be hidden from both sides). 
 * 
 * @author talm
 *
 * @param <G> Group element that will be returned from the flip
 * @param <C> Type of commitment 
 * @param <R> Type of commitment's randomness
 */
public class BlumTwoPartyGroupElementFlip<G, C, R> {
	protected Group<G> grp;
	protected StreamEncoder<G> groupEncoder;
	protected StreamEncoder<C> commitEncoder;
	protected StreamEncoder<R> commitRandomnessEncoder;
	
	public BlumTwoPartyGroupElementFlip(Group<G> grp, 
			StreamEncoder<G> groupEncoder, StreamEncoder<C> commitEncoder,
			StreamEncoder<R> commitRandomnessEncoder) {
		this.grp = grp;
		this.groupEncoder = groupEncoder;
		this.commitEncoder = commitEncoder;
		this.commitRandomnessEncoder = commitRandomnessEncoder;
	}
	
	/**
	 * Represents the first party in the coin flip. 
	 *
	 */
	public class First extends ProtocolPartyBase implements TwoPartyGroupElementFlip.First<G> {
		NonInteractiveCommitment.Committer<C, G, R> committer;
		
		First(NonInteractiveCommitment.Committer<C, G, R> committer) {
			this.committer = committer;
		}
		
		public G flip() throws IOException {
			Message commitMsg = toPeer.newMessage();
			OutputStream out = commitMsg.getOutputStream();
			
			G initial = grp.sample(rand);
			R initialRandomness = committer.getRandom(rand);
			C initialCommit = committer.commit(initial, initialRandomness);
			
			commitEncoder.encode(initialCommit, out);
			toPeer.send(commitMsg);
			
			Message valueMsg = toPeer.receive();
			InputStream in = valueMsg.getInputStream();
			
			G value = groupEncoder.decode(in);
			
			Message revealMsg = toPeer.newMessage();
			out = revealMsg.getOutputStream();
			
			groupEncoder.encode(initial, out);
			commitRandomnessEncoder.encode(initialRandomness, out);
			toPeer.send(revealMsg);
			
			return grp.add(initial, value);
		}
	}
	
	public First newFirst(NonInteractiveCommitment.Committer<C, G, R> committer) {
		return new First(committer);
	}

	/**
	 * Represents the second party in the coin flip. 
	 *
	 */
	public class Second extends ProtocolPartyBase implements TwoPartyGroupElementFlip.Second<G> {
		NonInteractiveCommitment.Verifier<C, G, R> verifier;
		
		Second(NonInteractiveCommitment.Verifier<C, G, R> verifier) {
			this.verifier = verifier;
		}
		
		public G flip() throws IOException {
			Message commitMsg = toPeer.receive();
			InputStream in = commitMsg.getInputStream();
			C initialCommit = commitEncoder.decode(in);
			
			if (!verifier.verifyCommitment(initialCommit))
				throw new CheatingPeerException("peer sent bad initial commitment");

			Message valueMsg = toPeer.newMessage();
			OutputStream out = valueMsg.getOutputStream();
			G value = grp.sample(rand);
			groupEncoder.encode(value, out);
			
			toPeer.send(valueMsg);
			Message revealMsg = toPeer.receive();
			in = revealMsg.getInputStream();
			
			G initial = groupEncoder.decode(in);
			R initialRandomness = commitRandomnessEncoder.decode(in);
			
			if (!verifier.verifyOpening(initialCommit, initial, initialRandomness))
				throw new CheatingPeerException("peer did not open commitment correctly");
			
			return grp.add(initial, value);
		}
	}
	
	public Second newSecond(NonInteractiveCommitment.Verifier<C, G, R> verifier) {
		return new Second(verifier);
	}
}
