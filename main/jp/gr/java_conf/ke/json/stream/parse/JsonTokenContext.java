package jp.gr.java_conf.ke.json.stream.parse;

import java.util.ArrayDeque;
import java.util.Deque;

import jp.gr.java_conf.ke.json.stream.JsonSyntaxException;

class JsonTokenContext {
//
//	enum State {
//		FIND_NAME,
//		FIND_VALUE,
//		NAME,
//		VALUE_NUMBER,
//		VALUE_STRING,
//		FIND_NEXT,
//		END,
//	}
//
//	private final Deque<Visitor> visitors;
//	private Visitor current;
//
//	private State state = null;
//	private boolean escape;
//
//	private VisitorHelper helper;
//
//	public JsonTokenContext(VisitorHelper helper) {
//		visitors = new ArrayDeque<Visitor>();
//		current = Visitor.newRoot(this);
//		this.helper = helper;
//	}
//
//	public void accept(char c) throws JsonSyntaxException {
//		current.visit(c);
//	}
//
//	public void setEscape(boolean escape) {
//		this.escape = escape;
//	}
//
//	public boolean isEscape() {
//		return escape;
//	}
//
//	public State getState() {
//		return state;
//	}
//
//	public void setState(State state) {
//		this.state = state;
//	}
//
//	public void startArray() {
//		visitors.push(current);
//		current = Visitor.newArray(this);
//		state = State.FIND_VALUE;
//	}
//
//	public void startObject() {
//		visitors.push(current);
//		current = Visitor.newObject(this);
//		state = State.FIND_NAME;
//	}
//
//	public void endElement() {
//		Visitor.release(current);
//		current = visitors.pop();
//
//		if (visitors.size() > 0) {
//			state = State.FIND_NEXT;
//		} else {
//			Visitor.release(current);
//			state = State.END;
//			helper.notifyEnd();
//		}
//	}
//
//	public VisitorHelper getHelper() {
//		return helper;
//	}
}
