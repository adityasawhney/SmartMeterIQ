// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Data_protocol.id
// by some sort of idl compiler.

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
import ds.ov2.util.Resizable_buffer;
import ds.ov2.util.APDU_boolean;
import ds.ov2.util.APDU_short_array;


/**
 * Stub code for running methods in the
 * testframe on the host.
 * Defines one stub method for each protocol step in Data_protocol.id.
 * This class is the test-frame alternative to
 * {@link Data_protocol_stubs}. It provides the same
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
 * @version automatically generated from Data_protocol.id
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Data_protocol_test_stubs {

    /**
     * A protocol description instance from Data_protocol.id. Used to access
     * the host initializers, which are additional parameters for
     * the APDU type constructors of arguments or results.
     * Initialized in the constructor.
     */
    private Data_protocol_description protocol_description;

    /**
     * The output channel for debugging information of 
     * the OV-chip protocol layer.
     * Initialized in the constructor.
     */
    private PrintWriter out = null;

    //#########################################################################
    //#########################################################################
    // 
    // Protocol check_data
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step check_data
    // 

    /**
     * Result record for step check_data of
     * protocol check_data.
     */
    public static class Check_data_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return value buf_6.
         */
        public final Resizable_buffer buf_6;
        /**
         * Return value buf_7.
         */
        public final Resizable_buffer buf_7;
        /**
         * Return value buf_8.
         */
        public final Resizable_buffer buf_8;
        /**
         * Return value buf_9.
         */
        public final Resizable_buffer buf_9;
        /**
         * Return record constructor.
         */
        public Check_data_result(
                    long ad,
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2,
                    Resizable_buffer a3,
                    Resizable_buffer a4) {
            duration = ad;
            buf_5 = a0;
            buf_6 = a1;
            buf_7 = a2;
            buf_8 = a3;
            buf_9 = a4;
        }
    }


    /**
     * Call step check_data of protocol check_data
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @param _buf_0_host_arg argument buf_0
     * @param _buf_1_host_arg argument buf_1
     * @param _buf_2_host_arg argument buf_2
     * @param _buf_3_host_arg argument buf_3
     * @param _buf_4_host_arg argument buf_4
     * @return Check_data_result record containing all results, including the duration of the call.
     */
    public Check_data_result check_data_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg,
                                         Resizable_buffer _buf_1_host_arg,
                                         Resizable_buffer _buf_2_host_arg,
                                         Resizable_buffer _buf_3_host_arg,
                                         Resizable_buffer _buf_4_host_arg)
    {
        if(out != null) 
            out.println("start step data check");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg,
            _buf_1_host_arg,
            _buf_2_host_arg,
            _buf_3_host_arg,
            _buf_4_host_arg
        };

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        Resizable_buffer _buf_6_host_res = new Resizable_buffer(protocol_description.buf_6.size());
        Resizable_buffer _buf_7_host_res = new Resizable_buffer(protocol_description.buf_7.size());
        Resizable_buffer _buf_8_host_res = new Resizable_buffer(protocol_description.buf_8.size());
        Resizable_buffer _buf_9_host_res = new Resizable_buffer(protocol_description.buf_9.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res,
            _buf_6_host_res,
            _buf_7_host_res,
            _buf_8_host_res,
            _buf_9_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.check_data_step.arguments);
        protocol_description.check_data_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.check_data_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step data check");
        return new Check_data_result(duration, _buf_5_host_res, _buf_6_host_res, _buf_7_host_res, _buf_8_host_res, _buf_9_host_res);
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
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @param _buf_sizes_host_arg argument buf_sizes to be converted to APDU_short_array
     * @param _performance_test_host_arg argument performance_test to be converted to APDU_boolean
     * @return result buf_sizes converted from APDU_short_array
     */
    public int[] set_size_call(CardChannel _cc,
                               int[] _buf_sizes_host_arg,
                               boolean _performance_test_host_arg)
    {
        if(out != null) 
            out.println("start step data set size");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            new APDU_short_array(protocol_description.buf_sizes.get_length(), _buf_sizes_host_arg),
            new APDU_boolean(_performance_test_host_arg)
        };

        APDU_short_array _buf_sizes_host_res = new APDU_short_array(protocol_description.buf_sizes.get_length());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_sizes_host_res
        };

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
            out.println("finished step data set size");
        return _buf_sizes_host_res.get_int_array();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_receive
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_performance_receive
    // 

    /**
     * Call step data_performance_receive of protocol data_performance_receive
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @param _buf_0_host_arg argument buf_0
     * @return Duration of the call in nanoseconds. 
     * The measurement includes (de-)serialization but not the
     * allocation of argument and result arrays.
     */
    public long data_performance_receive_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg)
    {
        if(out != null) 
            out.println("start step data measure send");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg
        };

        APDU_Serializable[] call_res = null;

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.data_performance_receive_step.arguments);
        protocol_description.data_performance_receive_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.data_performance_receive_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step data measure send");
        return duration;
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_send
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_performance_send
    // 

    /**
     * Result record for step data_performance_send of
     * protocol data_performance_send.
     */
    public static class Data_performance_send_result {
        /**
         * Duration of the call in nanoseconds. The measurement
         * includes (de-)serialization but not the
         * allocation of argument and result arrays.
         */
        public final long duration;

        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return record constructor.
         */
        public Data_performance_send_result(
                    long ad,
                    Resizable_buffer a0) {
            duration = ad;
            buf_5 = a0;
        }
    }


    /**
     * Call step data_performance_send of protocol data_performance_send
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @return Data_performance_send_result record containing all results, including the duration of the call.
     */
    public Data_performance_send_result data_performance_send_call(CardChannel _cc)
    {
        if(out != null) 
            out.println("start step data measure receive");
        APDU_Serializable[] call_args = null;

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res
        };

        long start = System.nanoTime();
        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.data_performance_send_step.arguments);
        protocol_description.data_performance_send_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.data_performance_send_step.results);
        long duration = System.nanoTime() - start;
        if(out != null)
            out.println("finished step data measure receive");
        return new Data_performance_send_result(duration, _buf_5_host_res);
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_perf_proof
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step data_perf_proof_commit
    // 

    /**
     * Result record for step data_perf_proof_commit of
     * protocol data_perf_proof.
     */
    public static class Data_perf_proof_commit_result {
        /**
         * Return value buf_1.
         */
        public final Resizable_buffer buf_1;
        /**
         * Return value buf_2.
         */
        public final Resizable_buffer buf_2;
        /**
         * Return value buf_3.
         */
        public final Resizable_buffer buf_3;
        /**
         * Return record constructor.
         */
        public Data_perf_proof_commit_result(
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2) {
            buf_1 = a0;
            buf_2 = a1;
            buf_3 = a2;
        }
    }


    /**
     * Call step data_perf_proof_commit of protocol data_perf_proof
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @param _buf_0_host_arg argument buf_0
     * @return Data_perf_proof_commit_result record containing all results.
     */
    public Data_perf_proof_commit_result data_perf_proof_commit_call(CardChannel _cc,
                                         Resizable_buffer _buf_0_host_arg)
    {
        if(out != null) 
            out.println("start step card commit");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_0_host_arg
        };

        Resizable_buffer _buf_1_host_res = new Resizable_buffer(protocol_description.buf_1.size());
        Resizable_buffer _buf_2_host_res = new Resizable_buffer(protocol_description.buf_2.size());
        Resizable_buffer _buf_3_host_res = new Resizable_buffer(protocol_description.buf_3.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_1_host_res,
            _buf_2_host_res,
            _buf_3_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.data_perf_proof_commit_step.arguments);
        protocol_description.data_perf_proof_commit_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.data_perf_proof_commit_step.results);
        if(out != null)
            out.println("finished step card commit");
        return new Data_perf_proof_commit_result(_buf_1_host_res, _buf_2_host_res, _buf_3_host_res);
    }


    //#########################################################################
    // Step data_perf_answer_to_challenge
    // 

    /**
     * Result record for step data_perf_answer_to_challenge of
     * protocol data_perf_proof.
     */
    public static class Data_perf_answer_to_challenge_result {
        /**
         * Return value buf_5.
         */
        public final Resizable_buffer buf_5;
        /**
         * Return value buf_6.
         */
        public final Resizable_buffer buf_6;
        /**
         * Return value buf_7.
         */
        public final Resizable_buffer buf_7;
        /**
         * Return value buf_8.
         */
        public final Resizable_buffer buf_8;
        /**
         * Return value buf_9.
         */
        public final Resizable_buffer buf_9;
        /**
         * Return record constructor.
         */
        public Data_perf_answer_to_challenge_result(
                    Resizable_buffer a0,
                    Resizable_buffer a1,
                    Resizable_buffer a2,
                    Resizable_buffer a3,
                    Resizable_buffer a4) {
            buf_5 = a0;
            buf_6 = a1;
            buf_7 = a2;
            buf_8 = a3;
            buf_9 = a4;
        }
    }


    /**
     * Call step data_perf_answer_to_challenge of protocol data_perf_proof
     * in the test environment.
     * 
     * @param _cc ignored here, can be null, card channel in {@link Data_protocol_stubs}
     * @param _buf_4_host_arg argument buf_4
     * @return Data_perf_answer_to_challenge_result record containing all results.
     */
    public Data_perf_answer_to_challenge_result data_perf_answer_to_challenge_call(CardChannel _cc,
                                         Resizable_buffer _buf_4_host_arg)
    {
        if(out != null) 
            out.println("start step card response to challenge");
        APDU_Serializable[] call_args = new APDU_Serializable[]{
            _buf_4_host_arg
        };

        Resizable_buffer _buf_5_host_res = new Resizable_buffer(protocol_description.buf_5.size());
        Resizable_buffer _buf_6_host_res = new Resizable_buffer(protocol_description.buf_6.size());
        Resizable_buffer _buf_7_host_res = new Resizable_buffer(protocol_description.buf_7.size());
        Resizable_buffer _buf_8_host_res = new Resizable_buffer(protocol_description.buf_8.size());
        Resizable_buffer _buf_9_host_res = new Resizable_buffer(protocol_description.buf_9.size());
        APDU_Serializable[] call_res = new APDU_Serializable[]{
            _buf_5_host_res,
            _buf_6_host_res,
            _buf_7_host_res,
            _buf_8_host_res,
            _buf_9_host_res
        };

        Convert_serializable.array_to(
                out, "arg[%d] = ",
                call_args,
                protocol_description.data_perf_answer_to_challenge_step.arguments);
        protocol_description.data_perf_answer_to_challenge_step.method.method();
        Convert_serializable.array_from(
                out, "res[%d] = ",
                call_res,
                protocol_description.data_perf_answer_to_challenge_step.results);
        if(out != null)
            out.println("finished step card response to challenge");
        return new Data_perf_answer_to_challenge_result(_buf_5_host_res, _buf_6_host_res, _buf_7_host_res, _buf_8_host_res, _buf_9_host_res);
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
     * with {@link Data_protocol_stubs}, it is ignored here.
     *
     * @param d protocol description instance for Data_protocol.id
     * @param o channel for printing debugging information, pass null 
     *           for disabling debugging information
     * @param script ignored here, controls printing of  apdutool lines
     *           in {@link Data_protocol_stubs}
     */
     
    public Data_protocol_test_stubs(Data_protocol_description d,
                                    PrintWriter o, 
                                    boolean script) {
        out = o;
        protocol_description = d;
        return;
    }
}

