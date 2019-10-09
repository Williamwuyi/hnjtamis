package cn.com.ite.eap2.domain.baseinfo;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.LogData</p>
 * <p>Description 日志数据</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-19 上午10:04:04
 * @version 2.0
 * 
 * @modified records:
 */
public class LogData implements java.io.Serializable {
	private static final long serialVersionUID = -8582075839177515176L;
	private String ldId;
	private LogMain logMain;
	private int dataOperaterType;
	private String dataKey;
	private int year;
	private int month;
	private String dataName;
	private String dataCode;
	private Integer no;
	private Set<LogSub> logSubs = new HashSet<LogSub>(0);

	// Constructors

	/** default constructor */
	public LogData() {
	}

	/** full constructor */
	public LogData(LogMain logMain, int dataOperaterType,
			String dataName, String dataCode, Integer no, Set<LogSub> logSubs) {
		this.logMain = logMain;
		this.dataOperaterType = dataOperaterType;
		this.dataName = dataName;
		this.dataCode = dataCode;
		this.no = no;
		this.logSubs = logSubs;
	}

	// Property accessors

	public String getLdId() {
		return this.ldId;
	}

	public void setLdId(String ldId) {
		this.ldId = ldId;
	}

	public LogMain getLogMain() {
		return this.logMain;
	}

	public void setLogMain(LogMain logMain) {
		this.logMain = logMain;
	}

	public int getDataOperaterType() {
		return this.dataOperaterType;
	}

	public void setDataOperaterType(int dataOperaterType) {
		this.dataOperaterType = dataOperaterType;
	}

	public String getDataName() {
		return this.dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDataCode() {
		return this.dataCode;
	}

	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}

	public Integer getNo() {
		return this.no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Set<LogSub> getLogSubs() {
		return this.logSubs;
	}

	public void setLogSubs(Set<LogSub> logSubs) {
		this.logSubs = logSubs;
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

	public String getDataKey() {
		return dataKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

}