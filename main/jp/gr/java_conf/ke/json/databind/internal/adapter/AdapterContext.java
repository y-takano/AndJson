package jp.gr.java_conf.ke.json.databind.internal.adapter;

import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;

public class AdapterContext<T> {

	private T instance;
	private Conversion<T> conversion;

	public AdapterContext(T instance, Conversion<T> conversion) {
		this.instance = instance;
		this.conversion = conversion;
	}

	public T getInstance() {
		return instance;
	}

	public Conversion<T> getConversion() {
		return conversion;
	}

}
