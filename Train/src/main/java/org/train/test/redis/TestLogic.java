package org.train.test.redis;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

@Service
public class TestLogic  {
	
	public void write(Object o){
		Jedis jedis = new Jedis("10.10.83.44",11216);
		jedis.set("tkey" + o.toString(), "tvalue" + o.toString());
		
		jedis.disconnect();
	}
	
	public Object read(Object o){
		Jedis jedis = new Jedis("10.10.83.44",11216);
		Object v = jedis.get("tkey" + o.toString());
		jedis.disconnect();
		return v;
	}
	/*
public static Jedis jedis = new Jedis("10.10.83.44",11216);
	
	public synchronized void write(Object o){
		jedis.set("tkey" + o.toString(), "tvalue" + o.toString());
	}
	
	public synchronized Object read(Object o){
		Object v = jedis.get("tkey" + o.toString());
		return v;
	}
	*/
}
