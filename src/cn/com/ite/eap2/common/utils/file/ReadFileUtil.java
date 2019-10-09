package cn.com.ite.eap2.common.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



/**
 * 文件处理方法
 * @create time: 2016年4月6日 下午1:36:34
 * @version 1.0
 * 
 * @modified records:
 */
public class ReadFileUtil {

	
	/**
	 * 读取一个文件夹内的所有文件
	 * @param path
	 * @return
	 * @modified
	 */
	public static List<File> readFileListInPath(String path){
		 List<File> list =new ArrayList<File>();
		 try{
			 File file = new File(path);  
			 File[] files = file.listFiles(); 
			 if(files!=null && files.length>0){
				 for (int i = 0; i < files.length; i++) {  
					if(!files[i].isDirectory()){  
						list.add(files[i]); 
					}else{
						 File[] childFiles = files[i].listFiles();
						 for (int j = 0; j < childFiles.length; j++) {  
							 if(!childFiles[j].isDirectory()){  
								list.add(childFiles[j]); 
							 }
						}
					}
				 }
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return list;
	}
	
	/**
	 *
	 * 读取文件
	 * @param context
	 * @param packageName
	 * @param fileName
	 * @return
	 * @modified
	 */
	public static String readFile(File file){
		String msg="";
		try{
			if(file.exists()){
				FileInputStream fileReader = new FileInputStream(file);
				InputStreamReader fsr = new InputStreamReader(fileReader,"UTF-8");
				BufferedReader bfreader = new BufferedReader(fsr);
				String line="";
				while ((line = bfreader.readLine()) != null){
					msg+=line;
				}
				bfreader.close();
				fsr.close();
				fileReader.close();
			}
		}catch(Exception e){
			System.out.println("读文件失败!");
			e.printStackTrace();
		}
		return msg;
	}
	
	

	/**
	 * 读取文件 
	 * @param context
	 * @param packageName
	 * @param fileName
	 * @return
	 * @modified
	 */
	public static String readFile(String filePathName){
		String msg="";
		try{
			File objFile = new File(filePathName);
			if(objFile.exists()){
				FileInputStream fileReader = new FileInputStream(objFile);
				InputStreamReader fsr = new InputStreamReader(fileReader,"UTF-8");
				BufferedReader bfreader = new BufferedReader(fsr);
				String line="";
				while ((line = bfreader.readLine()) != null){
					msg+=line;
				}
				bfreader.close();
				fsr.close();
				fileReader.close();
			}
		}catch(Exception e){
			System.out.println("读文件失败!");
			e.printStackTrace();
		}
		return msg;
	}
}
