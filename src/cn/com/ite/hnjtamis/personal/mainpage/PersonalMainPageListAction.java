package cn.com.ite.hnjtamis.personal.mainpage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.common.utils.NumericUtils;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.domain.organization.Quarter;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.document.DocumentLibService;
import cn.com.ite.hnjtamis.document.domain.DocumentLib;
import cn.com.ite.hnjtamis.exam.base.theme.ThemeService;
import cn.com.ite.hnjtamis.exam.base.themebank.service.ThemeBankService;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamPublic;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.jobstandard.domain.StandardTerms;
import cn.com.ite.hnjtamis.jobstandard.terms.StandardTermsService;
import cn.com.ite.hnjtamis.jobstandard.termsEx.StandardTermsExService;
import cn.com.ite.hnjtamis.jobstandard.termsEx.form.EmployeeLearningForm;
import cn.com.ite.hnjtamis.kb.domain.Courseware;
import cn.com.ite.hnjtamis.personal.domain.PersonalRateProgress;
import cn.com.ite.hnjtamis.personal.mainEx.UserDefineOption;
import cn.com.ite.hnjtamis.train.domain.TrainImplement;
/**
 * 
 * <p>Title 岗位达标培训信息系统-个人学习管理模块</p>
 * <p>Description 人个显示首页Action
 * 包括学习进度、培训积分、达标情况、考试信息，报名信息，达标过期信息等与个人相关的内容，
 * 还包括本岗位最新的教材、资料、考试通知提醒等
 * </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Apr 15, 2015  10:45:27 AM
 * @version 1.0
 * 
 * @modified records:
 */
public class PersonalMainPageListAction extends AbstractListAction implements ServletRequestAware{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8061559691203248709L;
	private StandardTermsService standardtermsServer;
	private HttpServletRequest request;
	/**
	 * 查询结果
	 */
	private List<PersonalRateProgress> list = new ArrayList<PersonalRateProgress>();
	private List<PersonalRateProgressEx> personalrateprogresslist=new ArrayList<PersonalRateProgressEx>();
	private List<ExamPublic> exampubliclist=new ArrayList<ExamPublic>();
	private List<ExamUserTestpaper> examusertestpaperlist= new ArrayList<ExamUserTestpaper>();
	private List<TrainImplement> trainimplementlist = new ArrayList<TrainImplement>();
	private List<Courseware> coursewarelist = new ArrayList<Courseware>();
	
	private ArrayList<String> contentList = new ArrayList<String>();
	private ArrayList<String> referenceBookList = new ArrayList<String>();
	private ArrayList<String> standardnameList = new ArrayList<String>();
	private List<ThemeBank> indexTkList = new ArrayList<ThemeBank>();
	private Map<String,ThemeBank> indexTkMap = new HashMap<String,ThemeBank>();
	private Map<String,String> indexTkThemeSuccMap = new HashMap<String,String>();
	private Map<String,String> moniExamScoreMap = new HashMap<String,String>();
	private Map<String,String> gwpxExamScore = new HashMap<String,String>();
	
	// 查询条件
	private String toptypeidTerm;
	private String startTimeTerm="";  //
	private String endTimeTerm="";
	private String contentsTerm;
	private String nameTerm="";  //
	private String personcodeTerm="";
	
	
	private List<StandardTerms> standardTermslist = new ArrayList<StandardTerms>();
	private EmployeeLearningForm employeeLearningForm;
	private List<DocumentLib> documentlist = new ArrayList<DocumentLib>();
	private List<ThemeBank> proBanklist = new ArrayList<ThemeBank>();
	
	public String getPersoncodeTerm() {
		return personcodeTerm;
	}
	public void setPersoncodeTerm(String personcodeTerm) {
		this.personcodeTerm = personcodeTerm;
	}
	public String getNameTerm() {
		return nameTerm;
	}
	public void setNameTerm(String nameTerm) {
		this.nameTerm = nameTerm;
	}
	 
	
	public String getStartTimeTerm() {
		return startTimeTerm;
	}
	public void setStartTimeTerm(String startTimeTerm) {
		this.startTimeTerm = startTimeTerm;
	}
	public String getEndTimeTerm() {
		return endTimeTerm;
	}
	public void setEndTimeTerm(String endTimeTerm) {
		this.endTimeTerm = endTimeTerm;
	}
	public String getContentsTerm() {
		return contentsTerm;
	}
	public void setContentsTerm(String contentsTerm) {
		this.contentsTerm = contentsTerm;
	}
	
	private int standardRownumInPage = 5;//每页显示的记录数目
	
