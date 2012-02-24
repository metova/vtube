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

import net.rim.device.api.applicationcontrol.ApplicationPermissions;

import org.metova.bb.util.security.PermissionsPlatform;

public class PermissionsManager extends org.metova.bb.util.security.PermissionsManager {

    private static final int[] PERMISSIONS = new int[] {
            PermissionsPlatform.PERMISSION_WIFI,
            PermissionsPlatform.PERMISSION_EXTERNAL_CONNECTIONS,
            PermissionsPlatform.PERMISSION_PHONE,
            PermissionsPlatform.PERMISSION_CHANGE_DEVICE_SETTINGS,
            ApplicationPermissions.PERMISSION_EMAIL,
            ApplicationPermissions.PERMISSION_IDLE_TIMER,
            ApplicationPermissions.PERMISSION_CODE_MODULE_MANAGEMENT };

    protected int[] getDesiredPermissions() {

        return PERMISSIONS;
    }

    /**
     * Automatic permission request for supported devices. Prompt on unsupported devices.
     * This is a modal screen and will block until the screen exits. 
     */
    public boolean performPermissionsRequest() {

        boolean success = false;

        if ( canRequestPermissions() ) {

            success = super.requestPermissions( getDesiredPermissions() );
        }

        return success;
    }

}
