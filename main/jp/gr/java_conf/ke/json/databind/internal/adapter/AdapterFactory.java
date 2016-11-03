package jp.gr.java_conf.ke.json.databind.internal.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import jp.gr.java_conf.ke.json.databind.JsonBindException;
import jp.gr.java_conf.ke.json.databind.UnsupportedTypeException;
import jp.gr.java_conf.ke.json.databind.annotation.JsonArray;
import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;
import jp.gr.java_conf.ke.json.databind.annotation.JsonIgnore;
import jp.gr.java_conf.ke.json.databind.annotation.JsonList;
import jp.gr.java_conf.ke.json.databind.annotation.JsonMap;
import jp.gr.java_conf.ke.json.databind.annotation.JsonValue;
import jp.gr.java_conf.ke.json.databind.internal.conversion.ClassAnnotateConversion;
import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;
import jp.gr.java_conf.ke.json.databind.internal.conversion.DefaultAnnotations;
import jp.gr.java_conf.ke.json.databind.internal.conversion.FieldAnnotateConversion;

public class AdapterFactory {

	private static final ConversionCache READ_CACHE = new ConversionCache();
	private static final ConversionCache WRITE_CACHE = new ConversionCache();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> ObjectAdapter<E> createClassReadAdapter(Class<E> clazz) {

		Conversion<E> conversion;
		if (READ_CACHE.contains(clazz)) {
			conversion = (Conversion<E>) READ_CACHE.get(clazz);
		} else {
			conversion = createReadConversion(clazz);
		}

		if (conversion == null) {
			return (ObjectAdapter<E>) new IgnoreAdapter();
		}

		ObjectAdapter<E> ret;
		try {
			Class<? extends E> concrete = conversion.getConcreteClass(clazz);

			// 配列型
			if (concrete.isArray()) {
				E instance = (E) Array.newInstance(concrete.getComponentType(),
						JsonArray.DEFAULT_LENGTH);
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new ArrayAdapter<E>(ctxt);

				// Map系のクラス
			} else if (Map.class.isAssignableFrom(concrete)) {
				E instance;
				if (concrete.isInterface()) {
					instance = (E) JsonMap.DEFAULT_CONCRETE.newInstance();
				} else {
					instance = (E) concrete.newInstance();
				}
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new MapAdapter(ctxt);

				// List系のクラス
			} else if (Collection.class.isAssignableFrom(concrete)) {
				E instance;
				if (concrete.isInterface()) {
					instance = (E) JsonList.DEFAULT_CONCRETE.newInstance();
				} else {
					instance = concrete.newInstance();
				}
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new ListAdapter(ctxt);

				// その他はBeanとして扱う
			} else {
				Constructor<E> constructor = (Constructor<E>) concrete.getDeclaredConstructor();
				constructor.setAccessible(true);
				E instance = constructor.newInstance();
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new BeanAdapter<E>(ctxt);
			}
		} catch (Exception e) {
			throw new UnsupportedTypeException(e);
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> ObjectAdapter<E> createClassWriteAdapter(E instance) {

		Class<E> concrete = (Class<E>) instance.getClass();

		Conversion<E> conversion;
		if (WRITE_CACHE.contains(concrete)) {
			conversion = (Conversion<E>) WRITE_CACHE.get(concrete);
		} else {
			conversion = createWriteConversion(concrete);
		}

		if (conversion == null) {
			return (ObjectAdapter<E>) new IgnoreAdapter();
		}

		ObjectAdapter<E> ret;
		try {
			// 配列型
			if (concrete.isArray()) {
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new ArrayAdapter<E>(ctxt);

				// Map系のクラス
			} else if (Map.class.isAssignableFrom(concrete)) {
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new MapAdapter(ctxt);

				// List系のクラス
			} else if (Collection.class.isAssignableFrom(concrete)) {
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new ListAdapter(ctxt);

				// その他はBeanとして扱う
			} else {
				AdapterContext<E> ctxt = new AdapterContext<E>(instance,
						conversion);
				ret = new BeanAdapter<E>(ctxt);
			}
		} catch (Exception e) {
			throw new UnsupportedTypeException(e);
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ObjectAdapter<?> createFieldReadAdapter(Field field,
			Conversion<?> parent) {

		Class<?> type = field.getType();
		boolean isValue = isValueType(type);
		Conversion<?> conversion;
		if (READ_CACHE.contains(field)) {
			conversion = READ_CACHE.get(field);
		} else {
			conversion = createReadConversion(field, type, isValue, parent);
		}

		Class<?> concrete = conversion.getConcreteClass(type);
		ObjectAdapter<?> adapter = null;

		try {
			// Value
			 if (isValue) {
				adapter = new ValueAdapter(concrete, conversion);

			// 配列型
			} else {

				Object instance;
				if (concrete.isArray()) {
					instance = Array.newInstance(concrete.getComponentType(),
						conversion.getArrayInitLength());
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new ArrayAdapter(ctxt);

				// Map系のクラス
				} else if (Map.class.isAssignableFrom(concrete)) {
					if (concrete.isInterface()) {
						instance = JsonMap.DEFAULT_CONCRETE.newInstance();
					} else {
						instance = concrete.newInstance();
					}
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new MapAdapter(ctxt);

					// List系のクラス
				} else if (Collection.class.isAssignableFrom(concrete)) {
					if (concrete.isInterface()) {
						instance = JsonList.DEFAULT_CONCRETE.newInstance();
					} else {
						instance = concrete.newInstance();
					}
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new ListAdapter(ctxt);

					// その他はBeanとして扱う
				} else {

					Constructor<?> constructor = concrete.getDeclaredConstructor();
					constructor.setAccessible(true);
					instance = constructor.newInstance();
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new BeanAdapter(ctxt);
				}
			}
		} catch (Exception e) {
			throw new UnsupportedTypeException(e);
		}
		return adapter;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ObjectAdapter<?> createFieldWriteAdapter(Field field,
			Conversion<?> parent, Object instance) {

		Class<?> type;
		if (instance == null) {
			type = field.getType();
		} else {
			type = instance.getClass();
		}

		Conversion<?> conversion;
		boolean isValue = isValueType(type);
		if (WRITE_CACHE.contains(field)) {
			conversion = WRITE_CACHE.get(field);
		} else {
			conversion = createWriteConversion(field, type, isValue, parent);
		}

		if (!conversion.isWritable()) return null;

		Class<?> concrete;
		if (instance == null) {
			concrete = conversion.getConcreteClass(type);
		} else {
			concrete = type;
		}
		ObjectAdapter<?> adapter = null;
		try {
			// Value
			if (isValue) {
				adapter = new ValueAdapter(instance, conversion);

				// 配列型
			} else {
				if (concrete.isArray()) {
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new ArrayAdapter(ctxt);

				// Map系のクラス
				} else if (Map.class.isAssignableFrom(concrete)) {
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new MapAdapter(ctxt);

					// List系のクラス
				} else if (Collection.class.isAssignableFrom(concrete)) {
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new ListAdapter(ctxt);

					// その他はBeanとして扱う
				} else {
					AdapterContext ctxt = new AdapterContext(instance, conversion);
					adapter = new BeanAdapter(ctxt);
				}
			}

		} catch (Exception e) {
			throw new UnsupportedTypeException(e);
		}
		return adapter;
	}

	@SuppressWarnings("unchecked")
	private static <E> Conversion<E> createReadConversion(Class<E> clazz) {

		Collection<Annotation> check = new HashSet<Annotation>();
		Conversion<E> conversion = selectClassTypeConversion(clazz, clazz.getDeclaredAnnotations(), check);

		int size = check.size();

		// 多重定義チェック
		if (1 < size) {
			throw new JsonBindException("多重定義無効: class=" + clazz + ", "
					+ check);

			// アノテートなし時はbean指定扱い
		} else if (size == 0) {
			conversion = new ClassAnnotateConversion<E>(DefaultAnnotations.ROOT_CLASS);
		}
		conversion = conversion == null ? (Conversion<E>) Conversion.EMPTY : conversion;
		READ_CACHE.put(clazz, conversion);
		return conversion;
	}

	@SuppressWarnings("unchecked")
	private static <E> Conversion<E> createWriteConversion(Class<E> clazz) {

		Collection<Annotation> check = new HashSet<Annotation>();
		Conversion<E> conversion = selectClassTypeConversion(clazz, clazz.getDeclaredAnnotations(), check);

		int size = check.size();

		// 多重定義チェック
		if (1 < size) {
			throw new JsonBindException("多重定義無効: class=" + clazz + ", "
					+ check);

			// アノテートなし時はbean指定扱い
		} else if (size == 0) {
			conversion = new ClassAnnotateConversion<E>(DefaultAnnotations.ROOT_CLASS);
		}
		conversion = conversion == null ? (Conversion<E>) Conversion.EMPTY : conversion;
		WRITE_CACHE.put(clazz, conversion);
		return conversion;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Conversion<?> createReadConversion(Field field,
			Class<?> type, boolean isValue, Conversion<?> parent) {

		Collection<Annotation> check = new HashSet<Annotation>();
		Conversion<?> conversion = selectFieldTypeConversion(type, parent, field.getDeclaredAnnotations(), check);

		int size = check.size();

		// 多重定義チェック
		if (1 < size) {
			throw new JsonBindException("多重定義無効: field=" + field + ", " + check);

		// アノテーションなし時
		} else if (size == 0) {

			if (isValue && parent.isReadable()) {

				// JsonValueとして読み込み
				conversion = new FieldAnnotateConversion(parent, DefaultAnnotations.FIELD_VALUE);
			}
		}
		conversion = conversion == null ? Conversion.EMPTY : conversion;
		READ_CACHE.put(field, conversion);
		return conversion;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Conversion<?> createWriteConversion(Field field,
			Class<?> type, boolean isValue, Conversion<?> parent) {

		Collection<Annotation> check = new HashSet<Annotation>();
		Conversion<?> conversion = selectFieldTypeConversion(type, parent, field.getDeclaredAnnotations(), check);

		int size = check.size();

		// 多重定義チェック
		if (1 < size) {
			throw new JsonBindException("多重定義無効: field=" + field + ", " + check);

		// アノテーションなし時
		} else if (size == 0) {

			if (isValue && parent.isWritable()) {

				// JsonValueとして読み込み
				conversion = new FieldAnnotateConversion(parent, DefaultAnnotations.FIELD_VALUE);
			}
		}
		conversion = conversion == null ? Conversion.EMPTY : conversion;
		WRITE_CACHE.put(field, conversion);
		return conversion;
	}

	private static <E> Conversion<E> selectClassTypeConversion(Class<E> type, Annotation[] annos, Collection<Annotation> check) {
		Conversion<E> conversion = null;

		for (Annotation anno : annos) {
			if (anno instanceof JsonIgnore) {
				conversion = new ClassAnnotateConversion<E>((JsonIgnore) anno);
				check.add(anno);

			} else if (anno instanceof JsonBean) {

				// 無効なクラス（配列型、Map系、List系）
				if (type.isArray() || Map.class.isAssignableFrom(type)
						|| Collection.class.isAssignableFrom(type))
					throw new JsonBindException("@JsonBeanと互換性がない型:" + type);

				conversion = new ClassAnnotateConversion<E>((JsonBean) anno);
				check.add(anno);
			}
		}

		return conversion;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Conversion<?> selectFieldTypeConversion(Class<?> type, Conversion<?> parent, Annotation[] annos, Collection<Annotation> check) {
		Conversion<?> conversion = null;

		for (Annotation anno : annos) {
			if (anno instanceof JsonIgnore) {
				conversion = new FieldAnnotateConversion(parent, (JsonIgnore) anno);
				check.add(anno);

			} else if (anno instanceof JsonBean) {

				// 無効なクラス（配列型、Map系、List系）
				if (type.isArray() || Map.class.isAssignableFrom(type)
						|| Collection.class.isAssignableFrom(type))
					throw new JsonBindException("@JsonBeanと互換性がない型:" + type);

				conversion = new FieldAnnotateConversion(parent, (JsonBean) anno);
				check.add(anno);

			} else if (anno instanceof JsonMap) {

				// 無効なクラス（Map系以外）
				if (!Map.class.isAssignableFrom(type))
					throw new JsonBindException("@JsonMapと互換性がない型:" + type);

				conversion = new FieldAnnotateConversion(parent, (JsonMap) anno);
				check.add(anno);

			} else if (anno instanceof JsonList) {

				// 無効なクラス（List系以外）
				if (!Collection.class.isAssignableFrom(type))
					throw new JsonBindException("@JsonListと互換性がない型:" + type);

				conversion = new FieldAnnotateConversion(parent, (JsonList) anno);
				check.add(anno);

			} else if (anno instanceof JsonArray) {

				// 無効なクラス（配列型以外）
				if (!type.isArray())
					throw new JsonBindException("@JsonArrayと互換性がない型:" + type);

				conversion = new FieldAnnotateConversion(parent, (JsonArray) anno);
				check.add(anno);

			} else if (anno instanceof JsonValue) {

				// 無効なクラス（配列型、Map系、List系）
				if (type.isArray() || Map.class.isAssignableFrom(type)
						|| Collection.class.isAssignableFrom(type))
					throw new JsonBindException("@JsonValueと互換性がない型:" + type);

				conversion = new FieldAnnotateConversion(parent, (JsonValue) anno);
				check.add(anno);
			}
		}
		return conversion;
	}

	private static boolean isValueType(Class<?> type) {
		return type.equals(String.class) || type.getName().equals("boolean") || type.equals(Boolean.class)
				|| type.getName().equals("int") || type.equals(Integer.class) || type.getName().equals("long") || type.equals(Long.class)
				|| type.getName().equals("double") || type.equals(Double.class) || type.getName().equals("float") || type.equals(Float.class)
				|| type.equals(BigDecimal.class) || type.equals(BigInteger.class) || type.isEnum();
	}
}
