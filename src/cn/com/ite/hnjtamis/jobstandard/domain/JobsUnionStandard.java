package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;



/**
 * JobsUnionStandard entity. @author MyEclipse Persistence Tools
 */

public class JobsUnionStandard extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
		 * 
		 */
	private static final long serialVersionUID = -6566940591422245122L;
	private String jusdid;
	private StandardTerms standardTerms;
	private String jobscode;
	private Integer standardMode;
	private Double standardScore;
	private String standardGrade;
	private Double proportition;
	private Integer periodweeks;
	private String jobsname;
	private String quarterTrainCode;//标准岗位编码
	private String quarterTrainName;//标准岗位
	private String parentTrainQuarterId;
	private String parentTrainQuarterName;
	private String parentTrainQuarterCode;
	
	//// 副加属性 提供多条标准条款选择记录保存
	private Quarter thisquarter; // 设置岗位对象 对应字段 jobscode,jobsname
	private List<StandardTerms> jobstandardterms = new ArrayList<StandardTerms>();
    // Constructors

    public List<StandardTerms> getJobstandardterms() {
		return jobstandardterms;
	}


	public void setJobstandardterms(List<StandardTerms> jobstandardterms) {
		this.jobstandardterms = jobstandardterms;
	}


	/** default constructor */
    public JobsUnionStandard() {
    }

    
    /** full constructor */
    public JobsUnionStandard(StandardTerms standardTerms, String jobscode, Integer standardMode, 
    		Double standardScore, String standardGrade, Double proportition, Integer periodweeks, 
    		String jobsname, String remarks, Integer orderno, Integer isavailable, Integer status, 
    		String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, 
    		String organid,String quarterTrainCode,String quarterTrainName,
			String parentTrainQuarterId,String parentTrainQuarterName,String parentTrainQuarterCode) {
        this.standardTerms = standardTerms;
        this.jobscode = jobscode;
        this.standardMode = standardMode;
        this.standardScore = standardScore;
        this.standardGrade = standardGrade;
        this.proportition = proportition;
        this.periodweeks = periodweeks;
        this.jobsname = jobsname;
        this.quarterTrainCode=quarterTrainCode;
        this.quarterTrainName=quarterTrainName;
        this.parentTrainQuarterId=parentTrainQuarterId;
        this.parentTrainQuarterName=parentTrainQuarterName;
        this.parentTrainQuarterCode=parentTrainQuarterCode;
    }

   
    // Property accessors

    public String getJusdid() {
        return this.jusdid;
    }
    
    public String getQuarterTrainCode() {
		return quarterTrainCode;
	}


	public void setQuarterTrainCode(String quarterTrainCode) {
		this.quarterTrainCode = quarterTrainCode;
	}


	public String getQuarterTrainName() {
		return quarterTrainName;
	}


	public void setQuarterTrainName(String quarterTrainName) {
		this.quarterTrainName = quarterTrainName;
	}


	public void setJusdid(String jusdid) {
        this.jusdid = jusdid;
    }

    public StandardTerms getStandardTerms() {
        return this.standardTerms;
    }
    
    public void setStandardTerms(StandardTerms standardTerms) {
        this.standardTerms = standardTerms;
    }

    public String getJobscode() {
        return this.jobscode;
    }
    
    public void setJobscode(String jobscode) {
        this.jobscode = jobscode;
    }

    public Integer getStandardMode() {
        return this.standardMode;
    }
    
    public void setStandardMode(Integer standardMode) {
        this.standardMode = standardMode;
    }

    public Double getStandardScore() {
        return this.standardScore;
    }
    
    public void setStandardScore(Double standardScore) {
        this.standardScore = standardScore;
    }

    public String getStandardGrade() {
        return this.standardGrade;
    }
    
    public void setStandardGrade(String standardGrade) {
        this.standardGrade = standardGrade;
    }

    public Double getProportition() {
        return this.proportition;
    }
    
    public void setProportition(Double proportition) {
        this.proportition = proportition;
    }

    public Integer getPeriodweeks() {
        return this.periodweeks;
    }
    
    public void setPeriodweeks(Integer periodweeks) {
        this.periodweeks = periodweeks;
    }

    public String getJobsname() {
        return this.jobsname;
    }
    
    public void setJobsname(String jobsname) {
        this.jobsname = jobsname;
    }


	public Quarter getThisquarter() {
		return thisquarter;
	}


	public void setThisquarter(Quarter thisquarter) {
		this.thisquarter = thisquarter;
	}

   
	public String getParentTrainQuarterId() {
		return parentTrainQuarterId;
	}

	public void setParentTrainQuarterId(String parentTrainQuarterId) {
		this.parentTrainQuarterId = parentTrainQuarterId;
	}

	public String getParentTrainQuarterName() {
		return parentTrainQuarterName;
	}

	public void setParentTrainQuarterName(String parentTrainQuarterName) {
		this.parentTrainQuarterName = parentTrainQuarterName;
	}

	public String getParentTrainQuarterCode() {
		return parentTrainQuarterCode;
	}

	public void setParentTrainQuarterCode(String parentTrainQuarterCode) {
		this.parentTrainQuarterCode = parentTrainQuarterCode;
	}
	

}