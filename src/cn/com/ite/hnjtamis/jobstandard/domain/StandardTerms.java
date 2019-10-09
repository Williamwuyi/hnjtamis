package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;


/**
 * StandardTerms entity. @author MyEclipse Persistence Tools
 */

public class StandardTerms extends AbstractDomain  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 2416480030957602641L;
	private String standardid;
	private StandardTypes standardTypes;
	private String standardname;
	private String contents;
	private String referenceBook;//参考资料
	private String themeBankName;//题库名称
	private String themeBankCode;//题库名称
	private ThemeBank themeBank;//题库
	private String efficient;//有效期 一年 两年 等
	private String refScore;//参考学分 得分
	private String upStandardScore;//达标标准 得分
	private String examTypeName;//考核方式 机试、实操等
	private String bankStandMapCode;//题库关联映射编码
	
	private String parentTypeName;
	private String parentTypeId;
	private String typename;
	private String typeId;
	
	private String parentTrainQuarterId;
	
	private int childeNums = 0;
	
	
	private List<JobsStandardQuarter> jobsStandardQuarters = new ArrayList<JobsStandardQuarter>(0);
	private List<JobsUnionStandard> jobsUnionStandards = new ArrayList<JobsUnionStandard>(0);
	private List<JobsStandardThemebank> jobsStandardThemebanks = new ArrayList<JobsStandardThemebank>(0);
	
    // Constructors

    /** default constructor */
    public StandardTerms() {
    }

    
    /** full constructor */
    public StandardTerms(StandardTypes standardTypes, String standardname, String contents, 
    		String remarks, Integer orderno, Integer isavailable, Integer status, 
    		String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, 
    		String organid, List<JobsUnionStandard> jobsUnionStandards,
    		String referenceBook,String themeBankName,String themeBankCode,ThemeBank themeBank,
    		String efficient,String refScore,String upStandardScore,String examTypeName,
    		List<JobsStandardQuarter> jobsStandardQuarters,List<JobsStandardThemebank> jobsStandardThemebanks,
    		String bankStandMapCode) {
        this.standardTypes = standardTypes;
        this.standardname = standardname;
        this.contents = contents;
        
        this.jobsUnionStandards = jobsUnionStandards;
        this.referenceBook=referenceBook;
        this.themeBankName=themeBankName;
        this.themeBank=themeBank;
        this.themeBankCode=themeBankCode;
        
        this.efficient=efficient;
        this.refScore=refScore;
        this.upStandardScore=upStandardScore;
        this.examTypeName=examTypeName;
        this.jobsStandardQuarters=jobsStandardQuarters;
        this.jobsStandardThemebanks=jobsStandardThemebanks;
        
        this.bankStandMapCode=bankStandMapCode;
    }

   
    // Property accessors

    
    
    public String getStandardid() {
        return this.standardid;
    }
    

	public List<JobsStandardQuarter> getJobsStandardQuarters() {
		return jobsStandardQuarters;
	}


	public void setJobsStandardQuarters(
			List<JobsStandardQuarter> jobsStandardQuarters) {
		this.jobsStandardQuarters = jobsStandardQuarters;
	}


	public String getThemeBankCode() {
		return themeBankCode;
	}


	public void setThemeBankCode(String themeBankCode) {
		this.themeBankCode = themeBankCode;
	}


	public void setStandardid(String standardid) {
        this.standardid = standardid;
    }

    public StandardTypes getStandardTypes() {
        return this.standardTypes;
    }
    
    public void setStandardTypes(StandardTypes standardTypes) {
        this.standardTypes = standardTypes;
    }

    public String getStandardname() {
        return this.standardname;
    }
    
    public void setStandardname(String standardname) {
        this.standardname = standardname;
    }

    public String getContents() {
        return this.contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }

   
    public List<JobsUnionStandard> getJobsUnionStandards() {
        return this.jobsUnionStandards;
    }
    
    public void setJobsUnionStandards(List<JobsUnionStandard> jobsUnionStandards) {
        this.jobsUnionStandards = jobsUnionStandards;
    }


	public String getReferenceBook() {
		return referenceBook;
	}


	public void setReferenceBook(String referenceBook) {
		this.referenceBook = referenceBook;
	}


	public String getThemeBankName() {
		return themeBankName;
	}


	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}


	public ThemeBank getThemeBank() {
		return themeBank;
	}


	public void setThemeBank(ThemeBank themeBank) {
		this.themeBank = themeBank;
	}


	public String getEfficient() {
		return efficient;
	}


	public void setEfficient(String efficient) {
		this.efficient = efficient;
	}


	public String getRefScore() {
		return refScore;
	}


	public void setRefScore(String refScore) {
		this.refScore = refScore;
	}


	public String getUpStandardScore() {
		return upStandardScore;
	}


	public void setUpStandardScore(String upStandardScore) {
		this.upStandardScore = upStandardScore;
	}


	public String getExamTypeName() {
		return examTypeName;
	}


	public void setExamTypeName(String examTypeName) {
		this.examTypeName = examTypeName;
	}


	public String getParentTypeName() {
		return parentTypeName;
	}


	public void setParentTypeName(String parentTypeName) {
		this.parentTypeName = parentTypeName;
	}


	public String getTypename() {
		return typename;
	}


	public void setTypename(String typename) {
		this.typename = typename;
	}


	public String getParentTrainQuarterId() {
		return parentTrainQuarterId;
	}


	public void setParentTrainQuarterId(String parentTrainQuarterId) {
		this.parentTrainQuarterId = parentTrainQuarterId;
	}


	public String getParentTypeId() {
		return parentTypeId;
	}


	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
	}


	public String getTypeId() {
		return typeId;
	}


	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}


	public int getChildeNums() {
		return childeNums;
	}


	public void setChildeNums(int childeNums) {
		this.childeNums = childeNums;
	}


	public List<JobsStandardThemebank> getJobsStandardThemebanks() {
		return jobsStandardThemebanks;
	}


	public void setJobsStandardThemebanks(
			List<JobsStandardThemebank> jobsStandardThemebanks) {
		this.jobsStandardThemebanks = jobsStandardThemebanks;
	}


	public String getBankStandMapCode() {
		return bankStandMapCode;
	}


	public void setBankStandMapCode(String bankStandMapCode) {
		this.bankStandMapCode = bankStandMapCode;
	}
	
	

}