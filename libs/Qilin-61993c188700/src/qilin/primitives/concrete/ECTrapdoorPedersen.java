package qilin.primitives.concrete;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.generic.TrapdoorPedersenCommitment;

/**
 * Elliptic-curve implementation of {@link TrapdoorPedersenCommitment}.
 * @author talm
 *
 */
public class ECTrapdoorPedersen extends TrapdoorPedersenCommitment<ECPoint> {
	public ECTrapdoorPedersen(ECGroup grp, BigInteger sk) {
		super(grp, sk);
	}
	
	/**
	 * Allow typesafe use of the group by returning an ECGroup rather than 
	 * a generic Group<ECPoint>. 
	 */
	@Override
	public ECGroup getGroup() {
		return (ECGroup) grp;
	}
}
