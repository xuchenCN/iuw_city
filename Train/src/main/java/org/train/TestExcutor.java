package org.train;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestExcutor {
	
	public static ExecutorService service = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		TestExcutor x = new TestExcutor();
		service.submit(x.new TodoSomeThingA());
		
//		List<DoSomeCallable> callList = new ArrayList<DoSomeCallable>();
//		DoSomeCallable a = new DoSomeCallable();
//		callList.add(a);
//		DoSomeCallable b = new DoSomeCallable();
//		callList.add(b);
//		long beginTime = System.currentTimeMillis();
//		List<Future<Object>> futures = new ArrayList<Future<Object>>(callList.size());
////		List<Future<Object>> futures = service.invokeAll(callList);
//		for(Callable<Object> c : callList){
//			Future<Object> f = service.submit(c);
//			futures.add(f);
//		}
//		System.out.println("do ..........");
//		
//		for(Future<Object> f :futures){
//			System.out.println(f.get());
//		}
//		
//		System.out.println(System.currentTimeMillis() - beginTime);
		
		service.shutdown();
	}
	
	public class TodoSomeThingA implements Runnable {

		@Override
		public void run() {
			System.out.println("todo some thing A");
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("done");
//			service.submit(new TestExcutor.TodoSomeThingB());
		}
		
	}
	
	public class TodoSomeThingB implements Runnable {

		@Override
		public void run() {
			System.out.println("todo some thing B");
		}
		
	}
}

