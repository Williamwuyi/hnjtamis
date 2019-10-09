package cn.com.ite.eap2.module.power.role;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.struts2.AbstractFormAction;
import cn.com.ite.eap2.domain.power.RoleType;
import cn.com.ite.eap2.domain.power.SysRole;
import cn.com.ite.eap2.domain.power.SysUser;
import cn.com.ite.eap2.module.power.login.LoginAction;
import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>Title cn.com.ite.eap2.module.power.roletype.RoleFormAction</p>
 * <p>Description 角色FormAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-8 上午09:14:24
 * @version 2.0
 * 
 * @modified records:
 */
public class RoleFormAction extends AbstractFormAction{
	private static final long serialVersionUID = 6061990327425264872L;
	/**
	 * 友情链结
	 */
	private RoleType form;
	/**
	 * 角色类型编码
	 */
	private String typeCode;
	/**
	 * 角色列表
	 */
	private List<SysRole> datas = null;
	public List<SysRole> getDatas() {
		return datas;
	}
	public void setDatas(List<SysRole> datas) {
		this.datas = datas;
	}
	/**
	 * 查询数据
	 * @return
	 * @modified
	 */
	public String find(){
		form = (RoleType)service.findDataByKey(this.getId(), RoleType.class);
		return "find";
	}
	/**
	 * 根据类型编码查询字典数据
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String findData(){
		datas = (List<SysRole>)service.queryData("findDataSql", this,null,SysRole.class);
		return "findData";
	}
	/**
	 * 保存主对象
	 * @return
	 * @throws Exception
	 */
	public String save() throws Exception{
		try{
			form = (RoleType)this.jsonToObject(RoleType.class);
			int i = 1;
			SysUser user = null;
			for(SysRole dic:form.getSysRoles()){
				dic.setRoleType(form);
				dic.setOrderNo(i);i++;
			}
			if(StringUtils.isEmpty(form.getRtId()))
				form.setSortNo(1+service.getFieldMax(RoleType.class, 
						"sortNo","roleType.rtId",
						form.getRoleType()==null?null:form.getRoleType().getRtId()));
			service.save(form);
			//当前编辑的用户自动有此角色的权限
			UserSession us = LoginAction.getUserSessionInfo();
			user = (SysUser)service.findDataByKey(us.getUserId(),SysUser.class);
			if(user!=null){
				form = (RoleType)service.findDataByKey(form.getRtId(), RoleType.class);
				for(SysRole dic:form.getSysRoles()){
					if(!user.getUserId().equals("admin"))
						user.getUserRoles().add(dic);
				}
				service.saveOld(user);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "save";
	}
	/**
	 * 排序保存
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String saveSort() throws Exception{
		List saves = new ArrayList();
		int index = 1;
		for(String id:this.getSortIds()){
			RoleType dt = (RoleType)service.findDataByKey(id, RoleType.class);
			dt.setSortNo(index++);
			saves.add(dt);
		}
		service.saves(saves);
		return "save";
	}
	public RoleType getForm() {
		return form;
	}

	public void setForm(RoleType form) {
		this.form = form;
	}
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
}