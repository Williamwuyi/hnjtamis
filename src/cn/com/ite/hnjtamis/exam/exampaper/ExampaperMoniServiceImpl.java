package cn.com.ite.hnjtamis.exam.exampaper;
 
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random; 

import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.ExamCode;
import cn.com.ite.hnjtamis.common.ExamPassword;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.Inticket;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperSkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemetype;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.testpaper.TestpaperService;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperMoniServiceImpl</p>
 * <p>Description 模拟试卷处理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月6日 上午9:36:11
 * @version 1.0
 * 
 * @modified records:
 */
/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperMoniServiceImpl</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年5月18日 下午4:00:49
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperMoniServiceImpl extends DefaultServiceImpl implements ExampaperMoniService {

	
	
	/**
	 *
	 * @author zhujian
	 * @description 根据题库获取题型的数目
	 * @param themeBankId
	 * @return
	 * @modified
	 */
	public List<ThemeType> getThemeTypeInBank(String themeBankId){
		Map<String,Object> term = new HashMap<String,Object>();
		term.put("themeBankId", themeBankId);
		List<ThemeType> themeTypeList = queryData("queryTypeInThemeAndBankHql", term, null);
		return themeTypeList;
		
	}
	
	
	

	
	/**
	 *
	 * @author zhujian
	 * @description 根据考生的试卷ID清除考生的考试答案
	 * @param examTestpaperId
	 * @modified
	 */
	public void saveAndCleanExamPaperById(String examTestpaperId){
		ExamTestpaper examTestpaper = (ExamTestpaper)this.findDataByKey(examTestpaperId, ExamTestpaper.class);
		if(examTestpaper!=null)
		this.saveAndCleanExamPaper(examTestpaper);
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 清除考生的考试答案
	 * @param examTestpaper
	 * @modified
	 */
	public void saveAndCleanExamPaper(ExamTestpaper examTestpaper){
		List<ExamTestpaperTheme> themeList = examTestpaper.getExamTestpaperThemes();
		for(int i=0;i<themeList.size();i++){
			themeList.get(i).getExamUserAnswerkeies().clear();
		}
		this.getDao().saveEntityOld(examTestpaper);
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 根据提供给其它接口的userTestpaperId，清除最后一份考试的考生答案
	 * @param userTestpaperId  提供给其它接口的userTestpaperId
	 * @modified
	 */
	public void saveAndCleanExamPaperByUserTestpaperId(String userTestpaperId){
		Map term = new HashMap();
		term.put("userTestpaperId", userTestpaperId);
		List<ExamTestpaper> list = this.queryData("queryExamTestpaperInExamUserTestpaperIdHql", term, null);
		if(list!=null && list.size()>0){
			this.saveAndCleanExamPaper(list.get(0));
		}
	}
	/**
	 *
	 * @author zhujian
	 * @description 自动产生并保存模拟试卷
	 * @param examTitle
	 * @param relationId
	 * @param relationType
	 * @param employeeIds
	 * @param themeBankIdArr
	 * @param examStartTime
	 * @param examEndTime
	 * @param createdBy
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * 人员ID
	 * @return
	 * @throws Exception
	 * @modified
	 */
	protected String[] addUserMoniExam(String examTitle,String relationId,String relationType,String[] employeeIds,String[] themeBankIdArr,
			String examStartTime,String examEndTime,String createdBy,String choutiParam,int examProperty,String choutiType)throws Exception{
		String[] userTestpaperId = null;
		if(createdBy==null || "".equals(createdBy))createdBy = "系统自动创建";//创建人
		userTestpaperId = new String[employeeIds.length];
		try{
			Employee employee = (Employee)this.findDataByKey(employeeIds[0], Employee.class);
			String organId = employee.getDept().getOrgan().getOrganId();
		    String organName = employee.getDept().getOrgan().getOrganAlias();
		    String testpaperType = "20";////试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
		    String examTypeId = null;//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
		    //int examProperty = 30;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
		    //产生试题方式 //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
		    String initParam = choutiParam==null || "".equals(choutiParam) || "null".equals(choutiParam) 
		    		? ExamVariable.getTrainImplementExamPaperInitThemeType() : choutiParam;//Config.getPropertyValue("trainImplement_ExamPaper_Init_ThemeType");//"{0001,null,10,5,0},{0002,null,10,5,0},";
		    
		    String themeBankIds = "";
		    if(themeBankIdArr!=null){
			    for(int i=0;i<themeBankIdArr.length;i++){
			    	themeBankIds+=themeBankIdArr[i]+",";
			    }
			    if(themeBankIds!=null){
			    	themeBankIds = themeBankIds.substring(0,themeBankIds.length()-1);
			    }
		    }else{
		    	 themeBankIds = null;
		    }
		    
		    Testpaper testpaper =this.saveOrGetMoniTestpaper(examTitle,relationId, relationType,themeBankIds, 
		    		organName, organId, initParam,createdBy,testpaperType, examProperty, examTypeId,choutiType,employee.getEmployeeId());
		    
		    ExamArrange examArrange = this.saveOrGetMoniExam(testpaper, examTitle, relationId, relationType, organName, 
		    		organId,examStartTime,examEndTime,createdBy,testpaperType, examProperty, examTypeId);
		    Exam exam = examArrange.getExams().get(0);
		    exam.setTestpaper(testpaper);
		    ExampaperService exampaperService = (ExampaperService)SpringContextUtil.getBean("exampaperService");
		    Map<String,SysUser> sysUserMap = exampaperService.getSysUserMap();
		    for(int i=0;i<employeeIds.length;i++){
		    	employee = (Employee)this.findDataByKey(employeeIds[i], Employee.class);
			    ExamUserTestpaper examUserTestpaper = this.saveOrGetMoniUserExamTestpaper(exam, testpaper, 
			    		examTitle, relationId, relationType, organName, 
			    		organId, employee,createdBy,testpaperType, examProperty, examTypeId,StaticVariable.defined_password,choutiType,sysUserMap);
			    userTestpaperId[i] = examUserTestpaper.getUserTestpaperId();
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return userTestpaperId;
	}


	/**
	 *
	 * @author zhujian
	 * @description 获取试卷模版，如没有则根据参数生成一个返回
	 * @param relationId
	 * @param relationType
	 * @param organName
	 * @param organId
	 * @param initParam //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @modified
	 */
	protected Testpaper saveOrGetMoniTestpaper(String examTitle,String relationId,String relationType,String themeBankIds,
			String organName,String organId,String initParam,String createdBy,String testpaperType,
			int examProperty,String examTypeId,String choutiType,String employeeId)throws Exception{		
		TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
		Testpaper testpaper = null;
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		List<Testpaper> list = this.queryData("queryTestpaperInRelationHql", term, null);
		if(list!=null && list.size()>0){
			testpaper = list.get(0);
		}else{
			testpaper = new Testpaper();
			testpaper.setRelationId(relationId);
			testpaper.setRelationType(relationType);
			testpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			testpaper.setCreatedBy(createdBy);//创建人
			testpaper.setOrganId(organId);//机构ID
			testpaper.setOrgan(organName);//机构名称
			testpaper.setUseNum(0);
			testpaper.setIsPrivate("5");//是否私有(5：否,10：是)
			testpaper.setTestpaperName(examTitle);//试卷名称
			testpaper.setTestpaperType(testpaperType);//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
			testpaper.setExamTypeId(examTypeId);//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
			List<Dictionary> examKslxList = testpaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
			if(testpaper.getExamTypeId()!=null && examKslxList!=null && examKslxList.size()>0){
				for(int i=0;i<examKslxList.size();i++){
					if(examKslxList.get(i).getDataKey().equals(testpaper.getExamTypeId())){
						testpaper.setExamTypeName(examKslxList.get(i).getDataName());//考试类型(性质)，与考试类型ID一组
						break;
					}
				}
			}
			testpaper.setTestpaperRank(1.0d);//难度系数
			testpaper.setScreeningMethods(10);//筛选方式(5：分数，10：题数)
			testpaper.setTestpaperTime((short)60);//参考考时（分钟）
			testpaper.setIsUse(10);//是否使用(5：否,10：是)
			testpaper.setRemark(null);//备注
			testpaper.setExamProperty(examProperty);//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			testpaper.setState("15");//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
			testpaper.getTestpaperThemetypes().clear();
			testpaper.getTestpaperSkeies().clear();
			testpaper.getTestpaperShares().clear();
			
			String[] initParamArr = initParam.split("},");
			List<TestpaperThemeForm> themeList =testpaperService.getThemeInTemplate(initParamArr, 
					themeBankIds,examProperty,choutiType,employeeId,null);
			if(themeList!=null && themeList.size()>0){
				if(themeBankIds!=null && themeBankIds.length()>0){
					String[] themeBankIdsArr = themeBankIds.split(",");
					for(int i = 0;i<themeBankIdsArr.length;i++){
						ThemeBank themeBank = (ThemeBank)this.findDataByKey(themeBankIdsArr[i], ThemeBank.class);
						TestpaperSkey testpaperSkey = new TestpaperSkey();
						testpaperSkey.setThemeBankId(themeBank.getThemeBankId());
						testpaperSkey.setThemeBankName(themeBank.getThemeBankName());
						testpaperSkey.setTestpaper(testpaper);
						testpaperSkey.setOrganId(organId);//机构ID
						testpaperSkey.setOrganName(organName);//机构名称
						testpaperSkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						testpaperSkey.setCreatedBy(createdBy);//创建人
						testpaperSkey.setSortNum(i);
						testpaper.getTestpaperSkeies().add(testpaperSkey);
					}
				}
				
				if(initParam!=null && initParamArr.length>0){
					for(int i = 0 ;i<initParamArr.length;i++){
						String params = initParamArr[i].substring(1,initParamArr[i].length());
						String[] paramsArr = params.split(",");
						String themeTypeId = paramsArr[0];
						String specialityid = "undefined".equals(paramsArr[1]) || "null".equals(paramsArr[1]) ? null : paramsArr[1];
						String screeningMethods =  paramsArr[2];
						Speciality speciality = specialityid==null? null : (Speciality)this.findDataByKey(specialityid, Speciality.class);
						int selectLength = paramsArr[3]!=null && !"".equals(paramsArr[3]) ? Integer.parseInt(paramsArr[3]) : 0;
						double score = paramsArr[4]!=null && !"".equals(paramsArr[4]) ? Double.parseDouble(paramsArr[4]) : 0.0d;
						
						//TestpaperThemetypeForm testpaperThemetypeForm = (TestpaperThemetypeForm)testpaperThemetypeFormList.get(i);
						TestpaperThemetype testpaperThemetype = new TestpaperThemetype();
						//testpaperThemetype.setTestpaperThemetypeId();//试卷-题型ID
						ThemeType themeType = (ThemeType)this.findDataByKey(themeTypeId, ThemeType.class);
						testpaperThemetype.setThemeType(themeType);//题型
						testpaperThemetype.setTestpaper(testpaper);///试卷
						testpaperThemetype.setRankRate(null);/////难度比例 {容易,难,很难}{0，0，0}逗号分割，表示某试卷某题型的难度分布
						testpaperThemetype.setProfessionId(speciality!=null ? speciality.getSpecialityid() : null);//专业ID
						testpaperThemetype.setProfessionName(speciality!=null ? speciality.getSpecialityname() : null);//专业
						//testpaperThemetype.setProfessionLevelCode(testpaperThemetypeForm.getSpeciality().get);//专业级别编码
						testpaperThemetype.setRate(null);//比例%
						testpaperThemetype.setScore(score);//按分数筛选时为分数,按题数筛选时为题数
						if("5".equals(screeningMethods)){//5：分数，10：题数
							testpaperThemetype.setSetThemetype("10");//配置方式 10-按分数 20-按题数
						}else if("10".equals(screeningMethods)){
							testpaperThemetype.setSetThemetype("20");//配置方式 10-按分数 20-按题数
						}
						testpaperThemetype.setTotal(selectLength);///题数
						testpaperThemetype.setSortNum(i+1);//排序号
						testpaperThemetype.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						testpaperThemetype.setCreatedBy(createdBy);//创建人
						//testpaperThemetype.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
						testpaper.getTestpaperThemetypes().add(testpaperThemetype);
						//this.service.save(testpaperThemetype);
					}
				}
				
	
				//试题
				//if(themeList!=null && themeList.size()>0){
					double themeScore = 0.0d;
					for(int i=0;i<themeList.size();i++){
						TestpaperThemeForm testpaperThemeForm = (TestpaperThemeForm)themeList.get(i);
						Theme theme = (Theme)this.findDataByKey(testpaperThemeForm.getThemeId(), Theme.class);
						if(theme!=null){
							TestpaperTheme testpaperTheme = new TestpaperTheme();
							testpaperTheme.setCreatedBy(createdBy);//创建人
							//testpaperTheme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
							testpaperTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							testpaperTheme.setTheme(theme);
							testpaperTheme.setTestpaper(testpaper);
							testpaperTheme.setThemeType(theme.getThemeType());
							testpaperTheme.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);//题型
							testpaperTheme.setKnowledgePoint(theme.getKnowledgePoint());//所属知识点
							testpaperTheme.setThemeName(theme.getThemeName());//试题
							testpaperTheme.setScoreType(theme.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
							testpaperTheme.setDegree(theme.getDegree());//难度 5：容易,10：一般15：难,20：很难
							testpaperTheme.setEachline(theme.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
							testpaperTheme.setWriteUser(theme.getWriteUser());//出题人
							testpaperTheme.setExplain(theme.getExplain());//注解
							testpaperTheme.setDefaultScore(theme.getDefaultScore());//默认分数
							testpaperTheme.setThemeSetNum(theme.getThemeSetNum());//出题次数
							testpaperTheme.setThemePeopleNum(theme.getThemePeopleNum());//考试人次
							testpaperTheme.setThemeRightNum(theme.getThemeRightNum());//答题正确数
							testpaperTheme.setRemark(theme.getRemark());//备注
							testpaperTheme.setState(theme.getState());//状态 5:保存10:上报15:发布20:打回
							testpaperTheme.setIsUse(theme.getIsUse());//有否有效 5:有效 10：无效
							testpaperTheme.setThemeName(theme.getThemeName());
							
							testpaperTheme.setOrganId(organId);//机构ID
							testpaperTheme.setOrganName(organName);//机构名称
							testpaperTheme.setSortNum(i);
							testpaperTheme.setIsUse("5");//是否有效 5：否,10：是
							if(testpaper.getState()!=null && !"".equals(testpaper.getState())){
								testpaperTheme.setState(new Integer(testpaper.getState()));
							}
							themeScore +=theme!=null && theme.getDefaultScore()!=null ? theme.getDefaultScore() : 0.0d;
							
							testpaperTheme.getTestpaperThemeAnswerkeies().clear();
							List<ThemeAnswerkey> anslist = theme.getThemeAnswerkeies();
							if(anslist!=null && anslist.size()>0){
								for(int t = 0 ;t < anslist.size() ; t++){
									ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)anslist.get(t);
									TestpaperThemeAnswerkey testpaperThemeAnswerkey = new TestpaperThemeAnswerkey();
									BeanUtils.copyProperties(themeAnswerkey, testpaperThemeAnswerkey);
									testpaperThemeAnswerkey.setAnswerkeyId(null);
									testpaperThemeAnswerkey.setTestpaperTheme(testpaperTheme);
									testpaperThemeAnswerkey.setThemeId(theme.getThemeId());
									testpaperThemeAnswerkey.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
									testpaperThemeAnswerkey.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);
									testpaperThemeAnswerkey.setCreatedBy(createdBy);//创建人
									testpaperThemeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
									testpaperThemeAnswerkey.setOrganId(organId);//机构ID
									testpaperThemeAnswerkey.setOrganName(organName);//机构名称
									testpaperThemeAnswerkey.setSortNum(themeAnswerkey.getSortNum());
									testpaperTheme.getTestpaperThemeAnswerkeies().add(testpaperThemeAnswerkey);
								}
							}
							testpaper.getTestpaperThemes().add(testpaperTheme);
						}
					} 
					testpaper.setTotalTheme((short)themeList.size());//总题数
					testpaper.setTotalScore(themeScore);//总分数
				//}
				
			}
			this.getDao().addEntity(testpaper);
		}
		
		return testpaper;
	}

	
	/**
	 *
	 * @author zhujian
	 * @description 获取科目信息
	 * @param testpaper
	 * @param trainImplement
	 * @param relationId
	 * @param relationType
	 * @param themeBankIds
	 * @param organName
	 * @param organId
	 * @param initParam
	 * @return
	 * @throws Exception
	 * @modified
	 */
	protected ExamArrange saveOrGetMoniExam(Testpaper testpaper,String examTitle,String relationId,String relationType,
			String organName,String organId,String examStartTime,String examEndTime,String createdBy,
			String testpaperType,int examProperty,String examTypeId)throws Exception{
		TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
		ExamArrange examArrange = null;
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		List<ExamArrange> list = this.queryData("queryExamArrangeInRelationHql", term, null);
		if(list!=null && list.size()>0){
			examArrange = list.get(0);
		}else{
			examArrange = new ExamArrange();
			examArrange.setRelationId(relationId);
			examArrange.setRelationType(relationType);
			examArrange.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			examArrange.setCreatedBy(createdBy);//创建人
			//examArrange.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
			examArrange.setOrganId(organId);//机构ID
			examArrange.setOrganName(organName);//机构名称
			//BeanUtils.copyProperties(form,examArrange);
			//examArrange.setExamArrangeId(form.getExamArrangeId());
			examArrange.setExamPublic(null);
			examArrange.setExamName(examTitle);//考试名称
			examArrange.setScore(testpaper.getTotalScore());//总分
			examArrange.setExamCode(ExamCode.getSequence());//考试编码
			examArrange.setExamTypeId(examTypeId);//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
			List<Dictionary> examKslxList = testpaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
			if(testpaper.getExamTypeId()!=null && examKslxList!=null && examKslxList.size()>0){
				for(int i=0;i<examKslxList.size();i++){
					if(examKslxList.get(i).getDataKey().equals(testpaper.getExamTypeId())){
						examArrange.setExamTypeName(examKslxList.get(i).getDataName());//考试类型(性质)，与考试类型ID一组
						break;
					}
				}
			}
			//examArrange.setExamTypeName(form.getExamTypeName());//考试类型(性质)，与考试类型ID一组
			examArrange.setExamProperty(examProperty);//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			examArrange.setIsPublic(10);//是否发布成绩 5：否，10：是
			//examArrange.setPublicUser(form.getPublicUser());//成绩发布人
			//examArrange.setPublicUserId(form.getPublicUserId());//成绩发布人编号
			//examArrange.setPublicTime(form.getPublicTime());//成绩发布时间
			examArrange.setIsUse(10);///是否使用 5：否,10：是
			//examArrange.setCheckUser(form.getCheckUser());//审核人
			//examArrange.setCheckUserId(form.getCheckUserId());///审核人ID
			//examArrange.setCheckTime(form.getCheckTime());//审核时间
			examArrange.setState("15");////状态 5：未上报，10：等待审核，15：审核通过，20：审核打回 30-发布
			examArrange.setExamPaperType(20);////考试方式 10-正式 20-模拟
			examArrange.setIsIdNumberLogin("1");//是否允许身份证登陆 0-允许  1-不允许
			//examArrange.setRelationId(form.getRelationId());//关联ID（如：练习安排等）
			//examArrange.setRelationType(form.getRelationType());//关联类型
			examArrange.setRemark(null);//备注
			
			
			
			Exam exam = new Exam();
			exam.setRelationId(relationId);
			exam.setRelationType(relationType);
			exam.setExamName(examTitle);
			exam.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			exam.setCreatedBy(createdBy);//创建人
			//exam.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
			exam.setOrganId(organId);//机构ID
			exam.setOrganName(organName);//机构名称
			
			exam.setExamPaperType(Integer.parseInt(testpaperType));//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟
			exam.setExamCode(ExamCode.getSequence()+"00");//科目编码  考试安排编码+二位顺序号
			exam.setScore(testpaper.getTotalScore());//科目总分
			exam.setIsPublic(10);//是否发布成绩 5：否，10：是
			exam.setBanTime((short)999);//禁止进入时间_分钟
			exam.setBeforeTime((short)999);//提前进入时间_分钟
			exam.setPassScore(60.0);//合格分数线 默认按总分的60％，可进行设置。
			//exam.setPublicUser(examForm.getPublicUser());//成绩发布人
			//exam.setPublicUserId(examForm.getPublicUserId());//成绩发布人ID
			//exam.setPublicTime(examForm.getPublicTime());//成绩发布时间
			exam.setIsCutScreen("0");//是否截取图片 0：否；5是
			exam.setIsUse(10);///是否使用 5：否,10：是
			//exam.setCheckUser(examForm.getCheckUser());//审核人
			//exam.setCheckUserId(examForm.getCheckUserId());///审核人ID
			//exam.setCheckTime(examForm.getCheckTime());//审核时间
			exam.setMarkScoreType("10");//评分方式 10-考完系统立即算分  20-待审核后算法
			exam.setState("15");//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
			exam.setExamStartTime(examStartTime);
			exam.setExamEndTime(examEndTime);
			
			exam.setTestpaper(testpaper);
			exam.setExamArrange(examArrange);
			
			examArrange.getExams().add(exam);
			
			this.getDao().addEntity(examArrange);
		}
		return examArrange;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取考生试卷，如没有则根据参数生成一个返回
	 * @param relationId
	 * @param relationType
	 * @param organName
	 * @param organId
	 * @param initParam //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @modified
	 */
	protected ExamUserTestpaper saveOrGetMoniUserExamTestpaper(Exam exam, Testpaper testpaper,String examTitle,String relationId,String relationType,
			 String organName,String organId,Employee employee,String createdBy,String testpaperType,
			 int examProperty,String examTypeId,String defaultPassword,String choutiType,Map<String,SysUser> sysUserMap)throws Exception{		
		TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
		ExamUserTestpaper examUserTestpaper = null;
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		term.put("employeeId", employee.getEmployeeId());
		List<ExamUserTestpaper> list = this.queryData("queryExamUserTestpaperInRelationHql", term, null);
		if(list!=null && list.size()>0){
			examUserTestpaper = list.get(0);
			examUserTestpaper.setSubTime(null);
			examUserTestpaper.setFristScote(null);
			examUserTestpaper.setScote(null);
			examUserTestpaper.setInTime(null);
			examUserTestpaper.setOutTime(null);
			examUserTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			examUserTestpaper.setCreatedBy(createdBy);//创建人
		}else{
			Inticket inticket = new Inticket();
			String prefix = inticket.getPrefix(exam.getExamArrange().getExamArrangeId());
			int maxPostfixNum = inticket.getPostfixMaxNum(exam.getExamArrange().getExamArrangeId(),prefix);
			maxPostfixNum++;
			
			examUserTestpaper = new ExamUserTestpaper();
			examUserTestpaper.setRelationId(relationId);
			examUserTestpaper.setRelationType(relationType);
			examUserTestpaper.setExamPublicUser(null);
			examUserTestpaper.setExam(exam);
			examUserTestpaper.setUserOrganId(organId);//所属机构ID
			examUserTestpaper.setUserOrganName(organName);//所属机构
			examUserTestpaper.setUserDeptId(employee.getDept()!=null ? employee.getDept().getDeptId() :null);//所属部门ID
			examUserTestpaper.setUserDeptName(employee.getDept()!=null ?employee.getDept().getDeptId():null);//所属部门
			//examUserTestpaper.setUserGroupId(employee.getUserGroupId());//所属班组ID
			//examUserTestpaper.setUserGroupName(employee.getUserGroupName());//所属班组
			examUserTestpaper.setPostId(employee.getQuarter()!=null ? employee.getQuarter().getQuarterId() : null);//所属岗位ID
			examUserTestpaper.setPostName(employee.getQuarter()!=null ? employee.getQuarter().getQuarterName() : null);//所属岗位
			examUserTestpaper.setUserName(employee.getEmployeeName());//姓名
			examUserTestpaper.setUserSex(employee.getSex()+"");//性别  1-男  2-女
			SysUser sysUser = sysUserMap.get(employee.getEmployeeId());
			String inticketStr=inticket.getInticket(prefix,exam.getExamStartTime(),++maxPostfixNum);
			if(sysUser!=null){
				examUserTestpaper.setLoginId(sysUser.getAccount());
				examUserTestpaper.setLoginPassword(sysUser.getPassword());
			}
			examUserTestpaper.setInticket(inticketStr);//准考证号
			examUserTestpaper.setIdNumber(null);//身份证号
			//examUserTestpaper.setUserBirthday(employee.getBirthday());//出生年月
			//examUserTestpaper.setUserNation(employee.getUserNation());//民族
			//examUserTestpaper.setUserAddr(employee.getUserAddr());//住址
			//examUserTestpaper.setUserPhone(employee.getUserPhone());//联系电话
			//examUserTestpaper.setUserInfo(employee.getUserInfo());//考生信息
			examUserTestpaper.setExamPassword(defaultPassword==null ? ExamPassword.getExamPassword(4) : null);//考试密码
			/*examUserTestpaper.setExamPaperType(examPublicUser.getExamPaperType());//考试方式 10-正式（同试卷打乱） 11-正式（各考生随机抽题） 20-模拟 30-练习
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
*/			examUserTestpaper.setState("5");//状态 5:生成10:待考15:已考完 16-作弊 20:已阅卷 25 打回 30-发布
			examUserTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			examUserTestpaper.setCreatedBy(createdBy);//创建人
			//examUserTestpaper.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
			examUserTestpaper.setOrganId(organId);//机构ID
			examUserTestpaper.setOrganName(organName);//机构名称
			examUserTestpaper.setEmployeeId(employee.getEmployeeId());//人员ID
			examUserTestpaper.setEmployeeName(employee.getEmployeeName());//人员
			
			ExamTestpaper examTestpaper = this.addExamTestpaper(examUserTestpaper,exam.getExamPaperType(),choutiType,employee.getEmployeeId());
			this.getDao().addEntity(examTestpaper);
		}
		return examUserTestpaper;
	}
	
	
	public ExamTestpaper addExamTestpaper(ExamUserTestpaper examUserTestpaper,int examPaperType,String choutiType,String employeeId){
		Exam exam = examUserTestpaper.getExam();
		Testpaper testpaper = exam.getTestpaper();
		ExamTestpaper examTestpaper = new ExamTestpaper();
		examTestpaper.setRelationId(examUserTestpaper.getRelationId());
		examTestpaper.setRelationType(examUserTestpaper.getRelationType());
		examTestpaper.getExamUserTestpapers().add(examUserTestpaper);
		examTestpaper.setState("5");//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
		examTestpaper.setOrganName(exam.getOrganName());//机构名
		examTestpaper.setOrganId(exam.getOrganId());//机构编号
		examTestpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时
		examTestpaper.setUseNum(0);
		if(exam!=null){
			examTestpaper.setExamArrangeId(exam.getExamArrange().getExamArrangeId());
			examTestpaper.setExamId(exam.getExamId());
			examTestpaper.setExamTestpaperName(exam.getExamName());
		}
		
		examTestpaper.setTestpaperRank(testpaper.getTestpaperRank());//难度系数
		examTestpaper.setScreeningMethods(testpaper.getScreeningMethods());//筛选方式(5：分数，10：题数)
		examTestpaper.setTestpaperTime(testpaper.getTestpaperTime());//参考考时（分钟）
		examTestpaper.setIsUse(10);//是否使用(5：否,10：是)
		examTestpaper.setIsPrivate("5");//是否私有(5：否,10：是)
		examTestpaper.setUserId(examUserTestpaper.getEmployeeId());
		examTestpaper.setMarkScoreType(exam.getMarkScoreType());
		examTestpaper.setTestpaperId(testpaper.getTestpaperId());
		examTestpaper.setEmployeeId(examUserTestpaper.getEmployeeId());
		examTestpaper.setEmployeeName(examUserTestpaper.getEmployeeName());

		//examTestpaper.getExamTestpaperThemes().clear();
		if(!(examPaperType == 10 || examPaperType == 20 || examPaperType == 30)){
			examPaperType = exam.getExamPaperType();
		}
		short totalTheme = 0;
		double totalScore = 0.0d;
		if(examPaperType == 10){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
			//List<TestpaperTheme> testpaperThemelist = testpaper.getTestpaperThemes();
			//------------用于产生缓存数据----------------------------
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
			
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			List<ThemeType> themeTypeList = this.getDao().findAll(ThemeType.class);
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeId(), themeType);
			}
			//---------------------------------------------------------------------------------
			
			int len = testpaperThemelist.size();
			Random ran = new Random(len);
			Map<String,String> sortNumMap = new HashMap<String,String>();
			for(int i=0;i<testpaperThemelist.size();i++){
				//产生随机数
				int rInt = ran.nextInt(len); 
				while(sortNumMap.get(rInt+"")!=null){
					rInt = ran.nextInt(len); 
				}
				sortNumMap.put(rInt+"",rInt+"");
				TestpaperTheme testpaperTheme = (TestpaperTheme)testpaperThemelist.get(i);
				ThemeType themeType = themeTypeMap.get(testpaperTheme.getThemeType().getThemeTypeId());
				if(themeType == null){
					themeType = testpaperTheme.getThemeType();
					themeTypeMap.put(testpaperTheme.getThemeType().getThemeTypeId(), themeType);
				}
				ExamTestpaperTheme examTestpaperTheme = new ExamTestpaperTheme();
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
				if(examUserTestpaper!=null && examUserTestpaper.getExamPublicUser()!=null){
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
					Random ran2 = new Random(len2);
					Map<String,String> sortNumMap2 = new HashMap<String,String>();
					for(int j=0;j<anslist.size();j++){
						//产生随机数
						int rInt2 = ran2.nextInt(len2); 
						while(sortNumMap2.get(rInt2+"")!=null){
							rInt2 = ran2.nextInt(len2); 
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
			
			
			
		}else if(examPaperType == 20){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
			//List<TestpaperTheme> testpaperThemelist = testpaper.getTestpaperThemes();
			//------------用于产生缓存数据----------------------------
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
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			List<ThemeType> themeTypeList = this.getDao().findAll(ThemeType.class);
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeId(), themeType);
			}
			//-----------------------------------------------------------------------------------
			
			for(int i=0;i<testpaperThemelist.size();i++){
				TestpaperTheme testpaperTheme = (TestpaperTheme)testpaperThemelist.get(i);
				ThemeType themeType = themeTypeMap.get(testpaperTheme.getThemeType().getThemeTypeId());
				if(themeType == null){
					themeType = testpaperTheme.getThemeType();
					themeTypeMap.put(testpaperTheme.getThemeType().getThemeTypeId(), themeType);
				}
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
				if(examUserTestpaper!=null && examUserTestpaper.getExamPublicUser()!=null){
					examTestpaperTheme.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
				}
				examTestpaperTheme.setDefaultScore(testpaperTheme.getDefaultScore());
				examTestpaperTheme.setScore(0.0d);
				examTestpaperTheme.setScoreType(testpaperTheme.getScoreType());//得分(算分)类型 0自动阅卷；1人工阅卷；2系统外阅卷
				examTestpaperTheme.setState(5);//状态 5:生成10:待考15:已考完20:已阅卷 25 打回
				totalTheme++;
				totalScore+=examTestpaperTheme!=null && examTestpaperTheme.getDefaultScore()!=null ?  examTestpaperTheme.getDefaultScore().doubleValue() : 0.0d;
				
				//short totalTheme = 0;
				//double totalScore = 0.0d;
				if("5".equals(themeType.getThemeType())
						|| "10".equals(themeType.getThemeType())
						|| "15".equals(themeType.getThemeType())){//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
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
		}else if(examPaperType == 30){//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
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
			TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
			List<TestpaperThemeForm> themeList = testpaperService.getThemeInTemplate(initParamArr, 
					themeBankIds,exam.getExamArrange().getExamProperty(),choutiType,employeeId,null);
			
			
			int len = themeList.size();
			Random ran = new Random(len);
			Map<String,String> sortNumMap = new HashMap<String,String>();
			for(int i=0;i<themeList.size();i++){
				//产生随机数
				int rInt = ran.nextInt(len); 
				while(sortNumMap.get(rInt+"")!=null){
					rInt = ran.nextInt(len); 
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
					Random ran2 = new Random(len2);
					Map<String,String> sortNumMap2 = new HashMap<String,String>();
					for(int j=0;j<anslist.size();j++){
						//产生随机数
						int rInt2 = ran2.nextInt(len2); 
						while(sortNumMap2.get(rInt2+"")!=null){
							rInt2 = ran2.nextInt(len2); 
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
		examUserTestpaper.setExamTestpaper(examTestpaper);
		return examTestpaper;
		
		
	}
}
