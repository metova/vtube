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

import java.io.IOException;
import java.util.Vector;

import m.org.apache.log4j.Logger;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.manager.phantom.PhantomCarouselManager;
import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.bb.widgets.theme.ThemeManager;
import org.metova.bb.widgets.theme.model.ClassDescriptor;
import org.metova.bb.widgets.theme.model.ThemeDescriptor;
import org.metova.bb.widgets.util.GraphicsUtility;
import org.metova.mobile.rt.graphics.MobileGraphics;
import org.metova.mobile.rt.graphics.MobileImages;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;
import org.metova.mobile.util.timer.FixedRateTimerTask;

import com.metova.vtube.model.feed.FeedEntry;
import com.metova.vtube.model.feed.Media;
import com.metova.vtube.model.feed.MediaThumbnail;
import com.metova.vtube.service.feed.FeedStack;
import com.metova.vtube.service.video.thumbnail.Thumbnails;

public abstract class AbstractThumbnailCarousel extends PhantomCarouselManager {

    private static final Logger log = Logger.getLogger( AbstractThumbnailCarousel.class );

    private int thumbnailPadding;

    private Bitmap leftArrow;
    private Bitmap leftArrowHighlight;
    private boolean highlightLeft;

    private Bitmap rightArrow;
    private Bitmap rightArrowHighlight;
    private boolean highlightRight;

    private HighlightTimerTask highlightTask;
    private FixedRateTimerTask thumbnailTask;

    private ComputedStyle boardStyle;

    protected AbstractThumbnailCarousel() {

        getStyleManager().setStyleClass( "ThumbnailCarousel" );
    }

    protected abstract int getThumbnailId();

    private int getTopBoardDescriptorY( int height ) {

        int thumbnailPadding = getThumbnailPadding();
        int thumbnailHeight = Thumbnails.instance().getThumbnailHeight();
        return (int) ( ( ( ( height - thumbnailHeight ) * 0.50 ) - thumbnailPadding ) * 0.50 );
    }

    private void paintArrows( Graphics graphics, int width, int height ) {

        int thumbnailPadding = getThumbnailPadding();

        int y = getTopBoardDescriptorY( height );

        Bitmap leftArrow = isHighlightLeft() ? getLeftArrowHighlight() : getLeftArrow();
        if ( leftArrow != null ) {

            int leftArrowX = thumbnailPadding;
            int leftArrowY = (int) ( y - ( leftArrow.getHeight() * 0.50 ) );
            MobileGraphics.instance().drawImage( graphics, leftArrow, leftArrowX, leftArrowY, MobileGraphics.LEFT | MobileGraphics.TOP );
        }

        Bitmap rightArrow = isHighlightRight() ? getRightArrowHighlight() : getRightArrow();
        if ( rightArrow != null ) {

            int rightArrowX = width - thumbnailPadding;
            int rightArrowY = (int) ( y - ( leftArrow.getHeight() * 0.50 ) );
            MobileGraphics.instance().drawImage( graphics, rightArrow, rightArrowX, rightArrowY, MobileGraphics.RIGHT | MobileGraphics.TOP );
        }
    }

    private void paintFocusBackground( Graphics graphics, int width, int height ) {

        int thumbnailPadding = getThumbnailPadding();

        int originalWidth = Thumbnails.instance().getOriginalWidth();
        int originalHeight = Thumbnails.instance().getOriginalHeight();

        int x = (int) ( ( width - originalWidth ) * 0.50 );
        int y = (int) ( ( height - originalHeight ) * 0.50 );

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges padding = computedStyle.getPadding();

        int highlightX = x - thumbnailPadding;
        int highlightY = y - padding.getTop() - (int) ( thumbnailPadding * 0.50 );
        int highlightWidth = originalWidth + ( 2 * thumbnailPadding );
        int highlightHeight = originalHeight + padding.getHeight() + thumbnailPadding;

        GraphicsUtility.paintBackground( graphics, highlightX, highlightY, highlightWidth, highlightHeight, true, computedStyle );
        GraphicsUtility.paintBorder( graphics, highlightX, highlightY, highlightWidth, highlightHeight, isFocus(), computedStyle );
    }

