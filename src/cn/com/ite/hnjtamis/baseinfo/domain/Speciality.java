package cn.com.ite.hnjtamis.baseinfo.domain;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;


/**
 * 
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业信息VO</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015  10:10:46 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class Speciality extends AbstractDomain implements java.io.Serializable {


    // Fields    

     private String specialityid;				/// 主键ID
     private SpecialityType specialityType;		/// 专业类型
     private String specialityname;				/// 专业名称
     private String specialitycode;				/// 专业编码
     private String parentTypesNames;
     private String typesNames;
     private List<JobsUnionSpeciality> jobunionspecial =new ArrayList<JobsUnionSpeciality>();
     private List<SpecialityStandardTypes> specialityStandardTypeslist=new ArrayList<SpecialityStandardTypes>();
    // Constructors

    public List<JobsUnionSpeciality> getJobunionspecial() {
		return jobunionspecial;
	}


	public void setJobunionspecial(List<JobsUnionSpeciality> jobunionspecial) {
		this.jobunionspecial = jobunionspecial;
	}


	/** default constructor */
    public Speciality() {
    }

    
    /** full constructor */
    public Speciality(SpecialityType specialityType, String specialityname, Integer orderno, 
    		String remarks, Integer isavailable, Integer status, String lastUpdateDate, 
    		String lastUpdatedBy, String creationDate, String createdBy, String organid,String specialitycode,
    		String parentTypesNames,String typesNames,List<SpecialityStandardTypes> specialityStandardTypeslist) {
        this.specialityType = specialityType;
        this.specialityname = specialityname;
        this.specialitycode = specialitycode;
        this.parentTypesNames = parentTypesNames;
        this.typesNames = typesNames;
        this.specialityStandardTypeslist = specialityStandardTypeslist;
        
    }

   
    // Property accessors

    public String getSpecialityid() {
        return this.specialityid;
    }
    
    public void setSpecialityid(String specialityid) {
        this.specialityid = specialityid;
    }

    public SpecialityType getSpecialityType() {
        return this.specialityType;
    }
    
    public void setSpecialityType(SpecialityType specialityType) {
        this.specialityType = specialityType;
    }

    public String getSpecialityname() {
        return this.specialityname;
    }
    
    public void setSpecialityname(String specialityname) {
        this.specialityname = specialityname;
    }


	public String getSpecialitycode() {
		return specialitycode;
	}


	public void setSpecialitycode(String specialitycode) {
		this.specialitycode = specialitycode;
	}


	public String getParentTypesNames() {
		return parentTypesNames;
	}


	public void setParentTypesNames(String parentTypesNames) {
		this.parentTypesNames = parentTypesNames;
	}


	public String getTypesNames() {
		return typesNames;
	}


	public void setTypesNames(String typesNames) {
		this.typesNames = typesNames;
	}


	public List<SpecialityStandardTypes> getSpecialityStandardTypeslist() {
		return specialityStandardTypeslist;
	}


	public void setSpecialityStandardTypeslist(
			List<SpecialityStandardTypes> specialityStandardTypeslist) {
		this.specialityStandardTypeslist = specialityStandardTypeslist;
	}

    

}