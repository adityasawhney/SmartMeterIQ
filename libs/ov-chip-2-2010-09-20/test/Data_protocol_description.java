// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Data_protocol.id
// by some sort of idl compiler.

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif


#if defined(JAVACARD_APPLET) || defined(JAVADOC)
  import javacard.framework.APDU;
#endif
#if !defined(JAVACARD_APPLET) || defined(JAVADOC)
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Protocol_step;
  import ds.ov2.util.Protocol;
  import ds.ov2.util.Resizable_buffer;
  import ds.ov2.util.APDU_boolean;
  import ds.ov2.util.APDU_short_array;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

/**
 * Protocol description for Data_protocol. Defines suitable Protocol's and Protocol_steps for all protocols described in Data_protocol.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from Data_protocol.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class Data_protocol_description {

    //#########################################################################
    // Variable declarations
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)
        /**
         * Card variable declararion from Data_protocol.id.
         * <P>
         * Only available if either JAVACARD_APPLET
         * or APPLET_TESTFRAME is defined.
         */
        /* package local */ Data_protocol_card data;

    #endif

    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Test_protocols test_protocols;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_0;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_1;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_2;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_3;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_4;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_5;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_6;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_7;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_8;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ Resizable_buffer buf_9;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ APDU_short_array buf_sizes;
    /**
     * Variable declaration from Data_protocol.id.
     */
    /* package local */ APDU_boolean performance_test;


    #include "Data_protocol_description_include.java"


    //#########################################################################
    //#########################################################################
    // 
    // Protocol check_data
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step check_data in protocol check_data.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class check_data_call implements Void_method {
          /** Empty constructor. */
          /* package */ check_data_call() {}

          /**
           * Run the card action for step check_data in protocol check_data.
           */
          public void method() { 
              if(!performance_test.value) 
                data.check(buf_0, buf_1, buf_2, buf_3, buf_4, 
                           buf_5, buf_6, buf_7, buf_8, buf_9);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step check_data in protocol check_data.
     */
    Protocol_step check_data_step;



    /**
     * Initialize {@link #check_data_step}.
     * Initialize the step instance for step check_data in protocol check_data.
     */
    private void init_check_data_step() {
        if(check_data_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            buf_0,
            buf_1,
            buf_2,
            buf_3,
            buf_4
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            buf_5,
            buf_6,
            buf_7,
            buf_8,
            buf_9
        };

        check_data_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new check_data_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #check_data_step}.
     * Update argument and result references in the step check_data
     * of protocol check_data.
     */
    public void update_check_data_step() {
        ASSERT(check_data_step != null);

        check_data_step.arguments[0] = buf_0;
        check_data_step.arguments[1] = buf_1;
        check_data_step.arguments[2] = buf_2;
        check_data_step.arguments[3] = buf_3;
        check_data_step.arguments[4] = buf_4;
        check_data_step.results[0] = buf_5;
        check_data_step.results[1] = buf_6;
        check_data_step.results[2] = buf_7;
        check_data_step.results[3] = buf_8;
        check_data_step.results[4] = buf_9;
        return;
    }



    //#########################################################################
    // check_data protocol definition
    // 

    /**
     * Protocol instance for protocol check_data.
     */
    public Protocol check_data_protocol;

    /**
     * Initialize {@link #check_data_protocol}.
     * Initialize the protocol instance for protocol check_data.
     */
    private void init_check_data_protocol() {
        if(check_data_protocol != null)
            return;

        init_check_data_step();

        Protocol_step[] steps = new Protocol_step[]{
            check_data_step
        };
        check_data_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #check_data_protocol}.
     * Update argument and result references in all 
     * steps of protocol check_data.
     */
    public void update_check_data_protocol() {
        update_check_data_step();
        check_data_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol set_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step set_size in protocol set_size.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class set_size_call implements Void_method {
          /** Empty constructor. */
          /* package */ set_size_call() {}

          /**
           * Run the card action for step set_size in protocol set_size.
           */
          public void method() { 
              set_size();
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step set_size in protocol set_size.
     */
    Protocol_step set_size_step;



    /**
     * Initialize {@link #set_size_step}.
     * Initialize the step instance for step set_size in protocol set_size.
     */
    private void init_set_size_step() {
        if(set_size_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            buf_sizes,
            performance_test
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            buf_sizes
        };

        set_size_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new set_size_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #set_size_step}.
     * Update argument and result references in the step set_size
     * of protocol set_size.
     */
    public void update_set_size_step() {
        ASSERT(set_size_step != null);

        set_size_step.arguments[0] = buf_sizes;
        set_size_step.arguments[1] = performance_test;
        set_size_step.results[0] = buf_sizes;
        return;
    }



    //#########################################################################
    // set_size protocol definition
    // 

    /**
     * Protocol instance for protocol set_size.
     */
    public Protocol set_size_protocol;

    /**
     * Initialize {@link #set_size_protocol}.
     * Initialize the protocol instance for protocol set_size.
     */
    private void init_set_size_protocol() {
        if(set_size_protocol != null)
            return;

        init_set_size_step();

        Protocol_step[] steps = new Protocol_step[]{
            set_size_step
        };
        set_size_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #set_size_protocol}.
     * Update argument and result references in all 
     * steps of protocol set_size.
     */
    public void update_set_size_protocol() {
        update_set_size_step();
        set_size_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_receive
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step data_performance_receive in protocol data_performance_receive.
     */
    Protocol_step data_performance_receive_step;



    /**
     * Initialize {@link #data_performance_receive_step}.
     * Initialize the step instance for step data_performance_receive in protocol data_performance_receive.
     */
    private void init_data_performance_receive_step() {
        if(data_performance_receive_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            buf_0
        };

        APDU_Serializable[] res = null;

        data_performance_receive_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #data_performance_receive_step}.
     * Update argument and result references in the step data_performance_receive
     * of protocol data_performance_receive.
     */
    public void update_data_performance_receive_step() {
        ASSERT(data_performance_receive_step != null);

        data_performance_receive_step.arguments[0] = buf_0;
        return;
    }



    //#########################################################################
    // data_performance_receive protocol definition
    // 

    /**
     * Protocol instance for protocol data_performance_receive.
     */
    public Protocol data_performance_receive_protocol;

    /**
     * Initialize {@link #data_performance_receive_protocol}.
     * Initialize the protocol instance for protocol data_performance_receive.
     */
    private void init_data_performance_receive_protocol() {
        if(data_performance_receive_protocol != null)
            return;

        init_data_performance_receive_step();

        Protocol_step[] steps = new Protocol_step[]{
            data_performance_receive_step
        };
        data_performance_receive_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #data_performance_receive_protocol}.
     * Update argument and result references in all 
     * steps of protocol data_performance_receive.
     */
    public void update_data_performance_receive_protocol() {
        update_data_performance_receive_step();
        data_performance_receive_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_performance_send
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step data_performance_send in protocol data_performance_send.
     */
    Protocol_step data_performance_send_step;



    /**
     * Initialize {@link #data_performance_send_step}.
     * Initialize the step instance for step data_performance_send in protocol data_performance_send.
     */
    private void init_data_performance_send_step() {
        if(data_performance_send_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            buf_5
        };

        data_performance_send_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #data_performance_send_step}.
     * Update argument and result references in the step data_performance_send
     * of protocol data_performance_send.
     */
    public void update_data_performance_send_step() {
        ASSERT(data_performance_send_step != null);

        data_performance_send_step.results[0] = buf_5;
        return;
    }



    //#########################################################################
    // data_performance_send protocol definition
    // 

    /**
     * Protocol instance for protocol data_performance_send.
     */
    public Protocol data_performance_send_protocol;

    /**
     * Initialize {@link #data_performance_send_protocol}.
     * Initialize the protocol instance for protocol data_performance_send.
     */
    private void init_data_performance_send_protocol() {
        if(data_performance_send_protocol != null)
            return;

        init_data_performance_send_step();

        Protocol_step[] steps = new Protocol_step[]{
            data_performance_send_step
        };
        data_performance_send_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #data_performance_send_protocol}.
     * Update argument and result references in all 
     * steps of protocol data_performance_send.
     */
    public void update_data_performance_send_protocol() {
        update_data_performance_send_step();
        data_performance_send_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol data_perf_proof
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step data_perf_proof_commit in protocol data_perf_proof.
     */
    Protocol_step data_perf_proof_commit_step;

    /**
     * Step instance for step data_perf_answer_to_challenge in protocol data_perf_proof.
     */
    Protocol_step data_perf_answer_to_challenge_step;



    /**
     * Initialize {@link #data_perf_proof_commit_step}.
     * Initialize the step instance for step data_perf_proof_commit in protocol data_perf_proof.
     */
    private void init_data_perf_proof_commit_step() {
        if(data_perf_proof_commit_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            buf_0
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            buf_1,
            buf_2,
            buf_3
        };

        data_perf_proof_commit_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #data_perf_proof_commit_step}.
     * Update argument and result references in the step data_perf_proof_commit
     * of protocol data_perf_proof.
     */
    public void update_data_perf_proof_commit_step() {
        ASSERT(data_perf_proof_commit_step != null);

        data_perf_proof_commit_step.arguments[0] = buf_0;
        data_perf_proof_commit_step.results[0] = buf_1;
        data_perf_proof_commit_step.results[1] = buf_2;
        data_perf_proof_commit_step.results[2] = buf_3;
        return;
    }


    /**
     * Initialize {@link #data_perf_answer_to_challenge_step}.
     * Initialize the step instance for step data_perf_answer_to_challenge in protocol data_perf_proof.
     */
    private void init_data_perf_answer_to_challenge_step() {
        if(data_perf_answer_to_challenge_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            buf_4
        };

        APDU_Serializable[] res = new APDU_Serializable[]{
            buf_5,
            buf_6,
            buf_7,
            buf_8,
            buf_9
        };

        data_perf_answer_to_challenge_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #data_perf_answer_to_challenge_step}.
     * Update argument and result references in the step data_perf_answer_to_challenge
     * of protocol data_perf_proof.
     */
    public void update_data_perf_answer_to_challenge_step() {
        ASSERT(data_perf_answer_to_challenge_step != null);

        data_perf_answer_to_challenge_step.arguments[0] = buf_4;
        data_perf_answer_to_challenge_step.results[0] = buf_5;
        data_perf_answer_to_challenge_step.results[1] = buf_6;
        data_perf_answer_to_challenge_step.results[2] = buf_7;
        data_perf_answer_to_challenge_step.results[3] = buf_8;
        data_perf_answer_to_challenge_step.results[4] = buf_9;
        return;
    }



    //#########################################################################
    // data_perf_proof protocol definition
    // 

    /**
     * Protocol instance for protocol data_perf_proof.
     */
    public Protocol data_perf_proof_protocol;

    /**
     * Initialize {@link #data_perf_proof_protocol}.
     * Initialize the protocol instance for protocol data_perf_proof.
     */
    private void init_data_perf_proof_protocol() {
        if(data_perf_proof_protocol != null)
            return;

        init_data_perf_proof_commit_step();
        init_data_perf_answer_to_challenge_step();

        Protocol_step[] steps = new Protocol_step[]{
            data_perf_proof_commit_step,
            data_perf_answer_to_challenge_step
        };
        data_perf_proof_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #data_perf_proof_protocol}.
     * Update argument and result references in all 
     * steps of protocol data_perf_proof.
     */
    public void update_data_perf_proof_protocol() {
        update_data_perf_proof_commit_step();
        update_data_perf_answer_to_challenge_step();
        data_perf_proof_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in Data_protocol.id.
     */
    public void update_all() {
        update_check_data_protocol();
        update_set_size_protocol();
        update_data_performance_receive_protocol();
        update_data_performance_send_protocol();
        update_data_perf_proof_protocol();
    }


    //#########################################################################
    //#########################################################################
    // 
    // constructor: initialize protocols
    // 
    //#########################################################################
    //#########################################################################

    /**
     * Construct protocol descriptions.
     * Construct and initialize the protocol descriptions
     * for all protocols described in Data_protocol.id,
     * except for those that are declared as delayed there.
     */
    public Data_protocol_description(Test_protocols test_protocols) {
        // initialize variables
        buf_0 = new Resizable_buffer(check_data_max_size, false);
        buf_1 = new Resizable_buffer(check_data_max_size, false);
        buf_2 = new Resizable_buffer(check_data_max_size, false);
        buf_3 = new Resizable_buffer(check_data_max_size, false);
        buf_4 = new Resizable_buffer(check_data_max_size, false);
        buf_5 = new Resizable_buffer(check_data_max_size, false);
        buf_6 = new Resizable_buffer(check_data_max_size, false);
        buf_7 = new Resizable_buffer(check_data_max_size, false);
        buf_8 = new Resizable_buffer(check_data_max_size, false);
        buf_9 = new Resizable_buffer(check_data_max_size, false);
        buf_sizes = new APDU_short_array((short)10);
        performance_test = new APDU_boolean();
        #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
            data = new Data_protocol_card();
        #endif

        // constructor statements
        this.test_protocols = test_protocols;

        // initialize protocols
        init_check_data_protocol();
        init_set_size_protocol();
        init_data_performance_receive_protocol();
        init_data_performance_send_protocol();
        init_data_perf_proof_protocol();
        return;
    }
}

