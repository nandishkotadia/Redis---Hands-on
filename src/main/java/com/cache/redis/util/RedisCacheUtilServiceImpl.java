package com.cache.redis.util;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cache.redis.model.RedisParam;

@Service
public class RedisCacheUtilServiceImpl<T> /*implements RedisCacheUtilService<T>*/{
	
	private static final Logger logger = LoggerFactory.getLogger(RedisCacheUtilServiceImpl.class);
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;	
	
	//@Override
	@SuppressWarnings("rawtypes")
	public void save(List<RedisParam> objectToCache) {
		this.doProcessing(objectToCache);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doProcessing(List<RedisParam> dto){
		try{
			if(dto !=null && dto.size()>0){
				for(RedisParam c:dto){
					String tableKey = c.getKey();
					String rowKey = c.getSecondaryKey();
					redisTemplate.opsForHash().put(tableKey, rowKey, c.getData());
				}
			}
		}catch(Exception ex){
			logger.error("RedisCacheUtilServiceImpl :: doProcessing():: ERROR IN REDIS"+ ex.getMessage()); 
		}
	}
	
	//@Override
	@SuppressWarnings("unchecked")
	public void remove(String key, String secondaryKey){
		try{
			redisTemplate.opsForHash().delete(key, secondaryKey);
		}catch(Exception ex){
			logger.error("RedisCacheUtilServiceImpl :: remove(2 param):: ERROR IN REDIS"+ ex.getMessage());
		}
	}
	
	//@Override
	@SuppressWarnings("unchecked")
	public void remove(String key){
		try{
			redisTemplate.delete(key);
		}catch(Exception ex){
			logger.error("RedisCacheUtilServiceImpl :: remove(1 param):: ERROR IN REDIS"+ ex.getMessage());
		}
	}
	
	//FlushOut Cache
	//@Override
	@SuppressWarnings("rawtypes")
	public void update(List<RedisParam> updatedDto){
		//this.removeFromCache(clientPropertyList);
		this.doProcessing(updatedDto);
	}
	
	//@Override
	@SuppressWarnings("unchecked")
	public T findByTableIdAndKey(String tableKey, String rowKey){
		try{
			T response =  (T) redisTemplate.opsForHash().get(tableKey,rowKey);
			return response;
		}catch(Exception ex){
			logger.error("RedisCacheUtilServiceImpl :: findByTableIdAndKey():: ERROR IN REDIS" + ex.getMessage());
		}
		return null;
	}
	
	//@Override
	@SuppressWarnings("unchecked")
	public T findByTableId(String key) {
		T responseList = null;
		try{
			responseList = (T)redisTemplate.opsForHash().values(key);
		}catch(Exception ex){
			logger.error("RedisCacheUtilServiceImpl :: findByTableId():: ERROR IN REDIS"+ ex.getMessage());
		}
		return responseList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String checkIfRateLimitReached(Long clientId) {
		try{
			String status = (String)
			redisTemplate.execute(new SessionCallback() {
				@Override
				public Object execute(RedisOperations operations) throws DataAccessException {
					long window = 10;//seconds
					
					long limit = 3;//max no. of requests
					long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
					long expires = currentTime - window;
					long now = currentTime;
					operations.watch("API-"+clientId.toString());
					operations.opsForZSet().removeRangeByScore("API-"+clientId.toString(),0,expires);//remove all the request which are out of timewindow
					long count = operations.opsForZSet().zCard("API-"+clientId.toString());
					if(count >= limit){
						logger.info("Max request limit reached.. Please try after some time");
						return "MAX_LIMIT_REACHED";
					}
					operations.multi();
					operations.opsForZSet().add("API-"+clientId.toString(), (now+""),now); //add the current into time window
					operations.exec();
					System.out.println(count+1);
					return "SUCCESS";
				}

			});
			return status;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		return null;
	}
	
	
}
