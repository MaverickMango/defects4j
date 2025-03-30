package com.fasterxml.jackson.core.json;


public class ReaderBasedJsonParser extends com.fasterxml.jackson.core.base.ParserBase {
    protected static final int FEAT_MASK_TRAILING_COMMA = com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_TRAILING_COMMA.getMask();

    protected static final int[] _icLatin1 = com.fasterxml.jackson.core.io.CharTypes.getInputCodeLatin1();

    protected java.io.Reader _reader;

    protected char[] _inputBuffer;

    protected boolean _bufferRecyclable;

    protected com.fasterxml.jackson.core.ObjectCodec _objectCodec;

    protected final com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer _symbols;

    protected final int _hashSeed;

    protected boolean _tokenIncomplete;

    protected long _nameStartOffset;

    protected int _nameStartRow;

    protected int _nameStartCol;

    public ReaderBasedJsonParser(com.fasterxml.jackson.core.io.IOContext ctxt, int features, java.io.Reader r, com.fasterxml.jackson.core.ObjectCodec codec, com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable) {
        super(ctxt, features);
        _reader = r;
        _inputBuffer = inputBuffer;
        _inputPtr = start;
        _inputEnd = end;
        _objectCodec = codec;
        _symbols = st;
        _hashSeed = st.hashSeed();
        _bufferRecyclable = bufferRecyclable;
    }

    public ReaderBasedJsonParser(com.fasterxml.jackson.core.io.IOContext ctxt, int features, java.io.Reader r, com.fasterxml.jackson.core.ObjectCodec codec, com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer st) {
        super(ctxt, features);
        _reader = r;
        _inputBuffer = ctxt.allocTokenBuffer();
        _inputPtr = 0;
        _inputEnd = 0;
        _objectCodec = codec;
        _symbols = st;
        _hashSeed = st.hashSeed();
        _bufferRecyclable = true;
    }

    @java.lang.Override
    public com.fasterxml.jackson.core.ObjectCodec getCodec() {
        return _objectCodec;
    }

    @java.lang.Override
    public void setCodec(com.fasterxml.jackson.core.ObjectCodec c) {
        _objectCodec = c;
    }

    @java.lang.Override
    public int releaseBuffered(java.io.Writer w) throws java.io.IOException {
        int count = (_inputEnd) - (_inputPtr);
        if (count < 1) {
            return 0;
        }
        int origPtr = _inputPtr;
        w.write(_inputBuffer, origPtr, count);
        return count;
    }

    @java.lang.Override
    public java.lang.Object getInputSource() {
        return _reader;
    }

    @java.lang.Deprecated
    protected char getNextChar(java.lang.String eofMsg) throws java.io.IOException {
        return getNextChar(eofMsg, null);
    }

    protected char getNextChar(java.lang.String eofMsg, com.fasterxml.jackson.core.JsonToken forToken) throws java.io.IOException {
        if ((_inputPtr) >= (_inputEnd)) {
            if (!(_loadMore())) {
                _reportInvalidEOF(eofMsg, forToken);
            }
        }
        return _inputBuffer[((_inputPtr)++)];
    }

