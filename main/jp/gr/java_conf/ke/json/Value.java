package jp.gr.java_conf.ke.json;

public class Value {

	private ValueType type;
	private volatile char[] text;

	public static final Value OF_NULL = new Value(ValueType.NULL, new char[] {'n', 'u', 'l', 'l'});

	Value(ValueType type, char[] text) {
		this.type = type;
		this.text = text;
	}

	public boolean isLiteral() {
		return type == ValueType.NULL || type == ValueType.BOOLEAN;
	}

	public boolean isBoolean() {
		return type == ValueType.BOOLEAN;
	}

	public boolean isNull() {
		return type == ValueType.NULL;
	}

	public boolean getBoolean() throws JsonSyntaxException {
		if (!isBoolean()) throw new JsonSyntaxException("このValueはBoolean型ではありません : " + toString());
		return Boolean.valueOf(getAsString());
	}

	public String getAsString() {
		return text == null ? "" : String.valueOf(text);
	}

	public String getText() {
		if (isNull() || isBoolean()) throw new JsonSyntaxException("このValueはBoolean型ではありません : " + toString());
		return getAsString();
	}

	public String toString() {
		if (type.equals(ValueType.STRING)) return type + ":\"" + getAsString() + "\"";
		else return type + ":" + getAsString();
	}
}
