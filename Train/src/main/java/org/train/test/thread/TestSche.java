package org.train.test.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestSche  implements Runnable {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
		ScheduledFuture<?> scheduleHandle = scheduler.scheduleAtFixedRate(new TestSche(),1, 2, TimeUnit.SECONDS);
		
		Thread.sleep(5000);
		scheduleHandle.cancel(true);
		Thread.sleep(5000);
		scheduleHandle.cancel(false);
	}

	@Override
	public void run() {
		System.out.println("run..");
	}
	
	

}
