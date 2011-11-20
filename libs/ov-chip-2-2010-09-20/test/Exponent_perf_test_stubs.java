// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Exponent_perf.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
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
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in Exponent_perf.id.
 * This class is the test-frame alternative to
 * {@link Exponent_perf_stubs}. It provides the same
 * public interface, but instead of communicating with
 * a Java Card and invoking methods there, it directly
 * invokes the methods on the very same Java Virtual
 * machine. This is of course only possible, if (almost)
 * all of the applet code is available on the host.
 * All {@link javax.smartcardio.CardChannel
 * CardChannel} arguments in this class are unused.<P>
 * 
 * Each stub method here performs the following actions:
 * <OL>
 * <LI>argument conversion (for instance from
 *     {@link java.math.BigInteger BigInteger} to
 *     {@link ds.ov2.bignat.Bignat Bignat})</LI>
 * <LI>running the specified action for this step</LI>
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
public class Exponent_perf_test_stubs {

    /**
     * A protocol description instance from Exponent_perf.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Exponent_perf_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

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
     * Call step set_vector_length of protocol Vector_length
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @param _n1_host_arg argument n1 to be converted to APDU_short
     * @param _n2_host_arg argument n2 to be converted to APDU_short
     */
    public void set_vector_length_call(CardChannel _cc,
                                       int _n1_host_arg,
                                       int _n2_host_arg)
    {
        if(out != null) 
            out.println("start step set vector length");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_n1_host_arg),
            new APDU_short(_n2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.set_vector_length_step.arguments);
        protocol_description.set_vector_length_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.set_vector_length_step.results);
        if(out != null)
            out.println("finished step set vector length");
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
     * Call step vector_exp_choose_mod of protocol Vector_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @param _vector_exp_variant_host_arg argument vector_exp_variant to be converted to APDU_short
     */
    public void vector_exp_choose_mod_call(CardChannel _cc,
                                         int _vector_exp_variant_host_arg)
    {
        if(out != null) 
            out.println("start step vector exponent set modulus");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_vector_exp_variant_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.vector_exp_choose_mod_step.arguments);
        protocol_description.vector_exp_choose_mod_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.vector_exp_choose_mod_step.results);
        if(out != null)
            out.println("finished step vector exponent set modulus");
        return;
    }


    //#########################################################################
    // Step vector_exp_init
    // 

    /**
     * Call step vector_exp_init of protocol Vector_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @param _bignats_base_host_arg argument bignats.base to be converted to Vector
     * @param _bignats_exponent_host_arg argument bignats.exponent to be converted to Vector
     * @param _vec_exp_modulus_host_arg argument vec_exp_modulus to be converted to Modulus
     * @param _n1_host_arg argument n1 to be converted to APDU_short
     * @param _bignats_base_factors_host_arg argument bignats.base_factors to be converted to Vector
     * @param _one_or_correction_host_arg argument one_or_correction to be converted to Bignat
     * @param _keep_modulus_host_arg argument keep_modulus to be converted to APDU_boolean
     */
    public void vector_exp_init_call(CardChannel _cc,
                                     Host_vector _bignats_base_host_arg,
                                     Host_vector _bignats_exponent_host_arg,
                                     Host_modulus _vec_exp_modulus_host_arg,
                                     int _n1_host_arg,
                                     Host_vector _bignats_base_factors_host_arg,
                                     BigInteger _one_or_correction_host_arg,
                                     boolean _keep_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step vector exponent init");
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

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.vector_exp_init_step.arguments);
        protocol_description.vector_exp_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.vector_exp_init_step.results);
        if(out != null)
            out.println("finished step vector exponent init");
        return;
    }


    //#########################################################################
    // Step vector_exp_1
    // 

    /**
     * Call step vector_exp_1 of protocol Vector_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long vector_exp_1_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step vector exponent 1");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.vector_exp_1_step.arguments);
        protocol_description.vector_exp_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.vector_exp_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step vector exponent 1");
        return duration;
    }


    //#########################################################################
    // Step vector_exp_2
    // 

    /**
     * Call step vector_exp_2 of protocol Vector_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long vector_exp_2_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step vector exponent 2");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.vector_exp_2_step.arguments);
        protocol_description.vector_exp_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.vector_exp_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step vector exponent 2");
        return duration;
    }


    //#########################################################################
    // Step vector_exp_result
    // 

    /**
     * Call step vector_exp_result of protocol Vector_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Exponent_perf_stubs}
     * @return result result converted from Bignat
     */
    public BigInteger vector_exp_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step vector result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _result_host_res = new APDU_BigInteger(protocol_description.result.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _result_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.vector_exp_result_step.arguments);
        protocol_description.vector_exp_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.vector_exp_result_step.results);
        if(out != null)
            out.println("finished step vector result");
        return _result_host_res.value;
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. In this test-frame alternative there are
     * no {@link ds.ov2.util.Host_protocol Host_protocol} data 
     * structures to initialize. Therefore this constructor only
     * saves the first two arguments into object local fields.
     * The argument {@code script} is only there for compatibility
     * with {@link Exponent_perf_stubs}, it is ignored here.
     *
     * @param d protocol description instance for Exponent_perf.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link Exponent_perf_stubs}
     */
     
    public Exponent_perf_test_stubs(Exponent_perf_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

