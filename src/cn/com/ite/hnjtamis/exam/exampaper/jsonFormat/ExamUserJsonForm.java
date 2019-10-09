package cn.com.ite.hnjtamis.exam.exampaper.jsonFormat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperJsonForm</p>
 * <p>Description 考试题目信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月17日 上午9:54:28
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamUserJsonForm extends ExamJsonForm{

	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static void getAndSaveExamUser(HttpServletRequest request,Exam exam){
		String jsonStr = ExamUserJsonForm.getJsonStrInExamUser(exam);
		String fileName = "user_"+exam.getExamId()+".txt";
		String path = ExamVariable.getExamUserFilePath(request) + fileName;
		FileOption.saveStringToFile(path, jsonStr,"UTF-8");
	}
	
	public static boolean isHaveFile(HttpServletRequest request,Exam exam){
		String fileName = "user_"+exam.getExamId()+".txt";
		String path = ExamVariable.getExamUserFilePath(request) + fileName;
		File file = new File(path);
		return file.exists();
	}
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInExamUser(Exam exam){
		List<ExamUserTestpaper> examUserTestpaperList = exam.getExamUserTestpapers();
		String examTestpaperStr = "{result:[";
		boolean haveTestpaper = false;
		for(int i=0;i<examUserTestpaperList.size();i++){
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperList.get(i);
			if(examUserTestpaper.getExamTestpaper()!=null){
				examTestpaperStr+="{examId:'"+exam.getExamId()+"',";
				examTestpaperStr+="examName:'"+exam.getExamName()+"',";
				examTestpaperStr += "userTestPaperId:'"+examUserTestpaper.getUserTestpaperId()+"',";
				examTestpaperStr += "testPaperId:'"+examUserTestpaper.getExamTestpaper().getExamTestpaperId()+"',";
				//examTestpaperStr+="userOrganId:'"+examUserTestpaper.getUserOrganId()+"',";//所属机构ID
				examTestpaperStr+="userOrganName:'"+examUserTestpaper.getUserOrganName()+"',";//所属机构
				//examTestpaperStr+="userDeptId:'"+examUserTestpaper.getUserDeptId()+"',";//所属部门ID
				examTestpaperStr+="userDeptName:'"+stringToJson(examUserTestpaper.getUserDeptName())+"',";//所属部门
				//examTestpaperStr+="userGroupId:'"+examUserTestpaper.getUserGroupId()+"',";//所属班组ID
				examTestpaperStr+="userGroupName:'"+stringToJson(examUserTestpaper.getUserGroupName())+"',";//所属班组
				//examTestpaperStr+="postId:'"+examUserTestpaper.getPostId()+"',";//所属岗位ID
				examTestpaperStr+="postName:'"+examUserTestpaper.getPostName()+"',";//所属岗位
				examTestpaperStr+="userName:'"+stringToJson(examUserTestpaper.getUserName())+"',";//姓名
				examTestpaperStr+="userSex:'"+stringToJson(examUserTestpaper.getUserSex())+"',";//性别  1-男  2-女
				examTestpaperStr+="inticket:'"+stringToJson(examUserTestpaper.getInticket())+"',";//准考证号
				examTestpaperStr+="idNumber:'"+stringToJson(examUserTestpaper.getIdNumber())+"',";//身份证号
				if(examUserTestpaper.getLoginId()!=null && !"".equals(examUserTestpaper.getLoginId())
						&& !"null".equals(examUserTestpaper.getLoginId())){
					examTestpaperStr+="loginUserId:'"+stringToJson(examUserTestpaper.getLoginId())+"',";//登录账户
					examTestpaperStr+="userPassword:'"+stringToJson(examUserTestpaper.getLoginPassword())+"',";//登录密码
				}
				//examTestpaperStr+="userBirthday:'"+examUserTestpaper.getUserBirthday()+"',";//出生年月
				//examTestpaperStr+="userNation:'"+stringToJson(examUserTestpaper.getUserNation())+"',";//民族
				//examTestpaperStr+="userAddr:'"+examUserTestpaper.getUserAddr()+"',";//住址
				//examTestpaperStr+="userPhone:'"+examUserTestpaper.getUserPhone()+"',";//联系电话
				//examTestpaperStr+="userInfo:'"+examUserTestpaper.getUserInfo()+"',";//考生信息
				examTestpaperStr+="examPassword:'"+stringToJson(examUserTestpaper.getExamPassword())+"',";//考试密码
				//examTestpaperStr+="examPaperType:'"+examUserTestpaper.getExamPaperType()+"',";//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟 30-练习
				examTestpaperStr+="inTime:'"+examUserTestpaper.getInTime()+"',";//入场时间
				examTestpaperStr+="outTime:'"+examUserTestpaper.getOutTime()+"',";//离场时间
				//examTestpaperStr+="isLocked:'"+examUserTestpaper.getIsLocked()+"',";//是否锁定 0-未锁定  5-锁定
				examTestpaperStr+="subTime:'"+examUserTestpaper.getSubTime()+"',";///交卷时间
				examTestpaperStr+="loginUrl:'"+examUserTestpaper.getLoginUrl()+"',";///登陆地址
				//examTestpaperStr+="examRootName:'"+stringToJson(examUserTestpaper.getExamRootName())+"',";//考点名称
				//examTestpaperStr+="examRootPlace:'"+stringToJson(examUserTestpaper.getExamRootPlace())+"',";//考点地点
				//examTestpaperStr+="seatNum:'"+examUserTestpaper.getSeatNum()+"',";//座位号
				examTestpaperStr+="isIdNumberLogin:'"+examUserTestpaper.getIsIdNumberLogin()+"',";//是否允许身份证登陆 0-允许  1-不允许
				//examTestpaperStr+="examPaperInit:'"+examUserTestpaper.getExamPaperInit()+"',";//试卷是否初始化 10-否 11-初始化失败 20-是  21-需要进行重置
				examTestpaperStr+="state:'"+examUserTestpaper.getState()+"',";//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				examTestpaperStr+="organName:'"+stringToJson(examUserTestpaper.getOrganName())+"',";//机构名
				//examTestpaperStr+="organId:'"+examUserTestpaper.getOrganId()+"',";//机构编号
				//examTestpaperStr+="employeeId:'"+examUserTestpaper.getEmployeeId()+"',";//人员ID
				//examTestpaperStr+="employeeName:'"+examUserTestpaper.getEmployeeName()+"',";//人员
				examTestpaperStr+="examStartTime:'"+exam.getExamStartTime()+"',";//开始时间
				examTestpaperStr+="examEndTime:'"+exam.getExamEndTime()+"',";//结束时间
				examTestpaperStr+="examBanTime:'"+exam.getBanTime()+"',";//开考后禁止进入时间
				examTestpaperStr+="examBeforeTime:'"+exam.getBeforeTime()+"'";//提前进入时间
				examTestpaperStr+="},";
				haveTestpaper = true;
			}
			
		}
		if(!"".equals(examTestpaperStr) && haveTestpaper)examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
		examTestpaperStr+="]}";
		return examTestpaperStr;
	}
}
