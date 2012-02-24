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

import net.rim.device.api.ui.XYRect;

import org.metova.bb.widgets.field.image.AnimatedField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;

public class LoadingAnimationManager extends AbstractVerticalFieldManager {

    private AnimatedField animatedField;

    public LoadingAnimationManager() {

        super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
    }

    protected void onInitialize() {

        AnimatedField animatedField = new AnimatedField();
        animatedField.getStyleManager().setStyleClass( "Media-load-animation" );
        setAnimatedField( animatedField );
    }

    protected void onLoading() {

        add( new NullLabelField( 0, 20 ) );
        add( getAnimatedField() );
        add( new NullLabelField( 0, 20 ) );
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();

        AnimatedField animatedField = getAnimatedField();
        if ( animatedField != null && equals( animatedField.getManager() ) ) {

            XYRect extent = animatedField.getExtent();

            int fieldWidth = animatedField.getWidth();
            int fieldX = (int) ( ( width - fieldWidth ) * 0.50 );

            setPositionChild( animatedField, fieldX, extent.y );
        }
    }

    private AnimatedField getAnimatedField() {

        return animatedField;
    }

    private void setAnimatedField( AnimatedField animatedField ) {

        this.animatedField = animatedField;
    }
}
