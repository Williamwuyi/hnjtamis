package cn.com.ite.hnjtamis.exam.base.exampublicuser.form;

public class ExamPublicSelfUserForm {
	private String publicId;
	private String examTitle;
	private String examStartTime;
	private String examEndTime;
	private boolean isDeadLine;
	
	private String userId;
	private String isDel;
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getExamTitle() {
		return examTitle;
	}
	public void setExamTitle(String examTitle) {
		this.examTitle = examTitle;
	}
	public String getExamStartTime() {
		return examStartTime;
	}
	public void setExamStartTime(String examStartTime) {
		this.examStartTime = examStartTime;
	}
	public String getExamEndTime() {
		return examEndTime;
	}
	public void setExamEndTime(String examEndTime) {
		this.examEndTime = examEndTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getIsDel() {
		return isDel;
	}
	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
	
	public void setIsDeadLine(boolean deadline){
		this.isDeadLine=deadline;
	}
	public boolean getIsDeadLine(){
		return isDeadLine;
	}
	
}
