package com.iuwcity.memcachedha.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientFactory {

	private int workPoolSize = DEFAULT_WORK_POOL_SIZE;
	
	private int opTimeout = DEFAULT_TIME_OUT;

	private Map<String, Client> clientMap = new HashMap<String, Client>(16);

	public ClientFactory() {
	}

	public ClientFactory(int workPoolSize) {
		this.workPoolSize = workPoolSize;
	}

	public int getWorkPoolSize() {
		return workPoolSize;
	}

	public void setWorkPoolSize(int workPoolSize) {
		this.workPoolSize = workPoolSize;
	}
	
	public Client getDefaultClient(final String ip, final int port ) throws IOException {
		Client client = clientMap.get(ip + port);
		if (client == null) {
			client = new DefaultClient(ip, port,workPoolSize,opTimeout);
			clientMap.put(ip + port, client);
		}

		return client;
	}
	
	public Client getDefaultClient(final String ip, final int port ,int workPoolSize,int opTimeout) throws IOException {
		Client client = clientMap.get(ip + port);
		if (client == null) {
			client = new DefaultClient(ip, port,workPoolSize,opTimeout);
			clientMap.put(ip + port, client);
		}

		return client;
	}

	public Client getDefaultClient(final String ip, final int port,int opTimeout) throws IOException {
		Client client = clientMap.get(ip + port);
		if (client == null) {
			client = new DefaultClient(ip, port,workPoolSize,opTimeout);
			clientMap.put(ip + port, client);
		}

		return client;
	}

	public static final int DEFAULT_WORK_POOL_SIZE = 4;
	public static final int DEFAULT_TIME_OUT = 5000;
}
