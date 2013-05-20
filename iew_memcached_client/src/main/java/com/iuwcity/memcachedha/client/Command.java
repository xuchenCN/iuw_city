package com.iuwcity.memcachedha.client;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public interface Command {
	public CountDownLatch getLatch();

	public ByteBuffer getBuffer();
	
	public ResponseCommand getResponse() ;

	public void setResponse(ResponseCommand response) ;

}
