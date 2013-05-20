package com.iuwcity.memcachedha.listener;

import com.iuwcity.memcachedha.MemcachedClientKeeper;

public interface ServerStatListener {
	
	public void serverDown(MemcachedClientKeeper<?> keeper ,int activeServerNumber);
	
	public void serverUp(MemcachedClientKeeper<?> keeper ,int activeServerNumber);
}
