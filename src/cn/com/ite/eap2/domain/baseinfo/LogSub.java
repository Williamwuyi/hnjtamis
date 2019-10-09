package cn.com.ite.eap2.domain.baseinfo;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.LogSub</p>
 * <p>Description 日志子数据</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-19 上午10:05:35
 * @version 2.0
 * 
 * @modified records:
 */
public class LogSub implements java.io.Serializable {
	private static final long serialVersionUID = -461014766796459840L;
	private String lsId;
	private LogData logData;
	private LogData linkData;
	private String attName;
	private String attCode;
	private int attType;
	private String attObject;
	private String attValue;
	private String attOldValue;
	private int linkType;
	private String linkId;
	private Integer no;
	private int year;
	private int month;
	private Set<LogForign> logForigns = new HashSet<LogForign>(0);
	public String getAttObject() {
		return attObject;
	}

	public void setAttObject(String attObject) {
		this.attObject = attObject;
	}
	public Set<LogForign> getLogForigns() {
		return logForigns;
	}

	public void setLogForigns(Set<LogForign> logForigns) {
		this.logForigns = logForigns;
	}
	/** default constructor */
	public LogSub() {
	}
	/** full constructor */
	public LogSub(LogData logData, String attName, String attCode,
			int attType, String attValue, Integer no) {
		this.logData = logData;
		this.attName = attName;
		this.attCode = attCode;
		this.attType = attType;
		this.attValue = attValue;
		this.no = no;
	}
	public String getLsId() {
		return this.lsId;
	}
	public void setLsId(String lsId) {
		this.lsId = lsId;
	}
	public LogData getLogData() {
		return this.logData;
	}
	public void setLogData(LogData logData) {
		this.logData = logData;
	}
	public String getAttName() {
		return this.attName;
	}
	public void setAttName(String attName) {
		this.attName = attName;
	}
	public String getAttCode() {
		return this.attCode;
	}
	public void setAttCode(String attCode) {
		this.attCode = attCode;
	}
	public int getAttType() {
		return this.attType;
	}
	public void setAttType(int attType) {
		this.attType = attType;
	}
	public String getAttValue() {
		return this.attValue;
	}
	/**
	 * 扩展属性输出，针对引用集合属性信息进行显示
	 * @return
	 * @modified
	 */
	public String getAttValueEx() {
		if(!StringUtils.isEmpty(this.attValue))
		  return this.attValue;
		else{
			String info = null;
			for(LogForign lf:this.getLogForigns()){
				if(lf.getDataType()==1){
					if(info==null)
						info = lf.getLdName();
					else
						info += ","+lf.getLdName();
				}
			}
			return info;
		}
	}
	public void setAttValue(String attValue) {
		this.attValue = attValue;
	}
	public Integer getNo() {
		return this.no;
	}
	public void setNo(Integer no) {
		this.no = no;
	}
	public LogData getLinkData() {
		return linkData;
	}
	public void setLinkData(LogData linkData) {
		this.linkData = linkData;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}

	public String getAttOldValue() {
		return attOldValue;
	}
	/**
	 * 扩展属性输出，针对引用集合属性信息进行显示
	 * @return
	 * @modified
	 */
	public String getAttOldValueEx() {
		if(!StringUtils.isEmpty(this.attOldValue))
		  return this.attOldValue;
		else{
			String info = null;
			for(LogForign lf:this.getLogForigns()){
				if(lf.getDataType()==2){
					if(info==null)
						info = lf.getLdName();
					else
						info += ","+lf.getLdName();
				}
			}
			return info;
		}
	}

	public void setAttOldValue(String attOldValue) {
		this.attOldValue = attOldValue;
	}

	public int getLinkType() {
		return linkType;
	}

	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
}