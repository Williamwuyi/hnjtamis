package cn.com.ite.hnjtamis.exam.testpaper;


import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.testpaper.form.TestpaperThemeForm;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.ThemeFormService</p>
 * <p>Description 试卷模版生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:06:25
 * @version 1.0
 * 
 * @modified records:
 */
public interface TestpaperService extends DefaultService{
	
	/**
	 * 获取一个顶级部门下所有部门ID
	 * @description
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<String> getAllDeptInDeptId(String deptId);
	
	/**
	 * 根据主键获取试题Map
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public Map<String,Theme> getThemeInIds(String[] ids);
	/**
	 * 根据参数查询考题
	 * @author 朱健
	 * @param term
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeByParam(Map term);

	/**
	 * 查询数据字典
	 * @author zhujian
	 * @description
	 * @param dtType
	 * @return
	 * @modified
	 */
	public List<Dictionary> getDictionaryTypeList(String dtType);
	
	/**
	 *
	 * @author zhujian
	 * @description 根据testpaperId查询试卷的试题
	 * @param testpaperId
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getTestpaperThemeList(String testpaperId);
	
	
	/**
	 *
	 * @author zhujian
	 * @description 获取手工选题
	 * @param selectThemeIds
	 * @param paramMap 过滤条件
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getHandAddThemeList(String selectThemeIds,Map paramMap);
	
	public int getHandAddThemeListCount(String selectThemeIds,Map paramMap);

	/**
	 *
	 * @author zhujian
	 * @description 根据模版生成试题，合成试卷
	 * @param tmplateArr
	 * @param themeBankIds
	 * @param examType
	 * @param choutiType 抽题类型  null 无规定方式  onlyNotUserUse 抽题规则中的题目不能为已经本人用过的题目
	 * @param employeeId 人员ID
	 * @return
	 * @modified
	 */
	public List<TestpaperThemeForm> getThemeInTemplate(String[] tmplateArr,String themeBankIds,
			Integer examType,String choutiType,String employeeId,String relationType);
	
	/**
	 * 同步试卷存在的题库与选择试题的题库一致
	 * @description
	 * @param testpaperId
	 * @param organId
	 * @param organName
	 * @modified
	 */
	public void saveAndSyncThemeBank(String testpaperId,String organId,String organName);
	
}
