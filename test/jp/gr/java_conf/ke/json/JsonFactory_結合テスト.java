package jp.gr.java_conf.ke.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jp.gr.java_conf.ke.android.domain.interfaces.UIEventListeners;
import jp.gr.java_conf.ke.android.ui.draw.DrawEvent;
import jp.gr.java_conf.ke.android.ui.msg.MessageEvent;
import jp.gr.java_conf.ke.android.ui.sound.SoundEvent;
import jp.gr.java_conf.ke.android.ui.transit.TransitionEvent;
import jp.gr.java_conf.ke.json.stream.JsonParser;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.ttg.app.battle.GameMaster;

import org.junit.Test;

public class JsonFactory_結合テスト {

	@Test
	public void test1() throws JsonConvertException, IOException, URISyntaxException {
		long time = System.currentTimeMillis();

//		URI uri = new URI("file:///C:/Users/YT/Desktop/KEN_SHORT.jsn");
		URI uri = new URI("file:///C:/Users/YT/Desktop/KEN_ALL.jsn");

		File jsonFile = new File(uri);

		time = System.currentTimeMillis();

		int times = 10;

		long[] records = new long[times];
		long[] recordsA = new long[times];
		long[] recordsB = new long[times];

		for (int i=0; i<times; i++) {

		//String jsonText = "{\"name1\":\"val1\",\"name2\":-229.31289,\"name3\":[],\"name4\":{}}";
//		String jsonText = "{\"name1\":\"val1\", \"name2\":\"val2\", \"name3\":\"val3\", \"name4\":\"val4\"}";

//		TextWriter writer = IOFactory.createWriter(new FileOutputStream(new File("aaa.jsn")));
//		writer.write(jsonText.toCharArray());
//		writer.flush();
//		System.out.println(writer.toString());
//		System.out.println(System.currentTimeMillis() - time);
//		writer.close();
//
//		BigDecimal decimal = new BigDecimal("-229.31289");
//		JsonGenerator generator = createGenerator(new File("bbb.jsn"));
//		time = System.currentTimeMillis();
//		JsonObjectBuilder root = generator.rootObject();
//		root.append("name1", "val1");
//		root.append("name2", decimal);
//		root.startArray("name3").endArray();
//		root.startObject("name4").endObject();
//		root.endObject();
//		generator.flushAndClose();
//		System.out.println(generator.toString());
//		System.out.println(System.currentTimeMillis() - time);
//
//		TextReader r = IOFactory.createReader(new FileReader("bbb.jsn"));
//		StringBuilder sb = new StringBuilder();
//		time = System.currentTimeMillis();
//		while (r.hasNext()) {
//			sb.append(r.read());
//		}
//		System.out.println(sb);
//		System.out.println(System.currentTimeMillis() - time);
//		r.close();
//
//		System.out.println();
		time = System.currentTimeMillis();
		JsonParser p = JsonFactory.createParser(jsonFile);
		System.out.println(i + "a(init) = " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
//		while (p.next() != null) {
		for (Token t : p) {
//			System.out.println(t);
//			System.out.println(p.current());
//			System.out.println(i + "a = " + (System.currentTimeMillis() - time));
//			time = System.currentTimeMillis();
		}
		recordsA[i] = System.currentTimeMillis() - time;
		System.out.println(i + "a = " + recordsA[i]);

		time = System.currentTimeMillis();
		org.codehaus.jackson.JsonFactory factory = new org.codehaus.jackson.JsonFactory();
		org.codehaus.jackson.JsonParser parser = factory.createJsonParser(jsonFile);
		System.out.println(i + "b(init) = " + (System.currentTimeMillis() - time));
		time = System.currentTimeMillis();
		 while (parser.nextToken() != null) {
//		       String name = parser.getCurrentName();
//		       org.codehaus.jackson.JsonToken token = parser.getCurrentToken();
//		       token.toString();
//		       parser.getText();
//		       System.out.println(token + ":" + parser.getText());
//			System.out.println(i + "b = " + (System.currentTimeMillis() - time));
//			time = System.currentTimeMillis();
		}
		recordsB[i] = System.currentTimeMillis() - time;
		System.out.println(i + "b = " + recordsB[i]);
		//time = System.currentTimeMillis();
		//JsonFactory.deserialize(new File("bbb.jsn"), TestClass.class);
		//System.out.println(deserialize(new File("bbb.jsn"), TestClass.class));
		//records[i] = System.currentTimeMillis() - time;
		//System.out.println(i + "c = " + records[i]);
//
//		TestClass clz = deserialize(new File("bbb.jsn"), TestClass.class);
//		time = System.currentTimeMillis();
//		String j = toJson(clz);
//		time = System.currentTimeMillis() - time;
//		System.out.println(j);
//		System.out.println(time);
//
//		GameMaster master = deserialize(new File("aaa.jsn"), GameMaster.class);
//		System.out.println(toJson(master));
//		System.out.println(master.getPhase());
//		System.out.println(master.getErrorState());
		}

		long total;
		total = 0;
		for (int i=0; i<times; i++) {
			total += recordsA[i];
		}
		System.out.println("A:AndJson avg = " + (double)((double)total/(double)times));

		total = 0;
		for (int i=0; i<times; i++) {
			total += recordsB[i];
		}
		System.out.println("B:Jackson avg = " + (double)((double)total/(double)times));

		//total = 0;
		//for (int i=0; i<times; i++) {
		//	total += records[i];
		//}
		//System.out.println("C avg = " + (double)((double)total/(double)times));

	}
	@Test
	public void test2() throws JsonConvertException, IOException {

		GameMaster gm = new GameMaster();

		gm.prepare(null, new UIEventListeners(){

			@Override
			public <E extends DrawEvent> void callback(E arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public <E extends MessageEvent> void callback(E arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public <E extends SoundEvent> void callback(E arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}

			@Override
			public <E extends TransitionEvent> void callback(E arg0) {
				// TODO 自動生成されたメソッド・スタブ

			}});

		String json = JsonFactory.toJson(gm);

		System.out.println(json);
		System.out.println(JsonFactory.deserialize(json, GameMaster.class));

		System.out.println(JsonFactory.deserialize("{\"enemy\":{\"name\":\"type\",\"type\":null},\"phase\":\"COMMAND\",\"player\":{\"name\":\"uuid\",\"uuid\":null},\"state\":\"NORMAL\"}", GameMaster.class));

	}

}
