package org.train;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestThreadFactory {

	public static void main(String[] args) {
		ThreadFactory tf = Executors.defaultThreadFactory();
		
		ExecutorService e = Executors.newCachedThreadPool(tf);

		e.submit(tf.newThread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " running...");
				}
			}
		}));
		
		e.submit(tf.newThread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " running...");
				}
			}
		}));
		

	}
}
