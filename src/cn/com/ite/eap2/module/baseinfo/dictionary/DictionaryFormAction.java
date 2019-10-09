package cn.com.ite.eap2.module.baseinfo.dictionary;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.baseinfo.DictionaryType;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.dictionary.DictionaryFormAction</p>
 * <p>Description 数据字典FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:21
 * @version 2.0
 * 
 * @modified records:
 */
public class DictionaryFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 友情链结
	 */
	private DictionaryType form;
	private String typeCode;
	private List<Dictionary> datas ;
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (DictionaryType)service.findDataByKey(this.getId(), DictionaryType.class);
		return "find";
	}
	/**
	 * 根据类型编码查询字典数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String findData(){
		datas = (List<Dictionary>)service.queryData("findDataSql", this,null,Dictionary.class,0,0);
		return "findData";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		form = (DictionaryType)this.jsonToObject(DictionaryType.class);
		if(StringUtils.isEmpty(form.getDtId()))
			form.setOrderNo(1+service.getFieldMax(DictionaryType.class, 
					"orderNo","dictionaryType.dtId",
					(form.getDictionaryType()==null?null:form.getDictionaryType().getDtId())));
		int i = 1;
		for(Dictionary dic:form.getDictionaries()){
			dic.setDictionaryType(form);
			dic.setSortNo(i);i++;
		}
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
			DictionaryType dt = (DictionaryType)service.findDataByKey(id, DictionaryType.class);
			dt.setOrderNo(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
	public DictionaryType getForm() {
		return form;
	}

	public void setForm(DictionaryType form) {
		this.form = form;
	}
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public List<Dictionary> getDatas() {
		return datas;
	}
}