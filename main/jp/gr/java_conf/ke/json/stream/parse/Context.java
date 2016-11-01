package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.IOStreamingException;
import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.Token;

class Context implements Iterator<Token> {

	private final BufferedTextReader reader;
	private final Queue<Token> tokenQueue;
	private final VisitorStack stack;

	public Context(BufferedTextReader reader) {
		this.reader = reader;
		this.tokenQueue = new LinkedList<Token>();
		this.stack = new VisitorStack();
	}

	@Override
	public boolean hasNext() {
		if (tokenQueue.isEmpty()) {
			postToken();
		}
		return !tokenQueue.isEmpty();
	}

	@Override
	public Token next() {
		return hasNext() ? tokenQueue.remove() : null;
	}

	void close() {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOStreamingException(e);
		}
	}

	private void postToken() throws JsonSyntaxException, IOStreamingException {

		try {
			while (reader.ready()) {

				if (stack.isEmpty()) {
					try {
						reader.close();
					} catch (IOException e) {
						throw new IOStreamingException(e);
					}
					break;
				}

				Visitor visitor = stack.getCurrentVisitor();
				char c = reader.read();
				visitor.visit(c, stack, tokenQueue);

				if (!tokenQueue.isEmpty()) break;
			}
		} catch (IOException e) {
			throw new IOStreamingException(e);
		}
	}
}
