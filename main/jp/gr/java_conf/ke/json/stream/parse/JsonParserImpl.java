package jp.gr.java_conf.ke.json.stream.parse;

import java.util.Iterator;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.IOStreamingException;
import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.json.stream.JsonParser;

class JsonParserImpl implements JsonParser {

	private final Context context;
	private Token currentToken;

	public JsonParserImpl(BufferedTextReader reader) {
		this.context = new Context(reader);
	}

	@Override
	public Iterator<Token> iterator() {
		return context;
	}

	@Override
	public void close() throws IOStreamingException {
		context.close();
	}

	@Override
	public boolean hasNext() {
		return context.hasNext();
	}

	@Override
	public Token current() {
		return currentToken;
	}

	@Override
	public Token next() {
		if (currentToken != null) Token.releaseToken(currentToken);
		currentToken = context.next();
		return currentToken;
	}

	@Override
	public Token nextAnyName() {
		Token t;
		while (hasNext()) if ((t = next()).isName()) return t;
		return null;
	}

	@Override
	public Token nextName(String name) {
		Token t;
		while (hasNext()) if ((t = next()).isName() && t.getName().equals(name)) return t;
		return null;
	}

	@Override
	public Token nextAnyValue() {
		Token t;
		while (hasNext()) if ((t = next()).isValue()) return t;
		return null;
	}

	@Override
	public Token nextSymbol(Symbol symbol) {
		Token t;
		while (hasNext()) if ((t = next()).isSymbol() && t.getSymbol().equals(symbol)) return t;
		return null;
	}

	@Override
	public Token nextAnySymbol() {
		Token t;
		while (hasNext()) if ((t = next()).isSymbol()) return t;
		return null;
	}

}
