package jp.gr.java_conf.ke.json.core;

import java.io.IOException;

import jp.gr.java_conf.ke.json.stream.IOStreamingException;

@Deprecated
class JsonOutputStream implements BufferedTextWriter {

	private OutputStreamDelegater delegater;
	private boolean closed = false;

	public JsonOutputStream(OutputStreamDelegater delegater) {
		this.delegater = delegater;
	}

	@Override
	public void close() throws IOException {
		delegater.close();
		closed = true;
	}

	@Override
	public void flush() throws IOException {
		delegater.flush();
	}

	@Override
	public BufferedTextWriter write(char c) throws IOStreamingException {
		delegater.write(c);
		return this;
	}

	@Override
	public BufferedTextWriter write(char[] str) throws IOStreamingException {
		delegater.write(str);
		return this;
	}

	@Override
	public BufferedTextWriter write(CharSequence str) throws IOStreamingException {
		delegater.write(str);
		return this;
	}

	@Override
	public boolean isClosed() {
		return closed;
	}

	@Override
	public String toString() {
		return delegater.toString();
	}

	@Override
	public void setPacketSize(int size) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public int getPacketSize() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

}
