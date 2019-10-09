package cn.com.ite.hnjtamis.personal.domain;

import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;



/**
 * PersonalLog entity. @author MyEclipse Persistence Tools
 */

public class PersonalLog extends AbstractDomain implements java.io.Serializable {


    // Fields    

     /**
		 * 
		 */
	private static final long serialVersionUID = -3689923345132700490L;
	private String plogid;
	private PersonalLearningPlan personalLearningplan;
	private PersonalRateProgress personalRateProgress;
	private String personname;
	private String personcode;
	private String contents;
	private String learningstarttime;
	private String learningendtime;
	private Integer learningtype;
	private Double logScore;
	private String businessid;


    // Constructors

    /** default constructor */
    public PersonalLog() {
    }

    
    /** full constructor */
    public PersonalLog(PersonalLearningPlan personalLearningplan, PersonalRateProgress personalRateProgress, String personname, String personcode, String contents, String learningstarttime, String learningendtime, Integer learningtype, Double logScore, String businessid, Integer status, String lastUpdateDate, String lastUpdatedBy, String creationDate, String createdBy, String organid) {
        this.personalLearningplan = personalLearningplan;
        this.personalRateProgress = personalRateProgress;
        this.personname = personname;
        this.personcode = personcode;
        this.contents = contents;
        this.learningstarttime = learningstarttime;
        this.learningendtime = learningendtime;
        this.learningtype = learningtype;
        this.logScore = logScore;
        this.businessid = businessid;
       
    }

   
    // Property accessors

    public String getPlogid() {
        return this.plogid;
    }
    
    public void setPlogid(String plogid) {
        this.plogid = plogid;
    }

    public PersonalLearningPlan getPersonalLearningplan() {
        return this.personalLearningplan;
    }
    
    public void setPersonalLearningplan(PersonalLearningPlan personalLearningplan) {
        this.personalLearningplan = personalLearningplan;
    }

    public PersonalRateProgress getPersonalRateProgress() {
        return this.personalRateProgress;
    }
    
    public void setPersonalRateProgress(PersonalRateProgress personalRateProgress) {
        this.personalRateProgress = personalRateProgress;
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

    public String getContents() {
        return this.contents;
    }
    
    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getLearningstarttime() {
        return this.learningstarttime;
    }
    
    public void setLearningstarttime(String learningstarttime) {
        this.learningstarttime = learningstarttime;
    }

    public String getLearningendtime() {
        return this.learningendtime;
    }
    
    public void setLearningendtime(String learningendtime) {
        this.learningendtime = learningendtime;
    }

    public Integer getLearningtype() {
        return this.learningtype;
    }
    
    public void setLearningtype(Integer learningtype) {
        this.learningtype = learningtype;
    }

    public Double getLogScore() {
        return this.logScore;
    }
    
    public void setLogScore(Double logScore) {
        this.logScore = logScore;
    }

    public String getBusinessid() {
        return this.businessid;
    }
    
    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }



}