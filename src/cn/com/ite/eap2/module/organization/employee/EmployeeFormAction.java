package cn.com.ite.eap2.module.organization.employee;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Employee;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.talent.reg.TalentRegistrationService;

/**
 * <p>Title cn.com.ite.eap2.module.organization.quarter.QuarterFormAction</p>
 * <p>Description 岗位FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-7 上午08:51:31
 * @version 2.0
 * 
 * @modified records:
 */
public class EmployeeFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 岗位
	 */
	private Employee form;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (Employee)service.findDataByKey(this.getId(), Employee.class);
		QuarterStandard quarterStandard = new QuarterStandard();
		if(form.getQuarterTrainId()!=null){
			quarterStandard = (QuarterStandard)service.findDataByKey(form.getQuarterTrainId(), QuarterStandard.class);
			quarterStandard.setQuarterCode(quarterStandard.getDeptName()+"@"+form.getQuarterTrainCode());
		}else if(form.getQuarterTrainCode()!=null){
			Map term = new HashMap();
			term.put("quarterCode",form.getQuarterTrainCode());
			List<QuarterStandard>  list = this.getService().queryData("quarterStandardByQCodeHql", term, null,QuarterStandard.class);
			if(list.size()>0){
				quarterStandard.setQuarterCode(((QuarterStandard)list.get(0)).getDeptName()+"@"+form.getQuarterTrainCode());
			}
		}
		form.setOldQuarterTrainId(quarterStandard.getQuarterId());
		form.setQuarterStandard(quarterStandard);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (Employee)this.jsonToObject(Employee.class);
		if(form.getQuarterStandard()!=null){
			if(form.getQuarterStandard().getQuarterId()!=null){
				QuarterStandard quarterStandard = (QuarterStandard)service.findDataByKey(form.getQuarterStandard().getQuarterId(), QuarterStandard.class);
				if(quarterStandard==null){
					this.setMsg("没有找到对应的标准岗位！");
					return "save";
				}
				form.setQuarterTrainId(quarterStandard.getQuarterId());
				form.setQuarterTrainCode(quarterStandard.getQuarterCode());
				form.setQuarterTrainName(quarterStandard.getQuarterName());
			}else if(form.getQuarterStandard().getQuarterCode()!=null){
				String quarterTrainCode = form.getQuarterStandard().getQuarterCode();
				if(quarterTrainCode.indexOf("@")!=-1){
					Map term = new HashMap();
					term.put("deptName",quarterTrainCode.split("@")[0]);
					term.put("quarterCode",quarterTrainCode.split("@")[1]);
					List<QuarterStandard>  list = this.getService().queryData("quarterStandardByDeptNameAndQCodeHql", term, null);
					if(list.size()>0){
						form.setQuarterTrainId(list.get(0).getQuarterId());
						form.setQuarterTrainName(list.get(0).getQuarterName());
					}
					form.setQuarterTrainCode(quarterTrainCode.split("@")[1]);//用@分割部门@岗位
				}else{
					form.setQuarterTrainCode(quarterTrainCode);
				}
			}
			if(!form.getQuarterTrainId().equals(form.getOldQuarterTrainId()) || form.getQuarter()==null){
				QuarterStandard quarterStandard =  null;
				if(form.getQuarterTrainId()!=null){
					quarterStandard = (QuarterStandard)service.findDataByKey(form.getQuarterTrainId(), QuarterStandard.class);
				}
				Map term = new HashMap();
				term.put("deptId",form.getDept().getDeptId());
				term.put("quarterTrainCode",form.getQuarterTrainCode());
				List<Quarter> qlist = this.getService().queryData("queryQuarterInTrainCode", term, null);
				Quarter quarter = new Quarter();
				if(qlist!=null && qlist.size()>0){
					Quarter qq= qlist.get(0);
					quarter.setQuarter(qq.getQuarter());
					quarter.setQuarterName(quarterStandard!=null ? quarterStandard.getQuarterName() : qq.getQuarterName());	
					quarter.setQuarterTrainName(quarterStandard!=null ? quarterStandard.getQuarterName() : qq.getQuarterTrainName());//培训岗位编码
					quarter.setRemark(qq.getRemark());
					quarter.setLevelCode(qq.getLevelCode());
					quarter.setResponsibility(qq.getResponsibility());
					quarter.setQuarterType(qq.getQuarterType());
				}else{
					quarter.setQuarterName(quarterStandard!=null ? quarterStandard.getQuarterName() : form.getQuarterTrainName());
					quarter.setQuarterTrainName(quarterStandard!=null ? quarterStandard.getQuarterName() : form.getQuarterTrainName());//培训岗位编码
				}
				quarter.setQuarterCode((System.currentTimeMillis()/1000)+"");//使用时间戳
				quarter.setValidation(true);
				quarter.setOrderNo(1+service.getFieldMax(Quarter.class, 
						"orderNo"));
				quarter.setQuarterTrainCode(form.getQuarterTrainCode());//培训岗位编码
				quarter.setQuarterTrainId(form.getQuarterTrainId());//培训岗位ID
				quarter.setDept(form.getDept());
				service.add(quarter);
				form.setQuarter(quarter);
			}
		}
		if(StringUtils.isEmpty(form.getEmployeeId()))
			form.setOrderNo(1+service.getFieldMax(Employee.class, 
					"orderNo"));
		service.save(form);
		
		try{
			TalentRegistrationService talentRegistrationService = (TalentRegistrationService)SpringContextUtil.getBean("talentRegistrationService");
			talentRegistrationService.updateEmployeeQuarter();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "save";
	}
	/**
	 * 排序保存
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		for(String id:this.getSortIds()){
			Employee dt = (Employee)service.findDataByKey(id, Employee.class);
			dt.setOrderNo(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
	public Employee getForm() {
		return form;
	}

	public void setForm(Employee form) {
		this.form = form;
	}
}