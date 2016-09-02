package com.dtstack.logstash.filters;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.filters.BaseFilter;

/**
 * 
 * @author sishu.yss
 *
 */
public class IpIp extends BaseFilter{
		
	private static Logger logger = LoggerFactory.getLogger(IpIp.class);
	
	public static String source ="clientip";
	
	public static String target = "ipip";
	
	public static int size = 50000;
	
	static{
		IP.load("17monipdb.dat");
	}
	
	
	private LRUCache cache = new LRUCache(size);
	
    public IpIp(Map config) {
		super(config);
		// TODO Auto-generated constructor stub
	}
    
    
	@Override
	public void prepare() {
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map filter(Map event){
		 try{
			  String ip = (String) event.get(source);
			  if(ip!=null&&!"".equalsIgnoreCase(ip)){
				  Map<String,Object> re =(Map<String,Object>)cache.get(ip);
				  if(re==null){
					  String[]result =IP.find(ip);
					  if(result!=null){
						  re = new HashMap<String,Object>();
						  re.put("country", result[0]);
						  re.put("province", result[1]);
						  re.put("city", result[2]);
						  cache.put(ip, re);
					  }
				  }
				  event.put(target, re);
			  }
		 
		 }catch(Exception e){
			logger.error("DtLogIpIp_filter_error", e);
		 }
		return event;
	}
    
	public static void main( String[] args ){

    }
}
