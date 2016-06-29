package com.cache.redis.util;

import java.util.List;

import com.cache.redis.model.RedisParam;

public interface RedisCacheUtilService<T> {

	public void save(List<RedisParam<T>> objectToCache);
	public void remove(String key, String secondaryKey);
	public void remove(String key);
	public void update(List<RedisParam<T>> updatedDto);
	public T findByTableIdAndKey(String tableKey, String rowKey);
	public T findByTableId(String key);
	
}
