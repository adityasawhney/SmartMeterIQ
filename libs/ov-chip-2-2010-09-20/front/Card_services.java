// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 1.2.09 by Hendrik
// 
// Applet installation/deletion, card status
// 
// $Id: Card_services.java,v 1.5 2009-05-12 10:00:15 tews Exp $

package ds.ov2.front;


import java.util.Vector;
import java.util.Arrays;
import java.io.IOException;
import java.io.PrintWriter;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import cardservices.AID;
import cardservices.AIDRegistry;
import cardservices.AIDRegistryEntry;
import cardservices.GlobalPlatformService;

import ds.ov2.util.PrintWriter_APDUListener;


/**
 * Interface class to the XXX link global platform manager.
 * Implements applet installation, deletion and card status for 
 * the frontoffice.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.5 $
 * @commitdate $Date: 2009-05-12 10:00:15 $ by $Author: tews $
 * @environment host
 * @CPP cpp preprocessing needed
 */
public class Card_services {

    /**
     * Static class, object construction disabled.
     */
    protected Card_services() {}


    /**
     * Query card status. 
     * 
     * @param channel the card channel to access the card
     * @param out channel to print diagnostics/progress to
     * @param full_apdus if true print full APDU's, otherwise print one 
     *     dot per exchanged APDU
     * @return AIDRegistry of the card
     * @throws CardException if some communication error occurs
     */
    public static AIDRegistry card_status(CardChannel channel, 
                                          PrintWriter out,
                                          boolean full_apdus)
        throws CardException
    {
        PrintWriter_APDUListener listener = new PrintWriter_APDUListener(out);

        if(out != null) {
            out.print("Query card status ");
            if(full_apdus) {
                out.println("");
            }
            else {
                out.flush();
                listener.print_dots(true);
            }
        }

        GlobalPlatformService gps = new GlobalPlatformService(channel);
        gps.addAPDUListener(listener);
        gps.openWithDefaultKeys();
        AIDRegistry res = gps.getStatus();

        if(out != null) {
            out.println("Query card status finished");
        }
        return res;
    }


    /**
     * (Re-)Install applet. If {@code package_name} is present on 
     * card delete it first. Then load and install {@code applet_file}
     * without any installation arguments.
     * 
     * @param channel card channel 
     * @param out diagnostics channel, pass null to disable diagnostics
     * @param full_apdus if true print full APDU's on {@code out}, 
     *        otherwise print one dot per exchanged APDU
     * @param applet_type the applet to install
     * @param applet_file cap file to load
     * @throws CardException for communication errors
     * @throws IOException if {@code applet_file} cannot be opened
     */
    public static void reinstall_applet(CardChannel channel, PrintWriter out,
                                        boolean full_apdus,
                                        Applet_type applet_type,
                                        String applet_file) 
        throws CardException, IOException
    {
        PrintWriter_APDUListener listener = new PrintWriter_APDUListener(out);

        if(out != null) {
            out.print("(Re-)Install applet, Query card status ");
            if(full_apdus) {
                out.println("");
            }
            else {
                out.flush();
                listener.print_dots(true);
            }
        }

        GlobalPlatformService gps = new GlobalPlatformService(channel);
        gps.addAPDUListener(listener);
        gps.openWithDefaultKeys();
        Vector<AIDRegistryEntry> packages = gps.getStatus().allPackages();

        if(out != null) {
            out.println("Query card status finished");
        }

        byte[] package_bytes = applet_type.package_aid();
        for(AIDRegistryEntry e : packages) {
            if(Arrays.equals(e.getAID().getBytes(), package_bytes)) {
                out.format("package %s found, deleting it ", 
                           applet_type.package_name());
                out.flush();
                if(full_apdus)
                    out.println("");
                gps.deleteAID(e.getAID(), true);
                out.println("Delete finished");
                }
        }

        out.format("Load applet %s ", applet_file);
        out.flush();
        if(full_apdus)
            out.println("");
        gps.loadCapFile(applet_file, 
                        false,  // loadDebug
                        false,  // loadCompSep
                        GlobalPlatformService.defaultLoadSize,
                        false,  // loadParam
                        false   // useHash
                        );
        out.println("Load applet finished");

        out.format("Install applet %s of package %s\n",
                   applet_type.applet_name(), 
                   applet_type.package_name());
        out.flush();
        gps.installAndMakeSelecatable
            (new AID(applet_type.package_aid()),
             new AID(applet_type.applet_aid()),
             null,              // instance AID
             (byte)0,           // privileges
             null,              // arguments
             null               // install token
             );
        out.println("Install finished");
    }    
}
