package cn.com.ite.hnjtamis.exam.base.theme;

import java.util.ArrayList;
import java.util.List;

import cn.com.ite.hnjtamis.common.Signature;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;

public class ThemeCheckOption {

	
	
	/**
	 *
	 * @author zhujian
	 * @description 试题检查
	 * @param theme
	 * @return
	 * @modified
	 */
	public static String checkTheme(Theme theme){
		String checkStr = "";
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int index = 1;
		if(theme.getThemeInBanks()==null || theme.getThemeInBanks().size() == 0){
			checkStr = "("+index+")题库为空！\n";
			index++;
		}
		if("5".equals(theme.getThemeType().getThemeType())){
			checkStr += checkOneChoiceQuestion(theme,index);
		}else if("10".equals(theme.getThemeType().getThemeType())){
			checkStr += checkMoreChoiceQuestion(theme,index);
		}else if("25".equals(theme.getThemeType().getThemeType())){
			checkStr += checkTrueOrFalse(theme,index);
		}else if("15".equals(theme.getThemeType().getThemeType())){
			checkStr += checkFillBlank(theme,index);
		}else if("20".equals(theme.getThemeType().getThemeType())){
			checkStr += checkShortAnswer(theme,index);
		}else{
			checkStr += checkShortAnswer(theme,index);
		}
		if("".equals(checkStr)){
			checkStr = null;
		}else if(checkStr.length()>1000){
			checkStr = checkStr.substring(0,995)+"...";
		}
		
		theme.setThemeCrc(getCrcInTheme(theme));
		return checkStr;
	}
	
