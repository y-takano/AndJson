package jp.gr.java_conf.ke.json;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import jp.gr.java_conf.ke.json.databind.JavaObjectMapper;
import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;
import jp.gr.java_conf.ke.json.databind.annotation.JsonIgnore;
import jp.gr.java_conf.ke.json.databind.annotation.JsonList;
import jp.gr.java_conf.ke.json.databind.annotation.JsonMap;
import jp.gr.java_conf.ke.json.databind.annotation.Generics;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;
import jp.gr.java_conf.ke.json.stream.JsonParser;
import jp.gr.java_conf.ke.json.stream.generate.JsonGeneratorFactory;
import jp.gr.java_conf.ke.json.stream.parse.JsonParserFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class JsonFactory {

	public static <E> String toJson(E jsonObject) {
		return new JavaObjectMapper().toJson(jsonObject, createGenerator());
	}

	public static <E> String serialize(E jsonObject, OutputStream out) {
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(out));
	}

	public static <E> String serialize(E jsonObject, Writer writer) {
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(writer));
	}

	public static <E> String serialize(E jsonObject, File file) throws FileNotFoundException {
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(file));
	}

	public static <E> E toObject(String jsonText, Class<E> clazz) throws JsonConvertException {
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}

	public static <E> E deserialize(InputStream jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}

	public static <E> E deserialize(Reader jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}


	public static <E> E deserialize(File jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}

	public static JsonParser createParser(String jsonText) {
		return JsonParserFactory.createParser(jsonText);
	}

	public static JsonParser createParser(InputStream in) throws IOException {
		return JsonParserFactory.createParser(in);
	}

	public static JsonParser createParser(Reader reader) throws IOException {
		return JsonParserFactory.createParser(reader);
	}

	public static JsonParser createParser(File jsonFile) throws IOException {
		return  JsonParserFactory.createParser(new FileReader(jsonFile));
	}

	public static JsonGenerator createGenerator() {
		return JsonGeneratorFactory.createGenerator();
	}

	public static JsonGenerator createGenerator(OutputStream out) {
		return JsonGeneratorFactory.createGenerator(out);
	}

	public static JsonGenerator createGenerator(Writer writer) {
		return JsonGeneratorFactory.createGenerator(writer);
	}

	public static JsonGenerator createGenerator(File file) throws FileNotFoundException {
		return JsonGeneratorFactory.createGenerator(new FileOutputStream(file));
	}

	@SuppressWarnings("unused")
	private static class RazyClass {
		static {
			try {
				toObject("{\"name1\":\"val1\",\"name2\":-229.31289,\"name3\":[\"val1\"],\"name4\":{\"key1\":\"val1\"},\"name5\":{}}", TestClass.class);
			} catch (JsonConvertException e) {
				throw new ExceptionInInitializerError(e);
			}
		}

		private static void load() {}

		@JsonBean
		public static class TestClass {

			@JsonIgnore
			private String name1;

			private BigDecimal name2;

			@JsonList(valueGeneric = @Generics(concreteType = String.class))
			private List name3;

			@JsonMap(valueGeneric = @Generics(concreteType = String.class))
			private Map name4;

			@JsonBean
			private TestClass2 name5;
		}

		public static class TestClass2 {

			private String name1;

			private BigInteger name2;

			private List name3;

			private Map name4;
		}
	}

	static {
		RazyClass.load();
	}
}
