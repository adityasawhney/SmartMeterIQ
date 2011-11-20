// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Misc_protocols.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
import ds.ov2.util.APDU_boolean;
import ds.ov2.util.APDU_short;
import ds.ov2.util.APDU_byte_array;


/**
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in Misc_protocols.id.
 * This class is the test-frame alternative to
 * {@link Misc_protocols_stubs}. It provides the same
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
 * @version automatically generated from Misc_protocols.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Misc_protocols_test_stubs {

    /**
     * A protocol description instance from Misc_protocols.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Misc_protocols_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol Ping
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step ping
    // 

    /**
     * Call step ping of protocol Ping
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Misc_protocols_stubs}
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long ping_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step ping");
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.ping_step.arguments);
        protocol_description.ping_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.ping_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step ping");
        return duration;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol set_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step set_size
    // 

    /**
     * Call step set_size of protocol set_size
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Misc_protocols_stubs}
     * @param _short_bignat_size_host_arg argument short_bignat_size to be converted to APDU_short
     * @param _long_bignat_size_host_arg argument long_bignat_size to be converted to APDU_short
     * @param _double_bignat_size_host_arg argument double_bignat_size to be converted to APDU_short
     * @param _cipher_size_host_arg argument cipher_size to be converted to APDU_short
     */
    public void set_size_call(CardChannel _cc,
                              int _short_bignat_size_host_arg,
                              int _long_bignat_size_host_arg,
                              int _double_bignat_size_host_arg,
                              int _cipher_size_host_arg)
    {
        if(out != null) 
            out.println("start step bignat size");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_short_bignat_size_host_arg),
            new APDU_short(_long_bignat_size_host_arg),
            new APDU_short(_double_bignat_size_host_arg),
            new APDU_short(_cipher_size_host_arg)
        };

        APDU_Serializable[] call_res = null;

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.set_size_step.arguments);
        protocol_description.set_size_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.set_size_step.results);
        if(out != null)
            out.println("finished step bignat size");
        return;
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
                    int a0,
                    int a1,
                    int a2) {
            mem_persistent = a0;
            mem_transient_reset = a1;
            mem_transient_deselect = a2;
        }
    }


    /**
     * Call step mem_size of protocol mem_size
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Misc_protocols_stubs}
     * @return Mem_size_result record containing all results.
     */
    public Mem_size_result mem_size_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step report ram size");
        APDU_Serializable[] call_args = null;

        APDU_short _mem_persistent_host_res = new APDU_short();
        APDU_short _mem_transient_reset_host_res = new APDU_short();
        APDU_short _mem_transient_deselect_host_res = new APDU_short();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
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
        return new Mem_size_result(_mem_persistent_host_res.value, _mem_transient_reset_host_res.value, _mem_transient_deselect_host_res.value);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol status
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step status
    // 

    /**
     * Result record for step status of
     * protocol status.
     */
    public static class Status_result {
        /**
         * Return value max_short_bignat_size converted from APDU_short.
         */
        public final int max_short_bignat_size;
        /**
         * Return value max_long_bignat_size converted from APDU_short.
         */
        public final int max_long_bignat_size;
        /**
         * Return value max_double_bignat_size converted from APDU_short.
         */
        public final int max_double_bignat_size;
        /**
         * Return value max_vector_length converted from APDU_short.
         */
        public final int max_vector_length;
        /**
         * Return value cap_creation_time.
         */
        public final APDU_byte_array cap_creation_time;
        /**
         * Return value assertions_on converted from APDU_boolean.
         */
        public final boolean assertions_on;
        /**
         * Return value use_squared_rsa_mult_4 converted from APDU_boolean.
         */
        public final boolean use_squared_rsa_mult_4;
        /**
         * Return record constructor.
         */
        public Status_result(
                    int a0,
                    int a1,
                    int a2,
                    int a3,
                    APDU_byte_array a4,
                    boolean a5,
                    boolean a6) {
            max_short_bignat_size = a0;
            max_long_bignat_size = a1;
            max_double_bignat_size = a2;
            max_vector_length = a3;
            cap_creation_time = a4;
            assertions_on = a5;
            use_squared_rsa_mult_4 = a6;
        }
    }


    /**
     * Call step status of protocol status
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Misc_protocols_stubs}
     * @return Status_result record containing all results.
     */
    public Status_result status_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step applet status");
        APDU_Serializable[] call_args = null;

        APDU_short _max_short_bignat_size_host_res = new APDU_short();
        APDU_short _max_long_bignat_size_host_res = new APDU_short();
        APDU_short _max_double_bignat_size_host_res = new APDU_short();
        APDU_short _max_vector_length_host_res = new APDU_short();
        APDU_byte_array _cap_creation_time_host_res = new APDU_byte_array(protocol_description.cap_creation_time.size());
        APDU_boolean _assertions_on_host_res = new APDU_boolean();
        APDU_boolean _use_squared_rsa_mult_4_host_res = new APDU_boolean();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _max_short_bignat_size_host_res,
            _max_long_bignat_size_host_res,
            _max_double_bignat_size_host_res,
            _max_vector_length_host_res,
            _cap_creation_time_host_res,
            _assertions_on_host_res,
            _use_squared_rsa_mult_4_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.status_step.arguments);
        protocol_description.status_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.status_step.results);
        if(out != null)
            out.println("finished step applet status");
        return new Status_result(_max_short_bignat_size_host_res.value, _max_long_bignat_size_host_res.value, _max_double_bignat_size_host_res.value, _max_vector_length_host_res.value, _cap_creation_time_host_res, _assertions_on_host_res.value, _use_squared_rsa_mult_4_host_res.value);
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
     * with {@link Misc_protocols_stubs}, it is ignored here.
     *
     * @param d protocol description instance for Misc_protocols.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link Misc_protocols_stubs}
     */
     
    public Misc_protocols_test_stubs(Misc_protocols_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

