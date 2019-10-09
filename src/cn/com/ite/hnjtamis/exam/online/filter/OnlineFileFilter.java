package cn.com.ite.hnjtamis.exam.online.filter;



import javax.servlet.Filter;    
import javax.servlet.FilterConfig;    
import javax.servlet.ServletRequest;    
import javax.servlet.ServletResponse;    
import javax.servlet.FilterChain;    
import javax.servlet.ServletException;    
import javax.servlet.http.HttpServletRequest; 

import java.io.IOException;  
    
/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.online.filter.OnlineFileFilter</p>
 * <p>Description 禁止答案等文件的访问</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年4月30日 下午1:52:04
 * @version 1.0
 * 
 * @modified records:
 */
public class OnlineFileFilter implements Filter {    
	
	private static final String LOGIN_PAGE_URI = "../../errorOnline.html";   

	/**   
	   * Initializes the Filter.   
	   */   
	public void init(FilterConfig filterConfig) throws ServletException {    

	}    
	     
	/**   
	   * Standard doFilter object.   
	   */   
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)    
	    throws IOException, ServletException {
		 String requestUri = ((HttpServletRequest)req).getRequestURI(); 
		 String adminAsTxt = ((HttpServletRequest)req).getParameter("adminAsTxt");
		 if(!"true".equals(adminAsTxt) && requestUri.indexOf(".txt")!=-1){
			 ((HttpServletRequest)req).getRequestDispatcher(LOGIN_PAGE_URI).forward(req, res);
		 }else{
			 chain.doFilter(req, res);
		 }
	}    
	     
	public void destroy() {}     
	     
	
  
} 