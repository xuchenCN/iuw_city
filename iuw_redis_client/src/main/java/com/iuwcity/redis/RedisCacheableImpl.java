package com.iuwcity.redis;

import java.util.Map;

import com.iuwcity.storage.cache.RedisCacheable;

/**
 * 需要存放到Redis中的对象则实现此类
 * @author <a target=blank href=http://wpa.qq.com/msgrd?V=1&Uin=25878467&Exe=QQ&Site=im.qq.com&Menu=No><img border="0" SRC=http://wpa.qq.com/pa?p=1:25878467:1 alt="���ҷ���Ϣ"></a><a href=mailto:chenxu198511@gmail.com>xuchen</a>
 *
 */
public interface RedisCacheableImpl extends RedisCacheable {
	
	public String getKey();
	
	public Map<String,String> getValuesMap();
	
	public int getId();
	
	public void setId(int id);
}
