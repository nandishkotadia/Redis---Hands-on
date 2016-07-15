package com.cache.redis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cache.redis.model.SampleData;
import com.cache.redis.service.RedisService;
import com.cache.redis.util.RedisCacheUtilServiceImpl;

/**
 * @author Nandish Kotadia
 * 
 * Handles requests for the Redis CRUD.
 */
@Controller
@RequestMapping("/data")
public class RedisController {
	
	@Autowired
	RedisService redisService;
	
	@SuppressWarnings("rawtypes")
	@Autowired
	RedisCacheUtilServiceImpl redisUtility;
	
	/**
	 * 
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public void saveData(@RequestBody List<SampleData> data) {
		//logger.info("Starting saveData() RedisController.");
		redisService.saveData(data);
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.GET)
	@ResponseBody
	public SampleData getData(@PathVariable String username) {
		//logger.info("Starting saveData() RedisController.");
		SampleData sampleData = redisService.getData(username);
		return sampleData;
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateData(@PathVariable String username,@RequestBody SampleData data) {
		//logger.info("Starting saveData() RedisController.");
		redisService.updateData(username,data);
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseBody
	public void deleteData(@PathVariable String username) {
		//logger.info("Starting saveData() RedisController.");
		redisService.deleteData(username);
	}
	
	
	/**
	 * To Test RateLimit
	 * currently the parameters are set to the following values:
	 *  		 timewindow = 10 seconds
	 *           limit = 3 request
	 * i.e. User can make 3 hits/requests in a time window of 10 seconds.
	 * 
	 * @param username
	 * @param clientId
	 * @return
	 */
	@RequestMapping(value = "/ratelimit/{username}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<SampleData> testRatelimit(@PathVariable String username,@RequestParam Long clientId) {
		//logger.info("Starting saveData() RedisController.");
		//API Rate Limit check
		String status = redisUtility.checkIfRateLimitReached(clientId);
		if("MAX_LIMIT_REACHED".equals(status)){
			return new ResponseEntity<SampleData>(HttpStatus.FORBIDDEN);
		}
		SampleData sampleData = redisService.getData(username);
		return new ResponseEntity<SampleData>(sampleData,HttpStatus.OK);
	}
}
