package org.train.test.redis;

import redis.clients.jedis.Jedis;

public class TestRedisPublish {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Jedis jedis = new Jedis("192.168.29.122",6379);
		
//		for(int i = 0 ;i < 3 ;i ++){
//			jedis.publish("test-channel", "test message 11");
//		}
		jedis.set("aa", "bb");
		
	}

}
