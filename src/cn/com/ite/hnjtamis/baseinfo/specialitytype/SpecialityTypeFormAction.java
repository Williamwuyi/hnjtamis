package cn.com.ite.hnjtamis.baseinfo.specialitytype;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;
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
public class SpecialityTypeFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private SpecialityType form;

	public SpecialityType getForm() {
		return form;
	}

	public void setForm(SpecialityType form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (SpecialityType)service.findDataByKey(this.getId(), SpecialityType.class);
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (SpecialityType)this.jsonToObject(SpecialityType.class);
		if(StringUtils.isEmpty(form.getBstid())){
			///新增操作公共字段
			form.setOrderno(1+service.getFieldMax(SpecialityType.class, 
					"orderno"));
			AbstractDomain.addCommonFieldValue(form);
		}else{
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
		}
		// 其他属性赋值
		
		service.save(form);
		return "save";
	}

	public String saveSort() throws Exception{
		List<SpecialityType> saves = new ArrayList<SpecialityType>();
		int index = 1;
		for(String id:this.getSortIds()){
			SpecialityType dt = (SpecialityType)service.findDataByKey(id, SpecialityType.class);
			dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
}
