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
package com.metova.vtube.service.video;

import m.java.util.Map;
import m.org.apache.log4j.Logger;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.mail.Message;
import net.rim.device.api.ui.component.Menu;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.menu.Menus;
import org.metova.mobile.message.TextMessage;
import org.metova.mobile.rt.alert.MobileAlert;
import org.metova.mobile.rt.system.MobileNetwork;
import org.metova.mobile.util.math.MathUtils;
import org.metova.mobile.util.text.Text;
import org.metova.mobile.util.time.Dates;

import com.metova.vtube.model.Preferences;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.model.feed.MediaContent;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.ui.MediaPlayerContainer;
import com.metova.vtube.widgets.popup.SendVideoPopupScreen;

public abstract class Videos {

    private static final Logger log = Logger.getLogger( Videos.class );

    private static boolean forceWap;

    private static final String getFormatType() {

        Preferences preferences = PreferenceStore.instance().load();
        String result = preferences.getFormatType();

        // reduce the format quality if we are not on WiFi
        if ( Videos.isForceWap() || !MobileNetwork.instance().isWiFiActive() ) {

            if ( preferences.useReducedFormat() ) {

                String[] acceptableFormatTypes = FormatTypes.getFormatTypeOptions( false );
                result = acceptableFormatTypes[0];
            }
        }

        return result;
    }

    public static MediaContent getMediaContent( FeedEntry feedEntry ) {

        Media media = feedEntry.getMedia();
        Map contents = media.getContents();

        String formatType = getFormatType();

        MediaContent mediaContent = (MediaContent) contents.get( formatType );
        if ( mediaContent == null ) {

            String backupFormatType = FormatTypes.getBackupFormatType( formatType );
            mediaContent = (MediaContent) contents.get( backupFormatType );
        }

        return mediaContent;
    }

    public static final String getUrl( FeedEntry feedEntry ) {

        MediaContent mediaContent = getMediaContent( feedEntry );
        return ( mediaContent == null ) ? null : mediaContent.getUrl();
    }

    public static void displayVideo( FeedEntry feedEntry ) {

        Screens.pushScreenLater( new MediaPlayerContainer( feedEntry ) );
    }

    private static void appendVisionLink( StringBuffer stringBuffer ) {

        stringBuffer.append( "You can download Vision by going to:\n" );
        stringBuffer.append( "http://vision.metova.com/download" );
    }

    private static final String getTellAFriendMessage() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "Check out this application called Vision;" );
        stringBuffer.append( " It allows you to watch videos on your BlackBerry.\n\n" );
        appendVisionLink( stringBuffer );
        return stringBuffer.toString();
    }

    public static void emailFriend() {

        try {

            Message message = new Message();
            message.setSubject( "Have you heard about Vision?" );
            message.setContent( getTellAFriendMessage() );
            Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, new MessageArguments( message ) );
        }
        catch (Throwable t) {

            log.error( "Unable to email message", t );
            MobileAlert.instance().informAndWait( "Unable to send email." );
        }
    }

    public static void smsFriend() {

        try {

            String body = getTellAFriendMessage();
            MessageArguments arguments = new MessageArguments( new TextMessage( body ) );
            Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, arguments );
        }
        catch (Throwable t) {

            log.error( "Unable to text message", t );
            MobileAlert.instance().informAndWait( "Unable to send text message." );
        }
    }

    private static final String getSendVideoMessage( FeedEntry feedEntry ) {

        Media media = feedEntry.getMedia();
        String title = media.getTitle();
        String link = feedEntry.getLink();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( "Check out this video on YouTube\n\n" );
        stringBuffer.append( "Title: " + title + "\n\n" );
        stringBuffer.append( "Link: " + link + "\n\n" );
        stringBuffer.append( "Sent via Vision. " );
        appendVisionLink( stringBuffer );
        return stringBuffer.toString();
    }

    public static void emailVideo( FeedEntry feedEntry ) {

        try {

            Message message = new Message();
            message.setSubject( "Check out this video on YouTube" );
            message.setContent( getSendVideoMessage( feedEntry ) );
            Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, new MessageArguments( message ) );
        }
        catch (Throwable t) {

            log.error( "Unable to email message", t );
            MobileAlert.instance().informAndWait( "Unable to send email." );
        }
    }

    public static void smsVideo( FeedEntry feedEntry ) {

        try {

            String body = getSendVideoMessage( feedEntry );
            MessageArguments arguments = new MessageArguments( new TextMessage( body ) );
            Invoke.invokeApplication( Invoke.APP_TYPE_MESSAGES, arguments );
        }
        catch (Throwable t) {

            log.error( "Unable to text message", t );
            MobileAlert.instance().informAndWait( "Unable to send text message." );
        }
    }

    public static void makeMenu( final FeedEntry feedEntry, Menu menu ) {

        menu.add( Menus.newMenuItem( "Watch Video", new ThreadPoolDelegatingRunnable() {

            public void onRun() {

                Videos.displayVideo( feedEntry );
            }
        }, Menus.ORDINAL_L1, 1 ) );

        if ( !Text.isNull( feedEntry.getLink() ) ) {

            menu.add( Menus.newMenuItemPushScreen( "Send Video", new SendVideoPopupScreen( feedEntry ), Menus.ORDINAL_L1, 1 ) );
        }
    }

    public static final String getLengthText( long length, boolean forceHours ) {

        StringBuffer stringBuffer = new StringBuffer();

        if ( length >= Dates.HOUR ) {

            int time = (int) Math.floor( MathUtils.doubleDivision( length, Dates.HOUR ) );
            length = length - (int) ( Dates.HOUR * time );

            if ( time < 10 ) {

                stringBuffer.append( "0" );
            }

            stringBuffer.append( time );
            stringBuffer.append( ":" );
        }
        else if ( forceHours ) {

            stringBuffer.append( "00:" );
        }

        if ( length >= Dates.MINUTE ) {

            int time = (int) Math.floor( MathUtils.doubleDivision( length, Dates.MINUTE ) );
            length = length - (int) ( Dates.MINUTE * time );

            stringBuffer.append( ( time < 10 ) ? "0" : "" );
            stringBuffer.append( time );
        }
        else {

            stringBuffer.append( "00" );
        }

        if ( length >= Dates.SECOND ) {

            int time = (int) Math.floor( MathUtils.doubleDivision( length, Dates.SECOND ) );
            length = length - (int) ( Dates.SECOND * time );

            stringBuffer.append( ( time < 10 ) ? ":0" : ":" );
            stringBuffer.append( time );
        }
        else {

            stringBuffer.append( ":00" );
        }

        return stringBuffer.toString();
    }

    public static boolean isForceWap() {

        return forceWap;
    }

    public static void setForceWap( boolean forceWap ) {

        Videos.forceWap = forceWap;
    }
}
