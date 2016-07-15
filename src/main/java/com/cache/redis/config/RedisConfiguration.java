package com.cache.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

@Configuration
public class RedisConfiguration {

	@Value("${redis.host}")
	private String redisHostName;

	@Value("${redis.port}")
	private int redisPort;
	
//	@Value("${redis.password}")
//	private String redisPassword;
	
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHostName);
		factory.setPort(redisPort);
		factory.setUsePool(true);
		//factory.setPassword(redisPassword);
		return factory;
	}

	@Bean
	  public JacksonJsonRedisSerializer<Object> jacksonJsonRedisJsonSerializer() {
	      return new JacksonJsonRedisSerializer<>(Object.class);
	  }
	
	@SuppressWarnings("rawtypes")
	@Bean
	public RedisTemplate redisTemplate(RedisConnectionFactory cf) {
		 RedisTemplate redisTemplate = new RedisTemplate();
		 redisTemplate.setConnectionFactory(cf);
		 //redisTemplate.setValueSerializer(jacksonJsonRedisJsonSerializer());
		 //redisTemplate.setDefaultSerializer(jacksonJsonRedisJsonSerializer());
		return redisTemplate;
	}

	@SuppressWarnings("rawtypes")
	@Bean
	public RedisCacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		return redisCacheManager;
	}
}
