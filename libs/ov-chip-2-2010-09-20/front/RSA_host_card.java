// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2008, 2009
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of
// the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// General Public License in file COPYING in this or one of the
// parent directories for more details.
// 
// Created 19.11.08 by Hendrik
// 
// host size of a card
// 
// $Id: RSA_host_card.java,v 1.46 2010-09-20 11:20:15 tews Exp $

#include <config>

package ds.ov2.front;


import java.io.PrintWriter;
import java.util.Random;
import java.math.BigInteger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import ds.ov2.util.Misc_host;
import ds.ov2.util.BigIntUtil;
import ds.ov2.bignat.Host_vector;
import ds.ov2.front.RSA_CARD_PROTOCOL_STUBS.Get_signature_result;
import ds.ov2.front.RSA_CARD_PROTOCOL_STUBS.Commit_result;
import ds.ov2.front.RSA_CARD_PROTOCOL_STUBS.Respond_result;
import ds.ov2.front.RSA_CARD_PROTOCOL_STUBS.Make_sig_hash_result;
import ds.ov2.front.RSA_CARD_PROTOCOL_STUBS.Finish_signature_result;


/** 
 * Host driver for all RSA applets. This
 * class contains methods for all tasks for these applets. These
 * methods run the host side of the protocols. This class only covers
 * the normal protocols. Additional host driver code for the debug
 * protcol is in {@link RSA_card_debug}.
 * <P>
 *
 * The code in this class can deal with both, the plain RSA applet and
 * the montgomerizing RSA applet. 
 * <P>
 *
 * There are two different channels where the methods here can show
 * their progress. First, there is {@link PrintWriter} object {@link
 * #out}, initialized in the {@link #RSA_host_card constructor}, which
 * will receive progress and debug messages if it is non-null.
 * <P>
 *
 * Alternatively one can use an instance of {@link
 * RSA_protocol_messages}. This interface specifies about 15 methods
 * that are if specific points are reached in the protocol. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.46 $
 * @commitdate $Date: 2010-09-20 11:20:15 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#TESTFRAME">TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#RSA_CARD_PROTOCOL_STUBS">RSA_CARD_PROTOCOL_STUBS<a>
 */
public class RSA_host_card {

    /**
     * 
     * Instance with the protocol description classes. Needed to
     * perform some operations directly in the host driver copy of the
     * protocol descriptions. 
     */
    private final Front_protocols front_protocols;


    /**
     * 
     * IDL compiler generated stubs. Used to invoke the applet
     * methods. Declared as <a
     * href="../../../overview-summary.html#RSA_CARD_PROTOCOL_STUBS">RSA_CARD_PROTOCOL_STUBS<a>,
     * which expands to either {@link RSA_card_protocol_stubs} or
     * {@link RSA_card_protocol_test_stubs}.
     */
    private final RSA_CARD_PROTOCOL_STUBS stubs;


    /**
     * 
     * Randomness source.
     */
    private final Random rand = new Random();


    /**
     * 
     * Message output channel.
     */
    private final PrintWriter out;


    /**
     * 
     * Requested verbosity of the messages on {@link #out}. The code
     * currently distinguishes the verbosity levels 0, 5, 10, and 15.
     * Level 0 prints only protocol error messages. Higher values
     * print more.
     */
    private final int verbosity;


    /**
     * 
     * Constructor. Initializes the fields {@link #front_protocols},
     * {@link #stubs}, {@link #out}, and {@link #verbosity} from its
     * arguments.
     * 
     * @param front_protocols instance with the protocol description
     * classes 
     * @param stubs delcared of type <a
     * href="../../../overview-summary.html#RSA_CARD_PROTOCOL_STUBS">RSA_CARD_PROTOCOL_STUBS<a>,
     * the step method stubs
     * @param out the channel for debug and error messages, pass null
     * to disable such messages
     * @param verbosity verbosity level on {@link #out}, see {@link
     * #verbosity} 
     */
    public RSA_host_card(Front_protocols front_protocols,
                         RSA_CARD_PROTOCOL_STUBS stubs, 
                         PrintWriter out,
                         int verbosity) {
        this.front_protocols = front_protocols;
        this.stubs = stubs;
        this.out = out;
        this.verbosity = verbosity;
        return;
    }


