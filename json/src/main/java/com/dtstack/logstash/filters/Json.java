package com.dtstack.logstash.filters;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dtstack.logstash.annotation.Required;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:53:50
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Json extends BaseFilter {
    private static final Logger logger = LoggerFactory.getLogger(Json.class);
    
    private static ObjectMapper objectMapper = new ObjectMapper();

    public Json(Map config) {
        super(config);
    }

    @Required(required=true)
    private static Map<String,String> fields;
        
    private String tagOnFailure="JsonParserfail";

    public void prepare() {
    }

    @SuppressWarnings("rawtypes")
	@Override
    protected Map filter(final Map event) {
        Set<Map.Entry<String, String>>set =fields.entrySet();     
        for(Map.Entry<String, String> entry:set){
        	String key = entry.getKey();
        	String value = entry.getValue();
            if (event.containsKey(key)) {
                try {
                	 Object obj =objectMapper.readValue((String)event.get(key), Map.class);
                    if (obj != null) {
                        if (StringUtils.isNotBlank(value)) {
                            event.put(value,obj);
                        } else {
                            event.put(key, obj);
                        }
                    }
                } catch (Exception e) {
                    logger.error("failed to json parse field:" + key,e.getCause());
                }
            }
        }
        return event;
    }
}
