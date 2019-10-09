package cn.com.ite.hnjtamis.client.domain;

/**
 * ClientFile entity. @author MyEclipse Persistence Tools
 */

public class ClientFile implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1763411312397434761L;
	private String id;
	private String fileName;
	private Long fileSize;
	private String uploadTime;
	private String fileVersion;
	private byte[] fileContent;
	private String filePath;
	private Integer isRecoverVer;
	private String lastUpdateTime;

	// Constructors

	/** default constructor */
	public ClientFile() {
	}

	/** full constructor */
	public ClientFile(String fileName, Long fileSize, String uploadTime,
			String fileVersion, byte[] fileContent) {
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.uploadTime = uploadTime;
		this.fileVersion = fileVersion;
		this.fileContent = fileContent;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileVersion() {
		return this.fileVersion;
	}

	public void setFileVersion(String fileVersion) {
		this.fileVersion = fileVersion;
	}

	public byte[] getFileContent() {
		return this.fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getIsRecoverVer() {
		return isRecoverVer;
	}

	public void setIsRecoverVer(Integer isRecoverVer) {
		this.isRecoverVer = isRecoverVer;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}