package cn.com.ite.hnjtamis.personal.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;


/**
 * PersonalLearningPlan entity. @author MyEclipse Persistence Tools
 */

public class PersonalLearningPlan extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 4293975956412063462L;
	private String plpid;
	private String personname;
	private String personcode;
	private String jobscode;
	private String jobsname;
	private String planname;
	private Integer planstatus;
	private String planstarttime;
	private String planendtime;
	private Integer planmode;
	private String contents;
	
	private List<PersonalLog> personalLogs = new ArrayList<PersonalLog>();

	////副加属性 提供多条标准条款选择记录保存
	private Quarter thisquarter; // 设置岗位对象 对应字段 jobscode,jobsname
    // Constructors

    /** default constructor */
    public PersonalLearningPlan() {
    }

    
    /** full constructor */
    public PersonalLearningPlan(String personname, String personcode, String jobscode, String jobsname, String planname, Integer planstatus, String planstarttime, String planendtime, Integer planmode, String contents, String remarks, Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid, List<PersonalLog> personalLogs) {
        this.personname = personname;
        this.personcode = personcode;
        this.jobscode = jobscode;
        this.jobsname = jobsname;
        this.planname = planname;
        this.planstatus = planstatus;
        this.planstarttime = planstarttime;
        this.planendtime = planendtime;
        this.planmode = planmode;
        this.contents = contents;
        
        this.personalLogs = personalLogs;
    }

   
    // Property accessors

    public String getPlpid() {
        return this.plpid;
    }
    
    public void setPlpid(String plpid) {
        this.plpid = plpid;
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

    public String getPlanname() {
        return this.planname;
    }
    
    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public Integer getPlanstatus() {
        return this.planstatus;
    }
    
    public void setPlanstatus(Integer planstatus) {
        this.planstatus = planstatus;
    }

    public String getPlanstarttime() {
        return this.planstarttime;
    }
    
    public void setPlanstarttime(String planstarttime) {
        this.planstarttime = planstarttime;
    }

    public String getPlanendtime() {
        return this.planendtime;
    }
    
    public void setPlanendtime(String planendtime) {
        this.planendtime = planendtime;
    }

    public Integer getPlanmode() {
        return this.planmode;
    }
    
    public void setPlanmode(Integer planmode) {
        this.planmode = planmode;
    }
 

    public String getContents() {
		return contents;
	}


	public void setContents(String contents) {
		this.contents = contents;
	}



    public List<PersonalLog>  getPersonalLogs() {
        return this.personalLogs;
    }
    
    public void setPersonalLogs(List<PersonalLog>  personalLogs) {
        this.personalLogs = personalLogs;
    }


	public Quarter getThisquarter() {
		return thisquarter;
	}


	public void setThisquarter(Quarter thisquarter) {
		this.thisquarter = thisquarter;
	}
   



}