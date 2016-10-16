package jp.gr.java_conf.ke.json.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class BufferControler {

	private static final int DEFAULT_PACKET_SIZE = 1024;
	private static final int MAX_BYTE_SIZE = DEFAULT_PACKET_SIZE * 10000;

	private final List<BufferPacket> buffers = new ArrayList<BufferPacket>();
	private final InputStreamDelegater input;
	private final OutputStreamDelegater output;
	private int length;

	public BufferControler(InputStreamDelegater delegate) {
		this.input = delegate;
		this.output = null;
	}

	public BufferControler(OutputStreamDelegater delegate) {
		this.input = null;
		this.output = delegate;
		allocate(100);
	}

	public BufferPacket usePacket() {
		if (buffers.isEmpty()) {
			allocate(100);
		}
		if (buffers.isEmpty()) return null;
		return buffers.remove(0);
	}

	public void releasePacket(BufferPacket used) {
		used.reset();
		buffers.add(used);
	}

	public void write(char c) {
		BufferPacket pack = null;
		for (int i=0; i<buffers.size(); i++) {
			pack = buffers.get(i);
			if (pack.write(c)) {
				break;
			}
		}
	}

	public void flush() throws IOException {
		int size = 0;
		for (BufferPacket pack : buffers) {
			size += pack.length();
		}

		char[] buf = new char[size];
		int index = 0;
		for (BufferPacket pack : buffers) {
			for (int i=0; i<pack.length(); i++) {
				buf[index++] = pack.charAt(i);
			}
		}
		output.flush(buf);
	}

	private void allocate(int packNum) {
		length = 0;
		for (BufferPacket buf : buffers) {
			length += buf.maxLength();
		}
		int nextSize = DEFAULT_PACKET_SIZE;
		for (int i=0; i<packNum; i++) {
			if (MAX_BYTE_SIZE <= length) {
				break;
			} else if (MAX_BYTE_SIZE <= length + DEFAULT_PACKET_SIZE) {
				nextSize = MAX_BYTE_SIZE - length;
			}
			BufferPacket ret = new BufferPacket(input, nextSize);
			buffers.add(ret);
		}
	}

	public String toString() {

		int size = 0;
		for (BufferPacket pack : buffers) {
			size += pack.length();
		}

		StringBuilder sb = new StringBuilder(size);
		for (BufferPacket pack : buffers) {
			sb.append(pack.unwrap());
		}
		return sb.toString();
	}
}
