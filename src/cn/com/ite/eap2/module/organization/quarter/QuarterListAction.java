package cn.com.ite.eap2.module.organization.quarter;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicUser;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 * <p>Title cn.com.ite.eap2.module.organization.quarter.QuarterListAction</p>
 * <p>Description 岗位ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-7 上午08:52:08
 * @version 2.0
 * 
 * @modified records:
 */
public class QuarterListAction extends AbstractListAction{
	private static final long serialVersionUID = -7244753779816555403L;
	/**
	 * 名称查询条件
	 */
	private String nameTerm;
	/**
	 * 编码条件 
	 */
	private String codeTerm;
	/**
	 * 类型条件
	 */
	private String typeTerm;
	/**
	 * 是否有效条件
	 */
	private Boolean valid;
	/**
	 * 部门条件
	 */
	private String deptTerm;
	private String organTerm;
	/**
	 * 查询结果
	 */
	private List<Quarter> quarters = new ArrayList<Quarter>();
	/**
	 * TreeNode类型树形数据
	 */
	private List<TreeNode> children;
	public String getDeptTerm() {
		return deptTerm;
	}
	public void setDeptTerm(String deptTerm) {
		this.deptTerm = deptTerm;
	}
	
	private List quarterStandardList;
	
	public List getQuarterStandardList() {
		return quarterStandardList;
	}
	public void setQuarterStandardList(List quarterStandardList) {
		this.quarterStandardList = quarterStandardList;
	}
	/**
	 * 列表查询方法
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public String list()throws Exception{
		if(this.getValid()==null) this.setValid(true);
		if(!service.existDate(this.getDeptTerm(), Dept.class)){
		   this.setOrganTerm(this.getDeptTerm());
		   this.setDeptTerm(null);
		}
		List querys = (List<Quarter>)service.queryData("queryHql", this,null);
		Map<String[],Class> copyFieldMap = new HashMap<String[],Class>();
		copyFieldMap.put(new String[]{"dept","deptId","deptName"}, Dept.class);
	    //把线性结构转成树形结构
		quarters = service.childObjectHandler(querys, "quarterId", "quarter", 
				"quarters",new String[]{"quarterRoles","employees"},copyFieldMap,this.getFilterIds(),"orderNo",null);
		return "list";
	}
	/**
	 * 机构部门岗位树
	 * @return
	 * @throws Exception
	 * @modified
	 */
	public String odqtree() throws Exception{
		QuarterService qs = (QuarterService)service;
		children = (List<TreeNode>)qs.findOrganDeptQuarterTree(organTerm, nameTerm);
		return "odqtree";
	}
	/**
	 * 子查询
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String subList()throws Exception{
		if(!service.existDate(this.getDeptTerm(), Dept.class)){
		   this.setOrganTerm(this.getDeptTerm());
		   this.setDeptTerm(null);
		}
		quarters = (List<Quarter>)service.queryData("querySubHql", this,null);
		return "subList";
	}
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		service.deleteByKeys(this.getId().split(","), Quarter.class);
		this.setMsg("岗位删除成功！");
		return "delete";
	}
	
	
	public String getQuarterStandard(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "quarterStandardList";
	 	}
		quarterStandardList = new ArrayList();
		Map termMap = new HashMap();
		termMap.put("organId", usersess.getCurrentOrganId());
		List<QuarterStandard> list = service.queryData("quarterStandardHql", termMap, null);
		Map quarterStandardMap = new HashMap();
		Map quarterCodeMap = new HashMap();
		for(int i=0;i<list.size();i++){
			QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			if(quarterCodeMap.get(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode())==null){
				TreeNode parentNode = (TreeNode)quarterStandardMap.get(quarterStandard.getDeptName());
				if(parentNode == null){
					parentNode = new TreeNode();
					parentNode.setId(quarterStandard.getDeptName());
					parentNode.setTitle(quarterStandard.getDeptName());
					parentNode.setType("dept");
					parentNode.setChildren(new ArrayList());
					
					quarterStandardList.add(parentNode);
					quarterStandardMap.put(parentNode.getId(), parentNode);
				}
				
				TreeNode childeNode = new TreeNode();
				//childeNode.setId(quarterStandard.getQuarterCode());
				//childeNode.setTitle(quarterStandard.getQuarterName());
				childeNode.setId(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode());
				if(quarterStandard.getSpecialityName()!=null && !"".equals(quarterStandard.getSpecialityName())
						&& !"null".equals(quarterStandard.getSpecialityName())){
					childeNode.setTitle(quarterStandard.getQuarterName()+"("+quarterStandard.getSpecialityName()+")");
				}else{
					childeNode.setTitle(quarterStandard.getQuarterName());
				}
				
				childeNode.setType("quarter");
				childeNode.setLeaf(true);
				childeNode.setParentId(parentNode.getId());
				parentNode.getChildren().add(childeNode);
				quarterCodeMap.put(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode(), quarterStandard.getQuarterCode());
			}
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(quarterStandardList, nameTerm);
		}
		return "quarterStandardList";
	}
	
	
	public String getQuarterStandardEx(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "quarterStandardList";
	 	}
		quarterStandardList = new ArrayList();
		Map termMap = new HashMap();
		termMap.put("organId", usersess.getCurrentOrganId());
		List<QuarterStandard> list = service.queryData("quarterStandardHql", termMap, null);
		Map quarterStandardMap = new HashMap();
		Map quarterCodeMap = new HashMap();
		for(int i=0;i<list.size();i++){
			QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			if(quarterCodeMap.get(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode())==null){
				TreeNode parentNode = (TreeNode)quarterStandardMap.get(quarterStandard.getDeptName());
				if(parentNode == null){
					parentNode = new TreeNode();
					parentNode.setId(quarterStandard.getDeptName());
					parentNode.setTitle(quarterStandard.getDeptName());
					parentNode.setType("dept");
					parentNode.setChildren(new ArrayList());
					
					quarterStandardList.add(parentNode);
					quarterStandardMap.put(parentNode.getId(), parentNode);
				}
				
				TreeNode childeNode = new TreeNode();
				//childeNode.setId(quarterStandard.getQuarterCode());
				//childeNode.setTitle(quarterStandard.getQuarterName());
				childeNode.setId(quarterStandard.getQuarterId());
				if(quarterStandard.getSpecialityName()!=null && !"".equals(quarterStandard.getSpecialityName())
						&& !"null".equals(quarterStandard.getSpecialityName())){
					childeNode.setTitle(quarterStandard.getQuarterName()+"("+quarterStandard.getSpecialityName()+")");
				}else{
					childeNode.setTitle(quarterStandard.getQuarterName());
				}
				
				childeNode.setType("quarter");
				childeNode.setLeaf(true);
				childeNode.setParentId(parentNode.getId());
				parentNode.getChildren().add(childeNode);
				quarterCodeMap.put(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode(), quarterStandard.getQuarterCode());
			}
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(quarterStandardList, nameTerm);
		}
		return "quarterStandardList";
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
	public String getTypeTerm() {
		return typeTerm;
	}
	public void setTypeTerm(String typeTerm) {
		this.typeTerm = typeTerm;
	}
	public Boolean isValid() {
		return valid;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public List<Quarter> getQuarters() {
		return quarters;
	}
	public void setQuarters(List<Quarter> quarters) {
		this.quarters = quarters;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String getOrganTerm() {
		return organTerm;
	}
	public void setOrganTerm(String organTerm) {
		this.organTerm = organTerm;
	}
}