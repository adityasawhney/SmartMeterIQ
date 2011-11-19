package phishmarket.generic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import qilin.comm.Message;
import qilin.primitives.CyclicGroup;
import qilin.primitives.NonInteractiveCommitment.Committer;
import qilin.primitives.NonInteractiveCommitment.Verifier;
import qilin.primitives.generic.TrapdoorPedersenCommitment;
import qilin.protocols.generic.ProtocolPartyBase;
import qilin.util.GenericsUtils;
import qilin.util.StreamEncoder;

/**
 * Generate keys for a {@link TrapdoorPedersenCommitment} over a generic group. 
 * Two keys are generated in such a way that the Buyer (who will play the
 * Committer) will learn exactly one of the keys, while the Seller (who plays
 * the Verifier) will not know which one. The Seller will learn the commitment
 * public keys, as well as a secret key that can be used by the Buyer to compute the
 * trapdoor for the second commitment.
 * @author talm
 *
 * @param <G> the element type for the underlying group.
 * 
 * @see Committer
 * @see Verifier
 */
public class PedersenKeyGeneration<G> {
	CyclicGroup<G> grp;
	
	/**
	 * A generator for the group g.
	 */
	transient G g;
	
	StreamEncoder<G> grpEncoder;
	
	public class SellerKeys {
		G pk0;
		G pk1;
		BigInteger k;
	}
	
	public class BuyerKeys {
		G pk0;
		G pk1;
		int b;
		BigInteger skb;
		
		/**
		 * Return the secret key for pk[1-b] whose value is k-skb  
		 * @param k the "global" key received by the Seller
		 * @return a key skOther such that pk[1-b]=skOther * b
		 */
		public BigInteger getSkOther(BigInteger k) {
			return k.subtract(skb).mod(grp.orderUpperBound());
		}
		
		/**
		 * Convenience method to get the public key pk[b] associated with sk[b].
		 */
		public G getPKb() {
			return b == 0 ? pk0 : pk1; 
		}
		

		/**
		 * Convenience method to get the public key pk[1-b] associated with k-skb.
		 */
		public G getPKother() {
			return b == 0 ? pk1 : pk0; 
		}
	}
	
	public PedersenKeyGeneration(CyclicGroup<G> grp, StreamEncoder<G> grpEncoder) {
		this.grp = grp;
		this.g = grp.getGenerator();
		this.grpEncoder = grpEncoder;
	}
	
	/**
	 * Represents the Seller (or Verifier) in the commitment. 
	 * @author talm
	 *
	 */
	public class Seller extends ProtocolPartyBase {
		public SellerKeys generateKeys() throws IOException, InterruptedException {
			SellerKeys keys = new SellerKeys();
			
			keys.k = TrapdoorPedersenCommitment.generateKey(grp, rand);
			G gk = grp.multiply(g, keys.k);
			
			Message msg = toPeer.newMessage();
			OutputStream out = msg.getOutputStream();
			
			grpEncoder.encode(gk, out);
			toPeer.send(msg);
			
			msg = toPeer.receive();
			InputStream in = msg.getInputStream();
		
			keys.pk0 = grpEncoder.decode(in);
			keys.pk1 = grp.add(gk, grp.negate(keys.pk0));

			return keys;
		}
	}
	
	/**
	 * Represents the Buyer (or Prover) in the commitment. 
	 * @author talm
	 *
	 */
	public class Buyer extends ProtocolPartyBase {
		public BuyerKeys generateKeys() throws IOException, InterruptedException {
			BuyerKeys keys = new BuyerKeys();
			
			Message msg = toPeer.receive();
			InputStream in = msg.getInputStream();
			
			G gk = grpEncoder.decode(in);

			G pk;
			G pkOther;
			
			do {
				keys.skb = TrapdoorPedersenCommitment.generateKey(grp, rand);
				pk = grp.multiply(g, keys.skb);

				pkOther = grp.add(gk, grp.negate(pk));
			} while (GenericsUtils.deepEquals(pkOther, grp.zero()));
			
			keys.b = rand.nextInt(2);
			
			if (keys.b == 0) {
				keys.pk0 = pk;
				keys.pk1 = pkOther;
			} else {
				keys.pk1 = pk;
				keys.pk0 = pkOther;
			}
			
			msg = toPeer.newMessage();
			OutputStream out = msg.getOutputStream();
			
			grpEncoder.encode(keys.pk0, out);
			toPeer.send(msg);
			return keys;
		}
		
	}

}
