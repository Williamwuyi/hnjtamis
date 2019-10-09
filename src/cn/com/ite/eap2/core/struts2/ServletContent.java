package cn.com.ite.eap2.core.struts2;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import cn.com.ite.eap2.common.utils.StringUtils;
import org.apache.struts2.dispatcher.Dispatcher;  

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.RuntimeConfiguration;
import com.opensymphony.xwork2.config.entities.ActionConfig;

/**
 * <p>Title cn.com.ite.eap2.core.struts2.ServletContent</p>
 * <p>Description servlet环境</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-6-18 下午03:24:22
 * @version 2.0
 * 
 * @modified records:
 */
public class ServletContent {
	private static List<String> namespaces=new ArrayList<String>();
	/**
	 * 说明不能实例化
	 * @modified
	 */
	private ServletContent(){}
	/**
	 * 获得request参数
	 * @return request参数
	 * @modified
	 */
	public static Map<String,Object> getParameter(){
		ActionContext context = ActionContext.getContext();
		if(context!=null)
			return context.getParameters();
		else
			return new HashMap<String,Object>();
		
	}
	/**
	 * 获得application属性
	 * @return 属性
	 * @modified
	 */
	public static Map<String,Object> getApplication(){
		ActionContext context = ActionContext.getContext();
		if(context!=null)
			return context.getApplication();
		else
			return new HashMap<String,Object>();
	}
	/**
	 * 设置application属性
	 * @param key
	 * @param value
	 * @modified
	 */
	public static void setApplication(String key,Object value){
		ActionContext context = ActionContext.getContext();
		if(context!=null)
		context.getApplication().put(key, value);
	}
	/**
	 * 获取request属性
	 * @return 属性
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getRequest(){
		ActionContext context = ActionContext.getContext();
		Map attmap = new HashMap();
		if(context==null)
			return attmap;
		HttpServletRequest request = (HttpServletRequest)context.get(
				org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		if(request!=null){
			java.util.Enumeration enums = request.getAttributeNames();
			while(enums.hasMoreElements()){
				String key = (String)enums.nextElement();
				attmap.put(key, request.getAttribute(key));
			}
		}
		return attmap;
	}
	/**
	 * 设置request属性
	 * @param key
	 * @param value
	 * @modified
	 */
	public static void setRequest(String key,Object value){
		ActionContext context = ActionContext.getContext();
		if(context==null) return;
		HttpServletRequest request = (HttpServletRequest)context.get(
				org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		if(request!=null)
		   request.setAttribute(key, value);
	}
	/**
	 * 返回IP地址
	 * @return
	 */
	public static String getIP(){
		ActionContext context = ActionContext.getContext();
		if(context==null) return "";
		HttpServletRequest request = (HttpServletRequest)context.get(
				org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		String ip = request.getHeader("x-forwarded-for"); 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getHeader("WL-Proxy-Client-IP"); 
	    } 
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
	        ip = request.getRemoteAddr(); 
	    } 
	    return ip;  
	}
	/**
	 * 获得request的ID
	 * @return Session的ID (会话ID)
	 * @modified
	 */
	public static String getSessionId(){
		ActionContext context = ActionContext.getContext();
		if(context==null){
			return null;
		}
		HttpServletRequest request = (HttpServletRequest)context.get(
				org.apache.struts2.StrutsStatics.HTTP_REQUEST);
		if(request==null||request.getSession()==null)
			return null;
		return request.getSession().getId();
	}
	/**
	 * 获得session属性
	 * @return 属性
	 * @modified 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getSession(){
		ActionContext ac = ActionContext.getContext();
		if(ac==null)
			return new HashMap();
		return ac.getSession();
	}
	/**
	 * 设置session属性
	 * @param key
	 * @param value
	 * @modified
	 */
	public static void putSession(String key,Object value){
		ActionContext.getContext().getSession().put(key, value);
	}
	/**
	 * 获取所有的namespace
	 * @return
	 * @modified
	 */
	public static List<String> getAllNameSpace(){
		if(namespaces.size()>0) return namespaces;
		Dispatcher dispatcher = Dispatcher.getInstance();
		if(dispatcher==null) return null;
        ConfigurationManager configurationManager = dispatcher.getConfigurationManager();
        Configuration config = configurationManager.getConfiguration();
        RuntimeConfiguration runtimeConfiguration = config.getRuntimeConfiguration();
        Map<String, Map<String, ActionConfig>> namespacemaps = runtimeConfiguration.getActionConfigs();
        /*Iterator nskeys = namespaces.keySet().iterator();
        Iterator acKeys;
        //遍历namespace
        while (nskeys.hasNext()) {
            Map<String, ActionConfig> actionConfigs = namespaces.get(nskeys.next());

            //获得所有的action配置信息
            acKeys = actionConfigs.keySet().iterator();
            while (acKeys.hasNext()) {
                ActionConfig ac = actionConfigs.get(acKeys.next());
                System.out.println(ac.getName());
            }
        }*/
        namespaces.addAll(namespacemaps.keySet());
        return namespaces;
	}
	/**
	 * 获得异常拦截中的错误信息
	 * @return
	 */
	public static String getError(){
		String message = (String)ActionContext.getContext().getValueStack().findString("exception.message");
		message = StringUtils.replaceBlank(message);
		message = message.replaceAll("\"", "\\\"");
		return message;
	}
}
