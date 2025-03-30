package org.apache.commons.lang3.time;
public class FastDateParser implements org.apache.commons.lang3.time.DateParser , java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private static final java.util.concurrent.ConcurrentMap<java.util.Locale, org.apache.commons.lang3.time.FastDateParser.TimeZoneStrategy> tzsCache = new java.util.concurrent.ConcurrentHashMap<java.util.Locale, org.apache.commons.lang3.time.FastDateParser.TimeZoneStrategy>(3);

	static final java.util.Locale JAPANESE_IMPERIAL = new java.util.Locale("ja", "JP", "JP");

	private final java.lang.String pattern;

	private final java.util.TimeZone timeZone;

	private final java.util.Locale locale;

	private transient java.util.regex.Pattern parsePattern;

	private transient org.apache.commons.lang3.time.FastDateParser.Strategy[] strategies;

	private transient int thisYear;

	private transient java.util.concurrent.ConcurrentMap<java.lang.Integer, org.apache.commons.lang3.time.FastDateParser.KeyValue[]> nameValues;

	private transient java.lang.String currentFormatField;

	private transient org.apache.commons.lang3.time.FastDateParser.Strategy nextStrategy;

	protected FastDateParser(java.lang.String pattern, java.util.TimeZone timeZone, java.util.Locale locale) {
		this.pattern = pattern;
		this.timeZone = timeZone;
		this.locale = locale;
		init();
	}

	private void init() {
		thisYear = java.util.Calendar.getInstance(timeZone, locale).get(java.util.Calendar.YEAR);
		nameValues = new java.util.concurrent.ConcurrentHashMap<java.lang.Integer, org.apache.commons.lang3.time.FastDateParser.KeyValue[]>();
		java.lang.StringBuilder regex = new java.lang.StringBuilder();
		java.util.List<org.apache.commons.lang3.time.FastDateParser.Strategy> collector = new java.util.ArrayList<org.apache.commons.lang3.time.FastDateParser.Strategy>();
		java.util.regex.Matcher patternMatcher = org.apache.commons.lang3.time.FastDateParser.formatPattern.matcher(pattern);
		if (!patternMatcher.lookingAt()) {
			throw new java.lang.IllegalArgumentException("Invalid pattern");
		}
		currentFormatField = patternMatcher.group();
		org.apache.commons.lang3.time.FastDateParser.Strategy currentStrategy = getStrategy(currentFormatField);
		for (; ;) {
			patternMatcher.region(patternMatcher.end(), patternMatcher.regionEnd());
			if (!patternMatcher.lookingAt()) {
				nextStrategy = null;
				break;
			}
			java.lang.String nextFormatField = patternMatcher.group();
			nextStrategy = getStrategy(nextFormatField);
			if (currentStrategy.addRegex(this, regex)) {
				collector.add(currentStrategy);
			}
			currentFormatField = nextFormatField;
			currentStrategy = nextStrategy;
		}
		if (currentStrategy.addRegex(this, regex)) {
			collector.add(currentStrategy);
		}
		currentFormatField = null;
		strategies = collector.toArray(new org.apache.commons.lang3.time.FastDateParser.Strategy[collector.size()]);
		parsePattern = java.util.regex.Pattern.compile(regex.toString());
	}

	@java.lang.Override
	public java.lang.String getPattern() {
		return pattern;
	}

	@java.lang.Override
	public java.util.TimeZone getTimeZone() {
		return timeZone;
	}

	@java.lang.Override
	public java.util.Locale getLocale() {
		return locale;
	}

	java.util.regex.Pattern getParsePattern() {
		return parsePattern;
	}

	@java.lang.Override
	public boolean equals(java.lang.Object obj) {
		if (!(obj instanceof org.apache.commons.lang3.time.FastDateParser)) {
			return false;
		}
		org.apache.commons.lang3.time.FastDateParser other = ((org.apache.commons.lang3.time.FastDateParser) (obj));
		return (pattern.equals(other.pattern) && timeZone.equals(other.timeZone)) && locale.equals(other.locale);
	}

	@java.lang.Override
	public int hashCode() {
		return pattern.hashCode() + (13 * (timeZone.hashCode() + (13 * locale.hashCode())));
	}

	@java.lang.Override
	public java.lang.String toString() {
		return ((((("FastDateParser[" + pattern) + ",") + locale) + ",") + timeZone.getID()) + "]";
	}

	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	@java.lang.Override
	public java.lang.Object parseObject(java.lang.String source) throws java.text.ParseException {
		return parse(source);
	}

	@java.lang.Override
	public java.util.Date parse(java.lang.String source) throws java.text.ParseException {
		java.util.Date date = parse(source, new java.text.ParsePosition(0));
		if (date == null) {
			if (locale.equals(org.apache.commons.lang3.time.FastDateParser.JAPANESE_IMPERIAL)) {
				throw new java.text.ParseException(((((("(The " + locale) + " locale does not support dates before 1868 AD)\n") + "Unparseable date: \"") + source) + "\" does not match ") + parsePattern.pattern(), 0);
			}
			throw new java.text.ParseException((("Unparseable date: \"" + source) + "\" does not match ") + parsePattern.pattern(), 0);
		}
		return date;
	}

	@java.lang.Override
	public java.lang.Object parseObject(java.lang.String source, java.text.ParsePosition pos) {
		return parse(source, pos);
	}

	@java.lang.Override
	public java.util.Date parse(java.lang.String source, java.text.ParsePosition pos) {
		int offset = pos.getIndex();
		java.util.regex.Matcher matcher = parsePattern.matcher(source.substring(offset));
		if (!matcher.lookingAt()) {
			return null;
		}
		java.util.Calendar cal = java.util.Calendar.getInstance(timeZone, locale);
		cal.clear();
		for (int i = 0; i < strategies.length;) {
			org.apache.commons.lang3.time.FastDateParser.Strategy strategy = strategies[i++];
			strategy.setCalendar(this, cal, matcher.group(i));
		}
		pos.setIndex(offset + matcher.end());
		return cal.getTime();
	}

	private static java.lang.StringBuilder escapeRegex(java.lang.StringBuilder regex, java.lang.String value, boolean unquote) {
		boolean wasWhite = false;
		for (int i = 0; i < value.length(); ++i) {
			char c = value.charAt(i);
			if (java.lang.Character.isWhitespace(c)) {
				if (!wasWhite) {
					wasWhite = true;
					regex.append("\\s*+");
				}
				continue;
			}
			wasWhite = false;
			switch (c) {
				case '\'' :
					{
						if (unquote) {
							if ((++i) == value.length()) {
								return regex;
							}
							c = value.charAt(i);
						}
						break;
					}
				case '?' :
				case '[' :
				case ']' :
				case '(' :
				case ')' :
				case '{' :
				case '}' :
				case '\\' :
				case '|' :
				case '*' :
				case '+' :
				case '^' :
				case '$' :
				case '.' :
					{
						regex.append('\\');
					}
			}
			regex.append(c);
		}
		return regex;
	}

	private static class KeyValue {
		public java.lang.String key;

		public int value;

		public KeyValue(java.lang.String key, int value) {
			this.key = key;
			this.value = value;
		}
	}

	private static final java.util.Comparator<org.apache.commons.lang3.time.FastDateParser.KeyValue> IGNORE_CASE_COMPARATOR = new java.util.Comparator<org.apache.commons.lang3.time.FastDateParser.KeyValue>() {
		@java.lang.Override
		public int compare(org.apache.commons.lang3.time.FastDateParser.KeyValue left, org.apache.commons.lang3.time.FastDateParser.KeyValue right) {
			return left.key.compareToIgnoreCase(right.key);
		}
	};

	org.apache.commons.lang3.time.FastDateParser.KeyValue[] getDisplayNames(int field) {
		java.lang.Integer fieldInt = java.lang.Integer.valueOf(field);
		org.apache.commons.lang3.time.FastDateParser.KeyValue[] fieldKeyValues = nameValues.get(fieldInt);
		if (fieldKeyValues == null) {
			java.text.DateFormatSymbols symbols = java.text.DateFormatSymbols.getInstance(locale);
			switch (field) {
				case java.util.Calendar.ERA :
					{
						java.util.Calendar c = java.util.Calendar.getInstance(locale);
						java.lang.String[] shortEras = toArray(c.getDisplayNames(java.util.Calendar.ERA, java.util.Calendar.SHORT, locale));
						java.lang.String[] longEras = toArray(c.getDisplayNames(java.util.Calendar.ERA, java.util.Calendar.LONG, locale));
						fieldKeyValues = org.apache.commons.lang3.time.FastDateParser.createKeyValues(longEras, shortEras);
						break;
					}
				case java.util.Calendar.DAY_OF_WEEK :
					{
						fieldKeyValues = org.apache.commons.lang3.time.FastDateParser.createKeyValues(symbols.getWeekdays(), symbols.getShortWeekdays());
						break;
					}
				case java.util.Calendar.AM_PM :
					{
						fieldKeyValues = org.apache.commons.lang3.time.FastDateParser.createKeyValues(symbols.getAmPmStrings(), null);
						break;
					}
				case java.util.Calendar.MONTH :
					{
						fieldKeyValues = org.apache.commons.lang3.time.FastDateParser.createKeyValues(symbols.getMonths(), symbols.getShortMonths());
						break;
					}
				default :
					throw new java.lang.IllegalArgumentException("Invalid field value " + field);
			}
			org.apache.commons.lang3.time.FastDateParser.KeyValue[] prior = nameValues.putIfAbsent(fieldInt, fieldKeyValues);
			if (prior != null) {
				fieldKeyValues = prior;
			}
		}
		return fieldKeyValues;
	}

	private java.lang.String[] toArray(java.util.Map<java.lang.String, java.lang.Integer> era) {
		java.lang.String[] eras = new java.lang.String[era.size()];
		for (java.util.Map.Entry<java.lang.String, java.lang.Integer> me : era.entrySet()) {
			int idx = me.getValue().intValue();
			final java.lang.String key = me.getKey();
			if (key == null) {
				throw new java.lang.IllegalArgumentException();
			}
			eras[idx] = key;
		}
		return eras;
	}

	private static org.apache.commons.lang3.time.FastDateParser.KeyValue[] createKeyValues(java.lang.String[] longValues, java.lang.String[] shortValues) {
		org.apache.commons.lang3.time.FastDateParser.KeyValue[] fieldKeyValues = new org.apache.commons.lang3.time.FastDateParser.KeyValue[org.apache.commons.lang3.time.FastDateParser.count(longValues) + org.apache.commons.lang3.time.FastDateParser.count(shortValues)];
		org.apache.commons.lang3.time.FastDateParser.copy(fieldKeyValues, org.apache.commons.lang3.time.FastDateParser.copy(fieldKeyValues, 0, longValues), shortValues);
		java.util.Arrays.sort(fieldKeyValues, org.apache.commons.lang3.time.FastDateParser.IGNORE_CASE_COMPARATOR);
		return fieldKeyValues;
	}

	private static int count(java.lang.String[] values) {
		int count = 0;
		if (values != null) {
			for (java.lang.String value : values) {
				if (value.length() > 0) {
					++count;
				}
			}
		}
		return count;
	}

	private static int copy(org.apache.commons.lang3.time.FastDateParser.KeyValue[] fieldKeyValues, int offset, java.lang.String[] values) {
		if (values != null) {
			for (int i = 0; i < values.length; ++i) {
				java.lang.String value = values[i];
				if (value.length() > 0) {
					fieldKeyValues[offset++] = new org.apache.commons.lang3.time.FastDateParser.KeyValue(value, i);
				}
			}
		}
		return offset;
	}

	int adjustYear(int twoDigitYear) {
		int trial = (twoDigitYear + thisYear) - (thisYear % 100);
		if (trial < (thisYear + 20)) {
			return trial;
		}
		return trial - 100;
	}

	boolean isNextNumber() {
		return (nextStrategy != null) && nextStrategy.isNumber();
	}

	int getFieldWidth() {
		return currentFormatField.length();
	}

	private interface Strategy {
		boolean isNumber();

		void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value);

		boolean addRegex(org.apache.commons.lang3.time.FastDateParser parser, java.lang.StringBuilder regex);
	}

	private static final java.util.regex.Pattern formatPattern = java.util.regex.Pattern.compile("D+|E+|F+|G+|H+|K+|M+|S+|W+|Z+|a+|d+|h+|k+|m+|s+|w+|y+|z+|''|'[^']++(''[^']*+)*+'|[^'A-Za-z]++");

	private org.apache.commons.lang3.time.FastDateParser.Strategy getStrategy(java.lang.String formatField) {
		switch (formatField.charAt(0)) {
			case '\'' :
				{
					if (formatField.length() > 2) {
						formatField = formatField.substring(1, formatField.length() - 1);
					}
				}
			default :
				{
					return new org.apache.commons.lang3.time.FastDateParser.CopyQuotedStrategy(formatField);
				}
			case 'D' :
				{
					return org.apache.commons.lang3.time.FastDateParser.DAY_OF_YEAR_STRATEGY;
				}
			case 'E' :
				{
					return org.apache.commons.lang3.time.FastDateParser.DAY_OF_WEEK_STRATEGY;
				}
			case 'F' :
				{
					return org.apache.commons.lang3.time.FastDateParser.DAY_OF_WEEK_IN_MONTH_STRATEGY;
				}
			case 'G' :
				{
					return org.apache.commons.lang3.time.FastDateParser.ERA_STRATEGY;
				}
			case 'H' :
				{
					return org.apache.commons.lang3.time.FastDateParser.MODULO_HOUR_OF_DAY_STRATEGY;
				}
			case 'K' :
				{
					return org.apache.commons.lang3.time.FastDateParser.HOUR_STRATEGY;
				}
			case 'M' :
				{
					return formatField.length() >= 3 ? org.apache.commons.lang3.time.FastDateParser.TEXT_MONTH_STRATEGY : org.apache.commons.lang3.time.FastDateParser.NUMBER_MONTH_STRATEGY;
				}
			case 'S' :
				{
					return org.apache.commons.lang3.time.FastDateParser.MILLISECOND_STRATEGY;
				}
			case 'W' :
				{
					return org.apache.commons.lang3.time.FastDateParser.WEEK_OF_MONTH_STRATEGY;
				}
			case 'Z' :
				break;
			case 'a' :
				{
					return org.apache.commons.lang3.time.FastDateParser.AM_PM_STRATEGY;
				}
			case 'd' :
				{
					return org.apache.commons.lang3.time.FastDateParser.DAY_OF_MONTH_STRATEGY;
				}
			case 'h' :
				{
					return org.apache.commons.lang3.time.FastDateParser.MODULO_HOUR_STRATEGY;
				}
			case 'k' :
				{
					return org.apache.commons.lang3.time.FastDateParser.HOUR_OF_DAY_STRATEGY;
				}
			case 'm' :
				{
					return org.apache.commons.lang3.time.FastDateParser.MINUTE_STRATEGY;
				}
			case 's' :
				{
					return org.apache.commons.lang3.time.FastDateParser.SECOND_STRATEGY;
				}
			case 'w' :
				{
					return org.apache.commons.lang3.time.FastDateParser.WEEK_OF_YEAR_STRATEGY;
				}
			case 'y' :
				{
					return formatField.length() > 2 ? org.apache.commons.lang3.time.FastDateParser.LITERAL_YEAR_STRATEGY : org.apache.commons.lang3.time.FastDateParser.ABBREVIATED_YEAR_STRATEGY;
				}
			case 'z' :
				break;
		}
		org.apache.commons.lang3.time.FastDateParser.TimeZoneStrategy tzs = org.apache.commons.lang3.time.FastDateParser.tzsCache.get(locale);
		if (tzs == null) {
			tzs = new org.apache.commons.lang3.time.FastDateParser.TimeZoneStrategy(locale);
			org.apache.commons.lang3.time.FastDateParser.TimeZoneStrategy inCache = org.apache.commons.lang3.time.FastDateParser.tzsCache.putIfAbsent(locale, tzs);
			if (inCache != null) {
				return inCache;
			}
		}
		return tzs;
	}

	private static class CopyQuotedStrategy implements org.apache.commons.lang3.time.FastDateParser.Strategy {
		private final java.lang.String formatField;

		CopyQuotedStrategy(java.lang.String formatField) {
			this.formatField = formatField;
		}

		@java.lang.Override
		public boolean isNumber() {
			char c = formatField.charAt(0);
			if (c == '\'') {
				c = formatField.charAt(1);
			}
			return java.lang.Character.isDigit(c);
		}

		@java.lang.Override
		public boolean addRegex(org.apache.commons.lang3.time.FastDateParser parser, java.lang.StringBuilder regex) {
			org.apache.commons.lang3.time.FastDateParser.escapeRegex(regex, formatField, true);
			return false;
		}

		@java.lang.Override
		public void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value) {
		}
	}

	private static class TextStrategy implements org.apache.commons.lang3.time.FastDateParser.Strategy {
		private final int field;

		TextStrategy(int field) {
			this.field = field;
		}

		@java.lang.Override
		public boolean isNumber() {
			return false;
		}

		@java.lang.Override
		public boolean addRegex(org.apache.commons.lang3.time.FastDateParser parser, java.lang.StringBuilder regex) {
			regex.append('(');
			for (org.apache.commons.lang3.time.FastDateParser.KeyValue textKeyValue : parser.getDisplayNames(field)) {
				org.apache.commons.lang3.time.FastDateParser.escapeRegex(regex, textKeyValue.key, false).append('|');
			}
			regex.setCharAt(regex.length() - 1, ')');
			return true;
		}

		@java.lang.Override
		public void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value) {
			org.apache.commons.lang3.time.FastDateParser.KeyValue[] textKeyValues = parser.getDisplayNames(field);
			int idx = java.util.Arrays.binarySearch(textKeyValues, new org.apache.commons.lang3.time.FastDateParser.KeyValue(value, -1), org.apache.commons.lang3.time.FastDateParser.IGNORE_CASE_COMPARATOR);
			if (idx < 0) {
				java.lang.StringBuilder sb = new java.lang.StringBuilder(value);
				sb.append(" not in (");
				for (org.apache.commons.lang3.time.FastDateParser.KeyValue textKeyValue : textKeyValues) {
					sb.append(textKeyValue.key).append(' ');
				}
				sb.setCharAt(sb.length() - 1, ')');
				throw new java.lang.IllegalArgumentException(sb.toString());
			}
			cal.set(field, textKeyValues[idx].value);
		}
	}

	private static class NumberStrategy implements org.apache.commons.lang3.time.FastDateParser.Strategy {
		protected final int field;

		NumberStrategy(int field) {
			this.field = field;
		}

		@java.lang.Override
		public boolean isNumber() {
			return true;
		}

		@java.lang.Override
		public boolean addRegex(org.apache.commons.lang3.time.FastDateParser parser, java.lang.StringBuilder regex) {
			if (parser.isNextNumber()) {
				regex.append("(\\p{IsNd}{").append(parser.getFieldWidth()).append("}+)");
			} else {
				regex.append("(\\p{IsNd}++)");
			}
			return true;
		}

		@java.lang.Override
		public void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value) {
			cal.set(field, modify(java.lang.Integer.parseInt(value)));
		}

		public int modify(int iValue) {
			return iValue;
		}
	}

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy ABBREVIATED_YEAR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.YEAR) {
		@java.lang.Override
		public void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value) {
			int iValue = java.lang.Integer.parseInt(value);
			if (iValue < 100) {
				iValue = parser.adjustYear(iValue);
			}
			cal.set(java.util.Calendar.YEAR, iValue);
		}
	};

	private static class TimeZoneStrategy implements org.apache.commons.lang3.time.FastDateParser.Strategy {
		final java.lang.String validTimeZoneChars;

		final java.util.SortedMap<java.lang.String, java.util.TimeZone> tzNames = new java.util.TreeMap<java.lang.String, java.util.TimeZone>(java.lang.String.CASE_INSENSITIVE_ORDER);

		TimeZoneStrategy(java.util.Locale locale) {
			for (java.lang.String id : java.util.TimeZone.getAvailableIDs()) {
				if (id.startsWith("GMT")) {
					continue;
				}
				java.util.TimeZone tz = java.util.TimeZone.getTimeZone(id);
				tzNames.put(tz.getDisplayName(false, java.util.TimeZone.SHORT, locale), tz);
				tzNames.put(tz.getDisplayName(false, java.util.TimeZone.LONG, locale), tz);
				if (tz.useDaylightTime()) {
					tzNames.put(tz.getDisplayName(true, java.util.TimeZone.SHORT, locale), tz);
					tzNames.put(tz.getDisplayName(true, java.util.TimeZone.LONG, locale), tz);
				}
			}
			java.lang.StringBuilder sb = new java.lang.StringBuilder();
			sb.append("(GMT[+\\-]\\d{0,1}\\d{2}|[+\\-]\\d{2}:?\\d{2}|");
			for (java.lang.String id : tzNames.keySet()) {
				org.apache.commons.lang3.time.FastDateParser.escapeRegex(sb, id, false).append('|');
			}
			sb.setCharAt(sb.length() - 1, ')');
			validTimeZoneChars = sb.toString();
		}

		@java.lang.Override
		public boolean isNumber() {
			return false;
		}

		@java.lang.Override
		public boolean addRegex(org.apache.commons.lang3.time.FastDateParser parser, java.lang.StringBuilder regex) {
			regex.append(validTimeZoneChars);
			return true;
		}

		@java.lang.Override
		public void setCalendar(org.apache.commons.lang3.time.FastDateParser parser, java.util.Calendar cal, java.lang.String value) {
			java.util.TimeZone tz;
			if ((value.charAt(0) == '+') || (value.charAt(0) == '-')) {
				tz = java.util.TimeZone.getTimeZone("GMT" + value);
			} else if (value.startsWith("GMT")) {
				tz = java.util.TimeZone.getTimeZone(value);
			} else {
				tz = tzNames.get(value);
				if (tz == null) {
					throw new java.lang.IllegalArgumentException(value + " is not a supported timezone name");
				}
			}
			cal.setTimeZone(tz);
		}
	}

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy ERA_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.TextStrategy(java.util.Calendar.ERA);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy DAY_OF_WEEK_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.TextStrategy(java.util.Calendar.DAY_OF_WEEK);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy AM_PM_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.TextStrategy(java.util.Calendar.AM_PM);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy TEXT_MONTH_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.TextStrategy(java.util.Calendar.MONTH);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy NUMBER_MONTH_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.MONTH) {
		@java.lang.Override
		public int modify(int iValue) {
			return iValue - 1;
		}
	};

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy LITERAL_YEAR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.YEAR);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy WEEK_OF_YEAR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.WEEK_OF_YEAR);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy WEEK_OF_MONTH_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.WEEK_OF_MONTH);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy DAY_OF_YEAR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.DAY_OF_YEAR);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy DAY_OF_MONTH_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.DAY_OF_MONTH);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy DAY_OF_WEEK_IN_MONTH_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.DAY_OF_WEEK_IN_MONTH);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy HOUR_OF_DAY_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.HOUR_OF_DAY);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy MODULO_HOUR_OF_DAY_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.HOUR_OF_DAY) {
		@java.lang.Override
		public int modify(int iValue) {
			return iValue % 24;
		}
	};

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy MODULO_HOUR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.HOUR) {
		@java.lang.Override
		public int modify(int iValue) {
			return iValue % 12;
		}
	};

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy HOUR_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.HOUR);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy MINUTE_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.MINUTE);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy SECOND_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.SECOND);

	private static final org.apache.commons.lang3.time.FastDateParser.Strategy MILLISECOND_STRATEGY = new org.apache.commons.lang3.time.FastDateParser.NumberStrategy(java.util.Calendar.MILLISECOND);
}