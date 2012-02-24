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
package com.metova.vtube.widgets.sidebar;

import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.widgets.action.ScreenNavigationAction;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;

import com.metova.vtube.widgets.feed.FeedPopupScreen;
import com.metova.vtube.widgets.page.next.NextPageButtonField;
import com.metova.vtube.widgets.page.previous.PreviousPageButtonField;

class SidebarButtonManager extends AbstractHorizontalFieldManager {

    private ButtonField feedButton;
    private ButtonField previousPageButton;
    private ButtonField nextPageButton;

    public SidebarButtonManager() {

        super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
    }

    protected void initializeFields() {

        setPreviousPageButton( new PreviousPageButtonField() );
        setFeedButton( new SidebarButtonField( "Load Feed", new ScreenNavigationAction( new FeedPopupScreen() ) ) );
        setNextPageButton( new NextPageButtonField() );
    }

    protected void addFields() {

        add( getPreviousPageButton() );
        add( getFeedButton() );
        add( getNextPageButton() );
    }

    protected int firstFocus( int arg0 ) {

        ButtonField feedButton = getFeedButton();
        return feedButton.getIndex();
    }

    protected void onSublayout( int width, int height ) {

        super.onSublayout( width, height );

        width = getWidth();
        height = getHeight();

        ButtonField feedButton = getFeedButton();
        if ( feedButton != null && equals( feedButton.getManager() ) ) {

            int fieldWidth = feedButton.getWidth();
            int fieldHeight = feedButton.getHeight();

            int fieldX = (int) ( ( width - fieldWidth ) * 0.50 );
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );

            setPositionChild( feedButton, fieldX, fieldY );
        }

        ButtonField nextPageButton = getNextPageButton();
        if ( nextPageButton != null && equals( nextPageButton.getManager() ) ) {

            int fieldWidth = nextPageButton.getWidth();
            int fieldHeight = nextPageButton.getHeight();

            int fieldX = (int) ( width - fieldWidth );
            int fieldY = (int) ( ( height - fieldHeight ) * 0.50 );

            setPositionChild( nextPageButton, fieldX, fieldY );
        }
    }

    private ButtonField getFeedButton() {

        return feedButton;
    }

    private void setFeedButton( ButtonField feedButton ) {

        this.feedButton = feedButton;
    }

    private ButtonField getPreviousPageButton() {

        return previousPageButton;
    }

    private void setPreviousPageButton( ButtonField previousPageButton ) {

        this.previousPageButton = previousPageButton;
    }

    private ButtonField getNextPageButton() {

        return nextPageButton;
    }

    private void setNextPageButton( ButtonField nextPageButton ) {

        this.nextPageButton = nextPageButton;
    }
}
