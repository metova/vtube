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
package com.metova.vtube.service.player;

import javax.microedition.media.Player;
import javax.microedition.media.control.VideoControl;
import javax.microedition.media.control.VolumeControl;

import m.org.apache.log4j.Logger;

import org.metova.bb.widgets.media.player.DefaultMediaPlayer;
import org.metova.bb.widgets.media.player.listener.RtspMediaPlayerListener;
import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.event.PlayerControlRepaintEvent;

public abstract class MediaPlayerListener extends RtspMediaPlayerListener {

    private static final Logger log = Logger.getLogger( MediaPlayerListener.class );

    /**
     * Hide the constructor
     */
    protected MediaPlayerListener() {

        // do nothing
    }

    protected abstract void invalidateDisplay();

    protected abstract void updatedDisplay();

    protected final void onBufferingStarted( Player player, long eventData ) {

        invalidateDisplay();
    }

    protected final void onBufferingStopped( Player player, long eventData ) {

        // do nothing
    }

    protected final void durationUpdated( Player player, long eventData ) {

        log.info( "Duration updated: " + eventData );

        updatedDisplay();
    }

    protected void playerClosed( Player player ) {

        log.info( "Player closed" );
    }

    protected void onPlayerStarted( Player player, long eventData ) {

        log.info( "Player started: " + eventData );

        EventDispatcher.instance().fireEvent( new PlayerControlRepaintEvent() );
    }

    protected void playerStopped( Player player, long eventData ) {

        log.info( "Player stopped: " + eventData );

        EventDispatcher.instance().fireEvent( new PlayerControlRepaintEvent() );
    }

    protected void playerStoppedAtTime( Player player, long eventData ) {

        log.info( "Player stopped at time: " + eventData );

        EventDispatcher.instance().fireEvent( new PlayerControlRepaintEvent() );
    }

    protected void onPlayerError( Player player, String eventData ) {

        log.warn( "Player error, killing player" );

        Timers.runNow( new KillPlayerAction() );
    }

    protected final void sizeChanged( Player player, VideoControl eventData ) {

        log.info( "Size changed" );
    }

    protected final void volumeChanged( Player player, VolumeControl eventData ) {

        log.info( "Volume changed" );
    }

    /**
     * Run this off the event thread
     */
    protected void endOfMedia( Player player, long eventData ) {

        log.info( "End of media: " + eventData );

        DefaultMediaPlayer mediaPlayer = getMediaPlayer();
        mediaPlayer.killPlayer();
    }

    private class KillPlayerAction implements Runnable {

        public void run() {

            DefaultMediaPlayer mediaPlayer = getMediaPlayer();
            mediaPlayer.killPlayer();
        }
    }
}
