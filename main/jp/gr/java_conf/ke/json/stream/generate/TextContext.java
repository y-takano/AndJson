package jp.gr.java_conf.ke.json.stream.generate;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;

class TextContext {

	protected static final char OBJ_START = '{';
	protected static final char OBJ_END = '}';
	protected static final char ARRAY_START = '[';
	protected static final char ARRAY_END = ']';
	protected static final char SPACE = ' ';
	protected static final char DEMILITOR = ':';
	protected static final char SEPARATOR = ',';
	protected static final char STR_START = '"';
	protected static final char STR_END = '"';

	private BufferedTextWriter writer;
	private final Deque<JsonBuilder> builders;

	public TextContext(BufferedTextWriter writer) {
		this.writer = writer;
		this.builders = new ArrayDeque<JsonBuilder>();
	}

	public void startObject() {
		JsonBuilder builder = new ObjectWriter(writer);
		builder.startObject();
		builders.push(builder);
	}

	public void startArray() {
		JsonBuilder builder = new ArrayWriter(writer);
		builder.startArray();
		builders.push(builder);
	}

	public void name(String name) {
		builders.peek().name(name);
	}

	public void value(Object data) {
		builders.peek().value(data);
	}

	public void value(String data) {
		builders.peek().valueAsString(data);
	}

	public void value(Number data) {
		builders.peek().valueAsNumber(data);
	}

	public void value(boolean data) {
		builders.peek().valueAsBoolean(data);
	}

	public void direct(String data) {
		builders.peek().valueAsDirect(data);
	}

	public void nullValue() {
		builders.peek().valueAsNull();
	}

	public void end() {
		builders.pop().endElement();
	}

	public String toString() {
		return writer.toString();
	}

	public void flush() throws IOException {
		writer.flush();
		writer.close();
	}
}
