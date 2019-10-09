package cn.com.ite.hnjtamis.statistics.domain;

import java.io.Serializable;

public class ViewExamStatisticsFailList implements Serializable {

	/**
	 * 每场考试的不及格人数
	 */
	private static final long serialVersionUID = 7986526058275267437L;
	private String employeename; // 姓名
	private Double score; //得分
	private String datetime; // 时间
	private String examname; // 考试（明细到科目）
	public String getEmployeename() {
		return employeename;
	}
	public void setEmployeename(String employeename) {
		this.employeename = employeename;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
}