	private int tkRownumInPage = 5;//每页显示的记录数目
	
	private int standardPagTotal = 0;//标准条款的页数
	
	private int tkPagTotal = 0;//题库的页数
	
	private int standardTotal;//总页数
	
	private int tkTotal;//总页数
	
	private int p;
	
	private String qtc;//quarterTrainCode
	
	private List<StandardTerms> typelist;
	
	private Map<String,List<StandardTerms>> typechildmap;
	
	private StandardTerms nowStandardTerms;
	
	private String treeOpenNode;
	
	public String list()throws Exception{
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMain";
	 	}
		standardTermslist = new ArrayList<StandardTerms>();
		employeeLearningForm = new EmployeeLearningForm();
		documentlist =new ArrayList();

		typelist = new ArrayList<StandardTerms>();
		typechildmap= new HashMap<String,List<StandardTerms>>();
		//unFinExamList = new ArrayList<ThemeBank>();
		//finExamList = new ArrayList<ThemeBank>();
		//exeExamList = new ArrayList<ThemeBank>();
		employeeLearningForm = new EmployeeLearningForm();
		
		StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
		//查询个人的岗位达标情况
		if(usersess.getEmployeeId()!=null){
			employeeLearningForm = standardTermsExService.getEmployeeStandardLearning(usersess.getEmployeeId());
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
		/*if(quarter!=null && quarter.getQuarterTrainCode()!=null){
			String quarterTrainCode = quarter.getQuarterTrainCode();//"FD0401";//
			qtc = quarterTrainCode;
			standardTermslist = standardTermsExService.queryStandardTermsByQuarterId(quarterTrainCode,new HashMap());
			standardTotal = standardTermslist.size();
			
			
			
			standardPagTotal = ((new BigDecimal(standardTermslist.size()/standardRownumInPage)).setScale(0,BigDecimal.ROUND_DOWN)).intValue();
			
			standardPagTotal++;
	
			
			List tmplist1 = new ArrayList();
			for(int i=0;i<standardTermslist.size() && i<standardRownumInPage;i++){
				tmplist1.add(standardTermslist.get(i));
			}
			standardTermslist = tmplist1;

		}*/
		/*if(usersess.getEmployeeId()!=null){
			indexTkThemeSuccMap = standardtermsServer.getThemeNumInBank(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
			moniExamScoreMap = standardtermsServer.getMoniExamScore(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
			gwpxExamScore = standardtermsServer.getGwpxExamScore(usersess.getEmployeeId(), StaticVariable.EXAM_EXAM_PROPERTY, null, null);
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
				}
				indexTkMap.put(themeBank.getThemeBankId(), themeBank);
			}
			double v = employeeLearningForm.getFinthemenum()>0 ?(employeeLearningForm.getFinthemenum()*1.0)/(employeeLearningForm.getThemenum()*1.0)*100.0 : 0.0;
			if(v>100){
				v = 100.0;
			}else if(v<0){
				v = 0.0;
			}
			employeeLearningForm.setFinthemebfl(Double.parseDouble(NumericUtils.roundToString(v,2)));
		}*/
		try{
		ThemeService themeService = (ThemeService)SpringContextUtil.getBean("themeService");
		double employeeXxb = themeService.getEmployeeXxb(usersess.getEmployeeId());
		request.setAttribute("employeeXxb", employeeXxb);
		
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
				
				try{
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
				}catch(Exception e){
					e.printStackTrace();
				}
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
			
			
			List<StandardTerms> standardTermsAlllist = standardTermsExService.queryStandardTermsByQuarterId(quarterTrainCode,new HashMap());
			List<StandardTerms> new_standardTermsAlllist = new ArrayList<StandardTerms>();
			Map<String,String> standardTermsBankIds= new HashMap<String,String>();
			for (StandardTerms standardTerms : standardTermsAlllist) {
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
						new_standardTermsAlllist.add(newStandardTerms);
						
						if(themeBank.getThemeBankId().equals(nowthemeBankId)){
							nowStandardTerms = newStandardTerms;
						}
					}
				}
			}
			standardTermsAlllist = new_standardTermsAlllist;
			
			
			for (StandardTerms standardTerms : standardTermsAlllist) {
				/*if("finExam".equals(qtype) && standardTerms.getThemeBank().getSuccThemeNum()<100){
					continue;
				}
				if("exeExam".equals(qtype) && standardTerms.getThemeBank().getSuccThemeNum()==100){
					continue;
				}
				*/
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
			for (StandardTerms standardTerms : standardTermsAlllist) {
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
		
		//查询专业题库
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
		}
		
		
		if(quarter!=null && quarter.getQuarterTrainCode()!=null){
			DocumentLibService documentLibService = (DocumentLibService)SpringContextUtil.getBean("documentLibService");
			Map term = new HashMap();
			term.put("stateTerm", "20");
			term.put("quarterTrainCode", quarter.getQuarterTrainCode());
			documentlist = documentLibService.queryData("getDocumentLibListByQuarterTrainCode", term, new HashMap());
			if(documentlist!=null && documentlist.size()>0){
				Map accessoryMap = documentLibService.getAccessoryIdInDocument();
				
				Map favoriteMap = documentLibService.getFavoriteInDocument(
						usersess.getEmployeeId()!=null ? usersess.getEmployeeId() : usersess.getAccount(), 
						usersess.getAccount());
				
				List newdocumentlist1 = new ArrayList();
				List newdocumentlist2 = new ArrayList();
				for(int i=0;i<documentlist.size();i++){
					DocumentLib documentLib = documentlist.get(i);
					String accId = (String)accessoryMap.get(documentLib.getDocumentId());
					documentLib.setAfficheId(accId);
					if(favoriteMap.get(documentLib.getDocumentId())!=null){
						documentLib.setTofavorite("true");
					}else{
						documentLib.setTofavorite("false");
					}
					if("true".equals(documentLib.getTofavorite())){
						newdocumentlist1.add(documentLib);
					}else{
						newdocumentlist2.add(documentLib);
					}
					
				}
				newdocumentlist1.addAll(newdocumentlist2);
				documentlist = new ArrayList();
				for(int i=0;i<newdocumentlist1.size() && i<8;i++){
					documentlist.add((DocumentLib)newdocumentlist1.get(i));
				}
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "showMain";
	}
	
	
	public String getStandardInPageNum(){
		StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
		standardTermslist = new ArrayList<StandardTerms>();
		standardTermslist = standardTermsExService.queryStandardTermsByQuarterId(qtc,new HashMap());
		
		List tmplist1 = new ArrayList();
		int st = (p-1)*standardRownumInPage;
		int end = st+standardRownumInPage;
		for(int i=st;i<standardTermslist.size() && i<end;i++){
			tmplist1.add(standardTermslist.get(i));
		}
		standardTermslist = tmplist1;
		return "standardInPageNum";
	}
	
	
	public String getTkInPageNum(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "showMain";
	 	}
		
		StandardTermsExService standardTermsExService = (StandardTermsExService)SpringContextUtil.getBean("standardtermsExServer");
		indexTkList = new ArrayList<ThemeBank>();
		indexTkList = standardtermsServer.queryIndexTk(qtc,usersess.getCurrentOrganId());
		
		indexTkThemeSuccMap = standardtermsServer.getThemeNumInBank(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
		moniExamScoreMap = standardtermsServer.getMoniExamScore(usersess.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
		gwpxExamScore = standardtermsServer.getGwpxExamScore(usersess.getEmployeeId(), StaticVariable.EXAM_EXAM_PROPERTY, null, null);
		
		
		List tmplist1 = new ArrayList();
		int st = (p-1)*standardRownumInPage;
		int end = st+standardRownumInPage;
		for(int i=st;i<indexTkList.size() && i<end;i++){
			tmplist1.add(indexTkList.get(i));
		}
		indexTkList = tmplist1;
		for (ThemeBank themeBank : indexTkList) {
			//设置完成比例
			String themeNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId());
			String finNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId()+"_FINNUM");
			themeBank.setSuccThemeNum(0.0);
			themeBank.setThemeNum(themeNum!=null && !"".equals(themeNum) ? Integer.parseInt(themeNum) : 0);
			
			if(themeNum!=null && finNum!=null && Integer.parseInt(finNum)>0 && Integer.parseInt(themeNum)>0){
				themeBank.setFinThemeNum(Integer.parseInt(finNum));
				double v = (Double.parseDouble(finNum)*1.0)/(Double.parseDouble(themeNum)*1.0)*100.0;
				if(v>100){
					v = 100.0;
				}else if(v<0){
					v = 0.0;
				}
				themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(v,2)));
			}
			indexTkMap.put(themeBank.getThemeBankId(), themeBank);
			
			themeBank.setItemmoniScore(moniExamScoreMap.get(themeBank.getThemeBankId()));
		}
		
