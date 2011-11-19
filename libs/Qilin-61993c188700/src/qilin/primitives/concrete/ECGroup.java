package qilin.primitives.concrete;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Random;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.CyclicGroup;
import qilin.util.ByteEncoder;
import qilin.util.EncodingUtils;
import qilin.util.IntegerUtils;
import qilin.util.StreamEncoder;

/**
 * An Elliptic-Curve group. Uses the <a href="http://www.bouncycastle.org/">BouncyCastle library</a> for
 * the low-level elliptic-curve operations. 
 * @author talm
 *
 */
public class ECGroup implements CyclicGroup<ECPoint>, ByteEncoder<ECPoint>, StreamEncoder<ECPoint> {
	ECParameterSpec curveParams;
	
	public ECGroup(ECParameterSpec curveParams) {
		this.curveParams = curveParams;
	}
	
	public ECGroup(String namedParams) throws IllegalArgumentException {
		this.curveParams = ECNamedCurveTable.getParameterSpec(namedParams);
		if (curveParams == null)
			throw new IllegalArgumentException("No such curve: " + namedParams);
	}
	
	public ECPoint getGenerator() {
		return curveParams.getG();
	}
	
	@Override
	public ECPoint add(ECPoint el, ECPoint el2) {
		return el.add(el2);
	}

	@Override
	public ECPoint multiply(ECPoint g, BigInteger integer) {
		return g.multiply(integer);
	}

	@Override
	public ECPoint negate(ECPoint g) {
		if (g.isInfinity())
			return g;
		return g.negate();
	}

	/**
	 * Return random point by multiplying base point with
	 * a random scalar (there are more efficient ways of doing this)
	 */
	@Override
	public ECPoint sample(Random rand) {
		return curveParams.getG().multiply(
				IntegerUtils.getRandomInteger(curveParams.getN(), rand));
	}

	@Override
	public BigInteger orderUpperBound() {
		return curveParams.getN();
	}

	/** 
	 * Check that y^2=x^3+Ax+B, where A and B are the curve parameters 
	 */
	@Override
	public boolean contains(ECPoint g) {
		if (g.isInfinity())
			return true;
		
		ECCurve curve = curveParams.getCurve();
		
		ECFieldElement A = curve.getA();
		ECFieldElement B = curve.getB();
		
		ECFieldElement X = g.getX();
		ECFieldElement Y = g.getY();
		
		ECFieldElement X3 = X.square().multiply(X);
		
		return Y.square().equals(X3.add(A.multiply(X)).add(B));
	}

	@Override
	public ECPoint zero() {
		return curveParams.getCurve().getInfinity();
	}

	public ECPoint decode(byte[] encoded) {
		return curveParams.getCurve().decodePoint(encoded);
	}
	
	@Override
	public ECPoint decode(InputStream in) throws IOException {
		byte[] encoded = EncodingUtils.decodeByteArray(in);
		return curveParams.getCurve().decodePoint(encoded);
	}

	public byte[] encode(ECPoint g) {
		return g.getEncoded();
	}
	
	@Override
	public int encode(ECPoint g, OutputStream out) throws IOException {
		byte[] encoded = g.getEncoded();
		return EncodingUtils.encode(encoded, out);
	}

	/**
	 * The dense decoding uses the input as a random integer
	 * and multiplies the group generator by that integer.
	 */
	@Override
	public ECPoint denseDecode(byte[] input) {
		BigInteger rand = new BigInteger(1, input);
		return getGenerator().multiply(rand);
	}

	@Override
	public int getMinLength() {
		// The number of bytes needed is the bitlength of the group
		// order (or a multiple of the order) divided by 8, rounded up. 
		int bitLen = curveParams.getN().bitLength();
		int byteLen = bitLen / 8 + (bitLen % 8 == 0 ? 0 : 1);
		return byteLen;
	}
}
