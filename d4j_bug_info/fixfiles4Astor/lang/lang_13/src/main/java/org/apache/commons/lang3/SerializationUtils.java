package org.apache.commons.lang3;
public class SerializationUtils {
	public SerializationUtils() {
		super();
	}

	public static <T extends java.io.Serializable> T clone(T object) {
		if (object == null) {
			return null;
		}
		byte[] objectData = org.apache.commons.lang3.SerializationUtils.serialize(object);
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(objectData);
		org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream in = null;
		try {
			in = new org.apache.commons.lang3.SerializationUtils.ClassLoaderAwareObjectInputStream(bais, object.getClass().getClassLoader());
			@java.lang.SuppressWarnings("unchecked")
			T readObject = ((T) (in.readObject()));
			return readObject;
		} catch (java.lang.ClassNotFoundException ex) {
			throw new org.apache.commons.lang3.SerializationException("ClassNotFoundException while reading cloned object data", ex);
		} catch (java.io.IOException ex) {
			throw new org.apache.commons.lang3.SerializationException("IOException while reading cloned object data", ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (java.io.IOException ex) {
				throw new org.apache.commons.lang3.SerializationException("IOException on closing cloned object data InputStream.", ex);
			}
		}
	}

	public static void serialize(java.io.Serializable obj, java.io.OutputStream outputStream) {
		if (outputStream == null) {
			throw new java.lang.IllegalArgumentException("The OutputStream must not be null");
		}
		java.io.ObjectOutputStream out = null;
		try {
			out = new java.io.ObjectOutputStream(outputStream);
			out.writeObject(obj);
		} catch (java.io.IOException ex) {
			throw new org.apache.commons.lang3.SerializationException(ex);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (java.io.IOException ex) {
			}
		}
	}

	public static byte[] serialize(java.io.Serializable obj) {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(512);
		org.apache.commons.lang3.SerializationUtils.serialize(obj, baos);
		return baos.toByteArray();
	}

	public static java.lang.Object deserialize(java.io.InputStream inputStream) {
		if (inputStream == null) {
			throw new java.lang.IllegalArgumentException("The InputStream must not be null");
		}
		java.io.ObjectInputStream in = null;
		try {
			in = new java.io.ObjectInputStream(inputStream);
			return in.readObject();
		} catch (java.lang.ClassNotFoundException ex) {
			throw new org.apache.commons.lang3.SerializationException(ex);
		} catch (java.io.IOException ex) {
			throw new org.apache.commons.lang3.SerializationException(ex);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (java.io.IOException ex) {
			}
		}
	}

	public static java.lang.Object deserialize(byte[] objectData) {
		if (objectData == null) {
			throw new java.lang.IllegalArgumentException("The byte[] must not be null");
		}
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(objectData);
		return org.apache.commons.lang3.SerializationUtils.deserialize(bais);
	}

	static class ClassLoaderAwareObjectInputStream extends java.io.ObjectInputStream {
		private java.lang.ClassLoader classLoader;

		public ClassLoaderAwareObjectInputStream(java.io.InputStream in, java.lang.ClassLoader classLoader) throws java.io.IOException {
			super(in);
			this.classLoader = classLoader;
		}

		@java.lang.Override
		protected java.lang.Class<?> resolveClass(java.io.ObjectStreamClass desc) throws java.io.IOException, java.lang.ClassNotFoundException {
			java.lang.String name = desc.getName();
			try {
				return java.lang.Class.forName(name, false, classLoader);
			} catch (java.lang.ClassNotFoundException ex) {
				return java.lang.Class.forName(name, false, java.lang.Thread.currentThread().getContextClassLoader());
			}
		}
	}
}