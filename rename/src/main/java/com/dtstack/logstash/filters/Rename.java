package com.dtstack.logstash.filters;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import com.dtstack.logstash.annotation.Required;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:54:32
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Rename extends BaseFilter {
	public Rename(Map config) {
		super(config);
	}

	@Required(required=true)
	private static Map<String, String> fields;

	public void prepare() {
	}

	@Override
	protected Map filter(Map event) {
		if(fields!=null){
			Iterator<Entry<String, String>> it = fields.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				String oldname = entry.getKey();
				String newname = entry.getValue();
				if (event.containsKey(oldname)) {
					event.put(newname, event.remove(oldname));
				}
			}
		}
		return event;
	}
}
