package jp.gr.java_conf.ke.json.databind.internal.adapter;

import jp.gr.java_conf.ke.json.Value;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

class ValueAdapter<T> extends ObjectAdapter<T> {

	private Class<T> type;
	private T instance;
	private Conversion<T> conversion;

	public ValueAdapter(Class<T> type, Conversion<T> conversion) {
		super(null);
		this.type = type;
		this.conversion = conversion;
	}

	@SuppressWarnings("unchecked")
	public ValueAdapter(T instance, Conversion<T> conversion) {
		super(null);
		this.instance = instance;
		this.type =  instance == null ? null : (Class<T>) instance.getClass();
		this.conversion = conversion;
	}

	@Override
	public T read(JsonParser parser) {

		Value v = parser.current().getValue();
		JsonConverter<T> cnv = conversion.getReadConverter(type);

		T val;
		if (v.isNull()) {
			val = cnv.toJava(null, type);
		} else {
			val = cnv.toJava(v.getText(), type);
		}
		return val;
	}

	@Override
	public void write(JsonBuilder generator) {
		JsonConverter<T> cnv = conversion.getWriteConverter(type);
		writeValue(instance, generator, cnv);
	}
}
