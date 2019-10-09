package cn.com.ite.hnjtamis.baseinfo.quarterStandard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.baseinfo.quarterStandard.QuarterStandardListAction</p>
 * <p>Description  标准岗位处理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月23日 下午2:05:27
 * @version 1.0
 * 
 * @modified records:
 */
public class QuarterStandardListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = 7286939450860085319L;
	private HttpServletRequest request;
	
	private List<TreeNode> children;
	
	private String nameTerm;
	
	private String deptName;
	
	private String organId;
	
	private String selectIds;
	
	private String queryType;//null-全部 10-标准 20-电厂私有
	
	private String isExist;
	
	private String notQid;
	
	private List<QuarterStandard> list;
	
	private String op;//stinput-标准岗位维护  oginput-电厂管理员维护   stview-标准岗位查看 ogview-电厂查看
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	/*
	 * 列表查询
	 */
	public String list() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			QuarterStandardService quarterStandardService = (QuarterStandardService)this.getService();
			Map quarterStrandardNumMap = quarterStandardService.getQuarterStrandardNum();
			
			list = new ArrayList();
			Map term = new HashMap();
			term.put("deptName", deptName);
			if(nameTerm==null || "".equals(nameTerm) || "null".equals(nameTerm)){
				term.put("nameTerm", "%");
			}else{
				term.put("nameTerm", nameTerm);
			}
			if(!"20".equals(queryType)){
				term.put("belongType", "10");
				list = service.queryData("queryHql", term, null);
			}
			
			if(("oginput".equals(op) || "ogview".equals(op)) && !"10".equals(queryType)){
				term.put("belongType", "20");
				term.put("organId", usersess.getCurrentOrganId());
				List tmplist = service.queryData("queryInOrganHql", term, null);
				if(tmplist.size()>0){
					list.addAll(tmplist);
				}
			}
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					QuarterStandard quarterStandard = list.get(i);
					String _standardnums = (String)quarterStrandardNumMap.get(quarterStandard.getQuarterId());
					if(_standardnums!=null && !"".equals(_standardnums)){
						try{
							quarterStandard.setStandardnums(Integer.parseInt(_standardnums));
						}catch(Exception e){
							quarterStandard.setStandardnums(0);
						}
					}
				}
			}
			
			this.setTotal(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "list";
	}
	

	
	public String getQuarterStandardDept(){		
		children = new ArrayList();
		List<QuarterStandard> list = service.queryData("quarterStandardAllHql", null, null);
		for(int i=0;i<list.size();i++){
			QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			if(quarterStandard.getDeptName()!=null && !"".equals(quarterStandard.getDeptName())
					&& !"null".equals(quarterStandard.getDeptName())){
				TreeNode parentNode = new TreeNode();
				parentNode.setId(quarterStandard.getDeptName());
				parentNode.setTitle(quarterStandard.getDeptName());
				parentNode.setType("dept");
				parentNode.setChildren(new ArrayList());
				children.add(parentNode);
			}

		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	
	public String getQuarterSpeciality(){		
		children = new ArrayList();
		List<QuarterStandard> list = service.queryData("quarterStandardAllHql", null, null);
		for(int i=0;i<list.size();i++){
			QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			if(quarterStandard.getSpecialityName()!=null && !"".equals(quarterStandard.getSpecialityName())
					&& !"null".equals(quarterStandard.getSpecialityName())){
				TreeNode parentNode = new TreeNode();
				parentNode.setId(quarterStandard.getSpecialityName());
				parentNode.setTitle(quarterStandard.getSpecialityName());
				parentNode.setType("speciality");
				parentNode.setChildren(new ArrayList());
				children.add(parentNode);
			}
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	
	public String getSysStandardQuarter(){
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
		List<QuarterStandard> list = service.queryData("querySysStandardHql", null, null);
		Map quarterStandardMap = new HashMap();
		for(int i=0;i<list.size();i++){
			    QuarterStandard quarterStandard = (QuarterStandard)list.get(i);
			    if(notQid!=null && notQid.indexOf(quarterStandard.getQuarterId())!=-1){
			    	continue;
			    }
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
				if(selectMap.get(childeNode.getId())!=null){
					childeNode.setChecked(true);
				}
				parentNode.getChildren().add(childeNode);

		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		return "tree";
	}
	
	
	public String delete() throws Exception{
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		for(int i = 0;i<ids.length ;i++){
			QuarterStandard  quarterStandard = (QuarterStandard)service.findDataByKey(ids[i], QuarterStandard.class);
			if(quarterStandard!=null){
				this.getService().delete(quarterStandard);
				succ++;
			} 
			sum++;
		}
		if(sum==succ){
			this.setMsg("成功岗位"+succ+"个岗位！");
		}else{
			this.setMsg("选中"+sum+"个岗位,成功删除"+succ+"个岗位！");
		}
		return "delete";
	}
	
	
	
	/**
	 *
	 * @author zhujian
	 * @description 查询是否编码有重复
	 * @return
	 * @modified
	 */
	public String existQuarterStrandardName(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "isExistCode";
	 	}
		try{
			Map param = new HashMap();
			Map sortMap = new HashMap();
			param.put("id", this.getId());
			param.put("quarterName", nameTerm);
			
			param.put("organId", usersess.getOrganId());
			param.put("deptName", this.getDeptName());
			List<QuarterStandard> pojolist = null;
			if("oginput".equals(op) || "ogview".equals(op)){
				pojolist = this.getService().queryData("existOrganQuarterStrandardNameHql", param , sortMap);
			}else{
				pojolist = this.getService().queryData("existQuarterStrandardNameHql", param , sortMap);
			}
			if(pojolist!=null && pojolist.size()>0){
				isExist = "1";
			}else{
				isExist = "0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "isExistCode";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 查询是否编码有重复
	 * @return
	 * @modified
	 */
	public String existQuarterStrandardCode(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "isExistCode";
	 	}
		try{
			Map param = new HashMap();
			Map sortMap = new HashMap();
			param.put("id", this.getId());
			param.put("quarterCode", nameTerm);
			List<QuarterStandard> pojolist = this.getService().queryData("existQuarterStrandardCodeHql", param , sortMap);
			if(pojolist!=null && pojolist.size()>0){
				isExist = "1";
			}else{
				isExist = "0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "isExistCode";
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

	public List<QuarterStandard> getList() {
		return list;
	}

	public void setList(List<QuarterStandard> list) {
		this.list = list;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getSelectIds() {
		return selectIds;
	}

	public void setSelectIds(String selectIds) {
		this.selectIds = selectIds;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getIsExist() {
		return isExist;
	}

	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}

	public String getOrganId() {
		return organId;
	}

	public void setOrganId(String organId) {
		this.organId = organId;
	}

	public String getNotQid() {
		return notQid;
	}

	public void setNotQid(String notQid) {
		this.notQid = notQid;
	}
	
	
}
