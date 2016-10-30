package jp.gr.java_conf.ke.io.core;

import java.util.LinkedList;
import java.util.Queue;

import jp.gr.java_conf.ke.io.BufferedIO;
import jp.gr.java_conf.ke.io.IOStreamingException;

class BufferPacketRecycler {

	private static final int PACKET_SIZE = BufferedIO.DEFAULT_PACKET_SIZE;
	private static final int MAX_POOL_SIZE = 10000;
	private static final int MAX_BYTE_SIZE = PACKET_SIZE * MAX_POOL_SIZE;
	private static final int NEW_POOL_SIZE = 500;

	private static final Queue<BufferPacket> PACKET_POOL = new LinkedList<BufferPacket>();

	static {
		allocate(NEW_POOL_SIZE);
	}

	public static BufferPacket getPacket() {
		BufferPacket p = PACKET_POOL.poll();
		if (p == null) {
			allocate(10);
			p = PACKET_POOL.remove();
		}
		return p;
	}

	public static void release(BufferPacket packet) {
		PACKET_POOL.offer(packet);
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
			PACKET_POOL.offer(new BufferPacket(PACKET_SIZE));
		}
	}
}
