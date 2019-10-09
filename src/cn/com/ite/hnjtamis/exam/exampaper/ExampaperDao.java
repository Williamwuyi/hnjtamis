package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserInticketForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperDao</p>
 * <p>Description 考试安排与试卷生成 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午2:55:21
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExampaperDao extends DefaultDAO{
	
	/**
	 * 发布试卷
	 * @description
	 * @param exam_arrange_id
	 * @modified
	 */
	public void saveAndPublicInExamArrange(String exam_arrange_id,String employeeId,String employeeName)throws Exception;
	/**
	 *  获取当天的用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<ExamUserInticketForm> getExamUserInticketRelation();
	/**
	 * 获取有效的员工对应用户信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,SysUser> getSysUserMap();
	
	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type,int maxRowNum);
	
	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeAndThemeNumsByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type);
	/**
	 * 更新标准编码
	 * @author 朱健
	 * @modified
	 */
	public void updateQuarterTrainInfo();
	
	/**
	 * 更新试题校验码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCrc();
	
	
	/**
	 * 更新试题编码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCode();
	
	/**
	 * 关闭到考试结束时间但未提交答案的考生的考试时间
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndsubInExamUserTestpaper();
	
	/**
	 * 更新试卷的分数、试题数量
	 * @author 朱健
	 * @modified
	 */
	public void updateExamTestPaperTotal();
	
	/**
	 *
	 * @author zhujian
	 * @description 获取有考试结束时间还未阅卷的考生
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getNotSubmitAnsExamUserTestpaperList();
	
	/**
	 * 清理考试安排中未选中考生的准考证
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndCleanPublicUserInticket();

	/**
	 * 查询数据字典
	 * @author zhujian
	 * @description
	 * @param dtType
	 * @return
	 * @modified
	 */
	public List<Dictionary> getDictionaryTypeList(String dtType);
	
	
	/**
	 * 获取考试安排编码
	 * @author zhujian
	 * @description
	 * @param exam_arrangeid
	 * @return
	 * @modified
	 */
	public String  getPrefix(String exam_arrangeid);
	
	
	/**
	 * 获取准考证编码的后几位的最大值
	 * @author zhujian
	 * @description
	 * @param exam_arrangeid
	 * @param prefix
	 * @return
	 * @modified
	 */
	public int getPostfixMaxNum(String exam_arrangeid,String prefix);
	
	
	/**
	 * 删除空的考试数据
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullExam();
	
	/**
	 * 删除没有分配考生科目的考生
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullexamUserTestpaper();
	
	/**
	 * 删除没有考生使用的试卷
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaperInExamUserTestpaper(String examId);
	
	/**
	 * 汇总出题数量与用题数量
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveUseTestpaperNum();
	
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取三天内要进行考试的考生
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExeExamUserList();
	
	/**
	 * 获取存在需要手工阅卷的科目
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveUserReviewExam();
	

	/**
	 * 获取存在阅卷人
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveMarkpeople();
	
	
	/**
	 * 获取机构的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamTjxxByOrgan(String organId,String examId,String startDay,String endDay,String exam_property);
	
	/**
	 * 获取部门的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamTjxxByUser(String organId,String examId,String startDay,String endDay,String exam_property);
	
	/**
	 * 获取部门的统计信息
	 * @author 朱健
	 * @param organId
	 * @param examId
	 * @param startDay
	 * @param endDay
	 * @return
	 * @modified
	 */
	public List getExamDeptTjxxByUser(String topDeptId,String examId,String startDay,String endDay,String exam_property);
	
	/**
	 * 获取考试成绩等信息
	 * @author 朱健
	 * @param id
	 * @param type
	 * @param examId
	 * @return
	 * @modified
	 */
	public List getExamUserInfos(String id,String type,String examId,String startDay,String endDay,String exam_property);
	
	/**
	 * 获取考试管理的考生信息树
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeTree(String publicId,String examId);
	
	/**
	 * 获取考试管理的父节点
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeOrganTree(String publicId,String examId);
	
	/**
	 * 根据查询条件查询考生信息ID
	 * @description
	 * @param examId
	 * @param organId
	 * @param quarter_train_id
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExamUserTestpaperList(String examId,String organId,String quarter_train_id);
}
