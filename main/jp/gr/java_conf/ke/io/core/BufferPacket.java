package jp.gr.java_conf.ke.io.core;

import java.util.Iterator;

class BufferPacket implements Iterator<Character>{

	private final char[] buf;
	private int size;
	private int index = 0;

	public BufferPacket(int limit) {
		this.buf = new char[limit];
	}

	public int limit() {
		return buf.length;
	}

	public boolean isLimit() {
		return limit() <= size;
	}

	public int size() {
		return size;
	}

	public boolean write(char c) {
		boolean ret = !isLimit();
		if (ret) buf[size++] = c;
		return ret;
	}

	public char[] read() {
		char[] ret = new char[size];
		System.arraycopy(buf, 0, ret, 0, size);
		return ret;
	}

	public void reset() {
		size = 0;
		index = 0;
	}

	@Override
	public boolean hasNext() {
		return index <= size - 1;
	}

	@Override
	public Character next() {
		char c = buf[index];
		index++;
		return c;
	}

	public String toString() {
		return String.valueOf(read());
	}

	@Override
	public void remove() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
