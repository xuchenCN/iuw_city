package org.train.test.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.springframework.data.keyvalue.redis.serializer.JdkSerializationRedisSerializer;
import org.train.introspector.bean.Dog;

import redis.clients.jedis.Jedis;

public class TestRedis {
	
	public static void main(String args[]) {
		Jedis jredis = new Jedis("127.0.0.1",6379);
		/*
		Map<String,String> values = new HashMap<String,String>();
		values.put("name", "tester");
		values.put("age", "30");
		jredis.hmset("user:100", values);
		
		System.out.println(jredis.hmget("user:100", "age","name"));
		
		System.out.println(jredis.hgetAll("user:100"));
		*/
		JdkSerializationRedisSerializer redisSerializer = new JdkSerializationRedisSerializer();
		
		Dog a = new Dog();
		a.setName("aa");
		Dog b = new Dog();
		b.setName("bb");
		
		jredis.zadd("dog_zset1".getBytes(), 0.0, redisSerializer.serialize(b));
		jredis.zadd("dog_zset1".getBytes(), 0.0, redisSerializer.serialize(a));
		
		Set<byte[]> set = jredis.zrange("dog_zset".getBytes(), 0, 10);
		for(byte[] bs : set){
			Dog d = (Dog)redisSerializer.deserialize(bs);
			System.out.println(d.getName());
		}
		
	}
	
	/**
	 * Get the bytes representing the given serialized object.
	 */
	public static byte[] serialize(Object o) {
		if (o == null) {
			throw new NullPointerException("Can't serialize null");
		}
		byte[] rv = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			os.writeObject(o);
			os.close();
			bos.close();
			rv = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		}
		return rv;
	}
	
	/**
	 * Get the object represented by the given serialized bytes.
	 */
	public static Object deserialize(byte[] in) {
		Object rv = null;
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				rv = is.readObject();

			}
		} catch (IOException e) {
//			log.error("Caught IOException decoding " + in.length
//					+ " bytes of data", e);
		} catch (ClassNotFoundException e) {
//			log
//					.error("Caught CNFE decoding " + in.length
//							+ " bytes of data", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// ignore
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return rv;
	}
	
}
