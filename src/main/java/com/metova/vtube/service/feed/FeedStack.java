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

import java.util.Stack;

import m.java.util.Comparator;
import m.java.util.Iterator;
import m.java.util.Map;
import m.java.util.Set;
import m.java.util.TreeSet;

import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.net.queue.QueueDefinition;
import org.metova.mobile.net.queue.QueueManager;
import org.metova.mobile.util.text.Text;

import com.metova.vtube.event.FeedLoadedEvent;
import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.service.Parameters;
import com.metova.vtube.service.UID;
import com.metova.vtube.service.WebServices;
import com.metova.vtube.service.video.thumbnail.Thumbnails;

public class FeedStack {

    private static final String MARKER_DELIMITER = ":";

    private static Stack myself;

    private static Stack instance() {

        if ( myself == null ) {

            myself = new Stack();
        }

        return myself;
    }

    private static int currentPageIndex;

    public static QueueDefinition getQueueDefinition( Feed feed ) {

        int startIndex = feed.getStartIndex();
        int endIndex = startIndex + feed.getItemsPerPage();

        String description = ".queue.thumbnail." + createPageId( feed );
        org.metova.mobile.persistence.UID uid = UID.getSafeUID( description );
        return new QueueDefinition( "Thumbnails [" + startIndex + "-" + endIndex + "]", uid, QueueDefinition.QUEUE_TYPE_TRANSIENT );
    }

    public static int getPageCount() {

        Stack feedStack = instance();
        return feedStack.size();
    }

    public static final String createPageId( Feed feed ) {

        String title = feed.getTitle();
        int startIndex = feed.getStartIndex();
        return String.valueOf( title.hashCode() ) + MARKER_DELIMITER + startIndex;
    }

    public static Feed currentPage() {

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            int currentPageIndex = getCurrentPageIndex();
            return (Feed) instance().elementAt( currentPageIndex );
        }

