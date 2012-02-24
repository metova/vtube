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
package com.metova.vtube.widgets.manager;

import m.org.apache.log4j.Logger;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.XYRect;

import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.indicator.BatteryStatusField;
import org.metova.bb.widgets.indicator.ClockField;
import org.metova.bb.widgets.indicator.SignalStatusField;
import org.metova.bb.widgets.theme.StyleManager;
import org.metova.mobile.util.coordinate.Edges;

import com.metova.vtube.widgets.signal.WifiSignalStatusField;

public abstract class IndicatorHeaderManager extends org.metova.bb.theme.precision.container.IndicatorHeaderManager {

    private static final Logger log = Logger.getLogger( IndicatorHeaderManager.class );

    private LabelField titleField;
    private String title;

    public IndicatorHeaderManager(String title) {

        super( USE_ALL_HEIGHT );

        setTitle( title );
    }

    protected abstract boolean displayWiFiLogo();

    protected void initializeFields() {

        super.initializeFields();

        String title = getTitle();
        LabelField titleField = new LabelField( title, DrawStyle.ELLIPSIS );
        titleField.getStyleManager().setStyleClass( "Media-header-title" );
        setTitleField( titleField );
    }

    protected void initializeSignalField() {

        StyleManager styleManager = getStyleManager();

        String signalWifi = styleManager.getImagePath( "signal-wifi" );
        String signalSmallDefault = styleManager.getImagePath( "signalSmall-default" );
        String signalSmallDead = styleManager.getImagePath( "signalSmall-dead" );
        setSignalStatusField( new MyWifiSignalStatusField( signalWifi, signalSmallDefault, signalSmallDead, new int[] { 22, 29, 35, 41, 47, 53 } ) );
    }

    protected void addFields() {

        super.addFields();

        add( getTitleField() );
    }

    protected void onExposed() {

        super.onExposed();

        invalidate();
    }

    public void updateTitle( String title ) {

        LabelField titleField = getTitleField();
        titleField.setText( title );

        invalidate();
    }

    protected void onSublayout( int width, int height ) {

        height = getPreferredHeight();
        super.onSublayout( width, height );

        width = getWidth();

        Edges padding = getStyleManager().getComputedStyle().getPadding();

        int x = 0;
        int signalWidth = 0;
        int batteryStatusFieldHeight = 0;

        BatteryStatusField batteryStatusField = getBatteryStatusField();
        if ( batteryStatusField != null && equals( batteryStatusField.getManager() ) ) {

            XYRect extent = batteryStatusField.getExtent();
            x = extent.x + batteryStatusField.getWidth();

            batteryStatusFieldHeight = batteryStatusField.getHeight();
        }

        ClockField clockField = getClockField();
        if ( clockField != null && equals( clockField.getManager() ) ) {

            XYRect extent = clockField.getExtent();

            x += padding.getLeft();

            setPositionChild( clockField, x, extent.y );

            x += clockField.getWidth();
        }

        SignalStatusField signalStatusField = getSignalStatusField();
        if ( signalStatusField != null && equals( signalStatusField.getManager() ) ) {

            signalWidth += signalStatusField.getWidth();
        }

        LabelField titleField = getTitleField();
        if ( titleField != null && equals( titleField.getManager() ) ) {

            int fieldWidth = width - x - signalWidth - padding.getRight();
            int fieldHeight = titleField.getHeight();

            Font labelFont = titleField.getFont();
            int labelBaseline = titleField.getHeight() - ( labelFont.getHeight() - labelFont.getBaseline() );

            int y = (int) ( ( batteryStatusFieldHeight - labelBaseline ) * 0.50 );

            layoutChild( titleField, fieldWidth, fieldHeight );
            setPositionChild( titleField, x, y + padding.getTop() );
        }
    }

    private class MyWifiSignalStatusField extends WifiSignalStatusField {

        public MyWifiSignalStatusField(String wifiImagePath, String signalImagePath, String deadSignalImagePath, int[] barSizes) {

            super( wifiImagePath, signalImagePath, deadSignalImagePath, barSizes );
        }

        protected boolean displayWiFiLogo() {

            return IndicatorHeaderManager.this.displayWiFiLogo();
        }
    }

    private LabelField getTitleField() {

        return titleField;
    }

    private void setTitleField( LabelField titleField ) {

        this.titleField = titleField;
    }

    private String getTitle() {

        return title;
    }

    private void setTitle( String title ) {

        this.title = title;
    }
}
