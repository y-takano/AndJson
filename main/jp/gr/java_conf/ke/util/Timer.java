package jp.gr.java_conf.ke.util;

public class Timer {

	public static boolean sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}


}
