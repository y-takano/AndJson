package jp.gr.java_conf.ke.util.collection.recycle;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import jp.gr.java_conf.ke.util.collection.BlockingQueue;
import jp.gr.java_conf.ke.util.collection.ConccurentStack;
import jp.gr.java_conf.ke.util.collection.NonBlockingQueue;
import jp.gr.java_conf.ke.util.collection.NonBlockingStack;
import jp.gr.java_conf.ke.util.collection.Queue;
import jp.gr.java_conf.ke.util.collection.Stack;

public class CollectionRecycler<E> implements RecyclableQueue<E>,
		RecyclableStack<E> {

	public static interface ElementCreater<E> {
		E create();
	}

	public static <E> Queue<E> createBlockingQueue(RecycleConf conf,
			ElementCreater<E> creater) {
		return new CollectionRecycler<E>(new BlockingQueue<E>(), conf, creater);
	}

	public static <E> Queue<E> createNonBlockingQueue(RecycleConf conf,
			ElementCreater<E> creater) {
		return new CollectionRecycler<E>(new NonBlockingQueue<E>(), conf,
				creater);
	}

	public static <E> Stack<E> createBlockingStack(RecycleConf conf,
			ElementCreater<E> creater) {
		return new CollectionRecycler<E>(new ConccurentStack<E>(), conf,
				creater);
	}

	public static <E> Stack<E> createNonBlockingStack(RecycleConf conf,
			ElementCreater<E> creater) {
		return new CollectionRecycler<E>(new NonBlockingStack<E>(), conf,
				creater);
	}

	private final RecycleConf conf;
	private final ElementCreater<E> creater;

	private CollectionWrapper<E> wrap;
	private AtomicInteger consume = new AtomicInteger();
	private AtomicInteger lastmemento = new AtomicInteger();

	private List<CycleEventListener> listeners = new CopyOnWriteArrayList<CycleEventListener>();

	private CollectionRecycler(Queue<E> queue, RecycleConf conf,
			ElementCreater<E> creater) {
		if (conf == null)
			throw new NullPointerException("RecycleConf is null");
		else if (creater == null)
			throw new NullPointerException("ElementCreater is null");
		this.wrap = new CollectionWrapper<E>(queue);
		this.conf = conf;
		this.creater = creater;
		extend(conf.getInitSize());
		this.consume.set(0);
		this.lastmemento.set(size());
	}

	private CollectionRecycler(Stack<E> stack, RecycleConf conf,
			ElementCreater<E> creater) {
		if (conf == null)
			throw new NullPointerException("RecycleConf is null");
		else if (creater == null)
			throw new NullPointerException("ElementCreater is null");
		this.wrap = new CollectionWrapper<E>(stack);
		this.conf = conf;
		this.creater = creater;
		extend(conf.getInitSize());
		this.consume.set(0);
		this.lastmemento.set(size());
	}

	@Override
	public void push(E e) {
		try {
			add(e);
		} catch (RuntimeException ex) {
			throwExp(ex);
		}
	}

	@Override
	public E pop() {
		try {
			return remove();
		} catch (RuntimeException ex) {
			throwExp(ex);
			return null;
		}
	}

	@Override
	public void offer(E e) {
		try {
			add(e);
		} catch (RuntimeException ex) {
			throwExp(ex);
		}
	}

	@Override
	public E take() {
		try {
			return remove();
		} catch (RuntimeException e) {
			throwExp(e);
			return null;
		}
	}

	@Override
	public int size() {
		return wrap.size();
	}

	@Override
	public boolean isEmpty() {
		return wrap.isEmpty();
	}

	@Override
	public boolean addListener(CycleEventListener listener) {
		if (listener == null)
			return false;
		return listeners.add(listener);
	}

	@Override
	public boolean removeListener(CycleEventListener listener) {
		if (listener == null)
			return false;
		return listeners.remove(listener);
	}

	private void add(E e) {

		synchronized (this) {
			// 現在合計値
			int now = size();

			// 限界値
			int limit = conf.getLimitSize();

			// 限界値に到達している場合
			if (limit <= now) {
				// コールバック
				for (CycleEventListener l : listeners) {
					l.onReachingLimit(getConf());
				}
				// 増減サイズ分縮小する
				reduct(conf.getGrowNumer());
				return;
			} else {

				// 閾値
				int threshold = conf.getThreshold();

				// 前回閾値からの増分
				int last = now + 1 - lastmemento.get();

				// 閾値を超えている場合
				if (threshold < last) {
					// 増減サイズ分縮小する
					reduct(conf.getGrowNumer());
				}
			}
		}
		// 要素追加
		wrap.add(creater.create());
		consume.decrementAndGet();
	}

	private E remove() {

		synchronized (this) {
			// 現在合計値
			int now = size();

			// 初期値
			int init = conf.getInitSize();

			// 初期値に下達している場合
			if (now <= init) {
				// コールバック
				for (CycleEventListener l : listeners) {
					l.onReachingInit(getConf());
				}
				// 増減サイズ分拡張する
				extend(conf.getGrowNumer());

			} else {

				// 閾値
				int threshold = conf.getThreshold();

				// 前回閾値からの減分
				int last = lastmemento.get() - now;

				// 閾値を超えている場合
				if (threshold < last) {
					// 増減サイズ分拡張する
					extend(conf.getGrowNumer());
				}
			}
		}
		// 要素取出し
		consume.incrementAndGet();
		return wrap.remove();
	}

	private void extend(int size) {
		int limit = conf.getLimitSize() - (size() + consume.get());
		size = limit < size ? limit : size;

		for (int i = 0; i < size; i++) {
			wrap.add(creater.create());

//			// 使用メモリが90%以上の場合
//			if (90 <= Memory.useMemoryParcent()) {
//				// 制限時間：10秒
//				int limitTimes = 1000;
//				int times = 0;
//
//				// 使用メモリが90%以上の間
//				while (80 <= Memory.useMemoryParcent()) {
//
//					// 0.01秒待機
//					Timer.sleep(10);
//
//					if (limitTimes <= times++) {
//						throw new RuntimeException("メモリ空き容量が少なすぎます: 空き容量=" + Memory.freeMemoryParcent() + "% (" + Memory.freeMemory() + "/" + Memory.totalMemory() + "byte)");
//					}
//				}
//			}
		}
		lastmemento.addAndGet(size);
		for (CycleEventListener l : listeners) {
			l.onExtend(getConf());
		}
	}

	private void reduct(int size) {
		int limit = (size() + consume.get()) - conf.getInitSize();
		size = limit < size ? limit : size;

		for (int i = 0; i < size; i++) {
			wrap.remove();
		}
		lastmemento.addAndGet(-size);
		for (CycleEventListener l : listeners) {
			l.onReduct(getConf());
		}
	}

	@Override
	public RecycleConf getConf() {
		return conf.copy();
	}

	private void throwExp(Exception e) {
		String msg = "例外発生: 原因=" + e.getMessage() + ", 対処: 設定値を見直してください。, 設定値="
				+ conf + ", 状態={Collection容量=" + wrap.size() + ", 基準値="
				+ lastmemento + ", 要素数(使用中/合計)=" + consume.get() + "/"
				+ (size() + consume.get()) + "}";
		RuntimeException t = new IllegalStateException(msg, e.getCause());
		StackTraceElement[] ste = e.getStackTrace();
		int cutSize = 4;
		int newSize = ste.length - cutSize;
		StackTraceElement[] newSte = new StackTraceElement[newSize];
		for (int i = cutSize; i < ste.length; i++) {
			newSte[i - cutSize] = ste[i];
		}
		t.setStackTrace(newSte);
		throw t;
	}

}
