package cn.com.ite.hnjtamis.statistics.domain;

import java.io.Serializable;

public class ViewExamStatisticsSection implements Serializable {

	/**
	 *  分段统计
	 */
	private static final long serialVersionUID = 9087272551120067816L;
	private String examname; // 考试（明细到科目）
	private String examid; // 考试科目ID
	private String sections; //分段名称
	private Integer nums; // 人数
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
	public String getExamid() {
		return examid;
	}
	public void setExamid(String examid) {
		this.examid = examid;
	}
	public String getSections() {
		return sections;
	}
	public void setSections(String sections) {
		this.sections = sections;
	}
	public Integer getNums() {
		return nums;
	}
	public void setNums(Integer nums) {
		this.nums = nums;
	}
}