    @java.lang.Override
    protected void _closeInput() throws java.io.IOException {
        if ((_reader) != null) {
            if ((_ioContext.isResourceManaged()) || (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE))) {
                _reader.close();
            }
            _reader = null;
        }
    }

    @java.lang.Override
    protected void _releaseBuffers() throws java.io.IOException {
        super._releaseBuffers();
        _symbols.release();
        if (_bufferRecyclable) {
            char[] buf = _inputBuffer;
            if (buf != null) {
                _inputBuffer = null;
                _ioContext.releaseTokenBuffer(buf);
            }
        }
    }

    protected void _loadMoreGuaranteed() throws java.io.IOException {
        if (!(_loadMore())) {
            _reportInvalidEOF();
        }
    }

    protected boolean _loadMore() throws java.io.IOException {
        final int bufSize = _inputEnd;
        _currInputProcessed += bufSize;
        _currInputRowStart -= bufSize;
        _nameStartOffset -= bufSize;
        if ((_reader) != null) {
            int count = _reader.read(_inputBuffer, 0, _inputBuffer.length);
            if (count > 0) {
                _inputPtr = 0;
                _inputEnd = count;
                return true;
            }
            _closeInput();
            if (count == 0) {
                throw new java.io.IOException(("Reader returned 0 characters when trying to read " + (_inputEnd)));
            }
        }
        return false;
    }

    @java.lang.Override
    public final java.lang.String getText() throws java.io.IOException {
        com.fasterxml.jackson.core.JsonToken t = _currToken;
        if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
            if (_tokenIncomplete) {
                _tokenIncomplete = false;
                _finishString();
            }
            return _textBuffer.contentsAsString();
        }
        return _getText2(t);
    }

    @java.lang.Override
    public int getText(java.io.Writer writer) throws java.io.IOException {
        com.fasterxml.jackson.core.JsonToken t = _currToken;
        if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
            if (_tokenIncomplete) {
                _tokenIncomplete = false;
                _finishString();
            }
            return _textBuffer.contentsToWriter(writer);
        }
        if (t == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            java.lang.String n = _parsingContext.getCurrentName();
            writer.write(n);
            return n.length();
        }
        if (t != null) {
            if (t.isNumeric()) {
                return _textBuffer.contentsToWriter(writer);
            }
            char[] ch = t.asCharArray();
            writer.write(ch);
            return ch.length;
        }
        return 0;
    }

    @java.lang.Override
    public final java.lang.String getValueAsString() throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
            if (_tokenIncomplete) {
                _tokenIncomplete = false;
                _finishString();
            }
            return _textBuffer.contentsAsString();
        }
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            return getCurrentName();
        }
        return super.getValueAsString(null);
    }

    @java.lang.Override
    public final java.lang.String getValueAsString(java.lang.String defValue) throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
            if (_tokenIncomplete) {
                _tokenIncomplete = false;
                _finishString();
            }
            return _textBuffer.contentsAsString();
        }
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            return getCurrentName();
        }
        return super.getValueAsString(defValue);
    }

    protected final java.lang.String _getText2(com.fasterxml.jackson.core.JsonToken t) {
        if (t == null) {
            return null;
        }
        switch (t.id()) {
            case com.fasterxml.jackson.core.JsonTokenId.ID_FIELD_NAME :
                return _parsingContext.getCurrentName();
            case com.fasterxml.jackson.core.JsonTokenId.ID_STRING :
            case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_INT :
            case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_FLOAT :
                return _textBuffer.contentsAsString();
            default :
                return t.asString();
        }
    }

    @java.lang.Override
    public final char[] getTextCharacters() throws java.io.IOException {
        if ((_currToken) != null) {
            switch (_currToken.id()) {
                case com.fasterxml.jackson.core.JsonTokenId.ID_FIELD_NAME :
                    if (!(_nameCopied)) {
                        java.lang.String name = _parsingContext.getCurrentName();
                        int nameLen = name.length();
                        if ((_nameCopyBuffer) == null) {
                            _nameCopyBuffer = _ioContext.allocNameCopyBuffer(nameLen);
                        }else
                            if ((_nameCopyBuffer.length) < nameLen) {
                                _nameCopyBuffer = new char[nameLen];
                            }

                        name.getChars(0, nameLen, _nameCopyBuffer, 0);
                        _nameCopied = true;
                    }
                    return _nameCopyBuffer;
                case com.fasterxml.jackson.core.JsonTokenId.ID_STRING :
                    if (_tokenIncomplete) {
                        _tokenIncomplete = false;
                        _finishString();
                    }
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_INT :
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_FLOAT :
                    return _textBuffer.getTextBuffer();
                default :
                    return _currToken.asCharArray();
            }
        }
        return null;
    }

    @java.lang.Override
    public final int getTextLength() throws java.io.IOException {
        if ((_currToken) != null) {
            switch (_currToken.id()) {
                case com.fasterxml.jackson.core.JsonTokenId.ID_FIELD_NAME :
                    return _parsingContext.getCurrentName().length();
                case com.fasterxml.jackson.core.JsonTokenId.ID_STRING :
                    if (_tokenIncomplete) {
                        _tokenIncomplete = false;
                        _finishString();
                    }
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_INT :
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_FLOAT :
                    return _textBuffer.size();
                default :
                    return _currToken.asCharArray().length;
            }
        }
        return 0;
    }

    @java.lang.Override
    public final int getTextOffset() throws java.io.IOException {
        if ((_currToken) != null) {
            switch (_currToken.id()) {
                case com.fasterxml.jackson.core.JsonTokenId.ID_FIELD_NAME :
                    return 0;
                case com.fasterxml.jackson.core.JsonTokenId.ID_STRING :
                    if (_tokenIncomplete) {
                        _tokenIncomplete = false;
                        _finishString();
                    }
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_INT :
                case com.fasterxml.jackson.core.JsonTokenId.ID_NUMBER_FLOAT :
                    return _textBuffer.getTextOffset();
                default :
            }
        }
        return 0;
    }

    @java.lang.Override
    public byte[] getBinaryValue(com.fasterxml.jackson.core.Base64Variant b64variant) throws java.io.IOException {
        if (((_currToken) == (com.fasterxml.jackson.core.JsonToken.VALUE_EMBEDDED_OBJECT)) && ((_binaryValue) != null)) {
            return _binaryValue;
        }
        if ((_currToken) != (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
            _reportError((("Current token (" + (_currToken)) + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary"));
        }
        if (_tokenIncomplete) {
            try {
                _binaryValue = _decodeBase64(b64variant);
            } catch (java.lang.IllegalArgumentException iae) {
                throw _constructError(((("Failed to decode VALUE_STRING as base64 (" + b64variant) + "): ") + (iae.getMessage())));
            }
            _tokenIncomplete = false;
        }else {
            if ((_binaryValue) == null) {
                @java.lang.SuppressWarnings("resource")
                com.fasterxml.jackson.core.util.ByteArrayBuilder builder = _getByteArrayBuilder();
                _decodeBase64(getText(), builder, b64variant);
                _binaryValue = builder.toByteArray();
            }
        }
        return _binaryValue;
    }

    @java.lang.Override
    public int readBinaryValue(com.fasterxml.jackson.core.Base64Variant b64variant, java.io.OutputStream out) throws java.io.IOException {
        if ((!(_tokenIncomplete)) || ((_currToken) != (com.fasterxml.jackson.core.JsonToken.VALUE_STRING))) {
            byte[] b = getBinaryValue(b64variant);
            out.write(b);
            return b.length;
        }
        byte[] buf = _ioContext.allocBase64Buffer();
        try {
            return _readBinary(b64variant, out, buf);
        } finally {
            _ioContext.releaseBase64Buffer(buf);
        }
    }

    protected int _readBinary(com.fasterxml.jackson.core.Base64Variant b64variant, java.io.OutputStream out, byte[] buffer) throws java.io.IOException {
        int outputPtr = 0;
        final int outputEnd = (buffer.length) - 3;
        int outputCount = 0;
        while (true) {
            char ch;
            do {
                if ((_inputPtr) >= (_inputEnd)) {
                    _loadMoreGuaranteed();
                }
                ch = _inputBuffer[((_inputPtr)++)];
            } while (ch <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE) );
            int bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (ch == '"') {
                    break;
                }
                bits = _decodeBase64Escape(b64variant, ch, 0);
                if (bits < 0) {
                    continue;
                }
            }
            if (outputPtr > outputEnd) {
                outputCount += outputPtr;
                out.write(buffer, 0, outputPtr);
                outputPtr = 0;
            }
            int decodedData = bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                bits = _decodeBase64Escape(b64variant, ch, 1);
            }
            decodedData = (decodedData << 6) | bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (bits != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if (ch == '"') {
                        decodedData >>= 4;
                        buffer[(outputPtr++)] = ((byte) (decodedData));
                        if (b64variant.usesPadding()) {
                            --(_inputPtr);
                            _handleBase64MissingPadding(b64variant);
                        }
                        break;
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 2);
                }
                if (bits == (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if ((_inputPtr) >= (_inputEnd)) {
                        _loadMoreGuaranteed();
                    }
                    ch = _inputBuffer[((_inputPtr)++)];
                    if (!(b64variant.usesPaddingChar(ch))) {
                        if ((_decodeBase64Escape(b64variant, ch, 3)) != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                            throw reportInvalidBase64Char(b64variant, ch, 3, (("expected padding character '" + (b64variant.getPaddingChar())) + "'"));
                        }
                    }
                    decodedData >>= 4;
                    buffer[(outputPtr++)] = ((byte) (decodedData));
                    continue;
                }
            }
            decodedData = (decodedData << 6) | bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (bits != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if (ch == '"') {
                        decodedData >>= 2;
                        buffer[(outputPtr++)] = ((byte) (decodedData >> 8));
                        buffer[(outputPtr++)] = ((byte) (decodedData));
                        if (b64variant.usesPadding()) {
                            --(_inputPtr);
                            _handleBase64MissingPadding(b64variant);
                        }
                        break;
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 3);
                }
                if (bits == (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    decodedData >>= 2;
                    buffer[(outputPtr++)] = ((byte) (decodedData >> 8));
                    buffer[(outputPtr++)] = ((byte) (decodedData));
                    continue;
                }
            }
            decodedData = (decodedData << 6) | bits;
            buffer[(outputPtr++)] = ((byte) (decodedData >> 16));
            buffer[(outputPtr++)] = ((byte) (decodedData >> 8));
            buffer[(outputPtr++)] = ((byte) (decodedData));
        } 
        _tokenIncomplete = false;
        if (outputPtr > 0) {
            outputCount += outputPtr;
            out.write(buffer, 0, outputPtr);
        }
        return outputCount;
    }

    @java.lang.Override
    public final com.fasterxml.jackson.core.JsonToken nextToken() throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            return _nextAfterName();
        }
        _numTypesValid = com.fasterxml.jackson.core.base.ParserMinimalBase.NR_UNKNOWN;
        if (_tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            return _currToken = null;
        }
        _binaryValue = null;
        if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
            _closeScope(i);
            return _currToken;
        }
        if (_parsingContext.expectComma()) {
            i = _skipComma(i);
            if (((_features) & (com.fasterxml.jackson.core.json.ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA)) != 0) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
                    _closeScope(i);
                    return _currToken;
                }
            }
        }
        boolean inObject = _parsingContext.inObject();
        if (inObject) {
            _updateNameLocation();
            java.lang.String name = (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) ? _parseName() : _handleOddName(i);
            _parsingContext.setCurrentName(name);
            _currToken = com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
            i = _skipColon();
        }
        _updateLocation();
        com.fasterxml.jackson.core.JsonToken t;
        switch (i) {
            case '"' :
                _tokenIncomplete = true;
                t = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
                break;
            case '[' :
                if (!inObject) {
                    _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
                }
                t = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
                break;
            case '{' :
                if (!inObject) {
                    _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                }
                t = com.fasterxml.jackson.core.JsonToken.START_OBJECT;
                break;
            case '}' :
                _reportUnexpectedChar(i, "expected a value");
            case 't' :
                _matchTrue();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
                break;
            case 'f' :
                _matchFalse();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
                break;
            case 'n' :
                _matchNull();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                break;
            case '-' :
                t = _parseNegNumber();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                t = _parsePosNumber(i);
                break;
            default :
                t = _handleOddValue(i);
                break;
        }
        if (inObject) {
            _nextToken = t;
            return _currToken;
        }
        _currToken = t;
        return t;
    }

    private final com.fasterxml.jackson.core.JsonToken _nextAfterName() {
        _nameCopied = false;
        com.fasterxml.jackson.core.JsonToken t = _nextToken;
        _nextToken = null;
        if (t == (com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
            _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
        }else
            if (t == (com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
            }

        return _currToken = t;
    }

    @java.lang.Override
    public void finishToken() throws java.io.IOException {
        if (_tokenIncomplete) {
            _tokenIncomplete = false;
            _finishString();
        }
    }

    @java.lang.Override
    public boolean nextFieldName(com.fasterxml.jackson.core.SerializableString sstr) throws java.io.IOException {
        _numTypesValid = com.fasterxml.jackson.core.base.ParserMinimalBase.NR_UNKNOWN;
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nextAfterName();
            return false;
        }
        if (_tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            _currToken = null;
            return false;
        }
        _binaryValue = null;
        if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
            _closeScope(i);
            return false;
        }
        if (_parsingContext.expectComma()) {
            i = _skipComma(i);
            if (((_features) & (com.fasterxml.jackson.core.json.ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA)) != 0) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
                    _closeScope(i);
                    return false;
                }
            }
        }
        if (!(_parsingContext.inObject())) {
            _updateLocation();
            _nextTokenNotInObject(i);
            return false;
        }
        _updateNameLocation();
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
            char[] nameChars = sstr.asQuotedChars();
            final int len = nameChars.length;
            if ((((_inputPtr) + len) + 4) < (_inputEnd)) {
                final int end = (_inputPtr) + len;
                if ((_inputBuffer[end]) == '"') {
                    int offset = 0;
                    int ptr = _inputPtr;
                    while (true) {
                        if (ptr == end) {
                            _parsingContext.setCurrentName(sstr.getValue());
                            _isNextTokenNameYes(_skipColonFast((ptr + 1)));
                            return true;
                        }
                        if ((nameChars[offset]) != (_inputBuffer[ptr])) {
                            break;
                        }
                        ++offset;
                        ++ptr;
                    } 
                }
            }
        }
        return _isNextTokenNameMaybe(i, sstr.getValue());
    }

    @java.lang.Override
    public java.lang.String nextFieldName() throws java.io.IOException {
        _numTypesValid = com.fasterxml.jackson.core.base.ParserMinimalBase.NR_UNKNOWN;
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nextAfterName();
            return null;
        }
        if (_tokenIncomplete) {
            _skipString();
        }
        int i = _skipWSOrEnd();
        if (i < 0) {
            close();
            _currToken = null;
            return null;
        }
        _binaryValue = null;
        if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
            _closeScope(i);
            return null;
        }
        if (_parsingContext.expectComma()) {
            i = _skipComma(i);
            if (((_features) & (com.fasterxml.jackson.core.json.ReaderBasedJsonParser.FEAT_MASK_TRAILING_COMMA)) != 0) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY))) {
                    _closeScope(i);
                    return null;
                }
            }
        }
        if (!(_parsingContext.inObject())) {
            _updateLocation();
            _nextTokenNotInObject(i);
            return null;
        }
        _updateNameLocation();
        java.lang.String name = (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) ? _parseName() : _handleOddName(i);
        _parsingContext.setCurrentName(name);
        _currToken = com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
        i = _skipColon();
        _updateLocation();
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
            _tokenIncomplete = true;
            _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
            return name;
        }
        com.fasterxml.jackson.core.JsonToken t;
        switch (i) {
            case '-' :
                t = _parseNegNumber();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                t = _parsePosNumber(i);
                break;
            case 'f' :
                _matchFalse();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
                break;
            case 'n' :
                _matchNull();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                break;
            case 't' :
                _matchTrue();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
                break;
            case '[' :
                t = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
                break;
            case '{' :
                t = com.fasterxml.jackson.core.JsonToken.START_OBJECT;
                break;
            default :
                t = _handleOddValue(i);
                break;
        }
        _nextToken = t;
        return name;
    }

    private final void _isNextTokenNameYes(int i) throws java.io.IOException {
        _currToken = com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
        _updateLocation();
        switch (i) {
            case '"' :
                _tokenIncomplete = true;
                _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
                return;
            case '[' :
                _nextToken = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
                return;
            case '{' :
                _nextToken = com.fasterxml.jackson.core.JsonToken.START_OBJECT;
                return;
            case 't' :
                _matchToken("true", 1);
                _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
                return;
            case 'f' :
                _matchToken("false", 1);
                _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
                return;
            case 'n' :
                _matchToken("null", 1);
                _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                return;
            case '-' :
                _nextToken = _parseNegNumber();
                return;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                _nextToken = _parsePosNumber(i);
                return;
        }
        _nextToken = _handleOddValue(i);
    }

    protected boolean _isNextTokenNameMaybe(int i, java.lang.String nameToMatch) throws java.io.IOException {
        java.lang.String name = (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) ? _parseName() : _handleOddName(i);
        _parsingContext.setCurrentName(name);
        _currToken = com.fasterxml.jackson.core.JsonToken.FIELD_NAME;
        i = _skipColon();
        _updateLocation();
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
            _tokenIncomplete = true;
            _nextToken = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
            return nameToMatch.equals(name);
        }
        com.fasterxml.jackson.core.JsonToken t;
        switch (i) {
            case '-' :
                t = _parseNegNumber();
                break;
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                t = _parsePosNumber(i);
                break;
            case 'f' :
                _matchFalse();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
                break;
            case 'n' :
                _matchNull();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                break;
            case 't' :
                _matchTrue();
                t = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
                break;
            case '[' :
                t = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
                break;
            case '{' :
                t = com.fasterxml.jackson.core.JsonToken.START_OBJECT;
                break;
            default :
                t = _handleOddValue(i);
                break;
        }
        _nextToken = t;
        return nameToMatch.equals(name);
    }

    private final com.fasterxml.jackson.core.JsonToken _nextTokenNotInObject(int i) throws java.io.IOException {
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
            _tokenIncomplete = true;
            return _currToken = com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
        }
        switch (i) {
            case '[' :
                _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
                return _currToken = com.fasterxml.jackson.core.JsonToken.START_ARRAY;
            case '{' :
                _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                return _currToken = com.fasterxml.jackson.core.JsonToken.START_OBJECT;
            case 't' :
                _matchToken("true", 1);
                return _currToken = com.fasterxml.jackson.core.JsonToken.VALUE_TRUE;
            case 'f' :
                _matchToken("false", 1);
                return _currToken = com.fasterxml.jackson.core.JsonToken.VALUE_FALSE;
            case 'n' :
                _matchToken("null", 1);
                return _currToken = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
            case '-' :
                return _currToken = _parseNegNumber();
            case '0' :
            case '1' :
            case '2' :
            case '3' :
            case '4' :
            case '5' :
            case '6' :
            case '7' :
            case '8' :
            case '9' :
                return _currToken = _parsePosNumber(i);
            case ',' :
            case ']' :
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES)) {
                    (_inputPtr)--;
                    return _currToken = com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                }
        }
        return _currToken = _handleOddValue(i);
    }

    @java.lang.Override
    public final java.lang.String nextTextValue() throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nameCopied = false;
            com.fasterxml.jackson.core.JsonToken t = _nextToken;
            _nextToken = null;
            _currToken = t;
            if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
                if (_tokenIncomplete) {
                    _tokenIncomplete = false;
                    _finishString();
                }
                return _textBuffer.contentsAsString();
            }
            if (t == (com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
                _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
            }else
                if (t == (com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                    _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                }

            return null;
        }
        return (nextToken()) == (com.fasterxml.jackson.core.JsonToken.VALUE_STRING) ? getText() : null;
    }

    @java.lang.Override
    public final int nextIntValue(int defaultValue) throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nameCopied = false;
            com.fasterxml.jackson.core.JsonToken t = _nextToken;
            _nextToken = null;
            _currToken = t;
            if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT)) {
                return getIntValue();
            }
            if (t == (com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
                _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
            }else
                if (t == (com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                    _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                }

            return defaultValue;
        }
        return (nextToken()) == (com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT) ? getIntValue() : defaultValue;
    }

    @java.lang.Override
    public final long nextLongValue(long defaultValue) throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nameCopied = false;
            com.fasterxml.jackson.core.JsonToken t = _nextToken;
            _nextToken = null;
            _currToken = t;
            if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT)) {
                return getLongValue();
            }
            if (t == (com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
                _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
            }else
                if (t == (com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                    _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                }

            return defaultValue;
        }
        return (nextToken()) == (com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT) ? getLongValue() : defaultValue;
    }

    @java.lang.Override
    public final java.lang.Boolean nextBooleanValue() throws java.io.IOException {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            _nameCopied = false;
            com.fasterxml.jackson.core.JsonToken t = _nextToken;
            _nextToken = null;
            _currToken = t;
            if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_TRUE)) {
                return java.lang.Boolean.TRUE;
            }
            if (t == (com.fasterxml.jackson.core.JsonToken.VALUE_FALSE)) {
                return java.lang.Boolean.FALSE;
            }
            if (t == (com.fasterxml.jackson.core.JsonToken.START_ARRAY)) {
                _parsingContext = _parsingContext.createChildArrayContext(_tokenInputRow, _tokenInputCol);
            }else
                if (t == (com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                    _parsingContext = _parsingContext.createChildObjectContext(_tokenInputRow, _tokenInputCol);
                }

            return null;
        }
        com.fasterxml.jackson.core.JsonToken t = nextToken();
        if (t != null) {
            int id = t.id();
            if (id == (com.fasterxml.jackson.core.JsonTokenId.ID_TRUE))
                return java.lang.Boolean.TRUE;

            if (id == (com.fasterxml.jackson.core.JsonTokenId.ID_FALSE))
                return java.lang.Boolean.FALSE;

        }
        return null;
    }

    protected final com.fasterxml.jackson.core.JsonToken _parsePosNumber(int ch) throws java.io.IOException {
        int ptr = _inputPtr;
        int startPtr = ptr - 1;
        final int inputLen = _inputEnd;
        if (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) {
            return _parseNumber2(false, startPtr);
        }
        int intLen = 1;
        int_loop : while (true) {
            if (ptr >= inputLen) {
                _inputPtr = startPtr;
                return _parseNumber2(false, startPtr);
            }
            ch = ((int) (_inputBuffer[(ptr++)]));
            if ((ch < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) || (ch > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9))) {
                break int_loop;
            }
            ++intLen;
        } 
        if (((ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_PERIOD)) || (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_e))) || (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_E))) {
            _inputPtr = ptr;
            return _parseFloat(ch, startPtr, ptr, false, intLen);
        }
        --ptr;
        _inputPtr = ptr;
        if (_parsingContext.inRoot()) {
            _verifyRootSpace(ch);
        }
        int len = ptr - startPtr;
        _textBuffer.resetWithShared(_inputBuffer, startPtr, len);
        return resetInt(false, intLen);
    }

    private final com.fasterxml.jackson.core.JsonToken _parseFloat(int ch, int startPtr, int ptr, boolean neg, int intLen) throws java.io.IOException {
        final int inputLen = _inputEnd;
        int fractLen = 0;
        if (ch == '.') {
            fract_loop : while (true) {
                if (ptr >= inputLen) {
                    return _parseNumber2(neg, startPtr);
                }
                ch = ((int) (_inputBuffer[(ptr++)]));
                if ((ch < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) || (ch > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9))) {
                    break fract_loop;
                }
                ++fractLen;
            } 
            if (fractLen == 0) {
                reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
            }
        }
        int expLen = 0;
        if ((ch == 'e') || (ch == 'E')) {
            if (ptr >= inputLen) {
                _inputPtr = startPtr;
                return _parseNumber2(neg, startPtr);
            }
            ch = ((int) (_inputBuffer[(ptr++)]));
            if ((ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_MINUS)) || (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_PLUS))) {
                if (ptr >= inputLen) {
                    _inputPtr = startPtr;
                    return _parseNumber2(neg, startPtr);
                }
                ch = ((int) (_inputBuffer[(ptr++)]));
            }
            while ((ch <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9)) && (ch >= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0))) {
                ++expLen;
                if (ptr >= inputLen) {
                    _inputPtr = startPtr;
                    return _parseNumber2(neg, startPtr);
                }
                ch = ((int) (_inputBuffer[(ptr++)]));
            } 
            if (expLen == 0) {
                reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
            }
        }
        --ptr;
        _inputPtr = ptr;
        if (_parsingContext.inRoot()) {
            _verifyRootSpace(ch);
        }
        int len = ptr - startPtr;
        _textBuffer.resetWithShared(_inputBuffer, startPtr, len);
        return resetFloat(neg, intLen, fractLen, expLen);
    }

    protected final com.fasterxml.jackson.core.JsonToken _parseNegNumber() throws java.io.IOException {
        int ptr = _inputPtr;
        int startPtr = ptr - 1;
        final int inputLen = _inputEnd;
        if (ptr >= inputLen) {
            return _parseNumber2(true, startPtr);
        }
        int ch = _inputBuffer[(ptr++)];
        if ((ch > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9)) || (ch < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0))) {
            _inputPtr = ptr;
            return _handleInvalidNumberStart(ch, true);
        }
        if (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) {
            return _parseNumber2(true, startPtr);
        }
        int intLen = 1;
        int_loop : while (true) {
            if (ptr >= inputLen) {
                return _parseNumber2(true, startPtr);
            }
            ch = ((int) (_inputBuffer[(ptr++)]));
            if ((ch < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) || (ch > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9))) {
                break int_loop;
            }
            ++intLen;
        } 
        if (((ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_PERIOD)) || (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_e))) || (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_E))) {
            _inputPtr = ptr;
            return _parseFloat(ch, startPtr, ptr, true, intLen);
        }
        --ptr;
        _inputPtr = ptr;
        if (_parsingContext.inRoot()) {
            _verifyRootSpace(ch);
        }
        int len = ptr - startPtr;
        _textBuffer.resetWithShared(_inputBuffer, startPtr, len);
        return resetInt(true, intLen);
    }

    private final com.fasterxml.jackson.core.JsonToken _parseNumber2(boolean neg, int startPtr) throws java.io.IOException {
        _inputPtr = (neg) ? startPtr + 1 : startPtr;
        char[] outBuf = _textBuffer.emptyAndGetCurrentSegment();
        int outPtr = 0;
        if (neg) {
            outBuf[(outPtr++)] = '-';
        }
        int intLen = 0;
        char c = ((_inputPtr) < (_inputEnd)) ? _inputBuffer[((_inputPtr)++)] : getNextChar("No digit following minus sign", com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT);
        if (c == '0') {
            c = _verifyNoLeadingZeroes();
        }
        boolean eof = false;
        int_loop : while ((c >= '0') && (c <= '9')) {
            ++intLen;
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[(outPtr++)] = c;
            if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
                c = com.fasterxml.jackson.core.base.ParserMinimalBase.CHAR_NULL;
                eof = true;
                break int_loop;
            }
            c = _inputBuffer[((_inputPtr)++)];
        } 
        if (intLen == 0) {
            return _handleInvalidNumberStart(c, neg);
        }
        int fractLen = 0;
        if (c == '.') {
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[(outPtr++)] = c;
            fract_loop : while (true) {
                if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
                    eof = true;
                    break fract_loop;
                }
                c = _inputBuffer[((_inputPtr)++)];
                if ((c < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) || (c > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9))) {
                    break fract_loop;
                }
                ++fractLen;
                if (outPtr >= (outBuf.length)) {
                    outBuf = _textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                outBuf[(outPtr++)] = c;
            } 
            if (fractLen == 0) {
                reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
            }
        }
        int expLen = 0;
        if ((c == 'e') || (c == 'E')) {
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[(outPtr++)] = c;
            c = ((_inputPtr) < (_inputEnd)) ? _inputBuffer[((_inputPtr)++)] : getNextChar("expected a digit for number exponent");
            if ((c == '-') || (c == '+')) {
                if (outPtr >= (outBuf.length)) {
                    outBuf = _textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                outBuf[(outPtr++)] = c;
                c = ((_inputPtr) < (_inputEnd)) ? _inputBuffer[((_inputPtr)++)] : getNextChar("expected a digit for number exponent");
            }
            exp_loop : while ((c <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_9)) && (c >= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0))) {
                ++expLen;
                if (outPtr >= (outBuf.length)) {
                    outBuf = _textBuffer.finishCurrentSegment();
                    outPtr = 0;
                }
                outBuf[(outPtr++)] = c;
                if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
                    eof = true;
                    break exp_loop;
                }
                c = _inputBuffer[((_inputPtr)++)];
            } 
            if (expLen == 0) {
                reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
            }
        }
        if (!eof) {
            --(_inputPtr);
            if (_parsingContext.inRoot()) {
                _verifyRootSpace(c);
            }
        }
        _textBuffer.setCurrentLength(outPtr);
        return reset(neg, intLen, fractLen, expLen);
    }

    private final char _verifyNoLeadingZeroes() throws java.io.IOException {
        if ((_inputPtr) < (_inputEnd)) {
            char ch = _inputBuffer[_inputPtr];
            if ((ch < '0') || (ch > '9')) {
                return '0';
            }
        }
        return _verifyNLZ2();
    }

    private char _verifyNLZ2() throws java.io.IOException {
        if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
            return '0';
        }
        char ch = _inputBuffer[_inputPtr];
        if ((ch < '0') || (ch > '9')) {
            return '0';
        }
        if (!(isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS))) {
            reportInvalidNumber("Leading zeroes not allowed");
        }
        ++(_inputPtr);
        if (ch == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_0)) {
            while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
                ch = _inputBuffer[_inputPtr];
                if ((ch < '0') || (ch > '9')) {
                    return '0';
                }
                ++(_inputPtr);
                if (ch != '0') {
                    break;
                }
            } 
        }
        return ch;
    }

    protected com.fasterxml.jackson.core.JsonToken _handleInvalidNumberStart(int ch, boolean negative) throws java.io.IOException {
        if (ch == 'I') {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    _reportInvalidEOFInValue(com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT);
                }
            }
            ch = _inputBuffer[((_inputPtr)++)];
            if (ch == 'N') {
                java.lang.String match = (negative) ? "-INF" : "+INF";
                _matchToken(match, 3);
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return resetAsNaN(match, (negative ? java.lang.Double.NEGATIVE_INFINITY : java.lang.Double.POSITIVE_INFINITY));
                }
                _reportError((("Non-standard token '" + match) + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow"));
            }else
                if (ch == 'n') {
                    java.lang.String match = (negative) ? "-Infinity" : "+Infinity";
                    _matchToken(match, 3);
                    if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                        return resetAsNaN(match, (negative ? java.lang.Double.NEGATIVE_INFINITY : java.lang.Double.POSITIVE_INFINITY));
                    }
                    _reportError((("Non-standard token '" + match) + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow"));
                }

        }
        reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
        return null;
    }

    private final void _verifyRootSpace(int ch) throws java.io.IOException {
        ++(_inputPtr);
        switch (ch) {
            case ' ' :
            case '\t' :
                return;
            case '\r' :
                _skipCR();
                return;
            case '\n' :
                ++(_currInputRow);
                _currInputRowStart = _inputPtr;
                return;
        }
        _reportMissingRootWS(ch);
    }

    protected final java.lang.String _parseName() throws java.io.IOException {
        int ptr = _inputPtr;
        int hash = _hashSeed;
        final int[] codes = com.fasterxml.jackson.core.json.ReaderBasedJsonParser._icLatin1;
        while (ptr < (_inputEnd)) {
            int ch = _inputBuffer[ptr];
            if ((ch < (codes.length)) && ((codes[ch]) != 0)) {
                if (ch == '"') {
                    int start = _inputPtr;
                    _inputPtr = ptr + 1;
                    return _symbols.findSymbol(_inputBuffer, start, (ptr - start), hash);
                }
                break;
            }
            hash = (hash * (com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.HASH_MULT)) + ch;
            ++ptr;
        } 
        int start = _inputPtr;
        _inputPtr = ptr;
        return _parseName2(start, hash, com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE);
    }

    private java.lang.String _parseName2(int startPtr, int hash, int endChar) throws java.io.IOException {
        _textBuffer.resetWithShared(_inputBuffer, startPtr, ((_inputPtr) - startPtr));
        char[] outBuf = _textBuffer.getCurrentSegment();
        int outPtr = _textBuffer.getCurrentSegmentSize();
        while (true) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    _reportInvalidEOF(" in field name", com.fasterxml.jackson.core.JsonToken.FIELD_NAME);
                }
            }
            char c = _inputBuffer[((_inputPtr)++)];
            int i = ((int) (c));
            if (i <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_BACKSLASH)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_BACKSLASH)) {
                    c = _decodeEscaped();
                }else
                    if (i <= endChar) {
                        if (i == endChar) {
                            break;
                        }
                        if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                            _throwUnquotedSpace(i, "name");
                        }
                    }

            }
            hash = (hash * (com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.HASH_MULT)) + c;
            outBuf[(outPtr++)] = c;
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
        } 
        _textBuffer.setCurrentLength(outPtr);
        {
            com.fasterxml.jackson.core.util.TextBuffer tb = _textBuffer;
            char[] buf = tb.getTextBuffer();
            int start = tb.getTextOffset();
            int len = tb.size();
            return _symbols.findSymbol(buf, start, len, hash);
        }
    }

    protected java.lang.String _handleOddName(int i) throws java.io.IOException {
        if ((i == '\'') && (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES))) {
            return _parseAposName();
        }
        if (!(isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES))) {
            _reportUnexpectedChar(i, "was expecting double-quote to start field name");
        }
        final int[] codes = com.fasterxml.jackson.core.io.CharTypes.getInputCodeLatin1JsNames();
        final int maxCode = codes.length;
        boolean firstOk;
        if (i < maxCode) {
            firstOk = (codes[i]) == 0;
        }else {
            firstOk = java.lang.Character.isJavaIdentifierPart(((char) (i)));
        }
        if (!firstOk) {
            _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
        }
        int ptr = _inputPtr;
        int hash = _hashSeed;
        final int inputLen = _inputEnd;
        if (ptr < inputLen) {
            do {
                int ch = _inputBuffer[ptr];
                if (ch < maxCode) {
                    if ((codes[ch]) != 0) {
                        int start = (_inputPtr) - 1;
                        _inputPtr = ptr;
                        return _symbols.findSymbol(_inputBuffer, start, (ptr - start), hash);
                    }
                }else
                    if (!(java.lang.Character.isJavaIdentifierPart(((char) (ch))))) {
                        int start = (_inputPtr) - 1;
                        _inputPtr = ptr;
                        return _symbols.findSymbol(_inputBuffer, start, (ptr - start), hash);
                    }

                hash = (hash * (com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.HASH_MULT)) + ch;
                ++ptr;
            } while (ptr < inputLen );
        }
        int start = (_inputPtr) - 1;
        _inputPtr = ptr;
        return _handleOddName2(start, hash, codes);
    }

    protected java.lang.String _parseAposName() throws java.io.IOException {
        int ptr = _inputPtr;
        int hash = _hashSeed;
        final int inputLen = _inputEnd;
        if (ptr < inputLen) {
            final int[] codes = com.fasterxml.jackson.core.json.ReaderBasedJsonParser._icLatin1;
            final int maxCode = codes.length;
            do {
                int ch = _inputBuffer[ptr];
                if (ch == '\'') {
                    int start = _inputPtr;
                    _inputPtr = ptr + 1;
                    return _symbols.findSymbol(_inputBuffer, start, (ptr - start), hash);
                }
                if ((ch < maxCode) && ((codes[ch]) != 0)) {
                    break;
                }
                hash = (hash * (com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.HASH_MULT)) + ch;
                ++ptr;
            } while (ptr < inputLen );
        }
        int start = _inputPtr;
        _inputPtr = ptr;
        return _parseName2(start, hash, '\'');
    }

    protected com.fasterxml.jackson.core.JsonToken _handleOddValue(int i) throws java.io.IOException {
        switch (i) {
            case '\'' :
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES)) {
                    return _handleApos();
                }
                break;
            case ']' :
                if (!(_parsingContext.inArray())) {
                    break;
                }
            case ',' :
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_MISSING_VALUES)) {
                    --(_inputPtr);
                    return com.fasterxml.jackson.core.JsonToken.VALUE_NULL;
                }
                break;
            case 'N' :
                _matchToken("NaN", 1);
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return resetAsNaN("NaN", java.lang.Double.NaN);
                }
                _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                break;
            case 'I' :
                _matchToken("Infinity", 1);
                if (isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS)) {
                    return resetAsNaN("Infinity", java.lang.Double.POSITIVE_INFINITY);
                }
                _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
                break;
            case '+' :
                if ((_inputPtr) >= (_inputEnd)) {
                    if (!(_loadMore())) {
                        _reportInvalidEOFInValue(com.fasterxml.jackson.core.JsonToken.VALUE_NUMBER_INT);
                    }
                }
                return _handleInvalidNumberStart(_inputBuffer[((_inputPtr)++)], false);
        }
        if (java.lang.Character.isJavaIdentifierStart(i)) {
            _reportInvalidToken(("" + ((char) (i))), "('true', 'false' or 'null')");
        }
        _reportUnexpectedChar(i, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
        return null;
    }

    protected com.fasterxml.jackson.core.JsonToken _handleApos() throws java.io.IOException {
        char[] outBuf = _textBuffer.emptyAndGetCurrentSegment();
        int outPtr = _textBuffer.getCurrentSegmentSize();
        while (true) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    _reportInvalidEOF(": was expecting closing quote for a string value", com.fasterxml.jackson.core.JsonToken.VALUE_STRING);
                }
            }
            char c = _inputBuffer[((_inputPtr)++)];
            int i = ((int) (c));
            if (i <= '\\') {
                if (i == '\\') {
                    c = _decodeEscaped();
                }else
                    if (i <= '\'') {
                        if (i == '\'') {
                            break;
                        }
                        if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                            _throwUnquotedSpace(i, "string value");
                        }
                    }

            }
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[(outPtr++)] = c;
        } 
        _textBuffer.setCurrentLength(outPtr);
        return com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
    }

    private java.lang.String _handleOddName2(int startPtr, int hash, int[] codes) throws java.io.IOException {
        _textBuffer.resetWithShared(_inputBuffer, startPtr, ((_inputPtr) - startPtr));
        char[] outBuf = _textBuffer.getCurrentSegment();
        int outPtr = _textBuffer.getCurrentSegmentSize();
        final int maxCode = codes.length;
        while (true) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    break;
                }
            }
            char c = _inputBuffer[_inputPtr];
            int i = ((int) (c));
            if (i < maxCode) {
                if ((codes[i]) != 0) {
                    break;
                }
            }else
                if (!(java.lang.Character.isJavaIdentifierPart(c))) {
                    break;
                }

            ++(_inputPtr);
            hash = (hash * (com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer.HASH_MULT)) + i;
            outBuf[(outPtr++)] = c;
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
        } 
        _textBuffer.setCurrentLength(outPtr);
        {
            com.fasterxml.jackson.core.util.TextBuffer tb = _textBuffer;
            char[] buf = tb.getTextBuffer();
            int start = tb.getTextOffset();
            int len = tb.size();
            return _symbols.findSymbol(buf, start, len, hash);
        }
    }

    @java.lang.Override
    protected final void _finishString() throws java.io.IOException {
        int ptr = _inputPtr;
        final int inputLen = _inputEnd;
        if (ptr < inputLen) {
            final int[] codes = com.fasterxml.jackson.core.json.ReaderBasedJsonParser._icLatin1;
            final int maxCode = codes.length;
            do {
                int ch = _inputBuffer[ptr];
                if ((ch < maxCode) && ((codes[ch]) != 0)) {
                    if (ch == '"') {
                        _textBuffer.resetWithShared(_inputBuffer, _inputPtr, (ptr - (_inputPtr)));
                        _inputPtr = ptr + 1;
                        return;
                    }
                    break;
                }
                ++ptr;
            } while (ptr < inputLen );
        }
        _textBuffer.resetWithCopy(_inputBuffer, _inputPtr, (ptr - (_inputPtr)));
        _inputPtr = ptr;
        _finishString2();
    }

    protected void _finishString2() throws java.io.IOException {
        char[] outBuf = _textBuffer.getCurrentSegment();
        int outPtr = _textBuffer.getCurrentSegmentSize();
        final int[] codes = com.fasterxml.jackson.core.json.ReaderBasedJsonParser._icLatin1;
        final int maxCode = codes.length;
        while (true) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    _reportInvalidEOF(": was expecting closing quote for a string value", com.fasterxml.jackson.core.JsonToken.VALUE_STRING);
                }
            }
            char c = _inputBuffer[((_inputPtr)++)];
            int i = ((int) (c));
            if ((i < maxCode) && ((codes[i]) != 0)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
                    break;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_BACKSLASH)) {
                        c = _decodeEscaped();
                    }else
                        if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                            _throwUnquotedSpace(i, "string value");
                        }


            }
            if (outPtr >= (outBuf.length)) {
                outBuf = _textBuffer.finishCurrentSegment();
                outPtr = 0;
            }
            outBuf[(outPtr++)] = c;
        } 
        _textBuffer.setCurrentLength(outPtr);
    }

    protected final void _skipString() throws java.io.IOException {
        _tokenIncomplete = false;
        int inPtr = _inputPtr;
        int inLen = _inputEnd;
        char[] inBuf = _inputBuffer;
        while (true) {
            if (inPtr >= inLen) {
                _inputPtr = inPtr;
                if (!(_loadMore())) {
                    _reportInvalidEOF(": was expecting closing quote for a string value", com.fasterxml.jackson.core.JsonToken.VALUE_STRING);
                }
                inPtr = _inputPtr;
                inLen = _inputEnd;
            }
            char c = inBuf[(inPtr++)];
            int i = ((int) (c));
            if (i <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_BACKSLASH)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_BACKSLASH)) {
                    _inputPtr = inPtr;
                    _decodeEscaped();
                    inPtr = _inputPtr;
                    inLen = _inputEnd;
                }else
                    if (i <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
                        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_QUOTE)) {
                            _inputPtr = inPtr;
                            break;
                        }
                        if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                            _inputPtr = inPtr;
                            _throwUnquotedSpace(i, "string value");
                        }
                    }

            }
        } 
    }

    protected final void _skipCR() throws java.io.IOException {
        if (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            if ((_inputBuffer[_inputPtr]) == '\n') {
                ++(_inputPtr);
            }
        }
        ++(_currInputRow);
        _currInputRowStart = _inputPtr;
    }

    private final int _skipColon() throws java.io.IOException {
        if (((_inputPtr) + 4) >= (_inputEnd)) {
            return _skipColon2(false);
        }
        char c = _inputBuffer[_inputPtr];
        if (c == ':') {
            int i = _inputBuffer[(++(_inputPtr))];
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    return _skipColon2(true);
                }
                ++(_inputPtr);
                return i;
            }
            if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB))) {
                i = ((int) (_inputBuffer[(++(_inputPtr))]));
                if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                    if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                        return _skipColon2(true);
                    }
                    ++(_inputPtr);
                    return i;
                }
            }
            return _skipColon2(true);
        }
        if ((c == ' ') || (c == '\t')) {
            c = _inputBuffer[(++(_inputPtr))];
        }
        if (c == ':') {
            int i = _inputBuffer[(++(_inputPtr))];
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    return _skipColon2(true);
                }
                ++(_inputPtr);
                return i;
            }
            if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB))) {
                i = ((int) (_inputBuffer[(++(_inputPtr))]));
                if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                    if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                        return _skipColon2(true);
                    }
                    ++(_inputPtr);
                    return i;
                }
            }
            return _skipColon2(true);
        }
        return _skipColon2(false);
    }

    private final int _skipColon2(boolean gotColon) throws java.io.IOException {
        while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            int i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) {
                    _skipComment();
                    continue;
                }
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH)) {
                    if (_skipYAMLComment()) {
                        continue;
                    }
                }
                if (gotColon) {
                    return i;
                }
                if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_COLON)) {
                    _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
                }
                gotColon = true;
                continue;
            }
            if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                    ++(_currInputRow);
                    _currInputRowStart = _inputPtr;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                        _skipCR();
                    }else
                        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                            _throwInvalidSpace(i);
                        }


            }
        } 
        _reportInvalidEOF(((" within/between " + (_parsingContext.typeDesc())) + " entries"), null);
        return -1;
    }

    private final int _skipColonFast(int ptr) throws java.io.IOException {
        int i = ((int) (_inputBuffer[(ptr++)]));
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_COLON)) {
            i = _inputBuffer[(ptr++)];
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) && (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    _inputPtr = ptr;
                    return i;
                }
            }else
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB))) {
                    i = ((int) (_inputBuffer[(ptr++)]));
                    if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                        if ((i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) && (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                            _inputPtr = ptr;
                            return i;
                        }
                    }
                }

            _inputPtr = ptr - 1;
            return _skipColon2(true);
        }
        if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB))) {
            i = _inputBuffer[(ptr++)];
        }
        boolean gotColon = i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_COLON);
        if (gotColon) {
            i = _inputBuffer[(ptr++)];
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) && (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    _inputPtr = ptr;
                    return i;
                }
            }else
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB))) {
                    i = ((int) (_inputBuffer[(ptr++)]));
                    if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                        if ((i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) && (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                            _inputPtr = ptr;
                            return i;
                        }
                    }
                }

        }
        _inputPtr = ptr - 1;
        return _skipColon2(gotColon);
    }

    private final int _skipComma(int i) throws java.io.IOException {
        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_COMMA)) {
            _reportUnexpectedChar(i, (("was expecting comma to separate " + (_parsingContext.typeDesc())) + " entries"));
        }
        while ((_inputPtr) < (_inputEnd)) {
            i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    --(_inputPtr);
                    return _skipAfterComma2();
                }
                return i;
            }
            if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                    ++(_currInputRow);
                    _currInputRowStart = _inputPtr;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                        _skipCR();
                    }else
                        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                            _throwInvalidSpace(i);
                        }


            }
        } 
        return _skipAfterComma2();
    }

    private final int _skipAfterComma2() throws java.io.IOException {
        while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            int i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) {
                    _skipComment();
                    continue;
                }
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH)) {
                    if (_skipYAMLComment()) {
                        continue;
                    }
                }
                return i;
            }
            if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                    ++(_currInputRow);
                    _currInputRowStart = _inputPtr;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                        _skipCR();
                    }else
                        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                            _throwInvalidSpace(i);
                        }


            }
        } 
        throw _constructError((("Unexpected end-of-input within/between " + (_parsingContext.typeDesc())) + " entries"));
    }

    private final int _skipWSOrEnd() throws java.io.IOException {
        if ((_inputPtr) >= (_inputEnd)) {
            if (!(_loadMore())) {
                return _eofAsNextChar();
            }
        }
        int i = _inputBuffer[((_inputPtr)++)];
        if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
            if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                --(_inputPtr);
                return _skipWSOrEnd2();
            }
            return i;
        }
        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
            if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                ++(_currInputRow);
                _currInputRowStart = _inputPtr;
            }else
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                    _skipCR();
                }else
                    if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                        _throwInvalidSpace(i);
                    }


        }
        while ((_inputPtr) < (_inputEnd)) {
            i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if ((i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) || (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH))) {
                    --(_inputPtr);
                    return _skipWSOrEnd2();
                }
                return i;
            }
            if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                    ++(_currInputRow);
                    _currInputRowStart = _inputPtr;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                        _skipCR();
                    }else
                        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                            _throwInvalidSpace(i);
                        }


            }
        } 
        return _skipWSOrEnd2();
    }

    private int _skipWSOrEnd2() throws java.io.IOException {
        while (true) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    return _eofAsNextChar();
                }
            }
            int i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i > (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) {
                    _skipComment();
                    continue;
                }
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_HASH)) {
                    if (_skipYAMLComment()) {
                        continue;
                    }
                }
                return i;
            }else
                if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                        ++(_currInputRow);
                        _currInputRowStart = _inputPtr;
                    }else
                        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                            _skipCR();
                        }else
                            if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                                _throwInvalidSpace(i);
                            }


                }

        } 
    }

    private void _skipComment() throws java.io.IOException {
        if (!(isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS))) {
            _reportUnexpectedChar('/', "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
        }
        if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
            _reportInvalidEOF(" in a comment", null);
        }
        char c = _inputBuffer[((_inputPtr)++)];
        if (c == '/') {
            _skipLine();
        }else
            if (c == '*') {
                _skipCComment();
            }else {
                _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
            }

    }

    private void _skipCComment() throws java.io.IOException {
        while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            int i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i <= '*') {
                if (i == '*') {
                    if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
                        break;
                    }
                    if ((_inputBuffer[_inputPtr]) == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SLASH)) {
                        ++(_inputPtr);
                        return;
                    }
                    continue;
                }
                if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                        ++(_currInputRow);
                        _currInputRowStart = _inputPtr;
                    }else
                        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                            _skipCR();
                        }else
                            if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                                _throwInvalidSpace(i);
                            }


                }
            }
        } 
        _reportInvalidEOF(" in a comment", null);
    }

    private boolean _skipYAMLComment() throws java.io.IOException {
        if (!(isEnabled(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_YAML_COMMENTS))) {
            return false;
        }
        _skipLine();
        return true;
    }

    private void _skipLine() throws java.io.IOException {
        while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            int i = ((int) (_inputBuffer[((_inputPtr)++)]));
            if (i < (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE)) {
                if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_LF)) {
                    ++(_currInputRow);
                    _currInputRowStart = _inputPtr;
                    break;
                }else
                    if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_CR)) {
                        _skipCR();
                        break;
                    }else
                        if (i != (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_TAB)) {
                            _throwInvalidSpace(i);
                        }


            }
        } 
    }

    @java.lang.Override
    protected char _decodeEscaped() throws java.io.IOException {
        if ((_inputPtr) >= (_inputEnd)) {
            if (!(_loadMore())) {
                _reportInvalidEOF(" in character escape sequence", com.fasterxml.jackson.core.JsonToken.VALUE_STRING);
            }
        }
        char c = _inputBuffer[((_inputPtr)++)];
        switch (((int) (c))) {
            case 'b' :
                return '\b';
            case 't' :
                return '\t';
            case 'n' :
                return '\n';
            case 'f' :
                return '\f';
            case 'r' :
                return '\r';
            case '"' :
            case '/' :
            case '\\' :
                return c;
            case 'u' :
                break;
            default :
                return _handleUnrecognizedCharacterEscape(c);
        }
        int value = 0;
        for (int i = 0; i < 4; ++i) {
            if ((_inputPtr) >= (_inputEnd)) {
                if (!(_loadMore())) {
                    _reportInvalidEOF(" in character escape sequence", com.fasterxml.jackson.core.JsonToken.VALUE_STRING);
                }
            }
            int ch = ((int) (_inputBuffer[((_inputPtr)++)]));
            int digit = com.fasterxml.jackson.core.io.CharTypes.charToHex(ch);
            if (digit < 0) {
                _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
            }
            value = (value << 4) | digit;
        }
        return ((char) (value));
    }

    private final void _matchTrue() throws java.io.IOException {
        int ptr = _inputPtr;
        if ((ptr + 3) < (_inputEnd)) {
            final char[] b = _inputBuffer;
            if ((((b[ptr]) == 'r') && ((b[(++ptr)]) == 'u')) && ((b[(++ptr)]) == 'e')) {
                char c = b[(++ptr)];
                if (((c < '0') || (c == ']')) || (c == '}')) {
                    _inputPtr = ptr;
                    return;
                }
            }
        }
        _matchToken("true", 1);
    }

    private final void _matchFalse() throws java.io.IOException {
        int ptr = _inputPtr;
        if ((ptr + 4) < (_inputEnd)) {
            final char[] b = _inputBuffer;
            if (((((b[ptr]) == 'a') && ((b[(++ptr)]) == 'l')) && ((b[(++ptr)]) == 's')) && ((b[(++ptr)]) == 'e')) {
                char c = b[(++ptr)];
                if (((c < '0') || (c == ']')) || (c == '}')) {
                    _inputPtr = ptr;
                    return;
                }
            }
        }
        _matchToken("false", 1);
    }

    private final void _matchNull() throws java.io.IOException {
        int ptr = _inputPtr;
        if ((ptr + 3) < (_inputEnd)) {
            final char[] b = _inputBuffer;
            if ((((b[ptr]) == 'u') && ((b[(++ptr)]) == 'l')) && ((b[(++ptr)]) == 'l')) {
                char c = b[(++ptr)];
                if (((c < '0') || (c == ']')) || (c == '}')) {
                    _inputPtr = ptr;
                    return;
                }
            }
        }
        _matchToken("null", 1);
    }

    protected final void _matchToken(java.lang.String matchStr, int i) throws java.io.IOException {
        final int len = matchStr.length();
        if (((_inputPtr) + len) >= (_inputEnd)) {
            _matchToken2(matchStr, i);
            return;
        }
        do {
            if ((_inputBuffer[_inputPtr]) != (matchStr.charAt(i))) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            ++(_inputPtr);
        } while ((++i) < len );
        int ch = _inputBuffer[_inputPtr];
        if (((ch >= '0') && (ch != ']')) && (ch != '}')) {
            _checkMatchEnd(matchStr, i, ch);
        }
    }

    private final void _matchToken2(java.lang.String matchStr, int i) throws java.io.IOException {
        final int len = matchStr.length();
        do {
            if ((((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) || ((_inputBuffer[_inputPtr]) != (matchStr.charAt(i)))) {
                _reportInvalidToken(matchStr.substring(0, i));
            }
            ++(_inputPtr);
        } while ((++i) < len );
        if (((_inputPtr) >= (_inputEnd)) && (!(_loadMore()))) {
            return;
        }
        int ch = _inputBuffer[_inputPtr];
        if (((ch >= '0') && (ch != ']')) && (ch != '}')) {
            _checkMatchEnd(matchStr, i, ch);
        }
    }

    private final void _checkMatchEnd(java.lang.String matchStr, int i, int c) throws java.io.IOException {
        char ch = ((char) (c));
        if (java.lang.Character.isJavaIdentifierPart(ch)) {
            _reportInvalidToken(matchStr.substring(0, i));
        }
    }

    @java.lang.SuppressWarnings("resource")
    protected byte[] _decodeBase64(com.fasterxml.jackson.core.Base64Variant b64variant) throws java.io.IOException {
        com.fasterxml.jackson.core.util.ByteArrayBuilder builder = _getByteArrayBuilder();
        while (true) {
            char ch;
            do {
                if ((_inputPtr) >= (_inputEnd)) {
                    _loadMoreGuaranteed();
                }
                ch = _inputBuffer[((_inputPtr)++)];
            } while (ch <= (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_SPACE) );
            int bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (ch == '"') {
                    return builder.toByteArray();
                }
                bits = _decodeBase64Escape(b64variant, ch, 0);
                if (bits < 0) {
                    continue;
                }
            }
            int decodedData = bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                bits = _decodeBase64Escape(b64variant, ch, 1);
            }
            decodedData = (decodedData << 6) | bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (bits != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if (ch == '"') {
                        decodedData >>= 4;
                        builder.append(decodedData);
                        if (b64variant.usesPadding()) {
                            --(_inputPtr);
                            _handleBase64MissingPadding(b64variant);
                        }
                        return builder.toByteArray();
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 2);
                }
                if (bits == (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if ((_inputPtr) >= (_inputEnd)) {
                        _loadMoreGuaranteed();
                    }
                    ch = _inputBuffer[((_inputPtr)++)];
                    if (!(b64variant.usesPaddingChar(ch))) {
                        if ((_decodeBase64Escape(b64variant, ch, 3)) != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                            throw reportInvalidBase64Char(b64variant, ch, 3, (("expected padding character '" + (b64variant.getPaddingChar())) + "'"));
                        }
                    }
                    decodedData >>= 4;
                    builder.append(decodedData);
                    continue;
                }
            }
            decodedData = (decodedData << 6) | bits;
            if ((_inputPtr) >= (_inputEnd)) {
                _loadMoreGuaranteed();
            }
            ch = _inputBuffer[((_inputPtr)++)];
            bits = b64variant.decodeBase64Char(ch);
            if (bits < 0) {
                if (bits != (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    if (ch == '"') {
                        decodedData >>= 2;
                        builder.appendTwoBytes(decodedData);
                        if (b64variant.usesPadding()) {
                            --(_inputPtr);
                            _handleBase64MissingPadding(b64variant);
                        }
                        return builder.toByteArray();
                    }
                    bits = _decodeBase64Escape(b64variant, ch, 3);
                }
                if (bits == (com.fasterxml.jackson.core.Base64Variant.BASE64_VALUE_PADDING)) {
                    decodedData >>= 2;
                    builder.appendTwoBytes(decodedData);
                    continue;
                }
            }
            decodedData = (decodedData << 6) | bits;
            builder.appendThreeBytes(decodedData);
        } 
    }

    @java.lang.Override
    public com.fasterxml.jackson.core.JsonLocation getTokenLocation() {
        if ((_currToken) == (com.fasterxml.jackson.core.JsonToken.FIELD_NAME)) {
            long total = (_currInputProcessed) + ((_nameStartOffset) - 1);
            return new com.fasterxml.jackson.core.JsonLocation(_getSourceReference(), (-1L), total, _nameStartRow, _nameStartCol);
        }
        return new com.fasterxml.jackson.core.JsonLocation(_getSourceReference(), (-1L), ((_tokenInputTotal) - 1), _tokenInputRow, _tokenInputCol);
    }

    @java.lang.Override
    public com.fasterxml.jackson.core.JsonLocation getCurrentLocation() {
        int col = ((_inputPtr) - (_currInputRowStart)) + 1;
        return new com.fasterxml.jackson.core.JsonLocation(_getSourceReference(), (-1L), ((_currInputProcessed) + (_inputPtr)), _currInputRow, col);
    }

    private final void _updateLocation() {
        int ptr = _inputPtr;
        _tokenInputTotal = (_currInputProcessed) + ptr;
        _tokenInputRow = _currInputRow;
        _tokenInputCol = ptr - (_currInputRowStart);
    }

    private final void _updateNameLocation() {
        int ptr = _inputPtr;
        _nameStartOffset = ptr;
        _nameStartRow = _currInputRow;
        _nameStartCol = ptr - (_currInputRowStart);
    }

    protected void _reportInvalidToken(java.lang.String matchedPart) throws java.io.IOException {
        _reportInvalidToken(matchedPart, "'null', 'true', 'false' or NaN");
    }

    protected void _reportInvalidToken(java.lang.String matchedPart, java.lang.String msg) throws java.io.IOException {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(matchedPart);
        while (((_inputPtr) < (_inputEnd)) || (_loadMore())) {
            char c = _inputBuffer[_inputPtr];
            if (!(java.lang.Character.isJavaIdentifierPart(c))) {
                break;
            }
            ++(_inputPtr);
            sb.append(c);
            if ((sb.length()) >= (com.fasterxml.jackson.core.base.ParserMinimalBase.MAX_ERROR_TOKEN_LENGTH)) {
                sb.append("...");
                break;
            }
        } 
        _reportError("Unrecognized token '%s': was expecting %s", sb, msg);
    }

    private void _closeScope(int i) throws com.fasterxml.jackson.core.JsonParseException {
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RBRACKET)) {
            _updateLocation();
            if (!(_parsingContext.inArray())) {
                _reportMismatchedEndMarker(i, '}');
            }
            _parsingContext = _parsingContext.clearAndGetParent();
            _currToken = com.fasterxml.jackson.core.JsonToken.END_ARRAY;
        }
        if (i == (com.fasterxml.jackson.core.base.ParserMinimalBase.INT_RCURLY)) {
            _updateLocation();
            if (!(_parsingContext.inObject())) {
                _reportMismatchedEndMarker(i, ']');
            }
            _parsingContext = _parsingContext.clearAndGetParent();
            _currToken = com.fasterxml.jackson.core.JsonToken.END_OBJECT;
        }
    }
}

