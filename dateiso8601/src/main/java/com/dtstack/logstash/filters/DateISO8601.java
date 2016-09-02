package com.dtstack.logstash.filters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;
import com.dtstack.logstash.date.DateParser;
import com.dtstack.logstash.date.FormatParser;
import com.dtstack.logstash.date.UnixMSParser;
import com.dtstack.logstash.date.UnixParser;
import com.google.common.collect.Maps;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:52:50
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class DateISO8601 extends BaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(DateISO8601.class);
	
//  match =>{"timestamp":{"srcFormat":"dd/MMM/yyyy:HH:mm:ss Z","target":"timestamp","timezone":"UTC","locale":"en"}}
	@Required(required=true)
	private static Map<String,Map<String,String>> match;
	
	private String tagOnFailure="DateISO8601fail";
	
	private Map<String,DateParser> parsers = Maps.newConcurrentMap();

	public DateISO8601(Map config) {
		super(config);
		super.tagOnFailure = tagOnFailure;
	}

	public void prepare() {
		try{
			Set<Map.Entry<String,Map<String,String>>> matchs =match.entrySet();
			for(Map.Entry<String,Map<String,String>> ma:matchs){
				String src =ma.getKey();
				Map<String,String> value = ma.getValue();
				String format =value.get("srcFormat");
				if (format.equalsIgnoreCase("UNIX")) {
					parsers.put(src, new UnixParser());
				} else if (format.equalsIgnoreCase("UNIX_MS")) {
					parsers.put(src, new UnixMSParser());
				} else {
					String timezone =value.get("timezone");
					String locale = value.get("locale");
					parsers.put(src,new FormatParser(format,timezone, locale));
				}
			}	
		}catch(Exception e){
			logger.error(e.getMessage());
			System.exit(1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map filter(Map event) {
		if(parsers.size()>0){
		  Set<Map.Entry<String,DateParser>> sets = parsers.entrySet();
          for(Map.Entry<String,DateParser> entry:sets){
            	  String src =entry.getKey();
            	  if(event.containsKey(src)){
            		    DateParser dateParser  = entry.getValue();
            		    String target = match.get(src).get("target");
            			String input = (String)event.get(src);
            			event.put(target, dateParser.parse(input).toString());
            	  }  
          }
		}
		return event;
	}
	
	
	public static void main(String[] args){
		Map<String,Map<String,String>> mm = new HashMap<String,Map<String,String>>();
		Map<String,String> m1 = new HashMap<String,String>();
		m1.put("srcFormat", "dd/MMM/yyyy:HH:mm:ss Z");
		m1.put("locale", "en");
		m1.put("target", "timestamp");
		mm.put("timestamp", m1);
		match = mm;
		Map<String,Object> event = new HashMap<String,Object>();
		event.put("timestamp", "25/May/2016:01:10:07 +0800");
		DateISO8601 dateISO8601 =new DateISO8601(new HashMap<String,Object>());
		dateISO8601.prepare();
		dateISO8601.process(event);
		System.out.println(event);
	}

}
