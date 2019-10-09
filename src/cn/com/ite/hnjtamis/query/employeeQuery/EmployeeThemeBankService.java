package cn.com.ite.hnjtamis.query.employeeQuery;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.query.employeeQuery.EmployeeThemeBankService</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年5月16日 上午10:32:30
 * @version 1.0
 * 
 * @modified records:
 */
public interface EmployeeThemeBankService extends DefaultService {
	
	/**
	 * 根据机构查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeXxQkInOrgan(String organId,int start,int limit,Map paramMap);
	
	/**
	 * 根据机构查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeXxQkInOrgan(String organId,Map paramMap);
	
	
	/**
	 * 根据部门查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeXxQkInDept(String qdeptId,int start,int limit,Map paramMap);
	
	/**
	 * 根据部门查询员工完成题库的学习数
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeXxQkInDept(String qdeptId,Map paramMap);

	/**
	 * 根据机构查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeThemeBankInOrgan(String organId,int start,int limit,Map paramMap);
	
	/**
	 * 根据机构查询员工题库的学习情况(总数量)
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeThemeBankInOrgan(String organId,Map paramMap);
	
	/**
	 * 根据部门查询员工题库的学习情况
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public List<EmployeeThemeBankForm> queryEmployeeThemeBankInDept(String qdeptId,int start,int limit,Map paramMap);
	
	/**
	 * 根据部门查询员工题库的学习情况(总数量)
	 * @description
	 * @param organId
	 * @return
	 * @modified
	 */
	public int countEmployeeThemeBankInDept(String qdeptId,Map paramMap);
	
	
	/**
	 * 获取人员题库更新时间
	 * @description
	 * @return
	 * @modified
	 */
	public String getEmployeeBankCreateTime();

}
