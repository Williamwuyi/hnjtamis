package cn.com.ite.hnjtamis.jobstandard.termsEx;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.form.EmployeeLearningForm;

/**
 * 岗位标准管理
 * @author 朱健
 * @create time: 2016年3月4日 上午9:01:09
 * @version 1.0
 * 
 * @modified records:
 */
public interface StandardTermsExService extends DefaultService {
	
	/**
	 * 获取模块与子模块Tree
	 * @description
	 * @return
	 * @modified
	 */
	public List<TreeNode> getStandardModelTree();
	
	/**
	 * 根据老的题库ID更新为新的题库ID
	 * @author 朱健
	 * @param standardid
	 * @param newBankId
	 * @param oldBankId
	 * @modified
	 */
	public void updateStandardQuarterBank(String standardid,String newBankId,String oldBankId);
	
	/**
	 * 根据老的题库ID更新为新的题库ID
	 * @author 朱健
	 * @param standardid
	 * @param newBankId
	 * @param oldBankId
	 * @modified
	 */
	public void updateMoreStandardQuarterBank(String standardid);
	/** 
	 * 获取标准岗位对应的题库
	 * @author 朱健
	 * @param standardid
	 * @return
	 * @modified
	 */
	public Map<String,List<ThemeBank>> queryBankIdByStandardQuarter(String standardid);
	/**
	 * 根据类型查询岗位信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByTypeId(String typeId,Map paramMap);
	
	
	/**
	 * 根据父类型查询岗位信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByParentTypeId(String typeId,Map paramMap);
	
	/**
	 * 根据岗位查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryStandardTermsByQuarterId(String typeId,Map paramMap);
	
	/**
	 * 获取顶级的节点
	 * @description
	 * @return
	 * @modified
	 */
	public List<StandardTerms> getStandardTopTypeList();
	/**
	 * 根据不属于该岗位查询标准信息
	 * @author 朱健
	 * @param typeId
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryUnStandardTermsByQuarterId(String typeId,Map paramMap);
	
	/**
	 * 查询全部标准信息
	 * @author 朱健
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<StandardTerms> queryAllStandardTerms(Map paramMap);
	
	/**
	 * 查询个人的岗位达标情况
	 * @author 朱健
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public EmployeeLearningForm getEmployeeStandardLearning(String employeeId);
}
