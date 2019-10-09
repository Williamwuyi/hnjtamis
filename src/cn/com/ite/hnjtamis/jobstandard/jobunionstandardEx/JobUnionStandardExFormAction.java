package cn.com.ite.hnjtamis.jobstandard.jobunionstandardEx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.speciality.SpecialityService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardThemebank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;

/**
 * 岗位对应的标准管理
 * @author 朱健
 * @create time: 2016年3月7日 下午1:31:07
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardExFormAction extends AbstractFormAction {
 
	private static final long serialVersionUID = -7678509544878673699L;
	
	private StandardTermsExService standardtermsExServer;
	
	private SpecialityService specialityServer;

	private String quarterId;
	
	public String saveSpeciality()throws EapException{
		List<Speciality> specialitylist = this.getService().queryAllDate(Speciality.class); 
		Map<String,Speciality> specialityMap = new HashMap<String,Speciality>();
		for(int i=0;i<specialitylist.size();i++){
			Speciality speciality = specialitylist.get(i);
			specialityMap.put(speciality.getSpecialityid(), speciality);
		}
		
		String[] sids = this.getId().split(",");
		
		Map term = new HashMap();
		term.put("quarterId", quarterId);
		List<JobsUnionSpeciality> jobsUnionSpecialityList = this.getService().queryData("querySpecialityByStandardQuarterId", term, null);
		Map<String,JobsUnionSpeciality> jobsUnionSpecialityMap = new HashMap<String,JobsUnionSpeciality>();
		for(int i=0;i<jobsUnionSpecialityList.size();i++){
			JobsUnionSpeciality tmpVo = jobsUnionSpecialityList.get(i);
			Speciality speciality =specialityMap.get(tmpVo.getSpeciality().getSpecialityid());
			if(speciality!=null){
				tmpVo.setSpeciality(speciality);
			}
			jobsUnionSpecialityMap.put(speciality.getSpecialityid(), tmpVo);
		}
		
		
		List<JobsUnionSpeciality> res_add = new ArrayList<JobsUnionSpeciality>();
		List<JobsUnionSpeciality> res_upp = new ArrayList<JobsUnionSpeciality>();
		for(int j=0;j<sids.length;j++){
			Speciality vo = specialityMap.get(sids[j]);
			JobsUnionSpeciality tmpVo = jobsUnionSpecialityMap.get(vo.getSpecialityid());
			if (tmpVo==null){/// 为空时添加
				tmpVo = new JobsUnionSpeciality();
				AbstractDomain.addCommonFieldValue(tmpVo);	
				tmpVo.setJusid(null); /// 置空ID
				tmpVo.setSpeciality(vo);
				tmpVo.setJobscode(quarterId);
				tmpVo.setOrderno(j);
				tmpVo.setOrganid(null);
				res_add.add(tmpVo);
			}else{/// 不为空时，更新
				AbstractDomain.updateCommonFieldValue(tmpVo); // 修改公共字段
				String id = tmpVo.getJusid();/// ID抽出来
				tmpVo.setJusid(id);/// 重新赋值ID
				tmpVo.setSpeciality(vo);
				tmpVo.setOrderno(j);
				tmpVo.setJobscode(quarterId);
				tmpVo.setOrganid(null);//标准岗位，不可能有机构ID
				res_upp.add(tmpVo);
				jobsUnionSpecialityMap.remove(vo.getSpecialityid());
			}
		}
		List<JobsUnionSpeciality> res_del = new ArrayList<JobsUnionSpeciality>();
		if(!jobsUnionSpecialityMap.keySet().isEmpty()){
			Iterator its = jobsUnionSpecialityMap.keySet().iterator();
			while(its.hasNext()){
				String key = (String)its.next();
				res_del.add(jobsUnionSpecialityMap.get(key));
			}
		}
		if (res_add.size()>0)
			this.getService().saves(res_add);
		if (res_upp.size()>0)
			this.getService().saves(res_upp);
		if (res_del.size()>0)
			this.getService().deletes(res_upp);	
		this.setMsg("保存成功！");
		return "save";
	}

	
	public String addStandardInQuarter()throws EapException{
		try{
			UserSession us = LoginAction.getUserSessionInfo();
			JobUnionStandardExService jobUnionStandardExService = (JobUnionStandardExService)this.getService();
			String quarterTrainCode = quarterId;
			//System.out.println(this.getId());
			//System.out.println(quarterId);
			List<QuarterStandard> quarterStandardList = this.getService().queryAllDate(QuarterStandard.class);
			Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
			for(int i=0;i<quarterStandardList.size();i++){
				QuarterStandard quarterStandard = quarterStandardList.get(i);
				List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
				if(list == null){ list = new ArrayList();}
				list.add(quarterStandard);
				quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
			}
			
			
			String[] ids = this.getId().split(",");
			List<StandardTerms> standardTermsList = this.getService().queryAllDate(StandardTerms.class);
			Map<String,StandardTerms> standardTermsMap = new HashMap<String,StandardTerms>();
			for(int i=0;i<standardTermsList.size();i++){
				StandardTerms standardTerms = standardTermsList.get(i);
				standardTermsMap.put(standardTerms.getStandardid(),standardTerms);
			}
			
			
			List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(quarterTrainCode);
			List<Quarter> quarterList= this.getService().queryAllDate(Quarter.class);
			Map<String,JobsStandardQuarter> standQuarterCodesMap = new HashMap<String,JobsStandardQuarter>();
			List<StandardTerms> savelist = new ArrayList<StandardTerms>();
			if(quarterStandardlist!=null && quarterStandardlist.size()>0){
				
				List<JobsStandardThemebank> allJobsStandardThemebankList = this.getService().queryAllDate(JobsStandardThemebank.class);
				Map<String,List<JobsStandardThemebank>> jobsStandardThemebankMap = new HashMap<String,List<JobsStandardThemebank>>();
				for(int i=0;i<allJobsStandardThemebankList.size();i++){
					JobsStandardThemebank jobsStandardThemebank = allJobsStandardThemebankList.get(i);
					String sid = jobsStandardThemebank.getStandardTerms().getStandardid();
					List<JobsStandardThemebank> tmplist = jobsStandardThemebankMap.get(sid);
					if(tmplist==null){
						tmplist = new ArrayList();
					}
					tmplist.add(jobsStandardThemebank);
					jobsStandardThemebankMap.put(sid,tmplist);
				}
				
				for(int i=0;i<ids.length;i++){
					StandardTerms standardTerms = standardTermsMap.get(ids[i]);
					//Map<String,List<ThemeBank>> standardQuarterBank = standardtermsExServer.queryBankIdByStandardQuarter(standardTerms.getStandardid());
					if(standardTerms!=null){
						for(int j=0;j<quarterStandardlist.size();j++){
							QuarterStandard quarterStandard = quarterStandardlist.get(j);
							List<JobsStandardThemebank> themeBankList = jobsStandardThemebankMap.get(standardTerms.getStandardid());
							if(themeBankList == null)themeBankList=standardTerms.getJobsStandardThemebanks();//standardQuarterBank.get(quarterStandard.getDeptName()+"_"+quarterTrainCode);
							if(themeBankList==null || themeBankList.size() == 0){
								JobsStandardQuarter jobsStandardQuarter = new JobsStandardQuarter();	
								jobsStandardQuarter.setQuarterTrainCode(quarterTrainCode);
								jobsStandardQuarter.setQuarterTrainName(quarterStandard.getQuarterName());
								jobsStandardQuarter.setStandardTerms(standardTerms);
								jobsStandardQuarter.setOrderno(standardTerms.getJobsStandardQuarters().size()+1);
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
									themeBank = standardTerms.getThemeBank();
								}*/
								/*if(themeBank!=null){
									jobsStandardQuarter.setThemeBankCode(themeBank.getThemeBankCode());
									jobsStandardQuarter.setThemeBankId(themeBank.getThemeBankId());
									jobsStandardQuarter.setThemeBankName(themeBank.getThemeBankName());
								}*/
								standardTerms.getJobsStandardQuarters().add(jobsStandardQuarter);
								standQuarterCodesMap.put(jobsStandardQuarter.getQuarterTrainCode(), jobsStandardQuarter);
							}else{
								for(int k=0;k<themeBankList.size();k++){
									JobsStandardThemebank themeBank = themeBankList.get(k);
									JobsStandardQuarter jobsStandardQuarter = new JobsStandardQuarter();	
									jobsStandardQuarter.setQuarterTrainCode(quarterTrainCode);
									jobsStandardQuarter.setQuarterTrainName(quarterStandard.getQuarterName());
									jobsStandardQuarter.setStandardTerms(standardTerms);
									jobsStandardQuarter.setOrderno(standardTerms.getJobsStandardQuarters().size()+1);
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
										themeBank = standardTerms.getThemeBank();
									}*/
									if(themeBank!=null){
										jobsStandardQuarter.setThemeBankCode(themeBank.getThemeBankCode());
										jobsStandardQuarter.setThemeBankId(themeBank.getThemeBankId());
										jobsStandardQuarter.setThemeBankName(themeBank.getThemeBankName());
									}
									standardTerms.getJobsStandardQuarters().add(jobsStandardQuarter);
									standQuarterCodesMap.put(jobsStandardQuarter.getQuarterTrainCode(), jobsStandardQuarter);
								}
							}
						}
						
						
						for(int k=0;k<quarterList.size();k++){
							Quarter quarter=(Quarter)quarterList.get(k);
							JobsStandardQuarter jobsStandardQuarter = standQuarterCodesMap.get(quarter.getQuarterTrainCode());
							if(quarter.getQuarterTrainCode()!=null && jobsStandardQuarter!=null){
								JobsUnionStandard jobsUnionStandard = new JobsUnionStandard();
								jobsUnionStandard.setJobscode(quarter.getQuarterId());
								jobsUnionStandard.setJobsname(quarter.getQuarterName());
								jobsUnionStandard.setOrganid(quarter.getDept().getOrgan().getOrganId());
								jobsUnionStandard.setOrderno(quarter.getOrderNo());
								jobsUnionStandard.setStandardTerms(standardTerms);
								jobsUnionStandard.setIsavailable(1);
								jobsUnionStandard.setStandardMode(0);
								jobsUnionStandard.setStatus(1);
								jobsUnionStandard.setQuarterTrainCode(jobsStandardQuarter.getQuarterTrainCode());
								jobsUnionStandard.setQuarterTrainName(jobsStandardQuarter.getQuarterTrainName());
								jobsUnionStandard.setCreatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
								jobsUnionStandard.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
								try{
									jobsUnionStandard.setStandardScore(Double.parseDouble(standardTerms.getRefScore()));
								}catch(Exception e){
									
								}
								standardTerms.getJobsUnionStandards().add(jobsUnionStandard);
							}
							
						}
						
						savelist.add(standardTerms);
					}
				}
				
			}
			if(savelist.size()>0){
				this.getService().saves(savelist);
			}
			//处理子的添加问题
			for(int j=0;j<quarterStandardlist.size();j++){
				QuarterStandard quarterStandard = quarterStandardlist.get(j);
				jobUnionStandardExService.saveChildeJobUnionStandard(quarterStandard.getQuarterId());
			}
			
			//处理岗位<->条款<->专业的联系
			this.getSpecialityServer().saveSyncJobsUnionSpeciality();
			
			this.setMsg("保存成功！");
		}catch(Exception e){
			e.printStackTrace();
			this.setMsg("保存失败！");
		}
		return "save";
	}
	
	
	public String getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}


	public StandardTermsExService getStandardtermsExServer() {
		return standardtermsExServer;
	}


	public void setStandardtermsExServer(
			StandardTermsExService standardtermsExServer) {
		this.standardtermsExServer = standardtermsExServer;
	}


	public SpecialityService getSpecialityServer() {
		return specialityServer;
	}


	public void setSpecialityServer(SpecialityService specialityServer) {
		this.specialityServer = specialityServer;
	}
	
	
	
}
