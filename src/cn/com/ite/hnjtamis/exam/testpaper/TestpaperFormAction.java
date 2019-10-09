package cn.com.ite.hnjtamis.exam.testpaper;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeInThemeBankForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperSkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemetype;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperForm;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemetypeForm;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperFormAction</p>
 * <p>Description 试卷模版生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:05:29
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperFormAction extends AbstractFormAction implements ServletRequestAware{

	private static final long serialVersionUID = 7568757041961830585L;

	private HttpServletRequest request;
	
	private TestpaperForm form;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

	/**
	 * 查询
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String find(){
		form = new TestpaperForm();
		Testpaper testpaper = this.getId()!=null && !"".equals(this.getId()) && !"null".equals(this.getId()) ? (Testpaper) service.findDataByKey(this.getId(), Testpaper.class) : null;
		if(testpaper!=null){
			try {
				BeanUtils.copyProperties(testpaper, form);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<ThemeBank> allThemeBankList = this.getService().queryAllDate(ThemeBank.class);
			Map<String,ThemeBank> themeBankMap = new HashMap<>();
			for(ThemeBank themeBank : allThemeBankList){
				themeBankMap.put(themeBank.getThemeBankId(), themeBank);
			}
			
			List testpaperThemetypes = testpaper.getTestpaperThemetypes();
			if(testpaperThemetypes!=null && testpaperThemetypes.size()>0){
				for(int i = 0 ;i<testpaperThemetypes.size();i++){
					TestpaperThemetype testpaperThemetype  = (TestpaperThemetype)testpaperThemetypes.get(i);
					TestpaperThemetypeForm testpaperThemetypeForm = new TestpaperThemetypeForm();
					try {
						BeanUtils.copyProperties(testpaperThemetype, testpaperThemetypeForm);
						if(testpaperThemetype.getProfessionId()!=null 
								&& !"".equals(testpaperThemetype.getProfessionId()) 
								&& !"null".equals(testpaperThemetype.getProfessionId())){
							Speciality speciality =
									(Speciality) service.findDataByKey(testpaperThemetype.getProfessionId(), Speciality.class);
							testpaperThemetypeForm.setSpeciality(speciality);
						}
						if(testpaperThemetype.getBankId()!=null && testpaperThemetype.getBankId().length()>0){
							String[] banks = testpaperThemetype.getBankId().split(",");
							for(int k=0;k<banks.length;k++){
								ThemeInThemeBankForm themeInThemeBankForm = new ThemeInThemeBankForm();
								themeInThemeBankForm.setThemeBankId(banks[k]);
								ThemeBank themeBank = themeBankMap.get(themeInThemeBankForm.getThemeBankId());
								if(themeBank!=null){
									themeInThemeBankForm.setThemeBankName(themeBank.getThemeBankName());
								}
								testpaperThemetypeForm.getSelectbanks().add(themeInThemeBankForm);
							}
						}
						
						testpaperThemetypeForm.setThemeType(testpaperThemetype.getThemeType());
						testpaperThemetypeForm.setThemeTypeId(testpaperThemetype.getThemeType()!=null?testpaperThemetype.getThemeType().getThemeTypeId():null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					form.getTestpaperThemetypeFormList().add(testpaperThemetypeForm);
				}
			}
			
			
			List<TestpaperSkey> testpaperSkeylist = testpaper.getTestpaperSkeies();
			if(testpaperSkeylist!=null && testpaperSkeylist.size()>0){
				form.setThemeBankFormList(new ArrayList());
				for(int i = 0;i<testpaperSkeylist.size();i++){
					TestpaperSkey testpaperSkey = (TestpaperSkey)testpaperSkeylist.get(i);
					if(testpaperSkey.getThemeBankId()!=null){
						ThemeInThemeBankForm themeInThemeBankForm = new ThemeInThemeBankForm();
						themeInThemeBankForm.setThemeBankId(testpaperSkey.getThemeBankId());
						themeInThemeBankForm.setThemeBankName(testpaperSkey.getThemeBankName());
						form.getThemeBankFormList().add(themeInThemeBankForm);
					}
				}
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
	 	}
		try {
			TestpaperService testpaperService = (TestpaperService)this.getService();
			form = (TestpaperForm) this.jsonToObject(TestpaperForm.class);
			Testpaper testpaper = null;
			if(form.getTestpaperId()!=null && !"".equals(form.getTestpaperId()) && !"null".equals(form.getTestpaperId())){
				testpaper = (Testpaper)this.getService().findDataByKey(form.getTestpaperId(), Testpaper.class);
			}
			if(testpaper==null){
				testpaper = new Testpaper();
				testpaper.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				testpaper.setCreatedBy(usersess.getEmployeeName());//创建人
				testpaper.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				testpaper.setOrganId(usersess.getOrganId());//机构ID
				testpaper.setOrgan(usersess.getOrganAlias());//机构名称
				testpaper.setUseNum(0);
			}else{
				testpaper.setTestpaperId(form.getTestpaperId());
				testpaper.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
				testpaper.setLastUpdatedBy(usersess.getEmployeeName());//最后修改人
				testpaper.setLastUpdatedIdBy(usersess.getEmployeeId());//最后修改人ID
			}
			//testpaper.setTestpaperId(form.getTestpaperId());//试卷模版ID
			testpaper.setIsPrivate(form.getIsPrivate());//是否私有(5：否,10：是)
			testpaper.setTestpaperName(form.getTestpaperName());//试卷名称
			testpaper.setTestpaperType(form.getTestpaperType());//试卷生成方式  10-同试卷打乱  20-同试卷同顺序 30-各考生按模版随机抽题
			testpaper.setExamTypeId(form.getExamTypeId());//考试类型(性质)ID，数据字典定义（安规、岗位、竞赛等）
			List<Dictionary> examKslxList = testpaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
			if(form.getExamTypeId()!=null && examKslxList!=null && examKslxList.size()>0){
				for(int i=0;i<examKslxList.size();i++){
					if(examKslxList.get(i).getDataKey().equals(form.getExamTypeId())){
						testpaper.setExamTypeName(examKslxList.get(i).getDataName());//考试类型(性质)，与考试类型ID一组
						break;
					}
				}
			}
			testpaper.setExamProperty(form.getExamProperty());//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
			testpaper.setTotalTheme(form.getTotalTheme());//总题数
			testpaper.setTotalScore(form.getTotalScore());//总分数
			testpaper.setTestpaperRank(form.getTestpaperRank());//难度系数
			testpaper.setScreeningMethods(form.getScreeningMethods());//筛选方式(5：分数，10：题数)
			testpaper.setTestpaperTime(form.getTestpaperTime());//参考考时（分钟）
			testpaper.setIsUse(form.getIsUse());//是否使用(5：否,10：是)
			testpaper.setRemark(form.getRemark());//备注
			testpaper.setExamProperty(form.getExamProperty());
			/*testpaper.setCheckUser(form.getCheckUser());//审核人
			testpaper.setCheckUserId(form.getCheckUserId());//审核人ID
			testpaper.setCheckTime(form.getCheckTime());//审核时间
			testpaper.setCheckIdear(form.getCheckIdear());///审核意见*/
			if(form.getExamProperty() == 30 || form.getExamProperty() ==40){
				testpaper.setState("15");//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
			}else{
				testpaper.setState(form.getState());//状态 5：未上报，10：等待审核，15：审核通过，20：审核打回
			}
			//testpaper.setUseNum(form.getUseNum());//使用次数
			/*private String organ;//机构名
			private String organId;//机构编号e
			private String syncFlag;////同步标志
			private String lastUpdateDate;///最后修改时间
			private String lastUpdatedBy;//最后修改人
			private String lastUpdatedIdBy;//最后修改人ID
			private String creationDate;//创建时间
			private String createdBy;//创建人
			private String createdIdBy;//创建人ID*/
			testpaper.getTestpaperThemetypes().clear();
			//testpaper.getTestpaperThemes().clear();
			testpaper.getTestpaperSkeies().clear();
			testpaper.getTestpaperShares().clear();
			
			List<ThemeInThemeBankForm> themeBankFormList = form.getThemeBankFormList();
			if(themeBankFormList!=null && themeBankFormList.size()>0){
				for(int i = 0;i<themeBankFormList.size();i++){
					ThemeInThemeBankForm themeInThemeBankForm = (ThemeInThemeBankForm)themeBankFormList.get(i);
					TestpaperSkey testpaperSkey = new TestpaperSkey();
					testpaperSkey.setThemeBankId(themeInThemeBankForm.getThemeBankId());
					testpaperSkey.setThemeBankName(themeInThemeBankForm.getThemeBankName());
					testpaperSkey.setTestpaper(testpaper);
					testpaperSkey.setOrganId(usersess.getOrganId());//机构ID
					testpaperSkey.setOrganName(usersess.getOrganAlias());//机构名称
					testpaperSkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					testpaperSkey.setCreatedBy(usersess.getEmployeeName());//创建人
					testpaperSkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					testpaperSkey.setSortNum(i);
					testpaper.getTestpaperSkeies().add(testpaperSkey);
				}
			}
			
			List<TestpaperThemetypeForm> testpaperThemetypeFormList = form.getTestpaperThemetypeFormList();
			if(testpaperThemetypeFormList!=null && testpaperThemetypeFormList.size()>0){
				for(int i = 0 ;i<testpaperThemetypeFormList.size();i++){
					TestpaperThemetypeForm testpaperThemetypeForm = (TestpaperThemetypeForm)testpaperThemetypeFormList.get(i);
					TestpaperThemetype testpaperThemetype = new TestpaperThemetype();
					//testpaperThemetype.setTestpaperThemetypeId();//试卷-题型ID
					ThemeType themeType = (ThemeType)this.getService().findDataByKey(testpaperThemetypeForm.getThemeTypeId(), ThemeType.class);
					testpaperThemetype.setThemeType(themeType);//题型
					testpaperThemetype.setTestpaper(testpaper);///试卷
					testpaperThemetype.setRankRate(testpaperThemetypeForm.getRankRate());/////难度比例 {容易,难,很难}{0，0，0}逗号分割，表示某试卷某题型的难度分布
					testpaperThemetype.setProfessionId(testpaperThemetypeForm.getSpeciality()!=null ? testpaperThemetypeForm.getSpeciality().getSpecialityid() : null);//专业ID
					testpaperThemetype.setProfessionName(testpaperThemetypeForm.getSpeciality()!=null ? testpaperThemetypeForm.getSpeciality().getSpecialityname() : null);//专业
					//testpaperThemetype.setProfessionLevelCode(testpaperThemetypeForm.getSpeciality().get);//专业级别编码
					testpaperThemetype.setRate(testpaperThemetypeForm.getRate());//比例%
					testpaperThemetype.setScore(testpaperThemetypeForm.getScore()==null ? new Double(0) : testpaperThemetypeForm.getScore());//按分数筛选时为分数,按题数筛选时为题数
					if(form.getScreeningMethods().intValue() == 5){
						testpaperThemetype.setSetThemetype("10");//配置方式 10-按分数 20-按题数
					}else if(form.getScreeningMethods().intValue() == 10){
						testpaperThemetype.setSetThemetype("20");//配置方式 10-按分数 20-按题数
					}
					testpaperThemetype.setTotal(testpaperThemetypeForm.getTotal());///题数
					testpaperThemetype.setSortNum(i+1);//排序号
					testpaperThemetype.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					testpaperThemetype.setCreatedBy(usersess.getEmployeeName());//创建人
					testpaperThemetype.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					testpaperThemetype.setActualScore(testpaperThemetypeForm.getActualScore());
					testpaperThemetype.setActualTotal(testpaperThemetypeForm.getActualTotal());
					
					List<ThemeInThemeBankForm> banks = testpaperThemetypeForm.getSelectbanks();
					String banksIds = "";
					if(banks!=null && banks.size()>0){
						for(int k = 0;k<banks.size();k++){
							ThemeInThemeBankForm themeInThemeBankForm = (ThemeInThemeBankForm)banks.get(k);
							if(k==0){
								banksIds = themeInThemeBankForm.getThemeBankId();
							}else{
								banksIds += ","+themeInThemeBankForm.getThemeBankId();
							}
						}
					}
					testpaperThemetype.setBankId(banksIds);
					
					testpaper.getTestpaperThemetypes().add(testpaperThemetype);
					//this.service.save(testpaperThemetype);
				}
			}
			
			//this.service.saveOld(testpaper);
			//this.service.deletes(testpaper.getTestpaperThemes());
			

			//修改试题
			if(form.getThemeIds()!=null && form.getThemeIds().length()>0){
				String[] themeIds = form.getThemeIds().split(",");
				String[] themeDefScores = form.getThemeDefScore().split(",");
				Map<String,Double> themeDefScoresMap = new HashMap<String,Double>();
				for(int i=0;i<themeDefScores.length;i++){
					if(themeDefScores[i]!=null && !"".equals(themeDefScores[i])){
						String[]  tmp =themeDefScores[i].split("@");
						try{
							themeDefScoresMap.put(tmp[0], new Double(tmp[1]));
						}catch(Exception e){
							e.printStackTrace();
						}
					}
					
				}
				//String[] testpaperThemeIds = form.getTestpaperThemeIds().split(",");
				Map<String,TestpaperTheme> themeMap = new HashMap<String,TestpaperTheme>();
				Iterator<TestpaperTheme> itss = testpaper.getTestpaperThemes().iterator();
				
				while(itss.hasNext()){
					TestpaperTheme testpaperTheme= itss.next();
					boolean isDel = true;
					for(int i = 0;i<themeIds.length;i++){
						if(testpaperTheme.getTheme().getThemeId().equals(themeIds[i])){//根据试题库试题ID(非模版试题ID)保证唯一性
							isDel = false;
							testpaperTheme.setSortNum(i);
							themeMap.put(testpaperTheme.getTheme().getThemeId(), testpaperTheme);
						}
					}
					if(isDel){
						itss.remove();
					}
				}
				
				Map<String,Theme> themeIdsMap = testpaperService.getThemeInIds(themeIds);
				for(int i=0;i<themeIds.length;i++){
					Theme theme = themeIdsMap.get(themeIds[i]);
					if(theme == null)theme=(Theme)this.getService().findDataByKey(themeIds[i], Theme.class);
					if(theme!=null){
						TestpaperTheme testpaperTheme = (TestpaperTheme)themeMap.get(theme.getThemeId());//根据试题库试题ID获取   this.getService().findDataByKey(testpaperThemeIds[i],TestpaperTheme.class);
						boolean isAddTestpaperTheme = false;
						if(testpaperTheme==null){
							testpaperTheme = new TestpaperTheme();
							testpaperTheme.setCreatedBy(usersess.getEmployeeName());//创建人
							testpaperTheme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
							testpaperTheme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							isAddTestpaperTheme = true;
						}else{
							testpaperTheme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							testpaperTheme.setLastUpdatedBy(usersess.getEmployeeName());//创建人
							testpaperTheme.setLastUpdatedIdBy(usersess.getEmployeeId());//创建人ID
						}
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
						
						Double defaultScore = themeDefScoresMap.get(themeIds[i]);
						if(defaultScore == null){
							defaultScore = theme.getDefaultScore();
						}
						testpaperTheme.setDefaultScore(defaultScore);//默认分数
						testpaperTheme.setThemeSetNum(theme.getThemeSetNum());//出题次数
						testpaperTheme.setThemePeopleNum(theme.getThemePeopleNum());//考试人次
						testpaperTheme.setThemeRightNum(theme.getThemeRightNum());//答题正确数
						testpaperTheme.setRemark(theme.getRemark());//备注
						testpaperTheme.setIsUse(theme.getIsUse());//有否有效 5:有效 10：无效
						testpaperTheme.setThemeName(theme.getThemeName());
						
						testpaperTheme.setOrganId(usersess.getOrganId());//机构ID
						testpaperTheme.setOrganName(usersess.getOrganAlias());//机构名称
						testpaperTheme.setSortNum(i);
						testpaperTheme.setIsUse("5");//是否有效 5：否,10：是
						if(testpaper.getState()!=null && !"".equals(testpaper.getState())){//状态 5:保存10:上报15:发布20:打回
							testpaperTheme.setState(new Integer(testpaper.getState()));
						}
						
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
								testpaperThemeAnswerkey.setCreatedBy(usersess.getEmployeeName());//创建人
								testpaperThemeAnswerkey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
								testpaperThemeAnswerkey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
								testpaperThemeAnswerkey.setOrganId(usersess.getOrganId());//机构ID
								testpaperThemeAnswerkey.setOrganName(usersess.getOrganAlias());//机构名称
								testpaperThemeAnswerkey.setSortNum(themeAnswerkey.getSortNum());
								testpaperTheme.getTestpaperThemeAnswerkeies().add(testpaperThemeAnswerkey);
							}
						}
						if(isAddTestpaperTheme){
							testpaper.getTestpaperThemes().add(testpaperTheme);
						}
					}
				}
			}else{
				testpaper.getTestpaperThemes().clear();
			}
			this.service.saveOld(testpaper);
			
			try{
				testpaperService.saveAndSyncThemeBank( testpaper.getTestpaperId(), usersess.getOrganId(), usersess.getOrganAlias());
			}catch(Exception e){
				e.printStackTrace();
			}
			//ExampaperService exampaperService=(ExampaperService)SpringContextUtil.getBean("exampaperService");
			//exampaperService.saveUseTestpaperNum();
			//testpaperService.deleteNullTestpaper();
			this.setMsg("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}

	public TestpaperForm getForm() {
		return form;
	}

	public void setForm(TestpaperForm form) {
		this.form = form;
	}
	
	

}