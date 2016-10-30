package jp.gr.java_conf.ke.io;

import java.io.IOException;

public interface BufferedTextReader extends BufferedIO {

	char read() throws IOException;

	char[] readPacket() throws IOException;
}
