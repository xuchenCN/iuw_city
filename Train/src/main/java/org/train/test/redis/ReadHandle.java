package org.train.test.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReadHandle implements Runnable ,Callable{
	
	@Autowired
	private TestLogic testLogic;
	
	private AtomicInteger ai = new AtomicInteger(0);
	
	private static int MAX_COUNT = 50;
	
	@Override
	public void run() {
		for(int i = 0 ; i < MAX_COUNT ; i ++){
			ai.incrementAndGet();
			System.out.println("get : " + ai.get() + Thread.currentThread().getName() + ":" + testLogic.read(ai.get()));
		}
	}

	@Override
	public Object call() throws Exception {
		testLogic.write(null);
		return null;
	}
	
	
}
