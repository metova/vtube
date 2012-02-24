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
package com.metova.vtube.widgets.media;

import java.io.IOException;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.field.label.LabelField;
import org.metova.mobile.rt.graphics.MobileImages;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;

public class MediaRatingField extends LabelField {

    private static final Logger log = Logger.getLogger( MediaRatingField.class );

    private Bitmap ratingEmpty;
    private Bitmap ratingFull;

    private double ratingValue;

    public MediaRatingField() {

        getStyleManager().setStyleClass( "Media-rating" );
    }

    public int getPreferredWidth() {

        Bitmap ratingEmpty = getRatingEmpty();

        Edges padding = getPadding();
        Edges margin = getMargin();
        return ratingEmpty.getWidth() + margin.getWidth() + padding.getWidth();
    }

    public int getPreferredHeight() {

        Bitmap ratingEmpty = getRatingEmpty();

        Edges padding = getPadding();
        Edges margin = getMargin();
        return ratingEmpty.getHeight() + margin.getHeight() + padding.getHeight();
    }

    protected void paint( Graphics graphics, int width, int height ) {

        super.paint( graphics, width, height );

        Bitmap ratingEmpty = getRatingEmpty();
        Bitmap ratingFull = getRatingFull();

        int bitmapWidth = ratingEmpty.getWidth();
        int bitmapHeight = ratingEmpty.getHeight();

        double ratingValue = getRatingValue();
        int percentWidth = (int) ( bitmapWidth * MathUtils.doubleDivision( ratingValue, 5 ) );

        graphics.drawBitmap( 0, 0, bitmapWidth, bitmapHeight, ratingEmpty, 0, 0 );
        graphics.drawBitmap( 0, 0, percentWidth, bitmapHeight, ratingFull, 0, 0 );
    }

    private Bitmap getRatingEmpty() {

        if ( ratingEmpty == null ) {

            String imagePath = getStyleManager().getImagePath( "rating-empty" );

            try {

                setRatingEmpty( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return ratingEmpty;
    }

    private void setRatingEmpty( Bitmap ratingEmpty ) {

        this.ratingEmpty = ratingEmpty;
    }

    private Bitmap getRatingFull() {

        if ( ratingFull == null ) {

            String imagePath = getStyleManager().getImagePath( "rating-full" );

            try {

                setRatingFull( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return ratingFull;
    }

    private void setRatingFull( Bitmap ratingFull ) {

        this.ratingFull = ratingFull;
    }

    private double getRatingValue() {

        return ratingValue;
    }

    public void setRatingValue( double ratingValue ) {

        this.ratingValue = ratingValue;
    }
}
