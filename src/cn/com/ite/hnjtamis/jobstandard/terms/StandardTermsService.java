package cn.com.ite.hnjtamis.jobstandard.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface StandardTermsService extends DefaultService {
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findStandardTermsTree(String topTypeId,String typeName)throws Exception;
	/*
	 * 查询首页 培训内容及标准  和  参考教材
	 * @param quarterId 岗位id
	 */
	public HashMap<String,ArrayList> queryIndexStandAndRefer(String quarterId);
	/*
	 * 查询首页 题库
	 * @param quarterId 岗位id
	 */
	public List<ThemeBank> queryIndexTk(String quarter_train_code,String organId);
	
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
