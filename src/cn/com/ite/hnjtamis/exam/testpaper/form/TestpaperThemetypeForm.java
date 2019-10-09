package cn.com.ite.hnjtamis.exam.testpaper.form;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeInThemeBankForm;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemetypeForm</p>
 * <p>Description 试卷产生方式</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月1日 下午3:11:19
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperThemetypeForm {

	
	private String testpaperThemetypeId;//试卷-题型ID
	private String rankRate;/////难度比例 {容易,难,很难}{0，0，0}逗号分割，表示某试卷某题型的难度分布
	private Speciality speciality;
	private String professionId;//专业ID
	private String professionName;//专业
	private String professionLevelCode;//专业级别编码
	private Short rate;//比例%
	private Double score;//按分数筛选时为分数,按题数筛选时为题数
	private String setThemetype;//配置方式 10-按分数 20-按题数
	private Integer total;///题数
	private Integer sortNum;//排序号
	private String syncFlag;////同步标志
	private String lastUpdateDate;///最后修改时间
	private String lastUpdatedBy;//最后修改人
	private String lastUpdatedIdBy;//最后修改人ID
	private String creationDate;//创建时间
	private String createdBy;//创建人
	private String createdIdBy;//创建人ID
	private String themeTypeId;//题型ID
	private ThemeType themeType;//题型
	private Double actualScore;//实际分数
	private Integer actualTotal;//实际题数
	
	private List<ThemeInThemeBankForm> selectbanks = new ArrayList<>(0);
	
	
	public String getThemeTypeId() {
		return themeTypeId;
	}
	public void setThemeTypeId(String themeTypeId) {
		this.themeTypeId = themeTypeId;
	}
	public ThemeType getThemeType() {
		return themeType;
	}
	public void setThemeType(ThemeType themeType) {
		this.themeType = themeType;
	}
	public Speciality getSpeciality() {
		return speciality;
	}
	public void setSpeciality(Speciality speciality) {
		this.speciality = speciality;
	}
	public String getTestpaperThemetypeId() {
		return testpaperThemetypeId;
	}
	public void setTestpaperThemetypeId(String testpaperThemetypeId) {
		this.testpaperThemetypeId = testpaperThemetypeId;
	}
	public String getRankRate() {
		return rankRate;
	}
	public void setRankRate(String rankRate) {
		this.rankRate = rankRate;
	}
	public String getProfessionId() {
		return professionId;
	}
	public void setProfessionId(String professionId) {
		this.professionId = professionId;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getProfessionLevelCode() {
		return professionLevelCode;
	}
	public void setProfessionLevelCode(String professionLevelCode) {
		this.professionLevelCode = professionLevelCode;
	}
	public Short getRate() {
		return rate;
	}
	public void setRate(Short rate) {
		this.rate = rate;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public String getSetThemetype() {
		return setThemetype;
	}
	public void setSetThemetype(String setThemetype) {
		this.setThemetype = setThemetype;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
	public String getSyncFlag() {
		return syncFlag;
	}
	public void setSyncFlag(String syncFlag) {
		this.syncFlag = syncFlag;
	}
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public String getLastUpdatedIdBy() {
		return lastUpdatedIdBy;
	}
	public void setLastUpdatedIdBy(String lastUpdatedIdBy) {
		this.lastUpdatedIdBy = lastUpdatedIdBy;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedIdBy() {
		return createdIdBy;
	}
	public void setCreatedIdBy(String createdIdBy) {
		this.createdIdBy = createdIdBy;
	}
	public Double getActualScore() {
		return actualScore;
	}
	public void setActualScore(Double actualScore) {
		this.actualScore = actualScore;
	}
	public Integer getActualTotal() {
		return actualTotal;
	}
	public void setActualTotal(Integer actualTotal) {
		this.actualTotal = actualTotal;
	}
	public List<ThemeInThemeBankForm> getSelectbanks() {
		return selectbanks;
	}
	public void setSelectbanks(List<ThemeInThemeBankForm> selectbanks) {
		this.selectbanks = selectbanks;
	}
	
	
	
}
