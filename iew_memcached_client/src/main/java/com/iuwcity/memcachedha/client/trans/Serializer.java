package com.iuwcity.memcachedha.client.trans;

import com.iuwcity.memcachedha.client.CommandData;

public interface Serializer {

	public CommandData encode(Object o);

	public Object decode(CommandData commandData);
}
