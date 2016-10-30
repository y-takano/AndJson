package jp.gr.java_conf.ke.json.stream.generate;

import java.io.IOException;

import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.JsonGenerator;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;

class JsonStreamWriter implements JsonGenerator, JsonBuilder {

	private TextContext ctxt;

	public JsonStreamWriter(BufferedTextWriter writer) {
		this.ctxt = new TextContext(writer);
	}

	@Override
	public JsonBuilder rootObject() {
		return this;
	}

	@Override
	public JsonBuilder rootArray() {
		return this;
	}

	@Override
	public void flushAndClose() throws IOException {
		ctxt.flush();
	}

	@Override
	public String toString() {
		return ctxt.toString();
	}

	@Override
	public JsonBuilder startObject() {
		ctxt.startObject();
		return this;
	}

	@Override
	public JsonBuilder startArray() {
		ctxt.startArray();
		return this;
	}

	@Override
	public JsonBuilder name(String name) {
		if (name == null) throw new NullPointerException();
		ctxt.name(name);
		return this;
	}

	@Override
	public JsonBuilder valueAsString(String data) {
		if (data == null) throw new NullPointerException();
		ctxt.value(data);
		return this;
	}

	@Override
	public JsonBuilder valueAsNumber(Number data) {
		if (data == null) throw new NullPointerException();
		ctxt.value(data);
		return this;
	}

	@Override
	public JsonBuilder valueAsBoolean(boolean data) {
		ctxt.value(data);
		return this;
	}

	@Override
	public JsonBuilder valueAsNull() {
		ctxt.nullValue();
		return this;
	}

	@Override
	public JsonBuilder endElement() {
		ctxt.end();
		return this;
	}

	@Override
	public JsonBuilder value(Object data) throws JsonSyntaxException {
		ctxt.value(data);
		return this;
	}

	@Override
	public JsonBuilder valueAsDirect(String data) throws JsonSyntaxException {
		if (data == null) throw new NullPointerException();
		ctxt.direct(data);
		return this;
	}

}
