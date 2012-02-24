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
package com.metova.vtube.widgets.media.player;

import java.util.TimerTask;

import javax.microedition.media.Player;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.Menu;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.field.Fields;
import org.metova.bb.widgets.field.image.AnimatedField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.bb.widgets.media.player.DefaultMediaPlayer;
import org.metova.bb.widgets.menu.Menus;
import org.metova.bb.widgets.touch.event.TouchEventPlatform;
import org.metova.mobile.rt.system.MobileNetwork;
import org.metova.mobile.util.text.Text;
import org.metova.mobile.util.time.Dates;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.MediaContent;
import com.metova.vtube.model.player.MediaPlayerException;
import com.metova.vtube.service.player.MediaPlayer;
import com.metova.vtube.service.player.MediaPlayerExceptions;
import com.metova.vtube.service.player.MediaPlayerListener;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.ui.MediaErrorContainer;
import com.metova.vtube.widgets.media.player.duration.MediaDurationManager;
import com.metova.vtube.widgets.popup.SendVideoPopupScreen;

public class MediaPlayerManager extends AbstractVerticalFieldManager {

    private static final Logger log = Logger.getLogger( MediaPlayerManager.class );

    private AnimatedField animatedField;
    private MediaControlManager mediaControlManager;

    private FeedEntry feedEntry;
    private MediaPlayer mediaPlayer;

    private boolean autoplay;
    private boolean repeat;

    private long lastTouchEvent;

    private boolean obscuredWhilePlaying;

    private UpdateDurationTask updateDurationTask;

    public MediaPlayerManager(FeedEntry feedEntry) {

        super( USE_ALL_HEIGHT | NO_VERTICAL_SCROLL );

        setFeedEntry( feedEntry );

        setAutoplay( true );

        setMediaPlayer( new MediaPlayer() );
    }

    protected void onInitialize() {

        AnimatedField animatedField = new AnimatedField();
        animatedField.getStyleManager().setStyleClass( "Media-load-animation" );
        setAnimatedField( animatedField );

        setMediaControlManager( new MyMediaControlManager() );
    }

    protected void onLoading() {

        MediaPlayer mediaPlayer = getMediaPlayer();
        Field videoField = mediaPlayer.getVideoField();
        if ( videoField != null ) {

            add( videoField );
        }
        else {

            add( getAnimatedField() );

            Timers.runNow( new MediaLoader() );
        }

        add( getMediaControlManager() );
    }

    protected void onObscured() {

        MediaPlayer mediaPlayer = getMediaPlayer();
        if ( mediaPlayer.isPlaying() ) {

            setObscuredWhilePlaying( true );
            mediaPlayer.stop();
        }

        super.onObscured();
    }

    protected void onExposed() {

        super.onExposed();

        if ( isObscuredWhilePlaying() ) {

            setObscuredWhilePlaying( false );

            MediaPlayer mediaPlayer = getMediaPlayer();
            mediaPlayer.start();
        }
    }

    /**
     * Run off the event thread
     */
    private void resetPlayer() {

        MediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.killPlayer();

        synchronized (UiApplication.getEventLock()) {

            reload();
        }
    }

    private void cancelTimerTask() {

        UpdateDurationTask updateDurationTask = getUpdateDurationTask();
        if ( updateDurationTask != null ) {

            updateDurationTask.cancel();
            setUpdateDurationTask( null );
        }
    }

    private void startTimerTask() {

        cancelTimerTask();

        long period = Dates.SECOND;
        setUpdateDurationTask( new UpdateDurationTask() );
        Timers.schedule( getUpdateDurationTask(), period, period );
    }

    private void initializeMediaPlayer() throws Exception {

        FeedEntry feedEntry = getFeedEntry();
        MediaContent mediaContent = Videos.getMediaContent( feedEntry );

        MediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.createMediaPlayer( mediaContent.getUrl() );
        mediaPlayer.addPlayerListener( new MyMediaPlayerListener() );

        synchronized (UiApplication.getEventLock()) {

            // reload to switch the loading animation with the new video field
            reload();
        }

        if ( isAutoplay() ) {

            if ( getScreen().getScreenAbove() != null ) {

                setObscuredWhilePlaying( true );
            }
            else {

                mediaPlayer.start();
            }
        }
    }

