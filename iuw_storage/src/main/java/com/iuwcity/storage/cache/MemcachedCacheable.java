package com.iuwcity.storage.cache;

import com.iuwcity.storage.cache.Cacheable;

public interface MemcachedCacheable extends Cacheable {
	public String getKey();

	public int getId();

	public void setId(int id);
}
