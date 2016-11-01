package jp.gr.java_conf.ke.json.stream.generate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;

class ObjectWriter implements JsonBuilder {

	private BufferedTextWriter writer;
	private boolean firstElement;

	public ObjectWriter(BufferedTextWriter writer) {
		this.writer = writer;
		this.firstElement = true;
	}

	public JsonBuilder startObject() {
		writer.append(TextContext.OBJ_START);
		return null;
	}

	public JsonBuilder startArray() {
		writer.append(TextContext.ARRAY_START);
		return null;
	}

	@Override
	public JsonBuilder name(String name) {
		if (!this.firstElement) writer.append(TextContext.SEPARATOR);
		firstElement = false;
		writer.append(TextContext.STR_START);
		appendString(name);
		writer.append(TextContext.STR_END);
		writer.append(TextContext.DEMILITOR);
		return null;
	}

	@Override
	public JsonBuilder valueAsString(String data) {
		writer.append(TextContext.STR_START);
		appendString(data);
		writer.append(TextContext.STR_END);
		return null;
	}

	@Override
	public JsonBuilder valueAsNumber(Number data) {
		appendString(String.valueOf(data));
		return null;
	}

	@Override
	public JsonBuilder valueAsBoolean(boolean data) {
		appendString(String.valueOf(data));
		return null;
	}

	@Override
	public JsonBuilder valueAsNull() {
		appendString("null");
		return null;
	}

	@Override
	public JsonBuilder endElement() {
		writer.append(TextContext.OBJ_END);
		return null;
	}

	@Override
	public JsonBuilder value(Object data) throws JsonSyntaxException {

		if (data == null) {
			valueAsNull();

		} else if (data instanceof String) {
			valueAsString((String)data);

		} else if (data instanceof Number) {
			valueAsNumber((Number)data);

		} else if (data instanceof Boolean) {
			valueAsBoolean((Boolean)data);

		} else if (data instanceof Object[]) {
			Object[] list = (Object[])data;
			boolean first = true;
			startArray();
			for (Object o : list) {
				if (first) {
					first = false;
				} else {
					writer.append(',');
				}
				value(o);
			}
			writer.append(']');
		} else if (data instanceof List) {
			List<?> list = (List<?>)data;
			boolean first = true;
			startArray();
			for (Object o : list) {
				if (first) {
					first = false;
				} else {
					writer.append(',');
				}
				value(o);
			}
			writer.append(']');
		} else if (data instanceof Map) {
			Map<?, ?> map = (Map<?,?>)data;
			boolean first = true;
			startObject();
			for (Entry<?,?> entry : map.entrySet()) {
				if (first) {
					first = false;
				} else {
					writer.append(',');
				}

				writer.append(TextContext.STR_START);
				appendString(String.valueOf(entry.getKey()));
				writer.append(TextContext.STR_END);
				writer.append(TextContext.DEMILITOR);
				value(entry.getValue());
			}
			writer.append('}');
		} else {
			throw new JsonSyntaxException("valueのデータとして無効な型:" + data.getClass());

		}
		return null;
	}

	@Override
	public JsonBuilder valueAsDirect(String data) throws JsonSyntaxException {
		appendString(data);
		return null;
	}

	private void appendString(String data) {
		for (int i=0; i<data.length(); i++) {
			writer.append(data.charAt(i));
		}
	}
}
