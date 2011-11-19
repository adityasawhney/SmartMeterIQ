package phishmarket.generic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import qilin.comm.Message;
import qilin.primitives.CyclicGroup;
import qilin.primitives.RandomOracle;
import qilin.primitives.concrete.Zn;
import qilin.primitives.generic.MerkleTree;
import qilin.primitives.generic.PedersenCommitment;
import qilin.primitives.generic.RandomOracleCommitment;
import qilin.primitives.generic.RandomOracleMerkleTree;
import qilin.primitives.generic.TrapdoorPedersenCommitment;
import qilin.protocols.CheatingPeerException;
import qilin.protocols.SigmaProtocol;
import qilin.protocols.TrapdoorTwoPartyGroupElementFlip;
import qilin.protocols.TwoPartyGroupElementFlip;
import qilin.protocols.generic.BlumTwoPartyGroupElementFlip;
import qilin.protocols.generic.HomomorphicCommitmentPoK;
import qilin.protocols.generic.NaorPinkasOT;
import qilin.protocols.generic.ProtocolPartyBase;
import qilin.protocols.generic.TrapdoorBlumTwoPartyGroupElementFlip;
import qilin.protocols.generic.TrapdoorHomomorphicCommitmentPoK;
import qilin.util.ByteEncoder;
import qilin.util.EncodingUtils;
import qilin.util.GenericsUtils;
import qilin.util.Pair;
import qilin.util.StreamEncoder;
import qilin.util.Triplet;
import qilin.util.EncodingUtils.ByteArrayEncoder;

/**
 * Allow a Seller to "obliviously" provide information to a Buyer
 * while being compensated iff the Buyer is interested in the information
 * and did not already know it.
 * 
 * @author talm
 *
 * @param <G>
 */
public class ObliviousInformationPurchase<G> {
	protected CyclicGroup<G> grp;
	protected StreamEncoder<G> grpEncoder;
	protected ByteEncoder<G> grpByteEncoder;
	protected RandomOracle H;

	protected Zn plainGroup;
	protected G g;
	
	protected PedersenKeyGeneration<G> keygen;
	protected NaorPinkasOT<G> ot;
	protected TrapdoorHomomorphicCommitmentPoK<G, BigInteger, BigInteger, PedersenCommitment<G>, PedersenCommitment<G>> comPoK;
	
	protected MerkleTree<byte[]> merkleTree;
	protected MerkleTree<byte[]>.PathEncoder pathEncoder;
	
	/**
	 * All known data. 
	 * Maps each known data string to a pair
	 * containing a commitment to the data and the randomness used
	 * for the commitment.
	 */
	protected Map<byte[],Pair<G,BigInteger>> knownData;
	
	/**
	 * "Chaff" commitments used to fake proofs that a value
	 * was previously known. 
	 */
	protected Queue<G> chaff;
	
	/**
	 * Chaff that has already been used (and shouldn't be used again).
	 */
	protected Set<G> usedChaff;
	
	
	/**
	 * Group element flip used to initialize Pedersen commitment that is
	 * binding for both parties.
	 */
	protected BlumTwoPartyGroupElementFlip<G, byte[], byte[]> oracleFlipper;
	
	/**
	 * Trapdoor flipper used in Trapdoor ZK proof of Knowledge.
	 * @see TrapdoorHomomorphicCommitmentPoK
	 */
	protected TrapdoorBlumTwoPartyGroupElementFlip<BigInteger, G, BigInteger> trapdoorFlipper;

	
	public ObliviousInformationPurchase(CyclicGroup<G> grp,
			StreamEncoder<G> grpEncoder, ByteEncoder<G> grpByteEncoder,
			RandomOracle H) {
		this(grp, grpEncoder, grpByteEncoder, H, new GenericsUtils.HashMap<byte[], Pair<G,BigInteger>>());
	}

