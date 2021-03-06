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
package com.metova.vtube.widgets.feed;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.screen.popup.AbstractPopupScreen;
import org.metova.mobile.util.accessor.ListDataAccessor;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.service.feed.Feeds;

public class FeedPopupScreen extends AbstractPopupScreen {

    private FeedManagedListField feedListField;

    public FeedPopupScreen() {

        super( new VerticalFieldManager() );
    }

    protected void onInitialize() {

        setFeedListField( new MyFeedManagedListField() );
    }

    protected void onLoading() {

        add( getFeedListField() );
    }

    protected boolean keyChar( char key, int status, int time ) {

        boolean result = false;

        if ( key == Characters.ESCAPE ) {

            Screens.popScreenLater( this );
            result = true;
        }
        else {

            result = super.keyChar( key, status, time );
        }

        return result;
    }

    private class MyFeedManagedListField extends FeedManagedListField {

        protected ListDataAccessor getListDataAccessor() {

            return new FeedListDataAccessor();
        }

        protected void viewDetails( final Object selected ) {

            Screens.popScreenLater( FeedPopupScreen.this );

            Timers.runNow( new Runnable() {

                public void run() {

                    Feeds.loadFeed( (String) selected );
                }
            } );
        }
    }

    private FeedManagedListField getFeedListField() {

        return feedListField;
    }

    private void setFeedListField( FeedManagedListField feedListField ) {

        this.feedListField = feedListField;
    }
}
