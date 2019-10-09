package cn.com.ite.eap2.core.service;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.redis.RedisDefaultDAO;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.exception.EapException;

/**
 * <p>Title cn.com.ite.eap2.core.service.DefaultServiceImpl</p>
 * <p>Description 缺省服务实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 20, 2014 1:50:46 PM
 * @version 2.0
 * 
 * @modified records:
 */
public class DefaultServiceImpl implements DefaultService{
	/**
	 * 缺省DAO
	 */
	private DefaultDAO dao;
	
	//redis基础接口
	private RedisDefaultDAO redisDao;
	
	public RedisDefaultDAO getRedisDao() {
		return redisDao;
	}

	public void setRedisDao(RedisDefaultDAO redisDao) {
		this.redisDao = redisDao;
	}

	

	/**
	 * 增加实体
	 * @param entity 实体
	 */
	public void add(Serializable bo) throws EapException {
		dao.addEntity(bo);
	}

	/**
	 * 查询数量
	 * @param queryName 配置名称
	 * @param term 条件
	 * @return 结果数量
	 * @modified
	 */
	public int countData(String queryName,Object term) {
		return dao.countConfigQl(queryName, term);
	}

	/**
	 * 删除对象
	 * @param bo 对象
	 * @modified
	 */
	public void delete(Serializable bo) throws EapException {
		dao.deleteEntity(bo);
	}

