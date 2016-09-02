package com.dtstack.logstash.filters;

import java.util.List;
import java.util.Map;

import com.dtstack.logstash.annotation.Required;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:55:04
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Trim extends BaseFilter {
	public Trim(Map config) {
		super(config);
	}

	@Required(required=true)
	private static  List<String> fields;

	public void prepare() {
	}

	@Override
	protected Map filter(final Map event) {
		for (String field : fields) {
			if (event.containsKey(field)) {
				event.put(field, ((String) event.remove(field)).trim());
			}
		}
		return event;
	}
}
