package com.cache.redis.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cache.redis.model.SampleData;
import com.cache.redis.service.RedisService;

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
	
	//private static final Logger logger = LoggerFactory.getLogger(RedisController.class);
	
	/**
	 * 
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ResponseBody
	public SampleData saveData(@RequestBody List<SampleData> data) {
		//logger.info("Starting saveData() RedisController.");
		SampleData sampleData = redisService.saveData(data);
		return sampleData;
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
	public SampleData updateData(@PathVariable String username,@RequestBody SampleData data) {
		//logger.info("Starting saveData() RedisController.");
		SampleData sampleData = redisService.saveData(username,data);
		return sampleData;
	}
	
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	@ResponseBody
	public Boolean deleteData(@PathVariable String username) {
		//logger.info("Starting saveData() RedisController.");
		Boolean flag = redisService.deleteData(username);
		return flag;
	}
}
