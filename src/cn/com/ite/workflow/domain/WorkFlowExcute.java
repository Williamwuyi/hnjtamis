package cn.com.ite.workflow.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.json.annotations.JSON;

import cn.com.ite.workflow.face.AuditInfo;
import cn.com.ite.workflow.manger.TextValue;

/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlowExcute</p>
 * <p>Description 流程执行</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午01:55:37
 * @version 2.0
 * 
 * @modified records:
 */
@SuppressWarnings("serial")
public class WorkFlowExcute implements java.io.Serializable {

	private String excuteId;
	private WorkFlowTo workFlowTo;
	private WorkFlowNode workFlowNode;
	private String busId;
	private String parentBusId;
	private String taskName;
	private String quarterId;
	private String quarterName;
	private String employeeId;
	private String employeeName;
	private String agent;
	private Date createTime;
	private Date excuteTiime;
	private Date planTime;
	private String ip;
	private String advice;
	private Boolean ifUndo;
	private Integer orderNo;
	/**
	 * 流程参数
	 */
	private Map<String,String> param = new HashMap<String,String>();
	
	//不保存
	private List<AuditInfo> audits = new ArrayList<AuditInfo>();

	/** default constructor */
	public WorkFlowExcute() {
	}
	/**
	 * 获取应用地址
	 * @return
	 */
	public String getUrl(){
		String url = workFlowNode.getUrl();
		boolean have$ = false;
		if(url==null)
			return "excuteId="+this.getExcuteId();
		if(url.indexOf("$")>0)
		for(String key:param.keySet()){
			url = url.replaceAll("\\$"+key, param.get(key));
		}else
		for(String key:param.keySet()){
			url += (url.indexOf("?")>0?"&":"?")+key+"="+param.get(key);
		}
		return url;
	}
	
	public List<TextValue> getParamInfo(){
		List<TextValue> list = new ArrayList<TextValue>();
		for(String key:param.keySet())
			list.add(new TextValue((String)param.get(key),(String)key));
		return list;
	}
	
	public List<TextValue> getOperater(){
		List<TextValue> list = new ArrayList<TextValue>();
		for(WorkFlowTo to:workFlowNode.getSrcWorkFlowTos())
			list.add(new TextValue(to.getName(),to.getCode()));
		return list;
	}
	
	public String getFlowName(){
		return workFlowNode.getWorkFlow().getName();
	}

	public String getExcuteId() {
		return this.excuteId;
	}

	public void setExcuteId(String excuteId) {
		this.excuteId = excuteId;
	}
	@JSON(serialize=false)
	public WorkFlowTo getWorkFlowTo() {
		return this.workFlowTo;
	}
	@JSON(serialize=false)
	public void setWorkFlowTo(WorkFlowTo workFlowTo) {
		this.workFlowTo = workFlowTo;
	}

	public WorkFlowNode getWorkFlowNode() {
		return this.workFlowNode;
	}

	public void setWorkFlowNode(WorkFlowNode workFlowNode) {
		this.workFlowNode = workFlowNode;
	}

	public String getBusId() {
		return this.busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getTaskName() {
		return this.taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getQuarterId() {
		return this.quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public String getQuarterName() {
		return this.quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getAgent() {
		return this.agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getExcuteTiime() {
		return this.excuteTiime;
	}

	public void setExcuteTiime(Date excuteTiime) {
		this.excuteTiime = excuteTiime;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAdvice() {
		return this.advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public Boolean getIfUndo() {
		return this.ifUndo;
	}

	public void setIfUndo(Boolean ifUndo) {
		this.ifUndo = ifUndo;
	}

	public Integer getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getParentBusId() {
		return parentBusId;
	}

	public void setParentBusId(String parentBusId) {
		this.parentBusId = parentBusId;
	}

	public Map<String, String> getParam() {
		return param;
	}

	public void setParam(Map<String, String> param) {
		this.param = param;
	}

	public List<AuditInfo> getAudits() {
		return audits;
	}

	public void setAudits(List<AuditInfo> audits) {
		this.audits = audits;
	}
	
	public String getToName(){
		if(this.workFlowTo!=null)
			return this.workFlowTo.getName();
		else
			return null;
	}
	public Date getPlanTime() {
		return planTime;
	}
	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

}