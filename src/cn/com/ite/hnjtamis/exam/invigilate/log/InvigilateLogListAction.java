package cn.com.ite.hnjtamis.exam.invigilate.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.exam.hibernatemap.Exam;
import cn.com.ite.hnjtamis.exam.invigilate.domain.InvigilateLog;

public class InvigilateLogListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6623499750682273449L;

	private List<InvigilateLog> list = new ArrayList<InvigilateLog>();
	private List<Exam> examList = new ArrayList<Exam>();

	private String examIdTerm;
	private String invigilaterId;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<InvigilateLog>) service.queryData("queryHql", this, this
				.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}

	/**
	 * 查询考试科目列表
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String examList() throws Exception {
		examList = (List<Exam>) service.queryData("queryExamHql", this, this
				.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(service.countData("queryExamHql", this));
		return "examList";
	}
	
	/**
	 * 获取服务端系统时间
	 * @return
	 * @throws Exception
	 */
	public String currentTime() throws Exception {
		this.setMsg(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
		return "save";
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
			InvigilateLog log = (InvigilateLog) service.findDataByKey(ids[i],
					InvigilateLog.class);
			service.delete(log);
		}
		return "delete";
	}

	public List<InvigilateLog> getList() {
		return list;
	}

	public void setList(List<InvigilateLog> list) {
		this.list = list;
	}

	public String getExamIdTerm() {
		return examIdTerm;
	}

	public void setExamIdTerm(String examIdTerm) {
		this.examIdTerm = examIdTerm;
	}

	public String getInvigilaterId() {
		return invigilaterId;
	}

	public void setInvigilaterId(String invigilaterId) {
		this.invigilaterId = invigilaterId;
	}

	public List<Exam> getExamList() {
		return examList;
	}

	public void setExamList(List<Exam> examList) {
		this.examList = examList;
	}

}
