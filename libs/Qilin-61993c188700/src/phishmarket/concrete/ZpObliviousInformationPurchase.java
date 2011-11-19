package phishmarket.concrete;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import phishmarket.generic.ObliviousInformationPurchase;
import qilin.primitives.concrete.DigestOracle;
import qilin.primitives.concrete.Zpsafe;

public class ZpObliviousInformationPurchase extends ObliviousInformationPurchase<BigInteger> {
	public ZpObliviousInformationPurchase(Random rand, Zpsafe grp) {
		super(grp, grp, grp, new DigestOracle());
	}
	
	public ZpObliviousInformationPurchase(Random rand, Zpsafe grp, String digestAlgorithm) throws NoSuchAlgorithmException {
		super(grp, grp, grp, new DigestOracle(digestAlgorithm));
	}
}
