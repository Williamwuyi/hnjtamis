package cn.com.ite.eap2.module.organization.employeeBatchModify;


import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.EmployeeModifyBatch;

/**
 *
 * <p>Title cn.com.ite.eap2.module.organization.employeeBatchModify.EmplyeeBatchModifyFormAction</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2018</p>
 * @create time: 2018年12月25日 上午9:35:07
 * @version 1.0
 * 
 * @modified records:
 */
public class EmplyeeBatchModifyFormAction extends AbstractFormAction{

	private static final long serialVersionUID = 7640713133308874012L;
	private EmployeeModifyBatch form;
	
	/**
	 * 查询
	 * @return
	 * @modified
	 */
	public String find(){
		form = (EmployeeModifyBatch)service.findDataByKey(this.getId(), EmployeeModifyBatch.class);
		return "find";
	}
	
	/**
	 * 保存
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (EmployeeModifyBatch)this.jsonToObject(EmployeeModifyBatch.class);
		try{
			this.getService().save(form);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}

	public EmployeeModifyBatch getForm() {
		return form;
	}

	public void setForm(EmployeeModifyBatch form) {
		this.form = form;
	}
}