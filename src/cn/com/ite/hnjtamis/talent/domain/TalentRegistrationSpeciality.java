package cn.com.ite.hnjtamis.talent.domain;

import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;

/**
 * TalentRegistrationSpeciality entity. @author MyEclipse Persistence Tools
 */

public class TalentRegistrationSpeciality implements java.io.Serializable {

	// Fields

	private String reSpecialityId;
	private TalentRegistration talentRegistration;
	private Speciality speciality;
	private String specialityname;
	private Integer orderno;
	private String toZz;
	private String toFzz;

	// Constructors

	/** default constructor */
	public TalentRegistrationSpeciality() {
	}

	/** full constructor */
	public TalentRegistrationSpeciality(TalentRegistration talentRegistration,
			Speciality speciality, String specialityname,
			Integer orderno, String toZz, String toFzz) {
		this.talentRegistration = talentRegistration;
		this.speciality = speciality;
		this.specialityname = specialityname;
		this.orderno = orderno;
		this.toZz = toZz;
		this.toFzz = toFzz;
	}

	// Property accessors

	public String getReSpecialityId() {
		return this.reSpecialityId;
	}

	public void setReSpecialityId(String reSpecialityId) {
		this.reSpecialityId = reSpecialityId;
	}

	public TalentRegistration getTalentRegistration() {
		return this.talentRegistration;
	}

	public void setTalentRegistration(TalentRegistration talentRegistration) {
		this.talentRegistration = talentRegistration;
	}

	public Speciality getSpeciality() {
		return speciality;
	}

	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}

	public String getSpecialityname() {
		return this.specialityname;
	}

	public void setSpecialityname(String specialityname) {
		this.specialityname = specialityname;
	}

	public Integer getOrderno() {
		return this.orderno;
	}

	public void setOrderno(Integer orderno) {
		this.orderno = orderno;
	}

	public String getToZz() {
		return this.toZz;
	}

	public void setToZz(String toZz) {
		this.toZz = toZz;
	}

	public String getToFzz() {
		return this.toFzz;
	}

	public void setToFzz(String toFzz) {
		this.toFzz = toFzz;
	}

}