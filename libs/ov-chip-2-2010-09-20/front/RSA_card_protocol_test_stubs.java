// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from RSA_card_protocol.id
// by some sort of idl compiler.

package ds.ov2.front;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
import java.math.BigInteger;
import ds.ov2.util.APDU_byte;
import ds.ov2.util.APDU_short;
import ds.ov2.util.APDU_boolean;
import ds.ov2.bignat.APDU_BigInteger;
import ds.ov2.bignat.Host_modulus;
import ds.ov2.bignat.Host_vector;


/**
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in RSA_card_protocol.id.
 * This class is the test-frame alternative to
 * {@link RSA_card_protocol_stubs}. It provides the same
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
 * @version automatically generated from RSA_card_protocol.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class RSA_card_protocol_test_stubs {

    /**
     * A protocol description instance from RSA_card_protocol.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private RSA_card_protocol_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol allocate
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step allocate
    // 

    /**
     * Call step allocate of protocol allocate
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @param _short_bignat_size_host_arg argument short_bignat_size to be converted to APDU_short
     * @param _long_bignat_size_host_arg argument long_bignat_size to be converted to APDU_short
     * @param _attribute_length_host_arg argument attribute_length to be converted to APDU_short
     * @param _mont_correction_len_host_arg argument mont_correction_len to be converted to APDU_short
     */
    public void allocate_call(CardChannel _cc,
                              int _short_bignat_size_host_arg,
                              int _long_bignat_size_host_arg,
                              int _attribute_length_host_arg,
                              int _mont_correction_len_host_arg)
    {
        if(out != null) 
            out.println("start step allocate");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_short_bignat_size_host_arg),
            new APDU_short(_long_bignat_size_host_arg),
            new APDU_short(_attribute_length_host_arg),
            new APDU_short(_mont_correction_len_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.allocate_step.arguments);
        protocol_description.allocate_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.allocate_step.results);
        if(out != null)
            out.println("finished step allocate");
        return;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol initialize
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step init_data
    // 

    /**
     * Call step init_data of protocol initialize
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @param _data_n_host_arg argument data.n to be converted to Modulus
     * @param _data_ptls_key_host_arg argument data.ptls_key to be converted to Bignat
     * @param _data_bases_host_arg argument data.bases to be converted to Vector
     * @param _data_base_factors_host_arg argument data.base_factors to be converted to Vector
     * @param _data_current_attributes_host_arg argument data.current_attributes to be converted to Vector
     * @param _data_montgomerized_one_host_arg argument data.montgomerized_one to be converted to Bignat
     * @param _data_montgomery_corrections_host_arg argument data.montgomery_corrections to be converted to Vector
     */
    public void init_data_call(CardChannel _cc,
                               Host_modulus _data_n_host_arg,
                               BigInteger _data_ptls_key_host_arg,
                               Host_vector _data_bases_host_arg,
                               Host_vector _data_base_factors_host_arg,
                               Host_vector _data_current_attributes_host_arg,
                               BigInteger _data_montgomerized_one_host_arg,
                               Host_vector _data_montgomery_corrections_host_arg)
    {
        if(out != null) 
            out.println("start step initialize");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _data_n_host_arg,
            new APDU_BigInteger(protocol_description.data.ptls_key.size(), _data_ptls_key_host_arg),
            _data_bases_host_arg,
            _data_base_factors_host_arg,
            _data_current_attributes_host_arg,
            new APDU_BigInteger(protocol_description.data.montgomerized_one.size(), _data_montgomerized_one_host_arg),
            _data_montgomery_corrections_host_arg
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.init_data_step.arguments);
        protocol_description.init_data_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.init_data_step.results);
        if(out != null)
            out.println("finished step initialize");
        return;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol resign
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step get_signature
    // 

    /**
     * Result record for step get_signature of
     * protocol resign.
     */
    public static class Get_signature_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value data.applet_id converted from APDU_byte.
         */
        public final byte data_applet_id;
        /**
         * Return value data.current_blinded_a converted from Bignat.
         */
        public final BigInteger data_current_blinded_a;
        /**
         * Return value data.current_signature converted from Signature.
         */
        public final Host_signature data_current_signature;
        /**
         * Return record constructor.
         */
        public Get_signature_result(
                    long ad,
                    byte a0,
                    BigInteger a1,
                    Host_signature a2) {
            duration = ad;
            data_applet_id = a0;
            data_current_blinded_a = a1;
            data_current_signature = a2;
        }
    }


    /**
     * Call step get_signature of protocol resign
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @return Get_signature_result record containing all results, including the duration of the call.
     */
    public Get_signature_result get_signature_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step resign get attribute signature");
        APDU_Serializable[] call_args = null;

        APDU_byte _data_applet_id_host_res = new APDU_byte();
        APDU_BigInteger _data_current_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.current_blinded_a.size());
        Host_signature _data_current_signature_host_res = new Host_signature(protocol_description.data.current_signature.sig_short_size, protocol_description.data.current_signature.sig_long_size, protocol_description.data.current_signature.applet_id);
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_applet_id_host_res,
            _data_current_blinded_a_host_res,
            _data_current_signature_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.get_signature_step.arguments);
        protocol_description.get_signature_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.get_signature_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step resign get attribute signature");
        return new Get_signature_result(duration, _data_applet_id_host_res.value, _data_current_blinded_a_host_res.value, _data_current_signature_host_res);
    }


    //#########################################################################
    // Step make_sig_hash
    // 

    /**
     * Result record for step make_sig_hash of
     * protocol resign.
     */
    public static class Make_sig_hash_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value data.sig_remainder converted from Bignat.
         */
        public final BigInteger data_sig_remainder;
        /**
         * Return record constructor.
         */
        public Make_sig_hash_result(
                    long ad,
                    BigInteger a0) {
            duration = ad;
            data_sig_remainder = a0;
        }
    }


    /**
     * Call step make_sig_hash of protocol resign
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @param _data_host_alpha_host_arg argument data.host_alpha to be converted to Bignat
     * @param _data_remainders_host_arg argument data.remainders to be converted to Vector
     * @return Make_sig_hash_result record containing all results, including the duration of the call.
     */
    public Make_sig_hash_result make_sig_hash_call(CardChannel _cc,
                                         BigInteger _data_host_alpha_host_arg,
                                         Host_vector _data_remainders_host_arg)
    {
        if(out != null) 
            out.println("start step resign make hash");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.data.host_alpha.size(), _data_host_alpha_host_arg),
            _data_remainders_host_arg
        };

        APDU_BigInteger _data_sig_remainder_host_res = new APDU_BigInteger(protocol_description.data.sig_remainder.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_sig_remainder_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.make_sig_hash_step.arguments);
        protocol_description.make_sig_hash_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.make_sig_hash_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step resign make hash");
        return new Make_sig_hash_result(duration, _data_sig_remainder_host_res.value);
    }


    //#########################################################################
    // Step finish_signature
    // 

    /**
     * Result record for step finish_signature of
     * protocol resign.
     */
    public static class Finish_signature_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value signature_accepted converted from APDU_boolean.
         */
        public final boolean signature_accepted;
        /**
         * Return record constructor.
         */
        public Finish_signature_result(
                    long ad,
                    boolean a0) {
            duration = ad;
            signature_accepted = a0;
        }
    }


    /**
     * Call step finish_signature of protocol resign
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @param _data_host_response_host_arg argument data.host_response to be converted to Bignat
     * @return Finish_signature_result record containing all results, including the duration of the call.
     */
    public Finish_signature_result finish_signature_call(CardChannel _cc,
                                         BigInteger _data_host_response_host_arg)
    {
        if(out != null) 
            out.println("start step signature finish");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.data.host_response.size(), _data_host_response_host_arg)
        };

        APDU_boolean _signature_accepted_host_res = new APDU_boolean();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _signature_accepted_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.finish_signature_step.arguments);
        protocol_description.finish_signature_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.finish_signature_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step signature finish");
        return new Finish_signature_result(duration, _signature_accepted_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol gate
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step commit
    // 

    /**
     * Result record for step commit of
     * protocol gate.
     */
    public static class Commit_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value data.applet_id converted from APDU_byte.
         */
        public final byte data_applet_id;
        /**
         * Return value data.current_blinded_a converted from Bignat.
         */
        public final BigInteger data_current_blinded_a;
        /**
         * Return value data.current_signature converted from Signature.
         */
        public final Host_signature data_current_signature;
        /**
         * Return value data.result converted from Bignat.
         */
        public final BigInteger data_result;
        /**
         * Return record constructor.
         */
        public Commit_result(
                    long ad,
                    byte a0,
                    BigInteger a1,
                    Host_signature a2,
                    BigInteger a3) {
            duration = ad;
            data_applet_id = a0;
            data_current_blinded_a = a1;
            data_current_signature = a2;
            data_result = a3;
        }
    }


    /**
     * Call step commit of protocol gate
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @return Commit_result record containing all results, including the duration of the call.
     */
    public Commit_result commit_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step gate commit");
        APDU_Serializable[] call_args = null;

        APDU_byte _data_applet_id_host_res = new APDU_byte();
        APDU_BigInteger _data_current_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.current_blinded_a.size());
        Host_signature _data_current_signature_host_res = new Host_signature(protocol_description.data.current_signature.sig_short_size, protocol_description.data.current_signature.sig_long_size, protocol_description.data.current_signature.applet_id);
        APDU_BigInteger _data_result_host_res = new APDU_BigInteger(protocol_description.data.result.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_applet_id_host_res,
            _data_current_blinded_a_host_res,
            _data_current_signature_host_res,
            _data_result_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.commit_step.arguments);
        protocol_description.commit_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.commit_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step gate commit");
        return new Commit_result(duration, _data_applet_id_host_res.value, _data_current_blinded_a_host_res.value, _data_current_signature_host_res, _data_result_host_res.value);
    }


    //#########################################################################
    // Step respond
    // 

    /**
     * Result record for step respond of
     * protocol gate.
     */
    public static class Respond_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value data.remainders converted from Vector.
         */
        public final Host_vector data_remainders;
        /**
         * Return value data.result converted from Bignat.
         */
        public final BigInteger data_result;
        /**
         * Return record constructor.
         */
        public Respond_result(
                    long ad,
                    Host_vector a0,
                    BigInteger a1) {
            duration = ad;
            data_remainders = a0;
            data_result = a1;
        }
    }


    /**
     * Call step respond of protocol gate
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_protocol_stubs}
     * @param _data_gamma_beta_3_host_arg argument data.gamma_beta_3 to be converted to Bignat
     * @return Respond_result record containing all results, including the duration of the call.
     */
    public Respond_result respond_call(CardChannel _cc,
                                       BigInteger _data_gamma_beta_3_host_arg)
    {
        if(out != null) 
            out.println("start step gate card response");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_BigInteger(protocol_description.data.gamma_beta_3.size(), _data_gamma_beta_3_host_arg)
        };

        Host_vector _data_remainders_host_res = new Host_vector(protocol_description.data.remainders.get_bignat_size(), protocol_description.data.remainders.get_length());
        APDU_BigInteger _data_result_host_res = new APDU_BigInteger(protocol_description.data.result.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_remainders_host_res,
            _data_result_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.respond_step.arguments);
        protocol_description.respond_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.respond_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step gate card response");
        return new Respond_result(duration, _data_remainders_host_res, _data_result_host_res.value);
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    /**
     * Delayed initialization.
     * Empty method, only here for compatibility with
     * {@link RSA_card_protocol_stubs}. In this test-frame
     * alternative there are no {@link 
     * ds.ov2.util.Host_protocol Host_protocol} data
     * structures to initialize, consequently nothing has
     * to be done in delayed initialization
     */
    public void delayed_init() {
    }


    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. In this test-frame alternative there are
     * no {@link ds.ov2.util.Host_protocol Host_protocol} data 
     * structures to initialize. Therefore this constructor only
     * saves the first two arguments into object local fields.
     * The argument {@code script} is only there for compatibility
     * with {@link RSA_card_protocol_stubs}, it is ignored here.
     *
     * @param d protocol description instance for RSA_card_protocol.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link RSA_card_protocol_stubs}
     */
     
    public RSA_card_protocol_test_stubs(RSA_card_protocol_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

