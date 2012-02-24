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
package com.metova.vtube.service;

import org.metova.mobile.util.text.Text;

import com.metova.vtube.Settings;

public class Parameters {

    private static final int NAME = 0;
    private static final int ID = 1;

    public static final String[] getParameterOptions( String[][] parameters, boolean name ) {

        String[] result = new String[parameters.length];

        for (int i = 0; i < result.length; i++) {

            result[i] = name ? parameters[i][0] : parameters[i][1];
        }

        return result;
    }

    public static final String getParameterId( String[][] parameters, String name ) {

        String result = null;

        for (int i = 0; i < parameters.length; i++) {

            if ( Text.equals( name, parameters[i][NAME] ) ) {

                result = parameters[i][ID];
                break;
            }
        }

        return result;
    }

    public static final String getParameterName( String[][] parameters, String id ) {

        String result = null;

        for (int i = 0; i < parameters.length; i++) {

            if ( Text.equals( id, parameters[i][ID] ) ) {

                result = parameters[i][NAME];
                break;
            }
        }

        return result;
    }

    /*
     * 
     * ENABLE
     * 
     * 
     */
    private static final String[][] getEnableOptions() {

        String[][] result = new String[2][];
        result[0] = Settings.ENABLE_ON;
        result[1] = Settings.ENABLE_OFF;
        return result;
    }

    public static final String[] getEnableOptions( boolean name ) {

        String[][] parameters = getEnableOptions();
        return getParameterOptions( parameters, name );
    }

    public static boolean getEnableId( String name ) {

        String[][] parameters = getEnableOptions();
        return Text.booleanValueOf( getParameterId( parameters, name ) );
    }

    public static final String getEnableName( boolean enable ) {

        String[][] parameters = getEnableOptions();
        return getParameterName( parameters, String.valueOf( enable ) );
    }

    /*
     * 
     * ORDER BY
     * 
     * 
     */
    private static final String[][] getOrderByOptions() {

        String[][] result = new String[4][];
        result[0] = Settings.ORDER_BY_RELEVANCE;
        result[1] = Settings.ORDER_BY_PUBLISHED;
        result[2] = Settings.ORDER_BY_VIEW_COUNT;
        result[3] = Settings.ORDER_BY_RATING;
        return result;
    }

    public static final String[] getOrderByOptions( boolean name ) {

        String[][] parameters = getOrderByOptions();
        return getParameterOptions( parameters, name );
    }

    public static final String getOrderById( String name ) {

        String[][] parameters = getOrderByOptions();
        return getParameterId( parameters, name );
    }

    public static final String getOrderByName( String id ) {

        String[][] parameters = getOrderByOptions();
        return getParameterName( parameters, id );
    }

    /*
     * 
     * FEED
     * 
     * 
     */
    private static final String[][] getFeeds() {

        String[][] result = new String[10][];
        result[0] = Settings.FEED_NONE;
        result[1] = Settings.FEED_TOP_RATED;
        result[2] = Settings.FEED_TOP_FAVORITES;
        result[3] = Settings.FEED_MOST_VIEWED;
        result[4] = Settings.FEED_MOST_POPULAR;
        result[5] = Settings.FEED_MOST_RECENT;
        result[6] = Settings.FEED_MOST_DISCUSSED;
        result[7] = Settings.FEED_MOST_LINKED;
        result[8] = Settings.FEED_MOST_RESPONDED;
        result[9] = Settings.FEED_RECENTLY_FEATURED;
        return result;
    }

    public static final String[] getFeedOptions( boolean name ) {

        String[][] parameters = getFeeds();
        return getParameterOptions( parameters, name );
    }

    public static final String getFeedId( String name ) {

        String[][] parameters = getFeeds();
        return getParameterId( parameters, name );
    }

    public static final String getFeedName( String id ) {

        String[][] parameters = getFeeds();
        return getParameterName( parameters, id );
    }
}
