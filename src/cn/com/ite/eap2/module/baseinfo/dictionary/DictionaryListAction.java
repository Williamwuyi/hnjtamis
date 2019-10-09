package cn.com.ite.eap2.module.baseinfo.dictionary;

import java.util.*;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.DictionaryType;

/**
 * <p>Title cn.com.ite.eap2.module.baseinfo.dictionary.DictionaryListAction</p>
 * <p>Description 数据字典ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class DictionaryListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753779816555403L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 编码条件 
	 */
	private String codeTerm;
	/**
	 * 类型条件
	 */
	private int typeTerm;
	/**
	 * 是否远程调用
	 */
	private String codesTerm;
	/**
	 * 查询结果
	 */
	private List<DictionaryType> dictionaryTypes = new ArrayList<DictionaryType>();
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		dictionaryTypes = (List<DictionaryType>)service.queryData("queryHql", this,this.getSortMap(),
				0, 0);
	    //把线性结构转成树形结构
		dictionaryTypes = service.childObjectHandler(dictionaryTypes, "dtId", "dictionaryType", 
				"dictionaryTypes",new String[]{"dictionaries","sysDics"},null,this.getFilterIds(),"orderNo",null);
	    return "list";
	}
	public String query() throws Exception {
		Map term = new HashMap();
		term.put("codesTerm", codesTerm.split(","));
		dictionaryTypes = (List<DictionaryType>)service.queryData("queryRmoteHql", term,null,0, 0);
		return "query";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		dictionaryTypes = (List<DictionaryType>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	public List<DictionaryType> getDictionaryTypes() {
		return dictionaryTypes;
	}
	public void setDictionaryTypes(List<DictionaryType> dictionaryTypes) {
		this.dictionaryTypes = dictionaryTypes;
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), DictionaryType.class);
		this.setMsg("字典类型删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public String getCodeTerm() {
		return codeTerm;
	}
	public void setCodeTerm(String codeTerm) {
		this.codeTerm = codeTerm;
	}
	public int getTypeTerm() {
		return typeTerm;
	}
	public void setTypeTerm(int typeTerm) {
		this.typeTerm = typeTerm;
	}
	public void setCodesTerm(String codesTerm) {
		this.codesTerm = codesTerm;
	}
}
