package jp.gr.java_conf.ke.json;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jp.gr.java_conf.ke.util.collection.Queue;
import jp.gr.java_conf.ke.util.collection.recycle.CollectionRecycler;
import jp.gr.java_conf.ke.util.collection.recycle.RecycleConf;
import jp.gr.java_conf.ke.util.collection.recycle.CollectionRecycler.ElementCreater;

public class TokenRecycler {

	private static final ThreadLocal<Queue<Token>> TOKEN_POOL = new ThreadLocal<Queue<Token>>() {
	};

	private static final int MAX_TOKEN_SIZE = Integer.MAX_VALUE / 1000;

	private static Lock lock = new ReentrantLock();

	public static void initialize() {
		initQueue();
		lock.lock();
	}

	public static Token getToken() {
		return TOKEN_POOL.get().take();
	}

	public static void offer(Token token) {
		TOKEN_POOL.get().offer(token);
	}

	public static void releaseLock() {
		initQueue();
		lock.unlock();
	}

	private static void initQueue() {
		RecycleConf conf = new RecycleConf();
		conf.setInitSize(1000);
		conf.setLimitSize(MAX_TOKEN_SIZE);
		conf.setGrowNumer(10000);
		conf.setThreshold(8000);
		ElementCreater<Token> creater = new ElementCreater<Token>() {
			@Override
			public Token create() {
				return new Token();
			}
		};
		TOKEN_POOL.set(CollectionRecycler.createBlockingQueue(conf, creater));
	}
}
