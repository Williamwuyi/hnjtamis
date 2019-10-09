package cn.com.ite.eap2.module.organization.quarter;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.terms.StandardTermsService;

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
public class QuarterFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 友情链结
	 */
	private Quarter form;
	
	private StandardTermsService standardtermsServer;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (Quarter)service.findDataByKey(this.getId(), Quarter.class);
		QuarterStandard quarterStandard = new QuarterStandard();
		if(form.getQuarterTrainId()!=null){
			quarterStandard = (QuarterStandard)service.findDataByKey(form.getQuarterTrainId(), QuarterStandard.class);
			quarterStandard.setQuarterCode(quarterStandard.getDeptName()+"@"+form.getQuarterTrainCode());
		}else if(form.getQuarterTrainCode()!=null){
			Map term = new HashMap();
			term.put("quarterCode",form.getQuarterTrainCode());
			List<QuarterStandard>  list = this.getService().queryData("quarterStandardByQCodeHql", term, null);
			if(list.size()>0){
				quarterStandard.setQuarterCode(list.get(0).getDeptName()+"@"+form.getQuarterTrainCode());
			}
		}
		form.setQuarterStandard(quarterStandard);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		try{
		form = (Quarter)this.jsonToObject(Quarter.class);
		if(form.getQuarterStandard()!=null){
			if(form.getQuarterStandard().getQuarterId()!=null){
				QuarterStandard quarterStandard = (QuarterStandard)service.findDataByKey(form.getQuarterStandard().getQuarterId(), QuarterStandard.class);
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
		}
		if(StringUtils.isEmpty(form.getQuarterId()))
			form.setOrderNo(1+service.getFieldMax(Quarter.class, 
					"orderNo","quarter.quarterId",(form.getQuarter()==null?null:form.getQuarter().getQuarterId())));
		service.save(form);
		
		
		standardtermsServer.updateUnionStandardByStandard();
		service.excuteQl("updateEmployeeDept", null);
		
		if(form.getQuarterId()!=null){
			Map pmap = new HashMap();
			pmap.put("quarterId1", form.getQuarterId());
			pmap.put("quarterId2", form.getQuarterId());
			pmap.put("quarterId3", form.getQuarterId());
			pmap.put("quarterId4", form.getQuarterId());
			   
			List params = new ArrayList();
			params.add(pmap);
			service.excuteQl("updateNewEmployeeTrainQuarter", params);
		}
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
			Quarter dt = (Quarter)service.findDataByKey(id, Quarter.class);
			dt.setOrderNo(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
	public Quarter getForm() {
		return form;
	}

	public void setForm(Quarter form) {
		this.form = form;
	}
	
	public StandardTermsService getStandardtermsServer() {
		return standardtermsServer;
	}
	
	public void setStandardtermsServer(StandardTermsService standardtermsServer) {
		this.standardtermsServer = standardtermsServer;
	}
	
	
	
}