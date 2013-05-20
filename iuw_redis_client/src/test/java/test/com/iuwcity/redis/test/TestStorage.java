package test.com.iuwcity.redis.test;

import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.JedisPool;

import com.iuwcity.redis.service.RedisServiceImpl;
import com.iuwcity.storage.bean.UserInfo;
import com.iuwcity.storage.cache.RedisService;


public class TestStorage {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = 50;
		config.maxWait = 2000;
		config.testOnBorrow = true;
		
		JedisPool pool = new JedisPool(config,"127.0.0.1",16379);
		
		RedisService service = new RedisServiceImpl(); 
		service.setPool(pool);
		
		UserInfo user = new UserInfo();
		/*
		user.setAge(31);
		user.setCreateTime(new Date());
		user.setName("tester1");
		user.setSex(0);
		user.setStatus(1);
		
		user = (UserInfo)service.putNewBean(user);
		System.out.println(user.getId());
		*/
		
		
//		String key = KeyUtils.getKey(UserInfo.class.getName(), "", "2");
		
		UserInfo testUser = (UserInfo)service.getBean(user.getId(), UserInfo.class);
		System.out.println(testUser.getAge());
		System.out.println(testUser.getId());
		System.out.println(testUser.getName());
		System.out.println(testUser.getCreateTime());
		System.out.println(testUser.getLastLoginDate());
		
	}

}
