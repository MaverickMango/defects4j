package org.apache.commons.compress.archivers.zip;


public class Zip64ExtendedInformationExtraField implements org.apache.commons.compress.archivers.zip.ZipExtraField {
    static final org.apache.commons.compress.archivers.zip.ZipShort HEADER_ID = new org.apache.commons.compress.archivers.zip.ZipShort(1);

    private static final java.lang.String LFH_MUST_HAVE_BOTH_SIZES_MSG = "Zip64 extended information must contain" + " both size values in the local file header.";

    private static final byte[] EMPTY = new byte[0];

    private org.apache.commons.compress.archivers.zip.ZipEightByteInteger size;

    private org.apache.commons.compress.archivers.zip.ZipEightByteInteger compressedSize;

    private org.apache.commons.compress.archivers.zip.ZipEightByteInteger relativeHeaderOffset;

    private org.apache.commons.compress.archivers.zip.ZipLong diskStart;

    private byte[] rawCentralDirectoryData;

    public Zip64ExtendedInformationExtraField() {
    }

    public Zip64ExtendedInformationExtraField(org.apache.commons.compress.archivers.zip.ZipEightByteInteger size, org.apache.commons.compress.archivers.zip.ZipEightByteInteger compressedSize) {
        this(size, compressedSize, null, null);
    }

    public Zip64ExtendedInformationExtraField(org.apache.commons.compress.archivers.zip.ZipEightByteInteger size, org.apache.commons.compress.archivers.zip.ZipEightByteInteger compressedSize, org.apache.commons.compress.archivers.zip.ZipEightByteInteger relativeHeaderOffset, org.apache.commons.compress.archivers.zip.ZipLong diskStart) {
        this.size = size;
        this.compressedSize = compressedSize;
        this.relativeHeaderOffset = relativeHeaderOffset;
        this.diskStart = diskStart;
    }

    public org.apache.commons.compress.archivers.zip.ZipShort getHeaderId() {
        return org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField.HEADER_ID;
    }

