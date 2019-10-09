package cn.com.ite.hnjtamis.exam.exampaper.form;

public class ReviewForm {
	private String examId;
	private String needReviewCount;
	private String reviewStartTime;
	private String reviewEndTime;
	private String yjState; // 0:试卷未提交  1: 可阅卷   2:未到阅卷时间  3:已过阅卷时间
	private String unReviewCount;//还未提交待阅卷数量
	private String succReviewCount;//已阅卷数量
	
	
	public String getUnReviewCount() {
		return unReviewCount;
	}
	public void setUnReviewCount(String unReviewCount) {
		this.unReviewCount = unReviewCount;
	}
	public String getSuccReviewCount() {
		return succReviewCount;
	}
	public void setSuccReviewCount(String succReviewCount) {
		this.succReviewCount = succReviewCount;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getNeedReviewCount() {
		return needReviewCount;
	}
	public void setNeedReviewCount(String needReviewCount) {
		this.needReviewCount = needReviewCount;
	}
	public String getReviewStartTime() {
		return reviewStartTime;
	}
	public void setReviewStartTime(String reviewStartTime) {
		this.reviewStartTime = reviewStartTime;
	}
	public String getReviewEndTime() {
		return reviewEndTime;
	}
	public void setReviewEndTime(String reviewEndTime) {
		this.reviewEndTime = reviewEndTime;
	}
	public String getYjState() {
		return yjState;
	}
	public void setYjState(String yjState) {
		this.yjState = yjState;
	}
	
	
}
