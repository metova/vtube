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

public class FeedEntry {

    public static final String ID = "feedEntry.id";

    //"id":{
    //   "$t":"http://gdata.youtube.com/feeds/api/videos/zeYcfiq1lJ4"
    //},
    //"published":{
    //   "$t":"2008-08-20T16:43:28.000Z"
    //},
    //"updated":{
    //   "$t":"2008-12-04T20:31:32.000Z"
    //},
    //"author":[
    //   {
    //      "name":{
    //         "$t":"tewsbox"
    //      },
    //      "uri":{
    //         "$t":"http://gdata.youtube.com/feeds/api/users/tewsbox"
    //      }
    //   }
    //],
    //"media$group":{
    //},
    //"yt$statistics":{
    //   "viewCount":"2600",
    //   "favoriteCount":"14"
    //},
    //"link":[
    //        {
    //           "rel":"alternate",
    //           "type":"text/html",
    //           "href":"http://www.youtube.com/watch?v\u003doHg5SJYRHA0"
    //        },
    //],
    //"gd$rating":{
    //   "min":"1",
    //   "max":"5",
    //   "numRaters":"15",
    //   "average":"5.00"
    //}

    private String id;
    private String authorName;
    private String authorUrl;
    private Media media;
    private int viewCount;
    private int favoriteCount;
    private int ratingCount;
    private double ratingValue;
    private String link;

    private int index;

    public String getId() {

        return id;
    }

    public void setId( String id ) {

        this.id = id;
    }

    public String getAuthorName() {

        return authorName;
    }

    public void setAuthorName( String authorName ) {

        this.authorName = authorName;
    }

    public String getAuthorUrl() {

        return authorUrl;
    }

    public void setAuthorUrl( String authorUrl ) {

        this.authorUrl = authorUrl;
    }

    public Media getMedia() {

        return media;
    }

    public void setMedia( Media media ) {

        this.media = media;
    }

    public int getViewCount() {

        return viewCount;
    }

    public void setViewCount( int viewCount ) {

        this.viewCount = viewCount;
    }

    public int getFavoriteCount() {

        return favoriteCount;
    }

    public void setFavoriteCount( int favoriteCount ) {

        this.favoriteCount = favoriteCount;
    }

    public int getRatingCount() {

        return ratingCount;
    }

    public void setRatingCount( int ratingCount ) {

        this.ratingCount = ratingCount;
    }

    public double getRatingValue() {

        return ratingValue;
    }

    public void setRatingValue( double ratingValue ) {

        this.ratingValue = ratingValue;
    }

    public int getIndex() {

        return index;
    }

    public void setIndex( int index ) {

        this.index = index;
    }

    public String getLink() {

        return link;
    }

    public void setLink( String link ) {

        this.link = link;
    }
}
