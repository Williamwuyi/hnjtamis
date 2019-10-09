package cn.com.ite.hnjtamis.exam.exampaper.jsonFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.exam.exampaper.ExampaperDao;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamNameForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserInticketForm;

/**
 * 用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
 * @author 朱健
 * @create time: 2016年3月9日 上午9:25:48
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamUserInticketMap {

	
	//用户信息与准考证映射Map
	private static boolean readUserInticketMapFlag = true;
	public static Map<String,String[]> UserInticketMap = new HashMap<String,String[]>();
	public static List<ExamNameForm> examNamesList = new ArrayList<ExamNameForm>();
	
	//备份UserInticketMap信息，用于UserInticketMap更新的时候调用
	private static boolean readUserInticketBackMapFlag = true;
	private static Map<String,String[]> UserInticketBackMap = new HashMap<String,String[]>();
	public static List<ExamNameForm> examNamesBackList = new ArrayList<ExamNameForm>();
	
	private static List<ExamUserInticketForm> examUserInticketFormList = null;
	
	//用户名或身份证对应的考试
	private static Map<String,List<ExamNameForm>> examUserInticketFormMap = new HashMap<String,List<ExamNameForm>>();
	
	private static boolean isInitFlag = true;
	
	private static boolean isInitExe = true;//是否正在处理
	
	private static boolean isCallOne = false;//是否要重复一次
	
	public static void initUserInticketMap(){
		//String nowDay = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd");
		//if(examStartTime!=null && examStartTime.substring(0,10).equals(nowDay)){}{
		if(isInitExe){
			isInitExe = false;
			readUserInticketMapFlag = false;
			ExampaperDao exampaperDao = (ExampaperDao) SpringContextUtil.getBean("exampaperDao");
			examUserInticketFormList = exampaperDao.getExamUserInticketRelation();
			examNamesList = new ArrayList();
			examUserInticketFormMap = new HashMap<String,List<ExamNameForm>>();
			Map examNamesMap = new HashMap();
			if(examUserInticketFormList!=null && examUserInticketFormList.size()>0){
				String userMappingStr = "{result:[";
				for(int i=0;i<examUserInticketFormList.size();i++){
					ExamUserInticketForm examUserInticketForm = examUserInticketFormList.get(i);
					//根据用户名与登录密码设置准考证
					String userSignature = getUserSignature( examUserInticketForm.getAccount().trim(),
							examUserInticketForm.getPassword(),examUserInticketForm.getExamId());
					setUserAndInticket(userSignature,examUserInticketForm.getInticket(),examUserInticketForm.getExamPassword());
					userMappingStr+="{userId:'"+userSignature+"',inticket:'"+examUserInticketForm.getInticket()+"',examPassword:'"+examUserInticketForm.getExamPassword()+"'},";
					
					
					//根据身份证与登录密码设置准考证
					if(examUserInticketForm.getIdentityCard()!=null && !"".equals(examUserInticketForm.getIdentityCard())
							&& !"null".equals(examUserInticketForm.getIdentityCard())){
						userSignature =getUserSignature(examUserInticketForm.getIdentityCard().trim(),examUserInticketForm.getPassword(),examUserInticketForm.getExamId());
						setUserAndInticket(userSignature,examUserInticketForm.getInticket(),examUserInticketForm.getExamPassword());
						userMappingStr+="{userId:'"+userSignature+"',inticket:'"+examUserInticketForm.getInticket()+"',examPassword:'"+examUserInticketForm.getExamPassword()+"'},";
					}
					
					if(examNamesMap.get(examUserInticketForm.getExamId())==null){
						ExamNameForm examNameForm = new ExamNameForm();
						examNameForm.setExamId(examUserInticketForm.getExamId());
						examNameForm.setExamName(examUserInticketForm.getExamName());
						examNamesList.add(examNameForm);
						examNamesMap.put(examUserInticketForm.getExamId(),examUserInticketForm.getExamId());
					}
					
					
					//用户名或身份证对应的考试
					List<ExamNameForm> tmpList = examUserInticketFormMap.get(examUserInticketForm.getAccount());
					if(tmpList == null){
						tmpList = new ArrayList();
					}
					ExamNameForm examNameForm = new ExamNameForm();
					examNameForm.setExamId(examUserInticketForm.getExamId());
					examNameForm.setExamName(examUserInticketForm.getExamName());
					tmpList.add(examNameForm);
					examUserInticketFormMap.put(examUserInticketForm.getAccount(),tmpList);
					
					
					tmpList = examUserInticketFormMap.get(examUserInticketForm.getIdentityCard());
					if(tmpList == null){
						tmpList = new ArrayList();
					}
					examNameForm = new ExamNameForm();
					examNameForm.setExamId(examUserInticketForm.getExamId());
					examNameForm.setExamName(examUserInticketForm.getExamName());
					tmpList.add(examNameForm);
					examUserInticketFormMap.put(examUserInticketForm.getIdentityCard(),tmpList);
				}
				//保存用户-身份证 与准考证映射
				userMappingStr =userMappingStr.substring(0,userMappingStr.length()-1);
				userMappingStr+="]}";
				/*String path =  ExamVariable.getUserMappingPath(null);
				try {
					File file = new File(path);
					file.deleteOnExit();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileOption.saveStringToFile(path, userMappingStr,"UTF-8");*/
				
				
				//以下为系统保存用户与参加考试信息
				String userExamStr = "{result:[";
				boolean isHave = false;
				if(examNamesList!=null&&examNamesList.size()>0){
					userExamStr+="{userId:'all',examlist:[";
					for(int i=0;i<examNamesList.size();i++){
						ExamNameForm examNameForm = examNamesList.get(i);
						userExamStr+="{examName:'"+examNameForm.getExamName()+"',examId:'"+examNameForm.getExamId()+"'},";
					}
					userExamStr = userExamStr.substring(0,userExamStr.length()-1);
					userExamStr+="]},";
					isHave = true;
				}
				
				if(!examUserInticketFormMap.isEmpty()){
					Iterator its = examUserInticketFormMap.keySet().iterator();
					while(its.hasNext()){
						String id = (String)its.next();
						List<ExamNameForm> list = examUserInticketFormMap.get(id);
						
						if(list!=null&&list.size()>0){
							userExamStr+="{userId:'"+id+"',examlist:[";
							for(int i=0;i<list.size();i++){
								ExamNameForm examNameForm = list.get(i);
								userExamStr+="{examName:'"+examNameForm.getExamName()+"',examId:'"+examNameForm.getExamId()+"'},";
							}
							userExamStr = userExamStr.substring(0,userExamStr.length()-1);
							userExamStr+="]},";
							isHave = true;
						}
					}
				}
				if(isHave){
					userExamStr = userExamStr.substring(0,userExamStr.length()-1);
				}
				userExamStr+="]}";
				/*path =  ExamVariable.getUserExamPath(null) ;
				try {
					File file = new File(path);
					file.deleteOnExit();
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				FileOption.saveStringToFile(path, userExamStr,"UTF-8");*/
				
				
			}
			readUserInticketMapFlag = true;
			
			
			readUserInticketBackMapFlag = false;
			UserInticketBackMap.clear();
			UserInticketBackMap.putAll(UserInticketMap);
			examNamesBackList = new ArrayList();
			examNamesBackList.addAll(examNamesList);
			readUserInticketBackMapFlag = true;
			isInitExe = true;
			
			if(isCallOne){//如果要重复调用
				isCallOne = false;
				initUserInticketMap();
			}
		}else{
			isCallOne = true;
		}
		//}
	}
	
	
	/**
	 * 根据 userName，md5_password，examId生成 Signature，其中userName与examId需要进行md5加密，而md5_password是已经md5加密过的
	 * @author 朱健
	 * @param userName
	 * @param md5_password
	 * @param examId
	 * @return
	 * @modified
	 */
	public static String getUserSignature(String userName,String md5_password,String examId){
		String value = CharsetSwitchUtil.encryptPassword(userName.trim())+md5_password+CharsetSwitchUtil.encryptPassword(examId);
		value = CharsetSwitchUtil.encryptPassword(value);
		value = value.substring(0,30);
		return value;
	}
	
	/**
	 * 根据登录的用户名，用户录入的密码（未md5加密），examId获取准考证与密码
	 * @author 朱健
	 * @param userName
	 * @param unmd5_password
	 * @param examId
	 * @return
	 * @modified
	 */
	public  static String[] getUserAndInticket(String userName,String unmd5_password,String examId){
		String userSignature = getUserSignature(userName.trim(),CharsetSwitchUtil.encryptPassword(unmd5_password),examId);
		return getUserAndInticket(userSignature);
	}
	
	/**
	 * 根据Signature插入对应的准考证与密码
	 * @author 朱健
	 * @param userSignature
	 * @param inticket
	 * @param inticketPwd
	 * @modified
	 */
	public static void setUserAndInticket(String userSignature,String inticket,String inticketPwd){
		UserInticketMap.put(userSignature,new String[]{inticket,inticketPwd});
	}
	
	/**
	 * 根据Signature获取对应的准考证与密码
	 * @author 朱健
	 * @param userSignature
	 * @param inticket
	 * @param inticketPwd
	 * @modified
	 */
	public static String[] getUserAndInticket(String userSignature){
		if(isInitFlag){//还没有初始化过，需要进行一次初始化
			initUserInticketMap();
			isInitFlag = false;
		}
		if(readUserInticketMapFlag){
			return UserInticketMap.get(userSignature);
		}else if(readUserInticketBackMapFlag){
			return UserInticketBackMap.get(userSignature);
		}else{
			return getUserAndInticket(userSignature);
		}
	}
	
	
	/**
	 * 获取当前日期的考试信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public static List<ExamNameForm> getNowDayExamNamesList(){
		if(isInitFlag){//还没有初始化过，需要进行一次初始化
			initUserInticketMap();
			isInitFlag = false;
		}
		if(readUserInticketMapFlag){
			return examNamesList;
		}else if(readUserInticketBackMapFlag){
			return examNamesBackList;
		}else{
			return getNowDayExamNamesList();
		}
	}
	
	public static List<ExamNameForm> getNowDayExamNamesListByAccountOrId(String accountOrId){
		if(isInitFlag){//还没有初始化过，需要进行一次初始化
			initUserInticketMap();
			isInitFlag = false;
		}
		if(readUserInticketMapFlag){
			return examUserInticketFormMap.get(accountOrId);
		}else{
			return null;
		}
	}
	
	
}
