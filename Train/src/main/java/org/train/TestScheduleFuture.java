package org.train;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestScheduleFuture {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep1");
			}
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 5, 2, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(true);
			}
		}, 10, TimeUnit.SECONDS);
		
		scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(false);
			}
		}, 10, TimeUnit.SECONDS);
	}
}
