package jp.gr.java_conf.ke.io.core;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import jp.gr.java_conf.ke.io.BufferedTextWriter;

class BufferedWriterImpl extends BufferControler implements BufferedTextWriter {

	public BufferedWriterImpl() {
		super(null, new StringWriter());
	}

	public BufferedWriterImpl(Writer writer) {
		super(null, writer);
	}

	public BufferedWriterImpl(OutputStream os) {
		super(null, new OutputStreamWriter(os));
	}

	@Override
	public BufferedTextWriter append(char c) {
		write(c);
		return this;
	}

}
