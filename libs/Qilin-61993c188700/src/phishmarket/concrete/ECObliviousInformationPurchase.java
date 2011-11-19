package phishmarket.concrete;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.bouncycastle.math.ec.ECPoint;

import phishmarket.generic.ObliviousInformationPurchase;
import qilin.primitives.concrete.DigestOracle;
import qilin.primitives.concrete.ECGroup;
import qilin.util.Pair;

public class ECObliviousInformationPurchase extends ObliviousInformationPurchase<ECPoint> {
	public ECObliviousInformationPurchase(ECGroup grp) {
		super(grp, grp, grp, new DigestOracle());
	}

	public ECObliviousInformationPurchase(ECGroup grp, Map<byte[], Pair<ECPoint,BigInteger>> commitCache) {
		super(grp, grp, grp, new DigestOracle(), commitCache);
	}

	public ECObliviousInformationPurchase(ECGroup grp, String digestAlgorithm) throws NoSuchAlgorithmException {
		super(grp, grp, grp, new DigestOracle(digestAlgorithm));
	}
	
	public ECObliviousInformationPurchase(ECGroup grp, String digestAlgorithm, Map<byte[], Pair<ECPoint,BigInteger>> commitCache) throws NoSuchAlgorithmException {
		super(grp, grp, grp, new DigestOracle(digestAlgorithm), commitCache);
	}
}
