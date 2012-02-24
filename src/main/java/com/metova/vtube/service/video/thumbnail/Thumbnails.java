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

import java.util.Enumeration;
import java.util.Vector;

import m.java.util.HashMap;
import m.java.util.Iterator;
import m.java.util.Map;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

import org.metova.bb.widgets.theme.StyleManager;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;
import org.metova.mobile.net.queue.Entry;
import org.metova.mobile.net.queue.Queue;
import org.metova.mobile.net.queue.QueueDefinition;
import org.metova.mobile.net.queue.QueueManager;
import org.metova.mobile.net.queue.Status;
import org.metova.mobile.util.cache.ExpiringCache;
import org.metova.mobile.util.math.MathUtils;

import com.metova.vtube.Settings;
import com.metova.vtube.event.ThumbnailLoadedEvent;
import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.model.feed.MediaThumbnail;
import com.metova.vtube.service.WebServices;
import com.metova.vtube.service.feed.FeedStack;

public class Thumbnails {

    private static Thumbnails myself;

    public static Thumbnails instance() {

        if ( myself == null ) {

            synchronized (Thumbnails.class) {

                if ( myself == null ) {

                    myself = new Thumbnails();
                }
            }
        }

        return myself;
    }

    private boolean highResolution;

    private int originalWidth;
    private int originalHeight;

    private int thumbnailWidth;
    private int thumbnailHeight;

    private StyleManager styleManager;

    private Thumbnails() {

        applyComputedStyle();
    }

    public int selectByResolution( int high, int low ) {

        return isHighResolution() ? high : low;
    }

    public int getOriginalWidth() {

        if ( originalWidth <= 0 ) {

            setOriginalWidth( selectByResolution( 130, 80 ) );
        }

        return originalWidth;
    }

    public int getOriginalHeight() {

        if ( originalHeight <= 0 ) {

            setOriginalHeight( selectByResolution( 97, 60 ) );
        }

        return originalHeight;
    }

    public int getThumbnailWidth() {

        if ( thumbnailWidth <= 0 ) {

            setThumbnailWidth( selectByResolution( 60, 36 ) );
        }

        return thumbnailWidth;
    }

    public int getThumbnailHeight() {

        if ( thumbnailHeight <= 0 ) {

            setThumbnailHeight( selectByResolution( 45, 27 ) );
        }

        return thumbnailHeight;
    }

    public static int getCarouselIncrement() {

        int originalWidth = instance().getOriginalWidth();
        int thumbnailWidth = instance().getThumbnailWidth();

        int difference = originalWidth - thumbnailWidth;
        return (int) MathUtils.doubleDivision( difference, Settings.CAROUSEL_FRAMES );
    }

    public static void cacheThumbnails( Feed feed, boolean cache ) {

        Map feedEntries = feed.getEntries();
        if ( feedEntries != null && !feedEntries.isEmpty() ) {

            Iterator iterator = feedEntries.values().iterator();
            while (iterator.hasNext()) {

                FeedEntry feedEntry = (FeedEntry) iterator.next();

                Media media = feedEntry.getMedia();
                Vector mediaThumbnails = media.getThumbnails();

                if ( cache ) {

                    cacheThumbnails( mediaThumbnails, true );
                }
                else {

                    uncacheThumbnails( mediaThumbnails );
                }
            }
        }
    }

    private static void cacheThumbnails( Vector mediaThumbnails, boolean animation ) {

        Enumeration enumerator = mediaThumbnails.elements();
        while (enumerator.hasMoreElements()) {

            MediaThumbnail mediaThumbnail = (MediaThumbnail) enumerator.nextElement();
            cacheThumbnail( mediaThumbnail, animation );
        }
    }

    private static void uncacheThumbnails( Vector mediaThumbnails ) {

        Enumeration enumerator = mediaThumbnails.elements();
        while (enumerator.hasMoreElements()) {

            MediaThumbnail mediaThumbnail = (MediaThumbnail) enumerator.nextElement();
            uncacheThumbnail( mediaThumbnail );
        }
    }

