package com.dtstack.logstash.filters;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;
import com.dtstack.logstash.ua.parser.UserAgentUtil;
import com.google.common.collect.Maps;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:55:14
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class UA extends BaseFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(UA.class);
	
	private String tagOnFailure="UaParserfail";
	
	private static Map<String,Map<String,Object>> msm = Maps.newHashMap();

	public UA(Map config) {
		super(config);
	}

	@Required(required=true)
	private static String source;
	

	public void prepare() {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map filter(Map event) {
		boolean ifsuccess = true;
		try{
			if (event.containsKey(source)) {
				String us = (String)event.get(source);
				Map<String,Object> mm = msm.get(us);
				if(mm==null){
					mm =UserAgentUtil.getUserAgent((us));
				}
				event.put(source, mm);
			}
		}catch(Exception e){
			ifsuccess =false;
			logger.error(e.getMessage());
		}
		return event;
	}
}
