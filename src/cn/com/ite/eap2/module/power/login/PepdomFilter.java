package cn.com.ite.eap2.module.power.login;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.common.utils.HttpUtils;
import cn.com.ite.eap2.common.utils.JsonUtils;
import cn.com.ite.eap2.core.struts2.ServletContent;
import cn.com.ite.eap2.module.power.PopedomUtils;

/**
 * <p>Title cn.com.ite.eap2.module.power.login.PepdomFilter</p>
 * <p>Description 权限过滤器,会话判断，权限判断，应用登录</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-7-16 上午08:52:45
 * @version 2.0
 * 
 * @modified records:
 */
public class PepdomFilter implements Filter{
	//不验证的URL
	private String noAccessUrls="";
	private String loginPage="";
	/**
	 * 存储应用的基地址，平台使用
	 */
	//private static List<String> clientBaseUrls = new ArrayList<String>();

	public void setNoAccessUrls(String noAccessUrls) {
		this.noAccessUrls = noAccessUrls;
	}

	@Override
	public void destroy() {
		
	}
	private void handleError(String servletPath,String error,ServletResponse response) throws IOException{
		if(servletPath.indexOf("export")>0  || servletPath.endsWith(".jsp")){//导出文件时
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("<html><head><META http-equiv=Content-Type content=\"text/html; charset=UTF-8\">" +
					"</head><body><script language='javascript'>alert('"+error+"');location='"+loginPage+"';</script></body></html>").getBytes("UTF-8"));
		}else
		if(servletPath.endsWith(".js")){
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("Ext.Msg.alert('警告', '"+error+"');").getBytes("UTF-8"));
		}else
		if(servletPath.endsWith(".action")){
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("[{success:false,'errors':'"+error+"'}]").getBytes("UTF-8"));
		}else{
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(("<META http-equiv=Content-Type content=\"text/html; charset=UTF-8\">" +
					"<script type=\"text/javascript\" src=\"base/ext/ext-all.js\"></script>" +
					"<script>Ext.Msg.alert('警告', '"+error+"');</script>").getBytes("UTF-8"));
		}
	}
	private boolean existJs(HttpServletRequest request,String js){
		File root = new File(request.getSession().getServletContext().getRealPath(js));
		return root.exists();
	}
    //String requestType = request.getHeader("X-Requested-With");  
	//如果requestType能拿到值，并且值为XMLHttpRequest,表示客户端的请求为异步请求，那自然是ajax请求了，反之如果为null,则是普通的请求 
	@SuppressWarnings("unused")
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String type = req.getParameter("$type");//请求类型
		if(type==null) type="page";
		loginPage = req.getContextPath()+"/index.jsp";
		String eapPath = Config.getPropertyValue("eap.base.url");//平台地址,应用系统中才有效
		//远程调用服务方法
		if(req.getServletPath().startsWith("/invokeRmoteMethod.action")){
			String password = request.getParameter("password");
		}
		//打开文件
		if(req.getServletPath().startsWith("/openFile.action")){
			String path = new String(request.getParameter("path").getBytes("ISO-8859-1"),"UTF-8");
		    String fileName = request.getParameter("fileName");
		    java.io.FileInputStream in = null;
		    File file = null;
		    try{
			    file = new File(req.getSession().getServletContext().getRealPath("/")+path);
			    if(!file.exists()){
			    	res.setContentType("text/html;charset=GBK");
			    	res.getWriter().write("未发现此文件'"+new String(fileName.getBytes("ISO-8859-1"),"UTF-8")
			    	      +"',可能已经删除或移除!");
			    	return;
			    }
			    in = new java.io.FileInputStream(file);
			    res.setContentType("application/x-msdownload");
				res.addHeader("Content-Disposition", "attachment; filename=\""+fileName + "\"");
				byte[] chrBuffer = new byte[1024]; //缓冲 
				int off = 0;
				while ((off = in.read(chrBuffer)) != -1 & in != null) {
					res.getOutputStream().write(chrBuffer,0,off);
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    	if(file!=null) file.delete();
		    	res.setContentType("text/html;charset=GBK");
		    	res.getWriter().write("未发现此文件'"+fileName+"',可能已经删除或移除!");
		    }finally{
			   if(in!=null) in.close();
		    }
			return;
		}
		//为防止跨域访问，应用的后台请求地址会带此参数clientPath，为应用的基地址,表示要中转到应用执行
		//应用action请求通过平台中转
		if(req.getServletPath().startsWith("/requestForward.action")){
			String clientPath= req.getQueryString();
			if(clientPath.indexOf("?")<0)
				clientPath = clientPath.replaceFirst("&", "?");
			try{
			   byte[] bytes = HttpUtils.readContentFromPost(clientPath, req);
			   response.getOutputStream().write(bytes);
			}catch(Exception e){
				if(type.equals("page")){//页面类型请求
					response.setCharacterEncoding("UTF-8");
					response.getOutputStream().write(("<html><head>" +
							"<META http-equiv=Content-Type content=\"text/html; charset=UTF-8\"></head>" +
							"<body><div style=\"height:100%\" align=\"center\" valign=\"middle\">" +
							"<img src='resources/images/pageError.png'></div></body></html>").getBytes("UTF-8"));
				}else{//控制类型请求
					String error = "此请求"+clientPath+"访问不到或超时！";
					response.setCharacterEncoding("UTF-8");
					response.getOutputStream().write(("[{success:false,errors:'"+error+"'}]").getBytes("UTF-8"));
				}
			}		
			return;
		}
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+
	     request.getServerPort()+req.getContextPath();
		UserSession us = (UserSession)req.getSession().getAttribute("USER_SESSION");
		//账号密码参数提交方式登录平台
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String encrypt = request.getParameter("encrypt");
		String uid = request.getParameter("uid");
		String pwd = request.getParameter("pwd");
		if(encrypt==null) encrypt="false";
		String jsessionid = req.getSession().getId();
		if(req.getServletPath().startsWith("/index.jsp")&&eapPath==null&&
				account!=null&&password!=null){
			req.getSession().removeAttribute("USER_SESSION");
			String loginUrl= basePath+"/power/login/loginForLoginAction!login.action;jsessionid="+jsessionid;
			HttpUtils.readContentFromGet(loginUrl, "account="+account+"&password="+password+"&encrypt="+encrypt);
		}else if(req.getServletPath().startsWith("/index.jsp")&&eapPath==null&&
				uid!=null&&pwd!=null){
			req.getSession().removeAttribute("USER_SESSION");
			String loginUrl= basePath+"/power/login/ssoLoginForLoginAction!ssoLogin.action;jsessionid="+jsessionid;
			HttpUtils.readContentFromGet(loginUrl, "uid="+uid+"&pwd="+pwd+"&encrypt="+encrypt);
		}else if(req.getServletPath().startsWith("/ssoWs.jsp")&&eapPath==null){
			if(uid!=null&&pwd!=null){
			  req.getSession().removeAttribute("USER_SESSION"); 
			  String loginUrl= basePath+"/power/login/ssoLoginForLoginAction!ssoLogin.action;jsessionid="+jsessionid;
			  HttpUtils.readContentFromGet(loginUrl, "uid="+uid+"&pwd="+pwd+"&encrypt="+encrypt);
			  us = (UserSession)req.getSession().getAttribute("USER_SESSION");
			  if(us==null){
				  response.setCharacterEncoding("GBK");
				  response.getWriter().println("登录失败，账号密码不正确！");
				  return;
			  }
			}else if(us==null){
				//未找到登录的平台
				response.setCharacterEncoding("GBK");
				response.getWriter().println("未找系统SESSION，请登录！");
				return;
			}
			String clientPath= req.getQueryString();
			clientPath = clientPath.replaceAll("&uid="+uid, "");
			clientPath = clientPath.replaceAll("&pwd="+pwd, "");
			req.setAttribute("jsFilePath", clientPath.replaceFirst("&", "?"));
			String[] match = clientPath.split("[?&]");
        	String jsClass = match[0].substring(7);
        	jsClass = jsClass.substring(jsClass.indexOf("/")+1);
        	jsClass = jsClass.substring(jsClass.indexOf("/")+1);
		    jsClass = jsClass.replace("/",".").substring(0,jsClass.length()-3);
		    req.setAttribute("jsClass", jsClass);		
		}else if(req.getServletPath().startsWith("/testJs.jsp")&&eapPath==null){
			if(account!=null&&password!=null){
				  req.getSession().removeAttribute("USER_SESSION"); 
				  String loginUrl= basePath+"/power/login/loginForLoginAction!login.action;jsessionid="+jsessionid;
				  HttpUtils.readContentFromGet(loginUrl, "account="+account+"&password="+password+"&encrypt="+encrypt);
				  us = (UserSession)req.getSession().getAttribute("USER_SESSION");
				  if(us==null){
					  response.setCharacterEncoding("GBK");
					  response.getWriter().println("登录失败，账号密码不正确！");
					  return;
				  }
				}else if(us==null){
					//未找到登录的平台
					response.setCharacterEncoding("GBK");
					response.getWriter().println("未找系统SESSION，请登录！");
					return;
				}
				String clientPath= req.getQueryString();
				clientPath = clientPath.replaceAll("&account="+account, "");
				clientPath = clientPath.replaceAll("&password="+password, "");
				req.setAttribute("jsFilePath", clientPath.replaceFirst("&", "?"));
				String[] match = clientPath.split("[?&]");
	        	String jsClass = match[0].substring(7);
	        	jsClass = jsClass.substring(jsClass.indexOf("/")+1);
	        	jsClass = jsClass.substring(jsClass.indexOf("/")+1);
			    jsClass = jsClass.replace("/",".").substring(0,jsClass.length()-3);
			    req.setAttribute("jsClass", jsClass);		
		}else if(!req.getServletPath().startsWith("/power/login/")){
			if(account!=null&&password!=null){
				String loginUrl= (eapPath!=null?eapPath:basePath)+"/power/login/clientLoginForLoginAction!clientLogin.action";
				String usJson = HttpUtils.readContentFromGet(loginUrl,
						"account="+account+"&password="+password+"&encrypt="+encrypt);
				if(usJson==null||usJson.equals("null")||usJson.equals("")
				   ||usJson.equals("{}")||usJson.startsWith("[{success:false,\"errors\":")){
					//未找到登录的平台
					response.setCharacterEncoding("GBK");
					response.getWriter().println("账号密码不正确！");
					return;
				}
				UserSession userSession = JsonUtils.fromJson(usJson, UserSession.class);
				req.getSession().setAttribute("USER_SESSION", userSession);
			}else if(us==null&&eapPath!=null){//处理应用登录
				//未找到登录的平台
				response.setCharacterEncoding("GBK");
				response.getWriter().println("未找系统SESSION，请登录！");
				return;
			}
		}
		String url = PopedomUtils.getExUrl(req);//全局请求地址，包括IP端口和应用名
		String servletPath = req.getServletPath();
		//处理应用退出
		if(req.getServletPath().startsWith("/clearClientSession.action")){
			req.getSession().removeAttribute("USER_SESSION");
			return;
		}
		//处理应用登录
		if(req.getServletPath().startsWith("/openClientSession.action")&&eapPath==null){
			String eapSessionId = req.getParameter("eapSessionId");//平台的sessionId
			//获取平台session
			String usJson = HttpUtils.readContentFromGet(eapPath +
					"/power/login/findUSOfEapForLoginAction!findUSOfEap.action","sessionId="+eapSessionId);
			if(usJson==null||usJson.equals("null")||usJson.equals("")||usJson.equals("{}")){
				//未找到登录的平台
				handleError(req.getServletPath(),"未登录平台！",response);
				return;
			}
			UserSession retUs = JsonUtils.fromJson(usJson, UserSession.class);
			req.getSession().setAttribute("USER_SESSION", retUs);
			res.getOutputStream().println("[{success:true,'jsessionId':'"+req.getSession().getId()+"'}]");
			return;
		}
		us = (UserSession)req.getSession().getAttribute("USER_SESSION");
		//只过滤未配置的，并且平台中非基础的JS文件
		if(this.judgeFilter(req.getServletPath())&&!req.getServletPath().startsWith("/base/")
		   &&!req.getServletPath().startsWith("/resourecs/")&&!req.getServletPath().startsWith("/power/login/")
		   &&!req.getServletPath().startsWith("/modules/baseinfo/Dictionary.js")
		   &&!req.getServletPath().startsWith("/baseinfo/dic/findDataForDicFormAction!findData.action")){			
			if(us!=null&&us.isRppeatLogin()){
				handleError(req.getServletPath(),"此账号在其它地方登录，请注销重新登录！",response);
				return;
			}
			//平台中判断会话
			if(us==null){
				String remoteInvodePassword = request.getParameter("remoteInvodePassword");
				String remoteInvode = "remoteInvode."+req.getServletPath();
				String configPawwwod = Config.getPropertyValue(remoteInvode) ;
				if(remoteInvodePassword==null||!remoteInvodePassword.equals(configPawwwod)){
				   handleError(req.getServletPath(),"未登录平台！",response);
				   return;
				}
			}else
			//权限判断
			if(!PopedomUtils.popedomJudage(us, req,eapPath!=null?eapPath:basePath)){
				String param = req.getQueryString();
				param = param.replaceAll("&_dc=\\d+", "").replaceAll("\\\\?_dc=\\d+", "");
				handleError(req.getServletPath(),
						"你没有此("+req.getServletPath()+(!StringUtils.isEmpty(param)?"?":"")+param+")请求的权限!",response);
				return;
			}else{
				//设置模块名称
				String[] pepdomCodeName = PopedomUtils.getUrlResourceCode(us,req);
				if(pepdomCodeName!=null)
				  us.setModuleName(pepdomCodeName[1]);
			}
		}
		if(eapPath==null&&us!=null){//访问平台请求时
			String toUrl = PopedomUtils.urlTo(us.getBasePath(), basePath);//应用系统地址，由于存在匹配符需要进行转化
			//如果是分布式部署，平台地址和应用地址不相等的情况，就是应用和平台发布在不同的包内
			if(!toUrl.equals(basePath)){
				List namespaces = ServletContent.getAllNameSpace();
				if(namespaces!=null){
					boolean isClientReq = true;//是否为客户请求
					if(req.getServletPath().endsWith(".js")&&this.existJs(req, req.getServletPath()))
						isClientReq = false;
					String actionUrl = req.getServletPath().substring(0,req.getServletPath().lastIndexOf("/"));
					if(StringUtils.isNotEmpty(actionUrl)){
						if(req.getServletPath().endsWith(".action")&&namespaces.contains(actionUrl))
							isClientReq = false;
						if(isClientReq){
							//转向应用系统执行
							byte[] bytes = HttpUtils.readContentFromPost(toUrl+req.getServletPath()
									+(req.getQueryString()==null?"":"?")+req.getQueryString(), req);
							response.getOutputStream().write(bytes);
							return;
						}
					}
				}
			}
		}
	    chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.setNoAccessUrls(config.getInitParameter("noAccessUrls"));
	}
	/**
	 * 判断是否要过滤
	 * @param url
	 * @return
	 * @modified
	 */
	private boolean judgeFilter(String url){
		return noAccessUrls.indexOf(url)<0;
	}
	
	public static void main(String[] args){
		String clientReqUrl = "http://localhost:8080/eap/power/dic.jsp?sf=tt";
		@SuppressWarnings("unused")
		String [] ts = clientReqUrl.split("[?&]");
		String clientBaseUrl = clientReqUrl.substring(0,
				clientReqUrl.indexOf("/", clientReqUrl.indexOf("/", 8)+1)+1);
		System.out.println(clientBaseUrl);
	}
}