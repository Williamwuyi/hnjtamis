package cn.com.ite.hnjtamis.personal.domain;

import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;



/**
 * PersonalRemind entity. @author MyEclipse Persistence Tools
 */

public class PersonalRemind extends AbstractDomain implements java.io.Serializable {


    // Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4462035219009117764L;
	private String reminedid;
	private String personname;
	private String personcode;
	private String contentsSubject;
	private String contents;
	


    // Constructors

    /** default constructor */
    public PersonalRemind() {
    }

    
    /** full constructor */
    public PersonalRemind(String personname, String personcode, String contentsSubject, String contents, Integer orderno, Integer isavailable, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid) {
        this.personname = personname;
        this.personcode = personcode;
        this.contentsSubject = contentsSubject;
        this.contents = contents;
       
    }

   
    // Property accessors

    public String getReminedid() {
        return this.reminedid;
    }
    
    public void setReminedid(String reminedid) {
        this.reminedid = reminedid;
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

    public String getContentsSubject() {
        return this.contentsSubject;
    }
    
    public void setContentsSubject(String contentsSubject) {
        this.contentsSubject = contentsSubject;
    }

    public String getContents() {
        return this.contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }









}