package jp.gr.java_conf.ke.json.stream;

import java.io.IOException;

public interface JsonGenerator {

	JsonBuilder rootObject();

	JsonBuilder rootArray();

	String toString();

	void flushAndClose() throws IOException;

	public abstract interface JsonBuilder {

		JsonBuilder startObject() throws JsonSyntaxException;

		JsonBuilder startArray() throws JsonSyntaxException;

		JsonBuilder name(String name) throws JsonSyntaxException;

		JsonBuilder value(Object data) throws JsonSyntaxException;

		JsonBuilder valueAsString(String data) throws JsonSyntaxException;

		JsonBuilder valueAsNumber(Number data) throws JsonSyntaxException;

		JsonBuilder valueAsBoolean(boolean data) throws JsonSyntaxException;

		JsonBuilder valueAsNull() throws JsonSyntaxException;

		JsonBuilder valueAsDirect(String data) throws JsonSyntaxException;

		JsonBuilder endElement() throws JsonSyntaxException;

		String toString();

	}
}
