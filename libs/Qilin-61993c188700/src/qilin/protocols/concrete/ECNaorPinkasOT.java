package qilin.protocols.concrete;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.RandomOracle;
import qilin.primitives.concrete.ECGroup;
import qilin.protocols.generic.NaorPinkasOT;


/**
 * Elliptic-curve implementation of Naor-Pinkas OT.
 * @author talm
 * @see ZpNaorPinkasOT
 *
 */
public class ECNaorPinkasOT extends NaorPinkasOT<ECPoint> {
	public ECNaorPinkasOT(RandomOracle H, ECGroup grp) {
		super(grp, H, grp);
	}
}
