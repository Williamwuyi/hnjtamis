package cn.com.ite.hnjtamis.baseinfo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业信息类型VO</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015  10:10:46 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class SpecialityType extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 2141121114930167350L;
	private String bstid; // 专业类型ID
	private String typename; // 类型名称
	// private String parentid; // 上级ID
	private SpecialityType parentspeciltype; // 上级类型
	private String levelno; // 层次号
	private String typecode;	 /// 编码
	private List<Speciality> baseSpecialities = new ArrayList<Speciality>();
	private List<SpecialityType> subSpecialitytypes=new ArrayList<SpecialityType>();
     

    // Constructors

    /** default constructor */
    public SpecialityType() {
    }

    
    /** full constructor */
    public SpecialityType(String typename, String levelno, Integer orderno, String remarks, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid) {
        this.typename = typename;
//        this.parentid = parentid;
        this.levelno = levelno;
        
    }

   
    // Property accessors

    public String getBstid() {
        return this.bstid;
    }
    
    public void setBstid(String bstid) {
        this.bstid = bstid;
    }

    public String getTypename() {
        return this.typename;
    }
    
    public void setTypename(String typename) {
        this.typename = typename;
    }

 

    public String getLevelno() {
        return this.levelno;
    }
    
    public void setLevelno(String levelno) {
        this.levelno = levelno;
    }

  

	public List<Speciality> getBaseSpecialities() {
		return baseSpecialities;
	}


	public void setBaseSpecialities(List<Speciality> baseSpecialities) {
		this.baseSpecialities = baseSpecialities;
	}


	public SpecialityType getParentspeciltype() {
		return parentspeciltype;
	}


	public void setParentspeciltype(SpecialityType parentspeciltype) {
		this.parentspeciltype = parentspeciltype;
	}


	public List<SpecialityType> getSubSpecialitytypes() {
		return subSpecialitytypes;
	}


	public void setSubSpecialitytypes(List<SpecialityType> subSpecialitytypes) {
		this.subSpecialitytypes = subSpecialitytypes;
	}


	public String getTypecode() {
		return typecode;
	}


	public void setTypecode(String typecode) {
		this.typecode = typecode;
	}
   








}