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
package com.metova.vtube.widgets.media.carousel;

import org.metova.mobile.util.timer.CreeperTask;

import com.metova.vtube.service.video.thumbnail.Thumbnails;

public abstract class HighlightTimerTask extends CreeperTask {

    private int amount;
    private int value;
    private int increment;

    private boolean quiteCancel;

    public HighlightTimerTask(int amount, long period) {

        super( period );

        setIncrement( Thumbnails.getCarouselIncrement() );
        setValue( Thumbnails.instance().getThumbnailWidth() );
        setAmount( amount );
    }

    protected abstract void loudCancel();

    public void onCancel() {

        if ( !isQuiteCancel() ) {

            loudCancel();
        }
    }

    protected int getEndValue() {

        return Thumbnails.instance().getOriginalWidth();
    }

    protected int getValue() {

        return value;
    }

    protected void setValue( int value ) {

        this.value = value;
    }

    public int getAmount() {

        return amount;
    }

    private void setAmount( int amount ) {

        this.amount = amount;
    }

    protected int getIncrement() {

        return increment;
    }

    private void setIncrement( int increment ) {

        this.increment = increment;
    }

    boolean isQuiteCancel() {

        return quiteCancel;
    }

    void setQuiteCancel( boolean quiteCancel ) {

        this.quiteCancel = quiteCancel;
    }
}
