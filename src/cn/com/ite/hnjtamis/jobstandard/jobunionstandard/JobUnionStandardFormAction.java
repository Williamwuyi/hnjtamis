package cn.com.ite.hnjtamis.jobstandard.jobunionstandard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.BeanUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
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
public class JobUnionStandardFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private JobsUnionStandard form;
	
	private String jobunionIds;
	private String jobId;
	private String standardtermIds;

	public JobsUnionStandard getForm() {
		return form;
	}

	public void setForm(JobsUnionStandard form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (JobsUnionStandard)service.findDataByKey(this.getId(), JobsUnionStandard.class);
		
		/// 添加关联属性
		
		/// 1.当前设置岗位VO
		if(!StringUtils.isEmpty(form.getJobscode())){
			Quarter thisquarter=(Quarter)this.getService().findDataByKey(form.getJobscode(), Quarter.class);
			form.setThisquarter(thisquarter);
		}
		/// 2.添加条款集合
		List<StandardTerms> jobstandardterms = new ArrayList<StandardTerms>();
		JobUnionStandardService uservice=(JobUnionStandardService)service;
		//// 根据设定岗位查询集合
		List<JobsUnionStandard> res = uservice.findDataByJobId(form.getJobscode());
		//// 从集合中取出条款并添加到jobstandardterms
		for(int i=0;i<res.size();i++){
			JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)res.get(i);
			if(jobsUnionStandard.getIsavailable()!=null && jobsUnionStandard.getIsavailable().intValue()!=0){
				StandardTerms term = res.get(i).getStandardTerms();
				if(term!=null && term.getIsavailable()!=null && term.getIsavailable().intValue()!=0){
					jobstandardterms.add(term);
				}
			}
		}
		form.setJobstandardterms(jobstandardterms);
		
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		boolean isAdd = false;
		JobUnionStandardService uservice=(JobUnionStandardService)service;
		form = (JobsUnionStandard)this.jsonToObject(JobsUnionStandard.class);
		
		
	//// 当前设定岗位
		Quarter thisquarter=(Quarter)this.getService().findDataByKey(form.getThisquarter().getQuarterId(), Quarter.class);
		if(form.getThisquarter()!=null){
			form.setJobscode(thisquarter.getQuarterId());
			form.setJobsname(thisquarter.getQuarterName());
			form.setOrganid(thisquarter.getDept().getOrgan().getOrganId());
		}
		
//		List<JobsUnionStandard> res = uservice.findDataByJobId(form.getJobscode());
//		if(res==null || res.size()<=0) isAdd=true;
//		/// 先删除原有数据
//		service.deletes(res);
		
		/// 设定的多个条款信息
		List<StandardTerms> jobstandardterms=form.getJobstandardterms();
		if(jobstandardterms==null || jobstandardterms.size()<=0){
			throw new Exception("请选择至少一条条款信息");
		}
		/// 添加时，同时生成多条记录。因为绑定了多个条款信息
		List<JobsUnionStandard> res_add = new ArrayList<JobsUnionStandard>();
		List<JobsUnionStandard> res_upp = new ArrayList<JobsUnionStandard>();
		Map term = new HashMap();
		term.put("jobscode", thisquarter.getQuarterId());
		List notAvailableList = this.getService().queryData("queryJobsUnionStandardInJobscodeNotAvailable", term, new HashMap());
		Map isAddMap = new HashMap();
		for(int i=0;i<jobstandardterms.size();i++){
			StandardTerms vo=jobstandardterms.get(i);
			//// 查询是否有旧记录
			JobsUnionStandard tmpVo=uservice.findDataByJobIdAndTermId(form.getJobscode(), vo.getStandardid());
			isAddMap.put(vo.getStandardid(), vo.getStandardid());
			//JobsUnionStandard tmpVo=null;
			if (tmpVo==null){/// 为空时添加
				tmpVo = new JobsUnionStandard();
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				AbstractDomain.addCommonFieldValue(tmpVo);	
				tmpVo.setJusdid(null); /// 置空ID
				tmpVo.setStandardTerms(vo);
				res_add.add(tmpVo);
			}else{/// 不为空时，更新
				AbstractDomain.updateCommonFieldValue(tmpVo); // 修改公共字段
				String id = tmpVo.getJusdid();/// ID抽出来
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				tmpVo.setJusdid(id);/// 重新赋值ID
				tmpVo.setStandardTerms(vo);
				res_upp.add(tmpVo);
			}
		}
		for(int i=0;i<notAvailableList.size();i++){
			JobsUnionStandard jobsUnionStandard = (JobsUnionStandard)notAvailableList.get(i);
			if(isAddMap.get(jobsUnionStandard.getJobscode()) == null){
				res_upp.add(jobsUnionStandard);
			}
		}
		if (res_add.size()>0)
			service.saves(res_add);
		if (res_upp.size()>0)
			service.saves(res_upp);
		
		return "save";
	}
	
	/**
	 * 设置岗位对应的标准条款
	 * @return
	 * @throws Exception
	 */
	public String saveStandardTerms() throws Exception{
		JobUnionStandardService juservice = (JobUnionStandardService)getService();
//		String[] jobunionIdArray = this.getJobunionIds().split(",");
		String[] standardtermIdArray = this.getStandardtermIds().split(",");
		
		//form = (JobsUnionStandard)this.jsonToObject(JobsUnionStandard.class);
		//String jobid = form.getJobscode(); // 岗位ID
		List<JobsUnionStandard> list = juservice.findDataByJobId(jobId);
		// 先删除岗位原有绑定的标准条款
		service.deletes(list);
		
		list.clear();
		/// 获得岗位对象
		Quarter quarter=(Quarter)service.findDataByKey(jobId, Quarter.class);
		//for(String jobunionId:jobunionIdArray){
			
			//JobsUnionStandard union = (JobsUnionStandard)service.findDataByKey(jobunionId, JobsUnionStandard.class);
			//JobsUnionStandard union = null;
			for(String standardtermId:standardtermIdArray){
				JobsUnionStandard union = new JobsUnionStandard();
				union.setJobscode(jobId);
				union.setJobsname(quarter.getQuarterName());
				union.setIsavailable(1);
				//BeanUtils.copyProperties(union, form);
				//union.setJusdid(null);
				AbstractDomain.addCommonFieldValue(union); /// 添加公共属性值
				StandardTerms standardTerm = (StandardTerms)service.findDataByKey(standardtermId, StandardTerms.class);
				union.setStandardTerms(standardTerm);
				
				list.add(union);
			}
			//if (StringUtils.isEmpty(union.get))
			//service.update(union);
		//}
		if (list.size()>0){
			service.saves(list);
		}
		return "save";
	}
	
	

	public String saveSort() throws Exception{
		List<JobsUnionStandard> saves = new ArrayList<JobsUnionStandard>();
		int index = 1;
		for(String id:this.getSortIds()){
			JobsUnionStandard dt = (JobsUnionStandard)service.findDataByKey(id, JobsUnionStandard.class);
			dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}

	public String getStandardtermIds() {
		return standardtermIds;
	}

	public void setStandardtermIds(String standardtermIds) {
		this.standardtermIds = standardtermIds;
	}

	public String getJobunionIds() {
		return jobunionIds;
	}

	public void setJobunionIds(String jobunionIds) {
		this.jobunionIds = jobunionIds;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
}
