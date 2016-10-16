package jp.gr.java_conf.ke.json.databind.internal.conversion;

import jp.gr.java_conf.ke.json.databind.annotation.DetectionPolicy;

class EmptyConversion <T> extends Conversion<T> {

	public EmptyConversion() {
		super(null, DetectionPolicy.DISABLE_CONVERT, DetectionPolicy.DISABLE_CONVERT, null);
	}

	@Override
	public boolean isReadable() {
		return false;
	}

	@Override
	public boolean isWritable() {
		return false;
	}
}
