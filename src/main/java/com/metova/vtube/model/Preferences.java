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
package com.metova.vtube.model;

import org.metova.mobile.rt.persistence.Persistable;

import com.metova.vtube.Settings;
import com.metova.vtube.service.feed.Feeds;

public class Preferences implements Persistable {

    private String formatType = Settings.FORMAT_TYPE_6[1];
    private String resultsPerPage = Feeds.DEFAULT_RESULTS_PER_PAGE;
    private String orderBy = Settings.ORDER_BY_RELEVANCE[1];
    private String defaultFeed = Settings.FEED_RECENTLY_FEATURED[1];

    private boolean acceptedEula;
    private boolean acceptedTrial;
    private boolean animatedScreen = true;
    private boolean reducedFormat = true;

    private int volume = 25;

    private long applicationLifetime;

    public String getFormatType() {

        return formatType;
    }

    public void setFormatType( String formatType ) {

        this.formatType = formatType;
    }

    public String getResultsPerPage() {

        return resultsPerPage;
    }

    public void setResultsPerPage( String resultsPerPage ) {

        this.resultsPerPage = resultsPerPage;
    }

    public String getOrderBy() {

        return orderBy;
    }

    public void setOrderBy( String orderBy ) {

        this.orderBy = orderBy;
    }

    public String getDefaultFeed() {

        return defaultFeed;
    }

    public void setDefaultFeed( String defaultFeed ) {

        this.defaultFeed = defaultFeed;
    }

    public boolean hasAcceptedEula() {

        return acceptedEula;
    }

    public void setAcceptedEula( boolean acceptedEula ) {

        this.acceptedEula = acceptedEula;
    }

    public long getApplicationLifetime() {

        return applicationLifetime;
    }

    public void setApplicationLifetime( long applicationLifetime ) {

        this.applicationLifetime = applicationLifetime;
    }

    public int getVolume() {

        return volume;
    }

    public void setVolume( int volume ) {

        this.volume = volume;
    }

    public boolean hasAcceptedTrial() {

        return acceptedTrial;
    }

    public void setAcceptedTrial( boolean acceptedTrial ) {

        this.acceptedTrial = acceptedTrial;
    }

    public boolean useAnimatedScreen() {

        return animatedScreen;
    }

    public void setAnimatedScreen( boolean animatedScreen ) {

        this.animatedScreen = animatedScreen;
    }

    public boolean useReducedFormat() {

        return reducedFormat;
    }

    public void setReducedFormat( boolean reducedFormat ) {

        this.reducedFormat = reducedFormat;
    }
}
