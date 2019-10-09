package cn.com.ite.eap2.log;

import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.baseinfo.LogMain;

/**
 * <p>Title cn.com.ite.eap2.log.LogListAction</p>
 * <p>Description 日志ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-23 上午08:39:49
 * @version 2.0
 * 
 * @modified records:
 */
public class LogListAction extends AbstractListAction {
	private static final long serialVersionUID = -2891610427793350391L;
	/**
	 * 用户条件
	 */
	private String userTerm;
	/**
	 * 年条件
	 */
	private int yearTerm = 2014;
	/**
	 * 月条件
	 */
	private int monthTerm;
	/**
	 * 日开始条件
	 */
	private int dayStartTerm;
	/**
	 * 日结束条件
	 */
	private int dayEndTerm;
	/**
	 * 日志类型条件
	 */
	private int typeTerm;
	/**
	 * 内容条件
	 */
	private String contentTerm;
	/**
	 * 系统条件
	 */
	private String appTerm;
	/**
	 * 模块条件
	 */
	private String moduleTerm;
	/**
	 * 查询结果
	 */
	private List<LogMain> list;
	/**
	 * 列表查询
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list(){
		list = (List<LogMain>)service.queryData("querySql", this,null, this.getStart(), this.getLimit());
		this.setTotal(service.countData("querySql", this));
		return "list";
	}
	public List<LogMain> getList() {
		return list;
	}
	public void setList(List<LogMain> list) {
		this.list = list;
	}
	public String getUserTerm() {
		return userTerm;
	}
	public void setUserTerm(String userTerm) {
		this.userTerm = userTerm;
	}
	public int getYearTerm() {
		return yearTerm;
	}
	public void setYearTerm(int yearTerm) {
		this.yearTerm = yearTerm;
	}
	public int getMonthTerm() {
		return monthTerm;
	}
	public void setMonthTerm(int monthTerm) {
		this.monthTerm = monthTerm;
	}
	public int getDayStartTerm() {
		return dayStartTerm;
	}
	public void setDayStartTerm(int dayStartTerm) {
		this.dayStartTerm = dayStartTerm;
	}
	public int getDayEndTerm() {
		return dayEndTerm;
	}
	public void setDayEndTerm(int dayEndTerm) {
		this.dayEndTerm = dayEndTerm;
	}
	public int getTypeTerm() {
		return typeTerm;
	}
	public void setTypeTerm(int typeTerm) {
		this.typeTerm = typeTerm;
	}
	public String getContentTerm() {
		return contentTerm;
	}
	public void setContentTerm(String contentTerm) {
		this.contentTerm = contentTerm;
	}
	public String getAppTerm() {
		return appTerm;
	}
	public void setAppTerm(String appTerm) {
		this.appTerm = appTerm;
	}
	public String getModuleTerm() {
		return moduleTerm;
	}
	public void setModuleTerm(String moduleTerm) {
		this.moduleTerm = moduleTerm;
	}
}