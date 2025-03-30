package org.mockito.internal.verification.argumentmatching;


@java.lang.SuppressWarnings("unchecked")
public class ArgumentMatchingTool {
    public java.lang.Integer[] getSuspiciouslyNotMatchingArgsIndexes(java.util.List<org.hamcrest.Matcher> matchers, java.lang.Object[] arguments) {
        if ((matchers.size()) != (arguments.length)) {
            return new java.lang.Integer[0];
        }
        java.util.List<java.lang.Integer> suspicious = new java.util.LinkedList<java.lang.Integer>();
        int i = 0;
        for (org.hamcrest.Matcher m : matchers) {
            if ((((m instanceof org.mockito.internal.matchers.ContainsExtraTypeInformation) && (!(safelyMatches(m, arguments[i])))) && (toStringEquals(m, arguments[i]))) && (!(((org.mockito.internal.matchers.ContainsExtraTypeInformation) (m)).typeMatches(arguments[i])))) {
                suspicious.add(i);
            }
            i++;
        }
        return suspicious.toArray(new java.lang.Integer[0]);
    }

    private boolean safelyMatches(org.hamcrest.Matcher m, java.lang.Object arg) {
        try {
            return m.matches(arg);
        } catch (java.lang.Throwable t) {
            return false;
        }
    }

    private boolean toStringEquals(org.hamcrest.Matcher m, java.lang.Object arg) {
        return org.hamcrest.StringDescription.toString(m).equals((arg == null ? "null" : arg.toString()));
    }
}

