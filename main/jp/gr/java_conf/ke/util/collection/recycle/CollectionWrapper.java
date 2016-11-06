package jp.gr.java_conf.ke.util.collection.recycle;

import jp.gr.java_conf.ke.util.collection.Queue;
import jp.gr.java_conf.ke.util.collection.Stack;

class CollectionWrapper<E> {

	private final Queue<E> queue;
	private final Stack<E> stack;

	public CollectionWrapper(Stack<E> stack) {
		this.queue = null;
		this.stack = stack;
	}


	public CollectionWrapper(Queue<E> queue) {
		this.queue = queue;
		this.stack = null;
	}

	public void add(E e) {
		if (queue != null)
			queue.offer(e);
		else stack.push(e);
	}

	public E remove() {
		if (queue != null)
			return queue.take();
		return stack.pop();
	}

	public int size() {
		if (queue != null)
			return queue.size();
		return stack.size();
	}

	public boolean isEmpty() {
		if (queue != null)
			return queue.isEmpty();
		return stack.isEmpty();
	}
}
