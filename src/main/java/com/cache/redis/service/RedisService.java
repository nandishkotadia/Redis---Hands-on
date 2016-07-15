package com.cache.redis.service;

import java.util.List;

import com.cache.redis.model.SampleData;

public interface RedisService {

	void saveData(List<SampleData> data);

	SampleData getData(String username);

	void updateData(String username, SampleData data);

	void deleteData(String username);

}
