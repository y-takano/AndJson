package jp.gr.java_conf.ke.io.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.io.IOStreamingException;

abstract class BufferControler implements BufferedTextReader, BufferedTextWriter {

	private final boolean readable;
	private final Reader reader;

	private final boolean writable;
	private final Writer writer;

	private boolean complete;
	private boolean streamClosed;
	private BufferPacket currentPacket;
	private BufferPacket nextPacket;

	protected BufferControler(Reader r, Writer w) {
		readable = r != null;
		writable = w != null;
		if (readable == writable)
			throw new IOStreamingException("状態矛盾: 読込・書込方向不明。 readable="
					+ readable + ", writable=" + writable);
		reader = r;
		writer = w;
	}

	public final int getPacketSize() {
		return getPacket().limit();
	}

	public final boolean hasNext() {
		BufferPacket p = getPacket();
		boolean ret = p.hasNext();
		if (!ret) complete = true;
		return ret;
	}

	protected final boolean write(char c) {
		BufferPacket p = getPacket();
		return p.write(c);
	}

	public final char read() {
		BufferPacket p = getPacket();
		return p.hasNext() ? p.next() : (char) -1;
	}

	public final void flush() throws IOException {
		onFlushOrClose();
		writer.flush();
	}

	public final void close() throws IOException {
		onFlushOrClose();
		if (readable) reader.close();
		else  writer.close();
		streamClosed = true;
	}

	public final boolean isClosed() {
		return streamClosed;
	}

	public final boolean ready() throws IOException {
		return !complete;
	}

	private BufferPacket getPacket() {

		BufferPacket ret = null;

		if (readable) {
			ret = getReadPacket();

		} else if (writable) {
			ret = getWritePacket();
		}
		return ret;
	}

	private BufferPacket getReadPacket() {

		if (currentPacket == null) {
			currentPacket = BufferPacketRecycler.getPacket();
			readPacket(currentPacket);
			if (!streamClosed) {
				nextPacket = BufferPacketRecycler.getPacket();
				readPacket(nextPacket);
			}

		} else if (!currentPacket.hasNext()) {
			BufferPacketRecycler.release(currentPacket);
			if (nextPacket != null) {
				currentPacket = nextPacket;
			} else {
				currentPacket = BufferPacketRecycler.getPacket();
			}
			if (!streamClosed) {
				nextPacket = BufferPacketRecycler.getPacket();
				readPacket(nextPacket);
			}
		}
		return currentPacket;
	}

	private BufferPacket getWritePacket() {

		if (currentPacket == null) {
			currentPacket = BufferPacketRecycler.getPacket();
			nextPacket = BufferPacketRecycler.getPacket();
		} else if (currentPacket.isLimit()) {
			writePacket(currentPacket);
			BufferPacketRecycler.release(currentPacket);
			currentPacket = nextPacket;
			nextPacket = BufferPacketRecycler.getPacket();
		}
		return currentPacket;
	}

	/**
	 * ストリームからメモリ（バッファパケット）にデータを展開する
	 *
	 * @param p
	 *            読み出し先パケット
	 */
	private void readPacket(BufferPacket p) {

		Reader r = reader;
		try {
			if (r.ready()) {

				char[] stream = new char[p.limit()];
				int size = r.read(stream);

				// ストリーム読み込み＋パケット書き込み
				for (int i = 0; i < size; i++)
					p.write(stream[i]);

				// 読み込みデータがパケットサイズを超えていない場合は終了
				if (!p.isLimit()) {
					r.close();
					streamClosed = true;
				}
			}
		} catch (IOException e) {
			closeStream(r);
			throw new IOStreamingException(e);
		}
	}

	/**
	 * メモリ（バッファパケット）からストリームへデータを出力する
	 *
	 * @param p
	 *            読み込み元パケット
	 */
	private void writePacket(BufferPacket p) {

		Writer w = writer;

		try {
			while (p.hasNext())
				w.append(p.next());
			w.flush();

		} catch (IOException e) {
			closeStream(w);
			throw new IOStreamingException(e);
		}
	}

	private void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void onFlushOrClose() {
		if (writable && !complete) {
			writePacket(currentPacket);
		}
		BufferPacketRecycler.release(currentPacket);
		complete = true;
	}

	public String toString() {
		return getPacket().toString();
	}
}
