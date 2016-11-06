package jp.gr.java_conf.ke.json.stream.parse;

import jp.gr.java_conf.ke.util.collection.ConccurentStack;
import jp.gr.java_conf.ke.util.collection.Stack;

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

//	private static final int ROOTS_SIZE = 1000;
//	private final Queue<Visitor> roots = new NonBlockingQueue<Visitor>();
//	static {
//		for(int i=0; i<ROOTS_SIZE; i++) {
//			roots.offer(new RootVisitor());
//		}
//	}
//
//	private static final int OBJS_SIZE = 100000;
//	private static final Queue<Visitor> objs = new BlockingQueue<Visitor>();
//	static {
//		for(int i=0; i<OBJS_SIZE; i++) {
//			objs.offer(new ObjectVisitor());
//		}
//	}
//
//	private static final int ARYS_SIZE = 2000000;
//	private static final Queue<Visitor> arrays = new BlockingQueue<Visitor>();
//	static {
//		for(int i=0; i<ARYS_SIZE; i++) {
//			arrays.offer(new ArrayVisitor());
//		}
//	}

	private final Stack<Visitor> visitors = new ConccurentStack<Visitor>();

	private State state;
	private Visitor currentVistor;

	public Visitor getCurrentVisitor() {
		if (visitors.size() == 0) {
			currentVistor = new RootVisitor();
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
		currentVistor = new ArrayVisitor();
		state = State.FIND_VALUE;
	}

	public void startObject() {
		visitors.push(currentVistor);
		currentVistor = new ObjectVisitor();
		state = State.FIND_NAME;
	}

	public synchronized void endElement() {
		//release(currentVistor);
		try {
			currentVistor = visitors.pop();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		if (visitors.size() > 0) {
			state = State.FIND_NEXT;
		} else {
			state = State.END;
		}
	}

	public boolean isEmpty() {
		return this.state == State.END;
	}

//	private synchronized void release(Visitor v) {
//		if (v instanceof RootVisitor) {
//			if (roots.size() < ROOTS_SIZE) {
//				roots.offer(v);
//			}
//		} else if (v instanceof ObjectVisitor) {
//			objs.offer(v);
//		} else if (v instanceof ArrayVisitor) {
//			arrays.offer(v);
//		} else {
//			throw new RuntimeException();
//		}
//	}

//	private synchronized Visitor getVisitor(Queue<Visitor> queue) {
//		Visitor visitor;
//		visitor = queue.take();
//		return visitor;
//	}
}
