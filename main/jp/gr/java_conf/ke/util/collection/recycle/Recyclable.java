package jp.gr.java_conf.ke.util.collection.recycle;


public abstract interface Recyclable {

	RecycleConf getConf();

	boolean addListener(CycleEventListener listener);

	boolean removeListener(CycleEventListener listener);
}
