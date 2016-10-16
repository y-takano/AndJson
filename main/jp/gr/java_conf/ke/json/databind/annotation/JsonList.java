package jp.gr.java_conf.ke.json.databind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonList {

	@SuppressWarnings("rawtypes")
	static final Class<? extends Collection> DEFAULT_CONCRETE = ArrayList.class;

	Generics valueGeneric();

	String name() default "";

	@SuppressWarnings("rawtypes")
	Class<? extends Collection> concrete() default ArrayList.class;

	Policy policy() default
			@Policy(read = DetectionPolicy.ENABLE_CONVERT,
					write = DetectionPolicy.ENABLE_CONVERT);

}
