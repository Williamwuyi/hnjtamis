package cn.com.ite.hnjtamis.talent.reg;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.module.power.login.UserSession;

public interface TalentRegistrationService extends DefaultService {
	
	/**
	 * 更新员工的岗位
	 * @description
	 * @modified
	 */
	public void updateEmployeeQuarter();
	/**
	 * 获取用户反馈审核的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getFkThemeAuditNumMap();
	/**
	 * 获取用户阅卷的次数
	 * @description
	 * @return
	 * @modified
	 */
	public Map<String,Integer> getExamMarkNumMap();
	
	/**
	 * 同步专家对应题库
	 * @description
	 * @modified
	 */
	public void saveSyncBank()throws Exception;
	/**
	 *
	 * @author zhujian
	 * @description 专家库信息导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importTalentRegistration(File xls,UserSession usersess)throws Exception;
	
	/**
	 * 查询数据并返回树形结构
	 * @param organId
	 * @return
	 */
	public List<TreeNode> findTreeList(String organId) throws Exception;
}