        return null;
    }

    public static Iterator currentPageEntries() {

        Set set = new TreeSet( new FeedEntryComparator() );

        Feed feed = currentPage();
        if ( feed != null ) {

            Map entries = feed.getEntries();
            if ( entries != null ) {

                set.addAll( entries.values() );
            }
        }

        return set.iterator();
    }

    /**
     * Check if a FeedEntry.id is compatible with the current results
     * @param feedId - Example: "http://gdata.youtube.com/feeds/api/videos/zeYcfiq1lJ4"
     * @return
     */
    public static boolean acceptsId( String feedId ) {

        boolean result = true;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            String pageId = createPageId( first );

            String feedMarker = Text.substringBefore( feedId, MARKER_DELIMITER );
            String pageMarker = Text.substringBefore( pageId, MARKER_DELIMITER );
            result = Text.equals( feedMarker, pageMarker );
        }

        return result;
    }

    /**
     * Check if a Feed title or search query is compatible with the current results
     * @param title
     * @return
     */
    public static boolean acceptsTitle( String title ) {

        boolean result = true;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            result = Text.equals( title, first.getTitle() );
        }

        return result;
    }

    public static boolean isCurrentPage( String feedId ) {

        boolean result = false;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            String pageId = createPageId( first );

            if ( Text.equals( feedId, pageId ) ) {

                result = true;
            }
        }

        return result;
    }

    public static final String getTitle() {

        String result = null;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            result = first.getTitle();
        }

        return result;
    }

    public static boolean isSearch() {

        boolean result = false;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            result = first.isSearch();
        }

        return result;
    }

    public static final String getOrderBy() {

        String result = null;

        Stack feedStack = instance();
        if ( !feedStack.isEmpty() ) {

            Feed first = (Feed) feedStack.firstElement();
            result = first.getOrderBy();
        }

        return result;
    }

    public static boolean isFeedEmpty( Feed feed ) {

        boolean result = true;

        if ( feed != null ) {

            Map entries = feed.getEntries();
            result = ( entries == null ) || entries.isEmpty();
        }

        return result;
    }

    public static void push( Feed feed ) {

        Stack feedStack = instance();

        if ( isFeedEmpty( feed ) || feed.getStartIndex() == 1 || !Text.equals( getTitle(), feed.getTitle() ) ) {

            feedStack.removeAllElements();

            QueueManager queueManager = WebServices.instance().getQueueManager();
            queueManager.clearAllWithBlocking();
        }

        feedStack.push( feed );
        setCurrentPageIndex( feedStack.size() - 1 );

        Feed currentPage = currentPage();
        Thumbnails.cacheThumbnails( currentPage, true );
    }

    public static Feed find( String pageId ) {

        String startIndex = Text.substringAfter( pageId, MARKER_DELIMITER );

        Stack feedStack = instance();
        for (int i = 0; i < feedStack.size(); i++) {

            Feed feed = (Feed) feedStack.elementAt( i );
            if ( Text.equals( String.valueOf( feed.getStartIndex() ), startIndex ) ) {

                return feed;
            }
        }

        return null;
    }

    public static void reloadFeed() {

        if ( !isSearch() ) {

            Feeds.loadFeed( getTitle() );
        }
    }

    public static boolean isPreviousPageAvailable() {

        int currentPageIndex = getCurrentPageIndex();
        return currentPageIndex > 0;
    }

    public static void previousPage() {

        if ( isPreviousPageAvailable() ) {

            int currentPageIndex = getCurrentPageIndex();
            changePage( currentPageIndex - 1 );

            EventDispatcher.instance().fireEvent( new FeedLoadedEvent( false ) );
        }
    }

    public static boolean isNextPageAvailable() {

        Stack feedStack = instance();
        int currentPageIndex = getCurrentPageIndex();
        return currentPageIndex < feedStack.size() - 1;
    }

    public static boolean doesNextPageExist() {

        boolean result = false;

        Stack feedStack = instance();
        if ( feedStack != null && !feedStack.isEmpty() ) {

            int currentPageIndex = getCurrentPageIndex();
            if ( currentPageIndex < feedStack.size() ) {

                Feed feed = (Feed) feedStack.elementAt( currentPageIndex );
                result = feed.getStartIndex() + feed.getItemsPerPage() < feed.getTotalResults();
            }
        }

        return result;
    }

    public static void nextPage() {

        if ( isNextPageAvailable() ) {

            int currentPageIndex = getCurrentPageIndex();
            changePage( currentPageIndex + 1 );

            EventDispatcher.instance().fireEvent( new FeedLoadedEvent( false ) );
        }
        else if ( doesNextPageExist() ) {

            String title = getTitle();
            String orderBy = Parameters.getOrderByName( getOrderBy() );

            Feed currentPage = currentPage();
            int startIndex = currentPage.getStartIndex() + currentPage.getItemsPerPage() + 1;

            if ( isSearch() ) {

                Feeds.search( title, orderBy, startIndex );
            }
            else {

                Feeds.loadFeed( title, startIndex );
            }
        }
    }

    private static void changePage( int currentPageIndex ) {

        Feed previousPage = currentPage();
        Thumbnails.cacheThumbnails( previousPage, false );

        setCurrentPageIndex( currentPageIndex );

        Feed currentPage = currentPage();
        Thumbnails.cacheThumbnails( currentPage, true );
    }

    private static class FeedEntryComparator implements Comparator {

        public int compare( Object object1, Object object2 ) {

            FeedEntry feedEntry1 = (FeedEntry) object1;
            int index1 = feedEntry1.getIndex();

            FeedEntry feedEntry2 = (FeedEntry) object2;
            int index2 = feedEntry2.getIndex();

            return index1 - index2;
        }
    }

    public static int getCurrentPageIndex() {

        return currentPageIndex;
    }

    private static void setCurrentPageIndex( int currentPageIndex ) {

        FeedStack.currentPageIndex = currentPageIndex;
    }
}
