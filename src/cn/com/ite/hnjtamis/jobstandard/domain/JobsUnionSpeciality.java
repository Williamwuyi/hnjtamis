package cn.com.ite.hnjtamis.jobstandard.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;


/**
 * JobsUnionSpeciality entity. @author MyEclipse Persistence Tools
 */

public class JobsUnionSpeciality extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = -1131000309910589693L;
	private String jusid;
	private Speciality speciality;
	private String jobscode;
	private String jobsname;
	
	//用于显示使用
	 private String specialityid;/// 专业ID
     private String specialityname;/// 专业名称
     private String bstid; // 专业类型ID
 	 private String typename; // 类型名称
 	 private String quarterId;
	

////副加属性 提供多条标准条款选择记录保存
	private Quarter thisquarter; // 设置岗位对象 对应字段 jobscode,jobsname
	private List<Speciality> specialities = new ArrayList<Speciality>();
    // Constructors

    public List<Speciality> getSpecialities() {
		return specialities;
	}


	public void setSpecialities(List<Speciality> specialities) {
		this.specialities = specialities;
	}


	public Quarter getThisquarter() {
		return thisquarter;
	}


	public void setThisquarter(Quarter thisquarter) {
		this.thisquarter = thisquarter;
	}


	/** default constructor */
    public JobsUnionSpeciality() {
    }

    
    /** full constructor */
    public JobsUnionSpeciality(Speciality speciality, String jobscode, String jobsname, String remarks, 
    		Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, 
    		String creationDate, String createdBy, String organid,String quarterId) {
        this.speciality = speciality;
        this.jobscode = jobscode;
        this.jobsname = jobsname;
        this.quarterId=quarterId;
        
    }

   
    // Property accessors

    public String getJusid() {
        return this.jusid;
    }
    
    public void setJusid(String jusid) {
        this.jusid = jusid;
    }

    public Speciality getSpeciality() {
        return this.speciality;
    }
    
    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
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


	public String getSpecialityid() {
		return specialityid;
	}


	public void setSpecialityid(String specialityid) {
		this.specialityid = specialityid;
	}


	public String getSpecialityname() {
		return specialityname;
	}


	public void setSpecialityname(String specialityname) {
		this.specialityname = specialityname;
	}


	public String getBstid() {
		return bstid;
	}


	public void setBstid(String bstid) {
		this.bstid = bstid;
	}


	public String getTypename() {
		return typename;
	}


	public void setTypename(String typename) {
		this.typename = typename;
	}


	public String getQuarterId() {
		return quarterId;
	}


	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}


}