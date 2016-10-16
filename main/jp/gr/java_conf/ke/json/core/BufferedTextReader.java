package jp.gr.java_conf.ke.json.core;

public interface BufferedTextReader extends BufferdIO {

	char read();

	char[] readPacket();

	boolean hasNext();
}
