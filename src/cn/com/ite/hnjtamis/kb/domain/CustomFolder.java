package cn.com.ite.hnjtamis.kb.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * CustomFolder entity. @author MyEclipse Persistence Tools
 */

public class CustomFolder implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 7172234029142065058L;
	private String id;
	private CustomFolder parentFolder;
	private String name;
	private Integer isDel;
	private Integer syncStatus;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String creationDate;
	private String createdBy;
	private String organId;
	private List<CustomFolder> childFolders = new ArrayList<CustomFolder>();

	// Constructors

	/** default constructor */
	public CustomFolder() {
	}

	/** full constructor */
	public CustomFolder(CustomFolder parentFolder, String name, Integer isDel,
			Integer syncStatus, String lastUpdateDate, String lastUpdatedBy,
			String creationDate, String createdBy, String organId,
			List<CustomFolder> childFolders) {
		this.parentFolder = parentFolder;
		this.name = name;
		this.isDel = isDel;
		this.syncStatus = syncStatus;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.organId = organId;
		this.childFolders = childFolders;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CustomFolder getParentFolder() {
		return this.parentFolder;
	}

	public void setParentFolder(CustomFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsDel() {
		return this.isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getSyncStatus() {
		return this.syncStatus;
	}

	public void setSyncStatus(Integer syncStatus) {
		this.syncStatus = syncStatus;
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

	public String getOrganId() {
		return this.organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public List<CustomFolder> getChildFolders() {
		return this.childFolders;
	}

	public void setChildFolders(List<CustomFolder> childFolders) {
		this.childFolders = childFolders;
	}

}