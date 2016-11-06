package jp.gr.java_conf.ke.util.collection;

import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

import jp.gr.java_conf.ke.util.Timer;

public class BlockingQueue<E> implements Queue<E>, Blockable {

	private final java.util.Queue<E> queue;

	public BlockingQueue() {
		this.queue = new LinkedBlockingQueue<E>();
	}

	public synchronized void offer(E element) {
		if (!queue.offer(element)) {
			throw new IndexOutOfBoundsException("限界値到達(over the limit)=" + queue.size());
		}
	}

	public int size() {
		return queue.size();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public synchronized E take() {
		if (queue.size() == 0) {
			int times = 0;
			while (times < 10) {
				Timer.sleep(10);
				if (0 < queue.size()) return queue.remove();
				times++;
			}
			throw new NoSuchElementException("リソース枯渇(Queue is Empty)");
		}
		return queue.remove();
	}
}
