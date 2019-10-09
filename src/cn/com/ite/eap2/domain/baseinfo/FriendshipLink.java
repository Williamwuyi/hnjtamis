package cn.com.ite.eap2.domain.baseinfo;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.FriendshipLink</p>
 * <p>Description 友情链结</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-12 时间08:58:45
 * @version 2.0
 * 
 * @modified records:
 */
public class FriendshipLink implements java.io.Serializable {
	private static final long serialVersionUID = 5158374902791460599L;
	private String flId;
	//名称
	private String flName;
	//路径
	private String flUrl;
	//图标
	private String flIcon;
	//图标大小
	private String flIconSize;

	/** default constructor */
	public FriendshipLink() {
	}

	/** full constructor */
	public FriendshipLink(String flName, String flUrl, String flIcon,
			String flIconSize) {
		this.flName = flName;
		this.flUrl = flUrl;
		this.flIcon = flIcon;
		this.flIconSize = flIconSize;
	}

	public String getFlId() {
		return this.flId;
	}

	public void setFlId(String flId) {
		this.flId = flId;
	}

	public String getFlName() {
		return this.flName;
	}

	public void setFlName(String flName) {
		this.flName = flName;
	}

	public String getFlUrl() {
		return this.flUrl;
	}

	public void setFlUrl(String flUrl) {
		this.flUrl = flUrl;
	}

	public String getFlIcon() {
		return this.flIcon;
	}

	public void setFlIcon(String flIcon) {
		this.flIcon = flIcon;
	}

	public String getFlIconSize() {
		return this.flIconSize;
	}

	public void setFlIconSize(String flIconSize) {
		this.flIconSize = flIconSize;
	}
}