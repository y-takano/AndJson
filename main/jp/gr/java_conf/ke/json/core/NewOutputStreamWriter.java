package jp.gr.java_conf.ke.json.core;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class NewOutputStreamWriter implements BufferedTextWriter {

	private final Writer writer;
	private final CharBuffer buf;
	private int packetSize;
	private boolean closed;

	NewOutputStreamWriter() {
		this.writer = new CharArrayWriter();
		this.buf = CharBuffer.newWriteBuffer();
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	public NewOutputStreamWriter(Writer writer) {
		this.writer = writer;
		this.buf = CharBuffer.newWriteBuffer();
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	public NewOutputStreamWriter(OutputStream os) {
		this.writer = new OutputStreamWriter(os);
		this.buf = CharBuffer.newWriteBuffer();
		this.packetSize = BufferdIO.DEFAULT_PACKET_SIZE;
		this.closed = false;
	}

	@Override
	public void close() throws IOException {
		if (!isClosed()) writer.close();
		buf.release();
		closed = true;
	}

	@Override
	public void flush() throws IOException {
		if (!isClosed()) writer.flush();
	}

	@Override
	public BufferedTextWriter write(char c) {
		if (!isClosed()) buf.write(c, writer);
		return this;
	}

	@Override
	public BufferedTextWriter write(char[] str) {
		if (!isClosed())
			for (int i=0; i<str.length; i++)
				buf.write(str[i], writer);
		return this;
	}

	@Override
	public BufferedTextWriter write(CharSequence str) {
		if (!isClosed())
			for (int i=0; i<str.length(); i++)
				buf.write(str.charAt(i), writer);
		return this;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	public String toString() {
		return this.writer.toString();
	}

	@Override
	public void setPacketSize(int size) {
		this.packetSize = size;
	}

	@Override
	public int getPacketSize() {
		return packetSize;
	}
}
