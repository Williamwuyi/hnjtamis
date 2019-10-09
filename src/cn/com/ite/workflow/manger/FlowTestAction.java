package cn.com.ite.workflow.manger;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.workflow.domain.WorkFlowExcute;
import cn.com.ite.workflow.face.AuditInfo;
import cn.com.ite.workflow.face.IWorkFlow;

/**
 * <p>Title cn.com.ite.workflow.manger.TestAction</p>
 * <p>Description 工作流ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-24 下午01:27:22
 * @version 2.0
 * 
 * @modified records:
 */
public class FlowTestAction extends AbstractListAction{
	private static final long serialVersionUID = -9009585488482082495L;
	private String employeeId;
	private String flowCode;
	private String taskCode;
	private Boolean complet;
	private int completType;
	private Date startDate;
	private Date endDate;
	private List<WorkFlowExcute> list;
	private String json;
	
	private List<TextValue> param = new ArrayList<TextValue>();
	private List<AuditInfo> auditInfos = new ArrayList<AuditInfo>();
	private List<FlowInfo> flowInfos = new ArrayList<FlowInfo>();
	
	private String[] text;
	private String[] value;
	
	private WorkFlowExcute excute;
	private String excuteId;
	private String advice;
	private String toCode;
	private String taskUrl;
	//人员条件
	private String man;
	/**
	 * 查询提醒
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String taskTip() throws Exception{
		WorkFlowService flowService = (WorkFlowService)service;
		list = flowService.findTaskTip(employeeId);
		return "testList";
	}
	/**
	 * 取消提醒
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String canelTip()throws Exception{
		WorkFlowService flowService = (WorkFlowService)service;
		flowService.canelTip(excuteId);
		return "testList";
	}
	/**
	 * 任务查询
	 * @return
	 * @throws Exception
	 */
	public String testList() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		list = flowService.findExcuteTask(flowCode, employeeId, startDate, 
			endDate, taskCode, null, complet, true, this.getStart(), this.getLimit());
		this.setTotal(flowService.findExcuteTask(flowCode, employeeId, startDate, 
				endDate, taskCode, null, complet, true, 0,0).size());
		return "testList";
	}
	
	/**
	 * 流程监控
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String flowMoniter() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map term = new HashMap();
		term.put("createId", employeeId);
		term.put("flowCode", flowCode);
		term.put("startDate", startDate);
		term.put("endDate", endDate);
		term.put("man", man);
		term.put("complet", completType);
		flowInfos = flowService.findFlowInfo(term, this.getStart(), this.getLimit());
		this.setTotal(flowService.findFlowInfo(term, 0, 0).size());
		return "flowMoniter";
	}
	/**
	 * 流程映射查询
	 * @return
	 * @throws Exception
	 */
	public String flowMap() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map<String,String> map = flowService.getFlowMap();
		for(Object key:map.keySet()){
			param.add(new TextValue((String)map.get(key),(String)key));
		}
		return "map";
	}
	/**
	 * 启动流程
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String start() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		if(text!=null)
		for(int i=0;i<text.length;i++){
			map.put(value[i],text[i]);
		}
		IWorkFlow flowService = (IWorkFlow)service;
		flowService.startWorkFlow(flowCode, employeeId, map, null);
		return "save";
	}
	/**
	 * 结点映射查询
	 * @return
	 * @throws Exception
	 */
	public String nodeMap() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map<String,String> map = flowService.getTaskMap(flowCode, employeeId);
		for(Object key:map.keySet()){
			param.add(new TextValue((String)map.get(key),(String)key));
		}
		return "map";
	}
	/**
	 * 状态映射查询
	 * @return
	 * @throws Exception
	 */
	public String stateMap() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map<String,String> map = flowService.getStateMapByFlowCode(flowCode);
		for(Object key:map.keySet()){
			param.add(new TextValue((String)map.get(key),(String)key));
		}
		return "map";
	}
	/**
	 * 获取任务的操作地址
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String url() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map term = new HashMap();
		term.put("id", this.getId());
		taskUrl = flowService.getTaskUrl(taskCode, term, employeeId);
		excuteId = taskUrl.split("excuteId=")[1];
		Map<String,String> map = flowService.getStateMapByExcuteId(excuteId);
		for(Object key:map.keySet()){
			param.add(new TextValue((String)map.get(key),(String)key));
		}
		return "url";
	}
	/**
	 * 查询任务信息
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String findTask() throws Exception{
		try{
			IWorkFlow flowService = (IWorkFlow)service;
			excute = (WorkFlowExcute)service.findDataByKey(this.getId(), WorkFlowExcute.class);
			excute.setAudits(flowService.getAuditInfo(excute.getBusId()));
			excute.getWorkFlowNode();
			excute.getWorkFlowTo();
		}catch(Exception e){			
			e.printStackTrace();
			throw e;
		}
		return "findTask";
	}
	/**
	 * 获取审核历史信息
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String auditInfo() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map param = new HashMap();
		param.put("id", this.getId());
		auditInfos = flowService.getAuditInfo(flowCode,param);
		return "auditInfo";
	}
	/**
	 * 获取状态类型
	 * @return
	 * @modified
	 */
	public String stateType() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		Map param = new HashMap();
		param.put("id", this.getId());
		if(StringUtils.isEmpty(this.getState()))
			this.setMsg("NO");
		else
		if(flowService.judgeStartState(flowCode, this.getState(), param))
			this.setMsg("START");
		else if(flowService.judgeEndState(flowCode, this.getState(), param))
			this.setMsg("END");
		else
			this.setMsg("PROCESS");
		return "save";
	}
	/**
	 * 接收任务
	 * @return
	 * @throws Exception
	 */
	public String take() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		try{
		    flowService.receiveTask(this.getId(), employeeId);
		}catch(Exception e){			
			e.printStackTrace();
			throw e;
		}
		return "delete";
	}
	/**
	 * 撤消任务
	 * @return
	 * @throws Exception
	 */
	public String undo() throws Exception{
		IWorkFlow flowService = (IWorkFlow)service;
		try{
		flowService.undo(this.getId(), employeeId);
		}catch(Exception e){			
			e.printStackTrace();
			throw e;
		}
		return "delete";
	}
	/**
	 * 工作流的执行测试
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String excute() throws Exception{
		try{
			IWorkFlow flowService = (IWorkFlow)service;
			Map p = new HashMap();
			Gson gson = new Gson();
			param = gson.fromJson(json, new TypeToken<List<TextValue>>(){}.getType());
			if(param!=null)
			for(TextValue tv:param)
				p.put(tv.getValue(), tv.getText());
			flowService.next(excuteId, toCode, p, employeeId, advice, ServletContent.getIP(), true);
		}catch(Exception e){			
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public Boolean getComplet() {
		return complet;
	}
	public void setComplet(Boolean complet) {
		this.complet = complet;
	}
	public String getFlowCode() {
		return flowCode;
	}
	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}
	public String getTaskCode() {
		return taskCode;
	}
	public void setTaskCode(String taskCode) {
		this.taskCode = taskCode;
	}
	public List<WorkFlowExcute> getList() {
		return list;
	}
	public void setList(List<WorkFlowExcute> list) {
		this.list = list;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public List<TextValue> getParam() {
		return param;
	}
	public void setParam(List<TextValue> param) {
		this.param = param;
	}
	public String[] getText() {
		return text;
	}
	public void setText(String[] text) {
		this.text = text;
	}
	public String[] getValue() {
		return value;
	}
	public void setValue(String[] value) {
		this.value = value;
	}
	public WorkFlowExcute getExcute() {
		return excute;
	}
	public void setExcute(WorkFlowExcute excute) {
		this.excute = excute;
	}
	public String getExcuteId() {
		return excuteId;
	}
	public void setExcuteId(String excuteId) {
		this.excuteId = excuteId;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	public String getToCode() {
		return toCode;
	}
	public void setToCode(String toCode) {
		this.toCode = toCode;
	}
	public String getTaskUrl() {
		return taskUrl;
	}
	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}
	public List<AuditInfo> getAuditInfos() {
		return auditInfos;
	}
	public void setAuditInfos(List<AuditInfo> auditInfos) {
		this.auditInfos = auditInfos;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public List<FlowInfo> getFlowInfos() {
		return flowInfos;
	}
	public void setFlowInfos(List<FlowInfo> flowInfos) {
		this.flowInfos = flowInfos;
	}
	public String getMan() {
		return man;
	}
	public void setMan(String man) {
		this.man = man;
	}
	public int getCompletType() {
		return completType;
	}
	public void setCompletType(int completType) {
		this.completType = completType;
	}
}