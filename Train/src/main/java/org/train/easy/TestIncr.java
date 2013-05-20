package org.train.easy;

import java.util.concurrent.atomic.AtomicInteger;

public class TestIncr implements Runnable {
	private AtomicInteger number = new AtomicInteger(0);
	public static void main(String[] args) {
		TestIncr t = new TestIncr();
		new Thread(t).start();
		new Thread(t).start();
	}
	
	public void run() {
		while(true){
			System.out.println("thread:" + Thread.currentThread().getName() + " number : " + this.number.incrementAndGet());
		}
	}
}
