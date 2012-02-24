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

import net.rim.device.api.system.Characters;

import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.media.detail.MediaDetailDescriptionField;

public class MediaDetailContainer extends AbstractLicensedContainer {

    private FeedEntry feedEntry;

    private MediaDetailDescriptionField mediaDetailDescriptionField;

    public MediaDetailContainer(FeedEntry feedEntry) {

        setTitle( "Details" );

        setFeedEntry( feedEntry );
    }

    protected void onInitializeContent() {

        setMediaDetailDescriptionField( new MyMediaDetailDescriptionField() );
    }

    protected void addContentFields() {

        add( getMediaDetailDescriptionField() );
    }

    protected boolean keyChar( char c, int status, int time ) {

        switch (c) {

            case Characters.SPACE: {

                Timers.runNow( new WatchVideoAction() );
                return true;
            }
        }

        return super.keyChar( c, status, time );
    }

    private class MyMediaDetailDescriptionField extends MediaDetailDescriptionField {

        protected FeedEntry getFeedEntry() {

            return MediaDetailContainer.this.getFeedEntry();
        }
    }

    private class WatchVideoAction implements Runnable {

        public void run() {

            Videos.displayVideo( getFeedEntry() );
        }
    }

    private FeedEntry getFeedEntry() {

        return feedEntry;
    }

    private void setFeedEntry( FeedEntry feedEntry ) {

        this.feedEntry = feedEntry;
    }

    private MediaDetailDescriptionField getMediaDetailDescriptionField() {

        return mediaDetailDescriptionField;
    }

    private void setMediaDetailDescriptionField( MediaDetailDescriptionField mediaDetailDescriptionField ) {

        this.mediaDetailDescriptionField = mediaDetailDescriptionField;
    }
}
