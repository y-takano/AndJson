package jp.gr.java_conf.ke.json.core;

import java.io.CharArrayReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Deprecated
class InputStreamDelegater implements Closeable {

	private Reader reader;
	private BufferControler buf;
	private BufferPacket packet;

	InputStreamDelegater(String str) {
		reader = new CharArrayReader(str.toCharArray());
		buf = new BufferControler(this);
		packet = buf.usePacket();
	}

	public InputStreamDelegater(InputStream is) {
		reader = new InputStreamReader(is);
		buf = new BufferControler(this);
		packet = buf.usePacket();
	}

	public InputStreamDelegater(Reader reader) {
		this.reader = reader;
		buf = new BufferControler(this);
		packet = buf.usePacket();
	}

	public char read() throws IOException {
		char ret = packet.read();
		if (ret == -1) {
			buf.releasePacket(packet);
			packet = buf.usePacket();
			ret = packet.read();
			if (ret == -1) {
				close();
			}
		}
		return ret;
	}

	int read(char[] buffer) throws IOException {
		return reader.read(buffer);
	}

	@Override
	public void close() throws IOException {
		reader.close();
	}

	public String toString() {
		return buf.toString();
	}
}
