package com.dtstack.jlogstash.filters;

import com.dtstack.jlogstash.annotation.Required;

import java.util.*;

public class SplitStr extends BaseFilter {

    @Required(required=true)
    private static  List<Map<String,Object>> parseConfig;

    private static final String DELIM_KEY = "delim";

    private static final String KEYS_KEY = "cols";

    private static final String CONTENT_KEY = "textKey";

    private static final String FILE_NAME_KEY = "signKey";

    private static final String FILE_NAME_VAL_KEY = "signVal";

    public SplitStr(Map config) {
        super(config);
    }

    @Override
    public void prepare() {

    }

    @Override
    protected Map filter(Map event) {
        for (Map<String, Object> conf : parseConfig) {
            if (event.get(conf.get(FILE_NAME_KEY)).equals(conf.get(FILE_NAME_VAL_KEY))){
                List<String> keys = (List<String>) conf.get(KEYS_KEY);
                String[] items = event.get(conf.get(CONTENT_KEY)).toString().split(conf.get(DELIM_KEY).toString());
                int size = Math.min(items.length,keys.size());
                for (int i = 0; i < size; i++) {
                    event.put(keys.get(i),items[i]);
                }
            }
        }

        return event;
    }

}
