package cn.com.ite.hnjtamis.exam.exampaper.jsonFormat;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.hnjtamis.common.ExamVariable;
import cn.com.ite.hnjtamis.common.FileOption;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaper;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperAnswerkey;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamTestpaperTheme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ExamUserTestpaper;

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
public class ExamTestpaperAnswerJsonForm extends ExamJsonForm{

	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static void getAndSaveExamTestpaperAnswer(HttpServletRequest request,ExamUserTestpaper examUserTestpaper,ExamTestpaper examTestpaper){
		String jsonStr = ExamTestpaperAnswerJsonForm.getJsonStrInExamTestpaperAnswer(examTestpaper);
		examTestpaper.setExamFileAnswerkey(jsonStr);
		String fileName ="ans_"+examUserTestpaper.getExam().getExamId()+"_"+examUserTestpaper.getInticket()+".txt";
		String path = ExamVariable.getExamUserFilePath(request);
		path = path+examUserTestpaper.getExam().getExamId()+System.getProperty("file.separator");
		ExamVariable.creatFilePath(path);
		path = path + fileName;
		FileOption.saveStringToFile(path, jsonStr,"UTF-8");
	}
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInExamTestpaperAnswer(ExamTestpaper examTestpaper){
		
		String examTestpaperStr = "";
		int themeIndex = 1;
		examTestpaperStr += "{";
		examTestpaperStr += "examId:'"+examTestpaper.getExamId()+"',";
		examTestpaperStr += "testPaperId:'"+examTestpaper.getExamTestpaperId()+"',";
		List<ExamTestpaperTheme> themelist = examTestpaper.getExamTestpaperThemes();
		examTestpaperStr+="theme:[";
		for(int i=0;i<themelist.size();i++){
			ExamTestpaperTheme examTestpaperTheme = (ExamTestpaperTheme)themelist.get(i);
			List<ExamTestpaperAnswerkey> ansList = examTestpaperTheme.getExamTestpaperAnswerkeies();
			if(ansList!=null && ansList.size()>0 && ("0".equals(examTestpaperTheme.getScoreType()) || "1".equals(examTestpaperTheme.getScoreType()))){
				examTestpaperStr+="answer:[{sort:'"+themeIndex+"',[";
				boolean isHaveRight = false;
				for(int tt=0;tt<ansList.size();tt++){
					ExamTestpaperAnswerkey answerkey = (ExamTestpaperAnswerkey)ansList.get(tt);
					if(answerkey.getIsRight()!=null 
							&& answerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
						examTestpaperStr+="{ansSort:'"+answerkey.getRandomSortnum()+"',value:'"+answerkey.getAnswerkeyValue()+"'},";
						isHaveRight = true;
					}
				}
				if(isHaveRight){
					examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				}else{
					examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-2);
				}
				themeIndex++;
				examTestpaperStr+="]},";
			}
		}
		examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
		examTestpaperStr+="]}";
		return examTestpaperStr;
	}
	
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInThemeAnswer(ExamTestpaperTheme examTestpaperTheme){
		
		String examTestpaperStr = "";
		examTestpaperStr += "{";
		examTestpaperStr += "examId:'"+examTestpaperTheme.getExamId()+"',";
		examTestpaperStr += "testPaperId:'"+examTestpaperTheme.getExamTestpaperThemeId()+"',";
		List<ExamTestpaperAnswerkey> ansList = examTestpaperTheme.getExamTestpaperAnswerkeies();
		if(ansList!=null && ansList.size()>0 && ("0".equals(examTestpaperTheme.getScoreType()) || "1".equals(examTestpaperTheme.getScoreType()))){
				examTestpaperStr+="answer:[";
				boolean isHaveRight = false;
				for(int tt=0;tt<ansList.size();tt++){
					ExamTestpaperAnswerkey answerkey = (ExamTestpaperAnswerkey)ansList.get(tt);
					if(answerkey.getIsRight()!=null && answerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
						examTestpaperStr+="{ansSort:'"+answerkey.getRandomSortnum()+"',value:'"+stringToJson(answerkey.getAnswerkeyValue())+"'},";
						isHaveRight = true;
					}
				}
				if(isHaveRight){
					examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				}
				examTestpaperStr+="],";
		}
		examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
		examTestpaperStr+="}";
		return examTestpaperStr;
	}
	
	
	/**
	 * 生成试题Json数据(不含答案)
	 * @author zhujian
	 * @description
	 * @param examTestpaper
	 * @return
	 * @modified
	 */
	public static String getJsonStrInThemeRightAnswer(ExamTestpaperTheme examTestpaperTheme,List<ExamTestpaperAnswerkey> examTestpaperAnswerkeyList){
		
		String examTestpaperStr = "";
		examTestpaperStr += "{";
		examTestpaperStr += "examId:'"+examTestpaperTheme.getExamId()+"',";
		//examTestpaperStr += "testPaperId:'"+examTestpaperTheme.getExamTestpaper().getExamTestpaperId()+"',";
		List<ExamTestpaperAnswerkey> ansList  = examTestpaperAnswerkeyList;
		if(ansList==null) ansList = examTestpaperTheme.getExamTestpaperAnswerkeies();
		if(ansList!=null && ansList.size()>0 && ("0".equals(examTestpaperTheme.getScoreType()) || "1".equals(examTestpaperTheme.getScoreType()))){
				examTestpaperStr+="answer:[";
				boolean isHaveRight = false;
				for(int tt=0;tt<ansList.size();tt++){
					ExamTestpaperAnswerkey answerkey = (ExamTestpaperAnswerkey)ansList.get(tt);
					if(answerkey.getIsRight()!=null && answerkey.getIsRight().intValue() == 10){//是否正确 5：否,10：是
						examTestpaperStr+="{ansSort:'"+answerkey.getRandomSortnum()+"',value:'"+stringToJson(answerkey.getAnswerkeyValue())+"'},";
						isHaveRight = true;
					}
				}
				if(isHaveRight){
					examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
				}
				examTestpaperStr+="],";
		}
		examTestpaperStr = examTestpaperStr.substring(0,examTestpaperStr.length()-1);
		examTestpaperStr+="}";
		return examTestpaperStr;
	}
}
