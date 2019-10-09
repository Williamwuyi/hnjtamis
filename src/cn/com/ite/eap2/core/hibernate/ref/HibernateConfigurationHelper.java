package cn.com.ite.eap2.core.hibernate.ref;

import java.util.*;

import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ManyToOne;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.ToOne;
import org.hibernate.mapping.Value;

import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.common.utils.StringUtils;

/**
 * <p>Title cn.com.ite.eap2.core.hibernate.HibernateConfigurationHelper</p>
 * <p>Description hibernate的HBM配置帮助类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2009</p>
 * @author  宋文科
 * @create time: 2009-7-29 时间08:55:37
 * @version 1.0
 * 
 * @modified records:
 */
public class HibernateConfigurationHelper {
	//构造
	private HibernateConfigurationHelper(){}
	
	private static HibernateConfigurationHelper instance = new HibernateConfigurationHelper();
	/**
	 * hibernate配置
	 */
	private static Configuration hibernateConf;
	
	public static HibernateConfigurationHelper getInstance(){
		return instance;
	}
	
	public static void setConfiguration(Configuration conf){
		hibernateConf = conf;
	}

	/**
	 * 获得实体类配置信息
	 * @param clazz
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public PersistentClass getPersistentClass(Class clazz) {
		synchronized (HibernateConfigurationHelper.class) {
			if(clazz.getName().indexOf("_$$_")>0)
				try {
					clazz=Class.forName(clazz.getName().split("_\\$\\$_")[0]);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			PersistentClass pc = hibernateConf.getClassMapping(clazz.getName());
			if (pc == null) {
				hibernateConf.addClass(clazz);
				pc = hibernateConf.getClassMapping(clazz.getName());
			}
			return pc;
		}
	}
	/**
	 * 判断是否存在此实体
	 * @param clazz
	 * @return
	 * @modified
	 */
	public boolean isExites(Class clazz){
		return hibernateConf.getClassMapping(clazz.getName())!=null;
	}

