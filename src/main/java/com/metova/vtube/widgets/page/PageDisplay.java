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
package com.metova.vtube.widgets.page;

import m.java.util.Iterator;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.component.Menu;

import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.field.Fields;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.bb.widgets.menu.Menus;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.service.feed.FeedStack;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.ui.MediaDetailContainer;
import com.metova.vtube.widgets.media.carousel.MediaCarouselDescriptionField;
import com.metova.vtube.widgets.media.carousel.ThumbnailCarousel;

public class PageDisplay extends AbstractVerticalFieldManager {

    private ThumbnailCarousel thumbnailCarousel;
    private MediaCarouselDescriptionField mediaDescriptorField;

    public PageDisplay() {

        super( USE_ALL_HEIGHT | NO_VERTICAL_SCROLL );
    }

    protected void onInitialize() {

        setThumbnailCarousel( new MyThumbnailCarousel() );
        setMediaDescriptorField( new MyMediaDescriptorField() );
    }

    protected void onLoading() {

        loadEntries();

        add( getThumbnailCarousel() );
        add( new NullLabelField( 0, 10 ) );
        add( getMediaDescriptorField() );
    }

    public boolean refresh() {

        boolean result = false;

        ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        int original = thumbnailCarousel.size();

        loadEntries();

        int target = thumbnailCarousel.size();

        if ( original > 0 && target > 0 ) {

            displayMediaDescriptors();
            result = true;
        }
        else if ( original == 0 && target == 0 ) {

            result = true;
        }

        return result;
    }

    private void loadEntries() {

        ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        thumbnailCarousel.deleteAllObjects();

        Iterator iterator = FeedStack.currentPageEntries();
        while (iterator.hasNext()) {

            thumbnailCarousel.addObject( iterator.next() );
        }
    }

    private void displayMediaDescriptors() {

        ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        if ( !thumbnailCarousel.isEmpty() ) {

            Object object = thumbnailCarousel.getCurrentObject();
            if ( object != null ) {

                FeedEntry feedEntry = (FeedEntry) object;

                MediaCarouselDescriptionField mediaDescriptorField = getMediaDescriptorField();
                mediaDescriptorField.setContent( feedEntry );
            }
        }
    }

    protected int moveFocus( int amount, int status, int time ) {

        int result = amount;

        ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        if ( thumbnailCarousel.isEmpty() || Fields.isHorizontalScroll( status, true ) ) {

            result = super.moveFocus( amount, status, time );
        }
        else {

            MediaCarouselDescriptionField mediaDescriptorField = getMediaDescriptorField();
            result = mediaDescriptorField.scrollVertically( amount );
        }

        return result;
    }

    protected boolean keyChar( char c, int status, int time ) {

        final ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        final FeedEntry feedEntry = (FeedEntry) thumbnailCarousel.getCurrentObject();

        switch (c) {

            case Characters.SPACE:
            case Characters.ENTER: {

                if ( feedEntry != null ) {

                    Timers.runNow( new Runnable() {

                        public void run() {

                            Videos.displayVideo( feedEntry );
                        }
                    } );
                }

                return true;
            }

            case Characters.LATIN_CAPITAL_LETTER_D:
            case Characters.LATIN_SMALL_LETTER_D: {

                if ( feedEntry != null ) {

                    Screens.pushScreen( new MediaDetailContainer( feedEntry ) );
                }

                return true;
            }
        }

        return super.keyChar( c, status, time );
    }

    protected void makeMenu( Menu menu, int instance ) {

        ThumbnailCarousel thumbnailCarousel = getThumbnailCarousel();
        Object object = thumbnailCarousel.getCurrentObject();
        if ( object != null ) {

            FeedEntry feedEntry = (FeedEntry) object;

            menu.add( Menus.newMenuItemPushScreen( "View Details", new MediaDetailContainer( feedEntry ), Menus.ORDINAL_L1, 1 ) );

            Videos.makeMenu( feedEntry, menu );
        }

        super.makeMenu( menu, instance );
    }

    private class MyThumbnailCarousel extends ThumbnailCarousel {

        protected void displayMediaDescriptors() {

            PageDisplay.this.displayMediaDescriptors();
        }
    }

    private class MyMediaDescriptorField extends MediaCarouselDescriptionField {

        protected void onLoading() {

            displayMediaDescriptors();

            super.onLoading();
        }
    }

    private ThumbnailCarousel getThumbnailCarousel() {

        return thumbnailCarousel;
    }

    private void setThumbnailCarousel( ThumbnailCarousel thumbnailCarousel ) {

        this.thumbnailCarousel = thumbnailCarousel;
    }

    private MediaCarouselDescriptionField getMediaDescriptorField() {

        return mediaDescriptorField;
    }

    private void setMediaDescriptorField( MediaCarouselDescriptionField mediaDescriptorField ) {

        this.mediaDescriptorField = mediaDescriptorField;
    }
}
