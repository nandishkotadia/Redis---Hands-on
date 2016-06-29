package com.cache.redis.model;

import javax.validation.constraints.NotNull;

public class RedisParam<T> {

	@NotNull
	private String key;//First Row Key
	
	@NotNull
	private String secondaryKey;//Property Key
	
	@NotNull
	private T data;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecondaryKey() {
		return secondaryKey;
	}

	public void setSecondaryKey(String secondaryKey) {
		this.secondaryKey = secondaryKey;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
