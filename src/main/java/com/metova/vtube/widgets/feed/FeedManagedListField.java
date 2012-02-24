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

import org.metova.bb.widgets.field.Fields;
import org.metova.bb.widgets.field.list.ListField;
import org.metova.bb.widgets.field.list.ManagedListField;
import org.metova.bb.widgets.theme.ComputedStyle;
import org.metova.mobile.util.accessor.ListDataAccessor;
import org.metova.mobile.util.coordinate.Edges;

public abstract class FeedManagedListField extends ManagedListField {

    public FeedManagedListField() {

        super( 0, new FeedListFieldCallback(), null, null );

        setListField( new MyListField() );
    }

    protected abstract void viewDetails( Object selected );

    protected boolean performTrackwheelClick( Object selection ) {

        viewDetails( selection );
        return true;
    }

    private class MyListField extends ListField {

        public MyListField() {

            getStyleManager().setStyleClass( "ListField-feed" );
        }

        public int getPreferredWidth() {

            int result = 0;

            ComputedStyle computedStyle = getStyleManager().getComputedStyle();
            Font font = computedStyle.getFont();

            ListDataAccessor listDataAccessor = getListDataAccessor();
            for (int i = 0; i < listDataAccessor.size(); i++) {

                String text = (String) listDataAccessor.get( i );
                result = Math.max( result, Fields.getTextWidth( font, text ) );
            }

            Edges padding = computedStyle.getPadding();
            return result + padding.getWidth();
        }
    }
}
