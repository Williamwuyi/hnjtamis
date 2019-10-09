package cn.com.ite.eap2.core.struts2;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.opensymphony.xwork2.config.ConfigurationException;

import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.util.location.LocatableProperties;
/**
 * <p>Title cn.com.ite.eap2.core.struts2.CutomConfigurationProvider</p>
 * <p>Description 解决struts2中自动加载模块的struts的配置文件</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: 2014-5-22 上午11:45:30
 * @version 2.0
 * 
 * @modified records:
 */
public final class CutomConfigurationProvider extends XmlConfigurationProvider{
	private static String configProvidersPathPattern;

	public static void setConfigProvidersPathPattern(
			String configProvidersPathPattern) {
		CutomConfigurationProvider.configProvidersPathPattern = configProvidersPathPattern;
	}

	public CutomConfigurationProvider() {
		Map<String, String> mappings = new HashMap<String, String>();
		mappings.put("-//OpenSymphony Group//XWork 2.1.3//EN","xwork-2.1.3.dtd");
		mappings.put("-//OpenSymphony Group//XWork 2.1//EN", "xwork-2.1.dtd");
		mappings.put("-//OpenSymphony Group//XWork 2.0//EN", "xwork-2.0.dtd");
		mappings.put("-//OpenSymphony Group//XWork 1.1.1//EN","xwork-1.1.1.dtd");
		mappings.put("-//OpenSymphony Group//XWork 1.1//EN", "xwork-1.1.dtd");
		mappings.put("-//OpenSymphony Group//XWork 1.0//EN", "xwork-1.0.dtd");
		mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.0//EN",
						"struts-2.0.dtd");
		mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1//EN",
						"struts-2.1.dtd");
		mappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN",
						"struts-2.1.7.dtd");
		setDtdMappings(mappings);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.config.ContainerProvider#needsReload()
	 */
	@Override
	public boolean needsReload() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.opensymphony.xwork2.config.ContainerProvider#register(com.opensymphony
	 * .xwork2.inject.ContainerBuilder,
	 * com.opensymphony.xwork2.util.location.LocatableProperties)
	 */
	@Override
	public void register(ContainerBuilder containerBuilder,
			LocatableProperties props) throws ConfigurationException {
		super.register(containerBuilder, props);
	}

	/*
	 * (non-Javadoc)
	 * @see com.opensymphony.xwork2.config.PackageProvider#loadPackages()
	 */
	@Override
	public void loadPackages() throws ConfigurationException {
		super.loadPackages();
	}

	@Override
	protected Iterator<URL> getConfigurationUrls(String fileName)
			throws IOException {
		List<URL> urls = new ArrayList<URL>();
		List<Resource> resources = getAllResourcesUrl();
		for (Resource resource : resources) {
			urls.add(resource.getURL());
		}
		return urls.iterator();
	}

	/**
	 * 获取系统中需要搜寻的struts的配置
	 * @return
	 * @throws IOException
	 */
	private List<Resource> getAllResourcesUrl() {
		ResourcePatternResolver resoler = new PathMatchingResourcePatternResolver();
		String[] files = configProvidersPathPattern.split("\\s*[,]\\s*");
		List<Resource> res = new ArrayList<Resource>();
		try {
			for(String file:files){
				Resource[] resources = resoler.getResources(file);
				for(Resource r:resources)
				   res.add(r);
			}
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Resource>();
	}
}