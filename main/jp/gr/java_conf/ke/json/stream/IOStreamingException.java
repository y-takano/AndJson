package jp.gr.java_conf.ke.json.stream;

public class IOStreamingException extends RuntimeException {

	public IOStreamingException(Exception e) {
		super(e);
	}

	public IOStreamingException(String msg) {
		super(msg);
	}
}
