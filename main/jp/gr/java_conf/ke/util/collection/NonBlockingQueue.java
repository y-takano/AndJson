package jp.gr.java_conf.ke.util.collection;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class NonBlockingQueue<E> implements Queue<E> {

	private final java.util.Queue<E> queue;

	public NonBlockingQueue() {
		this.queue = new LinkedList<E>();
	}

	@Override
	public void offer(E e) {
		if (!queue.offer(e)) {
			throw new IndexOutOfBoundsException("限界値到達(over the limit)=" + queue.size());
		}
	}

	@Override
	public E take() {
		if (queue.size() == 0) {
			throw new NoSuchElementException("リソース枯渇(Queue is Empty)");
		}
		return queue.remove();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
