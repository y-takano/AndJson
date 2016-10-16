package jp.gr.java_conf.ke.json.stream.parse;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import jp.gr.java_conf.ke.json.core.BufferedTextReader;
import jp.gr.java_conf.ke.json.stream.IOStreamingException;
import jp.gr.java_conf.ke.json.stream.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.Json.Token;

class Context implements Iterator<Token> {

	private final BufferedTextReader reader;
	private final Queue<Token> tokenQueue;
	private final VisitorsContext context;

	private char[] currentBuffer;
	private char[] nextBuffer;
	private int currentIndex;

	public Context(BufferedTextReader reader) {
		this.reader = reader;
		this.tokenQueue = new LinkedList<Token>();
		this.context = new VisitorsContext();
	}

	@Override
	public boolean hasNext() {
		// packet分先読み
		if (tokenQueue.size() == 0) {
			postToken();
		}
		return !tokenQueue.isEmpty();
	}

	@Override
	public Token next() {
		return hasNext() ? tokenQueue.remove() : null;
	}

	char read() {
		return currentBuffer[currentIndex];
	}

	char readForward(int index) {
		int pos = currentIndex + index;
		if (pos < currentBuffer.length) {
			return currentBuffer[pos];
		}

		// 先読み
		int nextPos = pos - currentBuffer.length;
		if (nextBuffer == null) {
			nextBuffer = reader.readPacket();
		}
		if (nextPos >= nextBuffer.length) {
			close();
			return (char)-1;
		}
		return nextBuffer[nextPos];
	}

	void skip(int index) {
		currentIndex += index;
	}

	VisitorsContext getContext() {
		return context;
	}

	void close() {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOStreamingException(e);
		}
	}

	boolean isClosed() {
		return reader.isClosed();
	}

	private void postToken() throws JsonSyntaxException, IOStreamingException {

		if (nextBuffer == null) {
			currentBuffer = reader.readPacket();
		} else {
			currentBuffer = nextBuffer;
			nextBuffer = null;
		}

		// テキストを読み込む
		currentIndex = 0;
		while (currentIndex < currentBuffer.length) {

			// visitorに処理を委譲
			context.getCurrentVisitor().visit(this, tokenQueue);

			// 解析終了
			if (context.isEnd()) {
				close();
				break;
			}

			currentIndex++;
		}
	}
}
