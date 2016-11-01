package jp.gr.java_conf.ke.json.stream.parse;

class ParseSpec {

	protected static final char OBJ_START = '{';
	protected static final char OBJ_END = '}';
	protected static final char ARRAY_START = '[';
	protected static final char ARRAY_END = ']';
	protected static final char SPACE = ' ';
	protected static final char DEMILITOR = ':';
	protected static final char SEPARATOR = ',';
	protected static final char STR_START = '"';
	protected static final char STR_END = '"';

	private static final char[] WS = {
		' ', '\t', '\r', '\n',
	};

	private static final char[] NUM = {
		'1', '2', '3', '4','5', '6', '7', '8', '9', '0',
		'-', 'E',
	};

	protected static boolean isWhiteSpace(char c) {
		for (char ch : WS) {
			if (c == ch) return true;
		}
		return false;
	}

	protected static boolean isNumber(char c) {
		for (char ch : NUM) {
			if (c == ch) return true;
		}
		return false;
	}







}
