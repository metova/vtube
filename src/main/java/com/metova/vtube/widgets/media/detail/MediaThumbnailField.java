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
package com.metova.vtube.widgets.media.detail;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.bb.widgets.theme.StyleManager;
import org.metova.bb.widgets.theme.StyledField;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;

import com.metova.vtube.model.feed.MediaThumbnail;
import com.metova.vtube.service.video.thumbnail.Thumbnails;

public abstract class MediaThumbnailField extends Field implements StyledField {

    private Bitmap bitmap;

    private StyleManager styleManager;

    protected abstract MediaThumbnail getMediaThumbnail();

    public int getPreferredWidth() {

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges margin = computedStyle.getMargin();

        Bitmap bitmap = getBitmap();
        return bitmap.getWidth() + margin.getWidth();
    }

    public int getPreferredHeight() {

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges margin = computedStyle.getMargin();

        Bitmap bitmap = getBitmap();
        return bitmap.getHeight() + margin.getHeight();
    }

    protected void paint( Graphics graphics ) {

        int width = getWidth();
        int height = getHeight();

        Bitmap bitmap = getBitmap();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int bitmapX = (int) ( ( width - bitmapWidth ) * 0.50 );
        int bitmapY = (int) ( ( height - bitmapHeight ) * 0.50 );
        graphics.drawBitmap( bitmapX, bitmapY, bitmapWidth, bitmapHeight, bitmap, 0, 0 );
    }

    protected void layout( int width, int height ) {

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges margin = computedStyle.getMargin();

        Bitmap bitmap = updateBitmap( width, height );
        setExtent( width, bitmap.getHeight() + margin.getHeight() );
        setBitmap( bitmap );
    }

    private Bitmap updateBitmap( int width, int height ) {

        Bitmap result = null;

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges margin = computedStyle.getMargin();
        width -= margin.getWidth();
        height -= margin.getHeight();

        MediaThumbnail mediaThumbnail = getMediaThumbnail();
        EncodedImage encodedImage = mediaThumbnail.getEncodedImage();

        int bitmapWidth = encodedImage.getWidth();
        int bitmapHeight = encodedImage.getHeight();

        if ( bitmapWidth <= width && bitmapHeight <= height ) {

            result = encodedImage.getBitmap();
        }
        else {

            double factor = 1.0;

            if ( bitmapWidth > width ) {

                factor = MathUtils.doubleDivision( width, bitmapWidth );
            }
            else {

                factor = MathUtils.doubleDivision( height, bitmapHeight );
            }

            bitmapWidth *= factor;
            bitmapHeight *= factor;
            result = Thumbnails.getBitmap( mediaThumbnail, bitmapWidth, bitmapHeight );
        }

        return result;
    }

    public void applyComputedStyle() {

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();

        Edges margin = computedStyle.getMargin();
        setMargin( margin.getTop(), margin.getRight(), margin.getBottom(), margin.getLeft() );
    }

    public StyleManager getStyleManager() {

        if ( styleManager == null ) {

            setStyleManager( new StyleManager( this, "Media-thumbnail" ) );
        }

        return styleManager;
    }

    public void setStyleManager( StyleManager styleManager ) {

        this.styleManager = styleManager;
    }

    private Bitmap getBitmap() {

        return bitmap;
    }

    private void setBitmap( Bitmap bitmap ) {

        this.bitmap = bitmap;
    }
}
