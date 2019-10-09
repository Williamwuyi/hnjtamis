package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.common.thread.Run;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;

public class ExamThreadRunServiceImpl implements Run{
	
	public final static String threadName = "examThread";//线程名称

	private static ExampaperService exampaperService;
	
	private int threadIndex = -1;
	
	public boolean isNowExeExamThreadRun = true;//是否正在处理   true-正在运行

	private List<Object[]> examList = new ArrayList<Object[]>();
	
	ExamThreadRunServiceImpl(int _threadIndex){
		threadIndex = _threadIndex;
	}
	
	public void hander() {
		if(threadIndex>=0){
			int mythreadIndex = threadIndex;
			if(exampaperService == null){
				exampaperService = (ExampaperService)SpringContextUtil.getBean("exampaperService");
			}
			try {
				if(this.examList!=null && this.examList.size()>0 && isNowExeExamThreadRun){
					isNowExeExamThreadRun= false;
					System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+mythreadIndex+"  exam thread start = 共处理"+this.examList.size()+"场考试。");
					for(int ii=0;ii<this.examList.size();){//ii++
						Object[] value= this.examList.get(0);
						String examId= (String)value[0];
						System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+mythreadIndex+" 处理考试安排 id = "+examId+" 开始  当前内存情况： "
								+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
								+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
								+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
						HttpServletRequest request = (HttpServletRequest)value[1];
						UserSession usersess =(UserSession)value[2];
						
						Exam exam = (Exam)exampaperService.findDataByKey(examId, Exam.class);
						if(exam!=null && exam.getExamArrange()!=null && exam.getExamArrange().getExamPublic()!=null){
							List<ExamPublicUser> examPublicUsers =exam.getExamArrange().getExamPublic().getExamPublicUsers();
							try{
								if(examPublicUsers!=null && examPublicUsers.size()>0){
									//添加考生信息
									exampaperService.saveExamUserInExamPublicUsers(exam,examPublicUsers, usersess,request,true);
									//处理每人的试卷信息
									exampaperService.saveExamTestPaperInExam(request,exam);
									//发出通知
									exampaperService.addExamToSysAffiche(exam, usersess);
									
									exampaperService.loadUserExam(exam,request);
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
						StaticVariable.examCrIdMap.remove(examId);
						this.examList.remove(0);
						System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"]["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+mythreadIndex+" 处理考试安排 id = "+examId+" 完成  当前内存情况："
								+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
								+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
								+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
						
						//移动其它线程的放到这个无处理任务的来进行处理
						if(this.examList.size()==0){
							addOtherExamTask();
						}
					}
					System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+mythreadIndex+"  exam thread end 处理完成。");
					exampaperService.initUserInticketMap();
					isNowExeExamThreadRun = true;
				}
			} catch (Exception e) {
				isNowExeExamThreadRun = true;
				e.printStackTrace();
			}
		}
	}

	/**
	 * 优化处理 添加其它线程的任务
	 * @description
	 * @modified
	 */
	private void addOtherExamTask(){
		try{
			for(int i=0;i<StaticVariable.examThreadRunList.length;i++){
				ExamThreadRunServiceImpl examThread = (ExamThreadRunServiceImpl)StaticVariable.examThreadRunList[i];
				if(this.getThreadIndex() == i){
					continue;
				}
				List<Object[]> _examList = examThread.getExamList();
				if(_examList == null || _examList.size()==0 
						|| _examList.size()==1 ){// || _examList.size()==2
					continue;
				}else{
					Object[] tmpObject = _examList.get(_examList.size()-1);
					_examList.remove(_examList.size()-1);
					this.getExamList().add(tmpObject);
					System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+threadIndex+" 从  线程："+i+" exam thread 移过来一个任务。");
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public boolean stop() {
		return false;
	}
	public boolean suspend() {
		return false;
	}
	public boolean start() {
		return false;
	}
	

	public List<Object[]> getExamList() {
		return examList;
	}

	public void setExamList(List<Object[]> examList) {
		this.examList = examList;
	}
	
	public int getThreadIndex() {
		return threadIndex;
	}

	
}
