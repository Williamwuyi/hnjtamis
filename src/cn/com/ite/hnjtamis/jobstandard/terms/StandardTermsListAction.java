package cn.com.ite.hnjtamis.jobstandard.terms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsUnionStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;
import cn.com.ite.hnjtamis.jobstandard.terms.form.JobsStandardQuarterForm;
/**
 * <p>Title 岗位达标培训信息系统-岗位标准模块</p>
 * <p>Description 岗位标准条款信息ListAction</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 27, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsListAction extends AbstractListAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	/**
	 * 查询结果
	 */
	private List<StandardTerms> list = new ArrayList<StandardTerms>();
	private List<TreeNode> children;
	private List quarters;
	
	// 查询条件
	private String toptypeidTerm;
	private String nameTerm="";  //
	private String jobnameTerm="";
	private String jobidTerm="";
	private String validStr;
	private String typenameTerm;
	private String parentNameTerm;
	private String quarterIdTerm;//系统岗位
	private String quarterStandardIdTerm;//标准岗位
	private String onlyParent;//1-仅仅显示 类型
	
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	
	public String list()throws Exception{
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<StandardTerms>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		
		//进行数据缓存
		List<StandardTypes> standardTypesList = this.getService().queryAllDate(StandardTypes.class);
		Map<String,StandardTypes> standardTypesMap = new HashMap<String,StandardTypes>();
		for(int i=0;i<standardTypesList.size();i++){
			StandardTypes standardTypes = standardTypesList.get(i);
			standardTypesMap.put(standardTypes.getJstypeid(), standardTypes);
		}
		for(int i=0;i<list.size();i++){
			StandardTerms standardTerms = list.get(i);
			standardTerms.setStandardTypes(standardTypesMap.get(standardTerms.getStandardTypes().getJstypeid()));
			standardTerms.setJobsUnionStandards(new ArrayList<JobsUnionStandard>(0));
			list.set(i, standardTerms);
			 
		}
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		this.setTotal(service.countData("queryHql", this));
		return "list";
	}
	
	public String sortlist()throws Exception{
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<StandardTerms>)service.queryData("queryHql", new HashMap(),null);
		
		//进行数据缓存
		List<StandardTypes> standardTypesList = this.getService().queryAllDate(StandardTypes.class);
		Map<String,StandardTypes> standardTypesMap = new HashMap<String,StandardTypes>();
		for(int i=0;i<standardTypesList.size();i++){
			StandardTypes standardTypes = standardTypesList.get(i);
			standardTypesMap.put(standardTypes.getJstypeid(), standardTypes);
		}
		for(int i=0;i<list.size();i++){
			StandardTerms standardTerms = list.get(i);
			standardTerms.setStandardTypes(standardTypesMap.get(standardTerms.getStandardTypes().getJstypeid()));
			standardTerms.setJobsUnionStandards(new ArrayList<JobsUnionStandard>(0));
			list.set(i, standardTerms);
			 
		}
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		this.setTotal(service.countData("queryHql", new HashMap()));
		return "list";
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		String[] ids =this.getId().split(",");
		for(int i=0;i<ids.length;i++){
			StandardTerms vo = (StandardTerms)service.findDataByKey(ids[i], StandardTerms.class);
			AbstractDomain.updateCommonFieldValue(vo);
			vo.setStatus(DicDefine.DATA_DELETE);
			vo.setIsavailable(DicDefine.NOT_VALIDATE);
			vo.setStandardname(vo.getStandardname()+DicDefine.DEL_SUFFIX);
			
			service.save(vo);
		}
		//service.deleteByKeys(this.getId().split(","), StandardTerms.class);
		
		this.setMsg("条款删除成功！");
		return "delete";
	}
	
	public String tree()throws Exception{
		StandardTermsService stService = (StandardTermsService)service;
//		UserSession us = LoginAction.getUserSessionInfo();
		try{
			children = (List<TreeNode>)stService.findStandardTermsTree("", jobidTerm);
			if(!StringUtils.isEmpty(nameTerm)){//条件过滤
				TreeNode.select(children, nameTerm);
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return "tree";
	}
	
	
	public String modeltree(){
		children = new ArrayList();
		StandardTermsService stService = (StandardTermsService)service;
		Map term = new HashMap();
		List<StandardTypes> list = (List<StandardTypes>)stService.queryData("modeltree", term, new HashMap());//.queryConfigQl("queryTreeHql", term, null, TreeNode.class);
//		Map<String,TreeNode> ms = new LinkedHashMap<String,TreeNode>();
		if(list!=null && list.size()>0){
			if("1".equals(onlyParent)){
				for(int i=0;i<list.size();i++){
					StandardTypes standardTypes = (StandardTypes)list.get(i);
					if(standardTypes.getParentSpeciltype() == null){
						TreeNode node = new TreeNode();
						node.setId(standardTypes.getJstypeid());
						node.setTitle(standardTypes.getTypename());
						
						node.setChildren(new ArrayList());
						node.setParentId(null);
						node.setIcon("resources/icons/fam/organ.gif");
						node.setType("parentType");
						children.add(node);
					}
				}
			}else{
				List tmpList = new ArrayList();
				Map tmpMap = new HashMap();
				for(int i=0;i<list.size();i++){
					StandardTypes standardTypes = (StandardTypes)list.get(i);
					TreeNode node = new TreeNode();
					node.setId(standardTypes.getJstypeid());
					node.setTitle(standardTypes.getTypename());
					
					node.setChildren(new ArrayList());
					if(standardTypes.getParentSpeciltype() != null){
						node.setParentId(standardTypes.getParentSpeciltype().getJstypeid());
						node.setIcon("resources/icons/fam/dept.gif");
						node.setType("standardtype");
					}else{
						node.setParentId(null);
						node.setIcon("resources/icons/fam/organ.gif");
						node.setType("parentType");
					}
					tmpMap.put(node.getId(), node);
					tmpList.add(node);
				}
				for(int i=0;i<tmpList.size();i++){
					TreeNode node = (TreeNode)tmpList.get(i);
					if(node.getParentId()==null){
						children.add(node);
					}else{
						TreeNode parentNode = (TreeNode)tmpMap.get(node.getParentId());
						if(parentNode!=null)
						parentNode.getChildren().add(node);
					}
				}
			}
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	public String standardTermsTree()throws Exception{
		StandardTermsService stService = (StandardTermsService)service;
		children = new ArrayList();
		try{
			Map term = new HashMap();
			List list = stService.queryData("queryStandardTypes", term, new HashMap());
			Map<String,TreeNode> standardTypesMap = new HashMap<String,TreeNode>();
			for(int i=0;i<list.size();i++){
				StandardTypes standardTypes = (StandardTypes)list.get(i);
				if(standardTypesMap.get(standardTypes.getJstypeid())==null){
					this.addStandardTypesNode(standardTypes, standardTypesMap);
				}
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
	
	private TreeNode addStandardTypesNode(StandardTypes standardTypes,Map<String,TreeNode> standardTypesMap){
		TreeNode node = new TreeNode();
		node.setId(standardTypes.getJstypeid());
		node.setTitle(standardTypes.getTypename());
		node.setType("standardType");
		node.setChildren(new ArrayList());
		if(standardTypes.getParentSpeciltype()!=null){
			node.setParentId(standardTypes.getParentSpeciltype().getJstypeid());
			TreeNode parentNode = standardTypesMap.get(standardTypes.getParentSpeciltype().getJstypeid());
			if(parentNode==null){
				parentNode = this.addStandardTypesNode(standardTypes.getParentSpeciltype(), standardTypesMap);
				standardTypesMap.put(standardTypes.getParentSpeciltype().getJstypeid(),parentNode);
			}
			parentNode.getChildren().add(node);
		}else{
			children.add(node);
		}
		
		
		List jobsStandardterms = standardTypes.getJobsStandardterms();
		if(jobsStandardterms!=null && jobsStandardterms.size()>0){
			for(int j=0;j<jobsStandardterms.size();j++){
				StandardTerms standardTerms = (StandardTerms)jobsStandardterms.get(j);
				
				TreeNode childNode = new TreeNode();
				childNode.setParentId(standardTypes.getJstypeid());
				childNode.setId(standardTerms.getStandardid());
				childNode.setTitle(standardTerms.getStandardname());
				childNode.setType("standardTerms");
				childNode.setChildren(new ArrayList());
				childNode.setLeaf(true);
				node.getChildren().add(childNode);
			}
		}
		return node;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 查询标准岗位信息
	 * @return
	 * @modified
	 */
	public String quartersTree(){
		quarters = new ArrayList();
		StandardTermsService stService = (StandardTermsService)service;
		Map term = new HashMap();
		List<QuarterStandard> list = stService.queryData("queryQuarterStandard", term, new HashMap());
		Map deptMap = new HashMap();
		Map specialityMap = new HashMap();
		for(int i=0;i<list.size();i++){
			QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			JobsStandardQuarterForm deptForm = (JobsStandardQuarterForm)deptMap.get(quarterStandard.getDeptName());
			if(deptForm==null){
				deptForm = new JobsStandardQuarterForm();
				deptForm.setQuarterName(quarterStandard.getDeptName());
				deptForm.setQuarterCode(quarterStandard.getDeptName());
				deptForm.setType("dept");
				deptForm.setIcon("resources/icons/fam/organ.gif");
				deptMap.put(quarterStandard.getDeptName(), deptForm);
				deptForm.setChecked(null);
				quarters.add(deptForm);
			}
			
			
			/*JobsStandardQuarterForm spForm = (JobsStandardQuarterForm)specialityMap.get(quarterStandard.getSpecialityName());
			if(spForm==null && quarterStandard.getSpecialityName()!=null && !"".equals(quarterStandard.getSpecialityName())){
				spForm = new JobsStandardQuarterForm();
				spForm.setQuarterName(quarterStandard.getSpecialityName());
				spForm.setQuarterCode(quarterStandard.getSpecialityName());
				spForm.setType("speciality");
				spForm.setIcon("resources/icons/fam/dept.gif");
				deptForm.getQuarters().add(spForm);
				specialityMap.put(quarterStandard.getSpecialityName(), spForm);
			}*/
			
			JobsStandardQuarterForm quarterForm= new JobsStandardQuarterForm();
			quarterForm.setQuarterCode(quarterStandard.getQuarterCode());
			quarterForm.setQuarterName(quarterStandard.getQuarterName()+"("+quarterStandard.getQuarterCode()+")");
			quarterForm.setIcon("resources/icons/fam/dept.gif");
			quarterForm.setType("quarter");
			/*if(quarterStandard.getSpecialityName()!=null && !"".equals(quarterStandard.getSpecialityName())){
				spForm.getQuarters().add(quarterForm);
			}else{*/
				deptForm.getQuarters().add(quarterForm);
			//}
			
		}
		
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			JobsStandardQuarterForm.select(quarters, nameTerm);
		}
		
		return "quartersTree";
	}
	
	
	
	
	public String getQuarterIdTerm() {
		return quarterIdTerm;
	}
	public void setQuarterIdTerm(String quarterIdTerm) {
		this.quarterIdTerm = quarterIdTerm;
	}
	public List<StandardTerms> getList() {
		return list;
	}
	public void setList(List<StandardTerms> list) {
		this.list = list;
	}
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public String getNameTerm() {
		//setTypenameTerm("3333");
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
	public String getJobnameTerm() {
		return jobnameTerm;
	}
	public void setJobnameTerm(String jobnameTerm) {
		this.jobnameTerm = jobnameTerm;
	}
	public String getJobidTerm() {
		return jobidTerm;
	}
	public void setJobidTerm(String jobidTerm) {
		this.jobidTerm = jobidTerm;
	}
	public List getQuarters() {
		return quarters;
	}
	public void setQuarters(List quarters) {
		this.quarters = quarters;
	}
	public String getTypenameTerm() {
		return typenameTerm;
	}
	public void setTypenameTerm(String typenameTerm) {
		this.typenameTerm = typenameTerm;
	}
	public String getParentNameTerm() {
		return parentNameTerm;
	}
	public void setParentNameTerm(String parentNameTerm) {
		this.parentNameTerm = parentNameTerm;
	}
	public String getOnlyParent() {
		return onlyParent;
	}
	public void setOnlyParent(String onlyParent) {
		this.onlyParent = onlyParent;
	}
	public String getQuarterStandardIdTerm() {
		return quarterStandardIdTerm;
	}
	public void setQuarterStandardIdTerm(String quarterStandardIdTerm) {
		this.quarterStandardIdTerm = quarterStandardIdTerm;
	}
	
	
}
