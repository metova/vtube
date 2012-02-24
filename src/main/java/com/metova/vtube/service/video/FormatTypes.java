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
package com.metova.vtube.service.video;

import org.metova.mobile.util.text.Text;

import com.metova.vtube.Settings;
import com.metova.vtube.service.Parameters;

public class FormatTypes {

    public static final String[][] getFormatTypeOptions() {

        String[][] result = new String[2][];
        result[0] = Settings.FORMAT_TYPE_1;
        result[1] = Settings.FORMAT_TYPE_6;
        return result;
    }

    public static final String[] getFormatTypeOptions( boolean name ) {

        String[][] parameters = getFormatTypeOptions();
        return Parameters.getParameterOptions( parameters, name );
    }

    public static final String getFormatTypeId( String name ) {

        String[][] parameters = getFormatTypeOptions();
        return Parameters.getParameterId( parameters, name );
    }

    public static final String getFormatTypeName( String id ) {

        String[][] parameters = getFormatTypeOptions();
        return Parameters.getParameterName( parameters, id );
    }

    public static boolean isAcceptableFormatType( String format ) {

        boolean result = false;

        String[] acceptableFormatTypes = getFormatTypeOptions( false );
        for (int i = 0; i < acceptableFormatTypes.length; i++) {

            if ( Text.equals( format, acceptableFormatTypes[i] ) ) {

                result = true;
                break;
            }
        }

        return result;
    }

    public static final String getBackupFormatType( String attempted ) {

        String result = null;

        String[] acceptableFormatTypes = getFormatTypeOptions( false );
        if ( acceptableFormatTypes.length > 1 ) {

            for (int i = 0; i < acceptableFormatTypes.length; i++) {

                String format = acceptableFormatTypes[i];
                if ( !Text.equals( attempted, format ) ) {

                    result = format;
                    break;
                }
            }
        }

        return result;
    }
}
