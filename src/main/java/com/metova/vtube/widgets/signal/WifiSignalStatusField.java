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
package com.metova.vtube.widgets.signal;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.indicator.CarrierSignalStatusField;
import org.metova.mobile.rt.graphics.MobileImages;

import com.metova.vtube.widgets.splash.SplashContainer;

public abstract class WifiSignalStatusField extends CarrierSignalStatusField {

    private static final Logger log = Logger.getLogger( SplashContainer.class );

    private Bitmap wifiBitmap;
    private String wifiImagePath;

    public WifiSignalStatusField(String wifiImagePath, String signalImagePath, String deadSignalImagePath, int[] barSizes) {

        super( signalImagePath, deadSignalImagePath, barSizes );

        setWifiImagePath( wifiImagePath );
    }

    protected abstract boolean displayWiFiLogo();

    protected void paint( Graphics graphics ) {

        if ( !displayWiFiLogo() ) {

            super.paint( graphics );
        }
        else {

            int width = getWidth();
            int height = getHeight();

            Bitmap bitmap = getWifiBitmap();

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            int bitmapX = width - bitmapWidth;
            int bitmapY = (int) ( ( height - bitmapHeight ) * 0.50 );

            graphics.drawBitmap( bitmapX, bitmapY, bitmapWidth, bitmapHeight, bitmap, 0, 0 );
        }
    }

    private Bitmap getWifiBitmap() {

        if ( wifiBitmap == null ) {

            String imagePath = getWifiImagePath();

            try {

                setWifiBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (Throwable t) {

                log.error( "Unable to display image: " + imagePath, t );
            }
        }

        return wifiBitmap;
    }

    private void setWifiBitmap( Bitmap wifiBitmap ) {

        this.wifiBitmap = wifiBitmap;
    }

    private String getWifiImagePath() {

        return wifiImagePath;
    }

    private void setWifiImagePath( String wifiImagePath ) {

        this.wifiImagePath = wifiImagePath;
    }
}
