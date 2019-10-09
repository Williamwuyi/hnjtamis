package cn.com.ite.hnjtamis.exam.study;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInService;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.ThemeService;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeFkauditForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperHis;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyTestpaperThemeSign;
import cn.com.ite.hnjtamis.exam.hibernatemap.StudyUserAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeFkaudit;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.study.jsonFormat.StudyTestpaperJsonForm;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.study.StudyThemeListAction</p>
 * <p>Description 在线学习试题</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2017</p>
 * @create time: 2017年9月1日 下午2:20:32
 * @version 1.0
 * 
 * @modified records:
 */
public class StudyThemeListAction extends AbstractListAction implements  ServletRequestAware,ServletResponseAware{

	private static final long serialVersionUID = 4540792403418541024L;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private String relationId; 
	private String relationType; 
	private String rsContent;
	private String userContent;
	private String requestHttpUrl;
	private String no;
	private String ans;
	private String submitType;
	private String opty;
	private Theme fktheme;
	private int themeCount = 0;//总题数
	private double themeFraction = 0.0;//总分数
	private int userRightThemeCount = 0;//考生正确题数
	private double userRightFraction = 0.0;//考生正确总分数
	private String pageIndexValue;//分页信息

	private List<ThemeFkauditForm> themeFkauditFormList;
	
	//private int signIncount;
	
	//private List themelist = null;
	 
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	@Override
	public void setServletResponse(HttpServletResponse httpservletresponse) {
		this.response = httpservletresponse;
	}

