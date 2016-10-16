package jp.gr.java_conf.ke.json.core;

import java.io.IOException;
import java.io.Writer;

interface CyclicWriter {

	boolean write(char c);

	void flush(Writer w) throws IOException;
}
