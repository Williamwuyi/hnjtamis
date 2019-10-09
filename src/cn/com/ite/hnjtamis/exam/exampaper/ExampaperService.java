package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserInticketForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperService</p>
 * <p>Description 考试安排与试卷生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午3:01:05
 * @version 1.0
 * 
 * @modified records:
 */
public interface ExampaperService extends DefaultService{
	
	/**
	 * 初始化用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
	 * @author 朱健
	 * @modified
	 */
	public void initUserInticketMap();
	
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
	 * 保存考生信息与试卷到文件
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveExamTestpaperInFile();
	
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
	 *
	 * @author zhujian
	 * @description 根据试卷模版中的试卷生成考生的试卷
	 * @param testpaper
	 * @return
	 * @modified
	 */
	public ExamTestpaper saveAndGetNewExamTestPaper(Exam exam,ExamUserTestpaper examUserTestpaper,Testpaper testpaper)throws Exception;
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 自动对考生得分进行计算
	 * @param examId
	 * @throws Exception
	 * @modified
	 */
	public void saveAutoMarkExam(String examId) throws Exception;
	
	
	/**
	 *
	 * @author zhujian
	 * @description 自动对一个考生的得分进行计算
	 * @param examUserTestpaper
	 *  @param isReMark 是否重算
	 * @throws Exception
	 * @modified
	 */
	public double saveAutoMarkPeople(ExamTestpaper examTestpaper,boolean isReMark)throws Exception;
	
	
	/**
	 * 获取考生的试题显示答案
	 * @author 朱健
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public Map<String,List<ExamTestpaperAnswerkey>> getExamTestpaperAnswerkeyByTestpaperId(String examTestpaperId);
	
	/**
	 *
	 * @author 朱健
	 * @param themeTypeList
	 * @param themeList
	 * @param isReMark
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public double saveAutoMarkPeopleInThemeType(List themeTypeList,List<ExamTestpaperTheme> themeList,boolean isReMark)throws Exception;
	
	/**
	 * 对答案进行处理，把答案文件读取并保存到数据库
	 * @author zhujian
	 * @description
	 * @param request
	 * @modified
	 */
	public void saveAnswerInUserAnswerFileInRealPath()throws Exception;
	
	/**
	 *
	 * @author zhujian
	 * @description 获取有考试结束时间还未阅卷的考生，对其自动计算分数进行计分
	 * @throws Exception
	 * @modified
	 */
	public void saveNotSubmitAnsExamUserMark()throws Exception;
	
	/**
	 * 对答案进行处理，把答案文件读取并保存到数据库
	 * @author zhujian
	 * @description
	 * @param request
	 * @modified
	 */
	public void saveAnswerInUserAnswerFile(HttpServletRequest request,Map<String,ExamUserTestpaper> userFinInticketMap)throws Exception;
	
	
	/**
	 * 对答案进行处理，把答案字符串保存到数据库
	 * @author zhujian
	 * @description
	 * @param examTestpaperId
	 * @param cookieStr
	 * @modified
	 */
	public boolean saveUserExamineeAnswer(ExamUserTestpaper examUserTestpaper,Map<String,String> userAnsMap)throws Exception;
	
	/**
	 *
	 * @author zhujian
	 * @description 保存选择的考生信息
	 * @param exam
	 * @param examUserFormList
	 * @param usersess
	 * @modified
	 */
	public void saveExamUserInExamPublicUsers(Exam exam,List<ExamPublicUser> examPublicUsers,UserSession usersess,HttpServletRequest request,boolean creatExamPaper);
	
	/**
	 *
	 * @author zhujian
	 * @description 保存选择的考生信息
	 * @param exam
	 * @param examUserFormList
	 * @param usersess
	 * @modified
	 */
	public List<ExamUserTestpaper> saveAndCleanExamUserFormList(Exam exam,List examUserFormList,
			UserSession usersess,HttpServletRequest request,boolean creatExamPaper);
	/**
	 *
	 * @author zhujian
	 * @description 保存选择的考生信息
	 * @param exam
	 * @param examUserFormList
	 * @param usersess
	 * @modified
	 */
	public List<ExamUserTestpaper> saveExamUserFormList(Exam exam,List examUserFormList,UserSession usersess,HttpServletRequest request,boolean creatExamPaper);
	
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
	 * 删除没有分配考生科目的考生试卷
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaperInExamUserTestpaper(String examId);
	/**
	 *
	 * @author zhujian
	 * @description 更新个人岗位达标。个人的学习、培训进度
	 * @param examUserTestpaper
	 * @param examTestpaper
	 * @param score
	 * @throws Exception
	 * @modified
	 */
	public void updatePersonalRateProgress(ExamUserTestpaper examUserTestpaper,
			ExamTestpaper examTestpaper,double score)throws Exception;
	
	/**
	 * 
	 * @author zhujian
	 * @description 根据考试科目产生新试卷并保存
	 * @return
	 * @modified
	 */
	public void saveExamTestPaperInExamInThread(HttpServletRequest request,Exam exam);
	
	/**
	 * 
	 * @author zhujian
	 * @description 根据考试科目产生新试卷并保存
	 * @return
	 * @modified
	 */
	public void saveExamTestPaperInExam(HttpServletRequest request,Exam exam)throws Exception;
	
	
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
	 * 发布试卷
	 * @description
	 * @param exam_arrange_id
	 * @modified
	 */
	public void saveAndPublicInExamArrange(String exam_arrange_id,String employeeId,String employeeName)throws Exception;
	
	/**
	 * 保存并发布成绩
	 * @author 朱健
	 * @param examArrange
	 * @return
	 * @modified
	 */
	public String saveAndPublicExam(String examArrangeId,UserSession usersess)throws Exception;
	
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
	 * @param topDeptId
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
	 * 发出通知
	 * @description
	 * @param exam
	 * @param usersess
	 * @modified
	 */
	public void addExamToSysAffiche(Exam exam,UserSession usersess)throws Exception;
	
	
	/**
	 * 生成对应试卷
	 * @description
	 * @param exam
	 * @modified
	 */
	public void loadUserExam(Exam exam,HttpServletRequest request) throws Exception;
	
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
