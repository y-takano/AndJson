package jp.gr.java_conf.ke.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.io.core.BufferedIOFactory;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.generate.JsonGeneratorFactory;
import jp.gr.java_conf.ke.json.stream.parse.JsonParserFactory;

import org.junit.Test;

public class JsonGenerator_単体テスト {

	@Test
	public void 一般的なジェネレート() throws IOException {

		File aaa = new File("aaa.jsn");
		if (aaa.exists()) aaa.delete();
		aaa.createNewFile();
		//aaa.deleteOnExit();

		Map<String, Object> map = new HashMap<>();
		map.put("name1", "aaa");
		map.put("name2", null);
		map.put("name3", true);
		map.put("name4", Integer.MIN_VALUE);
		map.put("name5", "");

		Object[] obj = new Object[]{"aaa", null, false, -210391};

		long time;
		time = System.currentTimeMillis();

		JsonGenerator writer = JsonGeneratorFactory.createGenerator(new FileOutputStream(aaa));
		JsonBuilder builder = writer.rootObject();
		builder.startObject();
		builder.name("name1").valueAsString("val1");
		builder.name("name2").value("{aaa:val}");
		builder.name("name3").value(obj);
		builder.name("name4").valueAsDirect("{\"aaa\":null}");
		builder.name("name5").valueAsBoolean(true);
		builder.name("name6").valueAsNull();
		builder.name("name7").valueAsNumber(Integer.MAX_VALUE);
		builder.name("name8").value(map);
		builder.endElement();
		System.out.println(System.currentTimeMillis() - time);

		String text = writer.toString();
		System.out.println(text);

		writer.flushAndClose();

		for (Token t: JsonParserFactory.createParser(text)) {
			System.out.println(t);
		}
	}

}
