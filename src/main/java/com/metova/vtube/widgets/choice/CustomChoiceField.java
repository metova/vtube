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
package com.metova.vtube.widgets.choice;

import net.rim.device.api.ui.Graphics;

public class CustomChoiceField extends org.metova.bb.widgets.field.choice.CustomChoiceField {

    public CustomChoiceField(Object[] entries, String initialValue) {

        super( entries, initialValue, FIELD_LEFT );
    }

    protected void paintBackground( Graphics graphics, int width, int height ) {

        super.paintBackground( graphics, width, height - 2 );
    }

    protected void paintBorder( Graphics graphics, int width, int height ) {

        super.paintBorder( graphics, width, height - 2 );
    }
}
