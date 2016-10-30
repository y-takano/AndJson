package jp.gr.java_conf.ke.json;

public enum Symbol {
	UNDEFINED((char) -1),
	OBJ_START('{'),
	OBJ_END('}'),
	ARRAY_START('['),
	ARRAY_END(']'),
	;

	private char c;

	private Symbol(char c) {
		this.c = c;
	}

	public boolean is(char c) {
		return this.c == c;
	}

	public boolean is(String s) {
		return s != null ? s.equals(s) : false;
	}

	public char asChar() {
		return c;
	}

	public String toString() {
		return String.valueOf(c);
	}

	public static Symbol valueOf(char c) {
		for (Symbol s : values())
			if (s.is(c))
				return s;
		return UNDEFINED;
	}
}
