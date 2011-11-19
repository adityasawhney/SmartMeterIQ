package qilin.primitives;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Random;

import org.junit.Test;

import qilin.util.ByteEncoder;
import qilin.util.GenericsUtils;
import qilin.util.GlobalTestParams;


/**
 * Test class for generic group
 * Actual tests should extend this class.
 * @author talm
 *
 * @param <G>
 */
public abstract class GroupTest<G> implements GlobalTestParams {
	
	abstract protected Group<G> getGroup();
	abstract protected ByteEncoder<G> getEncoder();
	abstract protected Random getRand();
	
	/**
	 * Check that the order of the group divides orderUpperBound() 
	 */
	@Test
	public void testOrder() {
		Group<G> g = getGroup();
		Random rand = getRand();
		BigInteger ord = g.orderUpperBound();
		G zero = g.zero();
		for (int i = 0; i < CONFIDENCE; ++i) {
			G e = g.sample(rand);
			G test = g.multiply(e, ord);
			assertTrue("Order of group does not divide upper bound", GenericsUtils.deepEquals(test, zero));
		}
	}
	
	/**
	 * Check that random returns a value in the group,
	 * and that the number of different values it returns
	 * is at least 1/4 the expected number. Note 
	 * that this test assumes orderUpperBound will return at most
	 * twice the actual upper bound.
	 */
	@Test
	public void testRandom() {
		Group<G> g = getGroup();
		Random rand = getRand();
		HashSet<G> set = new HashSet<G>();
		float expSize = 0;
		float groupOrder = g.orderUpperBound().floatValue(); 
		for (int i = 0; i < CONFIDENCE; ++i) {
			expSize += 1 - set.size()/groupOrder;
			G e = g.sample(rand);
			assertTrue("Random element isn't member of group", g.contains(e));
			set.add(e);
		}
		
		assertTrue("Random set is too small", set.size() > expSize / 4);
	}
	
	/**
	 * Check that random elements of the group are associative with
	 * respect to addition.
	 */
	@Test
	public void testAssociativeness() {
		Group<G> g = getGroup();
		Random rand = getRand();
		for (int i = 0; i < CONFIDENCE; ++i) {
			G e1 = g.sample(rand);
			//assertTrue("Random element isn't member of group", g.contains(e1));
			G e2 = g.sample(rand);
			//assertTrue("Random element isn't member of group", g.contains(e2));
			G e3 = g.sample(rand);
			//assertTrue("Random element isn't member of group", g.contains(e3));
			
			assertTrue("Random elements aren't associative", g.add(g.add(e1, e2),e3).equals(g.add(e1, g.add(e2, e3))));
		}
	}
	
	/**
	 * Check that the neutral element of the group obeys axioms.
	 */
	@Test
	public void testZero() {
		Group<G> g = getGroup();
		Random rand = getRand();
		G zero = g.zero();
		
		assertTrue("-0 != 0", GenericsUtils.deepEquals(zero, g.negate(zero)));
		
		for (int i = 0; i < CONFIDENCE; ++i) {
			G e1 = g.sample(rand);
			//assertTrue("Random element isn't member of group", g.contains(e1));
			assertTrue("r+0 != r", g.add(e1, zero).equals(e1));
			assertTrue("0+r != r", g.add(zero, e1).equals(e1));
			assertTrue("r+(-r) != 0", g.add(e1, g.negate(e1)).equals(zero));
			assertTrue("(-r)+r != 0", g.add(g.negate(e1), e1).equals(zero));
		}
	}
	

	/**
	 * Check distributivity of multiplication.
	 */
	@Test
	public void testMultiply() {
		Group<G> g = getGroup();
		Random rand = getRand();
		BigInteger TWO = new BigInteger("2");
		int maxBits = g.orderUpperBound().bitLength();
		for (int i = 0; i < CONFIDENCE; ++i) {
			G e1 = g.sample(rand);
			//assertTrue("Random element isn't member of group", g.contains(e1));
			assertTrue("r+r != 2r", g.add(e1, e1).equals(g.multiply(e1, TWO)));
			BigInteger a = new BigInteger(maxBits, rand);
			BigInteger b = new BigInteger(maxBits, rand);
			assertTrue("ar+br != (a+b)r", 
					g.add(g.multiply(e1, a), g.multiply(e1, b)).equals(g.multiply(e1, a.add(b))));
		}
	}
	
	/** 
	 * Check that random elements have inverses.
	 */
	 @Test
	 public void testInverse() {
		 Group<G> g = getGroup();
		 Random rand = getRand();
		 final G zero = g.zero();
		 for (int i = 0; i < CONFIDENCE; ++i) {
			 G e1 = g.sample(rand);
			 G e2 = g.negate(e1);
			 
			 //assertTrue("Random element isn't member of group", g.contains(e1));
			 assertTrue("r+ (-r) != 0", g.add(e1, e2).equals(zero));
		 }
	 }
}
