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

import java.util.Vector;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;

import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.field.richcontent.RichTextField;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;
import org.metova.mobile.util.text.Text;
import org.metova.mobile.util.time.Dates;

import com.metova.vtube.event.ThumbnailLoadedEvent;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.model.feed.MediaThumbnail;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.widgets.manager.ScrollingVerticalManager;
import com.metova.vtube.widgets.media.MediaRatingField;
import com.metova.vtube.widgets.media.PoweredByManager;
import com.metova.vtube.widgets.media.carousel.RatingLineField;

public abstract class MediaDetailDescriptionField extends ScrollingVerticalManager {

    private LoadingAnimationManager loadingAnimationManager;
    private MediaThumbnailField mediaThumbnailField;
    private RichTextField titleField;
    private RatingLineField ratingLineField;
    private LabelField ratingCountField;
    private LabelField favoritesField;
    private LabelField viewsField;
    private RichTextField descriptionField;
    private LabelField authorField;
    private PoweredByManager poweredByManager;

    protected MediaDetailDescriptionField() {

        getStyleManager().setStyleClass( "MediaDescriptorField" );

        addEventListener( new ThumbnailLoadedEventListener(), ThumbnailLoadedEvent.class );
    }

    protected abstract FeedEntry getFeedEntry();

    protected void onInitialize() {

        initializeThumbnailField();

        MediaThumbnailField mediaThumbnailField = getMediaThumbnailField();
        if ( mediaThumbnailField == null ) {

            setLoadingAnimationManager( new LoadingAnimationManager() );
        }

        FeedEntry feedEntry = getFeedEntry();

        Media media = feedEntry.getMedia();

        RichTextField titleField = new RichTextField( media.getTitle(), USE_ALL_WIDTH | NON_FOCUSABLE );
        titleField.getStyleManager().setStyleClass( "Media-title" );
        setTitleField( titleField );

        RatingLineField ratingLineField = new RatingLineField();
        ratingLineField.preinitialize();
        setRatingLineField( ratingLineField );

        MediaRatingField mediaRatingField = ratingLineField.getMediaRatingField();
        mediaRatingField.setRatingValue( feedEntry.getRatingValue() );

        LabelField durationField = ratingLineField.getDurationField();
        durationField.setText( Videos.getLengthText( media.getSeconds() * Dates.SECOND, true ) );

        LabelField ratingCountField = new LabelField( feedEntry.getRatingCount() + " ratings" );
        ratingCountField.getStyleManager().setStyleClass( "Media-rating-count" );
        setRatingCountField( ratingCountField );

        String favorites = feedEntry.getFavoriteCount() + " favorites";
        LabelField favoritesField = new LabelField( favorites, DrawStyle.ELLIPSIS | USE_ALL_WIDTH );
        favoritesField.getStyleManager().setStyleClass( "Media-favorites" );
        setFavoritesField( favoritesField );

        String views = feedEntry.getViewCount() + " views";
        LabelField viewsField = new LabelField( views, DrawStyle.ELLIPSIS | USE_ALL_WIDTH );
        viewsField.getStyleManager().setStyleClass( "Media-views" );
        setViewsField( viewsField );

        String description = media.getDescription();
        RichTextField descriptionField = new RichTextField( description, NON_FOCUSABLE | USE_ALL_WIDTH );
        descriptionField.getStyleManager().setStyleClass( "Media-desctiption" );
        setDescriptionField( descriptionField );

        String author = "by " + feedEntry.getAuthorName();
        LabelField authorField = new LabelField( author, DrawStyle.ELLIPSIS | USE_ALL_WIDTH );
        authorField.getStyleManager().setStyleClass( "Media-author" );
        setAuthorField( authorField );

        setPoweredByManager( new PoweredByManager() );
    }

    protected void onLoading() {

        MediaThumbnailField mediaThumbnailField = getMediaThumbnailField();
        if ( mediaThumbnailField != null ) {

            add( mediaThumbnailField );
        }
        else {

            add( getLoadingAnimationManager() );
        }

        add( getTitleField() );
        add( getRatingLineField() );
        add( getRatingCountField() );
        add( getFavoritesField() );
        add( getViewsField() );
        add( getDescriptionField() );
        add( getAuthorField() );
        add( new NullLabelField( 0, 5 ) );
        add( getPoweredByManager() );
    }

