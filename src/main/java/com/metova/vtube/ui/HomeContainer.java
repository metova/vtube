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
package com.metova.vtube.ui;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.widgets.ManagedUiApplication;
import org.metova.bb.widgets.managed.LoadingState;
import org.metova.bb.widgets.menu.Menus;
import org.metova.bb.widgets.screen.sidebar.CloseSidebarEvent;
import org.metova.bb.widgets.screen.sidebar.OpenSidebarEvent;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;
import org.metova.mobile.util.text.Text;
import org.metova.mobile.util.timer.Timers;

import com.metova.bb.provisioning.service.update.MetovaUpdateService;
import com.metova.vtube.Application;
import com.metova.vtube.event.FeedLoadedEvent;
import com.metova.vtube.model.Preferences;
import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.service.Parameters;
import com.metova.vtube.service.feed.FeedStack;
import com.metova.vtube.service.feed.Feeds;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.feed.FeedPopupScreen;
import com.metova.vtube.widgets.page.next.NextPageAction;
import com.metova.vtube.widgets.page.previous.PreviousPageAction;
import com.metova.vtube.widgets.sidebar.SidebarField;

public class HomeContainer extends AbstractLicensedContainer {

    private SidebarField sidebarField;

    private boolean loadedDefault;

    public HomeContainer() {

        // http://jira.metova.com/browse/TUBE-1527
        //        Application application = (Application) ManagedUiApplication.getManagedUiApplication();
        //        if ( Text.isNull( application.getThirdPartyQuery() ) ) {
        //
        //            setBackgroundOnClose( true );
        //        }

        addEventListener( new FeedLoadedEventListener(), FeedLoadedEvent.class );
    }

    protected void onInitializeContent() {

        SidebarField sidebarField = new SidebarField();
        sidebarField.preinitialize();
        setSidebarField( sidebarField );
    }

    protected void addContentFields() {

        add( getSidebarField() );
    }

    protected synchronized void onVisibilityChange( boolean visible ) {

        super.onVisibilityChange( visible );

        if ( visible ) {

            // keep the title updated
            updateTitle( FeedStack.getTitle() );

            LoadingState loadingState = getLoadingState();
            if ( !isLoadedDefault() && loadingState.isLoaded() ) {

                setLoadedDefault( true );
                Timers.runNow( new LoadDefaultFeedAction() );
            }
        }
    }

    protected void makeMenu( Menu menu, int instance ) {

        boolean search = FeedStack.isSearch();
        Feed currentPage = FeedStack.currentPage();

        if ( FeedStack.doesNextPageExist() ) {

            menu.add( Menus.newMenuItem( "Next Page", new NextPageAction(), Menus.ORDINAL_L2, 1 ) );
        }

        if ( FeedStack.isPreviousPageAvailable() ) {

            menu.add( Menus.newMenuItem( "Previous Page", new PreviousPageAction(), Menus.ORDINAL_L2, 1 ) );
        }

        if ( !search && currentPage != null ) {

            menu.add( Menus.newMenuItem( "Reload Feed", new ThreadPoolDelegatingRunnable() {

                public void onRun() {

                    FeedStack.reloadFeed();
                }
            }, Menus.ORDINAL_L2, 1 ) );
        }

        menu.add( Menus.newMenuItemPushScreen( "Select Feed", new FeedPopupScreen(), Menus.ORDINAL_L2, 1 ) );

        SidebarField sidebarField = getSidebarField();
        if ( !sidebarField.isCollapsing() && !sidebarField.isExpanding() ) {

            if ( sidebarField.isCollapsed() ) {

                menu.add( Menus.newMenuItemFireEvent( "Open Search", new OpenSidebarEvent(), Menus.ORDINAL_L2, 1 ) );
            }
            else {

                menu.add( Menus.newMenuItemFireEvent( "Close Search", new CloseSidebarEvent(), Menus.ORDINAL_L2, 1 ) );
            }
        }

        menu.add( Menus.newMenuItemPushScreen( "FAQ", new FaqContainer(), Menus.ORDINAL_L7, 1 ) );

        menu.add( Menus.newMenuItemPushScreen( "Tips", new TipContainer( null ), Menus.ORDINAL_L7, 1 ) );

        menu.add( Menus.newMenuItemPushScreen( "Preferences", new PreferencesContainer(), Menus.ORDINAL_L7, 1 ) );

        if ( MetovaUpdateService.instance().hasUpdateAvailable() ) {

            menu.add( Menus.newMenuItem( "Update Vision", new InstallUpdateAction(), Menus.ORDINAL_L8, 1 ) );
        }

        super.makeMenu( menu, instance );
    }

    protected void makeL9Menu( Menu menu ) {

        super.makeL9Menu( menu );

        menu.add( Menus.newMenuItemPushScreen( "About", new AboutContainer(), Menus.ORDINAL_L9, 1 ) );
    }

    private class InstallUpdateAction implements Runnable {

        public void run() {

            MetovaUpdateService.instance().installUpdate();
        }
    }

    private class FeedLoadedEventListener implements EventListener {

        public void onEvent( final Event event ) {

            synchronized (UiApplication.getEventLock()) {

                updateTitle( FeedStack.getTitle() );
            }
        }
    }

    private class LoadDefaultFeedAction extends ThreadPoolDelegatingRunnable {

        public void onRun() {

            Preferences preferences = PreferenceStore.instance().load();

            Application application = (Application) ManagedUiApplication.getManagedUiApplication();
            String thirdPartyQuery = application.getThirdPartyQuery();
            if ( !Text.isNull( thirdPartyQuery ) ) {

                Feeds.search( thirdPartyQuery, Parameters.getOrderByName( preferences.getOrderBy() ) );
            }
            else {

                String defaultFeed = preferences.getDefaultFeed();
                if ( !Text.isNull( defaultFeed ) ) {

                    Feeds.loadFeed( Parameters.getFeedName( defaultFeed ) );
                }
            }
        }
    }

    private SidebarField getSidebarField() {

        return sidebarField;
    }

    private void setSidebarField( SidebarField sidebarField ) {

        this.sidebarField = sidebarField;
    }

    private boolean isLoadedDefault() {

        return loadedDefault;
    }

    private void setLoadedDefault( boolean loadedDefault ) {

        this.loadedDefault = loadedDefault;
    }
}
