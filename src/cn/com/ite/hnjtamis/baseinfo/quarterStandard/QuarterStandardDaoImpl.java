package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.HibernateTemplate;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardDaoImpl</p>
 * <p>Description  标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:05:19
 * @version 1.0
 * 
 * @modified records:
 */
public class QuarterStandardDaoImpl extends HibernateDefaultDAOImpl implements QuarterStandardDao{

	
	 
	/**
	 * 获取岗位对应的标准有多少
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,String> getQuarterStrandardNum(){
		Map<String,String> value = new HashMap<String,String>();
		try{
			HibernateTemplate template = (HibernateTemplate)  this.template;
			Session session = template.getSessionFactory().getCurrentSession();
			String sqlStr = "select t.QUARTER_ID as QUARTERID,count(*) QNUM from JOBS_STANDARD_QUARTER t  group by t.QUARTER_ID";
			SQLQuery sqlQuery=session.createSQLQuery(sqlStr);
			List<Object[]> list =sqlQuery.addScalar("QNUM", IntegerType.INSTANCE).addScalar("QUARTERID",StringType.INSTANCE).list();
			for(int i=0;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				int qnum = (int)obj[0];
				String quarter_id = (String)obj[1];
				value.put(quarter_id, qnum+"");
			}
		}catch(Exception e ){
			e.printStackTrace();
		}
		return value;
	}
	

	
}
