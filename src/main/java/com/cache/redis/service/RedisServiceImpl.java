package com.cache.redis.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cache.redis.model.RedisParam;
import com.cache.redis.model.SampleData;
import com.cache.redis.util.RedisCacheUtilServiceImpl;

@Service
public class RedisServiceImpl implements RedisService{

	@SuppressWarnings("rawtypes")
	@Autowired
	RedisCacheUtilServiceImpl redisUtility;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SampleData saveData(List<SampleData> data) {
		List<RedisParam> dataCacheObjects = new ArrayList<RedisParam>();
		for(SampleData s:data){
			RedisParam r = new RedisParam();
			r.setKey(getPrimary());
			r.setSecondaryKey(getSecondaryKey(s.getUsername()));
			r.setData(s);
			dataCacheObjects.add(r);
		}
		redisUtility.save(dataCacheObjects);
		return null;
	}

	private String getSecondaryKey(String username) {
		return username;
	}

	private String getPrimary() {
		return "DATA";
	}

	@Override
	public SampleData getData(String username) {
		SampleData s = (SampleData) redisUtility.findByTableIdAndKey(getPrimary(), getSecondaryKey(username));
		return s;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public SampleData saveData(String username, SampleData data) {
		List<RedisParam> dataCacheObjects = new ArrayList<RedisParam>();
		RedisParam r = new RedisParam();
		r.setKey(getPrimary());
		r.setSecondaryKey(getSecondaryKey(data.getUsername()));
		r.setData(data);
		dataCacheObjects.add(r);
		redisUtility.save(dataCacheObjects);
		return null;
	}

	@Override
	public Boolean deleteData(String username) {
		redisUtility.remove(getPrimary(), getSecondaryKey(username));
		return true;
	}

	
}