	/**
	 * Constructor.
	 * @param grp The group MUST be of prime order (or at least have no small factors)
	 * @param grpEncoder
	 * @param grpByteEncoder
	 * @param H
	 */
	public ObliviousInformationPurchase(CyclicGroup<G> grp,
			StreamEncoder<G> grpEncoder, ByteEncoder<G> grpByteEncoder,
			RandomOracle H, Map<byte[],Pair<G,BigInteger>> knownData) {
		this.grp = grp;
		this.g = grp.getGenerator();
		this.grpEncoder = grpEncoder;
		this.grpByteEncoder = grpByteEncoder;
		this.H = H;

		ByteArrayEncoder trivialEncoder = new EncodingUtils.ByteArrayEncoder(H.getNativeLength());
		
		plainGroup = new Zn(grp.orderUpperBound());
		keygen = new PedersenKeyGeneration<G>(grp, grpEncoder);
		ot = new NaorPinkasOT<G>(grp, H, grpEncoder);
		comPoK = new TrapdoorHomomorphicCommitmentPoK<G, BigInteger, BigInteger, PedersenCommitment<G>, PedersenCommitment<G>>(plainGroup, plainGroup, grpEncoder, plainGroup);
		oracleFlipper = 
			new BlumTwoPartyGroupElementFlip<G, byte[], byte[]>(grp, grpEncoder, 
					trivialEncoder, trivialEncoder);
			
		trapdoorFlipper = 
			new TrapdoorBlumTwoPartyGroupElementFlip<BigInteger, G, BigInteger>(plainGroup, plainGroup, grpEncoder, plainGroup);
		
		merkleTree = new RandomOracleMerkleTree(H);
		pathEncoder = merkleTree.new PathEncoder(trivialEncoder);
		
		this.knownData = knownData;
		chaff = new LinkedList<G>();
		usedChaff = new HashSet<G>();
	}
	
	public Seller newSeller() {
		return new Seller();
	}
	
	public class Seller extends ProtocolPartyBase {
		/**
		 * Commitment that is always binding for the Buyer
		 */
		PedersenCommitment<G> comStar;
		NaorPinkasOT<G>.Sender otSender;
		
		/**
		 * Commitment to sum of payments
		 */
		G totalPayment;

		protected Seller() {
		}
		
		public G getCommitPK() {
			return comStar.getH();
		}
		
		@Override
		public void init() throws IOException, InterruptedException {
			// Generate "comStar" by flipping a random element to be
			// the second generator. Note that this only works with
			// groups of prime order!
			RandomOracleCommitment<G> committer = new RandomOracleCommitment<G>(H, H.getNativeLength(), grpByteEncoder);
			
			TwoPartyGroupElementFlip.First<G> firstFlipper = oracleFlipper.newFirst(committer);
			
			firstFlipper.setParameters(toPeer, rand);
			firstFlipper.init();
			G h = firstFlipper.flip();
			if (GenericsUtils.deepEquals(h, grp.zero())) {
				h = g;
			}
			init(h);
		}
		
