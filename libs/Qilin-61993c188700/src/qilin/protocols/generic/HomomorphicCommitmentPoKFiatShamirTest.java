package qilin.protocols.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import qilin.comm.Channel;
import qilin.primitives.Homomorphic;
import qilin.primitives.NonInteractiveCommitment;
import qilin.primitives.StreamingRandomOracle;
import qilin.util.GenericsUtils;
import qilin.util.GlobalTestParams;
import qilin.util.Pair;
import qilin.util.Triplet;


abstract public class HomomorphicCommitmentPoKFiatShamirTest<C, P, R,
Com  extends NonInteractiveCommitment.Committer<C, P, R> & Homomorphic<C, P, R>,
Ver extends NonInteractiveCommitment.Verifier<C, P, R> & Homomorphic<C, P, R>> implements GlobalTestParams {
	protected Random rand;
	
	abstract protected HomomorphicCommitmentPoK<C, P, R, Com, Ver>.Prover getProver();
	abstract protected Channel getProvertoVerifierChannel();
	abstract protected Com getCommitter();
	abstract protected HomomorphicCommitmentPoK<C, P, R, Com, Ver>.Verifier getVerifier();
	abstract protected Channel getVerifiertoProverChannel();
	abstract protected P getCommitPlaintext();
	abstract protected R getCommitRandom();
	abstract protected BigInteger getPlaintextGroupOrder();
	abstract protected StreamingRandomOracle getProverOracle();
	abstract protected StreamingRandomOracle getVerifierOracle();
	
	protected HomomorphicCommitmentPoKFiatShamirTest(Random rand) {
		this.rand = rand;
	}
	
	class TestValues {
		P plain;
		R random;
		C commit;
	};
	
	class TestResults {
		int correctlyVerifiedProofs;
		int incorrectlyVerifiedProofs;
	}
	
	@Test
	public void testProofs()  throws IOException, InterruptedException {
		final HomomorphicCommitmentPoK<C, P, R, Com, Ver>.Prover prover = getProver();
		final HomomorphicCommitmentPoK<C, P, R, Com, Ver>.Verifier verifier = getVerifier();
		final Com committer = getCommitter();
		
		final ArrayList<TestValues> tests = new ArrayList<TestValues>(CONFIDENCE);

		for (int i = 0; i < CONFIDENCE; ++i) {
			TestValues test = new TestValues();
			
			test.plain = getCommitPlaintext();
			test.random = getCommitRandom();
			test.commit = committer.commit(test.plain, test.random);
			tests.add(test);
		}
		
		final TestResults testResults = new TestResults();
		
		final StreamingRandomOracle proverOracle = getProverOracle();
		final StreamingRandomOracle verifierOracle = getVerifierOracle();

		final FiatShamir.Verifier<Pair<C,P>> fiatShamirVerifier = new FiatShamir.Verifier<Pair<C,P>>(verifier, verifierOracle); 
		final FiatShamir.Prover<Pair<C,P>, Triplet<C,P,R>> fiatShamirProver = new FiatShamir.Prover<Pair<C,P>, Triplet<C,P,R>>(prover, verifier, proverOracle); 
		
		fiatShamirProver.setParameters(getProvertoVerifierChannel(), rand);
		fiatShamirProver.init();
		
		fiatShamirVerifier.setParameters(getVerifiertoProverChannel(), null);
		fiatShamirVerifier.init();

		for (TestValues test : tests) {
			Triplet<C,P,R> params =  new Triplet<C,P,R>(test.commit, test.plain, test.random);
			fiatShamirProver.prove(params);
			fiatShamirProver.prove(params);
		}
		
		for (TestValues test : tests) {
			// Test completeness
			boolean result = fiatShamirVerifier.verify(new Pair<C,P>(test.commit, test.plain));
			if (result)
				++testResults.correctlyVerifiedProofs;
			
			// Test soundness
			P badPlain = getCommitPlaintext();
			result = fiatShamirVerifier.verify(new Pair<C,P>(test.commit, badPlain));
			// If badPlain != test.plain then verification should
			// fail except with small probability
			if (result && !GenericsUtils.deepEquals(badPlain, test.plain))
				++testResults.incorrectlyVerifiedProofs;
		}
		
		assertEquals(tests.size(), testResults.correctlyVerifiedProofs);
		// We expect soundness test to fail with probability 1/group order
		float expectedSoundness = tests.size() / getPlaintextGroupOrder().floatValue();
		assertTrue("Expected " + expectedSoundness + " but got: " + testResults.incorrectlyVerifiedProofs,
				testResults.incorrectlyVerifiedProofs <= 4 * expectedSoundness);
	}
}
