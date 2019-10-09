package cn.com.ite.hnjtamis.exam.base.exampublicuser.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

public class ExamPublicUserDaoImpl extends HibernateDefaultDAOImpl implements
		ExamPublicUserDao {
	/**
	 * 
	 * @author 
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
	 * @author 
	 * @description 查询HQL
	 * @param hql
	 * @return
	 * @modified
	 */
	public List queryHql(String hql) {
		Query query = (Query) template.getSessionFactory().getCurrentSession().createQuery(hql);
		return query.list();
	}
	
	/**
	 * 保存状态
	 * @author 朱健
	 * @param updateIds
	 * @return
	 * @modified
	 */
	public int savePublicUserState(String updateIds){
		String sql = "update exam_public_user t set t.state='20' where t.user_id in ("+updateIds+")";
		System.out.println(sql);
		int resultInt = this.saveSQL(sql);
		return resultInt;
	}
}
