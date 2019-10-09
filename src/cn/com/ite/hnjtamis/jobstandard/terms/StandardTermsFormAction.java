package cn.com.ite.hnjtamis.jobstandard.terms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private StandardTerms form;

	public StandardTerms getForm() {
		return form;
	}

	public void setForm(StandardTerms form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (StandardTerms)service.findDataByKey(this.getId(), StandardTerms.class);
		List<JobsUnionStandard> list = form.getJobsUnionStandards();
		List<JobsUnionStandard> newlist = new ArrayList<JobsUnionStandard>();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)list.get(i);
				if(jobsUnionStandard.getIsavailable()!=null && jobsUnionStandard.getIsavailable().intValue()!=0){
					newlist.add(jobsUnionStandard);
				}
			}
		}
		form.setJobsUnionStandards(newlist);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		form = (StandardTerms)this.jsonToObject(StandardTerms.class);
		List notAvailableList = new ArrayList();
		if(StringUtils.isEmpty(form.getStandardid())){
			///新增操作公共字段
			form.setOrderno(1+service.getFieldMax(StandardTerms.class, 
					"orderno"));
			AbstractDomain.addCommonFieldValue(form);
		}else{
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
			
			Map term = new HashMap();
			term.put("standardid", form.getStandardid());
			notAvailableList = this.getService().queryData("queryJobsUnionStandardNotAvailable", term, new HashMap());
		}
		// 其他属性赋值
		ThemeBank themeBank = null;
		if(form.getThemeBank()!=null && form.getThemeBank().getThemeBankId()!=null){
			themeBank= (ThemeBank)this.getService().findDataByKey(form.getThemeBank().getThemeBankId(), ThemeBank.class);
			if(themeBank!=null){
				form.setThemeBankName(themeBank.getThemeBankName());
				form.setThemeBankCode(themeBank.getThemeBankCode());
				form.setThemeBank(themeBank);
			}
		}else{
			form.setThemeBankName("");
			form.setThemeBankCode("");
			form.setThemeBank(null);
		}
		service.save(form);
		if(form.getJobsStandardQuarters()!=null 
				&& form.getJobsStandardQuarters().size()>0){
			List<QuarterStandard> quarterStandardList = this.getService().queryAllDate(QuarterStandard.class);
			Map<String,List<QuarterStandard>> quarterStandardMap = new HashMap<String,List<QuarterStandard>>();
			for(int i=0;i<quarterStandardList.size();i++){
				QuarterStandard quarterStandard = quarterStandardList.get(i);
				List list = quarterStandardMap.get(quarterStandard.getQuarterCode());//发现编码有重复的，因此用list
				if(list == null){ list = new ArrayList();}
				list.add(quarterStandard);
				quarterStandardMap.put(quarterStandard.getQuarterCode(),list);
			}
			
			
			Map<String,JobsStandardQuarter> standQuarterCodesMap = new HashMap<String,JobsStandardQuarter>();
			int standardQuarterIndex = 0;
			for(int i=0;i<form.getJobsStandardQuarters().size();i++){
				JobsStandardQuarter jobsStandardQuarter = (JobsStandardQuarter)form.getJobsStandardQuarters().get(i);
				List<QuarterStandard> quarterStandardlist = quarterStandardMap.get(jobsStandardQuarter.getQuarterTrainCode());
				if(quarterStandardlist!=null && quarterStandardlist.size()>0){
					QuarterStandard quarterStandard = quarterStandardlist.get(0);
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
					if(themeBank!=null){
						jobsStandardQuarter.setThemeBankCode(themeBank.getThemeBankCode());
						jobsStandardQuarter.setThemeBankId(themeBank.getThemeBankId());
						jobsStandardQuarter.setThemeBankName(themeBank.getThemeBankName());
					}
					standQuarterCodesMap.put(jobsStandardQuarter.getQuarterTrainCode(), jobsStandardQuarter);
					
					if(quarterStandardlist.size()>1){
						for(int t=0;t<quarterStandardList.size();t++){
							quarterStandard = quarterStandardList.get(t);
							jobsStandardQuarter = new JobsStandardQuarter();
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
							if(themeBank!=null){
								jobsStandardQuarter.setThemeBankCode(themeBank.getThemeBankCode());
								jobsStandardQuarter.setThemeBankId(themeBank.getThemeBankId());
								jobsStandardQuarter.setThemeBankName(themeBank.getThemeBankName());
							}
							form.getJobsStandardQuarters().add(jobsStandardQuarter);
						}
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
			
			/*
			for(int i=0;i<form.getJobsUnionStandards().size();i++){
				JobsUnionStandard jobsUnionStandard = form.getJobsUnionStandards().get(i);
				Quarter quarter=(Quarter)quarterMap.get(jobsUnionStandard.getJobscode());
				jobsUnionStandard.setOrganid(quarter!=null && quarter.getDept()!=null && quarter.getDept().getOrgan()!=null
						? quarter.getDept().getOrgan().getOrganId() : null);
				jobsUnionStandard.setStandardTerms(form);
				jobsUnionStandard.setIsavailable(1);
				jobsUnionStandard.setStandardMode(0);
				jobsUnionStandard.setStatus(1);
				jobsUnionStandard.setCreatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
				jobsUnionStandard.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
				
				isAddMap.put(jobsUnionStandard.getJobscode(), jobsUnionStandard.getJobscode());
			}*/
			
			for(int i=0;i<notAvailableList.size();i++){
				JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)notAvailableList.get(i);
				if(isAddMap.get(jobsUnionStandard.getJobscode()) == null){
					jobsUnionStandard.setStandardTerms(form);
					form.getJobsUnionStandards().add(jobsUnionStandard);
				}
			}
			
			
		}
		service.save(form);
		return "save";
	}

	public String saveSort() throws Exception{
		try{
			List saves = new ArrayList();
			int index = 1;
			for(String id:this.getSortIds()){
				StandardTerms dt = (StandardTerms)service.findDataByKey(id, StandardTerms.class);
				dt.setOrderno(index*10);
				saves.add(dt);
				index++;
			}
			service.saves(saves);
		}catch(Exception e){
			e.printStackTrace();
			this.setMsg("处理失败!");
		}
		return "save";
	}
}
