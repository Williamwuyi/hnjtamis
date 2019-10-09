package cn.com.ite.eap2.common.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件拷贝
 * @create time: 2016年4月6日 下午1:49:14
 * @version 1.0
 * 
 * @modified records:
 */
public class FileCopyUtil {

	
	/**
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
	
}
