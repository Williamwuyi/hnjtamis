package cn.com.ite.hnjtamis.exam.testpaper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.hnjtamis.common.StaticVariable;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.TestpaperThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeInBank;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperServiceImpl</p>
 * <p>Description 试卷模版生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:07:03
 * @version 1.0
 * 
 * @modified records:
 */
public class TestpaperServiceImpl extends DefaultServiceImpl implements TestpaperService {

	/**
	 * 获取一个顶级部门下所有部门ID
	 * @description
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<String> getAllDeptInDeptId(String deptId){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		return testpaperDao.getAllDeptInDeptId(deptId);
	}
	/**
	 * 根据主键获取试题Map
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public Map<String,Theme> getThemeInIds(String[] ids){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		return testpaperDao.getThemeInIds(ids);
	}
	/**
	 * 根据参数查询考题
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeByParam(Map term){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		return testpaperDao.getThemeByParam(term);
	}
	
	/**
	 * 查询数据字典
	 * @author zhujian
	 * @description
	 * @param dtType
	 * @return
	 * @modified
	 */
	public List<Dictionary> getDictionaryTypeList(String dtType){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		return testpaperDao.getDictionaryTypeList(dtType);
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 根据testpaperId查询试卷的试题
	 * @param testpaperId
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getTestpaperThemeList(String testpaperId){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		List<TestpaperThemeForm> themeInTemplateList = new ArrayList<TestpaperThemeForm>();
		Map<String,String> param = new HashMap<String,String>();
		param.put("testpaperId", testpaperId);
		Map<String,Boolean> sortMap = new HashMap<String,Boolean>();
		sortMap.put("sortNum", true);
		List<TestpaperTheme> testpaperthemeList = null;
		try{
			testpaperthemeList = this.queryData("themeInTemplateHql", param , sortMap);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(testpaperthemeList!=null && testpaperthemeList.size() > 0){
			String[] ansSort = StaticVariable.themeAnsSort;
			
			Map<String,List<TestpaperThemeAnswerkey>> ansMap = new HashMap<String,List<TestpaperThemeAnswerkey>>();
			List<TestpaperThemeAnswerkey> ansList = this.queryData("queryTestpaperThemeAnswerkeyInTestpaperHql", param , sortMap);
			if(ansList!=null){
				for(int i=0;i<ansList.size();i++){
					TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)ansList.get(i);
					String ansKey = testpaperThemeAnswerkey.getTestpaperTheme().getTestpaperThemeId();
					List<TestpaperThemeAnswerkey> tmplist = ansMap.get(ansKey);
					if(tmplist==null)tmplist = new ArrayList<TestpaperThemeAnswerkey>();
					tmplist.add(testpaperThemeAnswerkey);
					ansMap.put(ansKey,tmplist);
				}
			}
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			List<ThemeType> themeTypeList = this.getDao().findAll(ThemeType.class);
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeId(), themeType);
			}
			
			Map<String,Theme> themeMap = new HashMap<String,Theme>(); 
			Map<String,List<ThemeAnswerkey>> ansInThemeMap = new HashMap<String,List<ThemeAnswerkey>>();
			List<Theme> themeList = this.queryData("queryThemeInTespaperIdHql", param , sortMap,Theme.class);
			if(themeList!=null){
				String themeIds ="";
				for(int i=0;i<themeList.size();i++){
					Theme theme = themeList.get(i);
					themeMap.put(theme.getThemeId(), theme);
					themeIds += "'"+theme.getThemeId()+"',";
					if(i>0 && (i%100==0 || i==themeList.size()-1)){
						themeIds = themeIds.substring(0,themeIds.length()-1);
						Map<String,List<ThemeAnswerkey>> _ansMap = testpaperDao.getThemeAnswerkeyByThemeIds(themeIds);
						ansInThemeMap.putAll(_ansMap);
						themeIds = "";
					}
				}
			}
			Map<String,ThemeBank>  themeBankMap = new HashMap<>();
			List<ThemeBank> themeBankList = this.queryAllDate(ThemeBank.class);
			for(ThemeBank themeBank : themeBankList){
				themeBankMap.put(themeBank.getThemeBankId(), themeBank);
			}
			List<ThemeInBank> themeInBankList = this.queryData("queryThemeInBankInTespaperIdHql", param , null,ThemeInBank.class);
			Map<String,List<ThemeBank>> themeInBankMap =new HashMap<>();
			for(ThemeInBank themeInBank : themeInBankList){
				if(themeInBank.getThemeBank()!=null && themeInBank.getTheme()!=null){
					String themeId = themeInBank.getTheme().getThemeId();
					List<ThemeBank> tmplist = themeInBankMap.get(themeId);
					if(tmplist == null)tmplist = new ArrayList<>();
					ThemeBank themeBank = themeBankMap.get(themeInBank.getThemeBank().getThemeBankId());
					if(themeBank!=null)tmplist.add(themeBank);
					themeInBankMap.put(themeId,tmplist);
				}
			}
			
			
			for(int i=0;i<testpaperthemeList.size();i++){
				TestpaperTheme testpaperTheme = (TestpaperTheme)testpaperthemeList.get(i);
				Theme theme = themeMap.get(testpaperTheme.getTheme().getThemeId());
				if(theme == null){
					theme = testpaperTheme.getTheme();
				}else{
					testpaperTheme.setTheme(theme);
				}
				if(theme!=null){
					List<ThemeAnswerkey> themeAnswerkeyList = ansInThemeMap.get(theme.getThemeId());
					if(themeAnswerkeyList!=null){
						theme.setThemeAnswerkeies(themeAnswerkeyList);
						testpaperTheme.setTheme(theme);
					}
				}
				//ThemeType themeType = testpaperTheme.getThemeType();
				ThemeType themeType = themeTypeMap.get(theme.getThemeType().getThemeTypeId());
				if(themeType == null){
					themeType = theme.getThemeType();
					themeTypeMap.put(theme.getThemeType().getThemeTypeId(), themeType);
				}else{
					theme.setThemeType(themeType);
				}
				TestpaperThemeForm testpaperThemeForm = new TestpaperThemeForm();
				//BeanUtils.copyProperties(testpaperTheme, testpaperThemeForm);
				List<ThemeBank> banklist = themeInBankMap.get(theme.getThemeId());
				String themeBankName = "";
				if(banklist!=null && banklist.size()>0){
					for(ThemeBank themeBank: banklist){
						if("".equals(themeBankName)){
							themeBankName = themeBank.getThemeBankName();
						}else{
							themeBankName += ","+themeBank.getThemeBankName();
						}
					}
				}
				testpaperThemeForm.setThemeBankName(themeBankName);
				testpaperThemeForm.setTestpaperThemeId(testpaperTheme.getTestpaperThemeId());//试卷模版-试题ID
				testpaperThemeForm.setThemeType(themeType);//试题类型
				testpaperThemeForm.setTheme(theme);//试题
				testpaperThemeForm.setTestpaper(testpaperTheme.getTestpaper());//试卷
				testpaperThemeForm.setThemeTypeName(testpaperTheme.getThemeTypeName());//试题类型名称
				testpaperThemeForm.setThemeTypeId(themeType!=null?themeType.getThemeTypeId():null);
				testpaperThemeForm.setThemeName(testpaperTheme.getThemeName());//试题
				testpaperThemeForm.setDegree(testpaperTheme.getDegree());//难度 5：容易,10：一般15：难,20：很难
				testpaperThemeForm.setKnowledgePoint(testpaperTheme.getKnowledgePoint());//所属知识点
				testpaperThemeForm.setEachline(testpaperTheme.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
				testpaperThemeForm.setWriteUser(testpaperTheme.getWriteUser());//出题人
				testpaperThemeForm.setExplain(testpaperTheme.getExplain());//注解
				testpaperThemeForm.setDefaultScore(testpaperTheme.getDefaultScore());//分数
				testpaperThemeForm.setThemeSetNum(testpaperTheme.getThemeSetNum());//出题次数
				testpaperThemeForm.setThemePeopleNum(testpaperTheme.getThemePeopleNum());//考试人次
				testpaperThemeForm.setThemeRightNum(testpaperTheme.getThemeRightNum());///答题正确数
				testpaperThemeForm.setSortNum(testpaperTheme.getSortNum()!=null ? testpaperTheme.getSortNum().intValue()+"" : null);///试题排序（专业4位+题型4位+题目8位）
				testpaperThemeForm.setScoreType(testpaperTheme.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
				testpaperThemeForm.setRemark(testpaperTheme.getRemark());//备注
				testpaperThemeForm.setState(testpaperTheme.getState());//状态 5:保存10:上报15:发布20:打回
				testpaperThemeForm.setIsUse(testpaperTheme.getIsUse());//有否有效 5:有效 10：无效
				//testpaperThemeForm.setSyncFlag(testpaperTheme.getSyncFlag());////同步标志
				testpaperThemeForm.setOrganId(testpaperTheme.getOrganId());//机构ID
				testpaperThemeForm.setOrganName(testpaperTheme.getOrganName());//机构名
				//testpaperThemeForm.setLastUpdateDate(testpaperTheme.getLastUpdateDate());///最后修改时间
				//testpaperThemeForm.setLastUpdatedBy(testpaperTheme.getLastUpdatedBy());//最后修改人
				//testpaperThemeForm.setLastUpdatedIdBy(testpaperTheme.getLastUpdatedIdBy());//最后修改人ID
				//testpaperThemeForm.setCreationDate(testpaperTheme.getCreationDate());//创建时间
				//testpaperThemeForm.setCreatedBy(testpaperTheme.getCreatedBy());//创建人
				//testpaperThemeForm.setCreatedIdBy(testpaperTheme.getCreatedIdBy());//创建人ID
				//testpaperThemeForm.setRightAnswerkey(testpaperTheme.getRightAnswerkey());//正确答案
				//testpaperThemeForm.setRightAnswerkeyId(testpaperTheme.getRightAnswerkeyId());//正确答案ID(单选、多选用),多个逗号隔开
				
				testpaperThemeForm.setThemeId(theme!=null ? theme.getThemeId() : null);
				testpaperThemeForm.setThemeTypeName(themeType!=null ? themeType.getThemeTypeName() : null);
				testpaperThemeForm.setThemeTypeId(themeType!=null ? themeType.getThemeTypeId() : null);
				testpaperThemeForm.setSortIndex(i+1);
				int typeInTheme = themeType!=null ? Integer.parseInt(themeType.getThemeType()) : -1;
				
				List<TestpaperThemeAnswerkey> anslist = ansMap.get(testpaperTheme.getTestpaperThemeId());//testpaperTheme.getTestpaperThemeAnswerkeies();
				if(anslist==null){
					anslist = testpaperTheme.getTestpaperThemeAnswerkeies();
				}else{
					testpaperTheme.setTestpaperThemeAnswerkeies(anslist);
				}
				if(anslist!=null && anslist.size()>0){
					String answerkeyStr = "";
					for(int j = 0 ;j < anslist.size() ; j++){
						TestpaperThemeAnswerkey testpaperThemeAnswerkey = (TestpaperThemeAnswerkey)anslist.get(j);
						if(testpaperThemeAnswerkey!=null && (typeInTheme==5 || typeInTheme==10 || typeInTheme==25)){
							if(testpaperThemeAnswerkey.getIsRight()!=null && testpaperThemeAnswerkey.getIsRight().intValue() == 10){
								answerkeyStr+="<font color=blue>"+ansSort[j]+"."+testpaperThemeAnswerkey.getAnswerkeyValue()+"</font><br>";
							}else{
								answerkeyStr+=ansSort[j]+"."+testpaperThemeAnswerkey.getAnswerkeyValue()+"<br>";
							}
							
						}else{
							answerkeyStr+="("+(j+1)+")"+testpaperThemeAnswerkey.getAnswerkeyValue()+"<br>";
						}
					}
					testpaperThemeForm.setAnswerkeyStr(answerkeyStr);
				}
				themeInTemplateList.add(testpaperThemeForm);
			}
		}
		return themeInTemplateList;
	}
	
	public int getHandAddThemeListCount(String selectThemeIds,Map paramMap){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		int len = testpaperDao.getHandAddThemeListCount(selectThemeIds, paramMap);
		return len;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 获取手工选题
	 * @param selectThemeIds
	 * @param paramMap 过滤条件
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getHandAddThemeList(String selectThemeIds,Map paramMap){
		List<TestpaperThemeForm> themeInTemplateList = new ArrayList<TestpaperThemeForm>();
		String themeBankIds = (String)paramMap.get("themeBankIds");
		
		
		
		//Map term = new HashMap();
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		List<Theme> themeList = testpaperDao.getHandAddThemeList(selectThemeIds, paramMap);
		/*if(selectThemeIds!=null && !"".equals(selectThemeIds) && !"null".equals(selectThemeIds)
				&& themeBankIds!=null && !"".equals(themeBankIds) && !"null".equals(themeBankIds)){
			term.put("selectThemeIds", selectThemeIds);
			term.put("themeBankIds",(String)paramMap.get("themeBankIds"));
			themeList = (List<Theme>)this.queryData("handThemeAllParamHql", term, null);//(Theme.class);
		}else if((selectThemeIds==null || "".equals(selectThemeIds) || "null".equals(selectThemeIds))
				&& themeBankIds!=null && !"".equals(themeBankIds) && !"null".equals(themeBankIds)){
			term.put("themeBankIds",(String)paramMap.get("themeBankIds"));
			themeList = (List<Theme>)this.queryData("handThemeOnlyThemeBankHql", term, null);//(Theme.class);
		}else{
			term.put("selectThemeIds", selectThemeIds);
			term.put("themeBankIds",(String)paramMap.get("themeBankIds"));
			themeList = (List<Theme>)this.queryData("handThemeHql", term, null);//(Theme.class);
		}*/
		if(themeList!=null){
			String[] ansSort = StaticVariable.themeAnsSort;
			int sortIndex = 1;
			String xz = (String)paramMap.get("xz");
			if("page".equals(xz)){
				int row_start = (int)paramMap.get("row_start");
				sortIndex=row_start+1;
			}
			
			Map<String,List<ThemeAnswerkey>> ansMap = new HashMap<String,List<ThemeAnswerkey>>();
			String themeIds ="";
			for(int j = 0;j<themeList.size();j++){
				Theme theme = (Theme)themeList.get(j);
				themeIds += "'"+theme.getThemeId()+"',";
				
				if(j>0 && (j%100==0 || j==themeList.size()-1)){
					themeIds = themeIds.substring(0,themeIds.length()-1);
					Map<String,List<ThemeAnswerkey>> _ansMap = testpaperDao.getThemeAnswerkeyByThemeIds(themeIds);
					ansMap.putAll(_ansMap);
					/*Map param = new HashMap();
					param.put("themeIds", themeIds);
					//queryAnswerkeyInThemeIdsHql
					List<ThemeAnswerkey> ansList = this.queryData("queryAnswerkeyInThemeIdsnewHql", param , new HashMap());
					if(ansList!=null){
						for(int i=0;i<ansList.size();i++){
							ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)ansList.get(i);
							String ansKey = themeAnswerkey.getTheme().getThemeId();
							List<ThemeAnswerkey> tmplist = ansMap.get(ansKey);
							if(tmplist==null)tmplist = new ArrayList<ThemeAnswerkey>();
							tmplist.add(themeAnswerkey);
							ansMap.put(ansKey,tmplist);
						}
					}*/
					themeIds = "";
				}
			}
			
			
			Map<String,ThemeType> themeTypeMap = new HashMap<String,ThemeType>();
			List<ThemeType> themeTypeList = this.getDao().findAll(ThemeType.class);
			for(int i=0;i<themeTypeList.size();i++){
				ThemeType themeType = themeTypeList.get(i);
				themeTypeMap.put(themeType.getThemeTypeId(), themeType);
			}
			
			Map<String,ThemeBank>  themeBankMap = new HashMap<>();
			List<ThemeBank> themeBankList = this.queryAllDate(ThemeBank.class);
			for(ThemeBank themeBank : themeBankList){
				themeBankMap.put(themeBank.getThemeBankId(), themeBank);
			}
			Map param = new HashMap();
			List<ThemeInBank> themeInBankList = new ArrayList();
			if(themeBankIds!=null && !"".equals(themeBankIds)){
				param.put("themeBankIds", themeBankIds); 
				themeInBankList = this.queryData("queryThemeInBankInBankIdsHql", param , null,ThemeInBank.class);
			}
			Map<String,List<ThemeBank>> themeInBankMap =new HashMap<>();
			for(ThemeInBank themeInBank : themeInBankList){
				if(themeInBank.getThemeBank()!=null && themeInBank.getTheme()!=null){
					String themeId = themeInBank.getTheme().getThemeId();
					List<ThemeBank> tmplist = themeInBankMap.get(themeId);
					if(tmplist == null)tmplist = new ArrayList<>();
					ThemeBank themeBank = themeBankMap.get(themeInBank.getThemeBank().getThemeBankId());
					if(themeBank!=null)tmplist.add(themeBank);
					themeInBankMap.put(themeId,tmplist);
				}
			}
			
			String oldThemeTypeName = null;
			int themeTypeSort = 0;
			for(int j = 0;j<themeList.size();j++){
				Theme theme = (Theme)themeList.get(j);
				ThemeType themeType = themeTypeMap.get(theme.getThemeType().getThemeTypeId());
				if(themeType == null){
					themeType = theme.getThemeType();
					themeTypeMap.put(theme.getThemeType().getThemeTypeId(), themeType);
				}else{
					theme.setThemeType(themeType);
				}
				TestpaperThemeForm testpaperThemeForm = new TestpaperThemeForm();
				List<ThemeBank> banklist = themeInBankMap.get(theme.getThemeId());
				String themeBankName = "";
				if(banklist!=null && banklist.size()>0){
					for(ThemeBank themeBank: banklist){
						if("".equals(themeBankName)){
							themeBankName = themeBank.getThemeBankName();
						}else{
							themeBankName += ","+themeBank.getThemeBankName();
						}
					}
				}
				testpaperThemeForm.setThemeBankName(themeBankName);
				//BeanUtils.copyProperties(theme, testpaperThemeForm);
				//testpaperThemeForm.setTestpaperThemeId(theme.get);//试卷模版-试题ID
				//private ThemeType themeType(theme.get);//试题类型
				//private Theme theme(theme.get);//试题
				testpaperThemeForm.setThemeId(theme.getThemeId());
				//private Testpaper testpaper(theme.get)(theme.get);//试卷
				testpaperThemeForm.setThemeTypeName(theme.getThemeTypeName());//试题类型名称
				testpaperThemeForm.setThemeTypeId(themeType!=null ? themeType.getThemeTypeId() : null);
				if(oldThemeTypeName == null){
					oldThemeTypeName = testpaperThemeForm.getThemeTypeName();
				}
				if(!oldThemeTypeName.equals(testpaperThemeForm.getThemeTypeName())){
					oldThemeTypeName = testpaperThemeForm.getThemeTypeName();
					themeTypeSort++;
				}
				testpaperThemeForm.setThemeTypeNameSort(StaticVariable.numberSort[themeTypeSort]);
				testpaperThemeForm.setThemeName(theme.getThemeName());//试题
				testpaperThemeForm.setDegree(theme.getDegree());//难度 5：容易,10：一般15：难,20：很难
				testpaperThemeForm.setKnowledgePoint(theme.getKnowledgePoint());//所属知识点
				testpaperThemeForm.setEachline(theme.getEachline());//答案显示方式 0:不换行　1:一行一个 2:一行二个 3：一行三个 4:一行四个
				testpaperThemeForm.setWriteUser(theme.getWriteUser());//出题人
				testpaperThemeForm.setExplain(theme.getExplain());//注解
				testpaperThemeForm.setDefaultScore(theme.getDefaultScore());//分数
				testpaperThemeForm.setThemeSetNum(theme.getThemeSetNum());//出题次数
				testpaperThemeForm.setThemePeopleNum(theme.getThemePeopleNum());//考试人次
				testpaperThemeForm.setThemeRightNum(theme.getThemeRightNum());///答题正确数
				//testpaperThemeForm.setSortNum(theme.getSortNum());///试题排序（专业4位+题型4位+题目8位）
				testpaperThemeForm.setScoreType(theme.getScoreType());//得分类型 0自动阅卷；1人工阅卷；2系统外阅卷
				testpaperThemeForm.setRemark(theme.getRemark());//备注
				//testpaperThemeForm.setState(theme.getState());//状态 5:保存10:上报15:发布20:打回
				testpaperThemeForm.setIsUse(theme.getIsUse());//有否有效 5:有效 10：无效
				//testpaperThemeForm.setSyncFlag(theme.getSyncFlag());////同步标志
				testpaperThemeForm.setOrganId(theme.getOrganId());//机构ID
				testpaperThemeForm.setOrganName(theme.getOrganName());//机构名
				//testpaperThemeForm.setLastUpdateDate(theme.getLastUpdateDate());///最后修改时间
				//testpaperThemeForm.setLastUpdatedBy(theme.getLastUpdatedBy());//最后修改人
				//testpaperThemeForm.setLastUpdatedIdBy(theme.getLastUpdatedIdBy());//最后修改人ID
				//testpaperThemeForm.setCreationDate(theme.getCreationDate());//创建时间
				//testpaperThemeForm.setCreatedBy(theme.getCreatedBy());//创建人
				//testpaperThemeForm.setCreatedIdBy(theme.getCreatedIdBy());//创建人ID
				//testpaperThemeForm.setRightAnswerkey(theme.getRightAnswerkey());//正确答案
				//testpaperThemeForm.setRightAnswerkeyId(theme.getRightAnswerkeyId());//正确答案ID(单选、多选用),多个逗号隔开
				testpaperThemeForm.setThemeId(theme.getThemeId());
				testpaperThemeForm.setTheme(theme);
				testpaperThemeForm.setThemeTypeName(themeType!=null ?themeType.getThemeTypeName() : null);	
				testpaperThemeForm.setThemeTypeId(themeType!=null ? themeType.getThemeTypeId() : null);
				testpaperThemeForm.setSortIndex(sortIndex);
				int typeInTheme = themeType!=null ? Integer.parseInt(themeType.getThemeType()) : -1;
				
				List<ThemeAnswerkey> anslist = ansMap.get(theme.getThemeId());
				if(anslist==null){
					anslist = theme.getThemeAnswerkeies();
				}else{
					theme.setThemeAnswerkeies(anslist);
				}
				if(anslist!=null && anslist.size()>0){
					String answerkeyStr = "";
					String answerkeyLiStr = "";
					for(int t = 0 ;t < anslist.size() ; t++){
						ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)anslist.get(t);
						if(themeAnswerkey!=null && (typeInTheme==5 || typeInTheme==10 || typeInTheme==25)){
							if(themeAnswerkey.getIsRight()!=null && themeAnswerkey.getIsRight().intValue() == 10){
								answerkeyStr+="<font color=blue>"+ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"</font><br>";
								answerkeyLiStr+="<li><font color=green style='font-weight:bold;'>"+ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"</font></li>";
							}else{
								answerkeyStr+=ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"<br>";
								answerkeyLiStr+="<li>"+ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"</li>";
							}
						}else{
							answerkeyStr+="("+(t+1)+")"+themeAnswerkey.getAnswerkeyValue()+"<br>";
							answerkeyLiStr+="<li style='width:88%;'>"+themeAnswerkey.getAnswerkeyValue()+"</li>";
						}
					}
					testpaperThemeForm.setAnswerkeyStr(answerkeyStr);
					testpaperThemeForm.setAnswerkeyLiStr(answerkeyLiStr);
				}
				
				themeInTemplateList.add(testpaperThemeForm);
				sortIndex++;
			}
		}
		return themeInTemplateList;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 根据模版生成试题，合成试卷
	 * @param tmplateArr
	 * @param themeBankIds
	 * @param examType
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @param employeeId 人员ID
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getThemeInTemplate(String[] tmplateArr,String themeBankIds,
			Integer examType,String choutiType,String employeeId,String relationType){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		List<TestpaperThemeForm> themeInTemplateList = new ArrayList<TestpaperThemeForm>();
		try{
			if(tmplateArr!=null && tmplateArr.length>0){
				Map<String,ThemeBank>  themeBankMap = new HashMap<>();
				List<ThemeBank> themeBankList = this.queryAllDate(ThemeBank.class);
				for(ThemeBank themeBank : themeBankList){
					themeBankMap.put(themeBank.getThemeBankId(), themeBank);
				}
				Map param2 = new HashMap();
				List<ThemeInBank> themeInBankList = new ArrayList();
				if(themeBankIds!=null && !"".equals(themeBankIds)){
					param2.put("themeBankIds", themeBankIds); 
					themeInBankList = this.queryData("queryThemeInBankInBankIdsHql", param2 , null,ThemeInBank.class);
				}
				Map<String,List<ThemeBank>> themeInBankMap =new HashMap<>();
				for(ThemeInBank themeInBank : themeInBankList){
					if(themeInBank.getThemeBank()!=null && themeInBank.getTheme()!=null){
						String themeId = themeInBank.getTheme().getThemeId();
						List<ThemeBank> tmplist = themeInBankMap.get(themeId);
						if(tmplist == null)tmplist = new ArrayList<>();
						ThemeBank themeBank = themeBankMap.get(themeInBank.getThemeBank().getThemeBankId());
						if(themeBank!=null)tmplist.add(themeBank);
						themeInBankMap.put(themeId,tmplist);
					}
				}
				
				
				Map themeTypeHaveMap = new HashMap();
				String[] ansSort = StaticVariable.themeAnsSort;
				int sortIndex = 1;
				Map<String,String> themeIdsMap = new HashMap<String,String>();
				for(int k = 0;k<tmplateArr.length;k++){
					//{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},{类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分}
					if(tmplateArr[k]!=null && !"".equals( tmplateArr[k])){
						String params = tmplateArr[k].substring(1,tmplateArr[k].length());
						String[] paramsArr = params.split(",");
						String themeTypeId = paramsArr[0];
						String specialityid = "undefined".equals(paramsArr[1]) || "null".equals(paramsArr[1]) ? null : paramsArr[1];
						String setThemetype =  paramsArr[2];
						themeBankIds = paramsArr[6];
						String haveKey = themeTypeId +"@@"+specialityid+"@@"+themeBankIds;
						
						if("10".equals(setThemetype) && themeTypeHaveMap.get(haveKey)==null){
							themeTypeHaveMap.put(haveKey,haveKey);
							int selectLength = paramsArr[3]!=null && !"".equals(paramsArr[3]) ? Integer.parseInt(paramsArr[3]) : 0;
							if(selectLength>0){
								Map param = new HashMap();
								param.put("themeTypeId", themeTypeId);
								param.put("specialityid", specialityid);
								param.put("themeBankIds", themeBankIds);
								
								//所属题库类型 type  10-正式 20-模拟 30-非正式 40-都可以
								// examType //考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
								if(examType!=null){
									if(examType.intValue() == 10 || examType.intValue()==20){
										param.put("examType", "10,40");
									}else if(examType.intValue() == 30){
										param.put("examType", "20,30,40");
									}else if(examType.intValue() == 40){
										param.put("examType", "20,40");
									}
								}
								if("onlyNotUserUse".equals(choutiType)){
									param.put("employeeId", employeeId);
								}else{
									param.put("employeeId", null);
								}
								param.put("relationType", relationType);
								Map sortMap = new HashMap();
								List themeList = testpaperDao.getThemeByParam(param);//this.queryData("themeSelectHql", param , sortMap);
								if(themeList!=null && themeList.size()>0){
									//int[] selectArr = new int[selectLength<themeList.size() ? selectLength : themeList.size()];
									int maxThemeLen = selectLength<themeList.size() ? selectLength : themeList.size();
									List<Integer> randomThemeNums = new ArrayList<>(); 
									
									int len = themeList.size();
									Map<String,String> randomMap = new HashMap<>();
									for(int i=0;i<maxThemeLen;){
										int rInt = (int)(Math.random()*len);
										if(randomMap.get(rInt+"")==null){
											Theme theme = (Theme)themeList.get(rInt);
											if(themeIdsMap.get(theme.getThemeId()) == null){
												//selectArr[i] = ; 
												randomThemeNums.add(new Integer(rInt));
												i++;
												randomMap.put(rInt+"","a");
												themeIdsMap.put(theme.getThemeId(),"a");
											}else{
												maxThemeLen--;
											}
										}
									}
									
									for(int j = 0;j<randomThemeNums.size();j++){
										Theme theme = (Theme)themeList.get(randomThemeNums.get(j).intValue());
										TestpaperThemeForm testpaperThemeForm = new TestpaperThemeForm();
										BeanUtils.copyProperties(theme, testpaperThemeForm);
										List<ThemeBank> banklist = themeInBankMap.get(theme.getThemeId());
										String themeBankName = "";
										if(banklist!=null && banklist.size()>0){
											for(ThemeBank themeBank: banklist){
												if("".equals(themeBankName)){
													themeBankName = themeBank.getThemeBankName();
												}else{
													themeBankName += ","+themeBank.getThemeBankName();
												}
											}
										}
										testpaperThemeForm.setThemeBankName(themeBankName);
										testpaperThemeForm.setThemeId(theme.getThemeId());
										testpaperThemeForm.setTheme(theme);
										testpaperThemeForm.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
										testpaperThemeForm.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);	
										testpaperThemeForm.setSortIndex(sortIndex);
										int typeInTheme = theme.getThemeType()!=null ? Integer.parseInt(theme.getThemeType().getThemeType()) : -1;
										
										List anslist = theme.getThemeAnswerkeies();
										if(anslist!=null && anslist.size()>0){
											String answerkeyStr = "";
											for(int t = 0 ;t < anslist.size() ; t++){
												ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)anslist.get(t);
												if(themeAnswerkey!=null && (typeInTheme==5 || typeInTheme==10 || typeInTheme==25)){
													if(themeAnswerkey.getIsRight()!=null && themeAnswerkey.getIsRight().intValue() == 10){
														answerkeyStr+="<font color=blue>"+ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"</font><br>";
													}else{
														answerkeyStr+=ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"<br>";
													}
												}else{
													answerkeyStr+="("+(t+1)+")"+themeAnswerkey.getAnswerkeyValue()+"<br>";
												}
											}
											testpaperThemeForm.setAnswerkeyStr(answerkeyStr);
										}
										
										themeInTemplateList.add(testpaperThemeForm);
										sortIndex++;
									}
								}
							}
						}else if("5".equals(setThemetype)&& themeTypeHaveMap.get(haveKey)==null){
							themeTypeHaveMap.put(haveKey,haveKey);
							double score = paramsArr[4]!=null && !"".equals(paramsArr[4]) ? Double.parseDouble(paramsArr[4]) : 0.0d;
							Map param = new HashMap();
							param.put("themeTypeId", themeTypeId);
							param.put("specialityid", specialityid);
							param.put("themeBankIds", themeBankIds);
							//所属题库类型 type  10-正式 20-模拟 30-非正式 40-都可以
							// examType //考试类型  10- 达标考试 20-竞赛考试 30-培训测试 40-模拟考试
							if(examType!=null){
								if(examType.intValue() == 10 || examType.intValue()==20){
									param.put("examType", "10,40");
								}else if(examType.intValue() == 30){
									param.put("examType", "20,30,40");
								}else if(examType.intValue() == 40){
									param.put("examType", "20,40");
								}
							}
							if("onlyNotUserUse".equals(choutiType)){
								param.put("employeeId", employeeId);
							}else{
								param.put("employeeId", null);
							}
							param.put("relationType", relationType);
							Map sortMap = new HashMap();
							List themeList = testpaperDao.getThemeByParam(param);//this.queryData("themeSelectHql", param , sortMap);
							
							if(themeList!=null && themeList.size()>0){
								List newthemeList = new ArrayList();
								for(int i=0;i<themeList.size();i++){
									Theme theme = (Theme)themeList.get(i);
									if(theme.getDefaultScore().doubleValue()<=score){
										newthemeList.add(theme);
									}
								}
								themeList = newthemeList;
								
								//最多抽题3W次
								for(int i=0,j=0;i<themeList.size() && j<30000;j++){
									//Random ran = new Random(themeList.size());
									//int rInt = ran.nextInt(themeList.size());
									int rInt = (int)(Math.random()*themeList.size());
									Theme theme = (Theme)themeList.get(rInt);
									if(themeIdsMap.get(theme.getThemeId()) != null){
										continue;
									}
									themeIdsMap.put(theme.getThemeId(),"a");
									if(theme.getDefaultScore()!=null && theme.getDefaultScore().doubleValue()<=score){
										TestpaperThemeForm testpaperThemeForm = new TestpaperThemeForm();
										BeanUtils.copyProperties(theme, testpaperThemeForm);
										testpaperThemeForm.setThemeId(theme.getThemeId());
										List<ThemeBank> banklist = themeInBankMap.get(theme.getThemeId());
										String themeBankName = "";
										if(banklist!=null && banklist.size()>0){
											for(ThemeBank themeBank: banklist){
												if("".equals(themeBankName)){
													themeBankName = themeBank.getThemeBankName();
												}else{
													themeBankName += ","+themeBank.getThemeBankName();
												}
											}
										}
										testpaperThemeForm.setThemeBankName(themeBankName);
										testpaperThemeForm.setTheme(theme);	
										testpaperThemeForm.setThemeTypeName(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeName() : null);
										testpaperThemeForm.setThemeTypeId(theme.getThemeType()!=null ? theme.getThemeType().getThemeTypeId() : null);
										testpaperThemeForm.setSortIndex(sortIndex);
										int typeInTheme = theme.getThemeType()!=null ? Integer.parseInt(theme.getThemeType().getThemeType()) : -1;
										
										List anslist = theme.getThemeAnswerkeies();
										if(anslist!=null && anslist.size()>0){
											String answerkeyStr = "";
											for(int t = 0 ;t < anslist.size() ; t++){
												ThemeAnswerkey themeAnswerkey = (ThemeAnswerkey)anslist.get(t);
												if(themeAnswerkey!=null && (typeInTheme==5 || typeInTheme==10 || typeInTheme==25)){
													//answerkeyStr+=ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"<br>";
													if(themeAnswerkey.getIsRight()!=null && themeAnswerkey.getIsRight().intValue() == 10){
														answerkeyStr+="<font color=blue>"+ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"</font><br>";
													}else{
														answerkeyStr+=ansSort[t]+"."+themeAnswerkey.getAnswerkeyValue()+"<br>";
													}
												}else{
													answerkeyStr+="("+(t+1)+")"+themeAnswerkey.getAnswerkeyValue()+"<br>";
												}
											}
											testpaperThemeForm.setAnswerkeyStr(answerkeyStr);
										}
										
										themeInTemplateList.add(testpaperThemeForm);
										sortIndex++;
										score = score - theme.getDefaultScore().doubleValue();
										themeList.remove(rInt);
									}else{
										themeList.remove(rInt);
									}
									if(score ==0 )break;
									
								}
								
							}
							
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return themeInTemplateList;
	}
	 

	/**
	 * 同步试卷存在的题库与选择试题的题库一致
	 * @description
	 * @param testpaperId
	 * @param organId
	 * @param organName
	 * @modified
	 */
	public void saveAndSyncThemeBank(String testpaperId,String organId,String organName){
		TestpaperDao testpaperDao = (TestpaperDao)this.getDao();
		testpaperDao.saveAndSyncThemeBank( testpaperId, organId, organName);
	}
}
