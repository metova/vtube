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
import java.util.Vector;

import m.java.util.Comparator;
import m.java.util.Iterator;
import m.java.util.Set;
import m.java.util.TreeSet;

import org.metova.mobile.util.Vectors;

import com.google.jsonpull.Json;
import com.metova.vtube.model.feed.MediaThumbnail;

public class MediaThumbnailParser extends AbstractParser {

    public static Vector parseArray( Json parser ) {

        Set result = new TreeSet( new MediaThumbnailComparator() );

        Enumeration enumerator = parser.arrayElements();
        while (enumerator.hasMoreElements()) {

            MediaThumbnail mediaThumbnail = parse( parser );
            if ( mediaThumbnail != null ) {

                result.add( mediaThumbnail );
            }
        }

        return ensureOrderedIdSequence( result );
    }

    private static Vector ensureOrderedIdSequence( Set result ) {

        Iterator iterator = result.iterator();
        for (int i = 0; iterator.hasNext(); i++) {

            MediaThumbnail mediaThumbnail = (MediaThumbnail) iterator.next();
            mediaThumbnail.setId( i );
        }

        return Vectors.iteratorToVector( result.iterator() );
    }

    public static MediaThumbnail parse( Json parser ) {

        MediaThumbnail mediaThumbnail = new MediaThumbnail();
        String id = null;

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( "url" ) ) {

                String url = parser.getStringValue();
                String filename = url.substring( url.lastIndexOf( '/' ) + 1 );
                id = filename.substring( 0, filename.lastIndexOf( '.' ) );

                mediaThumbnail.setUrl( url );
            }
            else if ( key.equals( "width" ) ) {

                mediaThumbnail.setWidth( parser.getIntegerValue() );
            }
            else if ( key.equals( "height" ) ) {

                mediaThumbnail.setHeight( parser.getIntegerValue() );
            }
        }

        try {

            // make sure the id is a number
            mediaThumbnail.setId( Integer.parseInt( id ) );
        }
        catch (NumberFormatException e) {

            mediaThumbnail = null;
        }

        return mediaThumbnail;
    }

    private static class MediaThumbnailComparator implements Comparator {

        public int compare( Object object1, Object object2 ) {

            MediaThumbnail mediaThumbnail1 = (MediaThumbnail) object1;
            MediaThumbnail mediaThumbnail2 = (MediaThumbnail) object2;

            int id1 = mediaThumbnail1.getId();
            int id2 = mediaThumbnail2.getId();

            return id1 - id2;
        }
    }
}
