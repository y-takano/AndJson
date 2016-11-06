package jp.gr.java_conf.ke.util.collection;

public interface Queue<E> {

	void offer(E e);

	E take();

	int size();

	boolean isEmpty();
}
