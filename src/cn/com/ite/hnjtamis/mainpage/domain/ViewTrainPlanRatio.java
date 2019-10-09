package cn.com.ite.hnjtamis.mainpage.domain;

import java.io.Serializable;
/**
 * <p>Title 岗位达标培训信息系统-管理模块</p>
 * <p>Description 自定义首页使用培训计划人数比例数据集VO，对应视图查询结果
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015May 5, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class ViewTrainPlanRatio implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7258782577918751453L;
	private Integer totalnums; // 总人数
	private Integer plannums; //计划人数
	private Integer factnums; // 实际人数
	private Double planratio; // 计划比例
	private Double factratio; // 实际比例
	public Double getFactratio() {
		return factratio;
	}
	public void setFactratio(Double factratio) {
		this.factratio = factratio;
	}
	public Integer getTotalnums() {
		return totalnums;
	}
	public void setTotalnums(Integer totalnums) {
		this.totalnums = totalnums;
	}
	public Integer getPlannums() {
		return plannums;
	}
	public void setPlannums(Integer plannums) {
		this.plannums = plannums;
	}
	public Double getPlanratio() {
		return planratio;
	}
	public void setPlanratio(Double planratio) {
		this.planratio = planratio;
	}
	public Integer getFactnums() {
		return factnums;
	}
	public void setFactnums(Integer factnums) {
		this.factnums = factnums;
	}
}
