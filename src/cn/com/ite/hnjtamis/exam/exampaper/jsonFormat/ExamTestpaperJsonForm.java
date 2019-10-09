package cn.com.ite.hnjtamis.exam.exampaper.jsonFormat;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Comparator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;
import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeType;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.exampaper.jsonFormat.ExamTestpaperJsonForm</p>
 * <p>Description 考试题目信息</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月17日 上午9:54:28
 * @version 1.0
 * 
 * @modified records:
 */
public class ExamTestpaperJsonForm extends ExamJsonForm{

	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static void getAndSaveExamTestpaper(HttpServletRequest request,ExamUserTestpaper examUserTestpaper,
			ExamTestpaper examTestpaper,List<ThemeType> themeTypeList){
		String jsonStr = ExamTestpaperJsonForm.getJsonStrInExamTestpaper(examTestpaper,themeTypeList);
		examTestpaper.setExamFileTitle(jsonStr);
		String fileName = "title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
		String path =  ExamVariable.getExamFilePath(request) + fileName ;
		//System.out.println(path);
		FileOption.saveStringToFile(path, jsonStr,"UTF-8");
	}
	
	public static boolean isHaveFile(HttpServletRequest request,ExamUserTestpaper examUserTestpaper){
		String fileName = "title_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
		String path =  ExamVariable.getExamFilePath(request) + fileName ;
		File file = new File(path);
		return file.exists();
	}
	
	
	public static void sortThemelist(List<ExamTestpaperTheme> themelist){
		Collections.sort(themelist, new Comparator<Object>() {
 			public int compare(Object o1, Object o2) {
 				ExamTestpaperTheme t1 = (ExamTestpaperTheme)o1;
 				ExamTestpaperTheme t2 = (ExamTestpaperTheme)o2;
 				if(t1.getRandomSortnum()==null && t1.getRandomSortnum()==null){
 					return 0;
 				}else if(t1.getRandomSortnum()==null){
 					return -1;
 				}else if(t2.getRandomSortnum()==null){
 					return 1;
 				}
 				int cz=t2.getRandomSortnum()-t1.getRandomSortnum();
 				if(cz == 0){
 					if(t1.getSortNum()==null && t1.getSortNum()==null){
 	 					return 0;
 	 				}else if(t1.getSortNum()==null){
 	 					return 1;
 	 				}else if(t2.getSortNum()==null){
 	 					return -1;
 	 				}
 					return 0;
 				}
 				return  cz > 0 ?  -1 : 1;
 			}
		});
	}
	
	public static void sortAnsList(List<ExamTestpaperAnswerkey> ansList){
		Collections.sort(ansList, new Comparator<Object>() {
 			public int compare(Object o1, Object o2) {
 				ExamTestpaperAnswerkey t1 = (ExamTestpaperAnswerkey)o1;
 				ExamTestpaperAnswerkey t2 = (ExamTestpaperAnswerkey)o2;
 				if(t1.getRandomSortnum()==null && t1.getRandomSortnum()==null){
 					return 0;
 				}else if(t1.getRandomSortnum()==null){
 					return -1;
 				}else if(t2.getRandomSortnum()==null){
 					return 1;
 				}
 				int cz=t2.getRandomSortnum()-t1.getRandomSortnum();
 				if(cz == 0){
 					if(t1.getSortNum()==null && t1.getSortNum()==null){
 	 					return 0;
 	 				}else if(t1.getSortNum()==null){
 	 					return 1;
 	 				}else if(t2.getSortNum()==null){
 	 					return -1;
 	 				}
 				}
 				return  cz > 0 ?  -1 : 1;
 			}
		});
	}
	
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInExamTestpaper(ExamTestpaper examTestpaper,List<ThemeType> themeTypeList){
		return  getJsonStrInExamTestpaper(examTestpaper,themeTypeList,
				examTestpaper.getExamTestpaperThemes(),new HashMap<String,List<ExamTestpaperAnswerkey>>());
		
	}
	
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInExamTestpaper(ExamTestpaper examTestpaper,List<ThemeType> themeTypeList,
			List<ExamTestpaperTheme> examTestpaperThemelist,Map<String,List<ExamTestpaperAnswerkey>> ansListMap){
		String examTestpaperStr = "";
		int themeIndex = 1;
		if(themeTypeList!=null && themeTypeList.size()>0){
			examTestpaperStr += "{";
			examTestpaperStr += "examId:'"+examTestpaper.getExamId()+"',";
			examTestpaperStr += "testPaperId:'"+examTestpaper.getExamTestpaperId()+"'";
			if(themeTypeList.size()>0){
				examTestpaperStr+=",themeType:[";
				List<ExamTestpaperTheme> themelist = examTestpaperThemelist==null ? examTestpaper.getExamTestpaperThemes() : examTestpaperThemelist;
				sortThemelist(themelist);
				for(int k=0;k<themeTypeList.size();k++){
					ThemeType themeType = (ThemeType)themeTypeList.get(k);
					examTestpaperStr+="{themeTypeId:'"+themeType.getThemeTypeId()+"',";
					examTestpaperStr+="themeTypeName:'"+stringToJson(themeType.getThemeTypeName())+"',";
					examTestpaperStr+="type:'"+themeType.getThemeType()+"'";
					if(themelist.size()>0){
						examTestpaperStr+=",theme:[";
						boolean isAddTheme = false;
						for(int i=0;i<themelist.size();i++){
							ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themelist.get(i);
							if(themeType.getThemeTypeId().equals(examTestpaperTheme.getThemeTypeId())
									&& ("0".equals(examTestpaperTheme.getScoreType()) || "1".equals(examTestpaperTheme.getScoreType()))){
								examTestpaperStr+="{themeId:'"+examTestpaperTheme.getExamTestpaperThemeId()+"',";
								examTestpaperStr+="sort:"+themeIndex+",";
								examTestpaperTheme.setRandomSortnum(themeIndex);
								examTestpaperStr+="fraction:'"+examTestpaperTheme.getDefaultScore()+"',";
								examTestpaperStr+="eachline:'"+examTestpaperTheme.getEachline()+"',";
								examTestpaperStr+="explain:'"+examTestpaperTheme.getExplain()+"',";
								examTestpaperStr+="value:'"+CharsetSwitchUtil.encode(stringToJson(examTestpaperTheme.getThemeName()))+"'";
								
								List<ExamTestpaperAnswerkey> ansList = ansListMap.get(examTestpaperTheme.getExamTestpaperThemeId());
								if(ansList==null) ansList = examTestpaperTheme.getExamTestpaperAnswerkeies();
								if(ansList!=null && ansList.size()>0){
									examTestpaperStr+=",answer:[";
									sortAnsList(ansList);
									for(int tt=0;tt<ansList.size();tt++){
										ExamTestpaperAnswerkey answerkey = (ExamTestpaperAnswerkey)ansList.get(tt);
										answerkey.setRandomSortnum(tt+1);
										examTestpaperStr+="{ansSort:'"+answerkey.getRandomSortnum()+"',ansValue:'"+CharsetSwitchUtil.encode(stringToJson(answerkey.getAnswerkeyValue()))+"'},";
									}
									examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
									examTestpaperStr+="]";
								}
								examTestpaperStr+="},";
								isAddTheme = true;
								themeIndex++;
							}
						}
						if(isAddTheme){
							examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
						}
						examTestpaperStr+="]";
					}
					examTestpaperStr+="},";
				}
				examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				examTestpaperStr+="]";
			}
			examTestpaperStr+="}";
		}
		return examTestpaperStr;
	}
}
