package com.dtstack.logstash.filters;

import java.util.List;
import java.util.Map;
import java.net.URLDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:55:33
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class URLDecode extends BaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(URLDecode.class);

	@SuppressWarnings("rawtypes")
	public URLDecode(Map config) {
		super(config);
	}
    
	@Required(required=true)
	private static List<String> fields;
	
	private static String enc="UTF-8";
	
	private String tagOnFailure="URLDecodefail";
	

	@SuppressWarnings("unchecked")
	public void prepare() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map filter(final Map event) {
		boolean success = true;
		for (String f : fields) {
			if (event.containsKey(f)) {
				try {
					event.put(f,
							URLDecoder.decode((String) event.get(f), this.enc));
				} catch (Exception e) {
					logger.error("URLDecode failed", e);
					success = false;
				}
			}
		}
		return event;
	}
}
