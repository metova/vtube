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
package com.metova.vtube.ui;

import net.rim.device.api.ui.component.Menu;

import org.metova.bb.theme.precision.choice.ChoiceSection;
import org.metova.bb.widgets.field.HorizontalRule;
import org.metova.bb.widgets.field.choice.ChoiceField;
import org.metova.bb.widgets.field.choice.VectorChoiceField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.menu.Menus;
import org.metova.mobile.event.dispatcher.EventDispatcher;

import com.metova.vtube.Application;
import com.metova.vtube.Settings;
import com.metova.vtube.event.PreferenceUpdateEvent;
import com.metova.vtube.model.Preferences;
import com.metova.vtube.service.Parameters;
import com.metova.vtube.service.feed.Feeds;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.service.video.FormatTypes;
import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.choice.CustomChoiceField;

public class PreferencesContainer extends AbstractLicensedContainer {

    private ChoiceSection formatType;
    private ChoiceSection resultsPerPage;
    private ChoiceSection orderBy;
    private ChoiceSection defaultFeed;
    private ChoiceSection animatedScreen;
    private ChoiceSection reducedFormat;

    public PreferencesContainer() {

        setTitle( "Preferences" );

        setSavePromptEnabled( true );

        setContentManagerStyle( USE_ALL_HEIGHT | VERTICAL_SCROLL );
    }

    protected boolean onSave() {

        Preferences preferences = PreferenceStore.instance().load();

        VectorChoiceField formatType = (VectorChoiceField) getFormatType().getChoiceField();
        String formatTypeName = (String) formatType.getSelectedChoice();
        preferences.setFormatType( FormatTypes.getFormatTypeId( formatTypeName ) );

        VectorChoiceField resultsPerPage = (VectorChoiceField) getResultsPerPage().getChoiceField();
        preferences.setResultsPerPage( (String) resultsPerPage.getSelectedChoice() );

        VectorChoiceField orderBy = (VectorChoiceField) getOrderBy().getChoiceField();
        String orderByName = (String) orderBy.getSelectedChoice();
        preferences.setOrderBy( Parameters.getOrderById( orderByName ) );

        VectorChoiceField defaultFeed = (VectorChoiceField) getDefaultFeed().getChoiceField();
        String feedName = (String) defaultFeed.getSelectedChoice();
        preferences.setDefaultFeed( Parameters.getFeedId( feedName ) );

        VectorChoiceField animatedScreenField = (VectorChoiceField) getAnimatedScreen().getChoiceField();
        String animatedScreenName = (String) animatedScreenField.getSelectedChoice();
        boolean animatedScreen = Parameters.getEnableId( animatedScreenName );
        boolean resetScreenController = preferences.useAnimatedScreen() != animatedScreen;
        preferences.setAnimatedScreen( animatedScreen );

        VectorChoiceField reducedFormat = (VectorChoiceField) getReducedFormat().getChoiceField();
        String reducedFormatName = (String) reducedFormat.getSelectedChoice();
        preferences.setReducedFormat( Parameters.getEnableId( reducedFormatName ) );

        PreferenceStore.instance().commit();

        if ( resetScreenController ) {

            Application.resetScreenController();
        }

        EventDispatcher.instance().fireEvent( new PreferenceUpdateEvent() );

        return super.onSave();
    }

