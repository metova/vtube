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
package com.metova.vtube.widgets.media.player.button;

import java.io.IOException;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.tooltip.TooltipField;
import org.metova.mobile.rt.graphics.MobileImages;
import org.metova.mobile.util.coordinate.Edges;

import com.metova.vtube.service.player.MediaPlayer;
import com.metova.vtube.widgets.sidebar.SidebarButtonField;

public abstract class PlayButtonField extends SidebarButtonField implements TooltipField {

    private static final Logger log = Logger.getLogger( PlayButtonField.class );

    private Bitmap playBitmap;
    private Bitmap pauseBitmap;

    private Object tooltip;

    protected PlayButtonField(Runnable runnable) {

        this( runnable, 0 );
    }

    protected PlayButtonField(Runnable runnable, long style) {

        super( null, runnable, style );

        getStyleManager().setStyleClass( "ButtonField-play" );
    }

    protected abstract MediaPlayer getMediaPlayer();

    public int getPreferredWidth() {

        Edges padding = getPadding();
        Edges margin = getMargin();
        Bitmap bitmap = getPlayBitmap();
        return bitmap.getWidth() + margin.getWidth() + padding.getWidth();
    }

    public int getPreferredHeight() {

        Edges padding = getPadding();
        Edges margin = getMargin();
        Bitmap bitmap = getPlayBitmap();
        return bitmap.getHeight() + margin.getHeight() + padding.getHeight();
    }

    protected void paint( Graphics graphics, int width, int height ) {

        MediaPlayer mediaPlayer = getMediaPlayer();

        Bitmap bitmap = mediaPlayer.isPlaying() ? getPauseBitmap() : getPlayBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        graphics.drawBitmap( 0, 0, bitmapWidth, bitmapHeight, bitmap, 0, 0 );
    }

    private Bitmap getPlayBitmap() {

        if ( playBitmap == null ) {

            String imagePath = getStyleManager().getImagePath( "play" );

            try {

                setPlayBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return playBitmap;
    }

    private void setPlayBitmap( Bitmap playBitmap ) {

        this.playBitmap = playBitmap;
    }

    private Bitmap getPauseBitmap() {

        if ( pauseBitmap == null ) {

            String imagePath = getStyleManager().getImagePath( "pause" );

            try {

                setPauseBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return pauseBitmap;
    }

    private void setPauseBitmap( Bitmap pauseBitmap ) {

        this.pauseBitmap = pauseBitmap;
    }

    public Object getTooltip() {

        return tooltip;
    }

    public void setTooltip( Object tooltip ) {

        this.tooltip = tooltip;
    }
}
