package cn.com.ite.hnjtamis.exam.base.themebank;

import java.io.File;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Organ;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.CharsetSwitch;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.examroot.util.PinYin2Abbreviation;
import cn.com.ite.hnjtamis.exam.base.themebank.service.ThemeBankService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;
/*
 * 题库管理维护  - list
 */
public class ThemeBankListAction extends AbstractListAction implements ServletRequestAware{
	private static final long serialVersionUID = 8854045242098099393L;
	private HttpServletRequest request;
	
	//查询条件 ----start
	private String nameTerm;
	private Boolean valid;
	private String validStr;
	private String topThemeBankTerm;
	private String organIdTerm;
	private String createOrganIdTerm;//创建机构
	//查询条件 ----end
	
	private String themeBankName;
	private String themeBankCode;
	private String isExist;
	
	private String notZero;
	
	private List<ThemeBank> themeBanks = new ArrayList<ThemeBank>();
	
	private List<TreeNode> children;
	
	private String notHaveIds;
	
	private List themeBankList;
	
	private String standardId;

	private String op;////admin-管理员 可以配置    dc 电厂私有
	
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	
	private String showProTerm;//显示专业题库  true是 其它-否
	/**
	 * 导入文件
	 */
	private File xls;
	
	/*
	 * 导入
	 */
	public String importXls() throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		ThemeBankService themeBankService=(ThemeBankService)getService();
		String msg = themeBankService.importThemeBank(xls,usersess,bankType);
		this.setMsg(msg);
		return "save";
	}
	
	public String specialityThemeBankTree()throws Exception{
		children = new ArrayList();
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "tree";
	 	}
		ThemeBankService themeBankService = (ThemeBankService)this.getService();
		Map paramMap = new HashMap();
		paramMap.put("notHaveIds", notHaveIds);
		paramMap.put("notZero", notZero);
		if("auto".equals(this.getOp())){
			Organ organ = (Organ)this.getService().findDataByKey(usersess.getCurrentOrganId(), Organ.class);
			if(organ.getOrgan()!=null){
				paramMap.put("op", "autoall");
			}else{
				paramMap.put("op", "all");
			}
		}else{
			paramMap.put("op", this.getOp());
		}
		if("true".equals(showProTerm)){
			paramMap.put("bankType", "10");
		}else if(bankType!=null && !"".equals(bankType)  && !"undefined".equals(bankType)  && !"null".equals(bankType)){
			paramMap.put("bankType", bankType);
		}
		paramMap.put("organId", usersess.getCurrentOrganId());
		children = themeBankService.specialityThemeBankTree(paramMap);
		
		if("true".equals(showProTerm)){
			//加入专业题库
			TreeNode proThemeBankNode = themeBankService.getProThemeBankTree();
			children.add(proThemeBankNode);
		}
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	public String tree()throws Exception{
		children = new ArrayList();
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "tree";
	 	}
		ThemeBankService themeBankService = (ThemeBankService)this.getService();
		Map paramMap = new HashMap();
		paramMap.put("notHaveIds", notHaveIds);
		paramMap.put("op", op);
		paramMap.put("organId", usersess.getCurrentOrganId());
		paramMap.put("bankType", this.getBankType());
		children = themeBankService.getThemeBankTree(this.getId(),paramMap);
		if(!StringUtils.isEmpty(nameTerm)){//条件过滤
			TreeNode.select(children, nameTerm);
		}
		
		return "tree";
	}
	
	
	/*
	 * 删除
	 */
	public String delete() throws Exception{
		try {
			ThemeBankService themeBankService =(ThemeBankService)this.getService();
			String tmpId = this.getId().split(",")[0];
			ThemeBank po = (ThemeBank) service.findDataByKey(tmpId, ThemeBank.class);
			
			if(po.getThemeBanks()!=null && po.getThemeBanks().size()>0){
				this.setMsg("请先删除子题库，再删除此父题库");
			}else{
				int themeNum = themeBankService.getThemeNumInBank(po.getThemeBankId());
				if(themeNum == 0){
					themeBankService.deleteThemeInBank(po.getThemeBankId());
					((ThemeBankService)service).deletePosts(po);
					((ThemeBankService)service).deletePros(po);
					service.delete(po);
					try {
						StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
						Map term = new HashMap();
						term.put("id", tmpId);
						List<StandardTerms> old_standardTermslist= service.queryData("queryStandardTermsInBankIdHql", term, null);
						for(int j=0;j<old_standardTermslist.size();j++){
							StandardTerms standardTerms = old_standardTermslist.get(j);
							standardTermsExService.updateMoreStandardQuarterBank(standardTerms.getStandardid());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					this.setMsg("题库删除成功！");
				}else{
					this.setMsg("题库下存在有效的试题，不能删除！");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("题库删除失败,请联系管理员！");
		}
		return "delete";
	}
	
	public String subList() throws Exception{
		themeBanks = (List<ThemeBank>)service.queryData("querySubHql", this, null);
		return "subList";
	}
	
	/*
	 * 列表
	 */
	@SuppressWarnings("unchecked")
	public String list() throws Exception{
		/*if(!StringUtils.isEmpty(this.getValidStr())){
			this.setValid(this.getValidStr().equals("1"));
		}*/
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			ThemeBankService themeBankService = (ThemeBankService)service;
			this.setOrganIdTerm(usersess.getCurrentOrganId());
			if(this.getOp() == null){
				this.setOp("dc");
			}
			List querys = (List<ThemeBank>)themeBankService.queryData("queryHql", this,null);
			Map themeNumMap = themeBankService.getThemeNumMap();
			for(int i=0;i<querys.size();i++){
				ThemeBank themeBank =(ThemeBank)querys.get(i);
				String num = (String)themeNumMap.get(themeBank.getThemeBankId());
				if(num!=null)themeBank.setThemeNum(Integer.parseInt(num));
			}
			themeBanks = service.childObjectHandler(querys, "themeBankId", "themeBank", 
					"themeBanks",new String[]{"themeBankPosts","themeInBanks","themeBankProfessions"},
					null,this.getFilterIds(),"themeBankCode",topThemeBankTerm); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	
	
	public String queryThemeBankList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			this.setOrganIdTerm(usersess.getCurrentOrganId());
			if(this.getOp() == null){
				this.setOp("dc");
			}
			ThemeBankService themeBankService = (ThemeBankService)service;
			themeBankList = (List<ThemeBank>)themeBankService.queryData("queryHql", this,null,this.getStart(),this.getLimit());
			this.setTotal(themeBankService.countData("queryHql", this));
			Map themeNumMap = themeBankService.getThemeNumMap();
			for(int i=0;i<themeBankList.size();i++){
				ThemeBank themeBank =(ThemeBank)themeBankList.get(i);
				String num = (String)themeNumMap.get(themeBank.getThemeBankId());
				if(num!=null)themeBank.setThemeNum(Integer.parseInt(num));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "queryThemeBankList";	
	}
	
	public String themeBanklist() throws Exception{
		/*if(!StringUtils.isEmpty(this.getValidStr())){
			this.setValid(this.getValidStr().equals("1"));
		}*/
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		try {
			this.setOrganIdTerm(usersess.getCurrentOrganId());
			if(this.getOp() == null){
				this.setOp("dc");
			}
			ThemeBankService themeBankService = (ThemeBankService)service;
			List querys = (List<ThemeBank>)themeBankService.queryData("queryHql", this,null);
			themeBanks = service.childObjectHandler(querys, "themeBankId", "themeBank", 
					"themeBanks",new String[]{"themeBankPosts","themeInBanks","themeBankProfessions",
					"publicName","publicTime","bankLevelCode","sortNum","remark","syncFlag","lastUpdateDate","lastUpdatedBy",
					"lastUpdatedIdBy","creationDate","createdBy","createdIdBy","themeAuditName","themeAuditId"},
					null,this.getFilterIds(),"sortNum",topThemeBankTerm); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "list";
	}
	
	public String getThemeBankCodePy(){
		try {
			String tmpCode = PinYin2Abbreviation.cn2py(themeBankName);
			if(tmpCode==null || "".equals(tmpCode) || "null".equals(tmpCode)){
				themeBankCode = "";
			}else{
				themeBankCode = CharsetSwitch.CharacterUpperCase(tmpCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
			themeBankCode = "";
		}
		return "themeBankCodePy";
	}
	
	
	public String getNewThemeBankCode(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			themeBankCode = "";
			return "themeBankCodePy";
	 	}
		try {
			StandardTerms standardTerms = (StandardTerms)this.getService().findDataByKey(this.getStandardId(), StandardTerms.class);
			Organ organ = (Organ)this.getService().findDataByKey(usersess.getCurrentOrganId(), Organ.class);
			if(standardTerms != null){
				//System.out.println(standardTerms.getStandardTypes().getParentSpeciltype().getTypename());
				//System.out.println(standardTerms.getStandardTypes().getTypename());
				String bm1 = standardTerms.getStandardTypes().getParentSpeciltype().getBankMapCode();
				String bm2 = standardTerms.getStandardTypes().getBankMapCode();
				String bm3 = standardTerms.getBankStandMapCode();
				String bm4 = organ.getBankMapCode();
				if(bm1 == null || "null".equals(bm1)){
					bm1 = "";
				}
				if(bm1!=null && bm2!=null && bm3!=null && bm4!=null){
					themeBankCode = bm1+bm2+bm3+bm4;
					Map term = new HashMap();
					term.put("themeBankCode", themeBankCode);
					List<ThemeBank> ls = this.getService().queryData("queryBankInThemeBankCodeHql", term, null);
					if(ls!=null && ls.size()>0){
						ls = this.getService().queryData("queryMaxCodeInThemeBankCodeHql", term, null);
						if(ls==null || ls.size()==0){
							themeBankCode=themeBankCode+"01";
						}else{
							String oldCode = ls.get(0).getThemeBankCode();
							int ind = Integer.parseInt(oldCode.substring(themeBankCode.length(), oldCode.length()));
							ind++;
							themeBankCode=themeBankCode+(ind>9?ind:"0"+ind);
						}
					}
				}
			}
			if(themeBankCode == null){
				String bm4 = organ.getBankMapCode();
				String nowDay = DateUtils.convertDateToStr(new Date(),"yyyyMMdd");
				Map term = new HashMap();
				term.put("themeBankCode", nowDay);
				List<ThemeBank> ls = this.getService().queryData("queryMaxCodeInThemeBankCodeHql", term, null);
				if(ls!=null && ls.size()>0){
					String oldCode = ls.get(0).getThemeBankCode();
					int ind = Integer.parseInt(oldCode.substring(nowDay.length(), nowDay.length()+2));
					ind++;
					themeBankCode = nowDay+(ind>9?ind:"0"+ind)+bm4;
				}else{
					themeBankCode = nowDay+"01"+bm4;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			themeBankCode = "";
		}
		return "themeBankCodePy";
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 查询是否编码有重复
	 * @return
	 * @modified
	 */
	public String querylistInThemeBankCode(){
		try{
			Map param = new HashMap();
			Map sortMap = new HashMap();
			param.put("id", this.getId());
			param.put("themeBankCode", themeBankCode);
			List<ThemeBank> exampojolist = this.getService().queryData("listInThemeBankCodeHql", param , sortMap);
			if(exampojolist!=null && exampojolist.size()>0){
				isExist = "1";
			}else{
				isExist = "0";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "isExistCode";
	}
	
	
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getValidStr() {
		return validStr;
	}
	public void setValidStr(String validStr) {
		this.validStr = validStr;
	}
	public List<ThemeBank> getThemeBanks() {
		return themeBanks;
	}
	public void setThemeBanks(List<ThemeBank> themeBanks) {
		this.themeBanks = themeBanks;
	}
	public String getTopThemeBankTerm() {
		return topThemeBankTerm;
	}
	public void setTopThemeBankTerm(String topThemeBankTerm) {
		this.topThemeBankTerm = topThemeBankTerm;
	}

	public String getThemeBankName() {
		return themeBankName;
	}

	public void setThemeBankName(String themeBankName) {
		this.themeBankName = themeBankName;
	}

	public String getThemeBankCode() {
		return themeBankCode;
	}

	public void setThemeBankCode(String themeBankCode) {
		this.themeBankCode = themeBankCode;
	}

	public String getIsExist() {
		return isExist;
	}

	public void setIsExist(String isExist) {
		this.isExist = isExist;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public File getXls() {
		return xls;
	}

	public void setXls(File xls) {
		this.xls = xls;
	}

	public String getNotHaveIds() {
		return notHaveIds;
	}

	public void setNotHaveIds(String notHaveIds) {
		this.notHaveIds = notHaveIds;
	}

	public String getNotZero() {
		return notZero;
	}

	public void setNotZero(String notZero) {
		this.notZero = notZero;
	}

	public List getThemeBankList() {
		return themeBankList;
	}

	public void setThemeBankList(List themeBankList) {
		this.themeBankList = themeBankList;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getOrganIdTerm() {
		return organIdTerm;
	}

	public void setOrganIdTerm(String organIdTerm) {
		this.organIdTerm = organIdTerm;
	}

	public String getStandardId() {
		return standardId;
	}

	public void setStandardId(String standardId) {
		this.standardId = standardId;
	}

	public String getCreateOrganIdTerm() {
		return createOrganIdTerm;
	}

	public void setCreateOrganIdTerm(String createOrganIdTerm) {
		this.createOrganIdTerm = createOrganIdTerm;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getShowProTerm() {
		return showProTerm;
	}

	public void setShowProTerm(String showProTerm) {
		this.showProTerm = showProTerm;
	}
	
	
	
}
