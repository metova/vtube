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
package com.metova.vtube.service.provisioning;

import org.metova.mobile.rt.persistence.Persistable;
import org.metova.mobile.rt.system.MobileApplication;

import com.metova.bb.provisioning.ProvisionedUiApplication;
import com.metova.bb.provisioning.model.license.LicenseServiceConfiguration;
import com.metova.bb.provisioning.model.license.LicenseType;

public class VisionLicenseConfiguration extends LicenseServiceConfiguration implements Persistable {

    public VisionLicenseConfiguration(boolean isAppWorldLicense) {

        setEnabled( true );
        setPurchaseUrl( "http://vision.metova.com/" );
        setLicenseType( LicenseType.LIMITED );
        setPerpetual( true );

        ProvisionedUiApplication provisionedUiApplication = ProvisionedUiApplication.getProvisionedUiApplication();
        String operationUrl = provisionedUiApplication.getOperationsUrl();

        if ( isAppWorldLicense ) {

            setUrl( operationUrl + "rs/provisioning/license/rim/" + MobileApplication.instance().getModuleName() + "/getLicense" );
        }
        else {

            setUrl( operationUrl + "rs/provisioning/license/" + MobileApplication.instance().getModuleName() + "/getLicense" );
        }
    }
}
