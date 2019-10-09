package cn.com.ite.eap2.common.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import cn.com.ite.eap2.Config;
import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.DictionaryType;
import cn.com.ite.eap2.domain.organization.Organ;

/**
 * <p>Title cn.com.ite.eap2.common.utils.SystemUtils</p>
 * <p>Description 系统工具</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 宋文科
 * @create time: 2015-2-9 上午11:19:51
 * @version 2.0
 * 
 * @modified records:
 */
public class SystemUtils {
	//数据字典DAO
	private DefaultServiceImpl dtServer = (DefaultServiceImpl)SpringContextUtil.getBean("dicServer");
	/**
	 * 查询数据字典类型及数据或下级类型(支持多编辑查询，以“，”分割）
	 * @param name 名称
	 * @param code 编码
	 * @return
	 * @modified
	 */
	@SuppressWarnings("unchecked")
	public List<DictionaryType> findDictionaryTypeByCodes(String codesTerm){
		if(dtServer!=null){
			Map term = new HashMap();
			term.put("codesTerm", codesTerm.split(","));
			return dtServer.queryData("queryRmoteHql", term, null, DictionaryType.class);			
		}else{
			String url = Config.getEapPath()+"/baseinfo/dic/queryForDicListAction!query.action";
			String param = "codesTerm="+codesTerm+"&remoteInvode=true&remoteInvodePassword=ite_eap_xl1100";
			try {
				String json = HttpUtils.readContentFromGet(url, param);
				Type type = new TypeToken<DictionaryType>(){}.getType();
				DictionaryType dt = JsonUtils.fromJson(json, type);
				return dt.getDictionaryTypes();
			} catch (IOException e) {
				return null;
			}
		}
	}
	/**
	 * 查询机构
	 * @param idOrCode ID或编码
	 * @return 机构
	 * @modified
	 */
	public Organ findOrgan(String idOrCode,boolean ided){
		return null;
	}
	/**
	 * 查询子机构
	 * @param idOrCode ID或编码
	 * @return 子机构数组
	 * @modified
	 */
	public List<Organ> findSubOrgan(String idOrCode,boolean ided){
		return null;
	}
	/**
	 * 查询所有子机构
	 * @param idOrCode ID或编码
	 * @return 子机构数组
	 * @modified
	 */
	public List<Organ> findAllSubOrgan(String idOrCode,boolean ided){
		return null;
	}
	
	 
	
	public static void main(String arg[]) throws Exception{
		String url = "http://localhost:8080/eap/baseinfo/dic/queryForDicListAction!query.action";
		String param = "codesTerm=ORGAN_TYPE,DEPT_TYPE&remoteInvodePassword=ite_eap_xl1100&remoteInvode=true";
		String json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		json = HttpUtils.readContentFromGet(url, param);
		System.out.println(json);
		//Type type = new TypeToken<DictionaryType>(){}.getType();
		//DictionaryType list =  JsonUtils.fromJson(json, type);
		//System.out.println(list.toString());
	}
}