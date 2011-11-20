// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Exponent_perf.id
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
import ds.ov2.bignat.APDU_BigInteger;
import ds.ov2.bignat.Modulus;
import ds.ov2.bignat.Host_modulus;
import ds.ov2.bignat.Vector;
import ds.ov2.bignat.Host_vector;
import ds.ov2.bignat.Resize;


/**
 * Stub code for running methods on the card.
 * Defines one stub method for each protocol step in Exponent_perf.id.
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
 * @version automatically generated from Exponent_perf.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Exponent_perf_stubs {

    /**
     * A protocol description instance from Exponent_perf.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Exponent_perf_description protocol_description;


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
    // Protocol Vector_length
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step set_vector_length
    // 

    /**
     * Host protocol instance for step set_vector_length of protocol Vector_length.
     * Initialized via {@link #init_hp_set_vector_length init_hp_set_vector_length} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_set_vector_length;

    /**
     * Initialization method for {@link #hp_set_vector_length}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_set_vector_length(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_set_vector_length = 
            new Host_protocol(d.vector_length_protocol,
                              d.set_vector_length_step,
                              out,
                              script,
                              "set vector length"
                              );
    }


    /**
     * Call step set_vector_length of protocol Vector_length
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _n1_host_arg argument n1 to be converted to APDU_short
     * @param _n2_host_arg argument n2 to be converted to APDU_short
     * @throws CardException in case of communication errors
     */
    public void set_vector_length_call(CardChannel _cc,
                                       int _n1_host_arg,
                                       int _n2_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_n1_host_arg),
            new APDU_short(_n2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_set_vector_length.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol Vector_exp
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step vector_exp_choose_mod
    // 

    /**
     * Host protocol instance for step vector_exp_choose_mod of protocol Vector_exp.
     * Initialized via {@link #init_hp_vector_exp_choose_mod init_hp_vector_exp_choose_mod} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_vector_exp_choose_mod;

    /**
     * Initialization method for {@link #hp_vector_exp_choose_mod}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_vector_exp_choose_mod(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_vector_exp_choose_mod = 
            new Host_protocol(d.vector_exp_protocol,
                              d.vector_exp_choose_mod_step,
                              out,
                              script,
                              "vector exponent set modulus"
                              );
    }


    /**
     * Call step vector_exp_choose_mod of protocol Vector_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _vector_exp_variant_host_arg argument vector_exp_variant to be converted to APDU_short
     * @throws CardException in case of communication errors
     */
    public void vector_exp_choose_mod_call(CardChannel _cc,
                                         int _vector_exp_variant_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_vector_exp_variant_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_vector_exp_choose_mod.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step vector_exp_init
    // 

    /**
     * Host protocol instance for step vector_exp_init of protocol Vector_exp.
     * Initialized via {@link #init_hp_vector_exp_init init_hp_vector_exp_init} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_vector_exp_init;

    /**
     * Initialization method for {@link #hp_vector_exp_init}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_vector_exp_init(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_vector_exp_init = 
            new Host_protocol(d.vector_exp_protocol,
                              d.vector_exp_init_step,
                              out,
                              script,
                              "vector exponent init"
                              );
    }


    /**
     * Call step vector_exp_init of protocol Vector_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _bignats_base_host_arg argument bignats.base to be converted to Vector
     * @param _bignats_exponent_host_arg argument bignats.exponent to be converted to Vector
     * @param _vec_exp_modulus_host_arg argument vec_exp_modulus to be converted to Modulus
     * @param _n1_host_arg argument n1 to be converted to APDU_short
     * @param _bignats_base_factors_host_arg argument bignats.base_factors to be converted to Vector
     * @param _one_or_correction_host_arg argument one_or_correction to be converted to Bignat
     * @param _keep_modulus_host_arg argument keep_modulus to be converted to APDU_boolean
     * @throws CardException in case of communication errors
     */
    public void vector_exp_init_call(CardChannel _cc,
                                     Host_vector _bignats_base_host_arg,
                                     Host_vector _bignats_exponent_host_arg,
                                     Host_modulus _vec_exp_modulus_host_arg,
                                     int _n1_host_arg,
                                     Host_vector _bignats_base_factors_host_arg,
                                     BigInteger _one_or_correction_host_arg,
                                     boolean _keep_modulus_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _bignats_base_host_arg,
            _bignats_exponent_host_arg,
            _vec_exp_modulus_host_arg,
            new APDU_short(_n1_host_arg),
            _bignats_base_factors_host_arg,
            new APDU_BigInteger(protocol_description.one_or_correction.size(), _one_or_correction_host_arg),
            new APDU_boolean(_keep_modulus_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_vector_exp_init.run_step(_cc, call_args, call_res);
        return;
    }


    //#########################################################################
    // Step vector_exp_1
    // 

    /**
     * Host protocol instance for step vector_exp_1 of protocol Vector_exp.
     * Initialized via {@link #init_hp_vector_exp_1 init_hp_vector_exp_1} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_vector_exp_1;

    /**
     * Initialization method for {@link #hp_vector_exp_1}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_vector_exp_1(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_vector_exp_1 = 
            new Host_protocol(d.vector_exp_protocol,
                              d.vector_exp_1_step,
                              out,
                              script,
                              "vector exponent 1"
                              );
    }


    /**
     * Call step vector_exp_1 of protocol Vector_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long vector_exp_1_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_vector_exp_1.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step vector_exp_2
    // 

    /**
     * Host protocol instance for step vector_exp_2 of protocol Vector_exp.
     * Initialized via {@link #init_hp_vector_exp_2 init_hp_vector_exp_2} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_vector_exp_2;

    /**
     * Initialization method for {@link #hp_vector_exp_2}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_vector_exp_2(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_vector_exp_2 = 
            new Host_protocol(d.vector_exp_protocol,
                              d.vector_exp_2_step,
                              out,
                              script,
                              "vector exponent 2"
                              );
    }


    /**
     * Call step vector_exp_2 of protocol Vector_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long vector_exp_2_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_vector_exp_2.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
        return duration;
    }


    //#########################################################################
    // Step vector_exp_result
    // 

    /**
     * Host protocol instance for step vector_exp_result of protocol Vector_exp.
     * Initialized via {@link #init_hp_vector_exp_result init_hp_vector_exp_result} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_vector_exp_result;

    /**
     * Initialization method for {@link #hp_vector_exp_result}.
     *
     * @param d description instance for Exponent_perf.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_vector_exp_result(Exponent_perf_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_vector_exp_result = 
            new Host_protocol(d.vector_exp_protocol,
                              d.vector_exp_result_step,
                              out,
                              script,
                              "vector result"
                              );
    }


    /**
     * Call step vector_exp_result of protocol Vector_exp
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return result result converted from Bignat
     * @throws CardException in case of communication errors
     */
    public BigInteger vector_exp_result_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _result_host_res = new APDU_BigInteger(protocol_description.result.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _result_host_res
        };

        hp_vector_exp_result.run_step(_cc, call_args, call_res);
        return _result_host_res.value;
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. Initializes all host protocol
     * instances from Exponent_perf.id. 
     *
     * @param d protocol description instance for Exponent_perf.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public Exponent_perf_stubs(Exponent_perf_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
        init_hp_set_vector_length(protocol_description, out, with_apdu_script);
        init_hp_vector_exp_choose_mod(protocol_description, out, with_apdu_script);
        init_hp_vector_exp_init(protocol_description, out, with_apdu_script);
        init_hp_vector_exp_1(protocol_description, out, with_apdu_script);
        init_hp_vector_exp_2(protocol_description, out, with_apdu_script);
        init_hp_vector_exp_result(protocol_description, out, with_apdu_script);
    }
}

