package com.iuwcity.memcachedha.client;

import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;

public class BinaryCommand implements Command {

	private CountDownLatch latch;

	private ByteBuffer buffer;

	private ResponseCommand response;

	public ResponseCommand getResponse() {
		return response;
	}

	public void setResponse(ResponseCommand response) {
		this.response = response;
	}

	public BinaryCommand() {
		latch = new CountDownLatch(1);
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(ByteBuffer buffer) {
		this.buffer = buffer;
	}

}
