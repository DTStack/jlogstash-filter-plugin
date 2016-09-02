package com.dtstack.logstash.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;
import com.dtstack.logstash.render.FreeMarkerRender;
import com.dtstack.logstash.render.TemplateRender;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:52:37
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Add extends BaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(Add.class.getName());

	@Required(required=true)
	private static Map<String, String> fields=null;
	
	public Add(Map config) {
		super(config);
	}

	private static Map<String, TemplateRender> f;

	public void prepare() {
		f = new HashMap<String, TemplateRender>();
		Iterator<Entry<String, String>> it = fields.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String field = entry.getKey();
			String value = entry.getValue();
			try {
				f.put(field, new FreeMarkerRender(value, value));
			} catch (IOException e) {
				logger.error(e.getMessage());
				System.exit(1);
			}
		}
	};

	@Override
	protected Map filter(final Map event) {
		Iterator<Entry<String, TemplateRender>> it = f.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, TemplateRender> entry = it.next();

			String field = entry.getKey();
			TemplateRender render = entry.getValue();
			event.put(field, render.render(event));
		}
		return event;
	}
}
