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
package com.metova.vtube.widgets.sidebar;

import java.io.IOException;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.bb.widgets.util.GraphicsUtility;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;
import org.metova.mobile.rt.graphics.MobileImages;

import com.metova.vtube.event.FeedLoadedEvent;
import com.metova.vtube.widgets.page.PageDisplay;

public class SidebarField extends org.metova.bb.widgets.screen.sidebar.SidebarField {

    private static final Logger log = Logger.getLogger( SidebarField.class );

    public static final int COLLAPSED_PADDING_HEIGHT = 11;
    public static final int COLLAPSED_HEIGHT = 15 + COLLAPSED_PADDING_HEIGHT;

    private SidebarContentManager sidebarContentManager;
    private MainContentManager mainContentManager;

    public SidebarField() {

        super( POSITION_TOP, COLLAPSED_HEIGHT );

        addEventListener( new FeedLoadedEventListener(), FeedLoadedEvent.class );
    }

    protected void initializeSidebarManager() {

        setSidebarManager( new MySidebarManager() );
    }

    protected void initializeMainFields() {

        setMainContentManager( new MainContentManager() );
    }

    protected void initializeSidebarFields() {

        setSidebarContentManager( new SidebarContentManager() );
    }

    protected void addMainFields() {

        MainManager mainManager = getMainManager();
        mainManager.add( getMainContentManager() );
    }

    protected void addSidebarFields() {

        SidebarManager sidebarManager = getSidebarManager();
        sidebarManager.add( getSidebarContentManager() );
    }

    private class FeedLoadedEventListener implements EventListener {

        public void onEvent( Event event ) {

            FeedLoadedEvent feedLoadedEvent = (FeedLoadedEvent) event;

            // keep the sidebar disabled until everything is loaded and the initial focus
            // has been set. this will prevent automatically opening while loading
            setSidebarEnabled( false );

            synchronized (UiApplication.getEventLock()) {

                MainContentManager mainContentManager = getMainContentManager();
                PageDisplay pageDisplay = mainContentManager.getPageDisplay();
                if ( feedLoadedEvent.doReload() || !pageDisplay.refresh() ) {

                    reload();
                }
            }

            setSidebarEnabled( true );
        }
    }

    private class MySidebarManager extends SidebarManager {

        private Bitmap upArrow;
        private Bitmap upArrowHighlight;

        private Bitmap downArrow;
        private Bitmap downArrowHighlight;

        protected int moveFocus( int amount, int status, int time ) {

            int result = super.moveFocus( amount, status, time );

            invalidate();

            return result;
        }

        protected void paintBorder( Graphics graphics, int width, int height ) {

            ComputedStyle computedStyle = getStyleManager().getComputedStyle();
            int arcHeight = computedStyle.getBorderArcHeight();

            // draw border if configured
            GraphicsUtility.paintBorder( graphics, 0, -arcHeight, width, height + arcHeight, isFocus(), computedStyle );
        }

        protected void paintBackground( Graphics graphics, int width, int height ) {

            ComputedStyle computedStyle = getStyleManager().getComputedStyle();
            if ( computedStyle.getBackground() > 0 ) {

                int arcHeight = computedStyle.getBackgroundArcHeight();

                // style it with our attributes, but with the global Mask
                GraphicsUtility.fillRoundRect( graphics, 0, -arcHeight, width, height + arcHeight, computedStyle );
            }
        }

        protected void paint( Graphics graphics ) {

            int width = getWidth();
            int height = getHeight();

            boolean collapsed = isCollapsed();

            paintBackground( graphics, width, height );

            Bitmap arrow;

            if ( collapsed ) {

                arrow = getUpArrow();
            }
            else {

                super.paint( graphics );

                if ( isCollapsing() ) {

                    arrow = getUpArrowHighlight();
                }
                else if ( isExpanding() ) {

                    arrow = getDownArrowHighlight();
                }
                else {

                    arrow = getDownArrow();
                }
            }

            int arrowWidth = arrow.getWidth();
            int arrowHeight = arrow.getHeight();
            int arrowY = height - arrowHeight - 1;

            int rightX = (int) ( width * 0.50 );
            graphics.drawBitmap( rightX, arrowY, arrowWidth, arrowHeight, arrow, 0, 0 );

            int leftX = rightX - arrowWidth;
            graphics.drawBitmap( leftX, arrowY, arrowWidth, arrowHeight, arrow, 0, 0 );

            paintBorder( graphics, width, height );
        }

        private Bitmap getUpArrow() {

            if ( upArrow == null ) {

                String imagePath = getStyleManager().getImagePath( "arrow-up" );

                try {

                    setUpArrow( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
                }
                catch (IOException ex) {

                    log.error( "Unable to load bitmap: " + imagePath );
                }
            }

            return upArrow;
        }

        private void setUpArrow( Bitmap upArrow ) {

            this.upArrow = upArrow;
        }

        private Bitmap getUpArrowHighlight() {

            if ( upArrowHighlight == null ) {

                String imagePath = getStyleManager().getImagePath( "arrow-up-highlight" );

                try {

                    setUpArrowHighlight( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
                }
                catch (IOException ex) {

                    log.error( "Unable to load bitmap: " + imagePath );
                }
            }

            return upArrowHighlight;
        }

        private void setUpArrowHighlight( Bitmap upArrowHighlight ) {

            this.upArrowHighlight = upArrowHighlight;
        }

        private Bitmap getDownArrow() {

            if ( downArrow == null ) {

                String imagePath = getStyleManager().getImagePath( "arrow-down" );

                try {

                    setDownArrow( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
                }
                catch (IOException ex) {

                    log.error( "Unable to load bitmap: " + imagePath );
                }
            }

            return downArrow;
        }

        private void setDownArrow( Bitmap downArrow ) {

            this.downArrow = downArrow;
        }

        private Bitmap getDownArrowHighlight() {

            if ( downArrowHighlight == null ) {

                String imagePath = getStyleManager().getImagePath( "arrow-down-highlight" );

                try {

                    setDownArrowHighlight( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
                }
                catch (IOException ex) {

                    log.error( "Unable to load bitmap: " + imagePath );
                }
            }

            return downArrowHighlight;
        }

        private void setDownArrowHighlight( Bitmap downArrowHighlight ) {

            this.downArrowHighlight = downArrowHighlight;
        }
    }

    private SidebarContentManager getSidebarContentManager() {

        return sidebarContentManager;
    }

    private void setSidebarContentManager( SidebarContentManager sidebarContentManager ) {

        this.sidebarContentManager = sidebarContentManager;
    }

    private MainContentManager getMainContentManager() {

        return mainContentManager;
    }

    private void setMainContentManager( MainContentManager mainContentManager ) {

        this.mainContentManager = mainContentManager;
    }
}
