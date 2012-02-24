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

import java.util.Vector;

import m.java.util.Map;

public class Media {

    //"media$title":{
    //   "type":"plain",
    //   "$t":"The&nbsp;Opener&nbsp;-&nbsp;You&nbsp;Need&nbsp;Direction"
    //},
    //"media$description":{
    //   "type":"plain",
    //   "$t":"http://www.youtube.com/runawaybox\r\nA&nbsp;GPS&nbsp;system&nbsp;tries&nbsp;to&nbsp;save&nbsp;Jim&nbsp;from&nbsp;getting&nbsp;lost,&nbsp;in&nbsp;more&nbsp;ways&nbsp;than&nbsp;one."
    //},
    //"yt$duration":{
    //   "seconds":"175"
    //},
    //"media$category":[
    //],
    //"media$content":[
    //],
    //"media$thumbnail":[
    //]

    private String title;
    private String description;
    private int seconds;
    private Map contents;
    private Vector thumbnails;

    public String getTitle() {

        return title;
    }

    public void setTitle( String title ) {

        this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription( String description ) {

        this.description = description;
    }

    public int getSeconds() {

        return seconds;
    }

    public void setSeconds( int seconds ) {

        this.seconds = seconds;
    }

    public Map getContents() {

        return contents;
    }

    public void setContents( Map contents ) {

        this.contents = contents;
    }

    public Vector getThumbnails() {

        return thumbnails;
    }

    public void setThumbnails( Vector thumbnails ) {

        this.thumbnails = thumbnails;
    }
}
