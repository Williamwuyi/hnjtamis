package cn.com.ite.eap2.core.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * <p>Title cn.com.ite.eap2.core.redis.RedisDefaultDAO</p>
 * <p>Description Redis基础接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年12月3日 下午2:58:42
 * @version 1.0
 * 
 * @modified records:
 */
public interface RedisDefaultDAO {
	<T> boolean add(final String key, final T obj);

	/**
     * 选择库
     * @description
     * @param index
     * @modified
     */
    void select(int dbIndex);
    
    /**
     * setNx
     *
     * @param key
     * @param value
     * @return
     */
    boolean add(final String key, final String value);

    <T> boolean add(final String key, final List<T> list);

    void delete(final String key);

    void delete(final List<String> keys);

    <T> boolean update(final String key, final T obj);

    boolean update(final String key, final String value);

    /**
     * 保存 不存在则新建，存在则更新
     *
     * @param key
     * @param value
     * @return
     */
    boolean save(final String key, final String value);

    <T> boolean save(final String key, final T obj);

    <T> T get(final String key, final Class clazz);

    <T> List<T> getList(final String key, final Class<T> clazz);
    
    <T> Map<String,T> getMap(final String key);

    byte[] getByte(final String key);

    String get(final String key);

    <T> void add(final String key, final long timeout, final T obj);

    void add(final String key, final long timeout, final byte[] object);

    Set<String> keys(String pattern);

    boolean exist(final String key);

    boolean set(final String key,final byte[] value);

    boolean flushDB();

    long dbSize();
}