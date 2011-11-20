// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Bignat_protocols.id
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
import ds.ov2.bignat.Modulus;
import ds.ov2.bignat.Host_modulus;
import ds.ov2.bignat.Resize;
import ds.ov2.bignat.APDU_BigInteger;


/**
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in Bignat_protocols.id.
 * This class is the test-frame alternative to
 * {@link Bignat_protocols_stubs}. It provides the same
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
 * @version automatically generated from Bignat_protocols.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Bignat_protocols_test_stubs {

    /**
     * A protocol description instance from Bignat_protocols.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Bignat_protocols_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

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
     * Call step mont_mult_init of protocol mont_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     */
    public void mont_mult_init_call(CardChannel _cc,
                                    BigInteger _bignats_n_1_host_arg,
                                    BigInteger _bignats_n_2_host_arg,
                                    Host_modulus _bignats_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step mont mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mont_mult_init_step.arguments);
        protocol_description.mont_mult_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mont_mult_init_step.results);
        if(out != null)
            out.println("finished step mont mult init");
        return;
    }


    //#########################################################################
    // Step mont_mult_1
    // 

    /**
     * Call step mont_mult_1 of protocol mont_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long mont_mult_1_call(CardChannel _cc,
                                 int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step mont mult 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mont_mult_1_step.arguments);
        protocol_description.mont_mult_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mont_mult_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step mont mult 1");
        return duration;
    }


    //#########################################################################
    // Step mont_mult_2
    // 

    /**
     * Call step mont_mult_2 of protocol mont_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long mont_mult_2_call(CardChannel _cc,
                                 int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step mont mult 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mont_mult_2_step.arguments);
        protocol_description.mont_mult_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mont_mult_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step mont mult 2");
        return duration;
    }


    //#########################################################################
    // Step mont_mult_result
    // 

    /**
     * Call step mont_mult_result of protocol mont_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.r_1 converted from Bignat
     */
    public BigInteger mont_mult_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step mont mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mont_mult_result_step.arguments);
        protocol_description.mont_mult_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mont_mult_result_step.results);
        if(out != null)
            out.println("finished step mont mult result");
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
     * Call step demont_init of protocol demontgomerize
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     */
    public void demont_init_call(CardChannel _cc,
                                 BigInteger _bignats_r_1_host_arg,
                                 Host_modulus _bignats_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step demont init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.demont_init_step.arguments);
        protocol_description.demont_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.demont_init_step.results);
        if(out != null)
            out.println("finished step demont init");
        return;
    }


    //#########################################################################
    // Step demont_1
    // 

    /**
     * Call step demont_1 of protocol demontgomerize
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long demont_1_call(CardChannel _cc,
                              int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step demont 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.demont_1_step.arguments);
        protocol_description.demont_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.demont_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step demont 1");
        return duration;
    }


    //#########################################################################
    // Step demont_2
    // 

    /**
     * Call step demont_2 of protocol demontgomerize
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long demont_2_call(CardChannel _cc,
                              int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step demont 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.demont_2_step.arguments);
        protocol_description.demont_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.demont_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step demont 2");
        return duration;
    }


    //#########################################################################
    // Step demont_result
    // 

    /**
     * Call step demont_result of protocol demontgomerize
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.r_2 converted from Bignat
     */
    public BigInteger demont_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step demont result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_2_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.demont_result_step.arguments);
        protocol_description.demont_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.demont_result_step.results);
        if(out != null)
            out.println("finished step demont result");
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
     * Call step div_init of protocol div
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     */
    public void div_init_call(CardChannel _cc,
                              BigInteger _bignats_n_1_host_arg,
                              BigInteger _bignats_n_2_host_arg)
    {
        if(out != null) 
            out.println("start step div init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.div_init_step.arguments);
        protocol_description.div_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.div_init_step.results);
        if(out != null)
            out.println("finished step div init");
        return;
    }


    //#########################################################################
    // Step div_1
    // 

    /**
     * Call step div_1 of protocol div
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long div_1_call(CardChannel _cc,
                           int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step div 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.div_1_step.arguments);
        protocol_description.div_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.div_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step div 1");
        return duration;
    }


    //#########################################################################
    // Step div_2
    // 

    /**
     * Call step div_2 of protocol div
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long div_2_call(CardChannel _cc,
                           int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step div 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.div_2_step.arguments);
        protocol_description.div_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.div_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step div 2");
        return duration;
    }


    //#########################################################################
    // Step div_result
    // 

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
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Div_result_result record containing all results.
     */
    public Div_result_result div_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step div result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _bignats_r_2_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.div_result_step.arguments);
        protocol_description.div_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.div_result_step.results);
        if(out != null)
            out.println("finished step div result");
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
     * Call step rsa_exp_init of protocol RSA_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     */
    public void rsa_exp_init_call(CardChannel _cc,
                                  BigInteger _bignats_n_1_host_arg,
                                  BigInteger _bignats_s_1_host_arg,
                                  BigInteger _bignats_n_2_host_arg)
    {
        if(out != null) 
            out.println("start step rsa exp init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_init_step.arguments);
        protocol_description.rsa_exp_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_init_step.results);
        if(out != null)
            out.println("finished step rsa exp init");
        return;
    }


    //#########################################################################
    // Step rsa_exp_parts_empty
    // 

    /**
     * Call step rsa_exp_parts_empty of protocol RSA_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long rsa_exp_parts_empty_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step rsa exp parts nothing");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_parts_empty_step.arguments);
        protocol_description.rsa_exp_parts_empty_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_parts_empty_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step rsa exp parts nothing");
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_parts_exp
    // 

    /**
     * Call step rsa_exp_parts_exp of protocol RSA_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long rsa_exp_parts_exp_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step rsa exp parts exp");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_parts_exp_step.arguments);
        protocol_description.rsa_exp_parts_exp_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_parts_exp_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step rsa exp parts exp");
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_full_empty
    // 

    /**
     * Call step rsa_exp_full_empty of protocol RSA_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long rsa_exp_full_empty_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step rsa exp full empty");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_full_empty_step.arguments);
        protocol_description.rsa_exp_full_empty_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_full_empty_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step rsa exp full empty");
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_full_exp
    // 

    /**
     * Call step rsa_exp_full_exp of protocol RSA_exp
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long rsa_exp_full_exp_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step rsa exp full exp");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_full_exp_step.arguments);
        protocol_description.rsa_exp_full_exp_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_full_exp_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step rsa exp full exp");
        return duration;
    }


    //#########################################################################
    // Step rsa_exp_result
    // 

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
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Rsa_exp_result_result record containing all results.
     */
    public Rsa_exp_result_result rsa_exp_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step rsa exp result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_BigInteger _bignats_r_2_host_res = new APDU_BigInteger(protocol_description.bignats.r_2.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _bignats_r_2_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.rsa_exp_result_step.arguments);
        protocol_description.rsa_exp_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.rsa_exp_result_step.results);
        if(out != null)
            out.println("finished step rsa exp result");
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
     * Call step sq_mult_init of protocol squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_n_1_host_arg argument bignats.n_1 to be converted to Bignat
     * @param _bignats_n_2_host_arg argument bignats.n_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     */
    public void sq_mult_init_call(CardChannel _cc,
                                  BigInteger _bignats_n_1_host_arg,
                                  BigInteger _bignats_n_2_host_arg,
                                  Host_modulus _bignats_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step squared mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.n_1.size(), _bignats_n_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.n_2.size(), _bignats_n_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_init_step.arguments);
        protocol_description.sq_mult_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_init_step.results);
        if(out != null)
            out.println("finished step squared mult init");
        return;
    }


    //#########################################################################
    // Step sq_mult_1
    // 

    /**
     * Call step sq_mult_1 of protocol squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long sq_mult_1_call(CardChannel _cc,
                               int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step squared mult 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_1_step.arguments);
        protocol_description.sq_mult_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step squared mult 1");
        return duration;
    }


    //#########################################################################
    // Step sq_mult_2
    // 

    /**
     * Call step sq_mult_2 of protocol squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long sq_mult_2_call(CardChannel _cc,
                               int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step squared mult 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_2_step.arguments);
        protocol_description.sq_mult_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step squared mult 2");
        return duration;
    }


    //#########################################################################
    // Step sq_mult_result
    // 

    /**
     * Call step sq_mult_result of protocol squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.r_1 converted from Bignat
     */
    public BigInteger sq_mult_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step squared mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_result_step.arguments);
        protocol_description.sq_mult_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_result_step.results);
        if(out != null)
            out.println("finished step squared mult result");
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
     * Call step short_sq_mult_init of protocol short_squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_s_2_host_arg argument bignats.s_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     */
    public void short_sq_mult_init_call(CardChannel _cc,
                                        BigInteger _bignats_s_1_host_arg,
                                        BigInteger _bignats_s_2_host_arg,
                                        Host_modulus _bignats_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step short squared mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_2.size(), _bignats_s_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_mult_init_step.arguments);
        protocol_description.short_sq_mult_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_mult_init_step.results);
        if(out != null)
            out.println("finished step short squared mult init");
        return;
    }


    //#########################################################################
    // Step short_sq_mult_1
    // 

    /**
     * Call step short_sq_mult_1 of protocol short_squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long short_sq_mult_1_call(CardChannel _cc,
                                     int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step short squared mult 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_mult_1_step.arguments);
        protocol_description.short_sq_mult_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_mult_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step short squared mult 1");
        return duration;
    }


    //#########################################################################
    // Step short_sq_mult_2
    // 

    /**
     * Call step short_sq_mult_2 of protocol short_squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long short_sq_mult_2_call(CardChannel _cc,
                                     int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step short_squared mult 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_mult_2_step.arguments);
        protocol_description.short_sq_mult_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_mult_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step short_squared mult 2");
        return duration;
    }


    //#########################################################################
    // Step short_sq_mult_result
    // 

    /**
     * Call step short_sq_mult_result of protocol short_squared_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.dr_1 converted from Bignat
     */
    public BigInteger short_sq_mult_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step short squared mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_mult_result_step.arguments);
        protocol_description.short_sq_mult_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_mult_result_step.results);
        if(out != null)
            out.println("finished step short squared mult result");
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
     * Call step sq_mult_4_init of protocol squared_mult_4
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     * @param _bignats_mult_modulus_host_arg argument bignats.mult_modulus to be converted to Modulus
     */
    public void sq_mult_4_init_call(CardChannel _cc,
                                    BigInteger _bignats_r_1_host_arg,
                                    BigInteger _bignats_r_2_host_arg,
                                    Host_modulus _bignats_mult_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step squared 4 mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg),
            _bignats_mult_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_4_init_step.arguments);
        protocol_description.sq_mult_4_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_4_init_step.results);
        if(out != null)
            out.println("finished step squared 4 mult init");
        return;
    }


    //#########################################################################
    // Step sq_mult_4_1
    // 

    /**
     * Call step sq_mult_4_1 of protocol squared_mult_4
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long sq_mult_4_1_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step squared 4 mult empty");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_4_1_step.arguments);
        protocol_description.sq_mult_4_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_4_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step squared 4 mult empty");
        return duration;
    }


    //#########################################################################
    // Step sq_mult_4_2
    // 

    /**
     * Call step sq_mult_4_2 of protocol squared_mult_4
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long sq_mult_4_2_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step squared 4 mult mult");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_4_2_step.arguments);
        protocol_description.sq_mult_4_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_4_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step squared 4 mult mult");
        return duration;
    }


    //#########################################################################
    // Step sq_mult_4_result
    // 

    /**
     * Call step sq_mult_4_result of protocol squared_mult_4
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.r_3 converted from Bignat
     */
    public BigInteger sq_mult_4_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step squared 4 mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_3_host_res = new APDU_BigInteger(protocol_description.bignats.r_3.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_3_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.sq_mult_4_result_step.arguments);
        protocol_description.sq_mult_4_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.sq_mult_4_result_step.results);
        if(out != null)
            out.println("finished step squared 4 mult result");
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
     * Call step short_sq_4_mult_init of protocol short_square_4_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_s_1_host_arg argument bignats.s_1 to be converted to Bignat
     * @param _bignats_s_2_host_arg argument bignats.s_2 to be converted to Bignat
     * @param _bignats_modulus_host_arg argument bignats.modulus to be converted to Modulus
     */
    public void short_sq_4_mult_init_call(CardChannel _cc,
                                         BigInteger _bignats_s_1_host_arg,
                                         BigInteger _bignats_s_2_host_arg,
                                         Host_modulus _bignats_modulus_host_arg)
    {
        if(out != null) 
            out.println("start step short square 4 mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.s_1.size(), _bignats_s_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.s_2.size(), _bignats_s_2_host_arg),
            _bignats_modulus_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_4_mult_init_step.arguments);
        protocol_description.short_sq_4_mult_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_4_mult_init_step.results);
        if(out != null)
            out.println("finished step short square 4 mult init");
        return;
    }


    //#########################################################################
    // Step short_sq_4_mult_1
    // 

    /**
     * Call step short_sq_4_mult_1 of protocol short_square_4_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long short_sq_4_mult_1_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step short square 4 mult 1");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_4_mult_1_step.arguments);
        protocol_description.short_sq_4_mult_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_4_mult_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step short square 4 mult 1");
        return duration;
    }


    //#########################################################################
    // Step short_sq_4_mult_2
    // 

    /**
     * Call step short_sq_4_mult_2 of protocol short_square_4_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long short_sq_4_mult_2_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step short_square 4 mult 2");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_4_mult_2_step.arguments);
        protocol_description.short_sq_4_mult_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_4_mult_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step short_square 4 mult 2");
        return duration;
    }


    //#########################################################################
    // Step short_sq_4_mult_result
    // 

    /**
     * Call step short_sq_4_mult_result of protocol short_square_4_mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.dr_1 converted from Bignat
     */
    public BigInteger short_sq_4_mult_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step short square 4 mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.short_sq_4_mult_result_step.arguments);
        protocol_description.short_sq_4_mult_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.short_sq_4_mult_result_step.results);
        if(out != null)
            out.println("finished step short square 4 mult result");
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
     * Call step add_init of protocol add
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     */
    public void add_init_call(CardChannel _cc,
                              BigInteger _bignats_r_1_host_arg,
                              BigInteger _bignats_r_2_host_arg)
    {
        if(out != null) 
            out.println("start step add init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.add_init_step.arguments);
        protocol_description.add_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.add_init_step.results);
        if(out != null)
            out.println("finished step add init");
        return;
    }


    //#########################################################################
    // Step add_1
    // 

    /**
     * Call step add_1 of protocol add
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long add_1_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step add 1");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.add_1_step.arguments);
        protocol_description.add_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.add_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step add 1");
        return duration;
    }


    //#########################################################################
    // Step add_2
    // 

    /**
     * Call step add_2 of protocol add
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long add_2_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step add 2");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.add_2_step.arguments);
        protocol_description.add_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.add_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step add 2");
        return duration;
    }


    //#########################################################################
    // Step add_result
    // 

    /**
     * Call step add_result of protocol add
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.r_1 converted from Bignat
     */
    public BigInteger add_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step add result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.add_result_step.arguments);
        protocol_description.add_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.add_result_step.results);
        if(out != null)
            out.println("finished step add result");
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
     * Call step subtract_init of protocol subtract
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     */
    public void subtract_init_call(CardChannel _cc,
                                   BigInteger _bignats_r_1_host_arg,
                                   BigInteger _bignats_r_2_host_arg)
    {
        if(out != null) 
            out.println("start step subtract init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.subtract_init_step.arguments);
        protocol_description.subtract_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.subtract_init_step.results);
        if(out != null)
            out.println("finished step subtract init");
        return;
    }


    //#########################################################################
    // Step subtract_1
    // 

    /**
     * Call step subtract_1 of protocol subtract
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long subtract_1_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step subtract 1");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.subtract_1_step.arguments);
        protocol_description.subtract_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.subtract_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step subtract 1");
        return duration;
    }


    //#########################################################################
    // Step subtract_2
    // 

    /**
     * Call step subtract_2 of protocol subtract
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long subtract_2_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step subtract 2");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.subtract_2_step.arguments);
        protocol_description.subtract_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.subtract_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step subtract 2");
        return duration;
    }


    //#########################################################################
    // Step subtract_result
    // 

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
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return Subtract_result_result record containing all results.
     */
    public Subtract_result_result subtract_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step subtract result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_r_1_host_res = new APDU_BigInteger(protocol_description.bignats.r_1.size());
        APDU_boolean _carry_host_res = new APDU_boolean();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_r_1_host_res,
            _carry_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.subtract_result_step.arguments);
        protocol_description.subtract_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.subtract_result_step.results);
        if(out != null)
            out.println("finished step subtract result");
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
     * Call step mult_init of protocol mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _bignats_r_1_host_arg argument bignats.r_1 to be converted to Bignat
     * @param _bignats_r_2_host_arg argument bignats.r_2 to be converted to Bignat
     */
    public void mult_init_call(CardChannel _cc,
                               BigInteger _bignats_r_1_host_arg,
                               BigInteger _bignats_r_2_host_arg)
    {
        if(out != null) 
            out.println("start step mult init");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.bignats.r_1.size(), _bignats_r_1_host_arg),
            new APDU_BigInteger(protocol_description.bignats.r_2.size(), _bignats_r_2_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mult_init_step.arguments);
        protocol_description.mult_init_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mult_init_step.results);
        if(out != null)
            out.println("finished step mult init");
        return;
    }


    //#########################################################################
    // Step mult_1
    // 

    /**
     * Call step mult_1 of protocol mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long mult_1_call(CardChannel _cc,
                            int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step mult 1");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mult_1_step.arguments);
        protocol_description.mult_1_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mult_1_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step mult 1");
        return duration;
    }


    //#########################################################################
    // Step mult_2
    // 

    /**
     * Call step mult_2 of protocol mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @param _rounds_host_arg argument rounds to be converted to APDU_short
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long mult_2_call(CardChannel _cc,
                            int _rounds_host_arg)
    {
        if(out != null) 
            out.println("start step mult 2");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_rounds_host_arg)
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mult_2_step.arguments);
        protocol_description.mult_2_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mult_2_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step mult 2");
        return duration;
    }


    //#########################################################################
    // Step mult_result
    // 

    /**
     * Call step mult_result of protocol mult
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Bignat_protocols_stubs}
     * @return result bignats.dr_1 converted from Bignat
     */
    public BigInteger mult_result_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step mult result");
        APDU_Serializable[] call_args = null;

        APDU_BigInteger _bignats_dr_1_host_res = new APDU_BigInteger(protocol_description.bignats.dr_1.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _bignats_dr_1_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mult_result_step.arguments);
        protocol_description.mult_result_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mult_result_step.results);
        if(out != null)
            out.println("finished step mult result");
        return _bignats_dr_1_host_res.value;
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
     * with {@link Bignat_protocols_stubs}, it is ignored here.
     *
     * @param d protocol description instance for Bignat_protocols.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link Bignat_protocols_stubs}
     */
     
    public Bignat_protocols_test_stubs(Bignat_protocols_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

