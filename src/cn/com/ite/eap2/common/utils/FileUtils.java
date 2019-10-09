package cn.com.ite.eap2.common.utils;


import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

/**
 * <p>Title cn.com.ite.eap2.common.utils.FileUtils</p>
 * <p>Description 文件工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-9 ����09:08:25
 * @version 2.0
 * 
 * @modified records:
 */
public class FileUtils {
	/**
	 * 获取文件的CRC编码
	 * @param file
	 * @return
	 * @throws Exception
	 * @modified
	 */
	 public static String getFileCRCCode(File file) throws Exception {
	        FileInputStream fileinputstream = new FileInputStream(file);
	        CRC32 crc32 = new CRC32();
	        for (CheckedInputStream checkedinputstream =
	            new CheckedInputStream(fileinputstream, crc32);
	            checkedinputstream.read() != -1;
	            ) {
	        }
	        return Long.toHexString(crc32.getValue());
	    }
	 /**
	  * 过滤子文件
	  * @param filepath 文件目录
	  * @param filterfilename　过滤字符串
	  * @return
	  */
    public List<String> listFile(String filepath,String[] filterfilename) {
        List<String> resultfilename= new ArrayList<String>();
        String filename = "";
        File file = new File(filepath);
        File list[] = file.listFiles();
        for (int i = 0; i < list.length; i++) {
            try {
                if (list[i].isDirectory()) {
                    new FileUtils().listFile(list[i].toString(),filterfilename);
                } else {
                    filename = list[i].getName();
                    String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
                    if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext) || "gif".equalsIgnoreCase(ext)
                                        || "png".equalsIgnoreCase(ext) || "bmp".equalsIgnoreCase(ext)) {
                        boolean filter = false;
                        if(filterfilename!=null)
                        for(int j=0;j<filterfilename.length;j++){
                            if(filename.equalsIgnoreCase(filterfilename[j])){
                                filter = true;
                                break;
                            }
                        }
                        if(!filter){
                           resultfilename.add(filename);
                        }                        
                    }
                }
            } catch (Exception ex) {                 
                ex.printStackTrace();
            }
        }
        return resultfilename;
    }

    public static void main(String[] args) {
        try {
        	System.out.println(File.separator);
            System.out.println(new File(FileUtils.class.getResource("/").toURI()).getPath());
        }
        catch (ArrayIndexOutOfBoundsException ea) {
            ea.printStackTrace();
        } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * @description　读文件内容
     * @param url
     * @return
     * @modified
     */
    public static String readFile(String url){
    	try {
    		File f = new File(url);
    		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
    		BufferedReader reader=new BufferedReader(read);
            String myreadline="";    //����һ��String���͵ı���,����ÿ�ζ�ȡһ��
            while (reader.ready()) {
                myreadline += reader.readLine()+"\n";//��ȡһ��
            }
            reader.close();
            read.close();
            return myreadline;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 属性文件复制，把内容进行转码（native2ascii)
     * @param url
     * @return
     * @modified
     */
    public static File copePropertiesFile(String url){
    	try {
    		File f = new File(url);
    		InputStreamReader read = new InputStreamReader (new FileInputStream(f),"UTF-8");
    		BufferedReader reader=new BufferedReader(read);
    		File demp=File.createTempFile("language", "properties");
    		FileWriter outFile=new FileWriter(demp);
            String myreadline="";    //����һ��String���͵ı���,����ÿ�ζ�ȡһ��
            while (reader.ready()) {
                myreadline += reader.readLine()+"\n";//��ȡһ��
                outFile.write(StringUtils.native2ascii(myreadline));
            }
            outFile.close();
            reader.close();
            read.close();
            return demp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 删除文件
     * @param dfile
     * @return
     * @throws Exception
     * @modified
     */
    public static void delte(File file) throws Exception{
    	if(file.exists()){
	    	if(file.isFile())
	    		file.delete();
	    	else{
	    		deleteChildren(file.listFiles());
	    		file.delete();
	    	}
    	}
    }/**
     * 删除文件
     * @param dfile
     * @return
     * @throws Exception
     * @modified
     */
    public static void delete(String filePath) throws Exception{
    	File classPath = new File(FileUtils.class.getResource("/").toURI());
		File webPath = classPath.getParentFile().getParentFile();
		File bigPath = new File(webPath.getPath()+File.separator+filePath);
    	if(bigPath.exists()){
	    	if(bigPath.isFile())
	    		bigPath.delete();
	    	else{
	    		deleteChildren(bigPath.listFiles());
	    		bigPath.delete();
	    	}
    	}
    }
    /**
     * 递归删除子文件
     * @param files
     * @modified
     */
    private static void deleteChildren(File[] files){
    	if(files!=null)
    	for(int i=0;i<files.length;i++){
    		File file=files[i];
    		deleteChildren(file.listFiles());
    		file.delete();
    	}
    }
    /**
     * 保存文件到指定目录
     * @param file 文件
     * @param savePath 保存目录，以WEB目录为基本目录
     * @param fileName 保存的文件名
     */
    public static void saveFile(File file,String savePath,String fileName) throws Exception{
    	java.io.FileOutputStream fos = null;
		java.io.FileInputStream fis = null;
		try {
			File classPath = new File(FileUtils.class.getResource("/").toURI());
			File webPath = classPath.getParentFile().getParentFile();
			File bigPath = new File(webPath.getPath()+File.separator+savePath);
			if(!bigPath.exists())
				bigPath.mkdirs();
			File outerFilePath = new File(bigPath.getPath()+File.separator+fileName);
			if(!outerFilePath.exists())
				outerFilePath.createNewFile();
			fos = new java.io.FileOutputStream(outerFilePath);
			fis = new java.io.FileInputStream(file);
			byte[] bytes = new byte[1024];
			int off=0;
			while((off=fis.read(bytes))!=-1){
			    fos.write(bytes,0,off);
			    fos.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			fos.close();
			fis.close();
		}
    }
}

