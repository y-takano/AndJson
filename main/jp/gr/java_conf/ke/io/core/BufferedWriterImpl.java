package jp.gr.java_conf.ke.io.core;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import jp.gr.java_conf.ke.io.BufferedTextWriter;

class BufferedWriterImpl extends BufferControler implements BufferedTextWriter {

	private final Writer outputStream;
	private boolean closed;

	public BufferedWriterImpl() {
		this.outputStream = new StringWriter();
	}

	public BufferedWriterImpl(Writer w) {
		this.outputStream = w;
	}

	public BufferedWriterImpl(OutputStream os) {
		this.outputStream = new OutputStreamWriter(os);
	}

	@Override
	public boolean ready() throws IOException {
		return !isClosed() || hasNext();
	}

	@Override
	public void close() throws IOException {
		closed = true;
		outputStream.close();
	}

	@Override
	public void flush() throws IOException {
		outputStream.write(readAll());
		outputStream.flush();
	}

	@Override
	public BufferedTextWriter write(char c) {
		append(c);
		return this;
	}

	@Override
	public BufferedTextWriter write(char[] str) {
		for (char c : str) {
			if (!append(c)) break;
		}
		return this;
	}

	@Override
	public BufferedTextWriter write(CharSequence str) {
		for (int i=0; i<str.length(); i++) {
			if (!append(str.charAt(i))) break;
		}
		return this;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	protected Reader getReader() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
