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
package com.metova.vtube.widgets.popup;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.metova.bb.theme.precision.buttons.ButtonField;
import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.screen.popup.AbstractPopupScreen;

public abstract class AbstractSendPopupScreen extends AbstractPopupScreen {

    private ButtonField emailField;
    private ButtonField smsField;

    public AbstractSendPopupScreen() {

        super( new VerticalFieldManager() );
    }

    protected abstract String getEmailText();

    protected abstract void selectedEmail();

    protected abstract String getSmsText();

    protected abstract void selectedSms();

    protected void onInitialize() {

        setEmailField( new EmailField() );
        setSmsField( new SmsField() );
    }

    protected void onLoading() {

        add( getEmailField() );
        add( getSmsField() );
    }

    protected boolean keyChar( char key, int status, int time ) {

        boolean result = false;

        if ( key == Characters.ESCAPE ) {

            Screens.popScreenLater( this );
            result = true;
        }
        else {

            result = super.keyChar( key, status, time );
        }

        return result;
    }

    private class EmailField extends ButtonField {

        public EmailField() {

            super( getEmailText(), new EmailAction(), FIELD_HCENTER );

            getStyleManager().setStyleClass( "ButtonField-sendFriend" );
        }
    }

    private class EmailAction implements Runnable {

        public void run() {

            Screens.popScreenLater( AbstractSendPopupScreen.this );

            selectedEmail();
        }
    }

    private class SmsField extends ButtonField {

        public SmsField() {

            super( getSmsText(), new SmsAction(), FIELD_HCENTER );

            getStyleManager().setStyleClass( "ButtonField-sendFriend" );
        }
    }

    private class SmsAction implements Runnable {

        public void run() {

            Screens.popScreenLater( AbstractSendPopupScreen.this );

            selectedSms();
        }
    }

    private ButtonField getEmailField() {

        return emailField;
    }

    private void setEmailField( ButtonField emailField ) {

        this.emailField = emailField;
    }

    private ButtonField getSmsField() {

        return smsField;
    }

    private void setSmsField( ButtonField smsField ) {

        this.smsField = smsField;
    }
}
