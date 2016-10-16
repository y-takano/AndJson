package jp.gr.java_conf.ke.json.databind;

public class JsonBindException extends RuntimeException {

	public JsonBindException(String string) {
		super(string);
	}

	public JsonBindException(Exception e) {
		super(e);
	}

}
