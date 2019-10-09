package cn.com.ite.hnjtamis.mainpage.domain;
/** 
 * <p>Title 岗位达标培训信息系统-管理模块</p>
 * <p>Description 个人岗位达标信息VO  </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create 2015May 12, 2015 
 * @version 1.0
 * 
 * @modified records:
 */
public class ViewPersonProgress implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4536073060333511336L;
	private String organname;
	private String personname;
	private String contents;
	private String datetime; // 日期
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public String getOrganname() {
		return organname;
	}
	public void setOrganname(String organname) {
		this.organname = organname;
	}
	public String getPersonname() {
		return personname;
	}
	public void setPersonname(String personname) {
		this.personname = personname;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
