package cn.com.ite.eap2.core.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.ite.eap2.core.struts2.CutomConfigurationProvider;

/**
 * <p>Title cn.com.ite.eap2.core.spring.EapContextLoaderListener</p>
 * <p>Description 平台的SPRING环境监听器</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-5-22 时间05:17:08
 * @version 2.0
 * 
 * @modified records:
 */
public class EapContextLoaderListener extends ContextLoaderListener {
	public EapContextLoaderListener(){}

    public EapContextLoaderListener(WebApplicationContext context){
        super(context);
    }

    public void contextInitialized(ServletContextEvent event){
    	ServletContext sc = event.getServletContext();
        initWebApplicationContext(sc);
        CutomConfigurationProvider.setConfigProvidersPathPattern(sc.getInitParameter("configProvidersPathPattern"));
        SpringContextUtil.setApplicationContext(WebApplicationContextUtils.getRequiredWebApplicationContext(sc));
    }

    public void contextDestroyed(ServletContextEvent event){
        super.contextDestroyed(event);
    }
}
