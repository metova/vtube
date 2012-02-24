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

import m.java.util.HashMap;
import m.java.util.Map;

import org.metova.bb.widgets.ManagedUiApplication;
import org.metova.mobile.net.HttpPostRequest;
import org.metova.mobile.net.HttpResponse;
import org.metova.mobile.net.direct.ServiceEntry;
import org.metova.mobile.net.queue.QueueManager;
import org.metova.mobile.util.net.HttpGzipResponse;
import org.metova.mobile.util.text.Text;

import com.metova.bb.provisioning.service.license.provisioned.LicenseService;
import com.metova.bb.provisioning.service.license.provisioned.response.DirectLicenseCheckCallback;
import com.metova.vtube.Settings;
import com.metova.vtube.model.Preferences;
import com.metova.vtube.service.feed.FeedCallback;
import com.metova.vtube.service.feed.FeedServiceEntry;
import com.metova.vtube.service.persistence.PreferenceStore;

public class WebServices extends org.metova.mobile.net.direct.WebServices {

    private static WebServices myself;

    public static WebServices instance() {

        if ( myself == null ) {

            myself = new WebServices();
        }

        return myself;
    }

    public static final String urlEncode( String text ) {

        return Text.replaceAll( text, " ", "+" );
    }

    public static final String urlDecode( String text ) {

        return Text.replaceAll( text, "+", " " );
    }

    public static final String getServiceUrl( String service ) {

        return "http://" + Settings.API + "/" + service;
    }

    public FeedServiceEntry getFeedEntry( String url, Map parameters ) {

        return (FeedServiceEntry) createGetServiceEntry( FeedServiceEntry.class, url, parameters, FeedCallback.class );
    }

    public void requestLicenseUpdate() {

        HttpPostRequest httpPostRequest = LicenseService.instance().getLicenseUpdateRequest();
        handleServiceEntry( ServiceEntry.class, httpPostRequest, null, DirectLicenseCheckCallback.class );
    }

    public static Map getParameters( int startIndex ) {

        Preferences preferences = PreferenceStore.instance().load();

        Map parameters = new HashMap();
        parameters.put( "strict", "true" );
        parameters.put( "alt", "json" );
        parameters.put( "format", preferences.getFormatType() );
        parameters.put( "max-results", preferences.getResultsPerPage() );
        parameters.put( "start-index", String.valueOf( startIndex ) );
        parameters.put( "client", Settings.CLIENT_ID );
        parameters.put( "key", Settings.DEVELOPER_KEY );
        return parameters;
    }

    protected HttpResponse handleResponse( HttpResponse httpResponse ) {

        httpResponse = super.handleResponse( httpResponse );
        return new HttpGzipResponse( httpResponse );
    }

    public QueueManager getQueueManager() {

        return ManagedUiApplication.getManagedUiApplication().getQueueManager();
    }
}
