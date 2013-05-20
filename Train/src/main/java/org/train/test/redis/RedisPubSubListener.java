package org.train.test.redis;

import redis.clients.jedis.JedisPubSub;

public class RedisPubSubListener extends JedisPubSub {

	@Override
	public void onMessage(String channel, String message) {
		// TODO Auto-generated method stub
		System.out.println("channel : " + channel + " message : " + message);
	}

	@Override
	public void onPMessage(String pattern, String channel, String message) {
		// TODO Auto-generated method stub
		System.out.println("pattern : " + pattern + " channel : " + channel + " message : " + message);
	}

	@Override
	public void onSubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("channel : " + channel + " subscribedChannels : " + subscribedChannels);
	}

	@Override
	public void onUnsubscribe(String channel, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("channel : " + channel + " subscribedChannels : " + subscribedChannels);
	}

	@Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("pattern : " + pattern + " subscribedChannels : " + subscribedChannels);
	}

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {
		// TODO Auto-generated method stub
		System.out.println("pattern : " + pattern + " subscribedChannels : " + subscribedChannels);
	}
	

}
