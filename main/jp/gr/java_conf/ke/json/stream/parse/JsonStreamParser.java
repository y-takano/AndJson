package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.util.Iterator;

import jp.gr.java_conf.ke.json.core.BufferedTextReader;
import jp.gr.java_conf.ke.json.stream.JsonParser;
import jp.gr.java_conf.ke.json.stream.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.Json.Token;

class JsonStreamParser {
//class JsonStreamParser implements JsonParser {
//
//	private final JsonTokenMediator mediator;
//
//	public JsonStreamParser(BufferedTextReader in) {
//		this.mediator = new JsonTokenMediator(in);
//	}
//
//	@Override
//	public Iterator<Token> iterator() {
//		return mediator;
//	}
//
//	@Override
//	public void close() throws IOException {
//		mediator.close();
//	}
//
//	@Override
//	public boolean hasNext() {
//		return mediator.hasNext();
//	}
//
//	@Override
//	public Token current() {
//		return mediator.current();
//	}
//
//	@Override
//	public Token next() throws JsonSyntaxException {
//		return mediator.next();
//	}
//
//	@Override
//	public Token nextAnyName() {
//		Token t;
//		while (hasNext()) if ((t = next()).isName()) return t;
//		return null;
//	}
//
//	@Override
//	public Token nextName(String name) {
//		Token t;
//		while (hasNext()) if ((t = next()).isName() && t.getName().equals(name)) return t;
//		return null;
//	}
//
//	@Override
//	public Token nextAnyValue() {
//		Token t;
//		while (hasNext()) if ((t = next()).isValue()) return t;
//		return null;
//	}
//
//	@Override
//	public Token nextSymbol(Symbol symbol) {
//		Token t;
//		while (hasNext()) if ((t = next()).isSymbol() && t.getSymbol().equals(symbol)) return t;
//		return null;
//	}
//
//	@Override
//	public Token nextAnySymbol() {
//		Token t;
//		while (hasNext()) if ((t = next()).isSymbol()) return t;
//		return null;
//	}
//
}
