package com.iuwcity.memcachedha;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MemcachedHA<T> {

	protected MemcachedClientDispatch dispatch = new MemcachedClientDispatch();

	public MemcachedClientDispatch getDispatch() {
		return dispatch;
	}

	public void setDispatch(MemcachedClientDispatch dispatch) {
		this.dispatch = dispatch;
	}

	public void addServer(T server, String ip, int port) throws IOException {
		MemcachedClientKeeper<T> k = new MemcachedClientKeeper<T>(server, ip, port);
		dispatch.addKeeper(k);
	}

	public Object set(String key, int expiry, Object value) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("set", new Class[] { String.class, Integer.class, String.class }, new Object[] {
				key, expiry, value });
		return dispatch.command(key, cp);
	}

	public Object set(String key, int expiry, Object value, boolean needSync) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("set", new Class[] { String.class, Integer.class, String.class }, new Object[] {
				key, expiry, value });
		return dispatch.command(key, cp, needSync);
	}

	public Object add(String key, int expiry, Object value) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("add", new Class[] { String.class, Integer.class, String.class }, new Object[] {
				key, expiry, value });
		return dispatch.command(key, cp);
	}

	public Object add(String key, int expiry, Object value, boolean needSync) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("add", new Class[] { String.class, Integer.class, String.class }, new Object[] {
				key, expiry, value });
		return dispatch.command(key, cp, needSync);
	}

	public Object get(String key) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("get", new Class[] { String.class }, new Object[] { key, });
		return dispatch.command(key, cp);
	}

	public Object get(String key, final int reload, final int reloadExpiry) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		CommandParam cp = new CommandParam("get", new Class[] { String.class }, new Object[] { key, });
		return dispatch.command(key, cp,false,reload,reloadExpiry);
	}
}
