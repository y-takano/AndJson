package jp.gr.java_conf.ke.json.databind.internal;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;

class ConversionCache {

	private final Map<Integer, Conversion<?>> classMap = new LinkedHashMap<Integer, Conversion<?>>();
	private final Map<Integer, Conversion<?>> fieldMap = new LinkedHashMap<Integer, Conversion<?>>();

	public boolean contains(Class<?> clazz) {
		return classMap.containsKey(clazz.hashCode());
	}

	public boolean contains(Field field) {
		return fieldMap.containsKey(field.hashCode());
	}

	public Conversion<?> get(Class<?> clazz) {
		return classMap.get(clazz.hashCode());
	}

	public Conversion<?> get(Field field) {
		return classMap.get(field.hashCode());
	}

	public void put(Class<?> clazz, Conversion<?> conversion) {
		classMap.put(clazz.hashCode(), conversion);
	}

	public void put(Field field, Conversion<?> conversion) {
		classMap.put(field.hashCode(), conversion);
	}
}
