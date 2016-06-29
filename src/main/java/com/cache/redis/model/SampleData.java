package com.cache.redis.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class SampleData implements Serializable{

	@NotNull
	private String username;//unique key
	private String name;
	private Integer age;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	
}
