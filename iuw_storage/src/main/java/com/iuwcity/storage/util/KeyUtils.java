package com.iuwcity.storage.util;

import org.springframework.stereotype.Component;

@Component
public class KeyUtils {
	
	public static final String KEY_SPERATOR = ":";
	
	public static String getKey(final String clazz, final String field, final String value) {
		StringBuffer key = new StringBuffer(clazz);
		key.append(KEY_SPERATOR).append(field).append(KEY_SPERATOR).append(value);
		return key.toString();
	}
	
}
