package cn.com.ite.eap2.domain.baseinfo;

/**
 * SysAfficheIncepter entity. @author MyEclipse Persistence Tools
 */

public class SysAfficheIncepter implements java.io.Serializable {

	// Fields

	private String sysAiId;
	private SysAffiche sysAffiche;
	private String incepterId;
	private String incepterName;
	private Integer incepterType;
	private Integer sortNum;

	// Constructors

	/** default constructor */
	public SysAfficheIncepter() {
	}

	/** full constructor */
	public SysAfficheIncepter(SysAffiche sysAffiche, String incepterId,
			String incepterName, Integer incepterType,Integer sortNum) {
		this.sysAffiche = sysAffiche;
		this.incepterId = incepterId;
		this.incepterName = incepterName;
		this.incepterType = incepterType;
		this.sortNum=sortNum;
	}

	// Property accessors

	public String getSysAiId() {
		return this.sysAiId;
	}

	public void setSysAiId(String sysAiId) {
		this.sysAiId = sysAiId;
	}

	public SysAffiche getSysAffiche() {
		return sysAffiche;
	}

	public void setSysAffiche(SysAffiche sysAffiche) {
		this.sysAffiche = sysAffiche;
	}

	public String getIncepterId() {
		return this.incepterId;
	}

	public void setIncepterId(String incepterId) {
		this.incepterId = incepterId;
	}

	public String getIncepterName() {
		return this.incepterName;
	}

	public void setIncepterName(String incepterName) {
		this.incepterName = incepterName;
	}

	public Integer getIncepterType() {
		return this.incepterType;
	}

	public void setIncepterType(Integer incepterType) {
		this.incepterType = incepterType;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

}