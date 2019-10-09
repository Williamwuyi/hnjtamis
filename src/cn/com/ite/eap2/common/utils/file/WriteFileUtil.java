package cn.com.ite.eap2.common.utils.file;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * 文件处理方法
 * @create time: 2016年4月6日 下午1:36:34
 * @version 1.0
 * 
 * @modified records:
 */
public class WriteFileUtil {
	

	/**
	 * @author zhujian
	 * @description  将文件写入文件的末尾
	 * @param msg
	 * @return
	 * @modified
	 */
	public File writeFile(String filePathName,InputStream is){
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
	public File writeFileInString(String filePathName,String is){
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
	 * @param fileName
	 * @param towrite
	 * @modified
	 */
	public static void writeString(String fileName, String towrite) {		
		writeStringInFile( fileName,  towrite,"UTF-8");
	}
	
	
	/**
	 * 保存字符串到文件，并且可以按格式写入(写入到文件最后)
	 * @param fileName
	 * @param towrite
	 * @param encoding
	 * @modified
	 */
	public static void writeStringCoverFile(String fileName, String towrite,String encoding) {		
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
	 * @param fileName
	 * @param towrite
	 * @param encoding
	 * @modified
	 */
	public static void writeStringInFile(String fileName, String towrite,String encoding) {		
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

}
