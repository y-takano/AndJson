package jp.gr.java_conf.ke.json.databind.internal.conversion;

import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.ke.json.databind.JsonBindException;
import jp.gr.java_conf.ke.json.databind.annotation.DetectionPolicy;
import jp.gr.java_conf.ke.json.databind.converter.Converters.NullConverter;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;

public abstract class Conversion<T> {

	@SuppressWarnings({ "rawtypes" })
	public static final Conversion<?> EMPTY = new EmptyConversion();

	protected final Conversion<?> parent;
	protected final DetectionPolicy readPolicy;
	protected final DetectionPolicy writePolicy;
	protected final JsonConverter<T> converter;
	protected final Map<Class<?>, Conversion<?>> optional = new HashMap<Class<?>, Conversion<?>>();

	protected String name;
	protected int arrayLength;
	protected Class<?> concreteClass;
	protected Class<?> keyType;
	protected Conversion<?> keyConversion;
	protected Class<?> valueType;
	protected Conversion<?> valueConversion;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Conversion(Conversion<?> parent,
			DetectionPolicy readPolicy,
			DetectionPolicy writePolicy,
			Class<? extends JsonConverter> converter) {
		this.parent = parent;
		this.readPolicy = readPolicy == null ? DetectionPolicy.DISABLE_CONVERT : readPolicy;
		this.writePolicy = writePolicy == null ? DetectionPolicy.DISABLE_CONVERT : writePolicy;
		try {
			this.converter = converter == null ? null : converter.newInstance();
		} catch (Exception e) {
			throw new JsonBindException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public <E> Class<E> getConcreteClass(Class<?> clazz) {
		return concreteClass != null ? (Class<E>) concreteClass : (Class<E>) clazz;
	}

	public String getJsonName(String name) {
		return this.name == null || this.name.equals("") ? name : this.name;
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getReadConverter(Class<E> type) {

		if (type != null) {
			if (optional.containsKey(type)) {
				return optional.get(type).getReadConverter(type);
			}
		}

		if (this.isReadable()) {

			if (readPolicy.equals(DetectionPolicy.DEFAULT)) {

				if (parent != null) {
					return parent.getReadConverter(type);
				}
				return new NullConverter<E>();
			}
			return (JsonConverter<E>) converter;
		}
		return new NullConverter<E>();
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getWriteConverter(Class<E> type) {

		if (type != null) {
			if (optional.containsKey(type)) {
				return optional.get(type).getWriteConverter(type);
			}
		}

		if (this.isWritable()) {
			if (writePolicy.equals(DetectionPolicy.DEFAULT)) {

				if (parent != null) {
					return parent.getWriteConverter(type);
				}
				return new NullConverter<E>();
			}
			return (JsonConverter<E>) converter;
		}
		return new NullConverter<E>();
	}

	public Class<?> getKeyType() {
		return this.keyType;
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getReadKeyConverter() {
		if (this.isReadable()) {
			return (JsonConverter<E>) keyConversion.getReadConverter(keyType);
		}
		return new NullConverter<E>();
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getWriteKeyConverter() {
		if (this.isWritable()) {
			return (JsonConverter<E>) keyConversion.getWriteConverter(keyType);
		}
		return new NullConverter<E>();
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getReadValueConverter() {
		if (this.isReadable()) {
			return (JsonConverter<E>) valueConversion.getReadConverter(valueType);
		}
		return new NullConverter<E>();
	}

	@SuppressWarnings("unchecked")
	public <E> JsonConverter<E> getWriteValueConverter() {
		if (this.isWritable()) {
			return (JsonConverter<E>) valueConversion.getWriteConverter(keyType);
		}
		return new NullConverter<E>();
	}

	public Class<?> getValueType() {
		return this.valueType;
	}

	abstract public boolean isReadable();

	abstract public boolean isWritable();

	public int getArrayInitLength() {
		return this.arrayLength;
	}

}
