package org.apache.commons.codec.binary;


public class Base64 implements org.apache.commons.codec.BinaryDecoder , org.apache.commons.codec.BinaryEncoder {
    private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static final int MIME_CHUNK_SIZE = 76;

    public static final int PEM_CHUNK_SIZE = 64;

    static final byte[] CHUNK_SEPARATOR = new byte[]{ '\r', '\n' };

    private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

    private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{ 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };

    private static final byte PAD = '=';

    private static final byte[] DECODE_TABLE = new byte[]{ -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };

    private static final int MASK_6BITS = 63;

    private static final int MASK_8BITS = 255;

    private final byte[] encodeTable;

    private final int lineLength;

    private final byte[] lineSeparator;

    private final int decodeSize;

    private final int encodeSize;

    private byte[] buffer;

    private int pos;

    private int readPos;

    private int currentLinePos;

    private int modulus;

    private boolean eof;

    private int x;

    public Base64() {
        this(0);
    }

    public Base64(boolean urlSafe) {
        this(org.apache.commons.codec.binary.Base64.MIME_CHUNK_SIZE, org.apache.commons.codec.binary.Base64.CHUNK_SEPARATOR, urlSafe);
    }

    public Base64(int lineLength) {
        this(lineLength, org.apache.commons.codec.binary.Base64.CHUNK_SEPARATOR);
    }

    public Base64(int lineLength, byte[] lineSeparator) {
        this(lineLength, lineSeparator, false);
    }

    public Base64(int lineLength, byte[] lineSeparator, boolean urlSafe) {
        if (lineSeparator == null) {
            lineLength = 0;
            lineSeparator = org.apache.commons.codec.binary.Base64.CHUNK_SEPARATOR;
        }
        this.lineLength = (lineLength > 0) ? (lineLength / 4) * 4 : 0;
        this.lineSeparator = new byte[lineSeparator.length];
        java.lang.System.arraycopy(lineSeparator, 0, this.lineSeparator, 0, lineSeparator.length);
        if (lineLength > 0) {
            this.encodeSize = 4 + (lineSeparator.length);
        }else {
            this.encodeSize = 4;
        }
        this.decodeSize = (this.encodeSize) - 1;
        if (org.apache.commons.codec.binary.Base64.containsBase64Byte(lineSeparator)) {
            java.lang.String sep = org.apache.commons.codec.binary.StringUtils.newStringUtf8(lineSeparator);
            throw new java.lang.IllegalArgumentException((("lineSeperator must not contain base64 characters: [" + sep) + "]"));
        }
        this.encodeTable = (urlSafe) ? org.apache.commons.codec.binary.Base64.URL_SAFE_ENCODE_TABLE : org.apache.commons.codec.binary.Base64.STANDARD_ENCODE_TABLE;
    }

    public boolean isUrlSafe() {
        return (this.encodeTable) == (org.apache.commons.codec.binary.Base64.URL_SAFE_ENCODE_TABLE);
    }

    boolean hasData() {
        return (this.buffer) != null;
    }

    int avail() {
        return (buffer) != null ? (pos) - (readPos) : 0;
    }

    private void resizeBuffer() {
        if ((buffer) == null) {
            buffer = new byte[org.apache.commons.codec.binary.Base64.DEFAULT_BUFFER_SIZE];
            pos = 0;
            readPos = 0;
        }else {
            byte[] b = new byte[(buffer.length) * (org.apache.commons.codec.binary.Base64.DEFAULT_BUFFER_RESIZE_FACTOR)];
            java.lang.System.arraycopy(buffer, 0, b, 0, buffer.length);
            buffer = b;
        }
    }

    int readResults(byte[] b, int bPos, int bAvail) {
        if ((buffer) != null) {
            int len = java.lang.Math.min(avail(), bAvail);
            if ((buffer) != b) {
                java.lang.System.arraycopy(buffer, readPos, b, bPos, len);
                readPos += len;
                if ((readPos) >= (pos)) {
                    buffer = null;
                }
            }else {
                buffer = null;
            }
            return len;
        }
        return eof ? -1 : 0;
    }

