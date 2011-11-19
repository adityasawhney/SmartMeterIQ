package phishmarket.concrete;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.bouncycastle.math.ec.ECPoint;

import phishmarket.generic.ObliviousInformationPurchaseWithFiatShamir;
import qilin.primitives.concrete.DigestOracle;
import qilin.primitives.concrete.ECGroup;
import qilin.util.Pair;

public class ECObliviousInformationPurchaseWithFiatShamir extends ObliviousInformationPurchaseWithFiatShamir<ECPoint> {
	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp, DigestOracle H) { 
		super(grp, grp, grp, H, H);
	}

	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp, DigestOracle H, Map<byte[], Pair<ECPoint,BigInteger>> commitCache) {
		super(grp, grp, grp, H, H, commitCache);
	}

	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp) {
		this(grp, new DigestOracle());
	}
	
	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp, String digestAlgorithm) throws NoSuchAlgorithmException {
		this(grp, new DigestOracle(digestAlgorithm));
	}

	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp, Map<byte[], Pair<ECPoint,BigInteger>> commitCache) {
		this(grp, new DigestOracle(), commitCache);
	}
	
	public ECObliviousInformationPurchaseWithFiatShamir(ECGroup grp, String digestAlgorithm, Map<byte[], Pair<ECPoint,BigInteger>> commitCache) throws NoSuchAlgorithmException {
		this(grp, new DigestOracle(digestAlgorithm), commitCache);
	}
}
