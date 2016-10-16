package jp.gr.java_conf.ke.json.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOFactory {

	public static BufferedTextReader createReader(String str) throws IOException {
//		return new JsonInputStream(new InputStreamDelegater(str));
		return new NewInputStreamReader(str);
	}

	public static BufferedTextReader createReader(Reader str) throws IOException {
//		return new JsonInputStream(new InputStreamDelegater(str));
		return new NewInputStreamReader(str);
	}

	public static BufferedTextReader createReader(InputStream str) throws IOException {
//		return new JsonInputStream(new InputStreamDelegater(str));
		return new NewInputStreamReader(str);
	}

	public static BufferedTextWriter createWriter() {
//		return new JsonOutputStream(new OutputStreamDelegater());
		return new NewOutputStreamWriter();
	}

	public static BufferedTextWriter createWriter(Writer writer) {
//		return new JsonOutputStream(new OutputStreamDelegater(writer));
		return new NewOutputStreamWriter(writer);
	}

	public static BufferedTextWriter createWriter(OutputStream os) {
//		return new JsonOutputStream(new OutputStreamDelegater(os));
		return new NewOutputStreamWriter(os);
	}

}