    void setInitialBuffer(byte[] out, int outPos, int outAvail) {
        if ((out != null) && ((out.length) == outAvail)) {
            buffer = out;
            pos = outPos;
            readPos = outPos;
        }
    }

    void encode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        if (inAvail < 0) {
            eof = true;
            if (((buffer) == null) || (((buffer.length) - (pos)) < (encodeSize))) {
                resizeBuffer();
            }
            switch (modulus) {
                case 1 :
                    buffer[((pos)++)] = encodeTable[(((x) >> 2) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[(((x) << 4) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    if ((encodeTable) == (org.apache.commons.codec.binary.Base64.STANDARD_ENCODE_TABLE)) {
                        buffer[((pos)++)] = org.apache.commons.codec.binary.Base64.PAD;
                        buffer[((pos)++)] = org.apache.commons.codec.binary.Base64.PAD;
                    }
                    break;
                case 2 :
                    buffer[((pos)++)] = encodeTable[(((x) >> 10) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[(((x) >> 4) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[(((x) << 2) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    if ((encodeTable) == (org.apache.commons.codec.binary.Base64.STANDARD_ENCODE_TABLE)) {
                        buffer[((pos)++)] = org.apache.commons.codec.binary.Base64.PAD;
                    }
                    break;
            }
            if (((lineLength) > 0) && ((pos) > 0)) {
                java.lang.System.arraycopy(lineSeparator, 0, buffer, pos, lineSeparator.length);
                pos += lineSeparator.length;
            }
        }else {
            for (int i = 0; i < inAvail; i++) {
                if (((buffer) == null) || (((buffer.length) - (pos)) < (encodeSize))) {
                    resizeBuffer();
                }
                modulus = (++(modulus)) % 3;
                int b = in[(inPos++)];
                if (b < 0) {
                    b += 256;
                }
                x = ((x) << 8) + b;
                if (0 == (modulus)) {
                    buffer[((pos)++)] = encodeTable[(((x) >> 18) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[(((x) >> 12) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[(((x) >> 6) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    buffer[((pos)++)] = encodeTable[((x) & (org.apache.commons.codec.binary.Base64.MASK_6BITS))];
                    currentLinePos += 4;
                    if (((lineLength) > 0) && ((lineLength) <= (currentLinePos))) {
                        java.lang.System.arraycopy(lineSeparator, 0, buffer, pos, lineSeparator.length);
                        pos += lineSeparator.length;
                        currentLinePos = 0;
                    }
                }
            }
        }
    }

    void decode(byte[] in, int inPos, int inAvail) {
        if (eof) {
            return;
        }
        if (inAvail < 0) {
            eof = true;
        }
        for (int i = 0; i < inAvail; i++) {
            if (((buffer) == null) || (((buffer.length) - (pos)) < (decodeSize))) {
                resizeBuffer();
            }
            byte b = in[(inPos++)];
            if (b == (org.apache.commons.codec.binary.Base64.PAD)) {
                eof = true;
                break;
            }else {
                if ((b >= 0) && (b < (org.apache.commons.codec.binary.Base64.DECODE_TABLE.length))) {
                    int result = org.apache.commons.codec.binary.Base64.DECODE_TABLE[b];
                    if (result >= 0) {
                        modulus = (++(modulus)) % 4;
                        x = ((x) << 6) + result;
                        if ((modulus) == 0) {
                            buffer[((pos)++)] = ((byte) (((x) >> 16) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                            buffer[((pos)++)] = ((byte) (((x) >> 8) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                            buffer[((pos)++)] = ((byte) ((x) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                        }
                    }
                }
            }
        }
        if ((eof) && ((modulus) != 0)) {
            if (((buffer) == null) || (((buffer.length) - (pos)) < (decodeSize))) {
                resizeBuffer();
            }
            x = (x) << 6;
            switch (modulus) {
                case 2 :
                    x = (x) << 6;
                    buffer[((pos)++)] = ((byte) (((x) >> 16) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                    break;
                case 3 :
                    buffer[((pos)++)] = ((byte) (((x) >> 16) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                    buffer[((pos)++)] = ((byte) (((x) >> 8) & (org.apache.commons.codec.binary.Base64.MASK_8BITS)));
                    break;
            }
        }
    }

    public static boolean isBase64(byte octet) {
        return (octet == (org.apache.commons.codec.binary.Base64.PAD)) || (((octet >= 0) && (octet < (org.apache.commons.codec.binary.Base64.DECODE_TABLE.length))) && ((org.apache.commons.codec.binary.Base64.DECODE_TABLE[octet]) != (-1)));
    }

    public static boolean isArrayByteBase64(byte[] arrayOctet) {
        for (int i = 0; i < (arrayOctet.length); i++) {
            if ((!(org.apache.commons.codec.binary.Base64.isBase64(arrayOctet[i]))) && (!(org.apache.commons.codec.binary.Base64.isWhiteSpace(arrayOctet[i])))) {
                return false;
            }
        }
        return true;
    }

    private static boolean containsBase64Byte(byte[] arrayOctet) {
        for (int i = 0; i < (arrayOctet.length); i++) {
            if (org.apache.commons.codec.binary.Base64.isBase64(arrayOctet[i])) {
                return true;
            }
        }
        return false;
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, false);
    }

    public static java.lang.String encodeBase64String(byte[] binaryData) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, false));
    }

    public static byte[] encodeBase64URLSafe(byte[] binaryData) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, false, true);
    }

    public static java.lang.String encodeBase64URLSafeString(byte[] binaryData) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, false, true));
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, true);
    }

    public java.lang.Object decode(java.lang.Object pObject) throws org.apache.commons.codec.DecoderException {
        if (pObject instanceof byte[]) {
            return decode(((byte[]) (pObject)));
        }else
            if (pObject instanceof java.lang.String) {
                return decode(((java.lang.String) (pObject)));
            }else {
                throw new org.apache.commons.codec.DecoderException("Parameter supplied to Base64 decode is not a byte[] or a String");
            }

    }

    public byte[] decode(java.lang.String pArray) {
        return decode(org.apache.commons.codec.binary.StringUtils.getBytesUtf8(pArray));
    }

    public byte[] decode(byte[] pArray) {
        reset();
        if ((pArray == null) || ((pArray.length) == 0)) {
            return pArray;
        }
        long len = ((pArray.length) * 3) / 4;
        byte[] buf = new byte[((int) (len))];
        setInitialBuffer(buf, 0, buf.length);
        decode(pArray, 0, pArray.length);
        decode(pArray, 0, (-1));
        byte[] result = new byte[pos];
        readResults(result, 0, result.length);
        return result;
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, isChunked, false);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe) {
        return org.apache.commons.codec.binary.Base64.encodeBase64(binaryData, isChunked, urlSafe, java.lang.Integer.MAX_VALUE);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked, boolean urlSafe, int maxResultSize) {
        if ((binaryData == null) || ((binaryData.length) == 0)) {
            return binaryData;
        }
        long len = org.apache.commons.codec.binary.Base64.getEncodeLength(binaryData, org.apache.commons.codec.binary.Base64.MIME_CHUNK_SIZE, org.apache.commons.codec.binary.Base64.CHUNK_SEPARATOR);
        if (len > maxResultSize) {
            throw new java.lang.IllegalArgumentException(((("Input array too big, the output array would be bigger (" + len) + ") than the specified maxium size of ") + maxResultSize));
        }
        org.apache.commons.codec.binary.Base64 b64 = (isChunked) ? new org.apache.commons.codec.binary.Base64(urlSafe) : new org.apache.commons.codec.binary.Base64(0, org.apache.commons.codec.binary.Base64.CHUNK_SEPARATOR, urlSafe);
        return b64.encode(binaryData);
    }

    public static byte[] decodeBase64(java.lang.String base64String) {
        return new org.apache.commons.codec.binary.Base64().decode(base64String);
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        return new org.apache.commons.codec.binary.Base64().decode(base64Data);
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        for (int i = 0; i < (data.length); i++) {
            switch (data[i]) {
                case ' ' :
                case '\n' :
                case '\r' :
                case '\t' :
                    break;
                default :
                    groomedData[(bytesCopied++)] = data[i];
            }
        }
        byte[] packedData = new byte[bytesCopied];
        java.lang.System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    private static boolean isWhiteSpace(byte byteToCheck) {
        switch (byteToCheck) {
            case ' ' :
            case '\n' :
            case '\r' :
            case '\t' :
                return true;
            default :
                return false;
        }
    }

    public java.lang.Object encode(java.lang.Object pObject) throws org.apache.commons.codec.EncoderException {
        if (!(pObject instanceof byte[])) {
            throw new org.apache.commons.codec.EncoderException("Parameter supplied to Base64 encode is not a byte[]");
        }
        return encode(((byte[]) (pObject)));
    }

    public java.lang.String encodeToString(byte[] pArray) {
        return org.apache.commons.codec.binary.StringUtils.newStringUtf8(encode(pArray));
    }

    public byte[] encode(byte[] pArray) {
        reset();
        if ((pArray == null) || ((pArray.length) == 0)) {
            return pArray;
        }
        long len = org.apache.commons.codec.binary.Base64.getEncodeLength(pArray, lineLength, lineSeparator);
        byte[] buf = new byte[((int) (len))];
        setInitialBuffer(buf, 0, buf.length);
        encode(pArray, 0, pArray.length);
        encode(pArray, 0, (-1));
        if ((buffer) != buf) {
            readResults(buf, 0, buf.length);
        }
        if ((isUrlSafe()) && ((pos) < (buf.length))) {
            byte[] smallerBuf = new byte[pos];
            java.lang.System.arraycopy(buf, 0, smallerBuf, 0, pos);
            buf = smallerBuf;
        }
        return buf;
    }

    private static long getEncodeLength(byte[] pArray, int chunkSize, byte[] chunkSeparator) {
        chunkSize = (chunkSize / 4) * 4;
        long len = ((pArray.length) * 4) / 3;
        long mod = len % 4;
        if (mod != 0) {
            len += 4 - mod;
        }
        if (chunkSize > 0) {
            boolean lenChunksPerfectly = (len % chunkSize) == 0;
            len += (len / chunkSize) * (chunkSeparator.length);
            if (!lenChunksPerfectly) {
                len += chunkSeparator.length;
            }
        }
        return len;
    }

    public static java.math.BigInteger decodeInteger(byte[] pArray) {
        return new java.math.BigInteger(1, org.apache.commons.codec.binary.Base64.decodeBase64(pArray));
    }

    public static byte[] encodeInteger(java.math.BigInteger bigInt) {
        if (bigInt == null) {
            throw new java.lang.NullPointerException("encodeInteger called with null parameter");
        }
        return org.apache.commons.codec.binary.Base64.encodeBase64(org.apache.commons.codec.binary.Base64.toIntegerBytes(bigInt), false);
    }

    static byte[] toIntegerBytes(java.math.BigInteger bigInt) {
        int bitlen = bigInt.bitLength();
        bitlen = ((bitlen + 7) >> 3) << 3;
        byte[] bigBytes = bigInt.toByteArray();
        if ((((bigInt.bitLength()) % 8) != 0) && ((((bigInt.bitLength()) / 8) + 1) == (bitlen / 8))) {
            return bigBytes;
        }
        int startSrc = 0;
        int len = bigBytes.length;
        if (((bigInt.bitLength()) % 8) == 0) {
            startSrc = 1;
            len--;
        }
        int startDst = (bitlen / 8) - len;
        byte[] resizedBytes = new byte[bitlen / 8];
        java.lang.System.arraycopy(bigBytes, startSrc, resizedBytes, startDst, len);
        return resizedBytes;
    }

    private void reset() {
        buffer = null;
        pos = 0;
        readPos = 0;
        currentLinePos = 0;
        modulus = 0;
        eof = false;
    }
}

