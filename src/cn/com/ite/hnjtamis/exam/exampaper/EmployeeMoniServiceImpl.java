package cn.com.ite.hnjtamis.exam.exampaper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
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
 * 考生模拟试题
 * @author 朱健
 * @create time: 2015年11月27日 下午2:23:16
 * @version 1.0
 * 
 * @modified records:
 */
public class EmployeeMoniServiceImpl extends ExampaperMoniServiceImpl implements EmployeeMoniService{

	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type,int maxRowNum){
		ExampaperDao exampaperDao  = (ExampaperDao)this.getDao();
		return exampaperDao.getThemeTypeByBankId(themeBankId, relationType, themeTypes, chouti_theme_type,maxRowNum);
	}
	
	/**
	 * 根据题库获取题型的数目
	 * @author 朱健
	 * @param themeBankId 题库ID
	 * @param relationType 已生成试题关联类型
	 * @param themeTypes 试题类型
	 * @param  chouti_theme_type 考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
	 * @param maxRowNum 获取题目的最大值
	 * @return
	 * @modified
	 */
	public List<Object[]> getThemeTypeAndThemeNumsByBankId(String themeBankId,String relationType,
			String themeTypes,int chouti_theme_type){
		ExampaperDao exampaperDao  = (ExampaperDao)this.getDao();
		return exampaperDao.getThemeTypeAndThemeNumsByBankId(themeBankId, relationType, themeTypes, chouti_theme_type);
	}
	
	/**
	 * 首页生成模拟试题
	 * @author 朱健
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param employeeName
	 * @param cleanUserAns
	 * @param examTitle
	 * @param choutiParam
	 * @param addExamType
	 * @param themeBankId
	 * @return 首页生成模拟试题ID
	 * @throws Exception
	 * @modified
	 */
	public String addOrGetMoniIndexExam(String relationId,String relationType,String employeeId,String employeeName,
			String cleanUserAns,String examTitle,String choutiParam,String addExamType,String themeBankId)throws Exception{
		String id = null;
		String nowTime = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
		Map term = new HashMap();
		term.put("relationId", relationId);
		term.put("relationType", relationType);
		term.put("employeeId", employeeId);
		List<ExamUserTestpaper> userExamlist = this.queryData("queryExamUserTestpaperInRelationHql", term, new HashMap());
		boolean isNewExam = true;
		Testpaper perv_testpaper = null;
		if(userExamlist!=null && userExamlist.size()>0){
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)userExamlist.get(0);
			//如果已经提交则创建新的试卷
			if(examUserTestpaper.getSubTime()!=null 
					&& !"".equals(examUserTestpaper.getSubTime())
					&& !"null".equals(examUserTestpaper.getSubTime())){
				examUserTestpaper.setRelationType(relationType+"_fin");
				examUserTestpaper.getExamTestpaper().setRelationType(relationType+"_fin");
				isNewExam = true;
				
				String testpaperId = examUserTestpaper.getExamTestpaper().getTestpaperId();
				Testpaper testpaper = (Testpaper)this.findDataByKey(testpaperId,Testpaper.class);
				/*if(testpaper!=null){
					testpaper.setRelationType(relationType+"_fin");
					this.saveOld(testpaper);
				}*/
				if("mocs".equals(relationType)){
					testpaper.setRelationType(relationType+"_fin");
					this.saveOld(testpaper);
				}
				perv_testpaper = testpaper;
			}else{
				String testpaperId = examUserTestpaper.getExamTestpaper().getTestpaperId();
				Testpaper testpaper = (Testpaper)this.findDataByKey(testpaperId,Testpaper.class);
				//如果产生的试卷没有试题则需要重新创建
				if(testpaper.getTestpaperThemes() == null || testpaper.getTestpaperThemes().size()==0){
					examUserTestpaper.setRelationType(relationType+"_notTheme");
					//String testpaperId = examUserTestpaper.getExamTestpaper().getTestpaperId();
					examUserTestpaper.getExamTestpaper().setRelationType(relationType+"_notTheme");
					isNewExam = true;
					
					testpaper.setRelationType(relationType+"_notTheme");
					this.saveOld(testpaper);
					
					perv_testpaper = testpaper;
				}else{
					id = examUserTestpaper.getUserTestpaperId();
					
					if("1".equals(cleanUserAns)){ //清理答案
						this.saveAndCleanExamPaperByUserTestpaperId(id);
						
						examUserTestpaper.setSubTime(null);
						examUserTestpaper.setFristScote(null);
						examUserTestpaper.setScote(null);
						examUserTestpaper.setInTime(nowTime);
						examUserTestpaper.setOutTime(null);
						
						examUserTestpaper.getExamTestpaper().setInTime(nowTime);
						examUserTestpaper.getExamTestpaper().setOutTime(null);
						examUserTestpaper.getExamTestpaper().setSubTime(null);
					}
					isNewExam = false;
				}
			}
			this.saveOld(examUserTestpaper);
		}
		
		if(isNewExam){//创建新试题
			if(examTitle == null || "".equals(examTitle) || "null".equals(examTitle)){
				if("mocs".equals(relationType)){
					examTitle = "岗位培训模拟试题("+(DateUtils.convertDateToStr(new Date(),"yyyyMMdd"))+")";
				}else{
					examTitle = "岗位培训随堂练习("+(DateUtils.convertDateToStr(new Date(),"yyyyMMdd"))+")";
				}
			}
			
			
			int examPaperType = -1;
			if(addExamType!=null && !"".equals(addExamType) && !"null".equals(addExamType)){
				try{
					examPaperType = Integer.parseInt(addExamType);
				}catch(Exception e){
					e.printStackTrace();
					examPaperType = 20;
				}
			}
			
			///************抽题方式    设置   开始**************************/
			if(choutiParam==null || "".equals(choutiParam) || "null".equals(choutiParam)){
				choutiParam = "";
				int chouti_theme_type = 40;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
				int chouti_theme_num = 1000;//抽题数量
				if("mocs".equals(relationType)){
					chouti_theme_num = 75;
					List<Object[]> themeTypeList = this.getThemeTypeAndThemeNumsByBankId(themeBankId, relationType, "5,10,25", chouti_theme_type);//this.getThemeTypeInBank(themeBankId);
					//{4028e4a14d742159014d74891456062a,null,10,5,0},{4028e4a14d742159014d748944e8063f,null,10,5,0},
					 //产生试题方式 //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
					//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
					int allThemeNum = 0;
					for(int k=0;k<themeTypeList.size();k++){
						Object[] obj = themeTypeList.get(k);
						Integer themeNum = (Integer)obj[1];
						allThemeNum+=themeNum.intValue();
					}
					if(allThemeNum>chouti_theme_num){
						int[][] themeNumInType = new int[themeTypeList.size()][2];
						int synum = chouti_theme_num;
						for(int i=0;i<themeNumInType.length;i++){
							Object[] obj = themeTypeList.get(i);
							Integer themeNum = (Integer)obj[1];
							BigDecimal ss = (new BigDecimal((chouti_theme_num * themeNum.intValue()/allThemeNum))).setScale(0,BigDecimal.ROUND_DOWN);
							themeNumInType[i][0] = ss.intValue();
							synum = synum - themeNumInType[i][0];
							themeNumInType[i][1] = themeNum.intValue();
						}
						int ind = 0;//指向的修改位置
						int numXhs = 0;//只循环的次数
						while(synum<chouti_theme_num && numXhs<chouti_theme_num){
							if((themeNumInType[ind][0]+1) <= themeNumInType[ind][1]){
								themeNumInType[ind][0]++;
							}
							int sumValue = 0;
							for(int i=0;i<themeNumInType.length;i++){
								sumValue+=themeNumInType[i][0];
							}
							numXhs++;
							if(sumValue >= chouti_theme_num){
								break;
							}else{
								synum = chouti_theme_num - sumValue;
								ind++;
								if(ind>themeNumInType.length){
									ind = 0;
								}
							}
						}
						for(int k=0;k<themeTypeList.size();k++){
							Object[] obj = themeTypeList.get(k);
							obj[1] = themeNumInType[k][0];
							themeTypeList.set(k, obj);
						}
					}
					for(int k=0;k<themeTypeList.size();k++){
							Object[] obj = themeTypeList.get(k);
							ThemeType themeType = (ThemeType)obj[0];
							Integer themeNum = (Integer)obj[1];
							if("5".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							}else if("10".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							//}else if("15".equals(themeType.getThemeType())){
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							//}else if("20".equals(themeType.getThemeType())){
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							}else if("25".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							}else{
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							}
							
					}
				}else{
					List<Object[]> themeTypeList = this.getThemeTypeByBankId(themeBankId, relationType, "5,10,25", chouti_theme_type,chouti_theme_num);//this.getThemeTypeInBank(themeBankId);
					//{4028e4a14d742159014d74891456062a,null,10,5,0},{4028e4a14d742159014d748944e8063f,null,10,5,0},
					 //产生试题方式 //{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
					//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
					for(int k=0;k<themeTypeList.size();k++){
							Object[] obj = themeTypeList.get(k);
							ThemeType themeType = (ThemeType)obj[0];
							Integer themeNum = (Integer)obj[1];
							if("5".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							}else if("10".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							//}else if("15".equals(themeType.getThemeType())){
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							//}else if("20".equals(themeType.getThemeType())){
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							}else if("25".equals(themeType.getThemeType())){
								choutiParam+="{"+themeType.getThemeTypeId()+",null,10,"+themeNum+",0},";
							}else{
								//choutiParam+="{"+themeType.getThemeTypeId()+",null,10,20,0},";
							}
							
					}
				}
				//if(!"".equals(choutiParam))choutiParam = choutiParam.substring(0,choutiParam.length()-1);
			}
			///************抽题方式    设置   结束**************************/
				
			int examProperty = 40;//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试	
			String[] themeBankIds = null;
			if(themeBankId.indexOf(",")!=-1){
				themeBankIds = themeBankId.split(",");
			}else{
				themeBankIds = new String[]{themeBankId};
			}
			String  examUserTestpaperId = addOrGetExamPaperByUserMoni(examTitle, relationId, relationType,
					employeeId, themeBankIds, null, null, employeeName, true, 
					choutiParam,examProperty,"onlyNotUserUse",perv_testpaper);
			id = examUserTestpaperId;
				
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.findDataByKey(id, ExamUserTestpaper.class);
			if(examUserTestpaper!=null){
				examUserTestpaper.setSubTime(null);
				examUserTestpaper.setFristScote(null);
				examUserTestpaper.setScote(null);
				examUserTestpaper.setOutTime(null);
				examUserTestpaper.getExamTestpaper().setInTime(nowTime);
				examUserTestpaper.getExamTestpaper().setOutTime(null);
				examUserTestpaper.getExamTestpaper().setSubTime(null);
				examUserTestpaper.setInTime(nowTime);
				this.saveOld(examUserTestpaper);
			}
			
		}
		return id;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取模拟试卷,没有则创建
	 * @param examTitle
	 * @param relationId
	 * @param relationType
	 * @param employeeId
	 * @param themeBankIdArr
	 * @param examStartTime
	 * @param examEndTime
	 * @param createdBy
	 * @param isCreate
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private String addOrGetExamPaperByUserMoni(String examTitle,String relationId,String relationType,String employeeId,
			String[] themeBankIdArr, String examStartTime,String examEndTime,
			String createdBy,boolean isCreate,String choutiParam,int examProperty,String choutiType,Testpaper perv_testpaper)throws Exception{
		String  examUserTestpaperId = null;
		try{
			if(isCreate){
				String[] ids =  addUserMoniExam(examTitle,relationId, relationType,
						new String[]{employeeId}, themeBankIdArr,examStartTime, examEndTime, 
						createdBy,choutiParam,examProperty,choutiType,perv_testpaper);
				if(ids!=null){
					examUserTestpaperId = ids[0];
				}
			}else{
				ExamUserTestpaper examUserTestpaper = null;
				Map term = new HashMap();
				term.put("relationId", relationId);
				term.put("relationType", relationType);
				term.put("employeeId", employeeId);
				List<ExamUserTestpaper> list = this.queryData("queryExamUserTestpaperInRelationHql", term, null);
				if(list!=null && list.size()>0){
					examUserTestpaper = list.get(0);
					examUserTestpaperId = examUserTestpaper.getUserTestpaperId();
				}else{
					String[] ids =  addUserMoniExam(examTitle,relationId, relationType, new String[]{employeeId},
							themeBankIdArr, examStartTime, examEndTime, createdBy,choutiParam,examProperty,choutiType,
							perv_testpaper);
					if(ids!=null){
						examUserTestpaperId = ids[0];
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return examUserTestpaperId;
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
			String examStartTime,String examEndTime,String createdBy,String choutiParam,int examProperty,String choutiType,
			Testpaper perv_testpaper)throws Exception{
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
		    String initParam = choutiParam;
		   // String initParam = choutiParam==null || "".equals(choutiParam) || "null".equals(choutiParam) 
		    		//? ExamVariable.getTrainImplementExamPaperInitThemeType() : choutiParam;//Config.getPropertyValue("trainImplement_ExamPaper_Init_ThemeType");//"{0001,null,10,5,0},{0002,null,10,5,0},";
		    
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
		    
		    Testpaper testpaper = saveOrGetMoniTestpaper(examTitle,relationId, relationType,themeBankIds, 
		    		organName, organId, initParam,createdBy,testpaperType, examProperty, examTypeId,
		    		choutiType,employee.getEmployeeId(),perv_testpaper);
		    
		    ExamArrange examArrange = saveOrGetMoniExam(testpaper, examTitle, relationId, relationType, organName, 
		    		organId,examStartTime,examEndTime,createdBy,testpaperType, examProperty, examTypeId);
		    Exam exam = examArrange.getExams().get(0);
		    exam.setTestpaper(testpaper);
		    ExampaperService exampaperService = (ExampaperService)SpringContextUtil.getBean("exampaperService");
		    Map<String,SysUser> sysUserMap = exampaperService.getSysUserMap();
		    for(int i=0;i<employeeIds.length;i++){
		    	employee = (Employee)this.findDataByKey(employeeIds[i], Employee.class);
			    ExamUserTestpaper examUserTestpaper = saveOrGetMoniUserExamTestpaper(exam, testpaper, 
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
			int examProperty,String examTypeId,String choutiType,String employeeId,Testpaper perv_testpaper)throws Exception{
		choutiType = null;
		int sortIndex = 1;
		String relationId_new = relationId;
		if("mocs".equals(relationType)){
			
		}else{
			relationId_new = relationId+"@"+sortIndex;
			if(perv_testpaper!=null){
				String[] tmp = perv_testpaper.getRelationId().split("@");
				if(tmp.length>1){
					sortIndex = (Integer.parseInt(tmp[1])+1);
					relationId_new = relationId+"@"+sortIndex;
				}
			}
		}
		
		TestpaperService testpaperService = (TestpaperService)SpringContextUtil.getBean("testpaperService");
		Testpaper testpaper = null;
		Map term = new HashMap();
		term.put("relationId", relationId_new);//题库编码
		term.put("relationType", relationType);
		List<Testpaper> list = this.queryData("queryTestpaperInRelationHql", term, null);
		if((initParam==null || "".equals(initParam) && (list==null || list.size()==0))){
			if("mocs".equals(relationType)){
				relationId_new = relationId;
				term.put("relationId", relationId_new);//题库编码
				term.put("relationType", relationType);
				list = this.queryData("queryTestpaperInRelationHql", term, null);
			}else{
				relationId_new = relationId+"@1";
				term.put("relationId", relationId_new);//题库编码
				term.put("relationType", relationType);
				list = this.queryData("queryTestpaperInRelationHql", term, null);
			}
			
		}
		if(list!=null && list.size()>0){
			testpaper = list.get(0);
		}else{
			testpaper = new Testpaper();
			testpaper.setRelationId(relationId_new);
			testpaper.setRelationType(relationType);
			testpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			testpaper.setCreatedBy(createdBy);//创建人
			testpaper.setOrganId(organId);//机构ID
			testpaper.setOrgan(organName);//机构名称
			testpaper.setUseNum(0);
			testpaper.setIsPrivate("5");//是否私有(5：否,10：是)
			String examTitle_new = null;
			if("mocs".equals(relationType)){
				examTitle_new="模拟测试";
			}else{
				/*ThemeBank themeBank = (ThemeBank)this.findDataByKey(relationId, ThemeBank.class);
				if(themeBank!=null){
					examTitle_new = examTitle.replaceAll(createdBy, themeBank.getThemeBankName().replaceAll("("+relationId+")", ""))+"-"+sortIndex;
					examTitle_new = examTitle_new.replaceAll("()", "");
				}else{
					examTitle_new = examTitle.replaceAll(createdBy+"-", "")+"-"+sortIndex;
				}*/
				examTitle_new = examTitle+"-"+sortIndex;
			}
			testpaper.setTestpaperName(examTitle_new);//试卷名称
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
					themeBankIds,examProperty,choutiType,employeeId,relationType);
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
}
