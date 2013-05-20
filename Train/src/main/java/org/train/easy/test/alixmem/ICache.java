package org.train.easy.test.alixmem;

public interface ICache {
	public void doPut(String key ,Object value);
	public String doGet(String key);
}
