package jp.gr.java_conf.ke.json.databind.internal.conversion;

import java.lang.annotation.Annotation;

import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;
import jp.gr.java_conf.ke.json.databind.annotation.JsonValue;
import jp.gr.java_conf.ke.json.databind.annotation.Policy;
import jp.gr.java_conf.ke.json.databind.annotation.Generics;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.databind.converter.Converters.DefaultConverter;

public class DefaultAnnotations {

	public static final JsonBean ROOT_CLASS = new JsonBean() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return JsonBean.class;
		}

		@Override
		public Generics[] extention() {
			return new Generics[0];
		}

		@Override
		public String name() {
			return "";
		}

		@Override
		public Policy policy() {
			return DefaultPolicies.NOTHING_ANNOTATE_ROOT_SPEC;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends JsonConverter> defaultConverter() {
			return DefaultConverter.class;
		}
	};

	public static final JsonValue FIELD_VALUE = new JsonValue() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return JsonValue.class;
		}

		@Override
		public String name() {
			return "";
		}

		@Override
		public Policy policy() {
			return DefaultPolicies.NOTHING_ANNOTATE_FIELD_SPEC;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<? extends JsonConverter> converter() {
			return DefaultConverter.class;
		}

	};
}
