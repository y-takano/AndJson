package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.util.Iterator;

import jp.gr.java_conf.ke.io.BufferedTextReader;
import jp.gr.java_conf.ke.io.IOStreamingException;
import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.json.TokenRecycler;
import jp.gr.java_conf.ke.util.collection.BlockingQueue;
import jp.gr.java_conf.ke.util.collection.Queue;

class Context implements Iterator<Token> {

	private final BufferedTextReader reader;
	private final Queue<Token> tokenQueue;
	private final VisitorStack stack;

	public Context(BufferedTextReader reader) {
		this.reader = reader;
		this.tokenQueue = new BlockingQueue<Token>();
		this.stack = new VisitorStack();
		TokenRecycler.initialize();
	}

	@Override
	public boolean hasNext() {
		if (tokenQueue.isEmpty()) {
			postToken();
		}
		boolean ret = !tokenQueue.isEmpty();
		if (!ret) {
			close();
		}
		return ret;
	}

	@Override
	public Token next() {
		return hasNext() ? tokenQueue.take() : null;
	}

	void close() {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOStreamingException(e);
		} finally {
			TokenRecycler.releaseLock();
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

	@Override
	public void remove() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
