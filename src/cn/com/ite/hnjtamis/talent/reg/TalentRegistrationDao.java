package cn.com.ite.hnjtamis.talent.reg;


import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.talent.reg.TalentRegistrationDao</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月5日 下午1:10:58
 * @version 1.0
 * 
 * @modified records:
 */
public interface TalentRegistrationDao extends DefaultDAO{
	
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
	 * 查询人员信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<Object[]> queryEmployeeList()throws Exception;
}
