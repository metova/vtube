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

import com.metova.vtube.model.player.MediaPlayerException;

public class MediaErrorManager extends ScrollingDescriptionManager {

    private MediaPlayerException errorInfo;

    private RichTextField richTextField;

    public MediaErrorManager(MediaPlayerException errorInfo) {

        setErrorInfo( errorInfo );
    }

    protected void onInitialize() {

        StringBuffer stringBuffer = new StringBuffer();

        MediaPlayerException errorInfo = getErrorInfo();

        stringBuffer.append( "An error has occured during video playback. Would you like to try opening the BlackBerry media player instead?" );

        stringBuffer.append( "\n\nCode: " );
        stringBuffer.append( errorInfo.getCode() );

        String solution = errorInfo.getSolution();
        if ( solution != null ) {

            stringBuffer.append( "\n\nSuggested Solution: " );
            stringBuffer.append( solution );
        }

        stringBuffer.append( "\n\nDetails: " );
        stringBuffer.append( errorInfo.getMessage() );

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

    private MediaPlayerException getErrorInfo() {

        return errorInfo;
    }

    private void setErrorInfo( MediaPlayerException errorInfo ) {

        this.errorInfo = errorInfo;
    }
}
