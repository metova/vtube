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
package com.metova.vtube.service.video.thumbnail;

import java.util.Vector;

import m.java.util.Map;
import net.rim.device.api.system.EncodedImage;

import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.net.HttpResponse;
import org.metova.mobile.net.queue.Entry;
import org.metova.mobile.net.queue.HttpResponseCallback;
import org.metova.mobile.persistence.PersistableStringMap;

import com.metova.vtube.event.ThumbnailLoadedEvent;
import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.model.feed.MediaThumbnail;
import com.metova.vtube.service.feed.FeedStack;

public class ThumbnailCallback extends HttpResponseCallback {

    protected void receiveResponse() {

        Entry entry = getEntry();
        PersistableStringMap contextMap = entry.getContextMap();

        String feedId = contextMap.get( Feed.ID );
        if ( FeedStack.acceptsId( feedId ) ) {

            Feed feed = FeedStack.find( feedId );
            if ( feed != null ) {

                Map entries = feed.getEntries();
                String feedEntryId = contextMap.get( FeedEntry.ID );
                FeedEntry feedEntry = (FeedEntry) entries.get( feedEntryId );
                if ( feedEntry != null ) {

                    String thumbnailId = contextMap.get( MediaThumbnail.ID );
                    storeThumbnails( feedId, feedEntry, Integer.parseInt( thumbnailId ) );
                }
            }
        }
    }

    private void storeThumbnails( String feedId, FeedEntry feedEntry, int thumbnailId ) {

        HttpResponse httpResponse = getHttpResponse();
        byte[] data = httpResponse.getData();

        Media media = feedEntry.getMedia();
        Vector mediaThumbnails = media.getThumbnails();
        MediaThumbnail mediaThumbnail = (MediaThumbnail) mediaThumbnails.elementAt( thumbnailId );

        EncodedImage encodedImage = EncodedImage.createEncodedImage( data, 0, data.length );
        mediaThumbnail.setEncodedImage( encodedImage );

        if ( FeedStack.isCurrentPage( feedId ) ) {

            Thumbnails.cacheThumbnail( mediaThumbnail, false );
        }

        EventDispatcher.instance().fireEvent( new ThumbnailLoadedEvent( feedId, mediaThumbnail.getUrl() ) );
    }
}
