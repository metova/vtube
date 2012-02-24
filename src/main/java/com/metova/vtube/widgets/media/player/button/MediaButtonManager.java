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
package com.metova.vtube.widgets.media.player.button;

import net.rim.device.api.ui.XYRect;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;

import com.metova.vtube.event.PlayerControlRepaintEvent;
import com.metova.vtube.service.player.MediaPlayer;

public abstract class MediaButtonManager extends AbstractHorizontalFieldManager {

    private ButtonField playButton;
    private ButtonField stopButton;

    public MediaButtonManager() {

        super( USE_ALL_WIDTH | USE_ALL_HEIGHT | NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL );

        addEventListener( new PlayerControlRepaintEventListener(), PlayerControlRepaintEvent.class );
    }

    protected abstract MediaPlayer getMediaPlayer();

    protected abstract void resetPlayer();

    protected void initializeFields() {

        setPlayButton( new MyPlayButtonField( new PausePlayAction() ) );
        setStopButton( new StopButtonField( new StopAction() ) );
    }

    protected void addFields() {

        add( getPlayButton() );
        add( getStopButton() );
    }

    protected int firstFocus( int direction ) {

        ButtonField playButton = getPlayButton();
        return ( playButton == null ) ? 0 : playButton.getIndex();
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();
        height = getHeight();

        ButtonField playButton = getPlayButton();
        if ( playButton != null && equals( playButton.getManager() ) ) {

            XYRect extent = playButton.getExtent();

            int fieldWidth = playButton.getWidth();
            int fieldX = (int) ( width * 0.50 ) - fieldWidth;
            setPositionChild( playButton, fieldX, extent.y );
        }

        ButtonField stopButton = getStopButton();
        if ( stopButton != null && equals( stopButton.getManager() ) ) {

            XYRect extent = stopButton.getExtent();

            int fieldX = (int) ( width * 0.50 );
            setPositionChild( stopButton, fieldX, extent.y );
        }
    }

    private class PausePlayAction extends ThreadPoolDelegatingRunnable {

        public void onRun() {

            MediaPlayer mediaPlayer = getMediaPlayer();
            if ( mediaPlayer.isPlaying() ) {

                mediaPlayer.stop();
            }
            else {

                mediaPlayer.start();
            }

            invalidate();
        }
    }

    private class StopAction extends ThreadPoolDelegatingRunnable {

        public void onRun() {

            resetPlayer();
        }
    }

    private class MyPlayButtonField extends PlayButtonField {

        protected MyPlayButtonField(Runnable runnable) {

            super( runnable );
        }

        protected MediaPlayer getMediaPlayer() {

            return MediaButtonManager.this.getMediaPlayer();
        }
    }

    private class PlayerControlRepaintEventListener implements EventListener {

        public void onEvent( Event event ) {

            invalidate();
        }
    }

    private ButtonField getPlayButton() {

        return playButton;
    }

    private void setPlayButton( ButtonField playButton ) {

        this.playButton = playButton;
    }

    private ButtonField getStopButton() {

        return stopButton;
    }

    private void setStopButton( ButtonField stopButton ) {

        this.stopButton = stopButton;
    }
}
