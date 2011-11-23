package edu.colorado.aos.crypto;

import java.math.BigInteger;
import java.util.Random;



/**
 * Multiplicative group of integers mod p (p must be a prime) 
 *
 */
public class Zpstar implements Group<BigInteger> {
	BigInteger p;
	
	/**
	 * Construct the multiplicative group of integers mod p.
	 * 
	 * @param p must be a prime (this is not verified by the code)
	 */
	public Zpstar(BigInteger p) {
		this.p = p;
	}

	/**
	 * Group "add" operation is multiplication 
	 */
	@Override
	public BigInteger add(BigInteger el, BigInteger el2) {
		return el.multiply(el2).mod(p);
	}

	/**
	 * Group "multiply" operation is modular exponentiation 
	 */
	@Override
	public BigInteger multiply(BigInteger g, BigInteger integer) {
		return g.modPow(integer, p);
	}

	@Override
	public BigInteger negate(BigInteger g) {
		return g.modInverse(p);
	}

	@Override
	public BigInteger sample(Random rand) {
		// Return a random integer between 1 and p-1.
		return IntegerUtils.getRandomInteger(p.subtract(BigInteger.ONE), rand).add(BigInteger.ONE);
	}

	@Override
	public BigInteger orderUpperBound() {
		return p.subtract(BigInteger.ONE);
	}

	@Override
	public boolean contains(BigInteger g) {
		return (g.signum() > 0) && (g.compareTo(p) < 0);
	}

	@Override
	public BigInteger zero() {
		return BigInteger.ONE;
	}
}
