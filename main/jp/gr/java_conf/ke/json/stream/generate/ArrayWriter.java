package jp.gr.java_conf.ke.json.stream.generate;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.gr.java_conf.ke.io.BufferedTextWriter;
import jp.gr.java_conf.ke.json.JsonSyntaxException;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;

class ArrayWriter implements JsonBuilder {

	private BufferedTextWriter writer;
	private boolean firstElement;
	private boolean started;

	public ArrayWriter(BufferedTextWriter writer) {
		this.writer = writer;
		this.firstElement = true;
		this.started = false;
	}

	public JsonBuilder startObject() {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write(TextContext.OBJ_START);
		return null;
	}

	public JsonBuilder startArray() {
		if (started) {
			if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		} else {
			started = true;
		}
		writer.write(TextContext.ARRAY_START);
		return null;
	}

	@Override
	public JsonBuilder name(String name) {
		throw new JsonSyntaxException("nameはarray要素に追加できません: " + name);
	}

	@Override
	public JsonBuilder valueAsString(String data) {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write(TextContext.STR_START);
		writer.write(data);
		writer.write(TextContext.STR_END);
		return null;
	}

	@Override
	public JsonBuilder valueAsNumber(Number data) {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write(String.valueOf(data));
		return null;
	}

	@Override
	public JsonBuilder valueAsBoolean(boolean data) {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write(String.valueOf(data));
		return null;
	}

	@Override
	public JsonBuilder valueAsNull() {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write("null");
		return null;
	}

	@Override
	public JsonBuilder endElement() {
		writer.write(TextContext.ARRAY_END);
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
			startArray();
			for (Object o : list) {
				value(o);
			}
			endElement();
		} else if (data instanceof List) {
			List<?> list = (List<?>)data;
			boolean first = true;
			startArray();
			for (Object o : list) {
				if (first) {
					first = false;
				} else {
					writer.write(",");
				}
				value(o);
			}
			endElement();
		} else if (data instanceof Map) {
			Map<?, ?> map = (Map<?,?>)data;
			boolean first = true;
			startObject();
			for (Entry<?,?> entry : map.entrySet()) {
				if (first) {
					first = false;
				} else {
					writer.write(",");
				}
				name(String.valueOf(entry.getKey()));
				value(entry.getValue());
			}
			endElement();
		} else {
			throw new JsonSyntaxException("valueのデータとして無効な型:" + data.getClass());

		}
		return null;
	}

	@Override
	public JsonBuilder valueAsDirect(String data) throws JsonSyntaxException {
		if (!this.firstElement) writer.write(TextContext.SEPARATOR);
		firstElement = false;
		writer.write(data);
		return null;
	}

}
