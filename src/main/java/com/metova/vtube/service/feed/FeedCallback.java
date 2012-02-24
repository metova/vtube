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
package com.metova.vtube.service.feed;

import org.metova.mobile.event.ProgressEvent;
import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.net.HttpResponse;
import org.metova.mobile.net.direct.ServiceCallback;

import com.metova.vtube.Settings;
import com.metova.vtube.event.FeedLoadedEvent;
import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.service.feed.parse.FeedParser;
import com.metova.vtube.service.video.thumbnail.Thumbnails;

public class FeedCallback extends ServiceCallback {

    public boolean success() {

        FeedServiceEntry feedServiceEntry = (FeedServiceEntry) getEntry();

        HttpResponse httpResponse = getResponse();

        Feed feed = null;

        byte[] bytes = httpResponse.getData();
        if ( bytes != null && bytes.length > 0 ) {

            feed = FeedParser.parse( bytes );
            feed.setTitle( feedServiceEntry.getTitle() );
            feed.setSearch( feedServiceEntry.isSearch() );
            feed.setOrderBy( feedServiceEntry.getParameter( Settings.PARAMETER_ORDER_BY ) );

            EventDispatcher.instance().fireEvent( ProgressEvent.newAbsolute( null, 100 ) );
        }
        else {

            failure();
        }

        Feed currentPage = FeedStack.currentPage();
        boolean reload = ( currentPage == null ) || !FeedStack.acceptsTitle( feed.getTitle() );

        FeedStack.push( feed );
        Feeds.completeLoad();

        EventDispatcher.instance().fireEvent( new FeedLoadedEvent( reload ) );

        if ( feed != null ) {

            Thumbnails.queueThumbnails( feed );
        }

        return feed != null;
    }

    public void failure() {

        EventDispatcher.instance().fireEvent( ProgressEvent.newAbsolute( "Download failed", 100 ) );

        Feeds.completeLoad();
    }
}
