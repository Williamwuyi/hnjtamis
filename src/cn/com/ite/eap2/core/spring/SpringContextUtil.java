package cn.com.ite.eap2.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 *<p>Title:com.ite.oxhide.spring.SpringContextUtil</p>
 *<p>Description:spring工具类</p>
 *<p>Copyright: Copyright (c) 2007</p>
 *<p>Company: ITE</p>
 * @author 宋文科
 * @version 1.0
 * @date 2007-6-4
 *
 * @modify 
 * @date
 */
public class SpringContextUtil
{
	/**
     * 应用环境
     */
    private static ApplicationContext context;

    public SpringContextUtil(){
    }
    /**
     * 设置环境
     * @param acx 环境
     */
    public static void setApplicationContext(ApplicationContext acx){
        context = acx;
    }

    /**
     * 获取环境
     * @return 应用环境
     */
    public static ApplicationContext getApplicationContext(){
        return context;
    }

    /**
     * 获得spring对象，根据名称
     * @param name 名称 
     * @return BEAN BEAN
     * @throws BeansException
     */
    public static Object getBean(String name)
        throws BeansException{
        return context.getBean(name);
    }
    /**
     * 根据类型和名称获得spring对象
     * @param name BEAN名称
     * @param requiredType 返回类型
     * @return BEAN对象
     * @throws BeansException
     */
    @SuppressWarnings("unchecked")
	public static Object getBean(String name, Class requiredType)
        throws BeansException{
        return context.getBean(name, requiredType);
    }

    /**
     * 判断spring中是否存在此名称对应对象
     * @param name BEAN名称
     * @return boolean
     */
    public static boolean containsBean(String name){
        return context.containsBean(name);
    }

    /**
     * 判断BEAN是否单例
     * @param name BEAN名称
     * @return boolean
     * @throws NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name)
        throws NoSuchBeanDefinitionException{
        return context.isSingleton(name);
    }
    /**
     * 获取BEAN的类型
     * @param name BEAN名称
     * @return BEAN类型
     * @throws NoSuchBeanDefinitionException
     */
    @SuppressWarnings("unchecked")
	public static Class getType(String name)
        throws NoSuchBeanDefinitionException{
        return context.getType(name);
    }

    /**
     * 获得BEAN的别名
     * @param name BEAN名称
     * @return BEAN别名
     * @throws NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name)
        throws NoSuchBeanDefinitionException{
        return context.getAliases(name);
    }
}
