package cn.com.ite.eap2.domain.funres;

import java.io.File;
import java.util.*;


/**
 * <p>Title cn.com.ite.eap2.domain.funres.AppMenu</p>
 * <p>Description 系统菜单</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 上午09:25:28
 * @version 2.0
 * 
 * @modified records:
 */
public class AppMenu  implements java.io.Serializable {
	private static final long serialVersionUID = 7955753783564198375L;

    private String menuId;
    private AppSystem appSystem;
    private ModuleResource moduleResource;
    private AppMenu appMenu;
    private String menuName;
    private String tabName;
    private int target;
    private String otherUrl;
    private boolean hidden;
    private int menuType;
    private Integer orderNo;
    private String description;
    private boolean expand;
    private String helpUrl;
    private String helpContent;
    private String levelCode;
    private List<AppMenu> appMenus = new ArrayList<AppMenu>(0);
    
    public String getUrl(){
    	if(moduleResource!=null&&moduleResource.getResourceType()==1){
    		String url = moduleResource.getResourceUrl();
    		String url0 = url.split("\\|")[0];
    		return appSystem.getAppUrl()+"/"+url0;
    	}else
    		return this.otherUrl;
    }
    
    public String getIcon(){
    	if(moduleResource!=null)
    		return moduleResource.getIcon();
    	else
    		return null;
    }

    /** default constructor */
    public AppMenu() {
    }
   
    // Property accessors

    public String getMenuId() {
        return this.menuId;
    }
    
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public AppSystem getAppSystem() {
        return this.appSystem;
    }
    
    public void setAppSystem(AppSystem appSystem) {
        this.appSystem = appSystem;
    }

    public ModuleResource getModuleResource() {
        return this.moduleResource;
    }
    
    public void setModuleResource(ModuleResource moduleResource) {
        this.moduleResource = moduleResource;
    }

    public AppMenu getAppMenu() {
        return this.appMenu;
    }
    
    public void setAppMenu(AppMenu appMenu) {
        this.appMenu = appMenu;
    }

    public String getMenuName() {
        return this.menuName;
    }
    
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getTabName() {
        return this.tabName;
    }
    
    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
    
    public void setTarget(Byte target) {
        this.target = target;
    }

    public String getOtherUrl() {
        return this.otherUrl;
    }
    
    public void setOtherUrl(String otherUrl) {
        this.otherUrl = otherUrl;
    }
    
    public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public int getMenuType() {
		return menuType;
	}

	public void setMenuType(int menuType) {
		this.menuType = menuType;
	}

	public boolean isExpand() {
		return expand;
	}
	
	public boolean getExpand() {
		return expand;
	}

	public void setExpand(boolean expand) {
		this.expand = expand;
	}

	public void setMenuType(Byte menuType) {
        this.menuType = menuType;
    }

    public Integer getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelpUrl() {
        return this.helpUrl;
    }
    
    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getHelpContent() {
        return this.helpContent;
    }
    
    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }

    public String getLevelCode() {
        return this.levelCode;
    }
    
    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public List<AppMenu> getAppMenus() {
        return this.appMenus;
    }
    
    public void setAppMenus(List<AppMenu> appMenus) {
        this.appMenus = appMenus;
    }
}