package qilin.protocols.generic;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;

import qilin.primitives.Homomorphic;
import qilin.primitives.NonInteractiveCommitment;
import qilin.primitives.StreamingRandomOracle;
import qilin.util.Pair;
import qilin.util.Triplet;



abstract public class TrapdoorHomomorphicCommitmentPoKFiatShamirTest<C, P, R,
Com  extends NonInteractiveCommitment.Committer<C, P, R> & Homomorphic<C, P, R>,
Ver extends NonInteractiveCommitment.Verifier<C, P, R> & Homomorphic<C, P, R>> extends 
HomomorphicCommitmentPoKFiatShamirTest<C, P, R, Com, Ver> {

	abstract protected TrapdoorHomomorphicCommitmentPoK<C, P, R, Com, Ver>.TrapdoorProver getTrapdoorProver();
	
	protected TrapdoorHomomorphicCommitmentPoKFiatShamirTest(Random rand) {
		super(rand);
	}

	@Test
	public void testTrapdoorProofs()  throws IOException, InterruptedException {
		final TrapdoorHomomorphicCommitmentPoK<C, P, R, Com, Ver>.TrapdoorProver trapdoorProver = getTrapdoorProver();
		final HomomorphicCommitmentPoK<C, P, R, Com, Ver>.Verifier verifier = getVerifier();
		final Com committer = getCommitter();
		
		final ArrayList<TestValues> tests = new ArrayList<TestValues>(CONFIDENCE);
		
		final P arbitrary = getCommitPlaintext();

		for (int i = 0; i < CONFIDENCE; ++i) {
			TestValues test = new TestValues();
			
			test.plain = getCommitPlaintext();
			test.random = getCommitRandom();
			test.commit = committer.commit(arbitrary, test.random);
			tests.add(test);
		}
		

		final StreamingRandomOracle proverOracle = getProverOracle();
		final StreamingRandomOracle verifierOracle = getVerifierOracle();

		final FiatShamir.Verifier<Pair<C,P>> fiatShamirVerifier = new FiatShamir.Verifier<Pair<C,P>>(verifier, verifierOracle); 
		final FiatShamir.Prover<Pair<C,P>, Triplet<C,P,R>> fiatShamirProver = new FiatShamir.Prover<Pair<C,P>, Triplet<C,P,R>>(trapdoorProver, verifier, proverOracle); 
		
		fiatShamirProver.setParameters(getProvertoVerifierChannel(), rand);
		fiatShamirProver.init();
		
		fiatShamirVerifier.setParameters(getVerifiertoProverChannel(), null);
		fiatShamirVerifier.init();
		

		for (TestValues test : tests) {
			fiatShamirProver.prove(new Triplet<C,P,R>(test.commit, test.plain, null));
		}

		for (TestValues test : tests) {
			boolean result = fiatShamirVerifier.verify(new Pair<C,P>(test.commit, test.plain));
			assertTrue(result);
		}
	}


}
