package cn.com.ite.eap2.module.organization.organ;


import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganFormAction</p>
 * <p>Description 机构部门FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:21
 * @version 2.0
 * 
 * @modified records:
 */
public class OrganFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 友情链结
	 */
	private Organ form;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (Organ)service.findDataByKey(this.getId(), Organ.class);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (Organ)this.jsonToObject(Organ.class);
		try{
		((OrganService)service).saveOrgan(form);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	/**
	 * 排序保存
	 * @return
	 * @throws Exception
	 */
	public String saveSort() throws Exception{
		((OrganService)service).saveSort(this.getSortIds());
		return "save";
	}
	public Organ getForm() {
		return form;
	}

	public void setForm(Organ form) {
		this.form = form;
	}
}