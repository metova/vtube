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

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Menu;

import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.theme.precision.buttons.SingleCenteredButtonManager;
import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.menu.Menus;

import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.manager.PreferenceDetailsManager;

public class PreferenceDetailsContainer extends AbstractLicensedContainer {

    private ButtonManager buttonManager;
    private PreferenceDetailsManager preferenceDetailsManager;

    public PreferenceDetailsContainer() {

        setTitle( "Preferences" );
    }

    protected void onInitializeHeader() {

        super.onInitializeHeader();

        setButtonManager( new ButtonManager() );
    }

    protected void onInitializeContent() {

        setPreferenceDetailManager( new PreferenceDetailsManager() );
    }

    protected void addHeaderFields() {

        super.addHeaderFields();

        Manager headerManager = getHeaderManager();
        headerManager.add( getButtonManager() );
    }

    protected void addContentFields() {

        add( getPreferenceDetailManager() );
    }

    private String getContinueString() {

        return "Back";
    }

    protected void makeMenu( Menu menu, int instance ) {

        menu.add( Menus.newMenuItem( getContinueString(), new ContinueAction() ) );

        super.makeMenu( menu, instance );
    }

    private class ContinueAction implements Runnable {

        public void run() {

            Screens.popScreenLater( PreferenceDetailsContainer.this );
        }
    }

    private class ButtonManager extends SingleCenteredButtonManager {

        protected void initializeFields() {

            ButtonField buttonField = new ButtonField( getContinueString(), new ContinueAction() );
            buttonField.getStyleManager().setStyleClass( "ButtonField-tip" );
            setButtonField( buttonField );
        }
    }

    private ButtonManager getButtonManager() {

        return buttonManager;
    }

    private void setButtonManager( ButtonManager buttonManager ) {

        this.buttonManager = buttonManager;
    }

    private PreferenceDetailsManager getPreferenceDetailManager() {

        return preferenceDetailsManager;
    }

    private void setPreferenceDetailManager( PreferenceDetailsManager preferenceDetailsManager ) {

        this.preferenceDetailsManager = preferenceDetailsManager;
    }
}
