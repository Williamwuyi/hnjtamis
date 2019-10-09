package cn.com.ite.hnjtamis.personal.rateprogress;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习管理模块</p>
 * <p>Description 个人岗位达标记录FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 9, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PersonalRateProgressListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<PersonalRateProgress> list = new ArrayList<PersonalRateProgress>();
	
	// 查询条件
	private String toptypeidTerm;
	private String startTimeTerm="";  //
	private String endTimeTerm="";
	private String contentsTerm;
	private String nameTerm="";  //
	private String personcodeTerm="";
	
	
	public String getPersoncodeTerm() {
		return personcodeTerm;
	}
	public void setPersoncodeTerm(String personcodeTerm) {
		this.personcodeTerm = personcodeTerm;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	 
	
	public String getStartTimeTerm() {
		return startTimeTerm;
	}
	public void setStartTimeTerm(String startTimeTerm) {
		this.startTimeTerm = startTimeTerm;
	}
	public String getEndTimeTerm() {
		return endTimeTerm;
	}
	public void setEndTimeTerm(String endTimeTerm) {
		this.endTimeTerm = endTimeTerm;
	}
	public String getContentsTerm() {
		return contentsTerm;
	}
	public void setContentsTerm(String contentsTerm) {
		this.contentsTerm = contentsTerm;
	}
	
	public String list()throws Exception{
		UserSession us = LoginAction.getUserSessionInfo();
		setPersoncodeTerm(us.getEmployeeId());
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<PersonalRateProgress>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
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
		service.deleteByKeys(this.getId().split(","), PersonalRateProgress.class);
		this.setMsg("信息删除成功！");
		return "delete";
	}
	
	 
	public List<PersonalRateProgress> getList() {
		return list;
	}
	public void setList(List<PersonalRateProgress> list) {
		this.list = list;
	}
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	 
}
