package qilin.protocols.generic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import qilin.comm.Message;
import qilin.primitives.CyclicGroup;
import qilin.primitives.RandomOracle;
import qilin.protocols.OT1of2;
import qilin.util.EncodingUtils;
import qilin.util.IntegerUtils;
import qilin.util.StreamEncoder;


/**
 * An implementation of the Naor-Pinkas OT protocol over a generic group in which discrete-log is hard.
 * 
 * More information about the Naor-Pinkas protocol can be found in <a href="http://www.pinkas.net/PAPERS/effot.ps">the paper</a>.
 * 
 * @param <G> the group element type.
 */
public class NaorPinkasOT<G> {
	/**
	 * Random oracle
	 */
	protected RandomOracle H;

	/**
	 * The group over which we're working.
	 * DDH is assumed to be hard in this group
	 */
	protected CyclicGroup<G> grp;

	/**
	 * Encoder/decoder for group elements
	 */
	protected StreamEncoder<G> grpEncoder;

	/**
	 * A generator for the group {@link #grp}.
	 */
	transient protected G g;


	public NaorPinkasOT(CyclicGroup<G> grp, RandomOracle H, StreamEncoder<G> grpEncoder) {
		this.grp = grp;
		this.H = H;
		this.grpEncoder = grpEncoder;
		this.g = grp.getGenerator();
	}

	public class Sender extends ProtocolPartyBase implements OT1of2.Sender {
		/**
		 * The first part of the secret key for this OT.
		 * Note that the same key may be used for multiple
		 * transfers.
		 * The corresponding public key is computed {@link #C}={@link #g} k.
		 */
		BigInteger k;

		/**
		 * The second part of the secret key for this OT.
		 * Note that the same key may be used for multiple
		 * transfers.
		 * The corresponding public key is computed {@link #gr}={@link #g} r.
		 */
		BigInteger r;


		/**
		 * counter used to ensure each invocation is unique. Note that
		 * the counter value is sent to the chooser. 
		 */
		int R;


		// The values below are precomputed.


		/**
		 * A part of the public key for this instance of OT. This can be computed
		 * from the secret key: C={@link #g} {@link #k}
		 */
		transient G C;

		/**
		 * A part of the public key for this instance of OT. This can be computed
		 * from the secret key: Cr={@link #C} {@link #r}
		 */
		transient G Cr;

		/**
		 * A part of the public key for this instance of OT. This can be computed
		 * from the secret key: gr={@link #g} {@link #r}
		 */
		transient G gr;

		/**
		 * Was init called for this instance.
		 */
		boolean init = false;

		protected Sender() {
		}
		
		/**
		 * Compute the public keys from the secret values.
		 */
		void precompute() {
			C = grp.multiply(g, k);
			Cr = grp.multiply(C, r);
			gr = grp.multiply(g, r);
		}


		/**
		 * Initialize the public and secret keys.
		 * This method must be called before {@link #send(byte[], byte[])}
		 * @throws IOException
		 */
		@Override
		public void init() throws IOException, InterruptedException {
			// Secret key k
			k = IntegerUtils.getRandomInteger(grp.orderUpperBound(), rand);
			r = IntegerUtils.getRandomInteger(grp.orderUpperBound(), rand);
			R = 0;
			precompute();

			// Send public key to chooser.
			Message init = toPeer.newMessage();
			OutputStream outbuf = init.getOutputStream();

			grpEncoder.encode(C, outbuf);
			grpEncoder.encode(gr, outbuf);

			toPeer.send(init);

			this.init = true;
		}

