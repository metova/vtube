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
package com.metova.vtube.service.player;

import java.io.IOException;

import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

import m.org.apache.log4j.Logger;

import org.metova.bb.widgets.media.player.DefaultMediaPlayer;
import org.metova.mobile.rt.net.NetworkPathUtils;

import com.metova.vtube.model.Preferences;
import com.metova.vtube.service.persistence.PreferenceStore;
import com.metova.vtube.service.video.Videos;

public class MediaPlayer extends DefaultMediaPlayer {

    private static final Logger log = Logger.getLogger( MediaPlayer.class );

    public MediaPlayer() {

        Preferences preferences = PreferenceStore.instance().load();
        setVolume( preferences.getVolume() );
    }

    protected Player createPlayer( String url ) throws MediaException, IOException {

        boolean forceWap = Videos.isForceWap();

        if ( forceWap ) {

            url += NetworkPathUtils.getWapParameters( false );
        }
        else {

            url += NetworkPathUtils.getStreamingParameters( false );
        }

        log.info( "Creating player: url[" + url + "] forceWap[" + forceWap + "]" );
        return javax.microedition.media.Manager.createPlayer( url );
    }

    public int adjustVolume( int amount ) {

        int volume = super.adjustVolume( amount );

        Preferences preferences = PreferenceStore.instance().load();
        preferences.setVolume( volume );
        PreferenceStore.instance().commit();

        return volume;
    }
}
