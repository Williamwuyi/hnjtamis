package cn.com.ite.eap2.module.organization.organ;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * <p>Title cn.com.ite.eap2.module.organization.organ.OrganListAction</p>
 * <p>Description 数据字典ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-13 下午01:51:37
 * @version 2.0
 * 
 * @modified records:
 */
public class OrganListAction extends AbstractListAction{
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
	private String typeTerm;
	/**
	 * 地区条件
	 */
	private String areaTerm;
	/**
	 * 顶级机构条件,用于只显示当前机构及以下
	 */
	private String topOrganTerm;
	/**
	 * 是否有效条件
	 */
	private Boolean valid;
	private String validStr;
	/**
	 * 查询结果
	 */
	private List<Organ> organs = new ArrayList<Organ>();
	/**
	 * 导入文件
	 */
	private File xls;
	/**
	 * 导出文件流
	 */
	private InputStream inputStream;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 导入操作
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String importXls() throws Exception{
		OrganService organService= (OrganService)service;
		try{
		  organService.importDate(xls);
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	/**
	 * 导出文件
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String exportXls() throws Exception{
		OrganService organService= (OrganService)service;
		File exportXlsFile = organService.exportDate(this.getId());
		inputStream = new FileInputStream(exportXlsFile);
		this.fileName = new String("机构部门岗位员工用户.xls".getBytes(),"ISO-8859-1");
		return "export";
	}
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		if(!StringUtils.isEmpty(this.getValidStr()))
			this.setValid(this.getValidStr().equals("1"));
		List querys = (List<Organ>)service.queryData("queryHql", this,null);
	    //把线性结构转成树形结构
		organs = service.childObjectHandler(querys, "organId", "organ", 
				"organs",new String[]{"depts","organMangers","users"},null,this.getFilterIds(),"orderNo",topOrganTerm);
		return "list";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		organs = (List<Organ>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), Organ.class);
		this.setMsg("机构删除成功！");
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
	public String getTypeTerm() {
		return typeTerm;
	}
	public void setTypeTerm(String typeTerm) {
		this.typeTerm = typeTerm;
	}
	public String getAreaTerm() {
		return areaTerm;
	}
	public void setAreaTerm(String areaTerm) {
		this.areaTerm = areaTerm;
	}
	public Boolean isValid() {
		return valid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public List<Organ> getOrgans() {
		return organs;
	}
	public void setOrgans(List<Organ> organs) {
		this.organs = organs;
	}
	public String getTopOrganTerm() {
		return topOrganTerm;
	}
	public void setTopOrganTerm(String topOrganTerm) {
		this.topOrganTerm = topOrganTerm;
	}
	public File getXls() {
		return xls;
	}
	public void setXls(File xls) {
		this.xls = xls;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
}
