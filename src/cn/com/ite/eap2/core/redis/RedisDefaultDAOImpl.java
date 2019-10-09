package cn.com.ite.eap2.core.redis;

import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.common.utils.StringUtils;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * <p>Title cn.com.ite.eap2.core.redis.RedisDefaultDAOImpl</p>
 * <p>Description Redis基础接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年12月3日 下午2:58:28
 * @version 1.0
 * 
 * @modified records:
 */
public class RedisDefaultDAOImpl implements RedisDefaultDAO{

	    protected RedisTemplate<String, Serializable> redisTemplate;

	    /**
	     * 设置redisTemplate
	     *
	     * @param redisTemplate the redisTemplate to set
	     */
	    public void setRedisTemplate(RedisTemplate<String, Serializable> redisTemplate) {
	        this.redisTemplate = redisTemplate;
	        
	    }

	    public RedisTemplate<String, Serializable> getRedisTemplate() {
	        return redisTemplate;
	    }
	    
	    /**
	     * 选择库
	     * @description
	     * @param index
	     * @modified
	     */
	    public void select(int dbIndex){
	    	JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
	    	jedisConnectionFactory.setDatabase(dbIndex);
	    	redisTemplate.setConnectionFactory(jedisConnectionFactory);
	    }

	    /**
	     * 获取 RedisSerializer
	     */
	    private RedisSerializer<String> getRedisSerializer() {
	        return redisTemplate.getStringSerializer();
	    }


	    @Override
	    public <T> boolean add(final String key, final T obj) {
	        return add(key, JsonUtils.toJson(obj));
	    }

	    @Override
	    public <T> void add(final String key, final long timeout, final T obj) {
	        redisTemplate.execute(new RedisCallback() {
	            public Object doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                final byte[] object = serializer.serialize(JsonUtils.toJson(obj));
	                add(key,timeout,object);
	                return null;
	            }


	        });
	    }


	    @Override
	    public void add(final String key, final long timeout,final byte[] object) {
	        redisTemplate.execute(new RedisCallback() {
	            public Object doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                connection.setEx(keyStr, timeout, object);
	                return null;
	            }
	        });
	    }


	    @Override
	    public boolean add(final String key, final String value) {
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] object = serializer.serialize(value);
	                return connection.setNX(keyStr, object);
	            }
	        });
	        return result;
	    }

	    @Override
	    public <T> boolean add(final String key, final List<T> list) {
	        return this.add(key, JsonUtils.toJson(list));
	    }


	    @Override
	    public void delete(final String key) {
	        List<String> list = new ArrayList<>();
	        list.add(key);
	        this.delete(list);
	    }

	    @Override
	    public void delete(final List<String> keys) {
	        redisTemplate.delete(keys);
	    }

	    @Override
	    public <T> boolean update(final String key, final T obj) {
	        return this.update(key, JsonUtils.toJson(obj));
	    }


	    @Override
	    public boolean update(final String key, final String value) {
	        if (get(key) == null) {
	            throw new NullPointerException("数据行不存在, key = " + key);
	        }
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] valueStr = serializer.serialize(value);
	                connection.set(keyStr, valueStr);
	                return true;
	            }
	        });
	        return result;
	    }

	    @Override
	    public boolean save(String key, String value) {
	        if (StringUtils.isEmpty(get(key))) {
	            return this.add(key, value);
	        } else {
	            return this.update(key, value);
	        }
	    }

	    @Override
	    public <T> boolean save(String key, T obj) {
	        if (get(key, obj.getClass()) == null) {
	            return this.add(key, obj);
	        } else {
	            return this.update(key, obj);
	        }
	    }

	    @Override
	    public <T> T get(final String key, final Class clazz) {
	        T result = redisTemplate.execute(new RedisCallback<T>() {
	            public T doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] value = connection.get(keyStr);
	                if (value == null) {
	                    return null;
	                }
	                String valueStr = serializer.deserialize(value);
	                return (T) JsonUtils.fromJson(valueStr, clazz);
	            }
	        });
	        return result;
	    }


	    @Override
	    public <T> List<T> getList(final String key, final Class<T> clazz) {
	        List<T> result = redisTemplate.execute(new RedisCallback<List<T>>() {
	            public List<T> doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] value = connection.get(keyStr);
	                if (value == null) {
	                    return null;
	                }
	                String valueStr = serializer.deserialize(value);
	                List<T> list = JsonUtils.fromJson(valueStr, ArrayList.class);
	                return list;
	            }
	        });
	        return result;
	    }
	    
	    @Override
	    public <T> Map<String,T> getMap(final String key){
	    	Map<String,T> result = redisTemplate.execute(new RedisCallback<Map<String,T>>() {
	            public Map<String,T> doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] value = connection.get(keyStr);
	                if (value == null) {
	                    return null;
	                }
	                String valueStr = serializer.deserialize(value);
	                Map<String,T> map = JsonUtils.fromJson(valueStr, Map.class);
	                return map;
	            }
	        });
	        return result;
	    }



	    @Override
	    public String get(final String key) {
	        String result = redisTemplate.execute(new RedisCallback<String>() {
	            public String doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] value = connection.get(keyStr);
	                if (value == null) {
	                    return null;
	                }
	                String valueStr = serializer.deserialize(value);
	                return valueStr;
	            }
	        });
	        return result;
	    }

	    @Override
	    public byte[] getByte(final String key) {
	        byte[] result = redisTemplate.execute(new RedisCallback<byte[]>() {
	            public byte[] doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                byte[] value = connection.get(keyStr);
	                return value;
	            }
	        });
	        return result;
	    }

	    @Override
	    public Set<String> keys(final String pattern) {
	        return redisTemplate.keys(pattern);
	    }

	    @Override
	    public boolean exist(final String key){
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                return connection.exists(keyStr);
	            }
	        });
	        return result;
	    }


	    @Override
	    public boolean set(final String key,final byte[] value){
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                RedisSerializer<String> serializer = getRedisSerializer();
	                byte[] keyStr = serializer.serialize(key);
	                connection.set(keyStr, value);
	                return true;
	            }
	        });
	        return result;
	    }

	    public boolean flushDB(){
	        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
	            public Boolean doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                connection.flushDb();
	                return true;
	            }
	        });
	        return result;
	    }

	    public long dbSize(){
	        long result = redisTemplate.execute(new RedisCallback<Long>() {
	            public Long doInRedis(RedisConnection connection)
	                    throws DataAccessException {
	                return connection.dbSize();
	            }
	        });
	        return result;
	    }
	    
}
