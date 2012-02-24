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
package com.metova.vtube.widgets.manager;

import org.metova.bb.widgets.field.richcontent.RichTextField;

public class TipManager extends ScrollingDescriptionManager {

    private RichTextField richTextField;

    protected void onInitialize() {

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append( "\u2022 Send videos to others by email or sms.\n\n" );

        stringBuffer.append( "\u2022 Preferences can be changed through the main screen menu.\n\n" );

        stringBuffer.append( "\u2022 If your device has trouble with video playback," );
        stringBuffer.append( " try changing the format type in the preferences.\n\n" );

        stringBuffer.append( "\u2022 Videos can be played over WiFi without requiring a data plan.\n\n" );

        stringBuffer.append( "\u2022 An icon will be displayed in the header when WiFi is in use.\n\n" );

        stringBuffer.append( "\u2022 If your wireless router cannot handle the video streaming to your" );
        stringBuffer.append( " device, try disabling WiFi through the media player screen menu.\n\n" );

        // Submit Feedback removed per http://jira.metova.com/browse/TUBE-1467
        //        stringBuffer.append( "\u2022 Feedback can be submitted through the about screen.\n\n" );

        stringBuffer.append( "\u2022 A list of shortcut keys and further details can be found at" );
        stringBuffer.append( " http://vision.metova.com." );

        RichTextField richTextField = new RichTextField( stringBuffer.toString() );
        richTextField.getStyleManager().setStyleClass( "h3" );
        setRichTextField( richTextField );
    }

    protected void onLoading() {

        add( getDescriptionField() );
    }

    protected RichTextField getDescriptionField() {

        return richTextField;
    }

    private void setRichTextField( RichTextField richTextField ) {

        this.richTextField = richTextField;
    }
}
