package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;


/**
 * StandardTypes entity. @author MyEclipse Persistence Tools
 */

public class StandardTypes extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
		 * 
		 */
	private static final long serialVersionUID = -8461878675912385203L;
	private String jstypeid;
	private String typename;
	private String parentName;
	private String bankMapCode;
	private StandardTypes parentSpeciltype;

	private List<StandardTerms> jobsStandardterms = new ArrayList(0);


    // Constructors

    /** default constructor */
    public StandardTypes() {
    }

    
    /** full constructor */
    public StandardTypes(String typename, String remarks, Integer orderno, Integer isavailable, 
    		Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, 
    		String createdBy, String organid, List<StandardTerms> jobsStandards,
    		String parentName,StandardTypes parentSpeciltype,String bankMapCode) {
        this.typename = typename;
        this.jobsStandardterms = jobsStandards;
        this.parentName=parentName;
        this.parentSpeciltype=parentSpeciltype;
        this.bankMapCode=bankMapCode;
    }

   
    // Property accessors

    public String getJstypeid() {
        return this.jstypeid;
    }
    
    public void setJstypeid(String jstypeid) {
        this.jstypeid = jstypeid;
    }

    public String getTypename() {
        return this.typename;
    }
    
    public void setTypename(String typename) {
        this.typename = typename;
    }


    public List<StandardTerms> getJobsStandardterms() {
        return this.jobsStandardterms;
    }
    
    public void setJobsStandardterms(List<StandardTerms> jobsStandardterms) {
        this.jobsStandardterms = jobsStandardterms;
    }


	public String getParentName() {
		return parentName;
	}


	public void setParentName(String parentName) {
		this.parentName = parentName;
	}


	public StandardTypes getParentSpeciltype() {
		return parentSpeciltype;
	}


	public void setParentSpeciltype(StandardTypes parentSpeciltype) {
		this.parentSpeciltype = parentSpeciltype;
	}


	public String getBankMapCode() {
		return bankMapCode;
	}


	public void setBankMapCode(String bankMapCode) {
		this.bankMapCode = bankMapCode;
	}


	


}