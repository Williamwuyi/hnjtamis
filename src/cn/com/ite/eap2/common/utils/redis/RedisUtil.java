package cn.com.ite.eap2.common.utils.redis;

import cn.com.ite.eap2.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	private final static String host = Config.getPropertyValue("redis.host");
	private final static int port = Integer.parseInt(Config.getPropertyValue("redis.port"));
	private final static String password =  Config.getPropertyValue("redis.password");
	private final static int maxTotal =  Integer.parseInt(Config.getPropertyValue("redis.maxTotal"));
	private final static int maxIdle =  Integer.parseInt(Config.getPropertyValue("redis.maxIdle"));
	private final static int minIdle =  Integer.parseInt(Config.getPropertyValue("redis.minIdle"));
	private final static int maxWaitMillis =  Integer.parseInt(Config.getPropertyValue("redis.maxWaitMillis"));
	private final static String testOnBorrow =  Config.getPropertyValue("redis.testOnBorrow");
	private static JedisPool pool = null;
	
	/**
     * 构建redis连接池
     * 
     * @param ip
     * @param port
     * @return JedisPool
     */
    public static JedisPool getPool() {
    	if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxTotal);
			config.setMaxIdle(maxIdle);
			config.setMinIdle(minIdle);//设置最小空闲数
			config.setMaxWaitMillis(maxWaitMillis);
			config.setTestOnBorrow("true".equals(testOnBorrow));
			config.setTestOnReturn(true);
			//Idle时进行连接扫描
			config.setTestWhileIdle(true);
			//表示idle object evitor两次扫描之间要sleep的毫秒数
			config.setTimeBetweenEvictionRunsMillis(30000);
			//表示idle object evitor每次扫描的最多的对象数
			config.setNumTestsPerEvictionRun(10);
			//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
			config.setMinEvictableIdleTimeMillis(60000);
	
			pool = new JedisPool(config, host, port, 10000, password, 0);
    	}
		return pool;
	}
    
    /**
     * 返还到连接池
     * 
     * @param pool 
     * @param redis
     */
    public static void returnResource(JedisPool pool, Jedis jedis) {
       if (jedis != null) {  
    	  jedis.close();
       } 
    }
    
    /**
     * 获取数据
     * 
     * @param key
     * @return
     */
    public static String get(String key){
        String value = null;
        JedisPool pool = null;
        Jedis jedis = null;
        try {
            pool = getPool();
            jedis = pool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            //释放redis对象
        	returnResource(pool, jedis);
            e.printStackTrace();
        } finally {
            //返还到连接池
            returnResource(pool, jedis);
        }
        
        return value;
    }
    
    public static void main(String[] args){
    	System.out.println(RedisUtil.get("mykey"));
    }
}
