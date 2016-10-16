package jp.gr.java_conf.ke.json.databind.converter;

public interface JsonConverter<E> {

	E toJava(String value, Class<E> clazz);

	String toJson(E value);
}
