package org.train.easy.test.alixmem;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import com.iuwcity.storage.bean.UserInfo;

public class TestThread implements Runnable {
	
	private ICache cacheClient;
	
	private CountDownLatch cd ;
	
	private int sum = 500;
	
	public TestThread(ICache cache,CountDownLatch cd){
		this.cacheClient = cache;
		this.cd = cd;
	}

	@Override
	public void run() {
		
		UserInfo value = new UserInfo();
		value.setAge(30);
		value.setCreateTime(new Date());
		value.setId(90);
		value.setLastLoginDate(new Date());
		value.setName("阿克拉地方机阿里山打开飞机");
		value.setSex(3);
		value.setStatus(6);
		
		String key = "test";
		for(int i = 0 ; i <= sum ; i ++ ){
			
			this.cacheClient.doPut(key, value);
			this.cacheClient.doGet(key);
		}
		
		cd.countDown();
	}
	
}
