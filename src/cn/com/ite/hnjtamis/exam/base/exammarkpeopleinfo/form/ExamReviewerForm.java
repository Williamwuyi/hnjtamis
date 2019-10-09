package cn.com.ite.hnjtamis.exam.base.exammarkpeopleinfo.form;

import java.util.*;

import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;

public class ExamReviewerForm {
	private String examId;//考试科目
	private String examName;
	private String startTime;
	private String endTime;
	
	private List<ExamCReviewerForm> reviewerChilds = new ArrayList<ExamCReviewerForm>();

	

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<ExamCReviewerForm> getReviewerChilds() {
		return reviewerChilds;
	}

	public void setReviewerChilds(List<ExamCReviewerForm> reviewerChilds) {
		this.reviewerChilds = reviewerChilds;
	}
	
	
}