    protected void updatePlayer() {

        MediaPlayer mediaPlayer = getMediaPlayer();
        if ( mediaPlayer.isPlaying() && !mediaPlayer.isDisplayFullScreen() ) {

            synchronized (UiApplication.getEventLock()) {

                MediaControlManager mediaControlManager = getMediaControlManager();
                MediaDurationManager mediaDurationManager = mediaControlManager.getMediaDurationManager();
                mediaDurationManager.updatePlayer();
            }
        }
    }

    private void setDisplayFullScreen( MediaPlayer mediaPlayer, boolean displayFullScreen ) {

        mediaPlayer.setDisplayFullScreen( displayFullScreen );
    }

    private void popScreenLater() {

        Screen screen = MediaPlayerManager.this.getScreen();
        if ( screen != null ) {

            Screens.popScreenLater( screen );
        }
    }

    protected boolean onTouchEvent( TouchEventPlatform message ) {

        if ( message.getEvent() == TouchEventPlatform.DOWN ) {

            MediaPlayer mediaPlayer = getMediaPlayer();
            if ( mediaPlayer.isPlaying() ) {

                long touchEventTime = message.getTime();
                long lastTouchEvent = getLastTouchEvent();
                if ( touchEventTime - lastTouchEvent >= Dates.SECOND ) {

                    setLastTouchEvent( touchEventTime );

                    boolean displayFullScreen = false;

                    // if we touch inside the video field while
                    // it is playing, then make it fullscreen
                    if ( !mediaPlayer.isDisplayFullScreen() ) {

                        int x = message.getGlobalX( 1 );
                        int y = message.getGlobalY( 1 );

                        Field field = mediaPlayer.getVideoField();
                        XYRect absoluteExtent = Fields.getAbsoluteExtent( field );
                        if ( absoluteExtent.contains( x, y ) ) {

                            displayFullScreen = true;
                        }
                    }

                    setDisplayFullScreen( mediaPlayer, displayFullScreen );
                }
            }
        }

        return super.onTouchEvent( message );
    }

    protected boolean trackwheelClick( int status, int time ) {

        MediaPlayer mediaPlayer = getMediaPlayer();
        if ( mediaPlayer.isPlaying() && mediaPlayer.isDisplayFullScreen() ) {

            // if we click the trackball while the video is playing and is fullscreen,
            // make it not fullscreen. user can use menu or f-key to make it fullscreen again.
            setDisplayFullScreen( mediaPlayer, false );
            return true;
        }

        return super.trackwheelClick( status, time );
    }

    protected boolean keyChar( char key, int status, int time ) {

        final MediaPlayer mediaPlayer = getMediaPlayer();

        if ( key == Characters.SPACE ) {

            Timers.runNow( new PausePlayAction() );
            return true;
        }
        else {

            boolean fullscreen = mediaPlayer.isDisplayFullScreen();
            boolean switchFullscreen = mediaPlayer.isPlaying() && fullscreen;
            if ( switchFullscreen || key == Characters.LATIN_CAPITAL_LETTER_F || key == Characters.LATIN_SMALL_LETTER_F ) {

                setDisplayFullScreen( mediaPlayer, !fullscreen );
                return true;
            }
            else if ( key == Characters.LATIN_CAPITAL_LETTER_R || key == Characters.LATIN_SMALL_LETTER_R ) {

                Timers.runNow( new ResetPlayerAction() );
                return true;
            }
        }

        return super.keyChar( key, status, time );
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();
        height = getHeight();

        int availableHeight = height;

        MediaControlManager mediaControlManager = getMediaControlManager();
        if ( mediaControlManager != null && equals( mediaControlManager.getManager() ) ) {

            XYRect extent = mediaControlManager.getExtent();

            int fieldHeight = mediaControlManager.getPreferredHeight();
            availableHeight -= fieldHeight;

            setPositionChild( mediaControlManager, extent.x, availableHeight );
            layoutChild( mediaControlManager, width, fieldHeight );
        }

        MediaPlayer mediaPlayer = getMediaPlayer();
        Field videoField = mediaPlayer.getVideoField();
        if ( videoField != null && equals( videoField.getManager() ) ) {

            int fieldX = 10;
            int fieldY = 10;

            int fieldWidth = width - 20;
            int fieldHeight = availableHeight - 10;

            layoutChild( videoField, fieldWidth, fieldHeight );
            mediaPlayer.setDisplaySize( fieldWidth, fieldHeight );

            setPositionChild( videoField, fieldX, fieldY );
        }

        AnimatedField animatedField = getAnimatedField();
        if ( animatedField != null && equals( animatedField.getManager() ) ) {

            int fieldWidth = animatedField.getWidth();
            int fieldHeight = animatedField.getHeight();

            int fieldX = (int) ( ( width - fieldWidth ) * 0.50 );
            int fieldY = (int) ( ( availableHeight - fieldHeight ) * 0.50 );

            setPositionChild( animatedField, fieldX, fieldY );
        }
    }

