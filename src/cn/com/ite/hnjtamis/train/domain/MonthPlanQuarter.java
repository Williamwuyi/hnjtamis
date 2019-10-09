package cn.com.ite.hnjtamis.train.domain;

import cn.com.ite.eap2.domain.organization.Quarter;

/**
 * MonthPlanQuarter entity. @author MyEclipse Persistence Tools
 */

public class MonthPlanQuarter implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -4376717061533262542L;
	private String id;
	private Quarter quarter;
	private MonthPlan monthPlan;
	private String planContent;

	// Constructors

	/** default constructor */
	public MonthPlanQuarter() {
	}

	/** full constructor */
	public MonthPlanQuarter(Quarter quarter, MonthPlan monthPlan,
			String planContent) {
		this.quarter = quarter;
		this.monthPlan = monthPlan;
		this.planContent = planContent;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Quarter getQuarter() {
		return this.quarter;
	}

	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}

	public MonthPlan getMonthPlan() {
		return this.monthPlan;
	}

	public void setMonthPlan(MonthPlan monthPlan) {
		this.monthPlan = monthPlan;
	}

	public String getPlanContent() {
		return this.planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

}