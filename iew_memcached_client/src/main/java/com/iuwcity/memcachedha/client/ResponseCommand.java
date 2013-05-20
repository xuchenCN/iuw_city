package com.iuwcity.memcachedha.client;

import java.nio.ByteBuffer;

public class ResponseCommand {

	private ByteBuffer bodyBuf;
	private ByteBuffer headerBuf;

	public ByteBuffer getBodyBuf() {
		return bodyBuf;
	}

	public void setBodyBuf(ByteBuffer bodyBuf) {
		this.bodyBuf = bodyBuf;
	}

	public ByteBuffer getHeaderBuf() {
		return headerBuf;
	}

	public void setHeaderBuf(ByteBuffer headerBuf) {
		this.headerBuf = headerBuf;
	}

}
