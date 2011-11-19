package phishmarket.generic;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import qilin.primitives.CyclicGroup;
import qilin.primitives.RandomOracle;
import qilin.primitives.StreamingRandomOracle;
import qilin.primitives.generic.PedersenCommitment;
import qilin.protocols.SigmaProtocol;
import qilin.protocols.TwoPartyGroupElementFlip;
import qilin.protocols.generic.FiatShamir;
import qilin.util.ByteEncoder;
import qilin.util.GenericsUtils;
import qilin.util.Pair;
import qilin.util.StreamEncoder;
import qilin.util.Triplet;




/**
 * Allow a Seller to "obliviously" provide information to a Buyer
 * while being compensated iff the Buyer is interested in the information
 * and did not already know it.
 * 
 * @author talm
 *
 * @param <G>
 */
public class ObliviousInformationPurchaseWithFiatShamir<G> extends ObliviousInformationPurchase<G> {
	StreamingRandomOracle streamH;
	
	public ObliviousInformationPurchaseWithFiatShamir(CyclicGroup<G> grp,
			StreamEncoder<G> grpEncoder, ByteEncoder<G> grpByteEncoder,
			RandomOracle H, StreamingRandomOracle streamH) {
		this(grp, grpEncoder, grpByteEncoder, H, streamH, new GenericsUtils.HashMap<byte[], Pair<G,BigInteger>>());
	}

	/**
	 * Constructor.
	 * @param grp The group MUST be of prime order (or at least have no small factors)
	 * @param grpEncoder
	 * @param grpByteEncoder
	 * @param H
	 */
	public ObliviousInformationPurchaseWithFiatShamir(CyclicGroup<G> grp,
			StreamEncoder<G> grpEncoder, ByteEncoder<G> grpByteEncoder,
			RandomOracle H, StreamingRandomOracle streamH, Map<byte[],Pair<G,BigInteger>> knownData) {
		super(grp, grpEncoder, grpByteEncoder, H, knownData);
		this.streamH = streamH;
	}
	

	public Seller newSeller() {
		return new Seller();
	}
	
	public class Seller extends ObliviousInformationPurchase<G>.Seller {
		/**
		 * Run the verification protocol using the FiatShamir heuristic. 
		 */
		protected boolean executeSigmaProtocolVerification(
				SigmaProtocol.Verifier<Pair<G, BigInteger>> verifier, Pair<G, BigInteger> params)
				throws IOException, InterruptedException {
			FiatShamir.Verifier<Pair<G, BigInteger>> fiatShamirVerifier = 
				new FiatShamir.Verifier<Pair<G, BigInteger>>(verifier, streamH);
			fiatShamirVerifier.setParameters(toPeer, rand);
			fiatShamirVerifier.init();
			return fiatShamirVerifier.verify(params);
		}
	}
	
	public Buyer newBuyer() {
		return new Buyer();
	}
	
	public class Buyer extends ObliviousInformationPurchase<G>.Buyer  {
		/**
		 * Run the proof protocol using the FiatShamir heuristic. 
		 */
		protected void executeSigmaProtocolProof(
				SigmaProtocol.Prover<Triplet<G,BigInteger,BigInteger>> prover, 
				Triplet<G,BigInteger,BigInteger> params, G pk)
				throws IOException, InterruptedException {
			PedersenCommitment<G> comPk = new PedersenCommitment<G>(grp, pk);
			TwoPartyGroupElementFlip.Second<BigInteger> flipperSecond = trapdoorFlipper.newSecond(comPk);
			SigmaProtocol.Verifier<Pair<G,BigInteger>> verifier = comPoK.newVerifier(comStar, flipperSecond);
 
			FiatShamir.Prover<Pair<G, BigInteger>, Triplet<G,BigInteger,BigInteger>> fiatShamirProver = 
				new FiatShamir.Prover<Pair<G, BigInteger>, Triplet<G,BigInteger,BigInteger>>(prover, verifier, streamH);
			fiatShamirProver.setParameters(toPeer, rand);
			fiatShamirProver.init();
			fiatShamirProver.prove(params);
		}
	}
}
