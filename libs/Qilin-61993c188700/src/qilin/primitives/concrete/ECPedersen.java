package qilin.primitives.concrete;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.generic.PedersenCommitment;

/**
 * Elliptic-curve implementation of {@link PedersenCommitment}.
 * @author talm
 *
 */
public class ECPedersen extends PedersenCommitment<ECPoint> {

	public ECPedersen(ECGroup grp, ECPoint h) {
		super(grp, h);
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
