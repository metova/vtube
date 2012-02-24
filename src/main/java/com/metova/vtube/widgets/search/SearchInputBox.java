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
package com.metova.vtube.widgets.search;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Menu;

import org.apache.commons.threadpool.ThreadPoolDelegatingRunnable;
import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.theme.precision.input.box.InputBoxField;
import org.metova.bb.theme.precision.input.box.TextBoxField;
import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.managed.AbstractHorizontalFieldManager;
import org.metova.bb.widgets.menu.Menus;
import org.metova.bb.widgets.screen.sidebar.CloseSidebarEvent;
import org.metova.mobile.event.dispatcher.EventDispatcher;
import org.metova.mobile.persistence.PersistableVector;
import org.metova.mobile.util.text.Text;

import com.metova.vtube.service.feed.Feeds;
import com.metova.vtube.service.persistence.SearchHistoryStore;

public abstract class SearchInputBox extends AbstractHorizontalFieldManager {

    private InputBoxField inputBoxField;
    private ButtonField searchGoButton;

    public SearchInputBox() {

        super( USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL );
    }

    protected abstract String getOrderBy();

    protected void initializeFields() {

        setInputBoxField( new MyInputBoxField() );

        ButtonField buttonField = new ButtonField( "Go", new PerformSearch() );
        buttonField.getStyleManager().setStyleClass( "ButtonField-searchGo" );
        setSearchGoButton( buttonField );
    }

    protected void addFields() {

        add( getInputBoxField() );
        add( getSearchGoButton() );
    }

    protected int firstFocus( int arg0 ) {

        InputBoxField inputBoxField = getInputBoxField();
        return inputBoxField.getIndex();
    }

    public String getText() {

        String result = null;

        InputBoxField inputBoxField = getInputBoxField();
        if ( inputBoxField != null ) {

            BasicEditField basicEditField = inputBoxField.getBasicEditField();
            result = basicEditField.getText();
        }

        return result;
    }

    protected void onSublayout( int width, int height ) {

        int searchGoWidth = 0;
        int searchGoHeight = 0;

        int inputBoxHeight = 0;

        ButtonField searchGoButton = getSearchGoButton();
        if ( searchGoButton != null && equals( searchGoButton.getManager() ) ) {

            searchGoWidth = searchGoButton.getPreferredWidth();
            searchGoHeight = searchGoButton.getPreferredHeight();

            layoutChild( searchGoButton, searchGoWidth, searchGoHeight );
        }

        InputBoxField inputBoxField = getInputBoxField();
        if ( inputBoxField != null && equals( inputBoxField.getManager() ) ) {

            int fieldWidth = width - searchGoWidth;
            inputBoxHeight = inputBoxField.getPreferredHeight();

            setPositionChild( inputBoxField, 0, 0 );
            layoutChild( inputBoxField, fieldWidth, inputBoxHeight );
        }

        height = Math.max( inputBoxHeight, searchGoHeight );
        setExtent( width, height );

        if ( searchGoButton != null && equals( searchGoButton.getManager() ) ) {

            int x = width - searchGoWidth;
            int y = (int) ( ( height - searchGoHeight ) * 0.50 );
            setPositionChild( searchGoButton, x, y );
        }
    }

    private class PerformSearch extends ThreadPoolDelegatingRunnable {

        protected void onRun() {

            BasicEditField basicEditField = getInputBoxField().getBasicEditField();

            final String text = basicEditField.getText();
            if ( !Text.isNull( text ) ) {

                EventDispatcher.instance().fireEvent( new CloseSidebarEvent() );

                synchronized (UiApplication.getEventLock()) {

                    basicEditField.setText( null );
                }

                String orderBy = getOrderBy();
                Feeds.search( text, orderBy );
            }
        }
    }

    private class MyInputBoxField extends InputBoxField {

        public MyInputBoxField() {

            super( "Search:", new SearchTypeAheadField() );
        }

        protected void initializeFields() {

            super.initializeFields();

            LabelField labelField = getLabelField();
            labelField.getStyleManager().setStyleClass( "h3" );

            TextBoxField textBoxField = getTextBoxField();
            textBoxField.getStyleManager().setStyleClass( "TextBoxField-typeahead" );
        }

        protected void makeMenu( Menu menu, int instance ) {

            PersistableVector searchHistoryStore = SearchHistoryStore.instance().load();
            if ( !searchHistoryStore.isEmpty() ) {

                menu.add( Menus.newMenuItem( "Clear Search History", new ThreadPoolDelegatingRunnable() {

                    protected void onRun() {

                        PersistableVector searchHistoryStore = SearchHistoryStore.instance().load();
                        searchHistoryStore.removeAllElements();
                        SearchHistoryStore.instance().commit();
                    }
                }, Menus.ORDINAL_L1, 1 ) );
            }

            super.makeMenu( menu, instance );
        }
    }

    public InputBoxField getInputBoxField() {

        return inputBoxField;
    }

    private void setInputBoxField( InputBoxField inputBoxField ) {

        this.inputBoxField = inputBoxField;
    }

    public ButtonField getSearchGoButton() {

        return searchGoButton;
    }

    private void setSearchGoButton( ButtonField searchGoButton ) {

        this.searchGoButton = searchGoButton;
    }
}
