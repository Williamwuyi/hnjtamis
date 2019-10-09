package cn.com.ite.hnjtamis.baseinfo.domain;

import java.util.Date;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;

/**
 * 
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 其他信息VO的公共类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015  10:10:46 AM
 * @version 1.0
 * 
 * @modified records:
 */
public abstract class AbstractDomain  implements java.io.Serializable {


    // Fields    

     private Integer orderno;
     private String remarks;
     private Integer isavailable;
     private Integer status;
     private String lastUpdateDate;
     private String lastUpdatedBy;
     private String creationDate;
     private String createdBy;
     private String organid;


    // Constructors

    /** default constructor */
    public AbstractDomain() {
    }

   
    public Integer getOrderno() {
        return this.orderno;
    }
    
    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }

    public String getRemarks() {
        return this.remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getIsavailable() {
        return this.isavailable;
    }
    
    public void setIsavailable(Integer isavailable) {
        this.isavailable = isavailable;
    }

    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastUpdateDate() {
        return this.lastUpdateDate;
    }
    
    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getLastUpdatedBy() {
        return this.lastUpdatedBy;
    }
    
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOrganid() {
        return this.organid;
    }
    
    public void setOrganid(String organid) {
        this.organid = organid;
    }
   

    public static void addCommonFieldValue(AbstractDomain domain){
    	UserSession us = LoginAction.getUserSessionInfo();
    	//StringUtils s;
    	domain.setIsavailable(1);
    	domain.setCreatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
    	domain.setCreationDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
    	domain.setOrganid(us.getOrganId());
    	domain.setStatus(DicDefine.DATA_ADD);
    }

    public static void updateCommonFieldValue(AbstractDomain domain){
    	UserSession us = LoginAction.getUserSessionInfo();
    	//StringUtils s;
    	domain.setLastUpdatedBy(StringUtils.isEmpty(us.getEmployeeName())?us.getAccount():us.getEmployeeName());
    	domain.setLastUpdateDate(DateUtils.convertDateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
    	domain.setOrganid(us.getOrganId());
    	domain.setStatus(DicDefine.DATA_UPDATE);
    }
}