package cn.com.ite.hnjtamis.train.online;

public class StudyLeaf implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5970183889590370159L;
	private String id;
	private String implId;
	private String subject;
	private String courseId;
	private String courseTitle;
	private int type;
	private int sortNo;
	private String leafId;
	private String fileName;
	private String filePath;
	private int orderNo;
	private int duration;
	private int needDuration;
	private Integer isRequired;
	private String startTime;
	private String endTime;
	private int studyCount;
	private int level;
	private Integer finishStatus;
	private String unit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImplId() {
		return implId;
	}

	public void setImplId(String implId) {
		this.implId = implId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSortNo() {
		return sortNo;
	}

	public void setSortNo(int sortNo) {
		this.sortNo = sortNo;
	}

	public String getLeafId() {
		return leafId;
	}

	public void setLeafId(String leafId) {
		this.leafId = leafId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getNeedDuration() {
		return needDuration;
	}

	public void setNeedDuration(int needDuration) {
		this.needDuration = needDuration;
	}

	public Integer getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
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

	public int getStudyCount() {
		return studyCount;
	}

	public void setStudyCount(int studyCount) {
		this.studyCount = studyCount;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Integer getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(Integer finishStatus) {
		this.finishStatus = finishStatus;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
}
