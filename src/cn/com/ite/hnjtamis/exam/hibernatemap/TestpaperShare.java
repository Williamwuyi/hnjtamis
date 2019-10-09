package cn.com.ite.hnjtamis.exam.hibernatemap;

/**
 * TestpaperShare entity. @author MyEclipse Persistence Tools
 */

public class TestpaperShare implements java.io.Serializable {

	// Fields

	private String testpaperShareId;
	private Testpaper testpaper;
	private String shareOrganId;
	private String shareOrgan;
	private String shareUserId;
	private String shareUserName;
	private String organ;
	private String organId;
	private String syncFlag;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String lastUpdatedIdBy;
	private String creationDate;
	private String createdBy;
	private String createdIdBy;

	// Constructors

	/** default constructor */
	public TestpaperShare() {
	}

	/** full constructor */
	public TestpaperShare(Testpaper testpaper, String shareOrganId,
			String shareOrgan, String shareUserId, String shareUserName,
			String organ, String organId, String syncFlag,
			String lastUpdateDate, String lastUpdatedBy,
			String lastUpdatedIdBy, String creationDate, String createdBy,
			String createdIdBy) {
		this.testpaper = testpaper;
		this.shareOrganId = shareOrganId;
		this.shareOrgan = shareOrgan;
		this.shareUserId = shareUserId;
		this.shareUserName = shareUserName;
		this.organ = organ;
		this.organId = organId;
		this.syncFlag = syncFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.lastUpdatedIdBy = lastUpdatedIdBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.createdIdBy = createdIdBy;
	}

	// Property accessors

	public String getTestpaperShareId() {
		return this.testpaperShareId;
	}

	public void setTestpaperShareId(String testpaperShareId) {
		this.testpaperShareId = testpaperShareId;
	}

	public Testpaper getTestpaper() {
		return this.testpaper;
	}

	public void setTestpaper(Testpaper testpaper) {
		this.testpaper = testpaper;
	}

	public String getShareOrganId() {
		return this.shareOrganId;
	}

	public void setShareOrganId(String shareOrganId) {
		this.shareOrganId = shareOrganId;
	}

	public String getShareOrgan() {
		return this.shareOrgan;
	}

	public void setShareOrgan(String shareOrgan) {
		this.shareOrgan = shareOrgan;
	}

	public String getShareUserId() {
		return this.shareUserId;
	}

	public void setShareUserId(String shareUserId) {
		this.shareUserId = shareUserId;
	}

	public String getShareUserName() {
		return this.shareUserName;
	}

	public void setShareUserName(String shareUserName) {
		this.shareUserName = shareUserName;
	}

	public String getOrgan() {
		return this.organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getSyncFlag() {
		return this.syncFlag;
	}

	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}

	public String getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public String getLastUpdatedIdBy() {
		return this.lastUpdatedIdBy;
	}

	public void setLastUpdatedIdBy(String lastUpdatedIdBy) {
		this.lastUpdatedIdBy = lastUpdatedIdBy;
	}

	public String getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedIdBy() {
		return this.createdIdBy;
	}

	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}

}