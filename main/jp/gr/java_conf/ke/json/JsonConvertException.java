package jp.gr.java_conf.ke.json;

public class JsonConvertException extends RuntimeException {

	public JsonConvertException(String msg) {
		super(msg);
	}

	public JsonConvertException(Throwable cause) {
		super(cause);
	}

	public JsonConvertException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
