package qilin.protocols.concrete;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Random;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECGroupTest;
import qilin.primitives.concrete.ECPedersen;
import qilin.primitives.concrete.ECTrapdoorPedersen;
import qilin.primitives.concrete.Zn;
import qilin.primitives.generic.TrapdoorPedersenCommitment;
import qilin.protocols.TrapdoorTwoPartyGroupElementFlipTest;
import qilin.protocols.TrapdoorTwoPartyGroupElementFlip.TrapdoorFirst;
import qilin.protocols.TwoPartyGroupElementFlip.First;
import qilin.protocols.TwoPartyGroupElementFlip.Second;
import qilin.util.Pair;

@RunWith(Parameterized.class)
public class ECPedersenCoinflipTest extends TrapdoorTwoPartyGroupElementFlipTest<BigInteger> {
	ECGroup ecgrp;
	BigInteger sk;
	ECPedersenCoinflip flipper;
	ECTrapdoorPedersen trapdoorPedersen;
	ECPedersen standardPedersen;
	Channel firstChannel;
	Channel secondChannel;
	
	public ECPedersenCoinflipTest(Random rand, ECGroup ecgrp) {
		super(rand, new Zn(ecgrp.orderUpperBound())); 
		this.ecgrp = ecgrp;
		sk = TrapdoorPedersenCommitment.generateKey(ecgrp, rand);
		trapdoorPedersen = new ECTrapdoorPedersen(ecgrp, sk);
		standardPedersen = new ECPedersen(ecgrp, trapdoorPedersen.getH());
		
		LocalChannelFactory channelFactory = new LocalChannelFactory();
		Pair<Channel, Channel> channels = channelFactory.getChannelPair();
		firstChannel = channels.a;
		secondChannel = channels.b;
		
		flipper = new ECPedersenCoinflip(ecgrp);
	}

	@Parameters
	public static Collection<Object[]> getTestParams() {
		return ECGroupTest.getTestParams();
	}
	
	@Override
	protected TrapdoorFirst<BigInteger> getTrapdoorFirst() {
		return flipper.newTrapdoorFirst(trapdoorPedersen);
	}

	@Override
	protected First<BigInteger> getFirst() {
		return flipper.newFirst(standardPedersen);
	}

	@Override
	protected Second<BigInteger> getSecond() {
		return flipper.newSecond(standardPedersen);
	}

	@Override
	protected Channel getFirsttoSecondChannel() {
		return firstChannel;
	}

	@Override
	protected Channel getSecondtoFirstChannel() {
		return secondChannel;
	}
}
