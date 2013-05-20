package org.train;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestLatch {
	
	public void awaitLatch(CountDownLatch latch) throws InterruptedException{
//		if(!latch.await(1000, TimeUnit.MILLISECONDS)){
//			System.out.println("time out");
//			return ;
//		}
		System.out.println("awaited!");
	}
	
	public void doSomeThing() throws InterruptedException{
		System.out.println("try to do something...");
		Thread.sleep(5000);
		System.out.println("end to do something...");
	}
	
	public static void main(String[] args) throws InterruptedException {
		TestLatch tl = new TestLatch();
		CountDownLatch latch = new CountDownLatch(2);
		tl.awaitLatch(latch);
		tl.doSomeThing();
		
		latch.await();
	}
}
