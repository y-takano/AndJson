package jp.gr.java_conf.ke.json.databind.internal.conversion;

import jp.gr.java_conf.ke.json.databind.annotation.DetectionPolicy;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;

class OptionalConversion<T> extends Conversion<T> {

	@SuppressWarnings("rawtypes")
	public OptionalConversion(DetectionPolicy readPolicy, DetectionPolicy writePolicy,
			Class<? extends JsonConverter> converter) {
		super(null, readPolicy, writePolicy, converter);
	}

	@Override
	public boolean isReadable() {
		switch (readPolicy) {
		case ENABLE_CONVERT:
			return true;

		case DISABLE_CONVERT:
		case DEFAULT:
		default:
			return false;
		}
	}

	@Override
	public boolean isWritable() {
		switch (writePolicy) {
		case ENABLE_CONVERT:
			return true;

		case DISABLE_CONVERT:
		case DEFAULT:
		default:
			return false;
		}
	}

}
