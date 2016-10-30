package jp.gr.java_conf.ke.io.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import jp.gr.java_conf.ke.io.IOStreamingException;
import jp.gr.java_conf.ke.io.BufferedTextReader;

class BufferedReaderImpl extends BufferControler implements BufferedTextReader {

	private final Reader inputStream;
	private boolean closed;
	private boolean reading;

	public BufferedReaderImpl(Reader str) throws IOException {
		this.inputStream = str;
	}

	public BufferedReaderImpl(String str) throws IOException {
		this.inputStream = new StringReader(str);
	}

	public BufferedReaderImpl(InputStream str) throws IOException {
		this.inputStream = new InputStreamReader(str);
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public void close() throws IOException {
		closed = true;
		inputStream.close();
	}

	@Override
	public char read() throws IOException {
		char ret = next();
		reading = true;
		return ret;
	}

	@Override
	public char[] readPacket() throws IOException {
		if (reading)
			throw new IOStreamingException(
					"単文字読み込み（read）中にパケット読み込み（readPacket）を呼び出せません");
		char[] ret = readAll();
		reading = false;
		return ret;
	}

	@Override
	public boolean ready() throws IOException {
		return !isClosed() && hasNext() && inputStream.ready();
	}

	@Override
	protected Reader getReader() throws IOException {
		return ready() ? inputStream : null;
	}
}
