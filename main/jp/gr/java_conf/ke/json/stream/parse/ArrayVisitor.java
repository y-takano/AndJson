package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;

import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;

import static jp.gr.java_conf.ke.json.stream.parse.VisitorStack.State;
import static jp.gr.java_conf.ke.json.stream.parse.ParseSpec.*;

class ArrayVisitor  extends Visitor {

	@Override
	protected char endSymbol() {
		return ARRAY_END;
	}

	@Override
	protected State defaultState() {
		return State.FIND_VALUE;
	}

	@Override
	public void doVisit(char c) throws JsonSyntaxException, IOException {

		switch (getState()) {

		case FIND_VALUE:
			findValue(c);
			break;

		case VALUE_NUMBER:
			buildNumber(c);
			break;

		case VALUE_STRING:
			buildStringValue(c);
			break;

		case FIND_NEXT:
			findNext(c);
			break;

		case FIND_NAME:
		case END:
		default:
			throwException("構文エラー: [" + c + "] State=" + getState());
		}
	}

	private void findNext(char c) {
		if (c == ARRAY_END) {
			endElement(Token.createSymbol(Symbol.valueOf(c)));

		} else if (c == SEPARATOR) {
			setState(State.FIND_VALUE);
			sb = newBuilder();
		}
	}
}
