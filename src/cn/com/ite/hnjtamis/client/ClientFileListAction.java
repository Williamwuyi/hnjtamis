package cn.com.ite.hnjtamis.client;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.client.domain.ClientFile;

public class ClientFileListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3641730679861639201L;
	
	private List<ClientFile> list = new ArrayList<ClientFile>();
	
	/** 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<ClientFile>) service.queryData("queryHql", this,
				this.getSortMap(), this.getStart(), this.getLimit());
		this.setTotal(list.size());
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
			service.deleteByKey(ids[i], ClientFile.class);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	public List<ClientFile> getList() {
		return list;
	}

	public void setList(List<ClientFile> list) {
		this.list = list;
	}

}
