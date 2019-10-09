package cn.com.ite.workflow.domain;

/**
 * <p>Title cn.com.ite.workflow.domain.WorkFlowQuarter</p>
 * <p>Description 工作流执行岗位</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-3-24 下午02:03:52
 * @version 2.0
 * 
 * @modified records:
 */
public class WorkFlowQuarter implements java.io.Serializable {
	private static final long serialVersionUID = -1881708840836345948L;
	private String id;
	private String quarterId;
	private String quarterName;

	// Constructors

	/** default constructor */
	public WorkFlowQuarter() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

}