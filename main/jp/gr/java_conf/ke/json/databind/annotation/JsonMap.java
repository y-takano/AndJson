package jp.gr.java_conf.ke.json.databind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonMap {

	@SuppressWarnings("rawtypes")
	static final Class<? extends Map> DEFAULT_CONCRETE = HashMap.class;

	static final Class<?> DEFAULT_KEY_GENERIC = String.class;

	Generics valueGeneric();

	Generics keyGeneric() default @Generics(concreteType = String.class);

	String name() default "";

	@SuppressWarnings("rawtypes")
	Class<? extends Map> concrete() default HashMap.class;

	Policy policy() default
			@Policy(read = DetectionPolicy.ENABLE_CONVERT,
					write = DetectionPolicy.ENABLE_CONVERT);
}