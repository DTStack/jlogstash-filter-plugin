package com.dtstack.logstash.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;
import com.dtstack.logstash.render.FreeMarkerRender;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:54:43
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Replace extends BaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(Replace.class);

	public Replace(Map config) {
		super(config);
	}
	
	@Required(required=true)
	private static String src;
	
	@Required(required=true)
	private static String value;

	public void prepare() {
		try {
			this.render = new FreeMarkerRender(value, value);
		} catch (IOException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	protected Map filter(final Map event) {
		if (event.containsKey(src)) {
			event.put(src, render.render(value, new HashMap() {
				{
					put("event", event);
				}
			}));
		}
		return event;
	}
}
