package cn.com.ite.hnjtamis.baseinfo.speciality;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityStandardTypes;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionSpeciality;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;

/**
 * <p>
 * Title 岗位达标培训信息系统-基础信息模块
 * </p>
 * <p>
 * Description 专业类型ListAction
 * </p>
 * <p>
 * Company ITE
 * </p>
 * <p>
 * Copyright Copyright(c)2015
 * </p>
 * 
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class SpecialityListAction extends AbstractListAction implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	
	private HttpServletRequest request;
	
	//private List<Speciality> list = new ArrayList<Speciality>();
	private List<SpecialityType> subSpecialitytypes = new ArrayList<SpecialityType>();

	// 查询条件
	private String toptypeidTerm;
	private String nameTerm = ""; // 专业名称
	private String typenameTerm=""; // 专业类型名称
	private String validStr;
	private List<TreeNode> children;
	private String jobidTerm = ""; // 岗位ID
	
	/**
	 * 导入文件
	 */
	private File xls;

	public String getValidStr() {
		return validStr;
	}

	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}

	/*
	 * 导入
	 */
	public String importXls() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		SpecialityService specialityService=(SpecialityService)getService();
		String msg = specialityService.importSpeciality(xls,usersess);
		this.setMsg(msg);
		return "save";
	}
	
	public String syncJobsUnionSpeciality(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		try {
			SpecialityService specialityService=(SpecialityService)getService();
			specialityService.saveSyncJobsUnionSpeciality();
			this.setMsg("专业与岗位关联关系同步成功！");
		} catch (Exception e) {
			this.setMsg("专业与岗位关联关系同步失败！");
			e.printStackTrace();
		}
		return "delete";
	}
	
	public String expPublicSp(){
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		SpecialityService specialityService=(SpecialityService)getService();
		for(int i = 0;i<ids.length ;i++){
			Speciality  speciality = (Speciality)service.findDataByKey(ids[i], Theme.class);
			/*try{
				if(theme.getTestpaperThemes()!=null && theme.getTestpaperThemes().size() > 0){
					
				}else if(theme.getState()==1){
					theme.setState(15);
					service.saveOld(theme);
					succ++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}*/
			sum++;
		}
		//service.deleteByKeys(ids, Theme.class);
		if(sum==succ){
			this.setMsg("选中试题"+sum+"个,成功发布"+succ+"个！");
		}else{
			this.setMsg("选中试题"+sum+"个,成功发布"+succ+"个,请确定导入成功的试题才能发布！");
		}
		return "delete";
	}
	
	@SuppressWarnings("unchecked")
	public String list() throws Exception {
		try {
			// if(!StringUtils.isEmpty(this.getValidStr()))
			// this.setValid(this.getValidStr().equals("1"));
			List<SpecialityType> querys = (List<SpecialityType>) service
					.queryData("queryHql", this, this.getSortMap(), 0, 0);

			// List<Speciality> query =
			// (List<Speciality>)service.queryData("queryHql",
			// this,null,this.getStart(),this.getLimit());
			// list = (List<Speciality>)service.queryData("queryHql",
			// this,null,this.getStart(),this.getLimit());
			// 把线性结构转成树形结构
			subSpecialitytypes = service.childObjectHandler(querys, "bstid",
					"parentspeciltype", "subSpecialitytypes", new String[] {},
					null, this.getFilterIds(), "orderno", null);

			// this.setTotal(service.countData("queryHql", this));
			this.handleSpeciality(subSpecialitytypes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}

	private void handleSpeciality(List<SpecialityType> specialityTypes) {
		SpecialityService pservice=(SpecialityService)getService();
		try {
			for (SpecialityType rt : (List<SpecialityType>) specialityTypes) {
				/// if (rt == null) continue;
				handleSpeciality(rt.getSubSpecialitytypes());
				//if (rt.getSubSpecialitytypes() == null || rt.getSubSpecialitytypes().size() == 0) {
				//	rt.setSubSpecialitytypes(new ArrayList<SpecialityType>());
					 
					List<Speciality> specialities = rt.getBaseSpecialities();// 专业
					//List<Speciality> specialities = pservice.findSpecialitiesByTypeId(rt.getBstid(), nameTerm);
					if (specialities != null) {
						for (Speciality sp : specialities) {
							if (sp.getStatus() == DicDefine.DATA_DELETE) {
								continue;
							}
							SpecialityType type = new SpecialityType();
							type.setBstid(rt.getBstid() + "_"
									+ sp.getSpecialityid());
							type.setTypename(sp.getSpecialityname()); // 名称//
																		// 替换为子专业名称
							type.setTypecode(sp.getSpecialitycode());
							type.setRemarks(sp.getRemarks());
							type.setCreatedBy(sp.getCreatedBy());
							type.setCreationDate(sp.getCreationDate());
							type.setIsavailable(sp.getIsavailable());
							type.setLastUpdateDate(sp.getLastUpdateDate());
							type.setLastUpdatedBy(sp.getLastUpdatedBy());
							// type.setSubSpecialitytypes(null);
							// type.setParentspeciltype(null);
							// type.setBaseSpecialities(null);
							type.setSubSpecialitytypes(new ArrayList<SpecialityType>());
							rt.getSubSpecialitytypes().add(type);
							// rt.setBaseSpecialities(new
							// ArrayList<Speciality>());
						}
					}
					rt.setBaseSpecialities(new ArrayList<Speciality>());
//				} else {
//					////this.handleSpeciality(rt.getSubSpecialitytypes());
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 子查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String subList() throws Exception {
		subSpecialitytypes = (List<SpecialityType>) service.queryData(
				"querySubHql", this, null);
		return "subList";
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @modified
	 */
	public String delete() throws Exception {
		// 
		String[] ids = this.getId().split("_");
		if (ids.length == 1)
			service.deleteByKey(ids[0], SpecialityType.class);
		else {
			Speciality vo = (Speciality) service.findDataByKey(ids[1],
					Speciality.class);
			AbstractDomain.updateCommonFieldValue(vo);
			vo.setStatus(DicDefine.DATA_DELETE);
			vo.setIsavailable(DicDefine.NOT_VALIDATE);
			vo.setSpecialityname(vo.getSpecialityname() + DicDefine.DEL_SUFFIX);
			service.save(vo);
		}
		// service.deleteByKey(ids[1], Speciality.class);

		// service.deleteByKeys(this.getId().split(","), Speciality.class);
		this.setMsg("专业删除成功！");
		return "delete";
	}

	public String tree() throws Exception {
		SpecialityService stService = (SpecialityService) service;
		// UserSession us = LoginAction.getUserSessionInfo();
		try {
			children = (List<TreeNode>) stService.findSpecialitiesTree(
					jobidTerm, "");
			if (!StringUtils.isEmpty(nameTerm)) {// 条件过滤
				TreeNode.select(children, nameTerm);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return "tree";
	}

//	public List<Speciality> getList() {
//		return list;
//	}
//
//	public void setList(List<Speciality> list) {
//		this.list = list;
//	}

	public String getToptypeidTerm() {
		return toptypeidTerm;
	}

	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}

	public String getNameTerm() {
		// setTypenameTerm("3333");
		return nameTerm;
	}

	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getJobidTerm() {
		return jobidTerm;
	}

	public void setJobidTerm(String jobidTerm) {
		this.jobidTerm = jobidTerm;
	}

	public List<SpecialityType> getSubSpecialitytypes() {
		return subSpecialitytypes;
	}

	public void setSubSpecialitytypes(List<SpecialityType> subSpecialitytypes) {
		this.subSpecialitytypes = subSpecialitytypes;
	}

	public String getTypenameTerm() {
		return typenameTerm;
	}

	public void setTypenameTerm(String typenameTerm) {
		this.typenameTerm = typenameTerm;
	}
	
	public File getXls() {
		return xls;
	}


	public void setXls(File xls) {
		this.xls = xls;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}

}
