package cn.com.ite.eap2.core.service;

import java.util.*;

import cn.com.ite.eap2.core.redis.RedisDefaultDAO;

public class DefaultRedisServiceImpl implements DefaultRedisService{

	//redis基础接口
	private RedisDefaultDAO redisDao;
		
	public RedisDefaultDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(RedisDefaultDAO redisDao) {
		this.redisDao = redisDao;
	}
		
	public <T> boolean add(String key, T obj) {
		return getRedisDao().add(key, obj);
	}

	public void select(int dbIndex) {
		getRedisDao().select(dbIndex);
		
	}

	public boolean add(String key, String value) {
		return getRedisDao().add(key, value);
	}

	public <T> boolean add(String key, List<T> list) {
		return getRedisDao().add(key, list);
	}

	public void delete(String key) {
		getRedisDao().delete(key);
	}

	@Override
	public void delete(List<String> keys) {
		getRedisDao().delete(keys);
	}

	public <T> boolean update(String key, T obj) {
		return getRedisDao().update(key, obj);
	}

	public boolean update(String key, String value) {
		return getRedisDao().update(key, value);
	}

	public boolean save(String key, String value) {
		return getRedisDao().save(key, value);
	}

	public <T> boolean save(String key, T obj) {
		return getRedisDao().save(key, obj);
	}

	public <T> T get(String key, Class clazz) {
		return getRedisDao().get(key,clazz);
	}

	public <T> List<T> getList(String key, Class<T> clazz) {
		return getRedisDao().getList(key, clazz);
	}
	
	public <T> Map<String,T> getMap(String key){
		return getRedisDao().getMap(key);
	}

	public byte[] getByte(String key) {
		return getRedisDao().getByte(key);
	}

	public String get(String key) {
		return getRedisDao().get(key);
	}

	public <T> void add(String key, long timeout, T obj) {
		getRedisDao().add(key, timeout, obj);
	}

	public void add(String key, long timeout, byte[] object) {
		getRedisDao().add(key, timeout, object);
	}

	public Set<String> keys(String pattern) {
		return getRedisDao().keys(pattern);
	}

	public boolean exist(String key) {
		return getRedisDao().exist(key);
	}

	public boolean set(String key, byte[] value) {
		return getRedisDao().set(key, value);
	}

	public boolean flushDB() {
		return getRedisDao().flushDB();
	}

	public long dbSize() {
		return getRedisDao().dbSize();
	}
	
	
}