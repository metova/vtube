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

import com.google.jsonpull.Json;
import com.metova.vtube.model.feed.Media;

public class MediaParser extends AbstractParser {

    public static Media parse( Json parser ) {

        Media media = new Media();

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "media$title" ) ) {

                media.setTitle( parseString( parser ) );
            }
            else if ( key.equals( "media$description" ) ) {

                media.setDescription( parseString( parser ) );
            }
            else if ( key.equals( "yt$duration" ) ) {

                media.setSeconds( Integer.parseInt( parseString( parser, "seconds" ) ) );
            }
            else if ( key.equals( "media$content" ) ) {

                media.setContents( MediaContentParser.parseMap( parser ) );
            }
            else if ( key.equals( "media$thumbnail" ) ) {

                media.setThumbnails( MediaThumbnailParser.parseArray( parser ) );
            }
        }

        return media;
    }
}
