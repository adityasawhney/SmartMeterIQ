package qilin.primitives.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;

import qilin.primitives.CyclicGroup;
import qilin.util.ByteEncoder;
import qilin.util.EncodingUtils;
import qilin.util.IntegerUtils;
import qilin.util.StreamEncoder;


/**
 * The additive group of integers modulo n
 * @author talm
 *
 */
public class Zn implements CyclicGroup<BigInteger>, StreamEncoder<BigInteger>, ByteEncoder<BigInteger>  {
	BigInteger n;

	/**
	 * Create a new additive group of integers mod n
	 * @param n
	 */
	public Zn(BigInteger n) {
		this.n = n;
	}
	
	@Override
	public BigInteger add(BigInteger el, BigInteger el2) {
		return el.add(el2).mod(n);
	}

	@Override
	public BigInteger multiply(BigInteger g, BigInteger integer) {
		return g.multiply(integer);
	}

	@Override
	public BigInteger negate(BigInteger g) {
		return g.negate().mod(n);
	}

	@Override
	public BigInteger orderUpperBound() {
		return n;
	}

	@Override
	public BigInteger sample(Random rand) {
		return IntegerUtils.getRandomInteger(n, rand);
	}

	@Override
	public boolean contains(BigInteger g) {
		return (g.signum() >= 0) && (g.compareTo(n) < 0);
	}

	@Override
	public BigInteger zero() {
		return BigInteger.ZERO;
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

	@Override
	public BigInteger denseDecode(byte[] input) {
		BigInteger rand = new BigInteger(1, input);
		return rand.mod(n);
	}

	@Override
	public byte[] encode(BigInteger input) {
		return input.toByteArray();
	}

	@Override
	public int getMinLength() {
		// The number of bytes needed is the bitlength of the group
		// order (or a multiple of the order) divided by 8, rounded up. 
		int bitLen = n.bitLength();
		int byteLen = bitLen / 8 + (bitLen % 8 == 0 ? 0 : 1);
		return byteLen;
	}

	@Override
	public BigInteger getGenerator() {
		return BigInteger.ONE;
	}
}
