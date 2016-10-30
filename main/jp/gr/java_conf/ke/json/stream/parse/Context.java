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

	private char[] currentBuffer;
	private char[] nextBuffer;
	private int currentIndex;
	private int nextBufferIndex;

	public Context(BufferedTextReader reader) {
		this.reader = reader;
		this.tokenQueue = new LinkedList<Token>();
		this.stack = new VisitorStack();
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

	char readForward(int index) throws IOException {
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
			return (char) -1;
		}
		return nextBuffer[nextPos];
	}

	void skip(int index) {
		currentIndex += index;
	}

	VisitorStack getStack() {
		return stack;
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

		try {
			if (nextBuffer == null) {
				currentBuffer = reader.readPacket();
				currentIndex = 0;
			} else {
				currentIndex = nextBufferIndex;
				nextBufferIndex = 0;
				currentBuffer = nextBuffer;
				nextBuffer = null;
			}

			// テキストを読み込む
			while (currentIndex < currentBuffer.length) {

				// visitorに処理を委譲
				stack.getCurrentVisitor().visit(this, tokenQueue);

				// 解析終了
				if (stack.isEmpty()) {
					close();
					break;
				}

				currentIndex++;
			}

			if (reader.getPacketSize() < currentIndex) {

				nextBufferIndex = currentIndex - reader.getPacketSize() -1;
			}
		} catch (Exception e) {
			throw new IOStreamingException(e);
		}
	}

	@Override
	public void remove() {
		// TODO 自動生成されたメソッド・スタブ

	}
}
