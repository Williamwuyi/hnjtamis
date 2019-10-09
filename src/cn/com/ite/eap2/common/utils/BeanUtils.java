package cn.com.ite.eap2.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import cn.com.ite.eap2.core.hibernate.ref.HibernateConfigurationHelper;

/**
 * <p>
 * Title cn.com.ite.eap2.common.utils.BeanUtils
 * Description 数据复制
 * Company ITE
 * Copyright Copyright(c)2014
 * 
 * @author 宋文科
 * @create time: 2014-7-8 下午04:05:14
 * @version 2.0
 * 
 * @modified records:
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {
	/**
	 * 数据复制
	 * @param source
	 * @param target
	 * @throws BeansException
	 */
	@SuppressWarnings("unchecked")
	public static void copyProperties(Object target, Object source)
			throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		Class<?> actualEditable = target.getClass();
		PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
		for (PropertyDescriptor targetPd : targetPds) {
			if (targetPd.getWriteMethod() != null) {
				PropertyDescriptor sourcePd = getPropertyDescriptor(source
						.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass()
								.getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object value = readMethod.invoke(source);
						Method writeMethod = targetPd.getWriteMethod();
						// 这里判断以下value是否为空 当然这里也能进行一些特殊要求的处理 例如绑定时格式转换等等
						if (value != null) {							
							if (!Modifier.isPublic(writeMethod
									.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							List classz = new ArrayList();
							classz.add("java.util.Set");
							classz.add("java.util.List");
							if (classz.contains(targetPd.getPropertyType()
									.getName())) {
								if(HibernateConfigurationHelper.getInstance().//级联操作要设置
										judageCascade(source.getClass(), targetPd.getName())||
								    HibernateConfigurationHelper.getInstance().//多对多关联要设置
								        judageManyToMany(source.getClass(), targetPd.getName())){
									Method targetReadMethod = targetPd.getReadMethod();
									((Collection) targetReadMethod.invoke(target)).clear();
									if(((Collection)value).size()>0)
									((Collection) targetReadMethod.invoke(target)).
									          addAll((Collection) value);
								}
							} else
								writeMethod.invoke(target, value);
						}else if(HibernateConfigurationHelper.getInstance().
						        judageManyToOn(source.getClass(), targetPd.getName()))
							writeMethod.invoke(target, value);
					} catch (Throwable ex) {
						throw new FatalBeanException(
								"Could not copy properties from source to target",
								ex);
					}
				}
			}
		}
	}
}