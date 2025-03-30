package org.jsoup.parser;


import Token.Comment;
import org.jsoup.parser.TokeniserState;

import static org.jsoup.parser.TokeniserState.replacementStr;


enum TokeniserState {

    Data() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case '&' :
                    t.advanceTransition(TokeniserState.CharacterReferenceInData);
                    break;
                case '<' :
                    t.advanceTransition(TokeniserState.TagOpen);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.emit(r.consume());
                    break;
                case TokeniserState.eof :
                    t.emit(new org.jsoup.parser.Token.EOF());
                    break;
                default :
                    java.lang.String data = r.consumeToAny('&', '<', TokeniserState.nullChar);
                    t.emit(data);
                    break;
            }
        }
    },
    CharacterReferenceInData() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.Character c = t.consumeCharacterReference(null, false);
            if (c == null)
                t.emit('&');
            else
                t.emit(c);

            t.transition(TokeniserState.Data);
        }
    },
    Rcdata() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case '&' :
                    t.advanceTransition(TokeniserState.CharacterReferenceInRcdata);
                    break;
                case '<' :
                    t.advanceTransition(TokeniserState.RcdataLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.emit(new org.jsoup.parser.Token.EOF());
                    break;
                default :
                    java.lang.String data = r.consumeToAny('&', '<', TokeniserState.nullChar);
                    t.emit(data);
                    break;
            }
        }
    },
    CharacterReferenceInRcdata() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.Character c = t.consumeCharacterReference(null, false);
            if (c == null)
                t.emit('&');
            else
                t.emit(c);

            t.transition(TokeniserState.Rcdata);
        }
    },
    Rawtext() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case '<' :
                    t.advanceTransition(TokeniserState.RawtextLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.emit(new org.jsoup.parser.Token.EOF());
                    break;
                default :
                    java.lang.String data = r.consumeToAny('<', TokeniserState.nullChar);
                    t.emit(data);
                    break;
            }
        }
    },
    ScriptData() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case '<' :
                    t.advanceTransition(TokeniserState.ScriptDataLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.emit(new org.jsoup.parser.Token.EOF());
                    break;
                default :
                    java.lang.String data = r.consumeToAny('<', TokeniserState.nullChar);
                    t.emit(data);
                    break;
            }
        }
    },
    PLAINTEXT() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.emit(new org.jsoup.parser.Token.EOF());
                    break;
                default :
                    java.lang.String data = r.consumeTo(TokeniserState.nullChar);
                    t.emit(data);
                    break;
            }
        }
    },
    TagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.current()) {
                case '!' :
                    t.advanceTransition(TokeniserState.MarkupDeclarationOpen);
                    break;
                case '/' :
                    t.advanceTransition(TokeniserState.EndTagOpen);
                    break;
                case '?' :
                    t.advanceTransition(TokeniserState.BogusComment);
                    break;
                default :
                    if (r.matchesLetter()) {
                        t.createTagPending(true);
                        t.transition(TokeniserState.TagName);
                    }else {
                        t.error(this);
                        t.emit('<');
                        t.transition(TokeniserState.Data);
                    }
                    break;
            }
        }
    },
    EndTagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.emit("</");
                t.transition(TokeniserState.Data);
            }else
                if (r.matchesLetter()) {
                    t.createTagPending(false);
                    t.transition(TokeniserState.TagName);
                }else
                    if (r.matches('>')) {
                        t.error(this);
                        t.advanceTransition(TokeniserState.Data);
                    }else {
                        t.error(this);
                        t.advanceTransition(TokeniserState.BogusComment);
                    }


        }
    },
    TagName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String tagName = r.consumeToAny('\t', '\n', '\f', ' ', '/', '>', TokeniserState.nullChar).toLowerCase();
            t.tagPending.appendTagName(tagName);
            switch (r.consume()) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeAttributeName);
                    break;
                case '/' :
                    t.transition(TokeniserState.SelfClosingStartTag);
                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.tagPending.appendTagName(replacementStr);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
            }
        }
    },
    RcdataLessthanSign() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matches('/')) {
                t.createTempBuffer();
                t.advanceTransition(TokeniserState.RCDATAEndTagOpen);
            }else
                if ((r.matchesLetter()) && (!(r.containsIgnoreCase(("</" + (t.appropriateEndTagName())))))) {
                    t.tagPending = new org.jsoup.parser.Token.EndTag(t.appropriateEndTagName());
                    t.emitTagPending();
                    r.unconsume();
                    t.transition(TokeniserState.Data);
                }else {
                    t.emit("<");
                    t.transition(TokeniserState.Rcdata);
                }

        }
    },
    RCDATAEndTagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(java.lang.Character.toLowerCase(r.current()));
                t.dataBuffer.append(java.lang.Character.toLowerCase(r.current()));
                t.advanceTransition(TokeniserState.RCDATAEndTagName);
            }else {
                t.emit("</");
                t.transition(TokeniserState.Rcdata);
            }
        }
    },
    RCDATAEndTagName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    if (t.isAppropriateEndTagToken())
                        t.transition(TokeniserState.BeforeAttributeName);
                    else
                        anythingElse(t, r);

                    break;
                case '/' :
                    if (t.isAppropriateEndTagToken())
                        t.transition(TokeniserState.SelfClosingStartTag);
                    else
                        anythingElse(t, r);

                    break;
                case '>' :
                    if (t.isAppropriateEndTagToken()) {
                        t.emitTagPending();
                        t.transition(TokeniserState.Data);
                    }else
                        anythingElse(t, r);

                    break;
                default :
                    anythingElse(t, r);
            }
        }

        private void anythingElse(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            t.emit(("</" + (t.dataBuffer.toString())));
            t.transition(TokeniserState.Rcdata);
        }
    },
    RawtextLessthanSign() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matches('/')) {
                t.createTempBuffer();
                t.advanceTransition(TokeniserState.RawtextEndTagOpen);
            }else {
                t.emit('<');
                t.transition(TokeniserState.Rawtext);
            }
        }
    },
    RawtextEndTagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(TokeniserState.RawtextEndTagName);
            }else {
                t.emit("</");
                t.transition(TokeniserState.Rawtext);
            }
        }
    },
    RawtextEndTagName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            if ((t.isAppropriateEndTagToken()) && (!(r.isEmpty()))) {
                char c = r.consume();
                switch (c) {
                    case '\t' :
                    case '\n' :
                    case '\f' :
                    case ' ' :
                        t.transition(TokeniserState.BeforeAttributeName);
                        break;
                    case '/' :
                        t.transition(TokeniserState.SelfClosingStartTag);
                        break;
                    case '>' :
                        t.emitTagPending();
                        t.transition(TokeniserState.Data);
                        break;
                    default :
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                }
            }else
                anythingElse(t, r);

        }

        private void anythingElse(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            t.emit(("</" + (t.dataBuffer.toString())));
            t.transition(TokeniserState.Rawtext);
        }
    },
    ScriptDataLessthanSign() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            switch (r.consume()) {
                case '/' :
                    t.createTempBuffer();
                    t.transition(TokeniserState.ScriptDataEndTagOpen);
                    break;
                case '!' :
                    t.emit("<!");
                    t.transition(TokeniserState.ScriptDataEscapeStart);
                    break;
                default :
                    t.emit("<");
                    r.unconsume();
                    t.transition(TokeniserState.ScriptData);
            }
        }
    },
    ScriptDataEndTagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.transition(TokeniserState.ScriptDataEndTagName);
            }else {
                t.emit("</");
                t.transition(TokeniserState.ScriptData);
            }
        }
    },
    ScriptDataEndTagName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            if ((t.isAppropriateEndTagToken()) && (!(r.isEmpty()))) {
                char c = r.consume();
                switch (c) {
                    case '\t' :
                    case '\n' :
                    case '\f' :
                    case ' ' :
                        t.transition(TokeniserState.BeforeAttributeName);
                        break;
                    case '/' :
                        t.transition(TokeniserState.SelfClosingStartTag);
                        break;
                    case '>' :
                        t.emitTagPending();
                        t.transition(TokeniserState.Data);
                        break;
                    default :
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                }
            }else {
                anythingElse(t, r);
            }
        }

        private void anythingElse(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            t.emit(("</" + (t.dataBuffer.toString())));
            t.transition(TokeniserState.ScriptData);
        }
    },
    ScriptDataEscapeStart() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matches('-')) {
                t.emit('-');
                t.advanceTransition(TokeniserState.ScriptDataEscapeStartDash);
            }else {
                t.transition(TokeniserState.ScriptData);
            }
        }
    },
    ScriptDataEscapeStartDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matches('-')) {
                t.emit('-');
                t.advanceTransition(TokeniserState.ScriptDataEscapedDashDash);
            }else {
                t.transition(TokeniserState.ScriptData);
            }
        }
    },
    ScriptDataEscaped() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(TokeniserState.Data);
                return;
            }
            switch (r.current()) {
                case '-' :
                    t.emit('-');
                    t.advanceTransition(TokeniserState.ScriptDataEscapedDash);
                    break;
                case '<' :
                    t.advanceTransition(TokeniserState.ScriptDataEscapedLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                default :
                    java.lang.String data = r.consumeToAny('-', '<', TokeniserState.nullChar);
                    t.emit(data);
            }
        }
    },
    ScriptDataEscapedDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(TokeniserState.Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataEscapedDashDash);
                    break;
                case '<' :
                    t.transition(TokeniserState.ScriptDataEscapedLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.emit(TokeniserState.replacementChar);
                    t.transition(TokeniserState.ScriptDataEscaped);
                    break;
                default :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedDashDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.transition(TokeniserState.Data);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.emit(c);
                    break;
                case '<' :
                    t.transition(TokeniserState.ScriptDataEscapedLessthanSign);
                    break;
                case '>' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptData);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.emit(TokeniserState.replacementChar);
                    t.transition(TokeniserState.ScriptDataEscaped);
                    break;
                default :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedLessthanSign() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTempBuffer();
                t.dataBuffer.append(java.lang.Character.toLowerCase(r.current()));
                t.emit(("<" + (r.current())));
                t.advanceTransition(TokeniserState.ScriptDataDoubleEscapeStart);
            }else
                if (r.matches('/')) {
                    t.createTempBuffer();
                    t.advanceTransition(TokeniserState.ScriptDataEscapedEndTagOpen);
                }else {
                    t.emit('<');
                    t.transition(TokeniserState.ScriptDataEscaped);
                }

        }
    },
    ScriptDataEscapedEndTagOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createTagPending(false);
                t.tagPending.appendTagName(java.lang.Character.toLowerCase(r.current()));
                t.dataBuffer.append(r.current());
                t.advanceTransition(TokeniserState.ScriptDataEscapedEndTagName);
            }else {
                t.emit("</");
                t.transition(TokeniserState.ScriptDataEscaped);
            }
        }
    },
    ScriptDataEscapedEndTagName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.tagPending.appendTagName(name.toLowerCase());
                t.dataBuffer.append(name);
                return;
            }
            if ((t.isAppropriateEndTagToken()) && (!(r.isEmpty()))) {
                char c = r.consume();
                switch (c) {
                    case '\t' :
                    case '\n' :
                    case '\f' :
                    case ' ' :
                        t.transition(TokeniserState.BeforeAttributeName);
                        break;
                    case '/' :
                        t.transition(TokeniserState.SelfClosingStartTag);
                        break;
                    case '>' :
                        t.emitTagPending();
                        t.transition(TokeniserState.Data);
                        break;
                    default :
                        t.dataBuffer.append(c);
                        anythingElse(t, r);
                        break;
                }
            }else {
                anythingElse(t, r);
            }
        }

        private void anythingElse(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            t.emit(("</" + (t.dataBuffer.toString())));
            t.transition(TokeniserState.ScriptDataEscaped);
        }
    },
    ScriptDataDoubleEscapeStart() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.dataBuffer.append(name.toLowerCase());
                t.emit(name);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                case '/' :
                case '>' :
                    if (t.dataBuffer.toString().equals("script"))
                        t.transition(TokeniserState.ScriptDataDoubleEscaped);
                    else
                        t.transition(TokeniserState.ScriptDataEscaped);

                    t.emit(c);
                    break;
                default :
                    r.unconsume();
                    t.transition(TokeniserState.ScriptDataEscaped);
            }
        }
    },
    ScriptDataDoubleEscaped() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.current();
            switch (c) {
                case '-' :
                    t.emit(c);
                    t.advanceTransition(TokeniserState.ScriptDataDoubleEscapedDash);
                    break;
                case '<' :
                    t.emit(c);
                    t.advanceTransition(TokeniserState.ScriptDataDoubleEscapedLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.emit(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    java.lang.String data = r.consumeToAny('-', '<', TokeniserState.nullChar);
                    t.emit(data);
            }
        }
    },
    ScriptDataDoubleEscapedDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataDoubleEscapedDashDash);
                    break;
                case '<' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataDoubleEscapedLessthanSign);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.emit(TokeniserState.replacementChar);
                    t.transition(TokeniserState.ScriptDataDoubleEscaped);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataDoubleEscaped);
            }
        }
    },
    ScriptDataDoubleEscapedDashDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.emit(c);
                    break;
                case '<' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataDoubleEscapedLessthanSign);
                    break;
                case '>' :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptData);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.emit(TokeniserState.replacementChar);
                    t.transition(TokeniserState.ScriptDataDoubleEscaped);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.emit(c);
                    t.transition(TokeniserState.ScriptDataDoubleEscaped);
            }
        }
    },
    ScriptDataDoubleEscapedLessthanSign() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matches('/')) {
                t.emit('/');
                t.createTempBuffer();
                t.advanceTransition(TokeniserState.ScriptDataDoubleEscapeEnd);
            }else {
                t.transition(TokeniserState.ScriptDataDoubleEscaped);
            }
        }
    },
    ScriptDataDoubleEscapeEnd() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.dataBuffer.append(name.toLowerCase());
                t.emit(name);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                case '/' :
                case '>' :
                    if (t.dataBuffer.toString().equals("script"))
                        t.transition(TokeniserState.ScriptDataEscaped);
                    else
                        t.transition(TokeniserState.ScriptDataDoubleEscaped);

                    t.emit(c);
                    break;
                default :
                    r.unconsume();
                    t.transition(TokeniserState.ScriptDataDoubleEscaped);
            }
        }
    },
    BeforeAttributeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '/' :
                    t.transition(TokeniserState.SelfClosingStartTag);
                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(TokeniserState.AttributeName);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                case '\'' :
                case '<' :
                case '=' :
                    t.error(this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(TokeniserState.AttributeName);
                    break;
                default :
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(TokeniserState.AttributeName);
            }
        }
    },
    AttributeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String name = r.consumeToAny('\t', '\n', '\f', ' ', '/', '=', '>', TokeniserState.nullChar, '"', '\'', '<');
            t.tagPending.appendAttributeName(name.toLowerCase());
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.AfterAttributeName);
                    break;
                case '/' :
                    t.transition(TokeniserState.SelfClosingStartTag);
                    break;
                case '=' :
                    t.transition(TokeniserState.BeforeAttributeValue);
                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeName(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                case '\'' :
                case '<' :
                    t.error(this);
                    t.tagPending.appendAttributeName(c);
            }
        }
    },
    AfterAttributeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '/' :
                    t.transition(TokeniserState.SelfClosingStartTag);
                    break;
                case '=' :
                    t.transition(TokeniserState.BeforeAttributeValue);
                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeName(TokeniserState.replacementChar);
                    t.transition(TokeniserState.AttributeName);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                case '\'' :
                case '<' :
                    t.error(this);
                    t.tagPending.newAttribute();
                    t.tagPending.appendAttributeName(c);
                    t.transition(TokeniserState.AttributeName);
                    break;
                default :
                    t.tagPending.newAttribute();
                    r.unconsume();
                    t.transition(TokeniserState.AttributeName);
            }
        }
    },
    BeforeAttributeValue() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '"' :
                    t.transition(TokeniserState.AttributeValue_doubleQuoted);
                    break;
                case '&' :
                    r.unconsume();
                    t.transition(TokeniserState.AttributeValue_unquoted);
                    break;
                case '\'' :
                    t.transition(TokeniserState.AttributeValue_singleQuoted);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeValue(TokeniserState.replacementChar);
                    t.transition(TokeniserState.AttributeValue_unquoted);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                case '>' :
                    t.error(this);
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case '<' :
                case '=' :
                case '`' :
                    t.error(this);
                    t.tagPending.appendAttributeValue(c);
                    t.transition(TokeniserState.AttributeValue_unquoted);
                    break;
                default :
                    r.unconsume();
                    t.transition(TokeniserState.AttributeValue_unquoted);
            }
        }
    },
    AttributeValue_doubleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String value = r.consumeToAny('"', '&', TokeniserState.nullChar);
            if ((value.length()) > 0)
                t.tagPending.appendAttributeValue(value);

            char c = r.consume();
            switch (c) {
                case '"' :
                    t.transition(TokeniserState.AfterAttributeValue_quoted);
                    break;
                case '&' :
                    java.lang.Character ref = t.consumeCharacterReference('"', true);
                    if (ref != null)
                        t.tagPending.appendAttributeValue(ref);
                    else
                        t.tagPending.appendAttributeValue('&');

                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeValue(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
            }
        }
    },
    AttributeValue_singleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String value = r.consumeToAny('\'', '&', TokeniserState.nullChar);
            if ((value.length()) > 0)
                t.tagPending.appendAttributeValue(value);

            char c = r.consume();
            switch (c) {
                case '\'' :
                    t.transition(TokeniserState.AfterAttributeValue_quoted);
                    break;
                case '&' :
                    java.lang.Character ref = t.consumeCharacterReference('\'', true);
                    if (ref != null)
                        t.tagPending.appendAttributeValue(ref);
                    else
                        t.tagPending.appendAttributeValue('&');

                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeValue(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
            }
        }
    },
    AttributeValue_unquoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String value = r.consumeToAny('\t', '\n', '\f', ' ', '&', '>', TokeniserState.nullChar, '"', '\'', '<', '=', '`');
            if ((value.length()) > 0)
                t.tagPending.appendAttributeValue(value);

            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeAttributeName);
                    break;
                case '&' :
                    java.lang.Character ref = t.consumeCharacterReference('>', true);
                    if (ref != null)
                        t.tagPending.appendAttributeValue(ref);
                    else
                        t.tagPending.appendAttributeValue('&');

                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.tagPending.appendAttributeValue(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                case '\'' :
                case '<' :
                case '=' :
                case '`' :
                    t.error(this);
                    t.tagPending.appendAttributeValue(c);
                    break;
            }
        }
    },
    AfterAttributeValue_quoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeAttributeName);
                    break;
                case '/' :
                    t.transition(TokeniserState.SelfClosingStartTag);
                    break;
                case '>' :
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    r.unconsume();
                    t.transition(TokeniserState.BeforeAttributeName);
            }
        }
    },
    SelfClosingStartTag() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '>' :
                    t.tagPending.selfClosing = true;
                    t.emitTagPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.transition(TokeniserState.BeforeAttributeName);
            }
        }
    },
    BogusComment() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            r.unconsume();
            org.jsoup.parser.Token.Comment comment = new org.jsoup.parser.Token.Comment();
            comment.data.append(r.consumeTo('>'));
            t.emit(comment);
            t.advanceTransition(TokeniserState.Data);
        }
    },
    MarkupDeclarationOpen() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchConsume("--")) {
                t.createCommentPending();
                t.transition(TokeniserState.CommentStart);
            }else
                if (r.matchConsumeIgnoreCase("DOCTYPE")) {
                    t.transition(TokeniserState.Doctype);
                }else
                    if (r.matchConsume("[CDATA[")) {
                        t.transition(TokeniserState.CdataSection);
                    }else {
                        t.error(this);
                        t.advanceTransition(TokeniserState.BogusComment);
                    }


        }
    },
    CommentStart() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.transition(TokeniserState.CommentStartDash);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.Comment);
                    break;
                case '>' :
                    t.error(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.commentPending.data.append(c);
                    t.transition(TokeniserState.Comment);
            }
        }
    },
    CommentStartDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.transition(TokeniserState.CommentStartDash);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.Comment);
                    break;
                case '>' :
                    t.error(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.commentPending.data.append(c);
                    t.transition(TokeniserState.Comment);
            }
        }
    },
    Comment() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.current();
            switch (c) {
                case '-' :
                    t.advanceTransition(TokeniserState.CommentEndDash);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    r.advance();
                    t.commentPending.data.append(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.commentPending.data.append(r.consumeToAny('-', TokeniserState.nullChar));
            }
        }
    },
    CommentEndDash() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.transition(TokeniserState.CommentEnd);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.commentPending.data.append('-').append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.Comment);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.commentPending.data.append('-').append(c);
                    t.transition(TokeniserState.Comment);
            }
        }
    },
    CommentEnd() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '>' :
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.commentPending.data.append("--").append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.Comment);
                    break;
                case '!' :
                    t.error(this);
                    t.transition(TokeniserState.CommentEndBang);
                    break;
                case '-' :
                    t.error(this);
                    t.commentPending.data.append('-');
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.commentPending.data.append("--").append(c);
                    t.transition(TokeniserState.Comment);
            }
        }
    },
    CommentEndBang() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '-' :
                    t.commentPending.data.append("--!");
                    t.transition(TokeniserState.CommentEndDash);
                    break;
                case '>' :
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.commentPending.data.append("--!").append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.Comment);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.emitCommentPending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.commentPending.data.append("--!").append(c);
                    t.transition(TokeniserState.Comment);
            }
        }
    },
    Doctype() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeDoctypeName);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.createDoctypePending();
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.transition(TokeniserState.BeforeDoctypeName);
            }
        }
    },
    BeforeDoctypeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                t.createDoctypePending();
                t.transition(TokeniserState.DoctypeName);
                return;
            }
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    t.transition(TokeniserState.DoctypeName);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.createDoctypePending();
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.createDoctypePending();
                    t.doctypePending.name.append(c);
                    t.transition(TokeniserState.DoctypeName);
            }
        }
    },
    DoctypeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.matchesLetter()) {
                java.lang.String name = r.consumeLetterSequence();
                t.doctypePending.name.append(name.toLowerCase());
                return;
            }
            char c = r.consume();
            switch (c) {
                case '>' :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.AfterDoctypeName);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.name.append(TokeniserState.replacementChar);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.doctypePending.name.append(c);
            }
        }
    },
    AfterDoctypeName() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            if (r.isEmpty()) {
                t.eofError(this);
                t.doctypePending.forceQuirks = true;
                t.emitDoctypePending();
                t.transition(TokeniserState.Data);
                return;
            }
            if (r.matchesAny('\t', '\n', '\f', ' '))
                r.advance();
            else
                if (r.matches('>')) {
                    t.emitDoctypePending();
                    t.advanceTransition(TokeniserState.Data);
                }else
                    if (r.matchConsumeIgnoreCase("PUBLIC")) {
                        t.transition(TokeniserState.AfterDoctypePublicKeyword);
                    }else
                        if (r.matchConsumeIgnoreCase("SYSTEM")) {
                            t.transition(TokeniserState.AfterDoctypeSystemKeyword);
                        }else {
                            t.error(this);
                            t.doctypePending.forceQuirks = true;
                            t.advanceTransition(TokeniserState.BogusDoctype);
                        }



        }
    },
    AfterDoctypePublicKeyword() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeDoctypePublicIdentifier);
                    break;
                case '"' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypePublicIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypePublicIdentifier_singleQuoted);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    BeforeDoctypePublicIdentifier() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '"' :
                    t.transition(TokeniserState.DoctypePublicIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.transition(TokeniserState.DoctypePublicIdentifier_singleQuoted);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    DoctypePublicIdentifier_doubleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '"' :
                    t.transition(TokeniserState.AfterDoctypePublicIdentifier);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.doctypePending.publicIdentifier.append(c);
            }
        }
    },
    DoctypePublicIdentifier_singleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\'' :
                    t.transition(TokeniserState.AfterDoctypePublicIdentifier);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.publicIdentifier.append(TokeniserState.replacementChar);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.doctypePending.publicIdentifier.append(c);
            }
        }
    },
    AfterDoctypePublicIdentifier() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BetweenDoctypePublicAndSystemIdentifiers);
                    break;
                case '>' :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_singleQuoted);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    BetweenDoctypePublicAndSystemIdentifiers() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '>' :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_singleQuoted);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    AfterDoctypeSystemKeyword() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    t.transition(TokeniserState.BeforeDoctypeSystemIdentifier);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case '"' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.error(this);
                    t.transition(TokeniserState.DoctypeSystemIdentifier_singleQuoted);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
            }
        }
    },
    BeforeDoctypeSystemIdentifier() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '"' :
                    t.transition(TokeniserState.DoctypeSystemIdentifier_doubleQuoted);
                    break;
                case '\'' :
                    t.transition(TokeniserState.DoctypeSystemIdentifier_singleQuoted);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    DoctypeSystemIdentifier_doubleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '"' :
                    t.transition(TokeniserState.AfterDoctypeSystemIdentifier);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.doctypePending.systemIdentifier.append(c);
            }
        }
    },
    DoctypeSystemIdentifier_singleQuoted() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\'' :
                    t.transition(TokeniserState.AfterDoctypeSystemIdentifier);
                    break;
                case TokeniserState.nullChar :
                    t.error(this);
                    t.doctypePending.systemIdentifier.append(TokeniserState.replacementChar);
                    break;
                case '>' :
                    t.error(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.doctypePending.systemIdentifier.append(c);
            }
        }
    },
    AfterDoctypeSystemIdentifier() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '\t' :
                case '\n' :
                case '\f' :
                case ' ' :
                    break;
                case '>' :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.eofError(this);
                    t.doctypePending.forceQuirks = true;
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    t.error(this);
                    t.transition(TokeniserState.BogusDoctype);
            }
        }
    },
    BogusDoctype() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            char c = r.consume();
            switch (c) {
                case '>' :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                case TokeniserState.eof :
                    t.emitDoctypePending();
                    t.transition(TokeniserState.Data);
                    break;
                default :
                    break;
            }
        }
    },
    CdataSection() {
        void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r) {
            java.lang.String data = r.consumeTo("]]>");
            t.emit(data);
            r.matchConsume("]]>");
            t.transition(TokeniserState.Data);
        }
    };
    abstract void read(org.jsoup.parser.Tokeniser t, org.jsoup.parser.CharacterReader r);

    private static final char nullChar = '\u0000';

    private static final char replacementChar = Tokeniser.replacementChar;

    private static final java.lang.String replacementStr = java.lang.String.valueOf(Tokeniser.replacementChar);

    private static final char eof = CharacterReader.EOF;
}

