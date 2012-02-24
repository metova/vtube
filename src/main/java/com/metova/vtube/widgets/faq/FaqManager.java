/**
 * Copyright (c) 2009-2012 Martin M Reed, Metova Inc
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metova.vtube.widgets.faq;

import java.util.Enumeration;
import java.util.Vector;

import com.metova.vtube.widgets.manager.ScrollingVerticalManager;

public class FaqManager extends ScrollingVerticalManager {

    private static final String[][] FAQS_TEXT = {

            { "\u2022 Where is the search feature?", "The search feature is available only on the main screen navigable through a bar positioned at the top of the display. You can access it by scrolling your trackball up, or by touching the bar on a Storm.\n\n" },
            { "\u2022 How much does it cost?", "It is free! Data charges may apply with your carrier but the Vision application does not charge for any of the functionality provided.\n\n" },
            { "\u2022 Why does the video cutoff while watching?", "Network throughput, this is unfortunately out of our hands and is dependent on your carrier or WiFi connection.\n\n" },
            {
                    "\u2022 Can I stream videos over the BIS network?",
                    "Short answer is no. The BIS network is limited in bandwidth, and streaming content like videos through it would affect the BlackBerry experience for other users. Instead Vision will first try to stream over WiFi if it is available, otherwise it will use WAP/TCP. The correct WAP service books or carrier APN settings will be required.\n\n" },
            {
                    "\u2022 Why did the application ask to use the native media player?",
                    "If Vision encounters an error during video playback that it cannot recover from, it will provide the option of viewing the video in the native media player instead. This may happen if there is a server error or a problem streaming the specified format type.\n\nIf the application crashes or displays the error a lot during video playback, the first thing to do is reduce the video format through the preferences. Some older versions of a given operating system may contain defects that will affect some video formats over others. An example of this is the 9000 (Bold) 4.6.0.167 operating system where the more stable format is the lowest.\n\nThe lower quality format available is H.263 Video and AMR Audio.\n\nThe preferred solution would be to upgrade your operating system to the newest publicly available version.\n\n" },
            {
                    "\u2022 Why did the application crash during video playback?",
                    "If you are running an older version of your operating system, there may be defects that will affect some video formats over others. These defects can cause the application to crash unexpectedly. While Vision cannot prevent these unexpected crashes, it will try to predict a crash and display the option of viewing through the native media player (as of version 1.0.24).\n\nSee the FAQ above for more details.\n\n" },
            { "\u2022 Can I save the video to the device?", "No, this feature is not supported or currently possible due to restrictions with the RTSP stream.\n\n" },
            {
                    "\u2022 How do I uninstall the application?",
                    "Vision uninstalls just like any other third party application. If you installed the application via AppWorld, you may open AppWorld, go to \"My World\" and use the menu to uninstall. If you installed directly from our provisioning server and have a newer device, you should be able to highlight the icon on the ribbon, press the menu key and \"Delete\". If you are using an older device, you may go to Options | Advanced Options | Applications | Select the application | press the menu key | Delete. A reboot is required by the BlackBerry operating system before the icon will be removed from the ribbon.\n\n" }

    };

    private Vector faqFields;

    public FaqManager() {

        setFaqFields( new Vector() );
    }

    protected void onInitialize() {

        Vector faqFields = getFaqFields();

        String[][] faqText = FAQS_TEXT;
        for (int i = 0; i < faqText.length; i++) {

            String[] faq = faqText[i];
            faqFields.addElement( new FaqField( faq[0], faq[1] ) );
        }
    }

    protected void onLoading() {

        Vector faqFields = getFaqFields();
        Enumeration enumeration = faqFields.elements();
        while (enumeration.hasMoreElements()) {

            add( (FaqField) enumeration.nextElement() );
        }
    }

    private void setFaqFields( Vector faqFields ) {

        this.faqFields = faqFields;
    }

    private Vector getFaqFields() {

        return faqFields;
    }
}
