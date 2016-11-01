package jp.gr.java_conf.ke.json.databind.internal.adapter;

import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

class IgnoreAdapter extends ObjectAdapter<Object> {

	public IgnoreAdapter() {
		super(null);
	}

	@Override
	public Object read(JsonParser parser) {
		return null;
	}

	@Override
	public void write(JsonBuilder generator) {

	}

}
