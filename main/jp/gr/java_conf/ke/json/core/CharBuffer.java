package jp.gr.java_conf.ke.json.core;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Queue;

import jp.gr.java_conf.ke.json.stream.IOStreamingException;

public class CharBuffer {

	static final int PACKET_SIZE = 1024;
	private static final int MAX_BYTE_SIZE = PACKET_SIZE * 10000;

	private static final Queue<CharBufferPacket> PACKET_RECYCLER = new LinkedList<CharBufferPacket>();

	private CyclicReader currentReader;
	private CyclicWriter currentWriter;

	static {
		allocate(500);
	}

	private CharBuffer(Reader r) throws IOException {
		currentReader = getPacket().getReader(r);
	}

	private CharBuffer() {
		currentWriter = getPacket().getWriter();
	}

	public static CharBuffer newReadBuffer(Reader r) throws IOException {
		return new CharBuffer(r);
	}

	public static CharBuffer newWriteBuffer() {
		return new CharBuffer();
	}

	public char read(Reader reader) {
		if (currentReader.isLimit()) {
			try {
				PACKET_RECYCLER.offer((CharBufferPacket)currentReader);
				currentReader = getPacket().getReader(reader);
			} catch (IOException e) {
				throw new IOStreamingException(e);
			}
		}
		return currentReader.next();
	}

	public char[] readPacket(Reader reader) {
		if (currentReader.isLimit()) {
			try {
				PACKET_RECYCLER.offer((CharBufferPacket)currentReader);
				currentReader = getPacket().getReader(reader);
			} catch (IOException e) {
				throw new IOStreamingException(e);
			}
		}
		return currentReader.read();
	}

	public void write(char c, Writer w) {
		if (currentWriter.write(c)) {
			try {
				currentWriter.flush(w);
			} catch (IOException e) {
				throw new IOStreamingException(e);
			}
			PACKET_RECYCLER.offer((CharBufferPacket)currentReader);
			currentWriter = getPacket().getWriter();
		}
	}

	private static CharBufferPacket getPacket() {
		CharBufferPacket p = PACKET_RECYCLER.poll();
		if (p == null) {
			allocate(10);
			p = PACKET_RECYCLER.remove();
		}
		return p;
	}

	private static void allocate(int packNum) {
		int nowSize = PACKET_SIZE * PACKET_RECYCLER.size();
		int requestSize = PACKET_SIZE * packNum;
		int num;
		if (MAX_BYTE_SIZE <= (nowSize + requestSize)) {
			num = (MAX_BYTE_SIZE - nowSize) / PACKET_SIZE;
			if (num < 1) {
				throw new IOStreamingException("メモリが不足しています。 最大値:" + MAX_BYTE_SIZE + ", 現在値:" + nowSize + ", 1パケットサイズ:" + PACKET_SIZE);
			}
		} else {
			num = packNum;
		}
		for (int i=0; i<num; i++) {
			PACKET_RECYCLER.offer(new CharBufferPacket(PACKET_SIZE));
		}
	}

	public void release() {
		if (currentReader != null) PACKET_RECYCLER.offer((CharBufferPacket) currentReader);
		if (currentWriter != null) PACKET_RECYCLER.offer((CharBufferPacket) currentWriter);
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			release();
		} catch (Exception e) {
		}
		super.finalize();
	}
}
