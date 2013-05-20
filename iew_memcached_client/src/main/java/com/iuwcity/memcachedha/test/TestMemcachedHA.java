package com.iuwcity.memcachedha.test;

import java.util.Set;
import java.util.TreeSet;

import net.rubyeye.xmemcached.XMemcachedClient;

import com.iuwcity.memcachedha.CommandParam;
import com.iuwcity.memcachedha.MemcachedClientDispatch;
import com.iuwcity.memcachedha.MemcachedClientKeeper;
import com.iuwcity.memcachedha.ReloadLevel;

public class TestMemcachedHA {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		XMemcachedClient client1 = new XMemcachedClient("127.0.0.1", 11211);
		XMemcachedClient client2 = new XMemcachedClient("127.0.0.1", 11233);
		MemcachedClientKeeper<XMemcachedClient> k1 = new MemcachedClientKeeper<XMemcachedClient>(client1, "127.0.0.1", 11211);
		MemcachedClientKeeper<XMemcachedClient> k2 = new MemcachedClientKeeper<XMemcachedClient>(client2, "127.0.0.1", 11233);

		MemcachedClientDispatch dispatch = new MemcachedClientDispatch();
		dispatch.addKeeper(k1);
		dispatch.addKeeper(k2);
		Set<String> syncMethods = new TreeSet<String>();
		syncMethods.add("add");
		syncMethods.add("set");
		dispatch.setSyncMethods(syncMethods);

		String key = "testKey";

		long start = System.currentTimeMillis();
		for (int i = 0; i <= 1000; i++) {
			// Thread.sleep(300);
			key = key + i;

			CommandParam getParam = new CommandParam("get", new Class[] { String.class }, new Object[] { key });
			CommandParam addParam = new CommandParam("add", new Class[] { String.class, int.class, Object.class },
					new Object[] { key, 0, "testValue" + i });

			dispatch.command(key, addParam, true, ReloadLevel.UNRELOAD, 0);

			// System.out.println(dispatch.command(key,getParam,
			// false,ReloadLevel.CHECKALL,0));
			key = "testKey";
		}

		System.out.println(System.currentTimeMillis() - start);

	}

}
