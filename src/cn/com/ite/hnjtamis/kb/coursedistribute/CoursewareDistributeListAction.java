package cn.com.ite.hnjtamis.kb.coursedistribute;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.hnjtamis.kb.domain.CoursewareDistribute;

/**
 * <p>
 * Title cn.com.ite.hnjtamis.kb.coursedistribute.CoursewareDistributeListAction
 * </p>
 * <p>
 * Description 课件库分配ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2014
 * </p>
 * 
 * @author 李奉学
 * @create time 2015-3-24
 * @version 1.0
 * 
 * @modified
 */
public class CoursewareDistributeListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6086162645719337744L;

	private String organId;// 机构ID
	private String titleTerm;// 标题查询条件
	private Integer typeTerm;// 类型：课件或教材
	/**
	 * 查询结果对象
	 */
	private List<CoursewareDistribute> list = new ArrayList<CoursewareDistribute>();
	/**
	 * 树对象
	 */
	private List<TreeNode> children;
	private String selectIds;

	/**
	 * 查询方法
	 * 
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		list = (List<CoursewareDistribute>) service.queryData("queryHql", this,
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
			CoursewareDistribute courseDistribute = (CoursewareDistribute) service
					.findDataByKey(ids[i], CoursewareDistribute.class);
			courseDistribute.setIsDel(1);
			courseDistribute.setSyncStatus(3);
			service.saveOld(courseDistribute);
		}
		this.setMsg("删除成功！");
		return "delete";
	}

	/**
	 * 审核
	 * @return
	 * @throws Exception
	 */
	public String audit() throws Exception {
		CoursewareDistribute courseDistribute = (CoursewareDistribute) service
				.findDataByKey(this.getId(), CoursewareDistribute.class);
		courseDistribute.setIsAudited(1);
		courseDistribute.setAuditTime(DateUtils.convertDateToStr(new Date(),
				"yyyy-MM-dd HH:mm:ss"));
		courseDistribute.setAuditer(LoginAction.getUserSessionInfo()
				.getEmployeeName());
		service.saveOld(courseDistribute);
		this.setMsg("审核成功！");
		return "save";
	}
	
	/**
	 * 课件选择树
	 * @return
	 * @throws Exception
	 */
	public String findDistributeTree() throws Exception {
		children = ((CoursewareDistributeService)service).findDistributeTree(organId, selectIds);
		return "findDistributeTree";
	}

	public List<CoursewareDistribute> getList() {
		return list;
	}

	public void setList(List<CoursewareDistribute> list) {
		this.list = list;
	}

	public String getTitleTerm() {
		return titleTerm;
	}

	public void setTitleTerm(String titleTerm) {
		this.titleTerm = titleTerm;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public Integer getTypeTerm() {
		return typeTerm;
	}

	public void setTypeTerm(Integer typeTerm) {
		this.typeTerm = typeTerm;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}
}
