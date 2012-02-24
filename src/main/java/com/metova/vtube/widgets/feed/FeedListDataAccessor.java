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

import java.util.Vector;

import net.rim.device.api.util.Arrays;

import org.metova.mobile.util.accessor.ListDataAccessor;

import com.metova.vtube.Settings;
import com.metova.vtube.service.Parameters;

public class FeedListDataAccessor implements ListDataAccessor {

    private Vector delegate;

    public FeedListDataAccessor() {

        delegate = new Vector();

        String[] feedOptions = Parameters.getFeedOptions( true );
        Arrays.remove( feedOptions, Settings.FEED_NONE[0] );

        for (int i = 0; i < feedOptions.length; i++) {

            delegate.addElement( feedOptions[i] );
        }
    }

    public Object get( int index ) {

        return delegate.elementAt( index );
    }

    public int size() {

        return delegate.size();
    }
}
