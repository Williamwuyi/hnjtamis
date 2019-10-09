package cn.com.ite.hnjtamis.baseinfo.speciality;

import java.io.File;
import java.util.List;

import cn.com.ite.eap2.core.service.DefaultService;
import cn.com.ite.eap2.core.service.TreeNode;
import cn.com.ite.eap2.module.power.login.UserSession;
import cn.com.ite.hnjtamis.baseinfo.domain.Speciality;
/**
 * <p>Title 岗位达标培训信息系统-基础信息模块</p>
 * <p>Description 专业类型service接口定义</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2015</p>
 * @author wangyong
 * @create Mar 24, 2015 9:14:48 AM
 * @version 1.0
 * 
 * @modified records:
 */
public interface SpecialityService extends DefaultService {
	
	/**
	 * 同步岗位对应专业
	 * @description
	 * @modified
	 */
	public void saveSyncJobsUnionSpeciality()throws Exception;
	
	/**
	 *
	 * @author zhujian
	 * @description 专业导入
	 * @param xls
	 * @return
	 * @modified
	 */
	public String importSpeciality(File xls,UserSession usersess)throws Exception;
	
	/**
	 * 专业类型树
	 * @param topTypeId 项级类型ID
	 * @param typeName 类型名称(模糊匹配）
	 * @return 树结点
	 */
	public List<TreeNode> findSpecialitiesTree(String topTypeId,String typeName)throws Exception;
	
	/**
	 * 根据类型查询专业
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public List<Speciality> findSpecialitiesByTypeId(String typeId,String typeName) throws Exception;
	
	
}
