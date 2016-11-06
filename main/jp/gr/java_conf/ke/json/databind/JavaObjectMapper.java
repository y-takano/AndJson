package jp.gr.java_conf.ke.json.databind;

import java.io.IOException;

import jp.gr.java_conf.ke.json.databind.internal.adapter.AdapterFactory;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public class JavaObjectMapper<T> {

	public T toJava(JsonParser parser, Class<T> clazz) {
		try {
			return AdapterFactory.createClassReadAdapter(clazz).read(parser);
		} finally {
			try {
				if (parser.hasNext()) parser.close();
			} catch (IOException e) {
				throw new JsonBindException(e);
			}
		}
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
