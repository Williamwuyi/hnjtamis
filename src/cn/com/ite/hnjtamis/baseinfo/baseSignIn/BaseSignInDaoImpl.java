package cn.com.ite.hnjtamis.baseinfo.baseSignIn;

import java.util.List;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInDaoImpl</p>
 * <p>Description  签到</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:15:08
 * @version 1.0
 * 
 * @modified records:
 */
public class BaseSignInDaoImpl extends HibernateDefaultDAOImpl implements BaseSignInDao{

	/**
	 * 获取人员的签到记录量
	 * @description
	 * @param employeeId
	 * @param account
	 * @param day
	 * @return
	 * @modified
	 */
	public int getSignInDay(String employeeId,String account,String day){
		int _signnum = -1;
		try{
			String sql = " select count(*) signnum from BASE_SIGN_IN t "
					+ " where t.employee_id =? and t.user_name =? "
					+ " and substr(t.sign_in_date,0,10) = ? ";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, employeeId);
			sqlQuery.setString(1, account);
			sqlQuery.setString(2, day);
			List querylist = sqlQuery.addScalar("signnum", IntegerType.INSTANCE).list();
			if(querylist == null || querylist.size() == 0){
				_signnum = 0;
			}else{
				Integer signnum= (Integer)querylist.get(0);
				if(signnum == null){
					_signnum = 0;
				}else{
					_signnum = signnum.intValue();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return _signnum;
	}
	
	
	/**
	 * 获取某用户的签到天数
	 * @description
	 * @param employeeId
	 * @param account
	 * @return
	 * @modified
	 */
	public int getSignInCount(String employeeId,String account){
		int _signnum = -1;
		try{
			String sql = " select count(*) signnum from BASE_SIGN_IN t where t.employee_id = ? and t.user_name = ?";
			HibernateTemplate template = (HibernateTemplate) this.template;		   
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(sql); 
			sqlQuery.setString(0, employeeId);
			sqlQuery.setString(1, account);
			List querylist = sqlQuery.addScalar("signnum", IntegerType.INSTANCE).list();
			if(querylist == null || querylist.size() == 0){
				_signnum = 0;
			}else{
				Integer signnum= (Integer)querylist.get(0);
				if(signnum == null){
					_signnum = 0;
				}else{
					_signnum = signnum.intValue();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return _signnum;
	}

	
}
