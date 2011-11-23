package edu.colorado.aos.crypto;

import java.math.BigInteger;
import java.util.Random;


/**
 * A modular-arithmetic version of {@link PedersenCommitment}.
 *
 */
public class ZpPedersen extends PedersenCommitment<BigInteger> {
	public ZpPedersen(Zpsafe grp, BigInteger h) {
		super(grp, h);
	}

	/**
	 * Generate a new private key.
	 * To get the corresponding public key, create an instance of the SK class and use getPK().
	 * @param grp
	 * @param rand
	 * @return a new private key.
	 */
	public static BigInteger generateKey(Zpsafe grp, Random rand) {
		BigInteger sk = IntegerUtils.getRandomInteger(grp.orderUpperBound(), rand);
		return sk;
	}


}
