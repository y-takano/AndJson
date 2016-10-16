package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import jp.gr.java_conf.ke.json.core.IOFactory;
import jp.gr.java_conf.ke.json.core.BufferedTextReader;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public class JsonParserFactory {

	private JsonParserFactory() {}

	public static JsonParser createParser(String jsonText) throws IOException {
		return new JsonParserImpl(IOFactory.createReader(jsonText));
	}

	public static JsonParser createParser(Reader reader) throws IOException {
		return new JsonParserImpl(IOFactory.createReader(reader));
	}

	public static JsonParser createParser(InputStream is) throws IOException {
		return new JsonParserImpl(IOFactory.createReader(is));
	}

	public static JsonParser createParser(BufferedTextReader tr) {
		return new JsonParserImpl(tr);
	}

	static {
		String testStr = "{\"name1\":\"val1\",\"name2\":-229.31289,\"name3\":[\"val1\"],\"name4\":{\"key1\":\"val1\"}}";
		try {
			for (@SuppressWarnings("unused") Object o :new JsonParserImpl(IOFactory.createReader(testStr))) ;
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
