# AndJson
　Android用JSON-POJO変換ライブラリ - [ [GitHub Pages](https://y-takano.github.io/AndJson/) ]  
　（Android以外でも使用可能）。

## Overview（概要）

　databinding　・・・　Json文字列⇔POJO変換を1行で実現するための仕組み  
　iostreaming　・・・　Json文字列解析(Parser)、Json文字列生成(Generator)機能を利用するための仕組み  

## Install（環境設定）

　最新版 - 0.1.3ベータ版（以下）からダウンロードしてください。   
　https://github.com/y-takano/AndJson/releases/tag/0.1.3-beta  

## Requirement（依存関係）

　依存ライブラリなし。動作保障環境は以下の通り。

>　Java Runtime: 1.5(Tiger)以上  
>　Android minSdkLevel: 8(Froyo)以上 （最低動作保証APIレベル）  

## Usage（使い方）

### databinding
 
POJO.java
```java
import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;

@JsonBean
class POJO {
 private String aaa;

 public String getAaa() {
  return aaa;
 }
}
```

UseCase_JsonToPOJO.java
```java
import jp.gr.java_conf.ke.json.JsonFactory;

POJO pojo = JsonFactory.toObject("{\"aaa\":\"test\"}", POJO.class);
System.out.println(pojo.getAaa());
```
out:
> test

UseCase_POJOtoJson.java
```java
import jp.gr.java_conf.ke.json.JsonFactory;

POJO pojo = new POJO();
String json = JsonFactory.toJson(pojo);
System.out.println(json);
```

out:
> {"aaa":null}  

### iostreaming

Test.jsn
```js
{
  "aaa" : "",
  "bbb" : null,
  "ccc" : 1,
  "ddd" : true,
  "eee" : ["test1", "test2"]
}
```

UseCase_Parser.java
```java
import java.io.File;
import java.net.URI;

import jp.gr.java_conf.ke.json.JsonFactory;
import jp.gr.java_conf.ke.json.stream.JsonParser;
import jp.gr.java_conf.ke.json.Token;

File jsonFile = new File(new URI("Test.jsn"));
JsonParser parser = JsonFactory.createParser(jsonFile);

for (Token t : parser) {
  System.out.println(t);
}
```
out:  
> SYMBOL:{  
> NAME  :"aaa"  
> STRING:""  
> NAME  :"bbb"  
> NULL  :null  
> NAME  :"ccc"  
> NUMBER:1  
> NAME  :"ddd"  
> BOOLEAN:true  
> NAME  :"eee"  
> SYMBOL:[  
> STRING:"test1"  
> STRING:"test2"  
> SYMBOL:]  
> SYMBOL:}  


UseCase_Generator.java
```java
import java.util.HashMap;
import java.util.Map;

import jp.gr.java_conf.ke.json.JsonFactory;

Map<String, Object> map = new HashMap<>();
map.put("name1", "aaa");
map.put("name2", null);
map.put("name3", true);
map.put("name4", Integer.MIN_VALUE);
map.put("name5", "");

Object[] obj = new Object[]{"aaa", null, false, -210391};

JsonGenerator writer = JsonFactory.createGenerator(aaa);
JsonBuilder builder = writer.rootObject();
builder.startObject();
builder.name("name1").valueAsString("val1");
builder.name("name2").value("{aaa:val}");
builder.name("name3").value(obj);
builder.name("name4").valueAsDirect("{\"aaa\":null}");
builder.name("name5").valueAsBoolean(true);
builder.name("name6").valueAsNull();
builder.name("name7").valueAsNumber(Integer.MAX_VALUE);
builder.name("name8").value(map);
builder.endElement();

System.out.println(writer.toString());
writer.flushAndClose();
```
out:
> {"name1":"val1","name2":"{aaa:val}","name3":["aaa",null,false,-210391],"name4":{"aaa":null},"name5":true,"name6":null,"name7":2147483647,"name8":{"name5":"","name4":-2147483648,"name3":true,"name2":null,"name1":"aaa"}}

## Licence

　[MIT](http://opensource.org/licenses/mit-license.php)
