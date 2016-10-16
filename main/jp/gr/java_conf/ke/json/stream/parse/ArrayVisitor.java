package jp.gr.java_conf.ke.json.stream.parse;

import jp.gr.java_conf.ke.json.stream.Json;
import jp.gr.java_conf.ke.json.stream.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.parse.VisitorsContext.State;

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
	public void doVisit(char c) throws JsonSyntaxException {

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
			throwException("構文エラー: [" + c + "], State=" + getState());
		}
	}

	private void findNext(char c) {
		if (c == ARRAY_END) {
			endElement(Json.createSymbol(Symbol.valueOf(c)));

		} else if (c == SEPARATOR) {
			setState(State.FIND_VALUE);
			sb = newBuilder();
		}
	}
}
