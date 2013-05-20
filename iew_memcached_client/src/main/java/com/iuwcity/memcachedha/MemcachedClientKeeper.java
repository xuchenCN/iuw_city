package com.iuwcity.memcachedha;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.iuwcity.memcachedha.client.Client;

public class MemcachedClientKeeper<T> {
	private volatile int status;
	private T clientInstance;
	private String ip;
	private int port;
	
	private Client client;
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getClientInstance() {
		return clientInstance;
	}

	public void setClientInstance(T clientInstance) {
		this.clientInstance = clientInstance;
	}

	public MemcachedClientKeeper(T memcachedClientInstance, String ip, int port) {
		this.clientInstance = memcachedClientInstance;
		this.ip = ip;
		this.port = port;
	}

	public Object invokeMethod(final CommandParam param)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method method = clientInstance.getClass().getMethod(param.getMethodName(), param.getTypes());
		return method.invoke(this.clientInstance, param.getArgs());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MemcachedClientKeeper) {
			MemcachedClientKeeper<?> mk = (MemcachedClientKeeper<?>) obj;
			return (this.getIp().equals(mk.getIp()) && this.getPort() == mk.getPort());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (this.getIp() + this.getPort()).hashCode();
	}

	public static void main(String[] args) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
	}
}
