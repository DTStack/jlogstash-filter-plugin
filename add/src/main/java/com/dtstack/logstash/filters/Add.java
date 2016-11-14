package com.dtstack.logstash.filters;

import java.net.InetAddress;
import java.util.Map;
import java.util.Set;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dtstack.logstash.annotation.Required;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:52:37
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
@SuppressWarnings("serial")
public class Add extends BaseFilter {
	private static final Logger logger = LoggerFactory.getLogger(Add.class.getName());
	
	@Required(required=true)
	private static Map<String, Object> fields=null;
	
	@SuppressWarnings("rawtypes")
	public Add(Map config) {
		super(config);
	}

	public void prepare() {

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Map filter(final Map event) {
		Set<Map.Entry<String,Object>> sets =fields.entrySet();
		for(Map.Entry<String,Object> entry:sets){
			Object value = entry.getValue();
			String key = entry.getKey();
			event.put(key, value);
			if(event.get(value)!=null){
				event.put(key, event.get(value));
			}else if(value instanceof String){
				String vv =value.toString();
        		if ("%{hostname}%".equals(vv)){
        			event.put(key, getHostName());
        		}else if("%{timestamp}%".equals(vv)){
        			event.put(key, getTimeStamp());
        		}else if("%{ip}%".equals(vv)){
        			event.put(key, getHostAddress());
        		}
            }      	
		}
		return event;
	}
	
	
	private String getHostName(){
        try {
			InetAddress ia = InetAddress.getLocalHost();
			return ia.getHostName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getHostName:{}",e.getCause());
		}   
        return "localhost";
	}
	
	private String getHostAddress(){
        try {
			InetAddress ia = InetAddress.getLocalHost();
			return ia.getHostAddress();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getHostAddress:{}",e.getCause());
		}   
        return "localhost";
	}
	
	private String getTimeStamp(){
		return DateTime.now().toString();
	}	
}
