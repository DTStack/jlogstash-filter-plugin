package com.dtstack.logstash.filters;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:53:39
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class LRUCache<E> {


    private Map<Object,E> fCacheMap;
    
    private int fCacheSize;
	
    @SuppressWarnings("unchecked")
   public LRUCache(int size){
    	
        fCacheSize = size;

        // If the cache is to be used by multiple threads,
        // the hashMap must be wrapped with code to synchronize 
        fCacheMap = new LinkedHashMap<Object,E>(fCacheSize, .75F, true){                                
            @Override
            public boolean removeEldestEntry(Map.Entry eldest)  
            {
                //when to remove the eldest entry
                return size() > fCacheSize ;   //size exceeded the max allowed
            }
        };
    }

    public void put(Object key, E elem)
    {
        fCacheMap.put(key, elem);
    }

    public E get(Object key)
    {
        return fCacheMap.get(key);
    }

}
