package cn.com.ite.hnjtamis.jobstandard.promotion;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.BeanUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.Promotion;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位晋升通道FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 31, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PromotionFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private Promotion form;
	private String jobId;	// 岗位ID
	private String parentJobIds;
	
	//private JobUnionSpecialityService jobUnionSpecialityService

	public Promotion getForm() {
		return form;
	}

	public void setForm(Promotion form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		PromotionService promotionService=(PromotionService)service;
		form = (Promotion)service.findDataByKey(this.getId(), Promotion.class);
		/// 添加关联属性
		
		/// 当前设置岗位VO
		if(!StringUtils.isEmpty(form.getJobscode())){
			Quarter thisquarter=(Quarter)service.findDataByKey(form.getJobscode(), Quarter.class);
			form.setThisquarter(thisquarter);
		}
		/// 设置可晋升的岗位集合
		List<Quarter> quarters = new ArrayList<Quarter>();
		
		List<Promotion> promotions=promotionService.findDataByJobId(form.getJobscode());
		for(int i=0;i<promotions.size();i++){
			String quarterid=promotions.get(i).getParentjobscode();
			if (!StringUtils.isEmpty(quarterid)){
				Quarter quarter = new Quarter();
				quarter.setQuarterId(promotions.get(i).getParentjobscode());
				quarter.setQuarterName(promotions.get(i).getParentjobsname());
				quarters.add(quarter);
			}
			
		}
		
		//// 修改时只能修改当前数据对应的晋升岗位
//		if(!StringUtils.isEmpty(form.getParentjobscode())){ 
//			String quarterid=form.getParentjobscode();
//			Quarter pvo = (Quarter)service.findDataByKey(quarterid, Quarter.class);
//			quarters.add(pvo);
//		}
		form.setPromoteQuarters(quarters);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		PromotionService pservice=(PromotionService)service;
		form = (Promotion)this.jsonToObject(Promotion.class);
		// 其他属性赋值
		//// 当前设定岗位
		/// 查询岗位
		
		Quarter thisquarter=form.getThisquarter();
		if(form.getThisquarter()!=null){
			form.setJobscode(thisquarter.getQuarterId());
			form.setJobsname(thisquarter.getQuarterName());
		}
		thisquarter=(Quarter)service.findDataByKey(form.getJobscode(), Quarter.class);
		/// 岗位名称前，添加部门名称前缀
		String jobsname=thisquarter.getDept().getDeptName()+"->"+thisquarter.getQuarterName();
		form.setJobsname(jobsname);
		
		//// 选择的多个晋升岗位。同时保存多条记录
		List<Quarter> promotionquarters = form.getPromoteQuarters();
		/// 添加时，同时生成多条记录。因为绑定了晋升岗位集合
		List<Promotion> res_add = new ArrayList<Promotion>();
		List<Promotion> res_upp = new ArrayList<Promotion>();
		for(int i=0;i<promotionquarters.size();i++){
			Quarter vo=promotionquarters.get(i);
			//// 查询是否有旧记录
			Promotion tmpVo = pservice.findDataByJobIdAndPromotionJobId(form.getJobscode(), vo.getQuarterId());
			if (tmpVo==null){/// 为空时添加
				tmpVo = new Promotion();
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				AbstractDomain.addCommonFieldValue(tmpVo);
				tmpVo.setPromotionid(null); /// 置空ID
				tmpVo.setParentjobscode(vo.getQuarterId()); /// 关键在获得此数据
				tmpVo.setParentjobsname(vo.getQuarterName()); //// 设置晋升岗位集合
				res_add.add(tmpVo);
			}else{/// 不为空时，更新
				AbstractDomain.updateCommonFieldValue(tmpVo); // 修改公共字段
				String id = tmpVo.getPromotionid();/// ID抽出来
				BeanUtils.copyProperties(tmpVo, form); // 属性对拷
				tmpVo.setPromotionid(id);/// 重新赋值ID
				tmpVo.setParentjobscode(vo.getQuarterId()); /// 关键在获得此数据
				tmpVo.setParentjobsname(vo.getQuarterName()); //// 设置晋升岗位集合
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
	 * 设置(取消)
	 * @return
	 * @throws Exception
	 */
	public String savePromotions() throws Exception{
		PromotionService juservice = (PromotionService)getService();
//		String[] jobunionIdArray = this.getJobunionIds().split(",");
		String[] specilityIdArray = this.getParentJobIds().split(",");
		
		//form = (JobsUnionStandard)this.jsonToObject(JobsUnionStandard.class);
		//String jobid = form.getJobscode(); // 岗位ID
		List<Promotion> list = juservice.findDataByJobId(jobId);
		// 先删除岗位原有绑定的标准条款
		service.deletes(list);
		
		list.clear();
		/// 获得岗位对象
		Quarter quarter=(Quarter)service.findDataByKey(jobId, Quarter.class);
		//for(String jobunionId:jobunionIdArray){
			
			//JobsUnionStandard union = (JobsUnionStandard)service.findDataByKey(jobunionId, JobsUnionStandard.class);
			//JobsUnionStandard union = null;
			for(String specilityId:specilityIdArray){
				Promotion union = new Promotion();
				union.setJobscode(jobId);
				union.setJobsname(quarter.getQuarterName());
				//BeanUtils.copyProperties(union, form);
				//union.setJusdid(null);
				AbstractDomain.addCommonFieldValue(union); /// 添加公共属性值
				//Speciality speciality = (Speciality)service.findDataByKey(specilityId, Speciality.class);
				//union.setSpeciality(speciality);
				
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
		List<Promotion> saves = new ArrayList<Promotion>();
		int index = 1;
		for(String id:this.getSortIds()){
			Promotion dt = (Promotion)service.findDataByKey(id, Promotion.class);
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


	public String getParentJobIds() {
		return parentJobIds;
	}

	public void setParentJobIds(String parentJobIds) {
		this.parentJobIds = parentJobIds;
	}
}
