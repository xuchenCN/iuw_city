package com.iuwcity.memcachedha.listener;

import java.util.Map;

import com.iuwcity.memcachedha.CommandParam;
import com.iuwcity.memcachedha.MemcachedClientKeeper;

public interface CommandListener {

	void onCommandError(final MemcachedClientKeeper<?> keeper,final CommandParam param);

	void onCommandResponsed(final MemcachedClientKeeper<?> keeper, final CommandParam param, Object response);

	void onAllCommandResponsed(final Map<MemcachedClientKeeper<?>, Object> responses, final CommandParam param);
}