    private void initializeThumbnailField() {

        MediaThumbnail mediaThumbnail = getMediaThumbnail();
        if ( mediaThumbnail.getEncodedImage() != null ) {

            setMediaThumbnailField( new MyMediaThumbnailField() );
        }
    }

    protected MediaThumbnail getMediaThumbnail() {

        FeedEntry feedEntry = getFeedEntry();
        Media media = feedEntry.getMedia();
        Vector thumbnails = media.getThumbnails();
        return (MediaThumbnail) thumbnails.elementAt( 0 );
    }

    public int getScrollAmount() {

        RichTextField richTextField = getDescriptionField();
        Font font = richTextField.getFont();
        return font.getHeight();
    }

    protected void makeMenu( Menu menu, int instance ) {

        FeedEntry feedEntry = getFeedEntry();
        Videos.makeMenu( feedEntry, menu );

        super.makeMenu( menu, instance );
    }

    private class ThumbnailLoadedEventListener implements EventListener {

        public void onEvent( Event event ) {

            ThumbnailLoadedEvent thumbnailLoadedEvent = (ThumbnailLoadedEvent) event;
            String url = (String) thumbnailLoadedEvent.getSource();

            MediaThumbnail mediaThumbnail = getMediaThumbnail();
            if ( Text.equals( url, mediaThumbnail.getUrl() ) ) {

                initializeThumbnailField();

                UiApplication.getUiApplication().invokeLater( new Runnable() {

                    public void run() {

                        LoadingAnimationManager loadingAnimationManager = getLoadingAnimationManager();
                        MediaThumbnailField mediaThumbnailField = getMediaThumbnailField();
                        replace( loadingAnimationManager, mediaThumbnailField );
                    }
                } );
            }
        }
    }

    private class MyMediaThumbnailField extends MediaThumbnailField {

        protected MediaThumbnail getMediaThumbnail() {

            return MediaDetailDescriptionField.this.getMediaThumbnail();
        }
    }

    private PoweredByManager getPoweredByManager() {

        return poweredByManager;
    }

    private void setPoweredByManager( PoweredByManager poweredByManager ) {

        this.poweredByManager = poweredByManager;
    }

    private RatingLineField getRatingLineField() {

        return ratingLineField;
    }

    private void setRatingLineField( RatingLineField ratingLineField ) {

        this.ratingLineField = ratingLineField;
    }

    private LabelField getFavoritesField() {

        return favoritesField;
    }

    private void setFavoritesField( LabelField favoritesField ) {

        this.favoritesField = favoritesField;
    }

    private LabelField getViewsField() {

        return viewsField;
    }

    private void setViewsField( LabelField viewsField ) {

        this.viewsField = viewsField;
    }

    private RichTextField getDescriptionField() {

        return descriptionField;
    }

    private void setDescriptionField( RichTextField descriptionField ) {

        this.descriptionField = descriptionField;
    }

    private LabelField getAuthorField() {

        return authorField;
    }

    private void setAuthorField( LabelField authorField ) {

        this.authorField = authorField;
    }

    private RichTextField getTitleField() {

        return titleField;
    }

    private void setTitleField( RichTextField titleField ) {

        this.titleField = titleField;
    }

    private MediaThumbnailField getMediaThumbnailField() {

        return mediaThumbnailField;
    }

    private void setMediaThumbnailField( MediaThumbnailField mediaThumbnailField ) {

        this.mediaThumbnailField = mediaThumbnailField;
    }

    public LabelField getRatingCountField() {

        return ratingCountField;
    }

    private void setRatingCountField( LabelField ratingCountField ) {

        this.ratingCountField = ratingCountField;
    }

    private LoadingAnimationManager getLoadingAnimationManager() {

        return loadingAnimationManager;
    }

    private void setLoadingAnimationManager( LoadingAnimationManager loadingAnimationManager ) {

        this.loadingAnimationManager = loadingAnimationManager;
    }
}
