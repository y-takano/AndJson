package jp.gr.java_conf.ke.json.stream.parse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

class VisitorStack {

	enum State {
		FIND_NAME,
		FIND_VALUE,
		NAME,
		VALUE_NUMBER,
		VALUE_STRING,
		FIND_NEXT,
		END,
	}

	private static final int ROOTS_SIZE = 10;
	private static final Queue<Visitor> roots = new LinkedList<Visitor>();
	static {
		for(int i=0; i<ROOTS_SIZE; i++) {
			roots.add(new RootVisitor());
		}
	}

	private static final int OBJS_SIZE = 100;
	private static final Queue<Visitor> objs = new LinkedList<Visitor>();
	static {
		for(int i=0; i<OBJS_SIZE; i++) {
			objs.add(new ObjectVisitor());
		}
	}

	private static final int ARYS_SIZE = 100;
	private static final Queue<Visitor> arrays = new LinkedList<Visitor>();
	static {
		for(int i=0; i<ARYS_SIZE; i++) {
			arrays.add(new ArrayVisitor());
		}
	}

	private final Deque<Visitor> visitors;

	private State state;
	private Visitor currentVistor;

	public VisitorStack() {
		this.visitors = new ArrayDeque<Visitor>();
	}

	public Visitor getCurrentVisitor() {
		if (visitors.size() == 0) {
			currentVistor = roots.remove();
		}
		return this.currentVistor;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void startArray() {
		visitors.push(currentVistor);
		currentVistor = arrays.remove();
		state = State.FIND_VALUE;
	}

	public void startObject() {
		visitors.push(currentVistor);
		currentVistor = objs.remove();
		state = State.FIND_NAME;
	}

	public void endElement() {
		release(currentVistor);
		currentVistor = visitors.pop();
		if (visitors.size() > 0) {
			state = State.FIND_NEXT;
		} else {
			release(currentVistor);
			release(currentVistor);
			state = State.END;
		}
	}

	public boolean isEmpty() {
		return this.state == State.END;
	}

	private void release(Visitor v) {
		if (v instanceof RootVisitor) {
			roots.offer(v);
		} else if (v instanceof ObjectVisitor) {
			objs.offer(v);
		} else if (v instanceof ArrayVisitor) {
			arrays.offer(v);
		} else {
			throw new RuntimeException();
		}
	}
}
