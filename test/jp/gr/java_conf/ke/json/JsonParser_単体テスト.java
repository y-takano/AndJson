package jp.gr.java_conf.ke.json;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import jp.gr.java_conf.ke.json.stream.JsonParser;
import jp.gr.java_conf.ke.json.Token;

import org.junit.Test;

public class JsonParser_単体テスト {

	@Test
	public void 一般的なパース() throws URISyntaxException, IOException, InterruptedException {

//		URI uri = new URI("file:///C:/Users/YT/Desktop/KEN_SHORT.jsn");
		URI uri = new URI("file:///C:/Users/YT/Desktop/KEN_ALL.jsn");
		File jsonFile = new File(uri);

		long time;
		time = System.currentTimeMillis();

		int times = 1;
		long[] records = new long[times];

		for (int i=0; i<times; i++) {
			time = System.currentTimeMillis();
			JsonParser p = JsonFactory.createParser(jsonFile);
			System.out.println(i + "a(init) = " + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();

			time = System.currentTimeMillis();
			for (Token t : p) {
				System.out.println(t);
			}
			records[i] = System.currentTimeMillis() - time;
			System.out.println(i + "a = " + records[i]);
		}

		long total;
		total = 0;
		for (int i=0; i<times; i++) {
			total += records[i];
		}
		System.out.println(" avg = " + (double)((double)total/(double)times));
	}

}
