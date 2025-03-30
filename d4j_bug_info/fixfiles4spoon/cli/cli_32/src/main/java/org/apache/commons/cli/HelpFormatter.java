package org.apache.commons.cli;


public class HelpFormatter {
    public static final int DEFAULT_WIDTH = 74;

    public static final int DEFAULT_LEFT_PAD = 1;

    public static final int DEFAULT_DESC_PAD = 3;

    public static final java.lang.String DEFAULT_SYNTAX_PREFIX = "usage: ";

    public static final java.lang.String DEFAULT_OPT_PREFIX = "-";

    public static final java.lang.String DEFAULT_LONG_OPT_PREFIX = "--";

    public static final java.lang.String DEFAULT_LONG_OPT_SEPARATOR = " ";

    public static final java.lang.String DEFAULT_ARG_NAME = "arg";

    public int defaultWidth = org.apache.commons.cli.HelpFormatter.DEFAULT_WIDTH;

    public int defaultLeftPad = org.apache.commons.cli.HelpFormatter.DEFAULT_LEFT_PAD;

    public int defaultDescPad = org.apache.commons.cli.HelpFormatter.DEFAULT_DESC_PAD;

    public java.lang.String defaultSyntaxPrefix = org.apache.commons.cli.HelpFormatter.DEFAULT_SYNTAX_PREFIX;

    public java.lang.String defaultNewLine = java.lang.System.getProperty("line.separator");

    public java.lang.String defaultOptPrefix = org.apache.commons.cli.HelpFormatter.DEFAULT_OPT_PREFIX;

    public java.lang.String defaultLongOptPrefix = org.apache.commons.cli.HelpFormatter.DEFAULT_LONG_OPT_PREFIX;

    private java.lang.String longOptSeparator = org.apache.commons.cli.HelpFormatter.DEFAULT_LONG_OPT_SEPARATOR;

    public java.lang.String defaultArgName = org.apache.commons.cli.HelpFormatter.DEFAULT_ARG_NAME;

    protected java.util.Comparator optionComparator = new org.apache.commons.cli.HelpFormatter.OptionComparator();

    public void setWidth(int width) {
        this.defaultWidth = width;
    }

    public int getWidth() {
        return defaultWidth;
    }

    public void setLeftPadding(int padding) {
        this.defaultLeftPad = padding;
    }

    public int getLeftPadding() {
        return defaultLeftPad;
    }

    public void setDescPadding(int padding) {
        this.defaultDescPad = padding;
    }

    public int getDescPadding() {
        return defaultDescPad;
    }

    public void setSyntaxPrefix(java.lang.String prefix) {
        this.defaultSyntaxPrefix = prefix;
    }

    public java.lang.String getSyntaxPrefix() {
        return defaultSyntaxPrefix;
    }

    public void setNewLine(java.lang.String newline) {
        this.defaultNewLine = newline;
    }

    public java.lang.String getNewLine() {
        return defaultNewLine;
    }

    public void setOptPrefix(java.lang.String prefix) {
        this.defaultOptPrefix = prefix;
    }

    public java.lang.String getOptPrefix() {
        return defaultOptPrefix;
    }

    public void setLongOptPrefix(java.lang.String prefix) {
        this.defaultLongOptPrefix = prefix;
    }

    public java.lang.String getLongOptPrefix() {
        return defaultLongOptPrefix;
    }

    public void setLongOptSeparator(java.lang.String longOptSeparator) {
        this.longOptSeparator = longOptSeparator;
    }

    public java.lang.String getLongOptSeparator() {
        return longOptSeparator;
    }

    public void setArgName(java.lang.String name) {
        this.defaultArgName = name;
    }

    public java.lang.String getArgName() {
        return defaultArgName;
    }

    public java.util.Comparator getOptionComparator() {
        return optionComparator;
    }

    public void setOptionComparator(java.util.Comparator comparator) {
        if (comparator == null) {
            this.optionComparator = new org.apache.commons.cli.HelpFormatter.OptionComparator();
        }else {
            this.optionComparator = comparator;
        }
    }

    public void printHelp(java.lang.String cmdLineSyntax, org.apache.commons.cli.Options options) {
        printHelp(defaultWidth, cmdLineSyntax, null, options, null, false);
    }

    public void printHelp(java.lang.String cmdLineSyntax, org.apache.commons.cli.Options options, boolean autoUsage) {
        printHelp(defaultWidth, cmdLineSyntax, null, options, null, autoUsage);
    }

    public void printHelp(java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, java.lang.String footer) {
        printHelp(cmdLineSyntax, header, options, footer, false);
    }

    public void printHelp(java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, java.lang.String footer, boolean autoUsage) {
        printHelp(defaultWidth, cmdLineSyntax, header, options, footer, autoUsage);
    }

    public void printHelp(int width, java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, java.lang.String footer) {
        printHelp(width, cmdLineSyntax, header, options, footer, false);
    }

