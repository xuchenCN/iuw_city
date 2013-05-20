package com.iuwcity.memcachedha.protocol;

public enum CommandEnum {
	
	Add("add"),
	Set("set"),
	Get("get"),
	Version("version");
	
	private String v;

	private CommandEnum(String v) {
		this.v = v;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.v;
	}

}
