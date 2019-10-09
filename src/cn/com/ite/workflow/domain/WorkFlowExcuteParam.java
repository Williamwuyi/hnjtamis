package cn.com.ite.workflow.domain;



/**
 * WorkFlowExcuteParam entity. @author MyEclipse Persistence Tools
 */

public class WorkFlowExcuteParam  implements java.io.Serializable {


    // Fields    

     private WorkFlowExcuteParamId id;


    // Constructors

    /** default constructor */
    public WorkFlowExcuteParam() {
    }

    
    /** full constructor */
    public WorkFlowExcuteParam(WorkFlowExcuteParamId id) {
        this.id = id;
    }

   
    // Property accessors

    public WorkFlowExcuteParamId getId() {
        return this.id;
    }
    
    public void setId(WorkFlowExcuteParamId id) {
        this.id = id;
    }
   








}