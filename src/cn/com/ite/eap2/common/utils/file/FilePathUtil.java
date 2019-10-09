package cn.com.ite.eap2.common.utils.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
 
import javax.servlet.http.HttpServletRequest;


/**
 * 文件处理方法
 * @create time: 2016年4月6日 下午1:36:34
 * @version 1.0
 * 
 * @modified records:
 */
public class FilePathUtil {
	
	public static String getFileBasePathInRealPath(String realPath,String packageName,String filename){
		String basePath=realPath+System.getProperty("file.separator");
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName;
		 }
		 
		 basePath = basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 File file=new File(basePath);//创建路径
		 if(!file.isDirectory()){
			file.mkdirs();
		 }
		 basePath=basePath+System.getProperty("file.separator")+filename;
		 return basePath ; 
	}

	public static String getFileBasePath(HttpServletRequest request,String packageName,String filename){
		 try{
			 return getFileBasePathInRealPath(request.getRealPath(""),packageName,filename) ; 
		 }catch(Exception e){
			e.printStackTrace();
			return null;
		 }
		 
	} 
	
	/**
	 * 检查指定包是否存在文件
	 * @param request
	 * @param packageName
	 * @param fileName
	 * @return
	 * @modified
	 */
	public static boolean isExsitsFile(HttpServletRequest request,String packageName,String fileName){
		boolean isFlag = false;
		String webContext =  request.getRealPath(""); 
		String basePath=webContext+System.getProperty("file.separator")+"upload"+System.getProperty("file.separator");
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath+System.getProperty("file.separator");
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName+System.getProperty("file.separator");
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 try{
			 File file = new File(basePath);  
			 File[] files = file.listFiles(); 
			 if(files!=null && files.length>0){
				 for (int i = 0; i < files.length; i++) {  
					if(!files[i].isDirectory() && files[i].getName().equals(fileName)){  
						isFlag= true;
						break;
					}  
				 }
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return isFlag;
	}
	
	
	/**
	 * 获取指定文件
	 * @param request
	 * @param packageName
	 * @param fileName
	 * @return
	 * @modified
	 */
	public static File getFile(HttpServletRequest request,String packageName,String fileName){
		File file = null;
		String webContext =  request.getRealPath(""); 
		String basePath=webContext+System.getProperty("file.separator");
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath+System.getProperty("file.separator");
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName+System.getProperty("file.separator");
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 try{
			file=new File(basePath);//创建路径
			if(!file.isDirectory()){
				 file.mkdirs();
			}
			basePath=basePath+System.getProperty("file.separator")+fileName;
			
			file = new File(basePath); 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return file;
	}
	
	/**
	 * @description 获取系统的upload目录
	 * @param request
	 * @return
	 * @modified
	 */
	public static String getUploadPath(HttpServletRequest request){
		String webContext =request.getRealPath("");
		StringTokenizer st = new StringTokenizer(webContext,"\\");
		String basePath="";
        while(st.hasMoreElements()){
             String tmp = (String)st.nextElement();
             basePath+=tmp+"/";
        }
		basePath=basePath.replaceAll("/",System.getProperty("file.separator")
				+System.getProperty("file.separator"))+"upload"+System.getProperty("file.separator");
		File file=new File(basePath);//创建路径
		if(!file.isDirectory()){
			file.mkdirs();
		}
		return basePath;
	}

	/**
	 * 将文件转换为二进制
	 * @param file
	 * @return
	 */
	public static byte[] file2Bytes(File file){
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}   

}