	/**
	 * 主健删除对象
	 * @param key 主健
	 * @param type 类型
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void deleteByKey(Serializable key, Class type) throws EapException {
		dao.deleteBatchEntityByKeys(new Serializable[]{key}, type);
	}

	/**
	 * 根据类型及主健组删除对象
	 * @param keys 主健组
	 * @param type 对象类型
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void deleteByKeys(Serializable[] keys, Class type)
			throws EapException {
		try{
		   dao.deleteBatchEntityByKeys(keys, type);
		}catch(Exception e){
			throw new EapException(e.getMessage());
		}
	}

	/**
	 * 批量删除对象
	 * @param bos 对象组
	 * @throws EapException
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public void deletes(Collection bos) throws EapException {
		dao.deleteBatchEntity(bos);
	}

	/**
	 * 主健查询对象
	 * @param key 主健
	 * @param type 实体类型
	 * @return 实体
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public Serializable findDataByKey(Serializable key, Class type) {
		return dao.findEntityBykey(type, key);
	}
	
	/**
	 * 是否存在指定主键和类型的对象
	 * @param key
	 * @param type
	 * @return
	 * @modified
	 */
	public boolean existDate(Serializable key,Class type){
		try{
			Object ob = dao.findEntityBykey(type, key);
			return ob!=null;
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 查询所有数据
	 * @param type 实体类型
	 * @return 数据
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List queryAllDate(Class type) {
		return dao.getAll(type);
	}

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
	public List queryData(String queryName,Object term,Map sortMap, int pageNo, int rowSize) {
		return dao.queryConfigQl(queryName, term,sortMap, dao.getEntityClass(), pageNo, rowSize);
	}
	
	@SuppressWarnings("unchecked")
	public List queryData(String queryName,Object term,Map sortMap) {
		return dao.queryConfigQl(queryName, term,sortMap, dao.getEntityClass(), 0, 0);
	}
	
	@SuppressWarnings("unchecked")
	public List findEntityByField(Class entityClass,String field,String fieldValue){
		return dao.findEntityByField(entityClass, field, fieldValue);
	}
	
	@SuppressWarnings("unchecked")
	public List queryData(String queryName,Object term,Map sortMap, Class returnClass,int pageNo, int rowSize) {
		return dao.queryConfigQl(queryName, term,sortMap, returnClass, pageNo, rowSize);
	}
	
	@SuppressWarnings("unchecked")
	public List queryData(String queryName,Object term,Map sortMap, Class returnClass) {
		return dao.queryConfigQl(queryName, term,sortMap, returnClass, 0, 0);
	}
	
	/**
	 * 保存实体
	 * @param entity 实体
	 */
	public void save(Serializable bo) throws EapException {
		dao.saveEntity(bo);
	}
	/**
	 * 保存持久化实体
	 * @param entity 实体
	 */
	public void saveOld(Serializable bo) throws EapException {
		dao.saveEntityOld(bo);
	}

	/**
	 * 批量保存实体
	 * @param bos 实体组
	 */
	@SuppressWarnings("unchecked")
	public void saves(Collection bos) throws EapException {
		dao.saveBatchEntity(bos);
	}

	/**
	 * 更新实体
	 * @param entity 实体
	 */
	public void update(Serializable bo) throws EapException {
		dao.updateEntity(bo);
	}

	public DefaultDAO getDao() {
		return dao;
	}

	public void setDao(DefaultDAO dao) {
		this.dao = dao;
	}

	@SuppressWarnings("unchecked")
	public void excuteQl(String configQlName, List params) {
		this.dao.excuteQl(configQlName, params);
	}

	public void deleteNoLinkAccessory(String[] accessIds, String busId)
			throws Exception {
		this.dao.deleteNoLinkAccessory(accessIds, busId);
	}
	/**
	 * 查询字段最大值
	 * @param entityClass
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getFieldMax(Class entityClass,String fieldName){
		return this.getDao().getFieldMax(entityClass, fieldName);
	}
	/**
	 * 查询字段最大值
	 * @param entityClass 实体类型
	 * @param fieldName 字段名
	 * @param parentFieldName 父字段名
	 * @param parentFieldValue 父字段值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int getFieldMax(Class entityClass,String fieldName,String parentFieldName,Object parentFieldValue){
		return this.getDao().getFieldMax(entityClass, fieldName, parentFieldName, parentFieldValue);
	}
	/**
	 * 获得对象的父对象属性！
	 * @param bean
	 * @param field 父对象属性名
	 * @param nullFields 其它要设空的属性数组
	 * @return
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private Object getObjectPropertyForObject(Object bean,String field,String[] nullFields,Map<String[],Class> copyFieldClassMap) throws Exception{
		Object ret = PropertyUtils.getProperty(bean, field);
		if(ret==null) return null;
		Class fieldClass = PropertyUtils.getPropertyType(bean, field);
		Object newBean = fieldClass.newInstance();
		PropertyUtils.copyProperties(newBean, ret);
		for(String nullField:nullFields){
			PropertyUtils.setProperty(newBean, nullField, null);
		}
		if(copyFieldClassMap!=null)
			for(String[] copyFields:copyFieldClassMap.keySet()){//复制参数中指定的属性
				Object oldCopyFieldObject = PropertyUtils.getProperty(newBean, copyFields[0]);
				if(oldCopyFieldObject!=null){
					Object copyFieldObject = copyFieldClassMap.get(copyFields).newInstance();
					for(int i=1;i<copyFields.length;i++){
						PropertyUtils.setProperty(copyFieldObject, copyFields[i], 
								PropertyUtils.getProperty(oldCopyFieldObject, copyFields[i]));
					}
					PropertyUtils.setProperty(newBean, copyFields[0], copyFieldObject);
				}
			}
		return newBean;
	}
	/**
	 * 对象复制，同时对象中的父对象清空
	 * @param src
	 * @param parentColumnName
	 * @return
	 * @throws Exception
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private Object copeObject(Object src,String parentColumnName,String[] nullFields,
			Map<String[],Class> copyFieldClassMap)throws Exception{
		Object newBean = src.getClass().newInstance();
		PropertyUtils.copyProperties(newBean, src);
		PropertyUtils.setProperty(newBean, parentColumnName, null);
		for(String nullField:nullFields){
			PropertyUtils.setProperty(newBean, nullField, null);
		}
		if(copyFieldClassMap!=null)
			for(String[] copyFields:copyFieldClassMap.keySet()){//复制参数中指定的属性
				Object oldCopyFieldObject = PropertyUtils.getProperty(newBean, copyFields[0]);
				if(oldCopyFieldObject!=null){
					Object copyFieldObject = copyFieldClassMap.get(copyFields).newInstance();
					for(int i=1;i<copyFields.length;i++){
						PropertyUtils.setProperty(copyFieldObject, copyFields[i], 
								PropertyUtils.getProperty(oldCopyFieldObject, copyFields[i]));
					}
					PropertyUtils.setProperty(newBean, copyFields[0], copyFieldObject);
				}
			}
		return newBean;
	}
	/**
	 * 级别编码处理
	 * @param entity
	 * @param keyColumnName
	 * @param parentColumnName
	 * @param childColumnName
	 * @param orderColumnName
	 * @param levelColumnName
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void levelCodeHandler(Object entity,String keyColumnName,String parentColumnName,
			String childColumnName,String orderColumnName,String levelColumnName)throws Exception{
		String key = (String)PropertyUtils.getProperty(entity, keyColumnName);
		entity = getDao().findEntityBykey(entity.getClass(), key);
		//父对象级别编码
		Object parent = PropertyUtils.getProperty(entity, parentColumnName);
		String parentLevel = "";
		if(parent!=null)
			parentLevel = (String)PropertyUtils.getProperty(parent, levelColumnName);
		if(parentLevel==null) parentLevel = "";		
		Object order = PropertyUtils.getProperty(entity, orderColumnName);
		int orderInt = 0;
		if(order.getClass().isAssignableFrom(Integer.class))
			orderInt = (Integer)order;
		else
			orderInt = ((Long)order).intValue();
		String levelCode = fomat4Level(orderInt);
		PropertyUtils.setProperty(entity, levelColumnName, parentLevel+levelCode);
		if(!StringUtils.isEmpty(key)){//修改的处理
			Collection coll = getDao().findEntitySub(entity.getClass().getName(), keyColumnName, key, childColumnName);
			Iterator iterator = coll.iterator();
			while (iterator.hasNext()) {
				Object bean = iterator.next();
				levelCodeHandler(bean, keyColumnName, parentColumnName, 
						childColumnName, orderColumnName, levelColumnName, parentLevel+levelCode);
			}
		}
		getDao().updateEntity((Serializable)entity);
	}
	/**
	 * 级别编码迭代处理
	 * @param entity
	 * @param keyColumnName
	 * @param parentColumnName
	 * @param childColumnName
	 * @param orderColumnName
	 * @param levelColumnName
	 * @param parentLeveCode
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void levelCodeHandler(Object entity,String keyColumnName,String parentColumnName,
			String childColumnName,String orderColumnName,String levelColumnName,String parentLeveCode)throws Exception{
		Object order = PropertyUtils.getProperty(entity, orderColumnName);
		String key = (String)PropertyUtils.getProperty(entity, keyColumnName);
		int orderInt = 0;
		if(order.getClass().isAssignableFrom(Integer.class))
			orderInt = (Integer)order;
		else
			orderInt = ((Long)order).intValue();
		String levelCode = this.fomat4Level(orderInt);
		PropertyUtils.setProperty(entity, levelColumnName, parentLeveCode+levelCode);
		//getDao().saveEntity((Serializable)entity);
		Collection coll = getDao().findEntitySub(entity.getClass().getName(),
				keyColumnName, key, childColumnName);
		Iterator iterator = coll.iterator();
		while (iterator.hasNext()) {
			Object bean = iterator.next();
			levelCodeHandler(bean, keyColumnName, parentColumnName, 
					childColumnName, orderColumnName, levelColumnName, parentLeveCode+levelCode);
		}
	}
	private String fomat4Level(int order){
		//String str =  (order<10000?"0":"")+(order<1000?"0":"")+(order<100?"0":"")+order;
		String str =  "0000"+order;
		str = str.substring(str.length()-4,str.length());
		return str;
	}
	
	
	public static void main(String[] args){
		int order = 31;
		String str =  "0000"+order;
		str = str.substring(str.length()-4,str.length());
		System.out.println(str);
	}
	/**
	 * 把上下关系的同一类型对象集合处理成有层次关系的集合
	 * @param coll 同一类型对象集合
	 * @param keyColumnName 主健字段名
	 * @param parentColumnName 父主键字段名
	 * @param childColumnName 子字段名
	 * @param nullFields 要设置为空的属性集
	 * @param copyFieldClassMap 要复制的属性映射,注意键为数组，分别表示 对象属性..对象中需要复制的属性组
	 * @param filterIds 过滤结点ID数组
	 * @param topId 顶级结点ID条件，意思是只显示此结点及以结点数据
	 * return 新集合
	 */
	@SuppressWarnings("unchecked")
	public List childObjectHandler(Collection coll, String keyColumnName,
			String parentColumnName, String childColumnName,String[] nullFields,
			Map<String[],Class> copyFieldClassMap,List<String> filterIds,String sortField,String topId) throws Exception {
		Map keyToChildMap = new HashMap();
		Iterator iterator = coll.iterator();
		List tops = new ArrayList();//项层对象
		List<String> topIds = new ArrayList<String>();
		while (iterator.hasNext()) {
			Object bean = iterator.next();
			Object key = PropertyUtils.getProperty(bean, keyColumnName);
			Object parent = getObjectPropertyForObject(bean, parentColumnName,nullFields,copyFieldClassMap);
			Object copyBean = copeObject(bean,parentColumnName,nullFields,copyFieldClassMap);
			Object pre = copyBean;//前一个处理父对象
			if(parent == null) {
				if(!topIds.contains(key.toString())){
					tops.add(copyBean);
					topIds.add(key.toString());
				}
			} else
			while(parent!=null){
				Serializable parentId = null;
				if(!parent.getClass().isPrimitive())//对象类型，取主健
					parentId = (Serializable)PropertyUtils.getProperty(parent, keyColumnName);
				else
					parentId = (Serializable)parent;
				List childs = (List)keyToChildMap.get(parentId);
				if(childs == null){
					childs = new ArrayList();
					keyToChildMap.put(parentId, childs);
				}
				String preKey = PropertyUtils.getProperty(pre, keyColumnName).toString();
				boolean find = false;
				for(Object child:childs){
					String childKey = PropertyUtils.getProperty(child, keyColumnName).toString();
					if(preKey.equals(childKey))
						find = true;
				}
				if(!find){
				   PropertyUtils.setProperty(pre, parentColumnName, null);
			       childs.add(pre);
				}
				pre = parent;
				parent = getObjectPropertyForObject(parent, parentColumnName,nullFields,copyFieldClassMap);
				if(parent==null){
					PropertyUtils.setProperty(pre, parentColumnName, null);
					preKey = PropertyUtils.getProperty(pre, keyColumnName).toString();
					if(!topIds.contains(preKey)){
					   tops.add(pre);
					   topIds.add(preKey);
					}
				}
			}
		}
		this.childObjectHandlerForChild(tops, keyToChildMap,
				keyColumnName, parentColumnName, childColumnName,filterIds,sortField);
		if(!StringUtils.isEmpty(topId)){
			Object top = this.takeData(tops, topId, keyColumnName, childColumnName);
			tops.clear();
			if(top!=null)
				tops.add(top);
		}
		return tops;
	}
	/**
	 * 提取子数据
	 * @param childs 原数据
	 * @param topId 父结点ID
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	private Object takeData(List childs,String topId,String keyColumnName,String childColumnName) throws Exception{
		for(Object child:childs){
			Serializable key = (Serializable)PropertyUtils.getProperty(child, keyColumnName);
			if(topId.equals(key)){
				return child;
			}else{
				List childChild = (List)PropertyUtils.getProperty(child, childColumnName);
				Object find = this.takeData(childChild, topId, keyColumnName, childColumnName);
				if(find!=null) return find;
			}
		}
		return null;
	}
	/**
	 * childObjectHandler此方法中迭代方法，用于设置子对象中的子对象
	 * @param childs 子对象
	 * @param keyToChildMap 主健对下级集合映射
	 * @param keyColumnName
	 * @param parentColumnName
	 * @param childColumnName
	 */
	@SuppressWarnings("unchecked")
	private void childObjectHandlerForChild(List childs, Map keyToChildMap,
			String keyColumnName, String parentColumnName,
			String childColumnName,List<String> filterIds,final String sortField) throws Exception{
		java.util.Collections.sort(childs, new Comparator(){
			public int compare(Object o1, Object o2) {
				try{
					if(o1!=null && o2!=null){
						Comparable s1 = (Comparable)PropertyUtils.getProperty(o1, sortField);
						Comparable s2 = (Comparable)PropertyUtils.getProperty(o2, sortField);
						return s1!=null ? s1.compareTo(s2) : 0;
					}else{
						return 0;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				return 0;				
			}});
		for (int i = 0; i < childs.size(); i++) {
			Object child = childs.get(i);
			Object key = PropertyUtils.getProperty(child, keyColumnName);
			if(filterIds!=null&&filterIds.contains(key)){
				childs.remove(i--);
				continue;
			}
			Object childChild = PropertyUtils.getProperty(child, childColumnName);
			if (!(childChild instanceof Collection))
				throw new Exception(childColumnName + "此属性不为集合类型！");
			Collection childChilds = (Collection) childChild;//子对象中子对象属性
			childChilds.clear();
			List listchild = (List) keyToChildMap.get(key);
			if(listchild!=null&&listchild.size()>0){
				this.childObjectHandlerForChild(listchild, keyToChildMap,
						keyColumnName, parentColumnName, childColumnName,filterIds,sortField);
				for(Object c:listchild){
				    childChilds.add(c);
				}
			}
		}
	}

	
}