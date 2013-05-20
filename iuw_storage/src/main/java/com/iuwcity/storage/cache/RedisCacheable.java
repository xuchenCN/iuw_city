package com.iuwcity.storage.cache;

import java.util.Map;

import com.iuwcity.storage.cache.Cacheable;

public interface RedisCacheable extends Cacheable {
	
	public String getKey();

	public Map<String, String> getValuesMap();

	public int getId();

	public void setId(int id);
}
