package cn.com.ite.hnjtamis.exam.base.theme;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Workbook;

import org.apache.struts2.interceptor.ServletRequestAware;

import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.struts2.AbstractListAction;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.exception.EapException;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.CharsetSwitch;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.common.ZipCompress;
import cn.com.ite.hnjtamis.exam.base.theme.form.ThemeForm;
import cn.com.ite.hnjtamis.exam.base.theme.form.XxbItemFrom;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeInBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.ThemeListAction</p>
 * <p>Description 试题管理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午9:48:52
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeListAction extends AbstractListAction implements ServletRequestAware{

	private static final long serialVersionUID = 4675841085875193409L;

	private HttpServletRequest request;
	
	private List<ThemeForm> themelist;
	
	//以下为查询条件
	private String themeNameTerm;//试题名
	
	private String themeTypeIdTerm;//题型
	
	private String specialityIdTerm;//所属专业
	
	private String writeUserTerm;//出题人
	
	private Integer degreeTerm;//难度
	
	private String knowledgePointTerm;//知识点
	
	private String themeBankIdTerm;//题库
	
	private Integer stateTerm;//查询状态
	
	private String queryTypeTerm;//查询方式 'SAMETHEME' - '存在相同试题'

	private List<ThemeType> themeTypeList;
	
	private String importThemeType ;
	
	private String op;//input录入 audit审核 admin管理员
	
	private String sf;//pro-个人  all-全部 dc-电厂
	
	private String bankType;//题库类型 10-岗位题库  20-专业题库
	
	/**
	 * 导入文件
	 */
	private File xls;
	
	private String batchUpdateThemeBank;
	private String batchUpdateThemeIds;
	
	private int checkFinNum;
	
	private int checkSumNum;
	
	private double empXxb;//员工学习币
	
	private List<XxbItemFrom> empXxbItemList;
	private int empXxbItemListTotal;
	
	
	public String queryEmpXxbList(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		ThemeService themeService= (ThemeService)this.getService();
		empXxbItemList = themeService.getEmployeeXxbItemList(usersess.getEmployeeId());
		empXxbItemListTotal = empXxbItemList.size();
		return "queryEmpXxbList";
	}
	
	
	public String queryEmployeeXxb(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		ThemeService themeService= (ThemeService)this.getService();
		empXxb = themeService.getEmployeeXxb(usersess.getEmployeeId());
		return "queryEmployeeXxb";
	}
	
	/**
	 * 批量修改题库
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public String batchUpdateBank(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "save";
	 	}
		String msg = "";
		try{
			batchUpdateThemeBank = ","+batchUpdateThemeBank.replaceAll(" ", "")+",";
			Map term = new HashMap();
			term.put("themeBankIds", batchUpdateThemeBank);
			List<ThemeBank> themeBankList = this.getService().queryData("queryThemeBankListByIdsHql", term, null);
			
			if(themeBankList!=null && themeBankList.size()>0){
				term = new HashMap();
				batchUpdateThemeIds = ","+batchUpdateThemeIds.replaceAll(" ", "")+",";
				term.put("themeIds", batchUpdateThemeIds);
				List<Theme> themeList = this.getService().queryData("queryThemeByIdsHql", term, null);
				if(themeList!=null && themeList.size()>0){
					for(int i=0;i<themeList.size();i++){
						Theme theme = themeList.get(i);
						theme.getThemeInBanks().clear();
						for(int j=0;j<themeBankList.size();j++){
							ThemeBank themeBank = themeBankList.get(j);
							ThemeInBank themeInBank = new ThemeInBank();
							themeInBank.setThemeBank(themeBank);
							themeInBank.setTheme(theme);
							themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
							themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
							themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
							themeInBank.setOrganId(usersess.getOrganId());//机构ID
							themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
							theme.getThemeInBanks().add(themeInBank);
						}
					}
					this.getService().saves(themeList);
					msg = "修改成功";
				}else{
					msg = "没有找到对应的试题！";
				}
			}else{
				msg = "没有找到对应的题库！";
			}
			
		}catch(Exception e){
			e.printStackTrace();
			msg = "修改失败";
		}
		this.setMsg(msg);
		return "delete";
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
		try {
			ThemeService themeService = (ThemeService)this.getService();
			if(importThemeType==null || "".equals(importThemeType) || "null".equals(importThemeType)){
				importThemeType = "40";
			}
			try{
				 Workbook wb = Workbook.getWorkbook(xls);
				 String msg = themeService.importTheme(xls,usersess,importThemeType,"",new HashMap(),bankType);
				 this.setMsg(msg);
			}catch(Exception e){
				try{
				  String impPackageName = "imp"+DateUtils.convertDateToStr(new Date(),"yyyyMMddHHmmssSS");
				  String path = request.getRealPath("")+System.getProperty("file.separator")
						  +"upload"+System.getProperty("file.separator")
						  +"imp"+System.getProperty("file.separator")+impPackageName
						  +System.getProperty("file.separator");
				  //System.out.println(path);
				  ZipCompress.readByApacheZipFile(xls.getPath(), path);
				  
				  List<File> filelist = new ArrayList<File>();
				  getAllFileInPath(new File(path),filelist);
				  
				  List<File> xlsFileList = new ArrayList<File>();
				  Map<String,File> imgFileMap = new HashMap<String,File>();
				  for(int i=0;i<filelist.size();i++){
					  File tmpfile = filelist.get(i);
					  String xlsName = tmpfile.getName();
					  String hz = CharsetSwitch.CharacterLowerCase(xlsName.substring(xlsName.length()-4,xlsName.length()));
					  //System.out.println(xlsName +"   "+hz);
					  if(".xls".equals(hz)){
						  xlsFileList.add(tmpfile);
					  }else{
						  imgFileMap.put(CharsetSwitch.CharacterLowerCase(xlsName), tmpfile);
					  }
				  }
				  for(int i=0;i<xlsFileList.size();i++){
					  String msg = themeService.importTheme(xlsFileList.get(i),usersess,importThemeType,impPackageName,imgFileMap,bankType);
					  this.setMsg(msg);
				  }
				}catch(Exception ee){
					e.printStackTrace();
					this.setMsg("导入失败,请检查导入的文件是否符合要求，导入的文件必须是.xls结尾的Excel文件或.zip结尾的压缩文件！");
				}
			}
			 
			//String xlsName = xls.getName();
			//System.out.println(xlsName.substring(xlsName.length()-4,xlsName.length()));
			//System.out.println(xls.getPath());
			//String hz = FileType.getFileType(xls.getPath());
			//System.out.println(hz);
			//if(".xls".equals(hz)){
				///String msg = themeService.importTheme(xls,usersess,importThemeType);
				//this.setMsg(msg);
			//}else if(".zip".equals(hz)){
				
			//}else{
				//this.setMsg("导入的文件必须是.xls结尾的Excel文件或.zip结尾的压缩文件");
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
			this.setMsg("导入失败,请检查导入的文件是否符合要求，必须是.xls后缀的Excel文件！");
		}
		return "save";
	}
	
	private void getAllFileInPath(File file,List<File> filelist){
		 File[] files = file.listFiles(); 
		 if(files!=null && files.length>0){
			  for (int i = 0; i < files.length; i++) {  
					if(!files[i].isDirectory()){  
						filelist.add(files[i]); 
					}else{
						getAllFileInPath(files[i],filelist);
					}
			  }
		 }
	}

	/**
	 * 查询列表
	 * @author zhujian
	 * @description
	 * @return
	 * @modified
	 */
	public String list(){
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "list";
	 	}
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("themeNameTerm", themeNameTerm);
		param.put("themeTypeIdTerm", themeTypeIdTerm);
		param.put("specialityIdTerm", specialityIdTerm);
		param.put("writeUserTerm", writeUserTerm);
		param.put("degreeTerm", degreeTerm);
		param.put("knowledgePointTerm", knowledgePointTerm);
		param.put("themeBankIdTerm", themeBankIdTerm);
		param.put("stateTerm", stateTerm);
		param.put("op", op);
		param.put("sf", sf);
		param.put("employeeId", usersess.getEmployeeId());
		param.put("organId", usersess.getCurrentOrganId());
		param.put("queryTypeTerm", queryTypeTerm);
		param.put("bankType", bankType);
		
		/*System.out.println("specialityIdTerm = "+specialityIdTerm);
		System.out.println("writeUserTerm = "+writeUserTerm);
		System.out.println("degreeTerm = "+degreeTerm);
		System.out.println("knowledgePointTerm = "+knowledgePointTerm);*/
		
		//Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		//sortMap.put("createdBy", false);
		ThemeService themeService = (ThemeService)this.getService();
		List<Theme> themepojolist = themeService.getThemeList(param,this.getStart(),this.getLimit());//this.getService().queryData("themeForParamHql", param , sortMap,this.getStart(),this.getLimit());
		themelist = new ArrayList<ThemeForm>();
		if(themepojolist!=null && themepojolist.size()>0){
			for(int i=0;i<themepojolist.size();i++){
				Theme theme = (Theme)themepojolist.get(i);
				ThemeForm themeForm = new ThemeForm();
				themeForm.setThemeId(theme.getThemeId());
				themeForm.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
				themeForm.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);//题型
				themeForm.setKnowledgePoint(theme.getKnowledgePoint());//所属知识点
				themeForm.setThemeKeyword(theme.getThemeKeyword());//关键字
				themeForm.setType(theme.getType());//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
				themeForm.setThemeName(theme.getThemeName());//试题
				themeForm.setScoreType(theme.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
				themeForm.setDegree(theme.getDegree());//难度 5：容易,10：一般15：难,20：很难
				themeForm.setEachline(theme.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
				themeForm.setWriteUser(theme.getWriteUser());//出题人
				themeForm.setExplain(theme.getExplain());//注解
				themeForm.setDefaultScore(theme.getDefaultScore());//默认分数
				themeForm.setThemeSetNum(theme.getThemeSetNum());//出题次数
				themeForm.setThemePeopleNum(theme.getThemePeopleNum());//考试人次
				themeForm.setThemeRightNum(theme.getThemeRightNum());//答题正确数
				themeForm.setRemark(theme.getRemark());//备注
				themeForm.setState(theme.getState());//状态 5:保存10:上报15:发布20:打回
				themeForm.setSortNum(theme.getSortNum());//排序
				themeForm.setIsUse(theme.getIsUse());//有否有效 5:有效 10：无效
				themeForm.setOrganId(theme.getOrganId());//机构ID
				themeForm.setOrganName(theme.getOrganName());//机构名称
				themeForm.setSyncFlag(theme.getSyncFlag());//同步标志
				themeForm.setLastUpdateDate(theme.getLastUpdateDate());///最后修改时间
				themeForm.setLastUpdatedBy(theme.getLastUpdatedIdBy());//最后修改人
				themeForm.setLastUpdatedIdBy(theme.getLastUpdatedIdBy());//最后修改人ID
				themeForm.setCreationDate(theme.getCreationDate());//创建时间
				themeForm.setCreatedBy(theme.getCreatedBy());//创建人
				themeForm.setCreatedIdBy(theme.getCreatedIdBy());//创建人ID
				themeForm.setCheckRemark(theme.getCheckRemark());
				
				themeForm.setHaveImages(theme.getHaveImages());//是否存在图片文件
				themeForm.setImagesNames(theme.getImagesNames());//图片文件名字
				themeForm.setThemeVersion(theme.getThemeVersion());//版本号
				themeForm.setThemeCode(theme.getThemeCode());//试题编码
				themeForm.setThemeCrc(theme.getThemeCrc());//试题校验码
				themeForm.setThemeAns(theme.getThemeAns());//显示答案
				themeForm.setThemeHisId(theme.getThemeHisId());//最后一个试题版本ID
				themeForm.setImagesPath(theme.getImagesPath());//图片路径
			    themeForm.setImagesPackageName(theme.getImagesPackageName());//图片包名
			    themeForm.setImagesSucc(theme.getImagesSucc());//图片是否导入成功说明标识
			
				/*List<ThemeSearchKey> searchkeylist = theme.getThemeSearchKeies();
				themeForm.setSpecialityNames("");
				if(searchkeylist!=null && searchkeylist.size()>0){
					Iterator<ThemeSearchKey> its = searchkeylist.iterator();
					while(its.hasNext()){
						ThemeSearchKey themeSearchKey = (ThemeSearchKey)its.next();
						if(themeSearchKey.getProfessionId()!=null){
							ThemeSpecialityForm themeSpecialityForm = new ThemeSpecialityForm();
							themeForm.setSpecialityNames(themeForm.getSpecialityNames()+themeSearchKey.getProfessionName()+",");
						}
					}
				}
				if(!"".equals(themeForm.getSpecialityNames())){
					themeForm.setSpecialityNames(themeForm.getSpecialityNames().substring(0,themeForm.getSpecialityNames().length()-1));
				}*/
				
				themeForm.setThemeInBankNames("");
				themeForm.setThemeInBankIds("");
				themeForm.setThemeInBankCodes("");
				List<ThemeInBank> themeInBankList = theme.getThemeInBanks();
				if(themeInBankList!=null && themeInBankList.size()>0){
					Iterator<ThemeInBank> its = themeInBankList.iterator();
					while(its.hasNext()){
						ThemeInBank themeInBank = (ThemeInBank)its.next();
						if(themeInBank.getThemeBank()!=null){
							String themeBankName = themeInBank.getThemeBank().getThemeBankName();
							String themeBankCode = themeInBank.getThemeBank().getThemeBankCode();
							if(themeBankName.indexOf("("+themeBankCode+")")!=-1){
								themeBankName = themeBankName.replace(("("+themeBankCode+")"), "");
							}
							if(themeBankName.indexOf("（"+themeBankCode+"）")!=-1){
								themeBankName = themeBankName.replace(("（"+themeBankCode+"）"), "");
							}
							themeForm.setThemeInBankNames(themeForm.getThemeInBankNames()+themeBankName+",");
							themeForm.setThemeInBankCodes(themeForm.getThemeInBankCodes()+themeBankCode+",");
							themeForm.setThemeInBankIds(themeForm.getThemeInBankIds()+themeInBank.getThemeBank().getThemeBankId()+",");
						}
					}
				}
				if(!"".equals(themeForm.getThemeInBankNames())){
					themeForm.setThemeInBankNames(themeForm.getThemeInBankNames().substring(0,themeForm.getThemeInBankNames().length()-1));
				}
				if(!"".equals(themeForm.getThemeInBankCodes())){
					themeForm.setThemeInBankCodes(themeForm.getThemeInBankCodes().substring(0,themeForm.getThemeInBankCodes().length()-1));
				}
				themeForm.setLastFkAuditName(theme.getLastFkAuditName());
				themeForm.setLastFkState(theme.getLastFkState());
				themelist.add(themeForm);
			}
		}
		this.setTotal(themeService.getThemeCount(param));//service.countData("themeForParamHql", param));
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
		UserSession usersess = (UserSession)ServletContent.getSession().get(StaticVariable.USERSESSION);
		if(usersess == null){
			this.setMsg("SESSION　信息已失效，请关闭浏览器并重新登陆！");
			return "delete";
	 	}
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		int unsum = 0;
		for(int i = 0;i<ids.length ;i++){
			System.out.println("删除Id:"+ids[i]);
			Theme  theme = (Theme)service.findDataByKey(ids[i], Theme.class);
			if(theme!=null){
				try{
					//if(theme.getTestpaperThemes()!=null && theme.getTestpaperThemes().size() > 0){
						//unsum++;
					//}else{
						//theme.getThemeAnswerkeies().clear();
						//theme.getThemeInBanks().clear();
						//theme.getThemeSearchKeies().clear();
						//service.delete(theme);
						theme.setLastUpdateDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));///最后修改时间
						theme.setLastUpdatedBy(usersess.getEmployeeName()==null ? usersess.getAccount() : usersess.getEmployeeName());//最后修改人
						theme.setIsUse("10");
						theme.setSyncFlag("3");//数据状态1：增加 2：修改 3：删除
						service.saveOld(theme);
						succ++;
					//}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			sum++;
		}
		//service.deleteByKeys(ids, Theme.class);
		String msg = "选中试题"+sum+"个,成功删除"+succ+"个";
		if(unsum>0){
			msg+=","+unsum+"个试题已经被试卷引用，不能删除！";
		}else{
			msg+="。";
		}
		this.setMsg(msg);
		return "delete";
	}
	
	public String expPublic(){
		String[] ids = this.getId().split(",");
		int succ = 0;
		int sum = 0;
		for(int i = 0;i<ids.length ;i++){
			Theme  theme = (Theme)service.findDataByKey(ids[i], Theme.class);
			try{
				if(theme.getTestpaperThemes()!=null && theme.getTestpaperThemes().size() > 0){
					
				}else if(theme.getState()==1){
					theme.setState(15);
					service.saveOld(theme);
					succ++;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
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
	
	public String queryThemeTypeList(){
		themeTypeList = this.getService().queryData("queryThemeTypeListHql", new HashMap() , new HashMap());
		return "themeTypeList";
	}
	
	
	public String checkAllTheme(){
		if(StaticVariable.CHECK_SCHEDULE[0]<StaticVariable.CHECK_SCHEDULE[1]){
			checkFinNum = StaticVariable.CHECK_SCHEDULE[0];
			checkSumNum = StaticVariable.CHECK_SCHEDULE[1];
			this.setMsg("正在执行中，请稍后。");
		}else{
			ThemeService themeService = (ThemeService)this.getService();
			int[] checknum = themeService.saveAndcheckTheme();
			this.setMsg("本次供检查试题有"+checknum[0] +"道题，其中问题的有"+checknum[2]+"道题，与原来判断不同的有"+checknum[1]+"道题。");
		}
		return "checkAllTheme";
	}
	
	public String getCheckThemeFinNum(){
		checkFinNum = StaticVariable.CHECK_SCHEDULE[0];
		checkSumNum = StaticVariable.CHECK_SCHEDULE[1];
		return "checkThemeFinNum";
	}
	
	public String queryThemeTypeAndNullList(){
		themeTypeList = new ArrayList();
		ThemeType themeType = new ThemeType();
		themeType.setThemeTypeName("全部");
		themeTypeList.add(themeType);
		themeTypeList.addAll(this.getService().queryData("queryThemeTypeListHql", new HashMap() , new HashMap()));
		return "themeTypeList";
	}

	public List<ThemeForm> getThemelist() {
		return themelist;
	}

	public void setThemelist(List<ThemeForm> themelist) {
		this.themelist = themelist;
	}

	public String getThemeNameTerm() {
		return themeNameTerm;
	}

	public void setThemeNameTerm(String themeNameTerm) {
		this.themeNameTerm = themeNameTerm;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}


	public List<ThemeType> getThemeTypeList() {
		return themeTypeList;
	}


	public void setThemeTypeList(List<ThemeType> themeTypeList) {
		this.themeTypeList = themeTypeList;
	}


	public String getThemeTypeIdTerm() {
		return themeTypeIdTerm;
	}


	public void setThemeTypeIdTerm(String themeTypeIdTerm) {
		this.themeTypeIdTerm = themeTypeIdTerm;
	}


	public File getXls() {
		return xls;
	}


	public void setXls(File xls) {
		this.xls = xls;
	}
	
	public String getSpecialityIdTerm() {
		return specialityIdTerm;
	}

	public void setSpecialityIdTerm(String specialityIdTerm) {
		this.specialityIdTerm = specialityIdTerm;
	}

	public String getWriteUserTerm() {
		return writeUserTerm;
	}

	public void setWriteUserTerm(String writeUserTerm) {
		this.writeUserTerm = writeUserTerm;
	}

	public Integer getDegreeTerm() {
		return degreeTerm;
	}

	public void setDegreeTerm(Integer degreeTerm) {
		this.degreeTerm = degreeTerm;
	}

	public String getKnowledgePointTerm() {
		return knowledgePointTerm;
	}

	public void setKnowledgePointTerm(String knowledgePointTerm) {
		this.knowledgePointTerm = knowledgePointTerm;
	}

	public String getThemeBankIdTerm() {
		return themeBankIdTerm;
	}

	public void setThemeBankIdTerm(String themeBankIdTerm) {
		this.themeBankIdTerm = themeBankIdTerm;
	}

	public String getImportThemeType() {
		return importThemeType;
	}

	public void setImportThemeType(String importThemeType) {
		this.importThemeType = importThemeType;
	}

	public Integer getStateTerm() {
		return stateTerm;
	}

	public void setStateTerm(Integer stateTerm) {
		this.stateTerm = stateTerm;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getSf() {
		return sf;
	}

	public void setSf(String sf) {
		this.sf = sf;
	}

	public String getBatchUpdateThemeBank() {
		return batchUpdateThemeBank;
	}

	public void setBatchUpdateThemeBank(String batchUpdateThemeBank) {
		this.batchUpdateThemeBank = batchUpdateThemeBank;
	}

	public String getBatchUpdateThemeIds() {
		return batchUpdateThemeIds;
	}

	public void setBatchUpdateThemeIds(String batchUpdateThemeIds) {
		this.batchUpdateThemeIds = batchUpdateThemeIds;
	}

	public String getQueryTypeTerm() {
		return queryTypeTerm;
	}

	public void setQueryTypeTerm(String queryTypeTerm) {
		this.queryTypeTerm = queryTypeTerm;
	}

	public int getCheckFinNum() {
		return checkFinNum;
	}

	public void setCheckFinNum(int checkFinNum) {
		this.checkFinNum = checkFinNum;
	}

	public int getCheckSumNum() {
		return checkSumNum;
	}

	public void setCheckSumNum(int checkSumNum) {
		this.checkSumNum = checkSumNum;
	}

	public double getEmpXxb() {
		return empXxb;
	}

	public void setEmpXxb(double empXxb) {
		this.empXxb = empXxb;
	}


	public List<XxbItemFrom> getEmpXxbItemList() {
		return empXxbItemList;
	}


	public void setEmpXxbItemList(List<XxbItemFrom> empXxbItemList) {
		this.empXxbItemList = empXxbItemList;
	}


	public int getEmpXxbItemListTotal() {
		return empXxbItemListTotal;
	}


	public void setEmpXxbItemListTotal(int empXxbItemListTotal) {
		this.empXxbItemListTotal = empXxbItemListTotal;
	}
	
	
	
}
