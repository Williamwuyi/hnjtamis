package cn.com.ite.hnjtamis.personal.mainEx;



import java.io.File;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.hnjtamis.common.FileOption;

/**
 * 用户个性化
 * <p>Title cn.com.ite.hnjtamis.personal.mainEx.UserDefineOption</p>
 * <p>Description </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年12月2日 下午12:49:58
 * @version 1.0
 * 
 * @modified records:
 */
public class UserDefineOption{

	private static String MAIN_TREENODE_FILE_PATH = null; //存放模拟试题-试卷
	public static String getTreeNodePath(HttpServletRequest request){
		if(MAIN_TREENODE_FILE_PATH !=null){
			return MAIN_TREENODE_FILE_PATH;
		}
		if(request == null){
			return null;
		}
		if(request!=null){
			MAIN_TREENODE_FILE_PATH = FileOption.getFileBasePath(request,"online"+System.getProperty("file.separator")+"user","");
		}
		creatFilePath(MAIN_TREENODE_FILE_PATH);
		return MAIN_TREENODE_FILE_PATH;
	}
	
	public static void saveTreeNode(HttpServletRequest request,String account,String value){
		String filePath = getTreeNodePath(request)+System.getProperty("file.separator")+"treenode_"+account+".txt";
		FileOption.saveString(filePath, value);
	}
	
	public static String getTreeNode(HttpServletRequest request,String account){
		String filePath = getTreeNodePath(request)+System.getProperty("file.separator")+"treenode_"+account+".txt";
		return FileOption.readFile(filePath);
	}
	
	
	public static void saveNowThemeBank(HttpServletRequest request,String account,String value){
		String filePath = getTreeNodePath(request)+System.getProperty("file.separator")+"opbank_"+account+".txt";
		FileOption.saveString(filePath, value);
	}
	
	public static String getNowThemeBank(HttpServletRequest request,String account){
		String filePath = getTreeNodePath(request)+System.getProperty("file.separator")+"opbank_"+account+".txt";
		return FileOption.readFile(filePath);
	}


	public static void creatFilePath(String path){
		if(path!=null && !"".equals(path) && !"null".equals(path)){
			File file = new File(path);
			if(!file.isDirectory()){
				file.mkdirs();
			 }
		}
	}
}
