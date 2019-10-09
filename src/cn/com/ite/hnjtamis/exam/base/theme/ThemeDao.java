package cn.com.ite.hnjtamis.exam.base.theme;

import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.hibernate.DefaultDAO;
import cn.com.ite.hnjtamis.exam.base.theme.form.XxbItemFrom;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;

/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.testpaper.TestpaperDao</p>
 * <p>Description 试题管理</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午10:10:53
 * @version 1.0
 * 
 * @modified records:
 */
public interface ThemeDao extends DefaultDAO{

	/**
	 * 获取员工的学习币
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public double getEmployeeXxb(String employeeId);
	/**
	 * 获取员工的学习币(详细)
	 * @description
	 * @param employeeId
	 * @return
	 * @modified
	 */
	public List<XxbItemFrom> getEmployeeXxbItemList(String employeeId);
	
	/**
	 * 查询公有的题目校验码
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getGyThemeCrcMap();
	
	/**
	 * 按题库+试题校验码进行校验，一个题库只能存在一个试题
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getThemeBankAndThemeCrcMap(String themeBankId);
	
	/**
	 *
	 * @author 朱健
	 * @return
	 * @modified
	 */
	public Map getThemeCrcMap();
	
	/**
	 * 查询试题列表
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public List<Theme> getThemeList(Map param,int start,int limit);
	
	/**
	 * 查询试题列表的数量
	 * @author 朱健
	 * @param param
	 * @return
	 * @modified
	 */
	public int getThemeCount(Map param);
	
	/**
	 * 检查试题
	 * @description
	 * @modified
	 */
	public int[] saveAndcheckTheme();
	
}
