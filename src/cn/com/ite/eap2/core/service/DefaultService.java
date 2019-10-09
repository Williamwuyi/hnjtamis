package cn.com.ite.eap2.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.exception.EapException;

/**
 * <p>Title cn.com.ite.eap2.core.service.DefaultService</p>
 * <p>Description 缺省服务接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 20, 2014 11:49:31 AM
 * @version 2.0
 * 
 * @modified records:
 */
public interface DefaultService {
	/**
	 * 查询数据
	 * @param queryName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param pageNo 开始行号
	 * @param rowSize 页记录数
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryData(String queryName,Object term,Map sortMap,int pageNo,int rowSize);
	/**
	 * 查询数据
	 * @param queryName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryData(String queryName,Object term,Map sortMap);
	/**
	 * 根据指定字段查询数据
	 * @param entityClass 实体类名
	 * @param field 字段
	 * @param fieldValue 字段值
	 * @return 对象集合
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List findEntityByField(Class entityClass,String field,String fieldValue);
	/**
	 * 查询数据
	 * @param queryName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param returnClass 返回类型
	 * @param pageNo 开始行号
	 * @param rowSize 页记录数
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryData(String queryName,Object term,Map sortMap, Class returnClass,int pageNo, int rowSize);
	/**
	 * 查询数据
	 * @param queryName 配置名称
	 * @param term 条件
	 * @param sortMap 排序
	 * @param returnClass 返回类型
	 * @return 查询结果
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryData(String queryName,Object term,Map sortMap, Class returnClass);
	/**
	 * 查询所有数据
	 * @param type 实体类型
	 * @return 数据
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	List queryAllDate(Class type);
	/**
	 * 查询数量
	 * @param queryName 配置名称
	 * @param term 条件
	 * @return 结果数量
	 * @modified
	 */
	int countData(String queryName,Object term);
	/**
	 * 主健查询对象
	 * @param key 主健
	 * @param type 实体类型
	 * @return 实体
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	Serializable findDataByKey(Serializable key,Class type);
	/**
	 * 是否存在指定主键和类型的对象
	 * @param key
	 * @param type
	 * @return
	 * @modified
	 */
	boolean existDate(Serializable key,Class type);
	/**
	 * 增加实体
	 * @param entity 实体
	 */
	void add(Serializable bo) throws EapException;
	/**
	 * 更新实体
	 * @param entity 实体
	 */
	void update(Serializable bo) throws EapException;
	/**
	 * 保存实体
	 * @param entity 实体
	 */
	void save(Serializable bo) throws EapException;
	/**
	 * 保存持久化的实体
	 * @param entity 实体
	 */
	void saveOld(Serializable bo) throws EapException;
	/**
	 * 批量保存实体
	 * @param bos 实体组
	 */
	@SuppressWarnings("unchecked")
	void saves(Collection bos) throws EapException;
	/**
	 * 删除对象
	 * @param bo 对象
	 * @modified
	 */
	void delete(Serializable bo) throws EapException;
	/**
	 * 批量删除对象
	 * @param bos 对象组
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	void deletes(Collection bos) throws EapException;
	/**
	 * 主健删除对象
	 * @param key 主健
	 * @param type 类型
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	void deleteByKey(Serializable key,Class type) throws EapException;
	/**
	 * 根据类型及主健组删除对象
	 * @param keys 主健组
	 * @param type 对象类型
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	void deleteByKeys(Serializable[] keys,Class type) throws EapException;
	/**
	 * 执行SQL语句
	 * @param configQlName 配置名称
	 * @param params 参数
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	void excuteQl(String configQlName,List params);
	/**
	 * 删除无关联附件
	 * @param accessIds 附件ID数组
	 * @param busId 业务ID
	 */
	void deleteNoLinkAccessory(String[] accessIds,String busId) throws Exception;
	/**
	 * 把上下关系的同一类型对象集合处理成有层次关系的集合
	 * @param coll 同一类型对象集合
	 * @param keyColumnName 主健字段名
	 * @param parentColumnName 父主键字段名
	 * @param childColumnName 子字段名
	 * @param nullFields 要设置为空的属性集
	 * @param copyFieldClassMap 要复制的属性映射,注意键为数组，分别表示 对象属性..对象中需要复制的属性组
	 * @param filterIds 过滤结点ID数组
	 * @param sortField 排序字段
	 * @param topId 顶级结点ID条件，意思是只显示此结点及以结点数据
	 */
	@SuppressWarnings("unchecked")
	List childObjectHandler(Collection coll, String keyColumnName,
			String parentColumnName, String childColumnName,String[] nullFields,
			Map<String[],Class> copyFieldClassMap,List<String> filterIds,
			String sortField,String topId) throws Exception;
	/**
	 * 查询字段最大值
	 * @param entityClass
	 * @param fieldName
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
	 * 级别编码处理
	 * @param entity 实体对象
	 * @param keyColumnName 主键字段名
	 * @param parentColumnName 父对象字段名
	 * @param childColumnName 子对象字段名
	 * @param orderColumnName 排序字段名
	 * @param levelColumnName 级别字段名
	 * @throws Exception
	 */
	void levelCodeHandler(Object entity,String keyColumnName,String parentColumnName,
			String childColumnName,String orderColumnName,String levelColumnName)throws Exception;
}
