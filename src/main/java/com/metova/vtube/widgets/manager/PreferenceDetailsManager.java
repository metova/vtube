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

import com.metova.vtube.Settings;

public class PreferenceDetailsManager extends ScrollingDescriptionManager {

    private RichTextField richTextField;

    protected void onInitialize() {

        StringBuffer stringbuffer = new StringBuffer();

        stringbuffer.append( "\u2022 " );
        stringbuffer.append( Settings.OPTION_DEFAULT_FEED );
        stringbuffer.append( ": Select a feed that will be loaded at startup." );
        stringbuffer.append( " To turn this feature off, select \"None\".\n\n" );

        stringbuffer.append( "\u2022 " );
        stringbuffer.append( Settings.OPTION_RESULTS_PER_PAGE );
        stringbuffer.append( ": Defaults to 10 results. A larger amount will make load times longer.\n\n" );

        stringbuffer.append( "\u2022 " );
        stringbuffer.append( Settings.OPTION_ORDER_BY );
        stringbuffer.append( ": Preferred option for ordering search results. Unused for feeds.\n\n" );

        stringbuffer.append( "\u2022 " );
        stringbuffer.append( Settings.OPTION_FORMAT_TYPE );
        stringbuffer.append( ": Currently YouTube videos are limited to two low quality" );
        stringbuffer.append( " formats. The only significant difference is the audio.\n\n" );

        stringbuffer.append( "\u2022 " );
        stringbuffer.append( Settings.OPTION_REDUCE_FORMAT );
        stringbuffer.append( ": Automatic quality reduction can be enabled for when WiFi is not available." );

        RichTextField richTextField = new RichTextField( stringbuffer.toString(), NON_FOCUSABLE );
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
