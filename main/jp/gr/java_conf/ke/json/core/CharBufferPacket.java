package jp.gr.java_conf.ke.json.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

class CharBufferPacket implements CyclicReader, CyclicWriter {

	private final char[] buf;
	private int size;
	private int index;

	public CharBufferPacket(int size) {
		this.buf = new char[size];
	}

	public CyclicReader getReader(Reader r) throws IOException {
		size = r.read(buf);
		index = 0;
		return this;
	}

	public CyclicWriter getWriter() {
		index = 0;
		size = buf.length;
		return this;
	}

	@Override
	public boolean write(char c) {
		buf[index++] = c;
		return isLimit();
	}

	@Override
	public boolean isLimit() {
		return buf.length <= index;
	}

	@Override
	public char next() {
		return buf[index++];
	}

	public void flush(Writer w) throws IOException {
		w.write(buf);
		w.flush();
	}

	@Override
	public char[] read() {
		int length = size - index;
		char[] ret = new char[size - index];
		System.arraycopy(buf, index, ret, 0, length);
		index = size;
		return ret;
	}

}
