package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.List;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;



/**
 * Promotion entity. @author MyEclipse Persistence Tools
 */

public class Promotion extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String promotionid;
	private String jobscode;
	private String jobsname;
	private String promotiontype;
	private String parentjobscode;
	private String parentjobsname;
	
	/// 副加属性。方便后台提交处理
	private List<Quarter> promoteQuarters; /// 晋升岗位对象集合 对应字段 parentjobscode与parentjobsname
	private Quarter thisquarter; // 设置岗位对象 对应字段 jobscode,jobsname

    // Constructors

    public Quarter getThisquarter() {
		return thisquarter;
	}


	public void setThisquarter(Quarter thisquarter) {
		this.thisquarter = thisquarter;
	}


	public List<Quarter> getPromoteQuarters() {
		return promoteQuarters;
	}


	public void setPromoteQuarters(List<Quarter> promoteQuarters) {
		this.promoteQuarters = promoteQuarters;
	}


	/** default constructor */
    public Promotion() {
    }

    
    /** full constructor */
    public Promotion(String jobscode, String jobsname, String promotiontype, String parentjobscode, String parentjobsname, String remarks, Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid) {
        this.jobscode = jobscode;
        this.jobsname = jobsname;
        this.promotiontype = promotiontype;
        this.parentjobscode = parentjobscode;
        this.parentjobsname = parentjobsname;
        
    }

   
    // Property accessors

    public String getPromotionid() {
        return this.promotionid;
    }
    
    public void setPromotionid(String promotionid) {
        this.promotionid = promotionid;
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

    public String getPromotiontype() {
        return this.promotiontype;
    }
    
    public void setPromotiontype(String promotiontype) {
        this.promotiontype = promotiontype;
    }

    public String getParentjobscode() {
        return this.parentjobscode;
    }
    
    public void setParentjobscode(String parentjobscode) {
        this.parentjobscode = parentjobscode;
    }

    public String getParentjobsname() {
        return this.parentjobsname;
    }
    
    public void setParentjobsname(String parentjobsname) {
        this.parentjobsname = parentjobsname;
    }






}