package qilin.protocols.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import qilin.comm.Message;
import qilin.primitives.Group;
import qilin.primitives.TrapdoorNonInteractiveCommitment;
import qilin.protocols.TrapdoorTwoPartyGroupElementFlip;
import qilin.util.StreamEncoder;

/**
 * A trapdoor version of the {@link BlumTwoPartyGroupElementFlip}.
 * @author talm
 *
 * @param <G>
 * @param <C>
 * @param <R>
 */
public class TrapdoorBlumTwoPartyGroupElementFlip<G, C, R> extends
BlumTwoPartyGroupElementFlip<G, C, R> {
	public TrapdoorBlumTwoPartyGroupElementFlip(Group<G> grp, 
			StreamEncoder<G> groupEncoder, StreamEncoder<C> commitEncoder,
			StreamEncoder<R> commitRandomnessEncoder) {
		super(grp, groupEncoder, commitEncoder, commitRandomnessEncoder);
	}
	
	/**
	 * Represents a first party that can force the coin-flip to any value using the trapdoor. 
	 * @author talm
	 *
	 */
	public class TrapdoorFirst extends First implements TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst<G> {
		TrapdoorNonInteractiveCommitment.EquivocatingCommitter<C, G, R> committer;

		TrapdoorFirst(TrapdoorNonInteractiveCommitment.EquivocatingCommitter<C, G, R>  committer) {
			super(committer);
			this.committer = committer;
		}

		public void trapdoorFlip(G outcome) throws IOException {
			Message commitMsg = toPeer.newMessage();
			OutputStream out = commitMsg.getOutputStream();

			R initialRandomness = committer.getRandom(rand);
			C initialCommit = committer.commit(grp.zero(), initialRandomness);

			commitEncoder.encode(initialCommit, out);
			toPeer.send(commitMsg);

			Message valueMsg = toPeer.receive();
			InputStream in = valueMsg.getInputStream();

			G value = groupEncoder.decode(in);

			Message revealMsg = toPeer.newMessage();
			out = revealMsg.getOutputStream();

			G initial = grp.add(outcome, grp.negate(value));
			R fakeRandomness = committer.equivocate(grp.zero(), initialRandomness, initial);

			groupEncoder.encode(initial, out);
			commitRandomnessEncoder.encode(fakeRandomness, out);
			toPeer.send(revealMsg);
		}
	}

	public TrapdoorFirst newTrapdoorFirst(TrapdoorNonInteractiveCommitment.EquivocatingCommitter<C, G, R> committer) {
		return new TrapdoorFirst(committer);
	}

}
