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

import org.metova.mobile.rt.persistence.MobilePersistence;

import com.metova.vtube.model.RuntimePreferences;

public class RuntimePreferenceStore {

    private static RuntimePreferenceStore myself;

    public static RuntimePreferenceStore instance() {

        if ( myself == null ) {

            synchronized (RuntimePreferenceStore.class) {

                if ( myself == null ) {

                    myself = new RuntimePreferenceStore();
                }
            }
        }

        return myself;
    }

    private RuntimePreferences runtimePreferences;

    private static long getGuid() {

        return MobilePersistence.instance().getModuleGuid( RuntimePreferences.class.getName() );
    }

    public RuntimePreferences load() {

        if ( runtimePreferences == null ) {

            synchronized (RuntimePreferenceStore.class) {

                runtimePreferences = (RuntimePreferences) MobilePersistence.instance().getRuntimeObject( getGuid() );
                if ( runtimePreferences == null ) {

                    put( new RuntimePreferences() );
                }
            }
        }

        return runtimePreferences;
    }

    public void put( RuntimePreferences runtimePreferences ) {

        MobilePersistence.instance().putRuntimeObject( getGuid(), runtimePreferences );
        this.runtimePreferences = runtimePreferences;
    }
}
