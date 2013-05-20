package org.train.test.redis;

import redis.clients.jedis.Jedis;

public class TestRedisSubscribe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedis = new Jedis("127.0.0.1",6379);
		
	
		jedis.subscribe(new RedisPubSubListener(), "test-channel");
	}

}
