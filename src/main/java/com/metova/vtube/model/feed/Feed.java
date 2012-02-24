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

import m.java.util.Map;

public class Feed {

    public static final String ID = "feed.id";

    //"updated":{
    //   "$t":"2008-12-04T18:50:03.000-08:00"
    //},
    //"title":{
    //   "type":"text",
    //   "$t":"YouTube&nbsp;Mobile&nbsp;Videos"
    //},
    //"logo":{
    //   "$t":"http://www.youtube.com/img/pic_youtubelogo_123x63.gif"
    //},
    //"openSearch$totalResults":{
    //   "$t":"60"
    //},
    //"openSearch$startIndex":{
    //   "$t":"1"
    //},
    //"openSearch$itemsPerPage":{
    //   "$t":"25"
    //},
    //"entry":[
    //]

    private String title;
    private boolean search;
    private String orderBy;

    private int totalResults;
    private int startIndex;
    private int itemsPerPage;
    private Map entries;

    public String getTitle() {

        return title;
    }

    public void setTitle( String title ) {

        this.title = title;
    }

    public int getTotalResults() {

        return totalResults;
    }

    public void setTotalResults( int totalResults ) {

        this.totalResults = totalResults;
    }

    public int getItemsPerPage() {

        return itemsPerPage;
    }

    public int getStartIndex() {

        return startIndex;
    }

    public void setStartIndex( int startIndex ) {

        this.startIndex = startIndex;
    }

    public void setItemsPerPage( int itemsPerPage ) {

        this.itemsPerPage = itemsPerPage;
    }

    public Map getEntries() {

        return entries;
    }

    public void setEntries( Map entries ) {

        this.entries = entries;
    }

    public boolean isSearch() {

        return search;
    }

    public void setSearch( boolean search ) {

        this.search = search;
    }

    public String getOrderBy() {

        return orderBy;
    }

    public void setOrderBy( String orderBy ) {

        this.orderBy = orderBy;
    }
}
