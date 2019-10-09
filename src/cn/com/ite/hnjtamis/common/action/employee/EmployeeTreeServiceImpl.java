package cn.com.ite.hnjtamis.common.action.employee;

import java.util.*;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;

public class EmployeeTreeServiceImpl extends DefaultServiceImpl implements EmployeeTreeService{
	
	/**
	 *  获取机构部门信息，最多两层部门
	 * @author 朱健
	 * @param organId
	 * @param deptLevel 获取部门的层次
	 * @return
	 * @modified
	 */
	public List queryDeptInOwnerOrgan(String ownerOrganId,int deptLevel){
		EmployeeTreeDao employeeTreeDao = (EmployeeTreeDao)this.getDao();
		return employeeTreeDao.queryDeptInOwnerOrgan(ownerOrganId,deptLevel);
	}
	
	/**
	 *  获取机构部门信息，最多两层部门
	 * @author 朱健
	 * @param organId
	 * @param deptLevel 获取部门的层次
	 * @return
	 * @modified
	 */
	public List queryDeptInOrgan(String organId,int deptLevel){
		EmployeeTreeDao employeeTreeDao = (EmployeeTreeDao)this.getDao();
		return employeeTreeDao.queryDeptInOrgan(organId,deptLevel);
	}
	
	/**
	 * 获取机构人员树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List queryEmployeeDeptTree(String organId){
		EmployeeTreeDao employeeTreeDao = (EmployeeTreeDao)this.getDao();
		return employeeTreeDao.queryEmployeeDeptTree(organId);
	}
	
	/**
	 * 按培训岗位进行显示
	 * 获取机构人员树
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List queryPxEmployeeDeptTree(String organId,Map paramMap){
		EmployeeTreeDao employeeTreeDao = (EmployeeTreeDao)this.getDao();
		return employeeTreeDao.queryPxEmployeeDeptTree(organId,paramMap);
	}
	
	

	/**
	 * 获取未选中人员的信息
	 * @author 朱健
	 * @param id 选择节点的id
	 * @param type 选择节点的类型 organ-机构 dept-部门 quarter-岗位
	 * @param selectEmpIds 选择的人员
	 * @param inSelectEmpIdsType notIn-排除selectEmpIds  in-在selectEmpIds内 空则不处理
	 * @return
	 * @modified
	 */
	public List getSelectEmp(String id,String type,String selectEmpIds,String inSelectEmpIdsType,Map paramMap){
		EmployeeTreeDao employeeTreeDao = (EmployeeTreeDao)this.getDao();
		return employeeTreeDao.getSelectEmp(id,type,selectEmpIds,inSelectEmpIdsType,paramMap);
	}
	
}
