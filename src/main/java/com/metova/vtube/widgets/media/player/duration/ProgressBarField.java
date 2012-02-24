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

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.field.progress.AbstractProgressField;
import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.bb.widgets.touch.event.TouchEventPlatform;
import org.metova.bb.widgets.util.GraphicsUtility;
import org.metova.mobile.util.coordinate.Edges;
import org.metova.mobile.util.math.MathUtils;

class ProgressBarField extends AbstractProgressField {

    public ProgressBarField() {

        super( null, USE_ALL_WIDTH );

        getStyleManager().setStyleClass( "Media-progress" );
    }

    public int getPreferredHeight() {

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();
        Edges margin = computedStyle.getMargin();
        Font font = computedStyle.getFont();

        return font.getHeight() + margin.getHeight();
    }

    protected void onFocus( int direction ) {

        super.onFocus( direction );

        invalidate();
    }

    protected void onUnfocus() {

        super.onUnfocus();

        invalidate();
    }

    protected boolean onTouchEvent( TouchEventPlatform message ) {

        // repaint the background and border
        invalidate();

        return super.onTouchEvent( message );
    }

    protected void paint( Graphics graphics ) {

        int width = getWidth();
        int height = getHeight();

        ComputedStyle computedStyle = getStyleManager().getComputedStyle();

        GraphicsUtility.paintBackground( graphics, 0, 0, width, height, false, computedStyle );

        int highlightWidth = (int) MathUtils.doubleDivision( width * getValue(), getMaxValue() );

        graphics.pushContext( 0, 0, highlightWidth, height, 0, 0 );
        GraphicsUtility.paintBackground( graphics, 0, 0, width, height, true, computedStyle );
        graphics.popContext();

        Field leafFieldWithFocus = getLeafFieldWithFocus();
        boolean drawFocus = ( leafFieldWithFocus != null );
        GraphicsUtility.paintBorder( graphics, 0, 0, width, height, drawFocus, computedStyle );
    }
}