    private static void uncacheThumbnail( MediaThumbnail mediaThumbnail ) {

        ExpiringCache expiringCache = mediaThumbnail.getBitmapCache();
        expiringCache.clear();
    }

    public static void cacheThumbnail( MediaThumbnail mediaThumbnail, boolean animation ) {

        int thumbnailWidth = instance().getThumbnailWidth();
        int thumbnailHeight = instance().getThumbnailHeight();

        int originalWidth = instance().getOriginalWidth();
        int originalHeight = instance().getOriginalHeight();

        getBitmap( mediaThumbnail, thumbnailWidth, thumbnailHeight );
        getBitmap( mediaThumbnail, originalWidth, originalHeight );

        if ( animation ) {

            int increment = getCarouselIncrement();
            for (int i = thumbnailWidth; i <= originalWidth; i += increment) {

                cacheThumbnail( mediaThumbnail, originalWidth, i );
                cacheThumbnail( mediaThumbnail, i, thumbnailWidth );
            }

        }
    }

    private static void cacheThumbnail( MediaThumbnail mediaThumbnail, int divisor, int dividend ) {

        double scale = MathUtils.doubleDivision( divisor, dividend );

        int bitmapWidth = instance().getThumbnailWidth();
        int bitmapHeight = instance().getThumbnailHeight();

        bitmapWidth *= scale;
        bitmapHeight *= scale;
        getBitmap( mediaThumbnail, bitmapWidth, bitmapHeight );
    }

    public static Bitmap getBitmap( MediaThumbnail mediaThumbnail, int width, int height ) {

        Bitmap bitmap = null;

        EncodedImage encodedImage = mediaThumbnail.getEncodedImage();
        if ( encodedImage != null ) {

            ExpiringCache expiringCache = mediaThumbnail.getBitmapCache();
            String key = width + "x" + height;

            bitmap = (Bitmap) expiringCache.get( key );
            if ( bitmap == null ) {

                int multX = Fixed32.tenThouToFP( (int) MathUtils.doubleDivision( encodedImage.getWidth() * 10000, width ) );
                int multY = Fixed32.tenThouToFP( (int) MathUtils.doubleDivision( encodedImage.getHeight() * 10000, height ) );
                bitmap = encodedImage.scaleImage32( multX, multY ).getBitmap();

                expiringCache.put( key, bitmap );
            }
        }

        return bitmap;
    }

    public static int getFirstThumbnailId( FeedEntry feedEntry ) {

        Media media = feedEntry.getMedia();
        Vector mediaThumbnails = media.getThumbnails();
        int size = mediaThumbnails.size();
        return ( size > 1 ) ? 1 : 0;
    }

    private static int incrementThumbnailId( FeedEntry feedEntry, int first, int index, int size ) {

        return ( index < size - 1 ) ? index + 1 : first;
    }

    private static int incrementThumbnailId( FeedEntry feedEntry, int index, int size ) {

        int firstThumbnailId = getFirstThumbnailId( feedEntry );
        return incrementThumbnailId( feedEntry, firstThumbnailId, index, size );
    }

    private static boolean isThumbnailAvailable( Media media, int id ) {

        Vector thumbnails = media.getThumbnails();
        MediaThumbnail mediaThumbnail = (MediaThumbnail) thumbnails.elementAt( id );
        return mediaThumbnail.getEncodedImage() != null;
    }

    public static int findAvailableThumbnailId( FeedEntry feedEntry, int start, boolean increment ) {

        int result = start;

        if ( feedEntry != null ) {

            Media media = feedEntry.getMedia();
            Vector mediaThumbnails = media.getThumbnails();
            int size = mediaThumbnails.size();

            if ( increment ) {

                result = incrementThumbnailId( feedEntry, result, size );
            }

            for (int i = 0; size > 1 && i < size - 1 && !isThumbnailAvailable( media, result ); i++) {

                result = incrementThumbnailId( feedEntry, result, size );
            }
        }

        return result;
    }

