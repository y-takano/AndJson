package jp.gr.java_conf.ke.io;

import java.io.Flushable;

public interface BufferedTextWriter extends BufferedIO, Flushable {

	BufferedTextWriter write(char c);

	BufferedTextWriter write(char[] str);

	BufferedTextWriter write(CharSequence str);

	boolean isClosed();
}