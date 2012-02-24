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

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.UiApplication;

import org.metova.bb.theme.precision.choice.ChoiceSection;
import org.metova.bb.widgets.field.choice.ChoiceField;
import org.metova.bb.widgets.field.choice.VectorChoiceField;
import org.metova.bb.widgets.field.label.AbstractActionField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.mobile.event.Event;
import org.metova.mobile.event.EventListener;

import com.metova.vtube.event.PreferenceUpdateEvent;
import com.metova.vtube.service.Parameters;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.widgets.choice.CustomChoiceField;

public class SearchManager extends AbstractVerticalFieldManager {

    private SearchInputBox searchInputBox;
    private ChoiceSection choiceSection;

    public SearchManager() {

        super( NO_VERTICAL_SCROLL );

        addEventListener( new PreferenceUpdateEventListener(), PreferenceUpdateEvent.class );
    }

    protected void onInitialize() {

        setSearchInputBox( new MySearchInputBox() );

        String orderById = PreferenceStore.instance().load().getOrderBy();
        String orderByName = Parameters.getOrderByName( orderById );
        String[] orderByOptions = Parameters.getOrderByOptions( true );
        ChoiceField orderByField = new CustomChoiceField( orderByOptions, orderByName );
        setChoiceSection( new ChoiceSection( "Order By:", orderByField ) );
    }

    protected void onLoading() {

        add( getSearchInputBox() );
        add( getChoiceSection() );
    }

    protected boolean keyChar( char key, int status, int time ) {

        switch (key) {

            case Characters.ENTER: {

                SearchInputBox searchInputBox = getSearchInputBox();
                AbstractActionField searchGoButton = searchInputBox.getSearchGoButton();
                searchGoButton.action();

                return true;
            }
        }

        return super.keyChar( key, status, time );
    }

    private class MySearchInputBox extends SearchInputBox {

        protected String getOrderBy() {

            ChoiceSection choiceSection = getChoiceSection();
            VectorChoiceField arrayChoiceField = (VectorChoiceField) choiceSection.getChoiceField();
            return (String) arrayChoiceField.getSelectedChoice();
        }
    }

    private class PreferenceUpdateEventListener implements EventListener {

        public void onEvent( Event event ) {

            UiApplication.getUiApplication().invokeLater( new Runnable() {

                public void run() {

                    String orderById = PreferenceStore.instance().load().getOrderBy();
                    String orderByName = Parameters.getOrderByName( orderById );

                    ChoiceSection choiceSection = getChoiceSection();
                    ChoiceField choiceField = choiceSection.getChoiceField();
                    choiceField.setSelectedIndex( orderByName );
                }
            } );
        }
    }

    private SearchInputBox getSearchInputBox() {

        return searchInputBox;
    }

    private void setSearchInputBox( SearchInputBox searchInputBox ) {

        this.searchInputBox = searchInputBox;
    }

    public ChoiceSection getChoiceSection() {

        return choiceSection;
    }

    private void setChoiceSection( ChoiceSection choiceSection ) {

        this.choiceSection = choiceSection;
    }
}