	public static String getCrcInTheme(Theme theme){
		List list = new ArrayList();
		if(theme.getThemeName()!=null && !"".equals(theme.getThemeName())){
			list.add(theme.getThemeName());
		}
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 if(themeAnswerkey.getAnswerkeyValue()!=null && !"".equals(themeAnswerkey.getAnswerkeyValue())){
				 list.add(themeAnswerkey.getAnswerkeyValue());
			 }
		}
		return Signature.getSignatureCode(list);
	}
	
	private static String checkThemeImg(Theme theme,int index){
		if(theme.getImagesNames()!=null && !"".equals(theme.getImagesNames())){
			if(theme.getImagesSucc() == null){
				return "("+index+")试题涉及图片存在问题！\n";
			}else if(theme.getImagesSucc()!=null && !"".equals(theme.getImagesSucc())
					&& theme.getImagesSucc().indexOf("F")!=-1){
				return "("+index+")试题涉及图片存在问题！\n";
			}else{
				return null;
			}
			
		}else{
			return null;
		}
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 单选
	 * @param theme
	 * @return
	 * @modified
	 */
	private static String checkOneChoiceQuestion(Theme theme,int index){
		String checkStr = "";
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int isRightNum = 0;
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 String answerkeyValue = themeAnswerkey.getAnswerkeyValue();
			 if(answerkeyValue==null || "".equals(answerkeyValue.trim())){
				checkStr+="("+index+")答案值不能为空！\n";
				index++;
			 }
			 int isRight = themeAnswerkey==null || themeAnswerkey.getIsRight()==null ? -1 : themeAnswerkey.getIsRight();//是否正确 5：否,10：是
			 if(isRight==10){
				 isRightNum++;
			 } 
		}
		if(ansList.size() < 2){
			checkStr+="("+index+")单选最少需要有两个选择！\n";
			index++;
		}
		if(isRightNum!=1){
			checkStr+="("+index+")单选只能有一个正确答案！\n";
			index++;
		}
		String checkImgStr = checkThemeImg( theme, index);
		if(checkImgStr != null){
			checkStr+=checkImgStr;
			index++;
		}
		return checkStr;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 多选
	 * @param theme
	 * @return
	 * @modified
	 */
	private static String checkMoreChoiceQuestion(Theme theme,int index){
		String checkStr = "";
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int isRightNum = 0;
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 String answerkeyValue = themeAnswerkey.getAnswerkeyValue();
			 if(answerkeyValue==null || "".equals(answerkeyValue.trim())){
				 checkStr+="("+index+")答案值不能为空！\n";
				 index++;
			 }
			 int isRight = themeAnswerkey==null || themeAnswerkey.getIsRight()==null ? -1 : themeAnswerkey.getIsRight();//是否正确 5：否,10：是
			 if(isRight==10){
				 isRightNum++;
			 } 
		}
		if(ansList.size() < 2){
			checkStr+="("+index+")多选最少需要有两个选择！\n";
			index++;
		}
		if(isRightNum==0){
			checkStr+="("+index+")多选最少有一个正确答案！\n";
			index++;
		}
		String checkImgStr = checkThemeImg( theme, index);
		if(checkImgStr != null){
			checkStr+=checkImgStr;
			index++;
		}
		return checkStr;
	}
	
	
	/**
	 *
	 * @author zhujian
	 * @description 判断
	 * @param theme
	 * @return
	 * @modified
	 */
	private static String checkTrueOrFalse(Theme theme,int index){
		String checkStr = "";
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int isRightNum = 0;
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 String answerkeyValue = themeAnswerkey.getAnswerkeyValue();
			 if(answerkeyValue==null || "".equals(answerkeyValue.trim())){
				 checkStr+="("+index+")答案值不能为空！\n";
				 index++;
			 }
			 int isRight = themeAnswerkey==null || themeAnswerkey.getIsRight()==null ? -1 : themeAnswerkey.getIsRight();//是否正确 5：否,10：是
			 if(isRight==10){
				 isRightNum++;
			 } 
		}
		if(ansList.size() != 2){
			checkStr+="("+index+")判断只能有两个选择！\n";
			index++;
		}
		if(isRightNum!=1){
			checkStr+="("+index+")判断必须且只能有一个正确答案！\n";
			index++;
		}
		String checkImgStr = checkThemeImg( theme, index);
		if(checkImgStr != null){
			checkStr+=checkImgStr;
			index++;
		}
		return checkStr;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 填空
	 * @param theme
	 * @return
	 * @modified
	 */
	private static String checkFillBlank(Theme theme,int index){
		String checkStr = "";
		String themeName = theme.getThemeName();
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int kgNum = ((","+themeName+",").split("\\(\\)")).length-1;
		kgNum+=((","+themeName+",").split("\\（\\）")).length-1;
		int isRightNum = 0;
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 String answerkeyValue = themeAnswerkey.getAnswerkeyValue();
			 if(answerkeyValue==null || "".equals(answerkeyValue.trim())){
				 checkStr+="("+index+")答案值不能为空！\n";
				 index++;
			 }
			 int isRight = themeAnswerkey==null || themeAnswerkey.getIsRight()==null ? -1 : themeAnswerkey.getIsRight();//是否正确 5：否,10：是
			 if(isRight==10){
				 isRightNum++;
			 } 
		}
		if(ansList.size() != kgNum){
			 checkStr+="("+index+")设置的答案数量应该与题目中的“()”或“（）”数量匹配！\n";
			 index++;
		}
		if(isRightNum!=ansList.size()){
			 checkStr+="("+index+")设置的答案应全部选择正确！\n";
			 index++;
		}
		String checkImgStr = checkThemeImg( theme, index);
		if(checkImgStr != null){
			checkStr+=checkImgStr;
			index++;
		}
		return checkStr;
	}
	
	/**
	 *
	 * @author zhujian
	 * @description 简答
	 * @param theme
	 * @return
	 * @modified
	 */
	private static String checkShortAnswer(Theme theme,int index){
		String checkStr = "";
		//类型 5：单选；10：多选 15：填空；20：问答；25：判断；30：视听 35：其他
		int isRightNum = 0;
		List<ThemeAnswerkey>  ansList = theme.getThemeAnswerkeies();
		for(int i=0;i<ansList.size();i++){
			 ThemeAnswerkey themeAnswerkey = ansList.get(i);
			 String answerkeyValue = themeAnswerkey.getAnswerkeyValue();
			 if(answerkeyValue==null || "".equals(answerkeyValue.trim())){
				 checkStr+="("+index+")答案值不能为空！\n";
				 index++;
			 }
			 int isRight = themeAnswerkey==null || themeAnswerkey.getIsRight()==null ? -1 : themeAnswerkey.getIsRight();//是否正确 5：否,10：是
			 if(isRight==10){
				 isRightNum++;
			 } 
		}
		if(ansList.size()>1){
			checkStr+="("+index+")设置的答案最多为1个！\n";
			index++;
		}
		if(isRightNum!=ansList.size()){
			checkStr+="("+index+")设置的答案应全部选择正确！\n";
			index++;
		}
		String checkImgStr = checkThemeImg( theme, index);
		if(checkImgStr != null){
			checkStr+=checkImgStr;
			index++;
		}
		return checkStr;
	}
}
