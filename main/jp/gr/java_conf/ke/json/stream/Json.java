package jp.gr.java_conf.ke.json.stream;

import java.util.LinkedList;
import java.util.Queue;

public class Json {

	private static final TokenRecycler TOKEN_RECYCLER = new TokenRecycler();

	private static class TokenRecycler extends LinkedList<Token> {

		public TokenRecycler() {
			this.extend();
		}

		public Token remove() {
			if (this.size() == 0) {
				this.extend();
			}
			return super.remove();
		}

		private void extend() {
			for (int i=0; i<10000; i++) this.add(new Token());
		}
	}

	private Json() {}

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

	public enum ValueType {
		STRING, NUMBER, BOOLEAN, NULL,
	}

	public static Token createSymbol(Symbol symbol) {
		Token token = TOKEN_RECYCLER.remove();
		token.symbol = symbol;
		token.name = null;
		return token;
	}

	public static Token createName(StringBuilder name) {
		Token token = TOKEN_RECYCLER.remove();
		token.symbol = Symbol.UNDEFINED;
		char n[] = new char[name.length()];
		name.getChars(0, n.length, n, 0);
		token.name = n;
		return token;
	}

	private static final char[] NULLVALUE = {'n','u','l','l'};

	public static Token createNullValue() {
		final Token token = TOKEN_RECYCLER.remove();
		token.symbol = Symbol.UNDEFINED;
		token.name = null;
		Value value = token.value;
		value.type = ValueType.NULL;
		value.text = null;
		return token;
	}

	public static Token createValue(ValueType type, StringBuilder text) {
		final Token token = TOKEN_RECYCLER.remove();
		token.symbol = Symbol.UNDEFINED;
		token.name = null;
		Value value = token.value;
		char t[] = new char[text.length()];
		text.getChars(0, t.length, t, 0);
		value.type = type;
		value.text = value.isNull() ? NULLVALUE : t;
		return token;
	}

	public static void releaseToken(Token token) {
		TOKEN_RECYCLER.offer(token);
	}

	public static class Token {

		private Symbol symbol;
		private char[] name;
		private Value value;

		private Token() {
			this.symbol = null;
			this.name = null;
			this.value = new Value();
		}

		public Symbol getSymbol() {
			return symbol;
		}

		public boolean isName() {
			return name != null;
		}

		public boolean isValue() {
			return !isName() && !isSymbol();
		}

		public boolean isSymbol() {
			return !symbol.equals(Symbol.UNDEFINED);
		}

		public boolean isThat(Symbol symbol) {
			return this.symbol.equals(symbol);
		}

		public String getName() {
			return name != null ? name.toString() : null;
		}

		public Value getValue() {
			return value;
		}

		public String toString() {
			return symbol != Symbol.UNDEFINED ? "SYMBOL:" + symbol
					: isValue() ? value.toString() : "NAME  :\"" + new String(name) + "\"";
		}
	}

	public static class Value {

		private ValueType type;
		private char[] text;

		private Value() {}

		public boolean isLiteral() {
			return type == ValueType.NULL || type == ValueType.BOOLEAN;
		}

		public boolean isBoolean() {
			return type == ValueType.BOOLEAN;
		}

		public boolean isNull() {
			return type == ValueType.NULL;
		}

		public boolean getBoolean() {
			return isBoolean() ? Boolean.valueOf(getText()) : false;
		}

		public String getText() {
			return new String(text);
		}

		public String toString() {
			if (type.equals(ValueType.STRING)) return type + ":\"" + getText() + "\"";
			else return type + ":" + getText();
		}
	}
}
