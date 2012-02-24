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
package com.metova.vtube.widgets.media.carousel;

import net.rim.device.api.ui.XYRect;

import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;

import com.metova.vtube.widgets.media.MediaRatingField;

public class RatingLineField extends AbstractHorizontalFieldManager {

    private MediaRatingField mediaRatingField;
    private LabelField durationField;

    public RatingLineField() {

        super( NO_HORIZONTAL_SCROLL | USE_ALL_WIDTH );
    }

    protected void initializeFields() {

        setMediaRatingField( new MediaRatingField() );

        LabelField durationField = new LabelField();
        durationField.getStyleManager().setStyleClass( "Media-duration" );
        setDurationField( durationField );
    }

    protected void addFields() {

        add( getMediaRatingField() );
        add( getDurationField() );
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        height = getHeight();

        LabelField durationField = getDurationField();
        if ( durationField != null && equals( durationField.getManager() ) ) {

            XYRect extent = durationField.getExtent();

            int fieldHeight = durationField.getHeight();
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );
            setPositionChild( durationField, extent.x, fieldY );
        }

        MediaRatingField mediaRatingField = getMediaRatingField();
        if ( mediaRatingField != null && equals( mediaRatingField.getManager() ) ) {

            XYRect extent = mediaRatingField.getExtent();

            int fieldHeight = mediaRatingField.getHeight();
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );
            setPositionChild( mediaRatingField, extent.x, fieldY );
        }
    }

    public MediaRatingField getMediaRatingField() {

        return mediaRatingField;
    }

    private void setMediaRatingField( MediaRatingField mediaRatingField ) {

        this.mediaRatingField = mediaRatingField;
    }

    public LabelField getDurationField() {

        return durationField;
    }

    private void setDurationField( LabelField durationField ) {

        this.durationField = durationField;
    }
}
