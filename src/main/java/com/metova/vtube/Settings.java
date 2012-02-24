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
package com.metova.vtube;

public class Settings {

    public static final String API = "gdata.youtube.com/feeds/api";
    public static final String API_SEARCH = "videos";
    public static final String API_FEEDS = "standardfeeds";

    public static final String CLIENT_ID = #redacted;
    public static final String DEVELOPER_KEY = #redacted;

    public static final String[] FORMAT_TYPE_1 = new String[] { "Low :: H.263 Video and AMR Audio", "1" };
    public static final String[] FORMAT_TYPE_6 = new String[] { "Medium :: MPEG-4 SP Video and AAC Audio", "6" };

    public static final String[] ENABLE_ON = new String[] { "Enabled", "true" };
    public static final String[] ENABLE_OFF = new String[] { "Disabled", "false" };

    public static final String[] ORDER_BY_RELEVANCE = new String[] { "Relevance", "relevance" };
    public static final String[] ORDER_BY_PUBLISHED = new String[] { "Published", "published" };
    public static final String[] ORDER_BY_VIEW_COUNT = new String[] { "View Count", "viewCount" };
    public static final String[] ORDER_BY_RATING = new String[] { "Rating", "rating" };

    public static final String[] FEED_NONE = new String[] { "None", null };
    public static final String[] FEED_TOP_RATED = new String[] { "Top Rated", "top_rated" };
    public static final String[] FEED_TOP_FAVORITES = new String[] { "Top Favorites", "top_favorites" };
    public static final String[] FEED_MOST_VIEWED = new String[] { "Most Viewed", "most_viewed" };
    public static final String[] FEED_MOST_POPULAR = new String[] { "Most Popular", "most_popular" };
    public static final String[] FEED_MOST_RECENT = new String[] { "Most Recent", "most_recent" };
    public static final String[] FEED_MOST_DISCUSSED = new String[] { "Most Discussed", "most_discussed" };
    public static final String[] FEED_MOST_LINKED = new String[] { "Most Linked", "most_linked" };
    public static final String[] FEED_MOST_RESPONDED = new String[] { "Most Responded", "most_responded" };
    public static final String[] FEED_RECENTLY_FEATURED = new String[] { "Recently Featured", "recently_featured" };
    //    public static final String[] FEED_WATCH_ON_MOBILE = new String[] { "Videos for Mobile Phones", "watch_on_mobile" };

    public static final String METOVA_FAQ_PAGE = "http://metova.com/display/DOCVISIONYT/FAQ?mobile=true";

    public static final String OPTION_DEFAULT_FEED = "Default Feed";
    public static final String OPTION_FORMAT_TYPE = "Preferred Format";
    public static final String OPTION_REDUCE_FORMAT = "Network Aware";
    public static final String OPTION_RESULTS_PER_PAGE = "Results Per Page";
    public static final String OPTION_ORDER_BY = "Order By";
    public static final String OPTION_SCREEN_TRANSITIONS = "Screen Transitions";

    public static final String PARAMETER_QUERY = "q";
    public static final String PARAMETER_ORDER_BY = "orderby";

    public static final int MINIMUM_START_INDEX = 1;

    public static final long FADE_SPEED = 25;
    public static final long CAROUSEL_SPEED = 10;
    public static final int CAROUSEL_FRAMES = 10;
    public static final int CAROUSEL_DESCRIPTION_DELAY = 125;

    public static final String THIRD_PARTY_PARAMETER_QUERY = PARAMETER_QUERY + "=";
}
