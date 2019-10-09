package cn.com.ite.hnjtamis.jobstandard.terms;


import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;


public interface StandardTermsDao extends DefaultDAO{
	
	/**
	 *
	 * @author 朱健
	 * @param employeeId
	 * @param relationType
	 * @return 查询题库的题目数量与当前模拟考试（或对应relationType类型）的题目完成数量
	 * @modified
	 */
	public Map<String,String> getThemeNumInBank(String employeeId,String relationType);
	
	/**
	 * 获取模拟试题
	 * @author 朱健
	 * @param employee_id
	 * @return
	 * @modified
	 */
	public Map<String,String> getMoniExamScore(String employee_id,String relationType);
	
	
	/**
	 * 获取岗位培训得分情况 
	 * @author 朱健
	 * @param employee_id
	 * @param exam_type_id
	 * @param startTime
	 * @param endTime
	 * @return
	 * @modified
	 */
	public Map<String,String> getGwpxExamScore(String employee_id,String exam_property,String startTime,String endTime);
	
	/**
	 * 在jobs_standard_quarter表没有数据的时候进行初始化
	 * @author 朱健
	 * @modified
	 */
	public void saveInitAllJobStandardQuarter();
	
	
	/**
	 * 根据标准条款中对应的系统岗位信息，处理不存在的并保存到标准条款中的标准岗位信息里面
	 * @author 朱健
	 * @modified
	 */
	public void updateStandardQuarterNotInSysQuarter();
	
	
	/**
	 * 根据标准条款中对应的标准岗位信息更新标准条款对应的系统岗位信息
	 * @author 朱健
	 * @modified
	 */
	public void updateUnionStandardByStandard();
}
