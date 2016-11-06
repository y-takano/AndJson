package jp.gr.java_conf.ke.util.collection.recycle;


public interface CycleEventListener {

	void onReduct(RecycleConf conf);

	void onExtend(RecycleConf conf);

	void onReachingLimit(RecycleConf conf);

	void onReachingInit(RecycleConf conf);
}
