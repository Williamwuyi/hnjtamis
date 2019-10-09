package cn.com.ite.hnjtamis.jobstandard.termsEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardThemebank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;

/**
 * 岗位标准管理
 * @author 朱健
 * @create time: 2016年3月4日 上午9:02:33
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsExFormAction extends AbstractFormAction {
	
	private static final long serialVersionUID = -2976123155735359323L;
	
	private StandardTerms form;
	
	private String standardid;
	
	private String bankIds;
	
	private String sf;
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "find";
	 	}
		form = (StandardTerms)service.findDataByKey(this.getId(), StandardTerms.class);
		List<JobsStandardThemebank> tmp_jobsStandardThemebanks = new ArrayList<JobsStandardThemebank>();
		Map<String,String> themeIdsMap = new HashMap<String,String>();
		if("onlydcsf".equals(this.getSf())){//onlydcsf onlyall
			for(int i=0;i<form.getJobsStandardThemebanks().size();i++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(i);
				if("20".equals(jobsStandardThemebank.getBankPublic()) 
						&& jobsStandardThemebank.getOrganId().equals(usersess.getCurrentOrganId())
						&& themeIdsMap.get(jobsStandardThemebank.getThemeBankId()) == null){
					tmp_jobsStandardThemebanks.add(jobsStandardThemebank);
					themeIdsMap.put(jobsStandardThemebank.getThemeBankId(),"a");
				}
			}
		}else if("onlyall".equals(this.getSf())){
			for(int i=0;i<form.getJobsStandardThemebanks().size();i++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(i);
				if("10".equals(jobsStandardThemebank.getBankPublic()) 
						&& themeIdsMap.get(jobsStandardThemebank.getThemeBankId()) == null){
					tmp_jobsStandardThemebanks.add(jobsStandardThemebank);
					themeIdsMap.put(jobsStandardThemebank.getThemeBankId(),"a");
				}
			}
		}else if("all".equals(this.getSf())){
			for(int i=0;i<form.getJobsStandardThemebanks().size();i++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(i);
				if(("10".equals(jobsStandardThemebank.getBankPublic()) 
						|| ("20".equals(jobsStandardThemebank.getBankPublic()) 
								&& jobsStandardThemebank.getOrganId().equals(usersess.getCurrentOrganId())))
						&& themeIdsMap.get(jobsStandardThemebank.getThemeBankId()) == null){
					tmp_jobsStandardThemebanks.add(jobsStandardThemebank);
					themeIdsMap.put(jobsStandardThemebank.getThemeBankId(),"a");
				}
			}
		}
		form.setJobsStandardThemebanks(tmp_jobsStandardThemebanks);
		/*List<JobsUnionStandard> list = form.getJobsUnionStandards();
		List<JobsUnionStandard> newlist = new ArrayList<JobsUnionStandard>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)list.get(i);
				if(jobsUnionStandard.getIsavailable()!=null && jobsUnionStandard.getIsavailable().intValue()!=0){
					newlist.add(jobsUnionStandard);
				}
			}
		}
		form.setJobsUnionStandards(newlist);*/
		return "find";
	}
	
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String findBankIds(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "find";
	 	}
		form = (StandardTerms)service.findDataByKey(this.getId(), StandardTerms.class);
		bankIds = "";
		if("onlydcsf".equals(this.getSf())){//onlydcsf onlyall
			for(int i=0;i<form.getJobsStandardThemebanks().size();i++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(i);
				if("20".equals(jobsStandardThemebank.getBankPublic()) 
						&& jobsStandardThemebank.getOrganId().equals(usersess.getCurrentOrganId())){
					bankIds+=jobsStandardThemebank.getThemeBankId()+",";
				}
			}
		}else if("onlyall".equals(this.getSf())){
			for(int i=0;i<form.getJobsStandardThemebanks().size();i++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(i);
				if("10".equals(jobsStandardThemebank.getBankPublic())){
					bankIds+=jobsStandardThemebank.getThemeBankId()+",";
				}
			}
		}
		
		return "findBankIds";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		form = (StandardTerms)this.jsonToObject(StandardTerms.class);
		
		//String oldBankId = null;
		
		
		// 其他属性赋值
		/*ThemeBank themeBank = null;
		String newBankId = null;
		if(form.getThemeBank()!=null && form.getThemeBank().getThemeBankId()!=null){
			themeBank= (ThemeBank)this.getService().findDataByKey(form.getThemeBank().getThemeBankId(), ThemeBank.class);
			if(themeBank!=null){
				form.setThemeBankName(themeBank.getThemeBankName());
				form.setThemeBankCode(themeBank.getThemeBankCode());
			}
		}else{
			form.setThemeBankName("");
			form.setThemeBankCode("");
		}*/
		//List notAvailableList = new ArrayList();
		if(StringUtils.isEmpty(form.getStandardid())){
			///新增操作公共字段
			form.setOrderno(1+service.getFieldMax(StandardTerms.class, "orderno"));
			AbstractDomain.addCommonFieldValue(form);
			
			service.save(form);
		}else{
			
			
			StandardTerms standardTerms= (StandardTerms)this.getService().findDataByKey(form.getStandardid(), StandardTerms.class);
			/*oldBankId = standardTerms.getThemeBank()==null? null:standardTerms.getThemeBank().getThemeBankId();
			newBankId = themeBank!=null? themeBank.getThemeBankId() : null;
			standardTerms.setThemeBank(themeBank);
			if(themeBank!=null){
				standardTerms.setThemeBankName(themeBank.getThemeBankName());
				standardTerms.setThemeBankCode(themeBank.getThemeBankCode());
			}*/
			StandardTypes standardTypes = (StandardTypes)this.getService().findDataByKey(form.getStandardTypes().getJstypeid(), StandardTypes.class);
			standardTerms.setStandardTypes(standardTypes);
			standardTerms.setStandardname(form.getStandardname());
			standardTerms.setContents(form.getContents());
			standardTerms.setReferenceBook(form.getReferenceBook());//参考资料
			standardTerms.setEfficient(form.getEfficient());//有效期 一年 两年 等
			standardTerms.setRefScore(form.getRefScore());//参考学分 得分
			standardTerms.setUpStandardScore(form.getUpStandardScore());//达标标准 得分
			standardTerms.setExamTypeName(form.getExamTypeName());//考核方式 机试、实操等
			standardTerms.setBankStandMapCode(form.getBankStandMapCode());
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(standardTerms);
			
			Map themeBankIdsMap = new HashMap();
			for(int j=0;j<form.getJobsStandardThemebanks().size();j++){
				JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)form.getJobsStandardThemebanks().get(j);
				if("onlydcsf".equals(this.getSf())){
					jobsStandardThemebank.setBankPublic("20");
					jobsStandardThemebank.setOrganId(usersess.getCurrentOrganId());
					jobsStandardThemebank.setOrganName(usersess.getCurrentOrganName());
				}else if("onlyall".equals(this.getSf())){
					jobsStandardThemebank.setBankPublic("10");
				}
				jobsStandardThemebank.setStandardTerms(standardTerms);
				jobsStandardThemebank.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
				jobsStandardThemebank.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
				themeBankIdsMap.put(jobsStandardThemebank.getThemeBankId(), jobsStandardThemebank);
			}
			
			List<JobsStandardThemebank> jobsStandardThemebanks = standardTerms.getJobsStandardThemebanks();
			if(jobsStandardThemebanks.size()>0){
				//按分类进行分离
				if("onlydcsf".equals(this.getSf())){//onlydcsf onlyall
					for(int i=0;i<jobsStandardThemebanks.size();){
						JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)jobsStandardThemebanks.get(i);
						if("20".equals(jobsStandardThemebank.getBankPublic()) 
								&& jobsStandardThemebank.getOrganId().equals(usersess.getCurrentOrganId())){
							if(themeBankIdsMap.get(jobsStandardThemebank.getThemeBankId())!=null){
								themeBankIdsMap.remove(jobsStandardThemebank.getThemeBankId());
								i++;
							}else{
								jobsStandardThemebanks.remove(i);
							}
						}else{
							i++;
						}
					}
				}else if("onlyall".equals(this.getSf())){
					for(int i=0;i<jobsStandardThemebanks.size();){
						JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)jobsStandardThemebanks.get(i);
						if("10".equals(jobsStandardThemebank.getBankPublic())){
							if(themeBankIdsMap.get(jobsStandardThemebank.getThemeBankId())!=null){
								themeBankIdsMap.remove(jobsStandardThemebank.getThemeBankId());
								i++;
							}else{
								jobsStandardThemebanks.remove(i);
							}
						}else{
							i++;
						}
					}
				}
				
				if(!themeBankIdsMap.isEmpty()){
					Iterator its = themeBankIdsMap.keySet().iterator();
					while(its.hasNext()){
						standardTerms.getJobsStandardThemebanks().add((JobsStandardThemebank)themeBankIdsMap.get((String)its.next()));
					}
				}
			}else{
				standardTerms.setJobsStandardThemebanks(form.getJobsStandardThemebanks());
			}
			service.saveOld(standardTerms);	
			//if(oldBankId!=null && oldBankId!=newBankId)
			standardTermsExService.updateMoreStandardQuarterBank(standardid);
		}
		return "save";
	}

	public String saveSort() throws Exception{
		try{
			List<StandardTerms> standardTermsList = this.getService().queryAllDate(StandardTerms.class);
			Map<String,StandardTerms> standardTermsMap = new HashMap<String,StandardTerms>();
			for(int i=0;i<standardTermsList.size();i++){
				StandardTerms standardTerms = standardTermsList.get(i);
				standardTermsMap.put(standardTerms.getStandardid(),standardTerms);
			}
			List saves = new ArrayList();
			int index = 1;
			for(String id:this.getSortIds()){
				StandardTerms dt = standardTermsMap.get(id);//(StandardTerms)service.findDataByKey(id, StandardTerms.class);
				if(dt!=null){
					dt.setOrderno(index*10);
					saves.add(dt);
					index++;
				}
			}
			service.saves(saves);
		}catch(Exception e){
			e.printStackTrace();
			this.setMsg("处理失败!");
		}
		return "save";
	}

	public String saveStandardQuarter() throws EapException{
		UserSession us = LoginAction.getUserSessionInfo();
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		form = (StandardTerms)standardTermsExService.findDataByKey(standardid, StandardTerms.class);
		
		if(form!=null && this.getId()!=null 
				&& this.getId().length()>0){
			
			//Map<String,ThemeBank> standardQuarterBank = standardTermsExService.queryBankIdByStandardQuarter(standardid);
			
			String[] ids = this.getId().split(",");
			
			Map term = new HashMap();
			term.put("standardid", form.getStandardid());
			List notAvailableList = this.getService().queryData("queryJobsUnionStandardNotAvailable", term, new HashMap());
			
			List<QuarterStandard> quarterStandardList = this.getService().queryAllDate(QuarterStandard.class);
			Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
			for(int i=0;i<quarterStandardList.size();i++){
				QuarterStandard quarterStandard = quarterStandardList.get(i);
				List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
				if(list == null){ list = new ArrayList();}
				list.add(quarterStandard);
				quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
			}
			form.getJobsStandardQuarters().clear();
			Map<String,JobsStandardQuarter> standQuarterCodesMap = new HashMap<String,JobsStandardQuarter>();
			int standardQuarterIndex = 0;
			for(int i=0;i<ids.length;i++){
				String quarterTrainCode = ids[i];
				if(quarterTrainCode.indexOf("@")!=-1){
					quarterTrainCode = quarterTrainCode.split("@")[1];//用@分割部门@岗位
				}
				//JobsStandardQuarter jobsStandardQuarter = (JobsStandardQuarter)form.getJobsStandardQuarters().get(i);
				List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(quarterTrainCode);
				if(quarterStandardlist!=null && quarterStandardlist.size()>0){
					for(int j=0;j<quarterStandardlist.size();j++){
						JobsStandardQuarter jobsStandardQuarter = new JobsStandardQuarter();
						QuarterStandard quarterStandard = quarterStandardlist.get(j);
						jobsStandardQuarter.setQuarterTrainCode(quarterTrainCode);
						jobsStandardQuarter.setQuarterTrainName(quarterStandard.getQuarterName());
						jobsStandardQuarter.setStandardTerms(form);
						jobsStandardQuarter.setOrderno(standardQuarterIndex++);
						jobsStandardQuarter.setCreatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
						jobsStandardQuarter.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
						jobsStandardQuarter.setDeptName(quarterStandard.getDeptName());
						jobsStandardQuarter.setDeptId(quarterStandard.getDeptId());
						jobsStandardQuarter.setSpecialityName(quarterStandard.getSpecialityName());
						jobsStandardQuarter.setSpecialityCode(quarterStandard.getSpecialityCode());
						jobsStandardQuarter.setDcType(quarterStandard.getDcType());
						jobsStandardQuarter.setQuarterId(quarterStandard.getQuarterId());
						
						/*ThemeBank themeBank = standardQuarterBank.get(quarterStandard.getDeptName()+"_"+quarterTrainCode);
						if(themeBank == null){
							themeBank = form.getThemeBank();
						}
						if(themeBank!=null){
							jobsStandardQuarter.setThemeBankCode(themeBank.getThemeBankCode());
							jobsStandardQuarter.setThemeBankId(themeBank.getThemeBankId());
							jobsStandardQuarter.setThemeBankName(themeBank.getThemeBankName());
						}*/
						jobsStandardQuarter.setThemeBankCode(null);
						jobsStandardQuarter.setThemeBankId(null);
						jobsStandardQuarter.setThemeBankName(null);
						
						form.getJobsStandardQuarters().add(jobsStandardQuarter);
						standQuarterCodesMap.put(jobsStandardQuarter.getQuarterTrainCode(), jobsStandardQuarter);
					}
				}
			}

			//Map quarterMap = new HashMap();
			Map isAddMap = new HashMap();
			List<Quarter>  quarterList= this.getService().queryAllDate(Quarter.class);
			form.getJobsUnionStandards().clear();
			for(int i=0;i<quarterList.size();i++){
				Quarter quarter=(Quarter)quarterList.get(i);
				//quarterMap.put(quarter.getQuarterId(), quarter);
				
				JobsStandardQuarter jobsStandardQuarter = standQuarterCodesMap.get(quarter.getQuarterTrainCode());
				if(quarter.getQuarterTrainCode()!=null && jobsStandardQuarter!=null){
					JobsUnionStandard jobsUnionStandard = new JobsUnionStandard();
					jobsUnionStandard.setJobscode(quarter.getQuarterId());
					jobsUnionStandard.setJobsname(quarter.getQuarterName());
					jobsUnionStandard.setOrganid(quarter.getDept().getOrgan().getOrganId());
					jobsUnionStandard.setOrderno(quarter.getOrderNo());
					jobsUnionStandard.setStandardTerms(form);
					jobsUnionStandard.setIsavailable(1);
					jobsUnionStandard.setStandardMode(0);
					jobsUnionStandard.setStatus(1);
					jobsUnionStandard.setQuarterTrainCode(jobsStandardQuarter.getQuarterTrainCode());
					jobsUnionStandard.setQuarterTrainName(jobsStandardQuarter.getQuarterTrainName());
					jobsUnionStandard.setCreatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
					jobsUnionStandard.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					try{
						jobsUnionStandard.setStandardScore(Double.parseDouble(form.getRefScore()));
					}catch(Exception e){
						
					}
					form.getJobsUnionStandards().add(jobsUnionStandard);
					isAddMap.put(jobsUnionStandard.getJobscode(), jobsUnionStandard.getJobscode());
				}
				
			}
			for(int i=0;i<notAvailableList.size();i++){
				JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)notAvailableList.get(i);
				if(isAddMap.get(jobsUnionStandard.getJobscode()) == null){
					jobsUnionStandard.setStandardTerms(form);
					form.getJobsUnionStandards().add(jobsUnionStandard);
				}
			}
			

		}
		this.getService().saveOld(form);
		standardTermsExService.updateMoreStandardQuarterBank(standardid);
		this.setMsg("保存成功！");
		return "save";
	}
	
	
	public String saveBanks(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		StandardTerms standardTerms = (StandardTerms)standardTermsExService.findDataByKey(this.getId(), StandardTerms.class);
		if(standardTerms!=null){
			Map<String,ThemeBank> themeBankMap = new HashMap<String,ThemeBank>();
			List<ThemeBank> themeBanklist = this.getService().queryAllDate(ThemeBank.class);
			for(int i=0;i<themeBanklist.size();i++){
				ThemeBank themeBank = themeBanklist.get(i);
				themeBankMap.put(themeBank.getThemeBankId(), themeBank);
			}
			
			
			Map themeBankIdsMap = new HashMap();
			String[] bankIdsArr = bankIds.split(",");
			List addjobsStandardThemebanks = new ArrayList();
			for(int j=0;j<bankIdsArr.length;j++){
				ThemeBank themeBank = themeBankMap.get(bankIdsArr[j]);
				if(themeBank!=null){
					JobsStandardThemebank jobsStandardThemebank = new JobsStandardThemebank();
					jobsStandardThemebank.setThemeBankId(themeBank.getThemeBankId());
					jobsStandardThemebank.setThemeBankName(themeBank.getThemeBankName());
					if("onlydcsf".equals(this.getSf())){
						jobsStandardThemebank.setBankPublic("20");
						jobsStandardThemebank.setOrganId(usersess.getCurrentOrganId());
						jobsStandardThemebank.setOrganName(usersess.getCurrentOrganName());
					}else if("onlyall".equals(this.getSf())){
						jobsStandardThemebank.setBankPublic("10");
					}
					jobsStandardThemebank.setStandardTerms(standardTerms);
					jobsStandardThemebank.setCreatedBy(StringUtils.isEmpty(usersess.getEmployeeName())?usersess.getAccount():usersess.getEmployeeName());
					jobsStandardThemebank.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
					themeBankIdsMap.put(jobsStandardThemebank.getThemeBankId(), jobsStandardThemebank);
					addjobsStandardThemebanks.add(jobsStandardThemebank);
				}
			}
			
			
			List<JobsStandardThemebank> jobsStandardThemebanks = standardTerms.getJobsStandardThemebanks();
			if(jobsStandardThemebanks.size()>0){
				//按分类进行分离
				if("onlydcsf".equals(this.getSf())){//onlydcsf onlyall
					for(int i=0;i<jobsStandardThemebanks.size();){
						JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)jobsStandardThemebanks.get(i);
						if("20".equals(jobsStandardThemebank.getBankPublic()) 
								&& jobsStandardThemebank.getOrganId().equals(usersess.getCurrentOrganId())){
							if(themeBankIdsMap.get(jobsStandardThemebank.getThemeBankId())!=null){
								themeBankIdsMap.remove(jobsStandardThemebank.getThemeBankId());
								i++;
							}else{
								jobsStandardThemebanks.remove(i);
							}
						}else{
							i++;
						}
					}
				}else if("onlyall".equals(this.getSf())){
					for(int i=0;i<jobsStandardThemebanks.size();){
						JobsStandardThemebank jobsStandardThemebank = (JobsStandardThemebank)jobsStandardThemebanks.get(i);
						if("10".equals(jobsStandardThemebank.getBankPublic())){
							if(themeBankIdsMap.get(jobsStandardThemebank.getThemeBankId())!=null){
								themeBankIdsMap.remove(jobsStandardThemebank.getThemeBankId());
								i++;
							}else{
								jobsStandardThemebanks.remove(i);
							}
						}else{
							i++;
						}
					}
				}
				
				if(!themeBankIdsMap.isEmpty()){
					Iterator its = themeBankIdsMap.keySet().iterator();
					while(its.hasNext()){
						standardTerms.getJobsStandardThemebanks().add((JobsStandardThemebank)themeBankIdsMap.get((String)its.next()));
					}
				}
			}else{
				standardTerms.setJobsStandardThemebanks(addjobsStandardThemebanks);
			}
			try {
				standardTermsExService.saveOld(standardTerms);
				standardTermsExService.updateMoreStandardQuarterBank(standardTerms.getStandardid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.setMsg("保存成功！");
		return "save";
	}
	
	public StandardTerms getForm() {
		return form;
	}

	public void setForm(StandardTerms form) {
		this.form = form;
	}
	
	public String getStandardid() {
		return standardid;
	}

	public void setStandardid(String standardid) {
		this.standardid = standardid;
	}
	public String getSf() {
		return sf;
	}
	public void setSf(String sf) {
		this.sf = sf;
	}


	public String getBankIds() {
		return bankIds;
	}


	public void setBankIds(String bankIds) {
		this.bankIds = bankIds;
	}
	
	
	
}