    public void printHelp(int width, java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, java.lang.String footer, boolean autoUsage) {
        java.io.PrintWriter pw = new java.io.PrintWriter(java.lang.System.out);
        printHelp(pw, width, cmdLineSyntax, header, options, defaultLeftPad, defaultDescPad, footer, autoUsage);
        pw.flush();
    }

    public void printHelp(java.io.PrintWriter pw, int width, java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, int leftPad, int descPad, java.lang.String footer) {
        printHelp(pw, width, cmdLineSyntax, header, options, leftPad, descPad, footer, false);
    }

    public void printHelp(java.io.PrintWriter pw, int width, java.lang.String cmdLineSyntax, java.lang.String header, org.apache.commons.cli.Options options, int leftPad, int descPad, java.lang.String footer, boolean autoUsage) {
        if ((cmdLineSyntax == null) || ((cmdLineSyntax.length()) == 0)) {
            throw new java.lang.IllegalArgumentException("cmdLineSyntax not provided");
        }
        if (autoUsage) {
            printUsage(pw, width, cmdLineSyntax, options);
        }else {
            printUsage(pw, width, cmdLineSyntax);
        }
        if ((header != null) && ((header.trim().length()) > 0)) {
            printWrapped(pw, width, header);
        }
        printOptions(pw, width, options, leftPad, descPad);
        if ((footer != null) && ((footer.trim().length()) > 0)) {
            printWrapped(pw, width, footer);
        }
    }

    public void printUsage(java.io.PrintWriter pw, int width, java.lang.String app, org.apache.commons.cli.Options options) {
        java.lang.StringBuffer buff = new java.lang.StringBuffer(defaultSyntaxPrefix).append(app).append(" ");
        final java.util.Collection processedGroups = new java.util.ArrayList();
        org.apache.commons.cli.Option option;
        java.util.List optList = new java.util.ArrayList(options.getOptions());
        java.util.Collections.sort(optList, getOptionComparator());
        for (java.util.Iterator i = optList.iterator(); i.hasNext();) {
            option = ((org.apache.commons.cli.Option) (i.next()));
            org.apache.commons.cli.OptionGroup group = options.getOptionGroup(option);
            if (group != null) {
                if (!(processedGroups.contains(group))) {
                    processedGroups.add(group);
                    appendOptionGroup(buff, group);
                }
            }else {
                appendOption(buff, option, option.isRequired());
            }
            if (i.hasNext()) {
                buff.append(" ");
            }
        }
        printWrapped(pw, width, ((buff.toString().indexOf(' ')) + 1), buff.toString());
    }

    private void appendOptionGroup(final java.lang.StringBuffer buff, final org.apache.commons.cli.OptionGroup group) {
        if (!(group.isRequired())) {
            buff.append("[");
        }
        java.util.List optList = new java.util.ArrayList(group.getOptions());
        java.util.Collections.sort(optList, getOptionComparator());
        for (java.util.Iterator i = optList.iterator(); i.hasNext();) {
            appendOption(buff, ((org.apache.commons.cli.Option) (i.next())), true);
            if (i.hasNext()) {
                buff.append(" | ");
            }
        }
        if (!(group.isRequired())) {
            buff.append("]");
        }
    }

    private void appendOption(final java.lang.StringBuffer buff, final org.apache.commons.cli.Option option, final boolean required) {
        if (!required) {
            buff.append("[");
        }
        if ((option.getOpt()) != null) {
            buff.append("-").append(option.getOpt());
        }else {
            buff.append("--").append(option.getLongOpt());
        }
        if ((option.hasArg()) && (((option.getArgName()) == null) || ((option.getArgName().length()) != 0))) {
            buff.append(((option.getOpt()) == null ? longOptSeparator : " "));
            buff.append("<").append(((option.getArgName()) != null ? option.getArgName() : getArgName())).append(">");
        }
        if (!required) {
            buff.append("]");
        }
    }

    public void printUsage(java.io.PrintWriter pw, int width, java.lang.String cmdLineSyntax) {
        int argPos = (cmdLineSyntax.indexOf(' ')) + 1;
        printWrapped(pw, width, ((defaultSyntaxPrefix.length()) + argPos), ((defaultSyntaxPrefix) + cmdLineSyntax));
    }

