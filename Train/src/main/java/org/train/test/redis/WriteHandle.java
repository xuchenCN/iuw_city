package org.train.test.redis;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WriteHandle implements Runnable ,Callable{
	
	@Autowired
	private TestLogic testLogic;
	
	private AtomicInteger ai = new AtomicInteger(0);
	
	@Override
	public void run() {
		ai.incrementAndGet();
		System.out.println("put : " + ai.get() + Thread.currentThread().getName());
		testLogic.write(ai.get());
	}

	@Override
	public Object call() throws Exception {
		testLogic.write(null);
		return null;
	}
	
	
}
