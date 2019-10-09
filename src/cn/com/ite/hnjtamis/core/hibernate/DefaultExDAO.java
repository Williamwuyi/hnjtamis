package cn.com.ite.hnjtamis.core.hibernate;




import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;


/**
 * 
 * <p>Title cn.com.ite.ydkh.base.core.hibernate.DefaultExDAO</p>
 * <p>Description 项目的缺省DAO接口</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 朱健
 * @create time: May 19, 2014 9:07:31 AM
 * @version 2.0
 * 
 * @modified records:
 */
public interface DefaultExDAO extends DefaultDAO{
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 执行SQL
	 * @param sql
	 * @return
	 * @modified
	 */
	public int saveSQL(String sql);
	
	/**
	 *
	 * @author zhujian
	 * @description 查询HQL
	 * @param hql
	 * @return
	 * @modified
	 */
	public List queryHql(String hql);
	
}
