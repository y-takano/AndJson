package jp.gr.java_conf.ke.json.databind;

import jp.gr.java_conf.ke.json.databind.internal.adapter.AdapterFactory;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public class JavaObjectMapper<T> {

	public T toJava(JsonParser parser, Class<T> clazz) {
		return AdapterFactory.createClassReadAdapter(clazz).read(parser);
	}

	public String toJson(T instance, JsonGenerator generator) {
		JsonBuilder rootBuilder = generator.rootObject();
		AdapterFactory.createClassWriteAdapter(instance).write(rootBuilder);
		String ret = generator.toString();
		try {
			generator.flushAndClose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return ret;
	}
}