    protected void onInitializeContent() {

        Preferences preferences = PreferenceStore.instance().load();

        String feedId = preferences.getDefaultFeed();
        String feedName = Parameters.getFeedName( feedId );
        String[] feedOptions = Parameters.getFeedOptions( true );
        ChoiceField feedChoiceField = new CustomChoiceField( feedOptions, feedName );
        setDefaultFeed( new ChoiceSection( Settings.OPTION_DEFAULT_FEED + ":", feedChoiceField ) );

        String formatTypeId = preferences.getFormatType();
        String formatTypeName = FormatTypes.getFormatTypeName( formatTypeId );
        String[] formatTypeOptions = FormatTypes.getFormatTypeOptions( true );
        ChoiceField formatTypeField = new CustomChoiceField( formatTypeOptions, formatTypeName );
        setFormatType( new ChoiceSection( Settings.OPTION_FORMAT_TYPE + ":", formatTypeField ) );

        boolean reducedFormat = preferences.useReducedFormat();
        String reducedFormatName = Parameters.getEnableName( reducedFormat );
        String[] reducedFormatOptions = Parameters.getEnableOptions( true );
        ChoiceField reducedFormatField = new CustomChoiceField( reducedFormatOptions, reducedFormatName );
        setReducedFormat( new ChoiceSection( Settings.OPTION_REDUCE_FORMAT + ":", reducedFormatField ) );

        String resultsPerPage = preferences.getResultsPerPage();
        String[] resultsPerPageOptions = Feeds.getResultsPerPage();
        ChoiceField resultsPerPageField = new CustomChoiceField( resultsPerPageOptions, resultsPerPage );
        setResultsPerPage( new ChoiceSection( Settings.OPTION_RESULTS_PER_PAGE + ":", resultsPerPageField ) );

        String orderById = preferences.getOrderBy();
        String orderByName = Parameters.getOrderByName( orderById );
        String[] orderByOptions = Parameters.getOrderByOptions( true );
        ChoiceField orderByField = new CustomChoiceField( orderByOptions, orderByName );
        setOrderBy( new ChoiceSection( Settings.OPTION_ORDER_BY + ":", orderByField ) );

        boolean animatedScreen = preferences.useAnimatedScreen();
        String animatedScreenName = Parameters.getEnableName( animatedScreen );
        String[] animatedScreenOptions = Parameters.getEnableOptions( true );
        ChoiceField animatedScreenField = new CustomChoiceField( animatedScreenOptions, animatedScreenName );
        setAnimatedScreen( new ChoiceSection( Settings.OPTION_SCREEN_TRANSITIONS + ":", animatedScreenField ) );
    }

    protected void addContentFields() {

        add( getDefaultFeed() );
        add( getResultsPerPage() );
        add( getOrderBy() );

        add( new NullLabelField( 0, 5 ) );
        add( new HorizontalRule() );
        add( new NullLabelField( 0, 5 ) );

        add( getFormatType() );
        add( getReducedFormat() );

        add( new NullLabelField( 0, 5 ) );
        add( new HorizontalRule() );
        add( new NullLabelField( 0, 5 ) );

        add( getAnimatedScreen() );
    }

    protected void makeMenu( Menu menu, int instance ) {

        menu.add( Menus.newMenuItem( "Save", new SaveAction(), Menus.ORDINAL_L1, 1 ) );

        menu.add( Menus.newMenuItemPushScreen( "View Descriptions", new PreferenceDetailsContainer(), Menus.ORDINAL_L2, 1 ) );

        super.makeMenu( menu, instance );
    }

    private class SaveAction implements Runnable {

        public void run() {

            onSave();
            setDirty( false );
        }
    }

    private ChoiceSection getFormatType() {

        return formatType;
    }

    private void setFormatType( ChoiceSection formatType ) {

        this.formatType = formatType;
    }

    private ChoiceSection getResultsPerPage() {

        return resultsPerPage;
    }

    private void setResultsPerPage( ChoiceSection resultsPerPage ) {

        this.resultsPerPage = resultsPerPage;
    }

    public ChoiceSection getOrderBy() {

        return orderBy;
    }

    public void setOrderBy( ChoiceSection orderBy ) {

        this.orderBy = orderBy;
    }

    private ChoiceSection getDefaultFeed() {

        return defaultFeed;
    }

    private void setDefaultFeed( ChoiceSection defaultFeed ) {

        this.defaultFeed = defaultFeed;
    }

    private ChoiceSection getAnimatedScreen() {

        return animatedScreen;
    }

    private void setAnimatedScreen( ChoiceSection animatedScreen ) {

        this.animatedScreen = animatedScreen;
    }

    private ChoiceSection getReducedFormat() {

        return reducedFormat;
    }

    private void setReducedFormat( ChoiceSection reducedFormat ) {

        this.reducedFormat = reducedFormat;
    }
}
