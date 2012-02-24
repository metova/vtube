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
package com.metova.vtube.widgets.logo;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;

import org.metova.bb.widgets.field.image.AbstractBitmapField;
import org.metova.mobile.rt.graphics.MobileImages;

import com.metova.vtube.widgets.media.carousel.MediaCarouselDescriptionField;

public class PoweredByMetova extends AbstractBitmapField {

    private static final Logger log = Logger.getLogger( MediaCarouselDescriptionField.class );

    private Bitmap bitmap;

    public PoweredByMetova() {

        getStyleManager().setStyleClass( "PoweredByMetova" );
    }

    protected Bitmap getBitmap() {

        if ( bitmap == null ) {

            String imagePath = getStyleManager().getImagePath( "metova-logo" );

            try {

                setBitmap( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (Throwable t) {

                log.error( "Unable to display image: " + imagePath, t );
            }
        }

        return bitmap;
    }

    private void setBitmap( Bitmap bitmap ) {

        this.bitmap = bitmap;
    }
}
