package jp.gr.java_conf.ke.json.core;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

@Deprecated
class OutputStreamDelegater implements Closeable, Flushable {

	private Writer out;
	private BufferControler buf;

	OutputStreamDelegater() {
		this.out = null;
		this.buf = new BufferControler(this);
	}

	OutputStreamDelegater(Writer writer) {
		this.out = writer;
		this.buf = new BufferControler(this);
	}

	OutputStreamDelegater(OutputStream out) {
		this.out = new OutputStreamWriter(out);
		this.buf = new BufferControler(this);
	}

	public void write(char data) {
		buf.write(data);
	}

	public void write(char[] data) {
		for (char c : data) write(c);
	}

	public void write(CharSequence data) {
		for (int i=0; i<data.length(); i++) write(data.charAt(i));
	}

	void flush(char[] data) throws IOException {
		out.write(data);
		out.flush();
	}

	@Override
	public void flush() throws IOException {
		if (out != null) {
			buf.flush();
		}
	}

	@Override
	public void close() throws IOException {
		if (out != null) {
			out.close();
		}
	}

	@Override
	public String toString() {
		return buf.toString();
	}
}
