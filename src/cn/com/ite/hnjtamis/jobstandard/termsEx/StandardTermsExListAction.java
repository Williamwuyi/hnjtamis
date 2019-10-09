package cn.com.ite.hnjtamis.jobstandard.termsEx;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.common.DicDefine;
import cn.com.ite.hnjtamis.baseinfo.domain.AbstractDomain;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublicSearchkey;
import cn.com.ite.hnjtamis.jobstandard.domain.JobsStandardQuarter;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTypes;

/**
 * 岗位标准管理
 * @author 朱健
 * @create time: 2016年3月4日 上午9:03:03
 * @version 1.0
 * 
 * @modified records:
 */
public class StandardTermsExListAction extends AbstractListAction {

	private static final long serialVersionUID = 1269125633539314732L;
	
	private List<StandardTerms> list = new ArrayList<StandardTerms>();
	
	private List<JobsStandardQuarter> jobsStandardQuarterList = new ArrayList<JobsStandardQuarter>();
	
	private int jobsStandardQuarterTotal;
	
	private List<TreeNode> children;
	
	private String nameTerm="";
	
	private String onlyParent;
	
	private String typeId;
	
	private String nodeType;
	
	private String standardid;
	
	private String publicId;

	private String standardnameTerm;//查询条件
	
	private String modelIdTerm;
	
	private List quarterStandardList;
	
	private String selectIds;
	
	private String op;
	
	public List getQuarterStandardList() {
		return quarterStandardList;
	}
	public void setQuarterStandardList(List quarterStandardList) {
		this.quarterStandardList = quarterStandardList;
	}

	public String list(){
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		if("parentType".equals(nodeType)){
			Map term = new HashMap();
			term.put("standardnameTerm", standardnameTerm);
			list = standardTermsExService.queryStandardTermsByParentTypeId(typeId,term);//this.getService().queryData("queryStandardTermsByParentTypeId", term, null);
		}else if("standardtype".equals(nodeType)){
			Map term = new HashMap();
			term.put("standardnameTerm", standardnameTerm);
			list = standardTermsExService.queryStandardTermsByTypeId(typeId,term);//this.getService().queryData("queryStandardTermsByTypeId", term, null);
		}else if("quarter".equals(nodeType)){
			Map term = new HashMap();
			term.put("standardnameTerm", standardnameTerm);
			list = standardTermsExService.queryStandardTermsByQuarterId(typeId,term);//this.getService().queryData("queryStandardTermsByTypeId", term, null);
		}else if("userquarter".equals(nodeType)){
			UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
			if(usersess == null){
				this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
				return "list";
		 	}
			if(usersess.getQuarterId()!=null){
				Quarter quarter = (Quarter)this.getService().findDataByKey(usersess.getQuarterId(), Quarter.class);
				Map term = new HashMap();
				term.put("standardnameTerm", standardnameTerm);
				typeId = quarter.getQuarterTrainCode();
				list = standardTermsExService.queryStandardTermsByQuarterId(typeId,term);//this.getService().queryData("queryStandardTermsByTypeId", term, null);
			}else{
				typeId = "";
				list = new ArrayList();
			}
		}else if("all".equals(nodeType)){
			Map term = new HashMap();
			term.put("standardnameTerm", standardnameTerm);
			list = standardTermsExService.queryAllStandardTerms(term);//this.getService().queryData("queryStandardTermsByTypeId", term, null);
		}else if("unquarter".equals(nodeType)){
			Map term = new HashMap();
			term.put("standardnameTerm", standardnameTerm);
			term.put("modelIdTerm", modelIdTerm);
			list = standardTermsExService.queryUnStandardTermsByQuarterId(typeId,term);//this.getService().queryData("queryStandardTermsByTypeId", term, null);
		}else{
			list = new ArrayList();
		}
		this.setTotal(list.size());
		return "list";
	}
	
