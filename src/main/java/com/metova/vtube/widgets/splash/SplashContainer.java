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
package com.metova.vtube.widgets.splash;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.field.image.AnimatedField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.bb.widgets.screen.splash.SplashInitializer;
import org.metova.mobile.rt.graphics.MobileImages;
import org.metova.mobile.util.coordinate.Edges;

import com.metova.vtube.model.RuntimePreferences;
import com.metova.vtube.service.persistence.RuntimePreferenceStore;
import com.metova.vtube.ui.HomeContainer;

public class SplashContainer extends org.metova.bb.widgets.screen.splash.SplashContainer {

    private static final Logger log = Logger.getLogger( SplashContainer.class );

    private Bitmap splashBitmap;
    private Bitmap metovaBitmap;

    private AnimatedManager animatedManager;

    public SplashContainer() {

        super( MySplashInitializer.class );
    }

    protected void onInitializeContent() {

        super.onInitializeContent();

        setAnimatedManager( new AnimatedManager() );
    }

    protected void onInitializeFooter() {

        // do nothing
    }

    protected void onOrientationChange() {

        super.onOrientationChange();

        setSplashBitmap( null );
    }

    protected void addContentFields() {

        super.addContentFields();

        add( getAnimatedManager() );
    }

    protected long getMinimumDelay() {

        return 1500;
    }

    protected void paint( Graphics graphics, int width, int height ) {

        super.paint( graphics, width, height );

        width = getWidth();
        height = getHeight();

        Bitmap splashBitmap = getSplashBitmap();
        if ( splashBitmap != null ) {

            int bitmapWidth = splashBitmap.getWidth();
            int bitmapHeight = splashBitmap.getHeight();

            int x = (int) ( ( width - bitmapWidth ) * 0.50 );
            int y = (int) ( ( height - bitmapHeight ) * 0.50 );
            graphics.drawBitmap( x, y, bitmapWidth, bitmapHeight, splashBitmap, 0, 0 );
        }

        Bitmap metovaBitmap = getMetovaBitmap();
        if ( metovaBitmap != null ) {

            Edges padding = getStyleManager().getComputedStyle().getPadding();

            int bitmapWidth = metovaBitmap.getWidth();
            int bitmapHeight = metovaBitmap.getHeight();

            int x = width - bitmapWidth - padding.getRight();
            int y = height - padding.getBottom() + -bitmapHeight;
            graphics.drawBitmap( x, y, bitmapWidth, bitmapHeight, metovaBitmap, 0, 0 );
        }
    }

    private class AnimatedManager extends AbstractVerticalFieldManager {

        private AnimatedField leftAnimatedField;
        private AnimatedField rightAnimatedField;

        public AnimatedManager() {

            super( USE_ALL_WIDTH | USE_ALL_HEIGHT | NO_VERTICAL_SCROLL );
        }

        protected void onInitialize() {

            AnimatedField leftAnimatedField = new AnimatedField();
            leftAnimatedField.getStyleManager().setStyleClass( "AnimatedField-splash" );
            setLeftAnimatedField( leftAnimatedField );

            AnimatedField rightAnimatedField = new AnimatedField();
            rightAnimatedField.getStyleManager().setStyleClass( "AnimatedField-splash" );
            setRightAnimatedField( rightAnimatedField );
        }

        protected void onLoading() {

            add( getLeftAnimatedField() );
            add( getRightAnimatedField() );
        }

        protected void onSublayout( int width, int height ) {

            super.onSublayout( width, height );

            width = getWidth();
            height = getHeight();

            Bitmap splashBitmap = getSplashBitmap();
            if ( splashBitmap != null ) {

                int bitmapWidth = splashBitmap.getWidth();
                int bitmapHeight = splashBitmap.getHeight();

                int bitmapX = (int) ( ( width - bitmapWidth ) * 0.50 );
                int bitmapY = (int) ( ( height - bitmapHeight ) * 0.50 );

                AnimatedField leftAnimatedField = getLeftAnimatedField();
                if ( leftAnimatedField != null && equals( leftAnimatedField.getManager() ) ) {

                    int fieldWidth = leftAnimatedField.getWidth();
                    int fieldHeight = leftAnimatedField.getHeight();

                    int fieldX = bitmapX + (int) ( bitmapWidth * 0.275 ) - (int) ( fieldWidth * 0.50 );
                    int fieldY = bitmapY - fieldHeight;

                    setPositionChild( leftAnimatedField, fieldX, fieldY );
                }

                AnimatedField rightAnimatedField = getRightAnimatedField();
                if ( rightAnimatedField != null && equals( rightAnimatedField.getManager() ) ) {

                    int fieldWidth = rightAnimatedField.getWidth();
                    int fieldHeight = rightAnimatedField.getHeight();

                    int fieldX = bitmapX + (int) ( bitmapWidth * 0.530 ) - (int) ( fieldWidth * 0.50 );
                    int fieldY = bitmapY - fieldHeight;

                    setPositionChild( rightAnimatedField, fieldX, fieldY );
                }
            }
        }

        private AnimatedField getLeftAnimatedField() {

            return leftAnimatedField;
        }

        private void setLeftAnimatedField( AnimatedField leftAnimatedField ) {

            this.leftAnimatedField = leftAnimatedField;
        }

        private AnimatedField getRightAnimatedField() {

            return rightAnimatedField;
        }

        private void setRightAnimatedField( AnimatedField rightAnimatedField ) {

            this.rightAnimatedField = rightAnimatedField;
        }
    }

    public static class MySplashInitializer implements SplashInitializer {

        public void initialize( org.metova.bb.widgets.screen.splash.SplashContainer splashContainer ) {

            RuntimePreferences runtimePreferences = RuntimePreferenceStore.instance().load();
            runtimePreferences.setSeenSplash( true );

            HomeContainer homeContainer = new HomeContainer();
            homeContainer.preinitialize();

            splashContainer.initializeAndSetNext( homeContainer );
        }
    }

    private Bitmap getSplashBitmap() {

        if ( splashBitmap == null ) {

            String imagePath = getStyleManager().getImagePath( "splash" );

            try {

                setSplashBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (Throwable t) {

                log.error( "Unable to display image: " + imagePath, t );
            }
        }

        return splashBitmap;
    }

    private void setSplashBitmap( Bitmap splashBitmap ) {

        this.splashBitmap = splashBitmap;
    }

    private Bitmap getMetovaBitmap() {

        if ( metovaBitmap == null ) {

            String imagePath = getStyleManager().getImagePath( "metova-logo" );

            try {

                setMetovaBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (Throwable t) {

                log.error( "Unable to display image: " + imagePath, t );
            }
        }

        return metovaBitmap;
    }

    private void setMetovaBitmap( Bitmap metovaBitmap ) {

        this.metovaBitmap = metovaBitmap;
    }

    private AnimatedManager getAnimatedManager() {

        return animatedManager;
    }

    private void setAnimatedManager( AnimatedManager animatedManager ) {

        this.animatedManager = animatedManager;
    }
}
