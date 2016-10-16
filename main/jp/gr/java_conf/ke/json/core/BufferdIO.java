package jp.gr.java_conf.ke.json.core;

import java.io.Closeable;

public abstract interface BufferdIO extends Closeable {

	int DEFAULT_PACKET_SIZE = 512;

	void setPacketSize(int size);

	int getPacketSize();

	boolean isClosed();
}
