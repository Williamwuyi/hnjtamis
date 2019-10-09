package cn.com.ite.hnjtamis.exam.base.exampublicuser.dao;

import java.util.List;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

public interface ExamPublicUserDao extends DefaultDAO {
	public int saveSQL(String sql);
	public List queryHql(String hql);
	
	/**
	 * 保存状态
	 * @author 朱健
	 * @param updateIds
	 * @return
	 * @modified
	 */
	public int savePublicUserState(String updateIds);
}
