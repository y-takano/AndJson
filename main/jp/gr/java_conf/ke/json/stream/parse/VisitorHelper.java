package jp.gr.java_conf.ke.json.stream.parse;

import jp.gr.java_conf.ke.json.stream.Json.Token;

interface VisitorHelper {

	char readForward(int index);

	void skip(int index);

	void offer(Token token);

	void notifyEnd();
}
