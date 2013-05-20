package org.train;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.XMemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

public class TestXMemClient {
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException, MemcachedException {
		XMemcachedClient client = new XMemcachedClient("127.0.0.1",11211);
		client.set("a", 0, "b");
		
		String value = client.get("a");
		
		System.out.println(value);
	}
}
