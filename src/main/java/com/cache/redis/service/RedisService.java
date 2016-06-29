package com.cache.redis.service;

import java.util.List;

import com.cache.redis.model.SampleData;

public interface RedisService {

	SampleData saveData(List<SampleData> data);

	SampleData getData(String email);

	SampleData saveData(String email, SampleData data);

	Boolean deleteData(String email);

}
