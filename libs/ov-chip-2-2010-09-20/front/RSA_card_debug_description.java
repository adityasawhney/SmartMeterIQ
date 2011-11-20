// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from RSA_card_debug.id
// by some sort of idl compiler.

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.front;
#endif


#if defined(JAVACARD_APPLET) || defined(JAVADOC)
  import javacard.framework.APDU;
  import javacard.framework.JCSystem;
#endif
#if !defined(JAVACARD_APPLET) || defined(JAVADOC)
  import ds.ov2.util.APDU_Serializable;
  import ds.ov2.util.Protocol_step;
  import ds.ov2.util.Protocol;
  import java.math.BigInteger;;
  import ds.ov2.util.APDU_byte;;
  import ds.ov2.util.APDU_short;;
  import ds.ov2.util.APDU_boolean;;
  import ds.ov2.bignat.Host_modulus;;
  import ds.ov2.bignat.Host_vector;;
  import ds.ov2.bignat.APDU_BigInteger;;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

/**
 * Protocol description for RSA_card_debug. Defines suitable Protocol's and Protocol_steps for all protocols described in RSA_card_debug.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from RSA_card_debug.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class RSA_card_debug_description {

    //#########################################################################
    // Variable declarations
    // 

    /**
     * Variable declaration from RSA_card_debug.id.
     */
    /* package local */ RSA_data data;
    /**
     * Variable declaration from RSA_card_debug.id.
     */
    /* package local */ APDU_short mem_persistent;
    /**
     * Variable declaration from RSA_card_debug.id.
     */
    /* package local */ APDU_short mem_transient_reset;
    /**
     * Variable declaration from RSA_card_debug.id.
     */
    /* package local */ APDU_short mem_transient_deselect;
    /**
     * Variable declaration from RSA_card_debug.id.
     */
    /* package local */ APDU_boolean assertions_on;


    //#########################################################################
    //#########################################################################
    // 
    // Protocol status
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
     * Step instance for step get in protocol status.
     */
    Protocol_step get_step;



    /**
     * Initialize {@link #get_step}.
     * Initialize the step instance for step get in protocol status.
     */
    private void init_get_step() {
        if(get_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            data.applet_id,
            data.n,
            data.v,
            data.ptls_key,
            data.bases,
            data.current_attributes,
            data.new_attributes,
            data.current_blinding,
            data.new_blinding,
            data.current_blinded_a,
            data.new_blinded_a,
            data.current_signature,
            data.new_signature,
            data.montgomery_corrections
        };

        get_step = 
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
     * Update step instance in {@link #get_step}.
     * Update argument and result references in the step get
     * of protocol status.
     */
    public void update_get_step() {
        ASSERT(get_step != null);

        get_step.results[0] = data.applet_id;
        get_step.results[1] = data.n;
        get_step.results[2] = data.v;
        get_step.results[3] = data.ptls_key;
        get_step.results[4] = data.bases;
        get_step.results[5] = data.current_attributes;
        get_step.results[6] = data.new_attributes;
        get_step.results[7] = data.current_blinding;
        get_step.results[8] = data.new_blinding;
        get_step.results[9] = data.current_blinded_a;
        get_step.results[10] = data.new_blinded_a;
        get_step.results[11] = data.current_signature;
        get_step.results[12] = data.new_signature;
        get_step.results[13] = data.montgomery_corrections;
        return;
    }



    //#########################################################################
    // status protocol definition
    // 

    /**
     * Protocol instance for protocol status.
     */
    public Protocol status_protocol;

    /**
     * Initialize {@link #status_protocol}.
     * Initialize the protocol instance for protocol status.
     */
    private void init_status_protocol() {
        if(status_protocol != null)
            return;

        init_get_step();

        Protocol_step[] steps = new Protocol_step[]{
            get_step
        };
        status_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #status_protocol}.
     * Update argument and result references in all 
     * steps of protocol status.
     */
    public void update_status_protocol() {
        update_get_step();
        status_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mem_size
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step mem_size in protocol mem_size.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mem_size_call implements Void_method {
          /** Empty constructor. */
          /* package */ mem_size_call() {}

          /**
           * Run the card action for step mem_size in protocol mem_size.
           */
          public void method() { 
              
      #ifdef JAVACARD_APPLET 
        mem_persistent.value = 
            JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_PERSISTENT); 
        mem_transient_reset.value = 
            JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_RESET); 
        mem_transient_deselect.value = 
          JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_DESELECT) 
      #else 
        mem_persistent.value = -1; 
        mem_transient_reset.value = -1; 
        mem_transient_deselect.value = -1; 
      #endif 
;
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step mem_size in protocol mem_size.
     */
    Protocol_step mem_size_step;



    /**
     * Initialize {@link #mem_size_step}.
     * Initialize the step instance for step mem_size in protocol mem_size.
     */
    private void init_mem_size_step() {
        if(mem_size_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            assertions_on,
            mem_persistent,
            mem_transient_reset,
            mem_transient_deselect
        };

        mem_size_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mem_size_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mem_size_step}.
     * Update argument and result references in the step mem_size
     * of protocol mem_size.
     */
    public void update_mem_size_step() {
        ASSERT(mem_size_step != null);

        mem_size_step.results[0] = assertions_on;
        mem_size_step.results[1] = mem_persistent;
        mem_size_step.results[2] = mem_transient_reset;
        mem_size_step.results[3] = mem_transient_deselect;
        return;
    }



    //#########################################################################
    // mem_size protocol definition
    // 

    /**
     * Protocol instance for protocol mem_size.
     */
    public Protocol mem_size_protocol;

    /**
     * Initialize {@link #mem_size_protocol}.
     * Initialize the protocol instance for protocol mem_size.
     */
    private void init_mem_size_protocol() {
        if(mem_size_protocol != null)
            return;

        init_mem_size_step();

        Protocol_step[] steps = new Protocol_step[]{
            mem_size_step
        };
        mem_size_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #mem_size_protocol}.
     * Update argument and result references in all 
     * steps of protocol mem_size.
     */
    public void update_mem_size_protocol() {
        update_mem_size_step();
        mem_size_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol reset_applet_state
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step reset in protocol reset_applet_state.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class reset_call implements Void_method {
          /** Empty constructor. */
          /* package */ reset_call() {}

          /**
           * Run the card action for step reset in protocol reset_applet_state.
           */
          public void method() { 
              data.state = data.UNALLOCTED;
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step reset in protocol reset_applet_state.
     */
    Protocol_step reset_step;



    /**
     * Initialize {@link #reset_step}.
     * Initialize the step instance for step reset in protocol reset_applet_state.
     */
    private void init_reset_step() {
        if(reset_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        reset_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new reset_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #reset_step}.
     * Update argument and result references in the step reset
     * of protocol reset_applet_state.
     */
    public void update_reset_step() {
        ASSERT(reset_step != null);

        return;
    }



    //#########################################################################
    // reset_applet_state protocol definition
    // 

    /**
     * Protocol instance for protocol reset_applet_state.
     */
    public Protocol reset_applet_state_protocol;

    /**
     * Initialize {@link #reset_applet_state_protocol}.
     * Initialize the protocol instance for protocol reset_applet_state.
     */
    private void init_reset_applet_state_protocol() {
        if(reset_applet_state_protocol != null)
            return;

        init_reset_step();

        Protocol_step[] steps = new Protocol_step[]{
            reset_step
        };
        reset_applet_state_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #reset_applet_state_protocol}.
     * Update argument and result references in all 
     * steps of protocol reset_applet_state.
     */
    public void update_reset_applet_state_protocol() {
        update_reset_step();
        reset_applet_state_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in RSA_card_debug.id.
     */
    public void update_all() {
        update_status_protocol();
        update_mem_size_protocol();
        update_reset_applet_state_protocol();
    }


    /**
     * Initialization of delayed protocols.
     * Initialization of those protocols and their steps
     * that are declared as delayed in RSA_card_debug.id.
     */
    public void delayed_init() {
        init_status_protocol();
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
     * for all protocols described in RSA_card_debug.id,
     * except for those that are declared as delayed there.
     */
    public RSA_card_debug_description(RSA_card_protocol_description pd) {
        // initialize variables
        mem_persistent = new APDU_short();;
        mem_transient_reset = new APDU_short();;
        mem_transient_deselect = new APDU_short();;
        assertions_on = new APDU_boolean();

        // constructor statements
        data = pd.data;
        
  #ifdef NO_CARD_ASSERT 
    assertions_on.value = false; 
  #else 
    assertions_on.value = true; 
  #endif 
;

        // initialize protocols
        init_mem_size_protocol();
        init_reset_applet_state_protocol();
        return;
    }
}

