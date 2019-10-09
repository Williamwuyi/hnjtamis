package cn.com.ite.eap2.domain.funres;



/**
 * <p>Title cn.com.ite.eap2.domain.funres.HomePanel</p>
 * <p>Description 首页面板</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 上午09:26:55
 * @version 2.0
 * 
 * @modified records:
 */
public class HomePanel  implements java.io.Serializable {
	 private static final long serialVersionUID = 7956885171179384868L;
     private String hpId;
     private AppSystem appSystem;
     private int pageNo;
     private int row;
     private int col;
     private String title;
     private String mete;
     private int displayType;
     private String resourceId;


    // Constructors

    /** default constructor */
    public HomePanel() {
    }

    
    /** full constructor */
    public HomePanel(AppSystem appSystem, Byte pageNo, Byte row, Byte col, String title, String mete, Byte displayType, String resourceId) {
        this.appSystem = appSystem;
        this.pageNo = pageNo;
        this.row = row;
        this.col = col;
        this.title = title;
        this.mete = mete;
        this.displayType = displayType;
        this.resourceId = resourceId;
    }

   
    // Property accessors

    public String getHpId() {
        return this.hpId;
    }
    
    public void setHpId(String hpId) {
        this.hpId = hpId;
    }

    public AppSystem getAppSystem() {
        return this.appSystem;
    }
    
    public void setAppSystem(AppSystem appSystem) {
        this.appSystem = appSystem;
    }

    public int getPageNo() {
        return this.pageNo;
    }
    
    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getRow() {
        return this.row;
    }
    
    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }
    
    public void setCol(int col) {
        this.col = col;
    }

    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getMete() {
        return this.mete;
    }
    
    public void setMete(String mete) {
        this.mete = mete;
    }

    public int getDisplayType() {
        return this.displayType;
    }
    
    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    public String getResourceId() {
        return this.resourceId;
    }
    
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
   








}