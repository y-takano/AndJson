package jp.gr.java_conf.ke.io;

import java.io.Closeable;
import java.io.IOException;

public abstract interface BufferedIO extends Closeable {

	int DEFAULT_PACKET_SIZE = 1024;

	//void setPacketSize(int size);

	int getPacketSize();

	boolean isClosed();

	boolean ready() throws IOException;
}
