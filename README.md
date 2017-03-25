#Add:

   fields: 必填 map结构({"host":"hostname","ip":"%{ip}%"})
   
   hostname: 本生是event里的属性，则就会取event.hostname里的值,没有就是原声的字符串hostname,
   
   %{ip}% 这样就表示调用内置函数获取本机ip
   
   现在的内置函数有hostname，timestamp，ip

#DateISO8601:

   match: 必填 map结构（{"timestamp":{"srcFormat":"dd/MMM/yyyy:HH:mm:ss Z","target":"timestamp","locale":"en"}}）

#Remove:
  
  fields:必填 list结构

  removeNULL:默认值false ，是否删除null或空字符串字段

#Rename:

  fields:必填 map结构{"oldname":"newname"}

#IpIp:

  source: 默认值 clientip 需要解析的ip

  target: 默认值 ipip 

  size: 默认值 5000

#UA:

  source:必填 需要解析属性

#JGrok:

  srcs:必填 list 结构，需要grok解析的属性["e","b"]
  
  patterns:必填 map结构，需要的正则表达式，{"pattern":"[0-9A-B]"}
  
  如果:grok自带的已经有了,正则表达式不需要写，列如:{"%{COMBINEDAPACHELOG}":""}

#Json:

  fields: 必填 map 结构 example {"messgae":"messgae1"} 源属性是message  目标属性message1，没有目标属性可以为“”
   
#Java:
  code: 必填，String类型 。
  
  示例：
  ```
  filters:
     - Java:
         code: '
             Object name = event.get("NAME");
             event.put("XM", name);
         '
  ```
