package cn.com.ite.workflow.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlowNode</p>
 * <p>Description 工作流结点</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午01:56:36
 * @version 2.0
 * 
 * @modified records:
 */
@SuppressWarnings("serial")
public class WorkFlowNode implements java.io.Serializable {
	private String nodeId;
	private WorkFlow workFlow;
	private String name;
	private String code;
	private String type;
	private Integer excuteType;
	private String url;
	private String judgeExpress;
	private Integer orderNo;
	private Integer timer;
	private String timerToCode;
	private Set<WorkFlowTo> srcWorkFlowTos = new HashSet<WorkFlowTo>(0);
	private Set<WorkFlowTo> toWorkFlowTo = new HashSet<WorkFlowTo>(0);
	private Set<WorkFlowQuarter> workFlowQuarters = new HashSet<WorkFlowQuarter>(0);
	/**
	 * 获取定时导向
	 * @return
	 * @modified
	 */
	public WorkFlowTo getTimerTo(){
		for(WorkFlowTo to:srcWorkFlowTos){
			if(to.getCode().equals(timerToCode))
				return to;
		}
		return null;
	}

	// Constructors

	/** default constructor */
	public WorkFlowNode() {
	}

	// Property accessors

	public String getNodeId() {
		return this.nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public WorkFlow getWorkFlow() {
		return this.workFlow;
	}

	public void setWorkFlow(WorkFlow workFlow) {
		this.workFlow = workFlow;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getExcuteType() {
		return this.excuteType;
	}

	public void setExcuteType(Integer excuteType) {
		this.excuteType = excuteType;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJudgeExpress() {
		return this.judgeExpress;
	}

	public void setJudgeExpress(String judgeExpress) {
		this.judgeExpress = judgeExpress;
	}

	public Set<WorkFlowTo> getSrcWorkFlowTos() {
		return srcWorkFlowTos;
	}

	public void setSrcWorkFlowTos(Set<WorkFlowTo> srcWorkFlowTos) {
		this.srcWorkFlowTos = srcWorkFlowTos;
	}

	public Set<WorkFlowTo> getToWorkFlowTo() {
		return toWorkFlowTo;
	}

	public void setToWorkFlowTo(Set<WorkFlowTo> toWorkFlowTo) {
		this.toWorkFlowTo = toWorkFlowTo;
	}

	public Set<WorkFlowQuarter> getWorkFlowQuarters() {
		return workFlowQuarters;
	}

	public void setWorkFlowQuarters(Set<WorkFlowQuarter> workFlowQuarters) {
		this.workFlowQuarters = workFlowQuarters;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getTimer() {
		return timer;
	}

	public void setTimer(Integer timer) {
		this.timer = timer;
	}

	public String getTimerToCode() {
		return timerToCode;
	}

	public void setTimerToCode(String timerToCode) {
		this.timerToCode = timerToCode;
	}
}