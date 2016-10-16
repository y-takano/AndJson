package jp.gr.java_conf.ke.json.databind.converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import jp.gr.java_conf.ke.json.JsonConvertException;
import jp.gr.java_conf.ke.json.databind.JsonBindException;

public class Converters {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <E> JsonConverter<E> getConverter(Class<E> type) {
		if (type == null) throw new JsonBindException("null");
		else if (type.equals(String.class)) return (JsonConverter<E>) StrConv;
		else if (type.getName().equals("boolean") || type.equals(Boolean.class)) return (JsonConverter<E>) boolConv;
		else if (type.getName().equals("int") || type.equals(Integer.class))  return (JsonConverter<E>) IntConv;
		else if (type.getName().equals("long") || type.equals(Long.class)) return (JsonConverter<E>) longConv;
		else if (type.getName().equals("double") || type.equals(Double.class)) return (JsonConverter<E>) doubleConv;
		else if (type.getName().equals("float") || type.equals(Float.class)) return (JsonConverter<E>) floatConv;
		else if (type.equals(BigDecimal.class)) return (JsonConverter<E>) bdConv;
		else if (type.equals(BigInteger.class)) return (JsonConverter<E>) biConv;
		else if (type.isEnum()) return new EnumConverter();
		return (JsonConverter<E>) nullConv;
	}

	public static class DefaultConverter<E> implements JsonConverter<E> {

		@Override
		public E toJava(String value, Class<E> clazz) {
			return getConverter(clazz).toJava(value, clazz);
		}

		@Override
		public String toJson(E value) {
			@SuppressWarnings("unchecked")
			JsonConverter<E> cnv = (JsonConverter<E>) getConverter(value.getClass());
			return cnv.toJson(value);
		}

	}

	public static class NullConverter<E> implements JsonConverter<E> {

		@Override
		public E toJava(String value, Class<E> clazz) {
			return null;
		}

		@Override
		public String toJson(E value) {
			return "null";
		}
	}

	public static class  EnumConverter<T extends Enum<T>> implements JsonConverter<T> {

		@Override
		public T toJava(String value, Class<T> clazz) throws JsonConvertException {
			return (T) Enum.valueOf(clazz, value);
		}

		@Override
		public String toJson(T value) {
			return String.valueOf(value);
		}
	};

	private static final JsonConverter<Boolean> boolConv = new JsonConverter<Boolean>() {

		@Override
		public Boolean toJava(String value, Class<Boolean> clazz) {
			return Boolean.valueOf(value);
		}

		@Override
		public String toJson(Boolean value) {
			return String.valueOf(value);
		}
	};

	private static final JsonConverter<String> StrConv = new JsonConverter<String>() {

		@Override
		public String toJson(String value) {
			return value;
		}

		@Override
		public String toJava(String value, Class<String> clazz) throws JsonConvertException {
			return value;
		}

	};

	private static final JsonConverter<Integer> IntConv = new JsonConverter<Integer>() {

		@Override
		public String toJson(Integer value) {
			return String.valueOf(value);
		}

		@Override
		public Integer toJava(String value, Class<Integer> clazz) throws JsonConvertException {
			return Integer.parseInt(value);
		}

	};

	private static final JsonConverter<Long> longConv = new JsonConverter<Long>() {

		@Override
		public String toJson(Long value) {
			return String.valueOf(value);
		}

		@Override
		public Long toJava(String value, Class<Long> clazz) throws JsonConvertException {
			return Long.parseLong(value);
		}

	};

	private static final JsonConverter<Double> doubleConv = new JsonConverter<Double>() {

		@Override
		public Double toJava(String value, Class<Double> clazz) {
			return Double.parseDouble(value);
		}

		@Override
		public String toJson(Double value) {
			return String.valueOf(value);
		}
	};

	private static final JsonConverter<Float> floatConv = new JsonConverter<Float>() {

		@Override
		public Float toJava(String value, Class<Float> clazz) {
			return Float.parseFloat(value);
		}

		@Override
		public String toJson(Float value) {
			return String.valueOf(value);
		}
	};

	private static final JsonConverter<BigInteger> biConv = new JsonConverter<BigInteger>() {

		@Override
		public String toJson(BigInteger value) {
			return String.valueOf(value);
		}

		@Override
		public BigInteger toJava(String value, Class<BigInteger> clazz) throws JsonConvertException {
			return new BigInteger(value);
		}

	};

	private static final JsonConverter<BigDecimal> bdConv = new JsonConverter<BigDecimal>() {

		@Override
		public String toJson(BigDecimal value) {
			return String.valueOf(value);
		}

		@Override
		public BigDecimal toJava(String value, Class<BigDecimal> clazz) throws JsonConvertException {
			return new BigDecimal(value);
		}

	};

	private static final JsonConverter<?> nullConv = new NullConverter<Object>();
}
