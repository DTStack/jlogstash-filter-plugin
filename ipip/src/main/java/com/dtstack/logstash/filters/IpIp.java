package com.dtstack.logstash.filters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;
import com.dtstack.logstash.filters.BaseFilter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * 
 * @author sishu.yss
 *
 */
@SuppressWarnings("serial")
public class IpIp extends BaseFilter{
		
	private static Logger logger = LoggerFactory.getLogger(IpIp.class);
	
	@Required(required=true)
	private static Map<String,String> souTar;
	
	public static int size = 50000;
	
	static{
		IP.load("17monipdb.dat");
	}
		
	public static Cache<String, Map<String, Object>> cache;

    @SuppressWarnings("rawtypes")
	public IpIp(Map config) {
		super(config);
		// TODO Auto-generated constructor stub
	}
    
    
	@Override
	public void prepare() {
		if(cache==null){
			cache = CacheBuilder.newBuilder() .maximumSize(size).build();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map filter(Map event){
		 try{
			 Set<Map.Entry<String,String>> entrys = souTar.entrySet();
			 for(Map.Entry<String,String> entry:entrys){ 
				  String ip = (String) event.get(entry.getKey());
				  if(StringUtils.isNoneBlank(ip)){
					  Map<String,Object> re =(Map<String,Object>)cache.getIfPresent(ip);
					  if(re==null){
						  String[] result =IP.find(ip);
						  if(result!=null){
							  re = new HashMap<String,Object>();
							  re.put("country", result[0]);
							  re.put("province", result[1]);
							  re.put("city", result[2]);
							  cache.put(ip, re);
						  }
					  }
					  event.put(entry.getValue(), re);
				  }
			 }	 
		 }catch(Exception e){
			logger.error("DtLogIpIp_filter_error", e);
		 }
		return event;
	}
}
