package cn.com.ite.hnjtamis.statistics.domain;

import java.io.Serializable;

public class ViewExamStatistics1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -247737050316244389L;
	private String examname; ///考试名称+考试科目名称
	private Integer planpeoplenum; // 报名人数
	private Integer factpeoplenum; // 实到人数
	private Integer passpeoplenum; // 及格人数
	private Double totalscore; // 总分
	private Double avgscore; // 平均得分
	private Double passrate; // 及格率
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
	
	public Double getPassrate() {
		return passrate;
	}
	public void setPassrate(Double passrate) {
		this.passrate = passrate;
	}
	public Integer getPlanpeoplenum() {
		return planpeoplenum;
	}
	public void setPlanpeoplenum(Integer planpeoplenum) {
		this.planpeoplenum = planpeoplenum;
	}
	public Integer getFactpeoplenum() {
		return factpeoplenum;
	}
	public void setFactpeoplenum(Integer factpeoplenum) {
		this.factpeoplenum = factpeoplenum;
	}
	public Integer getPasspeoplenum() {
		return passpeoplenum;
	}
	public void setPasspeoplenum(Integer passpeoplenum) {
		this.passpeoplenum = passpeoplenum;
	}
	public Double getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(Double totalscore) {
		this.totalscore = totalscore;
	}
	public Double getAvgscore() {
		return avgscore;
	}
	public void setAvgscore(Double avgscore) {
		this.avgscore = avgscore;
	}

}
