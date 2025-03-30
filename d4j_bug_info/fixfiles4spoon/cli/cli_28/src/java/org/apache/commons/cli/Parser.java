package org.apache.commons.cli;


public abstract class Parser implements org.apache.commons.cli.CommandLineParser {
    protected org.apache.commons.cli.CommandLine cmd;

    private org.apache.commons.cli.Options options;

    private java.util.List requiredOptions;

    protected void setOptions(final org.apache.commons.cli.Options options) {
        this.options = options;
        this.requiredOptions = new java.util.ArrayList(options.getRequiredOptions());
    }

    protected org.apache.commons.cli.Options getOptions() {
        return options;
    }

    protected java.util.List getRequiredOptions() {
        return requiredOptions;
    }

    protected abstract java.lang.String[] flatten(org.apache.commons.cli.Options opts, java.lang.String[] arguments, boolean stopAtNonOption) throws org.apache.commons.cli.ParseException;

    public org.apache.commons.cli.CommandLine parse(org.apache.commons.cli.Options options, java.lang.String[] arguments) throws org.apache.commons.cli.ParseException {
        return parse(options, arguments, null, false);
    }

    public org.apache.commons.cli.CommandLine parse(org.apache.commons.cli.Options options, java.lang.String[] arguments, java.util.Properties properties) throws org.apache.commons.cli.ParseException {
        return parse(options, arguments, properties, false);
    }

    public org.apache.commons.cli.CommandLine parse(org.apache.commons.cli.Options options, java.lang.String[] arguments, boolean stopAtNonOption) throws org.apache.commons.cli.ParseException {
        return parse(options, arguments, null, stopAtNonOption);
    }

    public org.apache.commons.cli.CommandLine parse(org.apache.commons.cli.Options options, java.lang.String[] arguments, java.util.Properties properties, boolean stopAtNonOption) throws org.apache.commons.cli.ParseException {
        for (java.util.Iterator it = options.helpOptions().iterator(); it.hasNext();) {
            org.apache.commons.cli.Option opt = ((org.apache.commons.cli.Option) (it.next()));
            opt.clearValues();
        }
        for (java.util.Iterator it = options.getOptionGroups().iterator(); it.hasNext();) {
            org.apache.commons.cli.OptionGroup group = ((org.apache.commons.cli.OptionGroup) (it.next()));
            group.setSelected(null);
        }
        setOptions(options);
        cmd = new org.apache.commons.cli.CommandLine();
        boolean eatTheRest = false;
        if (arguments == null) {
            arguments = new java.lang.String[0];
        }
        java.util.List tokenList = java.util.Arrays.asList(flatten(getOptions(), arguments, stopAtNonOption));
        java.util.ListIterator iterator = tokenList.listIterator();
        while (iterator.hasNext()) {
            java.lang.String t = ((java.lang.String) (iterator.next()));
            if ("--".equals(t)) {
                eatTheRest = true;
            }else
                if ("-".equals(t)) {
                    if (stopAtNonOption) {
                        eatTheRest = true;
                    }else {
                        cmd.addArg(t);
                    }
                }else
                    if (t.startsWith("-")) {
                        if (stopAtNonOption && (!(getOptions().hasOption(t)))) {
                            eatTheRest = true;
                            cmd.addArg(t);
                        }else {
                            processOption(t, iterator);
                        }
                    }else {
                        cmd.addArg(t);
                        if (stopAtNonOption) {
                            eatTheRest = true;
                        }
                    }


            if (eatTheRest) {
                while (iterator.hasNext()) {
                    java.lang.String str = ((java.lang.String) (iterator.next()));
                    if (!("--".equals(str))) {
                        cmd.addArg(str);
                    }
                } 
            }
        } 
        processProperties(properties);
        checkRequiredOptions();
        return cmd;
    }

    protected void processProperties(java.util.Properties properties) {
        if (properties == null) {
            return;
        }
        for (java.util.Enumeration e = properties.propertyNames(); e.hasMoreElements();) {
            java.lang.String option = e.nextElement().toString();
            if (!(cmd.hasOption(option))) {
                org.apache.commons.cli.Option opt = getOptions().getOption(option);
                java.lang.String value = properties.getProperty(option);
                if (opt.hasArg()) {
                    if (((opt.getValues()) == null) || ((opt.getValues().length) == 0)) {
                        try {
                            opt.addValueForProcessing(value);
                        } catch (java.lang.RuntimeException exp) {
                        }
                    }
                }else
                    if (!((("yes".equalsIgnoreCase(value)) || ("true".equalsIgnoreCase(value))) || ("1".equalsIgnoreCase(value)))) {
                        continue;
                    }

                cmd.addOption(opt);
            }
        }
    }

    protected void checkRequiredOptions() throws org.apache.commons.cli.MissingOptionException {
        if (!(getRequiredOptions().isEmpty())) {
            throw new org.apache.commons.cli.MissingOptionException(getRequiredOptions());
        }
    }

    public void processArgs(org.apache.commons.cli.Option opt, java.util.ListIterator iter) throws org.apache.commons.cli.ParseException {
        while (iter.hasNext()) {
            java.lang.String str = ((java.lang.String) (iter.next()));
            if ((getOptions().hasOption(str)) && (str.startsWith("-"))) {
                iter.previous();
                break;
            }
            try {
                opt.addValueForProcessing(org.apache.commons.cli.Util.stripLeadingAndTrailingQuotes(str));
            } catch (java.lang.RuntimeException exp) {
                iter.previous();
                break;
            }
        } 
        if (((opt.getValues()) == null) && (!(opt.hasOptionalArg()))) {
            throw new org.apache.commons.cli.MissingArgumentException(opt);
        }
    }

    protected void processOption(java.lang.String arg, java.util.ListIterator iter) throws org.apache.commons.cli.ParseException {
        boolean hasOption = getOptions().hasOption(arg);
        if (!hasOption) {
            throw new org.apache.commons.cli.UnrecognizedOptionException(("Unrecognized option: " + arg), arg);
        }
        org.apache.commons.cli.Option opt = ((org.apache.commons.cli.Option) (getOptions().getOption(arg).clone()));
        if (opt.isRequired()) {
            getRequiredOptions().remove(opt.getKey());
        }
        if ((getOptions().getOptionGroup(opt)) != null) {
            org.apache.commons.cli.OptionGroup group = getOptions().getOptionGroup(opt);
            if (group.isRequired()) {
                getRequiredOptions().remove(group);
            }
            group.setSelected(opt);
        }
        if (opt.hasArg()) {
            processArgs(opt, iter);
        }
        cmd.addOption(opt);
    }
}

