package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.util.Queue;

import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.json.ValueType;
import jp.gr.java_conf.ke.json.stream.parse.VisitorStack.State;

abstract class Visitor {

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

	private final char END = endSymbol();
	private final State DEFAULT_STATE = defaultState();

	protected StringBuilder sb;

	private Context ctxt;
	private VisitorStack stack;
	private Queue<Token> tokenQueue;

	abstract protected char endSymbol() ;
	abstract protected State defaultState() ;
	abstract protected void doVisit(char c) throws JsonSyntaxException, IOException;

	public void visit(Context ctxt, Queue<Token> tokenQueue) throws JsonSyntaxException, IOException {
		this.ctxt = ctxt;
		this.stack = ctxt.getStack();
		this.tokenQueue = tokenQueue;

		int i = 0;
		char c;
		int cnt = tokenQueue.size();
		for (c = ctxt.read(); !ctxt.isClosed(); c = ctxt.readForward(i + 1), i++) {
			stack.setEscape(c == '\\');
			doVisit(c);
			if (cnt < tokenQueue.size()) break;
		}
		if (0 < i) ctxt.skip(i);
	}

	protected void findValue(char c) throws IOException {
		switch (c) {

		case STR_START:
			setState(State.VALUE_STRING);
			sb = newBuilder();
			break;

		case OBJ_START:
			startObject(Token.createSymbol(Symbol.valueOf(c)));
			break;

		case ARRAY_START:
			startArray(Token.createSymbol(Symbol.valueOf(c)));
			break;

		case OBJ_END:
		case ARRAY_END:
			if (c == END) {
				endElement(Token.createSymbol(Symbol.valueOf(c)));
				return;
			}
			throwException("構文エラー:開かれていないオブジェクトが閉じられています: " + c);

		default:
			if (isWhiteSpace(c) || isLiteral(c)) return;
			else if (isNumber(c)){
				setState(State.VALUE_NUMBER);
				sb = newBuilder();
				sb.append(c);
			}
			break;
		}
	}

	protected void buildNumber(char c) {
		if (c == END) {
			offerToken(Token.createValue(ValueType.NUMBER, sb));
			endElement(Token.createSymbol(Symbol.valueOf(c)));
		} else if (c == SEPARATOR) {
			offerToken(Token.createValue(ValueType.NUMBER, sb));
			setState(DEFAULT_STATE);
			sb = newBuilder();
		} else {
			sb.append(c);
		}
	}

	protected void buildStringValue(char c) {
		if (c == STR_END) {
			setState(State.FIND_NEXT);
			offerToken(Token.createValue(ValueType.STRING, sb));
		} else if (c == SEPARATOR) {
			setState(DEFAULT_STATE);
		} else {
			sb.append(c);
		}
	}

	protected void buildStringName(char c) {
		if (c == STR_END) {
			setState(State.FIND_NEXT);
			offerToken(Token.createName(sb));
		} else if (c == SEPARATOR) {
			setState(DEFAULT_STATE);
		} else {
			sb.append(c);
		}
	}

	protected boolean isLiteral(char c) throws IOException {
		boolean ret = false;
		if (stack.isEscape()) return ret;

		if (c == 'n' && readForward(2) == 'u' && readForward(3) == 'l' && readForward(4) == 'l') {
			offerToken(Token.createNullValue());
			skip(3);
			ret = true;

		} else if (c == 't' && readForward(2) == 'r' && readForward(3) == 'u' && readForward(4) == 'e') {
			offerToken(Token.createValue(ValueType.BOOLEAN, new StringBuilder("true")));
			skip(3);
			ret = true;

		} else if (c == 'f' && readForward(2) == 'a' && readForward(3) == 'l' && readForward(4) == 's' && readForward(5) == 'e') {
			offerToken(Token.createValue(ValueType.BOOLEAN, new StringBuilder("false")));
			skip(4);
			ret = true;
		}
		return ret;
	}

	protected boolean isWhiteSpace(char c) {
		for (char ch : WS) {
			if (c == ch) return true;
		}
		return false;
	}

	protected boolean isNumber(char c) {
		for (char ch : NUM) {
			if (c == ch) return true;
		}
		return false;
	}

	protected State getState() {
		return this.stack.getState();
	}

	protected void setState(State state) {
		this.stack.setState(state);
	}

	protected void offerToken(Token token) {
		this.tokenQueue.offer(token);
	}

	protected char readForward(int index) throws IOException {
		return this.ctxt.readForward(index);
	}

	protected void startObject(Token token) {
		stack.startObject();
		stack.setState(State.FIND_NAME);
		offerToken(token);
	}

	protected void startArray(Token token) {
		stack.startArray();
		stack.setState(State.FIND_VALUE);
		offerToken(token);
	}

	protected void endElement(Token token) {
		stack.endElement();
		offerToken(token);
	}

	protected void skip(int index) {
		ctxt.skip(index);
	}

	protected StringBuilder newBuilder() {
		return new StringBuilder(30);
	}

	protected void throwException(String msg) throws JsonSyntaxException {
		throw new JsonSyntaxException(msg);
	}
}
