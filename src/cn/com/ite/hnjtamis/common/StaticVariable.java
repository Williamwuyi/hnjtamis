package cn.com.ite.hnjtamis.common;

import java.util.HashMap;
import java.util.Map;

import cn.com.ite.eap2.common.thread.Run;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.common.StaticVariable</p>
 * <p>Description 通用的常量以及数据字典关键字 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月10日 上午9:22:14
 * @version 1.0
 * 
 * @modified records:
 */
public class StaticVariable { 
	
	/** 公用模块 **/
	public static final String USERSESSION = "USER_SESSION";//用户Session
	
	
	public static final String EXAM_KSLX = "KSLX";//数据字典 - 考试类型
	
	public static final String EXAM_EXAM_PROPERTY = "10";
	
	public static final String EXAM_TYPE_MONI="moni";
	
	//试题分隔符  时间@$@1@$@水分差#*#存损#$#时间@$@2@$@一个采样部位按规定#*#采样器具操作一次或截取一次煤流全断面#$#
	public static final String THEME_SPLIT ="#$#";//试题分割符
	public static final String THEME_ANSSPLIT ="#*#";//答案分隔符
	public static final String THEME_TYPESPLIT ="@$@";//一个题目中各部分（题号，时间，答案）分隔符
	
	public static final String[] numberSort = new String[]{"一","二","三","四","五","六","七","八","九","十"};
	public static final String[] themeAnsSort = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	//培训抽取试题方式初始化 ,格式： {类型ID,专业ID,筛选方式（5-按分数 10-按题数）,数量,总分},注意以','号结束
	public static final String trainImplement_ExamPaper_Init_ThemeType_code = "trainImplement_ExamPaper_Init_ThemeType";//配置到config文件中的，如没有读取下面的值
	public static final String trainImplement_ExamPaper_Init_ThemeType = "{0001,null,10,5,0},{0002,null,10,5,0},";
	
	public static final String defined_password = "888888";//默认密码
	
	
/*******************************************试题导入模版 start *********************************************************************************/
	//单项选择题,单选题,choiceQuestion@@多项选择题,多选题,choiceQuestion@@填空题,填空题,fillBlank@@判断题,判断题,trueOrFalse@@简答题,问答题,shortAnswer
	//@@论述题,论述题,discussAnswer@@计算题,计算题,calTheme@@画图题,画图题,drawTheme
	public static final String[][] xls_theme_tageNames = {{"单项选择题","单选","choiceQuestion"},{"多项选择题","多选","choiceQuestion"},
		 {"填空题","填空","fillBlank"},{"判断题","判断","trueOrFalse"},{"简答题","问答","shortAnswer"}
		 ,{"论述题","论述题","discussAnswer"},{"计算题","计算题","calTheme"},{"画图题","画图题","drawTheme"}};//Excle页签映射
	public static final String xls_theme_choiceQuestion = "choiceQuestion";//选择
	public static final String xls_theme_trueOrFalse = "trueOrFalse";//判断
	public static final String xls_theme_fillBlank = "fillBlank";//填空题
	public static final String xls_theme_shortAnswer = "shortAnswer";//简答题
	public static final String xls_theme_discussAnswer = "discussAnswer";//论述题
	public static final String xls_theme_calTheme = "calTheme";//计算
	public static final String xls_theme_drawTheme = "drawTheme";//绘图
	
	public static final String xls_theme_defined = "defined";//自定义
	public static final String xls_theme_ans = "ans";//答案
	public static final String xls_theme_rightAns = "rightAns";//正确答案
	public static final String xls_theme_field = "field";//字段
	
	public static final String xls_theme_haveImages = "haveImages";//是否存在图片
	public static final String xls_theme_imagesNames = "imagesNames";//存在图片名字
	
	//选择题导入模版
	public static final String[][] xls_theme_ExpTemplate_choiceQuestion = {
				{"模块","themeInBankParentName","-1","defined"},
				{"单元","themeInBankName","-1","defined"},
				{"题目","themeName","-1","field"},
				{"答案A","answerkeyA","-1","ans"},
				{"答案B","answerkeyB","-1","ans"},
				{"答案C","answerkeyC","-1","ans"},
				{"答案D","answerkeyD","-1","ans"},
				{"正确答案","rightAnswerkey","-1","rightAns"},
				{"分值","defaultScore","-1","field"},
				{"岗位级别","postLevel","-1","defined"},
				{"是否公开","isPublic","-1","defined"},
				{"备注","remark","-1","field"},
				{"是否有图片","haveImages","-1","haveImages"},
				{"图片名称","imagesNames","-1","imagesNames"}}; 
	//选择判断题模版
	public static final String[][] xls_theme_ExpTemplate_trueOrFalse = {
		{"模块","themeInBankParentName","-1","defined"},
		{"单元","themeInBankName","-1","defined"},
		{"题目","themeName","-1","field"},
		{"正确答案","rightAnswerkey","-1","rightAns"},
		{"分值","defaultScore","-1","field"},
		{"岗位级别","postLevel","-1","defined"},
		{"是否公开","isPublic","-1","defined"},
		{"备注","remark","-1","field"},
		{"是否有图片","haveImages","-1","haveImages"},
		{"图片名称","imagesNames","-1","imagesNames"}}; 
	
	//选择填空题模版
	public static final String[][] xls_theme_ExpTemplate_fillBlank = {
		{"模块","themeInBankParentName","-1","defined"},
		{"单元","themeInBankName","-1","defined"},
		{"题目","themeName","-1","field"},
		{"正确答案","rightAnswerkey","-1","rightAns"},
		{"分值","defaultScore","-1","field"},
		{"岗位级别","postLevel","-1","defined"},
		{"是否公开","isPublic","-1","defined"},
		{"备注","remark","-1","field"},
		{"是否有图片","haveImages","-1","haveImages"},
		{"图片名称","imagesNames","-1","imagesNames"}}; 
	
	//选择问答题模版
	public static final String[][] xls_theme_ExpTemplate_shortAnswer ={
		{"模块","themeInBankParentName","-1","defined"},
		{"单元","themeInBankName","-1","defined"},
		{"题目","themeName","-1","field"},
		{"正确答案","rightAnswerkey","-1","rightAns"},
		{"分值","defaultScore","-1","field"},
		{"岗位级别","postLevel","-1","defined"},
		{"是否公开","isPublic","-1","defined"},
		{"备注","remark","-1","field"},
		{"是否有图片","haveImages","-1","haveImages"},
		{"图片名称","imagesNames","-1","imagesNames"}}; 
	
/*******************************************试题导入模版   end ********************************************************/

	public static int[] CHECK_SCHEDULE = new int[]{0,0};//检查执行进度
	
/*******************************************首页个性化 ***************************************************/
	public static Map treeNodeMap = new HashMap();
	public static Map nowThemeBankInIndexMap = new HashMap();
	public static Map<String,String> moniCsBanksMap = new HashMap<String,String>();
	public static Map<String,String> banksNameMap = new HashMap<String,String>();
	
	public static Map<String,String> publicIdMap = new HashMap<String,String>();
	public static Map<String,String> examCrIdMap = new HashMap<String,String>();
	
	
	
/*******************************************考试处理线程 ***************************************************/
	
	public final static int maxExamThreadNum = 4;//最大处理的线程数量
	
	public  static Run[] examThreadRunList;//处理的线程
	
	
}