    private void makeL1Menu( Menu menu ) {

        MediaPlayer mediaPlayer = getMediaPlayer();

        String playLabel = mediaPlayer.isPlaying() ? "Pause" : "Play";
        menu.add( Menus.newMenuItem( playLabel, new PausePlayAction(), Menus.ORDINAL_L1, 1 ) );

        menu.add( Menus.newMenuItem( "Restart", new ResetPlayerAction(), Menus.ORDINAL_L1, 1 ) );
    }

    private void makeL2Menu( Menu menu ) {

        MediaPlayer mediaPlayer = getMediaPlayer();
        if ( mediaPlayer.isPlaying() ) {

            boolean fullscreen = mediaPlayer.isDisplayFullScreen();
            String fullscreenLabel = fullscreen ? "Disable" : "Enable";
            menu.add( Menus.newMenuItem( fullscreenLabel + " Fullscreen", new InvertFullScreenAction(), Menus.ORDINAL_L2, 1 ) );
        }

        String repeatLabel = isRepeat() ? "Disable" : "Enable";
        menu.add( Menus.newMenuItem( repeatLabel + " Repeat", new EnableRepeatAction(), Menus.ORDINAL_L2, 1 ) );

        if ( MobileNetwork.instance().isWiFiActive() ) {

            String connectionType = Videos.isForceWap() ? "Enable" : "Disable";
            menu.add( Menus.newMenuItem( connectionType + " WiFi", new DisableWiFiAction(), Menus.ORDINAL_L2, 1 ) );
        }
    }

    protected void makeMenu( Menu menu, int instance ) {

        makeL1Menu( menu );
        makeL2Menu( menu );

        FeedEntry feedEntry = getFeedEntry();
        if ( !Text.isNull( feedEntry.getLink() ) ) {

            menu.add( Menus.newMenuItem( "Send Video", new SendVideoAction(), Menus.ORDINAL_L3, 1 ) );
        }

        super.makeMenu( menu, instance );
    }

