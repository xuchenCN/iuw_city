package com.iuwcity.memcachedha.client;


public interface Client {
	
	public Object sendCommand(final Command command);
}
