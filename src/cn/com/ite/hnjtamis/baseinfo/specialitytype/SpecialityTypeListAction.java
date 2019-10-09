package cn.com.ite.hnjtamis.baseinfo.specialitytype;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
import cn.com.ite.hnjtamis.baseinfo.domain.SpecialityType;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class SpecialityTypeListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<SpecialityType> subSpecialitytypes = new ArrayList<SpecialityType>();
	/**
	 * TreeNode类型树形数据
	 */
	private List<TreeNode> children;
	// 查询条件
	private String toptypeidTerm;
	private String typenameTerm="";  //
	private String nameTerm="";// 表单查询时条件固定为nameTerm
	private String validStr;
	private String showAllSpecialtypeTerm="0"; // 显示已逻辑删除数据 1：是 0：否
	private String restoreSpecialtypeTerm="0"; // 还原已逻辑删除的数据
	
	public String getShowAllSpecialtypeTerm() {
		return showAllSpecialtypeTerm;
	}
	public void setShowAllSpecialtypeTerm(String showAllSpecialtypeTerm) {
		this.showAllSpecialtypeTerm = showAllSpecialtypeTerm;
	}
	public String getRestoreSpecialtypeTerm() {
		return restoreSpecialtypeTerm;
	}
	public void setRestoreSpecialtypeTerm(String restoreSpecialtypeTerm) {
		this.restoreSpecialtypeTerm = restoreSpecialtypeTerm;
	}
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String list()throws Exception{
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		List<SpecialityType> querys = (List<SpecialityType>)service.queryData("queryHql", this,null);
		//把线性结构转成树形结构
		subSpecialitytypes = service.childObjectHandler(querys, "bstid", "parentspeciltype", 
				"subSpecialitytypes",new String[]{},null,this.getFilterIds(),"orderno",null);
		//this.setTotal(service.countData("queryHql", this));
		handleSpecialityType(subSpecialitytypes);
		return "list";
	}
	
	public void handleSpecialityType(List<SpecialityType> specialityTypes){
		for (SpecialityType rt : (List<SpecialityType>) specialityTypes) {
			handleSpecialityType(rt.getSubSpecialitytypes());
			
			rt.setBaseSpecialities(new ArrayList<Speciality>());
		}
	}
	
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	public String subList()throws Exception{
		subSpecialitytypes = (List<SpecialityType>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids =this.getId().split(",");
		boolean b = false;
		for(int i=0;i<ids.length;i++){
			SpecialityType vo = (SpecialityType)service.findDataByKey(ids[i], SpecialityType.class);
			
//			if((vo.getBaseSpecialities()==null || vo.getBaseSpecialities().size()==0)
//					&& (vo.getSubSpecialitytypes()==null || vo.getSubSpecialitytypes().size()==0)){
				if("1".equals(restoreSpecialtypeTerm)){
					if(vo.getStatus()==DicDefine.DATA_ADD || vo.getStatus()==DicDefine.DATA_UPDATE) continue;
				}else{
					if(vo.getStatus()==DicDefine.DATA_DELETE) continue;
				}
				
				
				logicdeleteSpecialType(vo.getSubSpecialitytypes());// 递归删除下级
				
				AbstractDomain.updateCommonFieldValue(vo);
				if("1".equals(restoreSpecialtypeTerm)){
					restoreSpecialtypeTerm="0";
					vo.setStatus(DicDefine.DATA_UPDATE);
					vo.setIsavailable(DicDefine.YES_VALIDATE);
					vo.setTypename(vo.getTypename().replaceAll(DicDefine.DEL_SUFFIX, ""));
				}else{
					vo.setStatus(DicDefine.DATA_DELETE);
					vo.setIsavailable(DicDefine.NOT_VALIDATE);
					if(vo.getTypename().indexOf(DicDefine.DEL_SUFFIX)==-1)
						vo.setTypename(vo.getTypename()+DicDefine.DEL_SUFFIX);
				}
				
				service.saveOld(vo);
				
				
//			}else{
//				//
//				b = true;
//			}
		}
//		if(ids.length==1 && b)
//			this.setMsg("有已引用记录不能删除");
//		else
		//service.deleteByKeys(this.getId().split(","), SpecialityType.class);
			this.setMsg("操作成功！"+(b?"但有已引用记录不能删除":""));
		return "delete";
	}
	
	private void logicdeleteSpecialType(List<SpecialityType> list) throws EapException{
		if(list==null) return;
		for(SpecialityType vo:list){
			logicdeleteSpecialType(vo.getSubSpecialitytypes()); // 递归
			if(vo.getStatus()==DicDefine.DATA_ADD) continue;
			AbstractDomain.updateCommonFieldValue(vo);
			if("1".equals(restoreSpecialtypeTerm)){
				restoreSpecialtypeTerm="0";
				vo.setStatus(DicDefine.DATA_UPDATE);
				vo.setIsavailable(DicDefine.YES_VALIDATE);
				vo.setTypename(vo.getTypename().replaceAll(DicDefine.DEL_SUFFIX, ""));
			}else{
				vo.setStatus(DicDefine.DATA_DELETE);
				vo.setIsavailable(DicDefine.NOT_VALIDATE);
				if(vo.getTypename().indexOf(DicDefine.DEL_SUFFIX)==-1)
					vo.setTypename(vo.getTypename()+DicDefine.DEL_SUFFIX);
			}
			
			service.save(vo);
		}
	}
	
	public String specialtree() throws Exception {
		SpecialityTypeService ds = (SpecialityTypeService)service;
		children = (List<TreeNode>)ds.findSpecialTypeTree(toptypeidTerm, typenameTerm);
		return "specialtree";
	}
	
	public List<SpecialityType> getSubSpecialitytypes() {
		return subSpecialitytypes;
	}
	public void setSubSpecialitytypes(List<SpecialityType> list) {
		this.subSpecialitytypes = list;
	}
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public String getTypenameTerm() {
		//setTypenameTerm("3333");
		return typenameTerm;
	}
	public void setTypenameTerm(String typenameTerm) {
		this.typenameTerm = typenameTerm;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
}
