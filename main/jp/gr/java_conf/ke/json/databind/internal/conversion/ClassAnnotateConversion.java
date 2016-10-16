package jp.gr.java_conf.ke.json.databind.internal.conversion;

import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;
import jp.gr.java_conf.ke.json.databind.annotation.JsonIgnore;
import jp.gr.java_conf.ke.json.databind.annotation.Generics;

public class ClassAnnotateConversion<C> extends Conversion<C> {

	public ClassAnnotateConversion(JsonBean anno) {
		super(null, anno.policy().read(),
				anno.policy().write(),
				anno.defaultConverter());
		for (Generics tc : anno.extention()) {
			optional.put(tc.concreteType(),
					new OptionalConversion<C>(anno.policy().read(),
							anno.policy().write(),
							tc.converter()));
		}
		this.name = anno.name();
	}

	public ClassAnnotateConversion(JsonIgnore anno) {
		super(null, JsonIgnore.DEFAULT_READ_POLICY,
				JsonIgnore.DEFAULT_WRITE_POLICY,
				JsonIgnore.DEFAULT_CONVERTER);
		this.name = "~%ClassAnnotateConversion.JsonIgnore%~";
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
