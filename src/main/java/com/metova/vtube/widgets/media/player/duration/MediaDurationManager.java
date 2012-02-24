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
package com.metova.vtube.widgets.media.player.duration;

import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;
import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;
import org.metova.mobile.util.time.Dates;

import com.metova.vtube.service.player.MediaPlayer;
import com.metova.vtube.service.video.Videos;

public abstract class MediaDurationManager extends AbstractHorizontalFieldManager {

    private LabelField labelField;
    private ProgressBarField progressBarField;

    public MediaDurationManager() {

        super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
    }

    protected abstract MediaPlayer getMediaPlayer();

    protected void initializeFields() {

        setProgressBarField( new ProgressBarField() );

        LabelField labelField = new LabelField();
        labelField.getStyleManager().setStyleClass( "Media-duration" );
        setLabelField( labelField );
    }

    protected void addFields() {

        add( getProgressBarField() );
        add( getLabelField() );
    }

    protected synchronized void onVisibilityChange( boolean visible ) {

        super.onVisibilityChange( visible );

        if ( visible ) {

            updatePlayer();
        }
    }

    public void updatePlayer() {

        MediaPlayer mediaPlayer = getMediaPlayer();
        long mediaTime = mediaPlayer.getMediaTime() / Dates.SECOND;
        long duration = mediaPlayer.getDuration() / Dates.SECOND;
        boolean forceHours = ( duration >= Dates.HOUR );

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append( Videos.getLengthText( mediaTime, forceHours ) );
        stringBuffer.append( " / " );
        stringBuffer.append( Videos.getLengthText( duration, forceHours ) );

        LabelField labelField = getLabelField();
        labelField.setText( stringBuffer.toString() );

        ProgressBarField progressBarField = getProgressBarField();
        int progressValue = (int) ( MathUtils.doubleDivision( (int) mediaTime, (int) duration ) * 100 );
        progressBarField.setValue( null, progressValue );
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();
        height = getHeight();

        int labelFieldWidth = 0;

        LabelField labelField = getLabelField();
        if ( labelField != null && equals( labelField.getManager() ) ) {

            labelFieldWidth = labelField.getPreferredWidth();
            int fieldHeight = labelField.getHeight();
            layoutChild( labelField, labelFieldWidth, fieldHeight );

            int fieldX = width - labelFieldWidth;
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );
            setPositionChild( labelField, fieldX, fieldY );
        }

        ProgressBarField progressBarField = getProgressBarField();
        if ( progressBarField != null && equals( progressBarField.getManager() ) ) {

            ComputedStyle computedStyle = progressBarField.getStyleManager().getComputedStyle();
            Edges margin = computedStyle.getMargin();

            int fieldWidth = width - labelFieldWidth - margin.getWidth();
            int fieldHeight = progressBarField.getHeight();
            layoutChild( progressBarField, fieldWidth, fieldHeight );

            int fieldX = margin.getLeft();
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );
            setPositionChild( progressBarField, fieldX, fieldY );
        }
    }

    private LabelField getLabelField() {

        return labelField;
    }

    private void setLabelField( LabelField labelField ) {

        this.labelField = labelField;
    }

    private ProgressBarField getProgressBarField() {

        return progressBarField;
    }

    private void setProgressBarField( ProgressBarField progressBarField ) {

        this.progressBarField = progressBarField;
    }
}
