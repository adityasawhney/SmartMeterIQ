// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2008, 2009
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of
// the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// General Public License in file COPYING in this or one of the
// parent directories for more details.
// 
// Created 13.1.09 by Hendrik
// 
// bignat package doc
// 
// $Id: package-info.java,v 1.10 2009-06-19 20:37:35 tews Exp $



// <a href="../bignat/package-summary.html#montgomery_factor">Montgomery multiplication </a>


/** 
 * Big integer library for Java Card, including a test frame.
 *
 * <H3>Warning</H3>
 *
 * <strong>Depending on the configuration some types might be
 *   different from what is shown in the documentation here.
 *   Especially some method parameters of type short might have type
 *   long, because they are declared as DOUBLE_DIGIT_TYPE. <P>
 *
 *   The source code in this package cannot be used without
 *   the cpp preprocessor, see below.
 * </strong>
 *   
 *
 * <H3>Overview</H3>
 *
 * This package implements an allocation free big natural number
 * library for Java Cards. Allocation free means that only the
 * constructors allocate on the heap. Normal methods only
 * allocate on the stack, never on the heap. The package only
 * implements non-negative natural numbers, we refer to them as
 * big naturals.
 * <P>
 *
 * The allocation constraint implies that (in contrast to {@link
 * java.math.BigInteger}) objects are mutable and that the big numbers
 * are fixed in size. The (maximal) size of a big natural is
 * determined by an constructor argument. In case of an overflow an
 * assertion is thrown (compare {@link ds.ov2.util.Misc#myassert}).
 * Further, because of the allocation constraint some methods require
 * additional arguments that are only used as temporaries in
 * computations in side the method. <P>
 * 
 * Features have been added to this package on a by-need basis.
 * Therefore this package supports much less operations than a
 * normal BigInteger class. However, there is also support for
 * computing multi exponents that I have never seen in any other
 * BigInteger class. 
 * <P>
 *
 * Besides the classes that form the big natural library ({@link
 * ds.ov2.bignat.Bignat}, {@link ds.ov2.bignat.Bignat_array}, {@link
 * ds.ov2.bignat.Inverse_mod_256}, {@link ds.ov2.bignat.Modulus},
 * {@link ds.ov2.bignat.RSA_exponent} and {@link
 * ds.ov2.bignat.Vector}) this package contains also support code for
 * test frames ({@link ds.ov2.bignat.Resize}, {@link
 * ds.ov2.bignat.Fake_rsa_exponent}), the test frame itself ({@link
 * ds.ov2.bignat.Testbignat}) and support code for host applications
 * ({@link ds.ov2.bignat.APDU_BigInteger}, {@link
 * ds.ov2.bignat.Host_modulus}, {@link ds.ov2.bignat.Host_vector}).
 * <P>
 *
 * Standard Java Card does not support int and long. Therefore
 * this package normally only uses byte and short for all
 * computations (of course the test frame and the host support
 * code do not obey this restriction). However, it is possible to
 * configure this package to perform all computations with int
 * and long. For Java Cards with int support it should be
 * relatively easy to add a configuration that uses short/int.
 * <P>
 *
 * Standard Java maintains the wrong illusion of the absolut absense
 * of the just described architectural differences. Therefore it is
 * absolutely impossible to express architecture specific changes such
 * as turning a local variable of type byte into type int within the
 * Java language. [As a corollary of the wrong assumption that there
 * are no architectural differences we obtain that Java is completely
 * unsuitable for programming Java Cards.] This package solves the
 * architecture configuration problem by using <a target="_top"
 * href="http://en.wikipedia.org/wiki/C_preprocessor">cpp</a> macros.
 * For example, many variables in the source code are declared as
 * DIGIT_TYPE, where a preprocessor run expands DIGIT_TYPE to either
 * byte or int, depending on the configuration. <P>
 *
 * <strong>The source code in this package cannot be used without
 * the cpp preprocessor.</strong>
 *
 * <H3>Operations</H3>
 *
 * This class provides addition, subtraction, 
 * modular multiplication (in three
 * versions), and division and remainder. Multi-exponent is provided
 * by {@link ds.ov2.bignat.Vector#exponent_mod Vector.exponent_mod}
 * and {@link ds.ov2.bignat.Vector#mont_rsa_exponent_mod
 * Vector.mont_rsa_exponent_mod}. {@link ds.ov2.bignat.RSA_exponent}
 * computes a (single) exponent by abusing RSA public key encryption.
 * <P>
 *
 * <a name="montgomery_factor"></a>
 * <H3>Montgomery factor, montgomerization and Montgomery multiplication</H3>
 *
 * Montgomery multiplication produces intermediate results that
 * are up to two digits longer than the arguments. Therefore the
 * first two digits of each factor and of the modulus must be
 * zero to ensure no overflow can occur. The maximal modulus for 
 * montgomery multiplication for a given {@code size} is therefore
 * 2^({@link ds.ov2.bignat.Bignat#digit_len} * ({@code size} - 2)) - 1.
 * <P>
 * 
 * <STRONG>When using Montgomery multiplication, all numbers must be
 * two digits longer than their maximal value. These two additional
 * <U><I>Montgomery digits</I></U> are assumed to be always zero. 
 * </STRONG>
 * <P>
 *
 * At some places the Montgomery digits are hardwired. At other places
 * (for example in {@link ds.ov2.bignat.RSA_exponent}) one has to
 * specify the number of Montgomery digits in an additional argument.
 *
 * For a given modulus {@code mod} and a number size {@code size} the
 * <EM><U>montgomery factor</U></EM> {@code mont_fac} is defined as 2^({@link
 * ds.ov2.bignat.Bignat#digit_len} * ({@code size} - 2)) (modulo
 * {@code mod}). (Note the {@code -2} that takes care of the
 * Montgomery digits.)
 * <P>
 *
 * To <EM>montgomerize</EM> a bignat {@code x} means to compute {@code
 * x} * {@code mont_fac}. On the host this is conveniently done as
 * {@code x.mult(hmod.mont_fac).mod(hmod.m)}, where {@code hmod} is a
 * {@link ds.ov2.bignat.Host_modulus} initialized from the right size
 * and modulus. On the card one can use montgomery multiplication with
 * the squared montgomery factor. <P>
 *
 *
 * <P>
 *
 * In Montgomery multiplication the computation of the multiplication
 * and the modulus are done simultaneously. This saves a lot of space.
 * All intermediate results have the same length as the arguments
 * (provided there are two leading Montgomery digits). <STRONG>As a
 * side effect Montgomery multiplication works only with odd
 * moduli.</STRONG> If the modulus is even the construction of the
 * necessary auxiliary data (see {@link
 * ds.ov2.bignat.Modulus#last_digit_inverse}) is impossible. The {@link
 * ds.ov2.bignat.Host_modulus#Host_modulus(int, BigInteger)
 * initializing Host_modulus constructor} will, for instance,
 * terminate with an {@link java.lang.ArithmeticException} (thrown by
 * {@link java.math.BigInteger#modInverse} BigInteger.modInverse). 
 * <P>
 *
 * Montgomery multiplication only works with montgomerized arguments. 
 * That is, before multiplying {@code x} with {@code y} one first has 
 * to montgomerize {@code x} and {@code y}, ie, compute {@code x} * 
 * {@code mont_fac} and {@code y} * {@code mont_fac}. From the 
 * montgomerized arguments montgomery multiplication computes 
 * {@code x} * {@code y} * {@code mont_fac}, ie, the montgomerization of 
 * the product {@code x} * {@code y}.
 * <P>
 *
 * To obtain the real result one has to demontgomerize. On the card
 * one can use Montgomery multiplication with a (not montgomerized) 1.
 * Or, more efficiently, with {@link
 * ds.ov2.bignat.Bignat#demontgomerize demontgomerize}. On the host
 * one can use the demontgomerization factor in the {@link
 * ds.ov2.bignat.Host_modulus}, i.e., compute {@code
 * x.mult(hmod.demont_fac).mod(hmod.m)} for a suitable {@link
 * ds.ov2.bignat.Host_modulus}.
 * <P>
 *
 * When multiplying n numbers a_1,...,a_n instead of montgomerizing the 
 * a_i and demontgomerizing the result one can add {@code mont_fac}^(n-1)
 * as additional factor. The result is then in normal form 
 * (not montgomerized).
 * 
 *
 * <a name="resizing"></a>
 * <H3>Resizing</H3>
 *
 * Once allocated, the size of numbers (Bignat's) and arrays (Bignat
 * arrays) in this package stays constant. Also the key size of the
 * RSA ciphers used for exponentiation does not change.
 * <P>
 *
 * However, for use in test frames (if VARIABLE_SIZE_BIGNATS is
 * defined) {@link ds.ov2.bignat.Bignat Bignats}, {@link
 * ds.ov2.bignat.Bignat_array Bignat arrays} and {@link
 * ds.ov2.bignat.RSA_exponent RSA exponents} can provide the illusion
 * of having a different size or length. This works by changing the
 * {@link ds.ov2.bignat.Bignat#size} field in Bignat's to a value
 * smaller than the size of the digit array (see {@link
 * ds.ov2.bignat.Bignat#value}). For {@link ds.ov2.bignat.Bignat_array
 * bignat arrays} and {@link ds.ov2.bignat.Vector vectors} their
 * {@link ds.ov2.bignat.Bignat_array#length} field is changed. For
 * {@link ds.ov2.bignat.RSA_exponent} a new public RSA key is
 * internally allocated. The last indices of the respective arrays
 * stay then simply unused. Note that these arrays will never be
 * reallocated, in particular, one cannot use {@link
 * ds.ov2.bignat.Bignat#resize Bignat.resize} to make the Bignat
 * larger than originally specified in the constructor. <P>
 *
 * Additional support for resizing numbers and arrays is provided by
 * {@link ds.ov2.bignat.Resize}.
 *
 *
 * <H3>Other topics</H3>
 *
 * For the internal representation of Bignat's, <a
 * href="Bignat.html#data_representation">see here</a>.
 * <P>
 *
 * 
 *
 */
package ds.ov2.bignat;
