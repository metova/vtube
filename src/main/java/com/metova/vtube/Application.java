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

import java.io.InputStream;

import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.container.Container;
import org.metova.bb.widgets.screen.controller.DefaultScreenController;
import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.license.LicenseManager;
import org.metova.mobile.net.queue.QueueManager;
import org.metova.mobile.util.text.Text;

import com.metova.bb.provisioning.ProvisionedUiApplication;
import com.metova.bb.provisioning.license.AppWorldLicense;
import com.metova.bb.provisioning.model.ProvisioningConfiguration;
import com.metova.bb.provisioning.service.license.rim.RimLicenseManager;
import com.metova.bb.provisioning.service.persistence.ProvisioningConfigurationStore;
import com.metova.vtube.event.ThumbnailLoadedEvent;
import com.metova.vtube.model.Preferences;
import com.metova.vtube.model.RuntimePreferences;
import com.metova.vtube.service.PermissionsManager;
import com.metova.vtube.service.UID;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.service.persistence.RuntimePreferenceStore;
import com.metova.vtube.service.provisioning.VisionLicenseConfiguration;
import com.metova.vtube.service.provisioning.VisionUpdateConfiguration;
import com.metova.vtube.service.video.thumbnail.Thumbnails.ThumbnailQueueListener;
import com.metova.vtube.ui.EulaContainer;
import com.metova.vtube.ui.HomeContainer;
import com.metova.vtube.widgets.AnimatedScreenController;
import com.metova.vtube.widgets.splash.SplashContainer;

public class Application extends ProvisionedUiApplication implements AppWorldLicense {

    private static boolean IS_APPWORLD_LICENSE;

    private QueueManager queueManager;

    private LicenseManager licenseManager;

    private ThumbnailQueueListener thumbnailQueueListener;

    private String thirdPartyQuery;

    public static void main( String[] args ) {

        run( Application.class, args );
    }

    public Application() {

        resetScreenController();
    }

    public static void resetScreenController() {

        Preferences preferences = PreferenceStore.instance().load();
        if ( preferences.useAnimatedScreen() ) {

            Screens.setScreenController( new AnimatedScreenController() );
        }
        else {

            Screens.setScreenController( new DefaultScreenController() );
        }
    }

    protected void handleArguments( String[] args ) {

        super.handleArguments( args );

        if ( args.length > 0 ) {

            for (int i = 0; i < args.length; i++) {

                String argument = args[i];

                if ( Text.startsWith( argument, Settings.THIRD_PARTY_PARAMETER_QUERY ) ) {

                    setThirdPartyQuery( Text.substringAfter( argument, Settings.THIRD_PARTY_PARAMETER_QUERY ) );
                }
            }
        }
    }

    protected void lifecycleInitialize() {

        PermissionsManager permissionsManager = new PermissionsManager();
        if ( permissionsManager.shouldRequestPermissions() ) {

            permissionsManager.performPermissionsRequest();
        }

        super.lifecycleInitialize();

        setQueueManager( new QueueManager( Application.class.getName(), UID.QUEUE_MANAGER_ID ) );

        setLicenseManager( new RimLicenseManager( #redacted ) );

        setThumbnailQueueListener( new ThumbnailQueueListener() );
    }

    protected final void initializeProvisioningConfiguration() {

        ProvisioningConfiguration provisioningConfiguration = ProvisioningConfigurationStore.instance().load();
        provisioningConfiguration.setLicenseServiceConfiguration( new VisionLicenseConfiguration( IS_APPWORLD_LICENSE ) );
        provisioningConfiguration.setUpdateServiceConfiguration( new VisionUpdateConfiguration() );
        ProvisioningConfigurationStore.instance().commit();
    }

    protected void lifecycleSetup() {

        EventDispatcher.instance().registerListener( getThumbnailQueueListener(), ThumbnailLoadedEvent.class );
    }

    protected void lifecycleStartup() {

        // do nothing
    }

    protected void lifecycleShow() {

        super.lifecycleShow();

        Preferences preferences = PreferenceStore.instance().load();
        boolean acceptedEula = preferences.hasAcceptedEula();

        RuntimePreferences runtimePreferences = RuntimePreferenceStore.instance().load();
        boolean seenSplash = runtimePreferences.hasSeenSplash();

        Container homeContainer = seenSplash ? (Container) new HomeContainer() : new SplashContainer();
        Container eulaContainer = acceptedEula ? (Container) homeContainer : new EulaContainer( homeContainer );
        Screens.pushScreen( eulaContainer );
    }

    public InputStream getResourceAsStream( String resource ) {

        return getClass().getResourceAsStream( resource );
    }

    public void onExit() {

        EventDispatcher.instance().removeListener( getThumbnailQueueListener(), ThumbnailLoadedEvent.class );
    }

    public void reset() {

        getQueueManager().deleteAll();

        super.reset();
    }

    protected void initializeThemeManager() throws Exception {

        super.initializeThemeManager();

        getThemeManager().addThemeDescriptor( "vtube", true );
    }

    public LicenseManager getLicenseManager() {

        return licenseManager;
    }

    private void setLicenseManager( LicenseManager licenseManager ) {

        this.licenseManager = licenseManager;
    }

    public QueueManager getQueueManager() {

        return queueManager;
    }

    private void setQueueManager( QueueManager queueManager ) {

        this.queueManager = queueManager;
    }

    public String getThirdPartyQuery() {

        return thirdPartyQuery;
    }

    private void setThirdPartyQuery( String thirdPartyQuery ) {

        this.thirdPartyQuery = thirdPartyQuery;
    }

    private ThumbnailQueueListener getThumbnailQueueListener() {

        return thumbnailQueueListener;
    }

    private void setThumbnailQueueListener( ThumbnailQueueListener thumbnailQueueListener ) {

        this.thumbnailQueueListener = thumbnailQueueListener;
    }
}
