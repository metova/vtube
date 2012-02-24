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
package com.metova.vtube.model.feed;

import net.rim.device.api.system.EncodedImage;

import org.metova.mobile.util.cache.ExpiringCache;
import org.metova.mobile.util.cache.ExpiringItemCacheDecorator;
import org.metova.mobile.util.cache.adapter.AsynchronousMapAdapter;

public class MediaThumbnail {

    public static final String ID = "mediaThumbnail.id";

    //"url":"http://i.ytimg.com/vi/zeYcfiq1lJ4/2.jpg",
    //"height":"97",
    //"width":"130",
    //"time":"00:01:27.500"

    private int id;
    private String url;
    private int width;
    private int height;

    private EncodedImage encodedImage;
    private ExpiringCache bitmapCache;

    public String getUrl() {

        return url;
    }

    public void setUrl( String url ) {

        this.url = url;
    }

    public int getWidth() {

        return width;
    }

    public void setWidth( int width ) {

        this.width = width;
    }

    public int getHeight() {

        return height;
    }

    public void setHeight( int height ) {

        this.height = height;
    }

    public EncodedImage getEncodedImage() {

        return encodedImage;
    }

    public void setEncodedImage( EncodedImage encodedImage ) {

        this.encodedImage = encodedImage;
    }

    public int getId() {

        return id;
    }

    public void setId( int id ) {

        this.id = id;
    }

    public ExpiringCache getBitmapCache() {

        if ( bitmapCache == null ) {

            setBitmapCache( new ExpiringItemCacheDecorator( new AsynchronousMapAdapter() ) );
        }

        return bitmapCache;
    }

    public void setBitmapCache( ExpiringCache bitmapCache ) {

        this.bitmapCache = bitmapCache;
    }
}
