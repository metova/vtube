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

import java.util.TimerTask;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;

import org.metova.bb.theme.precision.scrollbar.vertical.HighlightedVerticalScrollbar;
import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.field.richcontent.RichTextField;
import org.metova.bb.widgets.managed.LoadingState;
import org.metova.mobile.util.time.Dates;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.widgets.manager.ScrollingVerticalManager;
import com.metova.vtube.widgets.media.MediaRatingField;
import com.metova.vtube.widgets.media.PoweredByManager;

public class MediaCarouselDescriptionField extends ScrollingVerticalManager {

    private LabelField titleField;
    private RatingLineField ratingLineField;
    private RichTextField descriptionField;
    private LabelField authorField;
    private PoweredByManager poweredByManager;

    public MediaCarouselDescriptionField() {

        super( NON_FOCUSABLE );

        getStyleManager().setStyleClass( "MediaDescriptorField" );
    }

    protected void onInitialize() {

        LabelField titleField = new LabelField( DrawStyle.ELLIPSIS | USE_ALL_WIDTH );
        titleField.getStyleManager().setStyleClass( "Media-title" );
        setTitleField( titleField );

        RatingLineField ratingLineField = new RatingLineField();
        ratingLineField.preinitialize();
        setRatingLineField( ratingLineField );

        LabelField authorField = new LabelField( DrawStyle.ELLIPSIS | USE_ALL_WIDTH );
        authorField.getStyleManager().setStyleClass( "Media-author" );
        setAuthorField( authorField );

        RichTextField descriptionField = new RichTextField( NON_FOCUSABLE | USE_ALL_WIDTH );
        descriptionField.getStyleManager().setStyleClass( "Media-desctiption" );
        setDescriptionField( descriptionField );

        setPoweredByManager( new PoweredByManager() );
    }

    protected void onLoading() {

        add( getTitleField() );
        add( getRatingLineField() );
        add( getDescriptionField() );
        add( getAuthorField() );
        add( new NullLabelField( 0, 5 ) );
        add( getPoweredByManager() );
    }

    public int scrollVertically( int amount ) {

        LoadingState loadingState = getLoadingState();
        if ( !loadingState.isUnloading() ) {

            HighlightedVerticalScrollbar verticalScrollbar = (HighlightedVerticalScrollbar) getVerticalScrollbar();

            TimerTask highlightTask = verticalScrollbar.getHighlightTask();
            if ( highlightTask != null && verticalScrollbar.isHighlightUp() ) {

                int verticalScroll = getVerticalScroll();
                if ( verticalScroll == 0 ) {

                    return 0;
                }
            }
        }

        return forceScrollVertically( amount );
    }

    private int forceScrollVertically( int amount ) {

        return super.scrollVertically( amount );
    }

    public int getScrollAmount() {

        RichTextField richTextField = getDescriptionField();
        Font font = richTextField.getFont();
        return font.getHeight();
    }

    public void setContent( FeedEntry feedEntry ) {

        Media media = feedEntry.getMedia();

        LabelField titleField = getTitleField();
        titleField.setText( media.getTitle() );

        RatingLineField ratingLineField = getRatingLineField();

        MediaRatingField mediaRatingField = ratingLineField.getMediaRatingField();
        mediaRatingField.setRatingValue( feedEntry.getRatingValue() );

        LabelField durationField = ratingLineField.getDurationField();
        durationField.setText( Videos.getLengthText( media.getSeconds() * Dates.SECOND, true ) );

        int limit = 200;
        String description = media.getDescription();
        description = ( description.length() > limit ) ? description.substring( 0, limit ) + "... (more)" : description;

        RichTextField richTextField = getDescriptionField();
        richTextField.setText( description );

        LabelField authorField = getAuthorField();
        authorField.setText( "by " + feedEntry.getAuthorName() );

        setVerticalScroll( 0 );

        invalidate();
    }

    private LabelField getTitleField() {

        return titleField;
    }

    private void setTitleField( LabelField titleField ) {

        this.titleField = titleField;
    }

    private RichTextField getDescriptionField() {

        return descriptionField;
    }

    private void setDescriptionField( RichTextField descriptionField ) {

        this.descriptionField = descriptionField;
    }

    private RatingLineField getRatingLineField() {

        return ratingLineField;
    }

    private void setRatingLineField( RatingLineField ratingLineField ) {

        this.ratingLineField = ratingLineField;
    }

    private PoweredByManager getPoweredByManager() {

        return poweredByManager;
    }

    private void setPoweredByManager( PoweredByManager poweredByManager ) {

        this.poweredByManager = poweredByManager;
    }

    private LabelField getAuthorField() {

        return authorField;
    }

    private void setAuthorField( LabelField authorField ) {

        this.authorField = authorField;
    }
}
