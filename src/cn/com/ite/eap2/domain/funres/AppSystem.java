package cn.com.ite.eap2.domain.funres;

import java.util.*;

import cn.com.ite.eap2.domain.baseinfo.DictionaryType;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.domain.power.SysUser;


/**
 * <p>Title cn.com.ite.eap2.domain.funres.AppSystem</p>
 * <p>Description 系统应用</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 上午09:26:23
 * @version 2.0
 * 
 * @modified records:
 */
public class AppSystem  implements java.io.Serializable {
	 private static final long serialVersionUID = -3794424384757632166L;
	 private String appId;
     private String appCode;
     private String appName;
     private String hasorgan;
     private String haspopedom;
     private String hasswaraj;
     private String appUrl;
     private String indexUrl;
     private String description;
     private String userParaName;
     private String passParaName;
     private String topLeftPic;
     private String topCenterPic;
     private String topRightPic;
     private Boolean flashScreen=false;
     private String smallPic;
     private int stat;
     private Integer orderNo;
     private String theme;
     private Set<HomePanel> homePanels = new HashSet<HomePanel>(0);
     private Set<AppModule> appModules = new HashSet<AppModule>(0);
     private Set<AppMenu> appMenus = new HashSet<AppMenu>(0);
     private List<DictionaryType> dictionaryTypes = new ArrayList<DictionaryType>();
     /**
      * 系统机构
      */
     private List<Organ> systemOrgans = new ArrayList<Organ>();
     /**
      * 系统管理员
      */
     private List<SysUser> systemMangers = new ArrayList<SysUser>();
     
     public List<Organ> getSystemOrgans() {
		return systemOrgans;
 	 }


	public void setSystemOrgans(List<Organ> systemOrgans) {
		this.systemOrgans = systemOrgans;
	}


	public List<SysUser> getSystemMangers() {
		return systemMangers;
	}


	public void setSystemMangers(List<SysUser> systemMangers) {
		this.systemMangers = systemMangers;
	}

    /** default constructor */
    public AppSystem() {
    }


    public String getAppId() {
        return this.appId;
    }
    
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return this.appCode;
    }
    
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return this.appName;
    }
    
    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getHasorgan() {
        return this.hasorgan;
    }
    
    public void setHasorgan(String hasorgan) {
        this.hasorgan = hasorgan;
    }

    public String getHaspopedom() {
        return this.haspopedom;
    }
    
    public void setHaspopedom(String haspopedom) {
        this.haspopedom = haspopedom;
    }

    public String getHasswaraj() {
        return this.hasswaraj;
    }
    
    public void setHasswaraj(String hasswaraj) {
        this.hasswaraj = hasswaraj;
    }

    public String getAppUrl() {
        return this.appUrl;
    }
    
    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserParaName() {
        return this.userParaName;
    }
    
    public void setUserParaName(String userParaName) {
        this.userParaName = userParaName;
    }

    public String getPassParaName() {
        return this.passParaName;
    }
    
    public void setPassParaName(String passParaName) {
        this.passParaName = passParaName;
    }

    public String getTopLeftPic() {
        return this.topLeftPic;
    }
    
    public void setTopLeftPic(String topLeftPic) {
        this.topLeftPic = topLeftPic;
    }

    public String getTopCenterPic() {
        return this.topCenterPic;
    }
    
    public void setTopCenterPic(String topCenterPic) {
        this.topCenterPic = topCenterPic;
    }

    public String getTopRightPic() {
        return this.topRightPic;
    }
    
    public void setTopRightPic(String topRightPic) {
        this.topRightPic = topRightPic;
    }

    public Boolean getFlashScreen() {
        return this.flashScreen;
    }
    
    public void setFlashScreen(Boolean flashScreen) {
        this.flashScreen = flashScreen;
    }

    public String getSmallPic() {
        return this.smallPic;
    }
    
    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public int getStat() {
        return this.stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getTheme() {
        return this.theme;
    }
    
    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Set<HomePanel> getHomePanels() {
        return this.homePanels;
    }
    
    public void setHomePanels(Set<HomePanel> homePanels) {
        this.homePanels = homePanels;
    }

    public Set<AppModule> getAppModules() {
        return this.appModules;
    }
    
    public void setAppModules(Set<AppModule> appModules) {
        this.appModules = appModules;
    }

    public Set<AppMenu> getAppMenus() {
        return this.appMenus;
    }
    
    public void setAppMenus(Set<AppMenu> appMenus) {
        this.appMenus = appMenus;
    }


	public List<DictionaryType> getDictionaryTypes() {
		return dictionaryTypes;
	}


	public void setDictionaryTypes(List<DictionaryType> dictionaryTypes) {
		this.dictionaryTypes = dictionaryTypes;
	}
	
	public String getIndexUrl() {
		return indexUrl;
	}


	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}
}