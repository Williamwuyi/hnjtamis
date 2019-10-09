package cn.com.ite.hnjtamis.kb.sharedlib;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.kb.domain.SharedLib;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.sharedlib.SharedLibListAction
 * </p>
 * <p>
 * Description 共享库ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-30
 * @version 1.0
 * 
 * @modified
 */
public class SharedLibListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2775485371067210785L;
	private String folderTerm;
	private String createUserId;
	/**
	 * 查询结果对象
	 */
	private List<SharedLib> list = new ArrayList<SharedLib>();

	/**
	 * 查询方法
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<SharedLib>) service.queryData("queryHql", this,
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
		service.deleteByKeys(this.getId().split(","), SharedLib.class);
		this.setMsg("删除成功！");
		return "delete";
	}

	public List<SharedLib> getList() {
		return list;
	}

	public void setList(List<SharedLib> list) {
		this.list = list;
	}

	public String getFolderTerm() {
		return folderTerm;
	}

	public void setFolderTerm(String folderTerm) {
		this.folderTerm = folderTerm;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}
}