		return "indexTkListInPageNum";
	}
	
	
	/*public String list()throws Exception{
		PersonalMainPageService mservice=(PersonalMainPageService)service;
		UserSession us = LoginAction.getUserSessionInfo();
		setPersoncodeTerm(us.getEmployeeId());
		String quarterId =us.getQuarterId();  /// 岗位ID
//		if(!StringUtils.isEmpty(this.getValidStr()))
//			this.setValid(this.getValidStr().equals("1"));
		list = (List<PersonalRateProgress>)service.queryData("queryHql", this,null,this.getStart(),this.getLimit());
		//把线性结构转成树形结构
//		baseSpecialities = service.childObjectHandler(baseSpecialities, "bstid", "parentspeciltype", 
//				"baseSpecialities",new String[]{},null,this.getFilterIds(),"orderno",null);
		//// 个人岗位达标记录
		this.personalrateprogresslist = mservice.findPersonalRateProgressRec(personcodeTerm, "");
		/// 发布的对应岗位的考试信息
		this.exampubliclist = mservice.findExamPublic(quarterId);
		/// 考试结果信息
		this.examusertestpaperlist = mservice.findExamUserTestpapaer(personcodeTerm);
		/// 岗位安排课程
		this.trainimplementlist = mservice.findTrainImplement(quarterId);
		//// 查询有效课件信息 仅查询20条
		this.coursewarelist = mservice.findCourseware();
		///this.setTotal(service.countData("queryHql", this));
		HashMap<String,ArrayList> standAndRef = standardtermsServer.queryIndexStandAndRefer(us.getQuarterId());
		this.contentList = standAndRef.get("contents");
		this.referenceBookList = standAndRef.get("referenceBook");
		this.standardnameList = standAndRef.get("standardname");
		
		this.indexTkList = standardtermsServer.queryIndexTk(quarterId);
		indexTkThemeSuccMap = standardtermsServer.getThemeNumInBank(us.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
		moniExamScoreMap = standardtermsServer.getMoniExamScore(us.getEmployeeId(),StaticVariable.EXAM_TYPE_MONI);
		gwpxExamScore = standardtermsServer.getGwpxExamScore(us.getEmployeeId(), StaticVariable.EXAM_EXAM_PROPERTY, null, null);
		for (ThemeBank themeBank : indexTkList) {
			//设置完成比例
			String themeNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId());
			String finNum = indexTkThemeSuccMap.get(themeBank.getThemeBankId()+"_FINNUM");
			themeBank.setSuccThemeNum(0.0);
			themeBank.setThemeNum(themeNum!=null && !"".equals(themeNum) ? Integer.parseInt(themeNum) : 0);
			if(themeNum!=null && finNum!=null && Integer.parseInt(finNum)>0 && Integer.parseInt(themeNum)>0){
				themeBank.setFinThemeNum(Integer.parseInt(finNum));
				double v = (Double.parseDouble(finNum)*1.0)/(Double.parseDouble(themeNum)*1.0)*100.0;
				if(v>100){
					v = 100.0;
				}else if(v<0){
					v = 0.0;
				}
				themeBank.setSuccThemeNum(Double.parseDouble(NumericUtils.roundToString(v,2)));
			}
			indexTkMap.put(themeBank.getThemeBankId(), themeBank);
		}
		return "showList";
	}*/
	/**
	 * 删除
	 * @return
	 * @modified
	 */
	public String delete() throws Exception{
		// 
		///service.findDataByKey(key, type)
		service.deleteByKeys(this.getId().split(","), PersonalRateProgress.class);
		this.setMsg("信息删除成功！");
		return "delete";
	}
	
	 
	public List<PersonalRateProgress> getList() {
		return list;
	}
	public void setList(List<PersonalRateProgress> list) {
		this.list = list;
	}
	public String getToptypeidTerm() {
		return toptypeidTerm;
	}
	public void setToptypeidTerm(String toptypeidTerm) {
		this.toptypeidTerm = toptypeidTerm;
	}
	public List<PersonalRateProgressEx> getPersonalrateprogresslist() {
		return personalrateprogresslist;
	}
	public void setPersonalrateprogresslist(
			List<PersonalRateProgressEx> personalrateprogresslist) {
		this.personalrateprogresslist = personalrateprogresslist;
	}
	public List<ExamPublic> getExampubliclist() {
		return exampubliclist;
	}
	public void setExampubliclist(List<ExamPublic> exampubliclist) {
		this.exampubliclist = exampubliclist;
	}
	public List<ExamUserTestpaper> getExamusertestpaperlist() {
		return examusertestpaperlist;
	}
	public void setExamusertestpaperlist(
			List<ExamUserTestpaper> examusertestpaperlist) {
		this.examusertestpaperlist = examusertestpaperlist;
	}
	public List<TrainImplement> getTrainimplementlist() {
		return trainimplementlist;
	}
	public void setTrainimplementlist(List<TrainImplement> trainimplementlist) {
		this.trainimplementlist = trainimplementlist;
	}
	public List<Courseware> getCoursewarelist() {
		return coursewarelist;
	}
	public void setCoursewarelist(List<Courseware> coursewarelist) {
		this.coursewarelist = coursewarelist;
	}
	public StandardTermsService getStandardtermsServer() {
		return standardtermsServer;
	}
	public void setStandardtermsServer(StandardTermsService standardtermsServer) {
		this.standardtermsServer = standardtermsServer;
	}
	public ArrayList<String> getContentList() {
		return contentList;
	}
	public void setContentList(ArrayList<String> contentList) {
		this.contentList = contentList;
	}
	public ArrayList<String> getReferenceBookList() {
		return referenceBookList;
	}
	public void setReferenceBookList(ArrayList<String> referenceBookList) {
		this.referenceBookList = referenceBookList;
	}
	public ArrayList<String> getStandardnameList() {
		return standardnameList;
	}
	public void setStandardnameList(ArrayList<String> standardnameList) {
		this.standardnameList = standardnameList;
	}
	public List<ThemeBank> getIndexTkList() {
		return indexTkList;
	}
	public void setIndexTkList(List<ThemeBank> indexTkList) {
		this.indexTkList = indexTkList;
	}
	public Map<String, ThemeBank> getIndexTkMap() {
		return indexTkMap;
	}
	public void setIndexTkMap(Map<String, ThemeBank> indexTkMap) {
		this.indexTkMap = indexTkMap;
	}
	public Map<String, String> getIndexTkThemeSuccMap() {
		return indexTkThemeSuccMap;
	}
	public void setIndexTkThemeSuccMap(Map<String, String> indexTkThemeSuccMap) {
		this.indexTkThemeSuccMap = indexTkThemeSuccMap;
	}
	public Map<String, String> getMoniExamScoreMap() {
		return moniExamScoreMap;
	}
	public void setMoniExamScoreMap(Map<String, String> moniExamScoreMap) {
		this.moniExamScoreMap = moniExamScoreMap;
	}
	public Map<String, String> getGwpxExamScore() {
		return gwpxExamScore;
	}
	public void setGwpxExamScore(Map<String, String> gwpxExamScore) {
		this.gwpxExamScore = gwpxExamScore;
	}
	public List<StandardTerms> getStandardTermslist() {
		return standardTermslist;
	}
	public void setStandardTermslist(List<StandardTerms> standardTermslist) {
		this.standardTermslist = standardTermslist;
	}
	public EmployeeLearningForm getEmployeeLearningForm() {
		return employeeLearningForm;
	}
	public void setEmployeeLearningForm(EmployeeLearningForm employeeLearningForm) {
		this.employeeLearningForm = employeeLearningForm;
	}
	public List<DocumentLib> getDocumentlist() {
		return documentlist;
	}
	public void setDocumentlist(List<DocumentLib> documentlist) {
		this.documentlist = documentlist;
	}
	public int getStandardPagTotal() {
		return standardPagTotal;
	}
	public void setStandardPagTotal(int standardPagTotal) {
		this.standardPagTotal = standardPagTotal;
	}
	public int getTkPagTotal() {
		return tkPagTotal;
	}
	public void setTkPagTotal(int tkPagTotal) {
		this.tkPagTotal = tkPagTotal;
	}
	public int getStandardRownumInPage() {
		return standardRownumInPage;
	}
	public void setStandardRownumInPage(int standardRownumInPage) {
		this.standardRownumInPage = standardRownumInPage;
	}
	public int getTkRownumInPage() {
		return tkRownumInPage;
	}
	public void setTkRownumInPage(int tkRownumInPage) {
		this.tkRownumInPage = tkRownumInPage;
	}
	public int getStandardTotal() {
		return standardTotal;
	}
	public void setStandardTotal(int standardTotal) {
		this.standardTotal = standardTotal;
	}
	public int getTkTotal() {
		return tkTotal;
	}
	public void setTkTotal(int tkTotal) {
		this.tkTotal = tkTotal;
	}
	public int getP() {
		return p;
	}
	public void setP(int p) {
		this.p = p;
	}
	public String getQtc() {
		return qtc;
	}
	public void setQtc(String qtc) {
		this.qtc = qtc;
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
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
	public List<ThemeBank> getProBanklist() {
		return proBanklist;
	}
	public void setProBanklist(List<ThemeBank> proBanklist) {
		this.proBanklist = proBanklist;
	}
	
	
	
}
