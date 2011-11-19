package qilin.protocols.concrete;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECPedersen;
import qilin.primitives.concrete.ECTrapdoorPedersen;
import qilin.primitives.concrete.Zn;
import qilin.primitives.generic.PedersenCommitment;
import qilin.protocols.generic.TrapdoorHomomorphicCommitmentPoK;

/**
 * Elliptic-curve implementation of {@link TrapdoorHomomorphicCommitmentPoK} using a {@link PedersenCommitment} as the
 * trapdoor commitment scheme. 
 * @author talm
 *
 */
public class ECPedersenCommitmentPoK extends 
TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>> {
	PedersenCommitment<ECPoint> committer;
	ECPedersenCoinflip pedersenFlip;
	
	public ECPedersenCommitmentPoK(ECPedersen committer) {
		this(committer, committer.getGroup(), new Zn(committer.getGroup().orderUpperBound()));
	}

	public ECPedersenCommitmentPoK(ECTrapdoorPedersen committer) {
		this(committer, committer.getGroup(), new Zn(committer.getGroup().orderUpperBound()));
	}

	private ECPedersenCommitmentPoK(PedersenCommitment<ECPoint> committer,
			ECGroup commitGroup, Zn	plainGroup) {
		super(plainGroup, plainGroup, commitGroup, plainGroup);
		this.committer = committer;
		pedersenFlip = new ECPedersenCoinflip(commitGroup);
	}
	
	public class TrapdoorProver extends
	TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>>.TrapdoorProver {
		protected TrapdoorProver(ECTrapdoorPedersen trapdoorCommitter) {
			super(committer, pedersenFlip.newTrapdoorFirst(trapdoorCommitter));
		}
	}

	public TrapdoorProver getTrapdoorProver() {
		if (!(committer instanceof ECTrapdoorPedersen)) {
			throw new UnsupportedOperationException("No trapdoor");
		} else {
			return new TrapdoorProver((ECTrapdoorPedersen) committer);
		}
	}
	
	public class Prover extends 
	TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>>.Prover {
		protected Prover() {
			super(committer, pedersenFlip.newFirst(committer));
		}
	}

	public Prover newProver() {
		return new Prover();
	}
	
	public class Verifier extends 
	TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>>.Verifier {
		protected Verifier() {
			super(committer, pedersenFlip.newSecond(committer));
		}
	}
	
	public Verifier newVerifier() {
		return new Verifier();
	}
}
