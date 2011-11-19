package qilin.protocols.concrete;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Random;

import org.bouncycastle.math.ec.ECPoint;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import qilin.comm.Channel;
import qilin.comm.LocalChannelFactory;
import qilin.primitives.StreamingRandomOracle;
import qilin.primitives.concrete.DigestOracle;
import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.ECGroupTest;
import qilin.primitives.concrete.ECPedersen;
import qilin.primitives.concrete.ECTrapdoorPedersen;
import qilin.primitives.generic.PedersenCommitment;
import qilin.primitives.generic.TrapdoorPedersenCommitment;
import qilin.protocols.generic.HomomorphicCommitmentPoK;
import qilin.protocols.generic.TrapdoorHomomorphicCommitmentPoK;
import qilin.protocols.generic.TrapdoorHomomorphicCommitmentPoKFiatShamirTest;
import qilin.util.Pair;


@RunWith(Parameterized.class)
public class ECPedersenCommitmentPoKFiatShamirTest extends 
TrapdoorHomomorphicCommitmentPoKFiatShamirTest<ECPoint, BigInteger, BigInteger, PedersenCommitment<ECPoint>, PedersenCommitment<ECPoint>> {
	ECPedersenCommitmentPoK commitPoK;
	ECPedersenCommitmentPoK verifyPoK;
	ECTrapdoorPedersen trapdoorPedersen;
	ECPedersen pedersen;
	Pair<Channel, Channel> channels;
	DigestOracle proverOracle;
	DigestOracle verifierOracle;
	
	
	
	public ECPedersenCommitmentPoKFiatShamirTest(Random rand, ECGroup grp) {
		super(rand);
	
		trapdoorPedersen = new ECTrapdoorPedersen(grp, TrapdoorPedersenCommitment.generateKey(grp, rand));
		pedersen = new ECPedersen(grp, trapdoorPedersen.getH());
		
		LocalChannelFactory channelFactory = new LocalChannelFactory();
		channels = channelFactory.getChannelPair();
		
		commitPoK = new ECPedersenCommitmentPoK(trapdoorPedersen);
		verifyPoK = new ECPedersenCommitmentPoK(pedersen);
		
		proverOracle = new DigestOracle();
		verifierOracle = new DigestOracle();
	}
	
	@Parameters
	public static Collection<Object[]> getTestParameters() {
		return ECGroupTest.getTestParams();
	}
	
	@Override
	protected TrapdoorHomomorphicCommitmentPoK<ECPoint,BigInteger,BigInteger,PedersenCommitment<ECPoint>,PedersenCommitment<ECPoint>>.TrapdoorProver getTrapdoorProver() {
		return commitPoK.getTrapdoorProver();
	}

	@Override
	protected BigInteger getCommitPlaintext() {
		return pedersen.getRandom(rand);
	}

	@Override
	protected BigInteger getCommitRandom() {
		return pedersen.getRandom(rand);
	}

	@Override
	protected PedersenCommitment<ECPoint> getCommitter() {
		return pedersen;
	}

	@Override
	protected HomomorphicCommitmentPoK<ECPoint,BigInteger,BigInteger,PedersenCommitment<ECPoint>,PedersenCommitment<ECPoint>>.Prover getProver() {
		return getTrapdoorProver();
	}

	@Override
	protected HomomorphicCommitmentPoK<ECPoint,BigInteger,BigInteger,PedersenCommitment<ECPoint>,PedersenCommitment<ECPoint>>.Verifier getVerifier() {
		return verifyPoK.newVerifier();
	}

	@Override
	protected BigInteger getPlaintextGroupOrder() {
		return pedersen.getGroup().orderUpperBound();
	}

	@Override
	protected Channel getProvertoVerifierChannel() {
		return channels.a;
	}

	@Override
	protected Channel getVerifiertoProverChannel() {
		return channels.b;
	}

	@Override
	protected StreamingRandomOracle getProverOracle() {
		return proverOracle;
	}

	@Override
	protected StreamingRandomOracle getVerifierOracle() {
		return verifierOracle;
	}
}