    private void paintPageIndex( Graphics graphics, int width, int height ) {

        int originalHeight = Thumbnails.instance().getOriginalHeight();

        String text = "Page " + String.valueOf( FeedStack.getCurrentPageIndex() + 1 );

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges padding = computedStyle.getPadding();
        Font font = computedStyle.getFont();

        int x = (int) ( width * 0.50 );
        int y = (int) ( ( height - originalHeight ) * 0.50 );

        int availableHeight = padding.getTop();
        int baseline = availableHeight - ( font.getHeight() - font.getBaseline() );
        int textY = ( y - availableHeight ) + (int) ( baseline * 0.50 );

        graphics.setFont( font );
        graphics.setColor( computedStyle.getColor() );
        MobileGraphics.instance().drawString( graphics, text, x, textY, MobileGraphics.HCENTER | MobileGraphics.VCENTER );
    }

    private void paintBoard( Graphics graphics, int width, int height ) {

        int thumbnailHeight = Thumbnails.instance().getThumbnailHeight();
        int thumbnailPadding = getThumbnailPadding();

        int fieldWidth = getWidth();
        int fieldHeight = getHeight();

        int highlightHeight = thumbnailHeight + ( 2 * thumbnailPadding );
        int y = (int) ( ( fieldHeight - highlightHeight ) * 0.50 );

        ComputedStyle boardStyle = getBoardStyle();
        GraphicsUtility.fillRoundRect( graphics, 0, y, fieldWidth, highlightHeight, boardStyle );
    }

    protected void paintBackground( Graphics graphics, int width, int height ) {

        paintBoard( graphics, width, height );
        paintFocusBackground( graphics, width, height );
        paintPageIndex( graphics, width, height );
        paintArrows( graphics, width, height );
    }