		/**
		 * Send two inputs. {@link #init()} must be called before 
		 * this method. The OT protocol ensures that the chooser will learn
		 * at most one of the two inputs. If the inputs are of different lengths,
		 * the length of both inputs will be leaked to the chooser.
		 * @param x0 
		 * @param x1
		 * @throws IOException
		 */
		public void send(byte[] x0, byte[] x1) throws IOException {
			assert init == true;

			// Get the instance public key PK0
			Message msg = toPeer.receive();
			G PK0 = grpEncoder.decode(msg.getInputStream());

			// Compute rPK0 and rPK1=rC-rPK0
			G PK0r = grp.multiply(PK0, r);
			G PK1r = grp.add(Cr, grp.negate(PK0r)); 

			Message outmsg = toPeer.newMessage();
			OutputStream outbuf = outmsg.getOutputStream();

			ByteArrayOutputStream buf = new ByteArrayOutputStream();

			// Create a local copy of R and increment the member variable.
			int R = this.R++;

			// Compute h0 = H(PK0r,R,0)
			grpEncoder.encode(PK0r, buf);
			EncodingUtils.encode(R, buf);
			buf.write(0);
			byte[] h0 = H.hash(buf.toByteArray(), x0.length);

			// Compute h1 = H(PK1r,R,1)
			buf.reset();
			grpEncoder.encode(PK1r, buf);
			EncodingUtils.encode(R, buf);
			buf.write(1);
			byte[] h1 = H.hash(buf.toByteArray(), x1.length);

			// "Encrypt" x0 by xoring with h0 
			for (int i = 0; i < x0.length; ++i) {
				h0[i] ^= x0[i];
			}
			// "Encrypt" x1 by xoring with h1
			for (int i = 0; i < x1.length; ++i) {
				h1[i] ^= x1[i];
			}

			// Send R and the two encrypted strings to the chooser.
			EncodingUtils.encode(R, outbuf); 
			EncodingUtils.encode(h0, outbuf);
			EncodingUtils.encode(h1, outbuf);
			toPeer.send(outmsg);
		}

	}
	
	public Sender newSender() {
		return new Sender();
	}


	public class Chooser extends ProtocolPartyBase implements OT1of2.Chooser {
		/**
		 * One part of the public key for this OT
		 */
		G C;

		/**
		 * The other part of the public key for this OT
		 */
		G gr;

		/**
		 * Was init called for this instance.
		 */
		boolean init = false;


		protected Chooser() {
		}
		
		/**
		 * Initialize the public key (generated by the sender).
		 * This method must be called before the first call to
		 * {@link #receive(int)}.
		 * @throws IOException
		 */
		public void init() throws IOException, InterruptedException {
			Message msg = toPeer.receive();
			C = grpEncoder.decode(msg.getInputStream());
			gr = grpEncoder.decode(msg.getInputStream());
			this.init = true;
		}

		/**
		 * Receive a value from the sender. {@link #init()} must
		 * be called before this method. The chooser
		 * can decide whether to receive the first value (idx=0)
		 * or the second (idx=1).
		 * @param idx either 0 or 1
		 * @return the value received
		 * @throws IOException
		 */
		public byte[] receive(int idx) throws IOException {
			assert init == true;

			// Choose the secret key (for generating PK_idx)
			BigInteger s = IntegerUtils.getRandomInteger(grp.orderUpperBound(), rand);
			G PK = grp.multiply(g, s);

			Message outmsg = toPeer.newMessage();
			OutputStream outbuf = outmsg.getOutputStream();

			if (idx == 0) {
				//PK0=PK, PK1=C-PK
				grpEncoder.encode(PK, outbuf);
			} else {
				// idx == 1
				// PK0=C-PK, PK1=PK
				G PK0 = grp.add(C, grp.negate(PK));
				grpEncoder.encode(PK0, outbuf);
			}
			toPeer.send(outmsg);

			Message inmsg = toPeer.receive();
			InputStream inbuf = inmsg.getInputStream();

			int R = EncodingUtils.decodeInt(inbuf);

			byte[][] ciph = { 
					EncodingUtils.decodeByteArray(inbuf),
					EncodingUtils.decodeByteArray(inbuf)
			};

			// PKr = (gs)r = (gr)s
			G PKr = grp.multiply(gr, s);

			// Compute h=H(PKr,R,idx)
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			grpEncoder.encode(PKr, buf);
			EncodingUtils.encode(R, buf);
			buf.write(idx);

			byte[] h = H.hash(buf.toByteArray(), ciph[idx].length);

			// Compute h=h XOR ciph[idx]
			for (int i = 0; i < h.length; ++i) {
				h[i] ^= ciph[idx][i];
			}

			return h;
		}
	}
	
	public Chooser newChooser() {
		return new Chooser();
	}

}
