package qilin.primitives.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;

import qilin.primitives.Group;
import qilin.util.ByteEncoder;
import qilin.util.EncodingUtils;
import qilin.util.IntegerUtils;
import qilin.util.StreamEncoder;


/**
 * Multiplicative group of integers mod p (p must be a prime) 
 * @author talm
 *
 */
public class Zpstar implements Group<BigInteger>, StreamEncoder<BigInteger>, ByteEncoder<BigInteger>  {
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
	@Override
	public BigInteger decode(InputStream in) throws IOException {
		byte[] encoded = EncodingUtils.decodeByteArray(in);
		return decode(encoded);
	}

	@Override
	public int encode(BigInteger g, OutputStream out) throws IOException {
		byte[] encoded = encode(g);
		return EncodingUtils.encode(encoded, out);
	}

	@Override
	public BigInteger decode(byte[] input) {
		return new BigInteger(input);
	}

	/**
	 * Returns a random integer chosen uniformly
	 * from 0 to 2^{n-1} mod p (where n is the bit length of p's
	 * two's-complement representation); when p is large enough,
	 * this is statistically close to a uniform group member.
	 */
	@Override
	public BigInteger denseDecode(byte[] input) {
		BigInteger rand = new BigInteger(1, input);
		return rand.mod(p);
	}

	@Override
	public byte[] encode(BigInteger input) {
		return input.toByteArray();
	}

	@Override
	public int getMinLength() {
		// The number of bytes needed is the bitlength of the group
		// order (or a multiple of the order) divided by 8, rounded up. 
		int bitLen = p.bitLength();
		int byteLen = bitLen / 8 + (bitLen % 8 == 0 ? 0 : 1);
		return byteLen;
	}
}
