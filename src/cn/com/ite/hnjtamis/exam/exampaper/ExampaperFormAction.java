package cn.com.ite.hnjtamis.exam.exampaper;


import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;




import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.MakePwd;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;
import cn.com.ite.eap2.domain.baseinfo.SysAfficheIncepter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamArrangeForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamTestpaperForm;
import cn.com.ite.hnjtamis.exam.exampaper.form.ExamUserForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperAnswerJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperJsonForm;
import cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamUserJsonForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamArrange;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.ExampaperFormAction</p>
 * <p>Description 考试安排与试卷生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月7日 下午2:55:36
 * @version 1.0
 * 
 * @modified records:
 */
public class ExampaperFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = 7081408055297084979L;

	private HttpServletRequest request;
	
	private ExamArrangeForm form;
	
	private ExamForm examForm;
	
	private String testpaperId;
	
	private String examType;
	
	private String examIds;
	
	private String op;
	
	private ExamThreadService examThreadService;
	

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	
	/**
	 * 创建个人试题
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String createUserExam(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			ExampaperService exampaperService = (ExampaperService)this.getService();
			if("30".equals(examType)){
				Testpaper testpaper = (Testpaper)this.getService().findDataByKey(testpaperId, Testpaper.class);
				if(testpaper!=null){
					TrainImplementMoniService trainImplementMoniService = (TrainImplementMoniService)SpringContextUtil.getBean("trainImplementMoniService");
					String[] ids = trainImplementMoniService.addOrgetTrainImplementExamPaperByImplId(testpaper.getRelationId(),
							testpaper.getRelationType(), new String[]{usersess.getEmployeeId()},null,null,usersess.getEmployeeName(),null);
					if(ids!=null && ids.length>0){
						for(int i=0;i<ids.length;i++){
							System.out.println(" 模拟试题ID "+i+"  = "+ids[i]);
						}
					}
					this.setMsg("处理成功！");
				}else{
					this.setMsg("没有找到对应的试卷！");
				}
			}else if("40".equals(examType)){
				Testpaper testpaper = (Testpaper)this.getService().findDataByKey(testpaperId, Testpaper.class);
				if(testpaper!=null){
					OnlineMoniService onlineMoniService = (OnlineMoniService)SpringContextUtil.getBean("onlineMoniService");
					String[] ids = onlineMoniService.addExamPaperByRelationId( testpaper.getTestpaperName(), 
							testpaperId, testpaper.getExamProperty(),
							testpaperId,"userExam",new String[]{usersess.getEmployeeId()},
							null,null,usersess.getEmployeeName(),null);
							
					if(ids!=null && ids.length>0){
						for(int i=0;i<ids.length;i++){
							System.out.println(" 创建试题："+i+"  = "+ids[i]);
						}
					}
					this.setMsg("处理成功！");
				}else{
					this.setMsg("没有找到对应的试卷！");
				}
			}else{
				this.setMsg("设置的examType没有对于配置！");
			}
			
			//exampaperService.saveUseTestpaperNum();
			
			exampaperService.initUserInticketMap();
		}catch(Exception e){
			e.printStackTrace();
			this.setMsg("处理失败！");
		}
		return "save";
	}
	
	/**
	 * 查询
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String findExamUser()throws Exception{
		Exam exam = this.getId()!=null && !"".equals(this.getId()) 
				&& !"null".equals(this.getId()) ? (Exam) service.findDataByKey(this.getId(), Exam.class) : null;
		if(exam!=null){
			examForm = new ExamForm();
			examForm.setExamId(exam.getExamId());
			
			List<ExamUserTestpaper> examUserTestpaperslist = exam.getExamUserTestpapers();
			if(examUserTestpaperslist!=null){
				for(int i = 0 ;i<examUserTestpaperslist.size();i++){
					ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)examUserTestpaperslist.get(i);
					if(examUserTestpaper.getExamPublicUser()!=null){
						ExamUserForm examUserForm = new ExamUserForm();
						examUserForm.setUserId(examUserTestpaper.getExamPublicUser().getUserId());
						examUserForm.setUserName(examUserTestpaper.getExamPublicUser().getUserName());
						examForm.getExamUserFormList().add(examUserForm);
					}
				}
			}
		}
		return "findExamUser";
	}
	
	
	
	/**
	 * 保存考生信息
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String saveExamUser()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			ExampaperService exampaperService=(ExampaperService)this.getService();
			
			examForm = (ExamForm) this.jsonToObject(ExamForm.class);
			Exam exam = examForm.getExamId()!=null && !"".equals(examForm.getExamId()) 
					&& !"null".equals(examForm.getExamId()) ? (Exam) service.findDataByKey(examForm.getExamId(), Exam.class) : null;
			if(exam!=null){
				List<ExamUserTestpaper> addExamUserTestpaperList = exampaperService.saveAndCleanExamUserFormList(exam, examForm.getExamUserFormList(), usersess,request,true);
				/**
				 * 删除没有分配考生科目的考生试卷
				 * @author zhujian
				 * @description
				 * @modified
				 */
				//exampaperService.deleteNullTestpaperInExamUserTestpaper(exam.getExamId());
				//exampaperService.saveExamTestPaperInExamInThread(request, exam);
				//exampaperService.saveExamTestPaperInExam(request,exam);
				//ExamUserJsonForm.getAndSaveExamUser(request, exam);
				if(addExamUserTestpaperList!=null && addExamUserTestpaperList.size()>0){
					Map term2 = new HashMap();
					term2.put("examId", examForm.getExamId());
					Map<String,SysAfficheIncepter> sysAfficheIncepterMap = new HashMap<String,SysAfficheIncepter>();
					List<SysAfficheIncepter> sysAfficheIncepterlist = exampaperService.queryData("querySysAfficheIncepterInExamId", term2, null);
					for(int i=0;i<sysAfficheIncepterlist.size();i++){
						SysAfficheIncepter sysAfficheIncepter = sysAfficheIncepterlist.get(i);
						sysAfficheIncepterMap.put(sysAfficheIncepter.getIncepterId(), sysAfficheIncepter);
					}
					
					List<SysAffiche> sysAffichelist = exampaperService.queryData("querySysAfficheInExamId", term2, null);
					SysAffiche sysAffiche= sysAffichelist.size()>0 ? sysAffichelist.get(0) : null;
					
					List<ThemeType> themeTypeList = null;
					List addsysAfficheIncepterList = new ArrayList();
					for(int i=0;i<addExamUserTestpaperList.size();i++){
						ExamUserTestpaper examUserTestpaper = addExamUserTestpaperList.get(i);
						try{
							ExamTestpaper examTestpaper = exampaperService.saveAndGetNewExamTestPaper(exam,examUserTestpaper,exam.getTestpaper());
							examUserTestpaper.setExamTestpaper(examTestpaper);
							examTestpaper.getExamUserTestpapers().add(examUserTestpaper);
							exampaperService.saveOld(examTestpaper);
							
							if(themeTypeList == null){
								Map<String,Object> term = new HashMap<String,Object>();
								term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
								themeTypeList = exampaperService.queryData("queryTypeInExampaperHql", term, null);
							}
							
							//保存并生成试题
							if(examUserTestpaper!=null && examTestpaper!=null &&  examTestpaper.getExamTestpaperThemes()!=null && 
									examTestpaper.getExamTestpaperThemes().size()>0 && themeTypeList!=null && themeTypeList.size()>0){
								ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examTestpaper, themeTypeList);
								if(exam.getExamArrange().getIsPublic()!=null && exam.getExamArrange().getIsPublic().intValue() == 10){
									ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examTestpaper);
								}
							}
							
							SysAfficheIncepter sysAfficheIncepter = sysAfficheIncepterMap.get(examUserTestpaper.getEmployeeId());
							if(sysAfficheIncepter!=null){
								sysAfficheIncepterMap.remove(examUserTestpaper.getEmployeeId());
							}else{
								sysAfficheIncepter = new SysAfficheIncepter();
								sysAfficheIncepter.setIncepterId(examUserTestpaper.getEmployeeId());
								sysAfficheIncepter.setIncepterName(examUserTestpaper.getEmployeeName());
								sysAfficheIncepter.setIncepterType(4);
								sysAfficheIncepter.setSortNum(new Integer(i));
								sysAfficheIncepter.setSysAffiche(sysAffiche);
								addsysAfficheIncepterList.add(sysAfficheIncepter);
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					ExamUserJsonForm.getAndSaveExamUser(request, exam);
					
					//导入试卷到IOCP
					exampaperService.loadUserExam(exam, request);
					
					
					/********* 通知添加与删除  **********/
					if(addsysAfficheIncepterList.size()>0){
						exampaperService.saves(addsysAfficheIncepterList);
					}
					if(!sysAfficheIncepterMap.keySet().isEmpty()){
						Iterator its = sysAfficheIncepterMap.keySet().iterator();
						List dellist = new ArrayList();
						while(its.hasNext()){
							String kk = (String)its.next();
							dellist.add(sysAfficheIncepterMap.get(kk));
						}
						if(dellist.size()>0){
							exampaperService.deletes(dellist);
						}
					}
					/********* 通知添加与删除   结束**********/
				}
				exampaperService.initUserInticketMap();
				
				this.setMsg("保存成功,已初始化考生试卷，请查看！");
			}else{
				this.setMsg("没有找到对应的科目！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "saveExamUser";
	}
	
	

	/**
	 * 查询
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String find(){
		form = new ExamArrangeForm();
		ExamArrange examArrange = this.getId()!=null && !"".equals(this.getId()) 
				&& !"null".equals(this.getId()) ? (ExamArrange) service.findDataByKey(this.getId(), ExamArrange.class) : null;
		if(examArrange==null && (this.getId()!=null && !"".equals(this.getId()) 
				&& !"null".equals(this.getId()))){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("examId", this.getId());
			List<ExamArrange> list = service.queryData("examArrangeInExamIdHql", paramMap, null);
			if(list!=null && list.size()>0){
				examArrange = (ExamArrange)list.get(0);
			}
		}
				
		if(examArrange!=null){
			try {
				form.setExamArrangeId(examArrange.getExamArrangeId());
				form.setExamArrangeName(examArrange.getExamName()); 
				form.setScore(examArrange.getScore());//总分
				form.setExamCode(examArrange.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）
				form.setExamTypeId(examArrange.getExamTypeId());//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
				form.setExamTypeName(examArrange.getExamTypeName());//考试类型(性质)，与考试类型ID一组
				form.setExamProperty(examArrange.getExamProperty());//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
				form.setIsPublic(examArrange.getIsPublic());//是否发布成绩 5：否，10：是
				form.setPublicUser(examArrange.getPublicUser());//成绩发布人
				form.setPublicUserId(examArrange.getPublicUserId());//成绩发布人编号
				form.setPublicTime(examArrange.getPublicTime());//成绩发布时间
				if(examArrange.getIsUse() == null){
					examArrange.setIsUse(10);
				}
				form.setIsUse(examArrange.getIsUse());///是否使用 5：否,10：是
				form.setCheckUser(examArrange.getCheckUser());//审核人
				form.setCheckUserId(examArrange.getCheckUserId());///审核人ID
				form.setCheckTime(examArrange.getCheckTime());//审核时间
				form.setState(examArrange.getState());//
				form.setExamPaperType(examArrange.getExamPaperType());//
				form.setIsIdNumberLogin(examArrange.getIsIdNumberLogin());//
				form.setRelationId(examArrange.getRelationId());//关联ID（如：练习安排等）
				form.setRelationType(examArrange.getRelationType());//关联类型
				form.setRemark(examArrange.getRemark());//备注
				form.setPublicId(examArrange.getExamPublic()!=null?examArrange.getExamPublic().getPublicId() : null);
				form.setScoreStartTime(examArrange.getScoreStartTime());
				form.setScoreEndTime(examArrange.getScoreEndTime());
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<Exam> examList = examArrange.getExams();
			if(examList!=null && examList.size()>0){
				Exam exam  = (Exam)examList.get(0);
				form.setExamPaperType(exam.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				form.setExamId(exam.getExamId());
				form.setExamStartTime(exam.getExamStartTime()!=null ? exam.getExamStartTime().replaceAll("T", " ") : null);
				form.setExamEndTime(exam.getExamEndTime()!=null ? exam.getExamEndTime().replaceAll("T", " ") : null);
				form.setBanTime(exam.getBanTime());//禁止进入时间_分钟
				form.setBeforeTime(exam.getBeforeTime());//提前进入时间_分钟
				form.setPassScore(exam.getPassScore());//合格分数线 默认按总分的60％，可进行设置。
				if(exam.getTestpaper()!=null){
					ExamTestpaperForm examTestpaperForm = new ExamTestpaperForm();
					examTestpaperForm.setTestpaperId(exam.getTestpaper().getTestpaperId());//试卷模版ID
					examTestpaperForm.setTestpaperName(exam.getTestpaper().getTestpaperName());//试卷名称
					form.setTestpaper(examTestpaperForm);
				}
				
				/*for(int i = 0 ;i<examList.size();i++){
					Exam exam  = (Exam)examList.get(i);
					ExamForm examForm = new ExamForm();
					examForm.setExamId(exam.getExamId());
					examForm.setCreationDate(exam.getCreationDate());//创建时间
					examForm.setCreatedBy(exam.getCreatedBy());//创建人
					examForm.setCreatedIdBy(exam.getCreatedIdBy());//创建人ID
					examForm.setOrganId(exam.getOrganId());//机构ID
					examForm.setOrganName(exam.getOrganName());//机构名称
					examForm.setExamName(exam.getExamName());
					examForm.setExamPaperType(exam.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
					examForm.setExamCode(exam.getExamCode());//科目编码  考试安排编码+二位顺序号
					examForm.setScore(exam.getScore());//科目总分
					examForm.setIsPublic(exam.getIsPublic());//是否发布成绩 5：否，10：是
					examForm.setBanTime(exam.getBanTime());//禁止进入时间_分钟
					examForm.setBeforeTime(exam.getBeforeTime());//提前进入时间_分钟
					examForm.setPassScore(exam.getPassScore());//合格分数线 默认按总分的60％，可进行设置。
					examForm.setPublicUser(exam.getPublicUser());//成绩发布人
					examForm.setPublicUserId(exam.getPublicUserId());//成绩发布人ID
					examForm.setPublicTime(exam.getPublicTime());//成绩发布时间
					examForm.setIsCutScreen(exam.getIsCutScreen());//是否截取图片 0：否；5是
					examForm.setIsUse(exam.getIsUse());///是否使用 5：否,10：是
					examForm.setCheckUser(exam.getCheckUser());//审核人
					examForm.setCheckUserId(exam.getCheckUserId());///审核人ID
					examForm.setCheckTime(exam.getCheckTime());//审核时间
					examForm.setMarkScoreType(exam.getMarkScoreType());//评分方式 10-考完系统立即算分  20-待审核后算法
					examForm.setState(exam.getState());//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
					examForm.setExamStartTime(exam.getExamStartTime()!=null ? exam.getExamStartTime().replaceAll("T", " ") : null);
					examForm.setExamEndTime(exam.getExamEndTime()!=null ? exam.getExamEndTime().replaceAll("T", " ") : null);
					if(exam.getTestpaper()!=null){
						ExamTestpaperForm examTestpaperForm = new ExamTestpaperForm();
						examTestpaperForm.setTestpaperId(exam.getTestpaper().getTestpaperId());//试卷模版ID
						examTestpaperForm.setTestpaperName(exam.getTestpaper().getTestpaperName());//试卷名称
						examForm.setTestpaper(examTestpaperForm);
					}
					form.getExamFormList().add(examForm);
				}*/
			}
		}
		return "find";
	}
	
	

	/**
	 * 保存
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String save() throws EapException{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}else if(usersess.getOrganId() == null || "".equals(usersess.getOrganId())){
	 		this.setMsg("登录人员员工信息为空，请用有效用户登录后录入数据！");
			return "save";
	 	}
		try {
			ExampaperService exampaperService = (ExampaperService)this.getService();
			form = (ExamArrangeForm) this.jsonToObject(ExamArrangeForm.class);
			ExamArrange examArrange = null;
			if(form.getExamArrangeId()!=null && !"".equals(form.getExamArrangeId()) && !"null".equals(form.getExamArrangeId())){
				examArrange = (ExamArrange)this.getService().findDataByKey(form.getExamArrangeId(), ExamArrange.class);
			}
			if(examArrange==null){
				examArrange = new ExamArrange();
				examArrange.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				examArrange.setCreatedBy(usersess.getEmployeeName());//创建人
				examArrange.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				examArrange.setOrganId(usersess.getOrganId());//机构ID
				examArrange.setOrganName(usersess.getOrganAlias());//机构名称
			}else{
				examArrange.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				examArrange.setLastUpdatedBy(usersess.getEmployeeName());//最后修改人
				examArrange.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			//BeanUtils.copyProperties(form,examArrange);
			//examArrange.setExamArrangeId(form.getExamArrangeId());
			ExamPublic examPublic = null;
			if(form.getPublicId()!=null && !"".equals(form.getPublicId()) && !"null".equals(form.getPublicId())){
				examPublic = (ExamPublic)this.getService().findDataByKey(form.getPublicId(), ExamPublic.class);
				examArrange.setExamPublic(examPublic);
				if(form.getScoreStartTime()==null || "".equals(form.getScoreStartTime()) || "null".equals(form.getScoreStartTime())){
					examArrange.setScoreStartTime(examPublic.getScoreStartTime());
				}else{
					examArrange.setScoreStartTime(form.getScoreStartTime());
				}
				if(form.getScoreEndTime()==null || "".equals(form.getScoreEndTime()) || "null".equals(form.getScoreEndTime())){
					examArrange.setScoreEndTime(examPublic.getScoreEndTime());
				}else{
					examArrange.setScoreEndTime(form.getScoreEndTime());
				}
					
			}
			examArrange.setExamName(form.getExamArrangeName());//考试名称
			examArrange.setScore(form.getScore());//总分
			if(examArrange.getExamCode()==null || "".equals(examArrange.getExamCode()) 
					|| "null".equals(examArrange.getExamCode())){
				//考试类型(性质)getExamTypeId，数据字典定义（安规、岗位、竞赛等）
				if("ag".equals(form.getExamTypeId())){
					examArrange.setExamCode("AG"+MakePwd.getTick()+"");//form.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）
				}else if("gw".equals(form.getExamTypeId())){
					examArrange.setExamCode("GW"+MakePwd.getTick()+"");//form.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）
				}else if("js".equals(form.getExamTypeId())){
					examArrange.setExamCode("JS"+MakePwd.getTick()+"");//form.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）
				}else{
					examArrange.setExamCode("KS"+MakePwd.getTick()+"");//form.getExamCode());//考试编码 考试类型编码 + 日期 + 序号（2位）	
				}
				
			}
			examArrange.setExamTypeId(form.getExamTypeId());//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
			examArrange.setExamTypeName(form.getExamTypeName());//考试类型(性质)，与考试类型ID一组
			examArrange.setExamProperty(form.getExamProperty());//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			examArrange.setIsPublic(form.getIsPublic());//是否发布成绩 5：否，10：是
			//examArrange.setPublicUser(form.getPublicUser());//成绩发布人
			//examArrange.setPublicUserId(form.getPublicUserId());//成绩发布人编号
			//examArrange.setPublicTime(form.getPublicTime());//成绩发布时间
			examArrange.setIsUse(form.getIsUse());///是否使用 5：否,10：是
			//examArrange.setCheckUser(form.getCheckUser());//审核人
			//examArrange.setCheckUserId(form.getCheckUserId());///审核人ID
			//examArrange.setCheckTime(form.getCheckTime());//审核时间
			examArrange.setState(form.getState());//
			examArrange.setExamPaperType(form.getExamPaperType());//
			examArrange.setIsIdNumberLogin(form.getIsIdNumberLogin());//
			//examArrange.setRelationId(form.getRelationId());//关联ID（如：练习安排等）
			//examArrange.setRelationType(form.getRelationType());//关联类型
			examArrange.setRemark(form.getRemark());//备注
			if("20".equals(form.getExamProperty())){
				examArrange.setScoreStartTime(null);
				examArrange.setScoreEndTime(null);
				form.setScoreStartTime(null);
				form.setScoreEndTime(null);
			}
			
				
			/*Iterator<Exam> its = examArrange.getExams().iterator();
			Map<String,Exam> examMap = new HashMap<String,Exam>();
			while(its.hasNext()){
				Exam exam = (Exam)its.next();
				boolean isDel = true;
				for(int i = 0 ;i<form.getExamFormList().size();i++){
					ExamForm examForm = (ExamForm)form.getExamFormList().get(i);
					if(exam.getExamId().equals(examForm.getExamId())){
						examMap.put(exam.getExamId(), exam);
						isDel = false;
						break;
					}
				}
				if(isDel){
					its.remove();
				}
			}*/
			
			Exam exam = null;
			boolean isAdd = false;
			if(form.getExamId()!=null && !"".equals(form.getExamId()) && !"null".equals(form.getExamId())){
				 exam = (Exam)examArrange.getExams().get(0);
			}
			if(exam == null){
				exam = new Exam();
				exam.setExamCode(examArrange.getExamCode()+"01");//科目编码  考试安排编码+二位顺序号
				exam.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				exam.setCreatedBy(usersess.getEmployeeName());//创建人
				exam.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				exam.setIsCutScreen("0");//是否截取图片 0：否；5是
				exam.setMarkScoreType("20");//评分方式 10-考完系统立即算分  20-待审核后算法
				isAdd = true;
			}else{
				exam.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				exam.setLastUpdatedBy(usersess.getEmployeeName());//最后修改人
				exam.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			
			exam.setExamName(form.getExamArrangeName());
			exam.setOrganId(usersess.getOrganId());//机构ID
			exam.setOrganName(usersess.getOrganAlias());//机构名称
			exam.setExamPaperType(form.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
			exam.setIsPublic(form.getIsPublic());//是否发布成绩 5：否，10：是
			exam.setBanTime(form.getBanTime());//禁止进入时间_分钟
			exam.setBeforeTime(form.getBeforeTime());//提前进入时间_分钟
			exam.setPassScore(form.getPassScore());//合格分数线 默认按总分的60％，可进行设置。
			//exam.setPublicUser(form.getPublicUser());//成绩发布人
			//exam.setPublicUserId(form.getPublicUserId());//成绩发布人ID
			exam.setPublicTime(form.getPublicTime());//成绩发布时间
			
			exam.setIsUse(10);///是否使用 5：否,10：是
			//exam.setCheckUser(examForm.getCheckUser());//审核人
			//exam.setCheckUserId(examForm.getCheckUserId());///审核人ID
			//exam.setCheckTime(examForm.getCheckTime());//审核时间	
			exam.setState(examArrange.getState());//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
			exam.setExamStartTime(form.getExamStartTime().replaceAll("T", " "));
			exam.setExamEndTime(form.getExamEndTime().replaceAll("T", " "));
			exam.setScoreStartTime(examArrange.getScoreStartTime());
			exam.setScoreEndTime(examArrange.getScoreEndTime());
			ExamTestpaperForm examTestpaperForm = (ExamTestpaperForm)form.getTestpaper();
			if(examTestpaperForm!=null && examTestpaperForm.getTestpaperId()!=null && !"".equals(examTestpaperForm.getTestpaperId())
					&& !"null".equals(examTestpaperForm.getTestpaperId())){
				Testpaper testpaper = (Testpaper)this.getService().findDataByKey(examTestpaperForm.getTestpaperId(), Testpaper.class);
				exam.setTestpaper(testpaper);
				exam.setScore(testpaper.getTotalScore());//科目总分
			}
			if(isAdd){
				exam.setExamArrange(examArrange);
				examArrange.getExams().add(exam);
			}
			
			/*for(int i = 0 ;i<form.getExamFormList().size();i++){
				ExamForm examForm = (ExamForm)form.getExamFormList().get(i);
				Exam exam = null;
				boolean isAdd = false;
				if(examForm.getExamId()!=null && !"".equals(examForm.getExamId()) && !"null".equals(examForm.getExamId())){
					 exam = (Exam)examMap.get(examForm.getExamId());
				}
				if(exam == null){
					exam = new Exam();
					isAdd = true;
				}
				
				exam.setExamName(examForm.getExamName());
				exam.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				exam.setCreatedBy(usersess.getEmployeeName());//创建人
				exam.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				exam.setOrganId(usersess.getOrganId());//机构ID
				exam.setOrganName(usersess.getOrganAlias());//机构名称
				
				exam.setExamPaperType(examForm.getExamPaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
				exam.setExamCode(examForm.getExamCode());//科目编码  考试安排编码+二位顺序号
				exam.setScore(examForm.getScore());//科目总分
				exam.setIsPublic(examForm.getIsPublic());//是否发布成绩 5：否，10：是
				exam.setBanTime(examForm.getBanTime());//禁止进入时间_分钟
				exam.setBeforeTime(examForm.getBeforeTime());//提前进入时间_分钟
				exam.setPassScore(examForm.getPassScore());//合格分数线 默认按总分的60％，可进行设置。
				exam.setPublicUser(examForm.getPublicUser());//成绩发布人
				exam.setPublicUserId(examForm.getPublicUserId());//成绩发布人ID
				exam.setPublicTime(examForm.getPublicTime());//成绩发布时间
				exam.setIsCutScreen(examForm.getIsCutScreen());//是否截取图片 0：否；5是
				exam.setIsUse(form.getIsUse());///是否使用 5：否,10：是
				//exam.setCheckUser(examForm.getCheckUser());//审核人
				//exam.setCheckUserId(examForm.getCheckUserId());///审核人ID
				//exam.setCheckTime(examForm.getCheckTime());//审核时间
				exam.setMarkScoreType(examForm.getMarkScoreType());//评分方式 10-考完系统立即算分  20-待审核后算法
				exam.setState(examForm.getState());//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
				exam.setExamStartTime(examForm.getExamStartTime().replaceAll("T", " "));
				exam.setExamEndTime(examForm.getExamEndTime().replaceAll("T", " "));
				exam.setScoreStartTime(examArrange.getScoreStartTime());
				exam.setScoreEndTime(examArrange.getScoreEndTime());
				exam.setState(examArrange.getState());
				
				ExamTestpaperForm examTestpaperForm = (ExamTestpaperForm)examForm.getTestpaper();
				if(examTestpaperForm!=null && examTestpaperForm.getTestpaperId()!=null && !"".equals(examTestpaperForm.getTestpaperId())
						&& !"null".equals(examTestpaperForm.getTestpaperId())){
					Testpaper testpaper = (Testpaper)this.getService().findDataByKey(examTestpaperForm.getTestpaperId(), Testpaper.class);
					exam.setTestpaper(testpaper);
				}
				if(isAdd){
					exam.setExamArrange(examArrange);
					examArrange.getExams().add(exam);
				}
			}*/
			exampaperService.saveOld(examArrange);
			
			
			//if("15".equals(form.getState()) && examPublic!=null){//发布情况 插入考生信息
				//ExamVariable.getSaveAnsPath(request);
				//ExamVariable.getExamSaveOverFlagPath(request);
				//List<ExamPublicUser> examPublicUsers = examPublic.getExamPublicUsers();
				//for(int i=0;i<examArrange.getExams().size();i++){
					//Exam exam = examArrange.getExams().get(i);
					//exampaperService.saveExamUserInExamPublicUsers(exam,examPublicUsers, usersess,request,true);
					//exampaperService.saveExamTestPaperInExam(request,exam);
					/*List<ThemeType> themeTypeList = null;
					for(int t=0;t<exam.getExamUserTestpapers().size();t++){
						ExamUserTestpaper examUserTestpaper = exam.getExamUserTestpapers().get(t);
						if(examUserTestpaper.getExamTestpaper()!=null){
							continue;
						}
						try{
							ExamTestpaper examTestpaper = exampaperService.saveAndGetNewExamTestPaper(exam,examUserTestpaper,exam.getTestpaper());
							examUserTestpaper.setExamTestpaper(examTestpaper);
							examTestpaper.getExamUserTestpapers().add(examUserTestpaper);
							this.service.save(examTestpaper);
							
							if(themeTypeList == null){
								Map<String,Object> term = new HashMap<String,Object>();
								term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
								themeTypeList = this.service.queryData("queryTypeInExampaperHql", term, null);
							}
							
							//保存并生成试题
							if(examUserTestpaper!=null && examTestpaper!=null &&  examTestpaper.getExamTestpaperThemes()!=null && 
									examTestpaper.getExamTestpaperThemes().size()>0 && themeTypeList!=null && themeTypeList.size()>0){
								ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examTestpaper, themeTypeList);
								if(exam.getExamArrange().getIsPublic()!=null && exam.getExamArrange().getIsPublic().intValue() == 10){
									ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examTestpaper);
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}*/
				//}
				
			//}
			
			//ExampaperService exampaperService=(ExampaperService)this.getService();
			//exampaperService.deleteNullExam();
			//exampaperService.saveUseTestpaperNum();
			examIds = "";
			boolean isFalg = true;
			//List<ExamPublicUser> examPublicUsers = examPublic.getExamPublicUsers();
			for(int i=0;i<examArrange.getExams().size();i++){
				Exam _exam = examArrange.getExams().get(i);
				examIds+=_exam.getExamId()+",";
				
				
				if("15".equals(form.getState()) && examPublic!=null){//发布情况 插入考生信息
					if(isFalg){
						ExamVariable.setHttpRequest(request);
						ExamVariable.getRequestUrl(request);
						ExamVariable.getExamUserFilePath(request);
						ExamVariable.getSaveAnsPath(request);
						ExamVariable.getExamSaveOverFlagPath(request);
						isFalg = false;
					}
					if("updateLess".equals(this.getOp())){
						StaticVariable.examCrIdMap.remove(_exam.getExamId());
					}
					examThreadService.addExam(_exam.getExamId(), request, usersess);
				}
			}
			this.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}
	
	
	
	/**
	 * 
	 * @author zhujian
	 * @description 针对考试产生新试卷并保存
	 * @return
	 * @modified
	 */
	public String saveExamTestPaper()throws Exception{
		try{
			ExamVariable.getSaveAnsPath(request);
			ExamVariable.getExamSaveOverFlagPath(request);
			ExampaperService exampaperService = (ExampaperService)this.getService();
			Exam exam = (Exam)exampaperService.findDataByKey(this.getId(), Exam.class);
			if(exam!=null){
				exampaperService.saveExamTestPaperInExam(request,exam);
				//exampaperService.saveOld(exam);
				this.setMsg("保存成功！");
			}else{
				ExamArrange examArrange = (ExamArrange)exampaperService.findDataByKey(this.getId(), ExamArrange.class);
				if(examArrange!=null){
					for(int i=0;i<examArrange.getExams().size();i++){
						exam = examArrange.getExams().get(i);
						exampaperService.saveExamTestPaperInExam(request,exam);
					}
					//exampaperService.saveOld(examArrange);
				}else{
					this.setMsg("没有找到对应的数据！");
				}
			}
			//exampaperService.saveUseTestpaperNum();
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
			throw  e;
		}
		return "save";
	}
	
	
	
	
	/**
	 * 处理相关答案到数据库中
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String saveAnsFileToDb()throws Exception{
		try{
			ExampaperService exampaperService = (ExampaperService)this.getService();
			exampaperService.saveAndsubInExamUserTestpaper();
			exampaperService.saveAnswerInUserAnswerFile(request,null);
			exampaperService.saveNotSubmitAnsExamUserMark();
			this.setMsg("处理成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("处理失败！");
			throw e;
		}
		return "save";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 初始化用户文件
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String initUserExamFile()throws Exception{
		try{
			ExampaperService exampaperService = (ExampaperService)this.getService();
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(this.getId(), ExamUserTestpaper.class);
			
			if(examUserTestpaper.getExamTestpaper()==null){
				ExamTestpaper examTestpaper = exampaperService.saveAndGetNewExamTestPaper(examUserTestpaper.getExam(),
						examUserTestpaper,examUserTestpaper.getExam().getTestpaper());
				examUserTestpaper.setExamTestpaper(examTestpaper);
				examTestpaper.getExamUserTestpapers().add(examUserTestpaper);
				exampaperService.add(examTestpaper);
				
				ExamUserJsonForm.getAndSaveExamUser(request, examUserTestpaper.getExam());
				
				Map<String,Object> term = new HashMap<String,Object>();
				term.put("examTestpaperId", examTestpaper.getExamTestpaperId());
				List<ThemeType> themeTypeList = this.getService().queryData("queryTypeInExampaperHql", term, null);
				
				//保存并生成试题
				ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examTestpaper, themeTypeList);
				if(examUserTestpaper.getExam().getExamArrange().getIsPublic()!=null && examUserTestpaper.getExam().getExamArrange().getIsPublic().intValue() == 10){
					ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examTestpaper);
				}
				exampaperService.saveOld(examTestpaper);
			}else{
				Map<String,Object> term = new HashMap<String,Object>();
				term.put("examTestpaperId", examUserTestpaper.getExamTestpaper().getExamTestpaperId());
				List<ThemeType> themeTypeList = this.getService().queryData("queryTypeInExampaperHql", term, null);
				
				ExamUserJsonForm.getAndSaveExamUser(request, examUserTestpaper.getExam());
				
				//保存并生成试题
				ExamTestpaperJsonForm.getAndSaveExamTestpaper(request, examUserTestpaper, examUserTestpaper.getExamTestpaper(), themeTypeList);
				if(examUserTestpaper.getExam().getExamArrange().getIsPublic()!=null && examUserTestpaper.getExam().getExamArrange().getIsPublic().intValue() == 10){
					ExamTestpaperAnswerJsonForm.getAndSaveExamTestpaperAnswer(request, examUserTestpaper, examUserTestpaper.getExamTestpaper());
				}
				exampaperService.saveOld(examUserTestpaper);
			}
			//exampaperService.saveUseTestpaperNum();
			this.setMsg("处理成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("处理失败！");
			throw e;
		}
		return "save";
		
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 删除用户初始化文件
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String cleanUserExamFile()throws Exception{
		try{
			ExampaperService exampaperService = (ExampaperService)this.getService();
			ExamUserTestpaper examUserTestpaper = (ExamUserTestpaper)this.getService().findDataByKey(this.getId(), ExamUserTestpaper.class);
			String examId = examUserTestpaper.getExam().getExamId();
			String examFileName = ExamVariable.getExamFilePath(request)+"title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
					/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"title",
					"title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/
			String examAnsFileName = ExamVariable.getExamUserFilePath(request)+examUserTestpaper.getExam().getExamId()+System.getProperty("file.separator")
					+"ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
					/*FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"ans"+System.getProperty("file.separator")
					+examUserTestpaper.getExam().getExamId(),
					"ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket())+".txt";*/
			examUserTestpaper.setExam(null);
			exampaperService.saveOld(examUserTestpaper);
			Exam exam = (Exam)this.getService().findDataByKey(examId, Exam.class);
			
			File file = new File(examFileName);
			if(file.exists()){
				file.delete();
			}
			
			File ansFile = new File(examAnsFileName);
			if(ansFile.exists()){
				ansFile.delete();
			}
			ExamUserJsonForm.getAndSaveExamUser(request, exam);
			
			exampaperService.deleteNullexamUserTestpaper();
			//清理考试安排中未选中考生的准考证
			exampaperService.saveAndCleanPublicUserInticket();
			this.setMsg("处理成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("处理失败！");
			throw e;
		}
		return "save";
		
	}
	
	
	/**
	 * 处理模拟试题
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String saveMoniExam()throws Exception{
		try{
			ExampaperMoniService exampaperMoniService = (ExampaperMoniService)SpringContextUtil.getBean("exampaperMoniService");
			exampaperMoniService.saveAndCleanExamPaperByUserTestpaperId("4028e4784d4ff2e2014d4ff471ca0004");
			//.saveAndCleanExamPaperInUse("4028e4784d4c1e03014d4c1f49e30001", "userExam", "4028e9864c4eba69014c4ec4b7f10051");
			/*String[] ids = exampaperMoniService.addOrgetTrainImplementExamPaperByImplId("4028e9884c917d8c014c9184622e0001", "moni", new String[]{"4028e9a54cc014fc014cc036b9530003"},null,null,null);
			if(ids!=null && ids.length>0){
				for(int i=0;i<ids.length;i++){
					System.out.println(" 模拟试题ID "+i+"  = "+ids[i]);
				}
			}*/
			this.setMsg("处理成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("处理失败！");
			throw e;
		}
		return "save";
	}
	
	
	
	public String exeTaskInFile(){
		
		return "saveAnswerToDb";
	}

	public ExamArrangeForm getForm() {
		return form;
	}

	public void setForm(ExamArrangeForm form) {
		this.form = form;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ExamForm getExamForm() {
		return examForm;
	}

	public void setExamForm(ExamForm examForm) {
		this.examForm = examForm;
	}


	public String getTestpaperId() {
		return testpaperId;
	}


	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}


	public String getExamType() {
		return examType;
	}


	public void setExamType(String examType) {
		this.examType = examType;
	}


	public String getExamIds() {
		return examIds;
	}


	public void setExamIds(String examIds) {
		this.examIds = examIds;
	}


	public ExamThreadService getExamThreadService() {
		return examThreadService;
	}


	public void setExamThreadService(ExamThreadService examThreadService) {
		this.examThreadService = examThreadService;
	}


	public String getOp() {
		return op;
	}


	public void setOp(String op) {
		this.op = op;
	}


}