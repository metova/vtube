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
package com.metova.vtube.ui;

import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYRect;

import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.field.HorizontalRule;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.player.MediaPlayerException;
import com.metova.vtube.service.video.Browsers;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.manager.MediaErrorManager;

public class MediaErrorContainer extends AbstractLicensedContainer {

    private FeedEntry feedEntry;
    private MediaPlayerException errorInfo;

    private ButtonManager buttonManager;

    private MediaErrorManager mediaErrorManager;

    public MediaErrorContainer(FeedEntry feedEntry, MediaPlayerException errorInfo) {

        setFeedEntry( feedEntry );
        setErrorInfo( errorInfo );

        setTitle( "Error" );
    }

    protected void onInitializeHeader() {

        super.onInitializeHeader();

        setButtonManager( new ButtonManager() );
    }

    protected void onInitializeContent() {

        setMediaErrorManager( new MediaErrorManager( getErrorInfo() ) );
    }

    protected void addHeaderFields() {

        super.addHeaderFields();

        Manager headerManager = getHeaderManager();
        headerManager.add( getButtonManager() );
        headerManager.add( new HorizontalRule() );
        headerManager.add( new NullLabelField( 0, 10 ) );
    }

    protected void addContentFields() {

        add( getMediaErrorManager() );
    }

    protected void onCancel() {

        Screens.popScreenLater( MediaErrorContainer.this );
    }

    private class NoAction implements Runnable {

        public void run() {

            onCancel();
        }
    }

    private class YesAction implements Runnable {

        public void run() {

            FeedEntry feedEntry = getFeedEntry();

            BrowserSession browserSession = Browsers.getStreamingBrowserSession();
            browserSession.displayPage( Videos.getUrl( feedEntry ) );

            onCancel();
        }
    }

    private class ButtonManager extends AbstractHorizontalFieldManager {

        private ButtonField noButtonField;
        private ButtonField yesButtonField;

        public ButtonManager() {

            super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
        }

        protected void initializeFields() {

            ButtonField noButtonField = new ButtonField( "Cancel", new NoAction() );
            noButtonField.getStyleManager().setStyleClass( "ButtonField-tip-left" );
            setNoButtonField( noButtonField );

            ButtonField yesButtonField = new ButtonField( "Continue", new YesAction() );
            yesButtonField.getStyleManager().setStyleClass( "ButtonField-tip-right" );
            setYesButtonField( yesButtonField );
        }

        protected void addFields() {

            add( getNoButtonField() );
            add( getYesButtonField() );
        }

        protected void onSublayout( int width, int height ) {

            super.onSublayout( width, height );

            width = getWidth();

            ButtonField noButtonField = getNoButtonField();
            if ( noButtonField != null && equals( noButtonField.getManager() ) ) {

                XYRect extent = noButtonField.getExtent();

                int fieldWidth = noButtonField.getWidth();
                int fieldX = (int) ( width * 0.50 ) - fieldWidth;

                setPositionChild( noButtonField, fieldX, extent.y );
            }

            ButtonField yesButtonField = getYesButtonField();
            if ( yesButtonField != null && equals( yesButtonField.getManager() ) ) {

                XYRect extent = yesButtonField.getExtent();

                int fieldX = (int) ( width * 0.50 );

                setPositionChild( yesButtonField, fieldX, extent.y );
            }
        }

        private ButtonField getNoButtonField() {

            return noButtonField;
        }

        private void setNoButtonField( ButtonField noButtonField ) {

            this.noButtonField = noButtonField;
        }

        private ButtonField getYesButtonField() {

            return yesButtonField;
        }

        private void setYesButtonField( ButtonField yesButtonField ) {

            this.yesButtonField = yesButtonField;
        }
    }

    private ButtonManager getButtonManager() {

        return buttonManager;
    }

    private void setButtonManager( ButtonManager buttonManager ) {

        this.buttonManager = buttonManager;
    }

    private MediaErrorManager getMediaErrorManager() {

        return mediaErrorManager;
    }

    private void setMediaErrorManager( MediaErrorManager mediaErrorManager ) {

        this.mediaErrorManager = mediaErrorManager;
    }

    private FeedEntry getFeedEntry() {

        return feedEntry;
    }

    private void setFeedEntry( FeedEntry feedEntry ) {

        this.feedEntry = feedEntry;
    }

    private MediaPlayerException getErrorInfo() {

        return errorInfo;
    }

    private void setErrorInfo( MediaPlayerException errorInfo ) {

        this.errorInfo = errorInfo;
    }
}
