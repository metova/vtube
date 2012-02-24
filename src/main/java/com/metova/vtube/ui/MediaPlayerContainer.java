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

import net.rim.device.api.ui.UiApplication;

import org.metova.bb.widgets.media.player.DefaultMediaPlayer;
import org.metova.bb.widgets.util.light.BacklightManager;
import org.metova.mobile.net.queue.QueueManager;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.service.WebServices;
import com.metova.vtube.service.player.MediaPlayer;
import com.metova.vtube.service.player.VolumeKeyListener;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.media.player.MediaPlayerManager;

public class MediaPlayerContainer extends AbstractLicensedContainer {

    private FeedEntry feedEntry;

    private MediaPlayerManager mediaPlayerManager;
    private VolumeKeyListener volumeKeyListener;

    public MediaPlayerContainer(FeedEntry feedEntry) {

        Media media = feedEntry.getMedia();
        setTitle( media.getTitle() );

        setFeedEntry( feedEntry );

        setVolumeKeyListener( new MyVolumeKeyListener() );
    }

    protected void onInitializeContent() {

        FeedEntry feedEntry = getFeedEntry();
        setMediaPlayerManager( new MediaPlayerManager( feedEntry ) );
    }

    protected void addContentFields() {

        add( getMediaPlayerManager() );
    }

    protected void onDisplay() {

        super.onDisplay();

        Timers.runNow( new PauseQueueManagerAction() );
        BacklightManager.instance().enablePersistentBacklight();
        UiApplication.getUiApplication().addKeyListener( getVolumeKeyListener() );
    }

    protected void onUndisplay() {

        super.onUndisplay();

        Timers.runNow( new KillPlayerAction() );

        UiApplication.getUiApplication().removeKeyListener( getVolumeKeyListener() );
        BacklightManager.instance().disablePersistentBacklight();
        Timers.runNow( new UnPauseQueueManagerAction() );
    }

    protected synchronized void onVisibilityChange( boolean visible ) {

        if ( !visible ) {

            ensureStopped();
        }

        super.onVisibilityChange( visible );
    }

    private void ensureStopped() {

        MediaPlayerManager mediaPlayerManager = getMediaPlayerManager();
        if ( mediaPlayerManager != null ) {

            MediaPlayer mediaPlayer = mediaPlayerManager.getMediaPlayer();
            mediaPlayer.stop();
        }
    }

    protected boolean displayWiFiLogo() {

        return super.displayWiFiLogo() && !Videos.isForceWap();
    }

    private class MyVolumeKeyListener extends VolumeKeyListener {

        protected DefaultMediaPlayer getMediaPlayer() {

            MediaPlayerManager mediaPlayerManager = getMediaPlayerManager();
            return mediaPlayerManager.getMediaPlayer();
        }
    }

    private class PauseQueueManagerAction implements Runnable {

        public void run() {

            QueueManager queueManager = WebServices.instance().getQueueManager();
            queueManager.pause();
        }
    }

    private class UnPauseQueueManagerAction implements Runnable {

        public void run() {

            QueueManager queueManager = WebServices.instance().getQueueManager();
            queueManager.unpause();
        }
    }

    private class KillPlayerAction implements Runnable {

        public void run() {

            MediaPlayerManager mediaPlayerManager = getMediaPlayerManager();
            MediaPlayer mediaPlayer = mediaPlayerManager.getMediaPlayer();
            mediaPlayer.killPlayer();
        }
    }

    private MediaPlayerManager getMediaPlayerManager() {

        return mediaPlayerManager;
    }

    private void setMediaPlayerManager( MediaPlayerManager mediaPlayerManager ) {

        this.mediaPlayerManager = mediaPlayerManager;
    }

    private FeedEntry getFeedEntry() {

        return feedEntry;
    }

    private void setFeedEntry( FeedEntry feedEntry ) {

        this.feedEntry = feedEntry;
    }

    private VolumeKeyListener getVolumeKeyListener() {

        return volumeKeyListener;
    }

    private void setVolumeKeyListener( VolumeKeyListener volumeKeyListener ) {

        this.volumeKeyListener = volumeKeyListener;
    }
}
