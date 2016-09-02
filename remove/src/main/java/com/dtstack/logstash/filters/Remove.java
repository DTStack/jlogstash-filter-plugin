package com.dtstack.logstash.filters;

import java.util.List;
import java.util.Map;

import com.dtstack.logstash.annotation.Required;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:54:23
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Remove extends BaseFilter {
	public Remove(Map config ) {
		super(config);
	}
	
    @Required(required=true)
	private static List<String> fields;

	public void prepare() {
		
	}

	@Override
	protected Map filter(final Map event) {
		if(fields!=null&&fields.size()>0){
			for (String t : fields) {
				event.remove(t);
			}

		}
		return event;
	}
}
