package cn.com.ite.eap2.domain.baseinfo;

import java.sql.Date;
import java.util.*;

import org.apache.struts2.json.annotations.JSON;

import cn.com.ite.eap2.common.utils.DateUtils;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.SysAffiche</p>
 * <p>Description 系统公告</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-5-21 时间01:25:43
 * @version 2.0
 * 
 * @modified records:
 */
public class SysAffiche implements java.io.Serializable {
	private static final long serialVersionUID = 1147182706060686351L;
	/**
	 * ID
	 */
	private String saId;
	/**
	 * 发送者
	 */
	private String sender;
	/**
	 * 发送时间
	 */
	private Date sendTime;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 有效时间
	 */
	private Integer deadline;
	/**
	 * 不起作用
	 */
	private String accessoriesItemId;
	
	private String relationId;//关联ID
	private String relationType;//关联类型
	
	/**
	 * 附件
	 */
	private Set<Accessory> accessories = new HashSet<Accessory>();
	
	private List<SysAfficheIncepter> sysAfficheIncepters = new ArrayList<SysAfficheIncepter>(0);
	
	private String userReadTime;
	
	public String getTitleView(){
		return "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"+
                "<tr><td style=\"height: 52px; text-align: center; color: #1584d9; font-size: 18px;\">"+this.title+"</td></tr>"+
                "<tr><td style=\"text-align: center; color: #999999; border-bottom: #1584d9 dashed 1px; font-size: 13px; height: 26px;\">" +
                "时间：<font color=\"#085a8e\">"+DateUtils.convertDateToStr(this.getSendTime(),"yyyy-MM-dd")+
                "</font>&nbsp;&nbsp; 作者： "+this.getSender()+"</font></td></tr><tr></table>";
	}
	public Set<Accessory> getAccessories() {
		return accessories;
	}

	public void setAccessories(Set<Accessory> accessories) {
		this.accessories = accessories;
	}

	/** default constructor */
	public SysAffiche() {
	}

	/** minimal constructor */
	public SysAffiche(String saId) {
		this.saId = saId;
	}

	/** full constructor */
	public SysAffiche(String saId, String sender, Date sendTime, String title,
			String content, Integer deadline, String accessoriesItemId,List<SysAfficheIncepter> sysAfficheIncepters,
			String relationId,String relationType) {
		this.saId = saId;
		this.sender = sender;
		this.sendTime = sendTime;
		this.title = title;
		this.content = content;
		this.deadline = deadline;
		this.accessoriesItemId = accessoriesItemId;
		this.sysAfficheIncepters=sysAfficheIncepters;
		this.relationId=relationId;
		this.relationType=relationType;
	}

	// Property accessors

	public String getSaId() {
		return this.saId;
	}

	public void setSaId(String saId) {
		this.saId = saId;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDeadline() {
		return this.deadline;
	}

	public void setDeadline(Integer deadline) {
		this.deadline = deadline;
	}

	public String getAccessoriesItemId() {
		return this.accessoriesItemId;
	}

	public void setAccessoriesItemId(String accessoriesItemId) {
		this.accessoriesItemId = accessoriesItemId;
	}
	
	public List<SysAfficheIncepter> getSysAfficheIncepters() {
		return sysAfficheIncepters;
	}
	
	public void setSysAfficheIncepters(List<SysAfficheIncepter> sysAfficheIncepters) {
		this.sysAfficheIncepters = sysAfficheIncepters;
	}
	public String getUserReadTime() {
		return userReadTime;
	}
	public void setUserReadTime(String userReadTime) {
		this.userReadTime = userReadTime;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	
	
}