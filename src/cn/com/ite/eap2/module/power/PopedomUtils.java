package cn.com.ite.eap2.module.power;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import cn.com.ite.eap2.module.power.login.UserSession;

/**
 * <p>
 * Title com.ite.oxhide.common.util.PopedomUtils
 * Description 权限工具类
 * Company ITE
 * Copyright Copyright(c)2009
 * 
 * @author songwenke
 * @create time: 2009-7-28 上午10:17:22
 * @version 1.0
 * 
 * @modified records:
 */
public class PopedomUtils {
	/**
	 * 地址转化，把其中localhost、8080、eap转换成实际地址
	 * @param url 未处理的权限地址
	 * @param basePath 实际系统地址
	 * @return
	 * @modified
	 */
	public static String urlTo(String url,String basePath){
		if(url.indexOf("localhost:8080/eap")>=0){
			url = url.replaceAll("http://localhost:8080/eap", basePath);
			url = url.replaceAll("https://localhost:8080/eap", basePath);
		}else
		if(url.indexOf("localhost:8080")>=0){
			url = url.replaceAll("localhost:8080", basePath.substring(basePath.indexOf(":")+3,
					basePath.indexOf("/", basePath.indexOf(":")+3)));
		}else
		if(url.indexOf("//localhost")>=0){
			url = url.replaceAll("//localhost", basePath.substring(basePath.indexOf(":")+1,
					basePath.indexOf("/", basePath.indexOf(":")+3)));
		}
		if(url.endsWith("/"))
			url = url.substring(0,url.length()-1);
		return url;
	}
	/**
	 * 权限判断
	 * @param user 用户信息
	 * @param uri 地址
	 * @param basePath 实际登录时系统地址
	 * @return 是否有权限
	 */
	public static boolean popedomJudage(UserSession user, HttpServletRequest req,String basePath) {
		boolean have = true;
		String url = getExUrl(req);
		if (user != null) {
			boolean find = false;
			for (String ps:user.getNoAuthUrls()) {
				for(String pss:ps.split("\\|")){
					pss = urlTo(pss,basePath);
					if (compareLike(pss, url)) {
						find = true;
						break;
					}
				}
				if(find) break;
			}
			if(find) //判断是否在没有权限地址列表中
				have = false;
		}else have = false;
		return have;
	}
	/**
	 * 在权限找到路径对应的资源标识和名称
	 * @param user 权限
	 * @param uri　路径
	 * @param req　请求
	 * @return 资源标识
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public static String[] getUrlResourceCode(UserSession user,HttpServletRequest req){
		if(req.getRequestURL()==null)
			return null;
		String url = getExUrl(req);
		if (user != null) {
			List popedoms = user.getAuthUrls();
			for (int i = 0; i < popedoms.size(); i++) {
				String ps = (String)popedoms.get(i);
				for(String pss:ps.split("\\|"))
				if (compareLike(pss, url)) {
					return new String[]{user.getAuthoritys().get(i),
							user.getAuthorityNames().get(i)};
				}
			}
		}
		return null;
	}

	/**
	 * 获取全局地址，包括参数
	 * @param url 原地址
	 * @param req 请求
	 * @return 新地址
	 */
	public static String getExUrl(HttpServletRequest req) {
		String url=req.getRequestURL().toString();
		String queryString = req.getQueryString();
		return url+(StringUtils.isEmpty(queryString)?"":((url.indexOf("?")>0?"&":"?")+queryString));
	}
	/**
	 * 获取POST提交的参数
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map getPostParam(HttpServletRequest req) {
		Map posts = new HashMap();
		for (Enumeration params = req.getParameterNames(); params
				.hasMoreElements();) {
			String param = (String) params.nextElement();
			String[] vs = req.getParameterValues(param);
			if(vs.length==1)
			   posts.put(param,vs[0]);
			else
			   posts.put(param,vs);
		}
		return posts;
	}
	/**
	 * 获得相对地址，包括参数
	 * @param req 请求
	 * @param noParameterNames 不需要加的参数
	 * @return
	 * @modified
	 */
	public static String getExPath(HttpServletRequest req,List<String> noParameterNames) {
		String path=req.getServletPath();
		String queryString = req.getQueryString();
		String[] qss = queryString.split("\\?|&");
		boolean start = true;
		for(String qs:qss){
			if(!StringUtils.isEmpty(qs)){
				String[] ps = qs.split("=");
				if(!noParameterNames.contains(ps[0])){
					path += (start?"?":"&")+ps[0]+"="+ps[1];
					start = false;
				}
			}
		}
		return path;
	}
	/**
	 * 把多个连接/符号转换成一个
	 * @param url
	 * @return
	 * @modified
	 */
	public static String formateUrl(String url){
		String regEx="/+"; //表示一个或多个a 
    	Pattern p=Pattern.compile(regEx); 
    	String end=url.startsWith("http://")?url.substring(7):url.substring(8);
    	Matcher m=p.matcher(end); //开始的双/不转换，如http://
    	url=(url.startsWith("http://")?url.substring(0,7):url.substring(0,8))+
    			m.replaceAll("/");
    	return url;
	}

	/**
	 * 两个地址相似比较
	 * @param popedom　权限地址
	 * @param act　实际请求地址
	 * @return
	 * @modified
	 */
	public static boolean compareLike(String popedom, String act) {
    	popedom=formateUrl(popedom);
    	if(StringUtils.isEmpty(act)) return false;
    	//请求地址与参数判断
		String[] paras = popedom.split("\\?|&");
		String[] acts = act.split("\\?|&");
		boolean have = true;
		for (int i = 0; i < paras.length; i++) {
			//判断请求地址
			if (i == 0){
			   //左相似并且没有参数情况下返回真
			   if(paras.length==1&&acts[0].startsWith(paras[0]))
				  return true;
			   else if(!acts[0].equals(paras[0]))//带参数的情况请求地址不相等则返回假
			      return false;
			}
			//判断参数
			else{
				boolean find = false;//标识是否找到
				boolean existPara = false;//标识是否存在参数
				for(int j=0;j<acts.length;j++){
					String ac = acts[j];					
					if(j==0||ac.startsWith("_dc=")||ac.startsWith("random="))
						continue;
					existPara=true;
					if(ac.equals(paras[i])){
						find = true;
						break;
					}
				}
				if(existPara&&!find) return false;
			}
		}
		return have;
	}
	
	/**
	 * 两个地址相等比较
	 * @param popedom　权限地址
	 * @param act　实际请求地址
	 * @return
	 */
	public static boolean compareEq(String popedom, String act) {
		boolean have = compareLike(popedom,act);
		String[] paras = popedom.split("\\?|&");
		String[] acts = act.split("\\?|&");
		int actLenth = acts.length;
		for (String ac:acts) {
			if(ac.startsWith("_dc=")||ac.startsWith("random="))
				actLenth--;
		}
		if(paras.length!=actLenth)
			return false;
		return have;
	}
	
	public static void main(String[] args){
		System.out.println("sfs|sfsf".split("\\|").length);
	}
}