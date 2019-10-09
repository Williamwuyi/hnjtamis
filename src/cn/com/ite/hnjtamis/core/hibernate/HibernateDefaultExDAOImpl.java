package cn.com.ite.hnjtamis.core.hibernate;


import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;




import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 * 
 * <p>Title cn.com.ite.ydkh.base.core.hibernate..HibernateDefaultExDAOImpl</p>
 * <p>Description 项目的缺省DAO实现</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 朱健
 * @create time: May 19, 2014 11:51:27 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class HibernateDefaultExDAOImpl extends HibernateDefaultDAOImpl implements DefaultExDAO{

	
	/**
	 * 
	 * @author zhujian
	 * @description 执行SQL
	 * @param sql
	 * @return
	 * @modified
	 */
	public int saveSQL(String sql) 
	{
		Session session=template.getSessionFactory().getCurrentSession();
		int len = session.createSQLQuery(sql).executeUpdate();
        return len;
	}
	
	
	/**
	 *  
	 * @author zhujian
	 * @description 查询HQL
	 * @param hql
	 * @return
	 * @modified
	 */
	public List queryHql(String hql) {
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		return query.list();
	}
	
}