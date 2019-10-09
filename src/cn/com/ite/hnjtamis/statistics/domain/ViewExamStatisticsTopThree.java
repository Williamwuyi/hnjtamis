package cn.com.ite.hnjtamis.statistics.domain;

import java.io.Serializable;

public class ViewExamStatisticsTopThree implements Serializable {

	/**
	 * 每场考试的前三名人员成绩
	 */
	private static final long serialVersionUID = 4845141760295723182L;
	private String examid; // 考试科目ID
	private String examname; // 考试名称（明细到科目名称）
	private String firstscore; //第1名 姓名+得分
	private String secondscore; // 第2名
	private String thirdscore; // 第3名
	private String datetime; //时间
	public String getExamname() {
		return examname;
	}
	public void setExamname(String examname) {
		this.examname = examname;
	}
	public String getFirstscore() {
		return firstscore;
	}
	public void setFirstscore(String firstscore) {
		this.firstscore = firstscore;
	}
	public String getSecondscore() {
		return secondscore;
	}
	public void setSecondscore(String secondscore) {
		this.secondscore = secondscore;
	}
	public String getThirdscore() {
		return thirdscore;
	}
	public void setThirdscore(String thirdscore) {
		this.thirdscore = thirdscore;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getExamid() {
		return examid;
	}
	public void setExamid(String examid) {
		this.examid = examid;
	}
	
}
