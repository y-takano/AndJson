package jp.gr.java_conf.ke.json.databind.internal.adapter;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jp.gr.java_conf.ke.json.Symbol;
import jp.gr.java_conf.ke.json.Token;
import jp.gr.java_conf.ke.json.databind.JsonBindException;
import jp.gr.java_conf.ke.json.databind.converter.JsonConverter;
import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

@SuppressWarnings("rawtypes")
class MapAdapter<T extends Map> extends ObjectAdapter<T> {

	@SuppressWarnings("unchecked")
	public MapAdapter(AdapterContext ctxt) {
		super(ctxt);
		// TODO 自動生成されたコンストラクター・スタブ
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public T read(JsonParser parser) {

		T instance = ctxt.getInstance();
		Conversion<T> conversion = ctxt.getConversion();
		JsonConverter keyConv = conversion.getReadKeyConverter();
		JsonConverter valueConv = conversion.getReadValueConverter();

		Token t;
		try {
			while (parser.hasNext()) {
				t = parser.next();
				if (t.isThat(Symbol.OBJ_END))
					break;
				else if (!t.isName())
					continue;

				if (!conversion.isReadable()) continue;

				instance.put((String) keyConv.toJava(t.getName(),
						conversion.getKeyType()),
						valueConv.toJava(parser.next().getValue().getText(),
								conversion.getValueType()));
			}
		} catch (Exception e) {
			throw new JsonBindException(e);
		}
		return conversion.isReadable() ? instance : null;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void write(JsonBuilder generator) {
		T instance = ctxt.getInstance();
		Conversion<T> conversion = ctxt.getConversion();
		JsonConverter keyConv = conversion.getWriteKeyConverter();
		JsonConverter valueConv = conversion.getWriteValueConverter();

		generator.startObject();
		Set<Entry> entryset = instance.entrySet();
		for (Entry entry : entryset) {
			generator.name(keyConv.toJson(entry.getKey()));
			writeValue(entry.getValue(), generator, valueConv);
		}
		generator.endElement();
	}

}