    /**
     * BEWARE THE MATH
     */
    protected void paintObject( Graphics graphics, Object object, int displayIndex ) {

        int originalWidth = Thumbnails.instance().getOriginalWidth();
        int originalHeight = Thumbnails.instance().getOriginalHeight();

        int thumbnailWidth = Thumbnails.instance().getThumbnailWidth();
        int thumbnailHeight = Thumbnails.instance().getThumbnailHeight();

        boolean leftHighlight = isHighlightLeft();
        boolean rightHighlight = isHighlightRight();

        HighlightTimerTask highlightTimerTask = getHighlightTask();
        int frame = ( highlightTimerTask == null ) ? thumbnailWidth : highlightTimerTask.getValue();

        int displayCount = getDisplayCount();
        int focusIndex = (int) Math.floor( displayCount * 0.50 );

        boolean centerIndex = focusIndex == displayIndex;
        boolean drawFocus = centerIndex;

        if ( highlightTimerTask != null ) {

            // while the center thumbnail shrinks, another must grow bigger
            drawFocus |= rightHighlight && focusIndex + 1 == displayIndex;
            drawFocus |= leftHighlight && focusIndex - 1 == displayIndex;
        }

        int bitmapWidth = thumbnailWidth;
        int bitmapHeight = thumbnailHeight;

        if ( drawFocus ) {

            int divisor = centerIndex ? originalWidth : frame;
            int dividend = centerIndex ? frame : thumbnailWidth;
            double focusScale = MathUtils.doubleDivision( divisor, dividend );

            // resize the shrinking/growing thumbnails
            bitmapWidth *= focusScale;
            bitmapHeight *= focusScale;
        }

        int fieldWidth = getWidth();
        int fieldHeight = getHeight();

        int position = (int) ( fieldWidth * 0.50 );

        int thumbnailPadding = getThumbnailPadding();

        if ( !centerIndex ) {

            // shift the position of non-centered thumbnails outside the focus box
            int offsetX = (int) ( originalWidth * 0.50 ) + ( Thumbnails.instance().selectByResolution( 5, 4 ) * thumbnailPadding );

            position += ( displayIndex < focusIndex ) ? -1 * offsetX : offsetX;
        }

        if ( !centerIndex ) {

            // shift the thumbnails to the proper position
            int offsetX = (int) ( thumbnailWidth * 0.50 ) + ( Thumbnails.instance().selectByResolution( 4, 3 ) * thumbnailPadding );
            offsetX *= ( Math.abs( focusIndex - displayIndex ) - 1 );

            position += ( displayIndex < focusIndex ) ? -1 * offsetX : offsetX;
        }

        int x = (int) ( position - ( bitmapWidth * 0.50 ) );
        int y = (int) ( ( fieldHeight - bitmapHeight ) * 0.50 );

        FeedEntry feedEntry = (FeedEntry) object;

        if ( highlightTimerTask != null ) {

            int offsetWidth = thumbnailWidth;

            if ( drawFocus ) {

                // the focused thumbnails need to move further in the same amount of time
                offsetWidth = centerIndex ? frame : originalWidth;
                offsetWidth -= ( Thumbnails.instance().selectByResolution( 3, 2 ) * thumbnailPadding );
                offsetWidth += Thumbnails.instance().selectByResolution( 0, (int) ( 0.50 * thumbnailPadding ) );
                offsetWidth += Thumbnails.instance().selectByResolution( -1, 1 );
            }
            else {

                offsetWidth += Thumbnails.instance().selectByResolution( 0, 4 );
            }

            int difference = frame - thumbnailWidth;
            int offsetX = (int) ( offsetWidth * MathUtils.doubleDivision( difference, thumbnailWidth ) );
            x += rightHighlight ? -1 * offsetX : offsetX;
        }

        if ( !drawFocus || highlightTimerTask == null ) {

            // paint the display index for the non-shrinking/growing thumbnails
            int displayIndexY = centerIndex ? originalHeight : thumbnailHeight;
            paintDisplayIndex( graphics, feedEntry, x, y + displayIndexY, bitmapWidth, drawFocus );
        }

        Media media = feedEntry.getMedia();
        Vector mediaThumbnails = media.getThumbnails();
        int thumbnailId = centerIndex ? getThumbnailId() : Thumbnails.getFirstThumbnailId( feedEntry );
        MediaThumbnail mediaThumbnail = (MediaThumbnail) mediaThumbnails.elementAt( thumbnailId );

        Bitmap bitmap = Thumbnails.getBitmap( mediaThumbnail, bitmapWidth, bitmapHeight );
        paintMediaThumbnail( graphics, bitmap, x, y, bitmapWidth, bitmapHeight );
    }

    private void paintDisplayIndex( Graphics graphics, FeedEntry feedEntry, int x, int y, int bitmapWidth, boolean focus ) {

        String text = String.valueOf( feedEntry.getIndex() + 1 );

        ComputedStyle computedStyle = focus ? getStyleManager().getComputedStyle() : getBoardStyle();
        Edges padding = computedStyle.getPadding();
        Font font = computedStyle.getFont();

        int textX = x + (int) ( bitmapWidth * 0.50 );

        int thumbnailPadding = focus ? getThumbnailPadding() : 0;
        int availableHeight = padding.getBottom() - (int) ( thumbnailPadding * 0.50 );
        int textY = y + availableHeight;

        graphics.setFont( font );
        graphics.setColor( computedStyle.getColor() );
        MobileGraphics.instance().drawString( graphics, text, textX, textY, MobileGraphics.HCENTER | MobileGraphics.VCENTER );
    }

