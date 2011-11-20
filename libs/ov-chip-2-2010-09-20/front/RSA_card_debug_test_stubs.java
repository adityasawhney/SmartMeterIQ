// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from RSA_card_debug.id
// by some sort of idl compiler.

package ds.ov2.front;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
import java.math.BigInteger;;
import ds.ov2.util.APDU_byte;;
import ds.ov2.util.APDU_short;;
import ds.ov2.util.APDU_boolean;;
import ds.ov2.bignat.Host_modulus;;
import ds.ov2.bignat.Host_vector;;
import ds.ov2.bignat.APDU_BigInteger;;


/**
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in RSA_card_debug.id.
 * This class is the test-frame alternative to
 * {@link RSA_card_debug_stubs}. It provides the same
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
 * @version automatically generated from RSA_card_debug.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class RSA_card_debug_test_stubs {

    /**
     * A protocol description instance from RSA_card_debug.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private RSA_card_debug_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol status
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step get
    // 

    /**
     * Result record for step get of
     * protocol status.
     */
    public static class Get_result {
        /**
         * Return value data.applet_id converted from APDU_byte.
         */
        public final byte data_applet_id;
        /**
         * Return value data.n converted from Modulus.
         */
        public final Host_modulus data_n;
        /**
         * Return value data.v converted from Bignat.
         */
        public final BigInteger data_v;
        /**
         * Return value data.ptls_key converted from Bignat.
         */
        public final BigInteger data_ptls_key;
        /**
         * Return value data.bases converted from Vector.
         */
        public final Host_vector data_bases;
        /**
         * Return value data.current_attributes converted from Vector.
         */
        public final Host_vector data_current_attributes;
        /**
         * Return value data.new_attributes converted from Vector.
         */
        public final Host_vector data_new_attributes;
        /**
         * Return value data.current_blinding converted from Bignat.
         */
        public final BigInteger data_current_blinding;
        /**
         * Return value data.new_blinding converted from Bignat.
         */
        public final BigInteger data_new_blinding;
        /**
         * Return value data.current_blinded_a converted from Bignat.
         */
        public final BigInteger data_current_blinded_a;
        /**
         * Return value data.new_blinded_a converted from Bignat.
         */
        public final BigInteger data_new_blinded_a;
        /**
         * Return value data.current_signature converted from Signature.
         */
        public final Host_signature data_current_signature;
        /**
         * Return value data.new_signature converted from Signature.
         */
        public final Host_signature data_new_signature;
        /**
         * Return value data.montgomery_corrections converted from Vector.
         */
        public final Host_vector data_montgomery_corrections;
        /**
         * Return record constructor.
         */
        public Get_result(
                    byte a0,
                    Host_modulus a1,
                    BigInteger a2,
                    BigInteger a3,
                    Host_vector a4,
                    Host_vector a5,
                    Host_vector a6,
                    BigInteger a7,
                    BigInteger a8,
                    BigInteger a9,
                    BigInteger a10,
                    Host_signature a11,
                    Host_signature a12,
                    Host_vector a13) {
            data_applet_id = a0;
            data_n = a1;
            data_v = a2;
            data_ptls_key = a3;
            data_bases = a4;
            data_current_attributes = a5;
            data_new_attributes = a6;
            data_current_blinding = a7;
            data_new_blinding = a8;
            data_current_blinded_a = a9;
            data_new_blinded_a = a10;
            data_current_signature = a11;
            data_new_signature = a12;
            data_montgomery_corrections = a13;
        }
    }


    /**
     * Call step get of protocol status
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_debug_stubs}
     * @return Get_result record containing all results.
     */
    public Get_result get_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step get status");
        APDU_Serializable[] call_args = null;

        APDU_byte _data_applet_id_host_res = new APDU_byte();
        Host_modulus _data_n_host_res = new Host_modulus(protocol_description.data.n.m.size());
        APDU_BigInteger _data_v_host_res = new APDU_BigInteger(protocol_description.data.v.size());
        APDU_BigInteger _data_ptls_key_host_res = new APDU_BigInteger(protocol_description.data.ptls_key.size());
        Host_vector _data_bases_host_res = new Host_vector(protocol_description.data.bases.get_bignat_size(), protocol_description.data.bases.get_length());
        Host_vector _data_current_attributes_host_res = new Host_vector(protocol_description.data.current_attributes.get_bignat_size(), protocol_description.data.current_attributes.get_length());
        Host_vector _data_new_attributes_host_res = new Host_vector(protocol_description.data.new_attributes.get_bignat_size(), protocol_description.data.new_attributes.get_length());
        APDU_BigInteger _data_current_blinding_host_res = new APDU_BigInteger(protocol_description.data.current_blinding.size());
        APDU_BigInteger _data_new_blinding_host_res = new APDU_BigInteger(protocol_description.data.new_blinding.size());
        APDU_BigInteger _data_current_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.current_blinded_a.size());
        APDU_BigInteger _data_new_blinded_a_host_res = new APDU_BigInteger(protocol_description.data.new_blinded_a.size());
        Host_signature _data_current_signature_host_res = new Host_signature(protocol_description.data.current_signature.sig_short_size, protocol_description.data.current_signature.sig_long_size, protocol_description.data.current_signature.applet_id);
        Host_signature _data_new_signature_host_res = new Host_signature(protocol_description.data.new_signature.sig_short_size, protocol_description.data.new_signature.sig_long_size, protocol_description.data.new_signature.applet_id);
        Host_vector _data_montgomery_corrections_host_res = new Host_vector(protocol_description.data.montgomery_corrections.get_bignat_size(), protocol_description.data.montgomery_corrections.get_length());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _data_applet_id_host_res,
            _data_n_host_res,
            _data_v_host_res,
            _data_ptls_key_host_res,
            _data_bases_host_res,
            _data_current_attributes_host_res,
            _data_new_attributes_host_res,
            _data_current_blinding_host_res,
            _data_new_blinding_host_res,
            _data_current_blinded_a_host_res,
            _data_new_blinded_a_host_res,
            _data_current_signature_host_res,
            _data_new_signature_host_res,
            _data_montgomery_corrections_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.get_step.arguments);
        protocol_description.get_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.get_step.results);
        if(out != null)
            out.println("finished step get status");
        return new Get_result(_data_applet_id_host_res.value, _data_n_host_res, _data_v_host_res.value, _data_ptls_key_host_res.value, _data_bases_host_res, _data_current_attributes_host_res, _data_new_attributes_host_res, _data_current_blinding_host_res.value, _data_new_blinding_host_res.value, _data_current_blinded_a_host_res.value, _data_new_blinded_a_host_res.value, _data_current_signature_host_res, _data_new_signature_host_res, _data_montgomery_corrections_host_res);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mem_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step mem_size
    // 

    /**
     * Result record for step mem_size of
     * protocol mem_size.
     */
    public static class Mem_size_result {
        /**
         * Return value assertions_on converted from APDU_boolean.
         */
        public final boolean assertions_on;
        /**
         * Return value mem_persistent converted from APDU_short.
         */
        public final int mem_persistent;
        /**
         * Return value mem_transient_reset converted from APDU_short.
         */
        public final int mem_transient_reset;
        /**
         * Return value mem_transient_deselect converted from APDU_short.
         */
        public final int mem_transient_deselect;
        /**
         * Return record constructor.
         */
        public Mem_size_result(
                    boolean a0,
                    int a1,
                    int a2,
                    int a3) {
            assertions_on = a0;
            mem_persistent = a1;
            mem_transient_reset = a2;
            mem_transient_deselect = a3;
        }
    }


    /**
     * Call step mem_size of protocol mem_size
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_debug_stubs}
     * @return Mem_size_result record containing all results.
     */
    public Mem_size_result mem_size_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step report ram size");
        APDU_Serializable[] call_args = null;

        APDU_boolean _assertions_on_host_res = new APDU_boolean();
        APDU_short _mem_persistent_host_res = new APDU_short();
        APDU_short _mem_transient_reset_host_res = new APDU_short();
        APDU_short _mem_transient_deselect_host_res = new APDU_short();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _assertions_on_host_res,
            _mem_persistent_host_res,
            _mem_transient_reset_host_res,
            _mem_transient_deselect_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.mem_size_step.arguments);
        protocol_description.mem_size_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.mem_size_step.results);
        if(out != null)
            out.println("finished step report ram size");
        return new Mem_size_result(_assertions_on_host_res.value, _mem_persistent_host_res.value, _mem_transient_reset_host_res.value, _mem_transient_deselect_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol reset_applet_state
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step reset
    // 

    /**
     * Call step reset of protocol reset_applet_state
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link RSA_card_debug_stubs}
     */
    public void reset_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step reset applet state");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.reset_step.arguments);
        protocol_description.reset_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.reset_step.results);
        if(out != null)
            out.println("finished step reset applet state");
        return;
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    /**
     * Delayed initialization.
     * Empty method, only here for compatibility with
     * {@link RSA_card_debug_stubs}. In this test-frame
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
     * with {@link RSA_card_debug_stubs}, it is ignored here.
     *
     * @param d protocol description instance for RSA_card_debug.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link RSA_card_debug_stubs}
     */
     
    public RSA_card_debug_test_stubs(RSA_card_debug_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

