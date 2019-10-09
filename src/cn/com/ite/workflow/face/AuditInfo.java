package cn.com.ite.workflow.face;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

/**
 * <p>Title cn.com.ite.workflow.face.AuditInfo</p>
 * <p>Description 审核信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午02:46:19
 * @version 2.0
 * 
 * @modified records:
 */
public class AuditInfo implements java.io.Serializable{
	private Date createTime;
	private Date time;
	private String employeeName;
	private String taskName;
	private String to;
	private String advice;
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}
	@JSON(format="yyyy-MM-dd HH:mm")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