    private void paintMediaThumbnail( Graphics graphics, Bitmap bitmap, int x, int y, int bitmapWidth, int bitmapHeight ) {

        if ( bitmap != null ) {

            MobileGraphics.instance().drawImage( graphics, bitmap, x, y, MobileGraphics.LEFT | MobileGraphics.TOP );
        }
        else {

            ComputedStyle boardStyle = getBoardStyle();
            int backgroundOpacity = boardStyle.getBackgroundOpacity();
            int backgroundColor = boardStyle.getBackgroundColor();
            GraphicsUtility.fillRect( graphics, x, y, bitmapWidth, bitmapHeight, backgroundOpacity, backgroundColor );

            ComputedStyle computedStyle = getStyleManager().getComputedStyle();
            graphics.setColor( computedStyle.getFocusBorderColor() );
            graphics.drawRect( x, y, bitmapWidth, bitmapHeight );
        }
    }

    public void applyComputedStyle() {

        super.applyComputedStyle();

        setThumbnailPadding( getStyleManager().getValueInt( "thumbnail-padding" ) );
    }

    private ComputedStyle getBoardStyle() {

        if ( boardStyle == null ) {

            ThemeDescriptor themeDescriptor = ThemeManager.instance().getThemeDescriptor( null );
            ClassDescriptor descriptor = themeDescriptor.getClassDescriptor( "ThumbnailCarousel-board" );
            seBoardStyle( ComputedStyle.getComputedStyle( descriptor ) );
        }

        return boardStyle;
    }

    private void seBoardStyle( ComputedStyle boardStyle ) {

        this.boardStyle = boardStyle;
    }

    protected int getThumbnailPadding() {

        return thumbnailPadding;
    }

    private void setThumbnailPadding( int thumbnailPadding ) {

        this.thumbnailPadding = thumbnailPadding;
    }

    private Bitmap getLeftArrow() {

        if ( leftArrow == null ) {

            String imagePath = getStyleManager().getImagePath( "arrow-left" );

            try {

                setLeftArrow( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return leftArrow;
    }

    private void setLeftArrow( Bitmap leftArrow ) {

        this.leftArrow = leftArrow;
    }

    private Bitmap getLeftArrowHighlight() {

        if ( leftArrowHighlight == null ) {

            String imagePath = getStyleManager().getImagePath( "arrow-left-highlight" );

            try {

                setLeftArrowHighlight( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return leftArrowHighlight;
    }

    private void setLeftArrowHighlight( Bitmap leftArrowHighlight ) {

        this.leftArrowHighlight = leftArrowHighlight;
    }

    private Bitmap getRightArrow() {

        if ( rightArrow == null ) {

            String imagePath = getStyleManager().getImagePath( "arrow-right" );

            try {

                setRightArrow( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return rightArrow;
    }

    private void setRightArrow( Bitmap rightArrow ) {

        this.rightArrow = rightArrow;
    }

    private Bitmap getRightArrowHighlight() {

        if ( rightArrowHighlight == null ) {

            String imagePath = getStyleManager().getImagePath( "arrow-right-highlight" );

            try {

                setRightArrowHighlight( (Bitmap) MobileImages.instance().getBitmapWithCache( imagePath, getClass() ) );
            }
            catch (IOException ex) {

                log.error( "Unable to load bitmap: " + imagePath );
            }
        }

        return rightArrowHighlight;
    }

    private void setRightArrowHighlight( Bitmap rightArrowHighlight ) {

        this.rightArrowHighlight = rightArrowHighlight;
    }

    private boolean isHighlightLeft() {

        return highlightLeft;
    }

    protected void setHighlightLeft( boolean highlightLeft ) {

        this.highlightLeft = highlightLeft;
    }

    private boolean isHighlightRight() {

        return highlightRight;
    }

    protected void setHighlightRight( boolean highlightRight ) {

        this.highlightRight = highlightRight;
    }

    protected HighlightTimerTask getHighlightTask() {

        return highlightTask;
    }

    protected void setHighlightTask( HighlightTimerTask highlightTask ) {

        this.highlightTask = highlightTask;
    }

    protected FixedRateTimerTask getThumbnailTask() {

        return thumbnailTask;
    }

    protected void setThumbnailTask( FixedRateTimerTask thumbnailTask ) {

        this.thumbnailTask = thumbnailTask;
    }
}
