package com.dtstack.logstash.filters;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.dtstack.logstash.annotation.Required;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:53:39
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Gsub extends BaseFilter {
	public Gsub(Map config) {
		super(config);
	}

	@Required(required=true)
	private static Map<String, List<String>> fields;

	public void prepare() {
	}

	@Override
	protected Map filter(final Map event) {
		Iterator<Entry<String, List<String>>> it = fields.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();

			String field = entry.getKey();
			String regex = entry.getValue().get(0);
			String replacement = entry.getValue().get(1);

			if (event.containsKey(field)) {
				event.put(field, ((String) event.remove(field)).replaceAll(
						regex, replacement));
			}
		}

		return event;
	}
}
