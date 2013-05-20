package com.iuwcity.memcachedha;

public interface ReloadLevel {
	public static final int UNRELOAD = 0x00;
	public static final int ONLYMISS = 0x01;
	public static final int CHECKALL = 0x02;
}
