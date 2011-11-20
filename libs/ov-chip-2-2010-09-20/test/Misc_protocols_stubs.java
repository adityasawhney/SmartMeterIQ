// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Misc_protocols.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Host_protocol;
import ds.ov2.util.APDU_boolean;
import ds.ov2.util.APDU_short;
import ds.ov2.util.APDU_byte_array;


/**
 * Stub code for running methods on the card.
 * Defines one stub method for each protocol step in Misc_protocols.id.
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
 * @version automatically generated from Misc_protocols.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Misc_protocols_stubs {

    /**
     * A protocol description instance from Misc_protocols.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Misc_protocols_description protocol_description;


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
    // Protocol Ping
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step ping
    // 

    /**
     * Host protocol instance for step ping of protocol Ping.
     * Initialized via {@link #init_hp_ping init_hp_ping} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_ping;

    /**
     * Initialization method for {@link #hp_ping}.
     *
     * @param d description instance for Misc_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_ping(Misc_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_ping = 
            new Host_protocol(d.ping_protocol,
                              d.ping_step,
                              out,
                              script,
                              "ping"
                              );
    }


    /**
     * Call step ping of protocol Ping
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     * @throws CardException in case of communication errors
     */
    public long ping_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        hp_ping.run_step(_cc, call_args, call_res);
        long duration = System.nanoTime() - start;
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
     * Host protocol instance for step set_size of protocol set_size.
     * Initialized via {@link #init_hp_set_size init_hp_set_size} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_set_size;

    /**
     * Initialization method for {@link #hp_set_size}.
     *
     * @param d description instance for Misc_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_set_size(Misc_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_set_size = 
            new Host_protocol(d.set_size_protocol,
                              d.set_size_step,
                              out,
                              script,
                              "bignat size"
                              );
    }


    /**
     * Call step set_size of protocol set_size
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @param _short_bignat_size_host_arg argument short_bignat_size to be converted to APDU_short
     * @param _long_bignat_size_host_arg argument long_bignat_size to be converted to APDU_short
     * @param _double_bignat_size_host_arg argument double_bignat_size to be converted to APDU_short
     * @param _cipher_size_host_arg argument cipher_size to be converted to APDU_short
     * @throws CardException in case of communication errors
     */
    public void set_size_call(CardChannel _cc,
                              int _short_bignat_size_host_arg,
                              int _long_bignat_size_host_arg,
                              int _double_bignat_size_host_arg,
                              int _cipher_size_host_arg)
        throws CardException
    {
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short(_short_bignat_size_host_arg),
            new APDU_short(_long_bignat_size_host_arg),
            new APDU_short(_double_bignat_size_host_arg),
            new APDU_short(_cipher_size_host_arg)
        };

        APDU_Serializable[] call_res = null;

        hp_set_size.run_step(_cc, call_args, call_res);
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
     * Host protocol instance for step mem_size of protocol mem_size.
     * Initialized via {@link #init_hp_mem_size init_hp_mem_size} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_mem_size;

    /**
     * Initialization method for {@link #hp_mem_size}.
     *
     * @param d description instance for Misc_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_mem_size(Misc_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_mem_size = 
            new Host_protocol(d.mem_size_protocol,
                              d.mem_size_step,
                              out,
                              script,
                              "report ram size"
                              );
    }


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
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Mem_size_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Mem_size_result mem_size_call(CardChannel _cc)
        throws CardException
    {
        APDU_Serializable[] call_args = null;

        APDU_short _mem_persistent_host_res = new APDU_short();
        APDU_short _mem_transient_reset_host_res = new APDU_short();
        APDU_short _mem_transient_deselect_host_res = new APDU_short();
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _mem_persistent_host_res,
            _mem_transient_reset_host_res,
            _mem_transient_deselect_host_res
        };

        hp_mem_size.run_step(_cc, call_args, call_res);
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
     * Host protocol instance for step status of protocol status.
     * Initialized via {@link #init_hp_status init_hp_status} 
     * (which is called from the constructor).
     */
    private Host_protocol hp_status;

    /**
     * Initialization method for {@link #hp_status}.
     *
     * @param d description instance for Misc_protocols.id
     * @param out the debugging out channel, {@code null} for disabling 
     *         debugging output
     * @param script whether this step prints apdutool lines
     */
    private void init_hp_status(Misc_protocols_description d,
                                    PrintWriter out, boolean script) 
    {
        hp_status = 
            new Host_protocol(d.status_protocol,
                              d.status_step,
                              out,
                              script,
                              "applet status"
                              );
    }


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
     * on the card.
     * 
     * @param _cc communication channel to the applet, must not be null
     * @return Status_result record containing all results.
     * @throws CardException in case of communication errors
     */
    public Status_result status_call(CardChannel _cc)
        throws CardException
    {
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

        hp_status.run_step(_cc, call_args, call_res);
        return new Status_result(_max_short_bignat_size_host_res.value, _max_long_bignat_size_host_res.value, _max_double_bignat_size_host_res.value, _max_vector_length_host_res.value, _cap_creation_time_host_res, _assertions_on_host_res.value, _use_squared_rsa_mult_4_host_res.value);
    }


    //#########################################################################
    // Delayed stub initialization
    // 

    //#########################################################################
    // Constructor
    // 

    /**
     * Stub constructor. Initializes all host protocol
     * instances from Misc_protocols.id. 
     *
     * @param d protocol description instance for Misc_protocols.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script if true, print apdutool lines for all APDUs as part 
     *          of the debugging information.
     */
    public Misc_protocols_stubs(Misc_protocols_description d,
                               PrintWriter o, 
                               boolean script) {
        protocol_description = d;
        out = o;
        with_apdu_script = script;
        // initialize the Host_protocols
        init_hp_ping(protocol_description, out, with_apdu_script);
        init_hp_set_size(protocol_description, out, with_apdu_script);
        init_hp_mem_size(protocol_description, out, with_apdu_script);
        init_hp_status(protocol_description, out, with_apdu_script);
    }
}

