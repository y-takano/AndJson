package jp.gr.java_conf.ke.util.collection;

public interface Stack<E> {

	void push(E e);

	E pop();

	int size();

	boolean isEmpty();
}
