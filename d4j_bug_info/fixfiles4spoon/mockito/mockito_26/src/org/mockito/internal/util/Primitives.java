package org.mockito.internal.util;


@java.lang.SuppressWarnings("unchecked")
public class Primitives {
    public static <T> java.lang.Class<T> primitiveTypeOf(java.lang.Class<T> clazz) {
        if (clazz.isPrimitive()) {
            return clazz;
        }
        return ((java.lang.Class<T>) (org.mockito.internal.util.Primitives.primitiveTypes.get(clazz)));
    }

    public static boolean isPrimitiveWrapper(java.lang.Class<?> type) {
        return org.mockito.internal.util.Primitives.wrapperReturnValues.containsKey(type);
    }

    public static <T> T primitiveWrapperOf(java.lang.Class<T> type) {
        return ((T) (org.mockito.internal.util.Primitives.wrapperReturnValues.get(type)));
    }

    public static <T> T primitiveValueOrNullFor(java.lang.Class<T> primitiveType) {
        return ((T) (org.mockito.internal.util.Primitives.primitiveValues.get(primitiveType)));
    }

    private static java.util.Map<java.lang.Class<?>, java.lang.Class<?>> wrapperTypes = new java.util.HashMap<java.lang.Class<?>, java.lang.Class<?>>();

    private static java.util.Map<java.lang.Class<?>, java.lang.Class<?>> primitiveTypes = new java.util.HashMap<java.lang.Class<?>, java.lang.Class<?>>();

    private static java.util.Map<java.lang.Class<?>, java.lang.Object> wrapperReturnValues = new java.util.HashMap<java.lang.Class<?>, java.lang.Object>();

    private static java.util.Map<java.lang.Class<?>, java.lang.Object> primitiveValues = new java.util.HashMap<java.lang.Class<?>, java.lang.Object>();

    static {
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Boolean.class, java.lang.Boolean.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Character.class, java.lang.Character.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Byte.class, java.lang.Byte.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Short.class, java.lang.Short.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Integer.class, java.lang.Integer.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Long.class, java.lang.Long.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Float.class, java.lang.Float.TYPE);
        org.mockito.internal.util.Primitives.primitiveTypes.put(java.lang.Double.class, java.lang.Double.TYPE);
    }

    static {
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Boolean.class, false);
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Character.class, '\u0000');
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Byte.class, ((byte) (0)));
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Short.class, ((short) (0)));
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Integer.class, 0);
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Long.class, 0L);
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Float.class, 0.0F);
        org.mockito.internal.util.Primitives.wrapperReturnValues.put(java.lang.Double.class, 0.0);
    }

    static {
        org.mockito.internal.util.Primitives.primitiveValues.put(boolean.class, false);
        org.mockito.internal.util.Primitives.primitiveValues.put(char.class, '\u0000');
        org.mockito.internal.util.Primitives.primitiveValues.put(byte.class, ((byte) (0)));
        org.mockito.internal.util.Primitives.primitiveValues.put(short.class, ((short) (0)));
        org.mockito.internal.util.Primitives.primitiveValues.put(int.class, 0);
        org.mockito.internal.util.Primitives.primitiveValues.put(long.class, 0L);
        org.mockito.internal.util.Primitives.primitiveValues.put(float.class, 0.0F);
        org.mockito.internal.util.Primitives.primitiveValues.put(double.class, 0.0);
    }
}

