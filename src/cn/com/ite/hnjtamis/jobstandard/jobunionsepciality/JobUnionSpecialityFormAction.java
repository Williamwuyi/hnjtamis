package cn.com.ite.hnjtamis.jobstandard.jobunionsepciality;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.BeanUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位关联专业信息FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionSpecialityFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private JobsUnionSpeciality form;
	private String jobId;	// 岗位ID
	private String specialityIds;
	
	//private JobUnionSpecialityService jobUnionSpecialityService

	public JobsUnionSpeciality getForm() {
		return form;
	}

	public void setForm(JobsUnionSpeciality form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (JobsUnionSpeciality)service.findDataByKey(this.getId(), JobsUnionSpeciality.class);
/// 添加关联属性
		/// 1.当前设置岗位VO
		if(!StringUtils.isEmpty(form.getJobscode())){
			Quarter thisquarter=(Quarter)service.findDataByKey(form.getJobscode(), Quarter.class);
			form.setThisquarter(thisquarter);
		}
		/// 2.添加专业集合
		List<Speciality> specialities = new ArrayList<Speciality>();
		JobUnionSpecialityService uservice=(JobUnionSpecialityService)service;
		//// 根据设定岗位查询集合
		List<JobsUnionSpeciality> res = uservice.findDataByJobId(form.getJobscode());
		//// 从集合中取出条款并添加到jobstandardterms
		for(int i=0;i<res.size();i++){
			Speciality term = res.get(i).getSpeciality();
			if(term!=null){
				specialities.add(term);
			}
		}
		form.setSpecialities(specialities);
		
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		boolean isAdd = false;
		JobUnionSpecialityService pservice=(JobUnionSpecialityService)service;
		form = (JobsUnionSpeciality)this.jsonToObject(JobsUnionSpeciality.class);
		// 其他属性赋值
		//// 当前设定岗位
		Quarter thisquarter=form.getThisquarter();
		if(form.getThisquarter()!=null){
			form.setJobscode(thisquarter.getQuarterId());
			form.setJobsname(thisquarter.getQuarterName());
		}
		
//		List<JobsUnionSpeciality> res = pservice.findDataByJobId(form.getJobscode());
//		if(res==null || res.size()<=0) isAdd=true;
//		/// 先删除原有数据
//		service.deletes(res);
		
		//// 选择的多个晋升岗位。同时保存多条记录
		List<Speciality> specialities = form.getSpecialities();
		if(specialities==null || specialities.size()<=0){
			throw new Exception("请选择至少一条专业信息");
		}
		/// 添加时，同时生成多条记录。因为绑定了晋升岗位集合
		List<JobsUnionSpeciality> res_add = new ArrayList<JobsUnionSpeciality>();
		List<JobsUnionSpeciality> res_upp = new ArrayList<JobsUnionSpeciality>();
		for(int i=0;i<specialities.size();i++){
			Speciality vo=specialities.get(i);
			//// 查询是否有旧记录
			JobsUnionSpeciality tmpVo = pservice.findDataByJobIdAndSpecialityId(form.getJobscode(), vo.getSpecialityid());
			//JobsUnionSpeciality tmpVo = null;
			if (tmpVo==null){/// 为空时添加
				tmpVo = new JobsUnionSpeciality();
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				AbstractDomain.addCommonFieldValue(tmpVo);	
				tmpVo.setJusid(null); /// 置空ID
				tmpVo.setSpeciality(vo);
				res_add.add(tmpVo);
			}else{/// 不为空时，更新
				AbstractDomain.updateCommonFieldValue(tmpVo); // 修改公共字段
				String id = tmpVo.getJusid();/// ID抽出来
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				tmpVo.setJusid(id);/// 重新赋值ID
				tmpVo.setSpeciality(vo);
//				tmpVo.setParentjobscode(vo.getQuarterId()); /// 关键在获得此数据
//				tmpVo.setParentjobsname(vo.getQuarterName()); //// 设置晋升岗位集合
				res_upp.add(tmpVo);
			}
		}
		if (res_add.size()>0)
			service.saves(res_add);
		if (res_upp.size()>0)
			service.saves(res_upp);
		
		//// 设置晋升岗位集合
		return "save";
	}
	
	/**
	 * 设置岗位对应的专业
	 * @return
	 * @throws Exception
	 */
	public String saveStandardSpecialities() throws Exception{
		JobUnionSpecialityService juservice = (JobUnionSpecialityService)getService();
//		String[] jobunionIdArray = this.getJobunionIds().split(",");
		String[] specilityIdArray = this.getSpecialityIds().split(",");
		
		//form = (JobsUnionStandard)this.jsonToObject(JobsUnionStandard.class);
		//String jobid = form.getJobscode(); // 岗位ID
		List<JobsUnionSpeciality> list = juservice.findDataByJobId(jobId);
		// 先删除岗位原有绑定的标准条款
		service.deletes(list);
		
		list.clear();
		/// 获得岗位对象
		Quarter quarter=(Quarter)service.findDataByKey(jobId, Quarter.class);
		//for(String jobunionId:jobunionIdArray){
			
			//JobsUnionStandard union = (JobsUnionStandard)service.findDataByKey(jobunionId, JobsUnionStandard.class);
			//JobsUnionStandard union = null;
			for(String specilityId:specilityIdArray){
				JobsUnionSpeciality union = new JobsUnionSpeciality();
				union.setJobscode(jobId);
				union.setJobsname(quarter.getQuarterName());
				//BeanUtils.copyProperties(union, form);
				//union.setJusdid(null);
				AbstractDomain.addCommonFieldValue(union); /// 添加公共属性值
				Speciality speciality = (Speciality)service.findDataByKey(specilityId, Speciality.class);
				union.setSpeciality(speciality);
				
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
		List<JobsUnionSpeciality> saves = new ArrayList<JobsUnionSpeciality>();
		int index = 1;
		for(String id:this.getSortIds()){
			JobsUnionSpeciality dt = (JobsUnionSpeciality)service.findDataByKey(id, JobsUnionSpeciality.class);
			//dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getSpecialityIds() {
		return specialityIds;
	}

	public void setSpecialityIds(String specialityIds) {
		this.specialityIds = specialityIds;
	}
}