    /**
     * 
     * The length of the array for the Montgomery correction factors.
     * On the Montgomerizing, the squareing and the squared 4
     * RSA applet no correction factors are
     * needed, therefore return 1 there (there are not 0-length {@link
     * ds.ov2.bignat.Bignat_array Bignat_arrays}). On the plain RSA
     * applet numbers are not montgomerized because of the use of the
     * RSA cipher there. When multiplying any two such numbers one
     * needs to multiply an correction factor, see <a
     * href="../bignat/package-summary.html#montgomery_factor"></a>.
     * The plain applet needs correction factors for 2,3,4,5
     * as well as for n + 1 and n + 2 factors, where n is the number
     * of attributes (see {@link
     * PTLS_rsa_parameters#attribute_number}). <P>
     *
     * In the array of the correction factors index 0 stores the
     * correction for two factors. Therefore the length of the array
     * is one less than the maximal needed factor.
     * 
     * @param applet_type the applet type
     * @return length of the array of Montgomery correction factors
     */
    public short get_montgomery_correction_length(PTLS_rsa_parameters params,
                                                  Applet_type applet_type) 
    { 
        int max_montgomery_correction = 0;

        switch(applet_type){
        case PLAIN_RSA_APPLET:
            // We need 2,3,4,5 as well as attribute_number + 1 and 
            // attribute_number + 2.
            max_montgomery_correction =
                5 > params.attribute_number + 2 ? 5 
                : params.attribute_number + 2;
            break;
        case MONT_RSA_APPLET:
        case SQUARED_RSA_APPLET:
        case SQUARED4_RSA_APPLET:
            max_montgomery_correction = 0;
            break;
        default:
            assert false;
        }

        max_montgomery_correction -= 1;

        // Ensure minimal return value 1, because there are no
        // zero-length Bignat_arrays.
        return max_montgomery_correction >= 1 ? 
            (short)(max_montgomery_correction) : (short)1;
    }





    /**
     * 
     * Initialize the host-driver copy of the protocols. This is
     * needed on the host side after the allocate protocol, because
     * the remaining protocols get valid only after the allocate
     * protocol has been run. This methods calls {@link
     * RSA_data#allocate RSA_data.allocate} with the same parameter on
     * the card, to fill argument and result arrays of the remaining
     * protocol steps. Then the delayed protocols and stubs are
     * initialized. 
     * <P>
     *
     * The debug protocol does also depend on this kind of delayed
     * initialization, see {@link RSA_card_debug#host_side_init
     * RSA_card_debug.host_side_init}.
     * <P>
     *
     * In order to have matching protocl description we have to use
     * the same applet type here as we installed on the card.
     * 
     * @param params the PTLS system parameters
     * @param applet_type what applet type we have been talking to on
     * the card
     */
    public void host_side_init(PTLS_rsa_parameters params, 
                               Applet_type applet_type) 
    {
        // Allocate data in our copy of RSA_data, otherwise all
        // protocols besides allocate are inconsistent.
        front_protocols.rsa_description.data.
            allocate((short)params.make_exponent_bytes(),
                     (short)params.make_base_bytes(
                                          applet_type.montgomery_digits()),
                     (short)params.attribute_number,
                     get_montgomery_correction_length(params, applet_type),
                     applet_type.to_byte());

        // Initialize the delayed protocols.
        // There might also be some debug protocols around, about which
        // we don't know here. They undergo the same procedure somewhere 
        // else.
        front_protocols.rsa_description.delayed_init();
        
        // If the applet and the host driver data structures have only
        // been reset via the reset_applet_state protocol then it is
        // necessary to update argument and result arrays in the host
        // data structures to make them point to the newly allocated
        // variables.
        // This update is only necessary after a reset, but it does
        // not harm. It would be better to place it somewhere else,
        // such that it is only run when the debug protocols and the
        // reset is really present. However, this is not possible,
        // because after this method returns, the initialize protocol
        // is run and by then the data structures must have been
        // updated. 
        front_protocols.rsa_description.update_all();

        // Reinitialize our protocol array, setting the protocol id's 
        // on the delayed protocols as a side effect.
        front_protocols.init_protocols();

        // Finally initialized the delayed stubs. This needs the proper 
        // protocol id's just established.
        stubs.delayed_init();
    }



