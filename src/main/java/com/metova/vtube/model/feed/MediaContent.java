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

public class MediaContent {

    //"url":"rtsp://rtsp2.youtube.com/CioLENy73wIaIQmelLUqfhzmzRMYESARFEgGUg5nZGF0YV9zdGFuZGFyZAw\u003d/0/0/0/video.3gp",
    //"type":"video/3gpp",
    //"medium":"video",
    //"expression":"full",
    //"duration":"175",
    //"yt$format":"6"

    private String url;
    private String type;
    private int format;

    public String getUrl() {

        return url;
    }

    public void setUrl( String url ) {

        this.url = url;
    }

    public String getType() {

        return type;
    }

    public void setType( String type ) {

        this.type = type;
    }

    public int getFormat() {

        return format;
    }

    public void setFormat( int format ) {

        this.format = format;
    }
}
