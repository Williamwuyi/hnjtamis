package cn.com.ite.hnjtamis.baseinfo.speciality;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityStandardTypes;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
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
public class SpecialityFormAction extends AbstractFormAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3477961125567014764L;
	private Speciality form;

	public Speciality getForm() {
		return form;
	}

	public void setForm(Speciality form) {
		this.form = form;
	}
	
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		String[] ids = this.getId().split("_");
		if(ids.length==1){
			/// 选择的是专业类型
		}else{
			form = (Speciality)service.findDataByKey(ids[1], Speciality.class);
		}
		
		return "find";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		form = (Speciality)this.jsonToObject(Speciality.class);
		if(StringUtils.isEmpty(form.getSpecialityid())){
			///新增操作公共字段
			form.setOrderno(1+service.getFieldMax(Speciality.class, 
					"orderno"));
			AbstractDomain.addCommonFieldValue(form);
		}else{
			// 修改操作公共字段
			AbstractDomain.updateCommonFieldValue(form);
		}
		// 其他属性赋值
		Map<String,String> pTypeNamesMap =new HashMap();
		String parentTypesNames = "";
		String typesNames="";
		if(form.getSpecialityStandardTypeslist()!=null && form.getSpecialityStandardTypeslist().size()>0){
			List<StandardTerms> standardTermslist =  (List<StandardTerms>)this.getService().queryAllDate(StandardTerms.class);
			if(standardTermslist!=null && standardTermslist.size()>0){
				String nowTime = DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
				Map<String,StandardTerms> standardTermsMap = new HashMap<String,StandardTerms>();
				for(int i=0;i<standardTermslist.size();i++){
					StandardTerms standardTerms = standardTermslist.get(i); 
					standardTermsMap.put(standardTerms.getStandardid(),standardTerms);
				}
				for(int i=0;i<form.getSpecialityStandardTypeslist().size();i++){
					SpecialityStandardTypes specialityStandardTypes = form.getSpecialityStandardTypeslist().get(i);
					StandardTerms standardTerms = standardTermsMap.get(specialityStandardTypes.getTypesId());
					if(standardTerms!=null){
						StandardTypes standardTypes = standardTerms.getStandardTypes();
						if(standardTypes!=null){
							specialityStandardTypes.setParentTypesName(standardTypes.getTypename());
							specialityStandardTypes.setParentTypesId(standardTypes.getJstypeid());
							if(pTypeNamesMap.get(standardTypes.getTypename()) == null){
								parentTypesNames+=standardTypes.getTypename()+";";
								pTypeNamesMap.put(standardTypes.getTypename(),standardTypes.getTypename());
							}
						}
						specialityStandardTypes.setSpeciality(form);
						specialityStandardTypes.setTypesName(standardTerms.getStandardname());
        				specialityStandardTypes.setTypesId(standardTerms.getStandardid());
						specialityStandardTypes.setOrderno(i);
        				specialityStandardTypes.setCreationDate(nowTime);
        				specialityStandardTypes.setCreatedBy(usersess.getEmployeeName());
        				typesNames+=standardTerms.getStandardname()+";";
					}
				}
			}
		}
		form.setParentTypesNames(parentTypesNames);
		form.setTypesNames(typesNames);
		service.save(form);
		return "save";
	}

	public String saveSort() throws Exception{
		List<Speciality> saves = new ArrayList<Speciality>();
		int index = 1;
		for(String id:this.getSortIds()){
			Speciality dt = (Speciality)service.findDataByKey(id, Speciality.class);
			dt.setOrderno(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
}
