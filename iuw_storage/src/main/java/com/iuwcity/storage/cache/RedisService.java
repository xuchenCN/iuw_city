package com.iuwcity.storage.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public interface RedisService {
	
	public Jedis getConnection();
	
	public void releaseConnection(Jedis conn);
	
	public RedisCacheable putNewBean(final RedisCacheable bean) throws Exception ;
	
	public RedisCacheable putNewBean(final String[] keys ,final RedisCacheable bean) throws Exception;
	
	public RedisCacheable updateBean(final RedisCacheable bean) throws Exception ;
	
	public RedisCacheable updateBean(final String[] keys ,final RedisCacheable bean) throws Exception ;
	
	public void removeBean(final RedisCacheable bean)throws Exception ;
	
	public RedisCacheable getBean(int id, Class<?> bean);
	
	public RedisCacheable getBean(final String key, Class<?> bean);
	
	public int getNextId(final String key);
	
	public JedisPool getPool() ;

	public void setPool(JedisPool pool) ;
}
