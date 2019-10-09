package cn.com.ite.hnjtamis.train.domain;

/**
 * AccessoryFileTransform entity. @author MyEclipse Persistence Tools
 */

public class AccessoryFileTransform implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 5092124217184615627L;
	private String id;
	private String filePath;
	private String fileName;
	private Integer fileCount;
	private Integer fileType;
	private Integer isFinished;
	private String remark;
	private Integer fullDuaration;

	// Constructors

	/** default constructor */
	public AccessoryFileTransform() {
	}

	/** full constructor */
	public AccessoryFileTransform(String filePath, String fileName,
			Integer fileCount, Integer fileType, Integer isFinished,
			String remark, Integer fullDuaration) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileCount = fileCount;
		this.fileType = fileType;
		this.isFinished = isFinished;
		this.remark = remark;
		this.fullDuaration = fullDuaration;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getFileCount() {
		return this.fileCount;
	}

	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}

	public Integer getFileType() {
		return this.fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

	public Integer getIsFinished() {
		return this.isFinished;
	}

	public void setIsFinished(Integer isFinished) {
		this.isFinished = isFinished;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getFullDuaration() {
		return this.fullDuaration;
	}

	public void setFullDuaration(Integer fullDuaration) {
		this.fullDuaration = fullDuaration;
	}

}