package org.train;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadPool implements Runnable {
	
	public static int nt = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(new TestThreadPool());
		service.execute(new TestThreadPool());
		service.execute(new TestThreadPool());
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println("running..." + ++nt);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
