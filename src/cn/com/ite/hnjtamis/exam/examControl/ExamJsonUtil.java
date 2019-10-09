package cn.com.ite.hnjtamis.exam.examControl;


import cn.com.ite.eap2.core.service.DefaultRedisService;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;

public class ExamJsonUtil {
	
	
	public static boolean saveJson(String examFileType,String fileName,String json){
		boolean saveSucc = false;
		try{
			if(ExamJsonUtil.EXAM_TYPE_SAVE_IN_FILE.equals(getSaveType())){
				String path = getFilePathName(examFileType,fileName);
				if(path!=null)saveSucc = FileOption.saveStringToFile(path, json,"UTF-8");
			}else if(ExamJsonUtil.EXAM_TYPE_SAVE_IN_REDIS.equals(getSaveType())){
				saveSucc = getRedisService().save(fileName, json);
			}
		}catch(Exception e){
			e.printStackTrace();
			saveSucc = false;
		}
		return saveSucc;
	}
	
	private static String getFilePathName(String examFileType,String fileName){
		String path = null;
		if(ExamJsonUtil.EXAM_TESTPAPER_FILE.equals(examFileType)){
			path = ExamVariable.getExamUserFilePath(null) + fileName;
		}
		return path;
	}
	
	
	public static String getJson(String examFileType,String fileName){
		String json = null;
		try{
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return json;
	}
	
	
	/*考生路由信息:UserName可为身份证号、准考证号、普通用户名。同一考生在该表中最多可有3条记录，每条记录所对应的MapId是唯一的*/
	public static final String EXAM_USER = "EXAM_USER";
	
	
	/*存放的考生信息文件*/
	public static final String EXAM_USER_FILE = "EXAM_USER_FILE";
	/*服务器存放的试题*/
	public static final String EXAM_TESTPAPER_FILE = "EXAM_TESTPAPER_FILE";
	/*服务器存放的考生答案*/
	public static final String EXAM_USER_ANS = "EXAM_USER_ANS";
	/*服务器存放的正确答案*/
	public static final String EXAM_TESTPAPER_ANS = "EXAM_TESTPAPER_ANS";
	/*服务器存放考试结束标识*/
	public static final String EXAM_OVER = "EXAM_OVER";
	
	/*保存方式： FILE文件 */
	private static final String EXAM_TYPE_SAVE_IN_FILE = "FILE";
	/*保存方式： REDIS库 */
	private static final String EXAM_TYPE_SAVE_IN_REDIS = "REDIS";
	
	
	
	private static String EXAM_SAVE_TYPE = null;
	public static String getSaveType(){
		if(ExamJsonUtil.EXAM_SAVE_TYPE == null){
			ExamJsonUtil.EXAM_SAVE_TYPE = ExamJsonUtil.EXAM_TYPE_SAVE_IN_REDIS;
		}
		return ExamJsonUtil.EXAM_SAVE_TYPE;
	}
	
	private static DefaultRedisService redisService = null;
	public static DefaultRedisService getRedisService(){
		if(redisService == null){
			redisService = (DefaultRedisService)SpringContextUtil.getBean("redisDefaultService");
		}
		return redisService;
	}
}
