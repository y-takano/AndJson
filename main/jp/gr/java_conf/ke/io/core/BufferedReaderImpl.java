package jp.gr.java_conf.ke.io.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import jp.gr.java_conf.ke.io.BufferedTextWriter;

class BufferedReaderImpl extends BufferControler {

	public BufferedReaderImpl(String str) {
		super(new StringReader(str), null);
	}

	public BufferedReaderImpl(Reader reader) throws IOException {
		super(reader, null);
	}

	public BufferedReaderImpl(InputStream is) throws IOException {
		super(new InputStreamReader(is), null);
	}

	@Override
	public BufferedTextWriter append(char c) {
		throw new UnsupportedOperationException();
	}
}