    /**
     * 
     * Basic applet initialization. Assumes a freshly installed RSA
     * applet behind {@code card_channel}. Runs the allocate and
     * init_data protocols to finish allocation on the applet and
     * download key material, attributes and whatelse is needed on the
     * applet. During this initialization the blinding of the card is
     * set to 1. The signature remains uninitialized and invalid. 
     * <P>
     *
     * The initialization steps are replicated in the host driver as
     * necessary to bring the protocol layer here into the same state
     * as on the card.
     * <P>
     *
     * This method can initialize both, the plain RSA applet and the
     * Montgomerizing RSA applet.
     * 
     * @param params the PTLS system parameters
     * @param card_channel communication channel to the applet, an
     * applet of type {@code applet_type} must have been selected on
     * that channel before
     * @param messages progress message instance, can be null
     * @param applet_type what applet type to initialize
     * @todo pass attributes as arguments instead of choosing them
     * randomly 
     * @throws CardException in case of an communication error with
     * the card
     */
    public void initialize_card(PTLS_rsa_parameters params, 
                                CardChannel card_channel,
                                RSA_protocol_messages messages,
                                Applet_type applet_type) 
        throws CardException
    {
        if(out != null && verbosity >= 5)
            out.format("## init card start (%d attributes)\n", 
                       params.attribute_number);

        if(messages != null)
            messages.initialize_allocate_start();

        int exponent_bytes = params.make_exponent_bytes();
        int base_bytes = 
            params.make_base_bytes(applet_type.montgomery_digits());

        stubs.allocate_call(card_channel,
                            exponent_bytes,
                            base_bytes,
                            params.attribute_number, 
                            get_montgomery_correction_length(params, 
                                                             applet_type));

        host_side_init(params, applet_type);

        if(messages != null)
            messages.initialize_allocate_finished();


        //####################################################################
        // 
        // Allocate is finished here.
        // 
        // Prepare now the init_data_call.

        boolean montgomerize = applet_type.montgomerize();

        // Third init_data argument: the public ptls key, usually
        // denoted with h
        BigInteger ptls_public_key;
        if(montgomerize)
            ptls_public_key = 
                params.ptls_public_key.multiply(params.hmod.mont_fac).
                                       mod(params.n);
        else
            ptls_public_key = params.ptls_public_key;


        // Fourth init_data argument: the bases containing the initial
        // blinding. 
        BigInteger first_blinding = BigInteger.ONE;

        BigInteger[] bases_with_blinding = 
            new BigInteger[params.attribute_number + 1];
        System.arraycopy(params.base, 0, bases_with_blinding, 0, 
                          params.attribute_number);
        bases_with_blinding[params.attribute_number] = first_blinding;

        Host_vector host_bases = null;
        if(montgomerize)
            host_bases = 
                Host_vector.make_montgomerized_vector(base_bytes,
                                                      bases_with_blinding,
                                                      params.hmod);
        else
            host_bases = new Host_vector(base_bytes,
                                         bases_with_blinding);


        // Fifth init_data argument: precomputed factors of the base.
        // Only needed on the Montgomerizing applet.
        Host_vector base_factors;
        switch(applet_type) {
        case PLAIN_RSA_APPLET:
        case SQUARED_RSA_APPLET:
        case SQUARED4_RSA_APPLET:
            // For the plain and the squaring applet, the base_factors
            // are not needed. However, there are no zero-length
            // Bignat_arrays or Vectors.
            base_factors = new Host_vector(base_bytes,
                                           new BigInteger[]{BigInteger.ONE});
            break;
        case MONT_RSA_APPLET:
            base_factors = 
                Host_vector.make_montgomerized_factors(base_bytes,
                                                       params.base,
                                                       params.hmod);
            break;
        default:
            base_factors = null;
            assert false;
        }


        // Sixth init_data argument: the attributes with v
        BigInteger[] attribute = new BigInteger[params.attribute_number + 1];

        #ifdef APPLET_TESTFRAME
            if(Test_state.fix_inputs.has_n_inputs(params.attribute_number, 
                                                  "card attributes"))
                {
                    for(int i = 0; i < params.attribute_number; i++) {
                        attribute[i] = Test_state.fix_inputs.pop();
                    }
                }
            else
        #endif

            for(int i = 0; i < params.attribute_number; i++) {
                attribute[i] = BigIntUtil.mod_rand(rand, params.v);
            }

        // Last index in attributes gets v.
        attribute[params.attribute_number] = params.v;

        if(out != null && verbosity >= 10)
            BigIntUtil.print_array(out, 
                                   "initialize card with %d attributes\n",
                                   "att", 
                                   attribute);

        // Wrap the attributes in an Host_vector.
        Host_vector host_atr = 
            new Host_vector(exponent_bytes, attribute);



        // Eighth init_data argument: The vector of Montgomery
        // corrections. It is only used on plain applet.
        // It could be precomputed, but that's probably not
        // noticeable. 
        Host_vector mont_corr = 
            Host_vector.make_montgomery_corrections
                (base_bytes,
                 get_montgomery_correction_length(params, applet_type),
                 params.hmod);
        if(out != null && verbosity >= 15)
            BigIntUtil.print_array(out,
                                   "%d montgomery corrections\n",
                                   "corr",
                                   mont_corr.a);


        stubs.init_data_call(card_channel,
                             params.hmod,
                             ptls_public_key,
                             host_bases,
                             base_factors,
                             host_atr,
                             params.hmod.mont_fac,
                             mont_corr);

        if(out != null && verbosity >= 5)
            out.println("## init card end");

        if(messages != null)
            messages.initialize_data_copied
                (BigIntUtil.multi_exponent(bases_with_blinding,
                                           attribute, 
                                           params.n));

        return;
    }


