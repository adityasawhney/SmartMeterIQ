package qilin.primitives.concrete;

import java.math.BigInteger;

import qilin.primitives.generic.TrapdoorPedersenCommitment;

/**
 * A modular-arithmetic implementation of {@link TrapdoorPedersenCommitment}.
 * @author talm
 *
 */
public class ZpTrapdoorPedersen extends TrapdoorPedersenCommitment<BigInteger> {
	protected ZpTrapdoorPedersen(Zpsafe grp, BigInteger sk) {
		super(grp, sk);
	}
}
