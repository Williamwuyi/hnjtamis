package cn.com.ite.eap2.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title cn.com.ite.eap2.common.utils.HttpUtils</p>
 * <p>Description HTTP工具类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-16 上午11:47:40
 * @version 2.0
 * 
 * @modified records:
 */
public class HttpUtils {
    /**
	 * POST请求
	 * @param url
	 * @throws IOException
	 * @modified
	 */
    public static String readContentFromGet(String url,String param) throws IOException { 
	       // Post请求的url，与get不同的是不需要带参数 
	       URL postUrl = new URL(url); 
	       // 打开连接 
	       HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection(); 
	       //打开读写属性，默认均为false 
	       connection.setDoOutput(true);                 
	       connection.setDoInput(true); 
	       connection.setRequestMethod("POST");
	       connection.setUseCaches(false); 
	       //connection.setRequestProperty("Accept-Charset", "ISO8859-1");
	       connection.setInstanceFollowRedirects(true); 
	       connection.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
	       //connection.setRequestProperty("contentType", "UTF-8");  
	       connection.connect();
	       DataOutputStream out = new DataOutputStream(connection.getOutputStream());
	       out.write(param.getBytes("UTF-8"));
           // 发送数据到服务器并使用Reader读取返回的数据 
           BufferedReader reader = new BufferedReader(
        		   new InputStreamReader(connection.getInputStream(),"UTF-8"));
           StringBuffer lines=new StringBuffer(); 
           String line;
           while ((line = reader.readLine()) != null) {
        	   lines.append(line);
           } 
           reader.close(); 
           // 断开连接 
           connection.disconnect(); 
           return lines.toString();
    }
    /**
     * POST请求跨域转发
     * @throws IOException
     * @modified
     */
    public static byte[] readContentFromPost(String url,HttpServletRequest req) throws IOException { 
           // Post请求的url，与get不同的是不需要带参数 
           URL postUrl = new URL(url); 
           // 打开连接 
           HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection(); 
           //打开读写属性，默认均为false 
           connection.setDoOutput(true);                 
           connection.setDoInput(true); 
           connection.setRequestMethod("POST");
           connection.setUseCaches(false); 
           connection.setInstanceFollowRedirects(true); 
           String contentType = req.getContentType();
           connection.setRequestProperty("Content-Type",contentType); 
           connection.connect();
           DataOutputStream out = new DataOutputStream(connection.getOutputStream()); 
           
           byte buffer[] = new byte[1024];
           InputStream in = req.getInputStream();
           while (in.read(buffer)!=-1) {
              out.write(buffer);
           }
           out.flush(); 
           out.close();
           
           InputStream reader = connection.getInputStream(); 
           byte[] bytes = new byte[reader.available()];
           reader.read(bytes);
           reader.close(); 
           connection.disconnect();
           return bytes;
    }
    
    public static void main(String[] arg) throws Exception{
    	String url = "http://localhost:8080/eap/baseinfo/dic/listForDicListAction!list.action";
    	System.out.println(HttpUtils.readContentFromGet(url,"nameTerm=基础信息&remoteInvodePassword=ite_eap_xl1100"));
    }
}