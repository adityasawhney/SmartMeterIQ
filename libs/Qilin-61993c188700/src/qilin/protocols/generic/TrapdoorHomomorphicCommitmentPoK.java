package qilin.protocols.generic;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

import qilin.comm.Message;
import qilin.primitives.Group;
import qilin.primitives.Homomorphic;
import qilin.primitives.NonInteractiveCommitment;
import qilin.protocols.TrapdoorCommitmentPoK;
import qilin.protocols.TrapdoorTwoPartyGroupElementFlip;
import qilin.util.IntegerUtils;
import qilin.util.StreamEncoder;
import qilin.util.Triplet;

/**
 * A protocol for zero-knowledge proof of knowledge of
 * an opening for a commitment. This class has a trapdoor
 * version that can be used to generate a "fake" proof
 * using a trapdoor in the group-flipping subprotocol used. 
 * @author talm
 *
 * @param <C> Commitment type
 * @param <P> Message type
 * @param <R> Randomness type (for commitment)
 */
public class TrapdoorHomomorphicCommitmentPoK<C, P, R,
Com extends NonInteractiveCommitment.Committer<C, P, R> & Homomorphic<C, P, R>,
Ver extends NonInteractiveCommitment.Verifier<C, P, R> & Homomorphic<C, P, R>> extends 
HomomorphicCommitmentPoK<C, P, R, Com, Ver> {
	
	public TrapdoorHomomorphicCommitmentPoK(Group<P> grp,
			StreamEncoder<P> plainEncoder, StreamEncoder<C> commitEncoder,
			StreamEncoder<R> commitRandomEncoder) {
		super(grp, plainEncoder, commitEncoder, commitRandomEncoder);
	}

	public class TrapdoorProver extends Prover implements TrapdoorCommitmentPoK.TrapdoorProver<C, P, R>{
		TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst<BigInteger> flipper;
		
		protected TrapdoorProver(Com committer, TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst<BigInteger> flipper) {
			super(committer, flipper);
			this.flipper = flipper;
		}
		
		/**
		 * Use the trapdoor to generate a fake proof that the prover 
		 * knows an opening of com1 to value plain1
		 *  
		 * @param com1
		 * @param plain1
		 * 
		 * 		 
		 */
		public void proveFakeValue(C com1, P plain1) throws IOException {
			Message commitMsg = toPeer.newMessage();
			OutputStream out = commitMsg.getOutputStream();

			// Choose a random challenge -- we'll force the real challenge
			// to this using the trapdoor coinflip.
			BigInteger chal = IntegerUtils.getRandomInteger(grp.orderUpperBound(), rand);
			
			// b and r' are randomly chosen.
			P b = grp.sample(rand);
			R rPrime = committer.getRandom(rand);
			
			// target = chal*plain1 + b
			P target = grp.add(grp.multiply(plain1, chal), b);
			C cTarget = committer.commit(target, rPrime);

			// bCom = cTarget/(chal * com1)
			C comMul = committer.multiply(com1, chal);
			C bCom = committer.add(cTarget, committer.negate(comMul));
			
			// Send (b, Com(b)) to verifier
			plainEncoder.encode(b, out);
			commitEncoder.encode(bCom, out);
			toPeer.send(commitMsg);

			// get a random challenge from verifier (we use
			// the trapdoor to force our desired challenge)
			flipper.trapdoorFlip(chal);

			// Send the precomputed r' to peer.
			Message responseMsg = toPeer.newMessage();
			out = responseMsg.getOutputStream();
			commitRandomEncoder.encode(rPrime, out);
			toPeer.send(responseMsg);
		}

		@Override
		public void prove(Triplet<C, P, R> params)	throws IOException {
			if (params.c != null) {
				super.prove(params);
			} else {
				proveFakeValue(params.a, params.b);
			}
		}
		
		
	}
	
	public TrapdoorProver newTrapdoorProver(Com committer, TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst<BigInteger> flipper) {
		return new TrapdoorProver(committer, flipper);
	}
}
