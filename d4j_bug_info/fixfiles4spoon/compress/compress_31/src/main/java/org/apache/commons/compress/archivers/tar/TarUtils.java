package org.apache.commons.compress.archivers.tar;


public class TarUtils {
    private static final int BYTE_MASK = 255;

    static final org.apache.commons.compress.archivers.zip.ZipEncoding DEFAULT_ENCODING = org.apache.commons.compress.archivers.zip.ZipEncodingHelper.getZipEncoding(null);

    static final org.apache.commons.compress.archivers.zip.ZipEncoding FALLBACK_ENCODING = new org.apache.commons.compress.archivers.zip.ZipEncoding() {
        public boolean canEncode(java.lang.String name) {
            return true;
        }

        public java.nio.ByteBuffer encode(java.lang.String name) {
            final int length = name.length();
            byte[] buf = new byte[length];
            for (int i = 0; i < length; ++i) {
                buf[i] = ((byte) (name.charAt(i)));
            }
            return java.nio.ByteBuffer.wrap(buf);
        }

        public java.lang.String decode(byte[] buffer) {
            final int length = buffer.length;
            java.lang.StringBuilder result = new java.lang.StringBuilder(length);
            for (byte b : buffer) {
                if (b == 0) {
                    break;
                }
                result.append(((char) (b & 255)));
            }
            return result.toString();
        }
    };

    private TarUtils() {
    }

    public static long parseOctal(final byte[] buffer, final int offset, final int length) {
        long result = 0;
        int end = offset + length;
        int start = offset;
        if (length < 2) {
            throw new java.lang.IllegalArgumentException((("Length " + length) + " must be at least 2"));
        }
        if ((buffer[start]) == 0) {
            return 0L;
        }
        while (start < end) {
            if ((buffer[start]) == ' ') {
                start++;
            }else {
                break;
            }
        } 
        byte trailer = buffer[(end - 1)];
        while ((start < end) && ((trailer == 0) || (trailer == ' '))) {
            end--;
            trailer = buffer[(end - 1)];
        } 
        for (; start < end; start++) {
            final byte currentByte = buffer[start];
            if ((currentByte < '0') || (currentByte > '7')) {
                throw new java.lang.IllegalArgumentException(org.apache.commons.compress.archivers.tar.TarUtils.exceptionMessage(buffer, offset, length, start, currentByte));
            }
            result = (result << 3) + (currentByte - '0');
        }
        return result;
    }

    public static long parseOctalOrBinary(final byte[] buffer, final int offset, final int length) {
        if (((buffer[offset]) & 128) == 0) {
            return org.apache.commons.compress.archivers.tar.TarUtils.parseOctal(buffer, offset, length);
        }
        final boolean negative = (buffer[offset]) == ((byte) (255));
        if (length < 9) {
            return org.apache.commons.compress.archivers.tar.TarUtils.parseBinaryLong(buffer, offset, length, negative);
        }
        return org.apache.commons.compress.archivers.tar.TarUtils.parseBinaryBigInteger(buffer, offset, length, negative);
    }

    private static long parseBinaryLong(final byte[] buffer, final int offset, final int length, final boolean negative) {
        if (length >= 9) {
            throw new java.lang.IllegalArgumentException((((((("At offset " + offset) + ", ") + length) + " byte binary number") + " exceeds maximum signed long") + " value"));
        }
        long val = 0;
        for (int i = 1; i < length; i++) {
            val = (val << 8) + ((buffer[(offset + i)]) & 255);
        }
        if (negative) {
            val--;
            val ^= ((long) (java.lang.Math.pow(2, ((length - 1) * 8)))) - 1;
        }
        return negative ? -val : val;
    }

    private static long parseBinaryBigInteger(final byte[] buffer, final int offset, final int length, final boolean negative) {
        byte[] remainder = new byte[length - 1];
        java.lang.System.arraycopy(buffer, (offset + 1), remainder, 0, (length - 1));
        java.math.BigInteger val = new java.math.BigInteger(remainder);
        if (negative) {
            val = val.add(java.math.BigInteger.valueOf((-1))).not();
        }
        if ((val.bitLength()) > 63) {
            throw new java.lang.IllegalArgumentException((((((("At offset " + offset) + ", ") + length) + " byte binary number") + " exceeds maximum signed long") + " value"));
        }
        return negative ? -(val.longValue()) : val.longValue();
    }

