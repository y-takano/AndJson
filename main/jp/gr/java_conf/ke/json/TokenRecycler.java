package jp.gr.java_conf.ke.json;

import java.util.LinkedList;

class TokenRecycler {

	private static LinkedList<Token> TOKEN_POOL = new LinkedList<Token>();

	static {
		extend();
	}

	public static Token getToken() {
		if (TOKEN_POOL.size() == 0) {
			extend();
		}
		return TOKEN_POOL.remove();
	}

	private static void extend() {
		for (int i=0; i<10000; i++) TOKEN_POOL.add(new Token());
	}

	public static void offer(Token token) {
		TOKEN_POOL.add(token);
	}
}
