package jp.gr.java_conf.ke.json.core;

interface CyclicReader {

	boolean isLimit();

	char next();

	char[] read();
}
