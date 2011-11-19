package phishmarket.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import qilin.comm.Channel;
import qilin.protocols.CheatingPeerException;
import qilin.util.GenericsUtils;
import qilin.util.GlobalTestParams;
import qilin.util.Pair;



abstract public class ObliviousInformationPurchaseTest<G> implements GlobalTestParams {
	final static int DATABASE_SIZE = 100;
	final static int URL_MINLEN = 10;
	final static int URL_MAXLEN = 200;
	
	abstract protected ObliviousInformationPurchase<G>.Seller getSeller();
	abstract protected Channel getSellertoBuyerChannel();
	abstract protected ObliviousInformationPurchase<G>.Buyer getBuyer();
	abstract protected Channel getBuyertoSellerChannel();

	protected Random rand;

	public ObliviousInformationPurchaseTest(Random rand) {
		this.rand = rand;
	}

	@Test
	public void testProtocol() throws IOException, InterruptedException {
		final ObliviousInformationPurchase<G>.Seller seller = getSeller();
		final ObliviousInformationPurchase<G>.Buyer buyer = getBuyer();
		
		final List<Pair<byte[],byte[]>> urls = new ArrayList<Pair<byte[],byte[]>>(DATABASE_SIZE);
		
		
		final ObliviousInformationPurchase.BuyerInterest parityInterest = new ObliviousInformationPurchase.BuyerInterest() {
			@Override
			public boolean isInterested(byte[] tag) {
				return (tag[0] & 1) == 1;
			}
			
		};
		
		for (int i = 0; i < DATABASE_SIZE; ++i) {
			int urlLen = rand.nextInt(URL_MAXLEN - URL_MINLEN) + URL_MINLEN;
			byte[] url = new byte[urlLen];
			rand.nextBytes(url);
			
			byte[] tag = { url[0] };
			
			urls.add(new Pair<byte[], byte[]>(url, tag));
		}
		
		
		Runnable sellerRunner = new Runnable() {
			@Override
			public void run() {
				seller.setParameters(getSellertoBuyerChannel(), rand);
				try {
					seller.init();
					
					for (Pair<byte[],byte[]> url : urls) {
						seller.sell(url.a, url.b);
					}
					
					seller.getPaymentValue();
				} catch (CheatingPeerException cpe) {
					assert false;
					cpe.printStackTrace();
					throw new Error("Cheating Peer: " + cpe.getMessage());
				} catch (IOException ioe) {
					assert false;
					ioe.printStackTrace();
					throw new Error("I/O Exception: " + ioe.getMessage());
				} catch (InterruptedException ie) {
					assert false;
					ie.printStackTrace();
					throw new Error("Interrupted Exception: " + ie.getMessage());
				}
			}
		};

		Thread secondThread = new Thread(sellerRunner, "Seller");
		secondThread.start();

		buyer.setParameters(getBuyertoSellerChannel(), rand);
		buyer.init();

		// Add approx. half of "interesting" urls to database in advance 
		for (Pair<byte[],byte[]> url : urls) {
			if (parityInterest.isInterested(url.b)) {
				if ((url.b[0] & 2) != 0)
					buyer.addInfo(url.a);
			}
		}
		
		int countPayment = 0;

		for (Pair<byte[],byte[]> url : urls) {
			ObliviousInformationPurchase.BuyerInfo buyerInfo;
			buyerInfo = buyer.buy(parityInterest);
			
			if (parityInterest.isInterested(url.b)) {
				// Make sure we received the correct URL
				assertTrue(GenericsUtils.deepEquals(url.a, buyerInfo.info));
				
				// Make sure we paid iff we didn't have it already.
				if ((url.b[0] & 2) != 0)
					assertFalse(buyerInfo.didPay);
				else {
					++countPayment;
					assertTrue(buyerInfo.didPay);
				}
			} else {
				assertNull(buyerInfo.info);
				assertFalse(buyerInfo.didPay);
			}
		}
		
		assertEquals(BigInteger.valueOf(countPayment), buyer.getPaymentValue());

		buyer.provePaymentValue();
		
		secondThread.join();
	}
}
