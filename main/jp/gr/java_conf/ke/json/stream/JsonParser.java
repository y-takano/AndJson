package jp.gr.java_conf.ke.json.stream;

import java.io.Closeable;
import java.io.IOException;

import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;

public interface JsonParser extends Iterable<Token>, Closeable {

	boolean hasNext() throws IOException;

	Token current();

	Token next() throws IOException;

	Token nextAnyName();

	Token nextName(String name);

	Token nextAnyValue();

	Token nextSymbol(Symbol symbol);

	Token nextAnySymbol();
}
