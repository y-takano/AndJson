package jp.gr.java_conf.ke.io.core;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.ke.io.BufferedIO;
import jp.gr.java_conf.ke.io.IOStreamingException;

class BufferPacketRecycler {

	private static final int PACKET_SIZE = BufferedIO.DEFAULT_PACKET_SIZE;
	private static final int MAX_POOL_SIZE = 10000;
	private static final int MAX_BYTE_SIZE = PACKET_SIZE * MAX_POOL_SIZE;
	private static final int NEW_POOL_SIZE = 10;

	private static final List<BufferPacket> PACKET_POOL = new ArrayList<BufferPacket>();

	static {
		allocate(NEW_POOL_SIZE);
	}

	public static BufferPacket getPacket() {
		if (PACKET_POOL.size() == 0) {
			allocate(10);
		}
		BufferPacket ret = PACKET_POOL.remove(0);
		ret.reset();
		return ret;
	}

	public static void release(BufferPacket packet) {
		PACKET_POOL.add(packet);
	}

	public static void allocate(int packNum) {
		int nowSize = PACKET_SIZE * PACKET_POOL.size();
		int requestSize = PACKET_SIZE * packNum;
		int num;
		if (MAX_BYTE_SIZE <= (nowSize + requestSize)) {
			num = (MAX_BYTE_SIZE - nowSize) / PACKET_SIZE;
			if (num < 1) {
				throw new IOStreamingException("メモリが不足しています。 最大値:" + MAX_BYTE_SIZE + ", 現在値:" + nowSize + ", 1パケットサイズ:" + PACKET_SIZE);
			}
		} else {
			num = packNum;
		}
		for (int i=0; i<num; i++) {
			PACKET_POOL.add(new BufferPacket(PACKET_SIZE));
		}
	}
}
