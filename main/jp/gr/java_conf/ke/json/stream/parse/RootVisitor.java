package jp.gr.java_conf.ke.json.stream.parse;

import jp.gr.java_conf.ke.json.stream.Json;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.parse.VisitorsContext.State;

class RootVisitor extends Visitor {

	@Override
	public void doVisit(char c) {

		if (OBJ_START == c) {
			startObject(Json.createSymbol(Symbol.valueOf(c)));

		} else if (ARRAY_START == c) {
			startArray(Json.createSymbol(Symbol.valueOf(c)));

		} else if (OBJ_END == c || ARRAY_END == c) {
			endElement(Json.createSymbol(Symbol.valueOf(c)));
		}
	}

	@Override
	protected char endSymbol() {
		return 0;
	}

	@Override
	protected State defaultState() {
		return null;
	}
}
