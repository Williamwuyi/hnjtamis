package cn.com.ite.workflow.domain;


/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlowTo</p>
 * <p>Description 结点导向</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午02:01:28
 * @version 2.0
 * 
 * @modified records:
 */
@SuppressWarnings("serial")
public class WorkFlowTo implements java.io.Serializable {

	private String toId;
	private WorkFlowNode srcNode;
	private WorkFlowNode toNode;
	private String code;
	private String name;
	private String state;
	private Integer orderNo;
	//方便维护操作的字段
	private String srcNodeId;
	private String toNodeId;

	// Constructors

	/** default constructor */
	public WorkFlowTo() {
	}

	public String getToId() {
		return this.toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}


	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public WorkFlowNode getSrcNode() {
		return srcNode;
	}

	public void setSrcNode(WorkFlowNode srcNode) {
		this.srcNode = srcNode;
	}

	public WorkFlowNode getToNode() {
		return toNode;
	}

	public void setToNode(WorkFlowNode toNode) {
		this.toNode = toNode;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getSrcNodeId() {
		return srcNodeId;
	}

	public void setSrcNodeId(String srcNodeId) {
		this.srcNodeId = srcNodeId;
	}

	public String getToNodeId() {
		return toNodeId;
	}

	public void setToNodeId(String toNodeId) {
		this.toNodeId = toNodeId;
	}

}