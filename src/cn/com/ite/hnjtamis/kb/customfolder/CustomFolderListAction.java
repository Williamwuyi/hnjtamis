package cn.com.ite.hnjtamis.kb.customfolder;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.kb.domain.CustomFolder;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.customfolde.CustomFolderListAction
 * </p>
 * <p>
 * Description 自定义文件夹ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time: 2015-3-30
 * @version 1.0
 * 
 * @modified records:
 */
public class CustomFolderListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -31781475099917832L;

	private String createUserId;

	/**
	 * 查询结果对象
	 */
	private List<CustomFolder> childFolders = new ArrayList<CustomFolder>();

	/**
	 * 树对象
	 */
	private List<TreeNode> children;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception{
		List<CustomFolder> querys = (List<CustomFolder>) service.queryData(
				"queryHql", this, this.getSortMap(), this.getStart(),
				this.getLimit());
		// this.setTotal(service.countData("queryHql", this));
		// 把线性结构转成树形结构
		try {
			childFolders = service.childObjectHandler(querys, "id", "parentFolder",
				"childFolders", new String[]{}, null, this.getFilterIds(), "name", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			CustomFolder folder = (CustomFolder) service.findDataByKey(ids[i],
					CustomFolder.class);
			folder.setIsDel(1);
			folder.setSyncStatus(3);
			service.saveOld(folder);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	public String findFolderTree() throws Exception {
		children = ((CustomFolderService) service).findFolderTree(createUserId);
		return "findFolderTree";
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public List<CustomFolder> getChildFolders() {
		return childFolders;
	}

	public void setChildFolders(List<CustomFolder> childFolders) {
		this.childFolders = childFolders;
	}
}