		/**
		 * Initialize with a prearranged commitment public key.
		 * @param h
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public void init(G h) throws IOException, InterruptedException {
			comStar = new PedersenCommitment<G>(grp, h);
			
			// Initialize OT sender
			otSender = ot.newSender();
			otSender.setParameters(toPeer, rand);
			otSender.init();
			
			resetPayment();
		}
		
		/**
		 * Attempt to sell tagged information to the Buyer.  
		 * @param info information to be sold
		 * @param tag tag that describes the information.
		 * 	The tag will be revealed to the Buyer, who should be able to decide
		 * 	whether or not to purchase based only on the tag.
		 * @return commitment to payment (commitment to 0 or to 1).
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public G sell(byte[] info, byte[] tag) throws IOException, InterruptedException {
			// Perform key generation for Pedersen commitment

			
			// Generate two pairs of commitment keys, such that
			// the buyer knows the secret key of a single key in each pair.
			PedersenKeyGeneration<G>.Seller keygenSeller = keygen.new Seller();
			keygenSeller.setParameters(toPeer, rand);
			keygenSeller.init();
			PedersenKeyGeneration<G>.SellerKeys commitKeys01 = keygenSeller.generateKeys(); 
			PedersenKeyGeneration<G>.SellerKeys commitKeys23 = keygenSeller.generateKeys();
			
			Message msg = toPeer.receive();
			InputStream in = msg.getInputStream();
			
			byte[] merkleRoot = EncodingUtils.decodeByteArray(in);
			
			BigInteger r = comStar.getRandom(rand);
			BigInteger infoElement = stringToMsg(info);
			
			G infoCommit = comStar.commit(infoElement, r);
			
			// Send (tag, comStar(H(info), r)) to Buyer
			msg = toPeer.newMessage();
			OutputStream out = msg.getOutputStream();
			
			EncodingUtils.encode(tag, out);
			grpEncoder.encode(infoCommit, out);

			toPeer.send(msg);
			
			// Perform OT protocol with input strings s0=(info,r) and s1=k
			ByteArrayOutputStream x0 = new ByteArrayOutputStream();
			EncodingUtils.encode(info, x0);
			plainGroup.encode(r, x0);
			
			byte[] x1 = plainGroup.encode(commitKeys01.k); 
			                   
			otSender.send(x0.toByteArray(), x1);

			// Wait to receive "payment" commitment, bits b and b2 and 
			// the "already known" commitment ("proving" the buyer already
			// knew the information).
			msg = toPeer.receive();
			in = msg.getInputStream();
			
			// Decode commitment to "payment"
			G payCommit = grpEncoder.decode(in);
			if (!comStar.verifyCommitment(payCommit))
				throw new CheatingPeerException("Peer sent bad commitment");

			// Decode "already known commitment"
			G knownCommit = grpEncoder.decode(in);
			if (!comStar.verifyCommitment(knownCommit))
				throw new CheatingPeerException("Peer sent bad commitment");
			
			// Decode bits b1 and b2
			int b = EncodingUtils.decodeInt(in);
			int b2 = EncodingUtils.decodeInt(in);
			
			// Decode merkle path to root
			List<Pair<byte[],byte[]>> path = pathEncoder.decode(in);
			
			// Verify that payCommit is a commitment to 1 using key pk[b]
			G pkb = (b == 0) ? commitKeys01.pk0 : commitKeys01.pk1;
			
			if (!verifyProof(payCommit, BigInteger.ONE, pkb))
				throw new CheatingPeerException("Buyer failed first proof of payment=1");

			// Verify that payCommit is a commitment to 1 using key pk[b2]
			G pkb2 = (b2 == 0) ? commitKeys23.pk0 : commitKeys23.pk1;

			if (!verifyProof(payCommit, BigInteger.ONE, pkb2))
				throw new CheatingPeerException("Buyer failed second proof of payment=1");

			// Verify that payCommit is a commitment to 0 using key pk[1-b2]
			// Note that either this or the previous verification must be faked using
			// the trapdoor.
			G pk2Other = (b2 == 1) ? commitKeys23.pk0 : commitKeys23.pk1;

			if (!verifyProof(payCommit, BigInteger.ZERO, pk2Other))
				throw new CheatingPeerException("Buyer failed proof of payment=0");

			// Verify the infoCommit/knownCommit=comStar(0) using pk[1-b] 
			// (i.e., knownCommit is actually a commitment to infocommit or buyer
			// knows sk[1-b]).
			G tstCommit = comStar.add(infoCommit, comStar.negate(knownCommit));

			G pkOther = (b == 1) ? commitKeys01.pk0 : commitKeys01.pk1;

			if (!verifyProof(tstCommit, BigInteger.ZERO, pkOther))
				throw new CheatingPeerException("Buyer failed proof of previous knowledge");
				
			//Check merkle path
			byte[] knownCommitHash = hash(knownCommit);
			
			if (!merkleTree.verifyPath(knownCommitHash, merkleRoot, path)) 
				throw new CheatingPeerException("Buyer failed Merkle proof");
			
			totalPayment = comStar.add(totalPayment, payCommit);
			return payCommit;
		}
		

		public BigInteger getPaymentValue() throws IOException, InterruptedException {
			Message msg = toPeer.receive();
			InputStream in = msg.getInputStream();
			
			BigInteger paymentValue = plainGroup.decode(in);
			BigInteger paymentRand = plainGroup.decode(in);
			
			if (!comStar.verify(totalPayment, paymentValue, paymentRand))
				throw new CheatingPeerException("prover cheated on payment!");
			
			return paymentValue;
		}
		
		public void resetPayment() {
			totalPayment = comStar.commit(BigInteger.ZERO, BigInteger.ZERO); 
		}
		
		protected boolean verifyProof(G comVal, BigInteger plainVal, G pk) throws IOException, InterruptedException {
			PedersenCommitment<G> comPk = new PedersenCommitment<G>(grp, pk);
			TwoPartyGroupElementFlip.Second<BigInteger> flipperSecond = trapdoorFlipper.newSecond(comPk);
			HomomorphicCommitmentPoK<G, BigInteger, BigInteger, PedersenCommitment<G>, PedersenCommitment<G>>.Verifier comPoKVerifier =
				comPoK.newVerifier(comStar, flipperSecond);
			
			return executeSigmaProtocolVerification(comPoKVerifier, new Pair<G,BigInteger>(comVal, plainVal));
		}
		
		/**
		 * Actually run the verification protocol. 
		 * This method is separate in order to allow subclasses to override 
		 * it when using the FiatShamir heuristic to make the proof non-interactive.
		 */
		protected <ProofParams> boolean executeSigmaProtocolVerification(
				SigmaProtocol.Verifier<ProofParams> verifier, ProofParams params)
				throws IOException, InterruptedException {
			verifier.setParameters(toPeer, rand);
			verifier.init();
			return verifier.verify(params);
		}
	}

