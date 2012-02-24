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
package com.metova.vtube.widgets.media;

import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;

import com.metova.vtube.widgets.logo.PoweredByYouTube;

public class PoweredByManager extends AbstractHorizontalFieldManager {

    private PoweredByYouTube poweredByYouTube;

    public PoweredByManager() {

        super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
    }

    protected void initializeFields() {

        PoweredByYouTube poweredByYouTube = new PoweredByYouTube();
        poweredByYouTube.getStyleManager().setStyleClass( "PoweredByYouTube-padding-right" );
        setPoweredByYouTube( poweredByYouTube );
    }

    protected void addFields() {

        add( getPoweredByYouTube() );
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();

        PoweredByYouTube poweredByYouTube = getPoweredByYouTube();
        if ( poweredByYouTube != null && equals( poweredByYouTube.getManager() ) ) {

            int fieldWidth = poweredByYouTube.getWidth();

            int fieldX = width - fieldWidth;
            setPositionChild( poweredByYouTube, fieldX, 0 );
        }
    }

    private PoweredByYouTube getPoweredByYouTube() {

        return poweredByYouTube;
    }

    private void setPoweredByYouTube( PoweredByYouTube poweredByYouTube ) {

        this.poweredByYouTube = poweredByYouTube;
    }
}
