package cg.zz.wf.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@SuppressWarnings({"rawtypes","unchecked"})
public final class CollectionUtil {

	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null) || (collection.isEmpty());
	}

	public static boolean isEmpty(Map map) {
		return (map == null) || (map.isEmpty());
	}

	public static List arrayToList(Object source) {
		return Arrays.asList(ObjectUtils.toObjectArray(source));
	}

	public static void mergeArrayIntoCollection(Object array, Collection collection) {
		if (collection == null) {
			throw new IllegalArgumentException("Collection must not be null");
		}
		Object[] arr = ObjectUtils.toObjectArray(array);
		Object[] arrayOfObject1;
		int j = (arrayOfObject1 = arr).length;
		for (int i = 0; i < j; i++) {
			Object elem = arrayOfObject1[i];
			collection.add(elem);
		}
	}

	public static void mergePropertiesIntoMap(Properties props, Map map) {
		if (map == null) {
			throw new IllegalArgumentException("Map must not be null");
		}
		if (props != null) {
			for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
				String key = (String) en.nextElement();
				Object value = props.getProperty(key);
				if (value == null) {
					value = props.get(key);
				}
				map.put(key, value);
			}
		}
	}

	public static boolean contains(Iterator iterator, Object element) {
		if (iterator != null) {
			while (iterator.hasNext()) {
				Object candidate = iterator.next();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean contains(Enumeration enumeration, Object element) {
		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				Object candidate = enumeration.nextElement();
				if (ObjectUtils.nullSafeEquals(candidate, element)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsInstance(Collection collection, Object element) {
		if (collection != null) {
			for (Object candidate : collection) {
				if (candidate == element) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsAny(Collection source, Collection candidates) {
		if ((isEmpty(source)) || (isEmpty(candidates))) {
			return false;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return true;
			}
		}
		return false;
	}

	public static Object findFirstMatch(Collection source, Collection candidates) {
		if ((isEmpty(source)) || (isEmpty(candidates))) {
			return null;
		}
		for (Object candidate : candidates) {
			if (source.contains(candidate)) {
				return candidate;
			}
		}
		return null;
	}

	public static <T> T findValueOfType(Collection<?> collection, Class<T> type) {
		if (isEmpty(collection)) {
			return null;
		}
		T value = null;
		for (Object element : collection) {
			if ((type == null) || (type.isInstance(element))) {
				if (value != null) {
					return null;
				}
				value = (T) element;
			}
		}
		return value;
	}

	public static Object findValueOfType(Collection<?> collection, Class<?>[] types) {
		if ((isEmpty(collection)) || (ObjectUtils.isEmpty(types))) {
			return null;
		}
		Class[] arrayOfClass;
		int j = (arrayOfClass = types).length;
		for (int i = 0; i < j; i++) {
			Class<?> type = arrayOfClass[i];
			Object value = findValueOfType(collection, type);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	public static boolean hasUniqueObject(Collection collection) {
		if (isEmpty(collection)) {
			return false;
		}
		boolean hasCandidate = false;
		Object candidate = null;
		for (Object elem : collection) {
			if (!hasCandidate) {
				hasCandidate = true;
				candidate = elem;
			} else if (candidate != elem) {
				return false;
			}
		}
		return true;
	}

	public static Class<?> findCommonElementType(Collection collection) {
		if (isEmpty(collection)) {
			return null;
		}
		Class<?> candidate = null;
		for (Object val : collection) {
			if (val != null) {
				if (candidate == null) {
					candidate = val.getClass();
				} else if (candidate != val.getClass()) {
					return null;
				}
			}
		}
		return candidate;
	}

	public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
		return new EnumerationIterator(enumeration);
	}

	private static class EnumerationIterator<E> implements Iterator<E> {
		private Enumeration<E> enumeration;

		public EnumerationIterator(Enumeration<E> enumeration) {
			this.enumeration = enumeration;
		}

		public boolean hasNext() {
			return this.enumeration.hasMoreElements();
		}

		public E next() {
			return (E) this.enumeration.nextElement();
		}

		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Not supported");
		}
	}

	private CollectionUtil() {

	}

}