    private class PausePlayAction extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            MediaPlayer mediaPlayer = getMediaPlayer();
            if ( mediaPlayer.isPlaying() ) {

                mediaPlayer.stop();
            }
            else {

                mediaPlayer.start();
            }
        }
    }

    private class ResetPlayerAction extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            resetPlayer();
        }
    }

    private class InvertFullScreenAction extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            MediaPlayer mediaPlayer = getMediaPlayer();
            boolean fullscreen = mediaPlayer.isDisplayFullScreen();
            setDisplayFullScreen( mediaPlayer, !fullscreen );
        }
    }

    private class EnableRepeatAction extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            boolean repeat = isRepeat();
            setRepeat( !repeat );
        }
    }

    private class DisableWiFiAction extends ResetPlayerAction {

        protected void onRun() {

            boolean forceWap = Videos.isForceWap();
            Videos.setForceWap( !forceWap );

            super.onRun();
        }
    }

    private class SendVideoAction extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            MediaPlayer mediaPlayer = getMediaPlayer();
            mediaPlayer.stop();

            FeedEntry feedEntry = getFeedEntry();
            Screens.pushScreen( new SendVideoPopupScreen( feedEntry ) );
        }
    }

    private class MyMediaControlManager extends MediaControlManager {

        protected MediaPlayer getMediaPlayer() {

            return MediaPlayerManager.this.getMediaPlayer();
        }

        protected void resetPlayer() {

            setAutoplay( false );

            MediaPlayerManager.this.resetPlayer();
        }
    }

    private class MediaLoader implements Runnable {

        public void run() {

            MediaPlayer mediaPlayer = getMediaPlayer();

            Field videoField = mediaPlayer.getVideoField();
            if ( videoField == null ) {

                synchronized (MediaLoader.class) {

                    videoField = mediaPlayer.getVideoField();
                    if ( videoField == null ) {

                        try {

                            initializeMediaPlayer();
                        }
                        catch (Throwable t) {

                            FeedEntry feedEntry = getFeedEntry();
                            MediaPlayerException errorInfo = MediaPlayerExceptions.generateException( MediaPlayerExceptions.CODE_5ml );
                            Screens.pushScreenLater( new MediaErrorContainer( feedEntry, errorInfo ) );
                            popScreenLater();

                            log.warn( "Unable to create media player", t );
                        }
                    }
                }
            }
        }
    }

    private class MyMediaPlayerListener extends MediaPlayerListener {

        protected DefaultMediaPlayer getMediaPlayer() {

            return MediaPlayerManager.this.getMediaPlayer();
        }

        protected void invalidateDisplay() {

            Screen screen = getScreen();
            if ( screen != null ) {

                screen.invalidate();
            }
        }

        protected void updatedDisplay() {

            synchronized (UiApplication.getEventLock()) {

                MediaControlManager mediaControlManager = getMediaControlManager();
                MediaDurationManager mediaDurationManager = mediaControlManager.getMediaDurationManager();
                mediaDurationManager.updatePlayer();
            }

            invalidateDisplay();
        }

        protected void onPlayerError( Player player, String eventData ) {

            super.onPlayerError( player, eventData );

            FeedEntry feedEntry = getFeedEntry();
            MediaPlayerException errorInfo = MediaPlayerExceptions.generateException( eventData );
            Screens.pushScreenLater( new MediaErrorContainer( feedEntry, errorInfo ) );
            popScreenLater();
        }

        protected void onPlayerStarted( Player player, long eventData ) {

            super.onPlayerStarted( player, eventData );

            startTimerTask();
        }

        protected void playerStopped( Player player, long eventData ) {

            super.playerStopped( player, eventData );

            cancelTimerTask();
        }

        protected void playerStoppedAtTime( Player player, long eventData ) {

            super.playerStoppedAtTime( player, eventData );

            cancelTimerTask();
        }

        protected void playerClosed( Player player ) {

            super.playerClosed( player );

            cancelTimerTask();
        }

        protected void endOfMedia( final Player player, final long eventData ) {

            Timers.runNow( new Runnable() {

                public void run() {

                    endOfMedia2( player, eventData );
                }
            } );
        }

        protected void endOfMedia2( Player player, long eventData ) {

            super.endOfMedia( player, eventData );

            if ( isRepeat() ) {

                resetPlayer();
            }
            else {

                Screen screen = MediaPlayerManager.this.getScreen();
                if ( screen != null ) {

                    Screens.popScreenLater( screen );
                }
            }
        }
    }

    private class UpdateDurationTask extends TimerTask {

        public void run() {

            updatePlayer();
        }
    }

    private FeedEntry getFeedEntry() {

        return feedEntry;
    }

    private void setFeedEntry( FeedEntry feedEntry ) {

        this.feedEntry = feedEntry;
    }

    private AnimatedField getAnimatedField() {

        return animatedField;
    }

    private void setAnimatedField( AnimatedField animatedField ) {

        this.animatedField = animatedField;
    }

    public MediaPlayer getMediaPlayer() {

        return mediaPlayer;
    }

    private void setMediaPlayer( MediaPlayer mediaPlayer ) {

        this.mediaPlayer = mediaPlayer;
    }

    private MediaControlManager getMediaControlManager() {

        return mediaControlManager;
    }

    private void setMediaControlManager( MediaControlManager mediaControlManager ) {

        this.mediaControlManager = mediaControlManager;
    }

    private boolean isAutoplay() {

        return autoplay;
    }

    private void setAutoplay( boolean autoplay ) {

        this.autoplay = autoplay;
    }

    private boolean isRepeat() {

        return repeat;
    }

    private void setRepeat( boolean repeat ) {

        this.repeat = repeat;
    }

    private UpdateDurationTask getUpdateDurationTask() {

        return updateDurationTask;
    }

    private void setUpdateDurationTask( UpdateDurationTask updateDurationTask ) {

        this.updateDurationTask = updateDurationTask;
    }

    private long getLastTouchEvent() {

        return lastTouchEvent;
    }

    private void setLastTouchEvent( long lastTouchEvent ) {

        this.lastTouchEvent = lastTouchEvent;
    }

    private boolean isObscuredWhilePlaying() {

        return obscuredWhilePlaying;
    }

    private void setObscuredWhilePlaying( boolean obscuredWhilePlaying ) {

        this.obscuredWhilePlaying = obscuredWhilePlaying;
    }
}