	/**
	 * Interface used to check buyer interest.
	 * 
	 * @author talm
	 *
	 */
	public interface BuyerInterest {
		/**
		 * Check if a buyer is interested in a particular tag.
		 * @param tag
		 * @return true iff the buyer is interested.
		 */
		public boolean isInterested(byte[] tag);
	}
	
	public static class BuyerInfo {
		public byte[] tag;
		public byte[] info;
		public boolean didPay;
	}
	
	public Buyer newBuyer() {
		return new Buyer();
	}
	
	public class Buyer extends ProtocolPartyBase {
		/**
		 * Commitment that is always binding for the Buyer
		 */
		PedersenCommitment<G> comStar;
		NaorPinkasOT<G>.Chooser otChooser;
		
		/**
		 * total payment so far
		 */
		BigInteger totalPayment;
		
		/**
		 * Sum of randomness used in commitments to payment so far.
		 */
		BigInteger totalPaymentRand;
		
		protected Buyer() {
		}
		
		public G getCommitPK() {
			return comStar.getH();
		}
		
		public void init() throws IOException, InterruptedException {
			// Generate "comStar" by flipping a random element to be
			// the second generator. Note that this only works with
			// groups of prime order!
			ByteArrayEncoder encoder = new EncodingUtils.ByteArrayEncoder(H.getNativeLength());
			
			BlumTwoPartyGroupElementFlip<G, byte[], byte[]> flipper = 
				new BlumTwoPartyGroupElementFlip<G, byte[], byte[]>(grp, grpEncoder, 
						encoder, encoder);

			RandomOracleCommitment<G> committer = new RandomOracleCommitment<G>(H, H.getNativeLength(), grpByteEncoder);
			
			TwoPartyGroupElementFlip.Second<G> secondFlipper = flipper.newSecond(committer);
			secondFlipper.setParameters(toPeer, rand);
			secondFlipper.init();
			G h = secondFlipper.flip();
			if (GenericsUtils.deepEquals(h, grp.zero())) {
				h = g;
			}
			init(h);
		}
			
