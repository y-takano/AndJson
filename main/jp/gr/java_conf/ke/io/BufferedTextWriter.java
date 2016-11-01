package jp.gr.java_conf.ke.io;

import java.io.Flushable;

public interface BufferedTextWriter extends BufferedIO, Flushable {

	BufferedTextWriter append(char c);

	boolean isClosed();
}