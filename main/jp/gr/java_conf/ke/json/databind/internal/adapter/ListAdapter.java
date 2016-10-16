package jp.gr.java_conf.ke.json.databind.internal.adapter;

import java.util.Collection;

import jp.gr.java_conf.ke.json.databind.JsonBindException;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.Json.Token;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

@SuppressWarnings("rawtypes")
public class ListAdapter<E extends Collection> extends ObjectAdapter<E> {

	public ListAdapter(AdapterContext<E> ctxt) {
		super(ctxt);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public E read(JsonParser parser) {
		E instance = ctxt.getInstance();
		Conversion<E> conversion = ctxt.getConversion();

		JsonConverter valueConv = conversion.getReadValueConverter();

		Token t;
		try {
			while (parser.hasNext()) {

				t = parser.next();
				if (t.isThat(Symbol.ARRAY_END)) break;
				else if (!t.isValue()) continue;

				if (!conversion.isReadable()) continue;

				instance.add(
						valueConv.toJava(
								t.getValue().getText(),
								conversion.getValueType()));
			}
		} catch (Exception e) {
			throw new JsonBindException(e);
		}
		return conversion.isReadable() ? instance : null;
	}

	@Override
	public void write(JsonBuilder generator) {
		E instance = ctxt.getInstance();
		Conversion<E> conversion = ctxt.getConversion();
		JsonConverter<?> valueConv = conversion.getWriteValueConverter();

		generator.startArray();
		for (Object e : instance) {
			writeValue(e, generator, valueConv);
		}
		generator.endElement();
	}


}
