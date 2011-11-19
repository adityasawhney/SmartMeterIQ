package qilin.protocols.concrete;

import java.math.BigInteger;

import qilin.primitives.RandomOracle;
import qilin.primitives.concrete.Zpsafe;
import qilin.protocols.generic.NaorPinkasOT;

/**
 * A modular-arithmetic implementation of Naor-Pinkas OT.
 * 
 * @author talm
 * @see ECNaorPinkasOT
  */
public class ZpNaorPinkasOT extends NaorPinkasOT<BigInteger> {
	public ZpNaorPinkasOT(RandomOracle H, Zpsafe grp) {
		super(grp, H, grp);
	}
}
