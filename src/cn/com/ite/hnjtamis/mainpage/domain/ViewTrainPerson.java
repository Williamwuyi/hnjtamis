package cn.com.ite.hnjtamis.mainpage.domain;

import java.io.Serializable;
/** 
 * <p>Title 岗位达标培训信息系统-首页管理模块</p>
 * <p>Description 最新人员达标记录VO </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015May 6, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class ViewTrainPerson implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5478840963702239258L;
	private String employeename; // 员工姓名
	private String deptname;  // 部门名称
	private String subject; // 培训主题
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
}
