package jp.gr.java_conf.ke.json.databind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jp.gr.java_conf.ke.json.databind.converter.Converters.NullConverter;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JsonIgnore {

	static final DetectionPolicy DEFAULT_READ_POLICY = DetectionPolicy.DISABLE_CONVERT;

	static final DetectionPolicy DEFAULT_WRITE_POLICY = DetectionPolicy.DISABLE_CONVERT;

	@SuppressWarnings("rawtypes")
	static final Class<? extends JsonConverter> DEFAULT_CONVERTER = NullConverter.class;

	Generics[] extention() default {};
}
