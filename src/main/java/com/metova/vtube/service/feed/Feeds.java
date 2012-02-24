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

import m.java.util.Map;

import org.metova.mobile.event.ProgressEvent;
import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.persistence.PersistableVector;

import com.metova.vtube.Settings;
import com.metova.vtube.service.Parameters;
import com.metova.vtube.service.WebServices;
import com.metova.vtube.service.persistence.SearchHistoryStore;

public class Feeds {

    private static final String[] RESULTS_PER_PAGE = new String[] { "10", "15", "20", "25", "30", "40", "50" };
    public static final String DEFAULT_RESULTS_PER_PAGE = RESULTS_PER_PAGE[0];

    private static boolean loading;

    public static final String[] getResultsPerPage() {

        return RESULTS_PER_PAGE;
    }

    public static void search( String query, String orderByName ) {

        search( query, orderByName, Settings.MINIMUM_START_INDEX );
    }

    public static void search( String query, String orderByName, int startIndex ) {

        if ( !isLoading() ) {

            setLoading( true );

            EventDispatcher.instance().fireEventWithBlocking( ProgressEvent.newAbsolute( "Loading: " + query, 1 ) );

            Map parameters = WebServices.getParameters( startIndex );
            parameters.put( Settings.PARAMETER_ORDER_BY, Parameters.getOrderById( orderByName ) );
            parameters.put( Settings.PARAMETER_QUERY, WebServices.urlEncode( query ) );

            String url = WebServices.getServiceUrl( Settings.API_SEARCH );

            FeedServiceEntry feedServiceEntry = WebServices.instance().getFeedEntry( url, parameters );
            feedServiceEntry.setTitle( query );
            feedServiceEntry.setSearch( true );

            PersistableVector searchHistoryStore = SearchHistoryStore.instance().load();
            if ( !searchHistoryStore.contains( query ) ) {

                searchHistoryStore.addElement( query );
                SearchHistoryStore.instance().commit();
            }

            WebServices.instance().directRequest( feedServiceEntry );
        }
    }

    public static void loadFeed( String feed ) {

        loadFeed( feed, Settings.MINIMUM_START_INDEX );
    }

    public static void loadFeed( String feed, int startIndex ) {

        if ( !isLoading() ) {

            setLoading( true );

            EventDispatcher.instance().fireEventWithBlocking( ProgressEvent.newAbsolute( "Loading: " + feed, 1 ) );

            Map parameters = WebServices.getParameters( startIndex );

            String url = WebServices.getServiceUrl( Settings.API_FEEDS ) + "/" + Parameters.getFeedId( feed );

            FeedServiceEntry feedServiceEntry = WebServices.instance().getFeedEntry( url, parameters );
            feedServiceEntry.setTitle( feed );

            WebServices.instance().directRequest( feedServiceEntry );
        }
    }

    public static void completeLoad() {

        setLoading( false );
    }

    private static boolean isLoading() {

        return loading;
    }

    private static void setLoading( boolean loading ) {

        Feeds.loading = loading;
    }
}
