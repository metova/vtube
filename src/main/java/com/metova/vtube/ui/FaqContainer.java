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
import org.metova.bb.widgets.field.HorizontalRule;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.menu.Menus;

import com.metova.vtube.widgets.AbstractContainer;
import com.metova.vtube.widgets.faq.FaqManager;

public class FaqContainer extends AbstractContainer {

    private ButtonManager buttonManager;
    private FaqManager faqManager;

    public FaqContainer() {

        setTitle( "FAQ" );
    }

    protected void onInitializeContent() {

        setFaqManager( new FaqManager() );
    }

    protected void addContentFields() {

        add( getFaqManager() );
    }

    protected void onInitializeHeader() {

        super.onInitializeHeader();

        setButtonManager( new ButtonManager() );
    }

    protected void addHeaderFields() {

        super.addHeaderFields();

        Manager headerManager = getHeaderManager();
        headerManager.add( getButtonManager() );
        headerManager.add( new HorizontalRule() );
        headerManager.add( new NullLabelField( 0, 10 ) );
    }

    protected void onContinue() {

        Screens.popScreenLater( FaqContainer.this );
    }

    protected void makeMenu( Menu menu, int instance ) {

        menu.add( Menus.newMenuItem( "Back", new ContinueAction() ) );

        super.makeMenu( menu, instance );
    }

    private class ContinueAction implements Runnable {

        public void run() {

            onContinue();
        }
    }

    private class ButtonManager extends SingleCenteredButtonManager {

        protected void initializeFields() {

            ButtonField buttonField = new ButtonField( "Back", new ContinueAction() );
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

    private void setFaqManager( FaqManager faqManager ) {

        this.faqManager = faqManager;
    }

    private FaqManager getFaqManager() {

        return faqManager;
    }
}
