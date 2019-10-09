package cn.com.ite.eap2.domain.funres;

import java.util.HashSet;
import java.util.Set;


/**
 * <p>Title cn.com.ite.eap2.domain.funres.ModuleResource</p>
 * <p>Description 模块功能资源</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 上午09:27:31
 * @version 2.0
 * 
 * @modified records:
 */
public class ModuleResource  implements java.io.Serializable {
	 private static final long serialVersionUID = -2265871768972422295L;
	 private String resourceId;
     private AppModule appModule;
     private String resourceCode;
     private String resourceName;
     private int resourceType=2;
     private String resourceUrl;
     private Integer orderNo;
     private int popedomType;
     private String bigIcon;
     private String icon;
     private Set<AppMenu> appMenus = new HashSet<AppMenu>(0);


    // Constructors

    /** default constructor */
    public ModuleResource() {
    }

    
    /** full constructor */
    public ModuleResource(AppModule appModule, String resourceCode, String resourceName, int resourceType, String resourceUrl, Integer orderNo, int popedomType, String bigIcon, String icon, Set<AppMenu> appMenus) {
        this.appModule = appModule;
        this.resourceCode = resourceCode;
        this.resourceName = resourceName;
        this.resourceType = resourceType;
        this.resourceUrl = resourceUrl;
        this.orderNo = orderNo;
        this.popedomType = popedomType;
        this.bigIcon = bigIcon;
        this.icon = icon;
        this.appMenus = appMenus;
    }

   
    // Property accessors

    public String getResourceId() {
        return this.resourceId;
    }
    
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public AppModule getAppModule() {
        return this.appModule;
    }
    
    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    public String getResourceCode() {
        return this.resourceCode;
    }
    
    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceType() {
        return this.resourceType;
    }
    
    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return this.resourceUrl;
    }
    
    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public int getPopedomType() {
        return this.popedomType;
    }
    
    public void setPopedomType(int popedomType) {
        this.popedomType = popedomType;
    }

    public String getBigIcon() {
        return this.bigIcon;
    }
    
    public void setBigIcon(String bigIcon) {
        this.bigIcon = bigIcon;
    }

    public String getIcon() {
        return this.icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Set<AppMenu> getAppMenus() {
        return this.appMenus;
    }
    
    public void setAppMenus(Set<AppMenu> appMenus) {
        this.appMenus = appMenus;
    }
}