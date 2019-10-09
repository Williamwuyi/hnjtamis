package cn.com.ite.eap2.module.power.role;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.domain.funres.ModuleResource;
import cn.com.ite.eap2.domain.power.RoleType;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.power.role.RoleListAction</p>
 * <p>Description 角色ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-8 上午09:32:47
 * @version 2.0
 * 
 * @modified records:
 */
public class RoleListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753779816555403L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 编码条件 
	 */
	private String codeTerm;
	
	private String rt;
	
	/**
	 * 查询结果
	 */
	private List<RoleType> roleTypes = new ArrayList<RoleType>();
	
	@SuppressWarnings("unchecked")
	public String query()throws Exception{
		List<RoleType> querys = (List<RoleType>)service.queryData("queryHql", this,this.getSortMap(),
				0, 0);
	    //把线性结构转成树形结构
		roleTypes = service.childObjectHandler(querys, "rtId", "roleType", 
				"roleTypes",new String[]{"sysRoles"},null,this.getFilterIds(),"sortNo",null);
		return "list";
	}
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		List querys = (List<RoleType>)service.queryData("queryHql", this,this.getSortMap(),
				0, 0);
	    //把线性结构转成树形结构
		roleTypes = service.childObjectHandler(querys, "rtId", "roleType", 
				"roleTypes",new String[]{},null,this.getFilterIds(),"sortNo",null);
		this.handleRole(roleTypes);
		return "list";
	}
	/**
	 * 把角色另到角色类型树中一起显示
	 * @param roleTypes
	 * @throws Exception
	 */
	private void handleRole(List roleTypes) throws Exception{
		for(RoleType rt:(List<RoleType>)roleTypes){
		    this.handleRole(rt.getRoleTypes());
			if(rt.getSysRoles()!=null)
			for(SysRole role:(List<SysRole>)rt.getSysRoles()){
				RoleType type = new RoleType();
				type.setRtId(rt.getRtId()+"_"+role.getRoleId());
				type.setRoleTypename(role.getRoleName());
				type.setRoleTypeCode(role.getRoleCode());
				type.setDesc(role.getDescription());
				String resourceName=null;
				for(ModuleResource mr:(Set<ModuleResource>)role.getRoleResources()){
					if(resourceName==null)
						resourceName = mr.getResourceName();
					else
						resourceName += ","+mr.getResourceName();
				}
				type.setResourceName(resourceName);
				rt.getRoleTypes().add(type);
			}
			rt.setSysRoles(new ArrayList());
		}
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		roleTypes = (List<RoleType>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	private String userId;
	private String allotUserid;
	private List<TreeNode> children;
	private String quarterId;
	public void setQuarterId(String quarterId) {
		this.quarterId = quarterId;
	}
	/**
	 * 角色树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String tree()throws Exception{
		RoleService roleService = (RoleService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		try{
			if("organ".equals(rt)){
				children = (List<TreeNode>)roleService.findMRTreeFilteByOran(us.getUserId(), allotUserid,null,quarterId,us.getOrganId());
			}else{
				children = (List<TreeNode>)roleService.findMRTree(us.getUserId(), allotUserid,null,quarterId);
			}
			
			if(!StringUtils.isEmpty(nameTerm)){//条件过滤
				TreeNode.select(children, nameTerm);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "tree";
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAllotUserid() {
		return allotUserid;
	}
	public void setAllotUserid(String allotUserid) {
		this.allotUserid = allotUserid;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids = this.getId().split("_");
		if(ids.length==1)
		   service.deleteByKey(ids[0], RoleType.class);
		else
		   service.deleteByKey(ids[1], SysRole.class);
		this.setMsg("字典类型及数据删除成功！");
		return "delete";
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public String getCodeTerm() {
		return codeTerm;
	}
	public void setCodeTerm(String codeTerm) {
		this.codeTerm = codeTerm;
	}
	public List<RoleType> getRoleTypes() {
		return roleTypes;
	}
	public void setRoleTypes(List<RoleType> roleTypes) {
		this.roleTypes = roleTypes;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	
}