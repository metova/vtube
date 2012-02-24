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
package com.metova.vtube.service.feed.parse;

import java.io.ByteArrayInputStream;
import java.util.Enumeration;

import org.metova.mobile.util.io.CharacterEncodings;

import com.google.jsonpull.Json;
import com.metova.vtube.model.feed.Feed;

public class FeedParser extends AbstractParser {

    public static Feed parse( byte[] bytes ) {

        Json parser = new Json( new ByteArrayInputStream( bytes ), CharacterEncodings.UTF_8 );
        Feed feed = null;

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "feed" ) ) {

                feed = parseFeed( parser );
                break;
            }
        }

        return feed;
    }

    private static Feed parseFeed( Json parser ) {

        Feed feed = new Feed();

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "title" ) ) {

                feed.setTitle( parseString( parser ) );
            }
            else if ( key.equals( "openSearch$totalResults" ) ) {

                feed.setTotalResults( parseInteger( parser ) );
            }
            else if ( key.equals( "openSearch$startIndex" ) ) {

                feed.setStartIndex( parseInteger( parser ) );
            }
            else if ( key.equals( "openSearch$itemsPerPage" ) ) {

                feed.setItemsPerPage( parseInteger( parser ) );
            }
            else if ( key.equals( "entry" ) ) {

                feed.setEntries( FeedEntryParser.parseArray( parser ) );
            }
        }

        return feed;
    }
}
