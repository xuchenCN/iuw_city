package com.iuwcity.storage.cache;

import java.io.Serializable;

public interface Cacheable extends Serializable{
	public String getKey();
	public int getId();
}
