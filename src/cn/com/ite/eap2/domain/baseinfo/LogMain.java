package cn.com.ite.eap2.domain.baseinfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;

/**
 * <p>Title cn.com.ite.eap2.domain.baseinfo.LogMain</p>
 * <p>Description 日志主信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-19 上午10:02:27
 * @version 2.0
 * 
 * @modified records:
 */
public class LogMain implements java.io.Serializable {
	private static final long serialVersionUID = -4846079464587033960L;
	private String logId;
	private String sessionId;
	private String user;
	private String employee;
	private String dept;
	private String organ;
	private String ip;
	private int type;
	private String content;
	private String app;
	private String moduleName;
	private String workName;
	private Long useTime;
	private int year;
	private int month;
	private int day;
	private int time;
	private Set<LogData> logDatas = new HashSet<LogData>(0);
	/**
	 * 用于日志缓存处理的临时变量
	 */
	@SuppressWarnings("unchecked")
	private List entitys = new ArrayList();
	@SuppressWarnings("unchecked")
	private List opers = new ArrayList();
	//修改前的数据
	@SuppressWarnings("unchecked")
	private List olds = new ArrayList();

	/** default constructor */
	public LogMain() {
	}
	/**
	 * 获取组合时间值
	 * @return
	 */
	public int[] getDateArray(){
		return new int[]{year,month,day,time};
	}
	public void setDateArray(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		this.year = cal.get(Calendar.YEAR);
		this.month = cal.get(Calendar.MONTH)+1;
		this.day = cal.get(Calendar.DAY_OF_MONTH);
		this.time = cal.get(Calendar.HOUR_OF_DAY)*3600+
		   cal.get(Calendar.MINUTE)*60+cal.get(Calendar.SECOND);
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Date getDateObject(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.year);
		cal.set(Calendar.MONTH, this.month-1);
		cal.set(Calendar.DAY_OF_MONTH, this.day);
		cal.set(Calendar.HOUR_OF_DAY, (int)(this.time/3600));
		cal.set(Calendar.MINUTE, (int)((this.time%3600)/60));
		return cal.getTime();
	}
	
	public String getEmployeeUser(){
		return (this.employee==null?"":this.employee)+(this.user==null?"":("("+this.user+")"));
	}

	/**
	 * 查询日志子数据
	 * @param clazz
	 * @param key
	 * @return
	 * @modified
	 */
   /* public LogSub findLogSub(String clazz,Serializable key){
		for(LogData ld:logDatas){
			if(!ld.getDataCode().equals(clazz))
				for(LogSub ls:ld.getLogSubs()){
					boolean have = false;
					if(ls.getAttType()==3&&clazz.equals(ls.getAttValue()))
					for(LogForign lf:ls.getLogForigns()){
						if(lf.getItemId().equals(key)){
							have = true;
							break;
						}
					}
					if(have) return ls;
				}
		}
		return null;
	}*/

	public String getLogId() {
		return this.logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmployee() {
		return this.employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public String getDept() {
		return this.dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getOrgan() {
		return this.organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getApp() {
		return this.app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getWorkName() {
		return this.workName;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public Long getUseTime() {
		return this.useTime;
	}

	public void setUseTime(Long useTime) {
		this.useTime = useTime;
	}

	public Set<LogData> getLogDatas() {
		return this.logDatas;
	}

	public void setLogDatas(Set<LogData> logDatas) {
		this.logDatas = logDatas;
	}

	@SuppressWarnings("unchecked")
	public List getEntitys() {
		return entitys;
	}

	@SuppressWarnings("unchecked")
	public void setEntitys(List entitys) {
		this.entitys = entitys;
	}

	@SuppressWarnings("unchecked")
	public List getOpers() {
		return opers;
	}

	@SuppressWarnings("unchecked")
	public void setOpers(List opers) {
		this.opers = opers;
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

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	@SuppressWarnings("unchecked")
	public List getOlds() {
		return olds;
	}
	@SuppressWarnings("unchecked")
	public void setOlds(List olds) {
		this.olds = olds;
	}
}