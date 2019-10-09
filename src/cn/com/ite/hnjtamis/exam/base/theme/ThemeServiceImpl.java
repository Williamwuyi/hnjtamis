package cn.com.ite.hnjtamis.exam.base.theme;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.PropertyUtils;

import jxl.BooleanCell;
import jxl.BooleanFormulaCell;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.DateFormulaCell;
import jxl.NumberCell;
import jxl.NumberFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import cn.com.ite.eap2.common.utils.DateUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.common.CharsetSwitch;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.common.ThemeMakeCode;
import cn.com.ite.hnjtamis.exam.base.theme.form.XxbItemFrom;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeInBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeSearchKey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;



/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.ThemeServiceImpl</p>
 * <p>Description 试题管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午9:51:09
 * @version 1.0
 * 
 * @modified records:
 */
public class ThemeServiceImpl extends DefaultServiceImpl implements ThemeService {

	/**
	 * 获取员工的学习币
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public double getEmployeeXxb(String employeeId){
		ThemeDao themeDao = (ThemeDao)this.getDao();
		return themeDao.getEmployeeXxb(employeeId);
	}
	/**
	 * 获取员工的学习币(详细)
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<XxbItemFrom> getEmployeeXxbItemList(String employeeId){
		ThemeDao themeDao = (ThemeDao)this.getDao();
		return themeDao.getEmployeeXxbItemList(employeeId);
	}
	
	/**
	 * 查询试题列表
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeList(Map param,int start,int limit){
		ThemeDao themeDao = (ThemeDao)this.getDao();
		return themeDao.getThemeList(param,start,limit);
	}
	
	/**
	 * 查询试题列表的数量
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public int getThemeCount(Map param){
		ThemeDao themeDao = (ThemeDao)this.getDao();
		return themeDao.getThemeCount(param);
	}
	
	private static int import_init_state = 1;//导入默认的状态
	private static int un_import_init_state = -1;//导入失败的状态
	
	/**
	 *
	 * @author zhujian
	 * @description 试题导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importTheme(File xls,UserSession usersess,String import_init_type,String impPackageName,
			Map<String,File> imgFileMap,String bankType)throws Exception{
		String msg = "";
		try{
			
			ThemeDao themeDao = (ThemeDao)this.getDao();
			List<ThemeType> themeTypeList =  this.queryAllDate(ThemeType.class);
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = (ThemeType)themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeName(), themeType);
			}
			
			
			List<ThemeBank> themeInBankList =  this.queryAllDate(ThemeBank.class);
			Map<String,ThemeBank> themeInBankMap = new HashMap<String,ThemeBank>();
			for(int i=0;i<themeInBankList.size();i++){
				ThemeBank themeBank = (ThemeBank)themeInBankList.get(i);
				if(!"5".equals(themeBank.getIsL())){
					themeInBankMap.put("BANK_"+themeBank.getThemeBankName(), themeBank);
					if(themeBank.getThemeBankCode()!=null && !"".equals(themeBank.getThemeBankCode())
							&& !"null".equals(themeBank.getThemeBankCode())){
						themeInBankMap.put("BANK_"+themeBank.getThemeBankCode(), themeBank);
					}
					if(themeBank.getThemeBank()!=null){
						themeInBankMap.put(themeBank.getThemeBank().getThemeBankName()+"###"+themeBank.getThemeBankName(), themeBank);
						if(themeBank.getThemeBank().getThemeBankCode()!=null && !"".equals(themeBank.getThemeBank().getThemeBankCode())
								&& !"null".equals(themeBank.getThemeBank().getThemeBankCode())){
							themeInBankMap.put(themeBank.getThemeBank().getThemeBankCode()+"###"+themeBank.getThemeBankCode(), themeBank);
						}
					}else{
						themeInBankMap.put(themeBank.getThemeBankName(), themeBank);
					}
				}
			}
			
			
			
			List<Theme> addThemeList = new ArrayList<Theme>();
			Workbook wb = null;
			 try{
				 wb = Workbook.getWorkbook(xls);
			 }catch(Exception e){
				 throw new Exception("只能选择EXCEL文件！");
			 }
			 
			 Map themeCrcMap = new HashMap();//试题的CRC校验码(仅仅公有题库的校验码)
			 String[][] tageNames = ExamVariable.getXlsThemeTageNames();//StaticVariable.xls_theme_tageNames;
			 Map<String,String[][]> themeExpTemplateMap = new HashMap<String,String[][]>();
			 themeExpTemplateMap.put(StaticVariable.xls_theme_choiceQuestion, ExamVariable.getxlsThemeExpTemplateChoiceQuestion());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_trueOrFalse, ExamVariable.getxlsThemeExpTemplateTrueOrFalse());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_fillBlank, ExamVariable.getxlsThemeExpTemplateFillBlank());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_shortAnswer, ExamVariable.getxlsThemeExpTemplateShortAnswer());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_discussAnswer, ExamVariable.getxlsThemeExpTemplateDiscussAnswer());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_calTheme, ExamVariable.getxlsThemeExpTemplateCalTheme());
			 themeExpTemplateMap.put(StaticVariable.xls_theme_drawTheme, ExamVariable.getxlsThemeExpTemplateDrawTheme());
			 
			 for(int i= 0;i<tageNames.length;i++){
				String tagName = tageNames[i][0];
				String tagNameType = tageNames[i][2];
				ThemeType themeType = (ThemeType)themeTypeMap.get(tageNames[i][1]);
				if(themeType!=null){
			        Sheet sheet = wb.getSheet(tagName);
			        if(sheet!=null){
			        	String[][] themeExpTemplate = themeExpTemplateMap.get(tagNameType);
			        	if(themeExpTemplate == null){
			        		themeExpTemplate = themeExpTemplateMap.get(StaticVariable.xls_theme_shortAnswer);
			        	}
				        if(StaticVariable.xls_theme_choiceQuestion.equals(tagNameType)){//选择
				        	addThemeList.addAll(this.importChoiceQuestion(sheet, themeType,usersess,themeInBankMap,
				        			import_init_type,themeExpTemplate,impPackageName,imgFileMap,themeCrcMap,bankType));
				        }else  if(StaticVariable.xls_theme_trueOrFalse.equals(tagNameType)){//判断
				        	addThemeList.addAll(this.importTrueOrFalseItem(sheet, themeType,usersess,themeInBankMap,
				        			import_init_type,themeExpTemplate,impPackageName,imgFileMap,themeCrcMap,bankType));
				        }else if(StaticVariable.xls_theme_fillBlank.equals(tagNameType)){//填空题
				        	addThemeList.addAll(this.importFillBlank(sheet, themeType,usersess,themeInBankMap,
				        			import_init_type,themeExpTemplate,impPackageName,imgFileMap,themeCrcMap,bankType));
				        }else if(StaticVariable.xls_theme_shortAnswer.equals(tagNameType)
				        		|| StaticVariable.xls_theme_discussAnswer.equals(tagNameType)
				        		|| StaticVariable.xls_theme_calTheme.equals(tagNameType)
				        		|| StaticVariable.xls_theme_drawTheme.equals(tagNameType)){//简答题
				        	addThemeList.addAll(this.importShortAnswer(sheet, themeType,usersess,themeInBankMap,
				        			import_init_type,themeExpTemplate,impPackageName,imgFileMap,themeCrcMap,bankType));
				        }else{
				        	addThemeList.addAll(this.importShortAnswer(sheet, themeType,usersess,themeInBankMap,
				        			import_init_type,themeExpTemplate,impPackageName,imgFileMap,themeCrcMap,bankType));
				        }
			        }
				}
			}
			 
			if(addThemeList.size()>0){
				this.saves(addThemeList);
				msg = "成功保存"+addThemeList.size()+"个试题！";
			}
		}catch(Exception e){
			e.printStackTrace();
			msg = "导入错误，请联系管理员！";
			throw e;
		}
		return msg;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 多选导入
	 * @param sheet
	 * @param themeType
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private List<Theme> importChoiceQuestion(Sheet sheet,ThemeType themeType,UserSession usersess,
			Map<String,ThemeBank> themeInBankMap,String import_init_type,
			String[][] model_themeExpTemplate,String impPackageName,Map<String,File> imgFileMap,
			Map themeCrcMap,String bankType)throws Exception{
		String[] ansSort = StaticVariable.themeAnsSort;
		List<Theme> list = new ArrayList<Theme>();
		try{
			//得到当前工作表的行数
	        int rowNum = sheet.getRows();
	        //String[][] themeExpTemplate = ExamVariable.getxlsThemeExpTemplateChoiceQuestion();//StaticVariable.xls_theme_ExpTemplate_choiceQuestion;
	        
	        //为了处理两个题型使用同一模版，但是列不同的情况
	        String[][] themeExpTemplate = new String[model_themeExpTemplate.length][];
	        for(int i=0;i<model_themeExpTemplate.length;i++){
	        	String[] tmplist = new String[model_themeExpTemplate[i].length];
	        	for(int j=0;j<model_themeExpTemplate[i].length;j++){
	        		tmplist[j] = model_themeExpTemplate[i][j];
	        	}
	        	themeExpTemplate[i] = tmplist;
	        }
	        
	        for(int i = 0 ;i<sheet.getColumns();i++){
	        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
	        	for(int j = 0 ;j<themeExpTemplate.length;j++){
	        		if(themeExpTemplate[j][0].equals(tmpColumns.getContents())&& "-1".equals(themeExpTemplate[j][2])){
	        			themeExpTemplate[j][2] = i+"";
	        		}
	        	}
	        }
	        for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
	        	Theme theme = new Theme();
	        	theme.setThemeCode(ThemeMakeCode.getCode(j));
	        	theme.setThemeType(themeType);
	        	theme.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);
	        	theme.setIsUse("5");
	        	theme.setState(import_init_state);
	        	theme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
	        	theme.setCreatedBy(usersess.getEmployeeName());//创建人
	        	theme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
	        	theme.setOrganId(usersess.getOrganId());//机构ID
	        	theme.setOrganName(usersess.getOrganAlias());//机构名称
	        	theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
	        	theme.setScoreType("0");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	        	theme.setEachline(0);//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	        	theme.setType(import_init_type);
	        	theme.setLastFkState("10");
	        	theme.setAllowFk("10");
	        	String themeInBankParentName = "";
	        	String themeInBankName = "";
	        	
		        for (int k = 0; k < themeExpTemplate.length; k++) {//第一行为标题行，从第二行开始读
		        	String expCode = (String)themeExpTemplate[k][1];
		        	int col = Integer.parseInt(themeExpTemplate[k][2]);
		        	String expType = (String)themeExpTemplate[k][3];
		        	if(col<0)continue;
		        	Cell tmpColumns = (Cell)sheet.getCell(col, j);
		        	if(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents().trim()) || "null".equals(tmpColumns.getContents())){
		        		continue;
		        	}
		        	//System.out.println(tmpColumns.getContents());
		        	if(StaticVariable.xls_theme_haveImages.equals(expType)){//是否存在图片
		        		theme.setHaveImages(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_imagesNames.equals(expType)){//存在图片名字
		        		theme.setImagesNames(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_field.equals(expType)){//字段
		        		 Class fieldClass = PropertyUtils.getPropertyType(theme, expCode);
		        		 CellType cellType = tmpColumns.getType();
		        		 if(fieldClass.isAssignableFrom(String.class))
		        			 PropertyUtils.setProperty(theme, expCode, tmpColumns.getContents());
		        		 else if(fieldClass.isAssignableFrom(Date.class)){
		        			 if(cellType.equals(CellType.DATE)){//日期类型
		            			 DateCell dateCell = (DateCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateCell.getDate());
		            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
		            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateFormulaCell.getDate());
		            		 }
		        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
		        			 if(cellType.equals(CellType.BOOLEAN)){
		            			 BooleanCell booleanCell = (BooleanCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanCell.getValue());
		            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
		            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanFormulaCell.getValue());
		            		 }
		        		 }else{//数字类型
		        			 double d = 0;
		        			 if(cellType.equals(CellType.NUMBER)){
		            			 NumberCell numberCell = (NumberCell)tmpColumns;
		            			 d = numberCell.getValue();
		            		 }else if(cellType.equals(CellType.NUMBER_FORMULA)){
		            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)tmpColumns;
		            			 d = numberFormulaCell.getValue();            			 
		            		 }
		        			 if(fieldClass.getName().equals("java.lang.Integer")||
		        				fieldClass.getName().equals("int")){
		        				 PropertyUtils.setProperty(theme, expCode,new Double(d).intValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Long")||
		        					  fieldClass.getName().equals("long")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).longValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Float")||
		        					  fieldClass.getName().equals("float")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).floatValue());
		        			 }else
		        				 PropertyUtils.setProperty(theme, expCode, d);
		        		 }
		        	}else if(StaticVariable.xls_theme_defined.equals(expType)){//自定义
		        		if("themeInBankParentName".equals(expCode)){
		        			themeInBankParentName = tmpColumns.getContents();
		        		}else if("themeInBankName".equals(expCode)){
		        			themeInBankName = tmpColumns.getContents();
		        		}else if("themeType".equals(expCode)){//类型
		        			if("全部".equals(tmpColumns.getContents())){//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
		        				theme.setType("40");
		        			}else if("练习".equals(tmpColumns.getContents())){
		        				theme.setType("20");
		        			}else if("考试".equals(tmpColumns.getContents())){
		        				theme.setType("10");
		        			}
		        			
		        		}else if("themeDegree".equals(expCode)){//难度系数
		        			if("难".equals(tmpColumns.getContents())){
		        				theme.setDegree(15);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("中等".equals(tmpColumns.getContents())){
		        				theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("易".equals(tmpColumns.getContents())){
		        				theme.setDegree(5);//难度 5：容易,10：一般15：难,20：很难
		        			}
		        		
		        		}
		        	}else if(StaticVariable.xls_theme_ans.equals(expType)){//显示的答案
		        		int ansIndex = -1;
		        		for(int i = 0;i<ansSort.length;i++){
		        			if(expCode.equals("answerkey"+ansSort[i])){
		        				ansIndex = i;
		        				break;
		        			}
		        		}
		        		if(ansIndex>=0 && tmpColumns.getContents()!=null && !"".equals(tmpColumns.getContents().trim())){
		        			if(ansIndex+1>theme.getThemeAnswerkeies().size()){
		        				for(int i = theme.getThemeAnswerkeies().size();i<(ansIndex+1);i++){
		        					ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
		        					themeAnswerkey.setTheme(theme);
		        					themeAnswerkey.setSortNum(i);
		        					themeAnswerkey.setIsRight(5);
		        					theme.getThemeAnswerkeies().add(new ThemeAnswerkey());
		        				}
		        			}
		        			ThemeAnswerkey themeAnswerkey= (ThemeAnswerkey)theme.getThemeAnswerkeies().get(ansIndex);
		        			themeAnswerkey.setSortNum(ansIndex);
		        			themeAnswerkey.setIsRight(5);
		        			themeAnswerkey.setAnswerkeyValue(tmpColumns.getContents());
		        			themeAnswerkey.setTheme(theme);
		        		}
		        	}else if(StaticVariable.xls_theme_rightAns.equals(expType)){//正确答案
		        		if(tmpColumns.getContents().indexOf(",")==-1){
		        			List ansList = new ArrayList();
		        			for(int i = 0;i<ansSort.length;i++){
			        			if(tmpColumns.getContents().indexOf(ansSort[i])!=-1){
			        				ansList.add(i+"");
			        				//break;
			        			}
			        		}
		        			if(ansList.size()>0){
		        				for(int ttt=0;ttt<ansList.size();ttt++){
		        					int ansIndex = Integer.parseInt((String)ansList.get(ttt));
				        			if(ansIndex>=0){
					        			if(ansIndex+1>theme.getThemeAnswerkeies().size()){
					        				for(int i = theme.getThemeAnswerkeies().size();i<(ansIndex+1);i++){
					        					ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
					        					themeAnswerkey.setTheme(theme);
					        					themeAnswerkey.setSortNum(i);
					        					themeAnswerkey.setIsRight(5);
					        					theme.getThemeAnswerkeies().add(new ThemeAnswerkey());
					        				}
					        			}
					        			ThemeAnswerkey themeAnswerkey= (ThemeAnswerkey)theme.getThemeAnswerkeies().get(ansIndex);
					        			themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
					        			theme.getThemeAnswerkeies().set(ansIndex,themeAnswerkey);
				        			}
		        				}
		        			}
		        		}else{
			        		String[] rightAnsArr = tmpColumns.getContents().split(",");
			        		for(int s=0;s<rightAnsArr.length;s++){
				        		int ansIndex = -1;
				        		String xlsContents = rightAnsArr[s];
				        		for(int i = 0;i<ansSort.length;i++){
				        			if(ansSort[i].equals(xlsContents)){
				        				ansIndex = i;
				        				break;
				        			}
				        		}
				        		if(ansIndex>=0){
				        			if(ansIndex+1>theme.getThemeAnswerkeies().size()){
				        				for(int i = theme.getThemeAnswerkeies().size();i<(ansIndex+1);i++){
				        					ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
				        					themeAnswerkey.setTheme(theme);
				        					themeAnswerkey.setSortNum(i);
				        					themeAnswerkey.setIsRight(5);
				        					theme.getThemeAnswerkeies().add(new ThemeAnswerkey());
				        				}
				        			}
				        			ThemeAnswerkey themeAnswerkey= (ThemeAnswerkey)theme.getThemeAnswerkeies().get(ansIndex);
				        			themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
				        			theme.getThemeAnswerkeies().set(ansIndex,themeAnswerkey);
				        		}
			        		}
		        		}
		        	}
		        }
		        
		        this.replaceImg(theme, impPackageName, imgFileMap);//替换图片
		        //System.out.println(theme.getThemeName()+"  "+themeInBankParentName+"  "+themeInBankName+"------------------------------");
		        if(theme.getThemeName()!=null && !"".equals(theme.getThemeName().trim())  && !"null".equals(theme.getThemeName().trim())){
		        	 if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)
				        		&& themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get(themeInBankParentName+"###"+themeInBankName);
				        	if(themeBank == null){
				        		ThemeBank parentthemeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        		if(parentthemeBank == null){
				        			parentthemeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess, null, bankType);
				        			if(parentthemeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,parentthemeBank);
				        		}
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,parentthemeBank, bankType);
				        		if(themeBank!=null){
				        			themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        			themeInBankMap.put(themeInBankParentName+"###"+themeInBankName,themeBank);
				        		}
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				   }else if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
				    }else if(themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
			        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankName);
			        	if(themeBank == null){
			        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,null, bankType);
			        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankName,themeBank);
			        	}
			        	if(themeBank!=null){
			        		ThemeInBank themeInBank = new ThemeInBank();
			        		themeInBank.setTheme(theme);
			        		themeInBank.setThemeBank(themeBank);
			        		themeInBank.setBankType(themeBank.getBankType());
			        		themeInBank.setBankOrganId(themeBank.getOrganId());
			        		themeInBank.setBankOrganName(themeBank.getOrganName());
			        		themeInBank.setBankPublic(themeBank.getBankPublic());
			        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
			        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
			        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
			        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
			        		theme.getThemeInBanks().add(themeInBank);
			        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
			        		
			        		try{
								theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
							}catch(Exception e){
								theme.setSortNum(new Long(999999));
								//e.printStackTrace();
							}
			        	}
			        	
				    }
		        	 
		        	theme.setCheckRemark(ThemeCheckOption.checkTheme(theme));
		        	if(theme.getDefaultScore()==null){
		        		theme.setDefaultScore(new Double(1));
		        	}
		        	if(theme.getCheckRemark()!=null && !"".equals(theme.getCheckRemark())){
		        		theme.setState(un_import_init_state);
		        	}
		        	if(theme.getThemeInBanks()!=null && theme.getThemeInBanks().size()==1){
		        		ThemeBank themeBank = theme.getThemeInBanks().get(0).getThemeBank();
		        		if(themeCrcMap.get(themeBank.getThemeBankId()) == null){
		        			ThemeDao themeDao = (ThemeDao)this.getDao();
		        			Map _themeCrcMap = themeDao.getThemeBankAndThemeCrcMap(themeBank.getThemeBankId());
		        			themeCrcMap.putAll(_themeCrcMap);
		        			themeCrcMap.put(themeBank.getThemeBankId(),"a");
		        		}
		        		if(themeCrcMap.get(themeBank.getThemeBankId()+"@"+theme.getThemeCrc())==null){
			        		 list.add(theme);
			        	}
		        	}else if(themeCrcMap.get(theme.getThemeCrc())==null){
		        		 list.add(theme);
		        	}
		           
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return list;
	}

	
	
	/**
	 *
	 * @author zhujian
	 * @description 判断导入
	 * @param sheet
	 * @param themeType
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private List<Theme> importTrueOrFalseItem (Sheet sheet,ThemeType themeType,
			UserSession usersess,Map<String,ThemeBank> themeInBankMap,String import_init_type,
			String[][] model_themeExpTemplate,String impPackageName,Map<String,File> imgFileMap,
			Map themeCrcMap,String bankType)throws Exception{
		List<Theme> list = new ArrayList<Theme>();
		try{
			//得到当前工作表的行数
	        int rowNum = sheet.getRows();
	        //为了处理两个题型使用同一模版，但是列不同的情况
	        String[][] themeExpTemplate = new String[model_themeExpTemplate.length][];
	        for(int i=0;i<model_themeExpTemplate.length;i++){
	        	String[] tmplist = new String[model_themeExpTemplate[i].length];
	        	for(int j=0;j<model_themeExpTemplate[i].length;j++){
	        		tmplist[j] = model_themeExpTemplate[i][j];
	        	}
	        	themeExpTemplate[i] = tmplist;
	        }
	        //String[][] themeExpTemplate = ExamVariable.getxlsThemeExpTemplateTrueOrFalse();//StaticVariable.xls_theme_ExpTemplate_trueOrFalse;
	        
	        for(int i = 0 ;i<sheet.getColumns();i++){
	        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
	        	for(int j = 0 ;j<themeExpTemplate.length;j++){
	        		if(themeExpTemplate[j][0].equals(tmpColumns.getContents())&& "-1".equals(themeExpTemplate[j][2])){
	        			themeExpTemplate[j][2] = i+"";
	        		}
	        	}
	        }
	        for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
	        	Theme theme = new Theme();
	        	theme.setThemeCode(ThemeMakeCode.getCode(j));
	        	theme.setThemeType(themeType);
	        	theme.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);
	        	theme.setIsUse("5");
	        	theme.setState(import_init_state);
	        	theme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
	        	theme.setCreatedBy(usersess.getEmployeeName());//创建人
	        	theme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
	        	theme.setOrganId(usersess.getOrganId());//机构ID
	        	theme.setOrganName(usersess.getOrganAlias());//机构名称
	        	theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
	        	theme.setScoreType("0");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	        	theme.setEachline(0);//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	        	theme.setLastFkState("10");
	        	theme.setAllowFk("10");
	        	theme.setType(import_init_type);
	        	String themeInBankParentName = "";
	        	String themeInBankName = "";
	        	
		        for (int k = 0; k < themeExpTemplate.length; k++) {//第一行为标题行，从第二行开始读
		        	String expCode = (String)themeExpTemplate[k][1];
		        	String expType = (String)themeExpTemplate[k][3];
		        	int col = Integer.parseInt(themeExpTemplate[k][2]);
		        	if(col<0)continue;
		        	Cell tmpColumns = (Cell)sheet.getCell(col, j);
		        	if(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents().trim()) || "null".equals(tmpColumns.getContents())){
		        		continue;
		        	}
		        	if(StaticVariable.xls_theme_haveImages.equals(expType)){//是否存在图片
		        		theme.setHaveImages(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_imagesNames.equals(expType)){//存在图片名字
		        		theme.setImagesNames(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_field.equals(expType)){//字段
		        		 Class fieldClass = PropertyUtils.getPropertyType(theme, expCode);
		        		 CellType cellType = tmpColumns.getType();
		        		 if(fieldClass.isAssignableFrom(String.class))
		        			 PropertyUtils.setProperty(theme, expCode, tmpColumns.getContents());
		        		 else if(fieldClass.isAssignableFrom(Date.class)){
		        			 if(cellType.equals(CellType.DATE)){//日期类型
		            			 DateCell dateCell = (DateCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateCell.getDate());
		            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
		            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateFormulaCell.getDate());
		            		 }
		        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
		        			 if(cellType.equals(CellType.BOOLEAN)){
		            			 BooleanCell booleanCell = (BooleanCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanCell.getValue());
		            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
		            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanFormulaCell.getValue());
		            		 }
		        		 }else{//数字类型
		        			 double d = 0;
		        			 if(cellType.equals(CellType.NUMBER)){
		            			 NumberCell numberCell = (NumberCell)tmpColumns;
		            			 d = numberCell.getValue();
		            		 }else if(cellType.equals(CellType.NUMBER_FORMULA)){
		            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)tmpColumns;
		            			 d = numberFormulaCell.getValue();            			 
		            		 }
		        			 if(fieldClass.getName().equals("java.lang.Integer")||
		        				fieldClass.getName().equals("int")){
		        				 PropertyUtils.setProperty(theme, expCode,new Double(d).intValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Long")||
		        					  fieldClass.getName().equals("long")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).longValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Float")||
		        					  fieldClass.getName().equals("float")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).floatValue());
		        			 }else
		        				 PropertyUtils.setProperty(theme, expCode, d);
		        		 }
		        	}else if(StaticVariable.xls_theme_defined.equals(expType)){//自定义
		        		if("themeInBankParentName".equals(expCode)){
		        			themeInBankParentName = tmpColumns.getContents();
		        		}else if("themeInBankName".equals(expCode)){
		        			themeInBankName = tmpColumns.getContents();
		        		}else if("themeType".equals(expCode)){//类型
		        			if("全部".equals(tmpColumns.getContents())){//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
		        				theme.setType("40");
		        			}else if("练习".equals(tmpColumns.getContents())){
		        				theme.setType("20");
		        			}else if("考试".equals(tmpColumns.getContents())){
		        				theme.setType("10");
		        			}
		        		}else if("themeDegree".equals(expCode)){//难度系数
		        			if("难".equals(tmpColumns.getContents())){
		        				theme.setDegree(15);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("中等".equals(tmpColumns.getContents())){
		        				theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("易".equals(tmpColumns.getContents())){
		        				theme.setDegree(5);//难度 5：容易,10：一般15：难,20：很难
		        			}
		        		
		        		}
		        	}
		        	if(StaticVariable.xls_theme_rightAns.equals(expType)){//正确答案
		        		//System.out.println(tmpColumns.getContents().trim());
		        		int sortNum = 0;
		        		ThemeAnswerkey r_themeAnswerkey = new ThemeAnswerkey();
		        		r_themeAnswerkey.setAnswerkeyValue("对");
	        			if(r_themeAnswerkey.getAnswerkeyValue().equals(tmpColumns.getContents().trim())
	        					|| "√".equals(tmpColumns.getContents().trim())
	        					|| "T".equals(tmpColumns.getContents().trim())){
	        				r_themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
	        			}else{
	        				r_themeAnswerkey.setIsRight(5);
	        			}
	        			r_themeAnswerkey.setTheme(theme);
	        			r_themeAnswerkey.setSortNum(sortNum);
	        			theme.getThemeAnswerkeies().add(r_themeAnswerkey);
	        			sortNum++;
	        			
	        			ThemeAnswerkey c_themeAnswerkey = new ThemeAnswerkey();
	        			c_themeAnswerkey.setAnswerkeyValue("错");
	        			if(c_themeAnswerkey.getAnswerkeyValue().equals(tmpColumns.getContents().trim())
	        					|| "×".equals(tmpColumns.getContents().trim())
	        					|| "F".equals(tmpColumns.getContents().trim())){
	        				c_themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
	        			}else{
	        				c_themeAnswerkey.setIsRight(5);
	        			}
	        			c_themeAnswerkey.setTheme(theme);
	        			c_themeAnswerkey.setSortNum(sortNum);
	        			c_themeAnswerkey.setTheme(theme);
	        			theme.getThemeAnswerkeies().add(c_themeAnswerkey);
		        	}
		        }
		        
		        this.replaceImg(theme, impPackageName, imgFileMap);//替换图片
		        //System.out.println(theme.getThemeName()+"  "+themeInBankParentName+"  "+themeInBankName+"------------------------------");
		        if(theme.getThemeName()!=null && !"".equals(theme.getThemeName().trim())  && !"null".equals(theme.getThemeName().trim())){
		        	 if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)
				        		&& themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get(themeInBankParentName+"###"+themeInBankName);
				        	if(themeBank == null){
				        		ThemeBank parentthemeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        		if(parentthemeBank == null){
				        			parentthemeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess, null, bankType);
				        			if(parentthemeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,parentthemeBank);
				        		}
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,parentthemeBank, bankType);
				        		if(themeBank!=null){
				        			themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        			themeInBankMap.put(themeInBankParentName+"###"+themeInBankName,themeBank);
				        		}
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        }else if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
				        }else if(themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
					    }
		        	 theme.setCheckRemark(ThemeCheckOption.checkTheme(theme));
		        	 if(theme.getDefaultScore()==null){
			        	theme.setDefaultScore(new Double(1));
			         }
		        	 if(theme.getCheckRemark()!=null && !"".equals(theme.getCheckRemark())){
			        		theme.setState(un_import_init_state);
			        }
		        	if(theme.getThemeInBanks()!=null && theme.getThemeInBanks().size()==1){
			        	ThemeBank themeBank = theme.getThemeInBanks().get(0).getThemeBank();
			        	if(themeCrcMap.get(themeBank.getThemeBankId()) == null){
		        			ThemeDao themeDao = (ThemeDao)this.getDao();
		        			Map _themeCrcMap = themeDao.getThemeBankAndThemeCrcMap(themeBank.getThemeBankId());
		        			themeCrcMap.putAll(_themeCrcMap);
		        			themeCrcMap.put(themeBank.getThemeBankId(),"a");
		        		}
			        	if(themeCrcMap.get(themeBank.getThemeBankId()+"@"+theme.getThemeCrc())==null){
				        	list.add(theme);
				        }
			        }else if(themeCrcMap.get(theme.getThemeCrc())==null){
			        	list.add(theme);
			        }
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 填空导入
	 * @param sheet
	 * @param themeType
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private List<Theme> importFillBlank(Sheet sheet,ThemeType themeType,
			UserSession usersess,Map<String,ThemeBank> themeInBankMap,String import_init_type,
			String[][] model_themeExpTemplate,String impPackageName,Map<String,File> imgFileMap,
			Map themeCrcMap,String bankType)throws Exception{
		List<Theme> list = new ArrayList<Theme>();
		try{
			//得到当前工作表的行数
	        int rowNum = sheet.getRows();
	        //为了处理两个题型使用同一模版，但是列不同的情况
	        String[][] themeExpTemplate = new String[model_themeExpTemplate.length][];
	        for(int i=0;i<model_themeExpTemplate.length;i++){
	        	String[] tmplist = new String[model_themeExpTemplate[i].length];
	        	for(int j=0;j<model_themeExpTemplate[i].length;j++){
	        		tmplist[j] = model_themeExpTemplate[i][j];
	        	}
	        	themeExpTemplate[i] = tmplist;
	        }
	        //String[][] themeExpTemplate = ExamVariable.getxlsThemeExpTemplateFillBlank();//StaticVariable.xls_theme_ExpTemplate_fillBlank;
	        for(int i = 0 ;i<sheet.getColumns();i++){
	        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
	        	for(int j = 0 ;j<themeExpTemplate.length;j++){
	        		if(themeExpTemplate[j][0].equals(tmpColumns.getContents())&& "-1".equals(themeExpTemplate[j][2])){
	        			themeExpTemplate[j][2] = i+"";
	        		}
	        	}
	        }
	        for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
	        	Theme theme = new Theme();
	        	theme.setThemeCode(ThemeMakeCode.getCode(j));
	        	theme.setThemeType(themeType);
	        	theme.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);
	        	theme.setIsUse("5");
	        	theme.setState(import_init_state);
	        	theme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
	        	theme.setCreatedBy(usersess.getEmployeeName());//创建人
	        	theme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
	        	theme.setOrganId(usersess.getOrganId());//机构ID
	        	theme.setOrganName(usersess.getOrganAlias());//机构名称
	        	theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
	        	theme.setScoreType("1");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	        	theme.setEachline(0);//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	        	theme.setLastFkState("10");
	        	theme.setAllowFk("10");
	        	theme.setType(import_init_type);
	        	String themeInBankParentName = "";
	        	String themeInBankName = "";
	        	
		        for (int k = 0; k < themeExpTemplate.length; k++) {//第一行为标题行，从第二行开始读
		        	String expCode = (String)themeExpTemplate[k][1];
		        	String expType = (String)themeExpTemplate[k][3];
		        	int col = Integer.parseInt(themeExpTemplate[k][2]);
		        	if(col<0)continue;
		        	Cell tmpColumns = (Cell)sheet.getCell(col, j);
		        	if(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents().trim()) || "null".equals(tmpColumns.getContents())){
		        		continue;
		        	}
		        	if(StaticVariable.xls_theme_haveImages.equals(expType)){//是否存在图片
		        		theme.setHaveImages(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_imagesNames.equals(expType)){//存在图片名字
		        		theme.setImagesNames(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_field.equals(expType)){//字段
		        		 Class fieldClass = PropertyUtils.getPropertyType(theme, expCode);
		        		 CellType cellType = tmpColumns.getType();
		        		 if(fieldClass.isAssignableFrom(String.class))
		        			 PropertyUtils.setProperty(theme, expCode, tmpColumns.getContents());
		        		 else if(fieldClass.isAssignableFrom(Date.class)){
		        			 if(cellType.equals(CellType.DATE)){//日期类型
		            			 DateCell dateCell = (DateCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateCell.getDate());
		            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
		            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateFormulaCell.getDate());
		            		 }
		        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
		        			 if(cellType.equals(CellType.BOOLEAN)){
		            			 BooleanCell booleanCell = (BooleanCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanCell.getValue());
		            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
		            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanFormulaCell.getValue());
		            		 }
		        		 }else{//数字类型
		        			 double d = 0;
		        			 if(cellType.equals(CellType.NUMBER)){
		            			 NumberCell numberCell = (NumberCell)tmpColumns;
		            			 d = numberCell.getValue();
		            		 }else if(cellType.equals(CellType.NUMBER_FORMULA)){
		            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)tmpColumns;
		            			 d = numberFormulaCell.getValue();            			 
		            		 }
		        			 if(fieldClass.getName().equals("java.lang.Integer")||
		        				fieldClass.getName().equals("int")){
		        				 PropertyUtils.setProperty(theme, expCode,new Double(d).intValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Long")||
		        					  fieldClass.getName().equals("long")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).longValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Float")||
		        					  fieldClass.getName().equals("float")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).floatValue());
		        			 }else
		        				 PropertyUtils.setProperty(theme, expCode, d);
		        		 }
		        	}else if(StaticVariable.xls_theme_defined.equals(expType)){//自定义
		        		if("themeInBankParentName".equals(expCode)){
		        			themeInBankParentName = tmpColumns.getContents();
		        		}else if("themeInBankName".equals(expCode)){
		        			themeInBankName = tmpColumns.getContents();
		        		}else if("themeType".equals(expCode)){//类型
		        			if("全部".equals(tmpColumns.getContents())){//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
		        				theme.setType("40");
		        			}else if("练习".equals(tmpColumns.getContents())){
		        				theme.setType("20");
		        			}else if("考试".equals(tmpColumns.getContents())){
		        				theme.setType("10");
		        			}
		        		}else if("themeDegree".equals(expCode)){//难度系数
		        			if("难".equals(tmpColumns.getContents())){
		        				theme.setDegree(15);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("中等".equals(tmpColumns.getContents())){
		        				theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("易".equals(tmpColumns.getContents())){
		        				theme.setDegree(5);//难度 5：容易,10：一般15：难,20：很难
		        			}
		        		
		        		}
		        	}else if(StaticVariable.xls_theme_rightAns.equals(expType)){//正确答案
		        		String[] rightAnsArr = tmpColumns.getContents().split(",");
		        		for(int i=0;i<rightAnsArr.length;i++){
		        			String[] rightAnsArr2 = rightAnsArr[i].split("；");
		        			for(int jj=0;jj<rightAnsArr2.length;jj++){
			        			ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
			        			themeAnswerkey.setTheme(theme);
			        			themeAnswerkey.setAnswerkeyValue(rightAnsArr2[jj]);
			        			themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
			        			themeAnswerkey.setSortNum(i);
			        			theme.getThemeAnswerkeies().add(themeAnswerkey);
		        			}
		        		}
		        		
		        	}
		        }
		        
		        this.replaceImg(theme, impPackageName, imgFileMap);//替换图片
		        //System.out.println(theme.getThemeName()+"  "+themeInBankParentName+"  "+themeInBankName+"------------------------------");
		        if(theme.getThemeName()!=null && !"".equals(theme.getThemeName().trim())  && !"null".equals(theme.getThemeName().trim())){
		        	 if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)
				        		&& themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get(themeInBankParentName+"###"+themeInBankName);
				        	if(themeBank == null){
				        		ThemeBank parentthemeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        		if(parentthemeBank == null){
				        			parentthemeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess, null, bankType);
				        			if(parentthemeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,parentthemeBank);
				        		}
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,parentthemeBank, bankType);
				        		if(themeBank!=null){
				        			themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        			themeInBankMap.put(themeInBankParentName+"###"+themeInBankName,themeBank);
				        		}
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        }else if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
				        }else if(themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
					    }
		        	 theme.setCheckRemark(ThemeCheckOption.checkTheme(theme));
		        	 if(theme.getDefaultScore()==null){
			        	theme.setDefaultScore(new Double(1));
			         }
		        	 if(theme.getCheckRemark()!=null && !"".equals(theme.getCheckRemark())){
			        	theme.setState(un_import_init_state);
			         }
		        	 if(theme.getThemeInBanks()!=null && theme.getThemeInBanks().size()==1){
				        ThemeBank themeBank = theme.getThemeInBanks().get(0).getThemeBank();
				        if(themeCrcMap.get(themeBank.getThemeBankId()) == null){
		        			ThemeDao themeDao = (ThemeDao)this.getDao();
		        			Map _themeCrcMap = themeDao.getThemeBankAndThemeCrcMap(themeBank.getThemeBankId());
		        			themeCrcMap.putAll(_themeCrcMap);
		        			themeCrcMap.put(themeBank.getThemeBankId(),"a");
		        		}
				        if(themeCrcMap.get(themeBank.getThemeBankId()+"@"+theme.getThemeCrc())==null){
					       list.add(theme);
					    }
				     }else if(themeCrcMap.get(theme.getThemeCrc())==null){
				        list.add(theme);
				     }
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 简答题导入
	 * @param sheet
	 * @param themeType
	 * @return
	 * @throws Exception
	 * @modified
	 */
	private List<Theme> importShortAnswer(Sheet sheet,ThemeType themeType,
			UserSession usersess,Map<String,ThemeBank> themeInBankMap,String import_init_type,
			String[][] model_themeExpTemplate,String impPackageName,Map<String,File> imgFileMap,
			Map themeCrcMap,String bankType)throws Exception{
		List<Theme> list = new ArrayList<Theme>();
		try{
			//得到当前工作表的行数
	        int rowNum = sheet.getRows();
	        //为了处理两个题型使用同一模版，但是列不同的情况
	        String[][] themeExpTemplate = new String[model_themeExpTemplate.length][];
	        for(int i=0;i<model_themeExpTemplate.length;i++){
	        	String[] tmplist = new String[model_themeExpTemplate[i].length];
	        	for(int j=0;j<model_themeExpTemplate[i].length;j++){
	        		tmplist[j] = model_themeExpTemplate[i][j];
	        	}
	        	themeExpTemplate[i] = tmplist;
	        }
	        //String[][] themeExpTemplate = ExamVariable.getxlsThemeExpTemplateShortAnswer();//StaticVariable.xls_theme_ExpTemplate_shortAnswer;
	        for(int i = 0 ;i<sheet.getColumns();i++){
	        	Cell tmpColumns = (Cell)sheet.getCell(i, 0);
	        	for(int j = 0 ;j<themeExpTemplate.length;j++){
	        		if(themeExpTemplate[j][0].equals(tmpColumns.getContents()) && "-1".equals(themeExpTemplate[j][2])){
	        			themeExpTemplate[j][2] = i+"";
	        		}
	        	}
	        }
	        for (int j = 1; j < rowNum; j++) {//第一行为标题行，从第二行开始读
	        	Theme theme = new Theme();
	        	theme.setThemeCode(ThemeMakeCode.getCode(j));
	        	theme.setThemeType(themeType);
	        	theme.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);
	        	theme.setIsUse("5");
	        	theme.setState(import_init_state);
	        	theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
	        	theme.setScoreType("1");//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
	        	theme.setEachline(0);//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
	        	theme.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
	        	theme.setCreatedBy(usersess.getEmployeeName());//创建人
	        	theme.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
	        	theme.setOrganId(usersess.getOrganId());//机构ID
	        	theme.setOrganName(usersess.getOrganAlias());//机构名称
	        	theme.setType(import_init_type);
	        	theme.setLastFkState("10");
	        	theme.setAllowFk("10");
	        	String themeInBankParentName = "";
	        	String themeInBankName = "";
	        	
		        for (int k = 0; k < themeExpTemplate.length; k++) {//第一行为标题行，从第二行开始读
		        	String expCode = (String)themeExpTemplate[k][1];
		        	String expType = (String)themeExpTemplate[k][3];
		        	int col = Integer.parseInt(themeExpTemplate[k][2]);
		        	if(col<0)continue;
		        	Cell tmpColumns = (Cell)sheet.getCell(col, j);
		        	if(tmpColumns==null || tmpColumns.getContents()==null || "".equals(tmpColumns.getContents()) || "null".equals(tmpColumns.getContents())){
		        		continue;
		        	}
		        	if(StaticVariable.xls_theme_haveImages.equals(expType)){//是否存在图片
		        		theme.setHaveImages(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_imagesNames.equals(expType)){//存在图片名字
		        		theme.setImagesNames(tmpColumns.getContents());
		        	}else if(StaticVariable.xls_theme_field.equals(expType)){//字段
		        		 Class fieldClass = PropertyUtils.getPropertyType(theme, expCode);
		        		 CellType cellType = tmpColumns.getType();
		        		 if(fieldClass.isAssignableFrom(String.class))
		        			 PropertyUtils.setProperty(theme, expCode, tmpColumns.getContents());
		        		 else if(fieldClass.isAssignableFrom(Date.class)){
		        			 if(cellType.equals(CellType.DATE)){//日期类型
		            			 DateCell dateCell = (DateCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateCell.getDate());
		            		 }else if(cellType.equals(CellType.DATE_FORMULA)){
		            			 DateFormulaCell dateFormulaCell = (DateFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, dateFormulaCell.getDate());
		            		 }
		        		 }else if(fieldClass.getName().equals("java.lang.Boolean")||fieldClass.getName().equals("boolean")){//布尔类型
		        			 if(cellType.equals(CellType.BOOLEAN)){
		            			 BooleanCell booleanCell = (BooleanCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanCell.getValue());
		            		 }else if(cellType.equals(CellType.BOOLEAN_FORMULA)){
		            			 BooleanFormulaCell booleanFormulaCell = (BooleanFormulaCell)tmpColumns;
		            			 PropertyUtils.setProperty(theme, expCode, booleanFormulaCell.getValue());
		            		 }
		        		 }else{//数字类型
		        			 double d = 0;
		        			 if(cellType.equals(CellType.NUMBER)){
		            			 NumberCell numberCell = (NumberCell)tmpColumns;
		            			 d = numberCell.getValue();
		            		 }else if(cellType.equals(CellType.NUMBER_FORMULA)){
		            			 NumberFormulaCell numberFormulaCell = (NumberFormulaCell)tmpColumns;
		            			 d = numberFormulaCell.getValue();            			 
		            		 }
		        			 if(fieldClass.getName().equals("java.lang.Integer")||
		        				fieldClass.getName().equals("int")){
		        				 PropertyUtils.setProperty(theme, expCode,new Double(d).intValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Long")||
		        					  fieldClass.getName().equals("long")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).longValue());
		        			 }else if(fieldClass.getName().equals("java.lang.Float")||
		        					  fieldClass.getName().equals("float")){
		        				 PropertyUtils.setProperty(theme, expCode, new Double(d).floatValue());
		        			 }else
		        				 PropertyUtils.setProperty(theme, expCode, d);
		        		 }
		        	}else if(StaticVariable.xls_theme_defined.equals(expType)){//自定义
		        		if("themeInBankParentName".equals(expCode)){
		        			themeInBankParentName = tmpColumns.getContents();
		        		}else if("themeInBankName".equals(expCode)){
		        			themeInBankName = tmpColumns.getContents();
		        		}else if("themeType".equals(expCode)){//类型
		        			if("全部".equals(tmpColumns.getContents())){//所属题库类型 10-正式 20-模拟 30-非正式 40-都可以
		        				theme.setType("40");
		        			}else if("练习".equals(tmpColumns.getContents())){
		        				theme.setType("20");
		        			}else if("考试".equals(tmpColumns.getContents())){
		        				theme.setType("10");
		        			}
		        		}else if("themeDegree".equals(expCode)){//难度系数
		        			if("难".equals(tmpColumns.getContents())){
		        				theme.setDegree(15);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("中等".equals(tmpColumns.getContents())){
		        				theme.setDegree(10);//难度 5：容易,10：一般15：难,20：很难
		        			}else if("易".equals(tmpColumns.getContents())){
		        				theme.setDegree(5);//难度 5：容易,10：一般15：难,20：很难
		        			}
		        		
		        		}
		        	}else if(StaticVariable.xls_theme_rightAns.equals(expType)){//正确答案
		        		ThemeAnswerkey themeAnswerkey = new ThemeAnswerkey();
	        			themeAnswerkey.setAnswerkeyValue(tmpColumns.getContents());
	        			themeAnswerkey.setTheme(theme);
	        			themeAnswerkey.setIsRight(10);////是否正确 5：否,10：是
	        			themeAnswerkey.setSortNum(0);
	        			theme.getThemeAnswerkeies().add(themeAnswerkey);
		        	}
		        }
		        
		        this.replaceImg(theme, impPackageName, imgFileMap);//替换图片
		        //System.out.println(theme.getThemeName()+"  "+themeInBankParentName+"  "+themeInBankName+"------------------------------");
		        if(theme.getThemeName()!=null && !"".equals(theme.getThemeName().trim())  && !"null".equals(theme.getThemeName().trim())){
		        	 if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)
				        		&& themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get(themeInBankParentName+"###"+themeInBankName);
				        	if(themeBank == null){
				        		ThemeBank parentthemeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        		if(parentthemeBank == null){
				        			parentthemeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess, null, bankType);
				        			if(parentthemeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,parentthemeBank);
				        		}
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,parentthemeBank, bankType);
				        		if(themeBank!=null){
				        			themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        			themeInBankMap.put(themeInBankParentName+"###"+themeInBankName,themeBank);
				        		}
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        }else if(themeInBankParentName!=null && !"".equals(themeInBankParentName) && !"null".equals(themeInBankParentName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankParentName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankParentName, themeInBankParentName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankParentName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
				        }else if(themeInBankName!=null && !"".equals(themeInBankName) && !"null".equals(themeInBankName)){
				        	ThemeBank themeBank = themeInBankMap.get("BANK_"+themeInBankName);
				        	if(themeBank == null){
				        		themeBank = this.addThemeBank(themeInBankName, themeInBankName, usersess,null, bankType);
				        		if(themeBank!=null)themeInBankMap.put("BANK_"+themeInBankName,themeBank);
				        	}
				        	if(themeBank!=null){
				        		ThemeInBank themeInBank = new ThemeInBank();
				        		themeInBank.setTheme(theme);
				        		themeInBank.setThemeBank(themeBank);
				        		themeInBank.setBankType(themeBank.getBankType());
				        		themeInBank.setBankOrganId(themeBank.getOrganId());
				        		themeInBank.setBankOrganName(themeBank.getOrganName());
				        		themeInBank.setBankPublic(themeBank.getBankPublic());
				        		themeInBank.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
				        		themeInBank.setCreatedBy(usersess.getEmployeeName());//创建人
				        		themeInBank.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
				        		themeInBank.setOrganId(usersess.getOrganId());//机构ID
				        		themeInBank.setOrganName(usersess.getOrganAlias());//机构名称
				        		theme.getThemeInBanks().add(themeInBank);
				        		this.saveThemeBankOtherInfo(theme, themeBank, usersess, 0);
				        		
				        		try{
									theme.setSortNum(Long.parseLong(themeBank.getThemeBankCode()));
								}catch(Exception e){
									theme.setSortNum(new Long(999999));
									//e.printStackTrace();
								}
				        	}
				        	
					    }
		        	theme.setCheckRemark(ThemeCheckOption.checkTheme(theme));
		        	if(theme.getDefaultScore()==null){
		        		theme.setDefaultScore(new Double(1));
		        	}
		        	if(theme.getCheckRemark()!=null && !"".equals(theme.getCheckRemark())){
			            theme.setState(un_import_init_state);
			        }
		        	if(theme.getThemeInBanks()!=null && theme.getThemeInBanks().size()==1){
			        	ThemeBank themeBank = theme.getThemeInBanks().get(0).getThemeBank();
			        	if(themeCrcMap.get(themeBank.getThemeBankId()) == null){
		        			ThemeDao themeDao = (ThemeDao)this.getDao();
		        			Map _themeCrcMap = themeDao.getThemeBankAndThemeCrcMap(themeBank.getThemeBankId());
		        			themeCrcMap.putAll(_themeCrcMap);
		        			themeCrcMap.put(themeBank.getThemeBankId(),"a");
		        		}
			        	if(themeCrcMap.get(themeBank.getThemeBankId()+"@"+theme.getThemeCrc())==null){
				        	list.add(theme);
				        }
			        }else if(themeCrcMap.get(theme.getThemeCrc())==null){
			        	list.add(theme);
			        }
		        }
	        }
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		return list;
	}
	
	private final static double img_width_Max = 400.0;
	private final static double img_height_Max = 200.0;
	private void replaceImg(Theme theme,String impPackageName,Map<String,File> imgFileMap) throws Exception{
		if("是".equals(theme.getHaveImages()) 
        		&& theme.getImagesNames()!=null && !"".equals(theme.getImagesNames())){
        	String imagesNames = theme.getImagesNames();
        	imagesNames = imagesNames.replaceAll(" ", ",");
        	imagesNames = imagesNames.replaceAll("\n", ",");
        	String[] imagesNameArr = imagesNames.split(",");
        	theme.setImagesNames(imagesNames);
        	theme.setImagesPackageName(impPackageName);
        	theme.setImagesPath("");
        	theme.setImagesSucc("");
        	if(imagesNameArr.length>0){
	        	for(int i=0;i<imagesNameArr.length;i++){
	        		String imagesName = imagesNameArr[i];
	        		imagesName = imagesName.replaceAll(" ", "");
	        		File imgFile = imgFileMap.get(CharsetSwitch.CharacterLowerCase(imagesName));
	        		if(imgFile!=null){
	        			BufferedImage sourceImg =ImageIO.read(new FileInputStream(imgFile));
	        			double width = sourceImg.getWidth();
	        			double height = sourceImg.getHeight();
	        			boolean isCreatNewFile = true;
	        			if(width>img_width_Max && height>img_height_Max){	
	        				height = Math.floor(height*img_width_Max/width);
	        				width = img_width_Max;
	        			}else if(width>img_width_Max){
	        				height = Math.floor(height*img_width_Max/width);
	        				width = img_width_Max;
	        			}else if(height>img_height_Max){
	        				width = Math.floor(width*img_height_Max/height);
	        				height = img_height_Max;
	        			}else{
	        				isCreatNewFile = false;
	        			}
	        			if(isCreatNewFile){
	        				String filePath = imgFile.getPath();
	        				String fileName = imgFile.getName();
	        				
	        				String fileNewPath = filePath.substring(0,filePath.length() - fileName.length());
	        				String fileNewName = "s"+fileName;
	        				String fileType = "";
	        				StringTokenizer st = new StringTokenizer(fileName,".");
	        				while(st.hasMoreElements()){
	        					fileType = (String)st.nextElement();
	        				}
	        				String outputPicName = fileNewPath + fileNewName;
	        				try{
	        					BufferedImage Bi = ImageIO.read(imgFile);
	        				    BufferedImage tag = new BufferedImage((int)width,(int)height,BufferedImage.TYPE_INT_RGB);
	        				    tag.getGraphics().drawImage(Bi.getScaledInstance((int)width,(int)height,BufferedImage.SCALE_SMOOTH),0,0,null);
	        				    File littleFile = new File(outputPicName);
	        				    ImageIO.write(tag, fileType, littleFile);	
	        				    
	        				    imgFile = littleFile;
	        				}catch(Exception ex){
	        					ex.printStackTrace();
	        				}
	        				
	        			}
	        			
		        		theme.setImagesPath(theme.getImagesPath()+imgFile.getPath()+",");
	        			theme.setImagesSucc(theme.getImagesSucc()+"T,");
	        			String themeName = theme.getThemeName();
	        			if(themeName.indexOf(imagesName)!=-1){
	        				String[] imglist = imgFile.getPath().split(impPackageName);
	        				StringTokenizer st = new StringTokenizer(imglist[1],"\\");
	    					String tmpPath="upload/imp/"+impPackageName;
	    			        while(st.hasMoreElements()){
	    			             String tmp = (String)st.nextElement();
	    			             tmpPath+="/"+tmp;
	    			        }
	        				themeName = themeName.replaceAll(imagesName,"<img src=\""+tmpPath+"\" />");
	        				theme.setThemeName(themeName);
	        			}
	        			for(int y=0;y<theme.getThemeAnswerkeies().size();y++){
	        				ThemeAnswerkey themeAnswerkey= theme.getThemeAnswerkeies().get(y);
	        				String ansvalue = themeAnswerkey.getAnswerkeyValue();
	        				if(ansvalue.indexOf(imagesName)!=-1){
	        					String[] imglist = imgFile.getPath().split(impPackageName);
	        					StringTokenizer st = new StringTokenizer(imglist[1],"\\");
	        					String tmpPath="upload/imp/"+impPackageName;
	        			        while(st.hasMoreElements()){
	        			             String tmp = (String)st.nextElement();
	        			             tmpPath+="/"+tmp;
	        			        }
	        					ansvalue = ansvalue.replaceAll(imagesName, "<img src=\""+tmpPath+"\" />");
	        					themeAnswerkey.setAnswerkeyValue(ansvalue);	
		        			}
	        			}
	        		}else{
	        			theme.setImagesSucc(theme.getImagesSucc()+"F,");
	        		}
	        	}
        	}
        	if(theme.getImagesPath().length()>0)theme.setImagesPath(theme.getImagesPath().substring(0,theme.getImagesPath().length()-1));
        	if(theme.getImagesSucc().length()>0)theme.setImagesSucc(theme.getImagesSucc().substring(0,theme.getImagesSucc().length()-1));
        }
	}
	
	private ThemeBank addThemeBank(String themeBankName,String themeBankCode,UserSession usersess,ThemeBank parentBank,
			String bankType){
		ThemeBank themeBank = new ThemeBank();
		themeBank.setCreatedBy(usersess.getEmployeeName());
		themeBank.setCreatedIdBy(usersess.getEmployeeId());
		themeBank.setCreationDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		themeBank.setThemeBankName(themeBankName);
		themeBank.setThemeBankCode(themeBankCode);
		themeBank.setPublicName(usersess.getEmployeeName());
		themeBank.setPublicTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		themeBank.setOrganId(usersess.getOrganId());
		themeBank.setOrganName(usersess.getOrganName());
		//private String bankLevelCode;
		themeBank.setSortNum(999999);
		//private String remark;
		themeBank.setIsL("10");
		themeBank.setBankPublic("20");
		themeBank.setBankType(bankType);
		if(parentBank!=null){
			themeBank.setThemeBank(parentBank);
		}
		//private String syncFlag;
		this.getDao().addEntity(themeBank);
		return themeBank;
	}
	
	/**
	 * 
	 * @author zhujian
	 * @description 设置相关的专业与岗位
	 * @param theme
	 * @param themeBank
	 * @param usersess
	 * @param exp_index
	 * @modified
	 */
	private void saveThemeBankOtherInfo(Theme theme,ThemeBank themeBank,UserSession usersess,int exp_index){
		if(themeBank!=null){
			ThemeSearchKey themeSearchKey = new ThemeSearchKey();
			themeSearchKey.setTheme(theme);
			themeSearchKey.setThemeBankId(themeBank.getThemeBankId());
			themeSearchKey.setThemeBankName(themeBank.getThemeBankName());
			themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
			themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
			themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
			themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
			themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
			themeSearchKey.setSortNum(exp_index);
			theme.getThemeSearchKeies().add(themeSearchKey);
			
			
			/*List<ThemeBankProfession> themeSpecialityFormList = themeBank.getThemeBankProfessions();
			if(themeSpecialityFormList!=null && themeSpecialityFormList.size()>0){
				for(int k=0;k<themeSpecialityFormList.size();k++){
					ThemeBankProfession themeSpecialityForm = (ThemeBankProfession)themeSpecialityFormList.get(k);
					if(themeSpecialityForm.getSpeciality()!=null){
						themeSearchKey = new ThemeSearchKey();
						themeSearchKey.setTheme(theme);
						themeSearchKey.setProfessionName(themeSpecialityForm.getSpeciality().getSpecialityname());
						themeSearchKey.setProfessionId(themeSpecialityForm.getSpeciality().getSpecialityid());
						themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
						themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
						themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
						themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
						themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
						themeSearchKey.setSortNum(k);
						theme.getThemeSearchKeies().add(themeSearchKey);
					}
					//service.add(themeSearchKey);
				}
			}
			
			List<ThemeBankPost> themePostFormList = themeBank.getThemeBankPosts();
			if(themePostFormList!=null && themePostFormList.size()>0){
				for(int k=0;k<themePostFormList.size();k++){
					ThemeBankPost themePostForm = (ThemeBankPost)themePostFormList.get(k);
					themeSearchKey = new ThemeSearchKey();
					themeSearchKey.setTheme(theme);
					themeSearchKey.setPostName(themePostForm.getPostName());
					themeSearchKey.setPostId(themePostForm.getPostId());
					themeSearchKey.setCreationDate(DateUtils.convertDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss"));//创建时间
					themeSearchKey.setCreatedBy(usersess.getEmployeeName());//创建人
					themeSearchKey.setCreatedIdBy(usersess.getEmployeeId());//创建人ID
					themeSearchKey.setOrganId(usersess.getOrganId());//机构ID
					themeSearchKey.setOrganName(usersess.getOrganAlias());//机构名称
					themeSearchKey.setSortNum(k);
					theme.getThemeSearchKeies().add(themeSearchKey);
					//service.add(themeSearchKey);
				}
			}*/
		
		}
	}
	
	/**
	 * 检查试题
	 * @description
	 * @modified
	 */
	public int[] saveAndcheckTheme(){
		ThemeDao themeDao = (ThemeDao)this.getDao();
		return themeDao.saveAndcheckTheme();
	}
}
