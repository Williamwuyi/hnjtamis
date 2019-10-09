package cn.com.ite.hnjtamis.personal.mainEx;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.baseSignIn.BaseSignInService;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.base.theme.ThemeService;
import cn.com.ite.hnjtamis.exam.base.themebank.service.ThemeBankService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.QuarterStandard;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.terms.StandardTermsService;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;
import cn.com.ite.hnjtamis.jobstandard.termsEx.form.EmployeeLearningForm;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.personal.mainEx.MainListAction</p>
 * <p>Description 首页查询</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月2日 下午1:23:54
 * @version 1.0
 * 
 * @modified records:
 */
public class MainExListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = -5715279641217626567L;
	
	private HttpServletRequest request;
	
	//private List<ThemeBank> unFinExamList;
	
	//private List<ThemeBank> finExamList;
	
	//private List<ThemeBank> exeExamList;
	
	private String usersessTheme;
	
	private String standardnameTerm;
	
	//private Map<String,ThemeBank> indexTkMap = new HashMap<String,ThemeBank>();
	
	private Map<String,String> moniExamScoreMap = new HashMap<String,String>();
	
	private EmployeeLearningForm employeeLearningForm;
	
	private String qtc;
	
	private String qtcName;
	
	private int standardCount;
	
	private int signIncount;
	
	private List<StandardTerms> typelist;
	
	private Map<String,List<StandardTerms>> typechildmap;
	
	private List<StandardTerms> standardTermslist = new ArrayList<StandardTerms>();
	
	private List<ThemeBank> proBanklist = new ArrayList<ThemeBank>();
	
	private StandardTerms nowStandardTerms;

	private String treeOpenNode;
	
	private String qtype;

	public String list()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMainEx";
	 	}
		
		treeOpenNode = (String)StaticVariable.treeNodeMap.get(usersess.getAccount());
		if(treeOpenNode == null){
			treeOpenNode = UserDefineOption.getTreeNode(request, usersess.getAccount());
		}
		if(treeOpenNode == null){
			treeOpenNode = "";
		}
		String nowthemeBankId = (String)StaticVariable.nowThemeBankInIndexMap.get(usersess.getAccount());
		if(nowthemeBankId == null){
			nowthemeBankId = UserDefineOption.getNowThemeBank(request, usersess.getAccount());
		}
		if(nowthemeBankId == null){
			nowthemeBankId = "";
		}
		
		usersessTheme = usersess.getTheme();
		typelist = new ArrayList<StandardTerms>();
		typechildmap= new HashMap<String,List<StandardTerms>>();
		//unFinExamList = new ArrayList<ThemeBank>();
		//finExamList = new ArrayList<ThemeBank>();
		//exeExamList = new ArrayList<ThemeBank>();
		employeeLearningForm = new EmployeeLearningForm();
		qtc = null;
		try{
		StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
		StandardTermsService standardtermsServer = (StandardTermsService)SpringContextUtil.getBean("standardtermsServer");
		ThemeService themeService = (ThemeService)SpringContextUtil.getBean("themeService");
		double employeeXxb = themeService.getEmployeeXxb(usersess.getEmployeeId());
		request.setAttribute("employeeXxb", employeeXxb);
		
		Quarter quarter =  null;
		if(usersess!=null && usersess.getQuarterId()!=null){
			quarter= (Quarter)this.getService().findDataByKey(usersess.getQuarterId(), Quarter.class);
		}
		Map idsExsitsMap = new HashMap();
		typelist = standardTermsExService.getStandardTopTypeList();//获取顶级节点，如果下级没有数据也显示
		for (StandardTerms standardTerms : typelist) {
			standardTerms.setChildeNums(0);
			StaticVariable.banksNameMap.put(standardTerms.getTypeId()+"@mk",standardTerms.getTypename());
			ThemeBank themeBank = new ThemeBank();
			themeBank.setThemeNum(0);
			themeBank.setFinThemeNum(0);
			standardTerms.setThemeBank(themeBank);
			idsExsitsMap.put(standardTerms.getTypeId(),standardTerms);
		}
		Map<String,String> indexTkThemeSuccMap = new HashMap<String,String>();
		if(usersess.getEmployeeId()!=null){
			indexTkThemeSuccMap = standardtermsServer.getThemeNumInBank(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
		}
		if(usersess.getEmployeeId()!=null && quarter!=null && quarter.getQuarterTrainCode()!=null){
			String quarterTrainCode = quarter.getQuarterTrainCode();//"FD0401";//
			qtc = quarterTrainCode;

			List<ThemeBank> indexTkList = standardtermsServer.queryIndexTk(quarterTrainCode,usersess.getCurrentOrganId());
			moniExamScoreMap = standardtermsServer.getMoniExamScore(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
			//Map<String,String> gwpxExamScore = standardtermsServer.getGwpxExamScore(usersess.getEmployeeId(), StaticVariable.EXAM_EXAM_PROPERTY, null, null);
			Map<String,ThemeBank> themeBankMap = new HashMap<String,ThemeBank>();
			for (ThemeBank themeBank : indexTkList) {
				//设置完成比例
				String themeNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId());
				String finNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId()+"_FINNUM");
				themeBank.setSuccThemeNum(0.0);
				themeBank.setThemeNum(themeNum!=null && !"".equals(themeNum) ? Integer.parseInt(themeNum) : 0);
				
				employeeLearningForm.setThemenum(themeBank.getThemeNum()+employeeLearningForm.getThemenum());
				employeeLearningForm.setFinthemenum((finNum!=null ? Integer.parseInt(finNum) : 0)+employeeLearningForm.getFinthemenum());
				if(themeNum!=null && finNum!=null && Integer.parseInt(finNum)>0 && Integer.parseInt(themeNum)>0){
					themeBank.setFinThemeNum(Integer.parseInt(finNum));
					double v = (Double.parseDouble(finNum)*1.0)/(Double.parseDouble(themeNum)*1.0)*100.0;
					if(v>100){
						v = 100.0;
					}else if(v<0){
						v = 0.0;
					}
					themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(v,2)));
					if(v==100){
						//finExamList.add(themeBank);
					}else if(v==0){
						//unFinExamList.add(themeBank);
					}else{
						//exeExamList.add(themeBank);
					}
				}else{
					//unFinExamList.add(themeBank);
					themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(0.0,2)));
				}
				themeBankMap.put(themeBank.getThemeBankId(), themeBank);
				//indexTkMap.put(themeBank.getThemeBankId(), themeBank);
			}
			double v = employeeLearningForm.getFinthemenum()>0 ?(employeeLearningForm.getFinthemenum()*1.0)/(employeeLearningForm.getThemenum()*1.0)*100.0 : 0.0;
			if(v>100){
				v = 100.0;
			}else if(v<0){
				v = 0.0;
			}
			employeeLearningForm.setFinthemebfl(Double.parseDouble(NumericUtils.roundToString(v,2)));
			/*if(unFinExamList.size()%2!=0){
				unFinExamList.add(new ThemeBank());
			}*/
			
			
			List<StandardTerms> standardTermslist = standardTermsExService.queryStandardTermsByQuarterId(quarterTrainCode,new HashMap());
			List<StandardTerms> new_standardTermslist = new ArrayList<StandardTerms>();
			Map<String,String> standardTermsBankIds= new HashMap<String,String>();
			for (StandardTerms standardTerms : standardTermslist) {
				if(standardTerms.getJobsStandardThemebanks() == null || standardTerms.getJobsStandardThemebanks().size()==0){
					continue;
				}
				for(int j=0;j<standardTerms.getJobsStandardThemebanks().size();j++){
					ThemeBank themeBank = themeBankMap.get(standardTerms.getJobsStandardThemebanks().get(j).getThemeBankId());
					if(themeBank!=null){
						if(standardTermsBankIds.get(themeBank.getThemeBankId())!=null){
							continue;
						}
						standardTermsBankIds.put(themeBank.getThemeBankId(),"aaa");
						
						
						StandardTerms newStandardTerms = new StandardTerms();
						BeanUtils.copyProperties(standardTerms, newStandardTerms);
						newStandardTerms.setStandardid(standardTerms.getStandardid());
						
						StaticVariable.banksNameMap.put(themeBank.getThemeBankId(),themeBank.getThemeBankName());
						newStandardTerms.setThemeBank(themeBank);
						new_standardTermslist.add(newStandardTerms);
						
						if(themeBank.getThemeBankId().equals(nowthemeBankId)){
							nowStandardTerms = newStandardTerms;
						}
					}
				}
			}
			standardTermslist = new_standardTermslist;
			
			
			for (StandardTerms standardTerms : standardTermslist) {
				if("finExam".equals(qtype) && standardTerms.getThemeBank().getSuccThemeNum()<100){
					continue;
				}
				if("exeExam".equals(qtype) && standardTerms.getThemeBank().getSuccThemeNum()==100){
					continue;
				}
				
				StandardTerms pst = (StandardTerms)idsExsitsMap.get(standardTerms.getParentTypeId());
				if(pst == null){
					pst = new StandardTerms();
					pst.setTypeId(standardTerms.getParentTypeId());
					pst.setTypename(standardTerms.getParentTypeName());
					pst.setChildeNums(0);
					typelist.add(pst);
					idsExsitsMap.put(standardTerms.getParentTypeId(),pst);
					
					ThemeBank themeBank = new ThemeBank();
					themeBank.setThemeNum(0);
					themeBank.setFinThemeNum(0);
					pst.setThemeBank(themeBank);
				}
				if(pst.getThemeBank().getThemeBankId() == null){
					pst.getThemeBank().setThemeBankId(standardTerms.getThemeBank().getThemeBankId());
				}else{
					pst.getThemeBank().setThemeBankId(pst.getThemeBank().getThemeBankId()+","+standardTerms.getThemeBank().getThemeBankId());
				}
				StaticVariable.moniCsBanksMap.put(usersess.getAccount()+"@"+pst.getTypeId(),pst.getThemeBank().getThemeBankId());
				
				StandardTerms sst = (StandardTerms)idsExsitsMap.get(standardTerms.getTypeId());
				if(sst== null){
					sst = new StandardTerms();
					sst.setTypeId(standardTerms.getTypeId());
					sst.setTypename(standardTerms.getTypename());
					
					List<StandardTerms> childList = typechildmap.get(standardTerms.getParentTypeId());
					if(childList == null){
						childList = new ArrayList();
					}
					childList.add(sst);
					typechildmap.put(standardTerms.getParentTypeId(),childList);
					idsExsitsMap.put(standardTerms.getTypeId(),sst);
					
					ThemeBank themeBank = new ThemeBank();
					themeBank.setThemeNum(0);
					themeBank.setFinThemeNum(0);
					sst.setThemeBank(themeBank);
					
					pst.setChildeNums(pst.getChildeNums()+1);
				}
				
				List<StandardTerms> childList = typechildmap.get(standardTerms.getTypeId());
				if(childList == null){
					childList = new ArrayList();
				}
				childList.add(standardTerms);
				typechildmap.put(standardTerms.getTypeId(),childList);
			}
			for (StandardTerms standardTerms : standardTermslist) {
				StandardTerms pst = (StandardTerms)idsExsitsMap.get(standardTerms.getParentTypeId());
				if(pst!=null){
					pst.getThemeBank().setThemeNum(pst.getThemeBank().getThemeNum()+standardTerms.getThemeBank().getThemeNum());
					pst.getThemeBank().setFinThemeNum(pst.getThemeBank().getFinThemeNum()+standardTerms.getThemeBank().getFinThemeNum());
					int finNum=pst.getThemeBank().getFinThemeNum();
					int themeNum=pst.getThemeBank().getThemeNum();
					double vv = themeNum>0?(finNum*1.0)/(themeNum*1.0)*100.0:0.0d;
					if(vv>100){
						vv = 100.0;
					}else if(vv<0){
						vv = 0.0;
					}
					pst.getThemeBank().setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(vv,2)));
				}
				
				StandardTerms sst = (StandardTerms)idsExsitsMap.get(standardTerms.getTypeId());
				if(sst!=null){
					sst.getThemeBank().setThemeNum(sst.getThemeBank().getThemeNum()+standardTerms.getThemeBank().getThemeNum());
					sst.getThemeBank().setFinThemeNum(sst.getThemeBank().getFinThemeNum()+standardTerms.getThemeBank().getFinThemeNum());
					int finNum=sst.getThemeBank().getFinThemeNum();
					int themeNum=sst.getThemeBank().getThemeNum();
					double vv = themeNum>0?(finNum*1.0)/(themeNum*1.0)*100.0:0.0d;
					if(vv>100){
						vv = 100.0;
					}else if(vv<0){
						vv = 0.0;
					}
					sst.getThemeBank().setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(vv,2)));
				}
			}
		}
		
			/*	//查询专业题库
				ThemeBankService themeBankService = (ThemeBankService)SpringContextUtil.getBean("themeBankServer");
				proBanklist = themeBankService.getProfessionTopThemeBanks();
				for(int i=0;i<proBanklist.size();i++){
					ThemeBank themeBank = proBanklist.get(i);
					themeBank.setThemeNum(0);
					themeBank.setFinThemeNum(0);
					themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(0.0d,2)));
					themeBank.setBankLevelCode("");
					for(int k=0;k<themeBank.getThemeBanks().size();k++){
						ThemeBank chlidThemeBank = themeBank.getThemeBanks().get(k);
						//设置子题库
						String _themeNum = indexTkThemeSuccMap.get(chlidThemeBank.getThemeBankId());
						String _finNum = indexTkThemeSuccMap.get(chlidThemeBank.getThemeBankId()+"_FINNUM");
						chlidThemeBank.setThemeNum(_themeNum!=null && !"".equals(_themeNum) ? Integer.parseInt(_themeNum) : 0);
						chlidThemeBank.setFinThemeNum(_finNum!=null && !"".equals(_finNum) ? Integer.parseInt(_finNum) : 0);
						int finNum=chlidThemeBank.getFinThemeNum();
						int themeNum=chlidThemeBank.getThemeNum();
						double vv = themeNum>0?(finNum*1.0)/(themeNum*1.0)*100.0:0.0d;
						if(vv>100){
							vv = 100.0;
						}else if(vv<0){
							vv = 0.0;
						}
						chlidThemeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(vv,2)));
						
						if(k==0){
							themeBank.setBankLevelCode(chlidThemeBank.getThemeBankId());
						}else{
							themeBank.setBankLevelCode(chlidThemeBank.getThemeBankId()+","+themeBank.getBankLevelCode());
						}
						
						
						//合计到父题库
						themeBank.setThemeNum(themeBank.getThemeNum()+chlidThemeBank.getThemeNum());
						themeBank.setFinThemeNum(themeBank.getFinThemeNum()+chlidThemeBank.getFinThemeNum());
						finNum=themeBank.getFinThemeNum();
						themeNum=themeBank.getThemeNum();
						vv = themeNum>0?(finNum*1.0)/(themeNum*1.0)*100.0:0.0d;
						if(vv>100){
							vv = 100.0;
						}else if(vv<0){
							vv = 0.0;
						}
						themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(vv,2)));
					}
					
					StaticVariable.banksNameMap.put(themeBank.getThemeBankId()+"@mk",themeBank.getThemeBankName());
					StaticVariable.moniCsBanksMap.put(usersess.getAccount()+"@"+themeBank.getThemeBankId(),
							themeBank.getBankLevelCode());
				}*/
		}catch(Exception e){
			e.printStackTrace();
		}
		return "showExList";
	}
	
	
	public String stlist()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMainEx";
	 	}
		StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
		List quarterStandardlist = this.getService().queryAllDate(QuarterStandard.class);
		if(quarterStandardlist!=null){
			for(int i=0;i<quarterStandardlist.size();i++){
				QuarterStandard quarterStandard = (QuarterStandard)quarterStandardlist.get(i);
				if(quarterStandard.getQuarterCode()!=null && quarterStandard.getQuarterCode().equals(qtc)){
					qtcName = quarterStandard.getQuarterName();
					qtc = quarterStandard.getQuarterCode();
					break;
				}
			}
		}
		
		standardTermslist = new ArrayList<StandardTerms>();
		Map termMap = new HashMap();
		if(standardnameTerm!=null && !"".equals(standardnameTerm) && !"null".equals(standardnameTerm)){
			termMap.put("standardnameTerm", standardnameTerm);
		}
		standardTermslist = standardTermsExService.queryStandardTermsByQuarterId(qtc,termMap);
		standardCount = standardTermslist!=null ? standardTermslist.size() : 0;
		
		BaseSignInService baseSignInService = (BaseSignInService)SpringContextUtil.getBean("baseSignInService");
		request.setAttribute("signIncount", baseSignInService.getSignInCount(
				usersess.getEmployeeId() == null ? usersess.getAccount() : usersess.getEmployeeId(), usersess.getAccount()));
		request.setAttribute("qtype", qtype);
		return "showStandardList";
	}
	
	
	public String saveTreeNode(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}else{
	 		StaticVariable.treeNodeMap.put(usersess.getAccount(), this.getId());
	 		UserDefineOption.saveTreeNode(request, usersess.getAccount(),  this.getId());
	 	}
		return "save";
	}

	public String getQtcName() {
		return qtcName;
	}


	public void setQtcName(String qtcName) {
		this.qtcName = qtcName;
	}


	/*public List<ThemeBank> getUnFinExamList() {
		return unFinExamList;
	}

	public void setUnFinExamList(List<ThemeBank> unFinExamList) {
		this.unFinExamList = unFinExamList;
	}

	public List<ThemeBank> getFinExamList() {
		return finExamList;
	}

	public void setFinExamList(List<ThemeBank> finExamList) {
		this.finExamList = finExamList;
	}

	public List<ThemeBank> getExeExamList() {
		return exeExamList;
	}

	public void setExeExamList(List<ThemeBank> exeExamList) {
		this.exeExamList = exeExamList;
	}*/

	public EmployeeLearningForm getEmployeeLearningForm() {
		return employeeLearningForm;
	}

	public void setEmployeeLearningForm(EmployeeLearningForm employeeLearningForm) {
		this.employeeLearningForm = employeeLearningForm;
	}

	public Map<String, String> getMoniExamScoreMap() {
		return moniExamScoreMap;
	}

	public void setMoniExamScoreMap(Map<String, String> moniExamScoreMap) {
		this.moniExamScoreMap = moniExamScoreMap;
	}

	public String getUsersessTheme() {
		return usersessTheme;
	}

	public void setUsersessTheme(String usersessTheme) {
		this.usersessTheme = usersessTheme;
	}


	public List<StandardTerms> getStandardTermslist() {
		return standardTermslist;
	}


	public void setStandardTermslist(List<StandardTerms> standardTermslist) {
		this.standardTermslist = standardTermslist;
	}


	public String getQtc() {
		return qtc;
	}


	public void setQtc(String qtc) {
		this.qtc = qtc;
	}


	public String getStandardnameTerm() {
		return standardnameTerm;
	}


	public void setStandardnameTerm(String standardnameTerm) {
		this.standardnameTerm = standardnameTerm;
	}


	public int getStandardCount() {
		return standardCount;
	}


	public void setStandardCount(int standardCount) {
		this.standardCount = standardCount;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}


	public int getSignIncount() {
		return signIncount;
	}


	public void setSignIncount(int signIncount) {
		this.signIncount = signIncount;
	}


	public List<StandardTerms> getTypelist() {
		return typelist;
	}


	public void setTypelist(List<StandardTerms> typelist) {
		this.typelist = typelist;
	}


	public Map<String, List<StandardTerms>> getTypechildmap() {
		return typechildmap;
	}


	public void setTypechildmap(Map<String, List<StandardTerms>> typechildmap) {
		this.typechildmap = typechildmap;
	}


	public String getTreeOpenNode() {
		return treeOpenNode;
	}


	public void setTreeOpenNode(String treeOpenNode) {
		this.treeOpenNode = treeOpenNode;
	}


	public StandardTerms getNowStandardTerms() {
		return nowStandardTerms;
	}


	public void setNowStandardTerms(StandardTerms nowStandardTerms) {
		this.nowStandardTerms = nowStandardTerms;
	}


	public String getQtype() {
		return qtype;
	}


	public void setQtype(String qtype) {
		this.qtype = qtype;
	}


	public List<ThemeBank> getProBanklist() {
		return proBanklist;
	}


	public void setProBanklist(List<ThemeBank> proBanklist) {
		this.proBanklist = proBanklist;
	}


	
	
	
}
