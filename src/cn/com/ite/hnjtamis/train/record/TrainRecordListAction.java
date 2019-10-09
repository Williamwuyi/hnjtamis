package cn.com.ite.hnjtamis.train.record;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.train.domain.TrainOnlineRecord;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.train.record.TrainRecordListAction
 * </p>
 * <p>
 * Description 在线培训记录ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-4-8
 * @version 1.0
 * 
 * @modified
 */
public class TrainRecordListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1056297382813030968L;
	/**
	 * 查询对象
	 */
	private List<TrainOnlineRecord> list = new ArrayList<TrainOnlineRecord>();

	private String trainIdTerm;// 培训ID查询条件
	private String studentId;//学员ID查询条件

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		try {
			list = (List<TrainOnlineRecord>) service.queryData("queryHql",
					this, this.getSortMap(), this.getStart(), this.getLimit());
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		String[] ids = this.getId().split(",");
		for (int i = 0; i < ids.length; i++) {
			TrainOnlineRecord online = (TrainOnlineRecord) service
					.findDataByKey(ids[i], TrainOnlineRecord.class);
			online.setIsDel(1);
			online.setSyncStatus(3);
			service.saveOld(online);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	public List<TrainOnlineRecord> getList() {
		return list;
	}

	public void setList(List<TrainOnlineRecord> list) {
		this.list = list;
	}

	public String getTrainIdTerm() {
		return trainIdTerm;
	}

	public void setTrainIdTerm(String trainIdTerm) {
		this.trainIdTerm = trainIdTerm;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}
