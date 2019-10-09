package cn.com.ite.hnjtamis.talent.reg;

public class TalentChart implements java.io.Serializable {
	private static final long serialVersionUID = -3345932581325501466L;
	private String specialityName;
	private Long talentCount;


	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Long getTalentCount() {
		return talentCount;
	}

	public void setTalentCount(Long talentCount) {
		this.talentCount = talentCount;
	}
}
