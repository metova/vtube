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
package com.metova.vtube.service.persistence;

import org.metova.mobile.persistence.AbstractStore;

import com.metova.vtube.model.Preferences;
import com.metova.vtube.service.UID;

public class PreferenceStore extends AbstractStore {

    private static PreferenceStore myself;

    public static PreferenceStore instance() {

        if ( myself == null ) {

            synchronized (PreferenceStore.class) {

                if ( myself == null ) {

                    myself = new PreferenceStore();
                }
            }
        }

        return myself;
    }

    private PreferenceStore() {

        super( Preferences.class, UID.PREFERENCE_STORE );
    }

    public Preferences load() {

        return (Preferences) super.loadObject();
    }

    public void persist( Preferences preferences ) {

        super.persistObject( preferences );
    }
}
