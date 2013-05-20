package com.iuwcity.storage.cache;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.stereotype.Component;

/**
 * @author xuchen
 *
 */

@Component
public interface MemcachedClientSevice {
		
		
	public MemcachedClient getClient();
}
