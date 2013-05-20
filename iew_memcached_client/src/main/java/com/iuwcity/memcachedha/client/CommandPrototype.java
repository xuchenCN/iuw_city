package com.iuwcity.memcachedha.client;

import java.io.UnsupportedEncodingException;

public class CommandPrototype {

	private String commandName;
	private String key;
	private int expiry;
	private Object value;

	public CommandPrototype(String commandName, String key, int expiry, Object value) {
		this.commandName = commandName;
		this.key = key;
		this.expiry = expiry;
		this.value = value;
	}
	
	public byte[] getKeyBytes(final String encode){
		if (key == null || key.length() == 0) {
			throw new IllegalArgumentException("Key must not be blank");
		}
		try {
			return key.getBytes(encode);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
