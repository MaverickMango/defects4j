package org.apache.commons.lang.text;
public class StrBuilder implements java.lang.Cloneable {
	static final int CAPACITY = 32;

	private static final long serialVersionUID = 7628716375283629643L;

	protected char[] buffer;

	protected int size;

	private java.lang.String newLine;

	private java.lang.String nullText;

	public StrBuilder() {
		this(org.apache.commons.lang.text.StrBuilder.CAPACITY);
	}

	public StrBuilder(int initialCapacity) {
		super();
		if (initialCapacity <= 0) {
			initialCapacity = org.apache.commons.lang.text.StrBuilder.CAPACITY;
		}
		buffer = new char[initialCapacity];
	}

	public StrBuilder(java.lang.String str) {
		super();
		if (str == null) {
			buffer = new char[org.apache.commons.lang.text.StrBuilder.CAPACITY];
		} else {
			buffer = new char[str.length() + org.apache.commons.lang.text.StrBuilder.CAPACITY];
			append(str);
		}
	}

	public java.lang.String getNewLineText() {
		return newLine;
	}

	public org.apache.commons.lang.text.StrBuilder setNewLineText(java.lang.String newLine) {
		this.newLine = newLine;
		return this;
	}

	public java.lang.String getNullText() {
		return nullText;
	}

	public org.apache.commons.lang.text.StrBuilder setNullText(java.lang.String nullText) {
		if ((nullText != null) && (nullText.length() == 0)) {
			nullText = null;
		}
		this.nullText = nullText;
		return this;
	}

	public int length() {
		return size;
	}

	public org.apache.commons.lang.text.StrBuilder setLength(int length) {
		if (length < 0) {
			throw new java.lang.StringIndexOutOfBoundsException(length);
		}
		if (length < size) {
			size = length;
		} else if (length > size) {
			ensureCapacity(length);
			int oldEnd = size;
			int newEnd = length;
			size = length;
			for (int i = oldEnd; i < newEnd; i++) {
				buffer[i] = '\u0000';
			}
		}
		return this;
	}

	public int capacity() {
		return buffer.length;
	}

