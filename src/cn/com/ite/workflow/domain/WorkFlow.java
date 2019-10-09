package cn.com.ite.workflow.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlow</p>
 * <p>Description 工作流定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午01:55:20
 * @version 2.0
 * 
 * @modified records:
 */
@SuppressWarnings("serial")
public class WorkFlow implements java.io.Serializable {
	private String flowId;
	private String name;
	private String code;
	private Integer version;
	private String that;
	private String pic;
	private String serviceName;
	private Set<WorkFlowNode> workFlowNodes = new HashSet<WorkFlowNode>(0);
	//把流程中所有导向单独进行维护
	private List<WorkFlowTo> tos = new ArrayList<WorkFlowTo>(0);

	// Constructors

	/** default constructor */
	public WorkFlow() {
	}


	public String getFlowId() {
		return this.flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getThat() {
		return this.that;
	}

	public void setThat(String that) {
		this.that = that;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public Set<WorkFlowNode> getWorkFlowNodes() {
		return this.workFlowNodes;
	}

	public void setWorkFlowNodes(Set<WorkFlowNode> workFlowNodes) {
		this.workFlowNodes = workFlowNodes;
	}

	public List<WorkFlowTo> getTos() {
		return tos;
	}

	public void setTos(List<WorkFlowTo> tos) {
		this.tos = tos;
	}
}