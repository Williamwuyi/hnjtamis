package cn.com.ite.hnjtamis.exam.testpaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.eap2.domain.organization.Dept;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.Testpaper;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperForm;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperListAction</p>
 * <p>Description 试卷模版生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:06:06
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = 3630769248916789696L;
	
	
	private HttpServletRequest request;
	
	private List<TestpaperForm> testpaperFormList;
	
	//以下几个为查询条件
	private String testpaperNameTerm;
	
	private Integer totalThemeMinTerm;
	
	private Integer totalThemeMaxTerm;
	
	private Double totalScoreMinTerm;
	
	private Double totalScoreMaxTerm;
	
	private String createdByTerm;
	
	private String creationDateMaxTerm;
	
	private String creationDateMinTerm;
	
	private String stateTerm;
	
	
	private List<TestpaperThemeForm> themeInTemplateList;
	
	private int themeInTemplateListTotal;
	
	private String testpaperId;
	
	private String templateConfigStr;
	
	private String listType;//获取试题列表方式 query-按父对象ID查询，create-生成
	
	private List<TreeNode> children;
	
	private String themeBankIds;
	
	private Integer examType;
	
	private String selectThemeIds;//已经选择的题目
	
	//查询题目条件 ---------------
	private String qthemeBankIds;
	
	private String qthemeType;
	
	private String qgjzName;
	//-------------------------
	
	private String sf;

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	
	public String list()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		TestpaperService testpaperService = (TestpaperService)this.getService();
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("testpaperNameTerm", testpaperNameTerm);
		
		if(totalThemeMinTerm!=null)
			param.put("totalThemeMinTerm", (short)totalThemeMinTerm.intValue());
		if(totalThemeMaxTerm!=null)
			param.put("totalThemeMaxTerm", (short)totalThemeMaxTerm.intValue());
		if(totalScoreMinTerm!=null)
			param.put("totalScoreMinTerm", (double)totalScoreMinTerm);
		if(totalScoreMaxTerm!=null)
			param.put("totalScoreMaxTerm", (double)totalScoreMaxTerm);
		param.put("createdByTerm", createdByTerm);
		param.put("creationDateMaxTerm", creationDateMaxTerm);
		param.put("creationDateMinTerm", creationDateMinTerm);
		param.put("stateTerm", stateTerm);
		/*
		System.out.println(" totalThemeMinTerm = "+ totalThemeMinTerm);
		System.out.println(" totalThemeMaxTerm = "+ totalThemeMaxTerm);
		System.out.println(" totalScoreMinTerm = "+ totalScoreMinTerm);
		System.out.println(" totalScoreMaxTerm = "+ totalScoreMaxTerm);
		System.out.println(" createdByTerm = "+ createdByTerm);
		System.out.println(" creationDateMaxTerm = "+ creationDateMaxTerm);
		System.out.println(" creationDateMinTerm = "+ creationDateMinTerm);
		System.out.println(" stateTerm = "+ stateTerm);*/
		
		
		
		//考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
		if(examType!=null){
			param.put("examType", examType);
			if(examType.intValue() == 40){
				param.put("employeeId", usersess.getEmployeeId());
			}
		}else{
			param.put("examType", "10,20");
		}
		String hqlName =  "testpaperForParamHql";
		if("pro".equals(sf)){
			param.put("employeeIdTerm", usersess.getEmployeeId());
			hqlName =  "testpaperForEmployeeParamHql";
		}else if("dept".equals(sf)){
			Dept dept = (Dept)this.getService().findDataByKey(usersess.getCurrentDeptId(), Dept.class);
			while(dept.getDept()!=null){
				dept = dept.getDept();
			}
			List<String> deptIdsList = testpaperService.getAllDeptInDeptId(dept.getDeptId());
			String deptIds = "";
			if(deptIdsList!=null && deptIdsList.size()>0){
				for(int i=0;i<deptIdsList.size();i++){
					deptIds+=deptIdsList.get(i)+",";
				}
				deptIds = deptIds.substring(0,deptIds.length()-1);
			}else{
				deptIds=dept.getDeptId();
			}
			param.put("deptIdTerm", deptIds);
			hqlName =  "testpaperForDeptParamHql";
		}else {
			param.put("organIdTerm", usersess.getCurrentOrganId());
		}
		
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		sortMap.put("createdBy", false);
		List<Testpaper> testpaperpojolist = this.getService().queryData(hqlName, param , sortMap,this.getStart(),this.getLimit());
		testpaperFormList = new ArrayList<TestpaperForm>();
		if(testpaperpojolist!=null && testpaperpojolist.size()>0){
			for(int i=0;i<testpaperpojolist.size();i++){
				Testpaper testpaper = (Testpaper)testpaperpojolist.get(i);
				TestpaperForm testpaperForm = new TestpaperForm();
				try {
					BeanUtils.copyProperties(testpaper, testpaperForm);
					if(testpaperForm.getIsUse() == null){
						testpaperForm.setIsUse(0);
					}
					testpaperFormList.add(testpaperForm);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		this.setTotal(service.countData(hqlName, param));
		return "list";
	}
	
	
	/**
	 * 删除
	 * @author zhujian
	 * @description
	 * @return
	 * @throws EapException
	 * @modified
	 */
	public String delete() throws EapException{
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		for(int i = 0;i<ids.length ;i++){
			Testpaper  testpaper = (Testpaper)service.findDataByKey(ids[i], Testpaper.class);
			try{
				if(testpaper==null){
					
				}else if(testpaper.getExams()!=null && testpaper.getExams().size() > 0){
					
				}else{
					testpaper.getTestpaperThemes().clear();
					testpaper.getTestpaperShares().clear();
					testpaper.getTestpaperThemetypes().clear();
					testpaper.getTestpaperSkeies().clear();
					service.delete(testpaper);
					succ++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			sum++;
		}
		//service.deleteByKeys(ids, Theme.class);
		this.setMsg("选中试卷"+sum+"个,成功删除"+succ+"个！");
		return "delete";
	}
	
	
	/**
	 * 获取考试的考题
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String getThemeList(){
		
		themeInTemplateList = new ArrayList<TestpaperThemeForm>();
		themeInTemplateListTotal = 0;
		TestpaperService testpaperService=(TestpaperService)this.getService();
		if("query".equals(listType) || "preview".equals(listType)){//获取试题列表方式 query-按父对象ID查询，create-生成
			themeInTemplateList = testpaperService.getTestpaperThemeList(testpaperId);
		}else if("create".equals(listType)){//获取试题列表方式 query-按父对象ID查询，create-生成
			String[] tmplateArr = templateConfigStr.split("},");
			themeInTemplateList = testpaperService.getThemeInTemplate(tmplateArr, 
					themeBankIds, examType,null,null,null);
		}
		themeInTemplateListTotal = themeInTemplateList.size();
		if("preview".equals(listType)){ //预览情况需要答案在页面进行格式生成
			return "themeList";
		}else{
			return "themeListNotAns";
		}
		
	}
	
	
	public String getHandThemeList(){
		themeInTemplateList = new ArrayList<TestpaperThemeForm>();
		themeInTemplateListTotal = 0;
		TestpaperService testpaperService=(TestpaperService)this.getService();
		Map paramMap = new HashMap();
		paramMap.put("themeBankIds",themeBankIds);
		paramMap.put("qthemeBankIds",qthemeBankIds);
		paramMap.put("qthemeType",qthemeType);
		paramMap.put("qgjzName",qgjzName);
		
		String xz = request.getParameter("xz");
		if("page".equals(xz)){
			paramMap.put("xz","page");
			paramMap.put("row_start", this.getStart());
			paramMap.put("row_limit", this.getLimit());
		}
		themeInTemplateList = testpaperService.getHandAddThemeList(selectThemeIds,paramMap);
		if("page".equals(xz)){
			themeInTemplateListTotal = testpaperService.getHandAddThemeListCount(selectThemeIds,paramMap);
		}else{
			themeInTemplateListTotal = themeInTemplateList.size();
		}
		
		return "handThemeList";
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 试卷 树
	 * @return
	 * @modified
	 */
	public String testPaperTreeList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			children = new ArrayList();
			return "testPaperTreeList";
	 	}
		TestpaperService testpaperService = (TestpaperService)this.getService();
		List<Dictionary> examKslxList = testpaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
		if(examKslxList!=null && examKslxList.size()>0){
			children = new ArrayList();
			for(int k=0;k<examKslxList.size();k++){
				Dictionary dictionary = (Dictionary)examKslxList.get(k);
				Map paramMap = new HashMap();
				paramMap.put("examTypeId",dictionary.getDataKey());
				Map sortMap = new HashMap();
				paramMap.put("creationDate",false);
				paramMap.put("organIdTerm",usersess.getCurrentOrganId());
				List<Testpaper> list = this.getService().queryData("testpaperInExamTypeIdTreeHql", paramMap, sortMap);
				if(list!=null && list.size()>0){
					TreeNode parent = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
					parent.setId(dictionary.getDataKey());
					parent.setTitle(dictionary.getDataName());
					parent.setType("type");
					parent.setChildren(new ArrayList());
					
					for(int i= 0;i<list.size();i++){
						Testpaper testpaper = (Testpaper)list.get(i);
						TreeNode child = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
						child.setId(testpaper.getTestpaperId());
						child.setTitle(testpaper.getTestpaperName());
						child.setType("testpaper");
						child.setParentId(dictionary.getDataKey());
						child.setLeaf(true);
						parent.getChildren().add(child);
					}
					children.add(parent);
				}
			}
		}
		return "testPaperTreeList";
	}
	
	
	public String queryBankTree(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			children = new ArrayList();
			return "testPaperTreeList";
	 	}
		if(this.getId()==null || this.getId().length()==0 || "null".equals(this.getId())){
			children = new ArrayList();
		}else{
			TestpaperService testpaperService = (TestpaperService)this.getService();
			Map term = new HashMap();
			term.put("id", this.getId());
			children = this.getService().queryData("queryBanktreeByIds", term, null,TreeNode.class);
			for(TreeNode treeNode : children){
				treeNode.setChildren(new ArrayList());
				treeNode.setLeaf(true);
			}
		}
		return "testPaperTreeList";
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 试卷 树
	 * @return
	 * @modified
	 */
	public String testExamPaperTreeList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			children = new ArrayList();
			return "testPaperTreeList";
	 	}
		TestpaperService testpaperService = (TestpaperService)this.getService();
		List<Dictionary> examKslxList = testpaperService.getDictionaryTypeList(StaticVariable.EXAM_KSLX);
		if(examKslxList!=null && examKslxList.size()>0){
			children = new ArrayList();
			String[][] treenames = {{"unUsedTestpaper","未安排考试的试卷"},{"usedTestpaper","已安排考试的试卷"}};
			for(int ii=0;ii<treenames.length;ii++){
				String tcode = treenames[ii][0];
				String tname = treenames[ii][1];
				TreeNode pparent = null;
				for(int k=0;k<examKslxList.size();k++){
					Dictionary dictionary = (Dictionary)examKslxList.get(k);
					Map paramMap = new HashMap();
					paramMap.put("examTypeId",dictionary.getDataKey());
					Map sortMap = new HashMap();
					paramMap.put("creationDate",false);
					paramMap.put("organIdTerm",usersess.getCurrentOrganId());
					List<Testpaper> list = this.getService().queryData(tcode+"InExamTypeIdTreeHql", paramMap, sortMap);
					if(list!=null && list.size()>0){
						TreeNode parent = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
						parent.setId(tcode+dictionary.getDataKey());
						parent.setTitle(dictionary.getDataName());
						parent.setType("type");
						parent.setChildren(new ArrayList());
						
						for(int i= 0;i<list.size();i++){
							Testpaper testpaper = (Testpaper)list.get(i);
							TreeNode child = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
							child.setId(testpaper.getTestpaperId());
							child.setTitle(testpaper.getTestpaperName());
							child.setType("testpaper");
							child.setParentId(parent.getId());
							child.setLeaf(true);
							parent.getChildren().add(child);
						}
						
						if(pparent == null){
							pparent = new TreeNode();//二级节点:部门        如果是部门管理员的话 需要查看 此部门的顶级部门下的所有子部门
							pparent.setId(tcode);
							pparent.setTitle(tname);
							pparent.setType("type");
							pparent.setChildren(new ArrayList());
							pparent.getChildren().add(parent);
							children.add(pparent);
						}else{
							pparent.getChildren().add(parent);
						}
					}
				}
			}
		}
		return "testPaperTreeList";
	}

	public List<TestpaperForm> getTestpaperFormList() {
		return testpaperFormList;
	}

	public void setTestpaperFormList(List<TestpaperForm> testpaperFormList) {
		this.testpaperFormList = testpaperFormList;
	}

	public String getTestpaperNameTerm() {
		return testpaperNameTerm;
	}

	public void setTestpaperNameTerm(String testpaperNameTerm) {
		this.testpaperNameTerm = testpaperNameTerm;
	}

	public List getThemeInTemplateList() {
		return themeInTemplateList;
	}

	public void setThemeInTemplateList(List themeInTemplateList) {
		this.themeInTemplateList = themeInTemplateList;
	}

	public int getThemeInTemplateListTotal() {
		return themeInTemplateListTotal;
	}

	public void setThemeInTemplateListTotal(int themeInTemplateListTotal) {
		this.themeInTemplateListTotal = themeInTemplateListTotal;
	}

	public String getTestpaperId() {
		return testpaperId;
	}

	public void setTestpaperId(String testpaperId) {
		this.testpaperId = testpaperId;
	}

	public String getTemplateConfigStr() {
		return templateConfigStr;
	}

	public void setTemplateConfigStr(String templateConfigStr) {
		this.templateConfigStr = templateConfigStr;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public String getThemeBankIds() {
		return themeBankIds;
	}

	public void setThemeBankIds(String themeBankIds) {
		this.themeBankIds = themeBankIds;
	}

	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}

	public Integer getTotalThemeMinTerm() {
		return totalThemeMinTerm;
	}

	public void setTotalThemeMinTerm(Integer totalThemeMinTerm) {
		this.totalThemeMinTerm = totalThemeMinTerm;
	}

	public Integer getTotalThemeMaxTerm() {
		return totalThemeMaxTerm;
	}

	public void setTotalThemeMaxTerm(Integer totalThemeMaxTerm) {
		this.totalThemeMaxTerm = totalThemeMaxTerm;
	}

	public Double getTotalScoreMinTerm() {
		return totalScoreMinTerm;
	}

	public void setTotalScoreMinTerm(Double totalScoreMinTerm) {
		this.totalScoreMinTerm = totalScoreMinTerm;
	}

	public Double getTotalScoreMaxTerm() {
		return totalScoreMaxTerm;
	}

	public void setTotalScoreMaxTerm(Double totalScoreMaxTerm) {
		this.totalScoreMaxTerm = totalScoreMaxTerm;
	}

	public String getCreatedByTerm() {
		return createdByTerm;
	}

	public void setCreatedByTerm(String createdByTerm) {
		this.createdByTerm = createdByTerm;
	}

	public String getCreationDateMaxTerm() {
		return creationDateMaxTerm;
	}

	public void setCreationDateMaxTerm(String creationDateMaxTerm) {
		this.creationDateMaxTerm = creationDateMaxTerm;
	}

	public String getCreationDateMinTerm() {
		return creationDateMinTerm;
	}

	public void setCreationDateMinTerm(String creationDateMinTerm) {
		this.creationDateMinTerm = creationDateMinTerm;
	}

	public String getStateTerm() {
		return stateTerm;
	}

	public void setStateTerm(String stateTerm) {
		this.stateTerm = stateTerm;
	}

	public String getSelectThemeIds() {
		return selectThemeIds;
	}

	public void setSelectThemeIds(String selectThemeIds) {
		this.selectThemeIds = selectThemeIds;
	}

	public String getQthemeBankIds() {
		return qthemeBankIds;
	}

	public void setQthemeBankIds(String qthemeBankIds) {
		this.qthemeBankIds = qthemeBankIds;
	}

	public String getQthemeType() {
		return qthemeType;
	}

	public void setQthemeType(String qthemeType) {
		this.qthemeType = qthemeType;
	}

	public String getQgjzName() {
		return qgjzName;
	}

	public void setQgjzName(String qgjzName) {
		this.qgjzName = qgjzName;
	}

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	

	
}