	/**
	 * 获得实体对应表名
	 * @param clazz 实体类型
	 * @return  表名
	 */
	@SuppressWarnings("unchecked")
	public String getTableName(Class clazz) {
		return getPersistentClass(clazz).getTable().getName();
	}
	/**
	 * 获得表注释
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getTableComment(Class clazz) {
		String comment= getPersistentClass(clazz).getTable().getComment();
		if(StringUtils.isEmpty(comment))
			return getTableName(clazz);
		else{
			return replaceJsonIsEmpty(comment);
		}
	}
	/**
	 * 获得实体属性描述
	 * @param clazz
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getEntityAttributeDesc(Class clazz,String attribute){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Property property = persistentClass.getProperty(attribute);
		Iterator ite = property.getColumnIterator();
		String comment = "";
		if(!(property.getValue() instanceof org.hibernate.mapping.Collection)){
		   Column column=(Column)ite.next();
		   comment = column.getComment();
		}else{
			comment = ((org.hibernate.mapping.Collection)property.getValue()).
			getCollectionTable().getComment();
		}
		if(StringUtils.isEmpty(comment))
			return property.getName();
		return replaceJsonIsEmpty(comment);
	}
	
	/**
	 * 获取实体配置
	 * @param clazz 实体类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getEntityConfig(Class clazz){
		Map map = new HashMap();
		String comment= getPersistentClass(clazz).getTable().getComment();
		if(!StringUtils.isEmpty(comment)){
			List cs = StringUtils.regexQuery("\\{[\\S\\s]*}", comment);
			if(cs.size()>0){
				map = JsonUtils.fromJson((String)cs.get(0), Map.class);
			}
		}
		return map;
	}
	/**
	 * 获得实体的属性配置
	 * @param clazz
	 * @param protery
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map getEntityProteryConfig(Class clazz,String protery){
		Map map = new HashMap();
		PersistentClass persistentClass = getPersistentClass(clazz);
		Property property = persistentClass.getProperty(protery);
		Iterator ite = property.getColumnIterator();
		String comment = "";
		if(!(property.getValue() instanceof org.hibernate.mapping.Collection)){
		   Column column=(Column)ite.next();
		   comment = column.getComment();
		}else{
			comment = ((org.hibernate.mapping.Collection)property.getValue()).
			getCollectionTable().getComment();
		}
		if(!StringUtils.isEmpty(comment)){
			List cs = StringUtils.regexQuery("\\{[\\S\\s]*}", comment);
			if(cs.size()>0){
				map = JsonUtils.fromJson((String)cs.get(0), Map.class);
			}
		}
		return map;
	}
	/**
	 * 字段配置的唯一性查询
	 * @param clazz
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public Map<String,List> getEntityProteryUnique(Class clazz){
		Map<String,List> unique = new LinkedHashMap<String,List>();
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			String comment = "";
			if(!(v instanceof org.hibernate.mapping.Collection)){
				Iterator ite = property.getColumnIterator();
				Column column=(Column)ite.next();
			    comment = column.getComment();
			}else
				continue;
			if(!StringUtils.isEmpty(comment)){
				List cs = StringUtils.regexQuery("\\{[\\S\\s]*}", comment);
				if(cs.size()>0){
					Map config = JsonUtils.fromJson((String)cs.get(0), Map.class);
					if(config!=null){
						List<Object> uniqueNos = (List<Object>)config.get("unique");
						for(Object uniqueNo:uniqueNos){
							String key = null;
							boolean self = true;
							if(uniqueNo instanceof String)
								key = uniqueNo+"";
							else{
								key = (String)((Map)uniqueNo).get("name");
								self = (Boolean)((Map)uniqueNo).get("self");
							}
							List uns = unique.get(key);
							if(uns==null){
								uns = new ArrayList();
								unique.put(key, uns);
							}
							uns.add(property.getName()+"$"+self);
						}
					}
				}
			}
		}
		return unique;
	}
	/**
	 * 获得实体的标题字段名
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getEntityTitlePropertyName(Class clazz){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			if(v.isSimpleValue()&&((SimpleValue)v).getTypeName()!=null
				&&((SimpleValue)v).getTypeName().equals("java.lang.String"))
				return property.getName();
		}
		return null;
	}
	/**
	 * 判断属性是否级联操作
	 * @param clazz 类路径
	 * @param propertyName 属性名
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public boolean judageCascade(Class clazz,String propertyName){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			if(property.getName().equals(propertyName)&&property.getCascade()!=null&&(
			 property.getCascade().startsWith("all")||property.getCascade().startsWith("save")))
				return true;
		}
		return false;
	}
	
	/**
	 * 判断属性是否多对多关系
	 * @param clazz 类路径
	 * @param propertyName 属性名
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public boolean judageManyToMany(Class clazz,String propertyName){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			if(property.getName().equals(propertyName)&&
					(v instanceof org.hibernate.mapping.Collection)){
				return (((org.hibernate.mapping.Collection)v).getElement() 
						instanceof org.hibernate.mapping.ManyToOne);
			}
		}
		return false;
	}
	/**
	 * 判断对象是否多对一关联
	 * @param clazz
	 * @param propertyName
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public boolean judageManyToOn(Class clazz,String propertyName){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			Value v = property.getValue();
			if(property.getName().equals(propertyName))
			  return (v instanceof ToOne);
		}
		return false;
	}

	/**
	 * 获得主健字段名
	 * @param clazz  实体类型
	 * @return 主健字段名
	 */
	@SuppressWarnings("unchecked")
	public String getPkColumnName(Class clazz) {
		return getPersistentClass(clazz).getTable().getPrimaryKey()
				.getColumn(0).getName();

	}
	/**
	 * 获取实体主健属性名称
	 * @param clazz  实体类型
	 * @return 主健属性名称
	 */
	@SuppressWarnings("unchecked")
	public String getPkProperyName(Class clazz) {
		PersistentClass persistentClass = getPersistentClass(clazz);
		return persistentClass.getIdentifierProperty().getName();
	}
	/**
	 * 获得实体的属性名组
	 * @param clazz 实体类型
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List<String> getEntityAttributes(Class clazz){
		PersistentClass persistentClass = getPersistentClass(clazz);
		Iterator iterator = persistentClass.getPropertyIterator();
		List<String> atts = new ArrayList<String>();
		while(iterator.hasNext()){
			Property property = (Property)iterator.next();
			atts.add(property.getName());
		}
		return atts;
	}
	/**
	 * 返回集合元素的类型
	 * @param value
	 * @return
	 */
	public String getCollectionElementClass(Value value){
		if(value instanceof org.hibernate.mapping.Collection){
			Value elementValue = ((org.hibernate.mapping.Collection)value).getElement();
			if(elementValue instanceof OneToMany)
				return ((OneToMany)elementValue).getReferencedEntityName();
			else if(elementValue instanceof SimpleValue)
				return null;
			else 
				return ((ManyToOne)elementValue).getReferencedEntityName();
		}
		return null;
	}

	/**
	 * 清除字符串中的配置信息
	 * @param str
	 * @return
	 */
	private String replaceJsonIsEmpty(String str){
		if(!StringUtils.isEmpty(str))
			return str.replaceAll("\\{[\\S\\s]*}", "");
		else
			return str;
	}
}
