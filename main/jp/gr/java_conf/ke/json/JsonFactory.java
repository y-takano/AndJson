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
import java.util.concurrent.atomic.AtomicBoolean;

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

	private static void waitLoad() {
		while (true) {
			if (started.get()) break;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public static <E> String toJson(E jsonObject) {
		waitLoad();
		try {
			return new JavaObjectMapper().toJson(jsonObject, createGenerator());
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static <E> String serialize(E jsonObject, OutputStream out) {
		waitLoad();
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(out));
	}

	public static <E> String serialize(E jsonObject, Writer writer) {
		waitLoad();
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(writer));
	}

	public static <E> String serialize(E jsonObject, File file) throws FileNotFoundException {
		waitLoad();
		return new JavaObjectMapper().toJson(jsonObject, createGenerator(file));
	}

	public static <E> E toObject(String jsonText, Class<E> clazz) throws JsonConvertException {
		waitLoad();
		try {
			return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
		} catch (JsonConvertException e) {
			e.printStackTrace();
			throw e;
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static <E> E deserialize(InputStream jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		waitLoad();
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}

	public static <E> E deserialize(Reader jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		waitLoad();
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}


	public static <E> E deserialize(File jsonText, Class<E> clazz) throws JsonConvertException, IOException {
		waitLoad();
		return (E) new JavaObjectMapper().toJava(createParser(jsonText), clazz);
	}

	public static JsonParser createParser(String jsonText) {
		waitLoad();
		return JsonParserFactory.createParser(jsonText);
	}

	public static JsonParser createParser(InputStream in) throws IOException {
		waitLoad();
		return JsonParserFactory.createParser(in);
	}

	public static JsonParser createParser(Reader reader) throws IOException {
		waitLoad();
		return JsonParserFactory.createParser(reader);
	}

	public static JsonParser createParser(File jsonFile) throws IOException {
		waitLoad();
		return  JsonParserFactory.createParser(new FileReader(jsonFile));
	}

	public static JsonGenerator createGenerator() {
		waitLoad();
		return JsonGeneratorFactory.createGenerator();
	}

	public static JsonGenerator createGenerator(OutputStream out) {
		waitLoad();
		return JsonGeneratorFactory.createGenerator(out);
	}

	public static JsonGenerator createGenerator(Writer writer) {
		waitLoad();
		return JsonGeneratorFactory.createGenerator(writer);
	}

	public static JsonGenerator createGenerator(File file) throws FileNotFoundException {
		waitLoad();
		return JsonGeneratorFactory.createGenerator(new FileOutputStream(file));
	}

	private static class RazyClass {

		private static void warmup() {
			try {
				new JavaObjectMapper()
					.toJava(
						JsonParserFactory.createParser(
								"{\"name1\":\"val1\",\"name2\":-229.31289,\"name3\":[\"val1\"],\"name4\":{\"key1\":\"val1\"},\"name5\":{}}"),
								TestClass.class);
				new JavaObjectMapper()
					.toJson(
						new TestClass2(),
						JsonGeneratorFactory.createGenerator());
			} catch (JsonConvertException e) {
				throw new ExceptionInInitializerError(e);
			}
		}

		@JsonBean
		public static class TestClass {

			@JsonIgnore
			private String name1;

			@SuppressWarnings("unused")
			private BigDecimal name2;

			@JsonList(valueGeneric = @Generics(concreteType = String.class))
			private List name3;

			@JsonMap(valueGeneric = @Generics(concreteType = String.class))
			private Map name4;

			@JsonBean
			private TestClass2 name5;
		}

		public static class TestClass2 {

			@SuppressWarnings("unused")
			private String name1;

			@SuppressWarnings("unused")
			private BigInteger name2;

			@SuppressWarnings("unused")
			private List name3;

			@SuppressWarnings("unused")
			private Map name4;
		}
	}

	private static AtomicBoolean started = new AtomicBoolean(false);
	static {
		RazyClass.warmup();
		started.set(true);
	}
}
