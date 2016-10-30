package jp.gr.java_conf.ke.io.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jp.gr.java_conf.ke.io.IOStreamingException;

abstract class BufferControler {

	private BufferPacket currentPacket;

	public final int getPacketSize() {
		return getPacket().limit();
	}

	protected final boolean hasNext() {
		BufferPacket p = getPacket();
		return p.hasNext();
	}

	protected final char next() throws IOException {
		BufferPacket p = getPacket();
		return p.hasNext() ? p.next() : (char) -1;
	}

	protected final char[] readAll() throws IOException {
		BufferPacket p = getPacket();
		return p.read();
	}

	protected final boolean append(char c) {
		BufferPacket p = getPacket();
		return p.write(c);
	}

	protected final void flush(Writer w) throws IOException {
		BufferPacket p = getPacket();
		p.reset();
		w.write(p.read());
		w.flush();
	}

	private BufferPacket getPacket() {

		BufferPacket ret;
		if (currentPacket == null) {
			currentPacket = BufferPacketRecycler.getPacket();
			currentPacket.reset();
			readPacket(currentPacket);
			ret = currentPacket;

		} else if (currentPacket.isLimit()) {
			BufferPacketRecycler.release(currentPacket);
			currentPacket = BufferPacketRecycler.getPacket();
			currentPacket.reset();
			readPacket(currentPacket);
			ret = currentPacket;

		} else {
			ret = currentPacket;
		}
		return ret;
	}

	private final void readPacket(BufferPacket p) {
		char[] stream = new char[currentPacket.limit()];
		int size = 0;
		try {
			Reader r = getReader();
			if (r != null) {
				size = r.read(stream);
			}
		} catch (IOException e) {
			throw new IOStreamingException(e);
		}
		for (int i=0;i<size;i++) p.write(stream[i]);
	}

	public String toString() {
		return getPacket().toString();
	}

	abstract protected Reader getReader() throws IOException;
}
