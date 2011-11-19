package phishmarket.concrete;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import phishmarket.generic.ObliviousInformationPurchaseTest;
import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.primitives.concrete.Zpsafe;
import qilin.util.Pair;




@RunWith(Parameterized.class)
public class ZpObliviousInformationPurchaseTest  extends ObliviousInformationPurchaseTest<BigInteger> {
	public final static int[] BITS = {4, 8, 64, 256, 2048};
	
	ZpObliviousInformationPurchase oip;
	ZpObliviousInformationPurchase.Seller seller;
	ZpObliviousInformationPurchase.Buyer buyer;
	Pair<Channel, Channel> channels;

	public ZpObliviousInformationPurchaseTest(Random rand, Zpsafe grp) {
		super(rand);
		oip = new ZpObliviousInformationPurchase(rand, grp);
		
		LocalChannelFactory channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		seller = oip.newSeller();
		buyer = oip.newBuyer();
	}
	
	@Parameters
	public static Collection<Object[]> getTestParameters() {
		Random rand = new Random(1);
		List<Object[]> params = new ArrayList<Object[]>(BITS.length);
		for (int bits : BITS) {
			Zpsafe grp = new Zpsafe(Zpsafe.randomSafePrime(bits, 50, rand));
			Object[] param = {rand, grp};
			params.add(param);
		}
		return params;
	}

	@Override
	protected ZpObliviousInformationPurchase.Buyer getBuyer() {
		return buyer;
	}

	@Override
	protected ZpObliviousInformationPurchase.Seller getSeller() {
		return seller;
	}

	@Override
	protected Channel getBuyertoSellerChannel() {
		return channels.a;
	}

	@Override
	protected Channel getSellertoBuyerChannel() {
		return channels.b;
	}
}
