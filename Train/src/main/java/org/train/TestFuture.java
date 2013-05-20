package org.train;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestFuture {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, TimeoutException {
		TestFuture.doSomeThing();
		System.out.println("main return");
		return;
	}
	
	public static void doSomeThing(){
		ExecutorService executor = Executors.newCachedThreadPool();
		DoSomeCallable call = new DoSomeCallable();
		FutureTask<Object> ft = new FutureTask<Object>(call);
		executor.execute(ft);
		System.out.println("at main");
		executor.shutdown();
		return ;
	}
}


class DoSomeCallable implements Callable<Object> {

	public Object call() throws Exception {
		System.out.println("do some thing in call " + Thread.currentThread().getName());
		System.out.println("call will return");
		return Thread.currentThread().getName();
	}
	
}