package qilin.protocols.concrete;


import java.io.IOException;
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
import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECGroupTest;
import qilin.protocols.OT1of2Test;
import qilin.protocols.OT1of2.Chooser;
import qilin.protocols.OT1of2.Sender;
import qilin.util.Pair;




@RunWith(Parameterized.class)
public class ECNaorPinkasOTTest extends OT1of2Test {
	Random rand;
	RandomOracle H;
	ECGroup grp;

	ECNaorPinkasOT ot;
	ECNaorPinkasOT.Chooser chooser;
	ECNaorPinkasOT.Sender  sender;
	Pair<Channel, Channel> channels;

	public ECNaorPinkasOTTest(ECGroup ecgrp, RandomOracle H) throws IOException {
		this.grp = ecgrp;
		this.H = H;

		LocalChannelFactory channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		ot = new ECNaorPinkasOT(H, grp);
		chooser = ot.newChooser();
		sender = ot.newSender();
	}

	@Parameters
	public static Collection<Object[]> getTestParams() {
		RandomOracle H = new DigestOracle();
		List<ECGroup> groups = ECGroupTest.getTestGroups();
		List<Object[]> params = new ArrayList<Object[]>(groups.size());
		for (ECGroup grp : groups) {
			Object[] param = {grp, H};
			params.add(param);
		}
		return params;
	}
	
	@Override
	protected Chooser getOTChooser() {
		assert chooser != null;
		return chooser;
	}

	@Override
	protected Sender getOTSender() {
		assert sender != null;
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
