package org.train.test.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;
import org.train.spring.Helper;

@Component
public class Luancher {
	
	private static final int MAX_THREAD = 100;
	private static final int MAX_POOL_SIZE = 100;
	
	private static ExecutorService threads =  Executors.newFixedThreadPool(MAX_POOL_SIZE);
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReadHandle rhandle = (ReadHandle)Helper.getCtx().getBean("readHandle");
		WriteHandle whandle = (WriteHandle)Helper.getCtx().getBean("writeHandle");
		long begin = System.currentTimeMillis();
		for(int i = 0 ; i < MAX_THREAD ; i ++){
			try {
				threads.execute(rhandle);
//				threads.execute(whandle);
			}catch(Exception ex){
				ex.printStackTrace();
				System.out.println("ERROR");
				threads.shutdown();
			}
		}
		
		threads.shutdown();
		while(!threads.isTerminated()){}
//		TestLogic.jedis.disconnect();
		System.out.println("complete : " + (System.currentTimeMillis() - begin));
		
	}
}
