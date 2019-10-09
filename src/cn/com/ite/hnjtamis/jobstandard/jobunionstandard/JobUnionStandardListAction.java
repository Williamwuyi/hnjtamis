package cn.com.ite.hnjtamis.jobstandard.jobunionstandard;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class JobUnionStandardListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<JobsUnionStandard> list = new ArrayList<JobsUnionStandard>();
	
	// 查询条件
	private String toptypeidTerm;
	private String jobnameTerm="";  //
	private String validStr;
	
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	
	public String list()throws Exception{
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<JobsUnionStandard>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		service.deleteByKeys(this.getId().split(","), JobsUnionStandard.class);
		this.setMsg("信息删除成功！");
		return "delete";
	}
	
	
	
	
	public List<JobsUnionStandard> getList() {
		return list;
	}
	public void setList(List<JobsUnionStandard> list) {
		this.list = list;
	}
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public String getJobnameTerm() {
		//setTypenameTerm("3333");
		return jobnameTerm;
	}
	public void setJobnameTerm(String jobnameTerm) {
		this.jobnameTerm = jobnameTerm;
	}
}
