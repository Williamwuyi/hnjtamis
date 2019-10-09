package cn.com.ite.hnjtamis.exam.online.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import cn.com.ite.hnjtamis.common.ExamVariable;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.online.service.IocpService</p>
 * <p>Description 处理IOCP服务</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2016</p>
 * @create time: 2016年11月14日 下午1:28:41
 * @version 1.0
 * 
 * @modified records:
 */
public class IocpService {

    public static String IOCPURL = null;
    
    public final static String SUCC_FLAG = "00001"; 
    
    private static boolean IocpFlag = true;

    public static boolean loadUserExam(HttpServletRequest request,String examid) {
    	boolean isFlag = false;
    	if(IocpFlag){
    		IocpFlag = false;
	    	if(IOCPURL==null){
	    		IOCPURL = ExamVariable.getRequestUrl(request);
	    	}
	    	HttpURLConnection connection = null;
	    	BufferedReader br = null;
	    	
	    	try {
	            URL url = new URL(IOCPURL+"/getuser?examid="+examid); //+"&inticket="+inticket   // 把字符串转换为URL请求地址
	            connection = (HttpURLConnection) url.openConnection();// 打开连接
	            connection.connect();// 连接会话
	            // 获取输入流
	            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	            String line;
	            StringBuilder sb = new StringBuilder();
	            while ((line = br.readLine()) != null) {// 循环读取流
	                sb.append(line);
	            }
	            //System.out.println(sb.toString());
	            String returnValue = sb.toString();
	            if(returnValue!=null && !"".equals(returnValue) && !"null".equals(returnValue) 
	            		&& returnValue.indexOf("\"code\":\"00001\"")!=-1){
	            	isFlag = true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            System.out.println("处理IOCP加载失败!");
	        }finally{
	        	try{
	        		if(br!=null){
	            	   br.close();// 关闭流
	            	}
	        		br = null;
	        	}catch(Exception e){
	        		br = null;
	        	}
	        	if(connection!=null){
	        		connection.disconnect();// 断开连接
	        		connection = null;
	        	}
	        }
	    	IocpFlag = true;
    	}
    	return isFlag;
    }
}