	public String studyOnlineExam()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "initStudyOnlineExam";
	 	}
		String themeBankId = request.getParameter("themeBankId");
		String ajaxType = request.getParameter("ajaxType");
		String examTitle = request.getParameter("examTitle");
		relationId = request.getParameter("relationId"); 
		relationType = request.getParameter("relationType"); 
		String examName = request.getParameter("examName"); 
		String ny = request.getParameter("ny");
		
		String employeeId = usersess.getEmployeeId();
		String employeeName = usersess.getEmployeeName();
		relationId = themeBankId;
		
		StudyThemeService studyThemeService = (StudyThemeService)this.getService();
		this.setId(studyThemeService.addOrGetExamStudyTestpaper(relationId,relationType,employeeId,
			employeeName, examTitle));
		studyThemeService.addThemeInStudyTestpaper(this.getId(), relationId, relationType, employeeId, employeeName);
		
		if(examName==null || "".equals(examName) || "null".equals(examName)){
			examName = StaticVariable.banksNameMap.get(themeBankId);
		}
		request.setAttribute("id", this.getId());
		request.setAttribute("examName", examName);
		request.setAttribute("relationId", relationId);
		request.setAttribute("relationType", relationType);
		request.setAttribute("ny", ny);
		
		if("init".equals(ajaxType)){
			return "initStudyOnlineExam";
		}else{
			
			if(usersess == null){
				requestHttpUrl = ExamVariable.getRequestUrl(request);
				request.setAttribute("requestHttpUrl", requestHttpUrl);
		 	}else{
		 		requestHttpUrl = ExamVariable.getRequestUrl(request);
				request.setAttribute("requestHttpUrl", requestHttpUrl);
		 	}
			request.setAttribute("themeBankId", themeBankId);
			BaseSignInService baseSignInService = (BaseSignInService)SpringContextUtil.getBean("baseSignInService");
			request.setAttribute("signIncount", baseSignInService.getSignInCount(
					usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId(), usersess.getAccount()));
 
			Object[] v = studyThemeService.getStudyTestpaperThemeCount(this.getId(),employeeId);
			themeCount = (Integer)v[0];
			themeFraction = (Double)v[1];//总分数
			userRightThemeCount = (Integer)v[2];//考生正确题数
			userRightFraction = (Double)v[3];//考生正确总分数
			request.setAttribute("themeCount", themeCount);
			request.setAttribute("themeFraction", themeFraction);
			request.setAttribute("userRightThemeCount", userRightThemeCount);
			request.setAttribute("userRightFraction", userRightFraction);
			return "studyOnlineExam";
		}
	}

	/**
	 *
	 * @author zhujian
	 * @description 获取考试题目
	 * @return
	 * @modified
	 */
	public String gettitle(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			rsContent="{code:'00003',message:'没有找到用户信息！'}";
			return "rsContent";
	 	}
		StudyThemeService studyThemeService = (StudyThemeService)this.getService();
		String employeeId = usersess.getEmployeeId();
		//rsContent = MoniExamControlMain.getMoniExamTitleInFile(request, this.getId());//先从预文件里面读取
		//if(rsContent == null){
			StudyTestpaper studyTestpaper = (StudyTestpaper)this.getService().findDataByKey(this.getId(), StudyTestpaper.class);
			if(studyTestpaper!=null){
				Object[] v = studyThemeService.getStudyTestpaperThemeCount(this.getId(),employeeId);
				themeCount = (Integer)v[0];
				themeFraction = (Double)v[1];//总分数
				userRightThemeCount = (Integer)v[2];//考生正确题数
				userRightFraction = (Double)v[3];//考生正确总分数
				
				Map term = new HashMap();
				term.put("testpaperId", studyTestpaper.getStudyTestpaperId());
				List<ThemeType> themeTypeList = this.getService().queryData("queryTypeInStudypaperHql", term, new HashMap());
				String[] pageTerm = null;
				if(this.getPageIndexValue()!=null
						&& !"".equals(this.getPageIndexValue())
						&& !"null".equals(this.getPageIndexValue())
						&& this.getPageIndexValue().length()>0){
					pageTerm = this.getPageIndexValue().split(",");
				}
				List<StudyTestpaperTheme> examTestpaperThemelist = 
						studyThemeService.getStudyTestpaperThemeList(studyTestpaper.getStudyTestpaperId(),
								pageTerm!=null?new Integer(pageTerm[1]):null,
								pageTerm!=null?new Integer(pageTerm[2]):null
								);//this.getService().queryData("queryStudyTestpaperThemeByTestpaperIdHql", term, new HashMap());
				Map<String,List<StudyTestpaperAnswerkey>> themeAnsMap = studyThemeService.getStudyTestpaperAnswerkeyByTestpaperId(studyTestpaper.getStudyTestpaperId());
				
				
				Map<String,StudyTestpaperThemeSign> signThemeMap = new HashMap<String,StudyTestpaperThemeSign>();//标记试题
				Map term2 = new HashMap();
				term2.put("employeeId", usersess.getEmployeeId());
				List<StudyTestpaperThemeSign> signObjectlist  = this.getService().queryData("queryStudyTestpaperThemeSignInEmployeeIdHql", term2, new HashMap());
				for(int i=0;i<signObjectlist.size();i++){
					StudyTestpaperThemeSign studyTestpaperThemeSign = signObjectlist.get(i);
					if("10".equals(studyTestpaperThemeSign.getSignInTheme())){
						signThemeMap.put(studyTestpaperThemeSign.getStudyTestpaperTheme().getStudyTestpaperThemeId(), studyTestpaperThemeSign);
					}
				}
				
				String examTestpaperStr = "{code:'00001',message:'返回成功！',"
						+ "themeCount:"+themeCount+",themeFraction:"+themeFraction+","
						+ "userRightThemeCount:"+userRightThemeCount+",userRightFraction:"+userRightFraction+",result:";
				examTestpaperStr+=StudyTestpaperJsonForm.getJsonStrInStudyTestpaper(studyTestpaper, 
						themeTypeList,examTestpaperThemelist,themeAnsMap,signThemeMap);
				examTestpaperStr+="}";
				rsContent = examTestpaperStr;
				//MoniExamControlMain.saveMoniExamTitleToFile(request, this.getId(), examTestpaperStr);
				
				
				Map<String,List<StudyUserAnswerkey>> userAnsMap = new HashMap<String,List<StudyUserAnswerkey>>();
				//term.put("employeeId", usersess.getEmployeeId());
				List<StudyUserAnswerkey> ansList2 = studyThemeService.getUserAnswerkeyList(
						studyTestpaper.getStudyTestpaperId(), usersess.getEmployeeId());
						//this.getService().queryData("queryStudyUserAnswerkeyInTestpaperIdHql", term , null);
				if(ansList2!=null){
					for(int i=0;i<ansList2.size();i++){
						StudyUserAnswerkey studyUserAnswerkey = (StudyUserAnswerkey)ansList2.get(i);
						String ansKey = studyUserAnswerkey.getStudyTestpaperThemeId();
						List<StudyUserAnswerkey> tmplist = userAnsMap.get(ansKey);
						if(tmplist==null)tmplist = new ArrayList<StudyUserAnswerkey>();
						boolean isAdd = true;
						for(int k=0;k<tmplist.size();k++){
							if(studyUserAnswerkey.getAnswerkeyValue()!=null 
									&& studyUserAnswerkey.getAnswerkeyValue().equals(tmplist.get(k).getAnswerkeyValue())){
								isAdd = false;
								break;
							}
						}
						if(isAdd){
							tmplist.add(studyUserAnswerkey);
						}
						userAnsMap.put(ansKey,tmplist);
					}
				}
				
				String ans = "";
				String no = "";
				for(int i=0;i<examTestpaperThemelist.size();i++){
					StudyTestpaperTheme studyTestpaperTheme = examTestpaperThemelist.get(i);
					List<StudyUserAnswerkey> ansList = userAnsMap.get(studyTestpaperTheme.getStudyTestpaperThemeId());
					if(ansList!=null && ansList.size()>0){
						String ansStr = "";
						ThemeType themeType = null;
						for(int j=0;j<themeTypeList.size();j++){
							ThemeType _themeType = themeTypeList.get(j);
							if(_themeType.getThemeTypeId().equals(studyTestpaperTheme.getThemeTypeId())){
								themeType = _themeType;
								break;
							}
						}
						String type = themeType!=null ? themeType.getThemeType() : null;
						if("5".equals(type) || "25".equals(type)){
							if(ansList.size()>1){
								String recodeDate = null;
								for(int j=0;j<ansList.size();j++){
									try{
										if(recodeDate==null || (ansList.get(j).getCreationDate()!=null && 
												new Date(recodeDate).getTime()> new Date(ansList.get(j).getCreationDate()).getTime())){
											ansStr=ansList.get(j).getAnswerkeyValue()+"#*#";
											recodeDate = ansList.get(j).getCreationDate();
										}
									}catch(Exception e){
										recodeDate = ansList.get(j).getCreationDate();
									}
								}
							}else{
								for(int j=0;j<ansList.size();j++){
									ansStr+=ansList.get(j).getAnswerkeyValue()+"#*#";
								}
							}
						}else{
							for(int j=0;j<ansList.size();j++){
								//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
								if("5".equals(type) || "25".equals(type)){
									ansStr+=ansList.get(j).getAnswerkeyValue()+"#*#";
								}else  if("5".equals(type) ||  "10".equals(type) || "25".equals(type) ){
									ansStr+=ansList.get(j).getAnswerkeyValue()+"#*#";
								}else{
									ansStr+=CharsetSwitchUtil.encode(ansList.get(j).getAnswerkeyValue())+"#*#";
								}
							}
						}
						
						if(ansStr.length()>0){
							ansStr = ansStr.substring(0,ansStr.length()-3);
						}else{
							ansStr = "null";
						}
						ans+=ansStr+"@$@";
						no+=(studyTestpaperTheme.getThemeIndex())+"@$@";
					}
				}
				userContent="{code:'00001',message:'返回成功！',result:{ans:'"+ans+"',no:'"+no+"'}}";
			}else{
				rsContent="{code:'00003',message:'未检索到试卷！'}";
			}
		//}
		return "rsContent";
	}
	

	/**
	 *
	 * @author zhujian
	 * @description 保存考试答案
	 * @return
	 * @modified
	 */
	public String saveans(){
		rsContent = null;
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		try{
			if(no == null || ans == null || "".equals(no)|| "".equals(ans)){
				rsContent="{code:'00005',message:'保存答案失败，没有找到要保存的答案！'}";
				
			}else{
				String nowTime = DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss");
				//获取考生写的答案
				Map<String,String> ansMap = new HashMap<String,String>();
				String[] noArr = no.split("@\\$@");
				String[] ansArr = ans.split("@\\$@");
				for(int i=0;i<noArr.length;i++){
					String themeId = noArr[i];
					ansMap.put(themeId, ansArr[i]);
				}
				
				StudyThemeService studyThemeService = (StudyThemeService)this.getService();
				Map term = new HashMap();
				term.put("testpaperId", this.getId());
				term.put("employeeId", usersess.getEmployeeId());
				
				
				//获取本试卷的试题
				List<StudyTestpaperTheme> examTestpaperThemelist = this.getService().queryData("queryStudyTestpaperThemeByTestpaperIdHql", term, new HashMap());
				//获取本试卷的试题对应的答案
				Map<String,List<StudyTestpaperAnswerkey>> themeAnsMap = studyThemeService.getStudyTestpaperAnswerkeyByTestpaperId(this.getId());
				
				//获取数据库中的试题
				Map<String,List<StudyUserAnswerkey>> userAnsMap = new HashMap<String,List<StudyUserAnswerkey>>();
				List<StudyUserAnswerkey> ansList2 = this.getService().queryData("queryStudyUserAnswerkeyInTestpaperIdHql", term , null);
				String batchNo = null;
				if(ansList2!=null){
					for(int i=0;i<ansList2.size();i++){
						StudyUserAnswerkey studyUserAnswerkey = (StudyUserAnswerkey)ansList2.get(i);
						batchNo = studyUserAnswerkey.getBatchNo();
						String ansKey = studyUserAnswerkey.getStudyTestpaperThemeId();
						List<StudyUserAnswerkey> tmplist = userAnsMap.get(ansKey);
						if(tmplist==null)tmplist = new ArrayList<StudyUserAnswerkey>();
						tmplist.add(studyUserAnswerkey);
						userAnsMap.put(ansKey,tmplist);
					}
				}
				if(batchNo == null){
					batchNo = DateUtils.convertDateToStr(new Date(), "yyyyMMddHHmmssSS");
				}
				if(batchNo.length()==16){
					batchNo+="0";
				}else if(batchNo.length()==15){
					batchNo+="00";
				}else if(batchNo.length()==14){
					batchNo+="000";
				}
				
				//获取试卷有的题型
				List<ThemeType> themeTypeList = this.getService().queryData("queryTypeInStudypaperHql", term, new HashMap());
				
				
				
				List<StudyUserAnswerkey> delList = new ArrayList<StudyUserAnswerkey>();//删除
				List<StudyUserAnswerkey> addList = new ArrayList<StudyUserAnswerkey>();//添加
				List<StudyUserAnswerkey> saveList = new ArrayList<StudyUserAnswerkey>();//保存
				double sumscore = 0.0d;//默认总分
				double score = 0.0d;//实际得分
				int total = 0;//题目数量
				int rightTotal = 0;//正确数量
				for(int i=0;i<examTestpaperThemelist.size();i++){
					StudyTestpaperTheme studyTestpaperTheme = examTestpaperThemelist.get(i);
					total++;
					ThemeType themeType = null;
					for(int j=0;j<themeTypeList.size();j++){
						ThemeType _themeType = themeTypeList.get(j);
						if(_themeType.getThemeTypeId().equals(studyTestpaperTheme.getThemeTypeId())){
							themeType = _themeType;
							break;
						}
					}
					sumscore += studyTestpaperTheme.getDefaultScore()!=null ?studyTestpaperTheme.getDefaultScore().doubleValue() : 0.0d;
					List<StudyUserAnswerkey> oldAnsList = userAnsMap.get(studyTestpaperTheme.getStudyTestpaperThemeId());
					if(oldAnsList == null) oldAnsList = new ArrayList<StudyUserAnswerkey>();
					String ansStr = ansMap.get(studyTestpaperTheme.getStudyTestpaperThemeId());
					List<StudyUserAnswerkey> oneThemeUserAnslist = oldAnsList;
					boolean isAdd = false;
					if(ansStr!=null){
						oneThemeUserAnslist = new ArrayList<StudyUserAnswerkey>();
						delList.addAll(oldAnsList);
						if(!"".equals(ansStr)){
							if("null".equals(ansStr)){
								
							}else{
								String[] themeAnsArr = ansStr.split("#\\*#");
								String type = themeType!=null ? themeType.getThemeType() : null;
								for(int k=0;k<themeAnsArr.length;k++){
									StudyUserAnswerkey studyUserAnswerkey = new StudyUserAnswerkey();
									studyUserAnswerkey.setStudyTestpaperThemeId(studyTestpaperTheme.getStudyTestpaperThemeId());//学习试卷试题ID
									studyUserAnswerkey.setStudyTestpaperId(this.getId());//学习试卷ID
									studyUserAnswerkey.setThemeId(studyTestpaperTheme.getThemeId());//试题ID(试题库)
									if("5".equals(type) || "10".equals(type) || "25".equals(type)){
										studyUserAnswerkey.setAnswerkeyValue(themeAnsArr[k]);
									}else{
										studyUserAnswerkey.setAnswerkeyValue(CharsetSwitchUtil.decode(themeAnsArr[k]));
									}
					    			//System.out.println(examUserAnswerkey.getAnswerkeyValue());
									studyUserAnswerkey.setSortNum(k);
									if("submit".equals(submitType) || "saveAndNew".equals(submitType)){//是否提交 10-未提交 20-提交
										studyUserAnswerkey.setState("20");
										studyUserAnswerkey.setSubTime(nowTime);
										studyUserAnswerkey.setOutTime(nowTime);
									}else{
										studyUserAnswerkey.setState("10");
									}
									studyUserAnswerkey.setBatchNo(batchNo);
									studyUserAnswerkey.setCreatedIdBy(usersess.getEmployeeId());
									studyUserAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
									studyUserAnswerkey.setOrganId(usersess!=null?usersess.getOrganId():null);
									studyUserAnswerkey.setOrganName(usersess!=null?usersess.getOrganName():null);
									studyUserAnswerkey.setThemeTypeId(studyTestpaperTheme.getThemeTypeId());
									studyUserAnswerkey.setThemeTypeName(studyTestpaperTheme.getThemeTypeName());
									studyUserAnswerkey.setRelationId(studyTestpaperTheme.getRelationId());
									studyUserAnswerkey.setRelationType(studyTestpaperTheme.getRelationType());
									oneThemeUserAnslist.add(studyUserAnswerkey);
									isAdd = true;
								}
							}
						}
					}else if("submit".equals(submitType) || "saveAndNew".equals(submitType)){
						for(int k=0;k<oldAnsList.size();k++){
							StudyUserAnswerkey studyUserAnswerkey = oldAnsList.get(k);
							studyUserAnswerkey.setState("20");
							studyUserAnswerkey.setSubTime(nowTime);
							studyUserAnswerkey.setOutTime(nowTime);
							studyUserAnswerkey.setCreatedIdBy(usersess.getEmployeeId());
							studyUserAnswerkey.setThemeTypeId(studyTestpaperTheme.getThemeTypeId());
							studyUserAnswerkey.setThemeTypeName(studyTestpaperTheme.getThemeTypeName());
							studyUserAnswerkey.setRelationId(studyTestpaperTheme.getRelationId());
							studyUserAnswerkey.setRelationType(studyTestpaperTheme.getRelationType());
							saveList.add(studyUserAnswerkey);
						}
					}
					
					if(oneThemeUserAnslist == null || oneThemeUserAnslist.size()==0){
						score+=0.0d;
			    	}else{
			    		List<StudyTestpaperAnswerkey> testpaperAnswerkeies = themeAnsMap.get(studyTestpaperTheme.getStudyTestpaperThemeId());
			    		String rightAns = "##";
				    	String userAns = "##";
				    	for(int t = 0;t<testpaperAnswerkeies.size();t++){
				    		StudyTestpaperAnswerkey ans = (StudyTestpaperAnswerkey)testpaperAnswerkeies.get(t);
				    	    if(ans.getIsRight()!=null && ans.getIsRight().intValue() == 10){//是否正确 5：否,10：是
				    	    	if("5".equals(themeType.getThemeType()) || "10".equals(themeType.getThemeType()) || "25".equals(themeType.getThemeType())){
				    	    		rightAns += StaticVariable.themeAnsSort[t]+"##";
				    	    	}else{
				    	    		rightAns += ans.getAnswerkeyValue()+"##";
				    	    	}
				    	    }
				    	}
				    	for(int t = 0;t<oneThemeUserAnslist.size();t++){
				    		StudyUserAnswerkey ans = (StudyUserAnswerkey)oneThemeUserAnslist.get(t);
				    		ans.setScore(0.0d);
				    		userAns += ans.getAnswerkeyValue()+"##";
				    	}
				    	if(rightAns.equals(userAns)){
				    		score+=studyTestpaperTheme.getDefaultScore();
				    		for(int t = 0;t<oneThemeUserAnslist.size();t++){
					    		StudyUserAnswerkey ans = (StudyUserAnswerkey)oneThemeUserAnslist.get(t);
					    		ans.setScore(studyTestpaperTheme.getDefaultScore());
					    	}
				    		rightTotal++;
				    	}else{
				    		score+=0.0d;
				    	}
			    	}
					
					if(isAdd){
						addList.addAll(oneThemeUserAnslist);
					}
					
					//移除对应主键对应的考生答案
					userAnsMap.remove(studyTestpaperTheme.getStudyTestpaperThemeId());
				}
				
				//删除数据库中有的，但是实际试题已不存在的
				if(!userAnsMap.keySet().isEmpty()){
					Iterator its = userAnsMap.keySet().iterator();
					while(its.hasNext()){
						String themeId = (String)its.next();
						delList.addAll(userAnsMap.get(themeId));
					}	
				}
				
				if(delList.size()>0) this.getService().deletes(delList);
				if(addList.size()>0) this.getService().saves(addList);
				if(saveList.size()>0) this.getService().saves(saveList);
				
				
				if("saveAndNew".equals(this.getSubmitType())){
					StudyTestpaperHis studyTestpaperHis = new StudyTestpaperHis();
					StudyTestpaper studyTestpaper = (StudyTestpaper)this.getService().findDataByKey(this.getId(), StudyTestpaper.class);
					if(studyTestpaper!=null){
						studyTestpaperHis.setTotalScore(sumscore);
						studyTestpaperHis.setTotalTheme(total);
						studyTestpaperHis.setRightScore(score);
						studyTestpaperHis.setRightTheme(rightTotal);
						if((sumscore*0.6)<(score*1.0)){
							studyTestpaperHis.setIsPass("T");
						}else{
							studyTestpaperHis.setIsPass("F");
						}
						studyTestpaperHis.setStudyTestpaperName(studyTestpaper.getStudyTestpaperName());
						studyTestpaperHis.setIsUse(10);
						studyTestpaperHis.setBatchNo(batchNo);
						studyTestpaperHis.setEmployeeId(usersess.getEmployeeId());
						studyTestpaperHis.setEmployeename(usersess.getEmployeeName());
						studyTestpaperHis.setState("20");
						studyTestpaperHis.setSubTime(nowTime);
						studyTestpaperHis.setOutTime(nowTime);
						studyTestpaperHis.setBatchNo(batchNo);
						studyTestpaperHis.setCreatedIdBy(usersess.getEmployeeId());
						studyTestpaperHis.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						studyTestpaperHis.setOrganId(usersess!=null?usersess.getOrganId():null);
						studyTestpaperHis.setOrganName(usersess!=null?usersess.getOrganName():null);
						studyTestpaperHis.setRelationId(studyTestpaper.getRelationId());
						studyTestpaperHis.setRelationType(studyTestpaper.getRelationType());
						this.getService().save(studyTestpaperHis);
					}
					studyThemeService.updateAnsHis(this.getId(), usersess.getEmployeeId());
				}
				rsContent="{code:'00001',message:'返回成功！',score:'"+score+"',sumscore:'"+sumscore+"',dfl:'"+(sumscore>0? NumericUtils.round(score/sumscore*100,2) : "0.00")+"',total:'"+total+"',rightTotal:'"+rightTotal+"',zql:'"+(total>0? NumericUtils.round(rightTotal/total*100,2) : "0.00")+"'}";
				//System.out.println(rsContent);
			}
		}catch(Exception e){
			rsContent="{code:'00005',message:'保存答案失败！'}";
			e.printStackTrace();
		}
		return "rsContent";
	}
	
	/**
	 * 试题收藏
	 * @description
	 * @return
	 * @modified
	 */
	public String signInMoniTheme(){
		rsContent = null;
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		try{
			Map term2 = new HashMap();
			term2.put("studyTestpaperThemeId", this.getId());
			term2.put("employeeId", usersess.getEmployeeId());
			List<StudyTestpaperThemeSign> signObjectlist  = this.getService().queryData("queryStudyTestpaperThemeSignInParamHql", term2, new HashMap());
			StudyTestpaperThemeSign studyTestpaperThemeSign = null;
			if(signObjectlist!=null && signObjectlist.size()>0){
				studyTestpaperThemeSign = signObjectlist.get(0);
			}
			if("add".equals(opty)){
				 if(studyTestpaperThemeSign == null){
					 StudyTestpaperTheme studyTestpaperTheme = (StudyTestpaperTheme)this.getService().findDataByKey(this.getId(), StudyTestpaperTheme.class);
					 if(studyTestpaperTheme==null){
						 rsContent="{code:'00005',message:'没有找到对应的试题！'}";
					 }else{
						 studyTestpaperThemeSign = new StudyTestpaperThemeSign();
						 studyTestpaperThemeSign.setStudyTestpaperTheme(studyTestpaperTheme);//学习试卷试题ID
						 studyTestpaperThemeSign.setThemeId(studyTestpaperTheme.getThemeId());//试题ID(试题库)
						 studyTestpaperThemeSign.setSignInTheme("10");//是否标记试题 10-是  空-否
						 studyTestpaperThemeSign.setOrganId(usersess.getOrganId());//机构编号
						 studyTestpaperThemeSign.setOrganName(usersess.getOrganName());//机构名称
						 studyTestpaperThemeSign.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));//创建时间
						 studyTestpaperThemeSign.setCreatedBy(usersess.getEmployeeName()==null?usersess.getAccount():usersess.getEmployeeName());//创建人
						 studyTestpaperThemeSign.setCreatedIdBy(usersess.getEmployeeId()==null?usersess.getAccount():usersess.getEmployeeId());//创建人ID
						 this.getService().saveOld(studyTestpaperThemeSign);
						 rsContent="{code:'00001',message:'已收藏！'}";
					 }
					 //	private String lastUpdateDate;//最后修改时间
				 }else if(studyTestpaperThemeSign!=null && "20".equals(studyTestpaperThemeSign.getSignInTheme())){
					 studyTestpaperThemeSign.setSignInTheme("10");
					 studyTestpaperThemeSign.setOrganId(usersess.getOrganId());//机构编号
					 studyTestpaperThemeSign.setOrganName(usersess.getOrganName());//机构名称
					 studyTestpaperThemeSign.setCreatedBy(usersess.getEmployeeName()==null?usersess.getAccount():usersess.getEmployeeName());//创建人
					 studyTestpaperThemeSign.setLastUpdateDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					 this.getService().saveOld(studyTestpaperThemeSign);
					 rsContent="{code:'00001',message:'已收藏！'}";
				 }else{
					 rsContent="{code:'00005',message:'已收藏，收藏失败！'}";
				 }
			} else if("del".equals(opty)){
				if(studyTestpaperThemeSign!=null && "10".equals(studyTestpaperThemeSign.getSignInTheme())){
					 studyTestpaperThemeSign.setSignInTheme("20");//是否标记试题 10-是  空-否
					 studyTestpaperThemeSign.setOrganId(usersess.getOrganId());//机构编号
					 studyTestpaperThemeSign.setOrganName(usersess.getOrganName());//机构名称
					 studyTestpaperThemeSign.setCreatedBy(usersess.getEmployeeName()==null?usersess.getAccount():usersess.getEmployeeName());//创建人
					 studyTestpaperThemeSign.setLastUpdateDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					 this.getService().saveOld(studyTestpaperThemeSign);
					 rsContent="{code:'00001',message:'取消收藏成功！'}";
				 }else{
					 rsContent="{code:'00005',message:'未收藏，取消失败！'}";
				 } 
			}else{
				rsContent="{code:'00005',message:'处理失败！'}";
			} 
		}catch(Exception e){
			rsContent="{code:'00005',message:'处理失败！'}";
			e.printStackTrace();
		}
		return "rsContent";
	}
	
	
	/**
	 * 反馈试题显示
	 * @description
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String fkThemeInExamThemeShow()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "fkThemeShow";
	 	}
		request.setAttribute("id", this.getId());
		StudyTestpaperTheme studyTestpaperTheme = (StudyTestpaperTheme)this.getService().findDataByKey(this.getId(), StudyTestpaperTheme.class);
		if(studyTestpaperTheme!=null){
			fktheme = (Theme)this.getService().findDataByKey(studyTestpaperTheme.getThemeId(), Theme.class);
			themeFkauditFormList = new ArrayList<ThemeFkauditForm>();
			if(fktheme==null){
				this.setMsg("没有找到对应的试题！");
			}else if(fktheme.getLastFkState()!=null && !"10".equals(fktheme.getLastFkState())){
				this.setMsg("该试题已经进行了反馈！");
				ThemeService themeService = (ThemeService)SpringContextUtil.getBean("themeService");
				Map term = new HashMap();
				term.put("themeId", fktheme.getThemeId());
				term.put("state", "10,20");
				List<ThemeFkaudit> fkAuditlist = themeService.queryData("queryThemeFkauditHql", term, null);
				for(int i=0;i<fkAuditlist.size();i++){
					ThemeFkaudit themeFkaudit = fkAuditlist.get(i);
					ThemeFkauditForm themeFkauditForm = new ThemeFkauditForm();
					BeanUtils.copyProperties(themeFkaudit,themeFkauditForm);
					themeFkauditFormList.add(themeFkauditForm);
				}
			}else{
				fktheme.setLastFkState("10");
			}
			request.setAttribute("fktheme", fktheme);
		}else{
			this.setMsg("没有找到对应的试题！");
		}
		return "fkThemeShow";
	}
	
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getRsContent() {
		return rsContent;
	}
	public void setRsContent(String rsContent) {
		this.rsContent = rsContent;
	}
	public String getUserContent() {
		return userContent;
	}
	public void setUserContent(String userContent) {
		this.userContent = userContent;
	}
	public String getRequestHttpUrl() {
		return requestHttpUrl;
	}
	public void setRequestHttpUrl(String requestHttpUrl) {
		this.requestHttpUrl = requestHttpUrl;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getAns() {
		return ans;
	}
	public void setAns(String ans) {
		this.ans = ans;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getOpty() {
		return opty;
	}
	public void setOpty(String opty) {
		this.opty = opty;
	}
	public Theme getFktheme() {
		return fktheme;
	}
	public void setFktheme(Theme fktheme) {
		this.fktheme = fktheme;
	}
	public List<ThemeFkauditForm> getThemeFkauditFormList() {
		return themeFkauditFormList;
	}
	public void setThemeFkauditFormList(List<ThemeFkauditForm> themeFkauditFormList) {
		this.themeFkauditFormList = themeFkauditFormList;
	}
	public int getThemeCount() {
		return themeCount;
	}
	public void setThemeCount(int themeCount) {
		this.themeCount = themeCount;
	}
	
	public String getPageIndexValue() {
		return pageIndexValue;
	}
	public void setPageIndexValue(String pageIndexValue) {
		this.pageIndexValue = pageIndexValue;
	}
	public double getThemeFraction() {
		return themeFraction;
	}
	public void setThemeFraction(double themeFraction) {
		this.themeFraction = themeFraction;
	}
	public int getUserRightThemeCount() {
		return userRightThemeCount;
	}
	public void setUserRightThemeCount(int userRightThemeCount) {
		this.userRightThemeCount = userRightThemeCount;
	}
	public double getUserRightFraction() {
		return userRightFraction;
	}
	public void setUserRightFraction(double userRightFraction) {
		this.userRightFraction = userRightFraction;
	}
	
	
}
