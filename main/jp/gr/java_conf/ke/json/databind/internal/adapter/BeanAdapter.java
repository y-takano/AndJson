package jp.gr.java_conf.ke.json.databind.internal.adapter;

import java.lang.reflect.Field;

import jp.gr.java_conf.ke.json.databind.JsonBindException;
import jp.gr.java_conf.ke.json.databind.internal.AdapterFactory;
import jp.gr.java_conf.ke.json.databind.internal.conversion.Conversion;
import jp.gr.java_conf.ke.json.stream.Json.Symbol;
import jp.gr.java_conf.ke.json.stream.Json.Token;
import jp.gr.java_conf.ke.json.stream.JsonGenerator.JsonBuilder;
import jp.gr.java_conf.ke.json.stream.JsonParser;

public class BeanAdapter<T> extends ObjectAdapter<T> {

	public BeanAdapter(AdapterContext<T> ctxt) {
		super(ctxt);
	}

	@Override
	public T read(JsonParser parser) {

		T instance = ctxt.getInstance();
		Conversion<T> conversion = ctxt.getConversion();

		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) instance.getClass();

		Token t;
		try {

			while (parser.hasNext()) {

				t = parser.next();
				if (t.isThat(Symbol.OBJ_END))
					break;
				else if (!t.isName())
					continue;

				// フィールド探索
				for (Field f : clazz.getDeclaredFields()) {

					// 名前不一致時はスキップ
					if (!conversion.getJsonName(f.getName()).equals(t.getName()))
						continue;

					// アダプタ生成
					ObjectAdapter<?> adapter = AdapterFactory
							.createFieldReadAdapter(f, conversion);

					// Json→Java変換
					parser.next();
					Object value = adapter.read(parser);

					// nullの場合は回避
					// 　・フィールドアクセスのコスト軽減のため
					// 　・フィールド型がプリミティブだとエラーになるため
					if (value != null) {
						f.setAccessible(true);
						f.set(instance, value);
					}
					break;
				}
			}
		} catch (Exception e) {
			throw new JsonBindException(e);
		}
		return instance;

	}

	@Override
	public void write(JsonBuilder generator) {

		Conversion<T> conversion = ctxt.getConversion();

		T instance = ctxt.getInstance();

		if (instance == null) {
			generator.valueAsNull();
			return;
		}

		@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) instance.getClass();

		try {
			generator.startObject();
			Field[] fs = clazz.getDeclaredFields();

			for (Field f : fs) {

				f.setAccessible(true);

				// アダプタ生成
				ObjectAdapter<?> adapter = AdapterFactory
						.createFieldWriteAdapter(f, conversion, f.get(instance));

				if (adapter == null) continue;

				generator.name(conversion.getJsonName(f.getName()));
				adapter.write(generator);
			}
			generator.endElement();

		} catch (Exception e) {
			throw new JsonBindException(e);
		}
	}

}
