package cn.com.ite.hnjtamis.common;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.hnjtamis.param.SystemParamsService;
import cn.com.ite.hnjtamis.param.domain.SystemParams;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.common.ExamVariable</p>
 * <p>Description 考试使用关键字与变量</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月20日 上午9:43:05
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamVariable { 
	
	//培训抽取试题方式初始化 ,格式： {类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},注意以','号结束
	public static String getTrainImplementExamPaperInitThemeType(){
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("trainImplement_ExamPaper_Init_ThemeType");
		String trainImplement_ExamPaper_Init_ThemeType = systemParams!=null ? systemParams.getValue() : null;
		if(trainImplement_ExamPaper_Init_ThemeType == null 
				|| "".equals(trainImplement_ExamPaper_Init_ThemeType)
				|| "null".equals(trainImplement_ExamPaper_Init_ThemeType)){
			trainImplement_ExamPaper_Init_ThemeType = StaticVariable.trainImplement_ExamPaper_Init_ThemeType;
		}
		return trainImplement_ExamPaper_Init_ThemeType;
	}
	
	//Excle页签映射
	public static String[][] getXlsThemeTageNames(){
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("xls_theme_tageNames");
		String xls_theme_tageNamesStr = systemParams!=null ? systemParams.getValue() : null;
		
		String[][] xls_theme_tageNames = null;
		if(xls_theme_tageNamesStr!=null 
				&& !"".equals(xls_theme_tageNamesStr) 
				&& !"null".equals(xls_theme_tageNamesStr)){
			String[] arr = xls_theme_tageNamesStr.split("@@");
			xls_theme_tageNames = new String[arr.length][];
			for(int i=0;i<arr.length;i++){
				xls_theme_tageNames[i] = arr[i].split(",");
			}
		}
		if(xls_theme_tageNames == null){
			xls_theme_tageNames = StaticVariable.xls_theme_tageNames;
		}
		return xls_theme_tageNames;
	}
	
	//选择题导入模版
	public static String[][] getxlsThemeExpTemplateChoiceQuestion(){
		String[][] template = null;
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_choiceQuestion");
		String xls_theme_ExpTemplate_choiceQuestionStr = systemParams!=null ? systemParams.getValue() : null;
		if(xls_theme_ExpTemplate_choiceQuestionStr!=null 
				&& !"".equals(xls_theme_ExpTemplate_choiceQuestionStr) 
				&& !"null".equals(xls_theme_ExpTemplate_choiceQuestionStr)){
			String[] arr = xls_theme_ExpTemplate_choiceQuestionStr.split("@@");
			template = new String[arr.length][];
			for(int i=0;i<arr.length;i++){
				System.out.println(arr[i]);
				template[i] = arr[i].split(",");
			}
		}
		
		if(template == null){
			template = StaticVariable.xls_theme_ExpTemplate_choiceQuestion;	
		}
		return template;
	}
	
	//选择判断题模版
	public static String[][] getxlsThemeExpTemplateTrueOrFalse(){
		String[][] template = null;
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_trueOrFalse");
		String xls_theme_ExpTemplate_trueOrFalseStr = systemParams!=null ? systemParams.getValue() : null;
		if(xls_theme_ExpTemplate_trueOrFalseStr!=null 
				&& !"".equals(xls_theme_ExpTemplate_trueOrFalseStr) 
				&& !"null".equals(xls_theme_ExpTemplate_trueOrFalseStr)){
			String[] arr = xls_theme_ExpTemplate_trueOrFalseStr.split("@@");
			template = new String[arr.length][];
			for(int i=0;i<arr.length;i++){
				template[i] = arr[i].split(",");
			}
		}
		if(template == null){
			template = StaticVariable.xls_theme_ExpTemplate_trueOrFalse;	
		}
		return template;
	}

	//选择填空题模版
	public static String[][] getxlsThemeExpTemplateFillBlank(){
		String[][] template = null;
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_fillBlank");
		String xls_theme_ExpTemplate_fillBlankStr = systemParams!=null ? systemParams.getValue() : null;
		if(xls_theme_ExpTemplate_fillBlankStr!=null 
				&& !"".equals(xls_theme_ExpTemplate_fillBlankStr) 
				&& !"null".equals(xls_theme_ExpTemplate_fillBlankStr)){
			String[] arr = xls_theme_ExpTemplate_fillBlankStr.split("@@");
			template = new String[arr.length][];
			for(int i=0;i<arr.length;i++){
				template[i] = arr[i].split(",");
			}
		}
		if(template == null){
			template = StaticVariable.xls_theme_ExpTemplate_fillBlank;	
		}
		return template;
	}
	
	//选择问答题模版
	public static String[][] getxlsThemeExpTemplateShortAnswer(){
		String[][] template = null;
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_shortAnswer");
		String xls_theme_ExpTemplate_shortAnswerStr = systemParams!=null ? systemParams.getValue() : null;
		if(xls_theme_ExpTemplate_shortAnswerStr!=null 
				&& !"".equals(xls_theme_ExpTemplate_shortAnswerStr) 
				&& !"null".equals(xls_theme_ExpTemplate_shortAnswerStr)){
			String[] arr = xls_theme_ExpTemplate_shortAnswerStr.split("@@");
			template = new String[arr.length][];
			for(int i=0;i<arr.length;i++){
				template[i] = arr[i].split(",");
			}
		}
		if(template == null){
			template = StaticVariable.xls_theme_ExpTemplate_shortAnswer;	
		}
		return template;
	}
	
	
	   //选择论述题模版
		public static String[][] getxlsThemeExpTemplateDiscussAnswer(){
			String[][] template = null;
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_discussAnswer");
			String xls_theme_ExpTemplate_shortAnswerStr = systemParams!=null ? systemParams.getValue() : null;
			if(xls_theme_ExpTemplate_shortAnswerStr!=null 
					&& !"".equals(xls_theme_ExpTemplate_shortAnswerStr) 
					&& !"null".equals(xls_theme_ExpTemplate_shortAnswerStr)){
				String[] arr = xls_theme_ExpTemplate_shortAnswerStr.split("@@");
				template = new String[arr.length][];
				for(int i=0;i<arr.length;i++){
					template[i] = arr[i].split(",");
				}
			}
			if(template == null){
				template = StaticVariable.xls_theme_ExpTemplate_shortAnswer;//可以就使用简答题的模版
			}
			return template;
		}
		
		
		//选择计算题模版
		public static String[][] getxlsThemeExpTemplateCalTheme(){
			String[][] template = null;
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_calTheme");
			String xls_theme_ExpTemplate_shortAnswerStr = systemParams!=null ? systemParams.getValue() : null;
			if(xls_theme_ExpTemplate_shortAnswerStr!=null 
					&& !"".equals(xls_theme_ExpTemplate_shortAnswerStr) 
					&& !"null".equals(xls_theme_ExpTemplate_shortAnswerStr)){
				String[] arr = xls_theme_ExpTemplate_shortAnswerStr.split("@@");
				template = new String[arr.length][];
				for(int i=0;i<arr.length;i++){
					template[i] = arr[i].split(",");
				}
			}
			if(template == null){
				template = StaticVariable.xls_theme_ExpTemplate_shortAnswer;//可以就使用简答题的模版	
			}
			return template;
		}

		//选择绘图模版
		public static String[][] getxlsThemeExpTemplateDrawTheme(){
			String[][] template = null;
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("xls_theme_ExpTemplate_drawTheme");
			String xls_theme_ExpTemplate_shortAnswerStr = systemParams!=null ? systemParams.getValue() : null;
			if(xls_theme_ExpTemplate_shortAnswerStr!=null 
							&& !"".equals(xls_theme_ExpTemplate_shortAnswerStr) 
							&& !"null".equals(xls_theme_ExpTemplate_shortAnswerStr)){
				String[] arr = xls_theme_ExpTemplate_shortAnswerStr.split("@@");
				template = new String[arr.length][];
				for(int i=0;i<arr.length;i++){
					template[i] = arr[i].split(",");
				}
			}
			if(template == null){
				template = StaticVariable.xls_theme_ExpTemplate_shortAnswer;//可以就使用简答题的模版	
			}
			return template;
		}
		
	
	private static HttpServletRequest http_request = null;
	
	//服务器存放的考生信息文件路径USER_FILE_PATH
	private static String USER_FILE_PATH = null;
	public static String getExamUserFilePath(HttpServletRequest request){
		if(USER_FILE_PATH !=null){
			return USER_FILE_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("USER_FILE_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"user","");
			}
		}
		creatFilePath(path);
		USER_FILE_PATH = path;
		return path;
	}
	
	//服务器存放的试题文件路径 EXAM_FILE_PATH
	private static String EXAM_FILE_PATH = null;
	public static String getExamFilePath(HttpServletRequest request){
		if(EXAM_FILE_PATH !=null){
			return EXAM_FILE_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("EXAM_FILE_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"title","");
			}
		}
		creatFilePath(path);
		EXAM_FILE_PATH = path;
		return path;		
	}
	
	
	
	
	//考生答案文件路径 SAVE_ANS_PATH
	private static String SAVE_ANS_PATH = null;
	public static String getSaveAnsPath(HttpServletRequest request){
		if(SAVE_ANS_PATH !=null){
			return SAVE_ANS_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("SAVE_ANS_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"ans_bak","");
			}
		}
		creatFilePath(path);
		SAVE_ANS_PATH = path;
		return path;
	}
	
	
	//服务器存放正确答案文件路径ANS_FILE_PATH
	private static String ANS_FILE_PATH = null;
	public static String getAnsFilePath(HttpServletRequest request){
		if(ANS_FILE_PATH !=null){
			return ANS_FILE_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("ANS_FILE_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"ans","");
			}
		}
		creatFilePath(path);
		ANS_FILE_PATH = path;
		return path;
	}
		
		
	//服务器存放考试结束标识的文件路径SAVE_OVER_FLAG
	private static String SAVE_OVER_PATH = null;
	public static String getExamSaveOverFlagPath(HttpServletRequest request){
		if(SAVE_OVER_PATH !=null){
			return SAVE_OVER_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("SAVE_OVER_FLAG");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"exam","");
			}
		}
		creatFilePath(path);
		SAVE_OVER_PATH = path;
		return path;	
	}
	
	
		//考生本日可参加考试文件USER_EXAM_PATH
		private static String USER_EXAM_PATH = null;
		public static String getUserExamPath(HttpServletRequest request){
			if(USER_EXAM_PATH !=null){
				return USER_EXAM_PATH;
			}
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("USER_EXAM_PATH");
			String path = systemParams!=null ? systemParams.getValue() : null;
			if(path==null){
				if(request == null && http_request!=null){
					request = http_request;
				}
				if(http_request == null && request!=null){
					http_request = request;
				}
				if(request!=null){
					path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"user","");
					path += System.getProperty("file.separator")+"userExam.txt";
					File file = new File(path);
					creatFilePath(path);
					if(!file.exists()){
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				File file = new File(path);
				creatFilePath(path);
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			USER_EXAM_PATH = path;
			return USER_EXAM_PATH;	
		}
		
		//考生本日可参加考试文件USER_EXAM_PATH
		private static String USER_MAPPING_PATH = null;
		public static String getUserMappingPath(HttpServletRequest request){
			if(USER_MAPPING_PATH !=null){
				return USER_MAPPING_PATH;
			}
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("USER_MAPPING_PATH");
			String path = systemParams!=null ? systemParams.getValue() : null;
			if(path==null){
				if(request == null && http_request!=null){
					request = http_request;
				}
				if(http_request == null && request!=null){
					http_request = request;
				}
				if(request!=null){
					path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"user","");
					path += System.getProperty("file.separator")+"userMapping.txt";
					File file = new File(path);
					creatFilePath(path);
					if(!file.exists()){
						try {
							file.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}else{
				File file = new File(path);
				creatFilePath(path);
				if(!file.exists()){
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			USER_MAPPING_PATH = path;
			return USER_MAPPING_PATH;	
	}
	
		
		
	private static String MONI_EXAM_FILE_PATH = null; //存放模拟试题-试卷
	public static String getMoniExamFilePath(HttpServletRequest request){
		if(MONI_EXAM_FILE_PATH !=null){
			return MONI_EXAM_FILE_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("MONI_EXAM_FILE_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"moni"+System.getProperty("file.separator")+"title","");
			}
		}
		creatFilePath(path);
		MONI_EXAM_FILE_PATH = path;
		return path;
	}
	
	
	private static String MONI_EXAM_USERANS_FILE_PATH = null; //存放模拟试题-试卷
	public static String getMoniExamUserAnsFilePath(HttpServletRequest request){
		if(MONI_EXAM_USERANS_FILE_PATH !=null){
			return MONI_EXAM_USERANS_FILE_PATH;
		}
		SystemParamsService systemParamsService = getSystemParamsService();
		SystemParams systemParams = systemParamsService.findByCode("MONI_EXAM_USERANS_FILE_PATH");
		String path = systemParams!=null ? systemParams.getValue() : null;
		if(path==null){
			if(request == null && http_request!=null){
				request = http_request;
			}
			if(http_request == null && request!=null){
				http_request = request;
			}
			if(request!=null){
				path = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"moni"+System.getProperty("file.separator")+"userans","");
			}
		}
		creatFilePath(path);
		MONI_EXAM_USERANS_FILE_PATH = path;
		return path;
	}
	
	public static void creatFilePath(String path){
		if(path!=null && !"".equals(path) && !"null".equals(path)){
			File file = new File(path);
			if(!file.isDirectory()){
				file.mkdirs();
			 }
		}
	}
	
	private static String IOCPURL = null;
	public static String getRequestUrl(HttpServletRequest request){
		if(IOCPURL==null){
			SystemParamsService systemParamsService = getSystemParamsService();
			SystemParams systemParams = systemParamsService.findByCode("IOCPURL");
			IOCPURL = systemParams!=null ? systemParams.getValue() : null;
			if(IOCPURL == null){
				IOCPURL = request.getScheme()+"://"+request.getServerName()+":80";
			}
		}
		return IOCPURL;	
	}
	
	private static SystemParamsService systemParamsService = null;
	private static SystemParamsService getSystemParamsService(){
		if(systemParamsService == null){
			systemParamsService = (SystemParamsService)SpringContextUtil.getBean("systemParamsService");
			return systemParamsService;
		}else{
			return systemParamsService;
		}
	}
	
	public static HttpServletRequest getHttpRequest(){
		 return http_request;
	}
	
	public static void setHttpRequest(HttpServletRequest _http_request){
		http_request = _http_request;
	}
	
}
