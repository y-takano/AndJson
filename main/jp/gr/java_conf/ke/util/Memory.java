package jp.gr.java_conf.ke.util;

public class Memory {

	public static long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	public static long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	public static long useMemory() {
		return totalMemory() - freeMemory();
	}

	public static int freeMemoryParcent() {
		float free = freeMemory();
		float total = totalMemory();
		float ret = (float)free / (float)total;
		return (int) (ret * 100);
	}

	public static int useMemoryParcent() {
		float use = useMemory();
		float total = totalMemory();
		float ret = (float)use / (float)total;
		return (int) (ret * 100);
	}

}