    /**
     * 
     * Run the resign protocol. The attributes of the card are thereby
     * updated with {@code updates_bi}. Positive values are added to
     * the card attribute, negative subtracted.
     * <P>
     *
     * The addition or subtraction of the update to the current
     * attribute value must not cause an over- or underflow. Otherwise
     * the attribute expression computed here (without knowing the
     * attribute values) will not equal the one on the card and the
     * signature creation will fail.
     * <P>
     *
     * Normally the signature on the card is checked. The check can be
     * ommitted, with {@code check_signature}. This is necessary in
     * the first resign step, which belongs to the card
     * initialization, because then, the card does not have a valid
     * signature yet. 
     * <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. The detailed operations of
     * this method are:
     * <DL>
     * <DT><STRONG>XXXXXXXXXXXXXX</STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * </DL>
     * 
     * @param updates_bi the updates in the range between -{@link
     * PTLS_rsa_parameters#v} + 1 and {@link PTLS_rsa_parameters#v}
     * -1, inclusive.
     * @param check_signature whether to check the signature of the
     * card 
     * @param params the PTLS system parameters
     * @param card_channel communication channel to the applet
     * @param messages progress message instance
     * @return true if the card accepted the attribute updates and the
     * new signature, false if not. Note, that there is a very small
     * chance that a communication error occors after the card has
     * finished the transaction for updating attribute values and
     * signature. In this case this method will return false or throw
     * an {@link CardException}, although the card accepted the new
     * signature. 
     * @throws CardException in case of communication errors with the
     * card
     */
    public boolean resign(BigInteger[] updates_bi,
                          boolean check_signature,
                          PTLS_rsa_parameters params,
                          CardChannel card_channel,
                          RSA_protocol_messages messages)
        throws CardException
    {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here. 
        // END PATENT CUT
    }



