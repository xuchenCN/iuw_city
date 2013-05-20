package com.iuwcity.memcachedha.test;

import com.iuwcity.memcachedha.NativeClient;

public class TestDefaultClient {

	public static void main(String[] args) throws Exception {

		NativeClient client = new NativeClient("127.0.0.1",11211);
		SimpleBean bean = new SimpleBean();
		bean.setAge(11);
		bean.setName("ssss");
		System.out.println(client.add("testNc", 0, bean ));
		
		SimpleBean bean1 = (SimpleBean)client.get("testNc");
		System.out.println(bean1.getAge() + " " + bean1.getName());
	}

}
