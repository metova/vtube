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
package com.metova.vtube.widgets;

import m.org.apache.log4j.Logger;
import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.theme.precision.container.Container;
import org.metova.bb.widgets.ManagedUiApplication;
import org.metova.bb.widgets.menu.Menus;
import org.metova.mobile.rt.system.MobileNetwork;

import com.metova.vtube.Settings;
import com.metova.vtube.widgets.manager.IndicatorHeaderManager;
import com.metova.vtube.widgets.popup.TellAFriendPopupScreen;

public abstract class AbstractContainer extends Container {

    private static final Logger log = Logger.getLogger( AbstractContainer.class );

    private IndicatorHeaderManager indicatorHeaderManager;

    protected AbstractContainer() {

        setDisplayDefaultMenu( false );

        setContentManagerType( CONTENT_MANAGER_VERTICAL );
        setContentManagerStyle( USE_ALL_HEIGHT | NO_VERTICAL_SCROLL );
    }

    protected void onInitializeHeader() {

        super.onInitializeHeader();

        String title = getTitle();
        setIndicatorHeaderManager( new MyIndicatorHeaderManager( title ) );
    }

    protected void addHeaderFields() {

        super.addHeaderFields();

        Manager headerManager = getHeaderManager();
        headerManager.add( getIndicatorHeaderManager() );
    }

    protected void onInitializeHeaderManager() {

        try {

            setHeaderManager( new VerticalFieldManager() );
        }
        catch (Throwable t) {

            log.error( "onInitializeHeaderManager()", t );
            throw new RuntimeException( t.getMessage() );
        }
    }

    protected void updateTitle( String title ) {

        IndicatorHeaderManager indicatorHeaderManager = getIndicatorHeaderManager();
        indicatorHeaderManager.updateTitle( title );
    }

    protected boolean displayWiFiLogo() {

        return MobileNetwork.instance().isWiFiActive();
    }

    protected void makeL9Menu( Menu menu ) {

        // do nothing
    }

    protected void makeMenu( Menu menu, int instance ) {

        menu.add( Menus.newMenuItem( "Help", new DisplayFaqPage(), Menus.ORDINAL_L9, 1 ) );

        makeL9Menu( menu );

        menu.add( Menus.newMenuItemPushScreen( "Tell A Friend", TellAFriendPopupScreen.class, Menus.ORDINAL_L9, 1 ) );

        // Submit Feedback removed per http://jira.metova.com/browse/TUBE-1467
        //        menu.add( Menus.newMenuItemPushScreen( "Submit Feedback", ProblemReportPopupScreen.class, Menus.ORDINAL_L9, 1 ) );

        menu.add( Menus.newMenuItem( "Shutdown Vision", new Runnable() {

            public void run() {

                ManagedUiApplication managedUiApplication = ManagedUiApplication.getManagedUiApplication();
                managedUiApplication.exit();

                System.exit( 0 );
            }
        }, Menus.ORDINAL_L9, 1 ) );
    }

    private class DisplayFaqPage extends ThreadPoolDelegatingRunnable {

        public void onRun() {

            BrowserSession browserSession = Browser.getDefaultSession();
            browserSession.displayPage( Settings.METOVA_FAQ_PAGE );
        }
    }

    private class MyIndicatorHeaderManager extends IndicatorHeaderManager {

        public MyIndicatorHeaderManager(String title) {

            super( title );
        }

        public int getPreferredHeight() {

            // return the header height with any image in the background
            // the PrecisionHeaderManager only maintains height for the indicators at the top
            return calculateHeaderHeightWithImage( super.getPreferredHeight() );
        }

        protected boolean displayWiFiLogo() {

            return AbstractContainer.this.displayWiFiLogo();
        }
    }

    private IndicatorHeaderManager getIndicatorHeaderManager() {

        return indicatorHeaderManager;
    }

    private void setIndicatorHeaderManager( IndicatorHeaderManager indicatorHeaderManager ) {

        this.indicatorHeaderManager = indicatorHeaderManager;
    }
}
