package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;

import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.json.ValueType;
import jp.gr.java_conf.ke.util.collection.Queue;
import static jp.gr.java_conf.ke.json.stream.parse.VisitorStack.State;
import static jp.gr.java_conf.ke.json.stream.parse.ParseSpec.*;

abstract class Visitor {

	private final char END = endSymbol();
	private final State DEFAULT_STATE = defaultState();

	protected StringBuffer sb;
	protected StringBuffer literal;

	private VisitorStack stack;
	private Queue<Token> tokenQueue;
	private boolean escape;

	abstract protected char endSymbol() ;
	abstract protected State defaultState() ;
	abstract protected void doVisit(char c) throws JsonSyntaxException, IOException;

//	public void visit(Context ctxt, Queue<Token> tokenQueue) throws JsonSyntaxException, IOException {
//		this.tokenQueue = tokenQueue;
//
//		int i = 0;
//		char c;
//		int cnt = tokenQueue.size();
//		for (c = ctxt.read(); !ctxt.isClosed(); c = ctxt.readForward(i + 1), i++) {
//
//			escape = c == '\\';
//			doVisit(c);
//			if (cnt < tokenQueue.size()) break;
//		}
//		if (0 < i) ctxt.skip(i);
//	}

	public void visit(char c, VisitorStack stack, Queue<Token> tokenQueue) throws JsonSyntaxException, IOException {
		this.stack = stack;
		this.tokenQueue = tokenQueue;
		escape = c == '\\';
		doVisit(c);
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

	protected State getState() {
		return stack.getState();
	}

	protected void setState(State state) {
		this.stack.setState(state);
	}

	protected void offerToken(Token token) {
		this.tokenQueue.offer(token);
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

	protected StringBuffer newBuilder() {
		return new StringBuffer(30);
	}

	protected void throwException(String msg) throws JsonSyntaxException {
		throw new JsonSyntaxException(msg);
	}

	private boolean isLiteral(char c) throws IOException {
		boolean ret = false;
		if (escape) return ret;

		if (literal == null) {
			literal = new StringBuffer(4);
			literal.append(c);
			return ret;
		}

		int len = literal.length();

		if (len == 3) {

			if ('l' == c && literal.charAt(0) == 'n' && literal.charAt(1) == 'u' && literal.charAt(2) == 'l') {
				offerToken(Token.createNullValue());
				ret = true;

			} else if ('e' == c && literal.charAt(0) == 't' && literal.charAt(1) == 'r' && literal.charAt(2) == 'u') {
				offerToken(Token.createValue(ValueType.BOOLEAN, new StringBuilder("true")));
				ret = true;
			}

		} else if (len == 4) {

			if ('e' == c && literal.charAt(0) == 'f' && literal.charAt(1) == 'a' && literal.charAt(2) == 'l' && literal.charAt(3) == 's') {
				offerToken(Token.createValue(ValueType.BOOLEAN, new StringBuilder("false")));
				ret = true;
			}
		}

		if (ret)
			literal = null;
		else if (len < 5)
			literal.append(c);
		else if (len == 5)
			literal = null;

		return ret;
	}

}
