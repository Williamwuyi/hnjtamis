package cn.com.ite.hnjtamis.param;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.param.domain.SystemParams;

public class SystemParamsListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2397876510826068151L;

	private List<SystemParams> list = new ArrayList<SystemParams>();
	private String sortTerm;
	
	/** 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<SystemParams>) service.queryData("queryHql", this,
				this.getSortMap(), this.getStart(), this.getLimit());
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
			service.deleteByKey(ids[i], SystemParams.class);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	public List<SystemParams> getList() {
		return list;
	}

	public void setList(List<SystemParams> list) {
		this.list = list;
	}

	public String getSortTerm() {
		return sortTerm;
	}

	public void setSortTerm(String sortTerm) {
		this.sortTerm = sortTerm;
	}
}
