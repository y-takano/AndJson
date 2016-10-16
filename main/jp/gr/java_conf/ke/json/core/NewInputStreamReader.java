package jp.gr.java_conf.ke.json.core;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import jp.gr.java_conf.ke.json.stream.IOStreamingException;

public class NewInputStreamReader implements BufferedTextReader {

	private final Reader reader;
	private final CharBuffer buf;
	private int packetSize;
	private boolean closed;

	NewInputStreamReader(String str) throws IOException {
		this.reader = new CharArrayReader(str.toCharArray());
		this.buf = CharBuffer.newReadBuffer(this.reader);
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	NewInputStreamReader(Reader reader) throws IOException {
		this.reader = reader;
		this.buf = CharBuffer.newReadBuffer(this.reader);
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	NewInputStreamReader(InputStream is) throws IOException {
		this.reader = new InputStreamReader(is);
		this.buf = CharBuffer.newReadBuffer(this.reader);
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	@Override
	public char read() {
		if (!hasNext()) return (char) -1;
		char ret = buf.read(reader);
		if ((char) -1 == ret) {
			try {
				close();
			} catch (IOException e) {
				throw new IOStreamingException(e);
			}
		}
		return ret;
	}

	@Override
	public boolean hasNext() {
		return !closed;
	}

	@Override
	public void close() throws IOException {
		reader.close();
		buf.release();
		closed = true;
	}

	@Override
	public char[] readPacket() {
		char[] ret = buf.readPacket(reader);
		if (!isClosed() && ret.length == 0) {
			try {
				close();
			} catch (IOException e) {
				throw new IOStreamingException(e);
			}
		}
		return ret;
	}

	@Override
	public void setPacketSize(int size) {
		this.packetSize = size;
	}

	@Override
	public int getPacketSize() {
		return packetSize;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

}