    public org.apache.commons.compress.archivers.zip.ZipShort getLocalFileDataLength() {
        return new org.apache.commons.compress.archivers.zip.ZipShort(((size) != null ? 2 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD) : 0));
    }

    public org.apache.commons.compress.archivers.zip.ZipShort getCentralDirectoryLength() {
        return new org.apache.commons.compress.archivers.zip.ZipShort((((((size) != null ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0) + ((compressedSize) != null ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0)) + ((relativeHeaderOffset) != null ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0)) + ((diskStart) != null ? org.apache.commons.compress.archivers.zip.ZipConstants.WORD : 0)));
    }

    public byte[] getLocalFileDataData() {
        if (((size) != null) || ((compressedSize) != null)) {
            if (((size) == null) || ((compressedSize) == null)) {
                throw new java.lang.IllegalArgumentException(org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
            }
            byte[] data = new byte[2 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD)];
            addSizes(data);
            return data;
        }
        return org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField.EMPTY;
    }

    public byte[] getCentralDirectoryData() {
        byte[] data = new byte[getCentralDirectoryLength().getValue()];
        int off = addSizes(data);
        if ((relativeHeaderOffset) != null) {
            java.lang.System.arraycopy(relativeHeaderOffset.getBytes(), 0, data, off, org.apache.commons.compress.archivers.zip.ZipConstants.DWORD);
            off += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        }
        if ((diskStart) != null) {
            java.lang.System.arraycopy(diskStart.getBytes(), 0, data, off, org.apache.commons.compress.archivers.zip.ZipConstants.WORD);
            off += org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
        }
        return data;
    }

    public void parseFromLocalFileData(byte[] buffer, int offset, int length) throws java.util.zip.ZipException {
        if (length == 0) {
            return;
        }
        if (length < (2 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD))) {
            throw new java.util.zip.ZipException(org.apache.commons.compress.archivers.zip.Zip64ExtendedInformationExtraField.LFH_MUST_HAVE_BOTH_SIZES_MSG);
        }
        size = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
        offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        compressedSize = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
        offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        int remaining = length - (2 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD));
        if (remaining >= (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD)) {
            relativeHeaderOffset = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
            offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
            remaining -= org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        }
        if (remaining >= (org.apache.commons.compress.archivers.zip.ZipConstants.WORD)) {
            diskStart = new org.apache.commons.compress.archivers.zip.ZipLong(buffer, offset);
            offset += org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
            remaining -= org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
        }
    }

    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws java.util.zip.ZipException {
        rawCentralDirectoryData = new byte[length];
        java.lang.System.arraycopy(buffer, offset, rawCentralDirectoryData, 0, length);
        if (length >= ((3 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD)) + (org.apache.commons.compress.archivers.zip.ZipConstants.WORD))) {
            parseFromLocalFileData(buffer, offset, length);
        }else
            if (length == (3 * (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD))) {
                size = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
                compressedSize = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
                relativeHeaderOffset = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(buffer, offset);
            }else
                if ((length % (org.apache.commons.compress.archivers.zip.ZipConstants.DWORD)) == (org.apache.commons.compress.archivers.zip.ZipConstants.WORD)) {
                    diskStart = new org.apache.commons.compress.archivers.zip.ZipLong(buffer, ((offset + length) - (org.apache.commons.compress.archivers.zip.ZipConstants.WORD)));
                }


    }

    public void reparseCentralDirectoryData(boolean hasUncompressedSize, boolean hasCompressedSize, boolean hasRelativeHeaderOffset, boolean hasDiskStart) throws java.util.zip.ZipException {
        if ((rawCentralDirectoryData) != null) {
            int expectedLength = (((hasUncompressedSize ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0) + (hasCompressedSize ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0)) + (hasRelativeHeaderOffset ? org.apache.commons.compress.archivers.zip.ZipConstants.DWORD : 0)) + (hasDiskStart ? org.apache.commons.compress.archivers.zip.ZipConstants.WORD : 0);
            if ((rawCentralDirectoryData.length) < expectedLength) {
                throw new java.util.zip.ZipException((((("central directory zip64 extended" + ((" information extra field's length" + " doesn't match central directory") + " data.  Expected length ")) + expectedLength) + " but is ") + (rawCentralDirectoryData.length)));
            }
            int offset = 0;
            if (hasUncompressedSize) {
                size = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(rawCentralDirectoryData, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
            }
            if (hasCompressedSize) {
                compressedSize = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(rawCentralDirectoryData, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
            }
            if (hasRelativeHeaderOffset) {
                relativeHeaderOffset = new org.apache.commons.compress.archivers.zip.ZipEightByteInteger(rawCentralDirectoryData, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
            }
            if (hasDiskStart) {
                diskStart = new org.apache.commons.compress.archivers.zip.ZipLong(rawCentralDirectoryData, offset);
                offset += org.apache.commons.compress.archivers.zip.ZipConstants.WORD;
            }
        }
    }

    public org.apache.commons.compress.archivers.zip.ZipEightByteInteger getSize() {
        return size;
    }

    public void setSize(org.apache.commons.compress.archivers.zip.ZipEightByteInteger size) {
        this.size = size;
    }

    public org.apache.commons.compress.archivers.zip.ZipEightByteInteger getCompressedSize() {
        return compressedSize;
    }

    public void setCompressedSize(org.apache.commons.compress.archivers.zip.ZipEightByteInteger compressedSize) {
        this.compressedSize = compressedSize;
    }

    public org.apache.commons.compress.archivers.zip.ZipEightByteInteger getRelativeHeaderOffset() {
        return relativeHeaderOffset;
    }

    public void setRelativeHeaderOffset(org.apache.commons.compress.archivers.zip.ZipEightByteInteger rho) {
        relativeHeaderOffset = rho;
    }

    public org.apache.commons.compress.archivers.zip.ZipLong getDiskStartNumber() {
        return diskStart;
    }

    public void setDiskStartNumber(org.apache.commons.compress.archivers.zip.ZipLong ds) {
        diskStart = ds;
    }

    private int addSizes(byte[] data) {
        int off = 0;
        if ((size) != null) {
            java.lang.System.arraycopy(size.getBytes(), 0, data, 0, org.apache.commons.compress.archivers.zip.ZipConstants.DWORD);
            off += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        }
        if ((compressedSize) != null) {
            java.lang.System.arraycopy(compressedSize.getBytes(), 0, data, off, org.apache.commons.compress.archivers.zip.ZipConstants.DWORD);
            off += org.apache.commons.compress.archivers.zip.ZipConstants.DWORD;
        }
        return off;
    }
}

