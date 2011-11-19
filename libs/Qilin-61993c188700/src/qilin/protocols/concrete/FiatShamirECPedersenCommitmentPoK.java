package qilin.protocols.concrete;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECPedersen;
import qilin.primitives.concrete.ECTrapdoorPedersen;
import qilin.primitives.concrete.Zn;
import qilin.primitives.generic.PedersenCommitment;
import qilin.protocols.generic.FiatShamir;
import qilin.protocols.generic.TrapdoorHomomorphicCommitmentPoK;

/**
 * A non-interactive elliptic-curve based version of {@link TrapdoorHomomorphicCommitmentPoK}, using the
 * Fiat-Shamir heuristic.
 * 
 * @see FiatShamir
 * @author talm
 *
 */
public class FiatShamirECPedersenCommitmentPoK extends 
TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>> {
	PedersenCommitment<ECPoint> committer;
	ECPedersenCoinflip pedersenFlip;
	
	public FiatShamirECPedersenCommitmentPoK(ECPedersen committer) {
		this(committer, committer.getGroup(), new Zn(committer.getGroup().orderUpperBound()));
	}

	public FiatShamirECPedersenCommitmentPoK(ECTrapdoorPedersen committer) {
		this(committer, committer.getGroup(), new Zn(committer.getGroup().orderUpperBound()));
	}

	private FiatShamirECPedersenCommitmentPoK(PedersenCommitment<ECPoint> committer,
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

	public Prover getProver() {
		return new Prover();
	}
	
	public class Verifier extends 
	TrapdoorHomomorphicCommitmentPoK<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>>.Verifier {
		protected Verifier() {
			super(committer, pedersenFlip.newSecond(committer));
		}
	}
	
	public Verifier getVerifier() {
		return new Verifier();
	}
}
