package cn.com.ite.hnjtamis.exam.online.task;

import java.util.Date;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperService;
import cn.com.ite.hnjtamis.exam.study.StudyThemeService;
import cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx.JobUnionStandardExService;
import cn.com.ite.hnjtamis.jobstandard.terms.StandardTermsService;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.online.task.TaskForm</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月30日 下午1:51:55
 * @version 1.0
 * 
 * @modified records:
 */
public class TaskForm {
	
	private static ExampaperService exampaperService = null;
	
	private static StandardTermsService standardtermsServer;
	
	private static JobUnionStandardExService jobUnionStandardExService;
	
	private static StudyThemeService studyThemeService;

	private static int exeTaskNum = 0;
	
	private static int exeTaskIn0To6Num = 0;
	
	private static String initUserInticketMapDay = null;
	
	private static String yxDay = null;//运行日期，用于判断当天只运行一次
	
	//private static boolean initInOne_userAnsInOld = false;//只运行一次
	
	/**
	 * @author 朱健
	 * @description 执行定时任务
	 * @modified
	 */
	public static void exeAutoStateTaskThree(){
		String nowday = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		try{
		if(studyThemeService == null){
			studyThemeService = (StudyThemeService)SpringContextUtil.getBean("studyThemeService");
		}
		System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器3]任务开始执行。   检测当前内存情况："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		//系统自动产生试卷 每天运行一次 一般是启动后 以及零点的样子
		if(yxDay == null || !yxDay.equals(nowday)){
			yxDay = nowday;
			String relationType = "moni";
			String employeeId = "sysadmin";
			String employeeName = "sysadmin";
			studyThemeService.addStudyTestpaper( relationType, employeeId, employeeName);
			/*if(initInOne_userAnsInOld){
				initInOne_userAnsInOld= false;
				List<String> employeeIds = studyThemeService.getEmployeeIdInExamUserTestpaper(relationType);
				for(int i=0;i<employeeIds.size();i++){
					System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][随堂练习][数据迁移]总数："+employeeIds.size()+" 已处理："+i);
					studyThemeService.updateUserAnsInOld(employeeIds.get(i),relationType);
				}
			}*/
		}
		System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器3]任务结束执行。   检测当前内存情况："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(" 历史数据 处理失败 ... ... ... ... ... ...");
		}
	}
	
	/**
	 * @author 朱健
	 * @description 执行定时任务
	 * @modified
	 */
	public static void exeAutoStateTaskTwo(){
		String nowday = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		try{
			if(exampaperService == null){
				exampaperService=(ExampaperService)SpringContextUtil.getBean("exampaperService");
			}
			if(standardtermsServer == null){
				standardtermsServer=(StandardTermsService)SpringContextUtil.getBean("standardtermsServer");
			}
			if(studyThemeService == null){
				studyThemeService = (StudyThemeService)SpringContextUtil.getBean("studyThemeService");
			}
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器2]任务开始执行。   检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			//每天初始化一次用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
			if(!nowday.equals(initUserInticketMapDay)){
				System.out.println(" 处理用户选择的考试   开始 ... ... ... ... ... ...");
				exampaperService.initUserInticketMap();
				System.out.println(" 处理用户选择的考试   结束 ... ... ... ... ... ...");
				initUserInticketMapDay = nowday;
			}
			
			exampaperService.saveAndsubInExamUserTestpaper();
			
			exampaperService.saveAnswerInUserAnswerFile(ExamVariable.getHttpRequest(),null);
			
			exampaperService.saveNotSubmitAnsExamUserMark();
			
			
			
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器2]任务结束执行。   检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(" 答案处理  失败 ... ... ... ... ... ...");
		}
	}
	/**
	 * @author 朱健
	 * @description 执行定时任务
	 * @modified
	 */
	public static void exeAutoStateTask(){
		String hour = DateUtils.convertDateToStr(new Date(),"HH");
		try{
			if(exampaperService == null){
				exampaperService=(ExampaperService)SpringContextUtil.getBean("exampaperService");
			}
			if(standardtermsServer == null){
				standardtermsServer=(StandardTermsService)SpringContextUtil.getBean("standardtermsServer");
			}
			if(jobUnionStandardExService == null){
				jobUnionStandardExService=(JobUnionStandardExService)SpringContextUtil.getBean("jobUnionStandardExService");
			}
			if(studyThemeService == null){
				studyThemeService = (StudyThemeService)SpringContextUtil.getBean("studyThemeService");
			}
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]任务开始执行。   检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			
			if(exeTaskIn0To6Num == 0 || Integer.parseInt(hour) == 1  || Integer.parseInt(hour) == 5  || Integer.parseInt(hour)==13 || Integer.parseInt(hour)==18){
				System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]开始执行人员题库信息处理。   检测当前内存情况："
						+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
						+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
						+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
				jobUnionStandardExService.updatePersonalBankLearning();
				System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]结束执行人员题库信息处理。   检测当前内存情况："
						+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
						+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
						+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			}
			
			if(exeTaskIn0To6Num == 0 && Integer.parseInt(hour)<6){
				exampaperService.updateQuarterTrainInfo();
				exampaperService.updateThemeCrc();
				exampaperService.updateThemeCode();
				standardtermsServer.saveInitAllJobStandardQuarter();
				
			     //根据标准条款中对应的系统岗位信息，处理不存在的并保存到标准条款中的标准岗位信息里面
				standardtermsServer.updateStandardQuarterNotInSysQuarter();
				//根据标准条款中对应的标准岗位信息更新标准条款对应的系统岗位信息
				standardtermsServer.updateUnionStandardByStandard();
				exeTaskIn0To6Num++;
			}
			if(Integer.parseInt(hour)<6 && exeTaskNum % 10 == 0){//满10次才执行一次
				exampaperService.updateExamTestPaperTotal();
				exampaperService.saveUseTestpaperNum();
				exampaperService.deleteNullExam();
			}
			/**
			 * 保存考生信息与试卷到文件
			 * @author zhujian
			 * @description
			 * @modified
			 */
			exampaperService.saveExamTestpaperInFile();
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][定时器1]任务结束执行。   检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			exeTaskNum++;
		}catch(Exception e){
			e.printStackTrace();
		}
    	
	}
}
