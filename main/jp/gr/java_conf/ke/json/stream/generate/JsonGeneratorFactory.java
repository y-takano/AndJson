package jp.gr.java_conf.ke.json.stream.generate;

import java.io.OutputStream;
import java.io.Writer;

import jp.gr.java_conf.ke.io.core.BufferedIOFactory;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;

public class JsonGeneratorFactory {

	private JsonGeneratorFactory() {}

	public static JsonGenerator createGenerator() {
		return new JsonStreamWriter(BufferedIOFactory.createWriter());
	}

	public static JsonGenerator createGenerator(OutputStream out) {
		return new JsonStreamWriter(BufferedIOFactory.createWriter(out));
	}

	public static JsonGenerator createGenerator(Writer writer) {
		return new JsonStreamWriter(BufferedIOFactory.createWriter(writer));
	}
}
