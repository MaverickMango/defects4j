package org.mockito.internal.matchers;


public class Equality {
    public static boolean areEqual(java.lang.Object o1, java.lang.Object o2) {
        if (o1 == o2) {
            return true;
        }else
            if ((o1 == null) || (o2 == null)) {
                return (o1 == null) && (o2 == null);
            }else
                if (org.mockito.internal.matchers.Equality.isArray(o1)) {
                    return (org.mockito.internal.matchers.Equality.isArray(o2)) && (org.mockito.internal.matchers.Equality.areArraysEqual(o1, o2));
                }else {
                    return o1.equals(o2);
                }


    }

    static boolean areArraysEqual(java.lang.Object o1, java.lang.Object o2) {
        return (org.mockito.internal.matchers.Equality.areArrayLengthsEqual(o1, o2)) && (org.mockito.internal.matchers.Equality.areArrayElementsEqual(o1, o2));
    }

    static boolean areArrayLengthsEqual(java.lang.Object o1, java.lang.Object o2) {
        return (java.lang.reflect.Array.getLength(o1)) == (java.lang.reflect.Array.getLength(o2));
    }

    static boolean areArrayElementsEqual(java.lang.Object o1, java.lang.Object o2) {
        for (int i = 0; i < (java.lang.reflect.Array.getLength(o1)); i++) {
            if (!(org.mockito.internal.matchers.Equality.areEqual(java.lang.reflect.Array.get(o1, i), java.lang.reflect.Array.get(o2, i))))
                return false;

        }
        return true;
    }

    static boolean isArray(java.lang.Object o) {
        return o.getClass().isArray();
    }
}