		/**
		 * Initialize with a prearranged commitment public key.
		 * @param h
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public void init(G h) throws IOException, InterruptedException {
			comStar = new PedersenCommitment<G>(grp, h);
			
			// Initialize OT sender
			otChooser = ot.newChooser();
			otChooser.setParameters(toPeer, rand);
			otChooser.init();
			
			totalPayment = BigInteger.ZERO;
			totalPaymentRand = BigInteger.ZERO;
		}
		
		public BuyerInfo buy(BuyerInterest checkInterest) throws IOException, InterruptedException {
			BuyerInfo retval = new BuyerInfo();
			
			// Perform key generation for Pedersen commitment
			
			// Generate two pairs of commitment keys, such that
			// the buyer knows the secret key of a single key in each pair.
			PedersenKeyGeneration<G>.Buyer keygenBuyer = keygen.new Buyer();
			keygenBuyer.setParameters(toPeer, rand);
			keygenBuyer.init(); // Doesn't actually do anything now, but might in the future..
			PedersenKeyGeneration<G>.BuyerKeys commitKeys01 = keygenBuyer.generateKeys(); 
			PedersenKeyGeneration<G>.BuyerKeys commitKeys23 = keygenBuyer.generateKeys();
			
			Message msg = toPeer.newMessage();
			OutputStream out = msg.getOutputStream();
			
			// Send root of Merkle tree
			if (chaff.isEmpty()) {
				// First add a chaff value
				G chaffVal = grp.sample(rand);
				chaff.add(chaffVal);
				merkleTree.addLeaf(hash(chaffVal));
			}
			EncodingUtils.encode(merkleTree.getRoot(), out);
			toPeer.send(msg);
			
			// Receive (tag, commit(H(info),r))
			msg = toPeer.receive();
			InputStream in = msg.getInputStream();
			
			retval.tag = EncodingUtils.decodeByteArray(in);
			G infoCommit = grpEncoder.decode(in);
			
			if (!comStar.verifyCommitment(infoCommit))
				throw new CheatingPeerException("Seller sent bad info commitment");

			msg = toPeer.newMessage();
			out = msg.getOutputStream();
			
			BigInteger paymentValue;
			BigInteger rComPayment = comStar.getRandom(rand);
			
			boolean interested = checkInterest.isInterested(retval.tag); 
			if (interested) {
				// Buyer is interested in tag
				byte[] infoEncoded = otChooser.receive(0);
				ByteArrayInputStream infoStream = new ByteArrayInputStream(infoEncoded);

				retval.info = EncodingUtils.decodeByteArray(infoStream);
				BigInteger r = plainGroup.decode(infoStream);
				
				// Check if we already knew info.
				List<Pair<byte[],byte[]>> path = null;
				Pair<G,BigInteger> known = knownData.get(retval.info);
				
				if (known != null) {
					assert comStar.verify(known.a, stringToMsg(retval.info), known.b);
					path = merkleTree.getPath(hash(known.a));
				}
				if (path != null) {
					// We already knew the info
					// "Payment" is 0, since we already knew the info
					retval.didPay = false;
					paymentValue = BigInteger.ZERO;
					G comPayment = comStar.commit(paymentValue, rComPayment);
					
					G knownCommit = known.a;
					BigInteger knownRandomness = known.b;

					
					msg = toPeer.newMessage();
					out = msg.getOutputStream();
					
					// Encode the payment commitment, the known commitment, the bits b, b' and the
					// path from the known commitment to the root of the merkle tree.
					grpEncoder.encode(comPayment, out);
					grpEncoder.encode(knownCommit, out);
					EncodingUtils.encode(commitKeys01.b, out);
					EncodingUtils.encode(commitKeys23.b, out);
					
					pathEncoder.encode(path, out);
				
					toPeer.send(msg);

					// "Prove" comPayment is a commitment to 1 using secret key skb
					fakeProof(comPayment, BigInteger.ONE, commitKeys01.skb, commitKeys01.getPKb());
					
					// "Prove" comPayment is a commitment to 1 using secret key skb'
					fakeProof(comPayment, BigInteger.ONE, commitKeys23.skb, commitKeys23.getPKb());

					// Prove (actual proof) comPayment is a commitment to 0 
					realProof(comPayment, BigInteger.ZERO, rComPayment, commitKeys23.getPKother());
					
					// Prove (real proof) that infoCommit is a commitment to the same value 
					// knownCommit
					BigInteger infoElement = stringToMsg(retval.info);
					G tstCommit = comStar.add(infoCommit, comStar.negate(knownCommit));
					BigInteger tstRand = comStar.rndadd(infoElement, r, 
							plainGroup.negate(infoElement), comStar.rndnegate(infoElement, knownRandomness));
		
					assert comStar.verify(tstCommit, BigInteger.ZERO, tstRand);
					realProof(tstCommit, BigInteger.ZERO, tstRand, commitKeys01.getPKother());
				} else {
					// We did not know the info

					// "Payment" is 1, since we received new info
					retval.didPay = true;
					paymentValue = BigInteger.ONE;
					G comPayment = comStar.commit(paymentValue, rComPayment);
					
					// We'll use a chaff commitment for "previously known"
					// (since we didn't previously know)
					G chaffVal = chaff.remove();
					usedChaff.add(chaffVal);

					byte[] chaffHash = hash(chaffVal);
					
					msg = toPeer.newMessage();
					out = msg.getOutputStream();
					
					// Encode the chaff commitment, the bits b, b' and the
					// path from the chaff commitment to the root of the Merkle tree.
					grpEncoder.encode(comPayment, out);
					grpEncoder.encode(chaffVal, out);
					EncodingUtils.encode(1 - commitKeys01.b, out);
					EncodingUtils.encode(1 - commitKeys23.b, out);
					
					path = merkleTree.getPath(chaffHash);
					assert path != null;
					
					pathEncoder.encode(path, out);
				
					toPeer.send(msg);
					
					// Prove comPayment is a commitment to 1 using public key pk[1-b]
					G pkOther = commitKeys01.b == 0 ? commitKeys01.pk1 : commitKeys01.pk0;
					realProof(comPayment, BigInteger.ONE, rComPayment, pkOther);
					
					// Prove comPayment is a commitment to 1 using public key pk'[1-b'] 
					G pk2Other = commitKeys23.b == 0 ? commitKeys23.pk1 : commitKeys23.pk0;
					realProof(comPayment, BigInteger.ONE, rComPayment, pk2Other);

					// "Prove" comPayment is a commitment to 0 using secret key sk[b'] 
					fakeProof(comPayment, BigInteger.ZERO, commitKeys23.skb, commitKeys23.getPKb());
					
					// "Prove" infoCommit is a commitment to the same value as chaff 
					G tstCommit = comStar.add(infoCommit, comStar.negate(chaffVal));
					fakeProof(tstCommit, BigInteger.ZERO, commitKeys01.skb, commitKeys01.getPKb());
				}
			} else {
				// Buyer is not interested in tag
				byte[] kEncoded = otChooser.receive(1);
				BigInteger k = plainGroup.decode(kEncoded);
				
				// "Payment" is 0, since we don't want the info
				retval.didPay = false;
				paymentValue = BigInteger.ZERO;
				G comPayment = comStar.commit(paymentValue, rComPayment);
				
				// We'll use a chaff commitment for "previously known"
				// (since we don't even know what we don't know...)
				G chaffVal = chaff.remove();
				usedChaff.add(chaffVal);
				
				byte[] chaffHash = hash(chaffVal);
				
				// Encode the chaff commitment, the bits b, b' and the
				// path from the chaff commitment to the root of the merkle tree.
				grpEncoder.encode(comPayment, out);
				grpEncoder.encode(chaffVal, out);
				EncodingUtils.encode(commitKeys01.b, out);
				EncodingUtils.encode(commitKeys23.b, out);
				
				List<Pair<byte[],byte[]>> path = merkleTree.getPath(chaffHash);
				assert path != null;
				
				pathEncoder.encode(path, out);
				
				toPeer.send(msg);
				
				// "Prove" comPayment is a commitment to 1 using secret key skb
				fakeProof(comPayment, BigInteger.ONE, commitKeys01.skb, commitKeys01.getPKb());
				
				// "Prove" comPayment is a commitment to 1 using secret key skb'
				fakeProof(comPayment, BigInteger.ONE, commitKeys23.skb, commitKeys23.getPKb());

				// Prove (actual proof) comPayment is a commitment to 0 
				realProof(comPayment, BigInteger.ZERO, rComPayment, commitKeys23.getPKother());
				
				// "Prove" infoCommit is a commitment to the same value as chaff
				G tstCommit = comStar.add(infoCommit, comStar.negate(chaffVal));
				fakeProof(tstCommit, BigInteger.ZERO, commitKeys01.getSkOther(k), commitKeys01.getPKother());
			}

			totalPaymentRand = comStar.rndadd(totalPayment, totalPaymentRand, 
					paymentValue, rComPayment);
			totalPayment = totalPayment.add(paymentValue);
			
			return retval;
		}


		public BigInteger getPaymentValue() {
			return totalPayment;
		}
		
		public void provePaymentValue() throws IOException, InterruptedException {
			Message msg = toPeer.newMessage();
			OutputStream out = msg.getOutputStream();
			
			plainGroup.encode(totalPayment, out);
			plainGroup.encode(totalPaymentRand, out);
			
			toPeer.send(msg);
		}
		
		public void resetPayment() {
			totalPayment = BigInteger.ZERO;
			totalPaymentRand = BigInteger.ZERO; 
		}

		public void addInfo(byte[] info) {
			
			Pair<G,BigInteger> infoComPair = knownData.get(info);
			
			if (infoComPair == null) {
				BigInteger infoElement = stringToMsg(info);
				
				BigInteger r = comStar.getRandom(rand);
				G infoCommit = comStar.commit(infoElement, r);
				infoComPair = new Pair<G, BigInteger>(infoCommit,r);
				knownData.put(info, infoComPair);
			}
			
			merkleTree.addLeaf(hash(infoComPair.a));
		}

		
		/**
 		 * "Prove" to the peer that comVal is a commitment to plainVal
		 * using Pedersen commitment with public key pk.
 		 * @param comVal
		 * @param plainVal
		 * @param comRand
		 * @param pk
		 */
		protected void realProof(G comVal, BigInteger plainVal, BigInteger comRand, G pk) throws IOException, InterruptedException {
			PedersenCommitment<G> comPk = new PedersenCommitment<G>(grp, pk);
			TwoPartyGroupElementFlip.First<BigInteger> flipperProver = 
				trapdoorFlipper.newFirst(comPk);

			HomomorphicCommitmentPoK<G, BigInteger, BigInteger, PedersenCommitment<G>, PedersenCommitment<G>>.Prover comPoKProver =
				comPoK.newProver(comStar, flipperProver);
			
			executeSigmaProtocolProof(comPoKProver, new Triplet<G, BigInteger, BigInteger>(comVal, plainVal, comRand), pk);
		}
		
