package com.iuwcity.memcachedha;

import com.iuwcity.memcachedha.protocol.CommandEnum;

class ReloadParam {
	private String commandName = CommandEnum.Add.toString();
	private String key;
	private int expiry;
	private Object value;

	public ReloadParam(String key, int expiry, Object value) {
		this.key = key;
		this.expiry = expiry;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
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
