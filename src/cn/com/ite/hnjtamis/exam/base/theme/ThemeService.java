package cn.com.ite.hnjtamis.exam.base.theme;


import java.io.File;
import java.util.List;
import java.util.Map;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.exam.base.theme.form.XxbItemFrom;
import cn.com.ite.hnjtamis.exam.hibernatemap.Theme;


/**
 *
 * <p>Title cn.com.ite.hnjtamis.exam.base.theme.ThemeService</p>
 * <p>Description 试题管理 </p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author 朱健
 * @create time: 2015年3月25日 上午9:50:55
 * @version 1.0
 * 
 * @modified records:
 */
public interface ThemeService extends DefaultService{

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
	 *
	 * @author zhujian
	 * @description 试题导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importTheme(File xls,UserSession usersess,String import_init_type,
			String impPackageName,Map<String,File> imgFileMap,String bankType)throws Exception;
	
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
