package test.com.iuwcity.redis.test;

import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestRedisClient {
	
	public static void main(String[] args) {
		
		/*
		
		JredisConnectionFactory connFactory = new JredisConnectionFactory();
		connFactory.setHostName("127.0.0.1");
		connFactory.setPooling(true);
		connFactory.setPort(16379);
//		connFactory.setTimeout(2000);
//		connFactory.setPoolSize(100);
	
//		connFactory.afterPropertiesSet();
		RedisConnection conn = null;
		for(int i = 0 ; i <= 50 ; i++){
			conn = connFactory.getConnection();
			System.out.println(conn);
		}
		
		System.out.println(conn.dbSize());
		
		*/
		
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = 50;
		config.maxWait = 2000;
		config.testOnBorrow = true;
		
		JedisPool pool = new JedisPool(config,"127.0.0.1",6379);
		
		 Jedis jedis = pool.getResource();
		 
		 System.out.println(jedis.dbSize());
		 
		 pool.returnResource(jedis);
 
	}
}
