package com.iuwcity.memcachedha.client;


public class CommandData {

	private int flag;
	private byte[] valueData;

	public CommandData(int flag, byte[] valueData) {
		this.flag = flag;
		this.valueData = valueData;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public byte[] getValueData() {
		return valueData;
	}

	public void setValueData(byte[] valueData) {
		this.valueData = valueData;
	}

}
