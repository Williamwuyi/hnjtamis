package cn.com.ite.workflow.manger;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * <p>Title cn.com.ite.workflow.manger.FlowInfo</p>
 * <p>Description 流程监控信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-5-13 下午01:35:04
 * @version 2.0
 * 
 * @modified records:
 */
public class FlowInfo {
	private String busId;
	private String flowName;
	private String flowTitle;
	private String creater;
	private Date createTime;
	private Date lastTime;
	private Date endTime;
	private String funId;
	private String url;
	/**
	 * 参与者
	 */
	private String inMan;
	/**
	 * 当前任务
	 */
	private String task;
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getFlowTitle() {
		return flowTitle;
	}
	public void setFlowTitle(String flowTitle) {
		this.flowTitle = flowTitle;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getInMan() {
		return inMan;
	}
	public void setInMan(String inMan) {
		this.inMan = inMan;
	}
	public String getTask() {
		return task;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public String getFunId() {
		return funId;
	}
	public void setFunId(String funId) {
		this.funId = funId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
