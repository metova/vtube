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

import net.rim.device.api.ui.UiApplication;

import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;
import org.metova.mobile.util.time.Dates;
import org.metova.mobile.util.timer.FixedRateTimerTask;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.Settings;
import com.metova.vtube.event.ThumbnailLoadedEvent;
import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.service.video.Videos;
import com.metova.vtube.service.video.thumbnail.Thumbnails;

public abstract class ThumbnailCarousel extends AbstractThumbnailCarousel {

    private int thumbnailId;

    private DisplayMediaTask displayMediaTask;

    protected ThumbnailCarousel() {

        addEventListener( new ThumbnailLoadedEventListener(), ThumbnailLoadedEvent.class );
    }

    protected abstract void displayMediaDescriptors();

    protected int getDisplayCount() {

        int width = getWidth();

        int thumbnailPadding = getThumbnailPadding();
        int originalWidth = Thumbnails.instance().getOriginalWidth();
        int thumbnailWidth = Thumbnails.instance().getThumbnailWidth();

        width -= originalWidth + ( 2 * thumbnailPadding );

        int thumbnailSpace = thumbnailWidth + ( 2 * thumbnailPadding );
        int factor = (int) Math.ceil( MathUtils.doubleDivision( width, thumbnailSpace ) );
        int count = ( factor % 2 == 0 ) ? factor : factor + 1;

        return 3 + count;
    }

    public int getPreferredHeight() {

        int originalHeight = Thumbnails.instance().getOriginalHeight();
        int thumbnailPadding = getThumbnailPadding();
        Edges padding = getStyleManager().getComputedStyle().getPadding();
        return originalHeight + ( thumbnailPadding * 2 ) + padding.getHeight();
    }

    protected void addFields() {

        add( new NullLabelField( 0, getPreferredHeight() ) );

        super.addFields();
    }

    protected synchronized void onVisibilityChange( boolean visible ) {

        super.onVisibilityChange( visible );

        cancelThumbnailTask();

        if ( visible ) {

            startThumbnailTask();
        }
    }

    private void startThumbnailTask() {

        cancelThumbnailTask();

        long delay = Dates.SECOND * 2;
        setThumbnailTask( new ThumbnailTimerTask( delay ) );
        Timers.scheduleAtFixedRate( getThumbnailTask(), delay );
    }

    private void cancelThumbnailTask() {

        FixedRateTimerTask thumbnailTask = getThumbnailTask();
        if ( thumbnailTask != null ) {

            thumbnailTask.cancel();
            setThumbnailTask( null );
        }
    }

    protected void scrollHorizontally( int amount ) {

        cancelThumbnailTask();
        cancelDisplayMediaTask();
        resetHighlightTask( true );

        if ( amount < 0 ) {

            setHighlightLeft( true );
        }
        else {

            setHighlightRight( true );
        }

        FeedEntry feedEntry = (FeedEntry) getCurrentObject();
        int firstThumbnailId = Thumbnails.getFirstThumbnailId( feedEntry );
        setThumbnailId( Thumbnails.findAvailableThumbnailId( feedEntry, firstThumbnailId, false ) );

        long delay = Settings.CAROUSEL_SPEED;
        setHighlightTask( new MyHighlightTimerTask( amount, delay ) );
        Timers.scheduleAtFixedRate( getHighlightTask(), 0 );
    }

    private void resetHighlightTask( boolean quiteCancel ) {

        synchronized (UiApplication.getEventLock()) {

            HighlightTimerTask highlightTask = getHighlightTask();
            if ( highlightTask != null ) {

                highlightTask.setQuiteCancel( quiteCancel );
                highlightTask.cancel();
                setHighlightTask( null );

                super.scrollHorizontally( highlightTask.getAmount() );
            }

            setHighlightLeft( false );
            setHighlightRight( false );
        }
    }

    private void cancelHighlightTask() {

        resetHighlightTask( false );

        startDisplayMediaTask();
    }

    private void startDisplayMediaTask() {

        cancelDisplayMediaTask();

        setDisplayMediaTask( new DisplayMediaTask() );
        Timers.schedule( getDisplayMediaTask(), Settings.CAROUSEL_DESCRIPTION_DELAY );
    }

    private void cancelDisplayMediaTask() {

        DisplayMediaTask displayMediaTask = getDisplayMediaTask();
        if ( displayMediaTask != null ) {

            displayMediaTask.cancel();
            setDisplayMediaTask( null );
        }
    }

    protected boolean trackwheelClick( int status, int time ) {

        final Object object = getCurrentObject();
        if ( object != null ) {

            Timers.runNow( new Runnable() {

                public void run() {

                    Videos.displayVideo( (FeedEntry) object );
                }
            } );
        }

        return true;
    }

    private class DisplayMediaTask extends TimerTask {

        public void run() {

            synchronized (UiApplication.getEventLock()) {

                displayMediaDescriptors();
            }

            startThumbnailTask();
        }
    }

    private class ThumbnailTimerTask extends FixedRateTimerTask {

        public ThumbnailTimerTask(long scheduledDelay) {

            super( scheduledDelay );
        }

        public void onRun() {

            HighlightTimerTask highlightTimerTask = getHighlightTask();
            if ( highlightTimerTask == null ) {

                FeedEntry feedEntry = (FeedEntry) getCurrentObject();
                setThumbnailId( Thumbnails.findAvailableThumbnailId( feedEntry, getThumbnailId(), true ) );

                invalidate();
            }
        }
    }

    private class MyHighlightTimerTask extends HighlightTimerTask {

        public MyHighlightTimerTask(int amount, long period) {

            super( amount, period );
        }

        public void onRun() {

            super.onRun();

            invalidate();
        }

        protected void loudCancel() {

            cancelHighlightTask();
        }
    }

    private class ThumbnailLoadedEventListener implements EventListener {

        public void onEvent( Event event ) {

            invalidate();
        }
    }

    protected int getThumbnailId() {

        return thumbnailId;
    }

    private void setThumbnailId( int thumbnailId ) {

        this.thumbnailId = thumbnailId;
    }

    private DisplayMediaTask getDisplayMediaTask() {

        return displayMediaTask;
    }

    private void setDisplayMediaTask( DisplayMediaTask displayMediaTask ) {

        this.displayMediaTask = displayMediaTask;
    }
}
