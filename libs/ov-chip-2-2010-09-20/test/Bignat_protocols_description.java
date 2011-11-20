// DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! DO NOT EDIT! 
//
// This file has been generated automatically from Bignat_protocols.id
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
  import ds.ov2.bignat.Modulus;
  import ds.ov2.bignat.Host_modulus;
  import ds.ov2.bignat.Resize;
  import ds.ov2.bignat.APDU_BigInteger;
#endif

#if defined(APPLET_TESTFRAME) || defined(JAVADOC)
   import ds.ov2.util.Void_method;
   import ds.ov2.util.Empty_void_method;
#endif

#ifdef JAVADOC
  import ds.ov2.bignat.RSA_exponent;
#endif


/**
 * Protocol description for Bignat_protocols. Defines suitable Protocol's and Protocol_steps for all protocols described in Bignat_protocols.id for use in the OV-chip protocol layer.
 * 
 * @author idl compiler
 * @version automatically generated from Bignat_protocols.id
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>
 */
PUBLIC class Bignat_protocols_description {

    //#########################################################################
    // Variable declarations
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)
        /**
         * Card variable declararion from Bignat_protocols.id.
         * <P>
         * Only available if either JAVACARD_APPLET
         * or APPLET_TESTFRAME is defined.
         */
        /* package local */ Performance_mult_card bignat_card;

    #endif

    /**
     * Variable declaration from Bignat_protocols.id.
     */
    /* package local */ Bignats bignats;
    /**
     * Variable declaration from Bignat_protocols.id.
     */
    /* package local */ APDU_short rounds;
    /**
     * Variable declaration from Bignat_protocols.id.
     */
    /* package local */ APDU_boolean carry;


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mont_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step mont_mult_1 in protocol mont_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mont_mult_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ mont_mult_1_call() {}

          /**
           * Run the card action for step mont_mult_1 in protocol mont_mult.
           */
          public void method() { 
              bignat_card.mont_mult(bignats.n_1, bignats.n_2, 
                                   bignats.modulus, bignats.r_1, rounds.value);
              return;
          }
      }

      /**
       * Card action for step mont_mult_2 in protocol mont_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mont_mult_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ mont_mult_2_call() {}

          /**
           * Run the card action for step mont_mult_2 in protocol mont_mult.
           */
          public void method() { 
              bignat_card.mont_mult(bignats.n_1, bignats.n_2, 
                              bignats.modulus, bignats.r_1, rounds.value);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step mont_mult_init in protocol mont_mult.
     */
    Protocol_step mont_mult_init_step;

    /**
     * Step instance for step mont_mult_1 in protocol mont_mult.
     */
    Protocol_step mont_mult_1_step;

    /**
     * Step instance for step mont_mult_2 in protocol mont_mult.
     */
    Protocol_step mont_mult_2_step;

    /**
     * Step instance for step mont_mult_result in protocol mont_mult.
     */
    Protocol_step mont_mult_result_step;



    /**
     * Initialize {@link #mont_mult_init_step}.
     * Initialize the step instance for step mont_mult_init in protocol mont_mult.
     */
    private void init_mont_mult_init_step() {
        if(mont_mult_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.n_1,
            bignats.n_2,
            bignats.modulus
        };

        APDU_Serializable[] res = null;

        mont_mult_init_step = 
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
     * Update step instance in {@link #mont_mult_init_step}.
     * Update argument and result references in the step mont_mult_init
     * of protocol mont_mult.
     */
    public void update_mont_mult_init_step() {
        ASSERT(mont_mult_init_step != null);

        mont_mult_init_step.arguments[0] = bignats.n_1;
        mont_mult_init_step.arguments[1] = bignats.n_2;
        mont_mult_init_step.arguments[2] = bignats.modulus;
        return;
    }


    /**
     * Initialize {@link #mont_mult_1_step}.
     * Initialize the step instance for step mont_mult_1 in protocol mont_mult.
     */
    private void init_mont_mult_1_step() {
        if(mont_mult_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        mont_mult_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mont_mult_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mont_mult_1_step}.
     * Update argument and result references in the step mont_mult_1
     * of protocol mont_mult.
     */
    public void update_mont_mult_1_step() {
        ASSERT(mont_mult_1_step != null);

        mont_mult_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #mont_mult_2_step}.
     * Initialize the step instance for step mont_mult_2 in protocol mont_mult.
     */
    private void init_mont_mult_2_step() {
        if(mont_mult_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        mont_mult_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mont_mult_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mont_mult_2_step}.
     * Update argument and result references in the step mont_mult_2
     * of protocol mont_mult.
     */
    public void update_mont_mult_2_step() {
        ASSERT(mont_mult_2_step != null);

        mont_mult_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #mont_mult_result_step}.
     * Initialize the step instance for step mont_mult_result in protocol mont_mult.
     */
    private void init_mont_mult_result_step() {
        if(mont_mult_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1
        };

        mont_mult_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mont_mult_result_step}.
     * Update argument and result references in the step mont_mult_result
     * of protocol mont_mult.
     */
    public void update_mont_mult_result_step() {
        ASSERT(mont_mult_result_step != null);

        mont_mult_result_step.results[0] = bignats.r_1;
        return;
    }



    //#########################################################################
    // mont_mult protocol definition
    // 

    /**
     * Protocol instance for protocol mont_mult.
     */
    public Protocol mont_mult_protocol;

    /**
     * Initialize {@link #mont_mult_protocol}.
     * Initialize the protocol instance for protocol mont_mult.
     */
    private void init_mont_mult_protocol() {
        if(mont_mult_protocol != null)
            return;

        init_mont_mult_init_step();
        init_mont_mult_1_step();
        init_mont_mult_2_step();
        init_mont_mult_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            mont_mult_init_step,
            mont_mult_1_step,
            mont_mult_2_step,
            mont_mult_result_step
        };
        mont_mult_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #mont_mult_protocol}.
     * Update argument and result references in all 
     * steps of protocol mont_mult.
     */
    public void update_mont_mult_protocol() {
        update_mont_mult_init_step();
        update_mont_mult_1_step();
        update_mont_mult_2_step();
        update_mont_mult_result_step();
        mont_mult_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol demontgomerize
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step demont_1 in protocol demontgomerize.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class demont_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ demont_1_call() {}

          /**
           * Run the card action for step demont_1 in protocol demontgomerize.
           */
          public void method() { 
              bignat_card.demontgomerize(bignats.r_1, bignats.modulus, 
                                        bignats.r_2, rounds.value);
              return;
          }
      }

      /**
       * Card action for step demont_2 in protocol demontgomerize.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class demont_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ demont_2_call() {}

          /**
           * Run the card action for step demont_2 in protocol demontgomerize.
           */
          public void method() { 
              bignat_card.demontgomerize(bignats.r_1, bignats.modulus, 
                                        bignats.r_2, rounds.value);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step demont_init in protocol demontgomerize.
     */
    Protocol_step demont_init_step;

    /**
     * Step instance for step demont_1 in protocol demontgomerize.
     */
    Protocol_step demont_1_step;

    /**
     * Step instance for step demont_2 in protocol demontgomerize.
     */
    Protocol_step demont_2_step;

    /**
     * Step instance for step demont_result in protocol demontgomerize.
     */
    Protocol_step demont_result_step;



    /**
     * Initialize {@link #demont_init_step}.
     * Initialize the step instance for step demont_init in protocol demontgomerize.
     */
    private void init_demont_init_step() {
        if(demont_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.r_1,
            bignats.modulus
        };

        APDU_Serializable[] res = null;

        demont_init_step = 
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
     * Update step instance in {@link #demont_init_step}.
     * Update argument and result references in the step demont_init
     * of protocol demontgomerize.
     */
    public void update_demont_init_step() {
        ASSERT(demont_init_step != null);

        demont_init_step.arguments[0] = bignats.r_1;
        demont_init_step.arguments[1] = bignats.modulus;
        return;
    }


    /**
     * Initialize {@link #demont_1_step}.
     * Initialize the step instance for step demont_1 in protocol demontgomerize.
     */
    private void init_demont_1_step() {
        if(demont_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        demont_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new demont_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #demont_1_step}.
     * Update argument and result references in the step demont_1
     * of protocol demontgomerize.
     */
    public void update_demont_1_step() {
        ASSERT(demont_1_step != null);

        demont_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #demont_2_step}.
     * Initialize the step instance for step demont_2 in protocol demontgomerize.
     */
    private void init_demont_2_step() {
        if(demont_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        demont_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new demont_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #demont_2_step}.
     * Update argument and result references in the step demont_2
     * of protocol demontgomerize.
     */
    public void update_demont_2_step() {
        ASSERT(demont_2_step != null);

        demont_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #demont_result_step}.
     * Initialize the step instance for step demont_result in protocol demontgomerize.
     */
    private void init_demont_result_step() {
        if(demont_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_2
        };

        demont_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #demont_result_step}.
     * Update argument and result references in the step demont_result
     * of protocol demontgomerize.
     */
    public void update_demont_result_step() {
        ASSERT(demont_result_step != null);

        demont_result_step.results[0] = bignats.r_2;
        return;
    }



    //#########################################################################
    // demontgomerize protocol definition
    // 

    /**
     * Protocol instance for protocol demontgomerize.
     */
    public Protocol demontgomerize_protocol;

    /**
     * Initialize {@link #demontgomerize_protocol}.
     * Initialize the protocol instance for protocol demontgomerize.
     */
    private void init_demontgomerize_protocol() {
        if(demontgomerize_protocol != null)
            return;

        init_demont_init_step();
        init_demont_1_step();
        init_demont_2_step();
        init_demont_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            demont_init_step,
            demont_1_step,
            demont_2_step,
            demont_result_step
        };
        demontgomerize_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #demontgomerize_protocol}.
     * Update argument and result references in all 
     * steps of protocol demontgomerize.
     */
    public void update_demontgomerize_protocol() {
        update_demont_init_step();
        update_demont_1_step();
        update_demont_2_step();
        update_demont_result_step();
        demontgomerize_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol div
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step div_1 in protocol div.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class div_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ div_1_call() {}

          /**
           * Run the card action for step div_1 in protocol div.
           */
          public void method() { 
              bignat_card.remainder_divide(bignats.n_1, bignats.n_2, 
                                          bignats.r_1, bignats.r_2, 
                                          rounds.value);
              return;
          }
      }

      /**
       * Card action for step div_2 in protocol div.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class div_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ div_2_call() {}

          /**
           * Run the card action for step div_2 in protocol div.
           */
          public void method() { 
              bignat_card.remainder_divide(bignats.n_1, bignats.n_2, 
                                          bignats.r_1, bignats.r_2, 
                                          rounds.value);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step div_init in protocol div.
     */
    Protocol_step div_init_step;

    /**
     * Step instance for step div_1 in protocol div.
     */
    Protocol_step div_1_step;

    /**
     * Step instance for step div_2 in protocol div.
     */
    Protocol_step div_2_step;

    /**
     * Step instance for step div_result in protocol div.
     */
    Protocol_step div_result_step;



    /**
     * Initialize {@link #div_init_step}.
     * Initialize the step instance for step div_init in protocol div.
     */
    private void init_div_init_step() {
        if(div_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.n_1,
            bignats.n_2
        };

        APDU_Serializable[] res = null;

        div_init_step = 
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
     * Update step instance in {@link #div_init_step}.
     * Update argument and result references in the step div_init
     * of protocol div.
     */
    public void update_div_init_step() {
        ASSERT(div_init_step != null);

        div_init_step.arguments[0] = bignats.n_1;
        div_init_step.arguments[1] = bignats.n_2;
        return;
    }


    /**
     * Initialize {@link #div_1_step}.
     * Initialize the step instance for step div_1 in protocol div.
     */
    private void init_div_1_step() {
        if(div_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        div_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new div_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #div_1_step}.
     * Update argument and result references in the step div_1
     * of protocol div.
     */
    public void update_div_1_step() {
        ASSERT(div_1_step != null);

        div_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #div_2_step}.
     * Initialize the step instance for step div_2 in protocol div.
     */
    private void init_div_2_step() {
        if(div_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        div_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new div_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #div_2_step}.
     * Update argument and result references in the step div_2
     * of protocol div.
     */
    public void update_div_2_step() {
        ASSERT(div_2_step != null);

        div_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #div_result_step}.
     * Initialize the step instance for step div_result in protocol div.
     */
    private void init_div_result_step() {
        if(div_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2
        };

        div_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #div_result_step}.
     * Update argument and result references in the step div_result
     * of protocol div.
     */
    public void update_div_result_step() {
        ASSERT(div_result_step != null);

        div_result_step.results[0] = bignats.r_1;
        div_result_step.results[1] = bignats.r_2;
        return;
    }



    //#########################################################################
    // div protocol definition
    // 

    /**
     * Protocol instance for protocol div.
     */
    public Protocol div_protocol;

    /**
     * Initialize {@link #div_protocol}.
     * Initialize the protocol instance for protocol div.
     */
    private void init_div_protocol() {
        if(div_protocol != null)
            return;

        init_div_init_step();
        init_div_1_step();
        init_div_2_step();
        init_div_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            div_init_step,
            div_1_step,
            div_2_step,
            div_result_step
        };
        div_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #div_protocol}.
     * Update argument and result references in all 
     * steps of protocol div.
     */
    public void update_div_protocol() {
        update_div_init_step();
        update_div_1_step();
        update_div_2_step();
        update_div_result_step();
        div_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol RSA_exp
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step rsa_exp_init in protocol RSA_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class rsa_exp_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ rsa_exp_init_call() {}

          /**
           * Run the card action for step rsa_exp_init in protocol RSA_exp.
           */
          public void method() { 
              bignats.rsa_exponent.set_modulus(bignats.n_2, (short)0); 
             bignats.rsa_exponent.set_exponent(bignats.s_1, bignats.r_1, 
                                               (short)0);
              return;
          }
      }

      /**
       * Card action for step rsa_exp_parts_exp in protocol RSA_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class rsa_exp_parts_exp_call implements Void_method {
          /** Empty constructor. */
          /* package */ rsa_exp_parts_exp_call() {}

          /**
           * Run the card action for step rsa_exp_parts_exp in protocol RSA_exp.
           */
          public void method() { 
              bignats.rsa_exponent.fixed_power(bignats.n_1, 
                                              bignats.r_1, (short)0);
              return;
          }
      }

      /**
       * Card action for step rsa_exp_full_exp in protocol RSA_exp.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class rsa_exp_full_exp_call implements Void_method {
          /** Empty constructor. */
          /* package */ rsa_exp_full_exp_call() {}

          /**
           * Run the card action for step rsa_exp_full_exp in protocol RSA_exp.
           */
          public void method() { 
              bignats.rsa_exponent.power(bignats.n_1, bignats.s_1, 
                                        bignats.r_2, (short)0);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step rsa_exp_init in protocol RSA_exp.
     */
    Protocol_step rsa_exp_init_step;

    /**
     * Step instance for step rsa_exp_parts_empty in protocol RSA_exp.
     */
    Protocol_step rsa_exp_parts_empty_step;

    /**
     * Step instance for step rsa_exp_parts_exp in protocol RSA_exp.
     */
    Protocol_step rsa_exp_parts_exp_step;

    /**
     * Step instance for step rsa_exp_full_empty in protocol RSA_exp.
     */
    Protocol_step rsa_exp_full_empty_step;

    /**
     * Step instance for step rsa_exp_full_exp in protocol RSA_exp.
     */
    Protocol_step rsa_exp_full_exp_step;

    /**
     * Step instance for step rsa_exp_result in protocol RSA_exp.
     */
    Protocol_step rsa_exp_result_step;



    /**
     * Initialize {@link #rsa_exp_init_step}.
     * Initialize the step instance for step rsa_exp_init in protocol RSA_exp.
     */
    private void init_rsa_exp_init_step() {
        if(rsa_exp_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.n_1,
            bignats.s_1,
            bignats.n_2
        };

        APDU_Serializable[] res = null;

        rsa_exp_init_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new rsa_exp_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #rsa_exp_init_step}.
     * Update argument and result references in the step rsa_exp_init
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_init_step() {
        ASSERT(rsa_exp_init_step != null);

        rsa_exp_init_step.arguments[0] = bignats.n_1;
        rsa_exp_init_step.arguments[1] = bignats.s_1;
        rsa_exp_init_step.arguments[2] = bignats.n_2;
        return;
    }


    /**
     * Initialize {@link #rsa_exp_parts_empty_step}.
     * Initialize the step instance for step rsa_exp_parts_empty in protocol RSA_exp.
     */
    private void init_rsa_exp_parts_empty_step() {
        if(rsa_exp_parts_empty_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        rsa_exp_parts_empty_step = 
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
     * Update step instance in {@link #rsa_exp_parts_empty_step}.
     * Update argument and result references in the step rsa_exp_parts_empty
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_parts_empty_step() {
        ASSERT(rsa_exp_parts_empty_step != null);

        return;
    }


    /**
     * Initialize {@link #rsa_exp_parts_exp_step}.
     * Initialize the step instance for step rsa_exp_parts_exp in protocol RSA_exp.
     */
    private void init_rsa_exp_parts_exp_step() {
        if(rsa_exp_parts_exp_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        rsa_exp_parts_exp_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new rsa_exp_parts_exp_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #rsa_exp_parts_exp_step}.
     * Update argument and result references in the step rsa_exp_parts_exp
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_parts_exp_step() {
        ASSERT(rsa_exp_parts_exp_step != null);

        return;
    }


    /**
     * Initialize {@link #rsa_exp_full_empty_step}.
     * Initialize the step instance for step rsa_exp_full_empty in protocol RSA_exp.
     */
    private void init_rsa_exp_full_empty_step() {
        if(rsa_exp_full_empty_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        rsa_exp_full_empty_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #rsa_exp_full_empty_step}.
     * Update argument and result references in the step rsa_exp_full_empty
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_full_empty_step() {
        ASSERT(rsa_exp_full_empty_step != null);

        return;
    }


    /**
     * Initialize {@link #rsa_exp_full_exp_step}.
     * Initialize the step instance for step rsa_exp_full_exp in protocol RSA_exp.
     */
    private void init_rsa_exp_full_exp_step() {
        if(rsa_exp_full_exp_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        rsa_exp_full_exp_step = 
            new Protocol_step(
                (byte)4,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new rsa_exp_full_exp_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #rsa_exp_full_exp_step}.
     * Update argument and result references in the step rsa_exp_full_exp
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_full_exp_step() {
        ASSERT(rsa_exp_full_exp_step != null);

        return;
    }


    /**
     * Initialize {@link #rsa_exp_result_step}.
     * Initialize the step instance for step rsa_exp_result in protocol RSA_exp.
     */
    private void init_rsa_exp_result_step() {
        if(rsa_exp_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2
        };

        rsa_exp_result_step = 
            new Protocol_step(
                (byte)5,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #rsa_exp_result_step}.
     * Update argument and result references in the step rsa_exp_result
     * of protocol RSA_exp.
     */
    public void update_rsa_exp_result_step() {
        ASSERT(rsa_exp_result_step != null);

        rsa_exp_result_step.results[0] = bignats.r_1;
        rsa_exp_result_step.results[1] = bignats.r_2;
        return;
    }



    //#########################################################################
    // RSA_exp protocol definition
    // 

    /**
     * Protocol instance for protocol RSA_exp.
     */
    public Protocol rsa_exp_protocol;

    /**
     * Initialize {@link #rsa_exp_protocol}.
     * Initialize the protocol instance for protocol RSA_exp.
     */
    private void init_rsa_exp_protocol() {
        if(rsa_exp_protocol != null)
            return;

        init_rsa_exp_init_step();
        init_rsa_exp_parts_empty_step();
        init_rsa_exp_parts_exp_step();
        init_rsa_exp_full_empty_step();
        init_rsa_exp_full_exp_step();
        init_rsa_exp_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            rsa_exp_init_step,
            rsa_exp_parts_empty_step,
            rsa_exp_parts_exp_step,
            rsa_exp_full_empty_step,
            rsa_exp_full_exp_step,
            rsa_exp_result_step
        };
        rsa_exp_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #rsa_exp_protocol}.
     * Update argument and result references in all 
     * steps of protocol RSA_exp.
     */
    public void update_rsa_exp_protocol() {
        update_rsa_exp_init_step();
        update_rsa_exp_parts_empty_step();
        update_rsa_exp_parts_exp_step();
        update_rsa_exp_full_empty_step();
        update_rsa_exp_full_exp_step();
        update_rsa_exp_result_step();
        rsa_exp_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol squared_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step sq_mult_init in protocol squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class sq_mult_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ sq_mult_init_call() {}

          /**
           * Run the card action for step sq_mult_init in protocol squared_mult.
           */
          public void method() { 
              bignats.square_exp.set_modulus(bignats.modulus.m, (short)0); 
             bignats.r_1.two(); 
             bignats.square_exp.set_exponent(bignats.r_1, 
                                             bignats.r_2, (short)0);
              return;
          }
      }

      /**
       * Card action for step sq_mult_1 in protocol squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class sq_mult_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ sq_mult_1_call() {}

          /**
           * Run the card action for step sq_mult_1 in protocol squared_mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                 bignats.r_1.squared_rsa_mult_2(bignats.n_1, bignats.n_2, 
                                                bignats.modulus,        
                                                bignats.square_exp, bignats.r_2);
              return;
          }
      }

      /**
       * Card action for step sq_mult_2 in protocol squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class sq_mult_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ sq_mult_2_call() {}

          /**
           * Run the card action for step sq_mult_2 in protocol squared_mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                 bignats.r_1.squared_rsa_mult_2(bignats.n_1, bignats.n_2, 
                                              bignats.modulus, 
                                              bignats.square_exp, bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step sq_mult_init in protocol squared_mult.
     */
    Protocol_step sq_mult_init_step;

    /**
     * Step instance for step sq_mult_1 in protocol squared_mult.
     */
    Protocol_step sq_mult_1_step;

    /**
     * Step instance for step sq_mult_2 in protocol squared_mult.
     */
    Protocol_step sq_mult_2_step;

    /**
     * Step instance for step sq_mult_result in protocol squared_mult.
     */
    Protocol_step sq_mult_result_step;



    /**
     * Initialize {@link #sq_mult_init_step}.
     * Initialize the step instance for step sq_mult_init in protocol squared_mult.
     */
    private void init_sq_mult_init_step() {
        if(sq_mult_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.n_1,
            bignats.n_2,
            bignats.modulus
        };

        APDU_Serializable[] res = null;

        sq_mult_init_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new sq_mult_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_init_step}.
     * Update argument and result references in the step sq_mult_init
     * of protocol squared_mult.
     */
    public void update_sq_mult_init_step() {
        ASSERT(sq_mult_init_step != null);

        sq_mult_init_step.arguments[0] = bignats.n_1;
        sq_mult_init_step.arguments[1] = bignats.n_2;
        sq_mult_init_step.arguments[2] = bignats.modulus;
        return;
    }


    /**
     * Initialize {@link #sq_mult_1_step}.
     * Initialize the step instance for step sq_mult_1 in protocol squared_mult.
     */
    private void init_sq_mult_1_step() {
        if(sq_mult_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        sq_mult_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new sq_mult_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_1_step}.
     * Update argument and result references in the step sq_mult_1
     * of protocol squared_mult.
     */
    public void update_sq_mult_1_step() {
        ASSERT(sq_mult_1_step != null);

        sq_mult_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #sq_mult_2_step}.
     * Initialize the step instance for step sq_mult_2 in protocol squared_mult.
     */
    private void init_sq_mult_2_step() {
        if(sq_mult_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        sq_mult_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new sq_mult_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_2_step}.
     * Update argument and result references in the step sq_mult_2
     * of protocol squared_mult.
     */
    public void update_sq_mult_2_step() {
        ASSERT(sq_mult_2_step != null);

        sq_mult_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #sq_mult_result_step}.
     * Initialize the step instance for step sq_mult_result in protocol squared_mult.
     */
    private void init_sq_mult_result_step() {
        if(sq_mult_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1
        };

        sq_mult_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_result_step}.
     * Update argument and result references in the step sq_mult_result
     * of protocol squared_mult.
     */
    public void update_sq_mult_result_step() {
        ASSERT(sq_mult_result_step != null);

        sq_mult_result_step.results[0] = bignats.r_1;
        return;
    }



    //#########################################################################
    // squared_mult protocol definition
    // 

    /**
     * Protocol instance for protocol squared_mult.
     */
    public Protocol squared_mult_protocol;

    /**
     * Initialize {@link #squared_mult_protocol}.
     * Initialize the protocol instance for protocol squared_mult.
     */
    private void init_squared_mult_protocol() {
        if(squared_mult_protocol != null)
            return;

        init_sq_mult_init_step();
        init_sq_mult_1_step();
        init_sq_mult_2_step();
        init_sq_mult_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            sq_mult_init_step,
            sq_mult_1_step,
            sq_mult_2_step,
            sq_mult_result_step
        };
        squared_mult_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #squared_mult_protocol}.
     * Update argument and result references in all 
     * steps of protocol squared_mult.
     */
    public void update_squared_mult_protocol() {
        update_sq_mult_init_step();
        update_sq_mult_1_step();
        update_sq_mult_2_step();
        update_sq_mult_result_step();
        squared_mult_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol short_squared_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step short_sq_mult_init in protocol short_squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class short_sq_mult_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ short_sq_mult_init_call() {}

          /**
           * Run the card action for step short_sq_mult_init in protocol short_squared_mult.
           */
          public void method() { 
              bignats.square_exp.set_modulus(bignats.modulus.m, (short)0); 
             bignats.r_1.two(); 
             bignats.square_exp.set_exponent(bignats.r_1, 
                                             bignats.r_2, (short)0);
              return;
          }
      }

      /**
       * Card action for step short_sq_mult_1 in protocol short_squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class short_sq_mult_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ short_sq_mult_1_call() {}

          /**
           * Run the card action for step short_sq_mult_1 in protocol short_squared_mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                 bignats.dr_1.short_squared_rsa_mult_2(bignats.s_1, 
                                                       bignats.s_2, 
                                                       bignats.square_exp, 
                                                       bignats.r_1, 
                                                       bignats.r_2);
              return;
          }
      }

      /**
       * Card action for step short_sq_mult_2 in protocol short_squared_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class short_sq_mult_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ short_sq_mult_2_call() {}

          /**
           * Run the card action for step short_sq_mult_2 in protocol short_squared_mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                 bignats.dr_1.short_squared_rsa_mult_2(bignats.s_1, 
                                                       bignats.s_2, 
                                                       bignats.square_exp, 
                                                       bignats.r_1, 
                                                       bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step short_sq_mult_init in protocol short_squared_mult.
     */
    Protocol_step short_sq_mult_init_step;

    /**
     * Step instance for step short_sq_mult_1 in protocol short_squared_mult.
     */
    Protocol_step short_sq_mult_1_step;

    /**
     * Step instance for step short_sq_mult_2 in protocol short_squared_mult.
     */
    Protocol_step short_sq_mult_2_step;

    /**
     * Step instance for step short_sq_mult_result in protocol short_squared_mult.
     */
    Protocol_step short_sq_mult_result_step;



    /**
     * Initialize {@link #short_sq_mult_init_step}.
     * Initialize the step instance for step short_sq_mult_init in protocol short_squared_mult.
     */
    private void init_short_sq_mult_init_step() {
        if(short_sq_mult_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.s_1,
            bignats.s_2,
            bignats.modulus
        };

        APDU_Serializable[] res = null;

        short_sq_mult_init_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new short_sq_mult_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_mult_init_step}.
     * Update argument and result references in the step short_sq_mult_init
     * of protocol short_squared_mult.
     */
    public void update_short_sq_mult_init_step() {
        ASSERT(short_sq_mult_init_step != null);

        short_sq_mult_init_step.arguments[0] = bignats.s_1;
        short_sq_mult_init_step.arguments[1] = bignats.s_2;
        short_sq_mult_init_step.arguments[2] = bignats.modulus;
        return;
    }


    /**
     * Initialize {@link #short_sq_mult_1_step}.
     * Initialize the step instance for step short_sq_mult_1 in protocol short_squared_mult.
     */
    private void init_short_sq_mult_1_step() {
        if(short_sq_mult_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        short_sq_mult_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new short_sq_mult_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_mult_1_step}.
     * Update argument and result references in the step short_sq_mult_1
     * of protocol short_squared_mult.
     */
    public void update_short_sq_mult_1_step() {
        ASSERT(short_sq_mult_1_step != null);

        short_sq_mult_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #short_sq_mult_2_step}.
     * Initialize the step instance for step short_sq_mult_2 in protocol short_squared_mult.
     */
    private void init_short_sq_mult_2_step() {
        if(short_sq_mult_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        short_sq_mult_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new short_sq_mult_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_mult_2_step}.
     * Update argument and result references in the step short_sq_mult_2
     * of protocol short_squared_mult.
     */
    public void update_short_sq_mult_2_step() {
        ASSERT(short_sq_mult_2_step != null);

        short_sq_mult_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #short_sq_mult_result_step}.
     * Initialize the step instance for step short_sq_mult_result in protocol short_squared_mult.
     */
    private void init_short_sq_mult_result_step() {
        if(short_sq_mult_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.dr_1
        };

        short_sq_mult_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_mult_result_step}.
     * Update argument and result references in the step short_sq_mult_result
     * of protocol short_squared_mult.
     */
    public void update_short_sq_mult_result_step() {
        ASSERT(short_sq_mult_result_step != null);

        short_sq_mult_result_step.results[0] = bignats.dr_1;
        return;
    }



    //#########################################################################
    // short_squared_mult protocol definition
    // 

    /**
     * Protocol instance for protocol short_squared_mult.
     */
    public Protocol short_squared_mult_protocol;

    /**
     * Initialize {@link #short_squared_mult_protocol}.
     * Initialize the protocol instance for protocol short_squared_mult.
     */
    private void init_short_squared_mult_protocol() {
        if(short_squared_mult_protocol != null)
            return;

        init_short_sq_mult_init_step();
        init_short_sq_mult_1_step();
        init_short_sq_mult_2_step();
        init_short_sq_mult_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            short_sq_mult_init_step,
            short_sq_mult_1_step,
            short_sq_mult_2_step,
            short_sq_mult_result_step
        };
        short_squared_mult_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #short_squared_mult_protocol}.
     * Update argument and result references in all 
     * steps of protocol short_squared_mult.
     */
    public void update_short_squared_mult_protocol() {
        update_short_sq_mult_init_step();
        update_short_sq_mult_1_step();
        update_short_sq_mult_2_step();
        update_short_sq_mult_result_step();
        short_squared_mult_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol squared_mult_4
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step sq_mult_4_init in protocol squared_mult_4.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class sq_mult_4_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ sq_mult_4_init_call() {}

          /**
           * Run the card action for step sq_mult_4_init in protocol squared_mult_4.
           */
          public void method() { 
              bignats.square_exp.set_modulus(bignats.mult_modulus.m, (short)0); 
             bignats.r_3.two(); 
             bignats.square_exp.set_exponent(bignats.r_3, 
                                             bignats.r_4, (short)0);
              return;
          }
      }

      /**
       * Card action for step sq_mult_4_2 in protocol squared_mult_4.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class sq_mult_4_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ sq_mult_4_2_call() {}

          /**
           * Run the card action for step sq_mult_4_2 in protocol squared_mult_4.
           */
          public void method() { 
              bignats.r_3.squared_rsa_mult_4(bignats.r_1, bignats.r_2, 
                                            bignats.mult_modulus,        
                                            bignats.square_exp, bignats.r_4);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step sq_mult_4_init in protocol squared_mult_4.
     */
    Protocol_step sq_mult_4_init_step;

    /**
     * Step instance for step sq_mult_4_1 in protocol squared_mult_4.
     */
    Protocol_step sq_mult_4_1_step;

    /**
     * Step instance for step sq_mult_4_2 in protocol squared_mult_4.
     */
    Protocol_step sq_mult_4_2_step;

    /**
     * Step instance for step sq_mult_4_result in protocol squared_mult_4.
     */
    Protocol_step sq_mult_4_result_step;



    /**
     * Initialize {@link #sq_mult_4_init_step}.
     * Initialize the step instance for step sq_mult_4_init in protocol squared_mult_4.
     */
    private void init_sq_mult_4_init_step() {
        if(sq_mult_4_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2,
            bignats.mult_modulus
        };

        APDU_Serializable[] res = null;

        sq_mult_4_init_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new sq_mult_4_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_4_init_step}.
     * Update argument and result references in the step sq_mult_4_init
     * of protocol squared_mult_4.
     */
    public void update_sq_mult_4_init_step() {
        ASSERT(sq_mult_4_init_step != null);

        sq_mult_4_init_step.arguments[0] = bignats.r_1;
        sq_mult_4_init_step.arguments[1] = bignats.r_2;
        sq_mult_4_init_step.arguments[2] = bignats.mult_modulus;
        return;
    }


    /**
     * Initialize {@link #sq_mult_4_1_step}.
     * Initialize the step instance for step sq_mult_4_1 in protocol squared_mult_4.
     */
    private void init_sq_mult_4_1_step() {
        if(sq_mult_4_1_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        sq_mult_4_1_step = 
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
     * Update step instance in {@link #sq_mult_4_1_step}.
     * Update argument and result references in the step sq_mult_4_1
     * of protocol squared_mult_4.
     */
    public void update_sq_mult_4_1_step() {
        ASSERT(sq_mult_4_1_step != null);

        return;
    }


    /**
     * Initialize {@link #sq_mult_4_2_step}.
     * Initialize the step instance for step sq_mult_4_2 in protocol squared_mult_4.
     */
    private void init_sq_mult_4_2_step() {
        if(sq_mult_4_2_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        sq_mult_4_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new sq_mult_4_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_4_2_step}.
     * Update argument and result references in the step sq_mult_4_2
     * of protocol squared_mult_4.
     */
    public void update_sq_mult_4_2_step() {
        ASSERT(sq_mult_4_2_step != null);

        return;
    }


    /**
     * Initialize {@link #sq_mult_4_result_step}.
     * Initialize the step instance for step sq_mult_4_result in protocol squared_mult_4.
     */
    private void init_sq_mult_4_result_step() {
        if(sq_mult_4_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_3
        };

        sq_mult_4_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #sq_mult_4_result_step}.
     * Update argument and result references in the step sq_mult_4_result
     * of protocol squared_mult_4.
     */
    public void update_sq_mult_4_result_step() {
        ASSERT(sq_mult_4_result_step != null);

        sq_mult_4_result_step.results[0] = bignats.r_3;
        return;
    }



    //#########################################################################
    // squared_mult_4 protocol definition
    // 

    /**
     * Protocol instance for protocol squared_mult_4.
     */
    public Protocol squared_mult_4_protocol;

    /**
     * Initialize {@link #squared_mult_4_protocol}.
     * Initialize the protocol instance for protocol squared_mult_4.
     */
    private void init_squared_mult_4_protocol() {
        if(squared_mult_4_protocol != null)
            return;

        init_sq_mult_4_init_step();
        init_sq_mult_4_1_step();
        init_sq_mult_4_2_step();
        init_sq_mult_4_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            sq_mult_4_init_step,
            sq_mult_4_1_step,
            sq_mult_4_2_step,
            sq_mult_4_result_step
        };
        squared_mult_4_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #squared_mult_4_protocol}.
     * Update argument and result references in all 
     * steps of protocol squared_mult_4.
     */
    public void update_squared_mult_4_protocol() {
        update_sq_mult_4_init_step();
        update_sq_mult_4_1_step();
        update_sq_mult_4_2_step();
        update_sq_mult_4_result_step();
        squared_mult_4_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol short_square_4_mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step short_sq_4_mult_init in protocol short_square_4_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class short_sq_4_mult_init_call implements Void_method {
          /** Empty constructor. */
          /* package */ short_sq_4_mult_init_call() {}

          /**
           * Run the card action for step short_sq_4_mult_init in protocol short_square_4_mult.
           */
          public void method() { 
              bignats.square_exp.set_modulus(bignats.modulus.m, (short)0); 
             bignats.r_1.two(); 
             bignats.square_exp.set_exponent(bignats.r_1, 
                                             bignats.r_2, (short)0);
              return;
          }
      }

      /**
       * Card action for step short_sq_4_mult_2 in protocol short_square_4_mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class short_sq_4_mult_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ short_sq_4_mult_2_call() {}

          /**
           * Run the card action for step short_sq_4_mult_2 in protocol short_square_4_mult.
           */
          public void method() { 
              bignats.dr_1.short_squared_rsa_mult_4(bignats.s_1, 
                                                   bignats.s_2, 
                                                   bignats.square_exp, 
                                                   bignats.r_1, 
                                                   bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step short_sq_4_mult_init in protocol short_square_4_mult.
     */
    Protocol_step short_sq_4_mult_init_step;

    /**
     * Step instance for step short_sq_4_mult_1 in protocol short_square_4_mult.
     */
    Protocol_step short_sq_4_mult_1_step;

    /**
     * Step instance for step short_sq_4_mult_2 in protocol short_square_4_mult.
     */
    Protocol_step short_sq_4_mult_2_step;

    /**
     * Step instance for step short_sq_4_mult_result in protocol short_square_4_mult.
     */
    Protocol_step short_sq_4_mult_result_step;



    /**
     * Initialize {@link #short_sq_4_mult_init_step}.
     * Initialize the step instance for step short_sq_4_mult_init in protocol short_square_4_mult.
     */
    private void init_short_sq_4_mult_init_step() {
        if(short_sq_4_mult_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.s_1,
            bignats.s_2,
            bignats.modulus
        };

        APDU_Serializable[] res = null;

        short_sq_4_mult_init_step = 
            new Protocol_step(
                (byte)0,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new short_sq_4_mult_init_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_4_mult_init_step}.
     * Update argument and result references in the step short_sq_4_mult_init
     * of protocol short_square_4_mult.
     */
    public void update_short_sq_4_mult_init_step() {
        ASSERT(short_sq_4_mult_init_step != null);

        short_sq_4_mult_init_step.arguments[0] = bignats.s_1;
        short_sq_4_mult_init_step.arguments[1] = bignats.s_2;
        short_sq_4_mult_init_step.arguments[2] = bignats.modulus;
        return;
    }


    /**
     * Initialize {@link #short_sq_4_mult_1_step}.
     * Initialize the step instance for step short_sq_4_mult_1 in protocol short_square_4_mult.
     */
    private void init_short_sq_4_mult_1_step() {
        if(short_sq_4_mult_1_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        short_sq_4_mult_1_step = 
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
     * Update step instance in {@link #short_sq_4_mult_1_step}.
     * Update argument and result references in the step short_sq_4_mult_1
     * of protocol short_square_4_mult.
     */
    public void update_short_sq_4_mult_1_step() {
        ASSERT(short_sq_4_mult_1_step != null);

        return;
    }


    /**
     * Initialize {@link #short_sq_4_mult_2_step}.
     * Initialize the step instance for step short_sq_4_mult_2 in protocol short_square_4_mult.
     */
    private void init_short_sq_4_mult_2_step() {
        if(short_sq_4_mult_2_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        short_sq_4_mult_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new short_sq_4_mult_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_4_mult_2_step}.
     * Update argument and result references in the step short_sq_4_mult_2
     * of protocol short_square_4_mult.
     */
    public void update_short_sq_4_mult_2_step() {
        ASSERT(short_sq_4_mult_2_step != null);

        return;
    }


    /**
     * Initialize {@link #short_sq_4_mult_result_step}.
     * Initialize the step instance for step short_sq_4_mult_result in protocol short_square_4_mult.
     */
    private void init_short_sq_4_mult_result_step() {
        if(short_sq_4_mult_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.dr_1
        };

        short_sq_4_mult_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #short_sq_4_mult_result_step}.
     * Update argument and result references in the step short_sq_4_mult_result
     * of protocol short_square_4_mult.
     */
    public void update_short_sq_4_mult_result_step() {
        ASSERT(short_sq_4_mult_result_step != null);

        short_sq_4_mult_result_step.results[0] = bignats.dr_1;
        return;
    }



    //#########################################################################
    // short_square_4_mult protocol definition
    // 

    /**
     * Protocol instance for protocol short_square_4_mult.
     */
    public Protocol short_square_4_mult_protocol;

    /**
     * Initialize {@link #short_square_4_mult_protocol}.
     * Initialize the protocol instance for protocol short_square_4_mult.
     */
    private void init_short_square_4_mult_protocol() {
        if(short_square_4_mult_protocol != null)
            return;

        init_short_sq_4_mult_init_step();
        init_short_sq_4_mult_1_step();
        init_short_sq_4_mult_2_step();
        init_short_sq_4_mult_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            short_sq_4_mult_init_step,
            short_sq_4_mult_1_step,
            short_sq_4_mult_2_step,
            short_sq_4_mult_result_step
        };
        short_square_4_mult_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #short_square_4_mult_protocol}.
     * Update argument and result references in all 
     * steps of protocol short_square_4_mult.
     */
    public void update_short_square_4_mult_protocol() {
        update_short_sq_4_mult_init_step();
        update_short_sq_4_mult_1_step();
        update_short_sq_4_mult_2_step();
        update_short_sq_4_mult_result_step();
        short_square_4_mult_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol add
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step add_2 in protocol add.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class add_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ add_2_call() {}

          /**
           * Run the card action for step add_2 in protocol add.
           */
          public void method() { 
              bignats.r_1.add(bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step add_init in protocol add.
     */
    Protocol_step add_init_step;

    /**
     * Step instance for step add_1 in protocol add.
     */
    Protocol_step add_1_step;

    /**
     * Step instance for step add_2 in protocol add.
     */
    Protocol_step add_2_step;

    /**
     * Step instance for step add_result in protocol add.
     */
    Protocol_step add_result_step;



    /**
     * Initialize {@link #add_init_step}.
     * Initialize the step instance for step add_init in protocol add.
     */
    private void init_add_init_step() {
        if(add_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2
        };

        APDU_Serializable[] res = null;

        add_init_step = 
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
     * Update step instance in {@link #add_init_step}.
     * Update argument and result references in the step add_init
     * of protocol add.
     */
    public void update_add_init_step() {
        ASSERT(add_init_step != null);

        add_init_step.arguments[0] = bignats.r_1;
        add_init_step.arguments[1] = bignats.r_2;
        return;
    }


    /**
     * Initialize {@link #add_1_step}.
     * Initialize the step instance for step add_1 in protocol add.
     */
    private void init_add_1_step() {
        if(add_1_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        add_1_step = 
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
     * Update step instance in {@link #add_1_step}.
     * Update argument and result references in the step add_1
     * of protocol add.
     */
    public void update_add_1_step() {
        ASSERT(add_1_step != null);

        return;
    }


    /**
     * Initialize {@link #add_2_step}.
     * Initialize the step instance for step add_2 in protocol add.
     */
    private void init_add_2_step() {
        if(add_2_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        add_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new add_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #add_2_step}.
     * Update argument and result references in the step add_2
     * of protocol add.
     */
    public void update_add_2_step() {
        ASSERT(add_2_step != null);

        return;
    }


    /**
     * Initialize {@link #add_result_step}.
     * Initialize the step instance for step add_result in protocol add.
     */
    private void init_add_result_step() {
        if(add_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1
        };

        add_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #add_result_step}.
     * Update argument and result references in the step add_result
     * of protocol add.
     */
    public void update_add_result_step() {
        ASSERT(add_result_step != null);

        add_result_step.results[0] = bignats.r_1;
        return;
    }



    //#########################################################################
    // add protocol definition
    // 

    /**
     * Protocol instance for protocol add.
     */
    public Protocol add_protocol;

    /**
     * Initialize {@link #add_protocol}.
     * Initialize the protocol instance for protocol add.
     */
    private void init_add_protocol() {
        if(add_protocol != null)
            return;

        init_add_init_step();
        init_add_1_step();
        init_add_2_step();
        init_add_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            add_init_step,
            add_1_step,
            add_2_step,
            add_result_step
        };
        add_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #add_protocol}.
     * Update argument and result references in all 
     * steps of protocol add.
     */
    public void update_add_protocol() {
        update_add_init_step();
        update_add_1_step();
        update_add_2_step();
        update_add_result_step();
        add_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol subtract
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step subtract_2 in protocol subtract.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class subtract_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ subtract_2_call() {}

          /**
           * Run the card action for step subtract_2 in protocol subtract.
           */
          public void method() { 
              carry.value = bignats.r_1.subtract(bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step subtract_init in protocol subtract.
     */
    Protocol_step subtract_init_step;

    /**
     * Step instance for step subtract_1 in protocol subtract.
     */
    Protocol_step subtract_1_step;

    /**
     * Step instance for step subtract_2 in protocol subtract.
     */
    Protocol_step subtract_2_step;

    /**
     * Step instance for step subtract_result in protocol subtract.
     */
    Protocol_step subtract_result_step;



    /**
     * Initialize {@link #subtract_init_step}.
     * Initialize the step instance for step subtract_init in protocol subtract.
     */
    private void init_subtract_init_step() {
        if(subtract_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2
        };

        APDU_Serializable[] res = null;

        subtract_init_step = 
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
     * Update step instance in {@link #subtract_init_step}.
     * Update argument and result references in the step subtract_init
     * of protocol subtract.
     */
    public void update_subtract_init_step() {
        ASSERT(subtract_init_step != null);

        subtract_init_step.arguments[0] = bignats.r_1;
        subtract_init_step.arguments[1] = bignats.r_2;
        return;
    }


    /**
     * Initialize {@link #subtract_1_step}.
     * Initialize the step instance for step subtract_1 in protocol subtract.
     */
    private void init_subtract_1_step() {
        if(subtract_1_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        subtract_1_step = 
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
     * Update step instance in {@link #subtract_1_step}.
     * Update argument and result references in the step subtract_1
     * of protocol subtract.
     */
    public void update_subtract_1_step() {
        ASSERT(subtract_1_step != null);

        return;
    }


    /**
     * Initialize {@link #subtract_2_step}.
     * Initialize the step instance for step subtract_2 in protocol subtract.
     */
    private void init_subtract_2_step() {
        if(subtract_2_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = null;

        subtract_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new subtract_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #subtract_2_step}.
     * Update argument and result references in the step subtract_2
     * of protocol subtract.
     */
    public void update_subtract_2_step() {
        ASSERT(subtract_2_step != null);

        return;
    }


    /**
     * Initialize {@link #subtract_result_step}.
     * Initialize the step instance for step subtract_result in protocol subtract.
     */
    private void init_subtract_result_step() {
        if(subtract_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.r_1,
            carry
        };

        subtract_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #subtract_result_step}.
     * Update argument and result references in the step subtract_result
     * of protocol subtract.
     */
    public void update_subtract_result_step() {
        ASSERT(subtract_result_step != null);

        subtract_result_step.results[0] = bignats.r_1;
        subtract_result_step.results[1] = carry;
        return;
    }



    //#########################################################################
    // subtract protocol definition
    // 

    /**
     * Protocol instance for protocol subtract.
     */
    public Protocol subtract_protocol;

    /**
     * Initialize {@link #subtract_protocol}.
     * Initialize the protocol instance for protocol subtract.
     */
    private void init_subtract_protocol() {
        if(subtract_protocol != null)
            return;

        init_subtract_init_step();
        init_subtract_1_step();
        init_subtract_2_step();
        init_subtract_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            subtract_init_step,
            subtract_1_step,
            subtract_2_step,
            subtract_result_step
        };
        subtract_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #subtract_protocol}.
     * Update argument and result references in all 
     * steps of protocol subtract.
     */
    public void update_subtract_protocol() {
        update_subtract_init_step();
        update_subtract_1_step();
        update_subtract_2_step();
        update_subtract_result_step();
        subtract_protocol.set_result_sizes();
    }


    //#########################################################################
    //#########################################################################
    // 
    // Protocol mult
    // 
    //#########################################################################
    //#########################################################################

    //#########################################################################
    // Step methods
    // 

    #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME) || defined(JAVADOC)

      /**
       * Card action for step mult_1 in protocol mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mult_1_call implements Void_method {
          /** Empty constructor. */
          /* package */ mult_1_call() {}

          /**
           * Run the card action for step mult_1 in protocol mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                bignats.dr_1.mult(bignats.r_1, bignats.r_2);
              return;
          }
      }

      /**
       * Card action for step mult_2 in protocol mult.
       * <P>
       * Only available if JAVACARD_APPLET or APPLET_TESTFRAME is defined.
       */
      /* package local */ class mult_2_call implements Void_method {
          /** Empty constructor. */
          /* package */ mult_2_call() {}

          /**
           * Run the card action for step mult_2 in protocol mult.
           */
          public void method() { 
              for(short i = 0; i < rounds.value; i++) 
                bignats.dr_1.mult(bignats.r_1, bignats.r_2);
              return;
          }
      }

    #endif


    //#########################################################################
    // Steps
    // 

    /**
     * Step instance for step mult_init in protocol mult.
     */
    Protocol_step mult_init_step;

    /**
     * Step instance for step mult_1 in protocol mult.
     */
    Protocol_step mult_1_step;

    /**
     * Step instance for step mult_2 in protocol mult.
     */
    Protocol_step mult_2_step;

    /**
     * Step instance for step mult_result in protocol mult.
     */
    Protocol_step mult_result_step;



    /**
     * Initialize {@link #mult_init_step}.
     * Initialize the step instance for step mult_init in protocol mult.
     */
    private void init_mult_init_step() {
        if(mult_init_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            bignats.r_1,
            bignats.r_2
        };

        APDU_Serializable[] res = null;

        mult_init_step = 
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
     * Update step instance in {@link #mult_init_step}.
     * Update argument and result references in the step mult_init
     * of protocol mult.
     */
    public void update_mult_init_step() {
        ASSERT(mult_init_step != null);

        mult_init_step.arguments[0] = bignats.r_1;
        mult_init_step.arguments[1] = bignats.r_2;
        return;
    }


    /**
     * Initialize {@link #mult_1_step}.
     * Initialize the step instance for step mult_1 in protocol mult.
     */
    private void init_mult_1_step() {
        if(mult_1_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        mult_1_step = 
            new Protocol_step(
                (byte)1,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mult_1_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mult_1_step}.
     * Update argument and result references in the step mult_1
     * of protocol mult.
     */
    public void update_mult_1_step() {
        ASSERT(mult_1_step != null);

        mult_1_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #mult_2_step}.
     * Initialize the step instance for step mult_2 in protocol mult.
     */
    private void init_mult_2_step() {
        if(mult_2_step != null) 
            return;

        APDU_Serializable[] args = new APDU_Serializable[]{
            rounds
        };

        APDU_Serializable[] res = null;

        mult_2_step = 
            new Protocol_step(
                (byte)2,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new mult_2_call(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mult_2_step}.
     * Update argument and result references in the step mult_2
     * of protocol mult.
     */
    public void update_mult_2_step() {
        ASSERT(mult_2_step != null);

        mult_2_step.arguments[0] = rounds;
        return;
    }


    /**
     * Initialize {@link #mult_result_step}.
     * Initialize the step instance for step mult_result in protocol mult.
     */
    private void init_mult_result_step() {
        if(mult_result_step != null) 
            return;

        APDU_Serializable[] args = null;

        APDU_Serializable[] res = new APDU_Serializable[]{
            bignats.dr_1
        };

        mult_result_step = 
            new Protocol_step(
                (byte)3,          // P1
                args,                 // args
                #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
                    new Empty_void_method(),      // method
                #endif
                res                   // results
            );
        return;
    }


    /**
     * Update step instance in {@link #mult_result_step}.
     * Update argument and result references in the step mult_result
     * of protocol mult.
     */
    public void update_mult_result_step() {
        ASSERT(mult_result_step != null);

        mult_result_step.results[0] = bignats.dr_1;
        return;
    }



    //#########################################################################
    // mult protocol definition
    // 

    /**
     * Protocol instance for protocol mult.
     */
    public Protocol mult_protocol;

    /**
     * Initialize {@link #mult_protocol}.
     * Initialize the protocol instance for protocol mult.
     */
    private void init_mult_protocol() {
        if(mult_protocol != null)
            return;

        init_mult_init_step();
        init_mult_1_step();
        init_mult_2_step();
        init_mult_result_step();

        Protocol_step[] steps = new Protocol_step[]{
            mult_init_step,
            mult_1_step,
            mult_2_step,
            mult_result_step
        };
        mult_protocol = new Protocol(steps);
        return;
    }


    /**
     * Update {@link #mult_protocol}.
     * Update argument and result references in all 
     * steps of protocol mult.
     */
    public void update_mult_protocol() {
        update_mult_init_step();
        update_mult_1_step();
        update_mult_2_step();
        update_mult_result_step();
        mult_protocol.set_result_sizes();
    }


    /**
     * Update all protocols in this object.
     * Update all argument and result references in all
     * steps of all protocol instances described in Bignat_protocols.id.
     */
    public void update_all() {
        update_mont_mult_protocol();
        update_demontgomerize_protocol();
        update_div_protocol();
        update_rsa_exp_protocol();
        update_squared_mult_protocol();
        update_short_squared_mult_protocol();
        update_squared_mult_4_protocol();
        update_short_square_4_mult_protocol();
        update_add_protocol();
        update_subtract_protocol();
        update_mult_protocol();
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
     * for all protocols described in Bignat_protocols.id,
     * except for those that are declared as delayed there.
     */
    public Bignat_protocols_description(Bignats bignats) {
        // initialize variables
        rounds = new APDU_short();
        carry = new APDU_boolean();
        #if defined(JAVACARD_APPLET) || defined(APPLET_TESTFRAME)
            bignat_card = new Performance_mult_card();
        #endif

        // constructor statements
        this.bignats = bignats;

        // initialize protocols
        init_mont_mult_protocol();
        init_demontgomerize_protocol();
        init_div_protocol();
        init_rsa_exp_protocol();
        init_squared_mult_protocol();
        init_short_squared_mult_protocol();
        init_squared_mult_4_protocol();
        init_short_square_4_mult_protocol();
        init_add_protocol();
        init_subtract_protocol();
        init_mult_protocol();
        return;
    }
}