    public static void queueThumbnails( Feed feed ) {

        Map feedEntries = feed.getEntries();
        if ( feedEntries != null && !feedEntries.isEmpty() ) {

            Iterator iterator = feedEntries.values().iterator();
            while (iterator.hasNext()) {

                FeedEntry feedEntry = (FeedEntry) iterator.next();

                Media media = feedEntry.getMedia();
                Vector mediaThumbnails = media.getThumbnails();
                queueThumbnails( feed, feedEntry, mediaThumbnails );
            }
        }
    }

    private static void queueThumbnails( Feed feed, FeedEntry feedEntry, Vector mediaThumbnails ) {

        int size = mediaThumbnails.size();
        for (int i = 0, thumbnailId = getFirstThumbnailId( feedEntry ); i < size; i++) {

            MediaThumbnail mediaThumbnail = (MediaThumbnail) mediaThumbnails.elementAt( thumbnailId );
            if ( mediaThumbnail.getEncodedImage() == null ) {

                Map contextMap = new HashMap();
                contextMap.put( Feed.ID, FeedStack.createPageId( feed ) );
                contextMap.put( FeedEntry.ID, feedEntry.getId() );
                contextMap.put( MediaThumbnail.ID, String.valueOf( mediaThumbnail.getId() ) );

                String url = mediaThumbnail.getUrl();
                QueueDefinition queueDefinition = FeedStack.getQueueDefinition( feed );
                WebServices.instance().queueGetRequest( url, ThumbnailCallback.class, contextMap, queueDefinition );

                // queue one frame at a time
                break;
            }
            else {

                thumbnailId = incrementThumbnailId( feedEntry, 0, thumbnailId, size );
            }
        }
    }

    public static class ThumbnailQueueListener implements EventListener {

        public void onEvent( Event event ) {

            synchronized (ThumbnailQueueListener.class) {

                ThumbnailLoadedEvent thumbnailLoadedEvent = (ThumbnailLoadedEvent) event;

                String feedId = thumbnailLoadedEvent.getFeedId();
                Feed feed = FeedStack.find( feedId );

                QueueManager queueManager = WebServices.instance().getQueueManager();
                Queue queue = queueManager.getQueue( FeedStack.getQueueDefinition( feed ) );

                boolean downloadMore = true;

                Enumeration enumerator = queue.getEnumeration();
                while (downloadMore && enumerator.hasMoreElements()) {

                    Entry entry = (Entry) enumerator.nextElement();
                    Status status = entry.getStatus();
                    int statusCode = status.getCode();

                    downloadMore &= statusCode != Status.QUEUED;
                    downloadMore &= statusCode != Status.SENDING;
                }

                if ( downloadMore ) {

                    queueThumbnails( feed );
                }
            }
        }
    }

    private void applyComputedStyle() {

        StyleManager styleManager = getStyleManager();
        setHighResolution( styleManager.getValueBoolean( "high-resolution" ) );
    }

    private StyleManager getStyleManager() {

        if ( styleManager == null ) {

            setStyleManager( new StyleManager( "ThumbnailCarousel-thumbnails" ) );
        }

        return styleManager;
    }

    private void setStyleManager( StyleManager styleManager ) {

        this.styleManager = styleManager;
    }

    private boolean isHighResolution() {

        return highResolution;
    }

    private void setHighResolution( boolean highResolution ) {

        this.highResolution = highResolution;
    }

    private void setOriginalWidth( int originalWidth ) {

        this.originalWidth = originalWidth;
    }

    private void setOriginalHeight( int originalHeight ) {

        this.originalHeight = originalHeight;
    }

    private void setThumbnailWidth( int thumbnailWidth ) {

        this.thumbnailWidth = thumbnailWidth;
    }

    private void setThumbnailHeight( int thumbnailHeight ) {

        this.thumbnailHeight = thumbnailHeight;
    }
}
