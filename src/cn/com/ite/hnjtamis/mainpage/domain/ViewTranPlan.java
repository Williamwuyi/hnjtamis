package cn.com.ite.hnjtamis.mainpage.domain;

import java.io.Serializable;

import cn.com.ite.eap2.common.utils.StringUtils;

/**
 * <p>Title 岗位达标培训信息系统-管理模块</p>
 * <p>Description 自定义首页使用培训计划数据集VO，对应视图查询结果
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015May 5, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class ViewTranPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8631022140285836444L;
	private String deptname; // 部门名称
	private Integer plannums; // 计划人数
	private Integer factnums; // 实际人数
	private Integer completenums; // 完成人数
	private Double duration; //培训时长（小时）
	private Double completeratio; // 培训完成比率
	private Double planratio; // 实际学习占总学习人数比例
	
	////// 以下2个属性仅在 基层单位首页显示计划时用到。培训计划并不是主要业务数据，且有可能与培训实施等业务数据无任何关联
	private String datetime; // 计划周期
	private Integer status; // 计划状态
	private Integer isfinished; // 计划是否已完成
	private String statusstr; // 计划状态描述
	public String getStatusstr() {
//		if(StringUtils.isEmpty(datetime))
//			statusstr= "无计划";
//		else if(isfinished!=null && isfinished==1)
//			statusstr= "已完成";
//		else
//			statusstr= "未完成";
			
		if(StringUtils.isEmpty(datetime))
			statusstr= "<span style='color:gray;'>无计划</span>";
		else if(isfinished!=null && isfinished==1)
			statusstr= "<span style='color:green;'>已完成</span>";
		else
			statusstr= "<span style='color:red;'>未完成</span>";
		return statusstr;
	}
	public void setStatusstr(String statusstr) {
		this.statusstr = statusstr;
	}
	public Integer getIsfinished() {
		return isfinished;
	}
	public void setIsfinished(Integer isfinished) {
		this.isfinished = isfinished;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Double getPlanratio() {
		return planratio;
	}
	public void setPlanratio(Double planratio) {
		this.planratio = planratio;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public Integer getPlannums() {
		return plannums;
	}
	public void setPlannums(Integer plannums) {
		this.plannums = plannums;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public Integer getFactnums() {
		return factnums;
	}
	public void setFactnums(Integer factnums) {
		this.factnums = factnums;
	}
	public Integer getCompletenums() {
		return completenums;
	}
	public void setCompletenums(Integer completenums) {
		this.completenums = completenums;
	}
	public Double getCompleteratio() {
		return completeratio;
	}
	public void setCompleteratio(Double completeratio) {
		this.completeratio = completeratio;
	}
}
