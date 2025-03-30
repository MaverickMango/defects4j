package org.apache.commons.cli;


public class PosixParser extends org.apache.commons.cli.Parser {
    private java.util.List tokens = new java.util.ArrayList();

    private boolean eatTheRest;

    private org.apache.commons.cli.Option currentOption;

    private org.apache.commons.cli.Options options;

    private void init() {
        eatTheRest = false;
        tokens.clear();
        currentOption = null;
    }

    protected java.lang.String[] flatten(org.apache.commons.cli.Options options, java.lang.String[] arguments, boolean stopAtNonOption) {
        init();
        this.options = options;
        java.util.Iterator iter = java.util.Arrays.asList(arguments).iterator();
        while (iter.hasNext()) {
            java.lang.String token = ((java.lang.String) (iter.next()));
            if (token.startsWith("--")) {
                if ((token.indexOf('=')) != (-1)) {
                    tokens.add(token.substring(0, token.indexOf('=')));
                    tokens.add(token.substring(((token.indexOf('=')) + 1), token.length()));
                }else {
                    tokens.add(token);
                }
            }else
                if ("-".equals(token)) {
                    tokens.add(token);
                }else
                    if (token.startsWith("-")) {
                        if ((token.length()) == 2) {
                            processOptionToken(token, stopAtNonOption);
                        }else
                            if (options.hasOption(token)) {
                                tokens.add(token);
                            }else {
                                burstToken(token, stopAtNonOption);
                            }

                    }else
                        if (stopAtNonOption) {
                            process(token);
                        }else {
                            tokens.add(token);
                        }



            gobble(iter);
        } 
        return ((java.lang.String[]) (tokens.toArray(new java.lang.String[tokens.size()])));
    }

    private void gobble(java.util.Iterator iter) {
        if (eatTheRest) {
            while (iter.hasNext()) {
                tokens.add(iter.next());
            } 
        }
    }

    private void process(java.lang.String value) {
        if (((currentOption) != null) && (currentOption.hasArg())) {
            if (currentOption.hasArg()) {
                tokens.add(value);
                currentOption = null;
            }else
                if (currentOption.hasArgs()) {
                    tokens.add(value);
                }

        }else {
            eatTheRest = true;
            tokens.add("--");
            tokens.add(value);
        }
    }

    private void processOptionToken(java.lang.String token, boolean stopAtNonOption) {
        if (options.hasOption(token)) {
            currentOption = options.getOption(token);
            tokens.add(token);
        }else
            if (stopAtNonOption) {
                eatTheRest = true;
                tokens.add(token);
            }

    }

    protected void burstToken(java.lang.String token, boolean stopAtNonOption) {
        for (int i = 1; i < (token.length()); i++) {
            java.lang.String ch = java.lang.String.valueOf(token.charAt(i));
            if (options.hasOption(ch)) {
                tokens.add(("-" + ch));
                currentOption = options.getOption(ch);
                if ((currentOption.hasArg()) && ((token.length()) != (i + 1))) {
                    tokens.add(token.substring((i + 1)));
                    break;
                }
            }else
                if (stopAtNonOption) {
                    process(token.substring(i));
                    break;
                }else {
                    tokens.add(token);
                    break;
                }

        }
    }
}

