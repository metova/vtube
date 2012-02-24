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

import java.util.Enumeration;

import m.java.util.HashMap;
import m.java.util.Map;

import org.metova.mobile.util.text.Text;

import com.google.jsonpull.Json;
import com.metova.vtube.model.feed.FeedEntry;

public class FeedEntryParser extends AbstractParser {

    public static Map parseArray( Json parser ) {

        Map map = new HashMap();

        Enumeration enumerator = parser.arrayElements();
        for (int i = 0; enumerator.hasMoreElements(); i++) {

            FeedEntry feedEntry = parse( parser );
            feedEntry.setIndex( i );

            map.put( feedEntry.getId(), feedEntry );
        }

        return map;
    }

    public static FeedEntry parse( Json parser ) {

        FeedEntry feedEntry = new FeedEntry();

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "id" ) ) {

                feedEntry.setId( parseString( parser ) );
            }
            else if ( key.equals( "author" ) ) {

                parseAuthorArray( feedEntry, parser );
            }
            else if ( key.equals( "media$group" ) ) {

                feedEntry.setMedia( MediaParser.parse( parser ) );
            }
            else if ( key.equals( "yt$statistics" ) ) {

                parseStatistics( feedEntry, parser );
            }
            else if ( key.equals( "gd$rating" ) ) {

                parseRating( feedEntry, parser );
            }
            else if ( key.equals( "link" ) ) {

                parseLinkArray( feedEntry, parser );
            }
        }

        return feedEntry;
    }

    private static void parseAuthorArray( FeedEntry feedEntry, Json parser ) {

        Enumeration enumerator = parser.arrayElements();
        while (enumerator.hasMoreElements()) {

            parseAuthor( feedEntry, parser );
            break;
        }
    }

    private static void parseAuthor( FeedEntry feedEntry, Json parser ) {

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "name" ) ) {

                feedEntry.setAuthorName( parseString( parser ) );
            }
            else if ( key.equals( "uri" ) ) {

                feedEntry.setAuthorUrl( parseString( parser ) );
            }
        }
    }

    private static void parseStatistics( FeedEntry feedEntry, Json parser ) {

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "viewCount" ) ) {

                feedEntry.setViewCount( parser.getIntegerValue() );
            }
            else if ( key.equals( "favoriteCount" ) ) {

                feedEntry.setFavoriteCount( parser.getIntegerValue() );
            }
        }
    }

    private static void parseRating( FeedEntry feedEntry, Json parser ) {

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "numRaters" ) ) {

                feedEntry.setRatingCount( parser.getIntegerValue() );
            }
            else if ( key.equals( "average" ) ) {

                feedEntry.setRatingValue( parser.getDoubleValue() );
            }
        }
    }

    private static void parseLinkArray( FeedEntry feedEntry, Json parser ) {

        Enumeration enumerator = parser.arrayElements();
        while (enumerator.hasMoreElements()) {

            if ( parseLink( feedEntry, parser ) ) {

                break;
            }
        }
    }

    private static boolean parseLink( FeedEntry feedEntry, Json parser ) {

        boolean result = false;

        String type = null;

        Enumeration enumerator = parser.objectElements();
        while (!result && enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "type" ) ) {

                type = parser.getStringValue();
            }
            else if ( key.equals( "href" ) && Text.equals( type, "text/html" ) ) {

                feedEntry.setLink( parser.getStringValue() );
                result = true;
            }
        }

        return result;
    }
}
