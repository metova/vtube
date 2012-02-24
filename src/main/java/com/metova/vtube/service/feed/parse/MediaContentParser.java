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

import com.google.jsonpull.Json;
import com.metova.vtube.model.feed.MediaContent;
import com.metova.vtube.service.video.FormatTypes;

public class MediaContentParser extends AbstractParser {

    public static Map parseMap( Json parser ) {

        Map map = new HashMap();

        Enumeration enumerator = parser.arrayElements();
        while (enumerator.hasMoreElements()) {

            MediaContent mediaContent = parse( parser );
            if ( mediaContent != null ) {

                String format = String.valueOf( mediaContent.getFormat() );
                map.put( format, mediaContent );
            }
        }

        return map;
    }

    public static MediaContent parse( Json parser ) {

        MediaContent mediaContent = new MediaContent();

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            if ( mediaContent == null ) {

                continue;
            }

            String key = parser.getKey();

            if ( key.equals( "url" ) ) {

                mediaContent.setUrl( parser.getStringValue() );
            }
            else if ( key.equals( "type" ) ) {

                mediaContent.setType( parser.getStringValue() );
            }
            else if ( key.equals( "yt$format" ) ) {

                int format = parser.getIntegerValue();
                if ( FormatTypes.isAcceptableFormatType( String.valueOf( format ) ) ) {

                    mediaContent.setFormat( format );
                }
                else {

                    mediaContent = null;
                }
            }
        }

        return mediaContent;
    }
}
