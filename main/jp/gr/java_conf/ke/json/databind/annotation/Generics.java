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
@Target(ElementType.ANNOTATION_TYPE)
public @interface Generics {

	Class<?> concreteType();

	@SuppressWarnings("rawtypes")
	Class<? extends JsonConverter> converter() default DefaultConverter.class;
}