	public String getStandardTermQuarterList(){
		Map term = new HashMap();
		term.put("standardid", standardid);
		List<JobsStandardQuarter> tmp_jobsStandardQuarterList = this.getService().queryData("queryStandardTermQuarterByStandardId", term, null);
		jobsStandardQuarterList = new ArrayList();
		Map vKeymap = new HashMap();
		for(int i=0;i<tmp_jobsStandardQuarterList.size();i++){
			JobsStandardQuarter jobsStandardQuarter = tmp_jobsStandardQuarterList.get(i);
			if(vKeymap.get(jobsStandardQuarter.getDeptId()+"_"+jobsStandardQuarter.getQuarterId())==null){
				jobsStandardQuarterList.add(jobsStandardQuarter);
				vKeymap.put(jobsStandardQuarter.getDeptId()+"_"+jobsStandardQuarter.getQuarterId(),"a");
			}
		}
		jobsStandardQuarterTotal = jobsStandardQuarterList.size();
		return "jobsStandardQuarterList";
	}
	
	
	public String standardtypeTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "tree";
	 	}
		children = new ArrayList();
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		Map term = new HashMap();
		List<StandardTypes> list = (List<StandardTypes>)standardTermsExService.queryData("queryTreeHql", term, new HashMap());
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
	
	
	public String getStandardModelTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "tree";
	 	}
		
		StandardTermsExService standardTermsExService = (StandardTermsExService)this.getService();
		children = standardTermsExService.getStandardModelTree();
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		String[] ids =this.getId().split(",");
		for(int i=0;i<ids.length;i++){
			StandardTerms vo = (StandardTerms)service.findDataByKey(ids[i], StandardTerms.class);
			AbstractDomain.updateCommonFieldValue(vo);
			vo.setStatus(DicDefine.DATA_DELETE);
			vo.setIsavailable(DicDefine.NOT_VALIDATE);
			vo.setStandardname(vo.getStandardname()+DicDefine.DEL_SUFFIX);
			service.saveOld(vo);
		}
		
		this.setMsg("删除成功！");
		return "delete";
	}
	
	
	public String getQuarterStandard(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "tree";
	 	}
		//两种选择方式，一个是根据标准，一个是根据选择项目来
		Map<String,String> quarterIdsMap = new HashMap<String,String>();
		if(standardid!=null && !"".equals(standardid)&& !"null".equals(standardid)){
			Map term = new HashMap();
			term.put("standardid", standardid);
			jobsStandardQuarterList = this.getService().queryData("queryStandardTermQuarterByStandardId", term, null);
			
			for(int i=0;i<jobsStandardQuarterList.size();i++){
				JobsStandardQuarter jobsStandardQuarter = (JobsStandardQuarter)jobsStandardQuarterList.get(i);
				quarterIdsMap.put(jobsStandardQuarter.getQuarterTrainCode(),jobsStandardQuarter.getQuarterTrainCode());
			}
		}
		
		if(publicId!=null && !"".equals(publicId)&& !"null".equals(publicId)){
			Map term = new HashMap();
			term.put("publicId", publicId);
			List<ExamPublicSearchkey> examPublicSearchkeylist= this.getService().queryData("queryStandardTermQuarterByPublicId", term, null);
			
			for(int i=0;i<examPublicSearchkeylist.size();i++){
				ExamPublicSearchkey jobsStandardQuarter = (ExamPublicSearchkey)examPublicSearchkeylist.get(i);
				quarterIdsMap.put(jobsStandardQuarter.getQuarterTrainCode(),jobsStandardQuarter.getQuarterTrainCode());
			}
		}
		
		
		Map selectMap = new HashMap();
		if(selectIds!=null && !"".equals(selectIds)&& !"null".equals(selectIds)){
			String[] array = selectIds.split(",");
			if(array.length>0){
				for(int i=0;i<array.length;i++){
					selectMap.put(array[i], array[i]);
				}
			}
		}
		//--------------结束----------------------------------------------------------------
		
		children = new ArrayList();
		
		List<QuarterStandard> list = null;
		if(op!=null && op.indexOf("sys")!=-1){
			list = service.queryData("quarterStandardHql", null, null);
		}else if(op!=null && op.indexOf("all")!=-1){
			list = service.queryData("quarterStandardAllHql", null, null);
		}else if(op!=null && op.indexOf("dcupdate")!=-1){
			Map termMap = new HashMap();
			termMap.put("organId", usersess.getCurrentOrganId());
			list = service.queryData("quarterStandardOnlyOrganHql", termMap, null);
		}else{
			Map termMap = new HashMap();
			termMap.put("organId", usersess.getCurrentOrganId());
			list = service.queryData("quarterStandardOrganHql", termMap, null);
		}
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
					
					children.add(parentNode);
					quarterStandardMap.put(parentNode.getId(), parentNode);
				}
				
				TreeNode childeNode = new TreeNode();
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
				if(quarterIdsMap.get(quarterStandard.getQuarterCode())!=null
						|| selectMap.get(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode())!=null){
					childeNode.setChecked(true);
				}
				parentNode.getChildren().add(childeNode);
				quarterCodeMap.put(quarterStandard.getDeptName()+"@"+quarterStandard.getQuarterCode(), quarterStandard.getQuarterCode());
			}
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	
	public List<StandardTerms> getList() {
		return list;
	}

	public void setList(List<StandardTerms> list) {
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


	public String getOnlyParent() {
		return onlyParent;
	}


	public void setOnlyParent(String onlyParent) {
		this.onlyParent = onlyParent;
	}

	public List<JobsStandardQuarter> getJobsStandardQuarterList() {
		return jobsStandardQuarterList;
	}

	public void setJobsStandardQuarterList(
			List<JobsStandardQuarter> jobsStandardQuarterList) {
		this.jobsStandardQuarterList = jobsStandardQuarterList;
	}

	public int getJobsStandardQuarterTotal() {
		return jobsStandardQuarterTotal;
	}

	public void setJobsStandardQuarterTotal(int jobsStandardQuarterTotal) {
		this.jobsStandardQuarterTotal = jobsStandardQuarterTotal;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getStandardid() {
		return standardid;
	}

	public void setStandardid(String standardid) {
		this.standardid = standardid;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getStandardnameTerm() {
		return standardnameTerm;
	}

	public void setStandardnameTerm(String standardnameTerm) {
		this.standardnameTerm = standardnameTerm;
	}
	public String getModelIdTerm() {
		return modelIdTerm;
	}
	public void setModelIdTerm(String modelIdTerm) {
		this.modelIdTerm = modelIdTerm;
	}
	public String getSelectIds() {
		return selectIds;
	}
	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	
	
	
	
	
}
