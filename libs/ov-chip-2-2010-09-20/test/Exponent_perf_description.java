// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Exponent_perf.id
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
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

/**
 * Protocol description for Exponent_perf. Defines suitable Protocol's and Protocol_steps for all protocols described in Exponent_perf.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from Exponent_perf.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class Exponent_perf_description {

    //#########################################################################
    // Variable declarations
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)
        /**
         * Card variable declararion from Exponent_perf.id.
         * <P>
         * Only available if either JAVACARD_APPLET
         * or APPLET_TESTFRAME is defined.
         */
        /* package local */ Exponent_perf_card exponent_perf_card;

    #endif

    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ Test_protocols test_protocols;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ Bignats bignats;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ Bignat one_or_correction;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ Modulus vec_exp_modulus;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ APDU_short n1;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ APDU_short n2;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ APDU_short vector_exp_variant;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ APDU_boolean keep_modulus;
    /**
     * Variable declaration from Exponent_perf.id.
     */
    /* package local */ Bignat result;


    //#########################################################################
    //#########################################################################
    // 
    // Protocol Vector_length
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step set_vector_length in protocol Vector_length.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class set_vector_length_call implements Void_method {
          /** Empty constructor. */
          /* package */ set_vector_length_call() {}

          /**
           * Run the card action for step set_vector_length in protocol Vector_length.
           */
          public void method() { 
              Resize.resize_vectors(n1.value, n2.value); 
         test_protocols.set_result_sizes();
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step set_vector_length in protocol Vector_length.
     */
    Protocol_step set_vector_length_step;



    /**
     * Initialize {@link #set_vector_length_step}.
     * Initialize the step instance for step set_vector_length in protocol Vector_length.
     */
    private void init_set_vector_length_step() {
        if(set_vector_length_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            n1,
            n2
        };

        APDU_Serializable[] res = null;

        set_vector_length_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new set_vector_length_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #set_vector_length_step}.
     * Update argument and result references in the step set_vector_length
     * of protocol Vector_length.
     */
    public void update_set_vector_length_step() {
        ASSERT(set_vector_length_step != null);

        set_vector_length_step.arguments[0] = n1;
        set_vector_length_step.arguments[1] = n2;
        return;
    }



    //#########################################################################
    // Vector_length protocol definition
    // 

    /**
     * Protocol instance for protocol Vector_length.
     */
    public Protocol vector_length_protocol;

    /**
     * Initialize {@link #vector_length_protocol}.
     * Initialize the protocol instance for protocol Vector_length.
     */
    private void init_vector_length_protocol() {
        if(vector_length_protocol != null)
            return;

        init_set_vector_length_step();

        Protocol_step[] steps = new Protocol_step[]{
            set_vector_length_step
        };
        vector_length_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #vector_length_protocol}.
     * Update argument and result references in all 
     * steps of protocol Vector_length.
     */
    public void update_vector_length_protocol() {
        update_set_vector_length_step();
        vector_length_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol Vector_exp
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step vector_exp_choose_mod in protocol Vector_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class vector_exp_choose_mod_call implements Void_method {
          /** Empty constructor. */
          /* package */ vector_exp_choose_mod_call() {}

          /**
           * Run the card action for step vector_exp_choose_mod in protocol Vector_exp.
           */
          public void method() { 
              if(vector_exp_variant.value == (short)2) { 
             #ifdef USE_SQUARED_RSA_MULT_4 
                vec_exp_modulus = bignats.mult_modulus; 
             #else 
                vec_exp_modulus = bignats.modulus; 
             #endif 
         } else { 
             vec_exp_modulus = bignats.modulus; 
         } 
         ASSERT(bignats.mult_modulus.size() == bignats.modulus.size()); 
         update_vector_exp_init_step();
              return;
          }
      }

      /**
       * Card action for step vector_exp_init in protocol Vector_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class vector_exp_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ vector_exp_init_call() {}

          /**
           * Run the card action for step vector_exp_init in protocol Vector_exp.
           */
          public void method() { 
              exponent_perf_card.vector_exp_init(vec_exp_modulus, 
                                            vector_exp_variant.value, 
                                            keep_modulus.value);
              return;
          }
      }

      /**
       * Card action for step vector_exp_1 in protocol Vector_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class vector_exp_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ vector_exp_1_call() {}

          /**
           * Run the card action for step vector_exp_1 in protocol Vector_exp.
           */
          public void method() { 
              exponent_perf_card.vector_exp(bignats.base, bignats.exponent, 
                                       vec_exp_modulus,                 
                                       n1.value, bignats.base_factors,  
                                       one_or_correction,               
                                       vector_exp_variant.value,        
                                       false);
              return;
          }
      }

      /**
       * Card action for step vector_exp_2 in protocol Vector_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class vector_exp_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ vector_exp_2_call() {}

          /**
           * Run the card action for step vector_exp_2 in protocol Vector_exp.
           */
          public void method() { 
              exponent_perf_card.vector_exp(bignats.base, bignats.exponent, 
                                       vec_exp_modulus,                
                                       n1.value, bignats.base_factors, 
                                       one_or_correction,               
                                       vector_exp_variant.value,        
                                       true);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step vector_exp_choose_mod in protocol Vector_exp.
     */
    Protocol_step vector_exp_choose_mod_step;

    /**
     * Step instance for step vector_exp_init in protocol Vector_exp.
     */
    Protocol_step vector_exp_init_step;

    /**
     * Step instance for step vector_exp_1 in protocol Vector_exp.
     */
    Protocol_step vector_exp_1_step;

    /**
     * Step instance for step vector_exp_2 in protocol Vector_exp.
     */
    Protocol_step vector_exp_2_step;

    /**
     * Step instance for step vector_exp_result in protocol Vector_exp.
     */
    Protocol_step vector_exp_result_step;



    /**
     * Initialize {@link #vector_exp_choose_mod_step}.
     * Initialize the step instance for step vector_exp_choose_mod in protocol Vector_exp.
     */
    private void init_vector_exp_choose_mod_step() {
        if(vector_exp_choose_mod_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            vector_exp_variant
        };

        APDU_Serializable[] res = null;

        vector_exp_choose_mod_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new vector_exp_choose_mod_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #vector_exp_choose_mod_step}.
     * Update argument and result references in the step vector_exp_choose_mod
     * of protocol Vector_exp.
     */
    public void update_vector_exp_choose_mod_step() {
        ASSERT(vector_exp_choose_mod_step != null);

        vector_exp_choose_mod_step.arguments[0] = vector_exp_variant;
        return;
    }


    /**
     * Initialize {@link #vector_exp_init_step}.
     * Initialize the step instance for step vector_exp_init in protocol Vector_exp.
     */
    private void init_vector_exp_init_step() {
        if(vector_exp_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.base,
            bignats.exponent,
            vec_exp_modulus,
            n1,
            bignats.base_factors,
            one_or_correction,
            keep_modulus
        };

        APDU_Serializable[] res = null;

        vector_exp_init_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new vector_exp_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #vector_exp_init_step}.
     * Update argument and result references in the step vector_exp_init
     * of protocol Vector_exp.
     */
    public void update_vector_exp_init_step() {
        ASSERT(vector_exp_init_step != null);

        vector_exp_init_step.arguments[0] = bignats.base;
        vector_exp_init_step.arguments[1] = bignats.exponent;
        vector_exp_init_step.arguments[2] = vec_exp_modulus;
        vector_exp_init_step.arguments[3] = n1;
        vector_exp_init_step.arguments[4] = bignats.base_factors;
        vector_exp_init_step.arguments[5] = one_or_correction;
        vector_exp_init_step.arguments[6] = keep_modulus;
        return;
    }


    /**
     * Initialize {@link #vector_exp_1_step}.
     * Initialize the step instance for step vector_exp_1 in protocol Vector_exp.
     */
    private void init_vector_exp_1_step() {
        if(vector_exp_1_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        vector_exp_1_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new vector_exp_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #vector_exp_1_step}.
     * Update argument and result references in the step vector_exp_1
     * of protocol Vector_exp.
     */
    public void update_vector_exp_1_step() {
        ASSERT(vector_exp_1_step != null);

        return;
    }


    /**
     * Initialize {@link #vector_exp_2_step}.
     * Initialize the step instance for step vector_exp_2 in protocol Vector_exp.
     */
    private void init_vector_exp_2_step() {
        if(vector_exp_2_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        vector_exp_2_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new vector_exp_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #vector_exp_2_step}.
     * Update argument and result references in the step vector_exp_2
     * of protocol Vector_exp.
     */
    public void update_vector_exp_2_step() {
        ASSERT(vector_exp_2_step != null);

        return;
    }


    /**
     * Initialize {@link #vector_exp_result_step}.
     * Initialize the step instance for step vector_exp_result in protocol Vector_exp.
     */
    private void init_vector_exp_result_step() {
        if(vector_exp_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            result
        };

        vector_exp_result_step = 
            new Protocol_step(
                (byte)4,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #vector_exp_result_step}.
     * Update argument and result references in the step vector_exp_result
     * of protocol Vector_exp.
     */
    public void update_vector_exp_result_step() {
        ASSERT(vector_exp_result_step != null);

        vector_exp_result_step.results[0] = result;
        return;
    }



    //#########################################################################
    // Vector_exp protocol definition
    // 

    /**
     * Protocol instance for protocol Vector_exp.
     */
    public Protocol vector_exp_protocol;

    /**
     * Initialize {@link #vector_exp_protocol}.
     * Initialize the protocol instance for protocol Vector_exp.
     */
    private void init_vector_exp_protocol() {
        if(vector_exp_protocol != null)
            return;

        init_vector_exp_choose_mod_step();
        init_vector_exp_init_step();
        init_vector_exp_1_step();
        init_vector_exp_2_step();
        init_vector_exp_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            vector_exp_choose_mod_step,
            vector_exp_init_step,
            vector_exp_1_step,
            vector_exp_2_step,
            vector_exp_result_step
        };
        vector_exp_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #vector_exp_protocol}.
     * Update argument and result references in all 
     * steps of protocol Vector_exp.
     */
    public void update_vector_exp_protocol() {
        update_vector_exp_choose_mod_step();
        update_vector_exp_init_step();
        update_vector_exp_1_step();
        update_vector_exp_2_step();
        update_vector_exp_result_step();
        vector_exp_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in Exponent_perf.id.
     */
    public void update_all() {
        update_vector_length_protocol();
        update_vector_exp_protocol();
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
     * for all protocols described in Exponent_perf.id,
     * except for those that are declared as delayed there.
     */
    public Exponent_perf_description(Test_protocols test_protocols, Bignats bignats) {
        // initialize variables
        n1 = new APDU_short();
        n2 = new APDU_short();
        vector_exp_variant = new APDU_short();
        keep_modulus = new APDU_boolean();
        #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
            exponent_perf_card = new Exponent_perf_card(bignats);
        #endif

        // constructor statements
        this.test_protocols = test_protocols;
        this.bignats = bignats;
        one_or_correction = bignats.n_1;
        vec_exp_modulus = bignats.modulus;
        result = bignats.r_1;

        // initialize protocols
        init_vector_length_protocol();
        init_vector_exp_protocol();
        return;
    }
}

