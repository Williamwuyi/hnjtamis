package cn.com.ite.hnjtamis.exam.exampaper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheIncepter;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.ExamPassword;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.common.Inticket;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserInticketForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperAnswerJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamUserInticketMap;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamUserJsonForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperSkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemetype;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.online.service.IocpService;
import cn.com.ite.hnjtamis.exam.testpaper.TestpaperService;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;
import cn.com.ite.hnjtamis.param.SystemParamsService;
import cn.com.ite.hnjtamis.param.domain.SystemParams;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperServiceImpl</p>
 * <p>Description 考试安排与试卷生成 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午3:01:14
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperServiceImpl extends DefaultServiceImpl implements ExampaperService {
	
	/**
	 * 初始化用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
	 * @author 朱健
	 * @modified
	 */
	public void initUserInticketMap(){
		ExamUserInticketMap.initUserInticketMap();
	}
	
	/**
	 * 获取当天的用户信息与准考证密码之间的映射Map，可以用于用户名密码以及身份证密码登录用
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public List<ExamUserInticketForm> getExamUserInticketRelation(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		return exampaperDao.getExamUserInticketRelation();
	}
	
	/**
	 * 获取有效的员工对应用户信息
	 * @author 朱健
	 * @return
	 * @modified
	 */
	private static Map<String,SysUser> SYS_USERMAP = null;//因为用户数据不经常更新，所以静态化处理
	private static Date SYS_USERMAP_FLUSH_TIME = null;//最新刷新时间
	private static boolean SYS_USERMAP_FLUSH_FLAG = true;//是否正在刷新
	public Map<String,SysUser> getSysUserMap(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		if(SYS_USERMAP == null){
			SYS_USERMAP = exampaperDao.getSysUserMap();
			SYS_USERMAP_FLUSH_TIME = new Date();
		}else if((((new Date()).getTime() - SYS_USERMAP_FLUSH_TIME.getTime())/1000>60) && SYS_USERMAP_FLUSH_FLAG){//一分钟内刷新过，就不再到数据库中查询
			SYS_USERMAP_FLUSH_FLAG = false;
			SYS_USERMAP = exampaperDao.getSysUserMap();
			SYS_USERMAP_FLUSH_TIME = new Date();
			SYS_USERMAP_FLUSH_FLAG = true;
		}
		return SYS_USERMAP;
	}
	
	/**
	 * 保存考生信息与试卷到文件
	 * @author zhujian
	 * @description
	 * @modified
	 */
	private static SystemParams USER_FILE_PATH = null;
	private static SystemParams EXAM_FILE_PATH = null;
	private static SystemParams SAVE_ANS_PATH = null;
	private static SystemParams ANS_FILE_PATH = null;
	public void saveExamTestpaperInFile(){
		SystemParamsService systemParamsService = (SystemParamsService)SpringContextUtil.getBean("systemParamsService");
		
		USER_FILE_PATH = USER_FILE_PATH==null ? systemParamsService.findByCode("USER_FILE_PATH") : USER_FILE_PATH;//服务器存放的考生信息文件路径USER_FILE_PATH
		EXAM_FILE_PATH = EXAM_FILE_PATH==null ?systemParamsService.findByCode("EXAM_FILE_PATH"):EXAM_FILE_PATH;//服务器存放的试题文件路径 EXAM_FILE_PATH
		SAVE_ANS_PATH = SAVE_ANS_PATH==null ? systemParamsService.findByCode("SAVE_ANS_PATH"):SAVE_ANS_PATH;//考生答案文件路径 SAVE_ANS_PATH
		ANS_FILE_PATH = ANS_FILE_PATH==null ? systemParamsService.findByCode("ANS_FILE_PATH"):ANS_FILE_PATH;//服务器存放正确答案文件路径ANS_FILE_PATH
		//SystemParams SAVE_OVER_FLAG = systemParamsService.findByCode("SAVE_OVER_FLAG");//服务器存放考试结束标识的文件路径SAVE_OVER_FLAG
		
		HttpServletRequest request = ExamVariable.getHttpRequest();
		
		List<Exam> list = (List<Exam>)this.queryData("queryExamOnly2InUserFin", null, null);//.queryAllDate(Exam.class);
		Map<String,ExamUserTestpaper> userFinInticketMap = new HashMap<String,ExamUserTestpaper>();
		for(int i=0;i<list.size();i++){
			Exam exam = list.get(i);
			if(exam.getExamStartTime() == null || exam.getExamEndTime() == null){
				continue;
			}
			if(USER_FILE_PATH!=null && !"".equals(USER_FILE_PATH)){
				try {
					//科目信息到文件
					if(ExamUserJsonForm.isHaveFile(request, exam) == false){
						ExamUserJsonForm.getAndSaveExamUser(request, exam);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			List<ExamUserTestpaper> testpaperList = exam.getExamUserTestpapers();
			for(int j=0;j<testpaperList.size();j++){
				ExamUserTestpaper examUserTestpaper = testpaperList.get(j);
				//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				if("16".equals(examUserTestpaper.getState())
						|| "20".equals(examUserTestpaper.getState())
						|| "25".equals(examUserTestpaper.getState())
						|| "30".equals(examUserTestpaper.getState())){
					continue;
				}
				ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();
				if(examTestpaper!=null && ExamTestpaperJsonForm.isHaveFile(request, examUserTestpaper) == false){
					System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][生成考卷到文件] "+examTestpaper.getExamTestpaperId()+"   "
							+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
							+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
							+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
					//System.out.print("examUserTestpaper = "+examUserTestpaper.getUserTestpaperId());
					//System.out.print("examTestpaper = "+examTestpaper.getExamTestpaperId());
					Map<String,Object> term = new HashMap<String,Object>();
					term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
					List<ThemeType> themeTypeList = queryData("queryTypeInExampaperHql", term, null);
					
					if(examUserTestpaper!=null && examTestpaper!=null &&  examTestpaper.getExamTestpaperThemes()!=null && 
							examTestpaper.getExamTestpaperThemes().size()>0 && themeTypeList!=null && themeTypeList.size()>0){
						if(EXAM_FILE_PATH!=null && !"".equals(EXAM_FILE_PATH)){
							try {
								//考卷到文件
								ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examTestpaper, themeTypeList);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if(exam.getExamArrange().getIsPublic()!=null && exam.getExamArrange().getIsPublic().intValue() == 10
								&& ANS_FILE_PATH!=null && !"".equals(ANS_FILE_PATH)){
							try { 
								//正确答案到文件
								ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examTestpaper);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					userFinInticketMap.put(examUserTestpaper.getInticket(), examUserTestpaper);
				}
			}
		}
		if(SAVE_ANS_PATH!=null && !"".equals(SAVE_ANS_PATH)){
			try {
				//考生答案文件处理
				this.saveAnswerInUserAnswerFile(request,userFinInticketMap);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 更新标准编码
	 * @author 朱健
	 * @modified
	 */
	public void updateQuarterTrainInfo(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.updateQuarterTrainInfo();
	}

	/**
	 * 更新试题校验码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCrc(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.updateThemeCrc();
	}
	
	/**
	 * 更新试题编码
	 * @author 朱健
	 * @modified
	 */
	public void updateThemeCode(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.updateThemeCode();
	}
	/**
	 * 关闭到考试结束时间但未提交答案的考生的考试时间
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndsubInExamUserTestpaper(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.saveAndsubInExamUserTestpaper();
	}
	
	/**
	 * 更新试卷的分数、试题数量
	 * @author 朱健
	 * @modified
	 */
	public void updateExamTestPaperTotal(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.updateExamTestPaperTotal();
	}
	
	/**
	 * 清理考试安排中未选中考生的准考证
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveAndCleanPublicUserInticket(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.saveAndCleanPublicUserInticket();
	}
	
	/**
	 * 查询数据字典
	 * @author zhujian
	 * @description
	 * @param dtType
	 * @return
	 * @modified
	 */
	public List<Dictionary> getDictionaryTypeList(String dtType){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		return exampaperDao.getDictionaryTypeList(dtType);
		
	}

	
	/**
	 *
	 * @author zhujian
	 * @description 根据试卷模版中的试卷生成考生的试卷
	 * @param testpaper
	 * @return
	 * @modified
	 */
	public ExamTestpaper saveAndGetNewExamTestPaper(Exam exam,ExamUserTestpaper examUserTestpaper ,Testpaper testpaper) throws Exception{
		String userId = "";
		if(examUserTestpaper!=null){
			userId = examUserTestpaper.getExamPublicUser().getUserId();
		}
		//Map<String,String> term = new HashMap<String,String>();
		//term.put("userId", userId);
		//term.put("examId", exam.getExamId());
		//List<ExamTestpaper> list = queryData("getExamTestpaperByUserIdAndExamIdHql", term, new HashMap());//new ExamTestpaper();
		ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();//list!=null && list.size()>0 ?(ExamTestpaper)list.get(0):null;
		try{
			if(examTestpaper == null){
				examTestpaper = new ExamTestpaper();
				examTestpaper.setState("5");//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
				examTestpaper.setOrganName(exam.getOrganName());//机构名
				examTestpaper.setOrganId(exam.getOrganId());//机构编号
				examTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时
				examTestpaper.setUseNum(0);
			}else{
				examTestpaper.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时
			}
			if(exam!=null){
				examTestpaper.setExamArrangeId(exam.getExamArrange().getExamArrangeId());
				examTestpaper.setExamId(exam.getExamId());
				examTestpaper.setExamTestpaperName(exam.getExamName()+"考试试题");
			}
			
			examTestpaper.setTestpaperRank(testpaper.getTestpaperRank());//难度系数
			examTestpaper.setScreeningMethods(testpaper.getScreeningMethods());//筛选方式(5：分数，10：题数)
			examTestpaper.setTestpaperTime(testpaper.getTestpaperTime());//参考考时（分钟）
			examTestpaper.setIsUse(10);//是否使用(5：否,10：是)
			examTestpaper.setIsPrivate("5");//是否私有(5：否,10：是)
			examTestpaper.setUserId(userId);
			examTestpaper.setMarkScoreType(exam.getMarkScoreType());
			examTestpaper.setTestpaperId(testpaper.getTestpaperId());
			
			examTestpaper.setEmployeeId(examUserTestpaper.getEmployeeId());
			examTestpaper.setEmployeeName(examUserTestpaper.getEmployeeName());
			examTestpaper.getExamTestpaperThemes().clear();
			//List<TestpaperTheme> testpaperThemelist = testpaper.getTestpaperThemes();
			
			Map term2 = new HashMap();
			term2.put("testpaperId", testpaper.getTestpaperId());
			List<TestpaperTheme> testpaperThemelist = this.queryData("queryTestpaperThemeByTestpaperIdHql", term2, null);
			
			Map<String,List<TestpaperThemeAnswerkey>> ansMap = new HashMap<String,List<TestpaperThemeAnswerkey>>();
			List<TestpaperThemeAnswerkey> ansList = this.queryData("queryTestpaperThemeAnswerkeyInTestpaperHql", term2 , null);
			if(ansList!=null){
				for(int i=0;i<ansList.size();i++){
					TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)ansList.get(i);
					String ansKey = testpaperThemeAnswerkey.getTestpaperTheme().getTestpaperThemeId();
					List<TestpaperThemeAnswerkey> tmplist = ansMap.get(ansKey);
					if(tmplist==null)tmplist = new ArrayList<TestpaperThemeAnswerkey>();
					tmplist.add(testpaperThemeAnswerkey);
					ansMap.put(ansKey,tmplist);
				}
			}
			
			short totalTheme = 0;
			double totalScore = 0.0d;
			if(exam.getExamPaperType() == 10){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				int len = testpaperThemelist.size();
				//Random ran = new Random(len);
				Map<String,String> sortNumMap = new HashMap<String,String>();
				
				Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
				List<ThemeType> themeTypeList = this.getDao().findAll(ThemeType.class);
				for(int i=0;i<themeTypeList.size();i++){
					ThemeType themeType = themeTypeList.get(i);
					themeTypeMap.put(themeType.getThemeTypeId(), themeType);
				}
				
				for(int i=0;i<testpaperThemelist.size();i++){
					//产生随机数
					int rInt = (int)(Math.random()*len);
					while(sortNumMap.get(rInt+"")!=null){
						rInt = (int)(Math.random()*len);
					}
					sortNumMap.put(rInt+"",rInt+"");
					TestpaperTheme testpaperTheme = (TestpaperTheme)testpaperThemelist.get(i);
					ExamTestpaperTheme examTestpaperTheme = new ExamTestpaperTheme();
					ThemeType themeType = themeTypeMap.get(testpaperTheme.getThemeType().getThemeTypeId());
					if(themeType == null){
						themeType = testpaperTheme.getThemeType();
						themeTypeMap.put(testpaperTheme.getThemeType().getThemeTypeId(), themeType);
					}
					BeanUtils.copyProperties(testpaperTheme, examTestpaperTheme);
					examTestpaperTheme.setExamTestpaper(examTestpaper);
					examTestpaperTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					examTestpaperTheme.setRandomSortnum(rInt);
					examTestpaperTheme.setThemeId(testpaperTheme.getTheme()!=null ? testpaperTheme.getTheme().getThemeId() : null);
					examTestpaperTheme.setThemeTypeId(testpaperTheme.getThemeType()!=null?testpaperTheme.getThemeType().getThemeTypeId() : null);
					if(exam!=null){
						examTestpaperTheme.setExamArrangeId(exam.getExamArrange().getExamArrangeId());
						examTestpaperTheme.setExamId(exam.getExamId());
					}
					if(examUserTestpaper!=null){
						examTestpaperTheme.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
					}
					examTestpaperTheme.setDefaultScore(testpaperTheme.getDefaultScore());
					examTestpaperTheme.setScore(0.0d);
					examTestpaperTheme.setScoreType(testpaperTheme.getScoreType());//得分(算分)类型 0自动阅卷；1人工阅卷；2系统外阅卷
					examTestpaperTheme.setState(5);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
					totalTheme++;
					totalScore+=examTestpaperTheme.getDefaultScore();
					
					
					if("5".equals(themeType.getThemeType())
							|| "10".equals(themeType.getThemeType())
							|| "15".equals(themeType.getThemeType())){//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
						//List<TestpaperThemeAnswerkey> anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						List<TestpaperThemeAnswerkey> anslist = ansMap.get(testpaperTheme.getTestpaperThemeId());
						if(anslist==null){
							anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						}
						int len2 = anslist.size();
						//Random ran2 = new Random(len2);
						Map<String,String> sortNumMap2 = new HashMap<String,String>();
						for(int j=0;j<anslist.size();j++){
							//产生随机数
							int rInt2 = (int)(Math.random()*len2);
							while(sortNumMap2.get(rInt2+"")!=null){
								//rInt2 = ran2.nextInt(len2); 
								rInt2 = (int)(Math.random()*len2);
							}
							sortNumMap2.put(rInt2+"",rInt2+"");
							TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setRandomSortnum(rInt2);
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
						
					}else{
						//List<TestpaperThemeAnswerkey> anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						List<TestpaperThemeAnswerkey> anslist = ansMap.get(testpaperTheme.getTestpaperThemeId());
						if(anslist==null){
							anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						}
						for(int j=0;j<anslist.size();j++){
							TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setThemeId(testpaperThemeAnswerkey.getThemeId());
							examTestpaperAnswerkey.setThemeTypeId(testpaperTheme.getThemeType()!=null ? testpaperTheme.getThemeType().getThemeTypeId() : null);
							examTestpaperAnswerkey.setThemeTypeName(testpaperTheme.getThemeType()!=null ? testpaperTheme.getThemeType().getThemeTypeName() : null);
							examTestpaperAnswerkey.setOrganId(testpaperTheme.getOrganId());//机构ID
							examTestpaperAnswerkey.setOrganName(testpaperTheme.getOrganName());//机构名称
							examTestpaperAnswerkey.setRandomSortnum(examTestpaperAnswerkey.getSortNum());
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
					}
					examTestpaper.getExamTestpaperThemes().add(examTestpaperTheme);
				}
			}else if(exam.getExamPaperType() == 20){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				for(int i=0;i<testpaperThemelist.size();i++){
					TestpaperTheme testpaperTheme = (TestpaperTheme)testpaperThemelist.get(i);
					ExamTestpaperTheme examTestpaperTheme = new ExamTestpaperTheme();
					BeanUtils.copyProperties(testpaperTheme, examTestpaperTheme);
					examTestpaperTheme.setExamTestpaper(examTestpaper);
					examTestpaperTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					examTestpaperTheme.setRandomSortnum(testpaperTheme.getSortNum());
					examTestpaperTheme.setThemeId(testpaperTheme.getTheme()!=null ? testpaperTheme.getTheme().getThemeId() : null);
					examTestpaperTheme.setThemeTypeId(testpaperTheme.getThemeType()!=null?testpaperTheme.getThemeType().getThemeTypeId() : null);
					if(exam!=null){
						examTestpaperTheme.setExamArrangeId(exam.getExamArrange().getExamArrangeId());
						examTestpaperTheme.setExamId(exam.getExamId());
					}
					if(examUserTestpaper!=null){
						examTestpaperTheme.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
					}
					examTestpaperTheme.setDefaultScore(testpaperTheme.getDefaultScore());
					examTestpaperTheme.setScore(0.0d);
					examTestpaperTheme.setScoreType(testpaperTheme.getScoreType());//得分(算分)类型 0自动阅卷；1人工阅卷；2系统外阅卷
					examTestpaperTheme.setState(5);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
					totalTheme++;
					totalScore+=examTestpaperTheme.getDefaultScore();
					
					//short totalTheme = 0;
					//double totalScore = 0.0d;
					if("5".equals(testpaperTheme.getThemeType().getThemeType())
							|| "10".equals(testpaperTheme.getThemeType().getThemeType())
							|| "15".equals(testpaperTheme.getThemeType().getThemeType())){//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
						//List<TestpaperThemeAnswerkey> anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						List<TestpaperThemeAnswerkey> anslist = ansMap.get(testpaperTheme.getTestpaperThemeId());
						if(anslist==null){
							anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						}
						for(int j=0;j<anslist.size();j++){
							TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setRandomSortnum(testpaperThemeAnswerkey.getSortNum());
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
					}else{
						//List<TestpaperThemeAnswerkey> anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						List<TestpaperThemeAnswerkey> anslist = ansMap.get(testpaperTheme.getTestpaperThemeId());
						if(anslist==null){
							anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
						}
						for(int j=0;j<anslist.size();j++){
							TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setThemeId(testpaperThemeAnswerkey.getThemeId());
							examTestpaperAnswerkey.setThemeTypeId(testpaperTheme.getThemeType()!=null ? testpaperTheme.getThemeType().getThemeTypeId() : null);
							examTestpaperAnswerkey.setThemeTypeName(testpaperTheme.getThemeType()!=null ? testpaperTheme.getThemeType().getThemeTypeName() : null);
							examTestpaperAnswerkey.setOrganId(testpaperTheme.getOrganId());//机构ID
							examTestpaperAnswerkey.setOrganName(testpaperTheme.getOrganName());//机构名称
							examTestpaperAnswerkey.setRandomSortnum(examTestpaperAnswerkey.getSortNum());
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
					}
					//examTestpaper.setTotalTheme(totalTheme);//总题数
					//examTestpaper.setTotalScore(totalScore);//总分数
					examTestpaper.getExamTestpaperThemes().add(examTestpaperTheme);
					
				}
			}else if(exam.getExamPaperType() == 30){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
				////{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
				String initParam = "";
				String themeBankIds = null;
				for(int i=0;i<testpaper.getTestpaperThemetypes().size();i++){
					TestpaperThemetype testpaperThemetype = testpaper.getTestpaperThemetypes().get(i);
					String themeTypeId = testpaperThemetype.getThemeType().getThemeTypeId();
					String specialityid = testpaperThemetype.getProfessionId();
					String setThemetype = testpaper.getScreeningMethods().intValue()+"";
					String total = testpaperThemetype.getTotal().intValue()+"";
					String score = testpaperThemetype.getScore().doubleValue()+"";
					String rate = "null";
					initParam+="{"+themeTypeId+","+specialityid+","+setThemetype+","+total+","+score+","+rate+"},";	
				}
				Iterator<TestpaperSkey> its = testpaper.getTestpaperSkeies().iterator();
			    while(its.hasNext()){
			    	TestpaperSkey testpaperSkey= (TestpaperSkey)its.next();
				    if(testpaperSkey.getThemeBankId()!=null && !"".equals(testpaperSkey)){
				    	if(themeBankIds==null){ themeBankIds = ""; }
				    	themeBankIds+=testpaperSkey.getThemeBankId()+",";
			    	}
			    }
				String[] initParamArr = initParam.split("},");
				List<TestpaperThemeForm> themeList = testpaperService.getThemeInTemplate(initParamArr, themeBankIds,
						testpaper.getExamProperty(),null,null,null);
				
				
				int len = themeList.size();
				//Random ran = new Random(len);
				Map<String,String> sortNumMap = new HashMap<String,String>();
				for(int i=0;i<themeList.size();i++){
					//产生随机数
					int rInt = (int)(Math.random()*len);
					while(sortNumMap.get(rInt+"")!=null){
						rInt = (int)(Math.random()*len);
					}
					sortNumMap.put(rInt+"",rInt+"");
					TestpaperThemeForm testpaperThemeForm = (TestpaperThemeForm)themeList.get(i);
					Theme theme = testpaperThemeForm.getTheme();
					
					ExamTestpaperTheme examTestpaperTheme = new ExamTestpaperTheme();
					BeanUtils.copyProperties(theme, examTestpaperTheme);
					examTestpaperTheme.setExamTestpaper(examTestpaper);
					examTestpaperTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					examTestpaperTheme.setRandomSortnum(rInt);
					examTestpaperTheme.setThemeId(theme!=null ? theme.getThemeId() : null);
					examTestpaperTheme.setThemeTypeId(theme.getThemeType()!=null?theme.getThemeType().getThemeTypeId() : null);
					if(exam!=null){
						examTestpaperTheme.setExamArrangeId(exam.getExamArrange().getExamArrangeId());
						examTestpaperTheme.setExamId(exam.getExamId());
					}
					if(examUserTestpaper!=null && examUserTestpaper.getExamPublicUser()!=null){
						examTestpaperTheme.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
					}
					examTestpaperTheme.setDefaultScore(theme.getDefaultScore());
					examTestpaperTheme.setScore(0.0d);
					examTestpaperTheme.setScoreType(theme.getScoreType());//得分(算分)类型 0自动阅卷；1人工阅卷；2系统外阅卷
					examTestpaperTheme.setState(5);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
					totalTheme++;
					totalScore+=examTestpaperTheme.getDefaultScore();
					
					//short totalTheme = 0;
					//double totalScore = 0.0d;
					if("5".equals(theme.getThemeType().getThemeType())
							|| "10".equals(theme.getThemeType().getThemeType())
							|| "15".equals(theme.getThemeType().getThemeType())){//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
						List<ThemeAnswerkey> anslist = theme.getThemeAnswerkeies();
						int len2 = anslist.size();
						//Random ran2 = new Random(len2);
						Map<String,String> sortNumMap2 = new HashMap<String,String>();
						for(int j=0;j<anslist.size();j++){
							//产生随机数
							//int rInt2 = ran2.nextInt(len2); 
							int rInt2 = (int)(Math.random()*len2);
							while(sortNumMap2.get(rInt2+"")!=null){
								rInt2 = (int)(Math.random()*len2);
							}
							sortNumMap2.put(rInt2+"",rInt2+"");
							ThemeAnswerkey testpaperThemeAnswerkey = (ThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setRandomSortnum(rInt2);
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
					}else{
						List<ThemeAnswerkey> anslist = theme.getThemeAnswerkeies();
						for(int j=0;j<anslist.size();j++){
							ThemeAnswerkey testpaperThemeAnswerkey = (ThemeAnswerkey)anslist.get(j);
							ExamTestpaperAnswerkey examTestpaperAnswerkey= new ExamTestpaperAnswerkey();
							BeanUtils.copyProperties(testpaperThemeAnswerkey, examTestpaperAnswerkey);
							examTestpaperAnswerkey.setAnswerkeyId(null);
							examTestpaperAnswerkey.setUserId(examTestpaperTheme.getUserId());
							examTestpaperAnswerkey.setExamArrangeId(examTestpaperTheme.getExamArrangeId());
							examTestpaperAnswerkey.setExamId(examTestpaperTheme.getExamId());
							examTestpaperAnswerkey.setTestpaperId(examTestpaperTheme.getTestpaperId());
							examTestpaperAnswerkey.setThemeId(theme.getThemeId());
							examTestpaperAnswerkey.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
							examTestpaperAnswerkey.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);
							examTestpaperAnswerkey.setOrganId(theme.getOrganId());//机构ID
							examTestpaperAnswerkey.setOrganName(theme.getOrganName());//机构名称
							examTestpaperAnswerkey.setRandomSortnum(examTestpaperAnswerkey.getSortNum());
							examTestpaperAnswerkey.setExamTestpaperTheme(examTestpaperTheme);
							examTestpaperAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							examTestpaperTheme.getExamTestpaperAnswerkeies().add(examTestpaperAnswerkey);
						}
					}
					//examTestpaper.setTotalTheme(totalTheme);//总题数
					//examTestpaper.setTotalScore(totalScore);//总分数
					examTestpaper.getExamTestpaperThemes().add(examTestpaperTheme);
					
				}
			}
			examTestpaper.setTotalTheme(totalTheme);//总题数
			examTestpaper.setTotalScore(totalScore);//总分数
		}catch(Exception e){
			throw e;
		}
		return examTestpaper;
		
	}
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 自动对考生得分进行计算
	 * @param examId
	 * @throws Exception
	 * @modified
	 */
	public void saveAutoMarkExam(String examId) throws Exception{
		if (examId!=null && !"".equals(examId) && !"null".equals(examId)) {
			Exam exam = (Exam)findDataByKey(examId, Exam.class);
			List<ExamUserTestpaper> list = exam.getExamUserTestpapers();
			for(int i = 0 ; i<list.size() && i<50;i++){
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)list.get(i);
				saveAutoMarkPeople(examUserTestpaper.getExamTestpaper(),true); // 自动批阅整场考试所有人员的题目
				examUserTestpaper.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			saveOld(exam);
		}
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 自动对一个考生的得分进行计算
	 * @param examUserTestpaper
	 * @param isReMark 是否重算
	 * @throws Exception
	 * @modified
	 */
	public double saveAutoMarkPeople(ExamTestpaper examTestpaper,boolean isReMark)throws Exception{
		//ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();
		double autoScore = 0.0d;
		if (examTestpaper!=null) {
			Map term = new HashMap();
			term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
			List themeTypeList = queryData("queryTypeInExampaperHql", term, new HashMap());
			
			List<ExamTestpaperTheme> themeList = examTestpaper.getExamTestpaperThemes();
			autoScore = this.saveAutoMarkPeopleInThemeType(themeTypeList, themeList, isReMark);
		}
		return autoScore;
	}
	
	
	/**
	 * 获取考生的试题显示答案
	 * @author 朱健
	 * @param examTestpaperId
	 * @return
	 * @modified
	 */
	public Map<String,List<ExamTestpaperAnswerkey>> getExamTestpaperAnswerkeyByTestpaperId(String examTestpaperId){
		Map term = new HashMap();
		term.put("examTestpaperId", examTestpaperId);
		List themeAnsList = this.queryData("queryExamTestpaperAnswerkeyByExamTestpaperIdHql", term, new HashMap());
		
		Map<String,List<ExamTestpaperAnswerkey>> themeAnsMap = new HashMap<String,List<ExamTestpaperAnswerkey>>();
		for(int i=0;i<themeAnsList.size();i++){
			ExamTestpaperAnswerkey examTestpaperAnswerkey = (ExamTestpaperAnswerkey)themeAnsList.get(i);
			String examTestpaperThemeId = examTestpaperAnswerkey.getExamTestpaperTheme().getExamTestpaperThemeId();
			List tmpList = (List)themeAnsMap.get(examTestpaperThemeId);
			if(tmpList == null)tmpList = new ArrayList();
			tmpList.add(examTestpaperAnswerkey);
			themeAnsMap.put(examTestpaperThemeId, tmpList);
		}
		return themeAnsMap;
	}
	
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
	public double saveAutoMarkPeopleInThemeType(List themeTypeList,List<ExamTestpaperTheme> themeList,boolean isReMark)throws Exception{
			double autoScore = 0.0d;
			Map<String,List<ExamTestpaperAnswerkey>> themeAnsMap = null;
			for(int kk = 0;kk<themeTypeList.size();kk++){
			    ThemeType themeType = (ThemeType)themeTypeList.get(kk);
			    for(int i = 0;i<themeList.size();i++){
			    	ExamTestpaperTheme theme = (ExamTestpaperTheme)themeList.get(i);
			    	if(themeType.getThemeTypeId().equals(theme.getThemeTypeId())){
				    	if("0".equals(theme.getScoreType()) 
				    			&& ((theme.getState()!=null  && theme.getState().intValue() !=20 && theme.getState().intValue() !=30) || isReMark)){//得分(算分)类型 0自动阅卷；1人工阅卷；2系统外阅卷	
					    	List<ExamUserAnswerkey> userAnswerkeies = theme.getExamUserAnswerkeies();
					    	if(userAnswerkeies == null || userAnswerkeies.size()==0){
					    		theme.setScore(0.0d);
					    		autoScore += theme.getScore();
					    		if(theme.getState()!=null && theme.getState()!=30){
						    		theme.setState(20);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
						    	}
					    	}else{
					    		if(themeAnsMap==null){
					    			themeAnsMap = this.getExamTestpaperAnswerkeyByTestpaperId(theme.getExamTestpaper().getExamTestpaperId());
					    		}
					    		
					    		List<ExamTestpaperAnswerkey> testpaperAnswerkeies = themeAnsMap.get(theme.getExamTestpaperThemeId());
					    		if(testpaperAnswerkeies == null)testpaperAnswerkeies = theme.getExamTestpaperAnswerkeies();
						    	ExamTestpaperJsonForm.sortAnsList(testpaperAnswerkeies);//按随机数进行排序显示	
						    	
						    	String rightAns = "##";
						    	String userAns = "##";
						    	for(int t = 0;t<testpaperAnswerkeies.size();t++){
						    		ExamTestpaperAnswerkey ans = (ExamTestpaperAnswerkey)testpaperAnswerkeies.get(t);
						    	    if(ans.getIsRight()!=null && ans.getIsRight().intValue() == 10){//是否正确 5：否,10：是
						    	    	if("5".equals(themeType.getThemeType()) || "10".equals(themeType.getThemeType()) || "25".equals(themeType.getThemeType())){
						    	    		rightAns += StaticVariable.themeAnsSort[t]+"##";
						    	    	}else{
						    	    		rightAns += ans.getAnswerkeyValue()+"##";
						    	    	}
						    	    }
						    	}
						    	for(int t = 0;t<userAnswerkeies.size();t++){
						    		ExamUserAnswerkey ans = (ExamUserAnswerkey)userAnswerkeies.get(t);
						    		userAns += ans.getAnswerkeyValue()+"##";
						    	}
						    	if(rightAns.equals(userAns)){
						    		theme.setScore(theme.getDefaultScore());
						    	}else{
						    		theme.setScore(0.0d);
						    	}
						    	autoScore += theme.getScore();
						    	if(theme.getState()!=null && theme.getState()!=30){
						    		theme.setState(20);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
						    	}
					    	}
				    	}
			    	}
			    }
			}
			return autoScore;
	}
	
	/**
	 * 对答案进行处理，把答案文件读取并保存到数据库
	 * @author zhujian
	 * @description
	 * @param request
	 * @modified
	 */
	public void saveAnswerInUserAnswerFileInRealPath()throws Exception{
		//FileOption.getAllFileInPathAndPackage(realPath,"online"+System.getProperty("file.separator")+"ans_bak");
		List<File> list = FileOption.getAllFileInPath(ExamVariable.getSaveAnsPath(null));
		Map subTimeInFileMap = new HashMap();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				File file =(File)list.get(i);
				saveAnswerFileInDb(file,null,subTimeInFileMap,null);
			}
		}
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取有考试结束时间还未阅卷的考生，对其自动计算分数进行计分
	 * @throws Exception
	 * @modified
	 */
	public void saveNotSubmitAnsExamUserMark()throws Exception{
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		List<ExamUserTestpaper> userList =  exampaperDao.getNotSubmitAnsExamUserTestpaperList();
		if(userList!=null && userList.size()>0){
			//i<300用于定时器一次性最多处理300套试题，防止过多内存溢出，数据因为处于同一事务不能提交
			for(int i=0;i<userList.size() && i<300;i++){
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)userList.get(i);
				saveAutoMarkPeople(examUserTestpaper.getExamTestpaper(),true);
				examUserTestpaper.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
		}
	}

	/**
	 * 对答案进行处理，把答案文件读取并保存到数据库
	 * @author zhujian
	 * @description
	 * @param request
	 * @modified
	 */
	public void saveAnswerInUserAnswerFile(HttpServletRequest request,Map<String,ExamUserTestpaper> userFinInticketMap)throws Exception{
		//FileOption.getAllFileInPackage(request,"online"+System.getProperty("file.separator")+"ans_bak");
		System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]正在对答案进行处理，把答案文件读取并保存到数据库   开始："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		//System.out.println(ExamVariable.getSaveAnsPath(request));
		List<File> list = FileOption.getAllFileInPath(ExamVariable.getSaveAnsPath(request));
		Map subTimeInFileMap = new HashMap();
		if(userFinInticketMap == null){
			userFinInticketMap = new HashMap<String,ExamUserTestpaper>();
			List<ExamUserTestpaper> examUserTestpaperlist = queryData("queryExamUserTestpaperOnly2InExamHql", null, new HashMap());
			 if(examUserTestpaperlist!=null && examUserTestpaperlist.size()>0){
				 for(int i=0;i<examUserTestpaperlist.size();i++){
					 ExamUserTestpaper examUserTestpaper = examUserTestpaperlist.get(i);
					 userFinInticketMap.put(examUserTestpaper.getInticket(), examUserTestpaper);
				 }
			 }
		}
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				File file =(File)list.get(i);
				System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]后台处理答案与提交时间： "+file.getName()+"   开始："
						+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
						+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
						+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
				saveAnswerFileInDb(file,request,subTimeInFileMap,userFinInticketMap);
				System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]后台处理答案与提交时间： "+file.getName()+"   结束："
						+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
						+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
						+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			}
		}
		System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]正在对答案进行处理，把答案文件读取并保存到数据库   结束："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
	}
	
	private void saveAnswerFileInDb(File file,HttpServletRequest request,
			Map subTimeInFileMap,Map<String,ExamUserTestpaper> userFinInticketMap) throws Exception{
		try{
			 String inticket = file.getName().replace(".txt", "");
			 ExamUserTestpaper examUserTestpaper = userFinInticketMap.get(inticket);
			 if(examUserTestpaper!=null){
				 this.saveAnswerFileByExamUserTestpaper(examUserTestpaper, file, 
						 request, subTimeInFileMap);
			 }
			 /*if(userFinInticketMap == null){//针对前期没有对ExamUserTestpaper操作，无法用准考证与ExamUserTestpaper对应
				 Map term = new HashMap();
				 term.put("inticket", inticket);
				 List<ExamUserTestpaper> list = queryData("queryExamUserTestpaperByInticketHql", term, new HashMap());
				 if(list!=null && list.size()>0){
					 this.saveAnswerFileByExamUserTestpaper((ExamUserTestpaper)list.get(0), file, 
							 request, subTimeInFileMap);
				 }
			 }else{//针对前期由准考证与ExamUserTestpaper对应的方式
				 
			 }*/
		 }catch(Exception e){
			 System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]"+file.getPath()+"执行错误！");
			 e.printStackTrace();
			 throw e;
		 }
	}
	
	
	private void saveAnswerFileByExamUserTestpaper(ExamUserTestpaper examUserTestpaper,File file,HttpServletRequest request,
			Map subTimeInFileMap) throws Exception{
		 if(!("16".equals(examUserTestpaper.getState())
					|| "20".equals(examUserTestpaper.getState())
					|| "25".equals(examUserTestpaper.getState())
					|| "30".equals(examUserTestpaper.getState()))){
			 
			 String examId = examUserTestpaper.getExam().getExamId();
			 //对考生没有答案提交时间的进行相关处理
			 if(examUserTestpaper.getSubTime()==null || "".equals(examUserTestpaper.getSubTime())){
				 String subTime = (String)subTimeInFileMap.get(examUserTestpaper.getInticket()) ; 
				 if(subTime==null && subTimeInFileMap.get(examId)==null){
					Map tmpMap =  getSubTimeInFileMap(request, examId);//Map里面包括了examId，标识已经对文件读取过，防止每次重复读取
					subTimeInFileMap.putAll(tmpMap);
					subTime = (String)subTimeInFileMap.get(examUserTestpaper.getInticket()) ; 
				 }
				 if(subTime!=null){
					 examUserTestpaper.setSubTime(subTime);
				 }
			 }
			 
			 if(examUserTestpaper.getSubTime()!=null){
				 String ansStr = FileOption.readFile(file.getPath());
				 String[] ansList = ansStr.split("#\\@#");
				 Map<String,String> ansMap = new HashMap<String,String>();
				 if(ansList!=null && ansList.length>0){
					 for(int i=0;i<ansList.length;i++){
						 if(ansList[i]!=null && !"".equals(ansList[i])&& !"null".equals(ansList[i])){
							ansList[i] = ansList[i].replaceAll("#\\*#@\\$@", "#\\*#null@\\$@");
							ansList[i] = ansList[i].replaceAll("#\\$#@\\$@", "#\\$#null@\\$@");
							
							if(ansList[i]!=null && !"".equals(ansList[i]) && !"null".equals(ansList[i])){
								String[] ansArr = ansList[i].split("#\\$#");
								if(ansArr!=null && ansArr.length>2){
									String[] sorts = ansArr[1].split("@\\$@");
									String[] ans = ansArr[2].split("@\\$@");
									for(int k = 0;k<sorts.length ;k++){
										ansMap.put(sorts[k],ans[k]);
									}
								}
							}
						 }
					 }
				 }
				 
				 if(saveUserExamineeAnswer(examUserTestpaper, ansMap)){//保存到数据库中
					 String targetFileBakSrc = file.getPath().replace("ans_bak", "ans_finbak");
					 File targetFileBak = new File(targetFileBakSrc.replaceAll(examUserTestpaper.getInticket()+".txt", ""));
					 if(!targetFileBak.isDirectory()){
						 targetFileBak.mkdirs();
					 }
					 FileOption.copyFile(file.getPath(), targetFileBakSrc);
					 file.delete();
					 examUserTestpaper.getExamTestpaper().setExamFileUserAnswerkey(ansStr);
					 examUserTestpaper.setState("15");
					 this.saveAutoMarkPeople(examUserTestpaper.getExamTestpaper(),true);
					 update(examUserTestpaper);
					 try{
						File fileDir =  new File(file.getPath().replace(file.getName(), ""));
						if(fileDir.isDirectory()){
							 File[] files = fileDir.listFiles(); 
							 if(files==null || files.length==0){
								 fileDir.delete();
							 }
						}
					 }catch(Exception e){
						 e.printStackTrace();
						 System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][考卷答案处理]"+file.getPath()+"删除文件夹错误！");
					 }
				 
				 }
			 }
		 }
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 获取考生答案提交时间
	 * @param request
	 * @param examId
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private Map<String,String> getSubTimeInFileMap(HttpServletRequest request,String examId)throws Exception{
		Map<String,String> value = new HashMap<String,String>();
		String path = ExamVariable.getExamSaveOverFlagPath(request);
		File file = new File(path+System.getProperty("file.separator")+examId+".ini");
		if(file.exists() && file.isFile()){
			FileInputStream fileReader = new FileInputStream(file);
			InputStreamReader fsr = new InputStreamReader(fileReader,"GBK");
			BufferedReader bfreader = new BufferedReader(fsr);
			String line="";
			boolean isFlag = false;
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][获取考生答案提交时间]读取时间 开始："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
			while ((line = bfreader.readLine()) != null){
				if(line.indexOf("[USER]")!=-1){
					isFlag = true;	
					continue;
				}
				if(!(line.indexOf("[USER]")!=-1 || line.indexOf("=")!=-1 || "".equals(line.trim()))){
					isFlag = false;
					continue;
					//break;
				}
				if(isFlag && line.indexOf("=")!=-1){
					//System.out.println(line);
					String[] arr = line.split("=");
					value.put(arr[0], arr[1]);
				}
				
			}
			bfreader.close();
			fsr.close();
			bfreader = null;
			fsr = null;
			System.out.println("["+DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss")+"][获取考生答案提交时间]读取时间  结束："
				+("最大内存" + NumericUtils.round(Runtime.getRuntime().maxMemory()/1024.0/1024.0,2)+"M  ")
				+("可用内存："+NumericUtils.round(Runtime.getRuntime().freeMemory()/1024.0/1024.0,2)+"M  ") //当前JVM空闲内存
				+("已用内存："+NumericUtils.round(Runtime.getRuntime().totalMemory()/1024.0/1024.0,2)+"M  ")); //当前JVM占用的内存总数，其值相当于当前JVM已使用的内存及freeMemory()的总和
		}
		value.put(examId, examId);
		return value;
	}
	
	/**
	 * 对答案进行处理，把答案字符串保存到数据库
	 * @author zhujian
	 * @description
	 * @param examTestpaperId
	 * @param cookieStr
	 * @modified
	 */
	public boolean saveUserExamineeAnswer(ExamUserTestpaper examUserTestpaper,Map<String,String> userAnsMap)throws Exception{
		 boolean isSave =false;	
		 try{
				ExamTestpaper examTestpaper = examUserTestpaper.getExamTestpaper();
				if (examTestpaper!=null) {
					Map term = new HashMap();
					term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
					List themeTypeList = queryData("queryTypeInExampaperHql", term, new HashMap());

					 List<ExamTestpaperTheme> themeList = examTestpaper.getExamTestpaperThemes();
					 ExamTestpaperJsonForm.sortThemelist(themeList);
					 
					 for(int kk = 0;kk<themeTypeList.size();kk++){
						 ThemeType themeType = (ThemeType)themeTypeList.get(kk);
						 for(int i = 0;i<themeList.size();i++){
							    ExamTestpaperTheme theme = (ExamTestpaperTheme)themeList.get(i);
							 	if(themeType.getThemeTypeId().equals(theme.getThemeTypeId())){
							    	theme.getExamUserAnswerkeies().clear(); 
							    	theme.setState(15);
							    	String sortIndex = theme.getRandomSortnum().intValue()+"";
							    	String ansStr = (String)userAnsMap.get(sortIndex);
							    	if(ansStr!=null && !"".equals(ansStr)){
							    		String[] anslist = ansStr.split("#\\*#");
							    		String type = themeType!=null ? themeType.getThemeType() : null;
							    		for(int k = 0;k<anslist.length;k++){
							    			ExamUserAnswerkey examUserAnswerkey = new ExamUserAnswerkey();
							    			//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他	
							    			//System.out.println(type);
							    			if("5".equals(type) || "10".equals(type) || "25".equals(type)){
							    				examUserAnswerkey.setAnswerkeyValue(anslist[k]);
							    			}else{
							    				//System.out.println(CharsetSwitchUtil.decode(anslist[k]));
							    				examUserAnswerkey.setAnswerkeyValue(CharsetSwitchUtil.decode(anslist[k]));
							    			}
							    			
							    			examUserAnswerkey.setSortNum(k);
							    			examUserAnswerkey.setState(15);
							    			examUserAnswerkey.setExamTestpaperTheme(theme);
							    			examUserAnswerkey.setCreatedBy(examTestpaper.getEmployeeName());
							    			examUserAnswerkey.setCreatedIdBy(examTestpaper.getEmployeeId());
							    			examUserAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							    			examUserAnswerkey.setOrganId(examTestpaper.getOrganId());
							    			examUserAnswerkey.setOrganName(examTestpaper.getOrganName());
							    			theme.getExamUserAnswerkeies().add(examUserAnswerkey);
							    		}
							    	}
							    	isSave = true;
							 	}
							
						}
					}
					 
					 
				}
		}catch(Exception e){
			isSave =false;	
			e.printStackTrace();
			throw e;
		}
		return isSave;
		//}
		
	}
	
	
	/**
	 * 删除空的考试数据
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullExam(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.deleteNullExam();
	}
	
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
			UserSession usersess,HttpServletRequest request,boolean creatExamPaper){
		List<ExamUserTestpaper> addExamUserTestpaperList = saveExamUserFormList(exam, examUserFormList, usersess,request,true);
		deleteNullTestpaperInExamUserTestpaper(exam.getExamId());
		return addExamUserTestpaperList;
		
	}
	/**
	 *
	 * @author zhujian
	 * @description 保存选择的考生信息
	 * @param exam
	 * @param examUserFormList
	 * @param usersess
	 * @modified
	 */
	public List<ExamUserTestpaper> saveExamUserFormList(Exam exam,List examUserFormList,UserSession usersess,HttpServletRequest request,boolean creatExamPaper){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		List<ExamUserTestpaper> list = exam.getExamUserTestpapers();//getService().queryData("examUserTestpaperInExamHql", term, null);
		Map<String,ExamUserTestpaper> examUserTestpaperMap = new HashMap<String,ExamUserTestpaper>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ExamUserTestpaper examUserTestpaper =(ExamUserTestpaper)list.get(i);
				examUserTestpaperMap.put(examUserTestpaper.getExamPublicUser().getUserId(), examUserTestpaper);
			}
		}
		Inticket inticket = new Inticket();
		String prefix = inticket.getPrefix(exam.getExamArrange().getExamArrangeId());
		int maxPostfixNum = inticket.getPostfixMaxNum(exam.getExamArrange().getExamArrangeId(),prefix);
		maxPostfixNum++;
		
		Map<String,SysUser> sysUserMap = this.getSysUserMap();
		List<ExamUserTestpaper> addExamUserTestpaperList = new ArrayList<ExamUserTestpaper>();
		for(int i=0;i<examUserFormList.size();i++){
			ExamUserForm examUserForm = (ExamUserForm)examUserFormList.get(i);
			if(examUserTestpaperMap.get(examUserForm.getUserId())==null){
				ExamPublicUser examPublicUser = (ExamPublicUser)getDao().findEntityBykey(ExamPublicUser.class, examUserForm.getUserId());
				ExamUserTestpaper examUserTestpaper = new ExamUserTestpaper();
				examUserTestpaper.setExamPublicUser(examPublicUser);
				examUserTestpaper.setExam(exam);
				examUserTestpaper.setUserOrganId(examPublicUser.getUserOrganId());//所属机构ID
				examUserTestpaper.setUserOrganName(examPublicUser.getUserOrganName());//所属机构
				examUserTestpaper.setUserDeptId(examPublicUser.getUserDeptId());//所属部门ID
				examUserTestpaper.setUserDeptName(examPublicUser.getUserDeptName());//所属部门
				examUserTestpaper.setUserGroupId(examPublicUser.getUserGroupId());//所属班组ID
				examUserTestpaper.setUserGroupName(examPublicUser.getUserGroupName());//所属班组
				examUserTestpaper.setPostId(examPublicUser.getPostId());//所属岗位ID
				examUserTestpaper.setPostName(examPublicUser.getPostName());//所属岗位
				examUserTestpaper.setUserName(examPublicUser.getUserName());//姓名
				examUserTestpaper.setUserSex(examPublicUser.getUserSex());//性别  1-男  2-女
				
				String inticketStr = null;
				String examPassword = null;
				//if(examPublicUser.getInticket()==null 
						//|| "".equals(examPublicUser.getInticket()) 
						//|| "null".equals(examPublicUser.getInticket())){
				    
					inticketStr = Inticket.getInticket(prefix,exam.getExamStartTime(),maxPostfixNum);
					examPassword = ExamPassword.getExamPassword(4);
					maxPostfixNum++;
					
					SysUser sysUser = sysUserMap.get(examPublicUser.getEmployeeId());
				    if(sysUser!=null){
				    	examUserTestpaper.setLoginId(sysUser.getAccount()!=null?sysUser.getAccount().trim():sysUser.getAccount());
						examUserTestpaper.setLoginPassword(sysUser.getPassword());
				    }
				    
					
					/*if(examPublicUser!=null){
						examPublicUser.setInticket(inticketStr);
						examPublicUser.setExamPassword(examPassword);
					}
					getDao().saveEntityOld(examPublicUser);*/
				//}else{
					//inticketStr = examPublicUser.getInticket();
					//examPassword = examPublicUser.getExamPassword();
				//}
				examUserTestpaper.setInticket(inticketStr);//准考证号
				examUserTestpaper.setExamPassword(examPassword);//考试密码
				
				examUserTestpaper.setIdNumber(examPublicUser.getIdNumber()!=null?examPublicUser.getIdNumber().trim():examPublicUser.getIdNumber());//身份证号
				examUserTestpaper.setUserBirthday(examPublicUser.getUserBirthday());//出生年月
				examUserTestpaper.setUserNation(examPublicUser.getUserNation());//民族
				examUserTestpaper.setUserAddr(examPublicUser.getUserAddr());//住址
				examUserTestpaper.setUserPhone(examPublicUser.getUserPhone());//联系电话
				examUserTestpaper.setUserInfo(examPublicUser.getUserInfo());//考生信息
				
				/*examUserTestpaper.setExamPaperType(examPublicUser.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				examUserTestpaper.setInTime(examPublicUser.getInTime());//入场时间
				examUserTestpaper.setOutTime(examPublicUser.getOutTime());//离场时间
				examUserTestpaper.setIsLocked(examPublicUser);//是否锁定 0-未锁定  5-锁定
				examUserTestpaper.setFristScote(examPublicUser);//调整前得分
				examUserTestpaper.setScote(examPublicUser);//调整得分
				examUserTestpaper.setAdjustRemark(examPublicUser);//调整说明
				examUserTestpaper.setAdjustId(examPublicUser);//调整人ID
				examUserTestpaper.setAdjustUser(examPublicUser);///调整人
				examUserTestpaper.setSubTime(examPublicUser);///交卷时间
				examUserTestpaper.setLoginUrl(examPublicUser);///登陆地址
				examUserTestpaper.setExamRootName(examPublicUser);//考点名称
				examUserTestpaper.setExamRootPlace(examPublicUser);//考点地点
				examUserTestpaper.setSeatNum(examPublicUser);//座位号
*/						/*examUserTestpaper.setIsIdNumberLogin(examPublicUser);//是否允许身份证登陆 0-允许  1-不允许
				examUserTestpaper.setExamPaperInit(examPublicUser);//试卷是否初始化 10-否 11-初始化失败 20-是  21-需要进行重置
				examUserTestpaper.setState(examPublicUser);//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				examUserTestpaper.setRelationId(examPublicUser);//关联ID（如：练习安排等）
				examUserTestpaper.setRelationType(examPublicUser);//关联类型
*/						examUserTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				examUserTestpaper.setCreatedBy(usersess.getEmployeeName());//创建人
				examUserTestpaper.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				examUserTestpaper.setOrganId(usersess.getOrganId());//机构ID
				examUserTestpaper.setOrganName(usersess.getOrganAlias());//机构名称
				examUserTestpaper.setEmployeeId(examPublicUser.getEmployeeId());//人员ID
				examUserTestpaper.setEmployeeName(examPublicUser.getEmployeeName());//人员
				exam.getExamUserTestpapers().add(examUserTestpaper);
				
				addExamUserTestpaperList.add(examUserTestpaper);
			}else{
				examUserTestpaperMap.remove(examUserForm.getUserId());
			}
		}
		
		if(list!=null && list.size()>0){
			Iterator<ExamUserTestpaper> its = list.iterator();
			while(its.hasNext()){
				ExamUserTestpaper examUserTestpaper =(ExamUserTestpaper)its.next();
				if(examUserTestpaperMap.get(examUserTestpaper.getExamPublicUser().getUserId())!=null){
					examUserTestpaper.setExam(null);
					
					if(creatExamPaper){
						String examId = exam.getExamId();
						String examFileName = ExamVariable.getExamFilePath(request)+"title_"+examId+"_"+examUserTestpaper.getInticket()+".txt";
								/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"title",
								"title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/
						String examAnsFileName = ExamVariable.getExamUserFilePath(request)+examId+System.getProperty("file.separator")
								+"ans_"+examId+"_"+examUserTestpaper.getInticket()+".txt";
								/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"ans"+System.getProperty("file.separator")
								+examUserTestpaper.getExam().getExamId(),
								"ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/	
						File file = new File(examFileName);
						if(file.exists()){
							file.delete();
						}
						
						File ansFile = new File(examAnsFileName);
						if(ansFile.exists()){
							ansFile.delete();
						}
						
						
					}
				}
			}
		}
		
		/*if(!examUserTestpaperMap.keySet().isEmpty()){
			Iterator<String> its = examUserTestpaperMap.keySet().iterator();
			while(its.hasNext()){
				String key = (String)its.next();
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperMap.get(key);
				exam.getExamUserTestpapers().remove(examUserTestpaper);
			}
		}*/
		getDao().saveEntityOld(exam);
		
		exampaperDao.deleteNullexamUserTestpaper();
		
		//清理考试安排中未选中考生的准考证
		exampaperDao.saveAndCleanPublicUserInticket();
		
		//ExamUserInticketMap.initUserInticketMap();
		
		return addExamUserTestpaperList;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 保存选择的考生信息
	 * @param exam
	 * @param examUserFormList
	 * @param usersess
	 * @modified
	 */
	public void saveExamUserInExamPublicUsers(Exam exam,List<ExamPublicUser> examPublicUsers,UserSession usersess,HttpServletRequest request,boolean creatExamPaper){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		List<ExamUserTestpaper> list = exam.getExamUserTestpapers();//getService().queryData("examUserTestpaperInExamHql", term, null);
		Map<String,ExamUserTestpaper> examUserTestpaperMap = new HashMap<String,ExamUserTestpaper>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				ExamUserTestpaper examUserTestpaper =(ExamUserTestpaper)list.get(i);
				examUserTestpaperMap.put(examUserTestpaper.getExamPublicUser().getUserId(), examUserTestpaper);
			}
		}
		Inticket inticket = new Inticket();
		String prefix = inticket.getPrefix(exam.getExamArrange().getExamArrangeId());
		int maxPostfixNum = inticket.getPostfixMaxNum(exam.getExamArrange().getExamArrangeId(),prefix);
		maxPostfixNum++;
		
		Map<String,SysUser> sysUserMap = this.getSysUserMap();
		
		for(int i=0;i<examPublicUsers.size();i++){
			ExamPublicUser examPublicUser = (ExamPublicUser)examPublicUsers.get(i);
			if("1".equals(examPublicUser.getIsDel())){//已删除的不要进行处理
				continue;
			}
			ExamUserTestpaper examUserTestpaper = examUserTestpaperMap.get(examPublicUser.getUserId());
			boolean isUpdate =  true;
			if(examUserTestpaper==null){
				examUserTestpaper = new ExamUserTestpaper();
				examUserTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				examUserTestpaper.setCreatedBy(usersess.getEmployeeName());//创建人
				examUserTestpaper.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				examUserTestpaper.setOrganId(usersess.getOrganId());//机构ID
				examUserTestpaper.setOrganName(usersess.getOrganAlias());//机构名称
				isUpdate = false;
			}else{
				examUserTestpaper.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				examUserTestpaper.setLastUpdatedBy(usersess.getEmployeeName());//创建人
				examUserTestpaper.setLastUpdatedIdBy(usersess.getEmployeeId());//创建人ID
				examUserTestpaper.setOrganId(usersess.getOrganId());//机构ID
				examUserTestpaper.setOrganName(usersess.getOrganAlias());//机构名称
			}
				examUserTestpaper.setExamPublicUser(examPublicUser);
				examUserTestpaper.setExam(exam);
				examUserTestpaper.setUserOrganId(examPublicUser.getUserOrganId());//所属机构ID
				examUserTestpaper.setUserOrganName(examPublicUser.getUserOrganName());//所属机构
				examUserTestpaper.setUserDeptId(examPublicUser.getUserDeptId());//所属部门ID
				examUserTestpaper.setUserDeptName(examPublicUser.getUserDeptName());//所属部门
				examUserTestpaper.setUserGroupId(examPublicUser.getUserGroupId());//所属班组ID
				examUserTestpaper.setUserGroupName(examPublicUser.getUserGroupName());//所属班组
				examUserTestpaper.setPostId(examPublicUser.getPostId());//所属岗位ID
				examUserTestpaper.setPostName(examPublicUser.getPostName());//所属岗位
				examUserTestpaper.setUserName(examPublicUser.getUserName());//姓名
				examUserTestpaper.setUserSex(examPublicUser.getUserSex());//性别  1-男  2-女
				
				String inticketStr = null;
				String examPassword = null;
				//if(examPublicUser.getInticket()==null 
						//|| "".equals(examPublicUser.getInticket()) 
						//|| "null".equals(examPublicUser.getInticket())){
				if(examUserTestpaper.getInticket()==null || "".equals(examUserTestpaper.getInticket())
						|| "null".equals(examUserTestpaper.getInticket())){   
					inticketStr = Inticket.getInticket(prefix,exam.getExamStartTime(),maxPostfixNum);
					examPassword = ExamPassword.getExamPassword(4);
					maxPostfixNum++;
					examUserTestpaper.setInticket(inticketStr);//准考证号
					examUserTestpaper.setExamPassword(examPassword);//考试密码
				}
				
				SysUser sysUser = sysUserMap.get(examPublicUser.getEmployeeId());
				if(sysUser!=null){
				   examUserTestpaper.setLoginId(sysUser.getAccount()!=null?sysUser.getAccount().trim():sysUser.getAccount());
				   examUserTestpaper.setLoginPassword(sysUser.getPassword());
				}
				
				
				examUserTestpaper.setIdNumber(examPublicUser.getIdNumber()!=null?examPublicUser.getIdNumber().trim():examPublicUser.getIdNumber());//身份证号
				examUserTestpaper.setUserBirthday(examPublicUser.getUserBirthday());//出生年月
				examUserTestpaper.setUserNation(examPublicUser.getUserNation());//民族
				examUserTestpaper.setUserAddr(examPublicUser.getUserAddr());//住址
				examUserTestpaper.setUserPhone(examPublicUser.getUserPhone());//联系电话
				examUserTestpaper.setUserInfo(examPublicUser.getUserInfo());//考生信息
				
				/*examUserTestpaper.setExamPaperType(examPublicUser.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				examUserTestpaper.setInTime(examPublicUser.getInTime());//入场时间
				examUserTestpaper.setOutTime(examPublicUser.getOutTime());//离场时间
				examUserTestpaper.setIsLocked(examPublicUser);//是否锁定 0-未锁定  5-锁定
				examUserTestpaper.setFristScote(examPublicUser);//调整前得分
				examUserTestpaper.setScote(examPublicUser);//调整得分
				examUserTestpaper.setAdjustRemark(examPublicUser);//调整说明
				examUserTestpaper.setAdjustId(examPublicUser);//调整人ID
				examUserTestpaper.setAdjustUser(examPublicUser);///调整人
				examUserTestpaper.setSubTime(examPublicUser);///交卷时间
				examUserTestpaper.setLoginUrl(examPublicUser);///登陆地址
				examUserTestpaper.setExamRootName(examPublicUser);//考点名称
				examUserTestpaper.setExamRootPlace(examPublicUser);//考点地点
				examUserTestpaper.setSeatNum(examPublicUser);//座位号
*/						/*examUserTestpaper.setIsIdNumberLogin(examPublicUser);//是否允许身份证登陆 0-允许  1-不允许
				examUserTestpaper.setExamPaperInit(examPublicUser);//试卷是否初始化 10-否 11-初始化失败 20-是  21-需要进行重置
				examUserTestpaper.setState(examPublicUser);//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				examUserTestpaper.setRelationId(examPublicUser);//关联ID（如：练习安排等）
				examUserTestpaper.setRelationType(examPublicUser);//关联类型
*/						
				examUserTestpaper.setEmployeeId(examPublicUser.getEmployeeId());//人员ID
				examUserTestpaper.setEmployeeName(examPublicUser.getEmployeeName());//人员
				exam.getExamUserTestpapers().add(examUserTestpaper);
				
				/*if(creatExamPaper){
					
				}*/
				if(isUpdate){
					examUserTestpaperMap.remove(examPublicUser.getUserId());
				}
		}
		
		if(list!=null && list.size()>0){
			Iterator<ExamUserTestpaper> its = list.iterator();
			while(its.hasNext()){
				ExamUserTestpaper examUserTestpaper =(ExamUserTestpaper)its.next();
				if(examUserTestpaperMap.get(examUserTestpaper.getExamPublicUser().getUserId())!=null){
					examUserTestpaper.setExam(null);
					
					if(creatExamPaper){
						String examId = exam.getExamId();
						String examFileName = ExamVariable.getExamFilePath(request)+"title_"+examId+"_"+examUserTestpaper.getInticket()+".txt";
								/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"title",
								"title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/
						String examAnsFileName = ExamVariable.getExamUserFilePath(request)+examId+System.getProperty("file.separator")
								+"ans_"+examId+"_"+examUserTestpaper.getInticket()+".txt";
								/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"ans"+System.getProperty("file.separator")
								+examUserTestpaper.getExam().getExamId(),
								"ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/	
						File file = new File(examFileName);
						if(file.exists()){
							file.delete();
						}
						
						File ansFile = new File(examAnsFileName);
						if(ansFile.exists()){
							ansFile.delete();
						}
						
						
					}
				}
			}
		}
		
		/*if(!examUserTestpaperMap.keySet().isEmpty()){
			Iterator<String> its = examUserTestpaperMap.keySet().iterator();
			while(its.hasNext()){
				String key = (String)its.next();
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperMap.get(key);
				exam.getExamUserTestpapers().remove(examUserTestpaper);
			}
		}*/
		getDao().saveEntityOld(exam);

		exampaperDao.deleteNullexamUserTestpaper();
		

		//清理考试安排中未选中考生的准考证
		exampaperDao.saveAndCleanPublicUserInticket();
		
		//ExamUserInticketMap.initUserInticketMap();
	}
	/**
	 * 删除没有分配考生科目的考生
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullexamUserTestpaper(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.deleteNullexamUserTestpaper();
	}
	
	/**
	 * 删除没有分配考生科目的考生试卷
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaperInExamUserTestpaper(String examId){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.deleteNullTestpaperInExamUserTestpaper(examId);
	}
	
	
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
			ExamTestpaper examTestpaper,double score)throws Exception{
		Employee employee = (Employee)findDataByKey(examUserTestpaper.getEmployeeId(), Employee.class);
		if(employee!=null 
				&& examUserTestpaper.getRelationId()!=null && !"".equals(examUserTestpaper.getRelationId())
				&& examUserTestpaper.getEmployeeId()!=null && !"".equals(examUserTestpaper.getEmployeeId())){
			Map term = new HashMap();
			term.put("businessid", examUserTestpaper.getRelationId());
			term.put("personcode", examUserTestpaper.getEmployeeId());
			List<PersonalRateProgress> personalRateProgressList = queryData("queryPersonalRateProgressHql", term, new HashMap());
			PersonalRateProgress personalRateProgress = null;
			if(personalRateProgressList!=null && personalRateProgressList.size()>0){
				personalRateProgress = personalRateProgressList.get(0);
				personalRateProgress.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				personalRateProgress.setLastUpdatedBy(employee.getEmployeeName());
				personalRateProgress.setStatus(2);
			}else{
				personalRateProgress = new PersonalRateProgress();
				personalRateProgress.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
				personalRateProgress.setCreatedBy(employee.getEmployeeName());
				personalRateProgress.setStatus(1);
			}
			personalRateProgress.setOrganid(employee.getDept()!=null && employee.getDept().getOrgan()!=null 
					? employee.getDept().getOrgan().getOrganId() : null);
			personalRateProgress.setBusinessid(examUserTestpaper.getRelationId()); // 考试或培训业务ID
			personalRateProgress.setJobscode(employee!=null && employee.getQuarter()!=null ? employee.getQuarter().getQuarterId() : null);//岗位编码（冗余）
			personalRateProgress.setJobsname(employee!=null && employee.getQuarter()!=null ? employee.getQuarter().getQuarterName() : null);
			personalRateProgress.setPersonname(employee!=null ? employee.getEmployeeName() : null);//人员姓名（冗余）
			personalRateProgress.setPersoncode(employee!=null ? employee.getEmployeeId() : null);
			personalRateProgress.setTotalscore(examTestpaper.getTotalScore());//总得分或总学时
			personalRateProgress.setContents(examUserTestpaper.getExam().getExamName());//本次达标内容
			personalRateProgress.setTargetscore(score);//目标得分或学时
			personalRateProgress.setApplytime(examUserTestpaper.getExam().getExamStartTime());//申请时间
			personalRateProgress.setTimeoverdue(examUserTestpaper.getExam().getExamEndTime());//过期时间
			if(examTestpaper.getTotalScore()!=null && examTestpaper.getTotalScore().doubleValue()>0){
				personalRateProgress.setIsreachthestd(score/examTestpaper.getTotalScore()*100 > examUserTestpaper.getExam().getPassScore() ? 1 : 0);//是否已达标 0：否 1：是
			}
			personalRateProgress.setReachtime(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd"));//达标日期
			personalRateProgress.setReachetype(2);///学时达标OR考试达标 1：学时达标 2：考试达标
			personalRateProgress.setOrderno(0);
			personalRateProgress.setIsavailable(1);
			saveOld(personalRateProgress);
		}
	}
	
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 根据考试科目产生新试卷并保存
	 * @return
	 * @modified
	 */
	private static boolean isNowRun = true;
	private List<Exam> examList = new ArrayList<Exam>();
	private Thread examUserThread = null;
	private HttpServletRequest httprequest;
	public void saveExamTestPaperInExamInThread(HttpServletRequest request,Exam exam){
		examList.add(exam);
		httprequest = request;
		if(isNowRun){
			isNowRun= false;
			examUserThread = new Thread(){
				public void run(){
					while(examList!=null && examList.size()>0){
						Exam _exam = examList.get(0);
						try {
							saveExamTestPaperInExam(httprequest,_exam);
						} catch (Exception e) {
							e.printStackTrace();
						}
						examList.remove(0);
					}
					examUserThread.stop();
					examUserThread.destroy();
					examUserThread = null;
				}
			};
			examUserThread.start();
			isNowRun = true;
		}
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 根据考试科目产生新试卷并保存
	 * @return
	 * @modified
	 */
	public void saveExamTestPaperInExam(HttpServletRequest request,Exam exam)throws Exception{
		try{
			ExamUserJsonForm.getAndSaveExamUser(request, exam);
			
			//ExampaperService exampaperService = (ExampaperService)getService();
			List<ExamUserTestpaper> list = exam.getExamUserTestpapers();
			Testpaper testpaper = (Testpaper)exam.getTestpaper();
			for(int i=0;i<list.size();i++){
				ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)list.get(i);
				if(examUserTestpaper.getState() == null || "".equals(examUserTestpaper.getState())){
					examUserTestpaper.setState("5");//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
				}
				ExamTestpaper examTestpaper = saveAndGetNewExamTestPaper(exam,examUserTestpaper,testpaper);
				examUserTestpaper.setExamTestpaper(examTestpaper);
				examTestpaper.getExamUserTestpapers().add(examUserTestpaper);
				add(examTestpaper);
				
				
				Map<String,Object> term = new HashMap<String,Object>();
				term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
				List<ThemeType> themeTypeList = queryData("queryTypeInExampaperHql", term, null);
				
				//保存并生成试题
				if(examUserTestpaper!=null && examTestpaper!=null &&  examTestpaper.getExamTestpaperThemes()!=null && 
						examTestpaper.getExamTestpaperThemes().size()>0 && themeTypeList!=null && themeTypeList.size()>0){
					ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examTestpaper, themeTypeList);
					if(exam.getExamArrange().getIsPublic()!=null && exam.getExamArrange().getIsPublic().intValue() == 10){
						ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examTestpaper);
					}
				}
			}
			ExamUserJsonForm.getAndSaveExamUser(request, exam);
		} catch (Exception e) {
			throw  e;
		}
	}
	
	/**
	 * 汇总出题数量与用题数量
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void saveUseTestpaperNum(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		exampaperDao.saveUseTestpaperNum();
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取三天内要进行考试的考生
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExeExamUserList(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		return exampaperDao.getExeExamUserList();
	}
	
	/**
	 * 获取存在需要手工阅卷的科目
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveUserReviewExam(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		return exampaperDao.getHaveUserReviewExam();
	}
	

	/**
	 * 获取存在阅卷人
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map<String,String> getHaveMarkpeople(){
		ExampaperDao exampaperDao =(ExampaperDao)getDao();
		return exampaperDao.getHaveMarkpeople();
	}
	
	
	/**
	 * 发布试卷
	 * @description
	 * @param exam_arrange_id
	 * @modified
	 */
	public void saveAndPublicInExamArrange(String exam_arrange_id,String employeeId,String employeeName)throws Exception{
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		exampaperDao.saveAndPublicInExamArrange(exam_arrange_id, employeeId, employeeName);
	}
	
	
	public String saveAndPublicExam(String examArrangeId,UserSession usersess)throws Exception{
		String msg = "";
		boolean isAllFlag = true;
		ExamArrange examArrange = (ExamArrange)this.findDataByKey(examArrangeId,ExamArrange.class);
		if(examArrange==null){
			Exam exam = (Exam)this.findDataByKey(examArrangeId,Exam.class);
			if(exam!=null){
				examArrange = (ExamArrange)this.findDataByKey(exam.getExamArrange().getExamArrangeId(),ExamArrange.class);
			}
		}
		if(examArrange!=null){
			List<Exam> examsList = examArrange.getExams();
			String publicTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
			if(examArrange.getExams().size()>0){
				for(int j=0;j<examsList.size();j++){
					Exam exam = examsList.get(j);
					
					boolean isExamFlag = true;
					Map term = new HashMap();
					term.put("examId", exam.getExamId());
					List<ExamUserTestpaper> examUserTestpaperslist =  this.getDao()
							.queryConfigQl("examUserTestpaperInExamHql", term, null,ExamUserTestpaper.class);
	
	
					/*List<ExamTestpaperTheme> examTestpaperThemeList = this.getDao()
							.queryConfigQl("queryExamTestpaperThemeByExamIdHql", term, null,ExamTestpaperTheme.class);
					Map<String,List<ExamTestpaperTheme>> examTestpaperThemeMap = new HashMap<String,List<ExamTestpaperTheme>>();
					for(int y=0;y<examTestpaperThemeList.size();y++){
						ExamTestpaperTheme examTestpaper = (ExamTestpaperTheme)examTestpaperThemeList.get(y);
						String key = examTestpaper.getExamTestpaper().getExamTestpaperId();
						List<ExamTestpaperTheme> tmplist = examTestpaperThemeMap.get(key);
						if(tmplist == null) tmplist = new ArrayList<ExamTestpaperTheme>();
						tmplist.add(examTestpaper);
						examTestpaperThemeMap.put(key, tmplist);
					}*/
					
					List<ExamTestpaper> examTestpaperList = this.getDao()
							.queryConfigQl("queryExamTestpaperByExamIdHql", term, null,ExamTestpaper.class);
					Map<String,ExamTestpaper> examTestpaperMap = new HashMap<String,ExamTestpaper>();
					for(int y=0;y<examTestpaperList.size();y++){
						ExamTestpaper examTestpaper = (ExamTestpaper)examTestpaperList.get(y);
						examTestpaperMap.put(examTestpaper.getExamTestpaperId(), examTestpaper);
					}
					
					
					for(int k=0;k<examUserTestpaperslist.size();k++){
						ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperslist.get(k);
						ExamTestpaper examTestpaper = examTestpaperMap.get(
								examUserTestpaper.getExamTestpaper().getExamTestpaperId());
						if(examTestpaper == null){
							examTestpaper = (ExamTestpaper)examUserTestpaper.getExamTestpaper();
						}/*else{
							examUserTestpaper.setExamTestpaper(examTestpaper);
						}*/
						if(examTestpaper == null)continue;
						//List<ExamTestpaperTheme> themeList = examTestpaper.getExamTestpaperThemes();
						Map term2 = new HashMap();
						term2.put("examTestpaperId", examTestpaper.getExamTestpaperId());
						List<ExamTestpaperTheme> themeList = this.getDao()
								.queryConfigQl("queryExamTestpaperThemeByExamTestpaperIdHql", term2, null,ExamTestpaperTheme.class);//examTestpaperThemeMap.get(examTestpaper.getExamTestpaperId());
						if(themeList==null){
							themeList = examTestpaper.getExamTestpaperThemes();
						}
						
						if(themeList!=null && themeList.size()>0){
							double score = 0.0d;
							double defaultScore = 0.0d;
							boolean isOnlFlag = true;
							for(int kk=0;kk<themeList.size();kk++){
								ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themeList.get(kk);
								if(examTestpaperTheme.getScore()!=null){
									score+=examTestpaperTheme.getScore().doubleValue();
								}
								if(examTestpaperTheme.getDefaultScore()!=null){
									defaultScore+=examTestpaperTheme.getDefaultScore().doubleValue();
								}
								if(examTestpaperTheme.getState()==null || !(examTestpaperTheme.getState().intValue()==20
										||examTestpaperTheme.getState().intValue()==30)){
									isOnlFlag = false;
									isAllFlag = false;
									isExamFlag = false;
								}
							}
							if(isOnlFlag){
								examUserTestpaper.setFristScote(score);
								examUserTestpaper.setScote(score);
								exam.setScore(defaultScore);
								examUserTestpaper.setState("30");
								for(int kk=0;kk<themeList.size();kk++){
									ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themeList.get(kk);
									examTestpaperTheme.setState(30);
								}
								examUserTestpaper.setPublicTime(publicTime);
								if(examArrange.getScoreStartTime()!=null && !"".equals(examArrange.getScoreStartTime())
										&& !"null".equals(examArrange.getScoreStartTime())
								&& examArrange.getScoreEndTime()!=null && !"".equals(examArrange.getScoreEndTime())
												&& !"null".equals(examArrange.getScoreEndTime())){
									examUserTestpaper.setScoreStartTime(examArrange.getScoreStartTime());
									examUserTestpaper.setScoreEndTime(examArrange.getScoreEndTime());
								}else{
									String stateTime = DateUtils.getDateAddDay(publicTime.substring(0,10),"yyyy-MM-dd",1);
									
									String year = stateTime.substring(0,4);
									String endTime = (Integer.parseInt(year)+1)+(stateTime.substring(4,10));
									endTime = DateUtils.getDateAddDay(endTime,"yyyy-MM-dd",-1);
									
									examUserTestpaper.setScoreStartTime(stateTime);
									examUserTestpaper.setScoreEndTime(endTime);
								}
								
								if(exam.getPassScore()!=null && examUserTestpaper.getFristScote()!=null
										&& examUserTestpaper.getScote()!=null){
									double passScore = (exam.getPassScore().doubleValue() * defaultScore) / 100.0;
									examUserTestpaper.setPassState(
											examUserTestpaper.getFristScote().doubleValue()>=passScore ? "T" : "F");
								}
								
							}else{
								if(examUserTestpaper.getEmployeeName()==null){
									msg+=examUserTestpaper.getUserName()+",";
								}else{
									msg+=examUserTestpaper.getEmployeeName()+",";
								}
							}
						}
					}
					if(isExamFlag){
						exam.setState("30");
						exam.setPublicUser(usersess.getEmployeeName());//成绩发布人
						exam.setPublicUserId(usersess.getEmployeeId());//成绩发布人ID
						exam.setPublicTime(publicTime);//成绩发布时间
					}
				}
			}
			if(isAllFlag){
				examArrange.setState("30");
				examArrange.setPublicUser(usersess.getEmployeeName());//成绩发布人
				examArrange.setPublicUserId(usersess.getEmployeeId());//成绩发布人ID
				examArrange.setPublicTime(publicTime);//成绩发布时间
				msg = "发布成功！";
				
			}else{
				if(!"".equals(msg))msg = msg.substring(0,msg.length()-1);
				msg = msg+"还有试题未审阅或审核，发布失败！";
			}
			this.saveOld(examArrange);
		}else{
			msg = "没有找到数据！";
		}
		return msg;
	}
	
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
	public List getExamTjxxByOrgan(String organId,String examId,String startDay,String endDay,String exam_property){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamTjxxByOrgan( organId, examId,startDay,endDay,exam_property);
	}
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
	public List getExamTjxxByUser(String organId,String examId,String startDay,String endDay,String exam_property){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamTjxxByUser( organId, examId,startDay,endDay,exam_property);
	}
	
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
	public List getExamDeptTjxxByUser(String topDeptId,String examId,String startDay,String endDay,String exam_property){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamDeptTjxxByUser( topDeptId, examId,startDay,endDay,exam_property);
	}
	/**
	 * 获取考试成绩等信息
	 * @author 朱健
	 * @param id
	 * @param type
	 * @param examId
	 * @return
	 * @modified
	 */
	public List getExamUserInfos(String id,String type,String examId,String startDay,String endDay,String exam_property){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamUserInfos( id, type, examId,startDay,endDay,exam_property);
	}
	
	
	/**
	 * 发出通知
	 * @description
	 * @param exam
	 * @param usersess
	 * @modified
	 */
	public void addExamToSysAffiche(Exam exam,UserSession usersess){
		ExamUserJsonForm.getAndSaveExamUser(null, exam);
		//自动发出通知
		SysAffiche sysAffiche = new SysAffiche();
		sysAffiche.setSender(usersess.getEmployeeName());//发送者
		sysAffiche.setSendTime(new java.sql.Date(System.currentTimeMillis()));//发送时间
		sysAffiche.setTitle((usersess.getOrganAlias()==null ? usersess.getOrganName() : usersess.getOrganAlias())+"进行“"+exam.getExamName()+"”考试通知");
		String[] a1 = exam.getExamStartTime().split(" ");
		String[] aa1 = a1[0].trim().split("-");
		String[] aa2 = a1[1].trim().split(":");
		String content = exam.getExamName();
		/*if(examArrange.getExamProperty().intValue() ==  10){
			+"考试";
		}else if(examArrange.getExamProperty().intValue() ==  20){
			content = exam.getExamName();
		}else{
			content = exam.getExamName();
		}*/
		content += "考试定于"+aa1[0]+"年"+aa1[1]+"月"+aa1[2]+"日"+aa2[0]+"时"+aa2[1]+"分举行，请相关人员准时参加。";
		sysAffiche.setContent(content);
		
		sysAffiche.setDeadline(DateUtils.getDaysBetween(new Date(),DateUtils.convertStrToDate(exam.getExamStartTime().substring(0,10), "yyyy-MM-dd"))+1);
		sysAffiche.setRelationId(exam.getExamId());
		sysAffiche.setRelationType("exam");
		this.getDao().addEntity(sysAffiche);
		
		//添加发送对象
		List<SysAfficheIncepter> addList = new ArrayList<SysAfficheIncepter>();
		List<ExamUserTestpaper> list = exam.getExamUserTestpapers();
		for(int i=0;i<list.size();i++){
			ExamUserTestpaper examUserTestpaper = list.get(i);
			if(examUserTestpaper.getEmployeeId()!=null && !"".equals(examUserTestpaper.getEmployeeId())
					&& !"null".equals(examUserTestpaper.getEmployeeId())){
				SysAfficheIncepter sysAfficheIncepter = new SysAfficheIncepter();
				sysAfficheIncepter.setIncepterId(examUserTestpaper.getEmployeeId());
				sysAfficheIncepter.setIncepterName(examUserTestpaper.getEmployeeName());
				sysAfficheIncepter.setIncepterType(4);
				sysAfficheIncepter.setSortNum(new Integer(i));
				sysAfficheIncepter.setSysAffiche(sysAffiche);
				addList.add(sysAfficheIncepter);
			}
		}
		if(addList!=null && addList.size()>0)
			this.getDao().saveBatchEntity(addList);
	}

	
	/**
	 * 生成对应试卷
	 * @description
	 * @param exam
	 * @modified
	 */
	public void loadUserExam(Exam exam,HttpServletRequest request) throws Exception{
		IocpService.loadUserExam(request,exam.getExamId());
	}
	
	/**
	 * 获取考试管理的考生信息树
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeTree(String publicId,String examId){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamineeTree(publicId, examId);
	}
	
	/**
	 * 获取考试管理的父节点
	 * @description
	 * @param publicId
	 * @param examId
	 * @return
	 * @modified
	 */
	public List<TreeNode> getExamineeOrganTree(String publicId,String examId){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamineeOrganTree(publicId, examId);
	}
	
	/**
	 * 根据查询条件查询考生信息ID
	 * @description
	 * @param examId
	 * @param organId
	 * @param quarter_train_id
	 * @return
	 * @modified
	 */
	public List<ExamUserTestpaper> getExamUserTestpaperList(String examId,String organId,String quarter_train_id){
		ExampaperDao exampaperDao = (ExampaperDao)this.getDao();
		return exampaperDao.getExamUserTestpaperList(examId, organId,quarter_train_id);
	}
	
	/*public static void main(String[] args){
		int len = 5;
		Map<String,String> sortNumMap = new HashMap<String,String>();
		for(int i=0;i<len;i++){
			//产生随机数
			int rInt = (int)(Math.random()*len);
			while(sortNumMap.get(rInt+"")!=null){
				rInt = (int)(Math.random()*len);
			}
			System.out.println(rInt);
			sortNumMap.put(rInt+"",rInt+"");
		}
	}*/
}