		/**
		 * "Prove" to the peer that comVal is a commitment to plainVal
		 * using trapdoor Pedersen commitment with secret key sk.
		 * @param comVal
		 * @param plainVal
		 * @param sk
		 */
		protected void fakeProof(G comVal, BigInteger plainVal, BigInteger sk, G pk) throws IOException, InterruptedException {
			TrapdoorPedersenCommitment<G> comPk = new TrapdoorPedersenCommitment<G>(grp, sk);
			TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst<BigInteger> trapdoorFlipperProver = 
				trapdoorFlipper.newTrapdoorFirst(comPk);

			TrapdoorHomomorphicCommitmentPoK<G, BigInteger, BigInteger, PedersenCommitment<G>, PedersenCommitment<G>>.TrapdoorProver comPoKProver =
				comPoK.newTrapdoorProver(comStar, trapdoorFlipperProver);
			
			executeSigmaProtocolProof(comPoKProver, new Triplet<G, BigInteger, BigInteger>(comVal, plainVal, null), pk);
		}
		
		/**
		 * Actually run the proof protocol. 
		 * This method is separate in order to allow subclasses to override 
		 * it when using the FiatShamir heuristic to make the proof non-interactive.
		 */
		protected <ProverParams> void executeSigmaProtocolProof(
				SigmaProtocol.Prover<ProverParams> prover, ProverParams params, G pk)
				throws IOException, InterruptedException {
			prover.setParameters(toPeer, rand);
			prover.init();
			prover.prove(params);
		}
	}
	
	protected BigInteger stringToMsg(byte[] str) {
		byte[] hashed = H.hash(str, plainGroup.getMinLength());
		return plainGroup.denseDecode(hashed);
	}
	
	protected byte[] hash(G element) {
		byte[] elementEncoded = grpByteEncoder.encode(element);
		return H.hash(elementEncoded, H.getNativeLength());
	}
}
