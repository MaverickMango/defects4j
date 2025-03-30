package org.mockito.internal.matchers;


public class Same extends org.mockito.ArgumentMatcher<java.lang.Object> implements java.io.Serializable {
    private static final long serialVersionUID = -1226959355938572597L;

    private final java.lang.Object wanted;

    public Same(java.lang.Object wanted) {
        this.wanted = wanted;
    }

    public boolean matches(java.lang.Object actual) {
        return (wanted) == actual;
    }

    public void describeTo(org.hamcrest.Description description) {
        description.appendText("same(");
        appendQuoting(description);
        description.appendText(((wanted) == null ? "null" : wanted.toString()));
        appendQuoting(description);
        description.appendText(")");
    }

    private void appendQuoting(org.hamcrest.Description description) {
        if ((wanted) instanceof java.lang.String) {
            description.appendText("\"");
        }else
            if ((wanted) instanceof java.lang.Character) {
                description.appendText("'");
            }

    }
}

