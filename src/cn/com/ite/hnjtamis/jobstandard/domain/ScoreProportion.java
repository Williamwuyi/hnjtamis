package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.List;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;



/**
 * ScoreProportion entity. @author MyEclipse Persistence Tools
 */

public class ScoreProportion extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
		 * 
		 */
	private static final long serialVersionUID = 4075504445376900213L;
	private String scoresetid;
	private String jobscode;
	private String jobsname;
	private Double proportionType1; /// 学时达标类型占比
	private Double proportionType2; /// 考试得分占比
	private Double proportionType3; // 类型3得分占比-预留
	private Double proportionType4; // 类型4得分占比-预留
	private Double proportionType5; // 类型5得分占比-预留
	
	/// 附加字段
	private List<Quarter> scoreproportionQuarters; /// 岗位对象集合 对应批量选择多个岗位时操作
	private Quarter thisquarter; // 设置岗位对象 对应字段 jobscode,jobsname
    // Constructors

    public Quarter getThisquarter() {
		return thisquarter;
	}


	public void setThisquarter(Quarter thisquarter) {
		this.thisquarter = thisquarter;
	}


	/** default constructor */
    public ScoreProportion() {
    }

    
    /** full constructor */
    public ScoreProportion(String jobscode, String jobsname, Double proportionType1, Double proportionType2, Double proportionType3, Double proportionType4, Double proportionType5, String remarks, Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid) {
        this.jobscode = jobscode;
        this.jobsname = jobsname;
        this.proportionType1 = proportionType1;
        this.proportionType2 = proportionType2;
        this.proportionType3 = proportionType3;
        this.proportionType4 = proportionType4;
        this.proportionType5 = proportionType5;
        
    }

   
    // Property accessors

    public String getScoresetid() {
        return this.scoresetid;
    }
    
    public void setScoresetid(String scoresetid) {
        this.scoresetid = scoresetid;
    }

    public String getJobscode() {
        return this.jobscode;
    }
    
    public void setJobscode(String jobscode) {
        this.jobscode = jobscode;
    }

    public String getJobsname() {
        return this.jobsname;
    }
    
    public void setJobsname(String jobsname) {
        this.jobsname = jobsname;
    }

    public Double getProportionType1() {
        return this.proportionType1;
    }
    
    public void setProportionType1(Double proportionType1) {
        this.proportionType1 = proportionType1;
    }

    public Double getProportionType2() {
        return this.proportionType2;
    }
    
    public void setProportionType2(Double proportionType2) {
        this.proportionType2 = proportionType2;
    }

    public Double getProportionType3() {
        return this.proportionType3;
    }
    
    public void setProportionType3(Double proportionType3) {
        this.proportionType3 = proportionType3;
    }

    public Double getProportionType4() {
        return this.proportionType4;
    }
    
    public void setProportionType4(Double proportionType4) {
        this.proportionType4 = proportionType4;
    }

    public Double getProportionType5() {
        return this.proportionType5;
    }
    
    public void setProportionType5(Double proportionType5) {
        this.proportionType5 = proportionType5;
    }


	public List<Quarter> getScoreproportionQuarters() {
		return scoreproportionQuarters;
	}


	public void setScoreproportionQuarters(List<Quarter> scoreproportionQuarters) {
		this.scoreproportionQuarters = scoreproportionQuarters;
	}



}