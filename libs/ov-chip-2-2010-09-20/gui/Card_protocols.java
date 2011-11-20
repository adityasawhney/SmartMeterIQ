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
// Created 1.1.09 by Hendrik
// 
// state and methods for running the various protocols on the card
// 
// $Id: Card_protocols.java,v 1.20 2009-06-18 11:57:39 tews Exp $

package ds.ov2.gui;


import java.util.Arrays;
import java.math.BigInteger;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;
import javax.smartcardio.Card;

import cardservices.AID;
import cardservices.AIDRegistry;
import cardservices.GlobalPlatformService;
import cardservices.GlobalPlatformDeleteException;
import cardservices.GlobalPlatformInstallForLoadException;
import cardservices.GlobalPlatformLoadException;

import ds.ov2.util.Card_terminal;
import ds.ov2.util.PrintWriter_APDUListener;
import ds.ov2.front.PTLS_rsa_parameters;
import ds.ov2.front.PTLS_rsa_parameters.PTLS_io_exception;
import ds.ov2.front.RSA_card_protocol_description;
import ds.ov2.front.RSA_card_protocol_stubs;
import ds.ov2.front.RSA_card_debug_description;
import ds.ov2.front.RSA_card_debug_stubs;
import ds.ov2.front.Front_protocols;
import ds.ov2.front.RSA_host_card;
import ds.ov2.front.Applet_type;
import ds.ov2.front.RSA_card_debug;


