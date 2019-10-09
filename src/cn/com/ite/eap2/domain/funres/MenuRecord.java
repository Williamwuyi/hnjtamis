package cn.com.ite.eap2.domain.funres;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * <p>Title cn.com.ite.eap2.domain.funres.MenuRecord</p>
 * <p>Description 菜单记录</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-12-10 下午01:15:45
 * @version 2.0
 * 
 * @modified records:
 */
public class MenuRecord implements java.io.Serializable {
	private static final long serialVersionUID = 4624793749453091065L;
	private String mrId;
	private AppMenu appMenu;
	private String userId;
	private Date openTime;
	private Integer userSize;
	private Integer orderNo;

	// Constructors

	/** default constructor */
	public MenuRecord() {
	}

	/** full constructor */
	public MenuRecord(AppMenu appMenu, String userId, Date openTime,
			Integer userSize) {
		this.appMenu = appMenu;
		this.userId = userId;
		this.openTime = openTime;
		this.userSize = userSize;
	}

	// Property accessors

	public String getMrId() {
		return this.mrId;
	}

	public void setMrId(String mrId) {
		this.mrId = mrId;
	}

	public AppMenu getAppMenu() {
		return this.appMenu;
	}

	public void setAppMenu(AppMenu appMenu) {
		this.appMenu = appMenu;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getOpenTime() {
		return this.openTime;
	}
	
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Integer getUserSize() {
		return this.userSize;
	}

	public void setUserSize(Integer userSize) {
		this.userSize = userSize;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
}