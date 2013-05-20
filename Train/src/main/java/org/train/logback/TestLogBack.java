package org.train.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestLogBack {
	
	public static  final Logger      logger        = LoggerFactory.getLogger("testlogback");

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		TestLogBack t = new TestLogBack();
		for(int i = 0 ; i < 100 ; i ++){
			if(i % 10 == 0){
				Thread.sleep(3000);
			}else {
				Thread.sleep(1000);
			}
			
			logger.info("aattaaaaaaaa");
		}
		
	}

}
