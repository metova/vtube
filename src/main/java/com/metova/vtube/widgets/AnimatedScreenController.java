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
package com.metova.vtube.widgets;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.container.animation.AbstractAnimationContainer;
import org.metova.bb.widgets.container.animation.ScreenFadeAnimation;

import com.metova.vtube.Settings;

public class AnimatedScreenController extends org.metova.bb.widgets.container.animation.AnimatedScreenController {

    protected AbstractAnimationContainer show( Bitmap currentContainerBitmap, Bitmap nextContainerBitmap, boolean push ) {

        int screenWidth = Graphics.getScreenWidth();
        int screenHeight = Graphics.getScreenHeight();

        ScreenFadeAnimation screenFadeAnimation = new ScreenFadeAnimation( Settings.FADE_SPEED );
        screenFadeAnimation.show( screenWidth, screenHeight, currentContainerBitmap, nextContainerBitmap );

        return screenFadeAnimation;
    }
}
