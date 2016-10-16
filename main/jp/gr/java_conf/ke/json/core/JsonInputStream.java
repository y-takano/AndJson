package jp.gr.java_conf.ke.json.core;

import java.io.IOException;

import jp.gr.java_conf.ke.json.stream.IOStreamingException;

@Deprecated
class JsonInputStream implements BufferedTextReader {

	private InputStreamDelegater delegater;

	private boolean eof = false;
	private boolean closed = false;

	public JsonInputStream(InputStreamDelegater delegater) {
		this.delegater = delegater;
	}

	@Override
	public void close() throws IOException {
		delegater.close();
		closed = true;
	}

	@Override
	public char read() {
		char ret;
		try {
			ret = delegater.read();
		} catch (IOException e) {
			throw new IOStreamingException(e);
		}
		if ((char) -1 == ret) {
			eof = true;
		}
		return ret;
	}

	@Override
	public boolean hasNext() {
		return !closed && !eof;
	}

	@Override
	public char[] readPacket() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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

	@Override
	public boolean isClosed() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

}
