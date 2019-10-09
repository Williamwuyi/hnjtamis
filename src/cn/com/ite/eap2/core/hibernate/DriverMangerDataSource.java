package cn.com.ite.eap2.core.hibernate;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.common.utils.CharsetSwitchUtil;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * 
 * <p>Title cn.com.ite.eap2.core.hibernate.DriverMangerDataSource</p>
 * <p>Description 数据库源</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 13, 2014 11:25:49 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class DriverMangerDataSource extends DriverManagerDataSource{
	/**
	 * 设置保存密码，并加密码
	 * @param password 明文密码
	 * @modified
	 */
	public void setPassword(String password){
		String encrypt=Config.getPropertyValue("jdbc.encrypt");
		if(encrypt==null||!encrypt.equals(password)){
			password=CharsetSwitchUtil.encodeBase64(password);
			Config.setPropertyValue("jdbc.encrypt", password);
			Config.setPropertyValue("jdbc.password", password);
			Config.save();
		}
        super.setPassword(CharsetSwitchUtil.decodeBase64(password));
    }
}
