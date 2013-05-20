package com.iuwcity.storage;

import java.lang.reflect.Constructor;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iuwcity.storage.cache.Cacheable;
import com.iuwcity.storage.cache.MemcachedCacheable;
import com.iuwcity.storage.cache.MemcachedClientSevice;
import com.iuwcity.storage.cache.RedisCacheable;
import com.iuwcity.storage.cache.RedisService;

@Service
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private MemcachedClientSevice memcachedService;
	
	public Cacheable getBean(final int id, Class<?> bean){
		
		try {
			Constructor<?> c = bean.getConstructor(new Class[] {});
			Cacheable newBean = (Cacheable)c.newInstance(new Object[] {});
			//FIX me 优先级问题
			if(newBean instanceof RedisCacheable){
				return redisService.getBean(id, bean);
			}
			
			if(newBean instanceof MemcachedCacheable){
				MemcachedClient client = memcachedService.getClient();
				MemcachedCacheable memcachendCacheBean = (MemcachedCacheable)newBean;
				memcachendCacheBean.setId(id);
				return client.get(memcachendCacheBean.getKey());
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 增加或更新
	 * @param bean
	 * @return
	 */
	public Cacheable saveOrUpdate(Cacheable bean){
		try {
			
			if(bean instanceof RedisCacheable){
				if(((RedisCacheable)bean).getId() > 0){
					redisService.updateBean((RedisCacheable)bean);
				}else {
					redisService.putNewBean((RedisCacheable)bean);
				}
			}
			
			if(bean instanceof MemcachedCacheable){
				MemcachedClient client = memcachedService.getClient();
				client.add(bean.getKey(), 0, bean);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return bean;
	}
	
	/**
	 * 将对象从存储中移除
	 * @param bean
	 */
	public void remove(Cacheable bean) {
		try {
			if(bean instanceof RedisService){
				redisService.removeBean((RedisCacheable)bean);
			}
			
			if(bean instanceof MemcachedCacheable){
				MemcachedClient client = memcachedService.getClient();
				client.delete(bean.getKey());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public RedisService getRedisService() {
		return redisService;
	}

	public MemcachedClientSevice getMemcachedService() {
		return memcachedService;
	}
	
	
}
