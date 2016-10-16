package jp.gr.java_conf.ke.json.databind.internal.conversion;

import jp.gr.java_conf.ke.json.databind.annotation.DetectionPolicy;
import jp.gr.java_conf.ke.json.databind.annotation.JsonArray;
import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;
import jp.gr.java_conf.ke.json.databind.annotation.JsonIgnore;
import jp.gr.java_conf.ke.json.databind.annotation.JsonList;
import jp.gr.java_conf.ke.json.databind.annotation.JsonMap;
import jp.gr.java_conf.ke.json.databind.annotation.JsonValue;
import jp.gr.java_conf.ke.json.databind.annotation.Generics;

public class FieldAnnotateConversion<F> extends Conversion<F> {

	public FieldAnnotateConversion(Conversion<?> parent, JsonBean anno) {
		super(parent, anno.policy().read(),
				anno.policy().write(),
				anno.defaultConverter());
		for (Generics tc : anno.extention()) {
			optional.put(tc.concreteType(),
					new OptionalConversion<F>(anno.policy().read(),
							anno.policy().write(),
							tc.converter()));
		}
		this.name = anno.name();
	}

	public FieldAnnotateConversion(Conversion<?> parent, JsonIgnore anno) {
		super(parent, JsonIgnore.DEFAULT_READ_POLICY,
				JsonIgnore.DEFAULT_WRITE_POLICY,
				JsonIgnore.DEFAULT_CONVERTER);
		this.name = "~%FieldAnnotateConversion.JsonIgnore%~";
	}

	public FieldAnnotateConversion(Conversion<?> parent, JsonValue anno) {
		super(parent, anno.policy().read(),
				anno.policy().write(),
				anno.converter());
		this.name = anno.name();
	}

	public FieldAnnotateConversion(Conversion<?> parent, JsonArray anno) {
		super(parent, anno.policy().read(),
				anno.policy().write(),
				anno.converter());
		this.name = anno.name();
		this.arrayLength = anno.initLength();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FieldAnnotateConversion(Conversion<?> parent, JsonList anno) {
		super(parent, anno.policy().read(),
				anno.policy().write(),
				null);
		this.name = anno.name();
		this.concreteClass = anno.concrete();
		Generics valConv = anno.valueGeneric();
		this.valueType = valConv.concreteType();
		this.valueConversion = new OptionalConversion(
				anno.policy().read(),
				anno.policy().write(),
				valConv.converter());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FieldAnnotateConversion(Conversion<?> parent, JsonMap anno) {
		super(parent, anno.policy().read(),
				anno.policy().write(),
				null);
		this.name = anno.name();
		this.concreteClass = anno.concrete();
		Generics keyConv = anno.keyGeneric();
		this.keyType = keyConv.concreteType();
		this.keyConversion = new OptionalConversion(
				anno.policy().read(),
				anno.policy().write(),
				keyConv.converter());
		Generics valConv = anno.valueGeneric();
		this.valueType = valConv.concreteType();
		this.valueConversion = new OptionalConversion(
				anno.policy().read(),
				anno.policy().write(),
				valConv.converter());
	}

	@Override
	public boolean isReadable() {
		if (readPolicy.equals(DetectionPolicy.DEFAULT)) {
			if (parent != null) {
				return parent.isReadable();
			}
			return false;

		} else if (readPolicy.equals(DetectionPolicy.ENABLE_CONVERT)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isWritable() {
		if (writePolicy.equals(DetectionPolicy.DEFAULT)) {
			if (parent != null) {
				return parent.isWritable();
			}
			return false;

		} else if (writePolicy.equals(DetectionPolicy.ENABLE_CONVERT)) {
			return true;
		}
		return false;
	}

}
