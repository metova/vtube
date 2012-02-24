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
package com.metova.vtube.widgets.feed;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import org.metova.bb.widgets.field.Fields;
import org.metova.bb.widgets.field.list.AbstractListFieldCallback;
import org.metova.bb.widgets.field.list.ListField;

public class FeedListFieldCallback extends AbstractListFieldCallback {

    protected void drawNormalRow( ListField listField, Graphics graphics, int index, int y, int width, Object rowItem ) {

        drawRow( listField, graphics, index, y, width, rowItem );
    }

    protected void drawFocusedRow( ListField listField, Graphics graphics, int index, int y, int width, Object rowItem ) {

        drawRow( listField, graphics, index, y, width, rowItem );
    }

    protected void drawRow( ListField listField, Graphics graphics, int index, int y, int width, Object rowItem ) {

        String text = (String) rowItem;
        Font font = graphics.getFont();
        int textWidth = Fields.getTextWidth( font, text );

        int textX = (int) ( ( width - textWidth ) * 0.50 );
        graphics.drawText( text, textX, y );
    }
}
