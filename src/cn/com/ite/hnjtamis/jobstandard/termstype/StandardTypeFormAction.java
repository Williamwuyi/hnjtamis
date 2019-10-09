package cn.com.ite.hnjtamis.jobstandard.termstype;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;

import java.util.ArrayList;
import java.util.List;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTypeFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private StandardTypes form;

	public StandardTypes getForm() {
		return form;
	}

	public void setForm(StandardTypes form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (StandardTypes)service.findDataByKey(this.getId(), StandardTypes.class);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (StandardTypes)this.jsonToObject(StandardTypes.class);
		if(StringUtils.isEmpty(form.getJstypeid())){
			///新增操作公共字段
			form.setOrderno(1+service.getFieldMax(StandardTypes.class, 
					"orderno"));
			AbstractDomain.addCommonFieldValue(form);
		}else{
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
		}
		// 其他属性赋值
		form.setParentName(form.getParentSpeciltype()!=null && form.getParentSpeciltype().getJstypeid()!=null ? form.getParentSpeciltype().getTypename():"");
		service.save(form);
		StandardTypeService standardTypeService =(StandardTypeService)service;
		standardTypeService.updateParentTypeName();
		return "save";
	}

	public String saveSort() throws Exception{
		List<StandardTypes> saves = new ArrayList<StandardTypes>();
		int index = 1;
		for(String id:this.getSortIds()){
			StandardTypes dt = (StandardTypes)service.findDataByKey(id, StandardTypes.class);
			dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
}
