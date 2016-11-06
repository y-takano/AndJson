package jp.gr.java_conf.ke.util.collection.recycle;

import java.io.Serializable;

public class RecycleConf implements Cloneable, Serializable {

	/** 初期容量初期値: {@value} */
	private static final int DEFAULT_INIT_SIZE = 0;

	/** 限界容量初期値: {@value} */
	private static final int NO_LIMIT = Integer.MAX_VALUE;

	/** 増減閾値初期値: {@value} */
	private static final int DEFAULT_THRESHOLD = 50;

	/** 増減容量初期値: {@value} */
	private static final int DEFAULT_GROW_NUMBER = 100;

	/** 初期容量 */
	private int initSize = DEFAULT_INIT_SIZE;

	/** 限界容量 */
	private int limitSize = NO_LIMIT;

	/** 増減閾値 */
	private int threshold = DEFAULT_THRESHOLD;

	/** 増減容量 */
	private int growNumer = DEFAULT_GROW_NUMBER;

	public int getInitSize() {
		return initSize;
	}


	public void setInitSize(int initSize) {
		this.initSize = initSize;
	}


	public int getLimitSize() {
		return limitSize;
	}


	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
	}


	public int getThreshold() {
		return threshold;
	}


	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}


	public int getGrowNumer() {
		return growNumer;
	}

	public void setGrowNumer(int growNumer) {
		this.growNumer = growNumer;
	}

	public RecycleConf copy() {
		try {
			return (RecycleConf) this.clone();
		} catch (CloneNotSupportedException e) {
			// 発生しない
			return null;
		}
	}

	public String toString() {
		return new StringBuilder()
			.append("{容量=")
			.append(initSize)
			.append("-")
			.append(limitSize)
			.append(", 増減閾値=")
			.append(threshold)
			.append(", 増減容量=")
			.append(growNumer)
			.append("}")
			.toString();
	}
}
