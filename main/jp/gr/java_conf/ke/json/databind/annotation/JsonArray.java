package jp.gr.java_conf.ke.json.databind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.databind.converter.Converters.DefaultConverter;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonArray {

	static final int DEFAULT_LENGTH = 10;

	int initLength();

	String name() default "";

	Policy policy() default
			@Policy(read = DetectionPolicy.ENABLE_CONVERT,
					write = DetectionPolicy.ENABLE_CONVERT);

	@SuppressWarnings("rawtypes")
	Class<? extends JsonConverter> converter() default DefaultConverter.class;
}
