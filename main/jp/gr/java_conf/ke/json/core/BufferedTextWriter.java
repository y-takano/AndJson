package jp.gr.java_conf.ke.json.core;

import java.io.Flushable;

public interface BufferedTextWriter extends BufferdIO, Flushable {

	BufferedTextWriter write(char c);

	BufferedTextWriter write(char[] str);

	BufferedTextWriter write(CharSequence str);

	boolean isClosed();
}