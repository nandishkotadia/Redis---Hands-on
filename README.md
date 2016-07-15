# Redis---Hands-on

Redis is an open source (BSD licensed), in-memory data structure store, used as database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs and geospatial indexes with radius queries. Redis has built-in replication, Lua scripting, LRU eviction, transactions and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.
You can run atomic operations on these types, like appending to a string; incrementing the value in a hash; pushing an element to a list; computing set intersection, union and difference; or getting the member with highest ranking in a sorted set.

In order to achieve its outstanding performance, Redis works with an in-memory dataset. Depending on your use case, you can persist it either by dumping the dataset to disk every once in a while, or by appending each command to a log. Persistence can be optionally disabled, if you just need a feature-rich, networked, in-memory cache.
Redis also supports trivial-to-setup master-slave asynchronous replication, with very fast non-blocking first synchronization, auto-reconnection with partial resynchronization on net split.

Other features include:
####Transactions
####Pub/Sub
####Lua scripting
####Keys with a limited time-to-live
####LRU eviction of keys
####Automatic failover

###Code walkthrough:

The data structure I have used is basic opsForHash redisOperation.
It can have 2 keys and a data:
i) primary key ii) secondary key iii) data

There are various data structures supported in Redis like Set, List, SortedSet, etc. Data structure to be chosen should be based on your use case scenario.

To explain this in simple way w.r.t SQL 
Consider following example:

Suppose there is user table, with userId as the primary key
####Case 1: To retrieve all user records,
SQL query will be  
####SELECT * FROM User;

####Case 2: To retrieve user record by userId,
SQL query will be 
####SELECT * FROM User WHERE userId = ?

So similarly above data can be stored in redis in similar way

####Primary key    Secondary key   Data
####User      	userId		 entire SQL row i.e user object

To run case 1:
query will be like find by primary key.

To run case 2:
query will be like find by primary key & secondary key.


####src/main/java/com/cache/redis/util/RedisCacheUtilServiceImpl.java

It provides the wrapper for the CRUD operations in REDIS. It requires the data to be passed in RedisParam.java object.

RedisParam.java contains the following fields:
####1) T data - data to be stored.
####2) String key - primary key for the data.
####3) String secondaryKey - secondary key for the data.


Also, it provides the rate limit utility method to perform rate limiting using sliding time window,
checkIfRateLimitReached()
It has 2 parameters:
1) window - time window in ms.
2) limit - no. of requests that can be made in that time window.  
  
So ‘limit’ requests can be made in ‘window’ time window for a given clientId.

This could be obtained from redis or some table to make it client specific and keep this parameters dynamic.

To test the code:

Step 1: Run the project in tomcat server

Step 2:

	2.1) Create 
		Api url: http://localhost:8080/redis/data
		HttpMethod : POST
		Request Body: [{“username”:”nandish”,”age”:22,”name”:”Nandish Kotadia”}]
		HttpHeaders: Content-Type : application/json

	2.2) Retrieve (Get)
		Api url: http://localhost:8080/redis/data/:username
		HttpMethod : GET

	2.3) Update)
		Api url: http://localhost:8080/redis/data/:username
		HttpMethod : PUT
		Request Body: {“username”:”nandish”,”age”:22,”name”:”Nandish dream Kotadia”}
		HttpHeaders: Content-Type : application/json

	2.4) DELETE
		Api url: http://localhost:8080/redis/data/:username
		HttpMethod : DELETE
	
	You can find this APIs in src/main/java/com/cache/redis/controller/RedisController.java
####Note: For the people, who don’t know how to test can use Postman extension in Google Chrome. 

Step 3:

	To test Redis Ratelimit
	Api url: http://localhost:8080/redis/data/ratelimit/nandish?clientId=24
	HttpMethod : GET
		
	clientId - client id of the client from which client the request is coming.
	This can be obtained from session in the ideal scenario.

	Try hitting the above API continuously many times 
	(when it will have more than 3 times in time window 10 seconds, and you will receive 403 (FORBIDDEN) in 	response 
	and on 	eclipse you can see the “Max limit reached “ text. )


##Note: It can also be used to store session which is most frequently used for every API request. Also constant and most frequently used data like Country-State-pincode data, Top 5 post of user, Recent ’n’ tweets of user, etc.  
 