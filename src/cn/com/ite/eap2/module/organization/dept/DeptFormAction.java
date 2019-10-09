package cn.com.ite.eap2.module.organization.dept;

import java.util.*;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.organization.Dept;

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
public class DeptFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 友情链结
	 */
	private Dept form;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (Dept)service.findDataByKey(this.getId(), Dept.class);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (Dept)this.jsonToObject(Dept.class);
		if(StringUtils.isEmpty(form.getDeptId()))
			form.setOrderNo(1+service.getFieldMax(Dept.class, 
					"orderNo","dept.deptId",
					form.getDept()!=null?form.getDept().getDeptId():null));
		service.save(form);
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
			Dept dt = (Dept)service.findDataByKey(id, Dept.class);
			dt.setOrderNo(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
	public Dept getForm() {
		return form;
	}

	public void setForm(Dept form) {
		this.form = form;
	}
}