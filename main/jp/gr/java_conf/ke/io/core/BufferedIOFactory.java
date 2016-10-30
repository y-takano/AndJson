package jp.gr.java_conf.ke.io.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.BufferedTextWriter;


public class BufferedIOFactory {

	public static BufferedTextReader createReader(String str) throws IOException {
		return new BufferedReaderImpl(str);
	}

	public static BufferedTextReader createReader(Reader str) throws IOException {
		return new BufferedReaderImpl(str);
	}

	public static BufferedTextReader createReader(InputStream is) throws IOException {
		return new BufferedReaderImpl(is);
	}

	public static BufferedTextWriter createWriter() {
		return new BufferedWriterImpl();
	}

	public static BufferedTextWriter createWriter(Writer writer) {
		return new BufferedWriterImpl(writer);
	}

	public static BufferedTextWriter createWriter(OutputStream os) {
		return new BufferedWriterImpl(os);
	}

}
