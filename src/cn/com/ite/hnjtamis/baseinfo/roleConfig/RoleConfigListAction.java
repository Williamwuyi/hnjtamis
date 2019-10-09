package cn.com.ite.hnjtamis.baseinfo.roleConfig;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.RoleAssobject;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamRoot;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.roleConfig.RoleConfigListAction</p>
 * <p>Description 角色配置管理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月18日 上午9:22:20
 * @version 1.0
 * 
 * @modified records:
 */
public class RoleConfigListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = 286669717268101784L;
	
	private HttpServletRequest request;
	
	private List<RoleAssobject>  list = new ArrayList<RoleAssobject>();
	
	private List<TreeNode> children;
	
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	/*
	 * 列表查询
	 */
	public String list() throws Exception{
		try {
			list = service.queryData("queryHql", this, null, ExamRoot.class, this.getStart(), this.getLimit());
			this.setTotal(service.countData("queryHql", this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	
	
	/**
	 * 角色树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String tree()throws Exception{
		RoleConfigService roleConfigService = (RoleConfigService)service;
		try{
			children = (List<TreeNode>)roleConfigService.findRoleConfigTree(this.getId());
			if(!StringUtils.isEmpty(nameTerm)){//条件过滤
				TreeNode.select(children, nameTerm);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "tree";
	}
	
	
	

	public List<RoleAssobject> getList() {
		return list;
	}

	public void setList(List<RoleAssobject> list) {
		this.list = list;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getNameTerm() {
		return nameTerm;
	}

	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	
	

}