    /**
     * 
     * Combined card initialization and personalization. Calls {@link
     * #initialize_card initialize_card} and performs resign step right
     * afterwards. As a result the card is initialized with an unknown
     * blinding and valid signature. 
     * <P>
     *
     * This method can issue both, a plain RSA applet and an
     * Montgomerizing RSA applet.
     * 
     * @param params the PTLS system parameters 
     * @param card_channel communication channel to the applet, an
     * applet of type {@code applet_id} must have been selected on
     * that channel before
     * @param messages the progress message instance
     * @param applet_type what applet type to initialize
     * @return true if the {@link #resign resign} step succeeded
     * @throws CardException in case of communication errors with the
     * card 
     */
    public boolean issue_card(PTLS_rsa_parameters params, 
                              CardChannel card_channel,
                              RSA_protocol_messages messages,
                              Applet_type applet_type) 
        throws CardException
    {
        if(messages != null)
            messages.initialize_start();

        initialize_card(params, card_channel, messages, applet_type);

        #ifdef TESTFRAME
            front_protocols.rsa_debug.delayed_init();
        #endif

        BigInteger[] updates = new BigInteger[params.attribute_number];
        for(int i = 0; i < params.attribute_number; i++) {
            updates[i] = BigInteger.ZERO;
        }


        if(messages != null)
            messages.initialize_resign();

        boolean res = 
            resign(updates, false, params, card_channel, messages);

        if(messages != null)
            messages.initialize_finished();

        return res;
    }


    /**
     * 
     * Run the gate protocol with the card. Let the card proof
     * knowledge of all its attributes. Selective disclosure is not
     * yet implemented. The signature check can be ommitted, but that
     * is only there to permit performance mesurements of the <a
     * href="../../../overview-summary.html#MONTGOMERY_MULT_SHORTCUT">MONTGOMERY_MULT_SHORTCUT<a>
     * optimization, in which all Montgomery multiplications on the
     * card are skipped (and therefore all data the card produces is
     * just garbage.
     * <P>
     *
     * The source code of this method is affected by Brand's patents
     * on selective disclosure protocols that are now in the posession
     * of Microsoft. Microsoft lawyers are still pondering our request
     * from January 2009 for making the full source code publically
     * available. The source code of this method is therefore
     * currently not publically available. The detailed operations of
     * this method are:
     * <DL>
     * <DT><STRONG>XXXXXXXXXXXXXX</STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * <DT><STRONG></STRONG>
     * <DD>
     * </DL>
     * 
     * 
     * @param params the PTLS system parameters
     * @param card_channel communication channel to the applet
     * @param messages the progress message instance
     * @param check_signature whether to check the signature of the
     * card (should never be false)
     * @return true if the card could correctly answer our challenge
     * @throws CardException in case a communication error occors
     */
    public boolean check_gate(PTLS_rsa_parameters params, 
                              CardChannel card_channel,
                              RSA_protocol_messages messages,
                              boolean check_signature) 
        throws CardException
    {
        // START PATENT CUT
        The code of this method is covered by patents owned by Microsoft.
        Microsoft lawyers are still pondering our request from January
        2009 to permit the distribution of the complete sources.
        See ``Rethinking Public Key Infrastructures and Digital
        Certificates: Building in Privacy'' by Brands or ``Performance
        issues of Selective Disclosure and Blinded Issuing Protocols
        on Java Card'' by Tews and Jacobs for a description of the
        algorithm to fill in here. 
        // END PATENT CUT
    }
}