/** 
 * Basic card/applet actions of the graphical demonstrator. This
 * includes running the protocols but also applet installation,
 * deletion and querying the card status. Additionally, this class
 * contains actions for managing PTLS parameter sets.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.20 $
 * @commitdate $Date: 2009-06-18 11:57:39 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Card_protocols {

    /**
     * 
     * PTLS parameter instance. This instance contains the parameters
     * that are used in the GUI demonstrator.
     */
    private PTLS_rsa_parameters ptls_parameters = null;


    /**
     * 
     * RSA host protocols instance.
     */
    RSA_host_card host_card;


    /**
     * 
     * RSA debug protocol instance.
     */
    RSA_card_debug debug_card;


    /**
     * 
     * Constructor. Creates and initializes the necessary objects on
     * the host side for talking to the OV-chip applets through the
     * protocol layer.
     * 
     */
    public Card_protocols() {
        // Initialize the meta-protocol class.
        Front_protocols front_protocols = new Front_protocols();

        // Get the protocol description data that is used to 
        // communicate to ccard.
        RSA_card_protocol_description p = front_protocols.rsa_description;
        RSA_card_debug_description pd = front_protocols.rsa_debug;

        // Initialize the host stub code.
        PrintWriter stub_out = 
            Gui_state.verbosity >= 15 ? Gui_state.out : null;
        RSA_card_protocol_stubs stubs = 
            new RSA_card_protocol_stubs(p, stub_out, false);

        RSA_card_debug_stubs dstubs = 
            new RSA_card_debug_stubs(pd, stub_out, false);

        // Make a host-side card instance.
        host_card = new RSA_host_card(front_protocols,
                                      stubs, Gui_state.out, 
                                      Gui_state.verbosity);

        // Make the debug object.
        debug_card = new RSA_card_debug(front_protocols,
                                        dstubs, Gui_state.out, 
                                        Gui_state.verbosity);
    }


    //########################################################################
    //########################################################################
    // 
    // PTLS Parameters
    // 
    //########################################################################
    //########################################################################

    /**
     * 
     * Check whether we have a set of PTLS parameters.
     * 
     * @return true if parameters have been configured
     */
    public boolean valid_parameters() {
        return ptls_parameters != null;
    }


    /**
     * 
     * Clear the current set of PTLS parameters.
     * 
     */
    public void clear_parameters() {
        ptls_parameters = null;
    }


    /**
     * 
     * Generate a new set of PTLS parameters.
     * 
     * @param attribute_number number of bases and attributes to use
     * @param key_size RSA key size in bits
     * @param exp_size exponent size in bits
     * @param applet_type the applet type (needed somewhere for the
     * number of Montgomery digits)
     */
    public void generate_ptls_parameters(int attribute_number, 
                                         int key_size,
                                         int exp_size,
                                         Applet_type applet_type) 
        throws NoSuchAlgorithmException
    {
        ptls_parameters = new PTLS_rsa_parameters(attribute_number,
                                                  Gui_state.out,
                                                  Gui_state.verbosity);
        ptls_parameters.generate(key_size, exp_size, applet_type);
    }


    /**
     * 
     * Save the current PTLS parameters to file.
     * 
     * @param file_name file to use
     * @throws PTLS_io_exception on I/O errors
     */
    public void save_ptls_parameters(String file_name)
        throws PTLS_io_exception
    {
        ptls_parameters.write_to_file(file_name);
    }


    /**
     * 
     * Read PTLS parameters from file
     * 
     * @param file_name the file name to read
     * @param applet_type the applet type for which the protocol layer
     * should be initialized
     * @return array with three entries: the number of
     * attributes/bases, the RSA key size in bits, the exponent size
     * in bits
     * @throws PTLS_io_exception on I/O errors
     */
    public int[] read_ptls_parameters(String file_name,
                                      Applet_type applet_type)
        throws PTLS_io_exception
    {
        ptls_parameters = 
            ptls_parameters.read_from_file(file_name, applet_type,
                                           Gui_state.out,
                                           Gui_state.verbosity);

        // RMI delayed init.
        host_card.host_side_init(ptls_parameters, applet_type);
        debug_card.host_side_init();

        return new int[]{ptls_parameters.attribute_number,
                         ptls_parameters.base_length,
                         ptls_parameters.exponent_length};
    }


    /**
     * 
     * Return the maximal possible base value.
     * 
     * @return the maximal possible base value
     */
    public BigInteger get_max_base() {
        return ptls_parameters.n.subtract(BigInteger.ONE);
    }


    /**
     * 
     * Return the number of attributes.
     * 
     * @return the number of attributes in the current PTLS parameters
     * @throws NullPointerException if there are no valid parameters
     */
    public int get_attribute_number() {
        return ptls_parameters.attribute_number;
    }


    /**
     * 
     * Return the array of bases.
     * 
     * @return array of bases
     */
    public BigInteger[] get_bases() {
        return Arrays.copyOf(ptls_parameters.base, ptls_parameters.base.length);
    }


    /**
     * 
     * Update the bases in the PTLS parameters.
     * 
     * @param new_bases the new bases to use from now on
     */
    public void update_bases(BigInteger[] new_bases) {
        assert new_bases.length == ptls_parameters.base.length;
        BigInteger max = get_max_base();
        for(int i = 0; i < new_bases.length; i++)
            assert new_bases[i].compareTo(max) <= 0;

        System.arraycopy(new_bases, 0, ptls_parameters.base, 0, 
                         new_bases.length);
    }



    //########################################################################
    //########################################################################
    // 
    // Card communication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Open the default channel to the card in the configured card
     * reader. 
     * <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @return a card channel
     * @throws CardException on communication problems with the card
     */
    private CardChannel get_card_channel()
        throws CardException
    {
        Card card = Gui_state.card_terminal.connect("*");
        CardChannel channel = card.getBasicChannel();
        return channel;
    }


    /**
     * 
     * Open a connection to the global platform manager on the card.
     * <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @param channel channel to the card
     * @return the global platform service instance
     * @throws CardException for communication problems and global
     * platform errors
     */
    private GlobalPlatformService get_gps(CardChannel channel)
        throws CardException
    {
        // XXXXXXXXXXXXXXX
        // if(gps != null)
        //     return;

        PrintWriter_APDUListener listener = 
            new PrintWriter_APDUListener(Gui_state.out);
        if(Gui_state.verbosity < 15)
            listener.print_dots(true);

        GlobalPlatformService gps = new GlobalPlatformService(channel);
        gps.addAPDUListener(listener);
        gps.openWithDefaultKeys();
        return gps;
    }



    /**
     * 
     * Get the registry of all installed applets/packages from the
     * card. 
     * 
     * @throws CardException on communication errors
     */
    public AIDRegistry get_card_status() 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        GlobalPlatformService gps = get_gps(channel);
        AIDRegistry r = gps.getStatus();
        channel.getCard().disconnect(true);
        return r;
    }


    /**
     * 
     * Install one of the OV-Chip applets on a card. Use an existing
     * global platform service instance, which has a channel to the
     * card. 
     * <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @param gps global platform service instance
     * @param applet_type denotes the applet to install
     * @param messages progress message instance
     * @throws GlobalPlatformInstallForLoadException if the
     * install-for-load command fails
     * @throws GlobalPlatformLoadException if cap file loading fails
     * @throws CardException for low-level communication errors
     * @throws IOException if the cap file cannot be opened
     */
    private void install_applet_with_gps(GlobalPlatformService gps, 
                                         Applet_type applet_type, 
                                         Gui_protocol_messages messages) 
        throws GlobalPlatformInstallForLoadException,
               GlobalPlatformLoadException,
               CardException, 
               IOException
    {
        Gui_state.out.println("gps load cap file");
        gps.loadCapFile(Gui_state.applet_file_name(applet_type),
                        false,  // loadDebug
                        false,  // loadCompSep
                        GlobalPlatformService.defaultLoadSize,
                        false,  // loadParam
                        false   // useHash
                        );
        Gui_state.out.println("Load applet finished");
        messages.load_applet_finished();

        Gui_state.out.format("Install applet %s of package %s\n",
                             applet_type.applet_name(), 
                             applet_type.package_name());

        gps.installAndMakeSelecatable
            (new AID(applet_type.package_aid()),
             new AID(applet_type.applet_aid()),
             null,              // instance AID
             (byte)0,           // privileges
             null,              // arguments
             null               // install token
             );
    }



    /**
     * 
     * Install one of the OV-chip applets. The connection to the card
     * and the global platform service instance is locally created and
     * destroyed. 
     * 
     * @param applet_type the applet to install
     * @param messages progress message instance
     * @throws GlobalPlatformInstallForLoadException if the
     * install-for-load command fails
     * @throws GlobalPlatformLoadException if cap file loading fails
     * @throws CardException for low-level communication errors
     * @throws IOException if the cap file cannot be opened
     */
    public void install_applet(Applet_type applet_type,
                               Gui_protocol_messages messages)
        throws GlobalPlatformInstallForLoadException,
               GlobalPlatformLoadException,
               CardException,
               IOException
    {
        CardChannel channel = get_card_channel();
        GlobalPlatformService gps = get_gps(channel);
        install_applet_with_gps(gps, applet_type, messages);
        channel.getCard().disconnect(true);
    }



    /**
     * 
     * Delete applets and/or packages from the card.
     * 
     * @param aids the ID's to delete
     * @param messages progress message instance
     * @throws CardException on communication errors
     */
    public void delete_applets(Printable_aid[] aids, 
                               Gui_protocol_messages messages) 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        GlobalPlatformService gps = get_gps(channel);
        for(Printable_aid aid : aids) {
            messages.delete_applet(aid.toString());
            try {
                gps.deleteAID(aid.aid, true);
            }
            catch(GlobalPlatformDeleteException e) {
                messages.delete_applet_failure(aid.toString());
            }
        }
        channel.getCard().disconnect(true);
    }



    //########################################################################
    //########################################################################
    // 
    // High-level protocols
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Reset the applet.
     * 
     * @param applet_type the applet to reset
     * @throws CardException on communication errors
     */
    public void reset_applet(Applet_type applet_type) 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        Card_terminal.open_applet_ex(channel, 
                                     applet_type.applet_aid(),
                                     Gui_state.out, false);

        debug_card.reset_applet(channel);

        channel.getCard().disconnect(true);
    }


    /**
     * 
     * Personalize the applet. Download key material, attributes and
     * do a initial resign. The channel to the card is locally created
     * and destroyed.
     * 
     * @param applet_type the applet to personalize
     * @param messages progress message instance
     * @throws CardException on communication errors
     */
    public void personalize(Applet_type applet_type,
                            Gui_protocol_messages messages) 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        issue(channel, messages, applet_type);
        channel.getCard().disconnect(true);
    }



    /**
     * 
     * Reinstall and personalize applet. The channel and the global
     * platform manager instance is locally created and destroyed.
     * 
     * @param applet_type the applet to reinstall
     * @param messages progress message instance
     * @throws GlobalPlatformInstallForLoadException if the
     * install-for-load command fails
     * @throws GlobalPlatformLoadException if cap file loading fails
     * @throws CardException for low-level communication errors
     * @throws IOException if the cap file cannot be opened
     */
    public void reinstall_personalize(Applet_type applet_type,
                                      Gui_protocol_messages messages)
        throws GlobalPlatformInstallForLoadException,
               GlobalPlatformLoadException,
               CardException,
               IOException
    {
        CardChannel channel = get_card_channel();
        GlobalPlatformService gps = get_gps(channel);

        messages.delete_applet(applet_type.package_name());
        try {
            gps.deleteAID(new AID(applet_type.package_aid()), 
                          true);
        }
        catch(GlobalPlatformDeleteException e) {
            messages.delete_applet_failure(applet_type.package_name());
        }

        messages.start_install(applet_type.toString());
        install_applet_with_gps(gps, applet_type, messages);

        issue(channel, messages, applet_type);
        channel.getCard().disconnect(true);
    }



    /**
     * 
     * Run the applet initialization protocol.
     * 
     * @param channel channel to the card
     * @param messages progress message instance
     * @param applet_type the applet to initialize
     * @return true if the final resign step was successful
     * @throws CardException on communication errors
     */
    public boolean issue(CardChannel channel, 
                         Gui_protocol_messages messages,
                         Applet_type applet_type) 
        throws CardException
    {
        
        Card_terminal.open_applet_ex(channel, applet_type.applet_aid(),
                                     Gui_state.out, false);

        boolean res = host_card.issue_card(ptls_parameters, channel, 
                                           messages, applet_type);

        // Host side RMI delayed init.
        debug_card.host_side_init();

        return res;
    }


    /**
     * 
     * Run the resign protocol with the specified attribute updates.
     * If the {@code updates_bi} parameter is null random attribute
     * updates are invented via {@link
     * RSA_card_debug#invent_attribute_updates
     * RSA_card_debug.invent_attribute_updates}.
     * 
     * @param applet_type the applet to select for resigning
     * @param updates_bi pass null for random attribute updates,
     * otherwise pass an array with with {@link #ptls_parameters}.{@link
     * PTLS_rsa_parameters#attribute_number attribute_number}
     * attribute updates in the range of -{@link
     * #ptls_parameters}.{@link PTLS_rsa_parameters#v v} + 1 .. {@link
     * #ptls_parameters}.{@link PTLS_rsa_parameters#v v} -1
     * @param messages progress message object
     * @return true if resign succeeds
     * @throws CardException on communication errors
     */
    public boolean resign_with_update(Applet_type applet_type,
                                      BigInteger[] updates_bi,
                                      Gui_protocol_messages messages) 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        Card_terminal.open_applet_ex(channel, 
                                     applet_type.applet_aid(),
                                     Gui_state.out, false);

        if(updates_bi == null)
            updates_bi = 
                debug_card.invent_attribute_updates(ptls_parameters, channel);
        
        boolean res = host_card.resign(updates_bi, true, 
                                       ptls_parameters, channel, messages);

        channel.getCard().disconnect(true);
        return res;
    }


    /**
     * 
     * Run the entry-gate proof protocol.
     * 
     * @param applet_type applet to select for proving
     * @param messages progress message instance
     * @return true if the proof was successful
     * @throws CardException on communication errors
     */
    public boolean entry_gate_check(Applet_type applet_type,
                                    Gui_protocol_messages messages) 
        throws CardException
    {
        CardChannel channel = get_card_channel();
        Card_terminal.open_applet_ex(channel, 
                                     applet_type.applet_aid(),
                                     Gui_state.out, false);

        boolean res = host_card.check_gate(ptls_parameters, channel, 
                                           messages, true);

        channel.getCard().disconnect(true);
        return res;
    }

}
