package jp.gr.java_conf.ke.json.databind.internal.conversion;

import java.lang.annotation.Annotation;

import jp.gr.java_conf.ke.json.databind.annotation.DetectionPolicy;
import jp.gr.java_conf.ke.json.databind.annotation.Policy;

class DefaultPolicies {

	static final Policy NOTHING_ANNOTATE_FIELD_SPEC = new Policy() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return Policy.class;
		}

		@Override
		public DetectionPolicy write() {
			return DetectionPolicy.DEFAULT;
		}

		@Override
		public DetectionPolicy read() {
			return DetectionPolicy.DEFAULT;
		}
	};

	static final Policy NOTHING_ANNOTATE_ROOT_SPEC = new Policy() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return Policy.class;
		}

		@Override
		public DetectionPolicy write() {
			return DetectionPolicy.DISABLE_CONVERT;
		}

		@Override
		public DetectionPolicy read() {
			return DetectionPolicy.DISABLE_CONVERT;
		}
	};
}
