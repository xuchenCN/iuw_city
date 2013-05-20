package com.iuwcity.storage;

import com.iuwcity.storage.cache.Cacheable;
import com.iuwcity.storage.cache.MemcachedClientSevice;
import com.iuwcity.storage.cache.RedisService;

public interface StorageService {
	
	public Cacheable saveOrUpdate(Cacheable bean);
	
	public Cacheable getBean(final int id, Class<?> bean);
	
	public RedisService getRedisService() ;

	public MemcachedClientSevice getMemcachedService();
}
