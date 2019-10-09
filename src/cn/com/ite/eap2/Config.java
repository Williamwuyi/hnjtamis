package cn.com.ite.eap2;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * <p>Title cn.com.ite.eap2.Config</p>
 * <p>Description 系统配置/config.properties管理类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文投入
 * @create time: May 20, 2014 11:12:54 AM
 * @version 2.0
 * 
 * @modified records:
 */
public final class Config {
	/**
	 * 单例，不能实例化
	 * @modified
	 */
	private Config(){}
	/**
	 * 属性对象
	 */
	private static Properties properties=new Properties();
	static{
		try {
			//导入属性文件
			properties.load(Config.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取属性数据，根据键值
	 * @param key 键值
	 * @return 属性值
	 * @modified
	 */
	public static String getPropertyValue(String key){
		return properties.getProperty(key);
	}
	
	/**
	 * 设置属性值
	 * @param key 键值
	 * @param value 属性值
	 * @modified
	 */
	public static void setPropertyValue(String key,String value){
		properties.setProperty(key, value);
	}
	
	/*
	 * 获取所有配置键
	 * @return ����ֵ
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getConfigKeys(){
		List<String> keys = new ArrayList<String>();
		Iterator iterator = properties.keySet().iterator();
		while(iterator.hasNext()){
			keys.add((String)iterator.next());
		}
		return keys;
	}
	
	/**
	 * 获得平台地址
	 * @return 平台地址
	 * @modified
	 */
	public static String getEapPath(){
		return getPropertyValue("eap.base.url");
	}
	
	/**
	 * 保存属性文件！
	 * @modified
	 */
	public static void save(){
		String path=Config.class.getResource("/config.properties").getPath();
		FileOutputStream outputFile = null;
		try{
		   outputFile = new FileOutputStream(path);
		   properties.store(outputFile, "config.properties");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
		   if(outputFile!=null)
			try {
				outputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 返回版本号,每次编译提交时变动一次
	 * @return 返回版本号
	 * @modified
	 */
	public static String version(){
		/*2.0.0  2014年9月通过首次验收测试的版本
		  2.0.1  2014年12月，适应对标系统的完善
		         1、增加OUTLOOK风格的框架界面；
		         2、增加系统的首页地址维护；
		         3、系统菜单高度问题完善；
		         4、窗口缺省全屏打开处理；
		         5、保存时加后台唯一验收时问题处理；
		         6、资源地址唯一改成同一系统内唯一处理；
		         7、数据字典打开报错处理；
		*/
		return "2.0.1";
	}
}