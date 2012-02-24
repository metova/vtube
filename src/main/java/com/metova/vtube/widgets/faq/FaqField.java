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
package com.metova.vtube.widgets.faq;

import org.metova.bb.widgets.field.richcontent.RichTextField;
import org.metova.bb.widgets.managed.AbstractVerticalFieldManager;

class FaqField extends AbstractVerticalFieldManager {

    private RichTextField question;
    private RichTextField answer;

    private String questionText;
    private String answerText;

    public FaqField(String question, String answer) {

        setQuestionText( question );
        setAnswerText( answer );
    }

    protected void onInitialize() {

        RichTextField question = new RichTextField( getQuestionText() );
        question.getStyleManager().setStyleClass( "h3" );
        setQuestion( question );

        RichTextField answer = new RichTextField( getAnswerText() );
        answer.getStyleManager().setStyleClass( "h6" );
        setAnswer( answer );
    }

    protected void onLoading() {

        add( getQuestion() );
        add( getAnswer() );
    }

    private void setQuestion( RichTextField question ) {

        this.question = question;
    }

    private RichTextField getQuestion() {

        return question;
    }

    private void setAnswer( RichTextField answer ) {

        this.answer = answer;
    }

    private RichTextField getAnswer() {

        return answer;
    }

    private void setQuestionText( String questionText ) {

        this.questionText = questionText;
    }

    private String getQuestionText() {

        return questionText;
    }

    private void setAnswerText( String answerText ) {

        this.answerText = answerText;
    }

    private String getAnswerText() {

        return answerText;
    }
}