    public static boolean parseBoolean(final byte[] buffer, final int offset) {
        return (buffer[offset]) == 1;
    }

    private static java.lang.String exceptionMessage(byte[] buffer, final int offset, final int length, int current, final byte currentByte) {
        java.lang.String string = new java.lang.String(buffer, offset, length);
        string = string.replaceAll("\u0000", "{NUL}");
        final java.lang.String s = (((((("Invalid byte " + currentByte) + " at offset ") + (current - offset)) + " in '") + string) + "' len=") + length;
        return s;
    }

    public static java.lang.String parseName(byte[] buffer, final int offset, final int length) {
        try {
            return org.apache.commons.compress.archivers.tar.TarUtils.parseName(buffer, offset, length, org.apache.commons.compress.archivers.tar.TarUtils.DEFAULT_ENCODING);
        } catch (java.io.IOException ex) {
            try {
                return org.apache.commons.compress.archivers.tar.TarUtils.parseName(buffer, offset, length, org.apache.commons.compress.archivers.tar.TarUtils.FALLBACK_ENCODING);
            } catch (java.io.IOException ex2) {
                throw new java.lang.RuntimeException(ex2);
            }
        }
    }

    public static java.lang.String parseName(byte[] buffer, final int offset, final int length, final org.apache.commons.compress.archivers.zip.ZipEncoding encoding) throws java.io.IOException {
        int len = length;
        for (; len > 0; len--) {
            if ((buffer[((offset + len) - 1)]) != 0) {
                break;
            }
        }
        if (len > 0) {
            byte[] b = new byte[len];
            java.lang.System.arraycopy(buffer, offset, b, 0, len);
            return encoding.decode(b);
        }
        return "";
    }

    public static int formatNameBytes(java.lang.String name, byte[] buf, final int offset, final int length) {
        try {
            return org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes(name, buf, offset, length, org.apache.commons.compress.archivers.tar.TarUtils.DEFAULT_ENCODING);
        } catch (java.io.IOException ex) {
            try {
                return org.apache.commons.compress.archivers.tar.TarUtils.formatNameBytes(name, buf, offset, length, org.apache.commons.compress.archivers.tar.TarUtils.FALLBACK_ENCODING);
            } catch (java.io.IOException ex2) {
                throw new java.lang.RuntimeException(ex2);
            }
        }
    }

    public static int formatNameBytes(java.lang.String name, byte[] buf, final int offset, final int length, final org.apache.commons.compress.archivers.zip.ZipEncoding encoding) throws java.io.IOException {
        int len = name.length();
        java.nio.ByteBuffer b = encoding.encode(name);
        while (((b.limit()) > length) && (len > 0)) {
            b = encoding.encode(name.substring(0, (--len)));
        } 
        final int limit = (b.limit()) - (b.position());
        java.lang.System.arraycopy(b.array(), b.arrayOffset(), buf, offset, limit);
        for (int i = limit; i < length; ++i) {
            buf[(offset + i)] = 0;
        }
        return offset + length;
    }

    public static void formatUnsignedOctalString(final long value, byte[] buffer, final int offset, final int length) {
        int remaining = length;
        remaining--;
        if (value == 0) {
            buffer[(offset + (remaining--))] = ((byte) ('0'));
        }else {
            long val = value;
            for (; (remaining >= 0) && (val != 0); --remaining) {
                buffer[(offset + remaining)] = ((byte) (((byte) ('0')) + ((byte) (val & 7))));
                val = val >>> 3;
            }
            if (val != 0) {
                throw new java.lang.IllegalArgumentException(((((value + "=") + (java.lang.Long.toOctalString(value))) + " will not fit in octal number buffer of length ") + length));
            }
        }
        for (; remaining >= 0; --remaining) {
            buffer[(offset + remaining)] = ((byte) ('0'));
        }
    }

