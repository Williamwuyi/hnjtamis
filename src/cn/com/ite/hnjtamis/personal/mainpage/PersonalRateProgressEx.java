package cn.com.ite.hnjtamis.personal.mainpage;

import java.io.Serializable;

public class PersonalRateProgressEx implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6656731317590243873L;
	private String jobscode;
	private String jobsname;
	private String personname;
	private String personcode;
	private Integer isreachthestd;
	private String reachtime;
	private String contents;
	private Double targetscore;
	private Double totalscore;
	private String applytime;
	private String timeoverdue;
	private String chktime;
	private String chkperson;
	private Integer checkstatus; /// 审核状态
	private Integer reachetype; /// 达标类型 1：学时达标，2：考试达标
	private Double proportionType1; /// 学时达标类型占比
	private Double proportionType2; /// 考试得分占比
	public String getJobscode() {
		return jobscode;
	}
	public void setJobscode(String jobscode) {
		this.jobscode = jobscode;
	}
	public String getJobsname() {
		return jobsname;
	}
	public void setJobsname(String jobsname) {
		this.jobsname = jobsname;
	}
	public String getPersonname() {
		return personname;
	}
	public void setPersonname(String personname) {
		this.personname = personname;
	}
	public String getPersoncode() {
		return personcode;
	}
	public void setPersoncode(String personcode) {
		this.personcode = personcode;
	}
	public Integer getIsreachthestd() {
		return isreachthestd;
	}
	public void setIsreachthestd(Integer isreachthestd) {
		this.isreachthestd = isreachthestd;
	}
	public String getReachtime() {
		return reachtime;
	}
	public void setReachtime(String reachtime) {
		this.reachtime = reachtime;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Double getTotalscore() {
		return totalscore;
	}
	public void setTotalscore(Double totalscore) {
		this.totalscore = totalscore;
	}
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
	public String getTimeoverdue() {
		return timeoverdue;
	}
	public void setTimeoverdue(String timeoverdue) {
		this.timeoverdue = timeoverdue;
	}
	public String getChktime() {
		return chktime;
	}
	public void setChktime(String chktime) {
		this.chktime = chktime;
	}
	public String getChkperson() {
		return chkperson;
	}
	public void setChkperson(String chkperson) {
		this.chkperson = chkperson;
	}
	public Integer getCheckstatus() {
		return checkstatus;
	}
	public void setCheckstatus(Integer checkstatus) {
		this.checkstatus = checkstatus;
	}
	public Integer getReachetype() {
		return reachetype;
	}
	public void setReachetype(Integer reachetype) {
		this.reachetype = reachetype;
	}
	public Double getProportionType1() {
		return proportionType1;
	}
	public void setProportionType1(Double proportionType1) {
		this.proportionType1 = proportionType1;
	}
	public Double getProportionType2() {
		return proportionType2;
	}
	public void setProportionType2(Double proportionType2) {
		this.proportionType2 = proportionType2;
	}
	public Double getTargetscore() {
		return targetscore;
	}
	public void setTargetscore(Double targetscore) {
		this.targetscore = targetscore;
	}
}
