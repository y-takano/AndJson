package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.core.BufferedIOFactory;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public class JsonParserFactory {

	private JsonParserFactory() {}

	public static JsonParser createParser(String jsonText) {
		return new JsonParserImpl(BufferedIOFactory.createReader(jsonText));
	}

	public static JsonParser createParser(Reader reader) throws IOException {
		return new JsonParserImpl(BufferedIOFactory.createReader(reader));
	}

	public static JsonParser createParser(InputStream is) throws IOException {
		return new JsonParserImpl(BufferedIOFactory.createReader(is));
	}

	public static JsonParser createParser(BufferedTextReader tr) {
		return new JsonParserImpl(tr);
	}

	static {
		String testStr = "{\"name1\":\"val1\",\"name2\":-229.31289,\"name3\":[\"val1\"],\"name4\":{\"key1\":\"val1\"}}";
		for (@SuppressWarnings("unused") Object o :new JsonParserImpl(BufferedIOFactory.createReader(testStr))) ;
	}
}
