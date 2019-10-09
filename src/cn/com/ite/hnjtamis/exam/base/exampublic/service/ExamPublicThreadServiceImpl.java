package cn.com.ite.hnjtamis.exam.base.exampublic.service;



import java.util.*;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.common.thread.*;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeStandardQuarterForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;

public class ExamPublicThreadServiceImpl extends DefaultServiceImpl implements ExamPublicThreadService{

	
	
	private static boolean isNowExeExamPublicThreadRun = true;//是否正在处理   true-正在运行
	
	private static List<Object[]> examPublicList = new ArrayList<Object[]>();//需要处理的试题
	
	private static boolean isInitThread = true;

	
	/**
	 * 构造，同时启动日志保存线程
	 * @modified
	 */
	public ExamPublicThreadServiceImpl() throws Exception{
		if(isInitThread){
			isInitThread = false;
			ThreadManger.openThread(new Run(){
				public void hander() {
					try {
						if(examPublicList.size()>0 && isNowExeExamPublicThreadRun){
							isNowExeExamPublicThreadRun = false;
							System.out.println(" ExamPublic thread start = 共处理"+examPublicList.size()+"场发布信息...............................");
							ExamPublicService examPublicService = (ExamPublicService)SpringContextUtil.getBean("examPublicServer");
							for(int i=0;i<examPublicList.size();i++){
								Object[] value= examPublicList.get(0);
								String examPublicId = (String)value[0];
								ThemeBank themeBank = (ThemeBank)value[1];
								List<Speciality> specialityList = (List<Speciality>)value[2];
								List<ThemeStandardQuarterForm> themeStandardQuarterFormList = (List<ThemeStandardQuarterForm>)value[3];
								List<Organ> organList = (List<Organ>)value[4];
								UserSession usersess = (UserSession)value[5];
								boolean isPublic = (boolean)value[6];
								examPublicService.saveSearchInfoAndExamPublicUser( examPublicId, themeBank, specialityList,
										 themeStandardQuarterFormList, organList,usersess, isPublic);
								StaticVariable.publicIdMap.remove(examPublicId);
								examPublicList.remove(0);
							}
							System.out.println(" ExamPublic thread end 处理完成...............................");
							isNowExeExamPublicThreadRun = true;
						}
					} catch (Exception e) {
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
				}}, 10000, 3000);
		}
	}
	
	/**
	 * 添加处理信息
	 * @description
	 * @param exam
	 * @param request
	 * @param usersess
	 * @modified
	 */
	public void addExamPublic(String examPublicId,ThemeBank themeBank,List<Speciality> specialityList,
			List<ThemeStandardQuarterForm> themeStandardQuarterFormList,List<Organ> organList,UserSession usersess,boolean isPublic){
		StaticVariable.publicIdMap.put(examPublicId,examPublicId);
		examPublicList.add(new Object[]{examPublicId,themeBank,specialityList,themeStandardQuarterFormList,organList,usersess,isPublic});
		
	}
	
}