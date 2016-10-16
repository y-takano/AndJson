package jp.gr.java_conf.ke.json.core;

import java.io.IOException;

class BufferPacket {

	private char[] buf;
	private int index = 0;
	private int maxLength;
	private final InputStreamDelegater delegate;

	public BufferPacket(InputStreamDelegater delegate, int maxLength) {
		this.buf = new char[maxLength];
		this.delegate = delegate;
		this.index = 0;
		this.maxLength = maxLength;
	}

	public char read() throws IOException {
		if (index >= maxLength)
			load();
		if (index >= buf.length)
			return (char)-1;
		return buf[index++];
	}

	public boolean write(char c) {
		if (index >= maxLength) {
			return false;
		}
		buf[index++] = c;
		return true;
	}

	public char charAt(int index) {
		return buf[index];
	}

	public char[] unwrap() {
		return buf;
	}

	private void load() throws IOException {
		int size = delegate.read(buf);
		if (-1 == size) return;
		if (size <= buf.length) {
			char[] newbuf = new char[size];
			System.arraycopy(buf, 0, newbuf, 0, size);
			buf = newbuf;
			index = 0;
			maxLength = size;
		}
	}

	public int length() {
		return index;
	}

	public void reset() {
		this.buf = new char[maxLength];
	}

	public int maxLength() {
		return maxLength;
	}
}
