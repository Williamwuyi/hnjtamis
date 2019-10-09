package cn.com.ite.hnjtamis.common;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
 
import javax.servlet.http.HttpServletRequest;


public class FileOption {
	

	/**
	 *
	 * @author zhujian
	 * @description 读取文件
	 * @param context
	 * @param packageName
	 * @param fileName
	 * @return
	 * @modified
	 */
	public static String readFile(File objFile){
		String msg="";
		try{
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
	
	
	/**
	 *
	 * @author zhujian
	 * @description 读取文件
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
	

	
	/**
	 * @author zhujian
	 * @description  将文件写入文件的末尾
	 * @param msg
	 * @return
	 * @modified
	 */
	public File saveFile(String filePathName,InputStream is){
		try{
			String infileName = filePathName;
			File file = new File(infileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			OutputStream os=null;
			try{
				os=new FileOutputStream(file);
				byte buffer[]=new byte[1024];
				while((is.read(buffer))!=-1){
					os.write(buffer);
				}
				os.flush();
			}catch(Exception e){
				System.out.println("文件创建到服务器失败！");
				e.printStackTrace();
			}finally{
				try{
					os.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return file;
		}catch(Exception e){
			System.out.println("创建文件失败！");
			e.printStackTrace();
			return null;
		}
		

	}
	
	
	/**
	 * @author zhujian
	 * @description  将文件写入文件的末尾
	 * @param msg
	 * @return
	 * @modified
	 */
	public File saveFileInString(String filePathName,String is){
		try{
			String infileName = filePathName;
			File file = new File(infileName);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			OutputStream os=null;
			byte[] byteValue = is.getBytes();  
			try{
				os=new FileOutputStream(file);
				os.write(byteValue);
				os.flush();
			}catch(Exception e){
				System.out.println("文件创建到服务器失败！");
				e.printStackTrace();
			}finally{
				try{
					os.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			return file;
		}catch(Exception e){
			System.out.println("创建文件失败！");
			e.printStackTrace();
			return null;
		}
		

	}
	
	
	/**
	 * 保存字符串到文件
	 * @author zhujian
	 * @description
	 * @param fileName
	 * @param towrite
	 * @modified
	 */
	public static void saveString(String fileName, String towrite) {		
		saveStringToFile( fileName,  towrite,"UTF-8");
	}
	
	
	/**
	 * 保存字符串到文件，并且可以按格式写入(写入到文件最后)
	 * @author zhujian
	 * @description
	 * @param fileName
	 * @param towrite
	 * @param encoding
	 * @modified
	 */
	public static void saveStringCoverFile(String fileName, String towrite,String encoding) {		
		FileOutputStream fos = null;		
		OutputStreamWriter osw = null;		
		try {			
			File file = new File(fileName);			
			if (!file.exists()) {
				file.createNewFile();
			}		
			fos = new FileOutputStream(file,true);			
			osw = new OutputStreamWriter(fos, encoding);			
			osw.write(towrite);			
			osw.flush();		
		} catch (IOException iex) {		
				iex.printStackTrace();		
		} finally {			
			try {		
				if (null != osw)					
					osw.close();				
				if (null != fos)				
					fos.close();			
			} catch (IOException e) {			
				// TODO Auto-generated catch block			
				e.printStackTrace();		
			}	
		}	
	}
	
	/**
	 * 保存字符串到文件，并且可以按格式写入
	 * @author zhujian
	 * @description
	 * @param fileName
	 * @param towrite
	 * @param encoding
	 * @modified
	 */
	public static boolean saveStringToFile(String fileName, String towrite,String encoding) {
		boolean succ = false;
		FileOutputStream fos = null;		
		OutputStreamWriter osw = null;		
		try {			
			File file = new File(fileName);			
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();			
			fos = new FileOutputStream(file);			
			osw = new OutputStreamWriter(fos, encoding);			
			osw.write(towrite);			
			osw.flush();	
			succ = true;
		} catch (IOException iex) {		
			iex.printStackTrace();		
			succ = false;
		} finally {			
			try {		
				if (null != osw)					
					osw.close();				
				if (null != fos)				
					fos.close();			
			} catch (IOException e) {			
				// TODO Auto-generated catch block			
				e.printStackTrace();
				succ = false;
			}	
		}	
		return succ;
	}
	
	public static String getFileBasePathInRealPath(String realPath,String packageName,String filename){
		String basePath=realPath+System.getProperty("file.separator");
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath;
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName;
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 File file=new File(basePath);//创建路径
		 if(!file.isDirectory()){
			file.mkdirs();
		 }
		 basePath=basePath+System.getProperty("file.separator")+filename;
		// System.out.println(basePath);
		 return basePath ; 
	}

	public static String getFileBasePath(HttpServletRequest request,String packageName,String filename){
		String webContext =  request.getRealPath("");; 
		String basePath=webContext+System.getProperty("file.separator");
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath;
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName;
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 File file=new File(basePath);//创建路径
		 if(!file.isDirectory()){
			file.mkdirs();
		 }
		 basePath=basePath+System.getProperty("file.separator")+filename;
		// System.out.println(basePath);
		 return basePath ; 
	}
	
	public static List<File> getAllFileInPath(String path){
		 String basePath=path; 
		 List<File> list =new ArrayList<File>();
		 try{
			 File file = new File(basePath);  
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
	
	
	public static List<File> getAllFileInPathAndPackage(String realPath,String packageName){
		String basePath=realPath+System.getProperty("file.separator");
		//System.out.println(myWebContext);
		//System.out.println(basePath);
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath+System.getProperty("file.separator");
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName+System.getProperty("file.separator");
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 List<File> list =new ArrayList<File>();
		 try{
			 File file = new File(basePath);  
			 File[] files = file.listFiles(); 
			 if(files!=null && files.length>0){
				 for (int i = 0; i < files.length; i++) {  
					if(!files[i].isDirectory()){  
						list.add(files[i]); 
					}  
				 }
			 }
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return list;
	}
	
	public static List<File> getAllFileInPackage(HttpServletRequest request,String packageName){
		String webContext =  request.getRealPath("");; 
		String basePath=webContext+System.getProperty("file.separator");
		//System.out.println(myWebContext);
		//System.out.println(basePath);
		 if(packageName == null || "".equals(packageName) || "null".equals(packageName)){
			 basePath=basePath+System.getProperty("file.separator");
		 }else{
			 basePath=basePath+System.getProperty("file.separator")+packageName+System.getProperty("file.separator");
		 }
		 
		 basePath.replaceAll("/",System.getProperty("file.separator")+System.getProperty("file.separator"));
		 List<File> list =new ArrayList<File>();
		 try{
			 File file = new File(basePath);  
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
	
	
	public static boolean isExsitsFile(HttpServletRequest request,String packageName,String fileName){
		boolean isFlag = false;
		String webContext =  request.getRealPath(""); 
		String basePath=webContext+System.getProperty("file.separator")+"upload"+System.getProperty("file.separator");
		//System.out.println(myWebContext);
		//System.out.println(basePath);
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
	
	
	public static File getFile(HttpServletRequest request,String packageName,String fileName){
		File file = null;
		String webContext =  request.getRealPath(""); 
		String basePath=webContext+System.getProperty("file.separator");
		//System.out.println(myWebContext);
		//System.out.println(basePath);
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
	 * @author 朱健 
	 * @description
	 * @param request
	 * @return
	 * @modified
	 */
	public static String getFileBasePath(HttpServletRequest request){
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
	 * @author 朱健
	 * @description 文件拷贝
	 * @param sourceFileSrc
	 * @param targetFileSrc
	 * @throws Exception
	 * @modified
	 */
	public static void copyFile(String sourceFileSrc,String targetFileSrc)throws Exception{
		File targetFile=new File(targetFileSrc);
		if (targetFile.exists()) {
			targetFile.delete();
		}
		targetFile.createNewFile();
		File sourceFile=new File(sourceFileSrc);
		FileInputStream input = new FileInputStream(sourceFile); 
        BufferedInputStream inBuff=new BufferedInputStream(input); 
        // 新建文件输出流并对它进行缓冲 
        FileOutputStream output = new FileOutputStream(targetFile); 
        BufferedOutputStream outBuff=new BufferedOutputStream(output); 
         
        // 缓冲数组 
        byte[] b = new byte[1024 * 5]; 
        int len; 
        while ((len =inBuff.read(b)) != -1) { 
            outBuff.write(b, 0, len); 
        } 
        // 刷新此缓冲的输出流 
        outBuff.flush(); 
         
        //关闭流 
        inBuff.close(); 
        outBuff.close(); 
        output.close(); 
        input.close(); 
		
	}
	
	
	/**
	 * @author 朱健
	 * @description 文件拷贝
	 * @param sourceFileSrc
	 * @param targetFileSrc
	 * @throws Exception
	 * @modified
	 */
	public static void copyFile(File sourceFile,File targetFile)throws Exception{
		if (targetFile.exists()) {
			targetFile.delete();
		}
		targetFile.createNewFile();
		FileInputStream input = new FileInputStream(sourceFile); 
        BufferedInputStream inBuff=new BufferedInputStream(input); 
        // 新建文件输出流并对它进行缓冲 
        FileOutputStream output = new FileOutputStream(targetFile); 
        BufferedOutputStream outBuff=new BufferedOutputStream(output); 
         
        // 缓冲数组 
        byte[] b = new byte[1024 * 5]; 
        int len; 
        while ((len =inBuff.read(b)) != -1) { 
            outBuff.write(b, 0, len); 
        } 
        // 刷新此缓冲的输出流 
        outBuff.flush(); 
         
        //关闭流 
        inBuff.close(); 
        outBuff.close(); 
        output.close(); 
        input.close(); 
		
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
