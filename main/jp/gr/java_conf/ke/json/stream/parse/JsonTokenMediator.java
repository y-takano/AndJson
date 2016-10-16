package jp.gr.java_conf.ke.json.stream.parse;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import jp.gr.java_conf.ke.json.core.BufferedTextReader;
import jp.gr.java_conf.ke.json.stream.IOStreamingException;
import jp.gr.java_conf.ke.json.stream.Json;
import jp.gr.java_conf.ke.json.stream.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.Json.Token;

class JsonTokenMediator {
//class JsonTokenMediator implements Iterator<Token>, Closeable, VisitorHelper {
//
//	private final BufferedTextReader reader;
//	private final JsonTokenContext ctxt;
//
//	private final Queue<Token> tokenQueue;
//
//	private Token current;
//	private char[] currentBuffer;
//	private char[] nextBuffer;
//	private int currentIndex;
//
//	public JsonTokenMediator(BufferedTextReader reader) {
//		this.ctxt = new JsonTokenContext(this);
//		this.tokenQueue = new LinkedList<Token>();
//		this.reader = reader;
//	}
//
//	@Override
//	public boolean hasNext() {
//
//		// packet分先読み
//		if (tokenQueue.size() == 0) {
//			postToken();
//		}
//
//		return !tokenQueue.isEmpty();
//	}
//
//	@Override
//	public Token next() {
//		Json.releaseToken(current);
//		if (hasNext()) {
//			current = tokenQueue.remove();
//			return current;
//		}
//		return null;
//	}
//
//	public Token current() {
//		return current;
//	}
//
//	@Override
//	public void close() throws IOException {
//		for (Token t : tokenQueue) Json.releaseToken(t);
//		reader.close();
//	}
//
//	@Override
//	public char readForward(int plus) {
//
//		int pos = currentIndex + plus;
//		if (pos <= currentBuffer.length) {
//			return currentBuffer[pos];
//		}
//
//		// 先読み
//		int nextPos = pos - currentBuffer.length - 1;
//		if (nextBuffer == null) {
//			nextBuffer = reader.readPacket();
//		}
//		return nextBuffer[nextPos];
//	}
//
//	@Override
//	public void offer(Token token) {
//		tokenQueue.offer(token);
//	}
//
//	@Override
//	public void notifyEnd() {
//		try {
//			reader.close();
//		} catch (IOException e) {
//			throw new IOStreamingException(e);
//		}
//	}
//
//	//
//	private void postToken() throws JsonSyntaxException, IOStreamingException {
//
//		if (nextBuffer == null) {
//			currentBuffer = reader.readPacket();
//		} else {
//			currentBuffer = nextBuffer;
//			nextBuffer = null;
//		}
//
//		// テキストを読み込む
//		char c;
//		currentIndex = 0;
//		while (currentIndex < currentBuffer.length) {
//
//			// EOF
//			c = currentBuffer[currentIndex];
//			if (c == (char) 0 || c == (char) -1) break;
//
//			// visitorに処理を委譲
//			ctxt.accept(c);
//
//			currentIndex++;
//		}
//	}
//
//	@Override
//	public void skip(int index) {
//		currentIndex+=index;
//	}
}
