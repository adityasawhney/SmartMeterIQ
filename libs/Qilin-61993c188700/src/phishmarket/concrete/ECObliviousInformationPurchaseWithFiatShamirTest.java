package phishmarket.concrete;

import java.util.Collection;
import java.util.Random;


import org.bouncycastle.math.ec.ECPoint;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import phishmarket.generic.ObliviousInformationPurchaseTest;
import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECGroupTest;
import qilin.util.Pair;




@RunWith(Parameterized.class)
public class ECObliviousInformationPurchaseWithFiatShamirTest extends ObliviousInformationPurchaseTest<ECPoint> {
	ECGroup grp;
	ECObliviousInformationPurchaseWithFiatShamir oip;
	ECObliviousInformationPurchaseWithFiatShamir.Seller seller;
	ECObliviousInformationPurchaseWithFiatShamir.Buyer buyer;
	Pair<Channel, Channel> channels;
	
	public ECObliviousInformationPurchaseWithFiatShamirTest(Random rand, ECGroup grp) {
		super(rand);
		this.grp = grp;
		oip = new ECObliviousInformationPurchaseWithFiatShamir(grp);
		
		LocalChannelFactory channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		seller = oip.newSeller();
		buyer = oip.newBuyer();
	}
	
	@Parameters
	public static Collection<Object[]> getTestParameters() {
		return ECGroupTest.getTestParams();
	}

	@Override
	protected ECObliviousInformationPurchaseWithFiatShamir.Buyer getBuyer() {
		return buyer;
	}

	@Override
	protected ECObliviousInformationPurchaseWithFiatShamir.Seller getSeller() {
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
