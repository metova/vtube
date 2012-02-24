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

import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;

import com.metova.vtube.service.player.MediaPlayer;
import com.metova.vtube.widgets.media.player.button.MediaButtonManager;
import com.metova.vtube.widgets.media.player.duration.MediaDurationManager;

abstract class MediaControlManager extends AbstractVerticalFieldManager {

    private MediaDurationManager mediaDurationManager;
    private MediaButtonManager mediaButtonManager;

    public MediaControlManager() {

        super( USE_ALL_WIDTH | USE_ALL_HEIGHT | NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL );
    }

    protected abstract MediaPlayer getMediaPlayer();

    protected abstract void resetPlayer();

    protected void onInitialize() {

        setMediaDurationManager( new MyMediaDurationManager() );
        setMediaButtonManager( new MyMediaButtonManager() );
    }

    protected void onLoading() {

        add( getMediaDurationManager() );
        add( getMediaButtonManager() );
    }

    protected int firstFocus( int direction ) {

        MediaButtonManager mediaButtonManager = getMediaButtonManager();
        return ( mediaButtonManager == null ) ? 0 : mediaButtonManager.getIndex();
    }

    private class MyMediaButtonManager extends MediaButtonManager {

        protected MediaPlayer getMediaPlayer() {

            return MediaControlManager.this.getMediaPlayer();
        }

        protected void resetPlayer() {

            MediaControlManager.this.resetPlayer();
        }
    }

    private class MyMediaDurationManager extends MediaDurationManager {

        protected MediaPlayer getMediaPlayer() {

            return MediaControlManager.this.getMediaPlayer();
        }
    }

    public MediaDurationManager getMediaDurationManager() {

        return mediaDurationManager;
    }

    private void setMediaDurationManager( MediaDurationManager mediaDurationManager ) {

        this.mediaDurationManager = mediaDurationManager;
    }

    public MediaButtonManager getMediaButtonManager() {

        return mediaButtonManager;
    }

    private void setMediaButtonManager( MediaButtonManager mediaButtonManager ) {

        this.mediaButtonManager = mediaButtonManager;
    }
}
