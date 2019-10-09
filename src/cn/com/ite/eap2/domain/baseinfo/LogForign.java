package cn.com.ite.eap2.domain.baseinfo;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.LogForign</p>
 * <p>Description 日志外健关联ID</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-19 下午04:21:53
 * @version 2.0
 * 
 * @modified records:
 */
public class LogForign implements java.io.Serializable {
	private static final long serialVersionUID = -1788492638704271486L;
	private String lfId;
	private LogSub logSub;
	private String ldName;
	private LogData linkData;
	private String linkId;
	private int dataType;
	private int year;
	private int month;
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

	public String getLdName() {
		return ldName;
	}

	public void setLdName(String ldName) {
		this.ldName = ldName;
	}

	public LogData getLinkData() {
		return linkData;
	}

	public void setLinkData(LogData linkData) {
		this.linkData = linkData;
	}

	public LogForign() {
	}

	public String getLfId() {
		return this.lfId;
	}
	public void setLfId(String lfId) {
		this.lfId = lfId;
	}
	public LogSub getLogSub() {
		return this.logSub;
	}
	public void setLogSub(LogSub logSub) {
		this.logSub = logSub;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
}