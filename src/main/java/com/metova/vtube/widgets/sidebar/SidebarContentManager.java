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

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.metova.bb.widgets.field.HorizontalRule;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.bb.widgets.theme.StyleManager;
import org.metova.bb.widgets.theme.StyledField;

import com.metova.vtube.widgets.search.SearchManager;

public class SidebarContentManager extends AbstractVerticalFieldManager implements StyledField {

    private StyleManager styleManager;

    private SearchManager searchManager;
    private SidebarButtonManager sidebarButtonManager;

    public SidebarContentManager() {

        super( NO_VERTICAL_SCROLL );
    }

    protected void onInitialize() {

        setSearchManager( new SearchManager() );
        setSidebarButtonManager( new SidebarButtonManager() );
    }

    protected void onLoading() {

        add( getSearchManager() );
        add( new NullLabelField( 0, SidebarField.COLLAPSED_PADDING_HEIGHT - 5 ) );

        HorizontalRule horizontalRule = new HorizontalRule();
        horizontalRule.getStyleManager().setStyleClass( "HorizontalRule-sidebar" );
        add( horizontalRule );

        add( new NullLabelField( 0, SidebarField.COLLAPSED_PADDING_HEIGHT ) );
        add( getSidebarButtonManager() );
        add( new NullLabelField( 0, SidebarField.COLLAPSED_HEIGHT ) );
    }

    public VerticalFieldManager getMainContentManager() {

        return SidebarContentManager.this.getMainContentManager();
    }

    public Font getFont() {

        return getStyleManager().getComputedStyle().getFont();
    }

    public void applyComputedStyle() {

        // do nothing
    }

    public StyleManager getStyleManager() {

        if ( styleManager == null ) {

            setStyleManager( new StyleManager( this, "SidebarManager" ) );
        }

        return styleManager;
    }

    public void setStyleManager( StyleManager styleManager ) {

        this.styleManager = styleManager;
    }

    private SearchManager getSearchManager() {

        return searchManager;
    }

    private void setSearchManager( SearchManager searchManager ) {

        this.searchManager = searchManager;
    }

    private SidebarButtonManager getSidebarButtonManager() {

        return sidebarButtonManager;
    }

    private void setSidebarButtonManager( SidebarButtonManager sidebarButtonManager ) {

        this.sidebarButtonManager = sidebarButtonManager;
    }
}
