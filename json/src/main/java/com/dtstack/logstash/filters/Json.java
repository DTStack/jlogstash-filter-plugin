package com.dtstack.logstash.filters;

import java.util.Map;

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
    private static String field;
    
    private static String target;
    
    private String tagOnFailure="JsonParserfail";

    public void prepare() {
    }

    @Override
    protected Map filter(final Map event) {
        Object obj = null;
        if (event.containsKey(field)) {
            try {
            	obj =objectMapper.readValue((String)event.get(field), Map.class);
                if (obj != null) {
                    if (target == null) {
                        event.put(field,obj);
                    } else {
                        event.put(target, obj);
                    }
                }
            } catch (Exception e) {
                logger.error("failed to json parse field: " + field);
            }
        }
        return event;
    }
}
