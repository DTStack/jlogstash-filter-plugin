package com.dtstack.logstash.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * 
 * @author sishu.yss
 *
 */
public class PatternRead {

	private static Logger logger = LoggerFactory.getLogger(PatternRead.class);

	private static String patternFile = "pattern";

	private static Map<String, String> patterns = Maps.newLinkedHashMap();

	public static void patternRead() {
		if (patterns.size() == 0) {
			BufferedReader br = null;
			InputStreamReader ir = null;
			try {
				ir = new InputStreamReader(PatternRead.class.getClassLoader()
						.getResourceAsStream(patternFile));
				br = new BufferedReader(ir);
				String line;
				// We dont want \n and commented line
				Pattern pattern = Pattern.compile("^([A-z0-9_]+)\\s+(.*)$");
				while ((line = br.readLine()) != null) {
					Matcher m = pattern.matcher(line);
					if (m.matches()) {
						patterns.put(m.group(1), m.group(2));
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (ir != null) {
						ir.close();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error(e.getMessage());
				}
			}
		}

	}

	public static Map<String, String> getPatterns() {
		return patterns;
	}
}
