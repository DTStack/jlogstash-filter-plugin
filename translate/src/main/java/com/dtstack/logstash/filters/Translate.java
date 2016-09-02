package com.dtstack.logstash.filters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;


/**
 * 
 * Reason: TODO ADD REASON(可选)
 * Date: 2016年8月31日 下午1:54:51
 * Company: www.dtstack.com
 * @author sishu.yss
 *
 */
public class Translate extends BaseFilter {
    private static final Logger logger = LoggerFactory.getLogger(Translate.class);

    public Translate(Map config) {
        super(config);
    }

    private String target;
    private String source;
    private String dictionaryPath;
    private int refreshInterval;
    private long nextLoadTime;
    private Map dictionary;

    private void loadDictionary() {
        logger.info("begin to loadDictionary: " + this.dictionaryPath);

        if (dictionaryPath == null) {
            dictionary = null;
            logger.warn("dictionary_path is null");
            return;
        }
        Yaml yaml = new Yaml();

        if (dictionaryPath.startsWith("http://") || dictionaryPath.startsWith("https://")) {
            URL httpUrl;
            URLConnection connection;
            try {
                httpUrl = new URL(dictionaryPath);
                connection = httpUrl.openConnection();
                connection.connect();
                dictionary = (Map) yaml.load(connection.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("failed to load " + dictionaryPath);
                System.exit(1);
            }
        } else {
            FileInputStream input;
            try {
                input = new FileInputStream(new File(dictionaryPath));
                dictionary = (HashMap) yaml.load(input);
            } catch (FileNotFoundException e) {
                logger.error(dictionaryPath + " is not found");
                logger.error(e.getMessage());
                System.exit(1);
            }
        }

        logger.info("loadDictionary done: " + this.dictionaryPath);
    }

    public void prepare() {
        target = (String) config.get("target");
        source = (String) config.get("source");

        dictionaryPath = (String) config.get("dictionary_path");

        loadDictionary();

        if (config.containsKey("refresh_interval")) {
            this.refreshInterval = (int) config.get("refresh_interval") * 1000;
        } else {
            this.refreshInterval = 300 * 1000;
        }
        nextLoadTime = System.currentTimeMillis() + refreshInterval * 1000;
    }

    ;

    @Override
    protected Map filter(final Map event) {
        if (dictionary == null || !event.containsKey(this.source)) {
            return event;
        }
        if (System.currentTimeMillis() >= nextLoadTime) {
            loadDictionary();
            nextLoadTime += refreshInterval;
        }
        Object t = dictionary.get(event.get(source));
        if (t != null) {
            event.put(target, t);
        }
        return event;
    }
}
