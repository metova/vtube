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

public abstract class AbstractParser {

    private static final int TYPE_STRING = 100;
    private static final int TYPE_INTEGER = 200;

    protected static int parseInteger( Json parser ) {

        return parseInteger( parser, "$t" );
    }

    protected static int parseInteger( Json parser, String extract ) {

        Integer integer = (Integer) parseObject( parser, extract, TYPE_INTEGER );
        return integer.intValue();
    }

    protected static final String parseString( Json parser ) {

        return parseString( parser, "$t" );
    }

    protected static final String parseString( Json parser, String extract ) {

        return (String) parseObject( parser, extract, TYPE_STRING );
    }

    private static Object parseObject( Json parser, String extract, int type ) {

        Object result = null;

        Enumeration enumerator = parser.objectElements();
        while (enumerator.hasMoreElements()) {

            String key = parser.getKey();

            if ( key.equals( extract ) ) {

                if ( type == TYPE_STRING ) {

                    result = parser.getStringValue();
                }
                else if ( type == TYPE_INTEGER ) {

                    result = new Integer( parser.getIntegerValue() );
                }

                break;
            }
        }

        return result;
    }
}