    public static int formatOctalBytes(final long value, byte[] buf, final int offset, final int length) {
        int idx = length - 2;
        org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString(value, buf, offset, idx);
        buf[(offset + (idx++))] = ((byte) (' '));
        buf[(offset + idx)] = 0;
        return offset + length;
    }

    public static int formatLongOctalBytes(final long value, byte[] buf, final int offset, final int length) {
        int idx = length - 1;
        org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString(value, buf, offset, idx);
        buf[(offset + idx)] = ((byte) (' '));
        return offset + length;
    }

    public static int formatLongOctalOrBinaryBytes(final long value, byte[] buf, final int offset, final int length) {
        final long maxAsOctalChar = (length == (TarConstants.UIDLEN)) ? TarConstants.MAXID : TarConstants.MAXSIZE;
        final boolean negative = value < 0;
        if ((!negative) && (value <= maxAsOctalChar)) {
            return org.apache.commons.compress.archivers.tar.TarUtils.formatLongOctalBytes(value, buf, offset, length);
        }
        if (length < 9) {
            org.apache.commons.compress.archivers.tar.TarUtils.formatLongBinary(value, buf, offset, length, negative);
        }
        org.apache.commons.compress.archivers.tar.TarUtils.formatBigIntegerBinary(value, buf, offset, length, negative);
        buf[offset] = ((byte) ((negative) ? 255 : 128));
        return offset + length;
    }

    private static void formatLongBinary(final long value, byte[] buf, final int offset, final int length, final boolean negative) {
        final int bits = (length - 1) * 8;
        final long max = 1L << bits;
        long val = java.lang.Math.abs(value);
        if (val >= max) {
            throw new java.lang.IllegalArgumentException((((("Value " + value) + " is too large for ") + length) + " byte field."));
        }
        if (negative) {
            val ^= max - 1;
            val |= 255 << bits;
            val++;
        }
        for (int i = (offset + length) - 1; i >= offset; i--) {
            buf[i] = ((byte) (val));
            val >>= 8;
        }
    }

    private static void formatBigIntegerBinary(final long value, byte[] buf, final int offset, final int length, final boolean negative) {
        java.math.BigInteger val = java.math.BigInteger.valueOf(value);
        final byte[] b = val.toByteArray();
        final int len = b.length;
        final int off = (offset + length) - len;
        java.lang.System.arraycopy(b, 0, buf, off, len);
        final byte fill = ((byte) ((negative) ? 255 : 0));
        for (int i = offset + 1; i < off; i++) {
            buf[i] = fill;
        }
    }

    public static int formatCheckSumOctalBytes(final long value, byte[] buf, final int offset, final int length) {
        int idx = length - 2;
        org.apache.commons.compress.archivers.tar.TarUtils.formatUnsignedOctalString(value, buf, offset, idx);
        buf[(offset + (idx++))] = 0;
        buf[(offset + idx)] = ((byte) (' '));
        return offset + length;
    }

    public static long computeCheckSum(final byte[] buf) {
        long sum = 0;
        for (byte element : buf) {
            sum += (org.apache.commons.compress.archivers.tar.TarUtils.BYTE_MASK) & element;
        }
        return sum;
    }

    public static boolean verifyCheckSum(byte[] header) {
        long storedSum = 0;
        long unsignedSum = 0;
        long signedSum = 0;
        int digits = 0;
        for (int i = 0; i < (header.length); i++) {
            byte b = header[i];
            if (((org.apache.commons.compress.archivers.tar.TarConstants.CHKSUM_OFFSET) <= i) && (i < ((org.apache.commons.compress.archivers.tar.TarConstants.CHKSUM_OFFSET) + (org.apache.commons.compress.archivers.tar.TarConstants.CHKSUMLEN)))) {
                if ((('0' <= b) && (b <= '7')) && ((digits++) < 6)) {
                    storedSum = ((storedSum * 8) + b) - '0';
                }else
                    if (digits > 0) {
                        digits = 6;
                    }

                b = ' ';
            }
            unsignedSum += 255 & b;
            signedSum += b;
        }
        return ((storedSum == unsignedSum) || (storedSum == signedSum)) || (storedSum > unsignedSum);
    }
}

