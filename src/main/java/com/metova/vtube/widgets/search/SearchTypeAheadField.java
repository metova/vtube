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
package com.metova.vtube.widgets.search;

import org.metova.bb.widgets.field.edit.EditField;
import org.metova.bb.widgets.field.list.ListField;
import org.metova.bb.widgets.field.typeahead.TypeAheadListField;
import org.metova.bb.widgets.field.typeahead.TypeAheadPopupField;
import org.metova.mobile.persistence.PersistableVector;
import org.metova.mobile.util.accessor.ListDataAccessor;

import com.metova.vtube.service.persistence.SearchHistoryStore;

public class SearchTypeAheadField extends TypeAheadPopupField {

    public SearchTypeAheadField() {

        super( 0, null, new SearchComparator() );

        TypeAheadListField typeAheadListField = getTypeAheadListField();
        ListField listField = typeAheadListField.getListField();
        listField.getStyleManager().setStyleClass( "ListField-search" );
    }

    protected void initializeEditField() {

        super.initializeEditField();

        EditField editField = getEditField();
        editField.getStyleManager().setStyleClass( "EditField-typeahead" );
    }

    protected ListDataAccessor getListDataAccessor() {

        return new MyListDataAccessor();
    }

    private class MyListDataAccessor implements ListDataAccessor {

        private Object[] delegate;

        public MyListDataAccessor() {

            PersistableVector searchHistoryStore = SearchHistoryStore.instance().load();
            delegate = new Object[searchHistoryStore.size()];
            searchHistoryStore.copyInto( delegate );
        }

        public Object get( int index ) {

            return delegate[index];
        }

        public int size() {

            return delegate.length;
        }
    }
}
