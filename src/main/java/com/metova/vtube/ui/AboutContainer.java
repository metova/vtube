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

import org.metova.bb.widgets.field.HorizontalRule;
import org.metova.bb.widgets.field.label.LabelField;
import org.metova.bb.widgets.field.label.NullLabelField;
import org.metova.bb.widgets.field.label.ScreenNavigationField;
import org.metova.bb.widgets.field.label.WebNavigationField;
import org.metova.mobile.rt.system.MobileApplication;

import com.metova.vtube.widgets.AbstractLicensedContainer;
import com.metova.vtube.widgets.logo.PoweredByMetova;
import com.metova.vtube.widgets.logo.PoweredByYouTube;

public class AboutContainer extends AbstractLicensedContainer {

    private PoweredByMetova poweredByMetova;
    private WebNavigationField metovaWebsite;
    private WebNavigationField visionWebsite;
    private ScreenNavigationField feedbackActionField;

    private PoweredByYouTube poweredByYouTube;
    private WebNavigationField youTubeWebsite;

    private LabelField versionField;

    public AboutContainer() {

        setTitle( "About" );
    }

    protected void onInitializeContent() {

        setPoweredByMetova( new PoweredByMetova() );

        String metovaWebsite = "http://www.metova.com";
        setMetovaWebsite( new WebNavigationField( metovaWebsite, metovaWebsite ) );

        String visionWebsite = "http://vision.metova.com";
        setVisionWebsite( new WebNavigationField( visionWebsite, visionWebsite ) );

        // Submit Feedback removed per http://jira.metova.com/browse/TUBE-1467
        //        setFeedbackActionField( new ScreenNavigationField( "Submit Feedback", new ProblemReportPopupScreen() ) );

        setPoweredByYouTube( new PoweredByYouTube() );

        String youTubeWebsite = "http://www.youtube.com";
        setYouTubeWebsite( new WebNavigationField( youTubeWebsite, youTubeWebsite ) );
    }

    protected void onInitializeFooter() {

        super.onInitializeFooter();

        setVersionField( new LabelField( "Version: " + MobileApplication.instance().getVersion().toString() ) );
    }

    protected void addContentFields() {

        add( new NullLabelField( 0, 10 ) );

        add( getPoweredByMetova() );
        add( getMetovaWebsite() );
        add( getVisionWebsite() );

        // Submit Feedback removed per http://jira.metova.com/browse/TUBE-1467
        //        add( getFeedbackActionField() );

        add( new NullLabelField( 0, 10 ) );
        add( new HorizontalRule() );
        add( new NullLabelField( 0, 10 ) );

        add( getPoweredByYouTube() );
        add( getYouTubeWebsite() );
    }

    protected void addFooterFields() {

        super.addFooterFields();

        getFooterManager().add( new NullLabelField( 0, 10 ) );
        getFooterManager().add( new HorizontalRule() );
        getFooterManager().add( new NullLabelField( 0, 10 ) );

        getFooterManager().add( getVersionField() );

        getFooterManager().add( new NullLabelField( 0, 10 ) );
    }

    private LabelField getVersionField() {

        return versionField;
    }

    private void setVersionField( LabelField versionField ) {

        this.versionField = versionField;
    }

    private WebNavigationField getMetovaWebsite() {

        return metovaWebsite;
    }

    private void setMetovaWebsite( WebNavigationField metovaWebsite ) {

        this.metovaWebsite = metovaWebsite;
    }

    private PoweredByMetova getPoweredByMetova() {

        return poweredByMetova;
    }

    private void setPoweredByMetova( PoweredByMetova poweredByMetova ) {

        this.poweredByMetova = poweredByMetova;
    }

    private PoweredByYouTube getPoweredByYouTube() {

        return poweredByYouTube;
    }

    private void setPoweredByYouTube( PoweredByYouTube poweredByYouTube ) {

        this.poweredByYouTube = poweredByYouTube;
    }

    private WebNavigationField getYouTubeWebsite() {

        return youTubeWebsite;
    }

    private void setYouTubeWebsite( WebNavigationField youTubeWebsite ) {

        this.youTubeWebsite = youTubeWebsite;
    }

    private ScreenNavigationField getFeedbackActionField() {

        return feedbackActionField;
    }

    private void setFeedbackActionField( ScreenNavigationField feedbackActionField ) {

        this.feedbackActionField = feedbackActionField;
    }

    private WebNavigationField getVisionWebsite() {

        return visionWebsite;
    }

    private void setVisionWebsite( WebNavigationField visionWebsite ) {

        this.visionWebsite = visionWebsite;
    }
}
