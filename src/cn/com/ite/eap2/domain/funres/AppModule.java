package cn.com.ite.eap2.domain.funres;

import java.util.*;

import cn.com.ite.eap2.domain.baseinfo.DictionaryType;


/**
 * <p>Title cn.com.ite.eap2.domain.funres.AppModule</p>
 * <p>Description 系统模块</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 上午09:25:59
 * @version 2.0
 * 
 * @modified records:
 */
public class AppModule  implements java.io.Serializable {
	private static final long serialVersionUID = -6016728125843669395L;
	private String moduleId;
    private AppModule appModule;
    private AppSystem appSystem;
    private String moduleCode;
    private String moduleName;
    private String description;
    private Integer orderNo;
    private String levelCode;
    private List<ModuleResource> moduleResources = new ArrayList<ModuleResource>(0);
    private List<AppModule> appModules = new ArrayList<AppModule>(0);
	private List<DictionaryType> dictionaryTypes = new ArrayList<DictionaryType>(0);

    /** default constructor */
    public AppModule() {
    }

    public String getModuleId() {
        return this.moduleId;
    }
    
    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public AppModule getAppModule() {
        return this.appModule;
    }
    
    public void setAppModule(AppModule appModule) {
        this.appModule = appModule;
    }

    public AppSystem getAppSystem() {
        return this.appSystem;
    }
    
    public void setAppSystem(AppSystem appSystem) {
        this.appSystem = appSystem;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }
    
    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getModuleName() {
        return this.moduleName;
    }
    
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getLevelCode() {
        return this.levelCode;
    }
    
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public List<ModuleResource> getModuleResources() {
        return this.moduleResources;
    }
    
    public void setModuleResources(List<ModuleResource> moduleResources) {
        this.moduleResources = moduleResources;
    }

    public List<AppModule> getAppModules() {
        return this.appModules;
    }
    
    public void setAppModules(List<AppModule> appModules) {
        this.appModules = appModules;
    }
    public List<DictionaryType> getDictionaryTypes() {
		return dictionaryTypes;
	}

	public void setDictionaryTypes(List<DictionaryType> dictionaryTypes) {
		this.dictionaryTypes = dictionaryTypes;
	}
}