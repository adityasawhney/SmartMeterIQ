package qilin.protocols.concrete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.primitives.RandomOracle;
import qilin.primitives.concrete.DigestOracle;
import qilin.primitives.concrete.Zpsafe;
import qilin.protocols.OT1of2Test;
import qilin.protocols.OT1of2.Chooser;
import qilin.protocols.OT1of2.Sender;
import qilin.util.Pair;





@RunWith(Parameterized.class)
public class ZpNaorPinkasOTTest extends OT1of2Test {
	public final static int[] BITS = {8, 64, 256}; 

	Random rand;
	RandomOracle H;
	LocalChannelFactory channelFactory;
	Zpsafe grp;

	ZpNaorPinkasOT.Chooser chooser;
	ZpNaorPinkasOT.Sender  sender;
	Pair<Channel, Channel> channels;
	
	
	public ZpNaorPinkasOTTest(Random rand, Zpsafe grp, RandomOracle H) {
		this.rand = rand;
		this.grp = grp;
		this.H = H;
		channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		ZpNaorPinkasOT ot = new ZpNaorPinkasOT(H, grp);
		chooser = ot.newChooser();
		sender = ot.newSender();
	}

	@Parameters
	public static Collection<Object[]>  getTestParameters() {
		Random rand = new Random(1);
		RandomOracle H = new DigestOracle();
		List<Object[]> params = new ArrayList<Object[]>(BITS.length);
		for (int bits : BITS) {
			Zpsafe grp = new Zpsafe(Zpsafe.randomSafePrime(bits, 50, rand));
			Object[] param = {rand, grp, H};
			params.add(param);
		}
		return params;
	}

	@Override
	protected Chooser getOTChooser() {
		return chooser;
	}

	@Override
	protected Sender getOTSender() {
		return sender;
	}

	@Override
	protected Channel getChoosertoSenderChannel() {
		return channels.a;
	}

	@Override
	protected Channel getSendertoChooserChannel() {
		return channels.b;
	}

}
