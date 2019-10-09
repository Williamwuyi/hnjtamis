package cn.com.ite.workflow.domain;



/**
 * WorkFlowExcuteParamId entity. @author MyEclipse Persistence Tools
 */

public class WorkFlowExcuteParamId  implements java.io.Serializable {


    // Fields    

     private String excuteId;
     private String paramKey;
     private String paramValue;


    // Constructors

    /** default constructor */
    public WorkFlowExcuteParamId() {
    }

    
    /** full constructor */
    public WorkFlowExcuteParamId(String excuteId, String paramKey, String paramValue) {
        this.excuteId = excuteId;
        this.paramKey = paramKey;
        this.paramValue = paramValue;
    }

   
    // Property accessors

    public String getExcuteId() {
        return this.excuteId;
    }
    
    public void setExcuteId(String excuteId) {
        this.excuteId = excuteId;
    }

    public String getParamKey() {
        return this.paramKey;
    }
    
    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return this.paramValue;
    }
    
    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }
   








}