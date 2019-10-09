package cn.com.ite.hnjtamis.talent.reg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import cn.com.ite.eap2.core.hibernate.HibernateDefaultDAOImpl;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.organization.Quarter;
/**
 *
 * <p>Title cn.com.ite.hnjtamis.talent.reg.TalentRegistrationDaoImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月5日 下午1:12:00
 * @version 1.0
 * 
 * @modified records:
 */
public class TalentRegistrationDaoImpl  extends HibernateDefaultDAOImpl implements TalentRegistrationDao{

	/**
	 * 更新员工的岗位
	 * @description
	 * @modified
	 */
	public void updateEmployeeQuarter(){
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql =" update TALENT_REGISTRATION t set t.quarter_id = (select e.quarter_id from employee e where e.employee_id = t.employee_id) ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			sqlQuery.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取用户反馈审核的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getFkThemeAuditNumMap(){
		Map<String,Integer> valueMap = new HashMap<String,Integer>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql =" select t.created_id_by as EMPLOYEEID,count(*) FKNUM from THEME_FKAUDIT t "
					+ " where t.fk_type = '30' group by t.created_id_by ";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List list = sqlQuery.addScalar("FKNUM", IntegerType.INSTANCE)
					.addScalar("EMPLOYEEID", StringType.INSTANCE).list();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] obj = (Object[])list.get(i);
					Integer fkNum = (Integer)obj[0];
					String employeeId = (String)obj[1];
					valueMap.put(employeeId, fkNum);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return valueMap;
	} 
	
	/**
	 * 获取用户阅卷的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getExamMarkNumMap(){
		Map<String,Integer> valueMap = new HashMap<String,Integer>();
		try{
			Session session = template.getSessionFactory().getCurrentSession();
			String sql = " select vvv.EMPLOYEEID, vvv.exam_id, count(*) MARKNUM from "
					+ " (select vv.empid as EMPLOYEEID,vv.exam_id "
					+ " from EXAM_MARK_THEME tt, EXAM_MARKPEOPLE vv "+
					" where tt.exam_markpeople_id = vv.exam_markpeople_id group by vv.empid,vv.exam_id) vvv "+
						" group by vvv.EMPLOYEEID, vvv.exam_id";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List list = sqlQuery.addScalar("MARKNUM", IntegerType.INSTANCE)
					.addScalar("EMPLOYEEID", StringType.INSTANCE).list();
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					Object[] obj = (Object[])list.get(i);
					Integer markNum = (Integer)obj[0];
					String employeeId = (String)obj[1];
					valueMap.put(employeeId, markNum);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return valueMap;
	} 
	/**
	 * 同步专家对应题库
	 * @description
	 * @modified
	 */
	public void saveSyncBank()throws Exception{
			Session session=template.getSessionFactory().getCurrentSession();
			String delStr = "delete TALENT_REGISTRATION_BANK";
			session.createSQLQuery(delStr).executeUpdate();
			
			
			
			String instr = " insert into TALENT_REGISTRATION_BANK "+
							  "(TALENT_BANK_ID, "+
							   "TALENT_ID, "+
							   "BANK_ID, "+
							   "BANK_CODE, "+
							   "BANK_NAME, "+
							   "JSTYPEID, "+
							   "TYPENAME, "+
							   "STANDARDID, "+
							   "STANDARDNAME, "+
							   "ORDERNO, "+
							   "LAST_UPDATE_DATE, "+
							   "LAST_UPDATED_BY, "+
							   "CREATION_DATE, "+
							   "CREATED_BY) "+
							  "(select to_char(SYSDATE, 'yyyymmddhh24miss') || REPLACE(TRIM(DBMS_RANDOM.VALUE), '.', '') as TALENT_BANK_ID, "+
							          "ts.talent_id as TALENT_ID, "+
							          "jsq.theme_bank_id as BANK_ID, "+
							          "jsq.theme_bank_code as BANK_CODE, "+
							          "jsq.theme_bank_name as BANK_NAME, "+
							          "jsq.JSTYPEID as JSTYPEID, "+
							          "jst.TYPENAME as TYPENAME, "+
							          "jsq.standardid as STANDARDID, "+
							          "jsq.STANDARDNAME as STANDARDNAME, "+
							          "0 as ORDERNO, "+
							          "'' as LAST_UPDATE_DATE, "+
							          "'' as LAST_UPDATED_BY, "+
							          "to_char(sysdate, 'yyyy-MM-dd hh24:mi:ss') as CREATION_DATE, "+
							          "'系统' as CREATED_BY "+
							     "from TALENT_REGISTRATION_SPECIALITY ts, "+
							          "BASE_SPECIALITY_STANDARD_TYPES bst, "+
							          "JOBS_STANDARD                  jsq, "+
							          "JOBS_STANDARD_TYPES            jst "+
							    "where ts.specialityid = bst.specialityid "+
							      "and bst.types_id = jsq.standardid "+
							     " and jsq.JSTYPEID = jst.JSTYPEID and jsq.theme_bank_id is not null) ";
			session.createSQLQuery(instr).executeUpdate();
			
			
			
			String sql = "select t.talent_id as TALENTID,t.bank_name as BANKNAME from TALENT_REGISTRATION_BANK t order by t.talent_id,t.bank_id";
			SQLQuery sqlQuery=session.createSQLQuery(sql);
			List<Object[]> tmplist = sqlQuery.addScalar("TALENTID", StringType.INSTANCE)
					.addScalar("BANKNAME", StringType.INSTANCE).list();
			if(tmplist!=null && tmplist.size()>0){
				Map<String,String> bankNamesMap = new HashMap<String,String>();
				for(int i=0;i<tmplist.size();i++){
					Object[] object = tmplist.get(i);
					String talent_id = (String)object[0];
					String bank_name = (String)object[1];
					if(talent_id == null  || bank_name == null){
						continue;
					}
					String bank_names= bankNamesMap.get(talent_id);
					if(bank_names == null){
						bankNamesMap.put(talent_id,bank_name+"，");
					}else{
						bankNamesMap.put(talent_id,bank_names+bank_name+"，");
					}
					
				}
				if(!bankNamesMap.keySet().isEmpty()){
					String updateStr = " update TALENT_REGISTRATION t set  t.bank_names = ? where t.talent_id = ?";
					Iterator its = bankNamesMap.keySet().iterator();
					while(its.hasNext()){
						String talent_id = (String)its.next();
						String bank_name = bankNamesMap.get(talent_id);
						bank_name = bank_name.substring(0,bank_name.length()-1);
						if(bank_name.length()>1500){
							bank_name = bank_name.substring(0,1480)+"...";
						}
						SQLQuery sqlQuery2=session.createSQLQuery(updateStr);
						sqlQuery2.setString(0, bank_name);
						sqlQuery2.setString(1, talent_id);
						sqlQuery2.executeUpdate();
					}
				}
				
			}
	}
	
	/**
	 * 查询人员信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<Object[]> queryEmployeeList()throws Exception{
		List<Object[]> tmplist = new ArrayList<Object[]>();
		try{
			String hql = " select {a.*},{d.*},{q.*},{e.*} from Organ a,Dept d,Quarter q,Employee e "+
		             	" where a.organ_Id = d.organ_Id "+ 
		             	" and d.dept_Id = q.dept_Id "+
		             	" and q.quarter_Id = e.quarter_Id  "+
		             	" and e.isvalidation = 1 order by a.order_No,d.order_No,q.order_No,e.employee_Code ";
			Session session = template.getSessionFactory().getCurrentSession();
			SQLQuery sqlQuery=session.createSQLQuery(hql);   
			tmplist = sqlQuery.addEntity("a", Organ.class)
							.addEntity("d", Dept.class)
							.addEntity("q", Quarter.class)
							.addEntity("e", Employee.class)
							.list();
			//tmplist = this.queryHql(hql);
		}catch(Exception e){
			e.printStackTrace();
		}
		return tmplist;
	}
	
	
	
}