    public void printOptions(java.io.PrintWriter pw, int width, org.apache.commons.cli.Options options, int leftPad, int descPad) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        renderOptions(sb, width, options, leftPad, descPad);
        pw.println(sb.toString());
    }

    public void printWrapped(java.io.PrintWriter pw, int width, java.lang.String text) {
        printWrapped(pw, width, 0, text);
    }

    public void printWrapped(java.io.PrintWriter pw, int width, int nextLineTabStop, java.lang.String text) {
        java.lang.StringBuffer sb = new java.lang.StringBuffer(text.length());
        renderWrappedText(sb, width, nextLineTabStop, text);
        pw.println(sb.toString());
    }

    protected java.lang.StringBuffer renderOptions(java.lang.StringBuffer sb, int width, org.apache.commons.cli.Options options, int leftPad, int descPad) {
        final java.lang.String lpad = createPadding(leftPad);
        final java.lang.String dpad = createPadding(descPad);
        int max = 0;
        java.lang.StringBuffer optBuf;
        java.util.List prefixList = new java.util.ArrayList();
        java.util.List optList = options.helpOptions();
        java.util.Collections.sort(optList, getOptionComparator());
        for (java.util.Iterator i = optList.iterator(); i.hasNext();) {
            org.apache.commons.cli.Option option = ((org.apache.commons.cli.Option) (i.next()));
            optBuf = new java.lang.StringBuffer();
            if ((option.getOpt()) == null) {
                optBuf.append(lpad).append(("   " + (defaultLongOptPrefix))).append(option.getLongOpt());
            }else {
                optBuf.append(lpad).append(defaultOptPrefix).append(option.getOpt());
                if (option.hasLongOpt()) {
                    optBuf.append(',').append(defaultLongOptPrefix).append(option.getLongOpt());
                }
            }
            if (option.hasArg()) {
                java.lang.String argName = option.getArgName();
                if ((argName != null) && ((argName.length()) == 0)) {
                    optBuf.append(' ');
                }else {
                    optBuf.append((option.hasLongOpt() ? longOptSeparator : " "));
                    optBuf.append("<").append((argName != null ? option.getArgName() : getArgName())).append(">");
                }
            }
            prefixList.add(optBuf);
            max = ((optBuf.length()) > max) ? optBuf.length() : max;
        }
        int x = 0;
        for (java.util.Iterator i = optList.iterator(); i.hasNext();) {
            org.apache.commons.cli.Option option = ((org.apache.commons.cli.Option) (i.next()));
            optBuf = new java.lang.StringBuffer(prefixList.get((x++)).toString());
            if ((optBuf.length()) < max) {
                optBuf.append(createPadding((max - (optBuf.length()))));
            }
            optBuf.append(dpad);
            int nextLineTabStop = max + descPad;
            if ((option.getDescription()) != null) {
                optBuf.append(option.getDescription());
            }
            renderWrappedText(sb, width, nextLineTabStop, optBuf.toString());
            if (i.hasNext()) {
                sb.append(defaultNewLine);
            }
        }
        return sb;
    }

    protected java.lang.StringBuffer renderWrappedText(java.lang.StringBuffer sb, int width, int nextLineTabStop, java.lang.String text) {
        int pos = findWrapPos(text, width, 0);
        if (pos == (-1)) {
            sb.append(rtrim(text));
            return sb;
        }
        sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
        if (nextLineTabStop >= width) {
            nextLineTabStop = 1;
        }
        final java.lang.String padding = createPadding(nextLineTabStop);
        while (true) {
            text = padding + (text.substring(pos).trim());
            pos = findWrapPos(text, width, 0);
            if (pos == (-1)) {
                sb.append(text);
                return sb;
            }
            if (((text.length()) > width) && (pos == (nextLineTabStop - 1))) {
                pos = width;
            }
            sb.append(rtrim(text.substring(0, pos))).append(defaultNewLine);
        } 
    }

    protected int findWrapPos(java.lang.String text, int width, int startPos) {
        int pos;
        if ((((pos = text.indexOf('\n', startPos)) != (-1)) && (pos <= width)) || (((pos = text.indexOf('\t', startPos)) != (-1)) && (pos <= width))) {
            return pos + 1;
        }else
            if ((startPos + width) >= (text.length())) {
                return -1;
            }

        pos = startPos + width;
        char c;
        while ((((pos >= startPos) && ((c = text.charAt(pos)) != ' ')) && (c != '\n')) && (c != '\r')) {
            --pos;
        } 
        if (pos > startPos) {
            return pos;
        }
        pos = startPos + width;
        return pos == (text.length()) ? -1 : pos;
    }

    protected java.lang.String createPadding(int len) {
        char[] padding = new char[len];
        java.util.Arrays.fill(padding, ' ');
        return new java.lang.String(padding);
    }

    protected java.lang.String rtrim(java.lang.String s) {
        if ((s == null) || ((s.length()) == 0)) {
            return s;
        }
        int pos = s.length();
        while ((pos > 0) && (java.lang.Character.isWhitespace(s.charAt((pos - 1))))) {
            --pos;
        } 
        return s.substring(0, pos);
    }

    private static class OptionComparator implements java.util.Comparator {
        public int compare(java.lang.Object o1, java.lang.Object o2) {
            org.apache.commons.cli.Option opt1 = ((org.apache.commons.cli.Option) (o1));
            org.apache.commons.cli.Option opt2 = ((org.apache.commons.cli.Option) (o2));
            return opt1.getKey().compareToIgnoreCase(opt2.getKey());
        }
    }
}

