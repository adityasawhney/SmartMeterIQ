package qilin.protocols.concrete;

import java.math.BigInteger;

import org.bouncycastle.math.ec.ECPoint;

import qilin.primitives.concrete.ECGroup;
import qilin.primitives.concrete.Zn;
import qilin.primitives.generic.PedersenCommitment;
import qilin.protocols.generic.TrapdoorBlumTwoPartyGroupElementFlip;

/**
 * Elliptic-curve implementation of the {@link TrapdoorBlumTwoPartyGroupElementFlip} using a {@link PedersenCommitment} as the
 * trapdoor commitment scheme.
 * @author talm
 *
 */
public class ECPedersenCoinflip extends TrapdoorBlumTwoPartyGroupElementFlip<BigInteger, ECPoint, BigInteger> {
	public ECPedersenCoinflip(ECGroup grp) {
		this(grp, new Zn(grp.orderUpperBound()));
	}
			
	/**
	 * Private constructor that gets the integer group as a parameter (used
	 * to bypass restriction that super must be first line of constructor).
	 * @param grp
	 * @param plainGroup
	 */
	private ECPedersenCoinflip(ECGroup grp, Zn plainGroup) {
		super(plainGroup, plainGroup, grp, plainGroup);
	}
}
