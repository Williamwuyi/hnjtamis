package cn.com.ite.hnjtamis.exam.exampaper;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.common.thread.*;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;

public class ExamThreadServiceImpl extends DefaultServiceImpl implements ExamThreadService{
	
	private static boolean isInitThread = true;
	
	/**
	 * 构造，同时启动日志保存线程
	 * @modified
	 */
	public ExamThreadServiceImpl() throws Exception{
		if(isInitThread){
			isInitThread = false;//防止多次注入多次初始化这个方法
			//一次性加载多个线程处理
			StaticVariable.examThreadRunList = new Run[StaticVariable.maxExamThreadNum];
			for(int i=0;i<StaticVariable.examThreadRunList.length;i++){
				StaticVariable.examThreadRunList[i] = new ExamThreadRunServiceImpl(i);
				ThreadManger.openThread(StaticVariable.examThreadRunList[i] , 10000, 3000);
			}
		}
	}
	
	
	/**
	 * 添加考试处理信息
	 * @description
	 * @param exam
	 * @param request
	 * @param usersess
	 * @modified
	 */
	public void addExam(String examId,HttpServletRequest request,UserSession usersess){
		if(StaticVariable.examCrIdMap.get(examId) == null){
			StaticVariable.examCrIdMap.put(examId,examId);
			ExamThreadRunServiceImpl examThread = getMixTaskExamThread();
			if(examThread!=null){
				examThread.getExamList().add(new Object[]{examId,request,usersess});
				System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]线程："+examThread.getThreadIndex()+"  exam thread 添加一个任务  当前有"+examThread.getExamList().size()+"任务需要处理。");
			}
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考试安排][线程添加考试人员信息]添加考试安排 id = "+examId+"完成  检测当前内存情况："
					+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
					+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
					+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		}
	}
	
	private  ExamThreadRunServiceImpl getMixTaskExamThread(){
		ExamThreadRunServiceImpl examThread = null;
		for(int i=0;i<StaticVariable.examThreadRunList.length;i++){
			ExamThreadRunServiceImpl _examThread = (ExamThreadRunServiceImpl)StaticVariable.examThreadRunList[i];
			List<Object[]> _examList = _examThread.getExamList();
			//如果任务没有的情况
			if(_examList == null || _examList.size()==0){
				examThread = _examThread;
				break;
			}
			if(i==0 || (examThread!=null && examThread.getExamList().size()>_examList.size())){
				examThread = _examThread;
			}
		}
		return examThread;
	}
}