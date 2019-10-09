package cn.com.ite.hnjtamis.personal.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;


/**
 * PersonalRateProgress entity. @author MyEclipse Persistence Tools
 */

public class PersonalRateProgress extends AbstractDomain implements java.io.Serializable {


    // Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1307015979982887853L;
	private String rateid;
	private String businessid; // 考试或培训业务ID
	private String jobscode;
	private String jobsname;
	private String personname;
	private String personcode;
	private Integer isreachthestd;
	private String reachtime;
	private String contents;
	private Double targetscore;
	private Double totalscore;
	private String applytime;
	private String timeoverdue;
	private String chktime;
	private String chkperson;
	private Integer checkstatus; /// 审核状态
	private Integer reachetype;  ///学时达标OR考试达标 1：学时达标 2：考试达标
	
	private List<PersonalLog> personalLogs = new ArrayList<PersonalLog>();


    // Constructors

    /** default constructor */
    public PersonalRateProgress() {
    }

    
    /** full constructor */
    public PersonalRateProgress(String jobscode, String jobsname, String personname, String personcode, Integer isreachthestd, String reachtime, Double totalscore, String applytime, String timeoverdue, String chktime, String chkperson, Integer checkstatus, Integer reachetype, Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid, List<PersonalLog> personalLogs) {
        this.jobscode = jobscode;
        this.jobsname = jobsname;
        this.personname = personname;
        this.personcode = personcode;
        this.isreachthestd = isreachthestd;
        this.reachtime = reachtime;
        this.totalscore = totalscore;
        this.applytime = applytime;
        this.timeoverdue = timeoverdue;
        this.chktime = chktime;
        this.chkperson = chkperson;
        this.checkstatus = checkstatus;
        this.reachetype = reachetype;
        
        this.personalLogs = personalLogs;
    }

   
    // Property accessors

    public String getRateid() {
        return this.rateid;
    }
    
    public void setRateid(String rateid) {
        this.rateid = rateid;
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

    public String getPersonname() {
        return this.personname;
    }
    
    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getPersoncode() {
        return this.personcode;
    }
    
    public void setPersoncode(String personcode) {
        this.personcode = personcode;
    }

    public Integer getIsreachthestd() {
        return this.isreachthestd;
    }
    
    public void setIsreachthestd(Integer isreachthestd) {
        this.isreachthestd = isreachthestd;
    }

    public String getReachtime() {
        return this.reachtime;
    }
    
    public void setReachtime(String reachtime) {
        this.reachtime = reachtime;
    }

    public Double getTotalscore() {
        return this.totalscore;
    }
    
    public void setTotalscore(Double totalscore) {
        this.totalscore = totalscore;
    }

    public String getApplytime() {
        return this.applytime;
    }
    
    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getTimeoverdue() {
        return this.timeoverdue;
    }
    
    public void setTimeoverdue(String timeoverdue) {
        this.timeoverdue = timeoverdue;
    }

    public String getChktime() {
        return this.chktime;
    }
    
    public void setChktime(String chktime) {
        this.chktime = chktime;
    }

    public String getChkperson() {
        return this.chkperson;
    }
    
    public void setChkperson(String chkperson) {
        this.chkperson = chkperson;
    }

    

    public Integer getCheckstatus() {
		return checkstatus;
	}


	public void setCheckstatus(Integer checkstatus) {
		this.checkstatus = checkstatus;
	}


	public Integer getReachetype() {
        return this.reachetype;
    }
    
    public void setReachetype(Integer reachetype) {
        this.reachetype = reachetype;
    }


    public List<PersonalLog> getPersonalLogs() {
        return this.personalLogs;
    }
    
    public void setPersonalLogs(List<PersonalLog> personalLogs) {
        this.personalLogs = personalLogs;
    }


	public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}


	public Double getTargetscore() {
		return targetscore;
	}


	public void setTargetscore(Double targetscore) {
		this.targetscore = targetscore;
	}


	public String getBusinessid() {
		return businessid;
	}


	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}
   


}