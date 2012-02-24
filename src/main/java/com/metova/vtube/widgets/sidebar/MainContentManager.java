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

import net.rim.device.api.system.Characters;

import org.metova.bb.widgets.Screens;
import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;
import org.metova.mobile.util.timer.Timers;

import com.metova.vtube.model.feed.Feed;
import com.metova.vtube.service.feed.FeedStack;
import com.metova.vtube.widgets.feed.FeedPopupScreen;
import com.metova.vtube.widgets.manager.TipManager;
import com.metova.vtube.widgets.page.PageDisplay;

public class MainContentManager extends AbstractVerticalFieldManager {

    private PageDisplay pageDisplay;
    private TipManager tipManager;

    protected void onInitialize() {

        setPageDisplay( new PageDisplay() );
        setTipManager( new MyTipManager() );
    }

    protected void onLoading() {

        Feed feed = FeedStack.currentPage();
        if ( feed == null || FeedStack.isFeedEmpty( feed ) ) {

            add( getTipManager() );
        }
        else {

            add( getPageDisplay() );
        }
    }

    protected boolean keyChar( char c, int status, int time ) {

        switch (c) {

            case Characters.LATIN_CAPITAL_LETTER_C:
            case Characters.LATIN_SMALL_LETTER_C: {

                Screens.pushScreen( new FeedPopupScreen() );

                return true;
            }

            case Characters.LATIN_CAPITAL_LETTER_N:
            case Characters.LATIN_SMALL_LETTER_N: {

                Timers.runNow( new Runnable() {

                    public void run() {

                        FeedStack.nextPage();
                    }
                } );

                return true;
            }

            case Characters.LATIN_CAPITAL_LETTER_P:
            case Characters.LATIN_SMALL_LETTER_P: {

                Timers.runNow( new Runnable() {

                    public void run() {

                        FeedStack.previousPage();
                    }
                } );

                return true;
            }

            case Characters.LATIN_CAPITAL_LETTER_R:
            case Characters.LATIN_SMALL_LETTER_R: {

                boolean search = FeedStack.isSearch();
                Feed currentPage = FeedStack.currentPage();
                if ( !search && currentPage != null ) {

                    Timers.runNow( new Runnable() {

                        public void run() {

                            FeedStack.reloadFeed();
                        }
                    } );
                }

                return true;
            }
        }

        return super.keyChar( c, status, time );
    }

    private class MyTipManager extends TipManager {

        private LabelField zeroResultsField;

        protected void onInitialize() {

            LabelField zeroResultsField = new LabelField( "0 results were returned", USE_ALL_WIDTH );
            zeroResultsField.getStyleManager().setStyleClass( "h3" );
            setZeroResultsField( zeroResultsField );

            super.onInitialize();
        }

        protected void onLoading() {

            add( new NullLabelField( 0, 10 ) );

            Feed feed = FeedStack.currentPage();
            if ( feed != null && FeedStack.isFeedEmpty( feed ) ) {

                add( getZeroResultsField() );
                add( new NullLabelField( 0, 10 ) );
            }

            super.onLoading();
        }

        private LabelField getZeroResultsField() {

            return zeroResultsField;
        }

        private void setZeroResultsField( LabelField zeroResultsField ) {

            this.zeroResultsField = zeroResultsField;
        }
    }

    public PageDisplay getPageDisplay() {

        return pageDisplay;
    }

    private void setPageDisplay( PageDisplay pageDisplay ) {

        this.pageDisplay = pageDisplay;
    }

    private TipManager getTipManager() {

        return tipManager;
    }

    private void setTipManager( TipManager tipManager ) {

        this.tipManager = tipManager;
    }
}
