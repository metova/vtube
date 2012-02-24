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

public class UID extends org.metova.mobile.persistence.UID {

    public static final UID QUEUE_MANAGER_ID = new UID( ".queueManager" );

    public static final UID MEDIA_PLAYER_ERROR_CONFIRMATION = new UID( ".confirmation.mediaPlayerError" );

    public static final UID PREFERENCE_STORE = new UID( ".store.preference" );
    public static final UID SEARCH_HISTORY_STORE = new UID( ".store.searchHistory" );

    protected UID(String description) {

        super( getUniqueDescription( description ) );
    }

    private static final String getUniqueDescription( String description ) {

        return UID.class.getName() + description;
    }

    public static org.metova.mobile.persistence.UID getSafeUID( String description ) {

        org.metova.mobile.persistence.UID uid = UID.getUid( getUniqueDescription( description ) );
        if ( uid == null ) {

            uid = new UID( description );
        }

        return uid;
    }
}
