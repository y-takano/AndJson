package jp.gr.java_conf.ke.json.stream.parse;

import jp.gr.java_conf.ke.json.stream.Json;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.parse.VisitorsContext.State;

class ObjectVisitor extends Visitor {

	@Override
	protected char endSymbol() {
		return OBJ_END;
	}

	@Override
	protected State defaultState() {
		return State.FIND_NAME;
	}

	@Override
	public void doVisit(char c) {

		switch (getState()) {

		case FIND_NAME:
			findName(c);
			break;

		case NAME:
			buildStringName(c);
			break;

		case FIND_VALUE:
			findValue(c);
			break;

		case VALUE_STRING:
			buildStringValue(c);
			break;

		case VALUE_NUMBER:
			buildNumber(c);
			break;

		case FIND_NEXT:
			findNext(c);
			break;

		case END:
		default:
			throwException("構文エラー: [" + c + "], State=" + getState());
		}
	}

	private void findName(char c) {
		if (c == STR_START) {
			setState(State.NAME);
			sb = newBuilder();

		} else if (c == OBJ_END) {
			endElement(Json.createSymbol(Symbol.valueOf(c)));
		}
	}

	private void findNext(char c) {
		if (isWhiteSpace(c)) return;
		else if (c == OBJ_END) {
			endElement(Json.createSymbol(Symbol.valueOf(c)));

		} else if (c == SEPARATOR) {
			setState(State.FIND_NAME);
			sb = newBuilder();

		} else if (c == DEMILITOR) {
			setState(State.FIND_VALUE);
			sb = newBuilder();
		}
	}

}