	public org.apache.commons.lang.text.StrBuilder ensureCapacity(int capacity) {
		if (capacity > buffer.length) {
			char[] old = buffer;
			buffer = new char[capacity];
			java.lang.System.arraycopy(old, 0, buffer, 0, size);
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder minimizeCapacity() {
		if (buffer.length > length()) {
			char[] old = buffer;
			buffer = new char[length()];
			java.lang.System.arraycopy(old, 0, buffer, 0, size);
		}
		return this;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public org.apache.commons.lang.text.StrBuilder clear() {
		size = 0;
		return this;
	}

	public char charAt(int index) {
		if ((index < 0) || (index >= length())) {
			throw new java.lang.StringIndexOutOfBoundsException(index);
		}
		return buffer[index];
	}

	public org.apache.commons.lang.text.StrBuilder setCharAt(int index, char ch) {
		if ((index < 0) || (index >= length())) {
			throw new java.lang.StringIndexOutOfBoundsException(index);
		}
		buffer[index] = ch;
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteCharAt(int index) {
		if ((index < 0) || (index >= size)) {
			throw new java.lang.StringIndexOutOfBoundsException(index);
		}
		deleteImpl(index, index + 1, 1);
		return this;
	}

	public char[] toCharArray() {
		if (size == 0) {
			return org.apache.commons.lang.ArrayUtils.EMPTY_CHAR_ARRAY;
		}
		char[] chars = new char[size];
		java.lang.System.arraycopy(buffer, 0, chars, 0, size);
		return chars;
	}

	public char[] toCharArray(int startIndex, int endIndex) {
		endIndex = validateRange(startIndex, endIndex);
		int len = endIndex - startIndex;
		if (len == 0) {
			return org.apache.commons.lang.ArrayUtils.EMPTY_CHAR_ARRAY;
		}
		char[] chars = new char[len];
		java.lang.System.arraycopy(buffer, startIndex, chars, 0, len);
		return chars;
	}

	public char[] getChars(char[] destination) {
		int len = length();
		if ((destination == null) || (destination.length < len)) {
			destination = new char[len];
		}
		java.lang.System.arraycopy(buffer, 0, destination, 0, len);
		return destination;
	}

	public void getChars(int startIndex, int endIndex, char[] destination, int destinationIndex) {
		if (startIndex < 0) {
			throw new java.lang.StringIndexOutOfBoundsException(startIndex);
		}
		if ((endIndex < 0) || (endIndex > length())) {
			throw new java.lang.StringIndexOutOfBoundsException(endIndex);
		}
		if (startIndex > endIndex) {
			throw new java.lang.StringIndexOutOfBoundsException("end < start");
		}
		java.lang.System.arraycopy(buffer, startIndex, destination, destinationIndex, endIndex - startIndex);
	}

	public org.apache.commons.lang.text.StrBuilder appendNewLine() {
		if (newLine == null) {
			append(org.apache.commons.lang.SystemUtils.LINE_SEPARATOR);
			return this;
		}
		return append(newLine);
	}

	public org.apache.commons.lang.text.StrBuilder appendNull() {
		if (nullText == null) {
			return this;
		}
		return append(nullText);
	}

	public org.apache.commons.lang.text.StrBuilder append(java.lang.Object obj) {
		if (obj == null) {
			return appendNull();
		}
		return append(obj.toString());
	}

	public org.apache.commons.lang.text.StrBuilder append(java.lang.String str) {
		if (str == null) {
			return appendNull();
		}
		int strLen = str.length();
		if (strLen > 0) {
			int len = length();
			ensureCapacity(len + strLen);
			str.getChars(0, strLen, buffer, len);
			size += strLen;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(java.lang.String str, int startIndex, int length) {
		if (str == null) {
			return appendNull();
		}
		if ((startIndex < 0) || (startIndex > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("startIndex must be valid");
		}
		if ((length < 0) || ((startIndex + length) > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("length must be valid");
		}
		if (length > 0) {
			int len = length();
			ensureCapacity(len + length);
			str.getChars(startIndex, startIndex + length, buffer, len);
			size += length;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(java.lang.StringBuffer str) {
		if (str == null) {
			return appendNull();
		}
		int strLen = str.length();
		if (strLen > 0) {
			int len = length();
			ensureCapacity(len + strLen);
			str.getChars(0, strLen, buffer, len);
			size += strLen;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(java.lang.StringBuffer str, int startIndex, int length) {
		if (str == null) {
			return appendNull();
		}
		if ((startIndex < 0) || (startIndex > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("startIndex must be valid");
		}
		if ((length < 0) || ((startIndex + length) > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("length must be valid");
		}
		if (length > 0) {
			int len = length();
			ensureCapacity(len + length);
			str.getChars(startIndex, startIndex + length, buffer, len);
			size += length;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(org.apache.commons.lang.text.StrBuilder str) {
		if (str == null) {
			return appendNull();
		}
		int strLen = str.length();
		if (strLen > 0) {
			int len = length();
			ensureCapacity(len + strLen);
			java.lang.System.arraycopy(str.buffer, 0, buffer, len, strLen);
			size += strLen;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(org.apache.commons.lang.text.StrBuilder str, int startIndex, int length) {
		if (str == null) {
			return appendNull();
		}
		if ((startIndex < 0) || (startIndex > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("startIndex must be valid");
		}
		if ((length < 0) || ((startIndex + length) > str.length())) {
			throw new java.lang.StringIndexOutOfBoundsException("length must be valid");
		}
		if (length > 0) {
			int len = length();
			ensureCapacity(len + length);
			str.getChars(startIndex, startIndex + length, buffer, len);
			size += length;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(char[] chars) {
		if (chars == null) {
			return appendNull();
		}
		int strLen = chars.length;
		if (strLen > 0) {
			int len = length();
			ensureCapacity(len + strLen);
			java.lang.System.arraycopy(chars, 0, buffer, len, strLen);
			size += strLen;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(char[] chars, int startIndex, int length) {
		if (chars == null) {
			return appendNull();
		}
		if ((startIndex < 0) || (startIndex > chars.length)) {
			throw new java.lang.StringIndexOutOfBoundsException("Invalid startIndex: " + length);
		}
		if ((length < 0) || ((startIndex + length) > chars.length)) {
			throw new java.lang.StringIndexOutOfBoundsException("Invalid length: " + length);
		}
		if (length > 0) {
			int len = length();
			ensureCapacity(len + length);
			java.lang.System.arraycopy(chars, startIndex, buffer, len, length);
			size += length;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(boolean value) {
		if (value) {
			ensureCapacity(size + 4);
			buffer[size++] = 't';
			buffer[size++] = 'r';
			buffer[size++] = 'u';
			buffer[size++] = 'e';
		} else {
			ensureCapacity(size + 5);
			buffer[size++] = 'f';
			buffer[size++] = 'a';
			buffer[size++] = 'l';
			buffer[size++] = 's';
			buffer[size++] = 'e';
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(char ch) {
		int len = length();
		ensureCapacity(len + 1);
		buffer[size++] = ch;
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder append(int value) {
		return append(java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder append(long value) {
		return append(java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder append(float value) {
		return append(java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder append(double value) {
		return append(java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder appendWithSeparators(java.lang.Object[] array, java.lang.String separator) {
		if ((array != null) && (array.length > 0)) {
			separator = (separator == null) ? "" : separator;
			append(array[0]);
			for (int i = 1; i < array.length; i++) {
				append(separator);
				append(array[i]);
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendWithSeparators(java.util.Collection coll, java.lang.String separator) {
		if ((coll != null) && (coll.size() > 0)) {
			separator = (separator == null) ? "" : separator;
			java.util.Iterator it = coll.iterator();
			while (it.hasNext()) {
				append(it.next());
				if (it.hasNext()) {
					append(separator);
				}
			} 
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendWithSeparators(java.util.Iterator it, java.lang.String separator) {
		if (it != null) {
			separator = (separator == null) ? "" : separator;
			while (it.hasNext()) {
				append(it.next());
				if (it.hasNext()) {
					append(separator);
				}
			} 
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendPadding(int length, char padChar) {
		if (length >= 0) {
			ensureCapacity(size + length);
			for (int i = 0; i < length; i++) {
				buffer[size++] = padChar;
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendFixedWidthPadLeft(java.lang.Object obj, int width, char padChar) {
		if (width > 0) {
			ensureCapacity(size + width);
			java.lang.String str = (obj == null) ? getNullText() : obj.toString();
			int strLen = str.length();
			if (strLen >= width) {
				str.getChars(strLen - width, strLen, buffer, size);
			} else {
				int padLen = width - strLen;
				for (int i = 0; i < padLen; i++) {
					buffer[size + i] = padChar;
				}
				str.getChars(0, strLen, buffer, size + padLen);
			}
			size += width;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendFixedWidthPadLeft(int value, int width, char padChar) {
		return appendFixedWidthPadLeft(java.lang.String.valueOf(value), width, padChar);
	}

	public org.apache.commons.lang.text.StrBuilder appendFixedWidthPadRight(java.lang.Object obj, int width, char padChar) {
		if (width > 0) {
			ensureCapacity(size + width);
			java.lang.String str = (obj == null) ? getNullText() : obj.toString();
			int strLen = str.length();
			if (strLen >= width) {
				str.getChars(0, strLen, buffer, size);
			} else {
				int padLen = width - strLen;
				str.getChars(0, strLen, buffer, size);
				for (int i = 0; i < padLen; i++) {
					buffer[(size + strLen) + i] = padChar;
				}
			}
			size += width;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
		return appendFixedWidthPadRight(java.lang.String.valueOf(value), width, padChar);
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, java.lang.Object obj) {
		if (obj == null) {
			return insert(index, nullText);
		}
		return insert(index, obj.toString());
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, java.lang.String str) {
		validateIndex(index);
		if (str == null) {
			str = nullText;
		}
		int strLen = (str == null) ? 0 : str.length();
		if (strLen > 0) {
			int newSize = size + strLen;
			ensureCapacity(newSize);
			java.lang.System.arraycopy(buffer, index, buffer, index + strLen, size - index);
			size = newSize;
			str.getChars(0, strLen, buffer, index);
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, char[] chars) {
		validateIndex(index);
		if (chars == null) {
			return insert(index, nullText);
		}
		int len = chars.length;
		if (len > 0) {
			ensureCapacity(size + len);
			java.lang.System.arraycopy(buffer, index, buffer, index + len, size - index);
			java.lang.System.arraycopy(chars, 0, buffer, index, len);
			size += len;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, char[] chars, int offset, int length) {
		validateIndex(index);
		if (chars == null) {
			return insert(index, nullText);
		}
		if ((offset < 0) || (offset > chars.length)) {
			throw new java.lang.StringIndexOutOfBoundsException("Invalid offset: " + offset);
		}
		if ((length < 0) || ((offset + length) > chars.length)) {
			throw new java.lang.StringIndexOutOfBoundsException("Invalid length: " + length);
		}
		if (length > 0) {
			ensureCapacity(size + length);
			java.lang.System.arraycopy(buffer, index, buffer, index + length, size - index);
			java.lang.System.arraycopy(chars, offset, buffer, index, length);
			size += length;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, boolean value) {
		validateIndex(index);
		if (value) {
			ensureCapacity(size + 4);
			java.lang.System.arraycopy(buffer, index, buffer, index + 4, size - index);
			buffer[index++] = 't';
			buffer[index++] = 'r';
			buffer[index++] = 'u';
			buffer[index] = 'e';
			size += 4;
		} else {
			ensureCapacity(size + 5);
			java.lang.System.arraycopy(buffer, index, buffer, index + 5, size - index);
			buffer[index++] = 'f';
			buffer[index++] = 'a';
			buffer[index++] = 'l';
			buffer[index++] = 's';
			buffer[index] = 'e';
			size += 5;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, char value) {
		validateIndex(index);
		ensureCapacity(size + 1);
		java.lang.System.arraycopy(buffer, index, buffer, index + 1, size - index);
		buffer[index] = value;
		size++;
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, int value) {
		return insert(index, java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, long value) {
		return insert(index, java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, float value) {
		return insert(index, java.lang.String.valueOf(value));
	}

	public org.apache.commons.lang.text.StrBuilder insert(int index, double value) {
		return insert(index, java.lang.String.valueOf(value));
	}

	private void deleteImpl(int startIndex, int endIndex, int len) {
		java.lang.System.arraycopy(buffer, endIndex, buffer, startIndex, size - endIndex);
		size -= len;
	}

	public org.apache.commons.lang.text.StrBuilder delete(int startIndex, int endIndex) {
		endIndex = validateRange(startIndex, endIndex);
		int len = endIndex - startIndex;
		if (len > 0) {
			deleteImpl(startIndex, endIndex, len);
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteAll(char ch) {
		for (int i = 0; i < size; i++) {
			if (buffer[i] == ch) {
				int start = i;
				while ((++i) < size) {
					if (buffer[i] != ch) {
						break;
					}
				} 
				int len = i - start;
				deleteImpl(start, i, len);
				i -= len;
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteFirst(char ch) {
		for (int i = 0; i < size; i++) {
			if (buffer[i] == ch) {
				deleteImpl(i, i + 1, 1);
				break;
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteAll(java.lang.String str) {
		int len = (str == null) ? 0 : str.length();
		if (len > 0) {
			int index = indexOf(str, 0);
			while (index >= 0) {
				deleteImpl(index, index + len, len);
				index = indexOf(str, index);
			} 
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteFirst(java.lang.String str) {
		int len = (str == null) ? 0 : str.length();
		if (len > 0) {
			int index = indexOf(str, 0);
			if (index >= 0) {
				deleteImpl(index, index + len, len);
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder deleteAll(org.apache.commons.lang.text.StrMatcher matcher) {
		return replace(matcher, null, 0, size, -1);
	}

	public org.apache.commons.lang.text.StrBuilder deleteFirst(org.apache.commons.lang.text.StrMatcher matcher) {
		return replace(matcher, null, 0, size, 1);
	}

	private void replaceImpl(int startIndex, int endIndex, int removeLen, java.lang.String insertStr, int insertLen) {
		int newSize = (size - removeLen) + insertLen;
		if (insertLen != removeLen) {
			ensureCapacity(newSize);
			java.lang.System.arraycopy(buffer, endIndex, buffer, startIndex + insertLen, size - endIndex);
			size = newSize;
		}
		if (insertLen > 0) {
			insertStr.getChars(0, insertLen, buffer, startIndex);
		}
	}

	public org.apache.commons.lang.text.StrBuilder replace(int startIndex, int endIndex, java.lang.String replaceStr) {
		endIndex = validateRange(startIndex, endIndex);
		int insertLen = (replaceStr == null) ? 0 : replaceStr.length();
		replaceImpl(startIndex, endIndex, endIndex - startIndex, replaceStr, insertLen);
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder replaceAll(char search, char replace) {
		if (search != replace) {
			for (int i = 0; i < size; i++) {
				if (buffer[i] == search) {
					buffer[i] = replace;
				}
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder replaceFirst(char search, char replace) {
		if (search != replace) {
			for (int i = 0; i < size; i++) {
				if (buffer[i] == search) {
					buffer[i] = replace;
					break;
				}
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder replaceAll(java.lang.String searchStr, java.lang.String replaceStr) {
		int searchLen = (searchStr == null) ? 0 : searchStr.length();
		if (searchLen > 0) {
			int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
			int index = indexOf(searchStr, 0);
			while (index >= 0) {
				replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
				index = indexOf(searchStr, index + replaceLen);
			} 
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder replaceFirst(java.lang.String searchStr, java.lang.String replaceStr) {
		int searchLen = (searchStr == null) ? 0 : searchStr.length();
		if (searchLen > 0) {
			int index = indexOf(searchStr, 0);
			if (index >= 0) {
				int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
				replaceImpl(index, index + searchLen, searchLen, replaceStr, replaceLen);
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder replaceAll(org.apache.commons.lang.text.StrMatcher matcher, java.lang.String replaceStr) {
		return replace(matcher, replaceStr, 0, size, -1);
	}

	public org.apache.commons.lang.text.StrBuilder replaceFirst(org.apache.commons.lang.text.StrMatcher matcher, java.lang.String replaceStr) {
		return replace(matcher, replaceStr, 0, size, 1);
	}

	public org.apache.commons.lang.text.StrBuilder replace(org.apache.commons.lang.text.StrMatcher matcher, java.lang.String replaceStr, int startIndex, int endIndex, int replaceCount) {
		endIndex = validateRange(startIndex, endIndex);
		return replaceImpl(matcher, replaceStr, startIndex, endIndex, replaceCount);
	}

	private org.apache.commons.lang.text.StrBuilder replaceImpl(org.apache.commons.lang.text.StrMatcher matcher, java.lang.String replaceStr, int from, int to, int replaceCount) {
		if ((matcher == null) || (size == 0)) {
			return this;
		}
		int replaceLen = (replaceStr == null) ? 0 : replaceStr.length();
		char[] buf = buffer;
		for (int i = from; (i < to) && (replaceCount != 0); i++) {
			int removeLen = matcher.isMatch(buf, i, from, to);
			if (removeLen > 0) {
				replaceImpl(i, i + removeLen, removeLen, replaceStr, replaceLen);
				to = (to - removeLen) + replaceLen;
				i = (i + replaceLen) - 1;
				if (replaceCount > 0) {
					replaceCount--;
				}
			}
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder reverse() {
		if (size == 0) {
			return this;
		}
		int half = size / 2;
		char[] buf = buffer;
		for (int leftIdx = 0, rightIdx = size - 1; leftIdx < half; leftIdx++ , rightIdx--) {
			char swap = buf[leftIdx];
			buf[leftIdx] = buf[rightIdx];
			buf[rightIdx] = swap;
		}
		return this;
	}

	public org.apache.commons.lang.text.StrBuilder trim() {
		if (size == 0) {
			return this;
		}
		int len = size;
		char[] buf = buffer;
		int pos = 0;
		while ((pos < len) && (buf[pos] <= ' ')) {
			pos++;
		} 
		while ((pos < len) && (buf[len - 1] <= ' ')) {
			len--;
		} 
		if (len < size) {
			delete(len, size);
		}
		if (pos > 0) {
			delete(0, pos);
		}
		return this;
	}

	public boolean startsWith(java.lang.String str) {
		if (str == null) {
			return false;
		}
		int len = str.length();
		if (len == 0) {
			return true;
		}
		if (len > size) {
			return false;
		}
		for (int i = 0; i < len; i++) {
			if (buffer[i] != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public boolean endsWith(java.lang.String str) {
		if (str == null) {
			return false;
		}
		int len = str.length();
		if (len == 0) {
			return true;
		}
		if (len > size) {
			return false;
		}
		int pos = size - len;
		for (int i = 0; i < len; i++ , pos++) {
			if (buffer[pos] != str.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	public java.lang.String substring(int start) {
		return substring(start, size);
	}

	public java.lang.String substring(int startIndex, int endIndex) {
		endIndex = validateRange(startIndex, endIndex);
		return new java.lang.String(buffer, startIndex, endIndex - startIndex);
	}

	public java.lang.String leftString(int length) {
		if (length <= 0) {
			return "";
		} else if (length >= size) {
			return new java.lang.String(buffer, 0, size);
		} else {
			return new java.lang.String(buffer, 0, length);
		}
	}

	public java.lang.String rightString(int length) {
		if (length <= 0) {
			return "";
		} else if (length >= size) {
			return new java.lang.String(buffer, 0, size);
		} else {
			return new java.lang.String(buffer, size - length, length);
		}
	}

	public java.lang.String midString(int index, int length) {
		if (index < 0) {
			index = 0;
		}
		if ((length <= 0) || (index >= size)) {
			return "";
		}
		if (size <= (index + length)) {
			return new java.lang.String(buffer, index, size - index);
		} else {
			return new java.lang.String(buffer, index, length);
		}
	}

	public boolean contains(char ch) {
		char[] thisBuf = buffer;
		for (int i = 0; i < this.size; i++) {
			if (thisBuf[i] == ch) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(java.lang.String str) {
		return indexOf(str, 0) >= 0;
	}

	public boolean contains(org.apache.commons.lang.text.StrMatcher matcher) {
		return indexOf(matcher, 0) >= 0;
	}

	public int indexOf(char ch) {
		return indexOf(ch, 0);
	}

	public int indexOf(char ch, int startIndex) {
		startIndex = (startIndex < 0) ? 0 : startIndex;
		if (startIndex >= size) {
			return -1;
		}
		char[] thisBuf = buffer;
		for (int i = startIndex; i < size; i++) {
			if (thisBuf[i] == ch) {
				return i;
			}
		}
		return -1;
	}

	public int indexOf(java.lang.String str) {
		return indexOf(str, 0);
	}

	public int indexOf(java.lang.String str, int startIndex) {
		startIndex = (startIndex < 0) ? 0 : startIndex;
		if ((str == null) || (startIndex >= size)) {
			return -1;
		}
		int strLen = str.length();
		if (strLen == 1) {
			return indexOf(str.charAt(0), startIndex);
		}
		if (strLen == 0) {
			return startIndex;
		}
		if (strLen > size) {
			return -1;
		}
		char[] thisBuf = buffer;
		int len = (size - strLen) + 1;
		outer : for (int i = startIndex; i < len; i++) {
			for (int j = 0; j < strLen; j++) {
				if (str.charAt(j) != thisBuf[i + j]) {
					continue outer;
				}
			}
			return i;
		}
		return -1;
	}

	public int indexOf(org.apache.commons.lang.text.StrMatcher matcher) {
		return indexOf(matcher, 0);
	}

	public int indexOf(org.apache.commons.lang.text.StrMatcher matcher, int startIndex) {
		startIndex = (startIndex < 0) ? 0 : startIndex;
		if ((matcher == null) || (startIndex >= size)) {
			return -1;
		}
		int len = size;
		char[] buf = buffer;
		for (int i = startIndex; i < len; i++) {
			if (matcher.isMatch(buf, i, startIndex, len) > 0) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(char ch) {
		return lastIndexOf(ch, size - 1);
	}

	public int lastIndexOf(char ch, int startIndex) {
		startIndex = (startIndex >= size) ? size - 1 : startIndex;
		if (startIndex < 0) {
			return -1;
		}
		for (int i = startIndex; i >= 0; i--) {
			if (buffer[i] == ch) {
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(java.lang.String str) {
		return lastIndexOf(str, size - 1);
	}

	public int lastIndexOf(java.lang.String str, int startIndex) {
		startIndex = (startIndex >= size) ? size - 1 : startIndex;
		if ((str == null) || (startIndex < 0)) {
			return -1;
		}
		int strLen = str.length();
		if ((strLen > 0) && (strLen <= size)) {
			if (strLen == 1) {
				return lastIndexOf(str.charAt(0), startIndex);
			}
			outer : for (int i = (startIndex - strLen) + 1; i >= 0; i--) {
				for (int j = 0; j < strLen; j++) {
					if (str.charAt(j) != buffer[i + j]) {
						continue outer;
					}
				}
				return i;
			}
		} else if (strLen == 0) {
			return startIndex;
		}
		return -1;
	}

	public int lastIndexOf(org.apache.commons.lang.text.StrMatcher matcher) {
		return lastIndexOf(matcher, size);
	}

	public int lastIndexOf(org.apache.commons.lang.text.StrMatcher matcher, int startIndex) {
		startIndex = (startIndex >= size) ? size - 1 : startIndex;
		if ((matcher == null) || (startIndex < 0)) {
			return -1;
		}
		char[] buf = buffer;
		int endIndex = startIndex + 1;
		for (int i = startIndex; i >= 0; i--) {
			if (matcher.isMatch(buf, i, 0, endIndex) > 0) {
				return i;
			}
		}
		return -1;
	}

	public org.apache.commons.lang.text.StrTokenizer asTokenizer() {
		return new org.apache.commons.lang.text.StrBuilder.StrBuilderTokenizer();
	}

	public java.io.Reader asReader() {
		return new org.apache.commons.lang.text.StrBuilder.StrBuilderReader();
	}

	public java.io.Writer asWriter() {
		return new org.apache.commons.lang.text.StrBuilder.StrBuilderWriter();
	}

	public boolean equalsIgnoreCase(org.apache.commons.lang.text.StrBuilder other) {
		if (this == other) {
			return true;
		}
		if (this.size != other.size) {
			return false;
		}
		char[] thisBuf = this.buffer;
		char[] otherBuf = other.buffer;
		for (int i = size - 1; i >= 0; i--) {
			char c1 = thisBuf[i];
			char c2 = otherBuf[i];
			if ((c1 != c2) && (java.lang.Character.toUpperCase(c1) != java.lang.Character.toUpperCase(c2))) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(org.apache.commons.lang.text.StrBuilder other) {
		if (this == other) {
			return true;
		}
		if (this.size != other.size) {
			return false;
		}
		char[] thisBuf = this.buffer;
		char[] otherBuf = other.buffer;
		for (int i = size - 1; i >= 0; i--) {
			if (thisBuf[i] != otherBuf[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean equals(java.lang.Object obj) {
		if (obj instanceof org.apache.commons.lang.text.StrBuilder) {
			return equals(((org.apache.commons.lang.text.StrBuilder) (obj)));
		}
		return false;
	}

	public int hashCode() {
		char[] buf = buffer;
		int hash = 0;
		for (int i = size - 1; i >= 0; i--) {
			hash = (31 * hash) + buf[i];
		}
		return hash;
	}

	public java.lang.String toString() {
		return new java.lang.String(buffer, 0, size);
	}

	public java.lang.StringBuffer toStringBuffer() {
		return new java.lang.StringBuffer(size).append(buffer, 0, size);
	}

	protected int validateRange(int startIndex, int endIndex) {
		if (startIndex < 0) {
			throw new java.lang.StringIndexOutOfBoundsException(startIndex);
		}
		if (endIndex > size) {
			endIndex = size;
		}
		if (startIndex > endIndex) {
			throw new java.lang.StringIndexOutOfBoundsException("end < start");
		}
		return endIndex;
	}

	protected void validateIndex(int index) {
		if ((index < 0) || (index > size)) {
			throw new java.lang.StringIndexOutOfBoundsException(index);
		}
	}

	class StrBuilderTokenizer extends org.apache.commons.lang.text.StrTokenizer {
		StrBuilderTokenizer() {
			super();
		}

		protected java.util.List tokenize(char[] chars, int offset, int count) {
			if (chars == null) {
				return super.tokenize(org.apache.commons.lang.text.StrBuilder.this.buffer, 0, org.apache.commons.lang.text.StrBuilder.this.size());
			} else {
				return super.tokenize(chars, offset, count);
			}
		}

		public java.lang.String getContent() {
			java.lang.String str = super.getContent();
			if (str == null) {
				return org.apache.commons.lang.text.StrBuilder.this.toString();
			} else {
				return str;
			}
		}
	}

	class StrBuilderReader extends java.io.Reader {
		private int pos;

		private int mark;

		StrBuilderReader() {
			super();
		}

		public void close() {
		}

		public int read() {
			if (ready() == false) {
				return -1;
			}
			return org.apache.commons.lang.text.StrBuilder.this.charAt(pos++);
		}

		public int read(char[] b, int off, int len) {
			if (((((off < 0) || (len < 0)) || (off > b.length)) || ((off + len) > b.length)) || ((off + len) < 0)) {
				throw new java.lang.IndexOutOfBoundsException();
			}
			if (len == 0) {
				return 0;
			}
			if (pos >= org.apache.commons.lang.text.StrBuilder.this.size()) {
				return -1;
			}
			if ((pos + len) > size()) {
				len = org.apache.commons.lang.text.StrBuilder.this.size() - pos;
			}
			org.apache.commons.lang.text.StrBuilder.this.getChars(pos, pos + len, b, off);
			pos += len;
			return len;
		}

		public long skip(long n) {
			if ((pos + n) > org.apache.commons.lang.text.StrBuilder.this.size()) {
				n = org.apache.commons.lang.text.StrBuilder.this.size() - pos;
			}
			if (n < 0) {
				return 0;
			}
			pos += n;
			return n;
		}

		public boolean ready() {
			return pos < org.apache.commons.lang.text.StrBuilder.this.size();
		}

		public boolean markSupported() {
			return true;
		}

		public void mark(int readAheadLimit) {
			mark = pos;
		}

		public void reset() {
			pos = mark;
		}
	}

	class StrBuilderWriter extends java.io.Writer {
		StrBuilderWriter() {
			super();
		}

		public void close() {
		}

		public void flush() {
		}

		public void write(int c) {
			org.apache.commons.lang.text.StrBuilder.this.append(((char) (c)));
		}

		public void write(char[] cbuf) {
			org.apache.commons.lang.text.StrBuilder.this.append(cbuf);
		}

		public void write(char[] cbuf, int off, int len) {
			org.apache.commons.lang.text.StrBuilder.this.append(cbuf, off, len);
		}

		public void write(java.lang.String str) {
			org.apache.commons.lang.text.StrBuilder.this.append(str);
		}

		public void write(java.lang.String str, int off, int len) {
			org.apache.commons.lang.text.StrBuilder.this.append(str, off, len);
		}
	}
}