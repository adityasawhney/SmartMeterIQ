// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Bignat_protocols.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Host_protocol;
import java.math.BigInteger;
import ds.ov2.util.APDU_short;
import ds.ov2.util.APDU_boolean;
import ds.ov2.bignat.Bignat;
import ds.ov2.bignat.Modulus;
import ds.ov2.bignat.Host_modulus;
import ds.ov2.bignat.Resize;
import ds.ov2.bignat.APDU_BigInteger;


/**
 * Stub code for running methods on the card.
 * Defines one stub method for each protocol step in Bignat_protocols.id.
 * The stub methods are the top entry point into the
 * OV-chip protocol layer for host driver code.
 * Each stub method performs the following actions:
 * <OL>
 * <LI>argument conversion (for instance from
 *     {@link java.math.BigInteger BigInteger} to
 *     {@link ds.ov2.bignat.Bignat Bignat})</LI>
 * <LI>transfers the arguments to the card
 *     (possibly using several APDU's)</LI>
 * <LI>invokes the right method on the card</LI>
 * <LI>transfers the results back (again with possibly
 *     several APDU's)</LI>
 * <LI>result conversion</LI>
 * <LI>and finally packages several results into one
 *     tuple object</LI>
 * </OL>
 * 
 * @author idl compiler
 * @version automatically generated from Bignat_protocols.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Bignat_protocols_stubs {

    /**
     * A protocol description instance from Bignat_protocols.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Bignat_protocols_description protocol_description;


    /**
     * The output channel for debugging information of the 
     * OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out;


    /**
     * Controls apdutool line printing. Initialized in the constructor,
     * if true, the OV-chip protocol layer prints apdutool lines as 
     * part of its debugging output.
     */
    private boolean with_apdu_script;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol mont_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step mont_mult_init
    // 

    /**
     * Host protocol instance for step mont_mult_init of protocol mont_mult.
     * Initialized via {@link #init_hp_mont_mult_init init_hp_mont_mult_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mont_mult_init;

    /**
     * Initialization method for {@link #hp_mont_mult_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mont_mult_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mont_mult_init = 
            new Host_protocol(d.mont_mult_protocol,
                              d.mont_mult_init_step,
                              out,
                              script,
                              "mont mult init"
                              );
    }


    /**
     * Call step mont_mult_init of protocol mont_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void mont_mult_init_call(CardChannel _cc,
                                    BigInteger _bignats_n_1_host_arg,
                                    BigInteger _bignats_n_2_host_arg,
                                    Host_modulus _bignats_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_mont_mult_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step mont_mult_1
    // 

    /**
     * Host protocol instance for step mont_mult_1 of protocol mont_mult.
     * Initialized via {@link #init_hp_mont_mult_1 init_hp_mont_mult_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mont_mult_1;

    /**
     * Initialization method for {@link #hp_mont_mult_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mont_mult_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mont_mult_1 = 
            new Host_protocol(d.mont_mult_protocol,
                              d.mont_mult_1_step,
                              out,
                              script,
                              "mont mult 1"
                              );
    }


    /**
     * Call step mont_mult_1 of protocol mont_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long mont_mult_1_call(CardChannel _cc,
                                 int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_mont_mult_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step mont_mult_2
    // 

    /**
     * Host protocol instance for step mont_mult_2 of protocol mont_mult.
     * Initialized via {@link #init_hp_mont_mult_2 init_hp_mont_mult_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mont_mult_2;

    /**
     * Initialization method for {@link #hp_mont_mult_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mont_mult_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mont_mult_2 = 
            new Host_protocol(d.mont_mult_protocol,
                              d.mont_mult_2_step,
                              out,
                              script,
                              "mont mult 2"
                              );
    }


    /**
     * Call step mont_mult_2 of protocol mont_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long mont_mult_2_call(CardChannel _cc,
                                 int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_mont_mult_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step mont_mult_result
    // 

    /**
     * Host protocol instance for step mont_mult_result of protocol mont_mult.
     * Initialized via {@link #init_hp_mont_mult_result init_hp_mont_mult_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mont_mult_result;

    /**
     * Initialization method for {@link #hp_mont_mult_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mont_mult_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mont_mult_result = 
            new Host_protocol(d.mont_mult_protocol,
                              d.mont_mult_result_step,
                              out,
                              script,
                              "mont mult result"
                              );
    }


    /**
     * Call step mont_mult_result of protocol mont_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.r_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger mont_mult_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        hp_mont_mult_result.run_step(_cc, call_args, call_res);
        return _bignats_r_1_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol demontgomerize
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step demont_init
    // 

    /**
     * Host protocol instance for step demont_init of protocol demontgomerize.
     * Initialized via {@link #init_hp_demont_init init_hp_demont_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_demont_init;

    /**
     * Initialization method for {@link #hp_demont_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_demont_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_demont_init = 
            new Host_protocol(d.demontgomerize_protocol,
                              d.demont_init_step,
                              out,
                              script,
                              "demont init"
                              );
    }


    /**
     * Call step demont_init of protocol demontgomerize
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void demont_init_call(CardChannel _cc,
                                 BigInteger _bignats_r_1_host_arg,
                                 Host_modulus _bignats_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_demont_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step demont_1
    // 

    /**
     * Host protocol instance for step demont_1 of protocol demontgomerize.
     * Initialized via {@link #init_hp_demont_1 init_hp_demont_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_demont_1;

    /**
     * Initialization method for {@link #hp_demont_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_demont_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_demont_1 = 
            new Host_protocol(d.demontgomerize_protocol,
                              d.demont_1_step,
                              out,
                              script,
                              "demont 1"
                              );
    }


    /**
     * Call step demont_1 of protocol demontgomerize
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long demont_1_call(CardChannel _cc,
                              int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_demont_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step demont_2
    // 

    /**
     * Host protocol instance for step demont_2 of protocol demontgomerize.
     * Initialized via {@link #init_hp_demont_2 init_hp_demont_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_demont_2;

    /**
     * Initialization method for {@link #hp_demont_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_demont_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_demont_2 = 
            new Host_protocol(d.demontgomerize_protocol,
                              d.demont_2_step,
                              out,
                              script,
                              "demont 2"
                              );
    }


    /**
     * Call step demont_2 of protocol demontgomerize
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long demont_2_call(CardChannel _cc,
                              int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_demont_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step demont_result
    // 

    /**
     * Host protocol instance for step demont_result of protocol demontgomerize.
     * Initialized via {@link #init_hp_demont_result init_hp_demont_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_demont_result;

    /**
     * Initialization method for {@link #hp_demont_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_demont_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_demont_result = 
            new Host_protocol(d.demontgomerize_protocol,
                              d.demont_result_step,
                              out,
                              script,
                              "demont result"
                              );
    }


    /**
     * Call step demont_result of protocol demontgomerize
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.r_2 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger demont_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_2_host_res
        };

        hp_demont_result.run_step(_cc, call_args, call_res);
        return _bignats_r_2_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol div
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step div_init
    // 

    /**
     * Host protocol instance for step div_init of protocol div.
     * Initialized via {@link #init_hp_div_init init_hp_div_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_div_init;

    /**
     * Initialization method for {@link #hp_div_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_div_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_div_init = 
            new Host_protocol(d.div_protocol,
                              d.div_init_step,
                              out,
                              script,
                              "div init"
                              );
    }


    /**
     * Call step div_init of protocol div
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @throws CardException in case of communication errors
     */
    public void div_init_call(CardChannel _cc,
                              BigInteger _bignats_n_1_host_arg,
                              BigInteger _bignats_n_2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_div_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step div_1
    // 

    /**
     * Host protocol instance for step div_1 of protocol div.
     * Initialized via {@link #init_hp_div_1 init_hp_div_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_div_1;

    /**
     * Initialization method for {@link #hp_div_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_div_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_div_1 = 
            new Host_protocol(d.div_protocol,
                              d.div_1_step,
                              out,
                              script,
                              "div 1"
                              );
    }


    /**
     * Call step div_1 of protocol div
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long div_1_call(CardChannel _cc,
                           int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_div_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step div_2
    // 

    /**
     * Host protocol instance for step div_2 of protocol div.
     * Initialized via {@link #init_hp_div_2 init_hp_div_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_div_2;

    /**
     * Initialization method for {@link #hp_div_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_div_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_div_2 = 
            new Host_protocol(d.div_protocol,
                              d.div_2_step,
                              out,
                              script,
                              "div 2"
                              );
    }


    /**
     * Call step div_2 of protocol div
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long div_2_call(CardChannel _cc,
                           int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_div_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step div_result
    // 

    /**
     * Host protocol instance for step div_result of protocol div.
     * Initialized via {@link #init_hp_div_result init_hp_div_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_div_result;

    /**
     * Initialization method for {@link #hp_div_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_div_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_div_result = 
            new Host_protocol(d.div_protocol,
                              d.div_result_step,
                              out,
                              script,
                              "div result"
                              );
    }


    /**
     * Result record for step div_result of
     * protocol div.
     */
    public static class Div_result_result {
        /**
         * Return value bignats.r_1 converted from Bignat.
         */
        public final BigInteger bignats_r_1;
        /**
         * Return value bignats.r_2 converted from Bignat.
         */
        public final BigInteger bignats_r_2;
        /**
         * Return record constructor.
         */
        public Div_result_result(
                    BigInteger a0,
                    BigInteger a1) {
            bignats_r_1 = a0;
            bignats_r_2 = a1;
        }
    }


    /**
     * Call step div_result of protocol div
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Div_result_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Div_result_result div_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _bignats_r_2_host_res
        };

        hp_div_result.run_step(_cc, call_args, call_res);
        return new Div_result_result(_bignats_r_1_host_res.value, _bignats_r_2_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol RSA_exp
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step rsa_exp_init
    // 

    /**
     * Host protocol instance for step rsa_exp_init of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_init init_hp_rsa_exp_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_init;

    /**
     * Initialization method for {@link #hp_rsa_exp_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_init = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_init_step,
                              out,
                              script,
                              "rsa exp init"
                              );
    }


    /**
     * Call step rsa_exp_init of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @throws CardException in case of communication errors
     */
    public void rsa_exp_init_call(CardChannel _cc,
                                  BigInteger _bignats_n_1_host_arg,
                                  BigInteger _bignats_s_1_host_arg,
                                  BigInteger _bignats_n_2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_rsa_exp_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step rsa_exp_parts_empty
    // 

    /**
     * Host protocol instance for step rsa_exp_parts_empty of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_parts_empty init_hp_rsa_exp_parts_empty} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_parts_empty;

    /**
     * Initialization method for {@link #hp_rsa_exp_parts_empty}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_parts_empty(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_parts_empty = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_parts_empty_step,
                              out,
                              script,
                              "rsa exp parts nothing"
                              );
    }


    /**
     * Call step rsa_exp_parts_empty of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long rsa_exp_parts_empty_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_rsa_exp_parts_empty.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_parts_exp
    // 

    /**
     * Host protocol instance for step rsa_exp_parts_exp of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_parts_exp init_hp_rsa_exp_parts_exp} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_parts_exp;

    /**
     * Initialization method for {@link #hp_rsa_exp_parts_exp}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_parts_exp(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_parts_exp = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_parts_exp_step,
                              out,
                              script,
                              "rsa exp parts exp"
                              );
    }


    /**
     * Call step rsa_exp_parts_exp of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long rsa_exp_parts_exp_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_rsa_exp_parts_exp.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_full_empty
    // 

    /**
     * Host protocol instance for step rsa_exp_full_empty of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_full_empty init_hp_rsa_exp_full_empty} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_full_empty;

    /**
     * Initialization method for {@link #hp_rsa_exp_full_empty}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_full_empty(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_full_empty = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_full_empty_step,
                              out,
                              script,
                              "rsa exp full empty"
                              );
    }


    /**
     * Call step rsa_exp_full_empty of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long rsa_exp_full_empty_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_rsa_exp_full_empty.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_full_exp
    // 

    /**
     * Host protocol instance for step rsa_exp_full_exp of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_full_exp init_hp_rsa_exp_full_exp} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_full_exp;

    /**
     * Initialization method for {@link #hp_rsa_exp_full_exp}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_full_exp(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_full_exp = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_full_exp_step,
                              out,
                              script,
                              "rsa exp full exp"
                              );
    }


    /**
     * Call step rsa_exp_full_exp of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long rsa_exp_full_exp_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_rsa_exp_full_exp.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_result
    // 

    /**
     * Host protocol instance for step rsa_exp_result of protocol RSA_exp.
     * Initialized via {@link #init_hp_rsa_exp_result init_hp_rsa_exp_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_rsa_exp_result;

    /**
     * Initialization method for {@link #hp_rsa_exp_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_rsa_exp_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_rsa_exp_result = 
            new Host_protocol(d.rsa_exp_protocol,
                              d.rsa_exp_result_step,
                              out,
                              script,
                              "rsa exp result"
                              );
    }


    /**
     * Result record for step rsa_exp_result of
     * protocol RSA_exp.
     */
    public static class Rsa_exp_result_result {
        /**
         * Return value bignats.r_1 converted from Bignat.
         */
        public final BigInteger bignats_r_1;
        /**
         * Return value bignats.r_2 converted from Bignat.
         */
        public final BigInteger bignats_r_2;
        /**
         * Return record constructor.
         */
        public Rsa_exp_result_result(
                    BigInteger a0,
                    BigInteger a1) {
            bignats_r_1 = a0;
            bignats_r_2 = a1;
        }
    }


    /**
     * Call step rsa_exp_result of protocol RSA_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Rsa_exp_result_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Rsa_exp_result_result rsa_exp_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _bignats_r_2_host_res
        };

        hp_rsa_exp_result.run_step(_cc, call_args, call_res);
        return new Rsa_exp_result_result(_bignats_r_1_host_res.value, _bignats_r_2_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol squared_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step sq_mult_init
    // 

    /**
     * Host protocol instance for step sq_mult_init of protocol squared_mult.
     * Initialized via {@link #init_hp_sq_mult_init init_hp_sq_mult_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_init;

    /**
     * Initialization method for {@link #hp_sq_mult_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_init = 
            new Host_protocol(d.squared_mult_protocol,
                              d.sq_mult_init_step,
                              out,
                              script,
                              "squared mult init"
                              );
    }


    /**
     * Call step sq_mult_init of protocol squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void sq_mult_init_call(CardChannel _cc,
                                  BigInteger _bignats_n_1_host_arg,
                                  BigInteger _bignats_n_2_host_arg,
                                  Host_modulus _bignats_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_sq_mult_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step sq_mult_1
    // 

    /**
     * Host protocol instance for step sq_mult_1 of protocol squared_mult.
     * Initialized via {@link #init_hp_sq_mult_1 init_hp_sq_mult_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_1;

    /**
     * Initialization method for {@link #hp_sq_mult_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_1 = 
            new Host_protocol(d.squared_mult_protocol,
                              d.sq_mult_1_step,
                              out,
                              script,
                              "squared mult 1"
                              );
    }


    /**
     * Call step sq_mult_1 of protocol squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long sq_mult_1_call(CardChannel _cc,
                               int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_sq_mult_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step sq_mult_2
    // 

    /**
     * Host protocol instance for step sq_mult_2 of protocol squared_mult.
     * Initialized via {@link #init_hp_sq_mult_2 init_hp_sq_mult_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_2;

    /**
     * Initialization method for {@link #hp_sq_mult_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_2 = 
            new Host_protocol(d.squared_mult_protocol,
                              d.sq_mult_2_step,
                              out,
                              script,
                              "squared mult 2"
                              );
    }


    /**
     * Call step sq_mult_2 of protocol squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long sq_mult_2_call(CardChannel _cc,
                               int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_sq_mult_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step sq_mult_result
    // 

    /**
     * Host protocol instance for step sq_mult_result of protocol squared_mult.
     * Initialized via {@link #init_hp_sq_mult_result init_hp_sq_mult_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_result;

    /**
     * Initialization method for {@link #hp_sq_mult_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_result = 
            new Host_protocol(d.squared_mult_protocol,
                              d.sq_mult_result_step,
                              out,
                              script,
                              "squared mult result"
                              );
    }


    /**
     * Call step sq_mult_result of protocol squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.r_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger sq_mult_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        hp_sq_mult_result.run_step(_cc, call_args, call_res);
        return _bignats_r_1_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol short_squared_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step short_sq_mult_init
    // 

    /**
     * Host protocol instance for step short_sq_mult_init of protocol short_squared_mult.
     * Initialized via {@link #init_hp_short_sq_mult_init init_hp_short_sq_mult_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_mult_init;

    /**
     * Initialization method for {@link #hp_short_sq_mult_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_mult_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_mult_init = 
            new Host_protocol(d.short_squared_mult_protocol,
                              d.short_sq_mult_init_step,
                              out,
                              script,
                              "short squared mult init"
                              );
    }


    /**
     * Call step short_sq_mult_init of protocol short_squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_s_2_host_arg argument bignats.s_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void short_sq_mult_init_call(CardChannel _cc,
                                        BigInteger _bignats_s_1_host_arg,
                                        BigInteger _bignats_s_2_host_arg,
                                        Host_modulus _bignats_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_2.size(), _bignats_s_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_short_sq_mult_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step short_sq_mult_1
    // 

    /**
     * Host protocol instance for step short_sq_mult_1 of protocol short_squared_mult.
     * Initialized via {@link #init_hp_short_sq_mult_1 init_hp_short_sq_mult_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_mult_1;

    /**
     * Initialization method for {@link #hp_short_sq_mult_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_mult_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_mult_1 = 
            new Host_protocol(d.short_squared_mult_protocol,
                              d.short_sq_mult_1_step,
                              out,
                              script,
                              "short squared mult 1"
                              );
    }


    /**
     * Call step short_sq_mult_1 of protocol short_squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long short_sq_mult_1_call(CardChannel _cc,
                                     int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_short_sq_mult_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step short_sq_mult_2
    // 

    /**
     * Host protocol instance for step short_sq_mult_2 of protocol short_squared_mult.
     * Initialized via {@link #init_hp_short_sq_mult_2 init_hp_short_sq_mult_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_mult_2;

    /**
     * Initialization method for {@link #hp_short_sq_mult_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_mult_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_mult_2 = 
            new Host_protocol(d.short_squared_mult_protocol,
                              d.short_sq_mult_2_step,
                              out,
                              script,
                              "short_squared mult 2"
                              );
    }


    /**
     * Call step short_sq_mult_2 of protocol short_squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long short_sq_mult_2_call(CardChannel _cc,
                                     int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_short_sq_mult_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step short_sq_mult_result
    // 

    /**
     * Host protocol instance for step short_sq_mult_result of protocol short_squared_mult.
     * Initialized via {@link #init_hp_short_sq_mult_result init_hp_short_sq_mult_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_mult_result;

    /**
     * Initialization method for {@link #hp_short_sq_mult_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_mult_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_mult_result = 
            new Host_protocol(d.short_squared_mult_protocol,
                              d.short_sq_mult_result_step,
                              out,
                              script,
                              "short squared mult result"
                              );
    }


    /**
     * Call step short_sq_mult_result of protocol short_squared_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.dr_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger short_sq_mult_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        hp_short_sq_mult_result.run_step(_cc, call_args, call_res);
        return _bignats_dr_1_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol squared_mult_4
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step sq_mult_4_init
    // 

    /**
     * Host protocol instance for step sq_mult_4_init of protocol squared_mult_4.
     * Initialized via {@link #init_hp_sq_mult_4_init init_hp_sq_mult_4_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_4_init;

    /**
     * Initialization method for {@link #hp_sq_mult_4_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_4_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_4_init = 
            new Host_protocol(d.squared_mult_4_protocol,
                              d.sq_mult_4_init_step,
                              out,
                              script,
                              "squared 4 mult init"
                              );
    }


    /**
     * Call step sq_mult_4_init of protocol squared_mult_4
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     * @param _bignats_mult_modulus_host_arg argument bignats.mult_modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void sq_mult_4_init_call(CardChannel _cc,
                                    BigInteger _bignats_r_1_host_arg,
                                    BigInteger _bignats_r_2_host_arg,
                                    Host_modulus _bignats_mult_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg),
            _bignats_mult_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_sq_mult_4_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step sq_mult_4_1
    // 

    /**
     * Host protocol instance for step sq_mult_4_1 of protocol squared_mult_4.
     * Initialized via {@link #init_hp_sq_mult_4_1 init_hp_sq_mult_4_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_4_1;

    /**
     * Initialization method for {@link #hp_sq_mult_4_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_4_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_4_1 = 
            new Host_protocol(d.squared_mult_4_protocol,
                              d.sq_mult_4_1_step,
                              out,
                              script,
                              "squared 4 mult empty"
                              );
    }


    /**
     * Call step sq_mult_4_1 of protocol squared_mult_4
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long sq_mult_4_1_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_sq_mult_4_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step sq_mult_4_2
    // 

    /**
     * Host protocol instance for step sq_mult_4_2 of protocol squared_mult_4.
     * Initialized via {@link #init_hp_sq_mult_4_2 init_hp_sq_mult_4_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_4_2;

    /**
     * Initialization method for {@link #hp_sq_mult_4_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_4_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_4_2 = 
            new Host_protocol(d.squared_mult_4_protocol,
                              d.sq_mult_4_2_step,
                              out,
                              script,
                              "squared 4 mult mult"
                              );
    }


    /**
     * Call step sq_mult_4_2 of protocol squared_mult_4
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long sq_mult_4_2_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_sq_mult_4_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step sq_mult_4_result
    // 

    /**
     * Host protocol instance for step sq_mult_4_result of protocol squared_mult_4.
     * Initialized via {@link #init_hp_sq_mult_4_result init_hp_sq_mult_4_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_sq_mult_4_result;

    /**
     * Initialization method for {@link #hp_sq_mult_4_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_sq_mult_4_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_sq_mult_4_result = 
            new Host_protocol(d.squared_mult_4_protocol,
                              d.sq_mult_4_result_step,
                              out,
                              script,
                              "squared 4 mult result"
                              );
    }


    /**
     * Call step sq_mult_4_result of protocol squared_mult_4
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.r_3 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger sq_mult_4_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_3_host_res = new APDU_BigInteger(protocol_description.bignats.r_3.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_3_host_res
        };

        hp_sq_mult_4_result.run_step(_cc, call_args, call_res);
        return _bignats_r_3_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol short_square_4_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step short_sq_4_mult_init
    // 

    /**
     * Host protocol instance for step short_sq_4_mult_init of protocol short_square_4_mult.
     * Initialized via {@link #init_hp_short_sq_4_mult_init init_hp_short_sq_4_mult_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_4_mult_init;

    /**
     * Initialization method for {@link #hp_short_sq_4_mult_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_4_mult_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_4_mult_init = 
            new Host_protocol(d.short_square_4_mult_protocol,
                              d.short_sq_4_mult_init_step,
                              out,
                              script,
                              "short square 4 mult init"
                              );
    }


    /**
     * Call step short_sq_4_mult_init of protocol short_square_4_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_s_2_host_arg argument bignats.s_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     * @throws CardException in case of communication errors
     */
    public void short_sq_4_mult_init_call(CardChannel _cc,
                                         BigInteger _bignats_s_1_host_arg,
                                         BigInteger _bignats_s_2_host_arg,
                                         Host_modulus _bignats_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_2.size(), _bignats_s_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        hp_short_sq_4_mult_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step short_sq_4_mult_1
    // 

    /**
     * Host protocol instance for step short_sq_4_mult_1 of protocol short_square_4_mult.
     * Initialized via {@link #init_hp_short_sq_4_mult_1 init_hp_short_sq_4_mult_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_4_mult_1;

    /**
     * Initialization method for {@link #hp_short_sq_4_mult_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_4_mult_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_4_mult_1 = 
            new Host_protocol(d.short_square_4_mult_protocol,
                              d.short_sq_4_mult_1_step,
                              out,
                              script,
                              "short square 4 mult 1"
                              );
    }


    /**
     * Call step short_sq_4_mult_1 of protocol short_square_4_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long short_sq_4_mult_1_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_short_sq_4_mult_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step short_sq_4_mult_2
    // 

    /**
     * Host protocol instance for step short_sq_4_mult_2 of protocol short_square_4_mult.
     * Initialized via {@link #init_hp_short_sq_4_mult_2 init_hp_short_sq_4_mult_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_4_mult_2;

    /**
     * Initialization method for {@link #hp_short_sq_4_mult_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_4_mult_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_4_mult_2 = 
            new Host_protocol(d.short_square_4_mult_protocol,
                              d.short_sq_4_mult_2_step,
                              out,
                              script,
                              "short_square 4 mult 2"
                              );
    }


    /**
     * Call step short_sq_4_mult_2 of protocol short_square_4_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long short_sq_4_mult_2_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_short_sq_4_mult_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step short_sq_4_mult_result
    // 

    /**
     * Host protocol instance for step short_sq_4_mult_result of protocol short_square_4_mult.
     * Initialized via {@link #init_hp_short_sq_4_mult_result init_hp_short_sq_4_mult_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_short_sq_4_mult_result;

    /**
     * Initialization method for {@link #hp_short_sq_4_mult_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_short_sq_4_mult_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_short_sq_4_mult_result = 
            new Host_protocol(d.short_square_4_mult_protocol,
                              d.short_sq_4_mult_result_step,
                              out,
                              script,
                              "short square 4 mult result"
                              );
    }


    /**
     * Call step short_sq_4_mult_result of protocol short_square_4_mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.dr_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger short_sq_4_mult_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        hp_short_sq_4_mult_result.run_step(_cc, call_args, call_res);
        return _bignats_dr_1_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol add
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step add_init
    // 

    /**
     * Host protocol instance for step add_init of protocol add.
     * Initialized via {@link #init_hp_add_init init_hp_add_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_add_init;

    /**
     * Initialization method for {@link #hp_add_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_add_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_add_init = 
            new Host_protocol(d.add_protocol,
                              d.add_init_step,
                              out,
                              script,
                              "add init"
                              );
    }


    /**
     * Call step add_init of protocol add
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     * @throws CardException in case of communication errors
     */
    public void add_init_call(CardChannel _cc,
                              BigInteger _bignats_r_1_host_arg,
                              BigInteger _bignats_r_2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_add_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step add_1
    // 

    /**
     * Host protocol instance for step add_1 of protocol add.
     * Initialized via {@link #init_hp_add_1 init_hp_add_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_add_1;

    /**
     * Initialization method for {@link #hp_add_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_add_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_add_1 = 
            new Host_protocol(d.add_protocol,
                              d.add_1_step,
                              out,
                              script,
                              "add 1"
                              );
    }


    /**
     * Call step add_1 of protocol add
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long add_1_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_add_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step add_2
    // 

    /**
     * Host protocol instance for step add_2 of protocol add.
     * Initialized via {@link #init_hp_add_2 init_hp_add_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_add_2;

    /**
     * Initialization method for {@link #hp_add_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_add_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_add_2 = 
            new Host_protocol(d.add_protocol,
                              d.add_2_step,
                              out,
                              script,
                              "add 2"
                              );
    }


    /**
     * Call step add_2 of protocol add
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long add_2_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_add_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step add_result
    // 

    /**
     * Host protocol instance for step add_result of protocol add.
     * Initialized via {@link #init_hp_add_result init_hp_add_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_add_result;

    /**
     * Initialization method for {@link #hp_add_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_add_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_add_result = 
            new Host_protocol(d.add_protocol,
                              d.add_result_step,
                              out,
                              script,
                              "add result"
                              );
    }


    /**
     * Call step add_result of protocol add
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.r_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger add_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        hp_add_result.run_step(_cc, call_args, call_res);
        return _bignats_r_1_host_res.value;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol subtract
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step subtract_init
    // 

    /**
     * Host protocol instance for step subtract_init of protocol subtract.
     * Initialized via {@link #init_hp_subtract_init init_hp_subtract_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_subtract_init;

    /**
     * Initialization method for {@link #hp_subtract_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_subtract_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_subtract_init = 
            new Host_protocol(d.subtract_protocol,
                              d.subtract_init_step,
                              out,
                              script,
                              "subtract init"
                              );
    }


    /**
     * Call step subtract_init of protocol subtract
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     * @throws CardException in case of communication errors
     */
    public void subtract_init_call(CardChannel _cc,
                                   BigInteger _bignats_r_1_host_arg,
                                   BigInteger _bignats_r_2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_subtract_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step subtract_1
    // 

    /**
     * Host protocol instance for step subtract_1 of protocol subtract.
     * Initialized via {@link #init_hp_subtract_1 init_hp_subtract_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_subtract_1;

    /**
     * Initialization method for {@link #hp_subtract_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_subtract_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_subtract_1 = 
            new Host_protocol(d.subtract_protocol,
                              d.subtract_1_step,
                              out,
                              script,
                              "subtract 1"
                              );
    }


    /**
     * Call step subtract_1 of protocol subtract
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long subtract_1_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_subtract_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step subtract_2
    // 

    /**
     * Host protocol instance for step subtract_2 of protocol subtract.
     * Initialized via {@link #init_hp_subtract_2 init_hp_subtract_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_subtract_2;

    /**
     * Initialization method for {@link #hp_subtract_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_subtract_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_subtract_2 = 
            new Host_protocol(d.subtract_protocol,
                              d.subtract_2_step,
                              out,
                              script,
                              "subtract 2"
                              );
    }


    /**
     * Call step subtract_2 of protocol subtract
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long subtract_2_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_subtract_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step subtract_result
    // 

    /**
     * Host protocol instance for step subtract_result of protocol subtract.
     * Initialized via {@link #init_hp_subtract_result init_hp_subtract_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_subtract_result;

    /**
     * Initialization method for {@link #hp_subtract_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_subtract_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_subtract_result = 
            new Host_protocol(d.subtract_protocol,
                              d.subtract_result_step,
                              out,
                              script,
                              "subtract result"
                              );
    }


    /**
     * Result record for step subtract_result of
     * protocol subtract.
     */
    public static class Subtract_result_result {
        /**
         * Return value bignats.r_1 converted from Bignat.
         */
        public final BigInteger bignats_r_1;
        /**
         * Return value carry converted from APDU_boolean.
         */
        public final boolean carry;
        /**
         * Return record constructor.
         */
        public Subtract_result_result(
                    BigInteger a0,
                    boolean a1) {
            bignats_r_1 = a0;
            carry = a1;
        }
    }


    /**
     * Call step subtract_result of protocol subtract
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Subtract_result_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Subtract_result_result subtract_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_boolean _carry_host_res = new APDU_boolean();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _carry_host_res
        };

        hp_subtract_result.run_step(_cc, call_args, call_res);
        return new Subtract_result_result(_bignats_r_1_host_res.value, _carry_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step mult_init
    // 

    /**
     * Host protocol instance for step mult_init of protocol mult.
     * Initialized via {@link #init_hp_mult_init init_hp_mult_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mult_init;

    /**
     * Initialization method for {@link #hp_mult_init}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mult_init(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mult_init = 
            new Host_protocol(d.mult_protocol,
                              d.mult_init_step,
                              out,
                              script,
                              "mult init"
                              );
    }


    /**
     * Call step mult_init of protocol mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     * @throws CardException in case of communication errors
     */
    public void mult_init_call(CardChannel _cc,
                               BigInteger _bignats_r_1_host_arg,
                               BigInteger _bignats_r_2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_mult_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step mult_1
    // 

    /**
     * Host protocol instance for step mult_1 of protocol mult.
     * Initialized via {@link #init_hp_mult_1 init_hp_mult_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mult_1;

    /**
     * Initialization method for {@link #hp_mult_1}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mult_1(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mult_1 = 
            new Host_protocol(d.mult_protocol,
                              d.mult_1_step,
                              out,
                              script,
                              "mult 1"
                              );
    }


    /**
     * Call step mult_1 of protocol mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long mult_1_call(CardChannel _cc,
                            int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_mult_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step mult_2
    // 

    /**
     * Host protocol instance for step mult_2 of protocol mult.
     * Initialized via {@link #init_hp_mult_2 init_hp_mult_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mult_2;

    /**
     * Initialization method for {@link #hp_mult_2}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mult_2(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mult_2 = 
            new Host_protocol(d.mult_protocol,
                              d.mult_2_step,
                              out,
                              script,
                              "mult 2"
                              );
    }


    /**
     * Call step mult_2 of protocol mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long mult_2_call(CardChannel _cc,
                            int _rounds_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_mult_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step mult_result
    // 

    /**
     * Host protocol instance for step mult_result of protocol mult.
     * Initialized via {@link #init_hp_mult_result init_hp_mult_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mult_result;

    /**
     * Initialization method for {@link #hp_mult_result}.
     *
     * @param d description instance for Bignat_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mult_result(Bignat_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mult_result = 
            new Host_protocol(d.mult_protocol,
                              d.mult_result_step,
                              out,
                              script,
                              "mult result"
                              );
    }


    /**
     * Call step mult_result of protocol mult
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result bignats.dr_1 converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger mult_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        hp_mult_result.run_step(_cc, call_args, call_res);
        return _bignats_dr_1_host_res.value;
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. Initializes all host protocol
     * instances from Bignat_protocols.id. 
     *
     * @param d protocol description instance for Bignat_protocols.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public Bignat_protocols_stubs(Bignat_protocols_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
        init_hp_mont_mult_init(protocol_description, out, with_apdu_script);
        init_hp_mont_mult_1(protocol_description, out, with_apdu_script);
        init_hp_mont_mult_2(protocol_description, out, with_apdu_script);
        init_hp_mont_mult_result(protocol_description, out, with_apdu_script);
        init_hp_demont_init(protocol_description, out, with_apdu_script);
        init_hp_demont_1(protocol_description, out, with_apdu_script);
        init_hp_demont_2(protocol_description, out, with_apdu_script);
        init_hp_demont_result(protocol_description, out, with_apdu_script);
        init_hp_div_init(protocol_description, out, with_apdu_script);
        init_hp_div_1(protocol_description, out, with_apdu_script);
        init_hp_div_2(protocol_description, out, with_apdu_script);
        init_hp_div_result(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_init(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_parts_empty(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_parts_exp(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_full_empty(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_full_exp(protocol_description, out, with_apdu_script);
        init_hp_rsa_exp_result(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_init(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_1(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_2(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_result(protocol_description, out, with_apdu_script);
        init_hp_short_sq_mult_init(protocol_description, out, with_apdu_script);
        init_hp_short_sq_mult_1(protocol_description, out, with_apdu_script);
        init_hp_short_sq_mult_2(protocol_description, out, with_apdu_script);
        init_hp_short_sq_mult_result(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_4_init(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_4_1(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_4_2(protocol_description, out, with_apdu_script);
        init_hp_sq_mult_4_result(protocol_description, out, with_apdu_script);
        init_hp_short_sq_4_mult_init(protocol_description, out, with_apdu_script);
        init_hp_short_sq_4_mult_1(protocol_description, out, with_apdu_script);
        init_hp_short_sq_4_mult_2(protocol_description, out, with_apdu_script);
        init_hp_short_sq_4_mult_result(protocol_description, out, with_apdu_script);
        init_hp_add_init(protocol_description, out, with_apdu_script);
        init_hp_add_1(protocol_description, out, with_apdu_script);
        init_hp_add_2(protocol_description, out, with_apdu_script);
        init_hp_add_result(protocol_description, out, with_apdu_script);
        init_hp_subtract_init(protocol_description, out, with_apdu_script);
        init_hp_subtract_1(protocol_description, out, with_apdu_script);
        init_hp_subtract_2(protocol_description, out, with_apdu_script);
        init_hp_subtract_result(protocol_description, out, with_apdu_script);
        init_hp_mult_init(protocol_description, out, with_apdu_script);
        init_hp_mult_1(protocol_description, out, with_apdu_script);
        init_hp_mult_2(protocol_description, out, with_apdu_script);
        init_hp_mult_result(protocol_description, out, with_apdu_script);
    }
}

