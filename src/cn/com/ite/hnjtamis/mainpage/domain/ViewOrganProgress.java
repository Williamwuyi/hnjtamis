package cn.com.ite.hnjtamis.mainpage.domain;

import java.io.Serializable;
/**
 * <p>Title 岗位达标培训信息系统-管理模块</p>
 * <p>Description 自定义首页企业达标情况数据集VO，对应视图查询结果
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015May 5, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class ViewOrganProgress implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3111895963359499279L;
	private String organid; // 单位Id
	private String organname; // 单位名称
	private Integer totalnums; // 总人数
	private Integer reachnums; // 达标人数
	private Double reachratio; // 达标比例
	public String getOrganname() {
		return organname;
	}
	public void setOrganname(String organname) {
		this.organname = organname;
	}
	public Integer getTotalnums() {
		return totalnums;
	}
	public void setTotalnums(Integer totalnums) {
		this.totalnums = totalnums;
	}
	public Integer getReachnums() {
		return reachnums;
	}
	public void setReachnums(Integer reachnums) {
		this.reachnums = reachnums;
	}
	public Double getReachratio() {
		return reachratio;
	}
	public void setReachratio(Double reachratio) {
		this.reachratio = reachratio;
	}
	public String getOrganid() {
		return organid;
	}
	public void setOrganid(String organid) {
		this.organid = organid;
	}
	
}
