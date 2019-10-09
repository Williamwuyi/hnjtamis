package cn.com.ite.eap2.core.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * <p>Title cn.com.ite.eap2.core.hibernate.DefaultDAO</p>
 * <p>Description 缺省DAO接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 9:07:31 AM
 * @version 2.0
 * 
 * @modified records:
 */
public interface DefaultDAO {
	/**
	 * 主健查询
	 * @param key 主健
	 * @return  实体对象
	 */
	Serializable findEntityBykey(Serializable key);
	/**
	 * 查询所有数据
	 * @param entityClass
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List findAll(Class entityClass);
	/**
	 * 根据指定属性查询数据
	 * @param entityClass 类型
	 * @param field 属性名
	 * @param fieldValue 属性值
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List findEntityByField(Class entityClass,String field,String fieldValue);
	
	/**
	 * 根据类型及主健查询实体
	 * @param entityClass 类型
	 * @param key 主健
	 * @return 实体对象
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	Serializable findEntityBykey(Class entityClass,Serializable key);

	/**
	 * 增加实体
	 * @param entity 实体
	 */
	void addEntity(Serializable entity);

	/**
	 * 更新实体
	 * @param entity 实体
	 */
	void updateEntity(Serializable entity);

	/**
	 * 保存实体
	 * @param entity 实体
	 */
	void saveEntity(Serializable entity);
	/**
	 * 保存序列化实体
	 * @param entity
	 * @modified
	 */
	void saveEntityOld(Serializable entity);

	/**
	 * 批量保存实体
	 * @param entitys 实体集合
	 */
	@SuppressWarnings("unchecked")
	void saveBatchEntity(Collection entitys);

	/**
	 * 删除实体
	 * @param entity 实体
	 */
	void deleteEntity(Serializable entity);

	/**
	 * 根据主健删除实体
	 * @param key 主健
	 */
	void deleteEntityByKey(Serializable key);

	/**
	 * 批量删除实体
	 * @param entitys 实体
	 */
	@SuppressWarnings("unchecked")
	void deleteBatchEntity(Collection entitys);

	/**
	 * 批量删除实体，根据主健组
	 * @param keys 主健数组
	 */
	void deleteBatchEntityByKeys(Serializable[] keys);

	/**
	 * 批量删除实体，根据主健组和类型
	 * @param keys 主健数组
	 * @param entityClass 实体类型
	 */
	@SuppressWarnings("unchecked")
	void deleteBatchEntityByKeys(Serializable[] keys, Class entityClass);
	
	/**
	 * 根据配置的QL语句查询
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param entityClass 返回实体类型
	 * @return  查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryConfigQl(String configQlName,Object term,Map<String,Boolean> sortMap,Class entityClass);
	/**
	 * 执行配置QL语句
	 * @param configQlName 配置名称
	 * @param params 参数ֵ
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	void excuteQl(String configQlName,List params);
	/**
	 * 查询字段最大值
	 * @param entityClass 实体类型
	 * @param fieldName 字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	int getFieldMax(Class entityClass,String fieldName);
	/**
	 * 查询字段最大值
	 * @param entityClass 实体类型
	 * @param fieldName 字段名
	 * @param parentFieldName 父字段名
	 * @param parentFieldValue 父字段值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	int getFieldMax(Class entityClass,String fieldName,String parentFieldName,Object parentFieldValue);
	
	/**
	 * 根据配置的QL语句查询结果数量
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @return 结果数量
	 * @modified
	 */
	int countConfigQl(String configQlName,Object term);
	/**
	 * 根据配置的QL语句查询
	 * @param configQlName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param entityClass 返回实体类型
	 * @param startNo 开始行号
	 * @param rowSize 页记录数
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryConfigQl(String configQlName,Object term,Map<String,Boolean> sortMap,Class entityClass,int startNo,int rowSize);
	/**
	 * 刷新对象
	 * @param entity 实体
	 */
	void refreshObject(Serializable entity);
	/**
	 * 获得当前实体类型
	 * @return 当前实体类型
	 */
	@SuppressWarnings("unchecked")
	Class getEntityClass();
	/**
	 * 获得实体类型
	 * @param entity 实体
	 * @return 实体类型
	 * @modified 
	 */
	@SuppressWarnings("unchecked")
	Class getEntityClass(Serializable entity);
	/**
	 * 清除缓存
	 * @param entity 实体
	 */
	void clearCache(Serializable entity);
	/**
	 * 获得所有数据
	 * @param entityClass 实体类型
	 * @return 实体数据
	 */
	@SuppressWarnings("unchecked")
	List getAll(Class entityClass);
	/**
	 * 获得实体主健
	 * @param entity 实体
	 * @return:主健
	 */
	Serializable getIdentifier(Object entity);
	/**
	 * 实体转成PO对象
	 * @param entity 实体
	 * @return PO对象
	 * @modified
	 */
	Serializable entityToPo(Serializable entity);
	/**
	 * 复制
	 * @param entity 实体对象
	 * @return  复制对象
	 */
	Serializable copy(Serializable entity);
	/**
	 * 删除无关联的附件
	 * @param accessIds 附件ID数组
	 * @param busId 业务ID
	 */
	void deleteNoLinkAccessory(String[] accessIds,String busId) throws Exception;
	/**
	 * 查询指定主健的实体的子对象
	 * @param clazz 实体类型
	 * @param keyColumnName 主健字段名
	 * @param keyValue 主健值
	 * @param childColumnName 子对象字段名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	Collection findEntitySub(String clazz,String keyColumnName,String keyValue,String childColumnName);
}