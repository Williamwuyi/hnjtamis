package cn.com.ite.hnjtamis.exam.base.exampublic.dao;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.domain.organization.Employee;

public interface ExamPublicDao extends DefaultDAO{

	/**
	 * 获取考生信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<Employee> getExamEmployeeInQuarter(String scoreStartTime,String scoreEndTime,Map<String,String> gwMap);
	
	/**
	 * 获取考生信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<Employee> getExamEmployee(String scoreStartTime,String scoreEndTime);
	
}
