package jp.gr.java_conf.ke.json.databind.internal.adapter;

import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public abstract class ObjectAdapter<T> {

	protected final AdapterContext<T> ctxt;

	public ObjectAdapter(AdapterContext<T> ctxt) {
		this.ctxt = ctxt;
	}

	public abstract T read(JsonParser parser);

	public abstract void write(JsonBuilder generator);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void writeValue(Object instance, JsonBuilder generator, JsonConverter cnv) {

		if (instance == null) {
			generator.valueAsNull();
		} else {
			String json = cnv.toJson(instance);
			if (json == null || json.equals("null")) {
				generator.valueAsNull();
			} else {
				if (instance instanceof Boolean) {
					generator.valueAsDirect(json);
				} else if (instance instanceof Number) {
					generator.valueAsDirect(json);
				} else {
					generator.valueAsString(json);
				}
			}
		}
	}
}
