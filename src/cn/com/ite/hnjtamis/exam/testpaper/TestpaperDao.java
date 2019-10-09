package cn.com.ite.hnjtamis.exam.testpaper;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.domain.baseinfo.Dictionary;
import cn.com.ite.hnjtamis.core.hibernate.DefaultExDAO;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;
import cn.com.ite.hnjtamis.exam.hibernatemap.ThemeAnswerkey;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperDao</p>
 * <p>Description 试卷模版生成</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:10:53
 * @version 1.0
 * 
 * @modified records:
 */
public interface TestpaperDao extends DefaultExDAO{
	
	
	/**
	 * 获取一个顶级部门下所有部门ID
	 * @description
	 * @param deptId
	 * @return
	 * @modified
	 */
	public List<String> getAllDeptInDeptId(String deptId);
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
	 * 删除无用的试题
	 * @author zhujian
	 * @description
	 * @modified
	 */
	public void deleteNullTestpaper();
	
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
	 *
	 * @description
	 * @param selectThemeIds
	 * @param paramMap
	 * @return
	 * @modified
	 */
	public List<Theme> getHandAddThemeList(String selectThemeIds,Map paramMap);
	
	public int getHandAddThemeListCount(String selectThemeIds,Map paramMap);
	
	
	/**
	 * 根据试题Id获取答案
	 * @description
	 * @param themeIds
	 * @return
	 * @modified
	 */
	public Map<String,List<ThemeAnswerkey>> getThemeAnswerkeyByThemeIds(String themeIds);
	
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
