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
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;

public class Applications {

    public static void testPermissions() {

        ApplicationPermissionsManager applicationPermissionsManager = ApplicationPermissionsManager.getInstance();
        ApplicationPermissions applicationPermissions = applicationPermissionsManager.getApplicationPermissions();
        ApplicationPermissions requestedPermissions = new ApplicationPermissions();

        int permissionWiFi = ApplicationPermissions.PERMISSION_WIFI;
        if ( !applicationPermissions.containsPermissionKey( permissionWiFi ) ) {

            requestedPermissions.addPermission( permissionWiFi );
        }

        int permissionCodeModuleManagement = ApplicationPermissions.PERMISSION_CODE_MODULE_MANAGEMENT;
        if ( !applicationPermissions.containsPermissionKey( permissionCodeModuleManagement ) ) {

            requestedPermissions.addPermission( permissionCodeModuleManagement );
        }

        int permissionExternalConnections = ApplicationPermissions.PERMISSION_EXTERNAL_CONNECTIONS;
        if ( !applicationPermissions.containsPermissionKey( permissionExternalConnections ) ) {

            requestedPermissions.addPermission( permissionExternalConnections );
        }

        int permissionPhone = ApplicationPermissions.PERMISSION_PHONE;
        if ( !applicationPermissions.containsPermissionKey( permissionPhone ) ) {

            requestedPermissions.addPermission( permissionPhone );
        }

        int permissionEmail = ApplicationPermissions.PERMISSION_EMAIL;
        if ( !applicationPermissions.containsPermissionKey( permissionEmail ) ) {

            requestedPermissions.addPermission( permissionEmail );
        }

        int[] permissionKeys = requestedPermissions.getPermissionKeys();
        if ( permissionKeys.length > 0 ) {

            applicationPermissionsManager.invokePermissionsRequest( requestedPermissions );
        }
    }
}
